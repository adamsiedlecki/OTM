package pl.adamsiedlecki.otm.data.fetcher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.AllDevicesGen1TemperatureGetter;
import pl.adamsiedlecki.otm.data.fetcher.gen3.GroupsOfGen3DevicesTemperatureGetter;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.devices.api.gen2.AllDevicesGen2TemperatureGetter;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemperatureDataFetcher {

    private final AllDevicesGen1TemperatureGetter allDevicesGen1TemperatureGetter;
    private final AllDevicesGen2TemperatureGetter allDevicesGen2TemperatureGetter;
    private final GroupsOfGen3DevicesTemperatureGetter allDevicesGen3TemperatureGetter;
    private final TemperatureDataService temperatureDataService;

    public List<TemperatureData> fetchAndSaveAllTemperatures() {
        List<TemperatureData> allResults = new ArrayList<>();

        allResults.addAll(fetchAndSaveTemperaturesFromGen1Stations());
        allResults.addAll(fetchAndSaveTemperaturesFromGen2Stations());
        allResults.addAll(fetchAndSaveTemperaturesFromGen3Stations());


        return allResults;
    }

    public List<TemperatureData> fetchAndSaveTemperaturesFromGen1Stations() {
        List<TemperatureData> temperatureData = allDevicesGen1TemperatureGetter.get();
        saveToDatabase(temperatureData);
        return temperatureData;
    }

    public List<TemperatureData> fetchAndSaveTemperaturesFromGen2Stations() {
        List<TemperatureData> temperatureData = allDevicesGen2TemperatureGetter.get();
        saveToDatabase(temperatureData);
        return temperatureData;
    }

    public List<TemperatureData> fetchAndSaveTemperaturesFromGen3Stations() {
        List<TemperatureData> temperatureData = allDevicesGen3TemperatureGetter.getFromAllStations();
        saveToDatabase(temperatureData);
        return temperatureData;
    }

    public List<TemperatureData> fetchAndSaveTemperaturesFromGen3StationsThatCanBeInDanger() {
        List<TemperatureData> temperatureData = allDevicesGen3TemperatureGetter.getFromStationsThatCanBeInDanger();
        saveToDatabase(temperatureData);
        return temperatureData;
    }

    private void saveToDatabase(final List<TemperatureData> td) {
        temperatureDataService.saveAll(td);
    }

}
