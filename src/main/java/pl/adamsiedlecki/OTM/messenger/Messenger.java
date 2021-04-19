package pl.adamsiedlecki.OTM.messenger;

import java.io.File;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Messenger {

    private String body = "\"messaging_type\": \"UPDATE\",\n" +
            "\"recipient\":{\n" +
            "  \"id\":\"<PSID>\"\n" +
            "},\n" +
            "\"message\":{\n" +
            "  \"text\":\"Raport z ostatniej nocy\",\n" +
            "\"attachment\":{\n" +
            "      \"type\":\"image\", \n" +
            "      \"payload\":{\n" +

            "        \"is_reusable\":true\n" +
            "      }\n" +
            "    }" +
            "}" +
            "filedata=@<FILE_PATH>;type=<FILE_TYPE>"; //filedata=@/tmp/shirt.png;type=image/png

    public void sendOverNightChartToUser(File f, String recipient, String fbPageAccessToken, String fbApiAddress) {
        body = body.replaceAll("<PSID>", recipient);
        body = body.replaceAll("<FILE_PATH>", f.getAbsolutePath());
        body = body.replaceAll("<FILE_TYPE>", "image/jpg");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(fbApiAddress + fbPageAccessToken))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .proxy(ProxySelector.getDefault())
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            response.previousResponse().ifPresent(System.out::println);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
