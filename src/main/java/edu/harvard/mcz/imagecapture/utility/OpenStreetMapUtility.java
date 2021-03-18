package edu.harvard.mcz.imagecapture.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

// TODO: refactor into https://github.com/westnordost/osmapi-overpass
// and load ISO 3166-1 & -2 instead
public class OpenStreetMapUtility extends AbstractRestClient {

    private static final Logger log =
            LoggerFactory.getLogger(OpenStreetMapUtility.class);

    private static OpenStreetMapUtility instance = null;

    public OpenStreetMapUtility() {
        super();
    }

    public static OpenStreetMapUtility getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtility();
        }
        return instance;
    }

    /**
     * Shortcut/utility method to get the coordinates of an address
     *
     * @param address
     * @return
     */
    public Map<String, Double> getCoordinates(String address) {
        Map<String, Object> values = this.searchValues(
                address, new ArrayList<String>(Arrays.asList("lon", "lat")));
        Map<String, Double> res = new HashMap<>();

        values.forEach(
                (key, value) -> {
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
    public Map<String, Object> searchValues(String search,
                                            Collection<String> keys) {
        Map<String, Object> res = new HashMap<String, Object>();
        StringBuffer query;
        String[] split = search.split(" ");
        StringBuilder url =
                new StringBuilder("http://nominatim.openstreetmap.org/search?q=");

        if (split.length == 0) {
            return null;
        }

        for (int i = 0; i < split.length; i++) {
            url.append(split[i]);
            if (i < (split.length - 1)) {
                url.append("+");
            }
        }
        url.append("&format=json&addressdetails=1&limit=1");
        return super.fetchValues(url.toString(), keys);
    }

    /**
     * Find an address by its coordinates
     *
     * @param lat  the latitude of the address
     * @param lon  the longitude of the address
     * @param keys
     * @return
     */
    public Map<String, Object> reverseSearchValues(BigDecimal lat, BigDecimal lon,
                                                   Collection<String> keys) {
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + lat +
                "&lon=" + lon + "&format=json&addressdetails=1";
        return super.fetchValues(url, keys);
    }
}
