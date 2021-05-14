package pl.adamsiedlecki.OTM.controller.notSecured;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.adamsiedlecki.OTM.db.location.LocationService;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTest {

    private final Logger log = LoggerFactory.getLogger(ApiControllerTest.class);
    @Autowired
    private ApiController apiController;
    @Autowired
    private TemperatureDataService temperatureDataService;
    @Autowired
    private LocationService locationService;

    @Test
    public void getTemperaturesNowTest() {
        List<TemperatureData> temperaturesNow = apiController.getTemperaturesNow();
        List<TemperatureData> temperaturesNow2 = apiController.getTemperaturesNow();
        if (locationService.count() > temperaturesNow.size()) {
            System.out.println("DUPLICATES");
        }
        log.info("locationService.count():" + locationService.count());
        log.info("temperatureDataService.count():" + temperatureDataService.count());
        log.info("temperatureDataService.findAll():" + temperatureDataService.findAll());
        assert locationService.count() == 4;
    }


}
