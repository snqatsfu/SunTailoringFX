package GUI;

import Data.Expense;
import Data.ExpenseStore;
import Data.Invoice;
import Data.InvoiceStore;
import Utils.Utils;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatsDialogController implements Initializable {

    public VBox root;
    public LineChart<String, Number> totalLineChart;
    public LineChart<String, Number> numInvoicesLineChart;

    public void setInvoiceStore(InvoiceStore invoiceStore, ExpenseStore expenseStore) {
        XYChart.Series<String, Number> invoiceTotalSeries = new XYChart.Series<>();
        invoiceTotalSeries.setName("Invoice Totals");

        XYChart.Series<String, Number> expenseTotalSeries = new XYChart.Series<>();
        expenseTotalSeries.setName("Expense Totals");

        XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
        profitSeries.setName("Profit");

        XYChart.Series<String, Number> numInvoicesSeries = new XYChart.Series<>();
        numInvoicesSeries.setName("# invoices");

        Map<LocalDate, List<Invoice>> groupedInvoices = new LinkedHashMap<>();
        Map<LocalDate, List<Expense>> groupedExpenses = new LinkedHashMap<>();

        List<Utils.LocalDateRange> dateRanges = Utils.getLast12MonthsDateRanges(LocalDate.now());

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

        for (LocalDate periodStart : groupedInvoices.keySet()) {
            double invoiceTotal = groupedInvoices.get(periodStart).stream().collect(Collectors.summingDouble(Invoice::getTotal));
            double expenseTotal = groupedExpenses.get(periodStart).stream().collect(Collectors.summingDouble(Expense::getTotal));
            double profit = invoiceTotal - expenseTotal;
            int count = groupedInvoices.get(periodStart).size();
            invoiceTotalSeries.getData().add(new XYChart.Data<>(periodStart.toString(), invoiceTotal));
            expenseTotalSeries.getData().add(new XYChart.Data<>(periodStart.toString(), expenseTotal));
            profitSeries.getData().add(new XYChart.Data<>(periodStart.toString(), profit));
            numInvoicesSeries.getData().add(new XYChart.Data<>(periodStart.toString(), count));
        }

        totalLineChart.getData().add(invoiceTotalSeries);
        totalLineChart.getData().add(expenseTotalSeries);
        totalLineChart.getData().add(profitSeries);
        numInvoicesLineChart.getData().add(numInvoicesSeries);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
