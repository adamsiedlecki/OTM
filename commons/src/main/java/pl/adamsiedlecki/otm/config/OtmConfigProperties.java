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
@ConfigurationProperties(prefix = "otm")
@PropertySource("classpath:application.properties")
public class OtmConfigProperties {

    @Value("${gen1.api.address}")
    private String gen1ApiAddress;

    @Value("${gen2.api.address}")
    private String gen2ApiAddress;

    @Value("${otm.default.chart.width}")
    private int defaultChartWidth;

    @Value("${otm.default.chart.height}")
    private int defaultChartHeight;
}
