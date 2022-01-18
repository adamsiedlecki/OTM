package pl.adamsiedlecki.otm.dataFetcher;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.devices.api.gen2.Gen2Device;
import pl.adamsiedlecki.otm.devices.api.gen2.Gen2DevicesInfo;
import pl.adamsiedlecki.otm.devices.api.gen2.TemperatureGen2Service;
import pl.adamsiedlecki.otm.exceptions.EspNoResponseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataFetcher {

    private final OtmConfigProperties properties;
    private final HtmlToTemperatureData htmlToData;
    private final TemperatureDataService temperatureDataService;
    private final Logger log = LoggerFactory.getLogger(DataFetcher.class);
    private final EspApiTool apiTool;
    private final TemperatureGen2Service temperatureGen2Service;
    private Gen2DevicesInfo gen2DevicesInfo;

    public List<TemperatureData> fetchAndSaveTemperatures() {
        List<TemperatureData> allResults = new ArrayList<>();

        allResults.addAll(getTemperaturesFromGen1Stations());
        allResults.addAll(getTemperaturesFromGen2Stations());

        saveToDatabase(allResults);
        return allResults;
    }

    private List<TemperatureData> getTemperaturesFromGen1Stations() {
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
        return temperatureData;
    }

    private List<TemperatureData> getTemperaturesFromGen2Stations() {
        LocalDateTime now = LocalDateTime.now();
        return temperatureGen2Service.sendTemperatureRequest(gen2DevicesInfo.ALL_DEVICES_ADDRESS)
                .stream()
                .map(output -> {
                    Optional<Gen2Device> device = gen2DevicesInfo.getByName(output.getSn());
                    if (device.isEmpty()) {
                        log.error("gen2 device is not recognized: " + output);
                        return null;
                    }
                    return TemperatureData.builder()
                            .date(now)
                            .temperatureCelsius(output.getTp())
                            .transmitterName(output.getSn())
                            .location(new Location(device.get().getLatitude(), device.get().getLongitude()))
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void saveToDatabase(List<TemperatureData> td) {
        temperatureDataService.saveAll(td);
    }

}
