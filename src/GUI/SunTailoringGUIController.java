package GUI;

import Data.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static GUI.GuiUtils.*;

public class SunTailoringGUIController implements Initializable {

    @FXML public BorderPane rootPane;

    @FXML public MenuItem quickJacketsSettingsMenuItem;
    @FXML public MenuItem quickPantsSettingsMenuItem;
    @FXML public MenuItem quickShirtsSettingsMenuItem;
    @FXML public MenuItem quickDryCleansSettingsMenuItem;
    @FXML public MenuItem quickOthersSettingsMenuItem;

    @FXML private TextField findInvoiceNumberTextField;
    @FXML private Button newInvoiceButton;

    @FXML private TextField invoiceNumberTextField;
    @FXML private DatePicker invoiceDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private CheckBox paidCheckBox;
    @FXML private CheckBox doneCheckBox;
    @FXML private CheckBox pickedUpCheckBox;

    @FXML public ImageView addressBookImageButton;
    @FXML public TextField customerNameTextField;
    @FXML public TextField customerPhoneTextField;
    @FXML public TextField customerEmailTextField;

    @FXML public Label subtotalLabel;
    @FXML public Label taxLabel;
    @FXML public Label totalLabel;
    @FXML public TextField creditTextField;

    @FXML public Button saveInvoiceButton;

    @FXML public ComboBox<Item> quickJacketComboBox;
    @FXML public ComboBox<Item> quickPantComboBox;
    @FXML public ComboBox<Item> quickShirtComboBox;
    @FXML public ComboBox<Item> quickDryCleanComboBox;
    @FXML public ComboBox<Item> quickOtherComboBox;

    @FXML public TableView<Item> itemsTable;
    @FXML public TableColumn<Item, String> itemsTableNameCol;
    @FXML public TableColumn<Item, Integer> itemsTableQuantityCol;
    @FXML public TableColumn<Item, Double> itemsTableUnitPriceCol;
    @FXML public TableColumn<Item, Double> itemsTablePriceCol;

    private final Invoice activeInvoice;
    private final AddressBook addressBook;

