package hospital.services.doctor;

import hospital.domain.Doctor;
import hospital.domain.enums.Speciality;
import hospital.dto.SelectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
//https://www.appsdeveloperblog.com/specification-predicate-advance-search-and-filtering-in-jpa/

@Slf4j
@Component
public class DoctorSpecification {

    public Specification<Doctor> getUsers(SelectDTO request) {
        log.debug("Start getPatients of SelectDTO");
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getSpeciality() != null && !request.getSpeciality().equals(Speciality.ALL)) {
                root.join("speciality");
                predicates.add(criteriaBuilder.equal(root.get("speciality"), request.getSpeciality().name()));
            }
          //  root.join("users").;
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}