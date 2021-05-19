package pl.adamsiedlecki.OTM.db.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepo extends JpaRepository<Statitics, Long> {

    Optional<Statitics> findBysKey(String sKey);
}
