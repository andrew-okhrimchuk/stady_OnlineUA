package hospital.domain.enums;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
public enum Speciality {
    ALL, LORE, SPECIALITY, GYNECOLOGIST;
}
