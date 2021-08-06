package hospital.services;

import hospital.dto.DoctorDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class DoctorDTOConverter implements Converter<String, DoctorDTO> {
    @Override
    public DoctorDTO convert(String id) {
        log.debug("Trying to convert id =" + id + " into a DoctorDTO");
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(id);
        return doctorDTO;
    }
}