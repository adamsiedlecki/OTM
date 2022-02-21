package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wind {
    private double speed;
    private int deg;
    private double gust;
}
