package pl.adamsiedlecki.OTM.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "fb")
@PropertySource("classpath:application.properties")
public class FacebookApiProperties {

    @Value("${otm.photo.album.id:defaultFbAlbumId}")
    private String photoAlbumId;

    @Value("${otm.page.id:defaultFbPageId}")
    private String otmPageId;

    @Value("${otm.page.access.token:defaultFbPageAccessToken}")
    private String pageAccessToken;

    @Value("${otm.user.access.token:defaultFbUserAccessToken}")
    private String userAccessToken;
}
