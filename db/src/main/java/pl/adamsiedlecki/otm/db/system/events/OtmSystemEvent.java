package pl.adamsiedlecki.otm.db.system.events;

import lombok.*;
import pl.adamsiedlecki.otm.db.enums.SystemEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otm_system_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OtmSystemEvent {

    @Id
    @GeneratedValue
    private Long id;
    private String deviceName;
    private int deviceId;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private SystemEvent systemEvent;
    private String description;

}
