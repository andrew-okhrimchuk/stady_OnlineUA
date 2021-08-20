package hospital.controller.doctor;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.patient.PatientService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/doctor")
public class DischargeController {

    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    HospitalListService hospitalListService;


    @GetMapping("/discharge/{user_id}")
    public String showdDischargeList(Model model,
                                            @NotNull @PathVariable("user_id") String userId) {
        log.debug("Start showdDischargeList");
        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            model.addAttribute("hospitalList", hospitalListService.findByParientIdAndDoctorName(userId, userNameDoctor).orElse(HospitalList.builder().dateCreate(LocalDateTime.now()).build()))
                    .addAttribute("user_id", Long.valueOf(userId));
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "doctor/dischargePatient";
    }

    @PostMapping("/discharge/{user_id}")
    public String saveFinalHospitalList(@NotNull @PathVariable("user_id") String userId,
                                   @ModelAttribute("hospitalList") @NonNull HospitalList hospitalList,
                                   Model model) {
        log.debug("Start saveFinalHospitalList,user_id {}", userId);
        log.debug("Start saveFinalHospitalList,hospitalList {}", hospitalList);
        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();
        hospitalList.setDateDischarge(LocalDateTime.now());
        hospitalList.setPatientId(Patient.chilerBuilder().id(Long.valueOf(userId)).build());

        try {
            hospitalListService.updateDateDischarge(hospitalList);
            model.addAttribute("errorMessage", "Save Ok.");
            return "redirect:/doctor/patients";
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        try {
            model.addAttribute("hospitalList", hospitalListService.findByParientIdAndDoctorName(userId, userNameDoctor).orElse(HospitalList.builder().dateCreate(LocalDateTime.now()).build()))
                    .addAttribute("user_id", Long.valueOf(userId));

        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "doctor/dischargePatient";
    }
}

