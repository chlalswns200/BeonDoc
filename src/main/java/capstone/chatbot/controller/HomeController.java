package capstone.chatbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    // 웹으로 호출 시 홈 페이지
    @RequestMapping("/")
    public String home() {
        log.info("home controller");
        return "home";
    }

}
