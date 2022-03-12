package pl.adamsiedlecki.otm.email.recipients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "email-subscribers")
public class SubscribersInfo {

    private List<Subscribers> subscribers = new ArrayList<>();

    public List<Subscribers> getSubscribers() {
        return List.copyOf(subscribers);
    }

    public void setSubscribers(List<Subscribers> devices) {
        this.subscribers = devices;
    }

    public Optional<Subscribers> getByLocationPlaceId(int id) {
        return subscribers.stream().filter(recipient -> recipient.getLocationPlaceId() == id).findFirst();
    }

}
