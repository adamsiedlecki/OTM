package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OtmConfigProperties {

    @Value("${otm.gen1.api.address}")
    private String gen1ApiAddress;

    @Value("${otm.gen2.api.address}")
    private String gen2ApiAddress;

    @Value("${otm.gen3.api.address}")
    private String gen3ApiAddress;

    @Value("${otm.default.chart.width}")
    private int defaultChartWidth;

    @Value("${otm.default.chart.height}")
    private int defaultChartHeight;

    @Value("${odg.base.path:http://10.0.0.20:8086/api/v1}")
    private String odgBasePath;
}
