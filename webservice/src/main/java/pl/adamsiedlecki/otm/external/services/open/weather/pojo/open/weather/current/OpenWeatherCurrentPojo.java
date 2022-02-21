package pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Pojo class to dealing with OpenWeather API.
 *
 * @see <a href="https://openweathermap.org/api/hourly-forecast">Documentation</a>
 */
@Getter
@Setter
public class OpenWeatherCurrentPojo {
    private Coord coord;
    private List<Weather> weather;
    private String base;
    private MainWeatherInformation main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
}
