package pl.adamsiedlecki.otm.db.health.check;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.adamsiedlecki.otm.db.PresentableOnChart;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.location.place.LocationPlace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class HealthCheckData implements PresentableOnChart {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal voltage;
    private String transmitterName;
    @ManyToOne
    private Location location;
    @ManyToOne
    private LocationPlace locationPlace;
    private LocalDateTime date;

    @Override
    @JsonIgnore
    public LocalDateTime getTime() {
        return date;
    }

    @Override
    @JsonIgnore
    public BigDecimal getValue() {
        return voltage;
    }

    @Override
    @JsonIgnore
    public String getGroupName() {
        return transmitterName;
    }
}
