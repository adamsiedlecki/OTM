package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;

import java.util.List;

@Controller
@RequestMapping("api/v1")
public class ApiController {

    private final TemperatureDataService temperatureDataService;
    private final TempDataAliasService tempDataAliasService;

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
}
