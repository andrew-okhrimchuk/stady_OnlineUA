package hospital.config.security;

import hospital.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String NURSE = "NURSE";
    public static final String PATIENT = "PATIENT";

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(ADMIN)) {
            response.sendRedirect("/" + ADMIN.toLowerCase());
        }
        if (roles.contains(DOCTOR)) {
            response.sendRedirect("/" + DOCTOR.toLowerCase());
        }
        if (roles.contains(NURSE)) {
            response.sendRedirect("/" + NURSE.toLowerCase());
        }
        if (roles.contains(PATIENT)) {
            response.sendRedirect("/" + PATIENT.toLowerCase());
        }
    }
}
