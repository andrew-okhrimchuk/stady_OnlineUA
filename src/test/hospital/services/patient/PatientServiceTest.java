package hospital.services.patient;

import hospital.domain.Patient;
import hospital.dto.PatientDTO;
import hospital.dto.SelectDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.ParientNurseJPARepository;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@SpringBootTest(classes = {PatientService.class, PatientSpecification.class, ModelMapper.class})

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
    ParientNurseJPARepository parientNurseJPARepository;
    @MockBean
    public PasswordEncoder bcryptPasswordEncoder;
    List<Patient>patientList;
    PatientDTO patientDTO;

    @BeforeEach
    void init () {
        patientList = new ArrayList<>();
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setUsername("Nina");
        patient.setBirthDate(LocalDate.of(2020, 1,1));
        patientList.add(patient);
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patientList.add(patient2);

        patientDTO = new  PatientDTO();
        patientDTO.setId("1");
        patientDTO.setUsername("Nina");
        patientDTO.setBirthDate("2020-01-01");
        patientDTO.setIsActualPatient(false);
    }

    @Test
    void getAll() throws Exception {
        when(jpaRepository
                .findAll(any(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(patientList));
        Page<Patient> expect = patientService.getAll(PageRequest.of(0, 15));
        assertEquals(expect.getContent(), patientList);
    }

    @Test
    void findNullThenThrowServiceExeption() {
        when(jpaRepository
                .findAll(any(), any(PageRequest.class)))
                .thenThrow(new DaoExeption("111", new Exception()));
        assertThrows(ServiceExeption.class, () -> {
            patientService.getAll(PageRequest.of(0, 15));
        });
    }

    @Test
    void testGetAll() throws ServiceExeption {
        when(jpaRepository
                .findAll(any(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(patientList));
        Page<Patient> expect = patientService.getAll(new SelectDTO(), PageRequest.of(0, 15));
        assertEquals(expect.getContent(), patientList);
    }

    @Test
    void save() throws ServiceExeption {
        when(jpaRepository.save(any())).thenReturn(patientList.get(0));
        Patient expect = patientService.save(patientDTO);
        assertEquals(expect, patientList.get(0));
    }

    @Test
    void saveDuplicateThenThrowServiceExeption() {
        when(jpaRepository
                .save(any()))
                .thenThrow(new DataIntegrityViolationException("Duplicate", new Exception()));
        assertThrows(ServiceExeption.class, () -> {
           patientService.save(patientDTO);
        });
    }

    @Test
    void getPatientById() throws ServiceExeption {
        when(jpaRepository.getPatientById(anyLong())).thenReturn(patientList.get(0));
        PatientDTO expect = patientService.getPatientById(1L);
        assertEquals(expect, patientDTO);
    }

    @Test
    void convertToDto() {
        PatientDTO expect = patientService.convertToDto(patientList.get(0));
        assertEquals(expect, patientDTO);
    }
}