package pl.adamsiedlecki.otm.odg;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.adamsiedlecki.odg.api.OdgApi;
import pl.adamsiedlecki.odg.invoker.ApiClient;
import pl.adamsiedlecki.otm.config.OtmConfigProperties;

@Service
public class OdgApiClient extends OdgApi {

    public OdgApiClient(OtmConfigProperties otmConfigProperties, RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(otmConfigProperties.getOdgBasePath());
        setApiClient(apiClient);
    }
}
