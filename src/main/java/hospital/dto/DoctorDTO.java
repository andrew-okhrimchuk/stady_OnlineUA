package hospital.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@Getter
@Setter
@RequestScope
@ToString
public class DoctorDTO {
    private String id;
    private String username;
    private String countOfPatients;

    public boolean isValid () {
        return this.countOfPatients !=null && !this.countOfPatients.isEmpty() ;
    }
}
