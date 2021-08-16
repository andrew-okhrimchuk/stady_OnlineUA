package hospital.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Set;

@Slf4j
@Controller
public class MainController {
    @Autowired
    private Environment env;

    public static int countItemOnPage = 15;

    @RequestMapping("/")
    public String getMainPage(Model model) {
        log.debug("Start getMainPage");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        model.addAttribute("logged", !roles.contains("ROLE_ANONYMOUS"));
        model.addAttribute("username", !roles.contains("ROLE_ANONYMOUS") ? authentication.getName() : "Guest");
        log.debug("End getMainPage");
        return "index";
    }

    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        log.debug("Start getLogin");
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        log.debug("End getLogin, error = {}, logout = {}", error, logout);
        return "login";
    }
}
