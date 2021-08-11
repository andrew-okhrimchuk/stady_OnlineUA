package hospital.persistence;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.domain.User;
import hospital.exeption.ServiceExeption;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalListJPARepository extends CrudRepository<HospitalList, Long>  {
    Optional<HospitalList> findByDoctorNameAndPatientId(String doctorName, Patient patientId);
}
