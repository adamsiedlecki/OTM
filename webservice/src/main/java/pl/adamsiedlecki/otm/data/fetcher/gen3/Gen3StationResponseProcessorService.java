package pl.adamsiedlecki.otm.data.fetcher.gen3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.location.place.LocationPlaceService;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.devices.api.gen3.StationGen3Service;
import pl.adamsiedlecki.otm.devices.api.gen3.exceptions.StationProbablyInDangerException;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;
import pl.adamsiedlecki.otm.services.StationInDangerService;
import pl.adamsiedlecki.otm.station.info.gen3.Gen3Device;
import pl.adamsiedlecki.otm.station.info.locations.LocationPlacesInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Gen3StationResponseProcessorService {

    private final LocationPlacesInfo locationPlacesInfo;
    private final LocationService locationService;
    private final LocationPlaceService locationPlaceService;
    private final StationInDangerService stationInDangerService;
    private final StationGen3Service stationGen3Service;


    public TemperatureData processTemperatureRequest(Gen3Device gen3Device) {
        LocalDateTime now = LocalDateTime.now();
        BigDecimal temperature;
        try {
            temperature = stationGen3Service.sendTemperatureRequest(gen3Device.getId());

        } catch(StationProbablyInDangerException stationProbablyInDangerException) {
            if (gen3Device.isCanBeInDanger()) {
                stationInDangerService.registerStationInDanger(gen3Device);
            }
            return null;
        } catch (Exception ex) {
            log.error("Temperature request error caught: {}", ex.getMessage());
            return null;
        }
        Optional<LocationPlaceDto> locPlaceOptional = locationPlacesInfo.getById(gen3Device.getLocationPlaceId());
        return TemperatureData.builder()
                .date(now)
                .temperatureCelsius(temperature)
                .transmitterName(gen3Device.getName())
                .location(locationService.getOrSave(gen3Device.getLatitude(), gen3Device.getLongitude()))
                .locationPlace(locationPlaceService.updateOrSave(locPlaceOptional.orElse(null)))
                .build();
    }
}
