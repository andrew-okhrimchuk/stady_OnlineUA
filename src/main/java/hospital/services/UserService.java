package hospital.services;

import hospital.domain.Role;
import hospital.domain.User;
import hospital.dto.SelectDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.UserJPARepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService, IUserService {
    @Autowired
    private UserJPARepository userDao;
    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private Environment env;


    @PostConstruct
    public void init() {
        log.info("Start init of user");
        if (!userDao.findByUsername("admin").isPresent()) {

            userDao.save(User.builder()
                    .username("admin")
                    .birthDate(LocalDate.now())
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.ADMIN)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .isCurrentPatient(true)
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
        return userDao.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("user " + username + " was not found!"));
    }

    @Override
    public List<User> getListPatients(SelectDTO selectDTO) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            List<User> list = userDao.findAll(userSpecification.getUsers(selectDTO));
            log.debug("End getPageListPatients of SelectDTO. Find Users count = {}", list.size());
            return list;
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getListDoctors() throws ServiceExeption {
        log.debug("Start getListDoctors of SelectDTO");
        SelectDTO selectDTO = new SelectDTO();
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            List<User> list = userDao.findAll(userSpecification.getUsers(selectDTO));
            log.debug("End getPageListPatients of SelectDTO. Find Users count = {}", list.size());
            return list;
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListDoctors {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public User savePatient(User user) throws ServiceExeption {
        log.debug("Start savePatient of User. user = {}", user);
        user.getAuthorities().add(Role.PATIENT);
        try {
            User result = userDao.save(user);
            log.debug("End savePatient of User. Get result = {}", result);
            return result;
        } catch (DaoExeption  e) {
            log.error("savePatient {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
        catch ( DataIntegrityViolationException e) {
            log.error("savePatient {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), user, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Override
    public User getUserById(long id)  {
        return userDao.getUserById(id);
    }
}
