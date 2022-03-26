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
@RequiredArgsConstructor
@Slf4j
public class ForecastChartCreator implements ChartCreator {

    private final ChartElementsCreator elemCreator = new ChartElementsCreator();
    private final Font font = new Font("Dialog", Font.PLAIN, 14);
    private final MyFilesystem myFilesystem;

    @Override
    public File createChart(List<? extends PresentableOnChart> presentableList, int width, int height, String title, String dataAxisTitle) {
        presentableList.sort(Comparator.comparing(PresentableOnChart::getTime));
        if (presentableList.isEmpty()) {
            log.error("Cannot create chart due to no data");
            return new File("");
        }

        XYPlot plot = elemCreator.createXYPlot(presentableList, font, dataAxisTitle);

        // create and return the chart panel...
        JFreeChart chart = new JFreeChart(title
                + TextFormatters.getPretty(presentableList.get(0).getTime())
                + "  -  " + TextFormatters.getPretty(presentableList.get(presentableList.size() - 1).getTime()),
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setItemFont(font);
        chart.setBackgroundPaint(Color.YELLOW);

        File destination = new File(myFilesystem.getForecastChartsPath() + UuidTool.getRandom() + ".jpg");
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
