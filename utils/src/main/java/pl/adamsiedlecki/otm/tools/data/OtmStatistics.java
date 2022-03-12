package pl.adamsiedlecki.otm.tools.data;

import lombok.Getter;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
public class OtmStatistics {

    private final Optional<TemperatureData> lowestTemperature;
    private final Optional<TemperatureData> highestTemperature;

    public OtmStatistics(List<TemperatureData> data) {
        lowestTemperature = data.stream().min(Comparator.comparing(TemperatureData::getTemperatureCelsius));
        highestTemperature = data.stream().max(Comparator.comparing(TemperatureData::getTemperatureCelsius));
    }

}
