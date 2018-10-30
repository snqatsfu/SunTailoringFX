package Utils;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class UtilsTest {

    @Test
    public void testGetLast12MonthsDateRanges() {
        List<Utils.LocalDateRange> localDateRanges = Utils.getLast12MonthsDateRanges(LocalDate.now());
        localDateRanges.forEach(System.out::println);
    }

    @Test
    public void testGetLast52WeeksDateRanges() {
        List<Utils.LocalDateRange> localDateRanges = Utils.getLast52WeeksDateRanges(LocalDate.now());
        localDateRanges.forEach(System.out::println);
    }

}
