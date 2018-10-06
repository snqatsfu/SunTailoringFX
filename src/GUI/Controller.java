package GUI;

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

import java.net.URL;
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

    @FXML public TextArea customerInfoTextArea;

    @FXML public Label subtotalLabel;
    @FXML public Label taxLabel;
    @FXML public Label totalLabel;
    @FXML public TextField creditTextField;

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
                "my address",
                items,
                0,
                true, true, false);
    }

    public void invoiceNumberEntered() {
        System.out.println("Entered invoice number: " + findInvoiceNumberTextField.getText());
    }

    public void invoiceNumberTextFieldClicked() {
        findInvoiceNumberTextField.setText("");
    }

    public void newInvoiceButtonClicked() {
        System.out.println("New invoice button clicked");
    }

    public void invoiceDatePickerDateSelected() {
        System.out.println("Invoice date is " + invoiceDatePicker.getValue());
    }

    public void dueDatePickerDateSelected() {
        System.out.println("Invoice date is " + dueDatePicker.getValue());
    }

    public void doneCheckBoxSelected() {
        System.out.println("Done? " + activeInvoice.getDone());
    }

    public void paidCheckBoxSelected() {
        System.out.println("Paid? " + activeInvoice.getPaid());
    }

    public void pickedUpCheckBoxSelected() {
        System.out.println("Picked up? " + activeInvoice.getPickedUp());
    }

    // todo: test function
    public void testButtonOnAction() {
        activeInvoice.setPaid(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialization");

        invoiceNumberTextField.textProperty().bind(activeInvoice.invoiceNumberProperty());
        invoiceDatePicker.valueProperty().bindBidirectional(activeInvoice.invoiceDateProperty());
        dueDatePicker.valueProperty().bindBidirectional(activeInvoice.dueDateProperty());

        doneCheckBox.selectedProperty().bindBidirectional(activeInvoice.doneProperty());
        paidCheckBox.selectedProperty().bindBidirectional(activeInvoice.paidProperty());
        pickedUpCheckBox.selectedProperty().bindBidirectional(activeInvoice.pickedUpProperty());

        customerInfoTextArea.textProperty().bind(activeInvoice.customerInfoProperty());

        creditTextField.textProperty().bindBidirectional(activeInvoice.creditProperty(), new NumberStringConverter());
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
