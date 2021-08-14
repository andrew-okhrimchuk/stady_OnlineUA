package hospital.services.hospitalList;

import hospital.domain.MedicationLog;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.MedicationLogJPARepository;
import hospital.services.interfaces.IMedicationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

@Slf4j
@Service
public class MedicationLogService implements IMedicationLogService {

    @Autowired
    private MedicationLogJPARepository medicationLogJPARepository;
    @Autowired
    private Environment env;

    public MedicationLog save(MedicationLog medicationLog) throws ServiceExeption {
        log.debug("Start save MedicationLog, medicationLog = {}", medicationLog);
        try {
            validation(medicationLog);
            return medicationLogJPARepository.save(medicationLog);
        } catch (DaoExeption | DateTimeParseException | NotValidExeption e) {
            log.error("MedicationLog {}, {}", env.getProperty("SAVE_NEW_MedicationLog"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("MedicationLog {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_MedicationLog"), medicationLog.getMedicationlogId(), e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_MedicationLog"), e);
        }
    }

    @Override
    public Page<MedicationLog> findByMedicationlogId(Long hospitalListId, Pageable pageable) throws ServiceExeption {
        log.debug("Start findByMedicationlogId. hospitalListId {}", hospitalListId);
        try {
            return medicationLogJPARepository.findAllByHospitallistidOrderByDateCreateDesc(hospitalListId, pageable);
        } catch (DaoExeption | DateTimeParseException e) {
            log.error("findByMedicationlogId {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("findByMedicationlogId {}, hospitalListId = {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), hospitalListId, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    private void validation(MedicationLog medicationLog) throws NotValidExeption {
        if (!medicationLog.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALID_MedicationLog"));
        }
    }
}
