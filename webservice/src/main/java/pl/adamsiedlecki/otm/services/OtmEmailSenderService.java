package pl.adamsiedlecki.otm.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.orchout.EmailSenderService;
import pl.adamsiedlecki.otm.tools.data.OtmStatistics;
import pl.adamsiedlecki.otm.tools.text.Emojis;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtmEmailSenderService {

    private final EmailSenderService emailSenderService;

    public void sendCharts(List<String> recipients, String subject, String htmlContentBeginning, List<File> files, OtmStatistics otmStatistics) {

        Map<String, File> imageMap = new HashMap<>();
        StringBuilder htmlContentSb = new StringBuilder(htmlContentBeginning);

        if (otmStatistics != null) {
            if (otmStatistics.getLowestTemperature().isPresent()) {
                TemperatureData lowest = otmStatistics.getLowestTemperature().get();
                htmlContentSb.append("<h5>");
                htmlContentSb.append(Emojis.FROST).append(" Najniższą temperaturą było: ");
                htmlContentSb.append(lowest.getTemperatureCelsius()).append(" °C");
                htmlContentSb.append(" o godzinie ").append(TextFormatters.getPrettyHour(lowest.getDate()));
                htmlContentSb.append(" przy stacji ").append(lowest.getTransmitterName());
                htmlContentSb.append("</h5>").append("\n");
            }
            if (otmStatistics.getHighestTemperature().isPresent()) {
                TemperatureData highest = otmStatistics.getHighestTemperature().get();
                htmlContentSb.append("<h5>");
                htmlContentSb.append(Emojis.WARM).append(" Najwyższą temperaturą było: ");
                htmlContentSb.append(highest.getTemperatureCelsius()).append(" °C");
                htmlContentSb.append(" o godzinie ").append(TextFormatters.getPrettyHour(highest.getDate()));
                htmlContentSb.append(" przy stacji ").append(highest.getTransmitterName());
                htmlContentSb.append("</h5>").append("\n");
            }
        }

        for (int i = 0; i < files.size(); i++) {
            imageMap.put("chart" + i + ".jpg", files.get(i));
            htmlContentSb.append("<img src=\"cid:chart").append(i).append(".jpg\">");
        }

        for (String recipient : recipients) {
            emailSenderService.send(recipient, subject, htmlContentSb.toString(), imageMap);
        }

    }
}