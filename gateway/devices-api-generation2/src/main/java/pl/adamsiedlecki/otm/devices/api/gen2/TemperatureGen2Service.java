package pl.adamsiedlecki.otm.devices.api.gen2;

import __project.generatedClassesPackage_.devices.gen2.client.api.Gen2DevicesApi;
import __project.generatedClassesPackage_.devices.gen2.client.invoker.ApiException;
import __project.generatedClassesPackage_.devices.gen2.model.GenericMessageToHandoverInput;
import __project.generatedClassesPackage_.devices.gen2.model.TemperatureInput;
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

    public void sendTemperatureRequest(String targetDevice) {
        TemperatureInput temperatureInput = new TemperatureInput();
        temperatureInput.setTarget(targetDevice);
        temperatureInput.setCommand(TemperatureInput.CommandEnum.TEMPREQ);
        try {
            gen2DevicesApi.sendRequestForTemperature(temperatureInput);
        } catch (ApiException e) {
            log.error("Exception was thrown during temperature request");
            throw new Gen2DevicesApiException(e.getMessage());
        }
    }

    public void sendHandoverMessage(List<Integer> devicesList) {
        GenericMessageToHandoverInput input = new GenericMessageToHandoverInput();
        input.command(GenericMessageToHandoverInput.CommandEnum.HANDOVER).recCount(5);
        input.setRelayPath(devicesList);
        try {
            gen2DevicesApi.sendGenericMessageToHandover(input);
        } catch (ApiException e) {
            log.error("Exception was thrown during temperature request");
            throw new Gen2DevicesApiException(e.getMessage());
        }
    }

}
