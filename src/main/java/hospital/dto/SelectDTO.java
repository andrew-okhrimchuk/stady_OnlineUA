package hospital.dto;

import hospital.domain.Role;
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
public class SelectDTO {
    private List<User> users = new ArrayList<>();
    private String hideArchive;
    private String hideCurrent;
    private String sortByDateOfBirth;
    private List<Role> authorities = new ArrayList<>();
}
