package pl.adamsiedlecki.OTM.tools.charts;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.tools.charts.tools.ChartElementsCreator;
import pl.adamsiedlecki.OTM.tools.files.MyFilesystem;
import pl.adamsiedlecki.OTM.tools.text.TextFormatters;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class ForecastChartCreator implements ChartCreator {

    private final ChartElementsCreator elemCreator = new ChartElementsCreator();
    private final Logger log = LoggerFactory.getLogger(ForecastChartCreator.class);
    private final Font font = new Font("Dialog", Font.PLAIN, 14);

    @Override
    public File createChart(List<TemperatureData> temperatureDataList, int width, int height, String title) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(title
                + TextFormatters.getPrettyDateTime(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPrettyDateTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);
        chart.setBackgroundPaint(Color.YELLOW);

        File file = new File(MyFilesystem.getStoragePath() + "img");
        file.mkdirs();
        URI uri = file.toURI();
        String mainPath = Paths.get(uri).toString();

        File destination = new File(mainPath + MyFilesystem.getSeparator() + "predictions.jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new File("");
        }
        log.info("CHART CREATED, path: " + destination.getAbsolutePath());
        return destination;
    }
}
