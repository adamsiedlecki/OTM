package pl.adamsiedlecki.otm.springIntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationPropertiesSpringTest extends BaseSpringTest {

    @Autowired
    private OtmConfigProperties sut;

    @Autowired
    private Environment env;

    @Test
    public void shouldReturnValue() {
        //given

        //when
        String result = sut.getGen1ApiAddress();

        //then
        assertThat(result)
                .isNotNull()
                .isNotBlank();
    }

    @Test
    public void shouldReturnValueEqualToEnvironment() {
        //given

        //when
        String result = sut.getGen1ApiAddress();
        String expectedProperty = env.getProperty("otm.gen1.api.address");

        //then
        assertThat(result).isEqualTo(expectedProperty);
    }

    @Test
    public void environmentShouldBeSet() {
        //given

        //when
        String expectedProperty = env.getProperty("otm.gen1.api.address");

        //then
        assertThat(expectedProperty)
                .isNotNull()
                .isNotBlank();
    }
}
