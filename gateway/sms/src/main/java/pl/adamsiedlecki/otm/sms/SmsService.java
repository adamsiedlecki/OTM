package pl.adamsiedlecki.otm.sms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.OtherApiProperties;
import pl.adamsiedlecki.otm.db.enums.SystemEvent;
import pl.adamsiedlecki.otm.db.system.events.OtmSystemEvent;
import pl.adamsiedlecki.otm.db.system.events.OtmSystemEventService;
import pl.adamsiedlecki.otm.sms.smsplanet.SmsplanetRequest;
import pl.adamsiedlecki.otm.sms.smsplanet.SmsplanetResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    private final OtherApiProperties otherApiProperties;
    private final OtmSystemEventService otmSystemEventService;
    private LocalDateTime lastSmsSendTime = LocalDateTime.now(); //TODO naive implementation to be improved
    private int todaySmsCount;

    public void sendSms(String sender, String message, List<String> smsRecipients) {
        smsRecipients = smsRecipients.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());

        String[] receivers = smsRecipients.toArray(new String[0]);
        log.info("Sms phone list after filtering: {}", String.join(",", receivers));
        if(otherApiProperties.isSmsApiEnabled()) {
            if(canSmsBeSendBasingOnMaxDailyAmount(receivers.length)) {
                send(sender, message, receivers);
            }
        } else {
            log.error("SMS API disabled by application parameter");
        }

    }

    private boolean canSmsBeSendBasingOnMaxDailyAmount(int amountThatIsGoingToBeSendNow) {
        if (lastSmsSendTime.toLocalDate().isEqual(LocalDate.now())) {
            if(todaySmsCount +  amountThatIsGoingToBeSendNow > otherApiProperties.getSmsApiMaxDailySmsAmount()) {
                log.error("Cannot send sms, daily Sms limit would be exceeded! Amount of sms sent today:{}, limit is:{}", todaySmsCount, otherApiProperties.getSmsApiMaxDailySmsAmount());
                return false;
            } else {
                return true;
            }
        } else {
            if(amountThatIsGoingToBeSendNow > otherApiProperties.getSmsApiMaxDailySmsAmount()) {
                log.error("Cannot send sms, daily Sms limit would be exceeded! Amount of sms sent today:{}, limit is:{}", todaySmsCount, otherApiProperties.getSmsApiMaxDailySmsAmount());
                return false;
            } else {
                return true;
            }
        }
    }

    private void send(String sender, String message, String[] receivers) {
        final String key = otherApiProperties.getSmsApiKey();
        final String password = otherApiProperties.getSmsApiPassword();

        SmsplanetRequest request = SmsplanetRequest.sendSMS(key, password, sender, message, receivers);
        try {
            SmsplanetResponse response = request.execute();
            if (response.getErrorCode() == 0) {
                if (!lastSmsSendTime.toLocalDate().isEqual(LocalDate.now())) {
                    todaySmsCount = 0;
                }
                lastSmsSendTime = LocalDateTime.now();
                todaySmsCount += receivers.length;
                log.info("SMS message is sent with id: {}", response.getMessageId());
                otmSystemEventService.save(buildSmsSentEvent(receivers));
            } else {
                log.error("Error while sending SMS: {}", response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Error with connection to SMS API");
        }
    }

    private OtmSystemEvent buildSmsSentEvent(String[] receivers) {
        return OtmSystemEvent.builder()
                .systemEvent(SystemEvent.SMS_GROUP_SENT)
                .dateTime(LocalDateTime.now())
                .description(String.join(",", receivers))
                .build();
    }
}
