package capstone.chatbot.service;

import capstone.chatbot.api.DiagnosisDTO;
import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.repository.DiagnosisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    //진단 내용을 db에 저장 한다
    @Transactional
    public Long addDiagnosis(Diagnosis diagnosis) {
        diagnosisRepository.save(diagnosis); // 진단 변수를 db에 저장 한다
        return diagnosis.getId(); // 해당 진단의 id를 반환 한다
    }

    //진단 내용을 id를 통해 db에서 조회 한다
     public Diagnosis findOne(Long id) {
        return diagnosisRepository.findOne(id); //db에서 id를 통해 찾은 진단 내용을 반환 한다.
    }

    //memberId를 통해서 해당 회원의 전체 진단 기록을 조회 한다
    public List<DiagnosisDTO> findDiagnosis(Long memberId) {
        return diagnosisRepository.findAllByStringV2(memberId); //memeberId를 통해서 전체 진단 기록을 반환한다
    }

}
