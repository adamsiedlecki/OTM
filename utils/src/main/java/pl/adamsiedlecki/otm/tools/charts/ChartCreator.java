package pl.adamsiedlecki.otm.tools.charts;

import pl.adamsiedlecki.otm.db.PresentableOnChart;

import java.io.File;
import java.util.List;


public interface ChartCreator {

     File createChart(List<PresentableOnChart> presentableOnChartList, int width, int height, String title, String dataAxisLabel);

}
