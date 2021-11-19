package pl.adamsiedlecki.otm.db.location;

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

    public Optional<Location> getLocationByCords(String latitude, String longitude) {
        return locationRepo.find(latitude, longitude);
    }

    public Location getOrSave(String latitude, String longitude) {
        Optional<Location> opLoc = getLocationByCords(latitude, longitude);

        if (opLoc.isPresent()) {
            return opLoc.get();
        } else {
            Location loc = new Location(latitude, longitude);
            locationRepo.saveAndFlush(loc);
            return loc;
        }
    }

    public long count() {
        return locationRepo.count();
    }
}
