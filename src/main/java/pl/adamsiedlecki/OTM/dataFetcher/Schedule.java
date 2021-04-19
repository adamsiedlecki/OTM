package pl.adamsiedlecki.OTM.dataFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.messengerRecipient.MessengerRecipient;
import pl.adamsiedlecki.OTM.db.messengerRecipient.MessengerRecipientService;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.messenger.Messenger;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
public class Schedule {

    private final DataFetcher dataFetcher;
    private final TemperatureDataService temperatureDataService;
    private final Environment env;
    private final MessengerRecipientService messengerRecipientService;

    @Autowired
    public Schedule(DataFetcher dataFetcher, TemperatureDataService temperatureDataService, Environment env, MessengerRecipientService messengerRecipientService) {
        this.dataFetcher = dataFetcher;
        this.temperatureDataService = temperatureDataService;
        this.env = env;
        this.messengerRecipientService = messengerRecipientService;
    }

    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperatures() {
        System.out.println("SCHEDULE 0 30 22,23,0,1,2,3,4,5,6,7 RUNNING");
        dataFetcher.fetch();
    }

    @Scheduled(cron = "0 59 * * * *")
    public void checkTemperaturesHourly(){
        System.out.println("SCHEDULE 0 59 * * * * RUNNING");
        dataFetcher.fetch();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void createGraphs(){
        System.out.println("SCHEDULE 0 0 8 RUNNING");
        ChartCreator chartCreator = new ChartCreator();

        Optional<List<TemperatureData>> allLast12Hours = temperatureDataService.findAllLastXHours(12);
        if(allLast12Hours.isPresent()) {
            File chart = chartCreator.createOvernightChart(allLast12Hours.get());
            Messenger messenger = new Messenger();
            List<MessengerRecipient> all = messengerRecipientService.findAll();

            for (MessengerRecipient rec : all) {
                messenger.sendOverNightChartToUser(chart, rec.getRecipientId(), env.getProperty("fb.page.access.token"), env.getProperty("fb.api"));
            }
        }

    }
}
