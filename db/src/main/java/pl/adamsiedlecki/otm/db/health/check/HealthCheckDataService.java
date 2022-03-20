package pl.adamsiedlecki.otm.db.health.check;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class HealthCheckDataService {

    private final HealthCheckDataRepo healthCheckDataRepo;

    public <S extends HealthCheckData> List<S> saveAll(Iterable<S> iterable) {
        return healthCheckDataRepo.saveAll(iterable);
    }

    public Page<HealthCheckData> findAll(Pageable pageable) {
        return healthCheckDataRepo.findAll(pageable);
    }

    public List<HealthCheckData> findAll() {
        return healthCheckDataRepo.findAll();
    }

    public <S extends HealthCheckData> S save(S s) {
        return healthCheckDataRepo.save(s);
    }

    public Optional<HealthCheckData> findById(Long aLong) {
        return healthCheckDataRepo.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return healthCheckDataRepo.existsById(aLong);
    }

    public long count() {
        return healthCheckDataRepo.count();
    }

    public void deleteById(Long aLong) {
        healthCheckDataRepo.deleteById(aLong);
    }

    public void delete(HealthCheckData temperatureData) {
        healthCheckDataRepo.delete(temperatureData);
    }

    public void deleteAll(Iterable<? extends HealthCheckData> iterable) {
        healthCheckDataRepo.deleteAll(iterable);
    }

    public void deleteAll() {
        healthCheckDataRepo.deleteAll();
    }

    public List<HealthCheckData> findAllLastXHours(int xHours) {
        return healthCheckDataRepo.findAllBetween(LocalDateTime.now().minus(xHours, ChronoUnit.HOURS), LocalDateTime.now());
    }

}
