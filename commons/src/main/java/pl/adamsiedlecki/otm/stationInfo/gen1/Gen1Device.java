package pl.adamsiedlecki.otm.stationInfo.gen1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Gen1Device {
    private String originalName;
    private String aliasName;
    private String longitude;
    private String latitude;
}
