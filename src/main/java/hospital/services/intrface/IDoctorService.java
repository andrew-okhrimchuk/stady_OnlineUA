package hospital.services.intrface;

import hospital.domain.Doctor;
import hospital.domain.Patient;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;

import java.util.List;

public interface IDoctorService {
    List<Doctor> getListDoctors(SelectDTO selectDTO) throws ServiceExeption;
    List<Doctor> getListDoctors() throws ServiceExeption;
    Doctor saveDoctor(UserDTO userDTO) throws ServiceExeption;
    Doctor getDoctorById(long id) throws ServiceExeption;
}
