package pl.adamsiedlecki.otm.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.db.enums.SystemError;
import pl.adamsiedlecki.otm.db.system.errors.OtmSystemError;
import pl.adamsiedlecki.otm.db.system.errors.OtmSystemErrorService;
import pl.adamsiedlecki.otm.devices.api.gen3.StationGen3Service;
import pl.adamsiedlecki.otm.devices.api.gen3.exceptions.StationProbablyInDangerException;
import pl.adamsiedlecki.otm.email.recipients.People;
import pl.adamsiedlecki.otm.email.recipients.SubscribersInfo;
import pl.adamsiedlecki.otm.orchout.SmsSenderService;
import pl.adamsiedlecki.otm.station.info.gen3.Gen3Device;
import pl.adamsiedlecki.otm.tools.text.TextFormatters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationInDangerService {

    private static final int ATTEMPTS_BEFORE_REGISTERING_STATION_IN_DANGER = 3;

    private final StationGen3Service stationGen3Service;
    private final OtmSystemErrorService otmSystemErrorService;
    private final SubscribersInfo subscribersInfo;
    private final SmsSenderService smsSenderService;

    public void registerStationInDanger(Gen3Device gen3Device) {
        log.info("Station {} can be now in danger!", gen3Device.getName());
        log.info("I am going to wait for response {} times before registering this event.", ATTEMPTS_BEFORE_REGISTERING_STATION_IN_DANGER);

        int exceptionCounter = countStationProbablyInDangerExceptions(gen3Device);

        if(exceptionCounter == ATTEMPTS_BEFORE_REGISTERING_STATION_IN_DANGER) {
            log.info("It is really likely that station {} is in danger.", gen3Device.getName());
            registerEventInDatabase(gen3Device);

            List<String> smsRecipients = subscribersInfo.getPeople()
                    .stream()
                    .filter(people -> people.getLocationPlaceId() == gen3Device.getLocationPlaceId())
                    .map(People::getPhone)
                    .collect(Collectors.toList());
            sendSmsToPeople(smsRecipients, gen3Device);
        }
    }

    private void sendSmsToPeople(List<String> smsRecipients, Gen3Device gen3Device) {
        String message = String.format("Stacja %s nie odpowiedziała %d razy. Pewnie ktoś ją odłączył. Może to złodziej? Czas wykrycia: %s",
                gen3Device.getName(),
                ATTEMPTS_BEFORE_REGISTERING_STATION_IN_DANGER,
                TextFormatters.getPretty(LocalDateTime.now())
        );

        smsSenderService.sendSms(message, smsRecipients);
    }

    private void registerEventInDatabase(Gen3Device gen3Device) {
        OtmSystemError error = OtmSystemError.builder()
                                             .systemError(SystemError.STATION_NOT_AVAILABLE_OR_IN_DANGER)
                                             .dateTime(LocalDateTime.now())
                                             .deviceId(gen3Device.getId())
                                             .deviceName(gen3Device.getName())
                                             .build();
        otmSystemErrorService.save(error);
    }

    private int countStationProbablyInDangerExceptions(Gen3Device gen3Device) {
        int exceptionCounter = 0;
        for (int i = 0; i < ATTEMPTS_BEFORE_REGISTERING_STATION_IN_DANGER; i++) {
            try {
                BigDecimal bigDecimal = stationGen3Service.sendTemperatureRequest(gen3Device.getId());
                log.info("Station {} returned temperature {} so it is not in danger", gen3Device.getName(), bigDecimal);
            } catch (StationProbablyInDangerException stationProbablyInDangerException) {
                log.error("StationProbablyInDangerException is returned (response == null)");
                exceptionCounter++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Process got interrupted", e);
                }
            }
        }
        return exceptionCounter;
    }
}
