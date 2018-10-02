package GUI;

import Data.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private TextField invoiceNumberTextField;
    @FXML private Button newInvoiceButton;
    @FXML private DatePicker invoiceDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private CheckBox paidCheckBox;
    @FXML private CheckBox doneCheckBox;
    @FXML private CheckBox pickedUpCheckBox;

    @FXML private Button testButton;

    private final Invoice activeInvoice;

    public Controller() {
        activeInvoice = new Invoice("invoice",
                Calendar.getInstance().getTimeInMillis(),
                Calendar.getInstance().getTimeInMillis(),
                "my address",
                new ArrayList<>(),
                0,
                true, true, false);
    }

    public void invoiceNumberEntered() {
        System.out.println("Entered invoice number: " + invoiceNumberTextField.getText());
    }

    public void invoiceNumberTextFieldClicked() {
        invoiceNumberTextField.setText("");
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
        doneCheckBox.selectedProperty().bindBidirectional(activeInvoice.doneProperty());
        paidCheckBox.selectedProperty().bindBidirectional(activeInvoice.paidProperty());
        pickedUpCheckBox.selectedProperty().bindBidirectional(activeInvoice.pickedUpProperty());
    }
}
