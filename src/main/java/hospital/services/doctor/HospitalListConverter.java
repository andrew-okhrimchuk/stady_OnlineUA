package hospital.services.doctor;

import hospital.domain.HospitalList;
import hospital.dto.DoctorDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class HospitalListConverter implements Converter<String, HospitalList> {
    @Override
    public HospitalList convert(String id) {
        log.debug("Trying to convert id =" + id + " into a HospitalList");
        return HospitalList.builder().hospitalListIid(Long.parseLong(id)).build();
    }
}