package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;
import pl.adamsiedlecki.otm.tools.net.Ping;

@Component
@RequiredArgsConstructor
@Slf4j
public class PingSchedule {

    private final Ping ping;
    private final OtmConfigProperties otmConfigProperties;

    /**
     * Checks availability of http devices.
     * @return areHttpDevicesAvailable
     */
    @Scheduled(cron = "0 55 * * * *")
    public boolean areHttpDevicesAvailable() {
        log.info("Ping schedule is running");

        boolean isGen1Available = pingUntilReachable(otmConfigProperties.getGen1ApiAddress(), 5);
        boolean isGen2Available = pingUntilReachable(otmConfigProperties.getGen2ApiAddress(), 5);

        return isGen1Available && isGen2Available;
    }

    private boolean pingUntilReachable(String address, int maxTryAmount) {
        boolean isReachable = false;
        for (int i = 1; i <= maxTryAmount; i++) {
            isReachable = ping.isReachable(address, 80);
            if (isReachable) {
                log.info("{} is reachable within {} try.", address, i);
                break;
            }
        }

        if (!isReachable) {
            log.info("{} is not reachable. Tried {} times", address, maxTryAmount);
        }
        return isReachable;
    }
}
