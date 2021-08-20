package hospital.persistence;

import hospital.domain.MedicationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface MedicationLogJPARepository extends CrudRepository<MedicationLog, Long>  {
    Page<MedicationLog> findAllByHospitallistidOrderByDateCreateDesc (Long hospitalListId, Pageable pageable);
    @Modifying
    @Transactional
    @Query("update MedicationLog m " +
            "set m.executor = CASE WHEN (m.executor is null) THEN (:executor) ELSE (m.executor) END, " +
            "m.dateEnd = CASE WHEN (m.dateEnd is null) THEN (:date) ELSE (m.dateEnd) END " +
            "where m.medicationlogId = :id")
    int done(@Param("id") Long medicationlogId, @Param("executor") String executor, @Param("date") LocalDateTime date);
}
