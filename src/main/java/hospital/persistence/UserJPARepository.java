package hospital.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import hospital.domain.User;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserJPARepository extends CrudRepository<User, Long>  {
     Optional<User> findByUsername(@NonNull String username) ;
     @Modifying
     @Query("update User u set u.isactualpatient = false where u.id = :id")
     int updateCurrent(@Param("id") Long id);
}
