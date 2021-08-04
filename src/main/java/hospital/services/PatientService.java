package hospital.services;

import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.PatientJPARepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PatientService implements IPatientService {
    @Autowired
    private PatientJPARepository userDao;
    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;


    @Override
    public List<Patient> getListPatients(SelectDTO selectDTO) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            return userDao.findAll(userSpecification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getListDoctors() throws ServiceExeption {
        log.debug("Start getListDoctors of SelectDTO");
        SelectDTO selectDTO = new SelectDTO();
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            return userDao.findAll(userSpecification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListDoctors {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Patient savePatient(UserDTO userDTO) throws ServiceExeption {
        log.debug("Start savePatient of User. userDTO = {}", userDTO);
        Patient user = null;
        try {
            user = convertToEntity(userDTO);
            user.getAuthorities().add(Role.PATIENT);
            return userDao.save(user);

        } catch (DaoExeption | DateTimeParseException | NotValidExeption e) {
            log.error("savePatient {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("savePatient {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), user, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Override
    public Patient getUserById(long id) {
        return userDao.getPatientById(id);
    }

    public Patient convertToEntity(UserDTO userDTO) throws DateTimeParseException, NotValidExeption {
        if (!userDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_PATIENT_NOT_VALOD"));
        }
        Patient user = modelMapper.map(userDTO, Patient.class);
        user.setBirthDate(userDTO.convertToEntityAttribute(userDTO.getBirthDate()));
        user.setCurrentPatient(userDTO.getIsCurrentPatient() != null);
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));/*
        if (userDTO.getId() != null && !userDTO.getId().isEmpty()) {
            User oldUser = getUserById(Long.parseLong(userDTO.getId()));
            user.setId(oldUser.getId());
        }*/
        return user;
    }

    public UserDTO convertToDto(Patient user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setBirthDate(userDTO.convertToDatabaseColumn(user.getBirthDate()));
        userDTO.setIsCurrentPatient(user.isCurrentPatient() ? "on" : null);
        userDTO.setId(String.valueOf(user.getId()));
        return userDTO;
    }
}
