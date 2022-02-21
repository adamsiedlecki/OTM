package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current.Weather;

import java.util.List;

@Getter
@Setter
public class Hourly {
    private int dt;
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
    private int pressure;
    private int humidity;
    @JsonProperty("dew_point")
    private double dewPoint;
    private int uvi;
    private int clouds;
    private int visibility;
    @JsonProperty("wind_speed")
    private double windSpeed;
    @JsonProperty("wind_deg")
    private int windDeg;
    @JsonProperty("wind_gust")
    private double windGust;
    private List<Weather> weather;
    private int pop;
    private Rain rain;
}
