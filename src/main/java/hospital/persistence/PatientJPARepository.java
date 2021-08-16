package hospital.persistence;

import hospital.domain.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientJPARepository extends CrudRepository<Patient, Long> , JpaSpecificationExecutor<Patient> {
     Optional<Patient> findByUsername(@NonNull String username) ;
     Page<Patient> findAll(Specification<Patient> spec, Pageable pageable);

     Patient getPatientById(long id);

     @Query("select p from User u join Patient p on u.id = p.id  where p.doctor.id =(select d.id from Doctor d join User u on u.id = d.id where u.username  = :username) order by p.username")
     Page<Patient> findAllPatientsByNameDoctor(@Param("username") String username, Pageable pageable);

     @Query("select p from User u join Patient p on u.id = p.id join HospitalList h on h.patientId=p.id  where p.doctor.id =(select d.id from Doctor d join User u on u.id = d.id  where u.username  = :username ) and h.dateDischarge IS NULL  order by p.username")
     Page<Patient> findAllCurrentPatientsByNameDoctor(@Param("username") String username, Pageable pageable);

}
