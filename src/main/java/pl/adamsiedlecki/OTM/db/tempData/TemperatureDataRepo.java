package pl.adamsiedlecki.OTM.db.tempData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemperatureDataRepo extends JpaRepository<TemperatureData, Long> {


    @Query("SELECT td FROM TemperatureData td WHERE td.date=:date")
    Optional<List<TemperatureData>> getLastTemperaturesByDate(LocalDateTime date);

    Optional<TemperatureData> findFirstByOrderByDateDesc();

    @Query("SELECT td FROM TemperatureData td WHERE date BETWEEN :start AND :end")
    Optional<List<TemperatureData>> findAllBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(td.id) FROM TemperatureData td WHERE td.temperatureCelsius<0")
    long countBelowTemperature(BigDecimal temperature);
}
