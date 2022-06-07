package pl.adamsiedlecki.otm.station.info.gen3;

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
@ConfigurationProperties(prefix = "gen3-devices")
public class Gen3DevicesInfo {

    private List<Gen3Device> devices = new ArrayList<>();

    public List<Gen3Device> getDevices() {
        return List.copyOf(devices);
    }

    public void setDevices(List<Gen3Device> devices) {
        this.devices = devices;
    }

    public Optional<Gen3Device> getById(int id) {
        return devices.stream().filter(device -> device.getId() == id).findFirst();
    }

    public Optional<Gen3Device> getByName(String name) {
        return devices.stream().filter(device -> device.getName().equals(name)).findFirst();
    }
}
