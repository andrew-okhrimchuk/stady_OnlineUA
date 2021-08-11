package hospital.domain;

import hospital.domain.enums.Role;
import hospital.domain.enums.Speciality;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
//@SuppressWarnings("PMD")
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName ="id")
public class Doctor extends User {

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection(targetClass = Speciality.class)
    @CollectionTable(name = "speciality", joinColumns = @JoinColumn(name = "user_id"),
            foreignKey = @ForeignKey(name = "speciality_fk", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"))
    @Enumerated(EnumType.STRING)
    private List<Speciality> speciality = new ArrayList<>();

    @Transient
    private String countOfPatients;

    @Builder(builderMethodName = "chilerBuilder")
    public Doctor(Long id, @NotNull @NotEmpty @NotBlank(message = "Name is mandatory") String username, List<Role> authorities, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        super(id, username, authorities, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);

    }
}
