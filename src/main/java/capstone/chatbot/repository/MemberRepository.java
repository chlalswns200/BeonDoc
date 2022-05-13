package capstone.chatbot.repository;

import capstone.chatbot.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository // 리포지토리를 컴포넌트 스캔에 등록한다. 자동으로 관리 된다.
@RequiredArgsConstructor
public class MemberRepository {


    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }// jpa가 저정하는 로직


    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public Member findByLoginId(String loginId) {

        List<Member> loginIdlist = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        return loginIdlist.get(0);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
