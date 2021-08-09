package hospital.persistence;

import hospital.domain.Patient;
import hospital.domain.User;
import hospital.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
class PatientJPARepositoryTest extends BaseTestDao {
    @Autowired
    PatientJPARepository patientJPARepository;
    List<Role> role = new ArrayList<>(Arrays.asList(Role.ADMIN));
    Patient patient = new Patient();

    @BeforeEach
    void init () {
        patient = new Patient();
        patient.setPassword("123");
        patient.setUsername("Andrew-123");
        patient.setAuthorities(role);
    }

    @Test
    void findByUsername() {
      /*  Patient expect = patientJPARepository.save(patient);
        patientJPARepository.findByUsername(patient.getUsername());
        assertEquals(expect, actual);*/

        assertEquals(patientJPARepository.save(patient), patient);
    }
}