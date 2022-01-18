package pl.adamsiedlecki.otm.db.user;


import pl.adamsiedlecki.otm.db.user.userRole.UserAuthority;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserAuthority> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserAuthority> getRoles() {
        return roles;
    }

    public void setRoles(List<UserAuthority> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, getRoles());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }


    public static final class UserBuilder {
        private String username;
        private String password;
        private List<UserAuthority> roles;

        private UserBuilder() {
        }

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withRoles(List<UserAuthority> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRoles(roles);
            return user;
        }
    }
}