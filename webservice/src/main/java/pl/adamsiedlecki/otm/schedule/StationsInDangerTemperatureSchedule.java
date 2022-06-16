package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.data.fetcher.TemperatureDataFetcher;

/**
 * This class is responsible for periodic requests to stations in order to get temperature, store it
 * and if certain conditions are met, send notifications in form of facebook posts and comments.
 *
 * It is especially designed for stations that are probably going to be stolen, to have specific information about last
 * response time.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StationsInDangerTemperatureSchedule {

    private final TemperatureDataFetcher temperatureDataFetcher;

    /**
     * Checks temperatures at every station every ten minutes at night (21PM - 6AM).
     */
    @Scheduled(cron = " 0 10,20,40,50 21,22,23,0,1,2,3,4,5,6 * * *")
    public void checkTemperatures() {
        temperatureDataFetcher.fetchAndSaveTemperaturesFromGen3StationsThatCanBeInDanger();
    }
}
