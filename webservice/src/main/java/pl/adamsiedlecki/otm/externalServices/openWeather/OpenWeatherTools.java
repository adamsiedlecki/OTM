package pl.adamsiedlecki.otm.externalServices.openWeather;

import pl.adamsiedlecki.otm.externalServices.openWeather.pojo.openWeatherTwoDaysAhead.Hourly;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class OpenWeatherTools {

    public List<Hourly> getOnlyXHoursForward(List<Hourly> hourly, int x) {
        return hourly.stream().filter(h -> LocalDateTime.ofEpochSecond(h.getDt(), 0, ZoneOffset.ofHours(2))
                .isBefore(LocalDateTime.now().plusHours(x))
        ).collect(Collectors.toList());
    }
}
