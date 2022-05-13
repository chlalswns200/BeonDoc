package capstone.chatbot.repository;

import capstone.chatbot.domain.Disease;
import capstone.chatbot.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiseaseRepository {

    private final EntityManager em;

    public void save(Disease disease) {
        if (disease.getId() == null) {
            em.persist(disease);
        } else {
            em.merge(disease);
        }
    }

    public Disease findOne(Long id) {
        return em.find(Disease.class, id);
    }


    public Disease findByName(String name) {

        List<Disease> namelist = em.createQuery("select m from Disease m where m.name = :name", Disease.class)
                .setParameter("name",name)
                .getResultList();

        return namelist.get(0);
    }


    public List<Disease> findAll() {
        return em.createQuery("select  i from Disease i", Disease.class)
                .getResultList();
    }
}
