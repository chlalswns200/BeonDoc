package capstone.chatbot.repository;

import capstone.chatbot.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {


    private final EntityManager em;
    // 회원 저장
    public void save(Member member) {
        em.persist(member);
    }

    // id를 통해 회원 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // loginId를 통해 회원 정보 조회
    public Member findByLoginId(String loginId) {

        List<Member> loginIdlist = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList(); // query를 통해서 조회

        return loginIdlist.get(0); // list의 첫 번째를 반환
    }

    //회원 탈퇴
    public void deleteMember(Long id) {
        Member findMember = findOne(id); // 회원 정보를 불러와 저장한다
        em.remove(findMember);// db에서 삭제한다
    }

    //loginId 중복 조회
    public Integer checkLoginId(String loginId) {

        List<Member> loginIdlist = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList(); // query를 통해서 조회

        if (!loginIdlist.isEmpty()) {
            return 1; //  이미 있는 loginId의 경우 1 반환
        }
        else return 0; // 없는 는 경우 0 반환
    }

}
