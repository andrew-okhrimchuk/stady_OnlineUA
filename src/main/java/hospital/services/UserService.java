package hospital.services;

import hospital.domain.Role;
import hospital.domain.User;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.UserJPARepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;


    @PostConstruct
    public void init() {
        log.info("Start init of user");
        if (!userDao.findByUsername("admin").isPresent()) {

            userDao.save(User.builder()
                    .username("admin")
                    .birthDate(LocalDate.now())
                    .password(bcryptPasswordEncoder.encode("admin"))
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
    public User savePatient(UserDTO userDTO) throws ServiceExeption {
        log.debug("Start savePatient of User. userDTO = {}", userDTO);
        User user = null;
        try {
            user = convertToEntity(userDTO);
            user.getAuthorities().add(Role.PATIENT);
            user = userDao.save(user);
            log.debug("End savePatient of User. Get user = {}", user);
            return user;
        } catch (DaoExeption | DateTimeParseException | NotValidExeption e) {
            log.error("savePatient {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("savePatient {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), user, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    public User convertToEntity(UserDTO userDTO) throws DateTimeParseException, NotValidExeption {
        if (!userDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_PATIENT_NOT_VALOD"));
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setBirthDate(userDTO.convertToEntityAttribute(userDTO.getBirthDate()));
        user.setCurrentPatient(userDTO.getIsCurrentPatient() != null);
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        if (userDTO.getId() != null && !userDTO.getId().isEmpty()) {
            User oldUser = getUserById(Long.parseLong(userDTO.getId()));
            user.setId(oldUser.getId());
        }
        return user;
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setBirthDate(userDTO.convertToDatabaseColumn(user.getBirthDate()));
        userDTO.setIsCurrentPatient(user.isCurrentPatient() ? "on" : null);
        userDTO.setId(String.valueOf(user.getId()));
        return userDTO;
    }
}
