package edu.harvard.mcz.imagecapture.data;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import edu.harvard.mcz.imagecapture.ImageCaptureApp;
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
    private final Map<String, Map<String, JSONObject>> resolveCache = new HashMap<>();
    private String token;

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
     * @param object  the object to create
     * @param objType the pool to create the object in
     * @return the created object as it is in Nahima
     */
    public JSONObject createObjectInNahima(JSONObject object, String objType) throws IOException, InterruptedException {
        // docs:
        // https://docs.easydb.de/en/technical/api/db/
        String queryURL = this.url + "db/" + objType + "?token=" + token;
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

    protected JSONObject loadFromCache(String id, String objectType) {
        Map<String, JSONObject> typeCache = resolveCache.get(objectType);
        if (typeCache != null) {
            return typeCache.get(id);
        }
        return null;
    }

    protected void storeInCache(String id, String objectType, JSONObject result) {
        Map<String, JSONObject> typeCache = resolveCache.computeIfAbsent(objectType, k -> new HashMap<>());
        typeCache.put(id, result);
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
        if (searchString == null || searchString.trim().equals("")) {
            return null;
        }

        // check cache for results
        if (!ignoreCache) {
            JSONObject fromCache = loadFromCache(searchString, objectType);
            if (fromCache != null) {
                return fromCache;
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
        storeInCache(searchString, objectType, result);

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
    public JSONObject resolveStringSearchToOne(String search, String objectType, boolean ignoreCache) throws IOException, InterruptedException {
        if (search == null || objectType == null) {
            log.warn("Cannot search as search " + search + " or objectType " + objectType + " is null");
            return null;
        }

        JSONObject results = this.searchForString(search, objectType, ignoreCache);

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

    public JSONObject resolveStringSearchToOne(String search, String objectType) throws IOException, InterruptedException {
        return resolveStringSearchToOne(search, objectType, false);
    }

    /**
     * Search an object in Nahima by a string. Create it if it is not found.
     *
     * @param name       the string to search by
     * @param objectType the type of object to search
     * @param mask       the mask of the field (the mapping), required for creation
     * @param inner      the object with the properties intended for the newly created object
     * @return the matching or new object
     */
    public JSONObject resolveOrCreate(String name, String objectType, String mask, JSONObject inner) throws IOException, InterruptedException {
        return resolveOrCreate(name, objectType, mask, inner, false);
    }

    public JSONObject resolveOrCreate(String name, String objectType, String mask, JSONObject inner, boolean omitPool) throws IOException, InterruptedException {
        if (name == null || name.equals("")) {
            return null;
        }

        JSONObject results = this.resolveStringSearchToOne(name, objectType);
        // TODO: select correct
        if (results == null) {
            // create
            JSONObject toCreate = wrapForCreation(inner, objectType, mask, omitPool);
            this.createObjectInNahima(toCreate, objectType);
            // TODO: it would be simpler to store the created in cache. But well... current format does not allow
            return this.resolveStringSearchToOne(name, objectType, true);
        }
        return results;
    }

    /**
     * Search an object in Nahima which has a "name" and a "bemerkung" field.
     * Create if not existing with Bemerkung = "Created by DataShot {version}"
     *
     * @param name       the expected value of the "name" field
     * @param objectType the "_objecttype"
     * @param mask       the mask of the field (the mapping)
     * @return the object in Nahima
     */
    public JSONObject resolveNameBemerkungObject(String name, String objectType, String mask) throws IOException, InterruptedException {
        return this.resolveOrCreate(name, objectType, mask, new JSONObject(new HashMap<>() {{
            put("name", name);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
    }

    public JSONObject resolveNameBemerkungObject(String name, String objectType) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(name, objectType, objectType + "_all_fields");
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
     * Find a typusstatus in Nahima
     *
     * @param nrType the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveOtherNrType(String nrType) throws IOException, InterruptedException {
        return this.resolveNameBemerkungObject(nrType, "id_typen", "id_typen_all_fields");
    }

    /**
     * Find the collection method in Nahima
     *
     * @param method the collection method to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveCollectionMethod(String method) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(method, "sammlungsmethoden", "sammlungsmethoden__all_fields");
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
     * Find a family in Nahima
     *
     * @param family the family to search for resp. create
     */
    public JSONObject resolveFamily(String family) throws IOException, InterruptedException {
        return resolveOrCreate(family, "familien", "familien_all_fields", new JSONObject(new HashMap<>() {{
            put("familielat", family);
        }}));
    }

    /**
     * Find a subfamily in Nahima
     *
     * @param family the subfamily's name to resolve
     */
    public JSONObject resolveSubFamily(String family) throws IOException, InterruptedException {
        return resolveOrCreate(family, "unterfamilien", "unterfamilien_all_fields", new JSONObject(new HashMap<>() {{
            put("unterfamilielat", family);
        }}));
    }

    /**
     * Find a specific epithet ("art") in Nahima
     *
     * @param specificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveSpecificEpithet(String specificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(specificEpithet, "art", "art_all_fields", new JSONObject(new HashMap<>() {{
            put("artlat", specificEpithet);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find a subspecific epithet in Nahima
     *
     * @param subspecificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveSubSpecificEpithet(String subspecificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(subspecificEpithet, "subspezifischeart", "subspezifischeart_all_fields", new JSONObject(new HashMap<>() {{
            put("subspezifischeartlat", subspecificEpithet);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find an author in Nahima
     *
     * @param authorship the name to search for
     * @return the existing or new author
     */
    public JSONObject resolveAuthorship(String authorship) throws IOException, InterruptedException {
        return resolveOrCreate(authorship, "authors", "author__all_fields", new JSONObject(new HashMap<>() {{
            put("autor", authorship);
            put("_nested:authors__personen", new JSONArray());
        }}), true);
    }

    /**
     * Find an infraspecific epithet in Nahima
     *
     * @param infraspecificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveInfraspecificEpithet(String infraspecificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(infraspecificEpithet, "infraspezifischetaxon", "infraspezifischetaxon_all_fields", new JSONObject(new HashMap<>() {{
            put("infraspezifischetaxon", infraspecificEpithet);
            put("abkuerzung", JSONObject.NULL);
            put("_nested:infraspezifischetaxon__trivialnamen", new JSONArray());
            put("_nested:infraspezifischetaxon__referenzenfuerintraspezifischestaxon", new JSONArray());
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find an infraspecific rank in Nahima
     *
     * @param infraspecificRank the subfamily's name to resolve
     */
    public JSONObject resolveInfraspecificRank(String infraspecificRank) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(infraspecificRank, "infraspezifischerrang", "id_typen_all_fields_1");
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

    /**
     * Add pool, id and version so the object can be created appropriately in Nahima
     *
     * @param inner      the object to create
     * @param objectType the "_objecttype" to create the object in
     * @param mask       the mask specifying the fields to use
     * @return the wrapped object, ready to be created
     */
    private JSONObject wrapForCreation(JSONObject inner, String objectType, String mask, boolean omitPool) {
        inner = this.addDefaultValuesForCreation(inner, omitPool);
        JSONObject result = new JSONObject(new HashMap<>() {{
            put("_mask", mask);
            put("_objecttype", objectType);
            put("_idx_in_objects", 0);
        }});
        result.put(objectType, inner);
        return result;
    }

    public JSONObject wrapForCreation(JSONObject inner, String objectType, String mask) {
        return wrapForCreation(inner, objectType, mask, false);
    }

    public JSONObject wrapForCreation(JSONObject inner, String objectType) {
        return wrapForCreation(inner, objectType, objectType + "_all_fields");
    }

    /**
     * Add fields with values that are required when creating a new entry in Nahima
     *
     * @param inner the object to add the fields to
     * @return the object with the adjusted/added fields
     */
    public JSONObject addDefaultValuesForCreation(JSONObject inner) {
        return addDefaultValuesForCreation(inner, false);
    }

    public JSONObject addDefaultValuesForCreation(JSONObject inner, boolean omitPool) {
        if (!omitPool) {
            inner.put("_pool", defaultPool);
        }
        inner.put("_id", JSONObject.NULL);
        inner.put("_version", 1);
        return inner;

    }

    public String getCreatedByThisSoftwareIndication() {
        return "Created by " + ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion();
    }
}
