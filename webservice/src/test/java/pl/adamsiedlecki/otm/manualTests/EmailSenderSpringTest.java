package pl.adamsiedlecki.otm.manualTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import pl.adamsiedlecki.otm.EmailSenderService;
import pl.adamsiedlecki.otm.testTools.BaseSpringTest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EmailSenderSpringTest extends BaseSpringTest {

    @Autowired
    private EmailSenderService sut;

    @Test(enabled = false)
    public void shouldReturnResponse() {
        //given
        String reciepientEmail = "exampleRecipient@gmail.com"; // TO CHANGE
        String subject = "subject";
        Map<String, File> images = new HashMap<>();
        images.put("chart1", getFile("test-chart.jpg"));
        images.put("chart2", getFile("test-chart-real.jpg"));
        String htmlText =
                "<h1>OTM raport</h1>" +
                        "<img src=\"cid:chart1\">";

        // when
        sut.send(reciepientEmail, subject, htmlText, images);

        //then

    }

    private File getFile(String filename) {
        return new File(EmailSenderSpringTest.class.getClassLoader().getResource("images/"+filename).getFile());
    }
}
