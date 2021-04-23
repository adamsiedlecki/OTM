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
    @Value("${fb.otm.access.token}")
    private String pageAccessToken;
    @Value("${fb.otm.photo.album.id}")
    private String albumId;

    private FacebookManager() {

    }

    public String postMessage(String message) {
        Facebook fb = new FacebookTemplate(pageAccessToken, appNamespace);
        PagePostData data = new PagePostData(otmPageId);
        data.message(message);
        return fb.pageOperations().post(data);
    }

    public void postChart(File file, String caption) {
        Facebook fb = new FacebookTemplate(pageAccessToken, appNamespace);
        String photoId = fb.pageOperations().postPhoto(otmPageId, albumId, new FileSystemResource(file), caption);
        System.out.println(photoId);
    }

    public String postComment(String postId, String comment) {
        Facebook fb = new FacebookTemplate(pageAccessToken, appNamespace);
        return fb.commentOperations().addComment(postId, comment);
    }
}
