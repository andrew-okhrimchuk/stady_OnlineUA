package hospital.web.admin;

import hospital.domain.Doctor;
import hospital.domain.Patient;
import hospital.dto.DoctorDTO;
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
public class AdminDoctorsController {
    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;


    @GetMapping("/doctors")
    public String getListDoctors(@ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getListDoctors, {}", selectDTO);

        try {
            List<Doctor> doctors = doctorService.getAll(selectDTO);
            List<DoctorDTO> users = doctorService.convertToDto(doctors);
            selectDTO.setUsers(users);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("SelectDTO", selectDTO);
        return "admin/doctors";
    }

    @GetMapping(value = {"/doctors/add"})
    public String showAddDoctor(Model model) {
        log.debug("Start showAddDoctor");
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
        return "admin/doctors-edit";
    }

    @PostMapping("/doctors/add")
    public String addDoctor (@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start addDoctor, {}", userDTO);
        model.addAttribute("user", userDTO);
        model.addAttribute("add", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/doctors";
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
        return "admin/doctors-edit";
    }

    @GetMapping(value = {"/doctors/edit/{user_id}"})
    public String showEditDoctor(Model model,
                                  @NotNull @PathVariable("user_id") String user_id) {
        log.debug("Start showEditDoctor");
        model.addAttribute("edit", true);
        model.addAttribute("user", doctorService.getDoctorById(Long.parseLong(user_id)));
        try {
            model.addAttribute("doctors", doctorService.findAllWithCount());

        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/doctors-edit";
    }

    @PostMapping("/doctors/edit")
    public String editDoctor(@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start editDoctor, {}", userDTO);
        model.addAttribute("user", userDTO);
        model.addAttribute("edit", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/doctors";
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
        return "admin/doctors-edit";
    }


}
