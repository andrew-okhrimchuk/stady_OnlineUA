package hospital.controller.doctor;

import hospital.domain.MedicationLog;
import hospital.domain.Nurse;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.interfaces.IMedicationLogService;
import hospital.services.nurse.NurseService;
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
@RequestMapping("/doctor")
public class SetNurseController {


    @Autowired
    IMedicationLogService medicationLogService;
    @Autowired
    NurseService nurseService;
    @Autowired
    PatientService userService;


    @GetMapping("/nurse/{hospitalListId}/{user_id}")
    public String getNurses(@NotNull @PathVariable("hospitalListId") String hospitalListId,
                            @NotNull @PathVariable("user_id") String userId,
                            @RequestParam("page1") Optional<Integer> page1,
                            @RequestParam("size") Optional<Integer> size,
                            @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getNurses, {} , {}", hospitalListId, userId);
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);
        try {
            List<Nurse> myNurses = nurseService.findByPatientId(Long.valueOf(userId));
            Page<Nurse> withOutPatientId = nurseService.findAllWithOutPatientId(Long.valueOf(userId), PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(withOutPatientId);
            setPageNumbers(model, withOutPatientId);
            model.addAttribute("SelectDTO", selectDTO)
                    .addAttribute("hospitalListId", hospitalListId)
                    .addAttribute("myNurses", myNurses);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error("getNurses  {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        log.debug("End getNurses");
        return "doctor/nurses";
    }


    @GetMapping("/nurse/add/{hospitalListId}/{nurseId}/{userId}")
    public String addNurse(@ModelAttribute("hospitalListId") @NonNull String hospitalListId,
                           @NotNull @PathVariable("nurseId") String id,
                           @NotNull @PathVariable("userId") String userId,
                           Model model) {
        log.debug("Start addNurse, hospitalListId {} ,nurseId {}", hospitalListId, id);
        try {
            model.addAttribute("hospitalListId", hospitalListId)
                    .addAttribute("errorMessage", "Save Ok.");
            userService.addNurseById(id, userId);
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        return "redirect:/doctor/nurse/" + hospitalListId + "/" + userId;
    }

    @GetMapping("/nurse/delete/{hospitalListId}/{nurseId}/{userId}")
    public String deleteNurse(@ModelAttribute("hospitalListId") @NonNull String hospitalListId,
                              @NotNull @PathVariable("nurseId") String nurseId,
                              @NotNull @PathVariable("userId") String userId,
                              Model model) {
        log.debug("Start addNurse, hospitalListId {} ,nurseId {}", hospitalListId, nurseId);
        try {
            model.addAttribute("hospitalListId", hospitalListId)
                    .addAttribute("errorMessage", "Save Ok.");
            userService.deleteNurseById(nurseId, userId);
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        return "redirect:/doctor/nurse/" + hospitalListId + "/" + userId;
    }


    private void setPageNumbers(Model model, Page<Nurse> medicationLogs) {
        int totalPages = medicationLogs.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }
}
