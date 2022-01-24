package pl.adamsiedlecki.otm.devices.api.gen2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Gen2Device {
    private int id;
    private String name;
    private String longitude;
    private String latitude;
}
