package capstone.chatbot.service;

import capstone.chatbot.domain.Disease;
import capstone.chatbot.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class DiseaseService {
    private final DiseaseRepository diseaseRepository;

    //id를 통해서 질병 데이터 불러오기
    public Disease findOne(Long diseaseId) {
        return diseaseRepository.findOne(diseaseId); //해당diseaseId에 따른 질병데이터 반환
    }

    //질병명을 통해서 질병 데이터 불러오기
    public Disease findByName(String name) {
        return diseaseRepository.findByName(name); // name을 통해db에서 질병데이터 조회 후 반환
    }
}
