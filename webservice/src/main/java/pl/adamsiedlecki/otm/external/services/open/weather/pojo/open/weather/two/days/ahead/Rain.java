package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rain {
    @JsonProperty("1h")
    private double rainVolumeForLastHourMillimeters;
}
