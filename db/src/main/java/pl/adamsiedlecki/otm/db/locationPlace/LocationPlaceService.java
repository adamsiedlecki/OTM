package pl.adamsiedlecki.otm.db.locationPlace;

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

    public LocationPlace updateOrSave(Optional<LocationPlaceDto> locPlaceDto) {
        if (locPlaceDto.isEmpty()) {
            log.error("Optional locPlaceDto is empty! ");
            return null;
        }
        Optional<LocationPlace> opLoc = locationPlaceRepo.findById(locPlaceDto.get().getId());

        if (opLoc.isPresent()) {
            return opLoc.get();
        } else {
            LocationPlace loc = new LocationPlace();
            loc.setId(locPlaceDto.get().getId());
            loc.setName(locPlaceDto.get().getName());
            loc.setTown(locPlaceDto.get().getTown());
            locationPlaceRepo.saveAndFlush(loc);
            return loc;
        }
    }

}
