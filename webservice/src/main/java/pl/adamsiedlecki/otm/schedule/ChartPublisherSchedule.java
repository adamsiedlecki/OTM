package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.services.ChartSummarySenderService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChartPublisherSchedule {

    private static final int NUMBER_OF_HOURS_FOR_OVERNIGHT_CHART = 9;
    private static final int NUMBER_OF_HOURS_FOR_WEEKLY_CHART = 7 * 24;

    private final ChartSummarySenderService chartSummarySenderService;

    @Scheduled(cron = "0 31 6 * * *")
    public void sendOvernightChart() {
        log.info("Daily overnight temperature publisher is running");
        chartSummarySenderService.send(NUMBER_OF_HOURS_FOR_OVERNIGHT_CHART, "Wykres temperatury z ostatniej nocy");
    }

    @Scheduled(cron = "0 0 10 * * SUN")
    public void sendWeeklyChart() {
        log.info("Weekly temperature publisher is running");
        chartSummarySenderService.send(NUMBER_OF_HOURS_FOR_WEEKLY_CHART, "Wykres temperatury z ostatniego tygodnia");
    }
}
