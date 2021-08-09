package hospital.services.patient;

import hospital.domain.Patient;
import hospital.persistence.PatientJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = {PatientService.class, PatientSpecification.class})

class PatientServiceTest {
    @Autowired
    PatientService patientService;
    @MockBean
    private PatientJPARepository jpaRepository;
    @Autowired
    private PatientSpecification patientSpecification;
    @MockBean
    private Environment env;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    public PasswordEncoder bcryptPasswordEncoder;
    List patientList;

    @BeforeEach
    void init () {
        patientList = new ArrayList<Patient>();
        Patient patient = new Patient();
        patient.setCurrentPatient(true);
        patient.setId(1L);
        patientList.add(patient);
        Patient patient2 = new Patient();
        patient2.setCurrentPatient(true);
        patient2.setId(2L);
        patientList.add(patient2);
    }



    //@Test
    void getAll() throws Exception {
        PatientSpecs patientSpecs = new PatientSpecs();
        when(jpaRepository.findAll(patientSpecs.getUsers())).thenReturn(patientList);
        Page<Patient> expect = patientService.getAll(PageRequest.of(0, 15));
        assertEquals(expect.getContent(), patientList);
    }

    @Test
    void testGetAll() {
    }

    @Test
    void save() {
    }

    @Test
    void getPatientById() {
    }

    @Test
    void convertToEntity() {
    }

    @Test
    void convertToDto() {
    }



    class PatientSpecs {
        public Specification<Patient> getUsers() {
            return (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                return  cb.and(predicates.toArray(new Predicate[0]));
            };
        }
    }
}