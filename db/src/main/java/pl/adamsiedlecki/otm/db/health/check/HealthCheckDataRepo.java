package pl.adamsiedlecki.otm.db.health.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckDataRepo extends JpaRepository<HealthCheckData, Long> {


    @Query("SELECT d FROM HealthCheckData d WHERE d.date=:date")
    Optional<List<HealthCheckData>> getLastDataByDate(LocalDateTime date);

    Optional<HealthCheckData> findFirstByOrderByDateDesc();

    @Query("SELECT d FROM HealthCheckData d WHERE date BETWEEN :start AND :end")
    List<HealthCheckData> findAllBetween(LocalDateTime start, LocalDateTime end);
}
