package capstone.chatbot.repository;

import capstone.chatbot.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
           Member member = createMember1("최민준","chlalswns200","1234","25","175","68",1,
                   "없음","없음","없음","없음","없음");
           em.persist(member);
           Disease disease1 = createDisease("탈모","머리가 빠집니다",3,"없어요");
           em.persist(disease1);
           Disease disease2 = createDisease("비염","코막혀요",3,"이비인후과");
           em.persist(disease2);
           DiagnosisDisease diagnosisDisease1 = DiagnosisDisease.createDiagnosisDiseaseList(disease1);
           DiagnosisDisease diagnosisDisease2 = DiagnosisDisease.createDiagnosisDiseaseList(disease2);

           Diagnosis diagnosis = Diagnosis.createDiagnosis(member, diagnosisDisease1);

           Diagnosis diagnosis2 = Diagnosis.createDiagnosis(member, diagnosisDisease2);
           em.persist(diagnosis);
           em.persist(diagnosis2);
        }

        public void dbInit2() {
            Member member = createMember1("김서연","rlatjdus200","1q2w3e4r","25","160","57",0,
                    "펜타닐","주에5병","고혈압","다리 골절","여자");
            em.persist(member);
            Disease disease1 = createDisease("웃음","웃음을 참지 못해요",1,"조커");
            em.persist(disease1);
            Disease disease2 = createDisease("병명","아파요",2,"정형외과");
            em.persist(disease2);
            DiagnosisDisease diagnosisDisease1 = DiagnosisDisease.createDiagnosisDiseaseList(disease1);
            DiagnosisDisease diagnosisDisease2 = DiagnosisDisease.createDiagnosisDiseaseList(disease2);

            Diagnosis diagnosis = Diagnosis.createDiagnosis(member, diagnosisDisease1);
            Diagnosis diagnosis2 = Diagnosis.createDiagnosis(member, diagnosisDisease2);
            em.persist(diagnosis);
            em.persist(diagnosis2);
        }

        private Member createMember(String name,String loginId,String password,String age, String height, String weight, long gender) {

            Member member = new Member();
            member.setName(name);
            member.setLoginId(loginId);
            member.setPassword(password);
            member.setProfile(new Profile(age,height,weight,gender));
            return member;
        }

        private Member createMember1(String name,String loginId,String password,String age, String height, String weight, long gender,
                                    String drug, String social, String family, String trauma, String femininity) {

            Member member = new Member();
            member.setName(name);
            member.setLoginId(loginId);
            member.setPassword(password);
            member.setProfile(new Profile(age,height,weight,gender));
            member.setBasicExam(new BasicExam(drug, social, family, trauma, femininity));
            return member;
        }

        private Disease createDisease(String name, String info, int level, String department) {

            Disease disease = new Disease();
            disease.setName(name);
            disease.setInfo(info);
            disease.setLevel(level);
            disease.setDepartment(department);

            return disease;
        }

    }

}
