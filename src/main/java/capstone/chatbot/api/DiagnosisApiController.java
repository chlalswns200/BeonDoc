package capstone.chatbot.api;

import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.DiagnosisDisease;
import capstone.chatbot.domain.Disease;
import capstone.chatbot.domain.Member;
import capstone.chatbot.service.DiagnosisService;
import capstone.chatbot.service.DiseaseService;
import capstone.chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DiagnosisApiController {

    private final DiseaseService diseaseService;
    private final MemberService memberService;
    private final DiagnosisService diagnosisService;

    //진단 결과 저장 api
    //request에서 받은 질병 3개의 병명을 통해 db에서 질병 정보 조회 후 각 질병의 발생 확률과 함께 db에 저장
    @PostMapping("/api/diagnosis/{id}")
    public DiagnosisResponse Diagnosis(@PathVariable("id") Long id, @RequestBody @Valid DiagnosisApiController.DiagnosisRequest request) {

        Disease disease1 = diseaseService.findByName(request.getDiseaseName1()); //request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장
        Disease disease2 = diseaseService.findByName(request.getDiseaseName2()); //request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장
        Disease disease3 = diseaseService.findByName(request.getDiseaseName3()); //request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장
        Member findMember = memberService.findOne(id); //입력받은 id를 통해 회원 조회 후 저장

        DiagnosisDisease diagnosisDisease1 = DiagnosisDisease.createDiagnosisDiseaseList(disease1); //저장 된 질병 데이터를 진단 된 질병 데이터로 변환
        DiagnosisDisease diagnosisDisease2 = DiagnosisDisease.createDiagnosisDiseaseList(disease2); //저장 된 질병 데이터를 진단 된 질병 데이터로 변환
        DiagnosisDisease diagnosisDisease3 = DiagnosisDisease.createDiagnosisDiseaseList(disease3); //저장 된 질병 데이터를 진단 된 질병 데이터로 변환

        Diagnosis diagnosis = Diagnosis.createDiagnosis(findMember, request.getPercent(), diagnosisDisease1,diagnosisDisease2,diagnosisDisease3); // 진단 변수 생성 후 위에서 입력받은 값들 인자로 저장

        diagnosisService.addDiagnosis(diagnosis); // 진단 기록 저장

        DiagnosisDTOV2 diagnosisDTO1 = new DiagnosisDTOV2(disease1.getName(),disease1.getInfo(),disease1.getDepartment(),disease1.getCause(),disease1.getSymptom()); // 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출
        DiagnosisDTOV2 diagnosisDTO2 = new DiagnosisDTOV2(disease2.getName(),disease2.getInfo(),disease2.getDepartment(),disease2.getCause(),disease2.getSymptom()); // 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출
        DiagnosisDTOV2 diagnosisDTO3 = new DiagnosisDTOV2(disease3.getName(),disease3.getInfo(),disease3.getDepartment(),disease3.getCause(),disease3.getSymptom()); // 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출

        List<DiagnosisDTOV2> diagnosisDTOV2List = Arrays.asList(diagnosisDTO1, diagnosisDTO2, diagnosisDTO3); // 저장 된 diagnosisDTOV2 3개를 list 형태로 변환

        return new DiagnosisResponse(diagnosisDTOV2List); // list로 변환한 diagnosisV2를 반환
    }

    //진단 결과 조회 api
    //request에서 받은 질병 3개의 병명을 통해 db에서 질병 정보 조회 후 반환
    @PostMapping("/api/diagnosis2/{id}")
    public DiagnosisResponse Diagnosis2(@PathVariable("id") Long id, @RequestBody @Valid DiagnosisApiController.DiagnosisRequest request) {

        Disease disease1 = diseaseService.findByName(request.getDiseaseName1());//request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장
        Disease disease2 = diseaseService.findByName(request.getDiseaseName2());//request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장
        Disease disease3 = diseaseService.findByName(request.getDiseaseName3());//request를 통해 입력 받은 질병 명으로 db에 있는 질병 데이터 조회 후 저장

        DiagnosisDTOV2 diagnosisDTO1 = new DiagnosisDTOV2(disease1.getName(),disease1.getInfo(),disease1.getDepartment(),disease1.getCause(),disease1.getSymptom());// 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출
        DiagnosisDTOV2 diagnosisDTO2 = new DiagnosisDTOV2(disease2.getName(),disease2.getInfo(),disease2.getDepartment(),disease2.getCause(),disease2.getSymptom());// 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출
        DiagnosisDTOV2 diagnosisDTO3 = new DiagnosisDTOV2(disease3.getName(),disease3.getInfo(),disease3.getDepartment(),disease3.getCause(),disease3.getSymptom());// 질병 데이터에서 diagnosisDTOV2에 맞는 데이터만 추출

        List<DiagnosisDTOV2> diagnosisDTOV2List = Arrays.asList(diagnosisDTO1, diagnosisDTO2, diagnosisDTO3);// 저장 된 diagnosisDTOV2 3개를 list 형태로 변환

        return new DiagnosisResponse(diagnosisDTOV2List);// list로 변환한 diagnosisV2를 반환
    }

    //진단 기록 조회 api
    //해당 id를 가진 진단 결과를 db에서 조회한 뒤 반환
    @GetMapping("api/diagnosisByOne/{id}")
    public List<DiagnosisByOneResponse> DiagnosisByOne(@PathVariable("id") Long id) {

        Diagnosis findDig = diagnosisService.findOne(id); //db에서 해당 id를 가진 진단 결과 조회 후 저장
        List<DiagnosisDisease> diagnosisDiseases = findDig.getDiagnosisDiseases(); // 진단 된 질병 list 를 저장
        List<DiagnosisByOneResponse> oneList =  new ArrayList<>(); // response로 반환 할 list 생성
        String percent = findDig.getPercent(); // 찾은 진단 결과의 확률을 저장

        String[] split = percent.split(" "); // 확률 값을 3개로 분할 하기 위해 공백을 기준으로 string을 나눠서 list 형태로 저장
        Integer cnt = 0; // list 저장을 위한 int 변수 생성

        //반복문을 통해 진단 된 질병 3개를 response 형식에 맞게 저장
        for (DiagnosisDisease diagnosisDisease : diagnosisDiseases) {

            Long diseaseId = diagnosisDisease.getDisease().getId(); // 진단 된 질병의 id를 반환
            String diseaseName = diagnosisDisease.getDisease().getName(); // 진단 된 질병의 병명을 반환

            DiagnosisByOneResponse diagnosisByOneResponse = new DiagnosisByOneResponse(diseaseId,diseaseName,split[cnt]); //respones 형식에 맞춰 변수 생성 후 저장
            cnt = cnt+1; //split list의 다음 값에 접근하기 위해 cnt값을 +1
            oneList.add(diagnosisByOneResponse); // response 반환 을 위해 생성한 list에 생성한 변수 저장
        }
        return oneList; // list 반환

    }

    //진단 기록 조회 api 호출 후 반환 하는 데이터 클래스
    @Data
    static class DiagnosisByOneResponse {
        public DiagnosisByOneResponse(Long diseaseId, String diseaseName, String percent) {
            this.diseaseId = diseaseId;
            this.diseaseName = diseaseName;
            this.percent = percent;
        }
        private Long diseaseId;
        private String diseaseName;
        private String percent;
    }

    //진단 결과 저장api 호출 시 필요한 데이터 클래스
    @Data
    static class DiagnosisRequest {

        private String diseaseName1;
        private String diseaseName2;
        private String diseaseName3;
        private String percent;
    }

    //진단 결과 저장api 호출 후 반환해야 하는 데이터 클래스
    @Data
    static class DiagnosisResponse {
        public DiagnosisResponse(List<DiagnosisDTOV2> diagnosisDTOV2List) {
            this.diagnosisDTOV2List = diagnosisDTOV2List;
        }
        List<DiagnosisDTOV2> diagnosisDTOV2List;

    }

    //회원의 전체 진단 기록 조회 api
    //입력 받은 id의 회원을 찾은 뒤 전체 진단 기록을 반환 한다
    @GetMapping("/api/diagnosisList/{id}")
    public Result diagnosisListV2(@PathVariable("id") Long id) {

        List<DiagnosisDTO> diagnosisList = diagnosisService.findDiagnosis(id); // 입력 받은 id를 가진 회원을 조회 후 받은 진단 기록을 저장
        List<DiagnosisDTO> collect = diagnosisList.stream()
                .map(d -> new DiagnosisDTO(
                        d.getId(),
                        d.getName(),
                        d.getInfo(),
                        d.getDepartment(),
                        d.getCause(),
                        d.getSymptom(),
                        d.getDiagnosisTime(),
                        d.getPercent()
                ))
                .collect(Collectors.toList()); // DiagnosisDTO형식에 맞게 진단기록에서 데이터 추출 후 저장

        List<DiagnosisDTOV3> v3List = new ArrayList<>(); // 반환 형식인 dignosisDTOV3의 list 생성

        for (DiagnosisDTO diagnosisDTO : collect) {
            diagnosisDTO.getId();
            diagnosisDTO.getName();
            DiagnosisDTOV3 diagnosisDTOV3 = new DiagnosisDTOV3(diagnosisDTO.getId(), diagnosisDTO.getName(),
                    diagnosisDTO.getPercent(),diagnosisDTO.getDiagnosisTime().toString());
            v3List.add(diagnosisDTOV3);
        } //반복문을 통해서 저장된 진단기록에서 DiagnosisDTOV3 형식에 맞게 데이터 저장


        List<List<DiagnosisDTOV3>> disList = new ArrayList<>(); // 진단 결과 1번 당 질병 데이터는 3개 데이터로 나오기 때문에 전체 list를 3개씩 분할
        for (int i = 0; i < collect.size(); i = i + 3) {

            disList.add(v3List.subList(i, i + 3));// list 분할

        }
        return new Result(disList); // 3개씩 분할 된 최종 list를 반환
    }

    //전체 진단 결과 반환을 위한 데이터 형식 클래스
    @Data
    @AllArgsConstructor
    static class DiagnosisDTOV3 {
        private Long id;
        private String diseaseName;
        private String percent;
        private String localDate;
    }

    //전체 진단 결과 반환을 위한 데이터 클래스
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
