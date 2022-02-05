package pl.adamsiedlecki.otm.stationInfo.gen1;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen1Device {
    private String originalName;
    private String aliasName;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
}
