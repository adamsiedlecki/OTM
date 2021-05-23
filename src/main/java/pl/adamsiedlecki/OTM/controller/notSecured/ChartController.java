package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.charts.ChartCreator;

import java.util.List;

@Controller
public class ChartController {

    private final TemperatureDataService temperatureDataService;

    @Autowired
    public ChartController(TemperatureDataService temperatureDataService) {
        this.temperatureDataService = temperatureDataService;
    }

    @GetMapping("/chart")
    public String getIndex(Model model,
                           @RequestParam(value = "numberOfHours") int numberOfHours,
                           @RequestParam(value = "width", defaultValue = "5000") int width,
                           @RequestParam(value = "height", defaultValue = "1000") int height
    ) {
        List<TemperatureData> temperatureData = temperatureDataService.findAllLastXHours(numberOfHours);
        if (temperatureData.size() != 0) {
            ChartCreator chartCreator = new ChartCreator();
            chartCreator.createChart(temperatureData, width, height);
        }
        return "chart";
    }
}
