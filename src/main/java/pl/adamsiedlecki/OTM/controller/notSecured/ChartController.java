package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.tools.ChartCreator;

import java.util.List;
import java.util.Optional;

@Controller
public class ChartController {

    private final TemperatureDataService temperatureDataService;

    @Autowired
    public ChartController(TemperatureDataService temperatureDataService) {
        this.temperatureDataService = temperatureDataService;
    }

    @GetMapping("/chart")
    public String getIndex(Model model, @RequestParam(value = "numberOfHours") int numberOfHours) {
        Optional<List<TemperatureData>> optionalTemperatureData = temperatureDataService.findAllLastXHours(numberOfHours);
        if(optionalTemperatureData.isPresent()){
            ChartCreator chartCreator = new ChartCreator();
            chartCreator.createChart(optionalTemperatureData.get(), 5000, 1080);
        }
        return "chart";
    }
}
