package pl.adamsiedlecki.otm.station.info.locations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2DevicesInfo;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "location-places")
public class LocationPlacesInfo {

    private final Gen2DevicesInfo gen1DevicesInfo;
    private final Gen2DevicesInfo gen2DevicesInfo;

    private List<LocationPlaceDto> places;

    public List<LocationPlaceDto> getPlaces() {
        return places;
    }

    public void setPlaces(List<LocationPlaceDto> places) {
        this.places = places;
    }

    public Optional<LocationPlaceDto> getById(long id) {
        return places.stream().filter(loc -> loc.getId() == id).findFirst();
    }

    public Optional<LocationPlaceDto> getByTown(String town) {
        return places.stream().filter(loc -> loc.getTown().equals(town)).findFirst();
    }
}
