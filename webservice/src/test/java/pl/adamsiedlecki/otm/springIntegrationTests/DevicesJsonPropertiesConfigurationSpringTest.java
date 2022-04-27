package pl.adamsiedlecki.otm.springIntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.station.info.gen1.Gen1Device;
import pl.adamsiedlecki.otm.station.info.gen1.Gen1DevicesInfo;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2Device;
import pl.adamsiedlecki.otm.station.info.gen2.Gen2DevicesInfo;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DevicesJsonPropertiesConfigurationSpringTest extends BaseSpringTest {

    @Autowired
    private Gen2DevicesInfo sut;

    @Autowired
    private Gen1DevicesInfo sut1;

    @Test
    public void shouldReturnGen1DataCorrectly() {
        //given

        //when
        List<Gen1Device> gen1Devices = sut1.getDevices();

        //then
        assertThat(gen1Devices).isNotNull();
        assertThat(gen1Devices.size()).isEqualTo(3);
        assertThat(gen1Devices.get(0).getOriginalName()).isEqualTo("t1");
        assertThat(gen1Devices.get(0).getAliasName()).isEqualTo("staw");
        assertThat(gen1Devices.get(0).getLongitude()).isEqualTo("20");
        assertThat(gen1Devices.get(0).getLatitude()).isEqualTo("51");
    }

    @Test
    public void shouldReturnGen2DataCorrectly() {
        //given

        //when
        List<Gen2Device> gen2Devices = sut.getDevices();

        //then
        assertThat(gen2Devices).isNotNull();
        assertThat(gen2Devices.size()).isEqualTo(1);
        assertThat(gen2Devices.get(0).getId()).isEqualTo(1);
        assertThat(gen2Devices.get(0).getName()).isEqualTo("s1g2");
        assertThat(gen2Devices.get(0).getLongitude()).isEqualTo("20");
        assertThat(gen2Devices.get(0).getLatitude()).isEqualTo("51");
    }
}
