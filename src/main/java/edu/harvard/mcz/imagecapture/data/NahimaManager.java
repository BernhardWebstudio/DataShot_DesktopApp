package edu.harvard.mcz.imagecapture.data;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.utility.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.*;

public class NahimaManager extends AbstractRestClient {
    public static final JSONObject defaultPool = new JSONObject("{ \"pool\": { \"_id\": 7 } }");
    private static final Logger log = LoggerFactory.getLogger(NahimaManager.class);
    private final String url;
    private final String username;
    private final String password;
    private String token;
    private final Map<String, Map<String, JSONObject>> resolveCache = new HashMap<>();

    public NahimaManager(String url, String username, String password) throws IOException, InterruptedException, RuntimeException {
        // normalize URL
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        if (!url.endsWith("api/v1/")) {
            url = url + "api/v1/";
        }
        // store properties
        this.url = url;
        this.username = username;
        this.password = password;

        this.startSessionAndRetrieveToken();
        this.login();
    }

    /**
     * Start a Nahima session
     *
     */
    protected void startSessionAndRetrieveToken() throws RuntimeException {
        String queryUrl = this.url + "session";
        Collection<String> params = new HashSet<>();
        params.add("token");
        this.token = (String) this.fetchValues(queryUrl, params).get("token");

        if (null == this.token) {
            throw new RuntimeException("Could not get session token from nahima.");
        }
    }

    /**
     * Do login to Nahima
     *
     */
    protected void login() throws IOException, InterruptedException, RuntimeException {
        String queryUrl = this.url + "session/authenticate?method=easydb&token=" + this.token;
        HashMap<String, String> params = new HashMap<>();
        params.put("password", this.password);
        params.put("login", this.username);

        // check status
        String response = this.postRequest(queryUrl, params);
        JSONObject responseObject = new JSONObject(response);
        if (responseObject.has("code")) {
            if (((String) responseObject.get("code")).startsWith("error")) {
                throw new RuntimeException("Failed to log in. Error code: " + responseObject.get("code"));
            }
        }
    }

    /**
     * This function loops the images in a Specimen and uploads them to Nahima
     *
     * @param specimen the specimen whose images to upload
     * @return the created mediassets
     */
    public Object[] uploadImagesForSpecimen(Specimen specimen) throws IOException, InterruptedException, RuntimeException {
        // docs:
        // https://docs.easydb.de/en/technical/api/eas/
        // https://docs.easydb.de/en/sysadmin/eas/api/put/
        String baseQueryUrl = this.url + "eas/put?token=" + this.token;
        ArrayList<Object> results = new ArrayList<>();

        for (ICImage image : specimen.getICImages()) {
            String queryUrl = baseQueryUrl + "&original_filename=" + image.getFilename() + "&instance=image";
            log.debug("Running image upload to URL " + queryUrl);

            String imagePath = ImageCaptureProperties.assemblePathWithBase(image.getPath(), image.getFilename());

            MultipartBodyPublisher multipartBody = MultipartBodyPublisher.newBuilder().filePart("image", Path.of(imagePath), MediaType.IMAGE_ANY).build();
            MutableRequest request = MutableRequest.POST(queryUrl, multipartBody).header("Content-Disposition", "attachment; filename=\"" + image.getFilename() + "\"");
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.debug("Response from uploading image: " + response.body());

            // check for errors
            if (!response.body().startsWith("[")) {
                JSONObject responseObject = new JSONObject(response.body());
                if (responseObject.has("code") && responseObject.getString("code").startsWith("error")) {
                    throw new RuntimeException("Failed to upload image: Error code: " + responseObject.get("code"));
                }
            }

            results.add((new JSONArray(response.body())).get(0));
        }

        return results.toArray();
    }

