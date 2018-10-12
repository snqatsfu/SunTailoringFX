package GUI;

import Data.Item;
import Data.QuickItems;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static GUI.GuiUtils.KEY_COMBO_CTRL_S;
import static GUI.GuiUtils.SETTINGS_DIR_PATH;

public class QuickItemsSettingsController implements Initializable {

    public VBox root;
    public Button saveButton;
    public Button addButton;
    public TableView<Item> quickItemsTable;
    public TableColumn<Item, String> itemNameCol;
    public TableColumn<Item, Double> itemUnitPriceCol;

    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    private String name;
    private QuickItems quickItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(keyEvent -> {
            if (KEY_COMBO_CTRL_S.match(keyEvent)) {
                saveQuickItems();
            }
        });
        saveButton.disableProperty().bind(modified.not());
    }

    public void setQuickItems(String name, QuickItems quickItems) {
        this.name = name;
        this.quickItems = quickItems;
        quickItemsTable.setItems(quickItems.getItems());

        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemNameCol.setOnEditCommit(event -> {
            quickItemsTable.getSelectionModel().getSelectedItem().setName(event.getNewValue());
            modified.setValue(true);
        });

        itemUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        itemUnitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        itemUnitPriceCol.setOnEditCommit(event -> {
            quickItemsTable.getSelectionModel().getSelectedItem().setUnitPrice(event.getNewValue());
            modified.setValue(true);
        });

        quickItemsTable.setOnKeyPressed(event -> {
            final int selectedIndex = quickItemsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < quickItemsTable.getItems().size()) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    quickItems.getItems().remove(selectedIndex);
                    modified.setValue(true);
                }
            }
        });
    }

    public QuickItems getQuickItems() {
        return quickItems;
    }

    public void saveQuickItems() {
        try {
            GuiUtils.createDirectoryIfNecessary(SETTINGS_DIR_PATH);
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(GuiUtils.getQuickItemsDatFile(name)))) {
                quickItems.serialize(os);
            }
            modified.setValue(false);
            System.out.println("Successfully saved Quick " + name);
        } catch (IOException e) {
            GuiUtils.showWarningAlertAndWait("Failed Saving quick items");
        }
    }

    public void addQuickItem(ActionEvent actionEvent) {
        actionEvent.consume();
        quickItems.getItems().add(new Item("Name", 1, 0));
        modified.setValue(true);
    }
}
