package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class FacebookApiProperties {

    @Value("${fb.otm.photo.album.id:defaultFbAlbumId}")
    private String photoAlbumId;

    @Value("${fb.otm.page.id:defaultFbPageId}")
    private String otmPageId;

    @Value("${fb.otm.page.access.token:defaultFbPageAccessToken}")
    private String pageAccessToken;

    @Value("${fb.otm.user.access.token:defaultFbUserAccessToken}")
    private String userAccessToken;

    @Value("${fb.otm.app.secret}")
    private String appSecret;

    @Value("${fb.otm.verify.token}")
    private String verifyToken;
}
