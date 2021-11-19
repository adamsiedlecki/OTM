package pl.adamsiedlecki.otm.controller.notSecured;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.tools.charts.SimpleChartCreator;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartTitle;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChartController {

    private final TemperatureDataService temperatureDataService;
    private final SimpleChartCreator chartCreator;

    @GetMapping("/chart")
    public String getIndex(Model model,
                           @RequestParam(value = "numberOfHours") int numberOfHours,
                           @RequestParam(value = "width", defaultValue = "${otm.default.chart.width}") int width,
                           @RequestParam(value = "height", defaultValue = "${otm.default.chart.height}") int height
    ) {
        List<TemperatureData> temperatureData = temperatureDataService.findAllLastXHours(numberOfHours);
        if (temperatureData.size() != 0) {
            chartCreator.createChart(temperatureData, width, height, ChartTitle.DEFAULT.get());
        }
        return "chart";
    }
}
