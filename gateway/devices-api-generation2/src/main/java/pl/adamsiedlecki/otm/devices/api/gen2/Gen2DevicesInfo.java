package pl.adamsiedlecki.otm.devices.api.gen2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Gen2DevicesInfo {

    public final String ALL_DEVICES_ADDRESS = "255";
    private final List<Gen2Device> list = List.of();

    public List<Gen2Device> get() {
        return list;
    }

    public Optional<Gen2Device> getById(String id) {
        return list.stream().filter(device -> device.getId().equals(id)).findFirst();
    }

    public Optional<Gen2Device> getByName(String name) {
        return list.stream().filter(device -> device.getName().equals(name)).findFirst();
    }
}
