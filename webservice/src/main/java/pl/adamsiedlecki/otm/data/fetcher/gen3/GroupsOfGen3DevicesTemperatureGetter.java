package pl.adamsiedlecki.otm.data.fetcher.gen3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.station.info.gen3.Gen3Device;
import pl.adamsiedlecki.otm.station.info.gen3.Gen3DevicesInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupsOfGen3DevicesTemperatureGetter {

    private final Gen3DevicesInfo gen3DevicesInfo;
    private final Gen3StationResponseProcessorService responseProcessorService;

    public List<TemperatureData> getFromAllStations() {
        return gen3DevicesInfo.getDevices()
                .stream()
                .map(responseProcessorService::processTemperatureRequest)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<TemperatureData> getFromStationsThatCanBeInDanger() {
        return gen3DevicesInfo.getDevices()
                .stream()
                .filter(Gen3Device::isCanBeInDanger)
                .map(responseProcessorService::processTemperatureRequest)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
