package pl.adamsiedlecki.otm.devices.api.gen2;

import __project.generatedClassesPackage_.devices.gen2.client.api.Gen2DevicesApi;
import __project.generatedClassesPackage_.devices.gen2.client.invoker.ApiException;
import __project.generatedClassesPackage_.devices.gen2.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.devices.api.gen2.exceptions.Gen2DevicesApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureGen2Service {

    private final Gen2DevicesApi gen2DevicesApi;

    public List<TemperatureOutput> sendTemperatureRequest(String targetDevice) {
        TemperatureInput temperatureInput = new TemperatureInput();
        temperatureInput.setTarget(targetDevice);
        temperatureInput.setCommand(TemperatureInput.CommandEnum.TEMPREQ);
        try {
            return gen2DevicesApi.sendRequestForTemperature(temperatureInput);
        } catch (ApiException e) {
            log.error("Exception was thrown during temperature request");
            throw new Gen2DevicesApiException(e.getMessage());
        }
    }

    public List<String> sendHandoverMessage(List<Integer> devicesList) {
        GenericMessageToHandoverInput input = new GenericMessageToHandoverInput();
        input.c(GenericMessageToHandoverInput.CEnum.HANDOVER).recC(5);
        input.rePa(devicesList);
        try {
            return gen2DevicesApi.sendGenericMessageToHandover(input);
        } catch (ApiException e) {
            log.error("Exception was thrown during mnessage handover request");
            throw new Gen2DevicesApiException(e.getMessage());
        }
    }

    public List<HealthCheckOutput> sendHealthCheckRequest(String targetDevice) {
        HealthCheckInput healthCheckInput = new HealthCheckInput();
        healthCheckInput.setTarget(targetDevice);
        healthCheckInput.setCommand(HealthCheckInput.CommandEnum.HEALTHCHECK);
        try {
            return gen2DevicesApi.sendRequestForHealthCheck(healthCheckInput);
        } catch (ApiException e) {
            log.error("Exception was thrown during health check request");
            throw new Gen2DevicesApiException(e.getMessage());
        }
    }

}
