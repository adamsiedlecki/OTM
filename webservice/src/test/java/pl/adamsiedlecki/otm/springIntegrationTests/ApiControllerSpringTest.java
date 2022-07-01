package pl.adamsiedlecki.otm.springIntegrationTests;

import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.controller.not.secured.api.ApiController;
import pl.adamsiedlecki.otm.db.temperature.TemperatureData;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class ApiControllerSpringTest extends BaseSpringTest {

    private static final int MOCK_API_PORT = 80;

    @Autowired
    private ApiController sut;

    private MockServerClient mockServer;

    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(MOCK_API_PORT);
    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
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

}
