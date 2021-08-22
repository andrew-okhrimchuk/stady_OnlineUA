package hospital.services.patient;

import hospital.domain.Patient;
import hospital.domain.PatientNurse;
import hospital.domain.enums.Role;
import hospital.dto.SelectDTO;
import hospital.dto.PatientDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.ParientNurseJPARepository;
import hospital.persistence.PatientJPARepository;
import hospital.services.interfaces.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PatientService implements IPatientService {
    @Autowired
    ParientNurseJPARepository parientNurseJPARepository;
    private final PatientJPARepository patientJPARepository;
    private final PatientSpecification patientSpecification;
    private final Environment env;
    private final ModelMapper modelMapper;
    private final PasswordEncoder bcryptPasswordEncoder;

    public PatientService(PatientJPARepository patientJPARepository, PatientSpecification patientSpecification, Environment env, ModelMapper modelMapper, PasswordEncoder bcryptPasswordEncoder) {
        this.patientJPARepository = patientJPARepository;
        this.patientSpecification = patientSpecification;
        this.env = env;
        this.modelMapper = modelMapper;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public Page<Patient> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        try {
            return patientJPARepository.findAll(patientSpecification.getUsers(selectDTO), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }


    public Page<Patient> getAllByNursesIsContaining(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption {
        log.debug("Start getAllByNursesIsContaining of SelectDTO");
        try {
            return patientJPARepository.findAllByNursename(selectDTO.getUserNameDoctor(), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getAllByNursesIsContaining {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Page<Patient> getAll(Pageable pageable) throws ServiceExeption {
        log.debug("Start getListPatients");
        SelectDTO selectDTO =  SelectDTO.builder().authorities(new ArrayList<>(Collections.singletonList(Role.PATIENT))).build();
        try {
            return patientJPARepository.findAll(patientSpecification.getUsers(selectDTO), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Patient save(PatientDTO patientDTO) throws ServiceExeption {
        log.debug("Start savePatient of User. userDTO = {}", patientDTO);
        Patient user = null;
        try {
            user = convertToEntity(patientDTO);
            user.getAuthorities().add(Role.PATIENT);
            return patientJPARepository.save(user);

        } catch (DaoExeption | DateTimeParseException | NotValidExeption e) {
            log.error("savePatient {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("savePatient {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), user, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Override
    public PatientDTO getPatientById(long id) throws ServiceExeption {
        log.debug("Start getPatientById of User. id = {}", id);

        try {
        return convertToDto(patientJPARepository.getPatientById(id));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("findAllPatientsByNameDoctor {}, {}", env.getProperty("GET_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Page<Patient> findAllCurrentPatientsByNameDoctor(SelectDTO selectDTO,Pageable pageable) throws ServiceExeption {
        log.debug("Start findAllCurrentPatientsByNameDoctor of User. selectDTO = {}", selectDTO);
        try {
            return patientJPARepository.findAll(patientSpecification.getCurrentPatientsByDoctorName(selectDTO), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("findAllPatientsByNameDoctor {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public PatientNurse addNurseById(String nurseId, String userId) throws ServiceExeption {
        log.debug("Start addNurseById of User. id = {}", userId);
        try {
            return parientNurseJPARepository.save(PatientNurse.builder()
                    .nurses_id(Long.valueOf(nurseId))
                    .patients_user_id(Long.valueOf(userId))
                    .build());
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("addNurseById {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public int deleteNurseById(String nurseId, String userId) throws ServiceExeption {
        log.debug("Start deleteNurseById of User. id = {}", nurseId);
        try {
            return parientNurseJPARepository.deleteNurse(
                    Long.valueOf(nurseId), Long.valueOf(userId));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("deleteNurseById {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public Patient convertToEntity(PatientDTO patientDTO) throws DateTimeParseException, NotValidExeption {
        if (!patientDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALID"));
        }
        Patient user = modelMapper.map(patientDTO, Patient.class);
        user.setBirthDate(patientDTO.convertToEntityAttribute(patientDTO.getBirthDate()));
        user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
        return user;
    }

    public Page<PatientDTO> convertToDto(Page<Patient> patients) {
        List<PatientDTO> doctorDTO = patients.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(doctorDTO);
    }

    public PatientDTO convertToDto(Patient patient) {
        PatientDTO patientDTO = modelMapper.map(patient, PatientDTO.class);
        patientDTO.setBirthDate(patientDTO.convertToDatabaseColumn(patient.getBirthDate()));
        patientDTO.setId(String.valueOf(patient.getId()));
        patientDTO.setPassword(null);
        return patientDTO;
    }
}
