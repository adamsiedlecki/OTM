package pl.adamsiedlecki.otm.tools.charts;

import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartElementsCreator;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;
import pl.adamsiedlecki.otm.tools.uuid.UuidTool;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastChartCreator implements ChartCreator {

    private final ChartElementsCreator elemCreator = new ChartElementsCreator();
    private final Logger log = LoggerFactory.getLogger(ForecastChartCreator.class);
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final MyFilesystem myFilesystem;

    @Override
    public File createChart(List<TemperatureData> temperatureDataList, int width, int height, String title) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        if (temperatureDataList.isEmpty()) {
            log.error("Cannot create chart due to no data");
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(title
                + TextFormatters.getPretty(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPretty(temperatureDataList.get(temperatureDataList.size() - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);
        chart.setBackgroundPaint(Color.YELLOW);

        File destination = new File(myFilesystem.getForecastChartsPath() + UuidTool.getRandom() + ".jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new File("");
        }
        log.info("Chart created: {}", destination.getAbsolutePath());
        return destination;
    }
}
