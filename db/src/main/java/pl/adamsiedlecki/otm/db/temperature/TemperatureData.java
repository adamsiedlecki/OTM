package pl.adamsiedlecki.otm.db.temperature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
public class TemperatureData {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal temperatureCelsius;
    private String transmitterName;
    @ManyToOne
    private Location location;
    @ManyToOne
    private LocationPlace locationPlace;
    private LocalDateTime date;

    @JsonIgnore
    public String getTransmitterNameAndTemperature() {
        return " " + transmitterName + " : " + temperatureCelsius + " °C ";
    }

    @Override
    public String toString() {
        return "TemperatureData{" +
                "id=" + id +
                ", temperatureCelsius=" + temperatureCelsius +
                ", transmitterName='" + transmitterName + '\'' +
                ", location=" + location +
                ", locationPlace=" + locationPlace +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemperatureData)) return false;
        TemperatureData that = (TemperatureData) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTemperatureCelsius(), that.getTemperatureCelsius()) && Objects.equals(getTransmitterName(), that.getTransmitterName()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTemperatureCelsius(), getTransmitterName(), getLocation(), getDate());
    }
}
