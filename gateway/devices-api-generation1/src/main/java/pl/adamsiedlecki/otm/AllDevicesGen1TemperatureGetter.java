package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.exception.EspNoResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllDevicesGen1TemperatureGetter {

    private final EspApiTool espApiTool;
    private final OtmConfigProperties properties;
    private final HtmlToTemperatureData htmlToData;

    public List<TemperatureData> get() {
        String content;
        try {
            content = espApiTool.getHtml(properties.getGen1ApiAddress());
        } catch (EspNoResponseException e) {
            content = espApiTool.espNoResponseStrategy(properties.getGen1ApiAddress());
        }

        List<TemperatureData> temperatureData = htmlToData.process(content);
        LocalDateTime now = LocalDateTime.now();
        for (TemperatureData td : temperatureData) {
            td.setDate(now);
        }
        return temperatureData;
    }
}
