package pl.adamsiedlecki.otm.dataFetcher;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class HtmlToTemperatureData {

    private final TemperatureAliasAdder adder;

    @Autowired
    public HtmlToTemperatureData(TemperatureAliasAdder adder) {
        this.adder = adder;
    }

    public List<TemperatureData> process(String html) {
        if (html == null || html.isBlank()) {
            return List.of();
        }
        List<TemperatureData> tempList = new ArrayList<>();

        String[] strings = html.split(";");
        for (String line : strings) {
            String lowerCase = line.toLowerCase();
            if (!lowerCase.contains("html") && !lowerCase.contains("HTTP") && !lowerCase.contains("date")) {
                String[] values = line.split("::");
                Optional<TemperatureData> temperatureDataByValues = getTemperatureDataByValues(values);
                temperatureDataByValues.ifPresent(adder::add);
                temperatureDataByValues.ifPresent(tempList::add);
            }
        }
        return tempList;
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
