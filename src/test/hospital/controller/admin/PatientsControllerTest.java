package hospital.controller.admin;

import hospital.dto.SelectDTO;
import hospital.dto.PatientDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.UserService;
import hospital.services.doctor.DoctorService;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.patient.PatientService;
import hospital.controller.config.security.SecurityConfigToTest;
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
import org.springframework.web.context.WebApplicationContext;
import java.util.ArrayList;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "qa")
@WebMvcTest({ PatientsController.class, SecurityConfigToTest.class})

class PatientsControllerTest {
    @MockBean
    private PatientService patientService;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private UserService userService;
    @MockBean
    private HospitalListService hospitalListService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mvc;

  @Test
    @WithMockUser(value = "spring")
    void getListPatients() throws Exception {
        when(patientService.getAll(any(SelectDTO.class), any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        mvc.perform(get("/admin/patients")
                .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/patients"))
                .andExpect(model().attribute("SelectDTO", notNullValue()))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void showAddPatient()throws Exception {
        when(doctorService.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        mvc.perform(get("/admin/patients/add")
                .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/patient-edit"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("add", true))
                .andExpect(model().attribute("doctors", notNullValue()))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void addPatient() throws Exception{
        mvc.perform(post("/admin/patients/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/patients"))
                .andExpect(redirectedUrl("/admin/patients"))
        ;

    }

    @Test
    @WithMockUser(value = "spring")
    void addPatientThrows() throws Exception{
        when(patientService.save(any(PatientDTO.class))).thenThrow(new ServiceExeption("", new Exception()));
        when(doctorService.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        mvc.perform(post("/admin/patients/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/patient-edit"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("add", true))
                .andExpect(model().attribute("doctors", notNullValue()));

    }

    @Test
    @WithMockUser(value = "spring")
    void showEditPatient() throws Exception{
        when(doctorService.findAllWithCount()).thenReturn(new ArrayList<>());
        when(patientService.getPatientById(anyLong())).thenReturn(new PatientDTO());

        mvc.perform(get("/admin/patients/edit/{user_id}", 1)
                .contentType(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/patient-edit"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(model().attribute("doctors", notNullValue()))
                ;
    }

    @Test
    @WithMockUser(value = "spring")
    void editPatient()throws Exception {
        mvc.perform(post("/admin/patients/edit")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/patients"))
                .andExpect(redirectedUrl("/admin/patients"))
        ;
    }

    @Test
    @WithMockUser(value = "spring")
    void editPatientThrows() throws Exception{
        when(patientService.save(any(PatientDTO.class))).thenThrow(new ServiceExeption("", new Exception()));
        when(doctorService.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        mvc.perform(post("/admin/patients/edit")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/patient-edit"))
                .andExpect(model().attribute("user", notNullValue()))
                .andExpect(model().attribute("edit", true))
                .andExpect(model().attribute("doctors", notNullValue()));

    }
}