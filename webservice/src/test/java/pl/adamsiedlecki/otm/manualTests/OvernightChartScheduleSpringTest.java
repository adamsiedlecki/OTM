package pl.adamsiedlecki.otm.manualTests;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.config.OtmMailProperties;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;
import pl.adamsiedlecki.otm.schedule.OvernightChartSchedule;
import pl.adamsiedlecki.otm.test.utils.TestDataUtils;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

public class OvernightChartScheduleSpringTest extends BaseSpringTest {

    @Autowired
    private OvernightChartSchedule sut;

    @MockBean
    private TemperatureDataService temperatureDataService;

    @MockBean
    private OtmMailProperties otmMailProperties;

    @Test(enabled = false)
    public void shouldReturnResponse() {
        //given
        Mockito.when(temperatureDataService.findAllLastXHours(Mockito.any())).thenReturn(TestDataUtils.prepareTemperatureDataList(5));
        Mockito.when(otmMailProperties.getEmailHost()).thenReturn("smtp.gmail.com");
        Mockito.when(otmMailProperties.getEmailPort()).thenReturn("587");
        Mockito.when(otmMailProperties.getEmailUsername()).thenReturn("email");
        Mockito.when(otmMailProperties.getEmailPassword()).thenReturn("password");

        // when
        sut.createAndPostChart();

        //then

    }
}
