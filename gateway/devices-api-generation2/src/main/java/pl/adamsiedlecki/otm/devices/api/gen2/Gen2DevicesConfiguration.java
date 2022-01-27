package pl.adamsiedlecki.otm.devices.api.gen2;

import __project.generatedClassesPackage_.devices.gen2.client.api.Gen2DevicesApi;
import __project.generatedClassesPackage_.devices.gen2.client.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;

@Configuration
@RequiredArgsConstructor
public class Gen2DevicesConfiguration {

    public final int DEFAULT_TIMEOUT = 10000;
    public final int DEFAULT_TF_FLAG_TIMEOUT = 10000;
    private final OtmConfigProperties otmConfigProperties;

    @Bean
    @Primary
    public Gen2DevicesApi gen2DevicesApi() {
        return new Gen2DevicesApi(getApiClient());
    }

    private ApiClient getApiClient() {
        return new ApiClient()
                .setBasePath(otmConfigProperties.getGen2ApiAddress())
                .setConnectTimeout(DEFAULT_TIMEOUT)
                .setReadTimeout(DEFAULT_TIMEOUT);
    }
}
