package hospital.services;

import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {
    UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException;
}
