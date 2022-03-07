package pl.adamsiedlecki.otm.controller.not.secured.api;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.message.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.adamsiedlecki.otm.config.FacebookApiProperties;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static com.github.messenger4j.Messenger.*;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("api/v1/messenger/callback")
public class MessengerCallbackController {

    private final FacebookApiProperties facebookApiProperties;
    private Messenger messenger;

    @PostConstruct
    public void postConstruct() {
        messenger = Messenger.create(facebookApiProperties.getPageAccessToken(),
                facebookApiProperties.getAppSecret(),
                facebookApiProperties.getVerifyToken());
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken,
                                                @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {

        log.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            log.warn("Webhook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
        try {
            messenger.onReceiveEvents(
                    payload,
                    Optional.empty(),
                    event -> {
                        final String senderId = event.senderId();
                        if (event.isTextMessageEvent()) {
                            final String text = event.asTextMessageEvent().text();

                            final TextMessage textMessage = TextMessage.create(text);
                            final MessagePayload messagePayload =
                                    MessagePayload.create(senderId, MessagingType.RESPONSE, textMessage);

                            try {
                                messenger.send(messagePayload);
                            } catch (MessengerApiException | MessengerIOException e) {
                                log.error("messenger sending message error: {}", e.getMessage());
                            }
                        }
                    });
        } catch (MessengerVerificationException e) {
            log.error("Facebook  messenger webook verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
