package pl.adamsiedlecki.otm.db.location;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"longitude", "latitude"})})
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    private long id;
    private String longitude;
    private String latitude;

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

}
