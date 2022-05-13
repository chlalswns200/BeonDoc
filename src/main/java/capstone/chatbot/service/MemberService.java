package capstone.chatbot.service;

import capstone.chatbot.domain.BasicExam;
import capstone.chatbot.domain.Member;
import capstone.chatbot.domain.Profile;
import capstone.chatbot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional// jpa의 모든 데이터 변경은 가급적 transactional 안에서 이루어 져야 한다.
    public Long join(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public Member findByLoginId(String loginId) {return memberRepository.findByLoginId(loginId);}

    @Transactional
    public void update(Long id,String loginId,String password,String name,String age, String height, String weight, long gender,
                       String drug, String social, String family, String trauma, String femininity) {
        Member member = memberRepository.findOne(id);

        BasicExam basicExam = new BasicExam(drug, social, family, trauma, femininity);

        Profile profile = new Profile(age, height, weight, gender);
        member.setName(name);
        member.setLoginId(loginId);
        member.setPassword(password);
        member.setProfile(profile);
        member.setBasicExam(basicExam);

    }

}
