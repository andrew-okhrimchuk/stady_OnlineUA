package hospital.services;

import hospital.domain.enums.Role;
import hospital.domain.User;
import hospital.persistence.UserJPARepository;
import hospital.services.intrface.IUserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Service
public class UserService implements UserDetailsService, IUserService {
    @Autowired
    private UserJPARepository jpaRepository;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;


    @PostConstruct
    public void init() {
        log.info("Start init of user");
        if (!jpaRepository.findByUsername("admin").isPresent()) {

            jpaRepository.save(User.builder()
                    .username("admin")
                    .password(bcryptPasswordEncoder.encode("admin"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.ADMIN)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
        log.info("End init of user");
    }

    /**
     * method for autentification user
     *
     * @param username is uniqe name of user
     * @return UserDetails for Spring Security
     * @throws UsernameNotFoundException when user not found
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return jpaRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }
}
