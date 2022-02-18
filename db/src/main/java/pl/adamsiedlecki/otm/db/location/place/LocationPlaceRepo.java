package pl.adamsiedlecki.otm.db.location.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPlaceRepo extends JpaRepository<LocationPlace, Long> {
}
