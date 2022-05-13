package capstone.chatbot.service;

import capstone.chatbot.domain.Disease;
import capstone.chatbot.repository.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class DiseaseService {

    private final DiseaseRepository diseaseRepository;

    @Transactional
    public void saveDisease(Disease disease) {
        diseaseRepository.save(disease);}

    public List<Disease> findDisease() {
        return diseaseRepository.findAll();
    }

    public Disease findone(Long diseaseId) {
        return diseaseRepository.findOne(diseaseId);
    }

    public Disease findByName(String name) {
        return diseaseRepository.findByName(name);
    }
}
