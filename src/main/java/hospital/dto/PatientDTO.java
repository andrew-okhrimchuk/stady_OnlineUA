package hospital.dto;

import hospital.domain.Doctor;
import hospital.domain.enums.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@RequestScope
@ToString
@EqualsAndHashCode
public class PatientDTO {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String id;
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String birthDate;
    @NotNull
    @NotEmpty
    private String password;
    private Boolean isActualPatient;
    private List<Role> authorities = new ArrayList<>();
    private DoctorDTO doctorDTO;

    public String convertToDatabaseColumn(LocalDate entityDate) {
        return entityDate.format(formatter);
    }

    public LocalDate convertToEntityAttribute(String databaseDate) {
        LocalDate start = LocalDate.parse(databaseDate);
        start.format(formatter);
        return start;
    }
}
