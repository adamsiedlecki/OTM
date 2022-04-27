package pl.adamsiedlecki.otm.devices.api.gen2;

import __project.generatedClassesPackage_.devices.gen2.client.api.Gen2DevicesApi;
import __project.generatedClassesPackage_.devices.gen2.client.invoker.ApiException;
import __project.generatedClassesPackage_.devices.gen2.model.GenericMessageInput;
import __project.generatedClassesPackage_.devices.gen2.model.GenericMessageOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.devices.api.gen2.exceptions.Gen2DevicesApiException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationGen2Service {

    private final Gen2DevicesApi gen2DevicesApi;

    public BigDecimal sendTemperatureRequest(int targetDevice, boolean canBeTransferredByOtherStations) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTg(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.TR);
        return genericSend(input, canBeTransferredByOtherStations).getTp();
    }

    public BigDecimal sendHumidityRequest(int targetDevice, boolean canBeTransferredByOtherStations) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTg(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.HR);
        return genericSend(input, canBeTransferredByOtherStations).getHu();
    }

    public BigDecimal sendBatteryVoltageRequest(int targetDevice, boolean canBeTransferredByOtherStations) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTg(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum.HC);
        return genericSend(input, canBeTransferredByOtherStations).getVol();
    }

    public String sendSleepModeRequest(int targetDevice, boolean canBeTransferredByOtherStations) {
        GenericMessageInput input = new GenericMessageInput();
        input.setTg(targetDevice);
        input.setCmm(GenericMessageInput.CmmEnum._10HS);
        return genericSend(input, canBeTransferredByOtherStations).getRes();
    }

    private GenericMessageOutput genericSend(GenericMessageInput input, boolean canBeTransferredByOtherStations) {
        if (input.getTg() == null) {
            log.error("GEN2 target station is not specified!");
        }
        if (canBeTransferredByOtherStations) {
            log.info("Setting tF flag..");
            input.setM(GenericMessageInput.MEnum.TF);
        }
        GenericMessageOutput output;
        log.info("Sending GEN2 request: \n" + input);
        try {
            output = gen2DevicesApi.sendGenericRequest(input);
            log.info("Received GEN2 response: \n" + output);
        } catch (ApiException e) {
            log.error("Exception was thrown during generic GEN2 request.");
            throw new Gen2DevicesApiException(e.getMessage());
        }
        if (!output.getA().equals(input.getTg())) {
            log.error("The gen2 response is not from the right station!");
        }
        return output;
    }

}
