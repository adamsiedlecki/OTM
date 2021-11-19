package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.externalServices.facebook.FacebookManager;
import pl.adamsiedlecki.otm.schedule.tools.ScheduleTools;
import pl.adamsiedlecki.otm.tools.charts.OvernightChartCreator;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartTitle;
import pl.adamsiedlecki.otm.tools.net.Ping;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Scope("singleton")
@RequiredArgsConstructor
public class ScheduleOvernightChart {

    private final Logger log = LoggerFactory.getLogger(ScheduleOvernightChart.class);
    private final TemperatureDataService temperatureDataService;
    private final FacebookManager facebookManager;
    private final ScheduleTools scheduleTools;
    private final OtmConfigProperties config;
    private final OvernightChartCreator chartCreator;
    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private final Ping ping;
    private static final String ADDRESS = "facebook.com";
    private static final short PORT = 443;

    @Scheduled(cron = "0 31 6 * * *")
    public void createAndPostChart() {
        log.info("SCHEDULE 0 31 6 * * * RUNNING");

        List<TemperatureData> lastXHours = temperatureDataService.findAllLastXHours(9);
        if (lastXHours.size() != 0) {
            log.info("there is enough data to build overnight chart");
            boolean isBelowZero = scheduleTools.getBelowZero(lastXHours);

            File chart = chartCreator.createChart(lastXHours, config.getDefaultChartWidth(), config.getDefaultChartHeight(), ChartTitle.DEFAULT.get());
            postChartOnlineStrategy(chart, isBelowZero, LocalDateTime.now());
        } else {
            log.info("there is NOT enough data to build overnight chart");
        }

    }

    // complicated strategy in case of no internet access for some time
    private void postChartOnlineStrategy(File chart, boolean isBelowZero, LocalDateTime generationTime) {
        for (int i = 0; i < 12; i++) {
            try {
                if (ping.isReachable(ADDRESS, PORT)) {
                    log.info(ADDRESS + " with port:" + PORT + " is reachable; attempt number: " + i);
                    postChart(chart, isBelowZero, generationTime);
                    break;
                } else {
                    log.info(ADDRESS + " with port:" + PORT + " is NOT reachable; attempt number: " + i);
                    Thread.sleep(TEN_MINUTES);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void postChart(File chart, boolean isBelowZero, LocalDateTime generationTime) {
        facebookManager.postChart(chart, scheduleTools.getEmoji(isBelowZero)
                + "Ostatnia noc \n [ wygenerowano: "
                + TextFormatters.getPrettyDateTime(generationTime)
                + ", \n opublikowano: "
                + TextFormatters.getPrettyDateTime(LocalDateTime.now())
                + " ]");
        log.info("overnight chart posted on facebook");
    }
}
