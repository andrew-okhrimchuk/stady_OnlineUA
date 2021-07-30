package hospital.persistence;

import org.springframework.data.repository.CrudRepository;
import hospital.domain.User;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserJPARepository extends CrudRepository<User, Long> {
    public Optional<User> findByUsername(@NonNull String username) ;
}
