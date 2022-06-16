package pl.adamsiedlecki.otm.manualTests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.schedule.StationsInDangerTemperatureSchedule;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

@Slf4j
public class StationsInDangerScheduleSpringTest extends BaseSpringTest {

    @Autowired
    private StationsInDangerTemperatureSchedule sut;

    @Test(enabled = false)
    public void shouldReturnResponse() {
        //given

        // when
        sut.checkTemperatures();

        //then
        log.info("Check logs");

    }
}
