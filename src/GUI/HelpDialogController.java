package GUI;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        shortcutTable.getItems().addAll(Shortcut.all());
        shortcutKeyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        shortcutDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

}
