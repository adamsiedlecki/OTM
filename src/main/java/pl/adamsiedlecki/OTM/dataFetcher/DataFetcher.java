package pl.adamsiedlecki.OTM.dataFetcher;

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

    public List<TemperatureData> fetch() {
        String content = getHtml();

        List<TemperatureData> temperatureData = htmlToData.process(content);

        // in case of blank response
        if (temperatureData.size() == 0) {
            System.out.println("There are no temperatures fetched!!!");
            sendRestartCommand();
            try {
                Thread.sleep(2500);
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
        System.out.println(status + "HTML content: " + content);
        return content;
    }

    private String sendRestartCommand() {
        String content = null;
        URLConnection connection = null;
        try {
            connection = new URL(apiAddress + "/restart").openConnection();
            connection.setReadTimeout(25000);
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }
}
