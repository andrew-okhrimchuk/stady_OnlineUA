package hospital.services.doctor;

import hospital.domain.Doctor;
import hospital.domain.enums.Role;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.DoctorJPARepository;
import hospital.services.interfaces.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DoctorService implements IDoctorService {
    private final DoctorJPARepository jpaRepository;
    private final DoctorSpecification specification;
    private final Environment env;
    private final ModelMapper modelMapper;
    public final PasswordEncoder bcryptPasswordEncoder;
    private final EntityManagerFactory emf;

    public DoctorService(DoctorJPARepository jpaRepository, DoctorSpecification specification, Environment env, ModelMapper modelMapper, PasswordEncoder bcryptPasswordEncoder, EntityManagerFactory emf) {
        this.jpaRepository = jpaRepository;
        this.specification = specification;
        this.env = env;
        this.modelMapper = modelMapper;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
        this.emf = emf;
    }


    @Override
    public Page<DoctorDTO> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption {
        log.debug("Start getListPatients of SelectDTO");
        selectDTO.getAuthorities().add(Role.DOCTOR);
        try {
            return convertToDto(jpaRepository.findAll(specification.getUsers(selectDTO), pageable));
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTORS"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public Page<DoctorDTO> getAll(Pageable pageable) throws ServiceExeption {
        log.debug("Start getListDoctors of SelectDTO");
        SelectDTO selectDTO = SelectDTO.builder().authorities(new ArrayList<>(Collections.singletonList(Role.DOCTOR))).build();
        try {
            return convertToDto(jpaRepository.findAll(specification.getUsers(selectDTO), pageable));
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
    public Doctor getDoctorById(long id) throws ServiceExeption {
        try {
        return jpaRepository.getDoctorById(id);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListDoctors {}, {}", env.getProperty("GET_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public Doctor convertToEntity(DoctorDTO doctorDTO) throws DateTimeParseException, NotValidExeption {
        if (!doctorDTO.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALOD"));
        }
        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);
        doctor.setPassword(bcryptPasswordEncoder.encode(doctor.getPassword()));
      //  doctor.setSpeciality(new ArrayList<>(Arrays.asList(doctorDTO.getSpeciality())));
        return doctor;
    }

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
                        doctor.setCountOfPatients(String.valueOf((long) item[2]));
                        return doctor;
                    })
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("savePatient {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTOR"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    public Page<DoctorDTO> convertToDto(Page<Doctor> doctors) {
        List<DoctorDTO> doctorDTO = doctors.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(doctorDTO);
    }

    public DoctorDTO convertToDto(Doctor doctor) {
        List<String> speciality = doctor.getSpeciality()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        DoctorDTO doctorDTO = modelMapper.map(doctor, DoctorDTO.class);
        doctorDTO.setSpeciality(speciality);
        return doctorDTO;
    }
}
