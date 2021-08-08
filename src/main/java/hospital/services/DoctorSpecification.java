package hospital.services;

import hospital.domain.Doctor;
import hospital.domain.enums.Speciality;
import hospital.dto.SelectDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
//https://www.appsdeveloperblog.com/specification-predicate-advance-search-and-filtering-in-jpa/
// https://thorben-janssen.com/hibernate-tip-left-join-fetch-join-criteriaquery/
//https://www.initgrep.com/posts/java/jpa/create-programmatic-queries-using-criteria-api

@Slf4j
@Component
public class DoctorSpecification {

    public Specification<Doctor> getUsers(SelectDTO request) {
        log.debug("Start getPatients of SelectDTO");
// TODO fixe parametres from Doctor!
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // TODO aDD Join speciality

       //     root.join("speciality", JoinType.INNER);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}