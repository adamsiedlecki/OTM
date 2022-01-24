package pl.adamsiedlecki.otm.devices.api.gen2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Gen2DevicesInfo {

    private final List<Gen2Device> list = List.of(
            Gen2Device.builder().id(1).name("Pierwsza stacja!").build()
    );

    public List<Gen2Device> get() {
        return list;
    }

    public Optional<Gen2Device> getById(int id) {
        return list.stream().filter(device -> device.getId() == id).findFirst();
    }

    public Optional<Gen2Device> getByName(String name) {
        return list.stream().filter(device -> device.getName().equals(name)).findFirst();
    }
}
