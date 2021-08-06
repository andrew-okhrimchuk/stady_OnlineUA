package hospital.services;

import hospital.domain.Doctor;
import hospital.dto.SelectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DoctorSpecification {
//https://www.appsdeveloperblog.com/specification-predicate-advance-search-and-filtering-in-jpa/

    public Specification<Doctor> getUsers(SelectDTO request) {
        log.debug("Start getPatients of SelectDTO");
// TODO fixe parametres from Doctor!
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getHideArchive() != null && request.getHideCurrent() != null) {
                predicates.add(criteriaBuilder.equal(root.get("username"), ""));
            }
            if (request.getHideArchive() != null && request.getHideCurrent() == null) {
                predicates.add(criteriaBuilder.equal(root.get("isCurrentPatient"), true));
            }
            if (request.getHideArchive() == null && request.getHideCurrent() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isCurrentPatient"), false));
            }
            if (request.getSortByDateOfBirth() != null) {
                query.orderBy(criteriaBuilder.asc(root.get("birthDate")));
            } else {
                query.orderBy(criteriaBuilder.asc(root.get("username")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}