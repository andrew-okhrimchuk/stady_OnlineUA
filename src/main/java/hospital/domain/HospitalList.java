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
/*@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"patientId", "dateCreate"})
})*/
public class HospitalList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long hospitalListId;
    private String primaryDiagnosis;
    private String finalDiagnosis;
    private String medicine;
    private String operations;
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId"  )  //, foreignKey = @ForeignKey(name = "patientId_fk", foreignKeyDefinition = "FOREIGN KEY (patientid) REFERENCES HospitalList(hospitalListIid)"))
    private Patient patientId;
    private LocalDateTime dateCreate;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "hospitallistid")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<MedicationLog> hospitallistid;

    public boolean isValid (){
        return this.primaryDiagnosis !=null
                && !this.primaryDiagnosis.isEmpty();
    }
}
