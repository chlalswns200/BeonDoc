package capstone.chatbot.repository;

import capstone.chatbot.api.DiagnosisDTO;
import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.DiagnosisDisease;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DiagnosisRepository {

    private final EntityManager em;
    // 진단 기록 저장
    public void save(Diagnosis diagnosis) {
        em.persist(diagnosis);
    }

    //id로 진단 조회
    public Diagnosis findOne(Long id) {
        return em.find(Diagnosis.class, id);
    }

    // memberId를 통해서 전체 진단 기록 조회
    public List<DiagnosisDTO> findAllByStringV2(Long memberId) {

        List<DiagnosisDTO> disList = new ArrayList<>(); // 빈 list 생성

        String jpql = "select d From Diagnosis d join d.member m where m.id = :memberId"; // memberId를 통해서 진단 기록 조회


        List<Diagnosis> members = em.createQuery(jpql, Diagnosis.class)
                .setParameter("memberId", memberId)
                .getResultList(); // query를 통해서 db탐색 후 결과 list 반환

        // DiagnosisDTO에 맞게 조회 된 내용 변환
        for (Diagnosis member : members) {
            List<DiagnosisDisease> diagnosisDiseases = member.getDiagnosisDiseases();
            for (DiagnosisDisease diagnosisDisease : diagnosisDiseases) {
                disList.add(new DiagnosisDTO(
                        member.getId(),
                        diagnosisDisease.getDisease().getName(),
                        diagnosisDisease.getDisease().getInfo(),
                        diagnosisDisease.getDisease().getDepartment(),
                        diagnosisDisease.getDisease().getCause(),
                        diagnosisDisease.getDisease().getSymptom(),
                        diagnosisDisease.getDiagnosis().getDiagnosisDate(),
                        diagnosisDisease.getDiagnosis().getPercent()));
            }
        }


        return disList; // 변환 된 리스트 반환
    }

    // memberId를 통해 전체 진단 기록 조회
    public List<Diagnosis> MemberDiagnosisList(Long memberId) {
        String jpql = "select d From Diagnosis d join d.member m where m.id = :memberId";
        List<Diagnosis> members = em.createQuery(jpql, Diagnosis.class)
                .setParameter("memberId", memberId)
                .getResultList();
        return members; // 여기서는 DiagnosisDTO로 변환하지 않고 진단을 바로 반환 한다
        }

    //진단 기록 삭제
    public void deleteDiagnosis(Long id) {
        Diagnosis findDiagnosis = findOne(id); //id를 통해 진단 조회
        em.remove(findDiagnosis); //db에서 삭제
    }
}
