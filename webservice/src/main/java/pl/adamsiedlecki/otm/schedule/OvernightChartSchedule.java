package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.email.recipients.People;
import pl.adamsiedlecki.otm.email.recipients.SubscribersInfo;
import pl.adamsiedlecki.otm.odg.JFreeChartCreator;
import pl.adamsiedlecki.otm.odg.properties.ChartProperties;
import pl.adamsiedlecki.otm.orchout.FacebookPublisherService;
import pl.adamsiedlecki.otm.services.OtmEmailSenderService;
import pl.adamsiedlecki.otm.tools.data.ChartDataUtils;
import pl.adamsiedlecki.otm.tools.data.OtmStatistics;
import pl.adamsiedlecki.otm.tools.text.Emojis;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;
import pl.adamsiedlecki.otm.utils.Converter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OvernightChartSchedule {

    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private static final int NUMBER_OF_HOURS_FOR_OVERNIGHT_CHART = 9;
    private static final int MAX_POST_ON_FACEBOOK_ATTEMPTS = 12;

    private final TemperatureDataService temperatureDataService;
    private final FacebookPublisherService facebookPublisherService;
    private final OtmConfigProperties config;
    private final JFreeChartCreator chartCreator;
    private final OtmEmailSenderService emailSenderService;
    private final SubscribersInfo recipientsInfo;
    private final Converter converter;

    @Scheduled(cron = "0 31 6 * * *")
    public void createAndPostChart() {
        log.info("SCHEDULE 0 31 6 * * * RUNNING");

        List<TemperatureData> lastXHours = temperatureDataService.findAllLastXHours(NUMBER_OF_HOURS_FOR_OVERNIGHT_CHART);
        if (!lastXHours.isEmpty()) {
            log.info("There is enough data to build overnight chart");
            lastXHours.sort(Comparator.comparing(TemperatureData::getDate));
            boolean isBelowZero = ChartDataUtils.isAnyBelowZero(lastXHours);
            String timePeriod = TextFormatters.getPretty(lastXHours.get(0).getDate()) + "  -  " + TextFormatters.getPretty(lastXHours.get(lastXHours.size() - 1).getDate());

            File chart = chartCreator.createXyChart(lastXHours.stream().map(converter::convert).collect(Collectors.toList()),
                    config.getDefaultChartWidth(),
                    config.getDefaultChartHeight(),
                    ChartProperties.TEMPERATURE_DEFAULT.get() + " " + timePeriod,
                    ChartProperties.TEMPERATURE_AXIS_TITLE.get(),
                    ChartProperties.TIME_AXIS_TITLE.get());

            try {
                sendViaEmail(timePeriod, chart, lastXHours);
            } catch (Exception e) {
                log.error("Error while sending email", e);
            }

            try {
                postChartFacebookStrategy(chart, isBelowZero, LocalDateTime.now());
            } catch (Exception e) {
                log.error("Error while posting on facebook", e);
            }
        } else {
            log.info("There is NOT enough data to build overnight chart");
        }

    }

    private void sendViaEmail(final String timePeriod, final File chart, final List<TemperatureData> td) {
        List<String> emailRecipients = recipientsInfo.getPeople().stream().map(People::getEmail).collect(Collectors.toList());
        emailSenderService.sendCharts(emailRecipients, "Wykres temperatury z ostatniej nocy", "Raport OTM z okresu: \n" + timePeriod, List.of(chart), new OtmStatistics(td));
    }

    // strategy in case of no internet access for some time
    private void postChartFacebookStrategy(final File chart, final boolean isBelowZero, final LocalDateTime generationTime) {
        for (int i = 0; i < MAX_POST_ON_FACEBOOK_ATTEMPTS; i++) {
            try {
                postChart(chart, isBelowZero, generationTime);
                break;
            } catch (Exception ex) {
                log.error("Posting chart on Facebook failed; attempt number: {}", i);
                log.error(ex.getMessage());
                sleep(TEN_MINUTES);
            }
        }
    }

    private void postChart(final File chart, final boolean isBelowZero, final LocalDateTime generationTime) {
        facebookPublisherService.post(chart, isBelowZero ? Emojis.FROST : Emojis.WARM
                + "Ostatnia noc \n [ wygenerowano: "
                + TextFormatters.getPretty(generationTime)
                + ", \n opublikowano: "
                + TextFormatters.getPretty(LocalDateTime.now())
                + " ]");
        log.info("overnight chart posted on facebook");
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
