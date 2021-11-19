package pl.adamsiedlecki.otm.springIntegrationTests;

import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.OtmApplication;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.controller.notSecured.api.ApiController;
import pl.adamsiedlecki.otm.db.tempData.TemperatureData;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class})
public class ApiControllerSpringTest extends AbstractTestNGSpringContextTests {

    private static final String MOCK_API_ADDRESS = "127.0.0.1";
    private static final int MOCK_API_PORT = 80;

    @Autowired
    private ApiController sut;

    @MockBean
    private OtmConfigProperties properties;

    private MockServerClient mockServer;

    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(MOCK_API_PORT);

    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
    }

    @Test(dataProvider = "getCorrectDataFromEsp")
    public void shouldReturnDataCorrectly(String dataFromEsp,
                                          int responsesCount,
                                          List<String> stationIds,
                                          List<String> stationTemperatures) {
        //given
        mockServer.reset();
        mockServer.when(request()
                        .withMethod("GET")
                        .withPath("")
                )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(dataFromEsp)
                        .withDelay(TimeUnit.SECONDS, 1)
                );
        when(properties.getApiAddress()).thenReturn("http://" + MOCK_API_ADDRESS);

        //when
        List<TemperatureData> result = sut.getTemperaturesNow();

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(responsesCount);
        assertThatCorrectTemperatures(result, stationTemperatures);
        assertThatCorrectStationNames(result, stationIds);
    }

    private void assertThatCorrectStationNames(List<TemperatureData> result, List<String> expected) {
        List<String> resultTemps = result.stream().map(TemperatureData::getTransmitterName).collect(Collectors.toList());
        assertThat(resultTemps).containsExactly(expected.toArray(new String[]{}));
    }

    private void assertThatCorrectTemperatures(List<TemperatureData> result, List<String> expected) {
        List<String> resultTemps = result.stream().map(temperatureData -> temperatureData.getTemperatureCelsius().toString()).collect(Collectors.toList());
        assertThat(resultTemps).containsExactly(expected.toArray(new String[]{}));
    }

    @DataProvider(name = "getCorrectDataFromEsp")
    public Object[][] getCorrectDataFromEsp() {
        return new Object[][]{
                // order: reponseBody, responsesCount, stationIds, stationTemperatures
                arr("t1::9.49; t3::9.63; ", 2, List.of("t1", "t3"), List.of("9.49", "9.63")),
                arr("t2::9.48; t3::9.44; ", 2, List.of("t2", "t3"), List.of("9.48", "9.44")),
                arr("t1::9.49; t2::10.11; t3::-10.1; t4::-0.01; ", 4, List.of("t1", "t2", "t3", "t4"), List.of("9.49", "10.11", "-10.1", "-0.01")),
        };
    }

    /* UTILS */

    private Object[] arr(Object... objects) {
        return objects;
    }
}
