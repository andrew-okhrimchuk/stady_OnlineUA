package hospital.services.intrface;

import hospital.domain.Doctor;
import hospital.domain.Patient;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.dto.UserDTO;
import hospital.exeption.ServiceExeption;

import java.util.List;

public interface IDoctorService extends GenericService <Doctor, SelectDTO> {
    List<Doctor> getAll(SelectDTO selectDTO) throws ServiceExeption;
    List<Doctor> getAll() throws ServiceExeption;
    Doctor save(DoctorDTO doctorDTO) throws ServiceExeption;
    Doctor getDoctorById(long id) throws ServiceExeption;
 //   List<DoctorDTO> findAllWithCount() throws ServiceExeption;
}
