package capstone.chatbot.service;

import capstone.chatbot.api.DiagnosisDTO;
import capstone.chatbot.domain.Diagnosis;
import capstone.chatbot.domain.DiagnosisDisease;
import capstone.chatbot.domain.Disease;
import capstone.chatbot.domain.Member;
import capstone.chatbot.repository.DiagnosisRepository;
import capstone.chatbot.repository.DiagnosisSearch;
import capstone.chatbot.repository.DiseaseRepository;
import capstone.chatbot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MemberRepository memberRepository;
    private final DiseaseRepository diseaseRepository;


    @Transactional// jpa의 모든 데이터 변경은 가급적 transactional 안에서 이루어 져야 한다.
    public Long addDiagnosis(Diagnosis diagnosis) {
        diagnosisRepository.save(diagnosis);
        return diagnosis.getId();
    }

    @Transactional
    public Long Disease(Long memberId, Long diseaseId) {

        Member member = memberRepository.findOne(memberId);
        Disease disease = diseaseRepository.findOne(diseaseId);

        DiagnosisDisease diagnosisDisease = DiagnosisDisease.createDiagnosisDiseaseList(disease);

        Diagnosis diagnosis = Diagnosis.createDiagnosis(member, diagnosisDisease);

        diagnosisRepository.save(diagnosis);

        return diagnosis.getId();
    }



    public List<DiagnosisDTO> findDiagnosis(Long id) {
        return diagnosisRepository.findAllByStringV2(id);
    }

    public DiagnosisDTO findOneByString(Long id) {
        return diagnosisRepository.findOneByString(id);
    }
}
