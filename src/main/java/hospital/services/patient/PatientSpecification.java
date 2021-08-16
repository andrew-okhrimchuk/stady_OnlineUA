package hospital.services.patient;

import hospital.domain.Patient;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import hospital.domain.User;
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

    public Specification<Patient> getCurrentPatientsByDoctorName(SelectDTO request) {
        log.debug("Start getPatients of SelectDTO");

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Where
            Subquery<Long> sub = query.subquery(Long.class);
            Root<User> subRoot = sub.from(User.class);
            sub.select(subRoot.get("id"));
            sub.where(criteriaBuilder.equal(subRoot.get("username"), request.getUserNameDoctor()));
            predicates.add(criteriaBuilder.equal(root.get("doctor").get("id"), sub));


            predicates.add(criteriaBuilder.equal(root.get("isactualpatient"), true));
            // Order By
            query.orderBy(criteriaBuilder.asc(root.get("birthDate")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}

