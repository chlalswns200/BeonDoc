package capstone.chatbot.api;

import capstone.chatbot.domain.Disease;
import capstone.chatbot.service.DiseaseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class DiseaseApiController {

    private final DiseaseService diseaseService;

    //질병 정보 조회 api
    //입력 받은 id의 질병 정보를 조회
    @GetMapping("/api/disease/{id}")
    public DiseaseResponse findDisease(@PathVariable("id") Long id) {

        Disease findDisease = diseaseService.findOne(id); // 해당 id를 가진 질병을 db에서 조회 한 뒤 저장
        return new DiseaseResponse(findDisease.getName(), findDisease.getInfo(), findDisease.getDepartment(),
                findDisease.getCause(), findDisease.getSymptom()); //response 형식에 맞게 반환

    }
    //질병명을 통한 질병 정보 조회 api
    //reqeust에서 입력 받은 질병명을 통해서 질병 정보 조회
    @PostMapping("/api/disease/byString")
    public DiseaseResponse DiseaseByString( @RequestBody @Valid DiseaseByStringRequest request) {

        Disease disease = diseaseService.findByName(request.getDiseaseName()); // request에서 입력 받은 질병 명을 통해서 질병 정보 조회 및 저장
        return new DiseaseResponse(disease.getName(),disease.getInfo(),disease.getDepartment(),
                disease.getCause(), disease.getSymptom()); //response 형식에 맞게 변환
    }

    // 질병명을 통한 질병 정보 조회 api 호출 데이터 클래스
    @Data
    static class DiseaseByStringRequest {
        private String diseaseName;
    }

    // 질병 조회 api 호출 이후 반환 데이터 클래스
    @Data
    @AllArgsConstructor
    static class DiseaseResponse {

        private String name;
        private String info;
        private String department;
        private String cause;
        private String symptom;

    }
}
