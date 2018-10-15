package GUI;

import Data.Invoice;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.converter.CurrencyStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class InvoiceStoreDialogController implements Initializable {

    @FXML public VBox root;
    @FXML public TextField searchByCustomerTextField;
    @FXML public TableView<Invoice> invoiceStoreTable;
    @FXML public TableColumn<Invoice, String> invoiceNumberCol;
    @FXML public TableColumn<Invoice, LocalDate> invoiceDateCol;
    @FXML public TableColumn<Invoice, LocalDate> dueDateCol;
    @FXML public TableColumn<Invoice, String> customerInfoCol;
    @FXML public TableColumn<Invoice, String> itemsCol;
    @FXML public TableColumn<Invoice, String> totalCol;
    @FXML public TableColumn<Invoice, Boolean> paidCol;
    @FXML public TableColumn<Invoice, Boolean> doneCol;
    @FXML public TableColumn<Invoice, Boolean> pickedUpCol;


    private InvoiceStore invoiceStore;

    public void setInvoiceStore(InvoiceStore invoiceStore) {
        this.invoiceStore = invoiceStore;
        updateInvoiceStoreTable();
    }

    private void updateInvoiceStoreTable() {
        invoiceStoreTable.setItems(invoiceStore.all());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceNumberCol.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        invoiceDateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        customerInfoCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCustomerInfo().toString()));
        itemsCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getItemsAsString()));
        totalCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new CurrencyStringConverter().toString(cellData.getValue().getTotal())));
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
        paidCol.setCellFactory(column -> new CheckBoxTableCell<>());
        doneCol.setCellValueFactory(new PropertyValueFactory<>("done"));
        doneCol.setCellFactory(column -> new CheckBoxTableCell<>());
        pickedUpCol.setCellValueFactory(new PropertyValueFactory<>("pickedUp"));
        pickedUpCol.setCellFactory(column -> new CheckBoxTableCell<>());
    }
}
