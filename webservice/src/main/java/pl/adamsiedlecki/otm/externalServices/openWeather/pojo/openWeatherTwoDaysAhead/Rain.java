package pl.adamsiedlecki.otm.externalServices.openWeather.pojo.openWeatherTwoDaysAhead;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rain {
    @JsonProperty("1h")
    public double _1h;
}