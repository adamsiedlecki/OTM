package pl.adamsiedlecki.otm.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataUtils {

    public static List<TemperatureData> prepareTemperatureDataList(int stationAmount) {
        Random rand = new Random();
        List<TemperatureData> tdList = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2021, 11, 1, 22, 0);

        for (int i = 1; i <= stationAmount; i++) {
            for (int j = 0; j < 24; j++) {
                TemperatureData td = new TemperatureData();
                setData(td, "station-" + i, start.plusMinutes(j * 30), (rand.nextInt(31) - 10.55f));
                tdList.add(td);
            }
        }

        return tdList;
    }

    public static void setData(TemperatureData td, String stationName, LocalDateTime time, float temperature) {
        td.setTransmitterName(stationName);
        td.setDate(time);
        td.setTemperatureCelsius(BigDecimal.valueOf(temperature));
    }
}
