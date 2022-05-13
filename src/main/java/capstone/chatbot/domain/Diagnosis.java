package capstone.chatbot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diagnosis")
@Getter
@Setter
@NoArgsConstructor

public class Diagnosis {

    @Id
    @GeneratedValue
    @Column(name = "diagnosis_id")

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "diagnosis", cascade = CascadeType.ALL)
    private List<DiagnosisDisease> diagnosisDiseases = new ArrayList<>();

    private LocalDate diagnosisDate;

    public void setMember(Member member) {
        this.member = member;
        member.getDiagnosis().add(this);
    }

    public void addDiagnosisDisease(DiagnosisDisease diagnosisDisease) {
        diagnosisDiseases.add(diagnosisDisease);
        diagnosisDisease.setDiagnosis(this);
    }

    public static Diagnosis createDiagnosis(Member member, DiagnosisDisease... diagnosisDiseases) {

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setMember(member);

        for (DiagnosisDisease diagnosisDisease : diagnosisDiseases) {
            diagnosis.addDiagnosisDisease(diagnosisDisease);
        }
        diagnosis.setDiagnosisDate(LocalDate.now());

        return diagnosis;
    }



}
