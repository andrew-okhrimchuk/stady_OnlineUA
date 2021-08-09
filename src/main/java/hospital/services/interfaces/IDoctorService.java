package hospital.services.interfaces;

import hospital.domain.Doctor;
import hospital.dto.DoctorDTO;
import hospital.dto.SelectDTO;
import hospital.exeption.ServiceExeption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDoctorService extends GenericService <DoctorDTO, SelectDTO> {
    Page<DoctorDTO> getAll(SelectDTO selectDTO, Pageable pageable) throws ServiceExeption;
    Page<DoctorDTO> getAll(Pageable pageable) throws ServiceExeption;
    Doctor save(DoctorDTO doctorDTO) throws ServiceExeption;
    Doctor getDoctorById(long id) throws ServiceExeption;
}
