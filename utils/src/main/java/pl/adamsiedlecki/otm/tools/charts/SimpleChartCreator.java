package pl.adamsiedlecki.otm.tools.charts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartElementsCreator;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;
import pl.adamsiedlecki.otm.tools.uuid.UuidTool;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimpleChartCreator implements ChartCreator {

    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final ChartElementsCreator elemCreator;
    private final MyFilesystem myFilesystem;


    @Override
    public File createChart(List<? extends PresentableOnChart> healthCheckDataList, int width, int height, String title, String dataAxisLabel) {
        if (healthCheckDataList.isEmpty()) {
            log.error("Cannot create chart due to no data");
            return new File("");
        }
        healthCheckDataList.sort(Comparator.comparing(PresentableOnChart::getTime));

        XYPlot plot = elemCreator.createXYPlot(healthCheckDataList, font, dataAxisLabel);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(
                title
                        + TextFormatters.getPretty(healthCheckDataList.get(0).getTime())
                        + "  -  " + TextFormatters.getPretty(healthCheckDataList.get(healthCheckDataList.size() - 1).getTime()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        String path = myFilesystem.getOnDemandChartsPath() + UuidTool.getRandom() + ".jpg";
        File destination = new File(path);
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
            log.info("On-demand chart created: {}", destination.getName());
        } catch (IOException e) {
            log.error("Error while creating a chart: {}", e.getMessage());
        }
        return destination;
    }
}
