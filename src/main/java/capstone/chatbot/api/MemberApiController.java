package capstone.chatbot.api;

import capstone.chatbot.domain.BasicExam;
import capstone.chatbot.domain.Member;
import capstone.chatbot.domain.Profile;
import capstone.chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
@RestController //데이터 자체를
@RequiredArgsConstructor

public class MemberApiController {

    private final MemberService memberService;


    @GetMapping("/api/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName(),
                        m.getLoginId(),
                        m.getPassword(),
                        m.getProfile().getGender(),
                        m.getProfile().getAge(),
                        m.getProfile().getHeight(),
                        m.getProfile().getWeight(),
                        m.getBasicExam().getDrug(),
                        m.getBasicExam().getSocial(),
                        m.getBasicExam().getFamily(),
                        m.getBasicExam().getTrauma(),
                        m.getBasicExam().getFemininity()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;

        private String loginId;

        private String password;

        private long gender;

        private String age;

        private String height;

        private String weight;

        private String drug;
        private String social;
        private String family;
        private String trauma;
        private String femininity;
    }


    @PostMapping("/api/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid UpdateMemberRequest request){

        BasicExam basicExam = new BasicExam(request.getDrug(),request.getSocial(),
                request.getFamily(), request.getTrauma(), request.getFemininity());

        Profile profile = new Profile(request.getAge(),
                request.getHeight(),
                request.getWeight(),
                request.getGender());

        Member member = new Member();
        member.setName(request.getName());
        member.setLoginId(request.getLoginId());
        member.setPassword(request.getPassword());
        member.setProfile(profile);
        member.setBasicExam(basicExam);

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id,request.getLoginId(),request.getPassword(),
                request.getName(),request.getAge(),request.getHeight(),
                request.getWeight(),request.getGender(),request.getDrug(),
                request.getSocial(),request.getFamily(),
                request.getTrauma(),request.getFemininity());

        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getLoginId(),findMember.getPassword(),
                findMember.getName(),findMember.getProfile().getAge(),findMember.getProfile().getHeight(),
                findMember.getProfile().getWeight(),findMember.getProfile().getGender(),findMember.getBasicExam().getDrug(),
                findMember.getBasicExam().getSocial(),findMember.getBasicExam().getFamily(),
                findMember.getBasicExam().getTrauma(),findMember.getBasicExam().getFemininity());
    }

    @Data
    static class UpdateMemberRequest {

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

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{

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

    @Data
    static class CreateMemberResponse {

        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }



}
