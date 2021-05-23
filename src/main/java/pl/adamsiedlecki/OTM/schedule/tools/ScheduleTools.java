package pl.adamsiedlecki.OTM.schedule.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.externalServices.facebook.FacebookManager;
import pl.adamsiedlecki.OTM.tools.text.TextFormatters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@Scope("singleton")
public class ScheduleTools {

    private final FacebookManager facebookManager;
    private final Logger log = LoggerFactory.getLogger(ScheduleTools.class);
    private LocalDateTime lastTextPostTime;
    private String lastTextPostId;


    @Autowired
    public ScheduleTools(FacebookManager facebookManager) {
        this.facebookManager = facebookManager;
    }

    public String getEmoji(boolean isBelowZero) {
        if (isBelowZero) {
            return "❄️";
        } else {
            return "✔️";
        }
    }

    // one post is created for 12 hours; next data is published as comment of this post
    public void sendPostOrComment(List<TemperatureData> data) {
        if (data.size() == 0) {
            return;
        }
        boolean isBelowZero = getBelowZero(data);

        if (isBelowZero) {
            data.sort(Comparator.comparing(TemperatureData::getTemperatureCelsius));
            log.info("TEMPERATURES BELOW ZERO FOUND!");
            StringBuilder sb = new StringBuilder();
            sb.append(getEmoji(true)).append(" Odnotowano temperaturę < 0  \n  [ ");
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

            } else {
                // in case of commenting existing post
                String commentId = facebookManager.postComment(lastTextPostId, sb.toString());
                log.info("Comment id: " + commentId);
            }
        }
    }

    public boolean getBelowZero(List<TemperatureData> data) {
        return data.stream().anyMatch(td -> td.getTemperatureCelsius().compareTo(BigDecimal.ZERO) < 0);
    }
}
