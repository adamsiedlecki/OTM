package pl.adamsiedlecki.otm.devices.api.gen2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.locationPlace.LocationPlaceService;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.stationInfo.gen2.Gen2DevicesInfo;
import pl.adamsiedlecki.otm.stationInfo.locations.LocationPlacesInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllDevicesGen2TemperatureGetter {

    private final StationGen2Service stationGen2Service;
    private final Gen2DevicesInfo gen2DevicesInfo;
    private final LocationPlacesInfo locationPlacesInfo;
    private final LocationService locationService;
    private final LocationPlaceService locationPlaceService;

    public List<TemperatureData> get() {
        LocalDateTime now = LocalDateTime.now();
        return gen2DevicesInfo.get().stream().map(gen2Device -> {
                    BigDecimal temperature = null;
                    try {
                        temperature = stationGen2Service.sendTemperatureRequest(gen2Device.getId(), false);

                    } catch (Exception ex) {
                        log.error("Temperature request error catched", ex);
                    }
                    return TemperatureData.builder()
                            .date(now)
                            .temperatureCelsius(temperature)
                            .transmitterName(gen2Device.getName())
                            .location(locationService.getOrSave(gen2Device.getLatitude(), gen2Device.getLongitude()))
                            .locationPlace(locationPlaceService.updateOrSave(locationPlacesInfo.getById(gen2Device.getLocationPlaceId())))
                            .build();

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
