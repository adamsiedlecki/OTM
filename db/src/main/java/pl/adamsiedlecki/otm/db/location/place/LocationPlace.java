package pl.adamsiedlecki.otm.db.location.place;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LocationPlace {

    @Id
    private long id;
    private String name;
    private String town;

    @Override
    public String toString() {
        return "LocationPlace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", town='" + town + '\'' +
                '}';
    }
}
