package pl.adamsiedlecki.OTM.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class OtmConfigProperties {

    private String apiAddress;
}
