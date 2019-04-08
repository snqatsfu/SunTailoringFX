package Data;

import Utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Summary {

    private final List<Invoice> inToday;
    private final List<Invoice> dueTodayOrTomorrow;
    private final List<Invoice> due3Days;

    public Summary(InvoiceStore store) {
        inToday = new ArrayList<>();
        dueTodayOrTomorrow = new ArrayList<>();
        due3Days = new ArrayList<>();
        final LocalDate now = LocalDate.now();

        store.all().stream().forEach(invoice -> {
            if (invoice.getInvoiceDate().equals(now)) {
                inToday.add(invoice);
            }
            if (!invoice.getDone() && !invoice.isDryCleanOnly()) {
                if (Utils.getNextBusinessDayDateRange(now).contains(invoice.getDueDate())) {
                    dueTodayOrTomorrow.add(invoice);
                } else if (Utils.getNextNBusinessDayDateRange(now, 3).contains(invoice.getDueDate())) {
                    due3Days.add(invoice);
                }
            }
        });

    }

    public Summary(List<Invoice> inToday, List<Invoice> dueTodayOrTomorrow, List<Invoice> due3Days) {
        this.inToday = new ArrayList<>(inToday);
        this.dueTodayOrTomorrow = new ArrayList<>(dueTodayOrTomorrow);
        this.due3Days = new ArrayList<>(due3Days);
    }

    public boolean changedFrom(@Nullable Summary other) {
        return other == null || !inToday.equals(other.inToday) || !dueTodayOrTomorrow.equals(other.dueTodayOrTomorrow) || !due3Days.equals(other.due3Days);
    }

    public String toHtml() {
        String retVal = "<h1>Summary " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, yyyy MMM dd")) + "</h1>\n";
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
