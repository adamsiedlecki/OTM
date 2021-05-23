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

public class SimpleChartCreator implements ChartCreator {

    private final Logger log = LoggerFactory.getLogger(SimpleChartCreator.class);
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final ChartElementsCreator elemCreator = new ChartElementsCreator();

    public File createChart(List<TemperatureData> temperatureDataList, int width, int height, String title) {
        if (temperatureDataList.size() == 0) {
            return new File("");
        }
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(
                title
                        + TextFormatters.getPrettyDateTime(temperatureDataList.get(0).getDate())
                        + "  -  " + TextFormatters.getPrettyDateTime(temperatureDataList.get(temperatureDataList.size() - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        URI uri = null;
        File file = new File(MyFilesystem.getStoragePath() + "img");
        file.mkdirs();
        uri = file.toURI();
        String mainPath = Paths.get(uri).toString();
        new File(mainPath + MyFilesystem.getSeparator() + "chart.jpg").delete();

        File destination = new File(mainPath + MyFilesystem.getSeparator() + "chart.jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
            log.info("CHART CREATED");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return destination;
    }
}
