package capstone.chatbot.api;

import capstone.chatbot.domain.BasicExam;
import capstone.chatbot.domain.Member;
import capstone.chatbot.domain.Profile;
import capstone.chatbot.security.SHA256;
import capstone.chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
@RestController
@RequiredArgsConstructor

public class MemberApiController {

    private final MemberService memberService;

    // 회원 가입 api
    //request 데이터를 바탕으로 회원 등록 후 저장
    @PostMapping("/api/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        SHA256 sha256 = new SHA256(); // 암호화 클래스 생성

        BasicExam basicExam = new BasicExam(request.getDrug(),request.getSocial(),
                request.getFamily(), request.getTrauma(), request.getFemininity()); // 기초 문진 변수 생성 후 입력 받은 값 저장

        Profile profile = new Profile(request.getAge(),
                request.getHeight(),
                request.getWeight(),
                request.getGender()); // 프로필 변수 생성 후 입력 받은 값 저장

        Member member = new Member(); // 멤버 변수 생성
        member.setName(request.getName()); // 멤버 변수의 이름 저장
        member.setLoginId(request.getLoginId()); // 멤버 변수의 loginId 저장

        String password = sha256.SHA256Encrypt(request.getPassword()); // request에서 입력 받은 비밀 번호 암호화
        member.setPassword(password);// 암호화 이후 멤버 변수의 비밀 번호 저장

        member.setProfile(profile); // 멤버의 임베드 클래스인 프로필의 데이터 저장
        member.setBasicExam(basicExam); // 멤버의 임베드 클래스인 기초 문진의 데이터 저장

        Long id = memberService.join(member); //  생성한 member를 db에 저장 한 뒤 id를 반환

        return new CreateMemberResponse(id); // id를 반환
    }

    //회원 가입 api 호출시 필요한 데이터 클래스
    @Data
    static class CreateMemberRequest {
        private String name;
        private String loginId;
        private String password;
        private String age;
        private String height;
        private String weight;
        private Long gender;
        private String drug;
        private String social;
        private String family;
        private String trauma;
        private String femininity;
    }

    //회원 가입 api 호출 후 반환할 데이터 클래스
    @Data
    static class CreateMemberResponse {

        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    //회원 정보 수정api
    //id를 통해 입력 받은 회원의 정보를 request에 입력 받은 값으로 저장
    @PutMapping("/api/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id,request.getLoginId(),
                request.getName(),request.getAge(),request.getHeight(),
                request.getWeight(),request.getGender(),request.getDrug(),
                request.getSocial(),request.getFamily(),
                request.getTrauma(),request.getFemininity()); // request에서 입력받은 데이터를 통해 db데이터 업데이트

        Member findMember = memberService.findOne(id); // id를 입력한 회원의 정보를 db에서 찾아서 반환

        return new UpdateMemberResponse(findMember.getId(), findMember.getLoginId(),
                findMember.getName(),findMember.getProfile().getAge(),findMember.getProfile().getHeight(),
                findMember.getProfile().getWeight(),findMember.getProfile().getGender(),findMember.getBasicExam().getDrug(),
                findMember.getBasicExam().getSocial(),findMember.getBasicExam().getFamily(),
                findMember.getBasicExam().getTrauma(),findMember.getBasicExam().getFemininity()); //request값으로 수정 된 회원의 정보를 다시 반환
    }

    //회원 정보 수정 api 호출 시 필요한 데이터 클래스
    @Data
    static class UpdateMemberRequest {
        private String name;
        private String loginId;
        private String age;
        private String height;
        private String weight;
        private Long gender;
        private String drug;
        private String social;
        private String family;
        private String trauma;
        private String femininity;
    }

    //회원 정보 수정 api 호출 이후 반환 해야 할 데이터 클래스
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{

        private Long id;
        private String loginId;
        private String name;
        private String age;
        private String height;
        private String weight;
        private long gender;
        private String drug;
        private String social;
        private String family;
        private String trauma;
        private String femininity;

    }

    //비밀 번호 일치 확인 api
    //입력 받은 id를 통해 db에서 찾은 회원의 비밀번호와 request에서 입력받은 비밀번호가 일치하는지 확인
    @PostMapping("/api/members/pwcheck/{id}")
    public Integer check(@PathVariable("id") Long id,
                      @RequestBody @Valid PasswordRequest request) {

        SHA256 sha256 = new SHA256(); // 암호화 객체 생성
        String password = sha256.SHA256Encrypt(request.getPassword()); // request에서 입력 받은 비밀번호 암호화

        Member findMember = memberService.findOne(id); // id를 통해 db에서 회원 조회 후 저장

        if (findMember.getPassword().equals(password)) {
            return 0;
        } // db에 저장된 비밀번호와 request를 통해 입력받은 비밀번호가 같으면 0 반환 아니면 1 반환
        return 1;
    }

    //비밀번호 수정api
    //request에서 입력받은 값으로 비밀번호 수정
    @PutMapping("/api/members/pwupdate/{id}")
    public void updatePassword(
            @PathVariable("id") Long id,
            @RequestBody @Valid PasswordRequest request) {

            memberService.updatePassword(id, request.getPassword()); //id를 통해 db속 회원 조회 이후 rquest에서 입력받은 비밀번호로 정보 업데이트

    }
    //비밀번호 수정 api 호출시 요청에 필요한 데이터 클래스
    @Data
    static class PasswordRequest {
        String password;
    }

    //loginId 중복 검사 api
    //request에서 입력 받은 loginId가 기존에 db에 저장되어 있는 loginId와 중복 되는지 check
    @PostMapping("/api/members/loginIdCheck/")
    public Integer idcheck(@RequestBody @Valid loginIdRequest request) {

        Integer check = memberService.checkLoginId(request.getLoginId()); // loginId가 db에 이미 있는 데이터면 1 아니면 0을 반환
        return check;

    }

    //loginId 중복 검사 api 호출 시 필요한 데이터 클래스
    @Data
    static class loginIdRequest {
        String loginId;
    }

    //회원 탈퇴api
    @DeleteMapping ("/api/members/{id}")
    public void deleteMember(@PathVariable("id") Long id){

        memberService.out(id); // 입력 받은 id를 가진 회원의 정보를 db에서 삭제
    }

}
