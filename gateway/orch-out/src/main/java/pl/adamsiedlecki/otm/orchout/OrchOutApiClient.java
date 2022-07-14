package pl.adamsiedlecki.otm.orchout;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.adamsiedlecki.orchout.api.OrchOutApi;
import pl.adamsiedlecki.orchout.invoker.ApiClient;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;

@Service
public class OrchOutApiClient extends OrchOutApi {

    public OrchOutApiClient(OtmConfigProperties otmConfigProperties, RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(otmConfigProperties.getOrchoutBasePath());
        setApiClient(apiClient);
    }
}
