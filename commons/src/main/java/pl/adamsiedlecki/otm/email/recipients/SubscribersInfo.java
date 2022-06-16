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
@ConfigurationProperties(prefix = "subscribers")
public class SubscribersInfo {

    private List<People> people = new ArrayList<>();

    public List<People> getPeople() {
        return List.copyOf(people);
    }

    public void setPeople(List<People> devices) {
        this.people = devices;
    }

    public Optional<People> getByLocationPlaceId(int id) {
        return people.stream().filter(recipient -> recipient.getLocationPlaceId() == id).findFirst();
    }

}
