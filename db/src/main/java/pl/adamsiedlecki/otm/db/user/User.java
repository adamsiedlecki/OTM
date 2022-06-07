package pl.adamsiedlecki.otm.db.user;


import lombok.*;
import pl.adamsiedlecki.otm.db.user.userRole.UserAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@EqualsAndHashCode
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserAuthority> roles;
}
