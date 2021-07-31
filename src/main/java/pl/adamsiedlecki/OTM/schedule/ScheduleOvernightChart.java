package pl.adamsiedlecki.OTM.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager;
import pl.adamsiedlecki.OTM.schedule.tools.ScheduleTools;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.OvernightChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.tools.ChartTitle;
import pl.adamsiedlecki.OTM.tools.net.Ping;
import pl.adamsiedlecki.OTM.tools.text.TextFormatters;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Scope("singleton")
public class ScheduleOvernightChart {

    private final Logger log = LoggerFactory.getLogger(ScheduleOvernightChart.class);
    private final TemperatureDataService temperatureDataService;
    private final FacebookManager facebookManager;
    private final ScheduleTools scheduleTools;
    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private final Ping ping;
    private final Environment env;

    @Autowired
    public ScheduleOvernightChart(TemperatureDataService temperatureDataService, FacebookManager facebookManager, ScheduleTools scheduleTools, Ping ping, Environment env) {
        this.temperatureDataService = temperatureDataService;
        this.facebookManager = facebookManager;
        this.scheduleTools = scheduleTools;
        this.ping = ping;
        this.env = env;
    }

    @Scheduled(cron = "0 31 6 * * *")
    public void createChart() {
        log.info("SCHEDULE 0 31 6 * * * RUNNING");

        List<TemperatureData> lastXHours = temperatureDataService.findAllLastXHours(9);
        if (lastXHours.size() != 0) {
            boolean isBelowZero = scheduleTools.getBelowZero(lastXHours);

            ChartCreator chartCreator = new OvernightChartCreator();
            File chart = chartCreator.createChart(lastXHours, 1200, 628, ChartTitle.DEFAULT.get());
            postChartOnlineStrategy(chart, isBelowZero);
        }

    }

    // complicated strategy in case of no internet access for some time
    private void postChartOnlineStrategy(File chart, boolean isBelowZero) {
        for (int i = 0; i < 12; i++) {
            try {
                if (ping.isReachable("facebook.com")) {
                    postChart(chart, isBelowZero);
                    break;
                }
                Thread.sleep(TEN_MINUTES);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void postChart(File chart, boolean isBelowZero) {
        facebookManager.postChart(chart, scheduleTools.getEmoji(isBelowZero)
                + "Ostatnia noc \n [ wygenerowano "
                + TextFormatters.getPrettyDateTime(LocalDateTime.now()) + " ]");
    }
}
