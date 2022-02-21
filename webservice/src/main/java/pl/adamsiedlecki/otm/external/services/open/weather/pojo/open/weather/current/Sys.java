package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sys {
    private int type;
    private int id;
    private String country;
    private int sunrise;
    private int sunset;
}
