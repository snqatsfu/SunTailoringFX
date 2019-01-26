package GUI;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpDialogController implements Initializable {

    public TableView<Shortcut> shortcutTable;
    public TableColumn<Shortcut, String> shortcutKeyCol;
    public TableColumn<Shortcut, String> shortcutDescriptionCol;
    public VBox root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

        shortcutTable.getItems().addAll(Shortcut.all());
        shortcutKeyCol.setCellValueFactory(new PropertyValueFactory<>("key"));
        shortcutDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

}
