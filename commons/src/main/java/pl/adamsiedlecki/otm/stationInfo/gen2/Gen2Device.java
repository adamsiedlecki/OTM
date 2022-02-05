package pl.adamsiedlecki.otm.stationInfo.gen2;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen2Device {
    private int id;
    private String name;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
}
