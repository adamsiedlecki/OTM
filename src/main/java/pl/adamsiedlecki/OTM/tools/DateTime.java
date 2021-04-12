package pl.adamsiedlecki.OTM.tools;

import java.time.LocalDateTime;

public class DateTime {

    public static LocalDateTime now() {
        return LocalDateTime.now().plusHours(2);
    }
}
