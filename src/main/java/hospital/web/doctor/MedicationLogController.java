package hospital.web.doctor;

import hospital.domain.HospitalList;
import hospital.domain.MedicationLog;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.hospitalList.HospitalListService;
import hospital.services.interfaces.IMedicationLogService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/doctor")
public class MedicationLogController {


    @Autowired
    IMedicationLogService medicationLogService;
    @Autowired
    HospitalListService hospitalListService;


    @GetMapping("/medicationLog/{hospitalListId}")
    public String getMedicationLog(@NotNull @PathVariable("hospitalListId") String hospitalListId,
                                   @RequestParam("page1") Optional<Integer> page1,
                                        @RequestParam("size") Optional<Integer> size,
                                        @ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getMedicationLog");
        int currentPage = page1.orElse(1);
        int pageSize = size.orElse(15);
        try {
            Page<MedicationLog> medicationLogs = medicationLogService.findByMedicationlogId(Long.valueOf(hospitalListId), PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(medicationLogs);
            setPageNumbers(model, medicationLogs);
            model.addAttribute("SelectDTO", selectDTO)
                    .addAttribute("hospitalListId", Long.valueOf(hospitalListId));
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error("getMedicationLog  {}" , e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        log.debug("End getMedicationLog");
        return "doctor/medicationLog";
    }

    @GetMapping("/medicationLog/add/{hospitalListId}")
    public String addMedicationLog(Model model,
                                            @NotNull @PathVariable("hospitalListId") String hospitalListId) {
        log.debug("Start addMedicationLog, hospitalListId = {}", hospitalListId);
        model.addAttribute("medicationLog", MedicationLog.builder().hospitallistid(Long.valueOf(hospitalListId)).dateCreate(LocalDateTime.now()).build());
        return "doctor/medicationLog-add";
    }

    @PostMapping("/medicationLog/add/{hospitalList}")
    public String addMedicationLogPost(@ModelAttribute("medicationLog") @NonNull MedicationLog medicationLog,
                                   Model model) {
        log.debug("Start addMedicationLogPost, {}", medicationLog);
       // medicationLog.setMedicationlogId(Long.valueOf(hospitalList));
        try {
            medicationLogService.save(medicationLog);
            model.addAttribute("errorMessage", "Save Ok.");
            return "redirect:/doctor/medicationLog/"+ medicationLog.getHospitallistid();
        } catch (ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        // unsucces ->
        return "doctor/medicationLog-add";
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
