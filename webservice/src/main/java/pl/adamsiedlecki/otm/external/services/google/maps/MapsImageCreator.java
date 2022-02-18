package pl.adamsiedlecki.otm.external.services.google.maps;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.GoogleApiProperties;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;

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
@RequiredArgsConstructor
public class MapsImageCreator {

    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(MapsImageCreator.class);
    private final GoogleApiProperties properties;

    public File get(List<TemperatureData> tdList) {

        URL url;
        try {
            url = getUrl(tdList);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return new File("");
        }
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {

            File mapFile = new File(MyFilesystem.getStoragePath() + "map.png");
            writeToFile(in, mapFile);
            log.info("Downloaded map file: {}", mapFile.getAbsolutePath());
            return mapFile;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new File("");
    }

    private void writeToFile(BufferedInputStream in, File mapFile) {
        try (FileOutputStream out = new FileOutputStream(mapFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private URL getUrl(List<TemperatureData> tdList) throws MalformedURLException {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap");
        sb.append("?size=1000x1000")
                .append("&maptype=satellite")
                .append("&key=")
                .append(properties.getMapsApiKey())
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
        url = url.replace(" ", "%20");
        log.info("Google maps url: {}", url);
        return new URL(url);
    }
}
