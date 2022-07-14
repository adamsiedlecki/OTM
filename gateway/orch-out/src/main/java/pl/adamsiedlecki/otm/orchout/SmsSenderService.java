package pl.adamsiedlecki.otm.orchout;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.orchout.model.SendSmsInput;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SmsSenderService {

    private final OrchOutApiClient orchOutApiClient;

    public void sendSms(String message, List<String> smsRecipients) {

        SendSmsInput input = new SendSmsInput()
                .message(message)
                .recipientsPhoneNumbers(smsRecipients);

        orchOutApiClient.sendSms(input);
    }


}
