package pl.adamsiedlecki.otm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class OtmMailProperties {

    @Value("${otm.email.username}")
    private String emailUsername;

    @Value("${otm.email.password}")
    private String emailPassword;

    @Value("${otm.email.host}")
    private String emailHost;

    @Value("${otm.email.port}")
    private String emailPort;
}
