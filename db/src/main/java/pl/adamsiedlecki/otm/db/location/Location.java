package pl.adamsiedlecki.otm.db.location;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"longitude", "latitude"})}
)
public class Location {

    @Id
    @GeneratedValue
    private long id;
    private String longitude;
    private String latitude;

    public Location() {
    }

    public Location(String latitude, String longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(getLongitude(), location.getLongitude()) && Objects.equals(getLatitude(), location.getLatitude());
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