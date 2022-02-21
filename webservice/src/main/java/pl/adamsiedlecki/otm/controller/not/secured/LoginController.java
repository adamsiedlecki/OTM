package pl.adamsiedlecki.otm.controller.not.secured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamsiedlecki.otm.db.user.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(final Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/login-error")
    public String getLoginWithError(final Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
