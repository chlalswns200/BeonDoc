package capstone.chatbot.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    private String loginId;

    private String password;

    private String name;

    private Long gender;

    private String age;

    private String height;

    private String weight;
}
