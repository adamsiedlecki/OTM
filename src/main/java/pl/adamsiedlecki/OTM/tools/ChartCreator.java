package pl.adamsiedlecki.OTM.tools;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class ChartCreator {

    private final String s = File.separator;

    public void createOvernightChart(List<TemperatureData> temperatureDataList) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return;
        }


        DateAxis xAxis = new DateAxis("Czas");
        //xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis("Temperatura");
        yAxis.setAutoRangeIncludesZero(false);

        XYSplineRenderer renderer1 = new XYSplineRenderer();
        XYPlot plot = new XYPlot(createSampleData(temperatureDataList), xAxis, yAxis, renderer1);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(6, 6, 20, 6));


        // create and return the chart panel...
        JFreeChart chart = new JFreeChart("Wykres temperatury "
                + TextFormatters.getPrettyTime(temperatureDataList.get(0).getDate())
                + " - " + TextFormatters.getPrettyTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);

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
        try {
            ChartUtils.saveChartAsJPEG(new File(mainPath + s + "night1.jpg"), chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("CHART CREATED");
    }

    public void createChart(List<TemperatureData> temperatureDataList, int width, int height) {
        temperatureDataList.sort(Comparator.comparing(TemperatureData::getDate));
        int size = temperatureDataList.size();
        if (size == 0) {
            return;
        }


        DateAxis xAxis = new DateAxis("Czas");
        //xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis("Temperatura");
        yAxis.setAutoRangeIncludesZero(false);

        XYSplineRenderer renderer1 = new XYSplineRenderer();
        renderer1.setAutoPopulateSeriesStroke(false);
        renderer1.setDefaultStroke(new BasicStroke(2.0f));

        XYPlot plot = new XYPlot(createSampleData(temperatureDataList), xAxis, yAxis, renderer1);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(6, 6, 20, 6));


        // create and return the chart panel...
        JFreeChart chart = new JFreeChart("Wykres temperatury "
                + TextFormatters.getPrettyTime(temperatureDataList.get(0).getDate())
                + " - " + TextFormatters.getPrettyTime(temperatureDataList.get(size - 1).getDate()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);

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
    private static XYDataset createSampleData(List<TemperatureData> temperatureDataList) {
        TimeSeriesCollection result = new TimeSeriesCollection();
        Map<String, List<TemperatureData>> map = temperatureDataList.stream().collect(Collectors.groupingBy(TemperatureData::getTransmitterName));
        Set<String> keys = map.keySet();
        for(String tName : keys){
            TimeSeries series = new TimeSeries(tName);

            List<TemperatureData> list = map.get(tName);

            for(TemperatureData td: list){

                series.add(new Minute(td.getDate().getMinute(), td.getDate().getHour(), td.getDate().getDayOfMonth(), td.getDate().getMonthValue(), td.getDate().getYear()),td.getTemperatureCelsius());
            }

            //td.getDate().getMinute(), td.getDate().getHour(), td.getDate().getDayOfMonth(), td.getDate().getMonthValue(), td.getDate().getYear())
            result.addSeries(series);
        }
        return result;
    }

}
