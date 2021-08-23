package hospital.services.hospitalList;

import hospital.domain.HospitalList;
import hospital.domain.Patient;
import hospital.exeption.DaoExeption;
import hospital.exeption.NotValidExeption;
import hospital.exeption.ServiceExeption;
import hospital.persistence.HospitalListJPARepository;
import hospital.persistence.PatientJPARepository;
import hospital.persistence.UserJPARepository;
import hospital.services.hospitalList.excel.ExcelFileExporter;
import hospital.services.interfaces.IHospitalListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HospitalListService implements IHospitalListService {

    @Autowired
    private UserJPARepository userJPARepository;
    @Autowired
    private PatientJPARepository patientJPARepository;
    @Autowired
    private HospitalListJPARepository hospitalListJPARepository;
    @Autowired
    private Environment env;
    @Autowired
    ExcelFileExporter excelFileExporter;

    public HospitalList save(HospitalList hospitalList) throws ServiceExeption {
        log.debug("Start saveHospitalList of User. userDTO = {}", hospitalList);
        try {
            return hospitalListJPARepository.save(hospitalList);
        } catch (DaoExeption | DateTimeParseException e) {
            log.error("saveHospitalList {}, {}", env.getProperty("SAVE_NEW_PATIENT"), e.getMessage());
            throw new ServiceExeption(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            log.error("saveHospitalList {}, {}, {}", env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), hospitalList.getHospitalListId(), e.getMessage());
            throw new ServiceExeption(env.getProperty("SAVE_NEW_PATIENT_DUPLICATE"), e);
        }
    }

    @Transactional
    public int updateDateDischarge(HospitalList hospitalList) throws ServiceExeption {
        log.debug("Start saveHospitalList of User. userDTO = {}", hospitalList);
        try {
            userJPARepository.updateCurrent(hospitalList.getPatientId().getId());
            patientJPARepository.updateCurrent(hospitalList.getPatientId().getId());
            return hospitalListJPARepository.updateDateDischarge(hospitalList.getHospitalListId(), hospitalList.getFinalDiagnosis(), LocalDateTime.now());

        } catch (DaoExeption | DateTimeParseException e) {
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
            Optional<HospitalList> result = hospitalListJPARepository.findFirstByDoctorNameAndPatientIdOrderByDateCreateDesc(doctorName, Patient.chilerBuilder().id(Long.parseLong(parientId)).build());
            if (result.isPresent() && result.get().getDateDischarge() != null) {
                return Optional.of(HospitalList.builder().dateCreate(LocalDateTime.now()).build());
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

    public ByteArrayInputStream ListToExcelFile(String userId) {
        List<HospitalList> list = hospitalListJPARepository
                .findAllByPatientId(Patient.chilerBuilder()
                        .id(Long.valueOf(userId))
                        .build());
        return excelFileExporter.callsListToExcelFile(list);
    }
}
