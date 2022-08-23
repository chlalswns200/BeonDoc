package capstone.chatbot.repository;

import capstone.chatbot.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.FileInputStream;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit3();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit3() {
            try {

                //초기 텍스트 파일을 불러오기 위한 파일 경로 지정 실시
                String filePath = "C:\\Capstone\\chatbot\\Data\\diseaseInfo.csv";

                //파일을 읽어오기 위한 FileInputStream 객체 선언
                FileInputStream fileStream = null;

                //FileInputStream에 읽어올 파일 경로 지정 실시
                fileStream = new FileInputStream(filePath);

                byte readBuffer[] = new byte[fileStream.available()];

                while (fileStream.read(readBuffer) != -1) {
                    String str = new String(readBuffer,"UTF-8"); // csv 파일의 데이터를 읽어옴

                    String[] StrList = str.split("\n"); // \n를 기준으로 문자로 나누기
                    for (String s : StrList) {

                        String[] diseaseList = s.split(","); // ,를 기준으로 문자 나누기
                        String name = diseaseList[0]; // 이름 변수 생성
                        String department = diseaseList[1].replace('`',','); // 담당 부서 변수 생성
                        String info = diseaseList[2].replace('`',',');// 정보 변수 생성
                        String cause = diseaseList[3].replace('`',','); // 원인 변수 생성
                        String symptom = diseaseList[4].replace('`',',');// 증상 변수 생성

                        Disease disease = createDisease(name,info,department,cause,symptom); //disease 클래스 변수 생성
                        em.persist(disease); // db에 저장

                    }
                }
                fileStream.close(); // 입출력 스트림 종료
            }
            catch(Exception e) {
                System.out.println(e.getMessage()); // 예외 발생시 문자 출력
            }
        }

        // diseases 변수를 생성하기 위한 함수
        private Disease createDisease(String name, String info, String department,String cause,String symptom) {
            Disease disease = new Disease();
            disease.setName(name);
            disease.setInfo(info);
            disease.setDepartment(department);
            disease.setCause(cause);
            disease.setSymptom(symptom);

            return disease;
        }

    }

}
