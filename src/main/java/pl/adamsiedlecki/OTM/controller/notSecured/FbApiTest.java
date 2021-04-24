package pl.adamsiedlecki.OTM.controller.notSecured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamsiedlecki.OTM.dataFetcher.Schedule;
import pl.adamsiedlecki.OTM.facebook.FacebookManager;

@Controller
@RequestMapping("/test")
public class FbApiTest {

    private final FacebookManager facebookManager;
    private final Schedule schedule;

    @Autowired
    public FbApiTest(FacebookManager facebookManager, Schedule schedule) {
        this.facebookManager = facebookManager;
        this.schedule = schedule;
    }

    @GetMapping("/fb/message")
    public @ResponseBody
    String test() {
        String postId = facebookManager.postMessage("test message");
        facebookManager.postComment(postId, "test comment");
        return postId;
    }

    @GetMapping("/schedule")
    public @ResponseBody
    String testSchedule() {
        schedule.checkTemperatures();
        return "nothing returned";
    }
}
