package pl.adamsiedlecki.otm.odg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.odg.model.CreateChartInput;
import pl.adamsiedlecki.odg.model.PresentableOnChart;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JFreeChartCreator {

    private final OdgApiClient odgApiClient;

    public File createXyChart(List<PresentableOnChart> chartData, int width, int height, String title, String valuesLabel, String timeLabel) {
        File xyChart = odgApiClient.createXyChart(prepareInput(chartData, width, height, title, valuesLabel, timeLabel));
        if (!xyChart.exists()) {
            log.error("Chart returned by ODG does not exist");
        }
        return xyChart;
    }

    private CreateChartInput prepareInput(List<PresentableOnChart> chartData, int width, int height, String title, String valuesLabel, String timeLabel) {
        return new CreateChartInput()
                .chartTitle(title)
                .widthPixels(width)
                .heightPixels(height)
                .valuesLabel(valuesLabel)
                .timeLabel(timeLabel)
                .valueList(chartData)
                .areItemLabelsVisible(isSmallAmountOfDataOnChart(chartData, width))
                .maxMinutesConnectingLines(121);
    }

    private boolean isSmallAmountOfDataOnChart(List<PresentableOnChart> chartData, int chartWidth) {
        return chartData.size() < chartWidth / 10;
    }
}
