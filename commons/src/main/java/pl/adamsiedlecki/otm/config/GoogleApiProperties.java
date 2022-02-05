package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class GoogleApiProperties {

    @Value("${google.maps.api.key:defaultGoogleMapsApiKey}")
    private String mapsApiKey;

}
