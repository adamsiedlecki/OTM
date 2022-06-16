package pl.adamsiedlecki.otm.station.info.gen3;

import lombok.*;
import pl.adamsiedlecki.otm.station.info.Traceable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen3Device implements Traceable {
    private int id;
    private String name;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
    private boolean hasBattery;
    private boolean canBeInDanger;
}
