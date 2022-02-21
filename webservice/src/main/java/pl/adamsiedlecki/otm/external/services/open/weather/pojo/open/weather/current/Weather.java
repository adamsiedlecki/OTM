package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
}
