package hospital.persistence;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import hospital.domain.User;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserJPARepository extends CrudRepository<User, Long> , JpaSpecificationExecutor<User> {
     Optional<User> findByUsername(@NonNull String username) ;
}
