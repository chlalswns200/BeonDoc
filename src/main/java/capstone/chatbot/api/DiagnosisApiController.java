package capstone.chatbot.api;

import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.DiagnosisDisease;
import capstone.chatbot.domain.Disease;
import capstone.chatbot.domain.Member;
import capstone.chatbot.repository.DiagnosisSearch;
import capstone.chatbot.service.DiagnosisService;
import capstone.chatbot.service.DiseaseService;
import capstone.chatbot.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController //데이터 자체를
@RequiredArgsConstructor
public class DiagnosisApiController {

    private final DiseaseService diseaseService;
    private final MemberService memberService;
    private final DiagnosisService diagnosisService;

    @PostMapping("/api/diagnosis/{id}")
    public DiagnosisResponse test1(@PathVariable("id") Long id, @RequestBody @Valid DiagnosisApiController.DiagnosisRequest request) {

        Disease disease1 = diseaseService.findByName(request.getDiseaseName());

        Member findMember = memberService.findOne(id);

        DiagnosisDisease diagnosisDisease1 = DiagnosisDisease.createDiagnosisDiseaseList(disease1);

        Diagnosis diagnosis = Diagnosis.createDiagnosis(findMember, diagnosisDisease1);

        Long diagnosis_id = diagnosisService.addDiagnosis(diagnosis);

        return new DiagnosisResponse(diagnosis_id);
    }

    @Data
    static class DiagnosisRequest {
        private String diseaseName;
    }

    @Data
    static class DiagnosisResponse {

        public DiagnosisResponse(Long id) {
            this.id = id;
        }

        private Long id;

    }


    @GetMapping("/api/diagnosisList/{id}")
    public Result diagnosisListV2(@PathVariable("id") Long id) {
        List<DiagnosisDTO> diagnosisList = diagnosisService.findDiagnosis(id);

        List<DiagnosisDTO> collect = diagnosisList.stream()
                .map(d -> new DiagnosisDTO(
                        d.getName(),
                        d.getInfo(),
                        d.getLevel(),
                        d.getDepartment(),
                        d.getDiagnosisTime()
                ))
                .collect(Collectors.toList());

        return new Result(collect);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @GetMapping("/api/diagnosisInfo/{id}")
    public DiagnosisDTO diagnosisInfo(@PathVariable("id") Long id) {

        DiagnosisDTO oneByString = diagnosisService.findOneByString(id);

        return oneByString;
    }



    @Data
    @AllArgsConstructor
    static class dtest {


        @NotNull
        private String name;
        @NotNull
        private String disease1;
        @NotNull
        private String info1;

    }


}
