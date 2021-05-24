package pl.adamsiedlecki.OTM.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager;
import pl.adamsiedlecki.OTM.schedule.tools.ScheduleTools;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.OvernightChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.tools.ChartTitle;
import pl.adamsiedlecki.OTM.tools.files.MyFilesystem;
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

    @Autowired
    public ScheduleOvernightChart(TemperatureDataService temperatureDataService, FacebookManager facebookManager, ScheduleTools scheduleTools) {
        this.temperatureDataService = temperatureDataService;
        this.facebookManager = facebookManager;
        this.scheduleTools = scheduleTools;
    }

    @Scheduled(cron = "0 31 6 * * *")
    public void createChart() {
        log.info("SCHEDULE 0 31 6 * * * RUNNING");

        List<TemperatureData> lastXHours = temperatureDataService.findAllLastXHours(9);
        if (lastXHours.size() != 0) {
            boolean isBelowZero = scheduleTools.getBelowZero(lastXHours);

            ChartCreator chartCreator = new OvernightChartCreator();
            File chart = chartCreator.createChart(lastXHours, 1200, 628, ChartTitle.DEFAULT.get());
            if (MyFilesystem.fileExistsAndIsNoOlderThanXSeconds(chart, 10)) {
                facebookManager.postChart(chart, scheduleTools.getEmoji(isBelowZero)
                        + "Ostatnia noc \n [ wygenerowano "
                        + TextFormatters.getPrettyDateTime(LocalDateTime.now()) + " ]");
            }
        }

    }
}
