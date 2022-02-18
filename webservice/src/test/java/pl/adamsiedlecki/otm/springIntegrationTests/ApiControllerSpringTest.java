package pl.adamsiedlecki.otm.springIntegrationTests;

import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.OtmApplication;
import pl.adamsiedlecki.otm.controller.not.secured.api.ApiController;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.db.temperature.TemperatureDataService;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class})
public class ApiControllerSpringTest extends AbstractTestNGSpringContextTests {

    private static final int MOCK_API_PORT = 80;

    @Autowired
    private ApiController sut;

    @Autowired
    private TemperatureDataService temperatureDataService;

    private MockServerClient mockServer;

    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(MOCK_API_PORT);
    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
    }

    @Test(dataProvider = "getCorrectGen1DataFromEsp")
    public void shouldReturnDataCorrectly(String dataFromEsp,
                                          int responsesCount,
                                          List<String> stationIds,
                                          List<String> stationIdsAfterAliasing,
                                          List<String> stationTemperatures) {
        //given
        mockServer.reset();
        //gen1
        mockServer.when(request()
                        .withMethod("GET")
                        .withPath("/")
                )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(dataFromEsp)
                        .withDelay(TimeUnit.SECONDS, 1)
                );
        //gen2
        mockServer.when(request()
                        .withMethod("POST")
                        .withPath("/sendRequest")
                )
                .respond(response()
                        .withStatusCode(200)
                        .withBody("{a:1,tp:21.37}")
                        .withDelay(TimeUnit.SECONDS, 1)
                );

        //when
        List<TemperatureData> result = sut.getCurrentTemperatures();

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(responsesCount);
        assertThatCorrectTemperatures(result, stationTemperatures);
        assertThatCorrectStationNames(result, stationIdsAfterAliasing);

        temperatureDataService.findAll().forEach(System.out::println);
    }

    @Test
    public void shouldReturnNoDataFromGen2() {
        //given
        mockServer.stop();

        //when
        List<TemperatureData> result = sut.getCurrentTemperaturesFromGen2();

        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isZero();
    }

    private void assertThatCorrectStationNames(List<TemperatureData> result, List<String> expected) {
        List<String> resultTemps = result.stream().map(TemperatureData::getTransmitterName).collect(Collectors.toList());
        assertThat(resultTemps).containsExactly(expected.toArray(new String[]{}));
    }

    private void assertThatCorrectTemperatures(List<TemperatureData> result, List<String> expected) {
        List<String> resultTemps = result.stream().map(temperatureData -> temperatureData.getTemperatureCelsius().toString()).collect(Collectors.toList());
        assertThat(resultTemps).containsExactly(expected.toArray(new String[]{}));
    }

    @DataProvider(name = "getCorrectGen1DataFromEsp")
    public Object[][] getCorrectGen1DataFromEsp() {
        return new Object[][]{
                // order: reponseBodyGen1, responsesCount, stationIds, stationIdsAfterAliasing, stationTemperatures
                arr("t1::9.49;t3::9.63; ", 3, List.of("t1", "t3", "1 stacja 2 generacji"), List.of("t1 staw", "t3 domek", "1 stacja 2 generacji"), List.of("9.49", "9.63", "21.37")),
                arr("t2::9.48;t3::9.44; ", 3, List.of("t2", "t3", "1 stacja 2 generacji"), List.of("t2 gala", "t3 domek", "1 stacja 2 generacji"), List.of("9.48", "9.44", "21.37")),
                arr("t1::9.49;t2::10.11;t3::-10.1;t4::-0.01; ", 5, List.of("t1", "t2", "t3", "t4", "1 stacja 2 generacji"), List.of("t1 staw", "t2 gala", "t3 domek", "t4", "1 stacja 2 generacji"), List.of("9.49", "10.11", "-10.1", "-0.01", "21.37")),
        };
    }

    /* UTILS */

    private Object[] arr(Object... objects) {
        return objects;
    }
}
