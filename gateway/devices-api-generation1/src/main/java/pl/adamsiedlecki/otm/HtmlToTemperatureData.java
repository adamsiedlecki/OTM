package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.location.LocationService;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;
import pl.adamsiedlecki.otm.stationInfo.gen1.Gen1Device;
import pl.adamsiedlecki.otm.stationInfo.gen1.Gen1DevicesInfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class HtmlToTemperatureData {

    private final Gen1DevicesInfo gen1DevicesInfo;
    private final LocationService locationService;

    public List<TemperatureData> process(String html) {
        if (html == null || html.isBlank()) {
            return List.of();
        }
        List<TemperatureData> tempList = new ArrayList<>();

        String[] strings = html.split(";");
        for (String line : strings) {
            String lowerCase = line.toLowerCase();
            if (!lowerCase.contains("html") && !lowerCase.contains("http") && !lowerCase.contains("date")) {
                String[] values = line.split("::");
                Optional<TemperatureData> temperatureDataOptional = getTemperatureDataByValues(values);
                updateNameAndLocation(temperatureDataOptional);
                temperatureDataOptional.ifPresent(tempList::add);
            }
        }
        return tempList;
    }

    private void updateNameAndLocation(Optional<TemperatureData> optionalTemperatureData) {
        optionalTemperatureData.ifPresent(td -> {
            Optional<Gen1Device> gen1Device = gen1DevicesInfo.getByOriginalName(td.getTransmitterName().trim());
            if (gen1Device.isEmpty()) {
                log.error("No information found about gen1 device: " + td.getTransmitterName());
            } else {
                td.setTransmitterName(td.getTransmitterName() + " " + gen1Device.get().getAliasName());
                Location location = locationService.getOrSave("" + gen1Device.get().getLatitude(), "" + gen1Device.get().getLongitude());
                td.setLocation(location);
            }
        });
    }

    private Optional<TemperatureData> getTemperatureDataByValues(String[] values) {
        if (values.length > 1) {

            TemperatureData temperatureData = new TemperatureData();
            String name = values[0];
            name = name.replaceAll("�", "");
            temperatureData.setTransmitterName(name);

            String temperature = values[1];
            Optional<BigDecimal> optionalTemp = extractTemperature(temperature);
            if (optionalTemp.isPresent()) {
                temperatureData.setTemperatureCelsius(optionalTemp.get());
            } else {
                return Optional.empty();
            }

            return Optional.of(temperatureData);
        }
        return Optional.empty();
    }

    private Optional<BigDecimal> extractTemperature(String temperature) {
        temperature = temperature.trim();

        if (temperature.contains("no response")) {

            return Optional.empty();
        } else if (temperature.length() > 6) { // temperature format is xx.xx or -xx.xx
            return Optional.empty();
        } else {
            if (NumberUtils.isCreatable(temperature)) {
                return Optional.of(new BigDecimal(temperature));

            } else {
                return Optional.empty();
            }

        }
    }
}
