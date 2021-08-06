package hospital.domain;

import hospital.domain.enums.Role;
import hospital.domain.enums.Speciality;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
//@Builder
@SuppressWarnings("PMD")
public class Doctor extends User {
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    @Builder(builderMethodName = "chilerBuilder")
    public Doctor(Long id, @NotNull @NotEmpty @NotBlank(message = "Name is mandatory") String username, List<Role> authorities, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, Speciality speciality) {
        super(id, username, authorities, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        this.speciality = speciality;
    }
}
