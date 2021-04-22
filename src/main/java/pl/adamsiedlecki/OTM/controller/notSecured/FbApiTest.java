package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.adamsiedlecki.OTM.facebook.FacebookManager;

@Controller
@RequestMapping("/test")
public class FbApiTest {

    private final FacebookManager facebookManager;

    @Autowired
    public FbApiTest(FacebookManager facebookManager) {
        this.facebookManager = facebookManager;
    }

    @GetMapping("/fb/message")
    public String test() {
        return facebookManager.postMessage("test message");
    }
}
