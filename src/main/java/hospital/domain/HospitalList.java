package hospital.domain;

import lombok.*;
import javax.persistence.*;
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
    private Long hospitalListIid;
    private String primaryDiagnosis;
    private String finalDiagnosis;
    private String medicine;
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "patientId_fk", foreignKeyDefinition = "FOREIGN KEY (patientid) REFERENCES HospitalList(hospitalListIid)"))
    private Patient patientId;
    private LocalDateTime dateCreate = LocalDateTime.now();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "medicationlog_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<MedicationLog> medicationLog;

    public boolean isValid (){
        return this.primaryDiagnosis !=null
                && !this.primaryDiagnosis.isEmpty();
    }
}
