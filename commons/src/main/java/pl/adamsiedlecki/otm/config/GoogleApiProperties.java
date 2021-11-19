package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
@PropertySource("classpath:application.properties")
public class GoogleApiProperties {

    @Value("${maps.api.key:defaultGoogleMapsApiKey}")
    private String mapsApiKey;

}
