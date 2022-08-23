package capstone.chatbot.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
@Setter
public class DiagnosisDTO {

    Long id;
    String name;
    String info;
    String department;
    String cause;
    String symptom;
    LocalDate diagnosisTime;
    String percent;

}
