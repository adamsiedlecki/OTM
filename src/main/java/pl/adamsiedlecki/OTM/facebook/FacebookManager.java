package pl.adamsiedlecki.OTM.facebook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FacebookManager {

    String appNamespace = "otm";
    @Value("${fb.otm.page.id}")
    private String otmPageId;
    @Value("${fb.otm.page.access.token}")
    private String pageAccessToken;
    @Value("${fb.otm.user.access.token}")
    private String userAccessToken;
    @Value("${fb.otm.photo.album.id}")
    private String albumId;

    private FacebookManager() {

    }

    public String postMessage(String message) {
        Facebook fb = new FacebookTemplate(userAccessToken, appNamespace);
        PagePostData data = new PagePostData(otmPageId);
        data.message(message);
        return fb.pageOperations().post(data);
    }

    public String postChart(File file, String caption) {
        Facebook fb = new FacebookTemplate(userAccessToken, appNamespace);
        return fb.pageOperations().postPhoto(otmPageId, albumId, new FileSystemResource(file), caption);
    }

    public String postComment(String postId, String comment) {
        Facebook fb = new FacebookTemplate(pageAccessToken, appNamespace);
        return fb.commentOperations().addComment(postId, comment);
    }
}
