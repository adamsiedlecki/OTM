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

    private static final int MAX_TRY_AMOUNT = 5;
    private static final int PING_PORT = 80;

    private final Ping ping;
    private final OtmConfigProperties otmConfigProperties;

    /**
     * Checks availability of http devices.
     * @return areHttpDevicesAvailable
     */
    @Scheduled(cron = "0 55 * * * *")
    public boolean areHttpDevicesAvailable() {
        log.info("Ping schedule is running");

        return pingUntilReachable(otmConfigProperties.getGen1ApiAddress(), MAX_TRY_AMOUNT);
    }

    private boolean pingUntilReachable(String address, int maxTryAmount) {
        boolean isReachable = false;
        for (int i = 1; i <= maxTryAmount; i++) {
            isReachable = ping.isReachable(address, PING_PORT);
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
