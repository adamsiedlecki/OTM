package pl.adamsiedlecki.otm.stationInfo.locations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationPlacesInfo {

    private final ObjectMapper objectMapper;

    @Value("#{'${locations.list}'.split('x')}")
    private String[] locationsJsons;

    private List<LocationPlaceDto> locationPlacesList;

    @PostConstruct
    private void postConstruct() {
        locationPlacesList = new ArrayList<>();
        for (String locationPlace : locationsJsons) {
            try {
                JsonNode jsonNode = objectMapper.readTree(locationPlace);

                long id = jsonNode.findValue("id").asLong();
                String name = jsonNode.findValue("n").asText();
                String town = jsonNode.findValue("t").asText();

                locationPlacesList.add(LocationPlaceDto.builder().id(id).name(name).town(town).build());

            } catch (JsonProcessingException e) {
                log.error("Error during json locationPlace parsing: \n" + locationPlace, e);
            }

        }
    }

    public List<LocationPlaceDto> get() {
        return locationPlacesList;
    }

    public Optional<LocationPlaceDto> getById(long id) {
        return locationPlacesList.stream().filter(loc -> loc.getId() == id).findFirst();
    }

    public Optional<LocationPlaceDto> getByTown(String town) {
        return locationPlacesList.stream().filter(loc -> loc.getTown().equals(town)).findFirst();
    }
}
