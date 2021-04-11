package pl.adamsiedlecki.OTM.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextFormatters {

    public final static DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static String getPrettyTime(LocalDateTime time){
        return time.format(LOCAL_DATE_TIME_FORMATTER);
    }
}
