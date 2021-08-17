package hospital.persistence;

import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.services.patient.PatientSpecification;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PatientJPARepositoryTest extends BaseTestDao {
    @Autowired
    PatientJPARepository patientJPARepository;

    List<Role> role = new ArrayList<>(Collections.singletonList(Role.ADMIN));
    Patient patient = new Patient();

    @BeforeEach
    void init() {
        patient = new Patient();
        patient.setPassword("123");
        patient.setUsername("Andrew");
        patient.setAuthorities(role);
    }

    @Test
    public void findByUsername() {
        Patient expect = patientJPARepository.save(patient);
        patientJPARepository.findByUsername(patient.getUsername());
        assertEquals(expect, patient);
    }

    @Test
    public void getPatientById() {
        Patient expect = patientJPARepository.save(patient);
        Patient actual = patientJPARepository.getPatientById(expect.getId());
        assertEquals(expect, actual);
    }
}