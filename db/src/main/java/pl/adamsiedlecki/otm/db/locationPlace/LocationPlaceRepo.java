package pl.adamsiedlecki.otm.db.locationPlace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPlaceRepo extends JpaRepository<LocationPlace, Long> {
}
