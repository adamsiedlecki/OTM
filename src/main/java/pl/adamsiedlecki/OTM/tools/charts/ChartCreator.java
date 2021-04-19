package pl.adamsiedlecki.OTM.tools.charts;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
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

    private final String s = File.separator;
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final ChartElementsCreator elemCreator = new ChartElementsCreator();

    public File createOvernightChart(List<TemperatureData> temperatureDataList) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(temperatureDataList, font);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart("OTM Adam Siedlecki    Wykres temperatury "
                + TextFormatters.getPrettyTime(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPrettyTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        URI uri = null;
        File file = new File(s + "storage" + s + "img");
        file.mkdirs();
        uri = file.toURI();
        String mainPath = Paths.get(uri).toString();

        new File(mainPath + s + "night7").delete();

        File f = new File(mainPath + s + "night6.jpg");
        f.renameTo(new File(mainPath + s + "night7.jpg"));
        File f1 = new File(mainPath + s + "night5.jpg");
        f1.renameTo(new File(mainPath + s + "night6.jpg"));
        File f2 = new File(mainPath + s + "night4.jpg");
        f2.renameTo(new File(mainPath + s + "night5.jpg"));
        File f3 = new File(mainPath + s + "night3.jpg");
        f3.renameTo(new File(mainPath + s + "night4.jpg"));
        File f4 = new File(mainPath + s + "night2.jpg");
        f4.renameTo(new File(mainPath + s + "night3.jpg"));
        File f5 = new File(mainPath + s + "night1.jpg");
        f5.renameTo(new File(mainPath + s + "night2.jpg"));

        File destination = new File(mainPath + s + "night1.jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
            return new File("");
        }
        System.out.println("CHART CREATED");
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
                + TextFormatters.getPrettyTime(temperatureDataList.get(0).getDate())
                + "  -  " + TextFormatters.getPrettyTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        URI uri = null;
        File file = new File(s + "storage" + s + "img");
        file.mkdirs();
        uri = file.toURI();
        String mainPath = Paths.get(uri).toString();
        new File(mainPath + s + "chart.jpg").delete();

        try {
            ChartUtils.saveChartAsJPEG(new File(mainPath + s + "chart.jpg"), chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("CHART CREATED");
    }

}
