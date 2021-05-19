package pl.adamsiedlecki.OTM.db.statistics;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatisticsService {

    private final StatisticsRepo statisticsRepo;

    public StatisticsService(StatisticsRepo statisticsRepo) {
        this.statisticsRepo = statisticsRepo;
    }

    public void updateStatistics(String key, Long value) {
        statisticsRepo.findBysKey(key).ifPresentOrElse(stat -> stat.setsValue(value), () -> {
            Statitics stat = new Statitics(key, value);
            statisticsRepo.saveAndFlush(stat);
        });
    }

    public Optional<Statitics> get(String key) {
        return statisticsRepo.findBysKey(key);
    }

    private void increment(String key) {
        statisticsRepo.findBysKey(key).ifPresentOrElse(stat -> {
            stat.setsValue(stat.getsValue() + 1);
            statisticsRepo.saveAndFlush(stat);
        }, () -> {
            Statitics stat = new Statitics(key, 1L);
            statisticsRepo.saveAndFlush(stat);
        });
    }

    public void increment(ExistingStatistics requestsToEspCount) {
        String key = requestsToEspCount.getKey();
        increment(key);
    }

    public Optional<Statitics> get(ExistingStatistics existing) {
        return statisticsRepo.findBysKey(existing.getKey());
    }

    public List<Statitics> findAll() {
        return statisticsRepo.findAll();
    }
}
