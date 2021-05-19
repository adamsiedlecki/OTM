package pl.adamsiedlecki.OTM.db.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Statitics {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String sKey;
    private Long sValue;

    public Statitics() {
    }

    public Statitics(String sKey, Long sValue) {
        this.sKey = sKey;
        this.sValue = sValue;
    }

    public String getsKey() {
        return sKey;
    }

    public void setsKey(String sKey) {
        this.sKey = sKey;
    }

    public Long getsValue() {
        return sValue;
    }

    public void setsValue(Long sValue) {
        this.sValue = sValue;
    }
}
