package pl.adamsiedlecki.otm.tools.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericsConverter {

    /**
     * Converts list of TemperatureData to list of its super type PresentableOnChart
     *
     * @param data temperature data list
     * @return PresentableOnChart data list
     */
    public static List<PresentableOnChart> convert(final List<TemperatureData> data) {
        return data.stream().map(PresentableOnChart.class::cast).collect(Collectors.toList());
    }
}
