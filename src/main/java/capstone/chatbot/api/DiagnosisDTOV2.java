package capstone.chatbot.api;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiagnosisDTOV2 {

    String name;
    String info;
    String department;
    String cause;
    String symptom;


}
