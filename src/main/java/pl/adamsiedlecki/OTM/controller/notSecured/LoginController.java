package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(Model model) {
        //model.addAttribute("user", new User());
        return "login";
    }

//    @PostMapping("/login")
//    public String postLogin(@ModelAttribute User user, Model model) {
//        System.out.println("USER: "+user);
//        System.out.println(user.getUsername());
//        return "mainPage";
//    }

    @GetMapping("/login-error")
    public String getLoginWithError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
