package Utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static final DecimalFormat currencyFormatter = new DecimalFormat("0.00");

    public static String formatCurrency(double amount) {
        return currencyFormatter.format(amount);
    }

    public static class LocalDateRange {
        public final LocalDate start;
        public final LocalDate end;

        public LocalDateRange(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(LocalDate date) {
            return !date.isBefore(start) && !date.isAfter(end);
        }

        @Override
        public String toString() {
            return "Start " + start + ", End " + end;
        }
    }

    public static List<LocalDateRange> getLast12MonthsDateRanges(LocalDate from) {
        List<LocalDateRange> retVal = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            LocalDate localDate = from.minusMonths(i);
            retVal.add(new LocalDateRange(localDate.withDayOfMonth(1), localDate.withDayOfMonth(localDate.lengthOfMonth())));
        }
        return retVal;
    }

    public static List<LocalDateRange> getLast52WeeksDateRanges(LocalDate from) {
        List<LocalDateRange> retVal = new ArrayList<>();
        TemporalField weekField = WeekFields.of(Locale.CANADA).dayOfWeek();
        for (int i = 51; i >= 0; i--) {
            LocalDate localDate = from.minusWeeks(i);
            retVal.add(new LocalDateRange(localDate.with(weekField, 1), localDate.with(weekField, 7)));
        }
        return retVal;
    }
}
