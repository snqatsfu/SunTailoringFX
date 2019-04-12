package Data;

import Utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Summary {

    private final List<Invoice> inToday;
    private final List<Invoice> dueTodayOrTomorrow;
    private final List<Invoice> due3Days;

    private double totalRecent2Days;
    private double totalRecent7Days;
    private double totalRecent30Days;

    public Summary(InvoiceStore store) {
        inToday = new ArrayList<>();
        dueTodayOrTomorrow = new ArrayList<>();
        due3Days = new ArrayList<>();

        totalRecent2Days = 0;
        totalRecent7Days = 0;
        totalRecent30Days = 0;
        final LocalDate now = LocalDate.now();
        final Utils.LocalDateRange recent2Days = Utils.getTodayMinusDaysDateRange(1);
        final Utils.LocalDateRange recent7Days = Utils.getTodayMinusDaysDateRange(7);
        final Utils.LocalDateRange recent30Days = Utils.getTodayMinusDaysDateRange(30);
        final Utils.LocalDateRange nextBusinessDayDateRange = Utils.getNextBusinessDayDateRange(now);
        final Utils.LocalDateRange next3BusinessDaysDateRange = Utils.getNextNBusinessDayDateRange(now, 3);

        store.all().stream().forEach(invoice -> {
            final LocalDate inDate = invoice.getInvoiceDate();
            if (recent2Days.contains(inDate)) {
                totalRecent2Days += invoice.getTotal();
            }
            if (recent7Days.contains(inDate)) {
                totalRecent7Days += invoice.getTotal();
            }
            if (recent30Days.contains(inDate)) {
                totalRecent30Days += invoice.getTotal();
            }

            if (inDate.equals(now)) {
                inToday.add(invoice);
            }
            if (!invoice.getDone() && !invoice.isDryCleanOnly()) {
                if (nextBusinessDayDateRange.contains(invoice.getDueDate())) {
                    dueTodayOrTomorrow.add(invoice);
                } else if (next3BusinessDaysDateRange.contains(invoice.getDueDate())) {
                    due3Days.add(invoice);
                }
            }
        });

    }

    public boolean changedFrom(@Nullable Summary other) {
        return other == null || !inToday.equals(other.inToday) || !dueTodayOrTomorrow.equals(other.dueTodayOrTomorrow) || !due3Days.equals(other.due3Days);
    }

    public String toHtml() {
        final NumberFormat cf = NumberFormat.getCurrencyInstance();
        String retVal = "<h1>Summary " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, yyyy MMM dd")) + "</h1>\n";
        retVal += "<h2>Revenue:</h2>"  + cf.format(totalRecent2Days) + " recent 2 days, "
                + cf.format(totalRecent7Days) + " recent 7 days, "
                + cf.format(totalRecent30Days) + " recent 30 days";
        retVal += "<h2>In Today (" + inToday.size() + ")</h2><ul>\n";
        for (Invoice invoice : inToday) {
            retVal += "<li>" + invoice.shortHtmlSummary() + "</li>\n";
        }
        retVal += "</ul>\n";

        retVal += "<h2>Due Today or Tomorrow (" + dueTodayOrTomorrow.size() + ")</h2><ul>\n";
        for (Invoice invoice : dueTodayOrTomorrow) {
            retVal += "<li>" + invoice.shortHtmlSummary() + "</li>\n";
        }
        retVal += "</ul>\n";

        retVal += "<h2>Due 3 Days (" + due3Days.size() + ")</h2><ul>\n";
        for (Invoice invoice : due3Days) {
            retVal += "<li>" + invoice.shortHtmlSummary() + "</li>\n";
        }
        retVal += "</ul>\n";

        return retVal;
    }

}
