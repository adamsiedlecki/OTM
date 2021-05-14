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

    public Optional<Location> getLocationByCords(float longitude, float latitude) {
        return locationRepo.findOneByLongitudeAndLatitude(longitude, latitude);
    }

    public Location save(Location location) {
        Optional<Location> opLoc = locationRepo.findOneByLongitudeAndLatitude(location.getLongitude(), location.getLatitude());
        return opLoc.orElseGet(() -> locationRepo.save(location));
    }
}
