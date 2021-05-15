package pl.adamsiedlecki.OTM.db.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepo locationRepo;

    @Autowired
    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public Optional<Location> getLocationByCords(String latitude, String longitude) {
        List<Location> locations = locationRepo.findByLatitudeAndLongitude(latitude, longitude);
        System.out.println("gOT LIST OF LOCATIONS: " + locations);
        if (locations.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(locations.get(0));
        }
    }

    public Location getOrSave(String latitude, String longitude) {
        Optional<Location> opLoc = getLocationByCords(latitude, longitude);

        if (opLoc.isPresent()) {
            System.out.println("GOT LOCATION BY lat and lng: " + opLoc.get());
            return opLoc.get();
        } else {
            System.out.println(" location not found, creating new");
            Location loc = new Location(latitude, longitude);
            locationRepo.saveAndFlush(loc);
            return loc;
        }
    }

    public long count() {
        return locationRepo.count();
    }
}
