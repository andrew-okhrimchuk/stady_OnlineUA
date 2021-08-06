package hospital.services.intrface;

import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;

import java.util.List;

public interface IPatientService extends GenericService <Patient, SelectDTO> {
    List<Patient> getAll(SelectDTO selectDTO) throws ServiceExeption;
    List<Patient> getAll() throws ServiceExeption;
    Patient save(UserDTO userDTO) throws ServiceExeption;
    Patient getUserById(long id) throws ServiceExeption;
}
