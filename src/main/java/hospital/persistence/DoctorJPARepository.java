package hospital.persistence;

import hospital.domain.Doctor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorJPARepository extends CrudRepository<Doctor, Long> , JpaSpecificationExecutor<Doctor> {
     Optional<Doctor> findByUsername(@NonNull String username) ;
     Page<Doctor> findAll(Specification<Doctor> spec, Pageable pageable);
     Doctor getDoctorById(long id);

     @Query("select d from Doctor d join User u on d.id = u.id where d.speciality =:speciality")
     Page<Doctor> findAllDoctors(String speciality, Pageable pageable);

     @Query("select u.username, d.id, count( p.doctor.id) from Doctor d join User u on u.id = d.id left join Patient p on d.id = p.doctor.id group by p.doctor.id, u.username, d.id")
     List<Object[]> findAllWithCount();

}
