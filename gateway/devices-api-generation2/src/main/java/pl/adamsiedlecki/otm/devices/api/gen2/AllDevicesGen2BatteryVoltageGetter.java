package pl.adamsiedlecki.otm.devices.api.gen2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckData;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.location.place.LocationPlaceService;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2DevicesInfo;
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
public class AllDevicesGen2BatteryVoltageGetter {

    private final StationGen2Service stationGen2Service;
    private final Gen2DevicesInfo gen2DevicesInfo;
    private final LocationPlacesInfo locationPlacesInfo;
    private final LocationService locationService;
    private final LocationPlaceService locationPlaceService;

    public List<HealthCheckData> get() {
        LocalDateTime now = LocalDateTime.now();
        return gen2DevicesInfo.getDevices().stream().map(gen2Device -> {
                    BigDecimal voltage;
                    try {
                        voltage = stationGen2Service.sendBatteryVoltageRequest(gen2Device.getId(), false);

                    } catch (Exception ex) {
                        log.error("Voltage (healthCheck) request error caught: {}", ex.getMessage());
                        return null;
                    }
                    Optional<LocationPlaceDto> locPlaceOptional = locationPlacesInfo.getById(gen2Device.getLocationPlaceId());
                    return HealthCheckData.builder()
                            .date(now)
                            .voltage(voltage)
                            .transmitterName(gen2Device.getName())
                            .location(locationService.getOrSave(gen2Device.getLatitude(), gen2Device.getLongitude()))
                            .locationPlace(locationPlaceService.updateOrSave(locPlaceOptional.orElse(null)))
                            .build();

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
