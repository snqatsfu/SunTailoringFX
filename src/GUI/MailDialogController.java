package GUI;

import Data.AddressBook;
import Utils.GmailSender;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static GUI.GuiUtils.KEY_COMBO_CTRL_ENTER;

public class MailDialogController implements Initializable {

    public VBox root;
    public Button toButton;
    public Button ccButton;
    public TextField toTextField;
    public TextField ccTextField;
    public TextField subjectTextField;
    public TextField attachmentTextField;
    public HTMLEditor bodyHtmlEditor;
    public Button sendButton;
    public Button attachButton;

    private AddressBook addressBook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(keyEvent -> {
            if (KEY_COMBO_CTRL_ENTER.match(keyEvent)) {
                send();
            }
        });
    }

    public void send() {
        final String attachmentFileAbsolutePath = getAttachmentFileAbsolutePath();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                root.getScene().setCursor(Cursor.WAIT);
                // todo: add email address validation
                GmailSender.DEFAULT.sendMail(toTextField.getText().trim(),
                        parseCCField(),
                        subjectTextField.getText(),
                        bodyHtmlEditor.getHtmlText(),
                        attachmentFileAbsolutePath);
                System.out.println("Mail send success");
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            root.getScene().setCursor(Cursor.DEFAULT);
            ((Stage) root.getScene().getWindow()).close();
        });
        task.setOnFailed(e -> {
            final Throwable exception = task.getException();
            if (exception instanceof FileNotFoundException) {
                GuiUtils.showWarningAlertAndWait("Attachment file not found " + attachmentFileAbsolutePath);
            } else {
                GuiUtils.showWarningAlertAndWait("Failed sending email");
            }
            root.getScene().setCursor(Cursor.DEFAULT);
        });
        final Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public void setTo(String to) {
        toTextField.setText(to);
    }

    public void setCC(String cc) {
        final String old = ccTextField.getText().trim();
        if (old.isEmpty()) {
            ccTextField.setText(cc);
        } else {
            ccTextField.setText(old + "; " + cc);
        }
    }

    public void setSubject(String subject) {
        subjectTextField.setText(subject);
    }

    public void setBodyHtml(String html) {
        bodyHtmlEditor.setHtmlText(html);
    }

    public List<String> parseCCField() {
        final String[] split = ccTextField.getText().split(";");
        List<String> ccEmails = new ArrayList<>();
        for (String cc : split) {
            if (!cc.trim().isEmpty()) {
                ccEmails.add(cc.trim());
            }
        }
        return ccEmails;
    }

    private String getAttachmentFileAbsolutePath() {
        return attachmentTextField.getText().isEmpty() ? null : attachmentTextField.getText();
    }

    public void selectAttachment() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(GuiUtils.SAVE_DIR_PATH + "/"));
        final File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            attachmentTextField.setText(file.getAbsolutePath());
        }
    }

    public void showAddressBookDialog(ActionEvent event) {
        if (event.getSource() == toButton || event.getSource() == ccButton) {
            try {
                final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddressBookDialog.fxml"));
                final Parent root = fxmlLoader.load();
                final AddressBookDialogController addressBookDialogController = fxmlLoader.getController();
                addressBookDialogController.setAddressBook(addressBook);
                addressBookDialogController.selectedCustomerInfoProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.getEmail().isEmpty()) {
                        if (event.getSource() == toButton) {
                            setTo(newValue.getEmail());
                        } else {
                            setCC(newValue.getEmail());
                        }
                    }
                });
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Address Book");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                GuiUtils.showWarningAlertAndWait("Failed loading address book dialog");
            }
        }
    }
}
