package pl.adamsiedlecki.otm.db.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDs implements UserDetailsService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDs(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;

//        if (!getUserByUsername("user").isPresent()) {
//            User user = new User();
//            user.setUsername("user");
//            user.setPassword(passwordEncoder.encode("user"));
//            userRepo.saveAndFlush(user);
//        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepo.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(userOptional.get());
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public boolean saveUser(User user) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return false;
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.saveAndFlush(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void flush() {
        userRepo.flush();
    }

    public void removeUserById(Long id) {
        Optional<User> userById = getUserById(id);
        if (userById.isPresent()) {
            User u = userById.get();
            u.setRoles(List.of());
            userRepo.flush();
            userRepo.deleteById(id);
        }

    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }
}
