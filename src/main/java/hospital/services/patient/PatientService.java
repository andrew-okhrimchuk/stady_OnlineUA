package hospital.services.patient;

import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.PatientJPARepository;
import hospital.services.interfaces.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PatientService implements IPatientService {
    private final PatientJPARepository jpaRepository;
    private final PatientSpecification patientSpecification;
    private final Environment env;
    private final ModelMapper modelMapper;
    public final PasswordEncoder bcryptPasswordEncoder;

    public PatientService(PatientJPARepository jpaRepository, PatientSpecification patientSpecification, Environment env, ModelMapper modelMapper, PasswordEncoder bcryptPasswordEncoder) {
        this.jpaRepository = jpaRepository;
        this.patientSpecification = patientSpecification;
        this.env = env;
        this.modelMapper = modelMapper;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }


    @Override
    public Page<Patient> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            return jpaRepository.findAll(patientSpecification.getUsers(selectDTO), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Page<Patient> getAll(Pageable pageable) throws ServiceExeption {
        log.debug("Start getListPatients");
        SelectDTO selectDTO =  SelectDTO.builder().authorities(new ArrayList<>(Collections.singletonList(Role.PATIENT))).build();
        try {
            return jpaRepository.findAll(patientSpecification.getUsers(selectDTO), pageable);
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
            return jpaRepository.save(user);

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
        return convertToDto(jpaRepository.getPatientById(id));
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
