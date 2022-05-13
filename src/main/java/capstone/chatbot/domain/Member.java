package capstone.chatbot.domain;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.ls.LSOutput;


import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Member {

    @Id
    @GeneratedValue
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
