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
    private String countOfPatients;
    private List<String> speciality;

    public boolean isValid () {
        return this.countOfPatients !=null && !this.countOfPatients.isEmpty() ;
    }
}
