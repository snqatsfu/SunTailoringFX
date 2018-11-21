package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpDialogController implements Initializable {

    public TableView<Shortcut> shortcutTable;
    public TableColumn<Shortcut, String> shortcutKeyCol;
    public TableColumn<Shortcut, String> shortcutDescriptionCol;

    private static final Shortcut F1 = new Shortcut("F1", "Show Help dialog");
    private static final Shortcut CTRL_N = new Shortcut("Ctrl + N", "Create a new invoice");
    private static final Shortcut CTRL_S = new Shortcut("Ctrl + S", "Save current invoice");
    private static final Shortcut CTRL_F = new Shortcut("Ctrl + F", "Show invoice store (find invoice)");
    private static final Shortcut CTRL_P = new Shortcut("Ctrl + P", "Print current invoice");
    private static final Shortcut CTRL_D = new Shortcut("Ctrl + D", "Duplicate the current item in the table");
    private static final Shortcut CTRL_ENTER = new Shortcut("Ctrl + Enter", "In the mail dialog, send the email and close the dialog");
    private static final ObservableList<Shortcut> shortcuts = FXCollections.observableArrayList(F1, CTRL_N, CTRL_S,
            CTRL_F, CTRL_P, CTRL_D, CTRL_ENTER);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shortcutTable.getItems().addAll(shortcuts);
        shortcutKeyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        shortcutDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

}
