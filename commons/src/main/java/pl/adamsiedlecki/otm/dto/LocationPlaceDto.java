package pl.adamsiedlecki.otm.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LocationPlaceDto {
    private long id;
    private String name;
    private String town;
}
