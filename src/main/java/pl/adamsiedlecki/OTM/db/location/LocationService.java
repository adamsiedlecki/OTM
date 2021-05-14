package pl.adamsiedlecki.OTM.db.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepo locationRepo;

    @Autowired
    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public Optional<Location> getLocationByCords(float latitude, float longitude) {
        return locationRepo.findByLatitudeAndLongitude(latitude, longitude);
    }

    public Location getOrSave(float latitude, float longitude) {
        Optional<Location> opLoc = getLocationByCords(latitude, longitude);
        return opLoc.orElseGet(() -> locationRepo.save(new Location(latitude, longitude)));
    }
}
