package hospital.dto;

import hospital.domain.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@RequestScope
@ToString
public class UserDTO {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private String id;
    private String username;
    private String birthDate;
    private String password;
    private String isCurrentPatient;
    private List<Role> authorities = new ArrayList<>();

    public String convertToDatabaseColumn(LocalDate entityDate) {
        return entityDate.format(formatter);
    }

    public LocalDate convertToEntityAttribute(String databaseDate) {
        LocalDate start = LocalDate.parse(databaseDate);
        start.format(formatter);
        return start;
    }
    public boolean isValid () {
        return this.username !=null && this.birthDate !=null && this.password !=null && !this.username.isEmpty() && !this.birthDate.isEmpty() && !this.password.isEmpty();
    }
}
