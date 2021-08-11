package hospital.web.admin;

import hospital.domain.Doctor;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.services.UserService;
import hospital.services.doctor.DoctorService;
import hospital.services.patient.PatientService;
import hospital.web.config.security.SecurityConfigToTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "qa")
@WebMvcTest({ AdminDoctorsController.class, SecurityConfigToTest.class})
class AdminDoctorsControllerTest {
    @MockBean
    private PatientService patientService;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;



    @Test
    @WithMockUser(value = "spring")
    void getListDoctors() throws Exception {
        when(doctorService.getAll(any(SelectDTO.class), any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        mvc.perform(get("/admin/doctors")
                .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/doctors"))
                .andExpect(model().attribute("SelectDTO", notNullValue()))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void showAddDoctor() throws Exception {
        when(doctorService.getAll(any(SelectDTO.class), any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(doctorService.convertToDto(any(Doctor.class))).thenReturn(new DoctorDTO());

        mvc.perform(get("/admin/doctors/add")
                .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/doctor-edit"))
                .andExpect(model().attribute("user", notNullValue()))
        ;
    }

    @Test
    void addDoctor() {
    }

    @Test
    void showEditDoctor() {
    }

    @Test
    void editDoctor() {
    }
}