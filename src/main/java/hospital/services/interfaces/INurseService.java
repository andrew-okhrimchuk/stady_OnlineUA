package hospital.services.interfaces;


import hospital.domain.MedicationLog;
import hospital.domain.Nurse;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface INurseService {
    List<Nurse> findByPatientId(Long id) throws ServiceExeption;
    Page<Nurse> findAllWithOutPatientId(Long id, Pageable pageable) throws ServiceExeption;
}
