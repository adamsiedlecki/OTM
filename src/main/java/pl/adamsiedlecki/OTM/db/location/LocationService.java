package pl.adamsiedlecki.OTM.db.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepo locationRepo;

    @Autowired
    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public Optional<Location> getLocationByCords(String latitude, String longitude) {
        List<Location> locations = locationRepo.find(latitude, longitude);
        System.out.println("gOT LIST OF LOCATIONS: " + locations);
        if (locations.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(locations.get(0));
        }
    }

    public Location getOrSave(String latitude, String longitude) {
        //Optional<Location> opLoc = getLocationByCords(latitude, longitude);
        List<Location> all = locationRepo.findAll();
        System.out.println("ALL: " + all);
        System.out.println("SEARCH FOR : " + latitude + "  " + longitude);
        List<Location> list = all.stream().filter(e -> e.getLatitude().equals(latitude) && e.getLongitude().equals(longitude)).collect(Collectors.toList());
        Optional<Location> opLoc;
        if (list.isEmpty()) {
            opLoc = Optional.empty();
        } else {
            opLoc = Optional.of(list.get(0));
        }

        System.out.println("location count: " + locationRepo.count());
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
