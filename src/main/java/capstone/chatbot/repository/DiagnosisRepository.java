package capstone.chatbot.repository;

import capstone.chatbot.api.DiagnosisDTO;
import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.DiagnosisDisease;
import capstone.chatbot.domain.Disease;
import capstone.chatbot.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class DiagnosisRepository {

    private final EntityManager em;

    public void save(Diagnosis diagnosis) {
        em.persist(diagnosis);
    }

    public Diagnosis findOne(Long id) {

        return em.find(Diagnosis.class, id);
    }

    public List<Diagnosis>findAllByString(DiagnosisSearch diagnosisSearch) {

        String jpql = "select o From Diagnosis o join o.member m";
        boolean isFirstCondition = true;

        if (StringUtils.hasText(diagnosisSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += "where";
                isFirstCondition = false;
            } else {
                jpql += "and";
            }
            jpql += "m.name like :name";
        }
        TypedQuery<Diagnosis> query = em.createQuery(jpql, Diagnosis.class)
                .setMaxResults(1000);
        if (StringUtils.hasText(diagnosisSearch.getMemberName())) {
            query = query.setParameter("name", diagnosisSearch.getMemberName());

        }
        return query.getResultList();
    }


    public List<DiagnosisDTO>findAllByStringV2(Long memberId) {

        List<DiagnosisDTO> disList = new ArrayList<>();

        String jpql = "select d From Diagnosis d join d.member m where m.id = :memberId";


        List<Diagnosis> members = em.createQuery(jpql, Diagnosis.class)
                .setParameter("memberId", memberId)
                .getResultList();

        for (Diagnosis member : members) {
            List<DiagnosisDisease> diagnosisDiseases = member.getDiagnosisDiseases();
            for (DiagnosisDisease diagnosisDisease : diagnosisDiseases) {
                disList.add(new DiagnosisDTO(diagnosisDisease.getDisease().getName(),
                        diagnosisDisease.getDisease().getInfo(),
                        diagnosisDisease.getDisease().getLevel(),
                        diagnosisDisease.getDisease().getDepartment(),
                        diagnosisDisease.getDiagnosis().getDiagnosisDate()));
            }
        }

        return disList;
    }

    public DiagnosisDTO findOneByString(Long id) {

        Diagnosis diagnosis = em.find(Diagnosis.class, id);
        List<DiagnosisDisease> diagnosisDiseases = diagnosis.getDiagnosisDiseases();
        DiagnosisDTO diagnosisDTO = new DiagnosisDTO(null,null,0,null,null);
        for (DiagnosisDisease diagnosisDisease : diagnosisDiseases) {
            diagnosisDTO.setName(diagnosisDisease.getDisease().getName());
            diagnosisDTO.setInfo(diagnosisDisease.getDisease().getInfo());
            diagnosisDTO.setLevel(diagnosisDisease.getDisease().getLevel());
            diagnosisDTO.setDepartment(diagnosisDisease.getDisease().getDepartment());
            diagnosisDTO.setDiagnosisTime(diagnosis.getDiagnosisDate());


        }
        return diagnosisDTO;


    }




}
