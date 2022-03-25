package pl.adamsiedlecki.otm.devices.api.gen2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2Device;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2DevicesInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AllDevicesGen2EnableSleepModeUtil {

    private final StationGen2Service stationGen2Service;
    private final Gen2DevicesInfo gen2DevicesInfo;

    public List<Pair<Gen2Device, String>> get() {
        return gen2DevicesInfo.getDevices()
                .stream()
                .map(gen2Device -> {
                    try {
                        String response = stationGen2Service.sendSleepModeRequest(gen2Device.getId(), false);
                        return Pair.of(gen2Device, response);
                    } catch (Exception ex) {
                        log.error("Sleep mode request error caught: {}", ex.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
