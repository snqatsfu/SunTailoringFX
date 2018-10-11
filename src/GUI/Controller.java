package GUI;

import Data.AddressBook;
import Data.CustomerInfo;
import Data.Invoice;
import Data.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public BorderPane rootPane;
    @FXML private TextField findInvoiceNumberTextField;
    @FXML private Button newInvoiceButton;

    @FXML private TextField invoiceNumberTextField;
    @FXML private DatePicker invoiceDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private CheckBox paidCheckBox;
    @FXML private CheckBox doneCheckBox;
    @FXML private CheckBox pickedUpCheckBox;

    @FXML public Button addressBookButton;
    @FXML public TextField customerNameTextField;
    @FXML public TextField customerPhoneTextField;
    @FXML public TextField customerEmailTextField;

    @FXML public Label subtotalLabel;
    @FXML public Label taxLabel;
    @FXML public Label totalLabel;
    @FXML public TextField creditTextField;

    @FXML public Button saveInvoiceButton;

    @FXML public ComboBox<Item> quickJacketComboBox;

    @FXML public TableView<Item> itemsTable;
    @FXML public TableColumn<Item, String> itemsTableNameCol;
    @FXML public TableColumn<Item, Integer> itemsTableQuantityCol;
    @FXML public TableColumn<Item, Double> itemsTableUnitPriceCol;
    @FXML public TableColumn<Item, Double> itemsTablePriceCol;

    private final Invoice activeInvoice;
    private final AddressBook addressBook;

    public Controller() {
        activeInvoice = Invoice.createEmptyInvoice(generateInvoiceNumber());

        AddressBook addressBook;
        File addressBookDatFile = new File(SETTINGS_DIR_PATH + "/" + "addressBook.dat");
        if (addressBookDatFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(addressBookDatFile))) {
                addressBook = AddressBook.deserialize(ois);
            } catch (Exception e) {
                // todo: pop alert
                addressBook = new AddressBook(Collections.emptyList());
            }
        } else {
            // todo: pop alert and empty addressbook
//            addressBook = new AddressBook(Collections.emptyList());
            List<CustomerInfo> customerInfos = new ArrayList<>();
            customerInfos.add(new CustomerInfo("Nathan Zheng", "604-123-4567", "nathan@gmail.com"));
            customerInfos.add(new CustomerInfo("Emma Liu", "604-454-7890", "emma@gmail.com"));
            addressBook = new AddressBook(customerInfos);
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
            createDirectoryIfNecessary(SAVE_DIR_PATH);

            File outputFile = new File(SAVE_DIR_PATH + "/" + activeInvoice.getInvoiceNumber() + ".dat");
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                activeInvoice.serialize(fos);
            }

            showInfoAlertAndWait("Saved invoice file " + outputFile.getPath());

        } catch (IOException e) {
            System.err.println("Save invoice failed.");
            e.printStackTrace();
        }
    }

    private void showInfoAlertAndWait(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    private static final Path SAVE_DIR_PATH = Paths.get("Save");
    private static final Path SETTINGS_DIR_PATH = Paths.get("Settings");
    private void createDirectoryIfNecessary(Path path) throws IOException {
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
    }

    public void quickJacketComboBoxOnAction(ActionEvent actionEvent) {
        // todo: this only fires when the selection changed
        activeInvoice.getItems().add(quickJacketComboBox.getValue().copy());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceNumberTextField.setStyle("-fx-control-inner-background: green");

        final KeyCombination saveKeyCombo = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        rootPane.setOnKeyPressed(keyEvent -> {
            if (saveKeyCombo.match(keyEvent)) {
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

        // todo: replace with quick items list
        quickJacketComboBox.getItems().add(new Item("Jacket - shorten", 1, 20));
        quickJacketComboBox.getItems().add(new Item("Jacket - lengthen", 1, 10));
        quickJacketComboBox.getSelectionModel().select(0);
    }

    public void showAddressBookDialog(ActionEvent actionEvent) {
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
            e.printStackTrace();
            // todo: display a failed alert
        }

    }
}
