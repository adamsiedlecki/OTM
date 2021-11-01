package pl.adamsiedlecki.OTM.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "otm")
@PropertySource("classpath:application.properties")
public class OtmConfigProperties {

    @Value("${otm.api.address}")
    private String apiAddress;

    @Value("${otm.default.chart.width}")
    private int defaultChartWidth;

    @Value("${otm.default.chart.height}")
    private int defaultChartHeight;
}
