package hospital.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @NotNull
    @NotEmpty
    

    private String description;
    private String executor;
    private String doctorName;
    private LocalDateTime dateCreate = LocalDateTime.now();
    private LocalDateTime dateEnd;
}
