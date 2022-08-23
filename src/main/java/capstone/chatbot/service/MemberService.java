package capstone.chatbot.service;

import capstone.chatbot.domain.BasicExam;
import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.Member;
import capstone.chatbot.domain.Profile;
import capstone.chatbot.repository.DiagnosisRepository;
import capstone.chatbot.repository.MemberRepository;
import capstone.chatbot.security.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class MemberService {

    //암호화를 위해 sha256 불러오기
    SHA256 sha265 = new SHA256();

    private final MemberRepository memberRepository;
    private final DiagnosisRepository diagnosisRepository;

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        memberRepository.save(member); // db에 member 를 저장
        return member.getId(); // id를 반환
    }

    // id로 회원 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);// 검색된 회원의 정보 반환
    }

    // 로그인 id가 기존 db에 저장되어 있어 중복되는지 조회
    public Integer checkLoginId(String loginId) {
        return memberRepository.checkLoginId(loginId);
    }

    //회원의 정보를 수정한다
    @Transactional
    public void update(Long id,String loginId,String name,String age, String height, String weight, long gender,
                       String drug, String social, String family, String trauma, String femininity) {
        Member member = memberRepository.findOne(id); // id를 통해 db에 존재하는 회원 데이터를 불러온다

        BasicExam basicExam = new BasicExam(drug, social, family, trauma, femininity); // 기초 문진 값 타입 변수 생성

        Profile profile = new Profile(age, height, weight, gender); // 프로필 값 타입 변수 생성
        member.setName(name); // 이름 수정
        member.setLoginId(loginId); // loginId 수정
        member.setProfile(profile); // 프로필 수정
        member.setBasicExam(basicExam); // 기초문진 수정
    }

    //비밀번호를 수정한다
    @Transactional
    public void updatePassword(Long id,String password) {

        Member findmember = memberRepository.findOne(id); // id를 통해 db에 존재하는 회원 데이터를 불러온다
        String passwordc = sha265.SHA256Encrypt(password); // sha256을 통해서 비밀번호 암호화
        findmember.setPassword(passwordc); // 비밀번호 수정

    }

    // 회원정보를 삭제한다
    @Transactional
    public void out(Long memberId) {

        List<Diagnosis> diagnoses = diagnosisRepository.MemberDiagnosisList(memberId); // memberId를 통해서 해당 회원으로 등록된 모든 진단 기록을 불러온다

        for (Diagnosis diagnosis : diagnoses) {
            diagnosisRepository.deleteDiagnosis(diagnosis.getId()); // 반복문을 통해서 진단 기록을 삭제한다
        }

        memberRepository.deleteMember(memberId); //해당 memberId의 회원 정보를 삭제한다

    }

}
