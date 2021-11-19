package pl.adamsiedlecki.otm.tools.charts.tools;

import lombok.extern.slf4j.Slf4j;
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
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ChartElementsCreator {

    public XYDataset createSampleData(List<TemperatureData> temperatureDataList) {

        TimeSeriesCollection result = new TimeSeriesCollection();
        Map<String, List<TemperatureData>> map =
                temperatureDataList.stream().collect(Collectors.groupingBy(TemperatureData::getTransmitterName));
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

    public XYPlot createXYPlot(List<TemperatureData> temperatureDataList, Font font) {
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

        renderer1.setSeriesItemLabelFont(0, new Font(Font.MONOSPACED, Font.PLAIN, 13));
        renderer1.setSeriesItemLabelFont(1, new Font(Font.MONOSPACED, Font.PLAIN, 12));
        renderer1.setSeriesItemLabelFont(2, new Font(Font.MONOSPACED, Font.PLAIN, 11));
        renderer1.setSeriesItemLabelFont(3, new Font(Font.MONOSPACED, Font.PLAIN, 10));
        renderer1.setSeriesItemLabelFont(4, new Font(Font.MONOSPACED, Font.PLAIN, 9));
        renderer1.setSeriesItemLabelFont(5, new Font(Font.MONOSPACED, Font.PLAIN, 8));
        renderer1.setSeriesItemLabelFont(6, new Font(Font.MONOSPACED, Font.PLAIN, 7));
        renderer1.setSeriesItemLabelFont(7, new Font(Font.MONOSPACED, Font.PLAIN, 6));
        renderer1.setSeriesItemLabelFont(8, new Font(Font.MONOSPACED, Font.PLAIN, 6));
        renderer1.setSeriesItemLabelFont(9, new Font(Font.MONOSPACED, Font.PLAIN, 6));
        renderer1.setSeriesItemLabelFont(10, new Font(Font.MONOSPACED, Font.PLAIN, 6));
        renderer1.setSeriesItemLabelFont(11, new Font(Font.MONOSPACED, Font.PLAIN, 6));


        int fontSize = 13;
        List<String> colorList = List.of("#182c25", "#ffd759", "#9d867e", "#e54f6e", "#3aff03", "#2403a4",
                "#0038fa", "#ff280a", "#00ffef", "#fd00fa", "#4d8679");
        colorList = colorList.stream().distinct().collect(Collectors.toList());
        for (int i = 0; i < 11; i++) {

            renderer1.setSeriesPaint(i, Color.decode(colorList.get(i)));
            renderer1.setSeriesItemLabelFont(i, new Font(Font.MONOSPACED, Font.BOLD, fontSize));
            renderer1.setSeriesItemLabelPaint(i, renderer1.getSeriesPaint(i));
            log.info("Series no: {}, fontSize: {}, color: {}", i, fontSize, renderer1.getSeriesPaint(i));
            if (fontSize > 6) {
                fontSize--;
            }
        }

        XYPlot plot = new XYPlot(createSampleData(temperatureDataList), xAxis, yAxis, renderer1);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(6, 6, 20, 6));

        return plot;
    }
}
