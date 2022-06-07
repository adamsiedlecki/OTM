package pl.adamsiedlecki.otm;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.adamsiedlecki.otm.config.OtmMailProperties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * @see <a href="https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4PS8ccwVs5gH85MCjsfe8dY9Pxh8szYB6lQaunzATCNp0joWglAxwwo8x8xXZip-8TyfWBZmsfgIWw1Yg-R9eM9LBfqsg">
 *     Enable less secure applications
 *     </a>
 */

@Configuration
@RequiredArgsConstructor
public class MailerConfiguration {

    private final OtmMailProperties mailProperties;

    private Properties getProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", mailProperties.getEmailHost());
        prop.put("mail.smtp.port", mailProperties.getEmailPort());
        prop.put("mail.smtp.ssl.trust", "*");
        return prop;
    }

    @Bean
    public Session getMailerSession() {
        return Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailProperties.getEmailUsername(), mailProperties.getEmailPassword());
            }
        });
    }
}
