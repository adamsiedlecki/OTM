package pl.adamsiedlecki.otm.controller.not.secured.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamsiedlecki.otm.data.fetcher.TemperatureDataFetcher;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.schedule.HealthCheckSchedule;
import pl.adamsiedlecki.otm.schedule.OvernightChartSchedule;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final TemperatureDataService temperatureDataService;
    private final TemperatureDataFetcher dataFetcher;
    private final OvernightChartSchedule scheduleOvernightChart;
    private final HealthCheckSchedule healthCheckSchedule;
    private final MyFilesystem myFilesystem;

    @GetMapping("/facebook/post/overnight/chart")
    public @ResponseBody
    String postOnFacebook() {
        scheduleOvernightChart.createAndPostChart();
        return "Operation completed.";
    }

    @GetMapping("/temperature/current")
    public @ResponseBody
    List<TemperatureData> getCurrentTemperatures() {
        return dataFetcher.fetchAndSaveAllTemperatures();
    }

    @GetMapping("/gen1/temperature/current")
    public @ResponseBody
    List<TemperatureData> getCurrentTemperaturesFromGen1() {
        return dataFetcher.fetchAndSaveTemperaturesFromGen1Stations();
    }

    @GetMapping("/gen2/temperature/current")
    public @ResponseBody
    List<TemperatureData> getCurrentTemperaturesFromGen2() {
        return dataFetcher.fetchAndSaveTemperaturesFromGen2Stations();
    }

    @GetMapping(path = "/temperature-data")
    public @ResponseBody
    List<TemperatureData> getTempData(final @RequestParam(value = "page", defaultValue = "0") int page,
                                      final @RequestParam(value = "size", defaultValue = "50") int size) {
        return temperatureDataService.findAll(PageRequest.of(page, size)).getContent();
    }

    @GetMapping("/health/current")
    public @ResponseBody
    List<HealthCheckData> getCurrentHealthCheck() {
        return healthCheckSchedule.checkBatteryVoltage();
    }

    @GetMapping(
            path = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(final @RequestParam(value = "name", defaultValue = "chart.jpg") String name) {
        log.info("Desired file: {}", name);
        List<File> allFilesRecursively = myFilesystem.getAllFilesRecursively(MyFilesystem.getStoragePath() + "img");
        Optional<File> optionalDesiredFile = allFilesRecursively.stream().filter(file -> file.getName().equals(name)).findFirst();

        if (optionalDesiredFile.isPresent()) {
            try (InputStream targetStream = new FileInputStream(optionalDesiredFile.get())) {
                return targetStream.readAllBytes();
            } catch (IOException e) {
                log.error("Error while sending image: {}", name);
            }
        }

        return new byte[]{};
    }
}
