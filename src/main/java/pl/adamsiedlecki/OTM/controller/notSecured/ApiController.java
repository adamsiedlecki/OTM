package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;

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

    @Autowired
    public ApiController(TemperatureDataService temperatureDataService, TempDataAliasService tempDataAliasService) {
        this.temperatureDataService = temperatureDataService;
        this.tempDataAliasService = tempDataAliasService;
    }

    @GetMapping("/aliases")
    public @ResponseBody
    List<TempDataAlias> getAliases() {
        return tempDataAliasService.findAll();
    }

    @GetMapping(path = "/tempData")
    public @ResponseBody
    List<TemperatureData> getTempData(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "50") int size) {
        return temperatureDataService.findAll(PageRequest.of(page, size)).getContent();
    }

    @GetMapping(
            value = "/image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType(
            @RequestParam(value = "name", defaultValue = "chart.jpg") String name
    ) {
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(s + "storage" + s + "img" + s + name);
            return targetStream.readAllBytes();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
