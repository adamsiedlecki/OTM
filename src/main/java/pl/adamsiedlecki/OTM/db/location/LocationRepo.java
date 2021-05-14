package pl.adamsiedlecki.OTM.db.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {

    Optional<Location> findOneByLatitudeAndLongitude(float longitude, float latitude);
}
