package pl.adamsiedlecki.OTM.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.dataFetcher.DataFetcher;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.schedule.tools.ScheduleTools;

import java.util.List;

@Component
@Scope("singleton")
public class ScheduleHourly {

    private final Logger log = LoggerFactory.getLogger(ScheduleHourly.class);
    private final DataFetcher dataFetcher;
    private final ScheduleTools scheduleTools;

    @Autowired
    public ScheduleHourly(DataFetcher dataFetcher, ScheduleTools scheduleTools) {
        this.dataFetcher = dataFetcher;
        this.scheduleTools = scheduleTools;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkTemperaturesHourly() {
        log.info("SCHEDULE 0 0 * * * * RUNNING");
        List<TemperatureData> data = dataFetcher.fetch();
        scheduleTools.sendPostOrComment(data);
    }
}