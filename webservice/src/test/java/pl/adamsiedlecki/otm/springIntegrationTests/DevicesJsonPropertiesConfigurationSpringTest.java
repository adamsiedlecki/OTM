package pl.adamsiedlecki.otm.springIntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.OtmApplication;
import pl.adamsiedlecki.otm.stationInfo.gen2.Gen2Device;
import pl.adamsiedlecki.otm.stationInfo.gen2.Gen2DevicesInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class})
public class DevicesJsonPropertiesConfigurationSpringTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Gen2DevicesInfo sut;

    @Test
    public void shouldReturnDataCorrectly() {
        //given

        //when
        List<Gen2Device> gen2Devices = sut.get();

        //then
        assertThat(gen2Devices).isNotNull();
        assertThat(gen2Devices.size()).isEqualTo(1);
        assertThat(gen2Devices.get(0).getId()).isEqualTo(1);
        assertThat(gen2Devices.get(0).getName()).isEqualTo("1 stacja 2 generacji");
        assertThat(gen2Devices.get(0).getLongitude()).isEqualTo("20");
        assertThat(gen2Devices.get(0).getLatitude()).isEqualTo("51");
    }
}
