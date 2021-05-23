package pl.adamsiedlecki.OTM.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;
import pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager;
import pl.adamsiedlecki.OTM.openWeather.OpenWeatherFetcher;
import pl.adamsiedlecki.OTM.openWeather.OpenWeatherTools;
import pl.adamsiedlecki.OTM.openWeather.pojo.openWeatherTwoDaysAhead.Hourly;
import pl.adamsiedlecki.OTM.openWeather.pojo.openWeatherTwoDaysAhead.OpenWeatherTwoDaysAheadPojo;
import pl.adamsiedlecki.OTM.schedule.tools.ScheduleTools;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.ForecastChartCreator;
import pl.adamsiedlecki.OTM.tools.charts.tools.ChartTitle;
import pl.adamsiedlecki.OTM.tools.files.MyFilesystem;
import pl.adamsiedlecki.OTM.tools.text.TextFormatters;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class ScheduleWeatherForecast {

    private final Logger log = LoggerFactory.getLogger(ScheduleWeatherForecast.class);
    private final TempDataAliasService aliasService;
    private final OpenWeatherFetcher openWeatherFetcher;
    private final FacebookManager facebookManager;
    private final ScheduleTools scheduleTools;
    private final OpenWeatherTools openWeatherTools = new OpenWeatherTools();

    @Autowired
    public ScheduleWeatherForecast(TempDataAliasService aliasService, OpenWeatherFetcher openWeatherFetcher, FacebookManager facebookManager, ScheduleTools scheduleTools) {
        this.aliasService = aliasService;
        this.openWeatherFetcher = openWeatherFetcher;
        this.facebookManager = facebookManager;
        this.scheduleTools = scheduleTools;
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void publishOpenWeatherPredictions() {
        log.info("SCHEDULE 0 0 20 RUNNING");

        Optional<OpenWeatherTwoDaysAheadPojo> twoDaysAhead = getTwoDaysAheadBasedOnStationCoordinates("t1");
        if (twoDaysAhead.isPresent()) {
            List<Hourly> hourly = twoDaysAhead.get().getHourly();
            hourly = openWeatherTools.getOnlyXHoursForward(hourly, 11);

            List<TemperatureData> predictionTdList = hourly.stream().map(h -> {
                TemperatureData td = new TemperatureData();
                td.setTransmitterName("prognoza Open Weather");
                td.setDate(LocalDateTime.ofEpochSecond(h.getDt(), 0, ZoneOffset.ofHours(2)));
                td.setTemperatureCelsius(new BigDecimal("" + h.getTemp()));
                return td;
            }).collect(Collectors.toList());
            boolean isBelowZero = scheduleTools.getBelowZero(predictionTdList);

            ChartCreator chartCreator = new ForecastChartCreator();
            File chart = chartCreator.createChart(predictionTdList, 1200, 628, ChartTitle.OPEN_WEATHER_FORECAST.get());
            if (MyFilesystem.fileExistsAndIsNoOlderThanXSeconds(chart, 10)) {
                facebookManager.postChart(chart,
                        scheduleTools.getEmoji(isBelowZero)
                                + " Prognoza z Open Weather na najbliższy czas: \n [ wygenerowano "
                                + TextFormatters.getPrettyDateTime(LocalDateTime.now()) + " ]");
            } else {
                log.error("prediction chart does not exist or is older than 10 seconds");
            }
        } else {
            log.error("OpenWeatherTwoDaysAheadPojo is not present");
        }

    }

    private Optional<OpenWeatherTwoDaysAheadPojo> getTwoDaysAheadBasedOnStationCoordinates(String stationName) {
        Optional<TempDataAlias> t1 = aliasService.findByOriginalName(stationName);
        if (t1.isPresent()) {
            return openWeatherFetcher.getTwoDaysAhead(t1.get().getLatitude(), t1.get().getLongitude());

        } else {
            log.error("alias t1 now found");
        }
        return Optional.empty();
    }

}
