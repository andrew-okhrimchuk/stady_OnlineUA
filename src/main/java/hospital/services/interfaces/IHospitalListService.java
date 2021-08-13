package hospital.services.interfaces;


import hospital.domain.HospitalList;
import hospital.exeption.ServiceExeption;

import java.util.Optional;

public interface IHospitalListService  {
    Optional<HospitalList> findByParientIdAndDoctorName(String parientId, String doctorName) throws ServiceExeption;
}
