package hospital.web.doctor;

import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import hospital.services.doctor.DoctorService;
import hospital.services.hospitalList.HospitalListService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/doctor")
public class PatientController {

    @Autowired
    PatientService userService;
    @Autowired
    DoctorService doctorService;
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
            Page<Patient> patients = userService.findAllPatientsByNameDoctor(userNameDoctor, PageRequest.of(currentPage - 1, pageSize));
            selectDTO.setPage(patients);
            setPageNumbers(model, patients);
            model.addAttribute("SelectDTO", selectDTO);
        } catch (ServiceExeption | ConstraintViolationException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "doctor/patients";
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
}
