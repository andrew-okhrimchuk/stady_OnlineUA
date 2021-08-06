package hospital.services.intrface;

import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;

import java.util.List;

public interface IPatientService {
    List<Patient> getListPatients(SelectDTO selectDTO) throws ServiceExeption;
    List<Patient> getListDoctors() throws ServiceExeption;
    Patient savePatient(UserDTO userDTO) throws ServiceExeption;
    Patient getUserById(long id) throws ServiceExeption;
}
