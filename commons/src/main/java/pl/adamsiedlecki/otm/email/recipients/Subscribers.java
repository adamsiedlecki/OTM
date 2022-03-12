package pl.adamsiedlecki.otm.email.recipients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Subscribers {
    private int id;
    private String email;
    private int locationPlaceId;
}
