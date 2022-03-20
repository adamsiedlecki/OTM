package pl.adamsiedlecki.otm.controller.not.secured;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckData;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckDataService;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.tools.charts.SimpleChartCreator;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartProperties;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private static final String FILENAME = "filename";
    private static final String CHART = "chart";

    private final TemperatureDataService temperatureDataService;
    private final HealthCheckDataService healthCheckDataService;
    private final SimpleChartCreator chartCreator;

    @GetMapping("/chart/temperature")
    public String getTemperatureChart(final Model model,
                                      final @RequestParam(value = "numberOfHours", defaultValue = "20") int numberOfHours,
                                      final @RequestParam(value = "width", defaultValue = "${otm.default.chart.width}") int width,
                                      final @RequestParam(value = "height", defaultValue = "${otm.default.chart.height}") int height) {
        List<TemperatureData> temperatureData = temperatureDataService.findAllLastXHours(numberOfHours);
        if (temperatureData.isEmpty()) {
            model.addAttribute(FILENAME, "notEnoughData");
            return CHART;
        }
        List<PresentableOnChart> presentableList = temperatureData.stream().map(PresentableOnChart.class::cast).collect(Collectors.toList());
        File chart = chartCreator.createChart(presentableList, width, height, ChartProperties.TEMPERATURE_DEFAULT.get(), ChartProperties.TEMPERATURE_AXIS_TITLE.get());
        model.addAttribute(FILENAME, chart.getName());
        return CHART;
    }

    @GetMapping("/chart/voltage")
    public String getVoltageChart(final Model model,
                                  final @RequestParam(value = "numberOfHours", defaultValue = "20") int numberOfHours,
                                  final @RequestParam(value = "width", defaultValue = "${otm.default.chart.width}") int width,
                                  final @RequestParam(value = "height", defaultValue = "${otm.default.chart.height}") int height) {
        List<HealthCheckData> healthCheckData = healthCheckDataService.findAllLastXHours(numberOfHours);
        if (healthCheckData.isEmpty()) {
            model.addAttribute(FILENAME, "notEnoughData");
            return CHART;
        }
        List<PresentableOnChart> presentableList = healthCheckData.stream().map(PresentableOnChart.class::cast).collect(Collectors.toList());
        File chart = chartCreator.createChart(presentableList, width, height, ChartProperties.VOLTAGE_DEFAULT.get(), "Napięcie (V)");
        model.addAttribute(FILENAME, chart.getName());
        return CHART;
    }
}
