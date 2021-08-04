package hospital.web;

import hospital.domain.User;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.DaoExeption;
import hospital.exeption.ServiceExeption;
import hospital.services.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/patients")
    public String getListPatients(@ModelAttribute @NonNull SelectDTO selectDTO, Model model) {
        log.debug("Start getListPatients, {}", selectDTO);

        try {
            List<User> users = userService.getListPatients(selectDTO);
            selectDTO.setUsers(users);
        } catch (ServiceExeption e) {
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
            model.addAttribute("doctors", userService.getListDoctors());
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
            User user = convertToEntity(userDTO);
            userService.savePatient(user);
            return "redirect:/admin/patients";
        } catch (DateTimeParseException | ServiceExeption e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "admin/patient-edit";
    }

    @RequestMapping
    public String getAdmin2() {
        log.debug("Start getAdmin2");
        return "admin/admin";
    }

    private User convertToEntity(UserDTO userDTO) throws DateTimeParseException {
        User user = modelMapper.map(userDTO, User.class);
        user.setBirthDate(userDTO.convertToEntityAttribute(userDTO.getBirthDate()));
        user.setCurrentPatient(userDTO.getIsCurrentPatient() != null);

        if (userDTO.getId() != null && !userDTO.getId().isEmpty()) {
            User oldUser = userService.getUserById(Long.parseLong(userDTO.getId()));
            user.setId(oldUser.getId());
        }
        return user;
    }

    private UserDTO convertToDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setBirthDate(userDTO.convertToDatabaseColumn(user.getBirthDate()));
        userDTO.setIsCurrentPatient(user.isCurrentPatient() ? "on" : null);
        userDTO.setId(String.valueOf(user.getId()));
        return userDTO;
    }
}
