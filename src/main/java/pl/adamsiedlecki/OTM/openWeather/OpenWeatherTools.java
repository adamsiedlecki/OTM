package pl.adamsiedlecki.OTM.openWeather;

import pl.adamsiedlecki.OTM.openWeather.pojo.openWeatherTwoDaysAhead.Hourly;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class OpenWeatherTools {

    public List<Hourly> getOnlyXHoursForward(List<Hourly> hourly, int x) {
        return hourly.stream().filter(h ->
                LocalDateTime.now().plusHours(x)
                        .isBefore(LocalDateTime.ofEpochSecond(h.getDt(), 0, ZoneOffset.ofHours(2)))
        ).collect(Collectors.toList());
    }
}
