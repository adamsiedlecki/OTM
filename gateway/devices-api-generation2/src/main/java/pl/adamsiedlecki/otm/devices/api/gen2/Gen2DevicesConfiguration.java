package pl.adamsiedlecki.otm.devices.api.gen2;

import __project.generatedClassesPackage_.devices.gen2.client.api.Gen2DevicesApi;
import __project.generatedClassesPackage_.devices.gen2.client.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class Gen2DevicesConfiguration {

    private final int DEFAULT_TIMEOUT = 10000;
    private final Gen2DevicesApi gen2DevicesApi;
    @Value("gen2.api.address:localhost:9090")
    private String gen2ApiAddress;

    @Bean
    @Primary
    public Gen2DevicesApi gen2DevicesApi() {
        gen2DevicesApi.setApiClient(getApiClient());
        return gen2DevicesApi;
    }

    private ApiClient getApiClient() {
        return new ApiClient()
                .setBasePath(gen2ApiAddress)
                .setConnectTimeout(DEFAULT_TIMEOUT)
                .setReadTimeout(DEFAULT_TIMEOUT);
    }
}
