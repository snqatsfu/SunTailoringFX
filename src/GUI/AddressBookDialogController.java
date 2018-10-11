package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddressBookDialogController implements Initializable {

    @FXML public VBox root;
    @FXML public TableView addressBookTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
