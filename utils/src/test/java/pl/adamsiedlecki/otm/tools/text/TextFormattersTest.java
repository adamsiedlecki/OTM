package pl.adamsiedlecki.otm.tools.text;

import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.testng.Assert.assertEquals;

public class TextFormattersTest {

    @Test
    public void shouldGetPrettyLocalDate() {
        //given
        LocalDate localDate = LocalDate.of(2022, 3, 4);

        //when
        String result = TextFormatters.getPretty(localDate);

        //then
        assertEquals(result, "4.3.2022");
    }

    @Test
    public void shouldGetPrettyLocalDateTime() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2022, 3, 4, 6, 30);

        //when
        String result = TextFormatters.getPretty(localDateTime);

        //then
        assertEquals(result, "4.3.2022 6:30");
    }

    @Test
    public void shouldGetPrettyHourAndMinutesFromLocalDateTime() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2022, 3, 4, 6, 30);

        //when
        String result = TextFormatters.getPrettyHour(localDateTime);

        //then
        assertEquals(result, "6:30");
    }
}