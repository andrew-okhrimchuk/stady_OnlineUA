package hospital.services;

import hospital.domain.User;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService {
    UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException;
    List<User> getListPatients(SelectDTO selectDTO) throws ServiceExeption;
    List<User> getListDoctors() throws ServiceExeption;
    User savePatient(User user) throws ServiceExeption;
    User getUserById(long id) throws ServiceExeption;
}
