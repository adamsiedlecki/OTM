package pl.adamsiedlecki.otm.sms.smsplanet;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// source https://github.com/smsplanetpl/smsplanet-java-lib
public class SmsplanetRequest {
    private static final boolean LOCALHOST_TEST = false;
    private static final String BASE_URL = (LOCALHOST_TEST) ? "http://localhost:8080" : "https://api2.smsplanet.pl";
    private final static Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    final String path;
    final boolean get;
    final HashMap<String, String> parameters = new HashMap<>();

    public SmsplanetRequest(String path) {
        this.path = path;
        this.get = false;
    }

    public SmsplanetRequest(String path, boolean get) {
        this.path = path;
        this.get = get;
    }

    public URI getURI() {
        try {
            URIBuilder builder = new URIBuilder(BASE_URL);
            builder.setPath(LOCALHOST_TEST ? "/p/api2/" + path : path);
            if (this.get) {
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    builder.setParameter(entry.getKey(), entry.getValue());
            }
            return builder.build();
        } catch (URISyntaxException e) {
            // This should never happen, all urls are defined in this file.
            return null;
        }
    }

    private SmsplanetResponse executePost() throws IOException {
        HttpPost http = new HttpPost(getURI());

        List<NameValuePair> parametersAsList = new ArrayList<>();
        for (Map.Entry<String, String> entry : parameters.entrySet())
            parametersAsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        http.setEntity(new UrlEncodedFormEntity(parametersAsList, UTF_8_CHARSET));

        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(http)){
            return new SmsplanetResponse(EntityUtils.toString(response.getEntity()));
        }
    }

    private SmsplanetResponse executeGet() throws IOException {
        HttpGet http = new HttpGet(getURI());

        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(http)){
            return new SmsplanetResponse(EntityUtils.toString(response.getEntity()));
        }
    }

    public SmsplanetResponse execute() throws IOException {
        return (this.get) ? executeGet() : executePost();
    }

    public void setDate(String date) {
        parameters.put("date", date);
    }

    public void setName(String name) {
        parameters.put("name", name);
    }

    public void setParam(int index, String[] values) {
        String param = "param" + index;
        parameters.put(param, String.join("|", values));
    }

    public void addParam(int index, String value) {
        String param = "param" + index;
        if (parameters.containsKey(param))
            parameters.put(param, parameters.get(param) + "|" + value);
        else
            parameters.put(param, value);
    }

    public void setParam1(String[] values) {
        setParam(1, values);
    }

    public void addParam1(String value) {
        addParam(1, value);
    }

    public void setParam2(String[] values) {
        setParam(2, values);
    }

    public void addParam2(String value) {
        addParam(2, value);
    }

    public void setParam3(String[] values) {
        setParam(3, values);
    }

    public void addParam3(String value) {
        addParam(3, value);
    }

    public void setParam4(String[] values) {
        setParam(4, values);
    }

    public void addParam4(String value) {
        addParam(4, value);
    }

    public void setClearPolish(boolean clear_polish) {
        parameters.put("clear_polish", clear_polish ? "1" : "0");
    }

    public void setTest(boolean test) {
        parameters.put("test", test ? "1" : "0");
    }

    public void setCompanyID(String company_id) {
        parameters.put("company_id", company_id);
    }

    public void setAttachment(String attachment) {
        parameters.put("attachment", attachment);
    }

    public void setDetailed(boolean detailed) {
        parameters.put("detailed", detailed ? "1" : "0");
    }

    public void setResponseCSV(boolean csv) {
        parameters.put("responseType", csv ? "1" : "0");
    }

    @Override
    public String toString() {
        return getURI().toString();
    }

    // Define all API methods below.

    public static SmsplanetRequest sendSMS(String key, String password, String from, String msg, String[] to) {
        SmsplanetRequest request = new SmsplanetRequest("sms");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("from", from);
        request.parameters.put("msg", msg);
        request.parameters.put("to",  String.join(",", to));
        return request;
    }

    public static SmsplanetRequest sendMMS(String key, String password, String from, String subject, String msg, String[] to) {
        SmsplanetRequest request = new SmsplanetRequest("mms");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("from", from);
        request.parameters.put("subject", subject);
        request.parameters.put("msg", msg);
        request.parameters.put("to",  String.join(",", to));
        return request;
    }

    public static SmsplanetRequest cancelMessage(String key, String password, String messageId) {
        SmsplanetRequest request = new SmsplanetRequest("cancelMessage");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("messageId", messageId);
        return request;
    }

    public static SmsplanetRequest getBalance(String key, String password, String product) {
        SmsplanetRequest request = new SmsplanetRequest("getBalance");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("product", product);
        return request;
    }

    public static SmsplanetRequest getBalance(String key, String password) {
        return getBalance(key, password, "SMS");
    }

    public static SmsplanetRequest addSenderField(String key, String password, String senderField) {
        SmsplanetRequest request = new SmsplanetRequest(LOCALHOST_TEST ? "senderFields/add" : "addSenderField");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("senderField", senderField);
        return request;
    }

    public static SmsplanetRequest getSenderFields(String key, String password, String product) {
        SmsplanetRequest request = new SmsplanetRequest(LOCALHOST_TEST ? "senderFields" : "getSenderFields");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("product", product);
        return request;
    }

    public static SmsplanetRequest getSenderFields(String key, String password) {
        return getSenderFields(key, password, "SMS");
    }

    public static SmsplanetRequest generateReport(String key, String password, String from, String to) {
        SmsplanetRequest request = new SmsplanetRequest("generateReport");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("from", from);
        request.parameters.put("to", to);
        return request;
    }

    public static SmsplanetRequest getMessageInfo(String key, String password, String messageId) {
        SmsplanetRequest request = new SmsplanetRequest("getMessageInfo");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("messageId", messageId);
        return request;
    }

    public static SmsplanetRequest addBlacklist(String key, String password, String msisdn) {
        SmsplanetRequest request = new SmsplanetRequest("blacklist/add");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("msisdn", msisdn);
        return request;
    }

    public static SmsplanetRequest removeBlacklist(String key, String password, String msisdn) {
        SmsplanetRequest request = new SmsplanetRequest("blacklist/remove");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("msisdn", msisdn);
        return request;
    }

    public static SmsplanetRequest shortUrl(String key, String password, String longUrl) {
        SmsplanetRequest request = new SmsplanetRequest("short-url");
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("longUrl", longUrl);
        return request;
    }

    public static SmsplanetRequest partsCount(String key, String password, String content) {
        SmsplanetRequest request = new SmsplanetRequest("sms/parts-count", true);
        request.parameters.put("key", key);
        request.parameters.put("password", password);
        request.parameters.put("content", content);
        return request;
    }
}
