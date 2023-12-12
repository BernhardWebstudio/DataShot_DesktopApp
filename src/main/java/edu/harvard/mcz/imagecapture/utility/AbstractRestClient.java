package edu.harvard.mcz.imagecapture.utility;

import com.github.mizosoft.methanol.Methanol;
import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRestClient {
    private static final Logger log =
            LoggerFactory.getLogger(AbstractRestClient.class);
    protected final Methanol httpClient;

    public AbstractRestClient() {
        httpClient = Methanol.newBuilder().userAgent("DataShot " + ImageCaptureApp.getAppVersion()).build();
    }

    private static String buildFormDataFromMap(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return builder.toString();
    }

    protected String getRequest(String url) throws IOException {
        return getRequest(url, null);
    }

    /**
     * Run a "GET" type request against a URL
     *
     * @param url     the URL to query
     * @param headers any headers to add to the request
     * @return the body's string
     */
    protected String getRequest(String url, Map<String, String> headers) throws IOException {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept-Charset", "UTF-8");
        // OpenStreetMap requires a personal User Agent:
        // https://operations.osmfoundation.org/policies/nominatim/
        con.setRequestProperty(
                "User-Agent",
                "Datashot/Imagecapture App " + ImageCaptureApp.getAppVersion() +
                        ", Education, https://github.com/BernhardWebstudio/DataShot_DesktopApp/");
        con.setRequestProperty("Accept-Language", "en, de-CH;q=0.8, de;q=0.7");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (con.getResponseCode() != 200) {
            log.error("Error on request. Response code: " + con.getResponseCode() +
                    ", message: " + con.getResponseMessage() +
                    ", response: " + response);
            return null;
        }

        con.disconnect();

        log.debug("Response of put request: " + response);

        return response.toString();
    }

    /**
     * Run a "POST" type request against a URL
     *
     * @param url  the URL to post to
     * @param data the data to post
     * @return the body string
     * @throws IOException          from HTTPclient
     * @throws InterruptedException from HTTPclient
     */
    protected String postRequest(String url, String data) throws IOException, InterruptedException {
        return postRequest(url, data, null);
    }

    /**
     * Run a "POST" type request against a URL
     *
     * @param url     the URL to post to
     * @param data    the data to post
     * @param headers additional headers to send
     * @return the response body string
     * @throws IOException          from HTTPclient
     * @throws InterruptedException from HTTPclient
     */
    protected String postRequest(String url, String data, Map<String, String> headers) throws IOException, InterruptedException {
        log.debug("Posting request to " + url + " with data " + data);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(url))
                .setHeader("User-Agent", "DataShot " + ImageCaptureApp.getAppVersion()) // add request header
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofMinutes(3));

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.setHeader(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // System.out.println(response.statusCode());
        log.debug("Response of post request: " + response.body());

        return response.body();
    }

    /**
     * @param formFields the fields to be sent as such
     * @see #postRequest(String, String, Map)
     */
    protected String postRequest(String url, Map<String, String> formFields, Map<String, String> headers) throws IOException, InterruptedException {
        return postRequest(url, buildFormDataFromMap(formFields), headers);
    }

    /**
     * @param formFields the fields to be sent as such
     * @see #postRequest(String, String, Map)
     */
    protected String postRequest(String url, Map<String, String> formFields) throws IOException, InterruptedException {
        return postRequest(url, formFields, null);
    }

    /**
     * @param data the JSON to be posted as such
     * @see #postRequest(String, String, Map)
     */
    protected JSONObject postRequest(String url, JSONObject data) throws IOException, InterruptedException {
        return new JSONObject(postRequest(url, data.toString(), new HashMap<>() {{
            put("Content-Type", "application/json");
        }}));
    }

    /**
     * Run a "PUT" type request against a URL
     *
     * @param url     the URL to post to
     * @param data    the data to post
     * @param headers additional headers to send
     * @return the response body
     * @throws IOException          from HttpClient
     * @throws InterruptedException from HttpClient
     */
    protected String putRequest(String url, String data, Map<String, String> headers) throws IOException, InterruptedException {
        log.debug("Putting request to " + url + " with data " + data);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(url))
                .setHeader("User-Agent", "DataShot " + ImageCaptureApp.getAppVersion()) // add request header
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofMinutes(3));

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.setHeader(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest request = builder.build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        log.debug("Response of put request: " + response.body());
        // System.out.println(response.statusCode());
        return response.body();
    }

    protected Object getValueForKeyInJson(JSONObject source, String key) {
        if (key.contains(".")) {
            String[] split = key.split("\\.");
            try {
                // rebuild all except 0th element
                StringBuilder rest = new StringBuilder(split[1]);
                for (int i = 2; i < split.length; ++i) {
                    rest.append(".").append(split[i]);
                }
                // use first part as key for recursion
                return getValueForKeyInJson((JSONObject) source.get(split[0]),
                        rest.toString());
            } catch (Exception e) {
                log.error("key " + split[0] + " does not exist in JSONObject");
                log.error("Error", e);
            }
        } else {
            try {
                return source.get(key);
            } catch (Exception e) {
                log.error("key " + key + " does not exist in JSONObject");
                log.error("Error", e);
            }
        }
        return null;
    }


    /**
     * Fetch a values, specified with key, from OpenStreetMap API
     *
     * @param url  the endpoint
     * @param keys the JSONArray keys
     * @return a map of the key to its value
     */
    protected Map<String, Object> fetchValues(String url,
                                              Collection<String> keys) {
        Map<String, Object> res = new HashMap<>();
        StringBuilder query;
        String queryResult = null;

        query = new StringBuilder();

        query.append(url);

        log.debug("Query:" + query);

        try {
            queryResult = this.getRequest(query.toString());
        } catch (Exception e) {
            log.error("Error when trying to get data with the following query " +
                    query);
            log.error("Error", e);
        }

        if (queryResult == null) {
            return null;
        }

        JSONObject obj = null;
        try {
            obj = new JSONObject(queryResult);
        } catch (JSONException e) {
            log.error("Error", e);
        }

        log.debug("Fetched obj=" + obj);

        if (obj != null) {
            if (obj.length() > 0) {
                // just take the first search result ^.^
                JSONObject finalObj = obj;
                keys.forEach((String key) -> res.put(key, this.getValueForKeyInJson(finalObj, key)));
            }
        }

        return res;
    }
}
