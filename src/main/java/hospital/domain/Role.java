package hospital.domain;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
@ToString
public enum Role implements GrantedAuthority {
    ADMIN, DOCTOR, NURSE, PATIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
