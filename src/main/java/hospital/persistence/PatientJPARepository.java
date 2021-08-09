package hospital.persistence;

import hospital.domain.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientJPARepository extends CrudRepository<Patient, Long> , JpaSpecificationExecutor<Patient> {
     Optional<Patient> findByUsername(@NonNull String username) ;
     Page<Patient> findAll(Specification<Patient> spec, Pageable pageable);
     Patient getPatientById(long id);

}
