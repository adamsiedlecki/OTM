package pl.adamsiedlecki.otm.db.tempData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import pl.adamsiedlecki.otm.db.location.Location;
import pl.adamsiedlecki.otm.db.locationPlace.LocationPlace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@AllArgsConstructor
@Builder
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

    public TemperatureData() {
    }

    public LocationPlace getLocationPlace() {
        return locationPlace;
    }

    public void setLocationPlace(LocationPlace locationPlace) {
        this.locationPlace = locationPlace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(BigDecimal temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public String getTransmitterName() {
        return transmitterName;
    }

    public void setTransmitterName(String transmitterName) {
        this.transmitterName = transmitterName.trim();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

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
