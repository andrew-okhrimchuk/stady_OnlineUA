package hospital.web.doctor;

import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.patient.PatientService;
import hospital.web.MainController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;


    @RequestMapping("/my-patients")
    public String getDoctors(Model model) {
        log.debug("Start getDoctors");
        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            SelectDTO selectDTO = SelectDTO.builder().userNameDoctor(userNameDoctor).build();
            Page<Patient> usersDTO = userService.getAll(selectDTO, PageRequest.of(0, MainController.countItemOnPage));
            model.addAttribute("UsersDTO", usersDTO);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "doctor/my-patients";
    }
}
