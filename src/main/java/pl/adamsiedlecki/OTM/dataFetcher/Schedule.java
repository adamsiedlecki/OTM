package pl.adamsiedlecki.OTM.dataFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.ChartCreator;

import java.util.List;
import java.util.Optional;

@Component
public class Schedule {

    private final DataFetcher dataFetcher;
    private final TemperatureDataService temperatureDataService;

    @Autowired
    public Schedule(DataFetcher dataFetcher, TemperatureDataService temperatureDataService) {
        this.dataFetcher = dataFetcher;
        this.temperatureDataService = temperatureDataService;
    }

    @Scheduled(cron = " 0 30 22,23,0,1,2,3,4,5,6,7 * * *")
    public void checkTemperatures() {
        System.out.println("SCHEDULE 0 30 22,23,0,1,2,3,4,5,6,7 RUNNING");
        dataFetcher.fetch();
    }

    @Scheduled(cron="@hourly")
    public void checkTemperaturesHourly(){
        System.out.println("SCHEDULE @hourly RUNNING");
        dataFetcher.fetch();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void createGraphs(){
        System.out.println("SCHEDULE 0 0 8 RUNNING");
        ChartCreator chartCreator = new ChartCreator();

        Optional<List<TemperatureData>> allLast12Hours = temperatureDataService.findAllLastXHours(12);
        if(allLast12Hours.isPresent()){
            chartCreator.createOvernightChart(allLast12Hours.get());
        }

    }
}
