package hospital.services.patient;

import hospital.domain.Patient;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import hospital.dto.SelectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PatientSpecification {
//https://www.appsdeveloperblog.com/specification-predicate-advance-search-and-filtering-in-jpa/

    public Specification<Patient> getUsers(SelectDTO request) {
        log.debug("Start getPatients of SelectDTO");

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Where
            if (request.getUserNameDoctor() != null && !request.getUserNameDoctor().isEmpty()) {
                root.join("doctor");
                predicates.add(criteriaBuilder.equal(root.get("username"), request.getUserNameDoctor()));
            }
            if (!request.getIsShowAllDischargePatients() && !request.getIsShowAllCurrentPatients()) {
                predicates.add(criteriaBuilder.equal(root.get("username"), ""));
            }

            if (!request.getIsShowAllDischargePatients() && request.getIsShowAllCurrentPatients()) {
                predicates.add(criteriaBuilder.equal(root.get("isactualpatient"), true));
            }

            if (request.getIsShowAllDischargePatients() && !request.getIsShowAllCurrentPatients()) {
                predicates.add(criteriaBuilder.equal(root.get("isactualpatient"), false));
            }


            // Order By
            if (request.getIsSortByDateOfBirth()) {
                query.orderBy(criteriaBuilder.asc(root.get("birthDate")));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get("username")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

