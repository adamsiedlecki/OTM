package pl.adamsiedlecki.otm.db.location.place;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.dto.LocationPlaceDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationPlaceService {

    private final LocationPlaceRepo locationPlaceRepo;

    public LocationPlace updateOrSave(LocationPlaceDto locPlaceDto) {
        if (locPlaceDto == null) {
            log.error("locPlaceDto is null! ");
            return null;
        }
        Optional<LocationPlace> opLoc = locationPlaceRepo.findById(locPlaceDto.getId());

        if (opLoc.isPresent()) {
            return opLoc.get();
        } else {
            LocationPlace loc = new LocationPlace();
            loc.setId(locPlaceDto.getId());
            loc.setName(locPlaceDto.getName());
            loc.setTown(locPlaceDto.getTown());
            locationPlaceRepo.saveAndFlush(loc);
            return loc;
        }
    }

}
