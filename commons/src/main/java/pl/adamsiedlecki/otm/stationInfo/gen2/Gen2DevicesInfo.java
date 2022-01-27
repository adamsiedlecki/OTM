package pl.adamsiedlecki.otm.stationInfo.gen2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class Gen2DevicesInfo {

    private final ObjectMapper objectMapper;

    @Value("#{'${devices.gen2}'.split('x')}")
    private String[] devicesJsons;

    private List<Gen2Device> devicesList;

    @PostConstruct
    private void postConstruct() {
        devicesList = new ArrayList<>();
        for (String device : devicesJsons) {
            try {
                JsonNode jsonNode = objectMapper.readTree(device);
                int id = jsonNode.findValue("id").asInt();
                String name = jsonNode.findValue("name").asText();
                String lng = jsonNode.findValue("lng").asText();
                String lat = jsonNode.findValue("lat").asText();
                long locationPlaceId = jsonNode.findValue("lp").asLong();

                devicesList.add(Gen2Device.builder().name(name).id(id).longitude(lng).latitude(lat).locationPlaceId(locationPlaceId).build());

            } catch (JsonProcessingException e) {
                log.error("Error during json deviceGen2 parsing: \n" + device, e);
            }

        }
    }

    public List<Gen2Device> get() {
        return devicesList;
    }

    public Optional<Gen2Device> getById(int id) {
        return devicesList.stream().filter(device -> device.getId() == id).findFirst();
    }

    public Optional<Gen2Device> getByName(String name) {
        return devicesList.stream().filter(device -> device.getName().equals(name)).findFirst();
    }
}
