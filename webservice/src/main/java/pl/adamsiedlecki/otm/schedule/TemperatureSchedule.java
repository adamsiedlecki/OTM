package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.schedule.jobs.TemperatureJob;

/**
 * This class is responsible for periodic requests to stations in order to get temperature, store it
 * and if certain conditions are met, send notifications in form of facebook posts and comments.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TemperatureSchedule {

    private final TemperatureJob temperatureJob;

    /**
     * Checks temperatures at every station every hour
     */
    @Scheduled(cron = "0 0 * * * *")
    public void checkTemperaturesHourly() {
        temperatureJob.fetchSaveAndConditionallyPostOnFacebook();
    }

    /**
     * Checks temperatures at every station every half hour at night (22PM - 7AM),
     * because I would like to have more detailed data about night frosts.
     */
    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperaturesHalfHourAtNight() {
        temperatureJob.fetchSaveAndConditionallyPostOnFacebook();
    }
}
