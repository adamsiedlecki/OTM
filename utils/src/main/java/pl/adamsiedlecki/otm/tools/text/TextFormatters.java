package pl.adamsiedlecki.otm.tools.text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextFormatters {

    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public final static DateTimeFormatter LOCAL_DATE_TIME_HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String getPrettyDateTime(LocalDateTime time) {
        return time.format(LOCAL_DATE_TIME_FORMATTER);
    }

    public static String getPrettyHour(LocalDateTime time) {
        return time.format(LOCAL_DATE_TIME_HOUR_FORMATTER);
    }
}
