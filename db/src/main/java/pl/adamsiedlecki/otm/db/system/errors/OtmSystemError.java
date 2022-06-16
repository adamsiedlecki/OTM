package pl.adamsiedlecki.otm.db.system.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.enums.SystemError;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "otm_system_error")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OtmSystemError implements PresentableOnChart {

    @Id
    @GeneratedValue
    private Long id;
    private String deviceName;
    private int deviceId;
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private SystemError systemError;


    @Override
    @JsonIgnore
    public LocalDateTime getTime() {
        return dateTime;
    }

    @Override
    @JsonIgnore
    public BigDecimal getValue() {
        return BigDecimal.valueOf(deviceId);
    }

    @Override
    @JsonIgnore
    public String getGroupName() {
        return deviceName;
    }
}
