package pl.adamsiedlecki.OTM.db.tempData;

import pl.adamsiedlecki.OTM.tools.TextFormatters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TemperatureData {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal temperatureCelsius;
    private String transmitterName;
    private float longitude;
    private float latitude;
    private LocalDateTime date;


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

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String toOneRowString() {
        return " "+transmitterName+" : "+temperatureCelsius+" °C "+ TextFormatters.getPrettyTime(date);
    }

    public String toOneRowStringWithoutDate() {
        return " " + transmitterName + " : " + temperatureCelsius + " °C ";
    }

    public String getTransmitterNameAndTemperature() {
        return " " + transmitterName + " : " + temperatureCelsius + " °C ";
    }

    @Override
    public String toString() {
        return "TemperatureData{" +
                "temperatureCelsius=" + temperatureCelsius +
                ", transmitterName='" + transmitterName + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureData that = (TemperatureData) o;
        return Float.compare(that.longitude, longitude) == 0 &&
                Float.compare(that.latitude, latitude) == 0 &&
                Objects.equals(temperatureCelsius, that.temperatureCelsius) &&
                Objects.equals(transmitterName, that.transmitterName) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperatureCelsius, transmitterName, longitude, latitude);
    }
}
