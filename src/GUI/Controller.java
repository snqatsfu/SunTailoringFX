package GUI;

import Data.CustomerInfo;
import Data.Invoice;
import Data.Item;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField findInvoiceNumberTextField;
    @FXML private Button newInvoiceButton;

    @FXML private TextField invoiceNumberTextField;
    @FXML private DatePicker invoiceDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private CheckBox paidCheckBox;
    @FXML private CheckBox doneCheckBox;
    @FXML private CheckBox pickedUpCheckBox;

    @FXML public TextField customerNameTextField;
    @FXML public TextField customerPhoneTextField;
    @FXML public TextField customerEmailTextField;

    @FXML public Label subtotalLabel;
    @FXML public Label taxLabel;
    @FXML public Label totalLabel;
    @FXML public TextField creditTextField;

    @FXML public Button saveInvoiceButton;

    @FXML public TableView<Item> itemsTable;
    @FXML public TableColumn<Item, String> itemsTableNameCol;
    @FXML public TableColumn<Item, Integer> itemsTableQuantityCol;
    @FXML public TableColumn<Item, Double> itemsTableUnitPriceCol;
    @FXML public TableColumn<Item, Double> itemsTablePriceCol;

    @FXML private Button testButton;

    private final Invoice activeInvoice;

    public Controller() {
        Item pant = new Item("pant", 1, 10);
        Item shirt = new Item("shirt", 2, 5);
        List<Item> items = new ArrayList<>();
        items.add(pant);
        items.add(shirt);

        activeInvoice = new Invoice("invoice",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                CustomerInfo.getSAMPLE(),
                items,
                0,
                true, true, false);
    }

    public void invoiceNumberEntered() {
        String invoiceNumber = findInvoiceNumberTextField.getText();
        File savedDatFile = new File(SAVE_DIR_PATH + "/" + invoiceNumber + ".dat");
        if (savedDatFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedDatFile))) {
                Invoice deserializedInvoice = Invoice.deserialize(ois);
                activeInvoice.cloneFrom(deserializedInvoice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Invoice " + invoiceNumber + " does not exist!");
        }
    }

    public void invoiceNumberTextFieldClicked() {
        findInvoiceNumberTextField.setText("");
    }

    public void newInvoiceButtonClicked() {
        System.out.println("New invoice button clicked");
    }

    // todo: test function
    public void testButtonOnAction() {
        activeInvoice.setPaid(false);
    }

    public void saveActiveInvoice() {
        try {
            createSaveDirectoryIfNecessary();

            File outputFile = new File(SAVE_DIR_PATH + "/" + activeInvoice.getInvoiceNumber() + ".dat");
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                activeInvoice.serialize(fos);
            }
            System.out.println("Saved invoice file " + outputFile.getPath());

        } catch (IOException e) {
            System.err.println("Save invoice failed.");
            e.printStackTrace();
        }
    }

    private static final Path SAVE_DIR_PATH = Paths.get("Save");
    private void createSaveDirectoryIfNecessary() throws IOException {
        if (Files.notExists(SAVE_DIR_PATH)) {
            Files.createDirectories(SAVE_DIR_PATH);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialization");

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
    }
}
