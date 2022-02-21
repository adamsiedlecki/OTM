package pl.adamsiedlecki.otm.manualTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.config.OtherApiProperties;
import pl.adamsiedlecki.otm.external.services.open.weather.OpenWeatherFetcher;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead.OpenWeatherTwoDaysAheadPojo;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class OpenWeatherFetcherSpringTest extends BaseSpringTest {

    private final String API_KEY = ""; // do not commit!
    private final String latitude = "50";
    private final String longitude = "20";

    @Autowired
    private OpenWeatherFetcher sut;

    @MockBean
    private OtherApiProperties otherApiProperties;

    @Test(enabled = false)
    public void shouldReturnResponse() {
        //given
        when(otherApiProperties.getOpenWeatherApiKey()).thenReturn(API_KEY);

        // when
        Optional<OpenWeatherTwoDaysAheadPojo> response = sut.getTwoDaysAhead(latitude, longitude);

        //then
        assertThat(response).isPresent();
        assertThat(response.get().getHourly()).isNotNull();
        assertThat(response.get().getHourly()).isNotEmpty();
        assertThat(response.get().getHourly().get(0).getTemp()).isGreaterThan(-100);
    }
}
