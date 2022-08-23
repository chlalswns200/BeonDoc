package capstone.chatbot.service;

import capstone.chatbot.domain.Member;
import capstone.chatbot.repository.MemberRepository;
import capstone.chatbot.security.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginService {

    private final MemberRepository memberRepository;
    // 로그인 기능
    // loginId를 통해서 조회한 db속 회원의 비밀 번호와 입력 받은 비밀 번호가 같을 경우 로그인 성공
    public Member login(String loginId, String password) {

        SHA256 sha256 = new SHA256(); // 암호화 클래스 sha256 생성
        String passwordc = sha256.SHA256Encrypt(password); // 입력 받은 비밀 번호를 암호화 한 후 저장

        Member findMember = memberRepository.findByLoginId(loginId); // loginId를 통해 회원 정보를 불러 와서 저장
        if (findMember.getPassword().equals(passwordc)) // 찾은 회원의 비밀 번호와 입력 받은 비밀 번호가 같을 경우
        {
            return findMember; // 회원 정보를 반환
        }
        else return null; // 아닐 경우 null 반환
    }

}
