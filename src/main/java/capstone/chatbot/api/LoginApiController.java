package capstone.chatbot.api;

import capstone.chatbot.domain.BasicExam;
import capstone.chatbot.domain.Member;
import capstone.chatbot.domain.Profile;
import capstone.chatbot.service.LoginService;
import capstone.chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

@RestController //데이터 자체를
@RequiredArgsConstructor
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/api/login")
    public LoginMemberResponse login(@RequestBody @Valid LoginApiController.LoginMemberRequest request) {

        Member loginMember = loginService.login(request.getLoginId(), request.getPassword());

        return new LoginMemberResponse(loginMember.getId(),
                loginMember.getLoginId(),
                loginMember.getPassword(),
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
        );

    }

    @Data
    static class LoginMemberRequest {

        private String loginId;
        private String password;
    }

    @Data
    static class LoginMemberResponse {

        public LoginMemberResponse(Long id, String loginId, String password, String name, String age, String height, String weight, long gender, String drug, String social, String family, String trauma, String femininity) {
            this.id = id;
            this.loginId = loginId;
            this.password = password;
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
        private String password;
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

    @PostMapping("/api/loginV2")
    public LoginMemberResponseV2 loginV2(@RequestBody @Valid LoginApiController.LoginMemberRequest request) {

        Member loginMember = loginService.login(request.getLoginId(), request.getPassword());

        Profile profile = new Profile(loginMember.getProfile().getAge(),loginMember.getProfile().getHeight(),
                loginMember.getProfile().getWeight(), loginMember.getProfile().getGender());
        BasicExam basicExam = new BasicExam(loginMember.getBasicExam().getDrug(),loginMember.getBasicExam().getSocial(),
                loginMember.getBasicExam().getFamily(),loginMember.getBasicExam().getTrauma(),loginMember.getBasicExam().getFemininity());

        Member member = new Member();
        member.setId(loginMember.getId());
        member.setLoginId(loginMember.getLoginId());
        member.setPassword(loginMember.getPassword());
        member.setName(loginMember.getName());
        member.setProfile(profile);
        member.setBasicExam(basicExam);

        return new LoginMemberResponseV2(member);

    }

    @Data
    static class LoginMemberResponseV2 {

        public LoginMemberResponseV2(Member member) {
            this.member = member;
        }

        Member member;

    }
}

