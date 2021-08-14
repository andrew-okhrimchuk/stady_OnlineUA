package hospital.persistence;

import hospital.domain.MedicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationLogJPARepository extends CrudRepository<MedicationLog, Long>  {
    Page<MedicationLog> findAllByHospitallistidOrderByDateCreateDesc (Long hospitalListId, Pageable pageable);
}
