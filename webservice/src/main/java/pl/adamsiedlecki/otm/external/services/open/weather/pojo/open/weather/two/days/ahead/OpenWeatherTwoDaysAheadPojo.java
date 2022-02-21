package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenWeatherTwoDaysAheadPojo {
    private double lat;
    private double lon;
    private String timezone;
    @JsonProperty("timezone_offset")
    private int timezoneOffset;
    private List<Hourly> hourly;
}
