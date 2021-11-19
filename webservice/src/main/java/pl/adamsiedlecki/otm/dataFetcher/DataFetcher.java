package pl.adamsiedlecki.otm.dataFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.exceptions.EspNoResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataFetcher {

    private final OtmConfigProperties properties;
    private final HtmlToTemperatureData htmlToData;
    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(DataFetcher.class);
    private final EspApiTool apiTool;

    @Autowired
    public DataFetcher(HtmlToTemperatureData htmlToData, TemperatureDataService temperatureDataService, EspApiTool apiTool, OtmConfigProperties properties) {
        this.htmlToData = htmlToData;
        this.temperatureDataService = temperatureDataService;
        this.apiTool = apiTool;
        this.properties = properties;
    }

    public List<TemperatureData> fetch() {
        String content;
        try {
            content = apiTool.getHtml(properties.getApiAddress());
        } catch (EspNoResponseException e) {
            content = apiTool.espNoResponseStrategy(properties.getApiAddress());
        }

        List<TemperatureData> temperatureData = htmlToData.process(content);
        LocalDateTime now = LocalDateTime.now();
        for (TemperatureData td : temperatureData) {
            td.setDate(now);
        }
        temperatureDataService.saveAll(temperatureData);
        return temperatureData;
    }

}
