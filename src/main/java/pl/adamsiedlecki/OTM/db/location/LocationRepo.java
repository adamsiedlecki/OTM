package pl.adamsiedlecki.OTM.db.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long> {

    @Query("SELECT l FROM Location l WHERE l.latitude=?1 AND l.longitude=?2 ")
    List<Location> find(String latitude, String longitude);
}
