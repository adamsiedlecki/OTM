package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.locationPlace.LocationPlaceService;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.exception.EspNoResponseException;
import pl.adamsiedlecki.otm.stationInfo.gen1.Gen1Device;
import pl.adamsiedlecki.otm.stationInfo.gen1.Gen1DevicesInfo;
import pl.adamsiedlecki.otm.stationInfo.locations.LocationPlacesInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllDevicesGen1TemperatureGetter {

    private final EspApiTool espApiTool;
    private final OtmConfigProperties properties;
    private final HtmlToTemperatureData htmlToData;
    private final Gen1DevicesInfo gen1DevicesInfo;
    private final LocationService locationService;
    private final LocationPlaceService locationPlaceService;
    private final LocationPlacesInfo locationPlacesInfo;

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
            Optional<Gen1Device> gen1Device = gen1DevicesInfo.getByOriginalName(td.getTransmitterName().trim());
            td.setDate(now);
            updateNameAndLocation(td, gen1Device);
            updateLocationPlace(td, gen1Device);
        }
        return temperatureData;
    }

    private void updateLocationPlace(TemperatureData td, Optional<Gen1Device> gen1Device) {
        if (gen1Device.isPresent()) {
            td.setLocationPlace(locationPlaceService.updateOrSave(locationPlacesInfo.getById(gen1Device.get().getLocationPlaceId())));
        }
    }

    private void updateNameAndLocation(TemperatureData td, Optional<Gen1Device> gen1Device) {
        if (gen1Device.isEmpty()) {
            log.error("No information found about gen1 device: " + td.getTransmitterName());
        } else {
            td.setTransmitterName(td.getTransmitterName() + " " + gen1Device.get().getAliasName());
            Location location = locationService.getOrSave("" + gen1Device.get().getLatitude(), "" + gen1Device.get().getLongitude());
            td.setLocation(location);
        }
    }
}
