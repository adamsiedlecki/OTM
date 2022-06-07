package pl.adamsiedlecki.otm.devices.api.gen3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckData;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.location.place.LocationPlaceService;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;
import pl.adamsiedlecki.otm.station.info.gen3.Gen3DevicesInfo;
import pl.adamsiedlecki.otm.station.info.locations.LocationPlacesInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllDevicesGen3BatteryVoltageGetter {

    private final StationGen3Service stationGen3Service;
    private final Gen3DevicesInfo gen3DevicesInfo;
    private final LocationPlacesInfo locationPlacesInfo;
    private final LocationService locationService;
    private final LocationPlaceService locationPlaceService;

    public List<HealthCheckData> get() {
        LocalDateTime now = LocalDateTime.now();
        return gen3DevicesInfo.getDevices().stream().map(gen3Device -> {
                    BigDecimal voltage;
                    try {
                        voltage = stationGen3Service.sendBatteryVoltageRequest(gen3Device.getId());

                    } catch (Exception ex) {
                        log.error("Voltage request error caught: {}", ex.getMessage());
                        return null;
                    }
                    Optional<LocationPlaceDto> locPlaceOptional = locationPlacesInfo.getById(gen3Device.getLocationPlaceId());
                    return HealthCheckData.builder()
                            .date(now)
                            .voltage(voltage)
                            .transmitterName(gen3Device.getName())
                            .location(locationService.getOrSave(gen3Device.getLatitude(), gen3Device.getLongitude()))
                            .locationPlace(locationPlaceService.updateOrSave(locPlaceOptional.orElse(null)))
                            .build();

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
