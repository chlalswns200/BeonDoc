package capstone.chatbot.service;

import capstone.chatbot.domain.Member;
import capstone.chatbot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {

        Member findMember = memberRepository.findByLoginId(loginId);
        if (findMember.getPassword().equals(password))
        {
            return findMember;
        }
        else return null;
    }

}
