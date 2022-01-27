package pl.adamsiedlecki.otm.db.locationPlace;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LocationPlace {

    @Id
    private long id;
    private String name;
    private String town;

    public LocationPlace() {
    }

    @Override
    public String toString() {
        return "LocationPlace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", town='" + town + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
