package pl.adamsiedlecki.OTM.dataFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.HtmlToTemperatureData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataFetcher {

    @Value("${api.address}")
    private String apiAddress;
    private final HtmlToTemperatureData htmlToData;
    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(DataFetcher.class);

    @Autowired
    public DataFetcher(HtmlToTemperatureData htmlToData, TemperatureDataService temperatureDataService) {
        this.htmlToData = htmlToData;
        this.temperatureDataService = temperatureDataService;
    }

    public List<TemperatureData> fetch() {
        String content = getHtml();

        List<TemperatureData> temperatureData = htmlToData.process(content);

        // in case of blank response
        if (temperatureData.size() == 0) {
            log.info("There are no temperatures fetched!!!");
            sendRestartCommand();
            try {
                Thread.sleep(5500);
                content = getHtml();
                temperatureData = htmlToData.process(content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        //in case of a second blank response
        try {
            Thread.sleep(10000);
            content = getHtml();
            temperatureData = htmlToData.process(content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //in case of a third blank response
        if (temperatureData.size() == 0) {
            sendRestartCommand();
            try {
                Thread.sleep(6500);
                content = getHtml();
                temperatureData = htmlToData.process(content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        LocalDateTime now = LocalDateTime.now();
        for (TemperatureData td : temperatureData) {
            td.setDate(now);
        }
        temperatureDataService.saveAll(temperatureData);

        return temperatureData;
    }

    private String getHtml() {
        String content = null;
        URLConnection connection = null;
        int status = 0;
        try {
            URL url = new URL(apiAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(25000);
            status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
            con.disconnect();
            content = stringBuilder.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.info(status + "HTML content: " + content);
        return content;
    }

    private void sendRestartCommand() {
        try {
            log.info("Sending restart command to ESP");
            new URL(apiAddress + "/restart").openConnection();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
