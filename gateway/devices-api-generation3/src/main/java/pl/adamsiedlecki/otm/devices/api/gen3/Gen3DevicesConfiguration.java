package pl.adamsiedlecki.otm.devices.api.gen3;

import __project.generatedClassesPackage_.devices.gen3.client.api.Gen3DevicesApi;
import __project.generatedClassesPackage_.devices.gen3.client.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;

@Configuration
@RequiredArgsConstructor
public class Gen3DevicesConfiguration {

    public static final int DEFAULT_TIMEOUT = 5000;
    private final OtmConfigProperties otmConfigProperties;

    @Bean
    @Primary
    public Gen3DevicesApi gen3DevicesApi() {
        return new Gen3DevicesApi(getApiClient());
    }

    private ApiClient getApiClient() {
        return new ApiClient()
                .setBasePath(otmConfigProperties.getGen3ApiAddress())
                .setConnectTimeout(DEFAULT_TIMEOUT)
                .setReadTimeout(DEFAULT_TIMEOUT);
    }
}
