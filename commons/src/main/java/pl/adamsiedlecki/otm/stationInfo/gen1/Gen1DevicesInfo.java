package pl.adamsiedlecki.otm.stationInfo.gen1;

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
public class Gen1DevicesInfo {

    private final ObjectMapper objectMapper;

    @Value("#{'${devices.gen1}'.split('x')}")
    private String[] devicesJsons;

    private List<Gen1Device> devicesList;

    @PostConstruct
    private void postConstruct() {
        devicesList = new ArrayList<>();
        for (String device : devicesJsons) {
            try {
                JsonNode jsonNode = objectMapper.readTree(device);
                String originalName = jsonNode.findValue("o").asText();
                String aliasName = jsonNode.findValue("a").asText();
                String lng = jsonNode.findValue("lng").asText();
                String lat = jsonNode.findValue("lat").asText();
                long locationPlaceId = jsonNode.findValue("lp").asLong();

                devicesList.add(
                        Gen1Device.builder()
                                .originalName(originalName)
                                .aliasName(aliasName)
                                .longitude(lng)
                                .latitude(lat)
                                .locationPlaceId(locationPlaceId)
                                .build());

            } catch (JsonProcessingException e) {
                log.error("Error during json deviceGen1 parsing: \n" + device, e);
            }

        }
    }

    public List<Gen1Device> get() {
        return devicesList;
    }

    public Optional<Gen1Device> getByAliasName(String aliasName) {
        return devicesList.stream().filter(device -> device.getAliasName().equals(aliasName)).findFirst();
    }

    public Optional<Gen1Device> getByOriginalName(String originalName) {
        return devicesList.stream().filter(device -> device.getOriginalName().equals(originalName)).findFirst();
    }
}
