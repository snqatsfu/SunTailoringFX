package GUI;

import Data.AddressBook;
import Data.CustomerInfo;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddressBookDialogController implements Initializable {

    @FXML
    public VBox root;
    @FXML
    public TextField searchTextField;
    @FXML
    public TableView<CustomerInfo> addressBookTable;
    @FXML
    public TableColumn<CustomerInfo, String> nameCol;
    @FXML
    public TableColumn<CustomerInfo, String> phoneCol;
    @FXML
    public TableColumn<CustomerInfo, String> emailCol;

    private FilteredList<CustomerInfo> filteredList;
    private ReadOnlyObjectWrapper<CustomerInfo> selectedCustomerInfo;

    public void setAddressBook(AddressBook addressBook) {
        filteredList = new FilteredList<>(addressBook.getEntries());
        SortedList<CustomerInfo> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(addressBookTable.comparatorProperty());
        addressBookTable.setItems(sortedList);
    }

    public ReadOnlyObjectWrapper<CustomerInfo> selectedCustomerInfoProperty() {
        return selectedCustomerInfo;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedCustomerInfo = new ReadOnlyObjectWrapper<>();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        addressBookTable.setRowFactory(tableView -> {
            TableRow<CustomerInfo> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    final CustomerInfo customerInfo = row.getItem();
                    System.out.println(customerInfo.getName());
                    selectedCustomerInfo.setValue(row.getItem());
                    final Stage stage = (Stage) addressBookTable.getScene().getWindow();
                    stage.close();
                }
            });
            return row;
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customerInfo -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                final String searchText = newValue.toLowerCase();
                return customerInfo.getName().toLowerCase().contains(searchText) ||
                        customerInfo.getPhone().toLowerCase().contains(searchText) ||
                        customerInfo.getEmail().toLowerCase().contains(searchText);
            });
        });
    }
}
