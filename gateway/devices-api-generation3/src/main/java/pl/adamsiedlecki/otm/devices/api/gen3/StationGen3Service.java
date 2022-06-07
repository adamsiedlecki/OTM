package pl.adamsiedlecki.otm.devices.api.gen3;

import __project.generatedClassesPackage_.devices.gen3.client.api.Gen3DevicesApi;
import __project.generatedClassesPackage_.devices.gen3.client.invoker.ApiException;
import __project.generatedClassesPackage_.devices.gen3.model.GenericMessageInput;
import __project.generatedClassesPackage_.devices.gen3.model.GenericMessageOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.devices.api.gen3.exceptions.Gen3DevicesApiException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationGen3Service {

    private final Gen3DevicesApi gen3DevicesApi;

    public BigDecimal sendTemperatureRequest(int targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.TR);
        return genericSend(input).getTp();
    }

    public BigDecimal sendHumidityRequest(int targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.HR);
        return genericSend(input).getHu();
    }

    public BigDecimal sendBatteryVoltageRequest(int targetDevice) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTid(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.VR);
        return genericSend(input).getV();
    }

    private GenericMessageOutput genericSend(GenericMessageInput input) {
        if (input.getTid() == null) {
            log.error("GEN3 target station is not specified!");
        }
        GenericMessageOutput output;
        log.info("Sending GEN3 request: \n" + input);
        try {
            output = gen3DevicesApi.sendGenericRequest(input);
            log.info("Received GEN3 response: \n" + output);
        } catch (ApiException e) {
            log.error("Exception was thrown during generic GEN3 request.");
            throw new Gen3DevicesApiException(e.getMessage());
        }
        if (!output.getA().equals(input.getTid())) {
            log.error("The gen3 response is not from the right station!");
        }
        return output;
    }

}
