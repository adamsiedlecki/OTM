package pl.adamsiedlecki.OTM.springIntegrationTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.adamsiedlecki.OTM.OtmApplication;
import pl.adamsiedlecki.OTM.config.OtmConfigProperties;

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
    public void shouldReturnCorreectValueForProperty() {
        //given

        //when
        String result = sut.getApiAddress();
        String expectedProperty = env.getProperty("api.address");

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotBlank();

        assertThat(expectedProperty).isNotNull();
        assertThat(expectedProperty).isNotBlank();

        assertThat(result).isEqualTo(expectedProperty);
    }
}
