package capstone.chatbot.domain;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Member {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "member_id")

    private Long id;

    private String loginId;

    private String password;

    private String name;

    @Embedded
    private Profile profile;

    @Embedded
    private BasicExam basicExam;

    @OneToMany(mappedBy = "member")
    private List<Diagnosis> diagnosis = new ArrayList<>();




}
