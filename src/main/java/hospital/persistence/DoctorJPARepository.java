package hospital.persistence;

import hospital.domain.Doctor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorJPARepository extends CrudRepository<Doctor, Long> , JpaSpecificationExecutor<Doctor> {
     Optional<Doctor> findByUsername(@NonNull String username) ;
     Page<Doctor> findAll(Specification<Doctor> spec, Pageable pageable);
     List<Doctor> findAll(Specification<Doctor> spec);
     Doctor getDoctorById(long id);

    // @Query("select users.username, users.id, count(users.id) from users join doctor d on users.id = d.id left join patient p on d.id = p.doctor_id group by users.id")
  //   List<Object[]>  findAllWithCount();

}
