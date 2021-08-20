package hospital.domain;

import hospital.domain.enums.Role;
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
@PrimaryKeyJoinColumn(referencedColumnName ="id")
public class Nurse extends User {

    @Transient
    private String countOfPatients;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "nurses")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Patient> patients;

    @Builder(builderMethodName = "chilerBuilder")
    public Nurse(Long id, @NotNull @NotEmpty @NotBlank(message = "Name is mandatory") String username, List<Role> authorities, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        super(id, username, authorities, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, false);
    }

    @PostLoad
    private void resetSyncTime() {
        countOfPatients = String.valueOf(patients.size());
    }
}