    public SunTailoringGUIController() {
        activeInvoice = Invoice.createEmptyInvoice(generateInvoiceNumber());

        AddressBook addressBook = null;
        if (ADDRESS_BOOK_DAT_FILE.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ADDRESS_BOOK_DAT_FILE))) {
                addressBook = AddressBook.deserialize(ois);
            } catch (Exception ignore) {}
        }

        if (addressBook == null) {
            GuiUtils.showWarningAlertAndWait("Can't find saved address book in Settings. Address book will be empty");
            addressBook = new AddressBook(Collections.emptyList());
        }
        this.addressBook = addressBook;
    }

    public void invoiceNumberEntered() {
        String invoiceNumber = findInvoiceNumberTextField.getText();
        File savedDatFile = new File(SAVE_DIR_PATH + "/" + invoiceNumber + ".dat");
        if (savedDatFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedDatFile))) {
                Invoice deserializedInvoice = Invoice.deserialize(ois);
                activeInvoice.cloneFrom(deserializedInvoice);
                invoiceNumberTextField.setStyle("-fx-control-inner-background: pink");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Invoice " + invoiceNumber + " does not exist!");
        }
    }

    public void invoiceNumberTextFieldClicked() {
//        findInvoiceNumberTextField.setText("");
    }

    public void newInvoiceButtonClicked() {
        String invoiceNumber = generateInvoiceNumber();
        activeInvoice.cloneFrom(Invoice.createEmptyInvoice(invoiceNumber));
        invoiceNumberTextField.setStyle("-fx-control-inner-background: green");
    }

    private String generateInvoiceNumber() {
        final LocalDate now = LocalDate.now();
        final String base = now.format(DateTimeFormatter.ofPattern("yyMMdd"));
        int invoiceNum = 0;
        String invoiceNumber = base + String.format("%03d", invoiceNum);
        // todo: change to search invoice store
        while ((new File(SAVE_DIR_PATH + "/" + invoiceNumber + ".dat")).exists()) {
            invoiceNum++;
            if (invoiceNum > 999) {
                return "InvalidInvoiceNumber";
            }
            invoiceNumber = base + String.format("%03d", invoiceNum);
        }
        return invoiceNumber;
    }

    public void saveActiveInvoice() {
        try {
            GuiUtils.createDirectoryIfNecessary(SAVE_DIR_PATH);

            File outputFile = new File(SAVE_DIR_PATH + "/" + activeInvoice.getInvoiceNumber() + ".dat");
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                activeInvoice.serialize(fos);
            }

            GuiUtils.showInfoAlertAndWait("Saved invoice file " + outputFile.getPath());

            // add customer to address book if necessary
            final CustomerInfo customerInfo = activeInvoice.getCustomerInfo();
            addressBook.add(customerInfo.copy());

        } catch (IOException e) {
            System.err.println("Save invoice failed.");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void quickItemComboBoxOnAction(ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (source == quickJacketComboBox || source == quickPantComboBox || source == quickShirtComboBox
                || source == quickDryCleanComboBox || source == quickOtherComboBox) {
            final Item selectedItem = ((ComboBox<Item>) source).getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                activeInvoice.getItems().add(selectedItem.copy());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceNumberTextField.setStyle("-fx-control-inner-background: green");

        rootPane.setOnKeyPressed(keyEvent -> {
            if (KEY_COMBO_CTRL_S.match(keyEvent)) {
                saveActiveInvoice();
            }
        });

        invoiceNumberTextField.textProperty().bindBidirectional(activeInvoice.invoiceNumberProperty());
        invoiceDatePicker.valueProperty().bindBidirectional(activeInvoice.invoiceDateProperty());
        dueDatePicker.valueProperty().bindBidirectional(activeInvoice.dueDateProperty());

        doneCheckBox.selectedProperty().bindBidirectional(activeInvoice.doneProperty());
        paidCheckBox.selectedProperty().bindBidirectional(activeInvoice.paidProperty());
        pickedUpCheckBox.selectedProperty().bindBidirectional(activeInvoice.pickedUpProperty());

        CustomerInfo customerInfo = activeInvoice.getCustomerInfo();
        customerNameTextField.textProperty().bindBidirectional(customerInfo.nameProperty());
        customerPhoneTextField.textProperty().bindBidirectional(customerInfo.phoneProperty());
        customerEmailTextField.textProperty().bindBidirectional(customerInfo.emailProperty());

        // use a formatter to commit on Enter / loss of focus
        TextFormatter<Number> formatter = new TextFormatter<>(new NumberStringConverter(), 0);
        creditTextField.setTextFormatter(formatter);
        formatter.valueProperty().bindBidirectional(activeInvoice.creditProperty());

        subtotalLabel.textProperty().bindBidirectional(activeInvoice.subtotalProperty(), new CurrencyStringConverter());
        taxLabel.textProperty().bindBidirectional(activeInvoice.taxProperty(), new CurrencyStringConverter());
        totalLabel.textProperty().bindBidirectional(activeInvoice.totalProperty(), new CurrencyStringConverter());

        itemsTable.setItems(activeInvoice.getItems());
        itemsTableNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTableQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemsTableUnitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        itemsTablePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // table edit
        itemsTableNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        itemsTableNameCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setName(event.getNewValue());
        });
        itemsTableQuantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemsTableQuantityCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setQuantity(event.getNewValue());
        });
        itemsTableUnitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        itemsTableUnitPriceCol.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
            selectedItem.setUnitPrice(event.getNewValue());
        });

        itemsTable.setOnKeyPressed(event -> {
            final int selectedIndex = itemsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < activeInvoice.getItems().size()) {
                if (event.getCode().equals(KeyCode.DELETE)) {
                    activeInvoice.getItems().remove(selectedIndex);
                }
            }
        });

        quickJacketComboBox.setItems(loadQuickItems("Jacket").getItems());
        quickPantComboBox.setItems(loadQuickItems("Pant").getItems());
        quickShirtComboBox.setItems(loadQuickItems("Shirt").getItems());
        quickDryCleanComboBox.setItems(loadQuickItems("Dry Clean").getItems());
        quickOtherComboBox.setItems(loadQuickItems("Other").getItems());
    }

    public void showAddressBookDialog(Event event) {
        event.consume();
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddressBookDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final AddressBookDialogController addressBookDialogController = fxmlLoader.getController();
            addressBookDialogController.setAddressBook(addressBook);
            addressBookDialogController.selectedCustomerInfoProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    activeInvoice.getCustomerInfo().setFrom(newValue);
                }
            });
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Address Book");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading address book dialog");
        }
    }

    public void saveAddressBook() {
        try {
            GuiUtils.createDirectoryIfNecessary(SETTINGS_DIR_PATH);
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(ADDRESS_BOOK_DAT_FILE))) {
                addressBook.serialize(os);
            }
            System.out.println("Successfully saved address book " + ADDRESS_BOOK_DAT_FILE.getPath());

        } catch (IOException e) {
            System.err.println("Address book saving failed");
        }
    }

    public void showQuickItemsSettingsDialog(ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        String quickItemsName = "";
        ComboBox<Item> quickItemComboBox = null;
        if (source == quickJacketsSettingsMenuItem) {
            quickItemsName = "Jacket";
            quickItemComboBox = quickJacketComboBox;

        } else if (source == quickPantsSettingsMenuItem) {
            quickItemsName = "Pant";
            quickItemComboBox = quickPantComboBox;

        } else if (source == quickShirtsSettingsMenuItem) {
            quickItemsName = "Shirt";
            quickItemComboBox = quickShirtComboBox;

        } else if (source == quickDryCleansSettingsMenuItem) {
            quickItemsName = "Dry Clean";
            quickItemComboBox = quickDryCleanComboBox;

        } else if (source == quickOthersSettingsMenuItem) {
            quickItemsName = "Other";
            quickItemComboBox = quickOtherComboBox;

        } else {
            assert false;
        }
        QuickItems quickItems = loadQuickItems(quickItemsName);
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QuickItemsSettingsDialog.fxml"));
            final Parent root = fxmlLoader.load();
            final QuickItemsSettingsDialogController quickItemsSettingsDialogController = fxmlLoader.getController();
            quickItemsSettingsDialogController.setQuickItems(quickItemsName, quickItems);

            Stage stage = new Stage();
            final ComboBox<Item> finalQuickItemComboBox = quickItemComboBox;
            stage.setOnCloseRequest(event -> {
                final ObservableList<Item> items = quickItemsSettingsDialogController.getQuickItems().getItems();
                finalQuickItemComboBox.setItems(items);
            });
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Configure Quick " + quickItemsName);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            GuiUtils.showWarningAlertAndWait("Failed loading quick items dialog");
        }
    }

    private QuickItems loadQuickItems(String name) {
        final File datFile = GuiUtils.getQuickItemsDatFile(name);
        QuickItems quickItems = null;
        if (datFile.exists()) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(datFile))) {
                quickItems = QuickItems.deserialize(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (quickItems == null) {
            quickItems = new QuickItems(new ArrayList<>());
        }
        return quickItems;
    }
}
