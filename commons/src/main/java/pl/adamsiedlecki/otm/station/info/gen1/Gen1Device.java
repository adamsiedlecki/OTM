package pl.adamsiedlecki.otm.station.info.gen1;

import lombok.*;
import pl.adamsiedlecki.otm.station.info.Traceable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Gen1Device implements Traceable {
    private String originalName;
    private String aliasName;
    private String longitude;
    private String latitude;
    private long locationPlaceId;
}
