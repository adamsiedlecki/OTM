package pl.adamsiedlecki.otm.springIntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.OtmApplication;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class})
public class ConfigurationPropertiesSpringTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private OtmConfigProperties sut;

    @Autowired
    private Environment env;

    @Test
    public void shouldReturnValue() {
        //given

        //when
        String result = sut.getApiAddress();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotBlank();
    }

    @Test
    public void shouldReturnValueEqualToEnvironment() {
        //given

        //when
        String result = sut.getApiAddress();
        String expectedProperty = env.getProperty("otm.api.address");

        //then
        assertThat(result).isEqualTo(expectedProperty);
    }

    @Test
    public void environmentShouldBeSet() {
        //given

        //when
        String expectedProperty = env.getProperty("otm.api.address");

        //then
        assertThat(expectedProperty).isNotNull();
        assertThat(expectedProperty).isNotBlank();
    }
}
