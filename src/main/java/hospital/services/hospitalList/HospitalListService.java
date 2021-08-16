package hospital.services.hospitalList;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.HospitalListJPARepository;
import hospital.persistence.UserJPARepository;
import hospital.services.interfaces.IHospitalListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@Service
public class HospitalListService implements IHospitalListService {

    @Autowired
    UserJPARepository userJPARepository;
    @Autowired
    private HospitalListJPARepository hospitalListJPARepository;
    @Autowired
    private Environment env;

    @Transactional
    public HospitalList save(HospitalList hospitalList, Long userId) throws ServiceExeption {
        log.debug("Start saveHospitalList of User. userDTO = {}", hospitalList);
        try {
            validation(hospitalList);
            if (userId != 0L) {userJPARepository.updateCurrent(userId);}
            return hospitalListJPARepository.save(hospitalList);
        } catch (DaoExeption | DateTimeParseException | NotValidExeption e) {
            log.error("saveHospitalList {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("saveHospitalList {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), hospitalList.getHospitalListId(), e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Override
    public Optional<HospitalList> findByParientIdAndDoctorName(String parientId, String doctorName) throws ServiceExeption {
        log.debug("Start findByParientIdAndDoctorId. parientId {}, doctorName {}", parientId, doctorName);
        try {
            Optional<HospitalList> result = hospitalListJPARepository.findByDoctorNameAndPatientId(doctorName, Patient.chilerBuilder().id(Long.parseLong(parientId)).build());
            if (result.isPresent() && result.get().getDateDischarge()!=null){
                return Optional.of(new HospitalList());
            }
            return result;
        } catch (DaoExeption | DateTimeParseException e) {
            log.error("findByParientIdAndDoctorId {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("findByParientIdAndDoctorId {}, parientId = {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), parientId, e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    private void validation(HospitalList hospitalList) throws NotValidExeption {
        if (!hospitalList.isValid()) {
            throw new NotValidExeption(env.getProperty("SAVE_NEW_NOT_VALID_HOSPITAL_LIST"));
        }
    }
}
