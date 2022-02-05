package pl.adamsiedlecki.otm.stationInfo.gen1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "gen1-devices")
public class Gen1DevicesInfo {

    private List<Gen1Device> devices = new ArrayList<>();

    public List<Gen1Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Gen1Device> devices) {
        this.devices = devices;
    }

    public Optional<Gen1Device> getByAliasName(String aliasName) {
        return devices.stream().filter(device -> device.getAliasName().equals(aliasName)).findFirst();
    }

    public Optional<Gen1Device> getByOriginalName(String originalName) {
        return devices.stream().filter(device -> device.getOriginalName().equals(originalName)).findFirst();
    }
}
