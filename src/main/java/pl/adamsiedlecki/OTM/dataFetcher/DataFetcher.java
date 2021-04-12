package pl.adamsiedlecki.OTM.dataFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.HtmlToTemperatureData;

import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

@Service
public class DataFetcher {

    @Value("${api.address}")
    private String apiAddress;
    private final HtmlToTemperatureData htmlToData;
    private final TemperatureDataService temperatureDataService;

    @Autowired
    public DataFetcher(HtmlToTemperatureData htmlToData, TemperatureDataService temperatureDataService) {
        this.htmlToData = htmlToData;
        this.temperatureDataService = temperatureDataService;
    }

    public List<TemperatureData> fetch(){
        String content = getHtml();

        List<TemperatureData> temperatureData = htmlToData.process(content);
        LocalDateTime now = LocalDateTime.now();
        for(TemperatureData td: temperatureData){
            td.setDate(now);
        }
        temperatureDataService.saveAll(temperatureData);
        return temperatureData;
    }

    private String getHtml() {
        String content = null;
        URLConnection connection = null;
        try {
            connection =  new URL(apiAddress).openConnection();
            connection.setReadTimeout(25000);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return content;
    }
}
