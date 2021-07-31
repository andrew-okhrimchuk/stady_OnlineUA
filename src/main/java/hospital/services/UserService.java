package hospital.services;

import hospital.domain.Role;
import hospital.domain.User;
import hospital.persistence.UserJPARepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserJPARepository userDao;

    @PostConstruct
    public void init() {
        if (!userDao.findByUsername("admin").isPresent()) {

            userDao.save(User.builder()
                    .username("admin")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.ADMIN)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                   .enabled(true)
                    .build());
        }
//
//        if (!userDao.findByUsername("power").isPresent()) {
//            userDao.save(User.builder()
//                    .username("power")
//                    .password(new BCryptPasswordEncoder().encode("power"))
//                    .authorities(ImmutableList.of(Role.POWER_USER))
//                    .accountNonExpired(true)
//                    .accountNonLocked(true)
//                    .credentialsNonExpired(true)
//                    .enabled(true)
//                    .build());
//        }
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userDao.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }
}
