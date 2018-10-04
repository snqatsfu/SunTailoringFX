import Data.Item;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * TODO: CLASS JAVA DOC HERE
 */
public class ItemTableTest extends Application {

    Stage window;
    Scene scene;
    TableView<Item> itemTableView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Tree View");

        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(400);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Item, Double> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        ObservableList<Item> itemObservableList = FXCollections.observableArrayList(Item.priceExtractor());
        itemObservableList.add(new Item("pant", 1, 10.0));
        itemObservableList.add(new Item("shirt", 2, 15.0));

        itemTableView = new TableView<>();
        itemTableView.setItems(itemObservableList);
        itemTableView.getColumns().addAll(nameCol, quantityCol, unitPriceCol, priceCol);

        VBox layout = new VBox();
        layout.getChildren().addAll(itemTableView);

        scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }
}
