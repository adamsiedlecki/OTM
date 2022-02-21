package pl.adamsiedlecki.otm.external.services.open.weather;

import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead.Hourly;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class OpenWeatherTools {

    public List<Hourly> getOnlyXHoursForward(final List<Hourly> hourly, final int x) {
        return hourly.stream().filter(h -> LocalDateTime.ofEpochSecond(h.getDt(), 0, ZoneOffset.ofHours(2))
                .isBefore(LocalDateTime.now().plusHours(x))
        ).collect(Collectors.toList());
    }
}
