package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.location.place.LocationPlaceService;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;
import pl.adamsiedlecki.otm.exception.EspNoResponseException;
import pl.adamsiedlecki.otm.station.info.gen1.Gen1Device;
import pl.adamsiedlecki.otm.station.info.gen1.Gen1DevicesInfo;
import pl.adamsiedlecki.otm.station.info.locations.LocationPlacesInfo;

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
            updateNameAndLocation(td, gen1Device.orElse(null));
            updateLocationPlace(td, gen1Device.orElse(null));
        }
        return temperatureData;
    }

    private void updateLocationPlace(TemperatureData td, Gen1Device gen1Device) {
        if (gen1Device != null) {
            Optional<LocationPlaceDto> locationPlaceDtoOptional = locationPlacesInfo.getById(gen1Device.getLocationPlaceId());
            td.setLocationPlace(locationPlaceService.updateOrSave(locationPlaceDtoOptional.orElse(null)));
        }
    }

    private void updateNameAndLocation(TemperatureData td, Gen1Device gen1Device) {
        if (gen1Device == null) {
            log.error("No information found about gen1 device: " + td.getTransmitterName());
        } else {
            td.setTransmitterName(td.getTransmitterName() + " " + gen1Device.getAliasName());
            Location location = locationService.getOrSave("" + gen1Device.getLatitude(), "" + gen1Device.getLongitude());
            td.setLocation(location);
        }
    }
}
