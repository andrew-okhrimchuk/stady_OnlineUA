package hospital.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import hospital.domain.User;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserJPARepository extends CrudRepository<User, Long> , JpaSpecificationExecutor<User> {
     Optional<User> findByUsername(@NonNull String username) ;
     Page<User> findAll(Specification<User> spec, Pageable pageable);
     List<User> findAll(Specification<User> spec);
     User getUserById(long id);

}
