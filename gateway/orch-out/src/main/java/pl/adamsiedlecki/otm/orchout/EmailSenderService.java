package pl.adamsiedlecki.otm.orchout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.orchout.model.FileMapEntry;
import pl.adamsiedlecki.orchout.model.SendEmailInput;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final OrchOutApiClient orchOutApiClient;

    public void send(String recipientEmail, String subject, String htmlText, Map<String, File> images) {

        List<FileMapEntry> fileMapEntries = new ArrayList<>();
        images.forEach((keyString, file) -> fileMapEntries.add(new FileMapEntry().key(keyString).value(convertToBase64(file))));

        SendEmailInput sendEmailInput = new SendEmailInput()
                .emailRecipient(recipientEmail)
                .emailSubject(subject)
                .emailHtmlContent(htmlText)
                .emailImages(fileMapEntries);
        orchOutApiClient.sendEmail(sendEmailInput);
    }

    private String convertToBase64(File file){
        try {
            return new String(Base64.getEncoder().encode(Files.readAllBytes(file.toPath())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error while converting file to base64 string", e);
            return null;
        }
    }


}
