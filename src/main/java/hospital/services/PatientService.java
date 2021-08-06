package hospital.services;

import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.PatientJPARepository;
import hospital.services.intrface.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.List;

@Slf4j
@Service
public class PatientService implements IPatientService {
    @Autowired
    private PatientJPARepository userDao;
    @Autowired
    private PatientSpecification patientSpecification;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;


    @Override
    public List<Patient> getAll(SelectDTO selectDTO) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            return userDao.findAll(patientSpecification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> getAll() throws ServiceExeption {
        log.debug("Start getListPatients");
        SelectDTO selectDTO = new SelectDTO();
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            return userDao.findAll(patientSpecification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Patient save(UserDTO userDTO) throws ServiceExeption {
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
    public UserDTO getPatientById(long id) {
        return convertToDto(userDao.getPatientById(id));
    }

    public Patient convertToEntity(UserDTO userDTO) throws DateTimeParseException, NotValidExeption {
        if (!userDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALOD"));
        }
        Patient user = modelMapper.map(userDTO, Patient.class);
        user.setBirthDate(userDTO.convertToEntityAttribute(userDTO.getBirthDate()));
        user.setCurrentPatient(userDTO.getIsCurrentPatient() != null);
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public UserDTO convertToDto(Patient patient) {
        UserDTO userDTO = modelMapper.map(patient, UserDTO.class);
        userDTO.setBirthDate(userDTO.convertToDatabaseColumn(patient.getBirthDate()));
        userDTO.setIsCurrentPatient(patient.isCurrentPatient() ? "on" : null);
        userDTO.setId(String.valueOf(patient.getId()));
        userDTO.setPassword(null);
        return userDTO;
    }
}
