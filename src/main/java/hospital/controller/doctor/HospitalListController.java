package hospital.controller.doctor;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.dto.DoctorDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.patient.PatientService;
import hospital.controller.MainController;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/doctor")
public class HospitalListController {

    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    HospitalListService hospitalListService;


    @GetMapping("/hospital-list/edit/{user_id}")
    public String showHospitalLis(Model model,
                                            @NotNull @PathVariable("user_id") String userId) {
        log.debug("Start showHospitalLis");
        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            model.addAttribute("hospitalList", hospitalListService.findByParientIdAndDoctorName(userId, userNameDoctor).orElse(HospitalList.builder().dateCreate(LocalDateTime.now()).build()))
                    .addAttribute("user_id", Long.valueOf(userId));
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "doctor/hospital-list";
    }

    @PostMapping("/hospital-list/edit/{user_id}")
    public String editHospitalList(@NotNull @PathVariable("user_id") String userId,
                                   @ModelAttribute("hospitalList") @NonNull HospitalList hospitalList,
                                   Model model) {
        log.debug("Start editHospitalList, {}", hospitalList);
        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();
        hospitalList.setDoctorName(userNameDoctor);
        hospitalList.setPatientId(Patient.chilerBuilder().id(Long.valueOf(userId)).build());
        try {
            hospitalListService.save(hospitalList, 0L);
            model.addAttribute("errorMessage", "Save Ok.");
            return "redirect:/doctor/patients";
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
        return "doctor/hospital-list";
    }
}

