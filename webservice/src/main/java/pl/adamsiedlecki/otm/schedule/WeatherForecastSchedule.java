package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.external.services.facebook.FacebookManager;
import pl.adamsiedlecki.otm.external.services.open.weather.OpenWeatherFetcher;
import pl.adamsiedlecki.otm.external.services.open.weather.OpenWeatherTools;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead.Hourly;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead.OpenWeatherTwoDaysAheadPojo;
import pl.adamsiedlecki.otm.odg.JFreeChartCreator;
import pl.adamsiedlecki.otm.odg.properties.ChartProperties;
import pl.adamsiedlecki.otm.tools.data.ChartDataUtils;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;
import pl.adamsiedlecki.otm.tools.text.Emojis;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;
import pl.adamsiedlecki.otm.utils.Converter;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
@RequiredArgsConstructor
@Slf4j
public class WeatherForecastSchedule {

    private static final int FORECAST_HOURS_FORWARD = 11;
    private static final long MAX_CHART_AGE_IN_SECONDS = 15;

    private final LocationService locationService;
    private final OpenWeatherFetcher openWeatherFetcher;
    private final FacebookManager facebookManager;
    private final OtmConfigProperties config;
    private final MyFilesystem myFilesystem;
    private final JFreeChartCreator chartCreator;
    private final OpenWeatherTools openWeatherTools = new OpenWeatherTools();
    private final Converter converter;

    @Scheduled(cron = "-")
    public void publishOpenWeatherPredictions() {
        log.info("SCHEDULE 0 0 20 RUNNING");

        Optional<OpenWeatherTwoDaysAheadPojo> twoDaysAhead = getTwoDaysAheadBasedOnFirstLocationFromDb();
        if (twoDaysAhead.isPresent()) {
            List<Hourly> hourly = twoDaysAhead.get().getHourly();
            hourly = openWeatherTools.getOnlyXHoursForward(hourly, FORECAST_HOURS_FORWARD);

            List<PresentableOnChart> predictionTdList = hourly.stream().map(h -> {
                TemperatureData td = new TemperatureData();
                td.setTransmitterName("prognoza Open Weather");
                td.setDate(LocalDateTime.ofEpochSecond(h.getDt(), 0, ZoneOffset.ofHours(2)));
                td.setTemperatureCelsius(new BigDecimal("" + h.getTemp()));
                return td;
            }).collect(Collectors.toList());
            boolean isBelowZero = ChartDataUtils.isAnyBelowZero(predictionTdList);

            File chart = chartCreator.createXyChart(predictionTdList.stream().map(converter::convert).collect(Collectors.toList()),
                    config.getDefaultChartWidth(),
                    config.getDefaultChartHeight(),
                    ChartProperties.OPEN_WEATHER_FORECAST.get(),
                    ChartProperties.TEMPERATURE_AXIS_TITLE.get(),
                    ChartProperties.TIME_AXIS_TITLE.get());
            if (myFilesystem.fileExistsAndIsNoOlderThanXSeconds(chart, MAX_CHART_AGE_IN_SECONDS)) {
                facebookManager.postChart(chart,
                        isBelowZero ? Emojis.FROST : Emojis.WARM
                                + " Prognoza z OpenWeather na najbliższy czas: \n [ wygenerowano "
                                + TextFormatters.getPretty(LocalDateTime.now()) + " ]");
            } else {
                log.error("prediction chart does not exist or is older than 10 seconds");
            }
        } else {
            log.error("OpenWeatherTwoDaysAheadPojo is not present");
        }

    }

    private Optional<OpenWeatherTwoDaysAheadPojo> getTwoDaysAheadBasedOnFirstLocationFromDb() {
        Optional<Location> optionalLocation = locationService.findAll().stream().findFirst();
        if (optionalLocation.isPresent()) {
            return openWeatherFetcher.getTwoDaysAhead(optionalLocation.get().getLatitude(), optionalLocation.get().getLongitude());

        } else {
            log.error("there are no locations in database to use for weather forecast");
        }
        return Optional.empty();
    }

}
