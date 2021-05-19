package pl.adamsiedlecki.OTM.dataFetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.adamsiedlecki.OTM.exceptions.EspNoResponseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Component
class EspApiTool {

    private final Logger log = LoggerFactory.getLogger(EspApiTool.class);
    private long noResponseCounter = 0;

    public String getHtml(String apiAddress) throws EspNoResponseException {
        String content = null;
        int status = 0;
        try {
            URL url = new URL(apiAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(25000);
            status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
            con.disconnect();
            content = stringBuilder.toString();

            if (content.contains("no response")) {
                throw new EspNoResponseException();
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        log.info(status + "HTML content: " + content);
        return content;
    }

    String espNoResponseStrategy(String apiAddress) {
        String content = "";
        log.info("esp no response");
        sendRestartCommand(apiAddress);
        try {
            Thread.sleep(5500);
            content = getHtml(apiAddress);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (EspNoResponseException e) {
            if (noResponseCounter < 5) {
                return espNoResponseStrategy(apiAddress);
            } else {
                noResponseCounter = 0;
                return content;
            }
        }
        return content;
    }

    void sendRestartCommand(String apiAddress) {
        try {
            log.info("Sending restart command to ESP");
            URLConnection conn = new URL(apiAddress + "/restart").openConnection();
            conn.connect();

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

    }

}
