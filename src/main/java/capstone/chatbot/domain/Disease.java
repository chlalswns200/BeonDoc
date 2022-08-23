package capstone.chatbot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Disease {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private Long id;

    private String name;

    @Column(length = 1000)
    private String info;
    private String department;

    @Column(length = 5000)
    private String cause;

    @Column(length = 5000)
    private String symptom;



}
