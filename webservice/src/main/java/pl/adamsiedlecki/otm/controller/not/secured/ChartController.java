package pl.adamsiedlecki.otm.controller.not.secured;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.tools.charts.SimpleChartCreator;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartTitle;

import java.io.File;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final TemperatureDataService temperatureDataService;
    private final SimpleChartCreator chartCreator;

    @GetMapping("/chart")
    public String getIndex(final Model model,
                           final @RequestParam(value = "numberOfHours") int numberOfHours,
                           final @RequestParam(value = "width", defaultValue = "${otm.default.chart.width}") int width,
                           final @RequestParam(value = "height", defaultValue = "${otm.default.chart.height}") int height) {
        List<TemperatureData> temperatureData = temperatureDataService.findAllLastXHours(numberOfHours);
        if (temperatureData.isEmpty()) {
            model.addAttribute("filename", "notEnoughData");
            return "chart";
        }
        File chart = chartCreator.createChart(temperatureData, width, height, ChartTitle.DEFAULT.get());
        model.addAttribute("filename", chart.getName());
        return "chart";
    }
}
