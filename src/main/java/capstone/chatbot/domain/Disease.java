package capstone.chatbot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Disease {

    @Id
    @GeneratedValue
    @Column(name = "disease_id")
    private Long id;

    private String name;

    private String info;

    private int level;

    private String department;



}
