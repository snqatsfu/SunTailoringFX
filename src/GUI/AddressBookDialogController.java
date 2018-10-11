package GUI;

import Data.AddressBook;
import Data.CustomerInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddressBookDialogController implements Initializable {

    @FXML public VBox root;
    @FXML public TextField searchTextField;
    @FXML public TableView<CustomerInfo> addressBookTable;
    @FXML public TableColumn<CustomerInfo, String> nameCol;
    @FXML public TableColumn<CustomerInfo, String> phoneCol;
    @FXML public TableColumn<CustomerInfo, String> emailCol;

    private AddressBook addressBook;

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
        addressBookTable.setItems(addressBook.getEntries());
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void search(ActionEvent actionEvent) {

    }
}
