package hospital.dto;

import hospital.domain.Patient;
import hospital.domain.enums.Role;
import hospital.domain.User;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
@RequestScope
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectDTO {
    private Page page;
    private String hideArchive;
    private String hideCurrent;
    private String sortByDateOfBirth;
    private String userNameDoctor;
    private List<Role> authorities = new ArrayList<>();
}
