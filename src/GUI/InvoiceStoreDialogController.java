package GUI;

import Data.Invoice;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InvoiceStoreDialogController implements Initializable {

    @FXML
    public VBox root;
    @FXML
    public TextField searchByCustomerTextField;
    @FXML
    public TableView<Invoice> invoiceStoreTable;
    @FXML
    public TableColumn<Invoice, String> invoiceNumberCol;
    @FXML
    public TableColumn<Invoice, LocalDate> invoiceDateCol;
    @FXML
    public TableColumn<Invoice, LocalDate> dueDateCol;
    @FXML
    public TableColumn<Invoice, String> customerInfoCol;
    @FXML
    public TableColumn<Invoice, String> itemsCol;
    @FXML
    public TableColumn<Invoice, String> totalCol;
    @FXML
    public TableColumn<Invoice, Boolean> paidCol;
    @FXML
    public TableColumn<Invoice, Boolean> doneCol;
    @FXML
    public TableColumn<Invoice, Boolean> pickedUpCol;
    @FXML
    public CheckBox notDoneOnlyCheckBox;
    @FXML
    public Label numInvoicesLabel;
    @FXML
    public Label invoicesTotalLabel;

    private InvoiceStore invoiceStore;
    private FilteredList<Invoice> filteredInvoices;
    private ReadOnlyObjectWrapper<Invoice> selectedInvoice;

    public void setInvoiceStore(InvoiceStore invoiceStore) {
        this.invoiceStore = invoiceStore;
        filteredInvoices = new FilteredList<>(invoiceStore.all(), invoice -> true);
        updateInvoiceStoreTable();
    }

    private void updateInvoiceStoreTable() {
        SortedList<Invoice> sortedList = new SortedList<>(filteredInvoices);
        sortedList.comparatorProperty().bind(invoiceStoreTable.comparatorProperty());
        invoiceStoreTable.setItems(sortedList);

        final ObservableList<Invoice> tableItems = invoiceStoreTable.getItems();
        numInvoicesLabel.textProperty().bind(Bindings.size(tableItems).asString());
        invoicesTotalLabel.textProperty().bind(Bindings.createDoubleBinding(() ->
                        tableItems.stream().collect(Collectors.summingDouble(Invoice::getTotal)),
                tableItems).asString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedInvoice = new ReadOnlyObjectWrapper<>();

        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

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

        invoiceStoreTable.setRowFactory(table -> {
            final TableRow<Invoice> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    selectedInvoice.setValue(row.getItem());
                    final Stage stage = (Stage) table.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });

        searchByCustomerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredInvoices.setPredicate(getAllPredicates());
        });

        notDoneOnlyCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredInvoices.setPredicate(getAllPredicates());
        });
    }

    private Predicate<Invoice> getAllPredicates() {
        return getCustomerTextPredicate()
                .and(getNotDoneOnlyPredicate());
    }

    private Predicate<Invoice> getCustomerTextPredicate() {
        String customerInfoSearchText = searchByCustomerTextField.getText();
        return invoice -> customerInfoSearchText == null || customerInfoSearchText.isEmpty() || invoice.getCustomerInfo().containsText(customerInfoSearchText);
    }

    private Predicate<Invoice> getNotDoneOnlyPredicate() {
        boolean notDoneOnly = notDoneOnlyCheckBox.isSelected();
        return invoice -> !notDoneOnly || !invoice.getDone();
    }

    public ReadOnlyObjectWrapper<Invoice> selectedInvoiceProperty() {
        return selectedInvoice;
    }
}
