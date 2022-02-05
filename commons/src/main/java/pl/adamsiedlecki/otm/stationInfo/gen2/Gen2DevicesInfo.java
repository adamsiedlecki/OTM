package pl.adamsiedlecki.otm.stationInfo.gen2;

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
@ConfigurationProperties(prefix = "gen2-devices")
public class Gen2DevicesInfo {

    private List<Gen2Device> devices = new ArrayList<>();

    public List<Gen2Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Gen2Device> devices) {
        this.devices = devices;
    }

    public Optional<Gen2Device> getById(int id) {
        return devices.stream().filter(device -> device.getId() == id).findFirst();
    }

    public Optional<Gen2Device> getByName(String name) {
        return devices.stream().filter(device -> device.getName().equals(name)).findFirst();
    }
}
