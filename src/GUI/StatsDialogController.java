package GUI;

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

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class StatsDialogController implements Initializable {

    public VBox root;
    public LineChart<String, Number> totalLineChart;
    public LineChart<String, Number> numInvoicesLineChart;

    public void setInvoiceStore(InvoiceStore store) {
        XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
        totalSeries.setName("Total");
        XYChart.Series<String, Number> numInvoicesSeries = new XYChart.Series<>();
        numInvoicesSeries.setName("# invoices");

        Map<LocalDate, List<Invoice>> groupedInvoices = new LinkedHashMap<>();
        List<Utils.LocalDateRange> dateRanges = Utils.getLast12MonthsDateRanges(LocalDate.now());
        dateRanges.forEach(dateRange -> groupedInvoices.put(dateRange.start, new ArrayList<>()));
        store.all().stream().forEach(invoice -> {
            final LocalDate invoiceDate = invoice.getInvoiceDate();
            for (Utils.LocalDateRange localDateRange : dateRanges) {
                if (localDateRange.contains(invoiceDate)) {
                    groupedInvoices.get(localDateRange.start).add(invoice);
                    break;
                }
            }
        });

        for (LocalDate periodStart : groupedInvoices.keySet()) {
            double total = groupedInvoices.get(periodStart).stream().collect(Collectors.summingDouble(Invoice::getTotal));
            int count = groupedInvoices.get(periodStart).size();
            totalSeries.getData().add(new XYChart.Data<>(periodStart.toString(), total));
            numInvoicesSeries.getData().add(new XYChart.Data<>(periodStart.toString(), count));
        }

        totalLineChart.getData().add(totalSeries);
        numInvoicesLineChart.getData().add(numInvoicesSeries);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
