package hospital.persistence;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.domain.User;
import hospital.exeption.ServiceExeption;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalListJPARepository extends CrudRepository<HospitalList, Long>  {
    Optional<HospitalList> findFirstByDoctorNameAndPatientIdOrderByDateCreateDesc(String doctorName, Patient patientId);
    List<HospitalList> findAllByPatientId(Patient patientId);
    @Modifying
    @Query("update HospitalList h " +
            "set h.dateDischarge = CASE WHEN (h.dateDischarge is null) THEN (:date) ELSE (h.dateDischarge) END, " +
            "h.finalDiagnosis = CASE WHEN (h.finalDiagnosis is null) THEN (:finalDiagnosis) ELSE (h.finalDiagnosis) END " +
            "where h.hospitalListId = :id")

    int updateDateDischarge(@Param("id") Long hospitalListId, @Param("finalDiagnosis") String finalDiagnosis, @Param("date") LocalDateTime date);
}
