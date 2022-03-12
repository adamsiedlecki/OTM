package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final Session session;

    public void send(String recipientEmail, String subject, String htmlText, Map<String, File> images) {
        try {
            log.info("Sending email to {}", recipientEmail);

            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);


            Multipart multipart = new MimeMultipart();

            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setContent(htmlText, "text/html; charset=UTF-8");

            multipart.addBodyPart(textBodyPart);

            addImagesToMultipart(multipart, images);

            message.setContent(multipart);


            Transport.send(message);
        } catch (Exception ex) {
            log.error("Error during email sending: {}", ex.getMessage());
        }
    }

    private void addImagesToMultipart(Multipart multipart, Map<String, File> images) throws MessagingException, IOException {
        if (images != null && images.size() > 0) {
            Set<String> setImageID = images.keySet();

            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(Part.INLINE);
                File imageFile = images.get(contentId);
                imagePart.attachFile(imageFile.getAbsolutePath());
                multipart.addBodyPart(imagePart);
            }
        }
    }
}
