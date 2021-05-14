package pl.adamsiedlecki.OTM.db.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Location {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, columnDefinition = " ")
    private float longitude;
    @Column(nullable = false, columnDefinition = " ")
    private float latitude;

    public Location() {
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
