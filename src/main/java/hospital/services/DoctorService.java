package hospital.services;

import hospital.domain.Doctor;
import hospital.domain.enums.Role;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.DoctorJPARepository;
import hospital.services.intrface.IDoctorService;
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
public class DoctorService implements IDoctorService {
    @Autowired
    private DoctorJPARepository userDao;
    @Autowired
    private DoctorSpecification specification;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;


    @Override
    public List<Doctor> getListDoctors(SelectDTO selectDTO) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            return userDao.findAll(specification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> getListDoctors() throws ServiceExeption {
        log.debug("Start getListDoctors of SelectDTO");
        SelectDTO selectDTO = new SelectDTO();
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            return userDao.findAll(specification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListDoctors {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Doctor saveDoctor(UserDTO userDTO) throws ServiceExeption {
        log.debug("Start savePatient of User. userDTO = {}", userDTO);
        Doctor user = null;
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
    public Doctor getDoctorById(long id) {
        return userDao.getPatientById(id);
    }

    public Doctor convertToEntity(UserDTO userDTO) throws DateTimeParseException, NotValidExeption {
        if (!userDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_PATIENT_NOT_VALOD"));
        }
        Doctor doctor = modelMapper.map(userDTO, Doctor.class);
        doctor.setPassword(bcryptPasswordEncoder.encode(doctor.getPassword()));
        return doctor;
    }

    public UserDTO convertToDto(Doctor user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setId(String.valueOf(user.getId()));
        return userDTO;
    }
}
