package pl.adamsiedlecki.otm.tools.charts;

import pl.adamsiedlecki.otm.db.temperature.TemperatureData;

import java.io.File;
import java.util.List;


public interface ChartCreator {

     File createChart(List<TemperatureData> temperatureDataList, int width, int height, String title);

}
