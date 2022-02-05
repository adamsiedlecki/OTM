package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OtherApiProperties {

    @Value("${open.weather.api.key:defaultOpenWeatherApiKey}")
    private String openWeatherApiKey;

}
