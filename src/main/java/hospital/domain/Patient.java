package hospital.domain;

import hospital.domain.enums.Role;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
//@SuppressWarnings("PMD")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName ="id")
public class Patient extends User {
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Doctor doctor;

    @Builder(builderMethodName = "chilerBuilder")
    public Patient(Long id, @NotNull @NotEmpty @NotBlank(message = "Name is mandatory") String username, List<Role> authorities, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, LocalDate birthDate) {
        super(id, username, authorities, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
        this.birthDate = birthDate;
    }
}
