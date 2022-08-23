package capstone.chatbot.repository;

import capstone.chatbot.domain.Disease;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiseaseRepository {

    private final EntityManager em;

    //질병 정보 저장
    public void save(Disease disease) {
        if (disease.getId() == null) {
            em.persist(disease);
        } else {
            em.merge(disease);
        }
    }

    //id를 통해 질병 정보 조회
    public Disease findOne(Long id) {
        return em.find(Disease.class, id);
    }

    // diseaseName을 통해 질병 이름 조회
    public Disease findByName(String name) {

        List<Disease> namelist = em.createQuery("select m from Disease m where m.name = :name", Disease.class)
                .setParameter("name",name)
                .getResultList(); // query로 질병 이름으로 질병 조회

        return namelist.get(0); //list의 첫번째 항목을 반환
    }
}
