package pl.adamsiedlecki.otm.externalServices.facebook;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagePostData;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import pl.adamsiedlecki.otm.config.FacebookApiProperties;

import java.io.File;

@Service
@RequiredArgsConstructor
public class FacebookManager {

    String appNamespace = "otm";
    private final FacebookApiProperties properties;

    public String postMessage(String message) {
        Facebook fb = new FacebookTemplate(properties.getUserAccessToken(), appNamespace);
        PagePostData data = new PagePostData(properties.getOtmPageId());
        data.message(message);
        return fb.pageOperations().post(data);
    }

    public String postChart(File file, String caption) {
        Facebook fb = new FacebookTemplate(properties.getUserAccessToken(), appNamespace);
        return fb.pageOperations().postPhoto(properties.getOtmPageId(), properties.getPhotoAlbumId(), new FileSystemResource(file), caption);
    }

    public String postComment(String postId, String comment) {
        Facebook fb = new FacebookTemplate(properties.getPageAccessToken(), appNamespace);
        return fb.commentOperations().addComment(postId, comment);
    }
}
