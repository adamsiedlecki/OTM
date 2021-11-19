package pl.adamsiedlecki.otm.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.dataFetcher.DataFetcher;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.schedule.tools.ScheduleTools;

import java.util.List;

@Component
@Scope("singleton")
public class ScheduleHalfHourNightly {

    private final Logger log = LoggerFactory.getLogger(ScheduleHalfHourNightly.class);
    private final DataFetcher dataFetcher;
    private final ScheduleTools scheduleTools;

    @Autowired
    public ScheduleHalfHourNightly(DataFetcher dataFetcher, ScheduleTools scheduleTools) {
        this.dataFetcher = dataFetcher;
        this.scheduleTools = scheduleTools;
    }

    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperatures() {
        log.info("SCHEDULE 0 30 22,23,0,1,2,3,4,5,6,7 RUNNING");
        List<TemperatureData> data = dataFetcher.fetch();
        scheduleTools.sendPostOrComment(data);

    }
}
