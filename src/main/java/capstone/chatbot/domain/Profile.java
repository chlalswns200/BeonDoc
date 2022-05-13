package capstone.chatbot.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable//어딘가에 내장이 될 수도 있다.
@Getter
public class Profile {

    private String age;
    private String height;
    private String weight;
    private long gender;

    protected Profile() {

    }

    public Profile(String age, String height, String weight, long gender) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

}
