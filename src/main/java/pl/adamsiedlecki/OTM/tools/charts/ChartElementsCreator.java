package pl.adamsiedlecki.OTM.tools.charts;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

class ChartElementsCreator {

    XYDataset createSampleData(List<TemperatureData> temperatureDataList) {

        TimeSeriesCollection result = new TimeSeriesCollection();
        Map<String, List<TemperatureData>> map = temperatureDataList.stream().collect(Collectors.groupingBy(TemperatureData::getTransmitterName));
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        for (String tName : keys) {
            TimeSeries series = new TimeSeries(tName);

            List<TemperatureData> list = map.get(tName);
            list.sort(Comparator.comparing(TemperatureData::getDate));
            LocalDateTime previous = list.get(0).getDate();

            for (TemperatureData td : list) {
                // adding null when three is no data
                if (td.getDate().minusHours(2).isAfter(previous)) {
                    LocalDateTime date = td.getDate().minusHours(1);
                    series.addOrUpdate(new Minute(date.getMinute(), date.getHour(), date.getDayOfMonth(), date.getMonthValue(), date.getYear()), null);
                }
                previous = td.getDate();
                series.addOrUpdate(new Minute(previous.getMinute(), previous.getHour(), previous.getDayOfMonth(), previous.getMonthValue(), previous.getYear()), td.getTemperatureCelsius());
            }

            result.addSeries(series);
        }
        return result;
    }

    XYPlot createXYPlot(List<TemperatureData> temperatureDataList, Font font) {
        DateAxis xAxis = new DateAxis("Czas");
        xAxis.setTickLabelFont(font);

        NumberAxis yAxis = new NumberAxis("Temperatura w °C");
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setTickLabelFont(font);

        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();

        renderer1.setDefaultStroke(new BasicStroke(4.0f));
        renderer1.setAutoPopulateSeriesStroke(false);
        renderer1.setDefaultItemLabelGenerator(new StandardXYItemLabelGenerator());
        renderer1.setDefaultItemLabelsVisible(true);

        XYPlot plot = new XYPlot(createSampleData(temperatureDataList), xAxis, yAxis, renderer1);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(6, 6, 20, 6));

        return plot;
    }
}
