package GUI;


import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends StringConverter<LocalDate> {

    private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy");

    @Override
    public String toString(LocalDate localDate) {
        return defaultFormatter.format(localDate);
    }

    @Override
    public LocalDate fromString(String string) {
        LocalDate retVal = LocalDate.now();
        if (string != null && !string.trim().isEmpty()) {
            retVal = LocalDate.parse(string, defaultFormatter);
        }
        return retVal;
    }
}
