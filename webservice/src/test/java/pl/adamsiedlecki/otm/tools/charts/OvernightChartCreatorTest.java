package pl.adamsiedlecki.otm.tools.charts;

import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.test.utils.TestDataUtils;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;
import pl.adamsiedlecki.otm.tools.files.MyFilesystem;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class OvernightChartCreatorTest extends BaseSpringTest {

    @Autowired
    private OvernightChartCreator overnightChartCreator;

    @Autowired
    private OtmConfigProperties config;

    @Autowired
    private MyFilesystem myFilesystem;

    @AfterClass
    private void clean() {
        myFilesystem.removeAllFilesFromFileSystem();
    }

    @Test
    public void shouldCreateChart() {
        //given
        List<PresentableOnChart> tempList = preparePresentableOnChartDataList();

        //when
        File file = overnightChartCreator.createChart(tempList, 1000, 500, "just simple chart title", "Temperature");

        //then
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(file::exists);
        assertTrue(file.exists());
    }

    @Test
    public void shouldCreateChartUsingRandomData() {
        //given
        List<PresentableOnChart> tempList = TestDataUtils.preparePresentableOnChartDataList(6);

        //when
        File file = overnightChartCreator.createChart(tempList, 1000, 500, "just simple chart title", "Temperature");

        //then
        Awaitility.await().atMost(20, TimeUnit.SECONDS).until(file::exists);
        assertTrue(file.exists());
    }

    @Test
    public void shouldCreateChartUsingRandomDataWithRealDimensions() {
        //given
        List<PresentableOnChart> tempList = TestDataUtils.preparePresentableOnChartDataList(6);

        //when
        File file = overnightChartCreator.createChart(tempList, config.getDefaultChartWidth(), config.getDefaultChartHeight(), "just simple chart title", "Temperature");

        //then
        Awaitility.await().atMost(20, TimeUnit.SECONDS).until(file::exists);
        assertTrue(file.exists());
    }

    private List<PresentableOnChart> preparePresentableOnChartDataList() {
        TemperatureData td1 = new TemperatureData();
        TestDataUtils.setData(td1, "test-t1", LocalDateTime.of(2021, 11, 1, 13, 0), 13);
        TemperatureData td2 = new TemperatureData();
        TestDataUtils.setData(td2, "test-t1", LocalDateTime.of(2021, 11, 1, 13, 30), 12);
        TemperatureData td3 = new TemperatureData();
        TestDataUtils.setData(td3, "test-t1", LocalDateTime.of(2021, 11, 1, 14, 0), 12);
        TemperatureData td4 = new TemperatureData();
        TestDataUtils.setData(td4, "test-t1", LocalDateTime.of(2021, 11, 1, 15, 0), 5);

        TemperatureData td5 = new TemperatureData();
        TestDataUtils.setData(td5, "test-t2", LocalDateTime.of(2021, 11, 1, 13, 0), 8);
        TemperatureData td6 = new TemperatureData();
        TestDataUtils.setData(td6, "test-t2", LocalDateTime.of(2021, 11, 1, 13, 30), 7);
        TemperatureData td7 = new TemperatureData();
        TestDataUtils.setData(td7, "test-t2", LocalDateTime.of(2021, 11, 1, 14, 0), 6.5f);
        TemperatureData td8 = new TemperatureData();
        TestDataUtils.setData(td8, "test-t2", LocalDateTime.of(2021, 11, 1, 15, 0), 13);

        return new ArrayList<>(List.of(td1, td2, td3, td4, td5, td6, td7, td8));
    }
}