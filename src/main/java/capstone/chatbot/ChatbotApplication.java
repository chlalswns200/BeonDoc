package capstone.chatbot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ChatbotApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	} // 프로젝트 실행
}
