package GUI;

import Data.Expense;
import Data.ExpenseStore;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class ExpenseStoreDialogController implements Initializable {

    public VBox root;

    public DatePicker datePicker;
    public TextField totalTextField;
    public TextField descriptionTextField;

    public TableView<Expense> expensesTable;
    public TableColumn<Expense, LocalDate> dateCol;
    public TableColumn<Expense, String> descriptionCol;
    public TableColumn<Expense, Double> totalCol;

    private ExpenseStore expenseStore;
    private FilteredList<Expense> filteredExpenses;

    public void setExpenseStore(ExpenseStore expenseStore) {
        this.expenseStore = expenseStore;
        rebuildTable(expenseStore);
    }

    private void rebuildTable(ExpenseStore expenseStore) {
        filteredExpenses = new FilteredList<>(expenseStore.all(), expense -> true);
        SortedList<Expense> sortedExpenses = new SortedList<>(filteredExpenses);
        sortedExpenses.comparatorProperty().bind(expensesTable.comparatorProperty());
        expensesTable.setItems(sortedExpenses);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit(event -> {
            Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
            selectedExpense.setDescription(event.getNewValue());
            expenseStore.save(selectedExpense);
        });

        totalCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        totalCol.setOnEditCommit(event -> {
            Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
            selectedExpense.setTotal(event.getNewValue());
            expenseStore.save(selectedExpense);
        });

        expensesTable.setOnKeyPressed(event -> {
            int selectedIndex = expensesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
                if (event.getCode().equals(KeyCode.DELETE)) {
                    if (GuiUtils.showConfirmationAlertAndWait("Are you sure you want to delete the selected expense?")) {
                        expenseStore.delete(selectedExpense);
                        rebuildTable(expenseStore);
                    }
                } else if (Shortcut.CTRL_D.getKeyCombo().match(event)) {
                    expenseStore.duplicateExpenseAndSave(selectedExpense);
                    rebuildTable(expenseStore);
                }
            }
        });
    }

    public void addExpense() {
        LocalDate date = datePicker.getValue();
        String description = descriptionTextField.getText();
        double total;
        try {
            total = Double.parseDouble(totalTextField.getText());
        } catch (NumberFormatException e) {
            GuiUtils.showWarningAlertAndWait("Enter a valid number for total");
            return;
        }
        expenseStore.createExpenseAndSave(date, description, total);
        rebuildTable(expenseStore);
    }
}
