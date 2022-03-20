package pl.adamsiedlecki.otm.tools.charts;

import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.tools.charts.tools.ChartElementsCreator;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;
import pl.adamsiedlecki.otm.tools.uuid.UuidTool;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OvernightChartCreator implements ChartCreator {

    private final Logger log = LoggerFactory.getLogger(OvernightChartCreator.class);
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final ChartElementsCreator elemCreator;
    private final MyFilesystem myFilesystem;

    @Override
    public File createChart(List<PresentableOnChart> presentableOnChartList, int width, int height, String title, String dataAxisTitle) {
        if (presentableOnChartList.isEmpty()) {
            log.error("Cannot create chart due to no data");
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(presentableOnChartList, font, dataAxisTitle);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);

        File destination = new File(myFilesystem.getOvernightChartsPath() + UuidTool.getRandom() + ".jpg");
        try {
            ChartUtils.saveChartAsJPEG(destination, chart, width, height);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new File("");
        }
        log.info("Chart created: {}", destination.getAbsolutePath());
        return destination;
    }
}
