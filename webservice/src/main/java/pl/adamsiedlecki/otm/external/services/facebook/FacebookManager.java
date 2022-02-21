package pl.adamsiedlecki.otm.external.services.facebook;

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

    private static final String APP_NAMESPACE = "otm";
    private final FacebookApiProperties properties;

    public String postMessage(final String message) {
        Facebook fb = new FacebookTemplate(properties.getUserAccessToken(), APP_NAMESPACE);
        PagePostData data = new PagePostData(properties.getOtmPageId());
        data.message(message);
        return fb.pageOperations().post(data);
    }

    public String postChart(final File file, final String caption) {
        Facebook fb = new FacebookTemplate(properties.getUserAccessToken(), APP_NAMESPACE);
        return fb.pageOperations()
                .postPhoto(properties.getOtmPageId(), properties.getPhotoAlbumId(), new FileSystemResource(file), caption);
    }

    public String postComment(final String postId, final String comment) {
        Facebook fb = new FacebookTemplate(properties.getPageAccessToken(), APP_NAMESPACE);
        return fb.commentOperations().addComment(postId, comment);
    }
}
