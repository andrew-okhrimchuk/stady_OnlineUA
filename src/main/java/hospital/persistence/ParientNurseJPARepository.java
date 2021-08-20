package hospital.persistence;

import hospital.domain.PatientNurse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParientNurseJPARepository extends CrudRepository<PatientNurse, Long> {
    @Modifying
    @Transactional
    @Query("delete from PatientNurse p where p.nurses_id = :nurseId and p.patients_user_id=:userId")
    int deleteNurse(Long nurseId, Long userId);
}
