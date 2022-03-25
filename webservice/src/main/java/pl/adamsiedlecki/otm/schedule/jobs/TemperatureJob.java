package pl.adamsiedlecki.otm.schedule.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.data.fetcher.TemperatureDataFetcher;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.external.services.facebook.FacebookManager;
import pl.adamsiedlecki.otm.tools.data.GenericsConverter;
import pl.adamsiedlecki.otm.tools.data.TemperatureDataUtils;
import pl.adamsiedlecki.otm.tools.text.Emojis;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TemperatureJob {

    private static final long MAX_POST_USAGE_HOURS = 12;
    private final TemperatureDataFetcher dataFetcher;
    private final FacebookManager facebookManager;
    private LocalDateTime lastTextPostTime;
    private String lastTextPostId;

    public void fetchSaveAndConditionallyPostOnFacebook() {
        List<TemperatureData> data = dataFetcher.fetchAndSaveAllTemperatures();
        sendFacebookPostOrComment(data);
    }

    /**
     * Only one facebook post is created for 12 hours period.
     * If more than one reading has temperature below zero celsius, information about it will be posted as
     * a comment below the facebook post, so there won't be spam on the wall.
     *
     * @param data list of readings from stations
     */
    private void sendFacebookPostOrComment(final List<TemperatureData> data) {
        if (data.isEmpty()) {
            return;
        }
        boolean isBelowZero = TemperatureDataUtils.isAnyBelowZero(GenericsConverter.convert(data));

        if (isBelowZero) {
            log.info("Temperatures below zero found");
            StringBuilder sb = new StringBuilder();
            sb.append(Emojis.FROST).append(" Odnotowano temperaturę < 0  \n  [ ");
            sb.append(TextFormatters.getPretty(data.get(0).getDate()));
            sb.append(" ] ");
            for (TemperatureData td : data) {
                sb.append(" \n ");
                sb.append(td.getTransmitterNameAndTemperature());
            }

            if (lastTextPostTime == null || LocalDateTime.now().isAfter(lastTextPostTime.plusHours(MAX_POST_USAGE_HOURS))) {
                // in case of creating new post (the old one is too old or des not exist)
                lastTextPostTime = LocalDateTime.now();
                lastTextPostId = facebookManager.postMessage(sb.toString());
                log.info("Text Post id: {}", lastTextPostId);
            } else {
                // in case of commenting existing post
                String commentId = facebookManager.postComment(lastTextPostId, sb.toString());
                log.info("Comment id: {}", commentId);
            }
        }
    }
}
