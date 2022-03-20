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
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    public String toString() {
        return "HealthCheckData{" +
                "id=" + id +
                ", voltage=" + voltage +
                ", transmitterName='" + transmitterName + '\'' +
                ", location=" + location +
                ", locationPlace=" + locationPlace +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthCheckData)) return false;
        HealthCheckData that = (HealthCheckData) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVoltage(), that.getVoltage()) && Objects.equals(getTransmitterName(), that.getTransmitterName()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getLocationPlace(), that.getLocationPlace()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVoltage(), getTransmitterName(), getLocation(), getLocationPlace(), getDate());
    }

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
