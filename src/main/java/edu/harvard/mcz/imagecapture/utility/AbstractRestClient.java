package edu.harvard.mcz.imagecapture.utility;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRestClient {
    private static final Logger log =
            LoggerFactory.getLogger(AbstractRestClient.class);

    public AbstractRestClient() {
    }

    protected String getRequest(String url) throws Exception {

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
        return response.toString();
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
        StringBuffer query;
        String queryResult = null;

        query = new StringBuffer();

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

        log.debug("obj=" + obj);

        if (obj != null) {
            if (obj.length() > 0) {
                // just take the first search result ^.^
                JSONObject finalObj = obj;
                keys.forEach((String key) -> {
                    res.put(key, this.getValueForKeyInJson(finalObj, key));
                });
            }
        }

        return res;
    }
}
