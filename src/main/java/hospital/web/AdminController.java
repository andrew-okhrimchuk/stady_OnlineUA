package hospital.web;

import hospital.domain.Doctor;
import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.DoctorService;
import hospital.services.PatientService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;


    @GetMapping("/patients")
    public String getListPatients(@ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getListPatients, {}", selectDTO);

        try {
            List<Patient> users = userService.getAll(selectDTO);
            selectDTO.setUsers(users);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("SelectDTO", selectDTO);
        return "admin/patients";
    }

    @GetMapping(value = {"/patients/add"})
    public String showAddPatient(Model model) {
        log.debug("Start showAddPatient");
        UserDTO userDTO = new UserDTO();
        model.addAttribute("add", true);
        model.addAttribute("user", userDTO);
        try {
            List<Doctor> doctor = doctorService.getAll();
            model.addAttribute("doctors", doctorService.convertToDto(doctor));

        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start addPatient, {}", userDTO);
        model.addAttribute("user", userDTO);
        model.addAttribute("add", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/patients";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        List<Doctor> doctor = null;
        // unsucces ->
        try {
            doctor = doctorService.getAll();
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctors", doctorService.convertToDto(doctor));
        return "admin/patient-edit";
    }

    @GetMapping(value = {"/patients/edit/{user_id}"})
    public String showEditPatient(Model model,
                                  @NotNull @PathVariable("user_id") String user_id) {
        log.debug("Start showEditPatient");
        model.addAttribute("edit", true);
        model.addAttribute("user", userService.getPatientById(Long.parseLong(user_id)));
        try {
            model.addAttribute("doctors", doctorService.findAllWithCount());

        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }

    @PostMapping("/patients/edit")
    public String editPatient(@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start editPatient, {}", userDTO);
        model.addAttribute("user", userDTO);
        model.addAttribute("edit", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/patients";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        List<Doctor> doctor = null;
        // unsucces ->
        try {
            doctor = doctorService.getAll();
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("doctors", doctorService.convertToDto(doctor));
        return "admin/patient-edit";
    }


}
