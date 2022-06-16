package pl.adamsiedlecki.otm.email.recipients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class People {
    private int id;
    private String email;
    private String phone;
    private int locationPlaceId;
}
