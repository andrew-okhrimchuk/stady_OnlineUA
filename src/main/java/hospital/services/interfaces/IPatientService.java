package hospital.services.interfaces;

import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.dto.PatientDTO;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPatientService extends GenericService <Patient, SelectDTO> {
    Page<Patient> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption;
    Page<Patient> getAll(Pageable pageable) throws ServiceExeption;
    Patient save(PatientDTO patientDTO) throws ServiceExeption;
    PatientDTO getPatientById(long id) throws ServiceExeption;
    Page<Patient> findAllPatientsByNameDoctor(String username,Pageable pageable) throws ServiceExeption;
    Page<Patient> findAllCurrentPatientsByNameDoctor(SelectDTO selectDTO,Pageable pageable) throws ServiceExeption ;

}
