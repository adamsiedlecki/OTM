package pl.adamsiedlecki.OTM.tools.charts;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;
import pl.adamsiedlecki.OTM.tools.TextFormatters;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class ChartCreator {

    private final Logger log = LoggerFactory.getLogger(ChartCreator.class);
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final ChartElementsCreator elemCreator = new ChartElementsCreator();

    public File createOvernightChart(List<TemperatureData> temperatureDataList, int width, int height) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart("OTM Adam Siedlecki    Wykres temperatury "
                + TextFormatters.getPrettyDateTime(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPrettyDateTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        File file = new File(MyFilesystem.getStoragePath() + "img");
        file.mkdirs();
        URI uri = file.toURI();
        String mainPath = Paths.get(uri).toString();

        new File(mainPath + MyFilesystem.getSeparator() + "night7").delete();

        File f = new File(mainPath + MyFilesystem.getSeparator() + "night6.jpg");
        f.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night7.jpg"));
        File f1 = new File(mainPath + MyFilesystem.getSeparator() + "night5.jpg");
        f1.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night6.jpg"));
        File f2 = new File(mainPath + MyFilesystem.getSeparator() + "night4.jpg");
        f2.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night5.jpg"));
        File f3 = new File(mainPath + MyFilesystem.getSeparator() + "night3.jpg");
        f3.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night4.jpg"));
        File f4 = new File(mainPath + MyFilesystem.getSeparator() + "night2.jpg");
        f4.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night3.jpg"));
        File f5 = new File(mainPath + MyFilesystem.getSeparator() + "night1.jpg");
        f5.renameTo(new File(mainPath + MyFilesystem.getSeparator() + "night2.jpg"));

        File destination = new File(mainPath + MyFilesystem.getSeparator() + "night1.jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new File("");
        }
        log.info("CHART CREATED, path: " + destination.getAbsolutePath());
        return destination;
    }

    public File createOvernightPredictionChart(List<TemperatureData> temperatureDataList, int width, int height, String title) {
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

    public void createChart(List<TemperatureData> temperatureDataList, int width, int height) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return;
        }

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart("OTM Adam Siedlecki    Wykres temperatury "
                + TextFormatters.getPrettyDateTime(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPrettyDateTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        URI uri = null;
        File file = new File(MyFilesystem.getStoragePath() + "img");
        file.mkdirs();
        uri = file.toURI();
        String mainPath = Paths.get(uri).toString();
        new File(mainPath + MyFilesystem.getSeparator() + "chart.jpg").delete();

        try {
            ChartUtils.saveChartAsJPEG(new File(mainPath + MyFilesystem.getSeparator() + "chart.jpg"), chart, width, height);
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }
        log.info("CHART CREATED");
    }

}
