package pl.adamsiedlecki.OTM.testTools;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import pl.adamsiedlecki.OTM.OtmApplication;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration(classes = {OtmApplication.class})
public class BaseSpringTest extends AbstractTestNGSpringContextTests {
}
