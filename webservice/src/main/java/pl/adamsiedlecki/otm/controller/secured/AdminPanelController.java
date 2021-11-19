package pl.adamsiedlecki.otm.controller.secured;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {

    @GetMapping("/admin-panel")
    public String getAdminPanel() {
        return "admin-panel";
    }
}
