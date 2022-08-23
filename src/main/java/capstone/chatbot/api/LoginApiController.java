package capstone.chatbot.api;

import capstone.chatbot.domain.Member;
import capstone.chatbot.service.LoginService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    //로그인 api
    //request에서 입력받은 loginid와 password가 db에 존재하는 것과 일치하는지 확인 한 뒤 일치하면 회원의 데이터를 반환
    @PostMapping("/api/login")
    public LoginMemberResponse login(@RequestBody @Valid LoginApiController.LoginMemberRequest request) {

        Member loginMember = loginService.login(request.getLoginId(), request.getPassword()); // request에서 입력받은 loginId와 password를 통해서 로그인 함수 호출 후 회원 정보 저장
        return new LoginMemberResponse(loginMember.getId(),
                loginMember.getLoginId(),
                loginMember.getName(),
                loginMember.getProfile().getAge(),
                loginMember.getProfile().getHeight(),
                loginMember.getProfile().getWeight(),
                loginMember.getProfile().getGender(),
                loginMember.getBasicExam().getDrug(),
                loginMember.getBasicExam().getSocial(),
                loginMember.getBasicExam().getFamily(),
                loginMember.getBasicExam().getTrauma(),
                loginMember.getBasicExam().getFemininity()
        ); // response 형식에 맞게 데이터 변환 후 반환

    }

    // 로그인 api 호출 시 요청 데이터 클래스
    @Data
    static class LoginMemberRequest {
        private String loginId;
        private String password;
    }

    //로그인 api 호출 후 반환 데이터 클래스
    @Data
    static class LoginMemberResponse {

        public LoginMemberResponse(Long id, String loginId, String name, String age, String height, String weight, long gender, String drug, String social, String family, String trauma, String femininity) {
            this.id = id;
            this.loginId = loginId;
            this.name = name;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.gender = gender;
            this.drug = drug;
            this.social = social;
            this.family = family;
            this.trauma = trauma;
            this.femininity = femininity;
        }
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

}

