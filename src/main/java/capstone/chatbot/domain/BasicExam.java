package capstone.chatbot.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable//어딘가에 내장이 될 수도 있다.
@Getter
public class BasicExam {

    public BasicExam(String drug, String social, String family, String trauma, String femininity) {
        this.drug = drug;
        this.social = social;
        this.family = family;
        this.trauma = trauma;
        this.femininity = femininity;
    }

    private String drug;
    private String social;
    private String family;
    private String trauma;
    private String femininity;

    protected BasicExam() {

    }

}
