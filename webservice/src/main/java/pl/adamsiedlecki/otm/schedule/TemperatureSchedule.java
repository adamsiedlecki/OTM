package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.data.fetcher.TemperatureDataFetcher;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.schedule.tools.ScheduleTools;

import java.util.List;

@Component
@Scope("singleton")
@RequiredArgsConstructor
@Slf4j
public class TemperatureSchedule {

    private final TemperatureDataFetcher dataFetcher;
    private final ScheduleTools scheduleTools;

    @Scheduled(cron = "0 0 * * * *")
    public void checkTemperaturesHourly() {
        log.info("SCHEDULE 0 0 * * * * RUNNING");
        List<TemperatureData> data = dataFetcher.fetchAndSaveAllTemperatures();
        scheduleTools.sendPostOrComment(data);
    }

    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperaturesHalfHourAtNight() {
        log.info("SCHEDULE 0 30 22,23,0,1,2,3,4,5,6,7 RUNNING");
        List<TemperatureData> data = dataFetcher.fetchAndSaveAllTemperatures();
        scheduleTools.sendPostOrComment(data);

    }
}
