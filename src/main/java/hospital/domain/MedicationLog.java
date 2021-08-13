package hospital.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class MedicationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long medicationlogId;
    private Long hospitallistid;
    private String description;
    private LocalDateTime dateCreate = LocalDateTime.now();

    public boolean isValid (){
        return this.description !=null
                && !this.description.isEmpty();
    }
}