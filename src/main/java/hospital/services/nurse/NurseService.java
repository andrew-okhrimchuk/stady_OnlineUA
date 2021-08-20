package hospital.services.nurse;

import hospital.domain.Nurse;
import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.exeption.DaoExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.NurseJPARepository;
import hospital.services.interfaces.INurseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class NurseService implements INurseService {
    @Autowired
    private Environment env;
    @Autowired
    private NurseJPARepository nurseJPARepository;
    @Autowired
    public PasswordEncoder bcryptPasswordEncoder;

    @Override
    public Page<Nurse> findAllWithOutPatientId(Long id, Pageable pageable) throws ServiceExeption {
        log.debug("Start findAllWithOutPatientId");
        try {
            return nurseJPARepository.findNursesByPatientsIsNotContaining(Patient.chilerBuilder().id(id).build(), pageable);
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTORS"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @Override
    public List<Nurse> findByPatientId(Long id) throws ServiceExeption {
        log.debug("Start findByPatientId");
        try {
            return nurseJPARepository.findByPatients(Patient.chilerBuilder().id(id).build());
        } catch (DaoExeption | DataIntegrityViolationException e) {
            log.error("getListPatients {}, {}", env.getProperty("GET_ALL_ERROR_MESSAGE_DOCTORS"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init() {
        log.info("Start init of Nurse");
        if (!nurseJPARepository.findByUsername("Sony").isPresent()) {
            nurseJPARepository.save(Nurse.chilerBuilder()
                    .username("Sony")
                    .password(bcryptPasswordEncoder.encode("Sony"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.NURSE)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
        if (!nurseJPARepository.findByUsername("Мариванна").isPresent()) {
            nurseJPARepository.save(Nurse.chilerBuilder()
                    .username("Мариванна")
                    .password(bcryptPasswordEncoder.encode("Мариванна"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.NURSE)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
        if (!nurseJPARepository.findByUsername("Natasha").isPresent()) {
            nurseJPARepository.save(Nurse.chilerBuilder()
                    .username("Natasha")
                    .password(bcryptPasswordEncoder.encode("Natasha"))
                    .authorities(new ArrayList<>(Arrays.asList(Role.NURSE)))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
        log.info("End init of Nurse");
    }
}
