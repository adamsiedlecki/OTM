package pl.adamsiedlecki.OTM.externalServices.openWeather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.externalServices.openWeather.pojo.openWeatherCurrent.OpenWeatherCurrentPojo;
import pl.adamsiedlecki.OTM.externalServices.openWeather.pojo.openWeatherTwoDaysAhead.OpenWeatherTwoDaysAheadPojo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Component
public class OpenWeatherFetcher {

    private final Logger log = LoggerFactory.getLogger(OpenWeatherFetcher.class);
    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    public Optional<OpenWeatherCurrentPojo> getCurrent(String latitude, String longitude) {
        String fullAddress = "https://api.openweathermap.org/data/2.5/weather" +
                "?lat=" + latitude +
                "&lon=" + longitude +
                "&appid=" + openWeatherApiKey +
                "&units=metric" +
                "&lang=pl";
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

    public Optional<OpenWeatherTwoDaysAheadPojo> getTwoDaysAhead(String latitude, String longitude) {
        String fullAddress = "https://api.openweathermap.org/data/2.5/onecall" +
                "?lat=" + latitude +
                "&lon=" + longitude +
                "&appid=" + openWeatherApiKey +
                "&units=metric" +
                "&lang=pl" +
                "&exclude=current,minutely,daily,alerts";
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

    private Optional<String> getResponse(String fullAddress) {
        try {
            URL url = new URL(fullAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(1000);
            int status = con.getResponseCode();
            log.info("Open weather request status: " + status);
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
