package pl.adamsiedlecki.otm.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckData;
import pl.adamsiedlecki.otm.db.health.check.HealthCheckDataService;
import pl.adamsiedlecki.otm.devices.api.gen3.AllDevicesGen3BatteryVoltageGetter;

import java.util.List;

@Component
@Scope("singleton")
@RequiredArgsConstructor
@Slf4j
public class BatteryVoltageCheckSchedule {

    private final HealthCheckDataService healthCheckDataService;
    private final AllDevicesGen3BatteryVoltageGetter allDevicesGen3BatteryVoltageGetter;

    @Scheduled(cron = "0 5 * * * *")
    public List<HealthCheckData> checkBatteryVoltage() {
        log.info("SCHEDULE 0 5 * * * * RUNNING");
        List<HealthCheckData> healthCheckData = allDevicesGen3BatteryVoltageGetter.get();
        healthCheckDataService.saveAll(healthCheckData);
        return healthCheckData;
    }
}