    /**
     * Run the query to create an object in Nahima
     *
     * @param object the object to create
     * @param pool the pool to create the object in
     * @return the created object as it is in Nahima
     */
    public JSONObject createObjectInNahima(JSONObject object, String pool) throws IOException, InterruptedException {
        // docs:
        // https://docs.easydb.de/en/technical/api/db/
        String queryURL = this.url + "db/" + pool + "?token=" + token;
        // EasyDB seems to have mixed up PUT & POST, but well, we don't care
        String returnValue = this.putRequest(queryURL, (new JSONArray()).put(object).toString(), new HashMap<>() {{
            put("Content-Type", "application/json");
        }});

        // check for errors
        if (!returnValue.startsWith("[")) {
            JSONObject responseObject = new JSONObject(returnValue);
            if (responseObject.has("code") && responseObject.getString("code").startsWith("error")) {
                throw new RuntimeException("Failed to upload image: Error code: " + responseObject.get("code"));
            }
        }

        return (JSONObject) (new JSONArray(returnValue)).get(0);
    }

    /**
     * Search for a string in a Nahima objecttype
     *
     * @param searchString the string to search for. Will be split by " ".
     * @param objectType   the objecttype we search for
     * @param searchMode   e.g. "fulltext", "token", ...
     * @param ignoreCache  cache speeds things up, but is annoying when we just created a new object
     * @return Nahima's response
     */
    public JSONObject searchForString(String searchString, String objectType, String searchMode, boolean ignoreCache) throws IOException, InterruptedException {
        // check cache for results
        Map<String, JSONObject> typeCache = resolveCache.get(objectType);
        if (typeCache != null) {
            JSONObject cacheLoaded = typeCache.get(searchString);
            if (cacheLoaded != null && !ignoreCache) {
                return cacheLoaded;
            }
        }

        // load fresh
        String queryURL = this.url + "search" + "?token=" + token;
        JSONArray search = new JSONArray();

        String[] splitName = searchString.split(" ");
        for (String name : splitName) {
            JSONObject namePartSearch = new JSONObject();
            namePartSearch.put("type", "match");
            namePartSearch.put("bool", "must");
            namePartSearch.put("mode", searchMode);
            namePartSearch.put("string", name);
            search.put(namePartSearch);
        }

        JSONObject superSearch = new JSONObject();
        superSearch.put("search", search);
        superSearch.put("type", "complex");
        JSONArray superSuperSearch = new JSONArray();
        superSuperSearch.put(superSearch);
        JSONObject query = new JSONObject();
        query.put("type", "object");
        query.put("objecttypes", new JSONArray(new String[]{objectType}));
        query.put("search", superSuperSearch);

        // store result in cache
        JSONObject result = this.postRequest(queryURL, query);
        if (typeCache == null) {
            typeCache = new HashMap<>();
            resolveCache.put(objectType, typeCache);
        }
        typeCache.put(searchString, result);

        return result;
    }

    public JSONObject searchForString(String searchString, String objectType) throws IOException, InterruptedException {
        return searchForString(searchString, objectType, false);
    }

    public JSONObject searchForString(String searchString, String objectType, String searchMode) throws IOException, InterruptedException {
        return searchForString(searchString, objectType, searchMode, false);
    }

    public JSONObject searchForString(String searchString, String objectType, boolean ignoreCache) throws IOException, InterruptedException {
        return searchForString(searchString, objectType, "fulltext", ignoreCache);
    }

    /**
     * Search in Nahima for one string, get result only if just one result though
     *
     * @param search     the string to search for
     * @param objectType the object type that is searched
     * @return the object if there is only one, null if not
     */
    public JSONObject resolveStringSearchToOne(String search, String objectType) throws IOException, InterruptedException {
        if (search == null || objectType == null) {
            log.warn("Cannot search as search " + search + " or objectType " + objectType + " is null");
            return null;
        }

        JSONObject results = this.searchForString(search, objectType);

        if (results.has("code") && results.getString("code").startsWith("error")) {
            throw new RuntimeException("Failed to resolve search. Error code: " + results.getString("code"));
        }

        JSONArray foundObjects = (JSONArray) results.get("objects");
        if (foundObjects.length() == 1) {
            return (JSONObject) foundObjects.get(0);
        } else {
            // TODO: create / select correct
            log.info("Got != 1 " + objectType + " status. {}", results);
            return null;
        }
    }

