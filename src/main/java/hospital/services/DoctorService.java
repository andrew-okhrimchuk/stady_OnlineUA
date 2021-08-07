package hospital.services;

import hospital.domain.Doctor;
import hospital.domain.enums.Role;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorService implements IDoctorService {
    @Autowired
    private DoctorJPARepository jpaRepository;
    @Autowired
    private DoctorSpecification specification;
    @Autowired
    private Environment env;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;
    @Autowired
    private EntityManagerFactory emf;


    @Override
    public List<Doctor> getAll(SelectDTO selectDTO) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.PATIENT);
        try {
            return jpaRepository.findAll(specification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<Doctor> getAll() throws ServiceExeption {
        log.debug("Start getListDoctors of SelectDTO");
        SelectDTO selectDTO = new SelectDTO();
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            return jpaRepository.findAll(specification.getUsers(selectDTO));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListDoctors {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Doctor save(DoctorDTO doctorDTO) throws ServiceExeption {
        log.debug("Start savePatient of User. userDTO = {}", doctorDTO);
        Doctor user = null;
        try {
            user = convertToEntity(doctorDTO);
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
    public Doctor getDoctorById(long id) {
        return jpaRepository.getDoctorById(id);
    }

    public Doctor convertToEntity(DoctorDTO doctorDTO) throws DateTimeParseException, NotValidExeption {
        // TODO check is all parametres ? !!!
        if (!doctorDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALOD"));
        }
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctor.setPassword(bcryptPasswordEncoder.encode(doctor.getPassword()));
        return doctor;
    }

 //
    public List<DoctorDTO> findAllWithCount() throws ServiceExeption {
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createQuery("select u.username, u.id, count(u.id) from User u join Doctor d on u.id = d.id left join Patient p on d.id = p.doctor.id group by u.id");
        List<Object[]> resultList;
        try {
            resultList = query.getResultList();
            return resultList
                    .stream()
                    .map(item -> {
                        Doctor doctor = new Doctor();
                        doctor.setUsername((String) item[0]);
                        doctor.setId((Long) item[1]);
                        doctor.setCountOfPatients(String.valueOf ((long) item[2]));
                        return doctor;
                    })
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("savePatient {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public List<DoctorDTO> convertToDto(List<Doctor> doctors) {
        return doctors
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public DoctorDTO convertToDto(Doctor doctor) {
        DoctorDTO doctorDTO = modelMapper.map(doctor, DoctorDTO.class);
        doctorDTO.setId(String.valueOf(doctor.getId()));
        return doctorDTO;
    }
}
