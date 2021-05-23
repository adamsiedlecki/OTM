package pl.adamsiedlecki.OTM.tools.charts;

import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import java.io.File;
import java.util.List;


public interface ChartCreator {

     File createChart(List<TemperatureData> temperatureDataList, int width, int height, String title);

}
