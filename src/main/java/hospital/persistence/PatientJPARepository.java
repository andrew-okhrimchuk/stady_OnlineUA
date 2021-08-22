package hospital.persistence;

import hospital.domain.Nurse;
import hospital.domain.Patient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PatientJPARepository extends CrudRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<Patient> findByUsername(@NonNull String username);
    Page<Patient> findAll(Specification<Patient> spec, Pageable pageable);
    @Query("select p from Patient p join PatientNurse pn on p.id=pn.patients_user_id where pn.nurses_id = (select u.id from User u where u.username = :username)")
    Page<Patient> findAllByNursename(String username, Pageable pageable);
    Patient getPatientById(long id);
    @Modifying
    @Query("update Patient p set p.doctor = null where p.id = :id")
    int updateCurrent(@Param("id") Long id);
}
