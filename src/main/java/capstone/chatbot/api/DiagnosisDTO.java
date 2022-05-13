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

    String name;
    String info;
    int level;
    String department;
    LocalDate diagnosisTime;

}
