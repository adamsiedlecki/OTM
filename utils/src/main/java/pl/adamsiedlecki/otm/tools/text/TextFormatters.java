package pl.adamsiedlecki.otm.tools.text;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TextFormatters {

    public static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public static final DateTimeFormatter LOCAL_DATE_TIME_HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String getPretty(LocalDateTime time) {
        return time.format(LOCAL_DATE_TIME_FORMATTER);
    }

    public static String getPretty(LocalDate date) {
        return date.format(LOCAL_DATE_FORMATTER);
    }

    public static String getPrettyHour(LocalDateTime time) {
        return time.format(LOCAL_DATE_TIME_HOUR_FORMATTER);
    }
}
