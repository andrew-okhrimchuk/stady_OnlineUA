package hospital.web;

import hospital.config.security.LoginSuccessHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import hospital.domain.User;
import hospital.domain.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Controller
public class MainController {
    @RequestMapping("/")
    public String getMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(LoginSuccessHandler.ADMIN)
                || roles.contains(LoginSuccessHandler.NURSE)
                || roles.contains(LoginSuccessHandler.DOCTOR)
                || roles.contains(LoginSuccessHandler.PATIENT)
        ) {
            User user = (User) authentication.getPrincipal();
            System.out.println("user = " + user);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("roles", user.getAuthorities().stream().map(Role::getAuthority).collect(joining(",")));
            model.addAttribute("logged", true);
        }
        else model.addAttribute("logged", false);

        if (authentication.getAuthorities().isEmpty()) {

        }
        return "index";
    }

    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @RequestMapping("/admin")
    public String getAdmin() {
        return "admin";
    }

    @RequestMapping("/doctor")
    public String getDoctors() {
        return "doctor";
    }
}
