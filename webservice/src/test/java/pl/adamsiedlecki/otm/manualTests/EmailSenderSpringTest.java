package pl.adamsiedlecki.otm.manualTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.EmailSenderService;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.test.utils.TestDataUtils;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;
import pl.adamsiedlecki.otm.tools.charts.OvernightChartCreator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailSenderSpringTest extends BaseSpringTest {

    @Autowired
    private EmailSenderService sut;

    @Autowired
    private OvernightChartCreator overnightChartCreator;

    @Test(enabled = false)
    public void shouldReturnResponse() {
        //given
        String reciepientEmail = "exampleRecipient@gmail.com"; // TO CHANGE
        String subject = "subject";
        List<PresentableOnChart> temperatureData1 = TestDataUtils.preparePresentableOnChartDataList(5);
        List<PresentableOnChart> temperatureData2 = TestDataUtils.preparePresentableOnChartDataList(3);
        Map<String, File> images = new HashMap<>();
        images.put("chart1", overnightChartCreator.createChart(temperatureData1, 1000, 800, "Test chart 1", "Temperature"));
        images.put("chart2", overnightChartCreator.createChart(temperatureData2, 700, 500, "Test chart 2", "Temperature"));
        String htmlText =
                "<h1>OTM raport</h1>" +
                        "<img src=\"cid:chart1\">";

        // when
        sut.send(reciepientEmail, subject, htmlText, images);

        //then

    }
}
