package hospital.persistence;

import hospital.domain.Nurse;
import hospital.domain.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NurseJPARepository extends CrudRepository<Nurse, Long>  {
    Optional<Nurse> findByUsername(@NonNull String username) ;
    Page<Nurse> findNursesByPatientsIsNotContaining(Patient id, Pageable pageable);
    List<Nurse> findByPatients (Patient patient);
}
