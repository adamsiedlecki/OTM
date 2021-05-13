package pl.adamsiedlecki.OTM.dataFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.facebook.FacebookManager;
import pl.adamsiedlecki.OTM.tools.TextFormatters;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@Scope("singleton")
public class Schedule {

    private final DataFetcher dataFetcher;
    private final TemperatureDataService temperatureDataService;
    private final FacebookManager facebookManager;
    private LocalDateTime lastTextPostTime;
    private String lastTextPostId;
    private final Logger log = LoggerFactory.getLogger(Schedule.class);

    @Autowired
    public Schedule(DataFetcher dataFetcher, TemperatureDataService temperatureDataService, FacebookManager facebookManager) {
        this.dataFetcher = dataFetcher;
        this.temperatureDataService = temperatureDataService;
        this.facebookManager = facebookManager;
    }

    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperatures() {
        log.info("SCHEDULE 0 30 22,23,0,1,2,3,4,5,6,7 RUNNING");
        List<TemperatureData> data = dataFetcher.fetch();
        sendPostOrComment(data);

    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkTemperaturesHourly() {
        log.info("SCHEDULE 0 0 * * * * RUNNING");
        List<TemperatureData> data = dataFetcher.fetch();
        sendPostOrComment(data);
    }

    @Scheduled(cron = "0 31 7 * * *")
    public void createChart() {
        log.info("SCHEDULE 0 0 8 RUNNING");
        ChartCreator chartCreator = new ChartCreator();

        Optional<List<TemperatureData>> allLast12Hours = temperatureDataService.findAllLastXHours(10);
        if (allLast12Hours.isPresent()) {
            File chart = chartCreator.createOvernightChart(allLast12Hours.get(), 1200, 628);
            if (chart.exists() && (System.currentTimeMillis() - chart.lastModified()) < 10000) {
                facebookManager.postChart(chart, "Ostatnia noc \n [ wygenerowano " + TextFormatters.getPrettyDateTime(LocalDateTime.now()) + " ]");
            }
        }

    }

    private void sendPostOrComment(List<TemperatureData> data) {
        if (data.size() == 0) {
            return;
        }
        boolean isBelowZero = false;
        for (TemperatureData td : data) {
            if (td.getTemperatureCelsius().compareTo(BigDecimal.ZERO) < 0) {
                isBelowZero = true;
                break;
            }
        }
        if (isBelowZero) {
            data.sort(Comparator.comparing(TemperatureData::getTemperatureCelsius));
            log.info("TEMPERATURES BELOW ZERO FOUND!");
            StringBuilder sb = new StringBuilder();
            sb.append("Odnotowano temperaturę < 0  \n  [ ");
            sb.append(TextFormatters.getPrettyDateTime(data.get(0).getDate()));
            sb.append(" ]\n ");
            for (TemperatureData td : data) {
                sb.append(td.getTransmitterNameAndTemperature());
                sb.append(" \n ");
            }
            // in case of creating new post
            if (lastTextPostTime == null || LocalDateTime.now().isAfter(lastTextPostTime.plusHours(12))) {
                lastTextPostTime = LocalDateTime.now();
                lastTextPostId = facebookManager.postMessage(sb.toString());
                log.info("Text Post id: " + lastTextPostId);
                // in case of commenting existing post
            } else {
                String commentId = facebookManager.postComment(lastTextPostId, sb.toString());
                log.info("Comment id: " + commentId);
            }
        }
    }
}
