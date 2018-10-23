package GUI;

import Data.Invoice;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
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

        final LocalDate twelveMonthsBeforeNow = LocalDate.now().minusMonths(11);
        for (int i = 0; i <= 12; i++) {
            final Month searchMonth = twelveMonthsBeforeNow.plusMonths(i).getMonth();
            final Double total = store.all().stream()
                    .filter(invoice -> invoice.getInvoiceDate().getMonth().equals(searchMonth))
                    .collect(Collectors.summingDouble(Invoice::getTotal));
            final Long count = store.all().stream()
                    .filter(invoice -> invoice.getInvoiceDate().getMonth().equals(searchMonth))
                    .count();

            totalSeries.getData().add(new XYChart.Data<>(searchMonth.name(), total));
            numInvoicesSeries.getData().add(new XYChart.Data<>(searchMonth.name(), count));
        }

        totalLineChart.getData().add(totalSeries);
        numInvoicesLineChart.getData().add(numInvoicesSeries);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
