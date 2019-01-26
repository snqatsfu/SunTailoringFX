package GUI;

import Data.Expense;
import Data.ExpenseStore;
import Data.Invoice;
import Data.InvoiceStore;
import Utils.Utils;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatsDialogController implements Initializable {

    private static final String TIME_RANGE_12_MONTHS = "Last 12 months";
    private static final String TIME_RANGE_8_WEEKS = "Last 8 weeks";

    public VBox root;
    public LineChart<String, Number> chart;
    public ComboBox<String> timeRangeComboBox;
    public CheckBox showInvoicesCheckBox;
    public CheckBox showExpensesCheckBox;
    public CheckBox showProfitsCheckBox;

    private final Map<LocalDate, List<Invoice>> groupedInvoices = new LinkedHashMap<>();
    private final Map<LocalDate, List<Expense>> groupedExpenses = new LinkedHashMap<>();
    private final XYChart.Series<String, Number> invoiceTotalSeries = new XYChart.Series<>();
    private final XYChart.Series<String, Number> expenseTotalSeries = new XYChart.Series<>();
    private final XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();

    private InvoiceStore invoiceStore;
    private ExpenseStore expenseStore;

    public void setInvoiceStore(InvoiceStore invoiceStore, ExpenseStore expenseStore) {
        this.invoiceStore = invoiceStore;
        this.expenseStore = expenseStore;
        rebuildCharts();
    }

    private void rebuildCharts() {
        // group data
        List<Utils.LocalDateRange> dateRanges = getTimeRange();
        groupInvoices(invoiceStore, dateRanges);
        groupExpenses(expenseStore, dateRanges);

        // put data into series
        invoiceTotalSeries.getData().clear();
        expenseTotalSeries.getData().clear();
        profitSeries.getData().clear();
        for (LocalDate periodStart : groupedInvoices.keySet()) {
            double invoiceTotal = groupedInvoices.get(periodStart).stream().collect(Collectors.summingDouble(Invoice::getTotal));
            double expenseTotal = groupedExpenses.get(periodStart).stream().collect(Collectors.summingDouble(Expense::getTotal));
            double profit = invoiceTotal - expenseTotal;
            invoiceTotalSeries.getData().add(new XYChart.Data<>(periodStart.toString(), invoiceTotal));
            expenseTotalSeries.getData().add(new XYChart.Data<>(periodStart.toString(), expenseTotal));
            profitSeries.getData().add(new XYChart.Data<>(periodStart.toString(), profit));
        }

        // refresh chart
        refreshCharts();
    }

    private void groupExpenses(ExpenseStore expenseStore, List<Utils.LocalDateRange> dateRanges) {
        groupedExpenses.clear();
        dateRanges.forEach(dateRange -> groupedExpenses.put(dateRange.start, new ArrayList<>()));
        expenseStore.all().stream().forEach(expense -> {
            LocalDate date = expense.getDate();
            for (Utils.LocalDateRange localDateRange : dateRanges) {
                if (localDateRange.contains(date)) {
                    groupedExpenses.get(localDateRange.start).add(expense);
                    break;
                }
            }
        });
    }

    private void groupInvoices(InvoiceStore invoiceStore, List<Utils.LocalDateRange> dateRanges) {
        groupedInvoices.clear();
        dateRanges.forEach(dateRange -> groupedInvoices.put(dateRange.start, new ArrayList<>()));
        invoiceStore.all().stream().forEach(invoice -> {
            final LocalDate invoiceDate = invoice.getInvoiceDate();
            for (Utils.LocalDateRange localDateRange : dateRanges) {
                if (localDateRange.contains(invoiceDate)) {
                    groupedInvoices.get(localDateRange.start).add(invoice);
                    break;
                }
            }
        });
    }

    private List<Utils.LocalDateRange> getTimeRange() {
        String selectedItem = timeRangeComboBox.getSelectionModel().getSelectedItem();
        switch (selectedItem) {
            case TIME_RANGE_12_MONTHS:
                return Utils.getLast12MonthsDateRanges(LocalDate.now());
            case TIME_RANGE_8_WEEKS:
                return Utils.getLast8WeeksDateRanges(LocalDate.now());
            default:
                return Utils.getLast12MonthsDateRanges(LocalDate.now());
        }
    }

    private void refreshCharts() {
        chart.getData().clear();

        if (showInvoicesCheckBox.isSelected()) {
            chart.getData().add(invoiceTotalSeries);
            invoiceTotalSeries.getData().stream().forEach(data -> {
                Tooltip tooltip = new Tooltip(data.getXValue() + " - " + new CurrencyStringConverter().toString(data.getYValue()));
                tooltip.setStyle("-fx-font-size: 20");
                Tooltip.install(data.getNode(), tooltip);
            });
        }

        if (showExpensesCheckBox.isSelected()) {
            chart.getData().add(expenseTotalSeries);
            expenseTotalSeries.getData().stream().forEach(data -> {
                Tooltip tooltip = new Tooltip(data.getXValue() + " - " + new CurrencyStringConverter().toString(data.getYValue()));
                tooltip.setStyle("-fx-font-size: 20");
                Tooltip.install(data.getNode(), tooltip);
            });

        }

        if (showProfitsCheckBox.isSelected()) {
            chart.getData().add(profitSeries);
            profitSeries.getData().stream().forEach(data -> {
                Tooltip tooltip = new Tooltip(data.getXValue() + " - " + new CurrencyStringConverter().toString(data.getYValue()));
                tooltip.setStyle("-fx-font-size: 20");
                Tooltip.install(data.getNode(), tooltip);
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                final Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
            }
        });

        timeRangeComboBox.getItems().addAll(TIME_RANGE_12_MONTHS, TIME_RANGE_8_WEEKS);
        timeRangeComboBox.getSelectionModel().select(0);
        invoiceTotalSeries.setName("Invoice Totals");
        expenseTotalSeries.setName("Expense Totals");
        profitSeries.setName("Profit");
    }

    public void timeRangeChanged() {
        rebuildCharts();
    }

    public void seriesSelectionChanged() {
        refreshCharts();
    }
}
