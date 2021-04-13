package pl.adamsiedlecki.OTM.tools;


import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HtmlToTemperatureData {

    private final TemperatureAliasAdder adder;

    @Autowired
    public HtmlToTemperatureData(TemperatureAliasAdder adder) {
        this.adder = adder;
    }

    public List<TemperatureData> process(String html) {
        List<TemperatureData> tempList = new ArrayList<>();

        String[] strings = html.split(";");
        for (String line : strings) {
            String lowerCase = line.toLowerCase();
            if (!lowerCase.contains("html") && !lowerCase.contains("HTTP") && !lowerCase.contains("date")) {
                String[] values = line.split("::");
                Optional<TemperatureData> temperatureDataByValues = getTemperatureDataByValues(values);
                if(temperatureDataByValues.isPresent()){
                    adder.add(temperatureDataByValues.get());
                }
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
            Optional<BigDecimal> optionalTemp = extractTemperature(temperature,
                    temperatureData.getTransmitterName());
            if (optionalTemp.isPresent()) {
                temperatureData.setTemperatureCelsius(optionalTemp.get());
            } else {
                return Optional.empty();
            }

            if(values.length == 4) {
                temperatureData.setLatitude(Float.parseFloat(values[2]));
                temperatureData.setLongitude(Float.parseFloat(values[3]));
            }

            return Optional.of(temperatureData);
        }
        return Optional.empty();
    }

    private Optional<BigDecimal> extractTemperature(String temperature, String transmitterName) {
        temperature = temperature.trim();

        if (temperature.contains("no response")) {

            return Optional.empty();
        } else if (temperature.length() > 6) {
            return Optional.empty();
        } else {
            if (NumberUtils.isCreatable(temperature)) {
                return Optional.of(new BigDecimal(temperature));

            } else {
                //ctx.ifPresent(context -> Toasts.temperatureIsNotANumber(context, transmitterName));
                return Optional.empty();
            }

        }
    }
}
