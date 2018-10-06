import Data.Invoice;
import Data.Item;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;
import java.util.ArrayList;


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

        Invoice invoice = createBlankInvoice();
        invoice.getItems().add(new Item("pant", 1, 10.0));
        invoice.getItems().add(new Item("shirt", 2, 15.0));

        itemTableView = new TableView<>();
        itemTableView.setItems(invoice.getItems());
        itemTableView.getColumns().addAll(nameCol, quantityCol, unitPriceCol, priceCol);
        itemTableView.setEditable(true);

        // table edit
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
            selectedItem.setName(event.getNewValue());
        });

        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityCol.setOnEditCommit(event -> {
            Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
            selectedItem.setQuantity(event.getNewValue());
        });
        unitPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        unitPriceCol.setOnEditCommit(event -> {
            Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
            selectedItem.setUnitPrice(event.getNewValue());
        });

        // invoice information
        HBox invoiceNumberRow = new HBox();
        Label label1 = new Label("Invoice Number: ");
        Label label2 = new Label();
        label2.textProperty().bind(invoice.invoiceNumberProperty());
        invoiceNumberRow.getChildren().addAll(label1, label2);

        HBox creditRow = new HBox();
        Label creditLabel = new Label("Credit ");
        TextField creditTextField = new TextField();
        creditTextField.textProperty().bindBidirectional(invoice.creditProperty(), new CurrencyStringConverter());
        creditRow.getChildren().addAll(creditLabel, creditTextField);

        HBox subtotalRow = new HBox();
        Label subtotalLabel = new Label("Subtotal ");
        Label subtotalAmountLabel = new Label();
        subtotalAmountLabel.textProperty().bindBidirectional(invoice.subtotalProperty(), new CurrencyStringConverter());
        subtotalRow.getChildren().addAll(subtotalLabel, subtotalAmountLabel);

        HBox taxRow = new HBox();
        Label taxLabel = new Label("Tax: ");
        Label taxAmountLabel = new Label();
        taxAmountLabel.textProperty().bindBidirectional(invoice.taxProperty(), new CurrencyStringConverter());
        taxRow.getChildren().addAll(taxLabel, taxAmountLabel);

        HBox totalRow = new HBox();
        Label totalLabel = new Label("Total: ");
        Label totalAmountLabel = new Label();
        totalAmountLabel.textProperty().bindBidirectional(invoice.totalProperty(), new CurrencyStringConverter());
        totalRow.getChildren().addAll(totalLabel, totalAmountLabel);

        VBox layout = new VBox();
        layout.getChildren().addAll(invoiceNumberRow, itemTableView, subtotalRow, taxRow, creditRow, totalRow);

        scene = new Scene(layout);
        window.setScene(scene);
        window.sizeToScene();
        window.show();
    }

    private Invoice createBlankInvoice() {
        return new Invoice("myInvoice",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                "Nathan",
                new ArrayList<>(),
                0, false,false,false);
    }
}
