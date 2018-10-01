package GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField invoiceNumberTextField;

    public void invoiceNumberEntered() {
        System.out.println("Entered invoice number: " + invoiceNumberTextField.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Controller initialization");
    }
}
