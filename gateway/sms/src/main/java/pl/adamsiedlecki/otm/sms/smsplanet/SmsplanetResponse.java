package pl.adamsiedlecki.otm.sms.smsplanet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
// source https://github.com/smsplanetpl/smsplanet-java-lib
public class SmsplanetResponse {
    private static final Gson gson = new Gson();

    private final String body;

    public boolean succeeded;
    public HashMap<String, Object> message;

    public SmsplanetResponse(String body) {
        this.body = body;
        try {
            double d = Double.parseDouble(body);
            message = new HashMap<>();
            message.put("number", d);
        } catch (NumberFormatException nfe) {
            // Do nothing.
        }
        try {
            Type mapType = new TypeToken<HashMap<String, Object>>() {}.getType();
            message = gson.fromJson(body, mapType);
        } catch (com.google.gson.JsonSyntaxException ex) {
            // Do nothing.
        }
        succeeded = message != null;
    }

    public boolean result() {
        return message.containsKey("result");
    }

    public String getMessageId() {
        return succeeded ? (String) message.getOrDefault("messageId", "0") : null;
    }

    public String getMessage() {
        return succeeded ? (String) message.getOrDefault("message", null) : null;
    }

    public int balance() {
        return succeeded ? ((Double) message.getOrDefault("balance", (double) -1)).intValue() : -1;
    }

    public int count() {
        return succeeded ? ((Double) message.getOrDefault("number", (double) -1)).intValue() : -1;
    }

    public String shortUrl() {
        return succeeded ? (String) message.getOrDefault("shortUrl", null) : null;
    }

    public String[] senderFields() {
        if  (succeeded) {
            String fields = (String) message.getOrDefault("senderFields",null);
            return fields != null ? fields.split(",") : null;
        }
        return null;
    }

    public int getErrorCode() {
        if (!succeeded)
            return -1;
        if (message.containsKey("errorMsg"))
            return ((Double) message.getOrDefault("errorCode", (double) 199)).intValue();
        return 0;
    }

    public String getErrorMessage() {
        return succeeded ? (String) message.getOrDefault("errorMsg",null) : null;
    }

    @Override
    public String toString() {
        return body;
    }
}