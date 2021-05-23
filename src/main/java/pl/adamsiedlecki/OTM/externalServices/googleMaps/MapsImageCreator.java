package pl.adamsiedlecki.OTM.externalServices.googleMaps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.files.MyFilesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// class is not finished yet; temperature is not included at marker because of behaviour of Google api(only one char for label)
@Component
public class MapsImageCreator {

    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(MapsImageCreator.class);
    @Value("${google.maps.api.key}")
    private String apiKey;

    @Autowired
    public MapsImageCreator(TemperatureDataService temperatureDataService) {
        this.temperatureDataService = temperatureDataService;
    }

    public File get(List<TemperatureData> tdList) {

        try {
            URL url = getUrl(tdList);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            File mapFile = new File(MyFilesystem.getStoragePath() + "map.png");
            FileOutputStream out = new FileOutputStream(mapFile);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            log.info("Downloaded map file: " + mapFile.getAbsolutePath());
            return mapFile;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new File("");
    }

    private URL getUrl(List<TemperatureData> tdList) throws MalformedURLException {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap");
        sb.append("?size=1000x1000")
                .append("&maptype=satellite")
                .append("&key=")
                .append(apiKey)
                .append("&scale=2");
        for (TemperatureData td : tdList) {
            String color;
            if (td.getTemperatureCelsius().compareTo(BigDecimal.ZERO) < 0) {
                color = "blue";
            } else {
                color = "yellow";
            }
            sb.append("&markers=color:")
                    .append(color)
                    .append("|") // %7C
                    .append("label:")
                    .append(td.getTemperatureCelsius())
                    .append("|")
                    .append(td.getLocation().getLatitude())
                    .append(",")
                    .append(td.getLocation().getLongitude())
            ;
        }
        String url = sb.toString();
        url = url.replaceAll(" ", "%20");
        log.info("Google maps url: " + url);
        return new URL(url);
    }
}
