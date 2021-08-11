package hospital.services.interfaces;

import hospital.domain.Patient;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPatientService extends GenericService <Patient, SelectDTO> {
    Page<Patient> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption;
    Page<Patient> getAll(Pageable pageable) throws ServiceExeption;
    Patient save(UserDTO userDTO) throws ServiceExeption;
    UserDTO getPatientById(long id) throws ServiceExeption;
    Page<Patient> findAllPatientsByNameDoctor(String username,Pageable pageable) throws ServiceExeption;
}
