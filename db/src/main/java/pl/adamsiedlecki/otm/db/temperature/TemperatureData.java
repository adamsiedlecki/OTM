package pl.adamsiedlecki.otm.db.temperature;

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
public class TemperatureData implements PresentableOnChart {

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
    @JsonIgnore
    public LocalDateTime getTime() {
        return date;
    }

    @Override
    @JsonIgnore
    public BigDecimal getValue() {
        return temperatureCelsius;
    }

    @Override
    @JsonIgnore
    public String getGroupName() {
        return transmitterName;
    }
}
