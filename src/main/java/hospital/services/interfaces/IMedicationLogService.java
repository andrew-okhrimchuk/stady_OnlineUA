package hospital.services.interfaces;


import hospital.domain.HospitalList;
import hospital.domain.MedicationLog;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IMedicationLogService {
    Page<MedicationLog> findByMedicationlogId(Long hospitalListId, Pageable pageable) throws ServiceExeption;
    MedicationLog save(MedicationLog medicationLog) throws ServiceExeption;
    int done(MedicationLog medicationLog) throws ServiceExeption;
}
