package pl.adamsiedlecki.OTM.controller.secured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {

    @GetMapping("/about-us")
    public String getAboutUs(Model m) {
        return "aboutUs";
    }
}
