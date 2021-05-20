package pl.adamsiedlecki.OTM.dataFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.exceptions.EspNoResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataFetcher {

    @Value("${api.address}")
    private String apiAddress;
    private final HtmlToTemperatureData htmlToData;
    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(DataFetcher.class);
    private final EspApiTool apiTool;

    @Autowired
    public DataFetcher(HtmlToTemperatureData htmlToData, TemperatureDataService temperatureDataService, EspApiTool apiTool) {
        this.htmlToData = htmlToData;
        this.temperatureDataService = temperatureDataService;
        this.apiTool = apiTool;
    }

    public List<TemperatureData> fetch() {
        String content;
        try {
            content = apiTool.getHtml(apiAddress);
        } catch (EspNoResponseException e) {
            content = apiTool.espNoResponseStrategy(apiAddress);
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
