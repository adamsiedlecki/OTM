package pl.adamsiedlecki.otm.dataFetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.AllDevicesGen1TemperatureGetter;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.otm.devices.api.gen2.AllDevicesGen2TemperatureGetter;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataFetcher {

    private final AllDevicesGen1TemperatureGetter allDevicesGen1TemperatureGetter;
    private final AllDevicesGen2TemperatureGetter allDevicesGen2TemperatureGetter;
    private final TemperatureDataService temperatureDataService;
    private final LocationService locationService;

    public List<TemperatureData> fetchAndSaveTemperatures() {
        List<TemperatureData> allResults = new ArrayList<>();

        allResults.addAll(allDevicesGen1TemperatureGetter.get());
        allResults.addAll(allDevicesGen2TemperatureGetter.get());

        saveToDatabase(allResults);
        return allResults;
    }

    private void saveToDatabase(List<TemperatureData> td) {
        temperatureDataService.saveAll(td);
    }

}
