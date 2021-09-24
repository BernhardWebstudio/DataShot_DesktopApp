package edu.harvard.mcz.imagecapture.data;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.utility.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.*;

public class NahimaManager extends AbstractRestClient {
    private static final Logger log = LoggerFactory.getLogger(NahimaManager.class);

    private final String url;
    private final String username;
    private final String password;
    private String token;
    private Map<String, Map<String, JSONObject>> resolveCache = new HashMap<String, Map<String, JSONObject>>();

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

    protected void startSessionAndRetrieveToken() throws RuntimeException {
        String queryUrl = this.url + "session";
        Collection<String> params = new HashSet<String>();
        params.add("token");
        this.token = (String) this.fetchValues(queryUrl, params).get("token");

        if (null == this.token) {
            throw new RuntimeException("Could not get session token from nahima.");
        }
    }

    protected void login() throws IOException, InterruptedException {
        String queryUrl = this.url + "session/authenticate?token=" + this.token;
        HashMap<String, String> params = new HashMap<>();
        params.put("username", this.username);
        params.put("password", this.password);

        this.postRequest(queryUrl, params);
    }

    public JSONObject[] uploadImagesForSpecimen(Specimen specimen) throws IOException, InterruptedException {
        // docs:
        // https://docs.easydb.de/en/technical/api/eas/
        // https://docs.easydb.de/en/sysadmin/eas/api/put/
        String baseQueryUrl = this.url + "eas/put?token=" + this.token;
        ArrayList results = new ArrayList<>();

        for (ICImage image : specimen.getICImages()) {
            String queryUrl = baseQueryUrl + "&original_filename=" + image.getFilename() + "&instance=image";

            HttpRequest.Builder builder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofFile(Path.of(ImageCaptureProperties.assemblePathWithBase(image.getPath(), image.getFilename())))).uri(URI.create(url))
                    .setHeader("User-Agent", "DataShot " + ImageCaptureApp.getAppVersion()) // add request header
                    .setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpRequest request = builder.build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            results.add((new JSONArray(response.body())).get(0));
        }

        return (JSONObject[]) results.toArray();
    }

    public JSONObject createObjectInNahima(JSONObject object, String pool) throws IOException, InterruptedException {
        // docs:
        // https://docs.easydb.de/en/technical/api/db/
        String queryURL = this.url + "db/" + pool + "?token=" + token;
        // EasyDB seems to have mixed up PUT & POST, but well, we don't care
        return new JSONObject(this.putRequest(queryURL, (new JSONArray()).put(object).toString(), null));
    }

    public JSONObject searchForString(String searchString, String objectType) throws IOException, InterruptedException {
        return searchForString(searchString, objectType, false);
    }

    /**
     * Search for a string in a Nahima objecttype
     *
     * @param searchString the string to search for. Will be split by " ".
     * @param objectType the objecttype we search for
     * @param ignoreCache cache speeds things up, but is annoying when we just created a new object
     * @return Nahima's response
     * @throws IOException
     * @throws InterruptedException
     */
    public JSONObject searchForString(String searchString, String objectType, boolean ignoreCache) throws IOException, InterruptedException {
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
            namePartSearch.put("mode", "fulltext");
            namePartSearch.put("string", name);
            search.put(namePartSearch);
        }

        JSONObject superSearch = new JSONObject();
        superSearch.put("search", search);
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

    /**
     * Find a person in DataShot
     *
     * @param personName the name to search for
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public JSONObject resolvePerson(String personName) throws IOException, InterruptedException {
        JSONObject results = this.searchForString(personName, "person");
        // TODO: check compliance, does it really match well? We use "must", so let's hope so, but the resolution could still go wrong.
        // also, multiple could be returned.
        // or, none, in which case we might need to create one
        JSONArray foundPersons = (JSONArray) results.get("objects");
        if (foundPersons.length() == 1) {
            return (JSONObject) foundPersons.get(0);
        } else {
            // TODO.
            log.info("Got != 1 person. {}", results);
            return null;
        }
    }

    /**
     * This function translates a full-hydrated EasyDB object into an object that can be used to reference the
     * fully-hydrated object when creating another entity
     *
     * @param associate the object from easyDB to reduce
     * @return
     */
    public JSONObject reduceAssociateForAssociation(JSONObject associate) {
        JSONObject reduced = new JSONObject();
        String[] namesToKeep = {"_objecttype", "_mask", "_global_object_id"};
        for (String name : namesToKeep) {
            reduced.put(name, associate.get(name));
        }
        JSONObject child = new JSONObject();
        child.put("_id", ((JSONObject) associate.get((String) associate.get("_objecttype"))).get("_id"));
        reduced.put((String) associate.get("_objecttype"), child);
        return reduced;
    }
}
