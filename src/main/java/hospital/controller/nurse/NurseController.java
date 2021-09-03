package hospital.controller.nurse;

import hospital.domain.MedicationLog;
import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.interfaces.IMedicationLogService;
import hospital.services.patient.PatientService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/nurse")
public class NurseController {

    @Autowired
    PatientService userService;
    @Autowired
    IMedicationLogService medicationLogService;
    @Autowired
    HospitalListService hospitalListService;


    @GetMapping("/patients")
    public String getParientsFromDoctor(@RequestParam("page1") Optional<Integer> page1,
                                        @RequestParam("size") Optional<Integer> size,
                                        @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getParientsFromDoctor");
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);

        String userNameDoctor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Page<Patient> patients = userService.getAllByNursesIsContaining(
                    SelectDTO.builder()
                            .userNameDoctor(userNameDoctor)
                            .isShowAllCurrentPatients(true)
                            .isSortByDateOfBirth(false)
                            .build(),
                    PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(patients);
            setPageNumbersPatient(model, patients);
            model.addAttribute("SelectDTO", selectDTO);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "nurse/patients";
    }


    @GetMapping("/patients/medicationLog/{userId}")
    public String getMedicationLog(@NotNull @PathVariable("userId") String hospitalListId,
                                   @RequestParam("page1") Optional<Integer> page1,
                                        @RequestParam("size") Optional<Integer> size,
                                        @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getMedicationLog");
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);
        try {
            Page<MedicationLog> medicationLogs = medicationLogService.findByPatientId(Long.valueOf(hospitalListId), PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(medicationLogs);
            setPageNumbers(model, medicationLogs);
            model.addAttribute("SelectDTO", selectDTO)
                    .addAttribute("hospitalListId", Long.valueOf(hospitalListId));
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error("getMedicationLog  {}" , e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        log.debug("End getMedicationLog");
        return "nurse/medicationLog";
    }


    @GetMapping("/medicationLog/done/{hospitalListId}/{medicationlogId}")
    public String doneMedicationLog(Model model,
                                   @NotNull @PathVariable("hospitalListId") String hospitalListId,
                                   @NotNull @PathVariable("medicationlogId") String medicationlogId) {
        log.debug("Start doneMedicationLog, hospitalListId = {}, medicationlogId = {}", hospitalListId, medicationlogId);
        String executor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            MedicationLog medicationLog = MedicationLog.builder().medicationlogId(Long.valueOf(medicationlogId)).executor(executor).build();
            medicationLogService.done(medicationLog);
            model.addAttribute("errorMessage", "Save Ok.");
            return "redirect:/nurse/patients/medicationLog/"+ hospitalListId;
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        StringBuilder sb = new  StringBuilder();
        sb.append("/nurse/patients/medicationLog/").append(hospitalListId);
        return sb.toString();
    }

    private void setPageNumbersPatient(Model model, Page<Patient> medicationLogs) {
        int totalPages = medicationLogs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
    private void setPageNumbers(Model model, Page<MedicationLog> medicationLogs) {
        int totalPages = medicationLogs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
