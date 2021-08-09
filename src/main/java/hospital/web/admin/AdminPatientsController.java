package hospital.web.admin;

import hospital.domain.Patient;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.patient.PatientService;
import hospital.web.MainController;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j

@Controller
@RequestMapping("/admin")
public class AdminPatientsController {
    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;


    @GetMapping("/patients")
    public String getListPatients(@RequestParam("page1") Optional<Integer> page1,
                                  @RequestParam("size") Optional<Integer> size,
                                  @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getListPatients, {}", selectDTO);
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);

        try {
            Page<Patient> users = userService.getAll(selectDTO, PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(users);
            setPageNumbers(model, users);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("SelectDTO", selectDTO);
        return "admin/patients";
    }

    private void setPageNumbers(Model model, Page<Patient> users) {
        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }

    @GetMapping(value = {"/patients/add"})
    public String showAddPatient(Model model) {
        log.debug("Start showAddPatient");
        UserDTO userDTO = new UserDTO();
        model.addAttribute("add", true).addAttribute("user", userDTO);
        try {
            model.addAttribute("doctors", doctorService.findAllWithCount());
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start addPatient, {}", userDTO);
        model.addAttribute("user", userDTO).addAttribute("add", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/patients";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        try {
            Page<DoctorDTO>  doctor = doctorService.getAll(PageRequest.of(0, MainController.countItemOnPage));
            model.addAttribute("doctors", doctor.getContent());
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin/patient-edit";
    }

    @GetMapping(value = {"/patients/edit/{user_id}"})
    public String showEditPatient(Model model,
                                  @NotNull @PathVariable("user_id") String user_id) {
        log.debug("Start showEditPatient");
        try {  model
                .addAttribute("edit", true)
                .addAttribute("user", userService.getPatientById(Long.parseLong(user_id)))
                .addAttribute("doctors", doctorService.findAllWithCount());

        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }

    @PostMapping("/patients/edit")
    public String editPatient(@ModelAttribute("user") @NonNull UserDTO userDTO, Model model) {
        log.debug("Start editPatient, {}", userDTO);
        model.addAttribute("user", userDTO).addAttribute("edit", true);
        try {
            userService.save(userDTO);
            return "redirect:/admin/patients";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        try {
            Page<DoctorDTO> doctor = doctorService.getAll(PageRequest.of(0, MainController.countItemOnPage));
            model.addAttribute("doctors", doctor.getContent());
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }


}
