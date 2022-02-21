package pl.adamsiedlecki.otm.external.services.open.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.otm.config.OtherApiProperties;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.current.OpenWeatherCurrentPojo;
import pl.adamsiedlecki.otm.external.services.open.weather.pojo.open.weather.two.days.ahead.OpenWeatherTwoDaysAheadPojo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenWeatherFetcher {

    private static final int READ_TIMEOUT = 1000;
    private final OtherApiProperties otherApiProperties;

    public Optional<OpenWeatherCurrentPojo> getCurrent(final String latitude, final String longitude) {
        String fullAddress = "https://api.openweathermap.org/data/2.5/weather"
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + otherApiProperties.getOpenWeatherApiKey()
                + "&units=metric"
                + "&lang=pl";
        Optional<String> content = getResponse(fullAddress);

        if (content.isPresent()) {
            ObjectMapper om = new ObjectMapper();
            try {
                return Optional.of(om.readValue(content.get(), OpenWeatherCurrentPojo.class));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }

        return Optional.empty();
    }

    public Optional<OpenWeatherTwoDaysAheadPojo> getTwoDaysAhead(final String latitude, final String longitude) {
        String fullAddress = "https://api.openweathermap.org/data/2.5/onecall"
                + "?lat=" + latitude
                + "&lon=" + longitude
                + "&appid=" + otherApiProperties.getOpenWeatherApiKey()
                + "&units=metric"
                + "&lang=pl"
                + "&exclude=current,minutely,daily,alerts";
        Optional<String> content = getResponse(fullAddress);

        if (content.isPresent()) {
            ObjectMapper om = new ObjectMapper();
            try {
                return Optional.of(om.readValue(content.get(), OpenWeatherTwoDaysAheadPojo.class));
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }

        return Optional.empty();
    }

    private Optional<String> getResponse(final String fullAddress) {
        try {
            URL url = new URL(fullAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(READ_TIMEOUT);
            int status = con.getResponseCode();
            log.info("Open weather request status: {}", status);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
            con.disconnect();
            return Optional.of(stringBuilder.toString());

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return Optional.empty();
    }
}
