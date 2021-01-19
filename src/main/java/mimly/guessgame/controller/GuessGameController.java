package mimly.guessgame.controller;

import inet.ipaddr.IPAddressString;
import lombok.extern.slf4j.Slf4j;
import mimly.guessgame.model.Guess;
import mimly.guessgame.model.GuessGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/")
@Slf4j(topic = "** Guess Game **")
public class GuessGameController {

    private final GuessGame guessGame;
    private final Map<String, IPAddressString> sessionIpMap;

    @Autowired
    public GuessGameController(GuessGame guessGame, Map<String, IPAddressString> sessionIpMap) {
        this.guessGame = guessGame;
        this.sessionIpMap = sessionIpMap;
    }

    @GetMapping("/invalidate")
    public String doInvalidateGet(HttpSession httpSession) {
        String sessionId = httpSession.getId();
        this.sessionIpMap.remove(sessionId);
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/gameover")
    public String doGameOverGet() {
        if (!guessGame.isOver()) {
            return "redirect:/";
        }
        return "gameover";
    }

    @GetMapping
    public String doGet(Model model) {
        if (guessGame.isOver()) {
            return "redirect:/gameover";
        }

        model.addAttribute("guessGame", guessGame);
        return "index";
    }

    @PostMapping
    public String doPost(Guess guess) {
        log.info("Received: " + guess);

        guessGame.updateErrorMessage(guess);
        if (guessGame.isValid(guess)) {
            guessGame.setLimits(guess);
            if (guessGame.isCorrect(guess)) {
                guessGame.setOver(true);
                return "redirect:/gameover";
            }
        }

        return "redirect:/";
    }
}
