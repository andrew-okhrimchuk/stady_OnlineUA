package hospital.controller.admin;

import hospital.domain.enums.Speciality;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.dto.PatientDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.patient.PatientService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
public class DoctorsController {
    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;


    @GetMapping("/doctors")
    public String getListDoctors(@RequestParam("page1") Optional<Integer> page1,
                                 @RequestParam("size") Optional<Integer> size,
                                 @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getListDoctors, {}", selectDTO);
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);

        try {
            selectDTO.setSpecialities(Speciality.getAllSpeciality());
            Page<DoctorDTO> doctors = doctorService.getAll(selectDTO, PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(doctors);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        model.addAttribute("SelectDTO", selectDTO);
        return "admin/doctors";
    }

    @GetMapping(value = {"/doctors/add"})
    public String showAddDoctor(@ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start showAddDoctor");
        selectDTO.setSpecialities(Speciality.getAllSpeciality());
        selectDTO.getSpecialities().remove(Speciality.ALL);
        model.addAttribute("add", true)
                .addAttribute("user", new DoctorDTO())
                .addAttribute("SelectDTO", selectDTO);
        return "admin/doctor-edit";
    }

    @PostMapping("/doctors/add")
    public String addDoctor(@ModelAttribute("user") @Valid DoctorDTO doctorDTO,
                            BindingResult bindingResult,
                            Model model,
                            @ModelAttribute @NonNull SelectDTO selectDTO) {
        log.debug("Start addDoctor, Username = {}", doctorDTO.getUsername());
        selectDTO.setSpecialities(Speciality.getAllSpeciality());
        selectDTO.getSpecialities().remove(Speciality.ALL);
        model.addAttribute("user", doctorDTO)
                .addAttribute("add", true)
                .addAttribute("SelectDTO", selectDTO);

        if (bindingResult.hasErrors()) {
            return "admin/doctor-edit";
        }
        try {
            doctorService.save(doctorDTO);
            return "redirect:/admin/doctors";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/doctor-edit";
    }

    @GetMapping(value = {"/doctors/edit/{user_id}"})
    public String showEditDoctor(Model model, @NotNull @PathVariable("user_id") String user_id) {
        log.debug("Start showEditDoctor");
        try {
            model.addAttribute("edit", true).addAttribute("user", doctorService.getDoctorById(Long.parseLong(user_id)));
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/doctor-edit";
    }

    @PostMapping("/doctors/edit")
    public String editDoctor(@ModelAttribute("user") @Valid PatientDTO user,
                             BindingResult bindingResult,
                             Model model) {
        log.debug("Start editDoctor, id = {}", user.getId());
        model.addAttribute("user", user).
                addAttribute("edit", true);

        if (bindingResult.hasErrors()) {
            return "admin/doctor-edit";
        }
        try {
            userService.save(user);
            return "redirect:/admin/doctors";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/doctor-edit";
    }
}
