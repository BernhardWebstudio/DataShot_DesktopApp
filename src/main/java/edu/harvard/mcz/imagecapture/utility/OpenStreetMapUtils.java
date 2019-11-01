package edu.harvard.mcz.imagecapture.utility;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class OpenStreetMapUtils {

    private static final Log log = LogFactory.getLog(OpenStreetMapUtils.class);

    private static OpenStreetMapUtils instance = null;

    public OpenStreetMapUtils() {
    }

    public static OpenStreetMapUtils getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtils();
        }
        return instance;
    }

    private String getRequest(String url) throws Exception {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        // OpenStreetMap requires a personal User Agent: https://operations.osmfoundation.org/policies/nominatim/
        con.setRequestProperty("User-Agent", "Datashot/Imagecapture App " + ImageCaptureApp.getAppVersion() + ", Education, https://github.com/BernhardWebstudio/DataShot_DesktopApp/");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        if (con.getResponseCode() != 200) {
            log.error("Error on request. Response code: " + con.getResponseCode()
                    + ", message: " + con.getResponseMessage()
                    + ", response: " + response);
            return null;
        }

        con.disconnect();
        return response.toString();
    }

    /**
     * Shortcut/utility method to get the coordinates of an address
     *
     * @param address
     * @return
     */
    public Map<String, Double> getCoordinates(String address) {
        Map<String, Object> values = this.searchValues(address, new ArrayList<String>(Arrays.asList("lon", "lat")));
        Map<String, Double> res = new HashMap<>();

        values.forEach((key, value) -> {
            res.put(key, Double.parseDouble((String) value));
        });

        return res;
    }

    /**
     * Find an adress by a search term
     *
     * @param search the search terms, separated by space
     * @param keys
     * @return
     */
    public Map<String, Object> searchValues(String search, Collection<String> keys) {
        Map<String, Object> res = new HashMap<String, Object>();
        StringBuffer query;
        String[] split = search.split(" ");
        String url = "http://nominatim.openstreetmap.org/search?q=";

        if (split.length == 0) {
            return null;
        }

        for (int i = 0; i < split.length; i++) {
            url += split[i];
            if (i < (split.length - 1)) {
                url += "+";
            }
        }
        url += "&format=json&addressdetails=1&limit=1";
        return this.fetchValues(url, keys);
    }

    /**
     * Find an address by its coordinates
     *
     * @param lat the latitude of the address
     * @param lon the longitude of the address
     * @param keys
     * @return
     */
    public Map<String, Object> reverseSearchValues(BigDecimal lat, BigDecimal lon, Collection<String> keys) {
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + lat + "&lon=" + lon + "&format=json&addressdetails=1";
        return this.fetchValues(url, keys);
    }

    /**
     * Fetch a values, specified with key, from OpenStreetMap API
     * @param url the endpoint
     * @param keys the JSONArray keys
     * @return a map of the key to its value
     */
    protected Map<String, Object> fetchValues(String url, Collection<String> keys) {
        Map<String, Object> res = new HashMap<>();
        StringBuffer query;
        String queryResult = null;

        query = new StringBuffer();

        query.append(url);

        log.debug("Query:" + query);

        try {
            queryResult = getRequest(query.toString());
        } catch (Exception e) {
            log.error("Error when trying to get data with the following query " + query);
            log.error(e);
        }

        if (queryResult == null) {
            return null;
        }

        JSONObject obj = null;
        try {
            obj = new JSONObject(queryResult); }
        catch (JSONException e) {
            log.error(e);
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

    private Object getValueForKeyInJson(JSONObject source, String key) {
        if (key.contains(".")) {
            String[] split = key.split("\\.");
            try {
                // rebuild all except 0th element
                StringBuilder rest = new StringBuilder(split[1]);
                for (int i = 2; i < split.length; ++i) {
                    rest.append(".").append(split[i]);
                }
                // use first part as key for recursion
                return getValueForKeyInJson((JSONObject) source.get(split[0]), rest.toString());
            } catch (Exception e) {
                log.error("key " + split[0] + " does not exist in JSONObject");
                log.error(e);
            }
        } else {
            try {
                return source.get(key);
            } catch (Exception e) {
                log.error("key " + key + " does not exist in JSONObject");
                log.error(e);
            }
        }
        return null;
    }
}