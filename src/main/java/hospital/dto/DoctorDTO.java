package hospital.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Service
@Getter
@Setter
@RequestScope
@ToString
public class DoctorDTO {
    private Long id;
    private String username;
    private String password;
    private String countOfPatients;
    private String speciality;

    public boolean isValid () {
        return this.username !=null && !this.username.isEmpty() && this.password !=null && !this.password.isEmpty() ;
    }
}
