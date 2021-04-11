package pl.adamsiedlecki.OTM.db.user.userRole;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class UserAuthority implements GrantedAuthority {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String role;

    public UserAuthority(String role) {
        this.role = role;
    }

    public UserAuthority() {
    }

    @Override
    public String getAuthority() {
        return role;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                '}';
    }
}

