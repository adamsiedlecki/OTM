package pl.adamsiedlecki.OTM.db.location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationServiceTests {

    private final Logger log = LoggerFactory.getLogger(LocationServiceTests.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private TemperatureDataService tempDataService;

    @Test
    public void getOrSaveTest() {
        assert locationService.count() == 0;
        locationService.getOrSave(1f, 2f);
        locationService.getOrSave(1f, 2f);
        assert locationService.count() == 1;

    }

    @Test
    public void saveWithTemperatureTest() {
        assert locationService.count() == 0;
        assert tempDataService.count() == 0;
        Location location = locationService.getOrSave(1f, 2f);
        TemperatureData td = new TemperatureData();
        td.setLocation(location);
        tempDataService.saveAll(List.of(td));
        assert locationService.count() == 1;
        assert tempDataService.count() == 1;

        Location location2 = locationService.getOrSave(1f, 2f);
        TemperatureData td2 = new TemperatureData();
        td2.setLocation(location2);
        tempDataService.saveAll(List.of(td2));

        log.info("locationService.count(): " + locationService.count());
        log.info("tempDataService.count(): " + tempDataService.count());
        assert locationService.count() == 1;
        assert tempDataService.count() == 2;
    }
}
