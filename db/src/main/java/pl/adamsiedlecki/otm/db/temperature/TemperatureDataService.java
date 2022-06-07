package pl.adamsiedlecki.otm.db.temperature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class TemperatureDataService {

    private final TemperatureDataRepo temperatureDataRepo;

    @Autowired
    public TemperatureDataService(TemperatureDataRepo temperatureDataRepo) {
        this.temperatureDataRepo = temperatureDataRepo;
    }

    public Optional<List<TemperatureData>> getLastTemperatures() {
        Optional<TemperatureData> firstByOrderByDateDesc = temperatureDataRepo.findFirstByOrderByDateDesc();
        if (firstByOrderByDateDesc.isEmpty()) {
            return Optional.empty();
        }
        return temperatureDataRepo.getLastTemperaturesByDate(firstByOrderByDateDesc.get().getDate());
    }

    public <S extends TemperatureData> List<S> saveAll(Iterable<S> iterable) {
        return temperatureDataRepo.saveAll(iterable);
    }

    public Page<TemperatureData> findAll(Pageable pageable) {
        return temperatureDataRepo.findAll(pageable);
    }

    public List<TemperatureData> findAll() {
        return temperatureDataRepo.findAll();
    }

    public <S extends TemperatureData> S save(S s) {
        return temperatureDataRepo.save(s);
    }

    public Optional<TemperatureData> findById(Long aLong) {
        return temperatureDataRepo.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return temperatureDataRepo.existsById(aLong);
    }

    public long count() {
        return temperatureDataRepo.count();
    }

    public void deleteById(Long aLong) {
        temperatureDataRepo.deleteById(aLong);
    }

    public void delete(TemperatureData temperatureData) {
        temperatureDataRepo.delete(temperatureData);
    }

    public void deleteAll(Iterable<? extends TemperatureData> iterable) {
        temperatureDataRepo.deleteAll(iterable);
    }

    public void deleteAll() {
        temperatureDataRepo.deleteAll();
    }

    public List<TemperatureData> findAllLastXHours(int xHours) {
        return temperatureDataRepo.findAllBetween(LocalDateTime.now().minus(xHours, ChronoUnit.HOURS), LocalDateTime.now());
    }

    public long countBelowTemperature(BigDecimal temperature) {
        return temperatureDataRepo.countBelowTemperature(temperature);
    }
}
