package pl.adamsiedlecki.otm.controller.notSecured.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamsiedlecki.otm.dataFetcher.DataFetcher;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.otm.db.tempDataAlias.TempDataAliasService;
import pl.adamsiedlecki.otm.schedule.ScheduleOvernightChart;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("api/v1")
public class ApiController {

    private final TemperatureDataService temperatureDataService;
    private final TempDataAliasService tempDataAliasService;
    private final String s = File.separator;
    private final DataFetcher dataFetcher;
    private final Logger log = LoggerFactory.getLogger(ApiController.class);
    private final ScheduleOvernightChart scheduleOvernightChart;

    @Autowired
    public ApiController(TemperatureDataService temperatureDataService, TempDataAliasService tempDataAliasService, DataFetcher dataFetcher, ScheduleOvernightChart scheduleOvernightChart) {
        this.temperatureDataService = temperatureDataService;
        this.tempDataAliasService = tempDataAliasService;
        this.dataFetcher = dataFetcher;
        this.scheduleOvernightChart = scheduleOvernightChart;
    }

    @GetMapping("/facebook-post")
    public @ResponseBody
    String postOnFacebook() {
        new Thread(scheduleOvernightChart::createAndPostChart).start();
        return "method is running";
    }

    @GetMapping("/aliases")
    public @ResponseBody
    List<TempDataAlias> getAliases() {
        return tempDataAliasService.findAll();
    }

    @GetMapping("/temperature-now")
    public @ResponseBody
    List<TemperatureData> getTemperaturesNow() {
        return dataFetcher.fetchAndSaveTemperatures();
    }

    @GetMapping(path = "/temperature-data")
    public @ResponseBody
    List<TemperatureData> getTempData(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "50") int size) {
        return temperatureDataService.findAll(PageRequest.of(page, size)).getContent();
    }

    @GetMapping(
            path = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(
            @RequestParam(value = "name", defaultValue = "chart.jpg") String name
    ) {
        InputStream targetStream;
        try {
            File f = new File(MyFilesystem.getStoragePath() + "img" + s + name);
            log.info("DESIRED FILE: " + f.getAbsolutePath());
            targetStream = new FileInputStream(f);
            return targetStream.readAllBytes();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
