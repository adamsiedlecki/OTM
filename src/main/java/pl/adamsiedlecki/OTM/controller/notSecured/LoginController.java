package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamsiedlecki.OTM.db.user.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/login-error")
    public String getLoginWithError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