    /**
     * Find a person in Nahima
     *
     * @param personName the name to search for
     */
    public JSONObject resolvePerson(String personName) throws IOException, InterruptedException {
        JSONObject results = this.resolveStringSearchToOne(personName, "person");
        // TODO: check compliance, does it really match well? We use "must", so let's hope so, but the resolution could still go wrong.
        // TODO: create / select correct
        if (results == null) {
            // TODO: create
        }
        return results;
    }

    /**
     * Find a location in Nahima
     *
     * @param location the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveLocation(String location) throws IOException, InterruptedException {
        JSONObject results = this.resolveStringSearchToOne(location, "gazetteer");
        // TODO: create / select correct
        if (results == null) {
            // TODO: create
        }
        return results;
    }

    /**
     * Find a typusstatus in Nahima
     *
     * @param status the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveTypeStatus(String status) throws IOException, InterruptedException {
        JSONObject results = this.resolveStringSearchToOne(status, "typusstatus");
        // TODO: create / select correct
        if (results == null) {
            // TODO: create
        }
        return results;
    }

    /**
     * Generic function to resolve unit for a certain field
     *
     * @param unit        the unit to find in Nahima
     * @param unitSubject the unit type
     */
    public JSONObject resolveUnitFor(String unit, String unitSubject) throws IOException, InterruptedException {
        JSONObject results = this.resolveStringSearchToOne(unit, unitSubject);
        // TODO: create / select correct
        if (results == null) {
            // TODO: create
        }
        return results;
    }

    /**
     * Find a unit in Nahima
     *
     * @param unit the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveUnitForHeight(String unit) throws IOException, InterruptedException {
        return resolveUnitFor(unit, "einheitenfuerhoehe");
    }

    /**
     * Find a unit in Nahima
     *
     * @param unit the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveUnitForErrorRadius(String unit) throws IOException, InterruptedException {
        return resolveUnitFor(unit, "einheitenfuerdenfehlerradius");
    }

    /**
     * Find the location "Datum" in Nahima
     *
     * @param format the datum to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveDatumFormat(String format) throws IOException, InterruptedException {
        JSONObject results = this.searchForString(format, "datumsformate", "token");

        JSONArray foundUnits = (JSONArray) results.get("objects");
        if (foundUnits.length() == 1) {
            return (JSONObject) foundUnits.get(0);
        } else {
            // TODO: create / select correct
            log.info("Got != 1 datum format. {}", results);
            return null;
        }
    }

    /**
     * Find the collection method in Nahima
     *
     * @param method the collection method to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveCollectionMethod(String method) throws IOException, InterruptedException {
        JSONObject results = this.resolveStringSearchToOne(method, "sammlungsmethoden");
        // TODO: create / select correct
        if (results == null) {
            // TODO: create
        }
        return results;
    }

    /**
     * This function translates a full-hydrated EasyDB object into an object that can be used to reference the
     * fully-hydrated object when creating another entity
     *
     * @param associate the object from easyDB to reduce
     * @return the reduced associate, ideal to reference on creation
     */
    public JSONObject reduceAssociateForAssociation(JSONObject associate) {
        if (associate == null) {
            return null;
        }
        JSONObject reduced = new JSONObject();
        String[] namesToKeep = {"_objecttype", "_mask", "_global_object_id"};
        for (String name : namesToKeep) {
            try {
                if (associate.has(name)) {
                    reduced.put(name, associate.get(name));
                }
            } catch (JSONException e) {
                // hmmm
                log.error("Error trying to reduce", e);
            }
        }
        JSONObject child = new JSONObject();

        child.put("_id", associate.has("_id") ? associate.get("_id") : ((JSONObject) associate.get((String) associate.get("_objecttype"))).get("_id"));
        reduced.put((String) associate.get("_objecttype"), child);
        return reduced;
    }
}
