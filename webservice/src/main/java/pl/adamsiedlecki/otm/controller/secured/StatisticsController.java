package pl.adamsiedlecki.otm.controller.secured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamsiedlecki.otm.db.statistics.ExistingStatistics;
import pl.adamsiedlecki.otm.db.statistics.StatisticsService;
import pl.adamsiedlecki.otm.db.statistics.Statitics;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class StatisticsController {

    private final TemperatureDataService tempDataService;
    private final StatisticsService statService;
    private final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    public StatisticsController(TemperatureDataService tempDataService, StatisticsService statService) {
        this.tempDataService = tempDataService;
        this.statService = statService;
    }

    @GetMapping("/statistics")
    public String getAdminPanel(Model model) {
        model.addAttribute("temperaturesDataAmount", tempDataService.count());

        List<Statitics> stats = statService.findAll();
        model.addAttribute("statistics", stats);
        addEspNoResponsePercentage(model);
        model.addAttribute("temperaturesBelowZeroCount", tempDataService.countBelowTemperature(BigDecimal.ZERO));
        return "statistics";
    }

    private void addEspNoResponsePercentage(Model model) {
        statService.get(ExistingStatistics.REQUESTS_TO_ESP_COUNT)
                .ifPresent(s1 -> {
                    long total = s1.getsValue();
                    if (total == 0) {
                        return;
                    }
                    statService.get(ExistingStatistics.ESP_NO_RESPONSE_COUNT)
                            .ifPresentOrElse(s2 -> {
                                long noResponseCount = s2.getsValue();
                                float percentage = (float) noResponseCount / total * 100;
                                model.addAttribute("espNoResponsePercentage", percentage + "%");
                            }, () -> {
                                model.addAttribute("espNoResponsePercentage", 0 + "%");
                            });
                });

    }
}
