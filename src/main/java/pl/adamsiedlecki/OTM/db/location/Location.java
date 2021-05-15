package pl.adamsiedlecki.OTM.db.location;

import pl.adamsiedlecki.OTM.db.tempData.TemperatureData;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"longitude", "latitude"})}
)
public class Location {

    @Id
    @GeneratedValue
    private long id;
    private float longitude;
    private float latitude;
    @OneToMany(mappedBy = "location")
    private List<TemperatureData> temperatures;

    public Location() {
    }

    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<TemperatureData> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<TemperatureData> temperatures) {
        this.temperatures = temperatures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Float.compare(location.getLongitude(), getLongitude()) == 0 && Float.compare(location.getLatitude(), getLatitude()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLongitude(), getLatitude());
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
