package capstone.chatbot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class DiagnosisDisease {

    @Id
    @GeneratedValue
    @Column(name = "diagnosis_disease_list_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;

    public static DiagnosisDisease createDiagnosisDiseaseList(Disease disease) {

        DiagnosisDisease diagnosisDisease = new DiagnosisDisease();
        diagnosisDisease.setDisease(disease);
        return diagnosisDisease;
    }

}
