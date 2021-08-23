package hospital.domain;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity

public class HospitalList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long hospitalListId;
    @NotNull
    @NotEmpty
    private String primaryDiagnosis;
    private String finalDiagnosis;
    @NotNull
    @NotEmpty
    private String medicine;
    @NotNull
    @NotEmpty
    private String operations;
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId"  )  //, foreignKey = @ForeignKey(name = "patientId_fk", foreignKeyDefinition = "FOREIGN KEY (patientid) REFERENCES HospitalList(hospitalListIid)"))
    private Patient patientId;
    private LocalDateTime dateCreate;
    private LocalDateTime dateDischarge;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hospitallistid")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<MedicationLog> hospitallistid;


}
