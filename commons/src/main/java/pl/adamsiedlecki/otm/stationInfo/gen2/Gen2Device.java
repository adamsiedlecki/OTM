package pl.adamsiedlecki.otm.stationInfo.gen2;

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
    private long locationPlaceId;

    public long getLocationPlaceId() {
        return locationPlaceId;
    }
}
