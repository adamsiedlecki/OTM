package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.devices.api.gen2.AllDevicesGen2EnableSleepModeUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnableSleepModeAtStartOfDaySchedule {

    private final AllDevicesGen2EnableSleepModeUtil allDevicesGen2EnableSleepModeUtil;

    /**
     * In order to save battery, stations will be sleeping (at low power mode).
     * During this time (about 10 hours) stations will not be responding to any request unless they will be manually restarted.
     */
    @Scheduled(cron = "0 6 8 * * *")
    public void orderStationsToSleep() {
        log.info("Ordering stations to sleep for 10 hours");
        allDevicesGen2EnableSleepModeUtil.get();
    }
}
