package edu.harvard.mcz.imagecapture.data;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
import edu.harvard.mcz.imagecapture.ui.dialog.ChooseFromJArrayDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.VerifyJSONDialog;
import edu.harvard.mcz.imagecapture.utility.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class NahimaManager extends AbstractRestClient {
    public static final int defaultPoolId = 7;
    public static final JSONObject zoologyPool = new JSONObject("{ \"pool\": { \"_id\": 4 } }");
    public static final JSONObject entomologyPool = new JSONObject("{ \"pool\": { \"_id\": 7 } }");
    private static final Logger log = LoggerFactory.getLogger(NahimaManager.class);
    private final String url;
    private final String username;
    private final String password;
    private final boolean interactive;
    private final Map<String, Map<String, JSONObject>> resolveCache = new HashMap<>();
    private String token;

    public NahimaManager(String url, String username, String password, boolean interactive) throws IOException, InterruptedException, RuntimeException {
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
        this.interactive = interactive;

        this.startSessionAndRetrieveToken();
        this.login();
    }

    /**
     * Somehow, the ids of objects are done inconsistently in Nahima.
     * Instead of finding out why/how, this function just goes through the possibilites
     *
     * @param objWithId the object whose id is searched
     * @return the object id, null if not found
     */
    protected static Integer resolveId(JSONObject objWithId, String objectType) {
        if (objWithId == null) {
            return null;
        }
        Integer id = null;
        if (objWithId.has(objectType)) {
            id = resolveId(objWithId.getJSONObject(objectType));
        }
        if (id == null) {
            return resolveId(objWithId);
        }
        return id;
    }

    public static Integer resolveId(JSONObject objWithId) {
        if (objWithId == null) {
            return null;
        }
        if (objWithId.has("_id")) {
            return objWithId.getInt("_id");
        }
        if (objWithId.has("_system_object_id")) {
            return objWithId.getInt("_system_object_id");
        }
        if (objWithId.has("_global_object_id")) {
            return Integer.parseInt(objWithId.getString("_global_object_id").split("@")[0]);
        }
        return null;
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
                throw new RuntimeException("Failed to log in. Error code: " + responseObject.get("code") + ". " + (responseObject.has("description") ? responseObject.getString("description") : ""));
            }
        }
    }

    /**
     * This function loops the images in a Specimen and uploads them to Nahima
     *
     * @param specimen the specimen whose images to upload
     * @return the created mediassets
     */
    public ArrayList<JSONObject> uploadImagesForSpecimen(Specimen specimen, JSONObject existingExport) throws IOException, InterruptedException, RuntimeException {
        // docs:
        // https://docs.easydb.de/en/technical/api/eas/
        // https://docs.easydb.de/en/sysadmin/eas/api/put/
        String baseQueryUrl = this.url + "eas/put?token=" + this.token;
        ArrayList<JSONObject> results = new ArrayList<>();

        for (ICImage image : specimen.getICImages()) {
            // first, filter whether this particular image is already online, part of this specimen
            boolean foundExisting = false;
            if (existingExport != null && existingExport.getJSONObject("entomologie").has("_reverse_nested:entomologie_mediaassetpublic:entomologie")) {
                // check if the image has already been uploaded
                JSONArray mediaassets = existingExport.getJSONObject("entomologie").getJSONArray("_reverse_nested:entomologie_mediaassetpublic:entomologie");
                for (int i = 0; i < mediaassets.length(); ++i) {
                    JSONObject std = mediaassets.getJSONObject(i).getJSONObject("mediaassetpublic").getJSONObject("_standard").getJSONObject("eas");
                    Iterator<String> keys = std.keys();

                    while (keys.hasNext()) {
                        String key = keys.next();
                        String filename = std.getJSONArray(key).getJSONObject(0).getString("original_filename");
                        if (Objects.equals(filename, image.getFilename())) {
                            // file has been uploaded already
                            results.add(
                                    mediaassets.getJSONObject(i)
                            );
                            foundExisting = true;
                            break;
                        }
                    }
                }
            }
            if (foundExisting) {
                continue;
            }

            // if not existing, continue with uploading
            String queryUrl = baseQueryUrl + "&original_filename=" + image.getFilename() + "&instance=image";
            log.debug("Running image upload to URL " + queryUrl);

            String imagePath = FileUtility.findValidFilepath(
                    ImageCaptureProperties.assemblePathWithBase(image.getPath(), image.getFilename()),
                    image.getPath() + File.separator + image.getFilename(),
                    image.getPath()
            );

            MultipartBodyPublisher multipartBody = MultipartBodyPublisher.newBuilder().filePart("image", Path.of(imagePath), MediaType.IMAGE_ANY).build();
            log.debug("Built multipart body...");
            MutableRequest request = MutableRequest.POST(queryUrl, multipartBody).header("Content-Disposition", "attachment; filename=\"" + image.getFilename() + "\"");
            log.debug("Built request to upload...");
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            log.debug("Response from uploading image: " + response.body());

            // check for errors
            if (!response.body().startsWith("[")) {
                JSONObject responseObject = new JSONObject(response.body());
                if (responseObject.has("code") && responseObject.getString("code").startsWith("error")) {
                    throw new RuntimeException("Failed to upload image: Error code: " + responseObject.get("code"));
                }
            }

            results.add((new JSONArray(response.body())).getJSONObject(0));
        }

        return results;
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
                throw new RuntimeException("Failed to create object of type '" + objType + "': Error code: " + responseObject.get("code"));
            }
        }

        // check which object types the user is allowed to create
//        try {
//            this.postRequest(this.url + "db_info/create", new JSONObject(new HashMap<>() {{
//                put("objecttype", objType);
//                put("tag_ids", JSONObject.NULL);
//            }}));
//        } catch (Exception e) {
//        }

        return (new JSONArray(returnValue)).getJSONObject(0);
    }

    public JSONObject findObjectByUuid(String uuid) throws IOException {
        String queryURL = this.url + "objects/uuid/" + uuid + "?token=" + token;
        String returnValue = this.getRequest(queryURL, new HashMap<>() {{
            put("Content-Type", "application/json");
        }});

        // check for errors
        if (returnValue == null || !returnValue.startsWith("[")) {
            JSONObject responseObject = new JSONObject(returnValue);
            if (responseObject.has("code") && responseObject.getString("code").startsWith("error")) {
                throw new RuntimeException("Failed to fetch object by uuid '" + uuid + "': Error code: " + responseObject.get("code"));
            }
        }

        return new JSONObject(returnValue);
    }

    /**
     * Load an object from Nahima by its global object identifier
     *
     * @param globalObjectId the global object identifier
     * @return the object
     * @throws IOException
     * @throws InterruptedException
     */
    public JSONObject findObjectByGlobalObjectId(String globalObjectId) throws IOException, InterruptedException {
        if (globalObjectId == null || globalObjectId.equals("")) {
            return null;
        }

        String queryURL = this.url + "search" + "?token=" + token;
        JSONObject query = new JSONObject() {{
            put("best_mask_filter", true);
            put("generate_rights", false);
            put("search", new JSONArray() {{
                put(new JSONObject() {{
                    put("type", "in");
                    put("fields", new JSONArray() {{
                        put("_global_object_id");
                    }});
                    put("in", new JSONArray() {{
                        put(globalObjectId);
                    }});
                    put("language", "en-US");
                }});
            }});
        }};

        JSONObject result = this.postRequest(queryURL, query);
        if (result.has("code") && result.getString("code").startsWith("error")) {
            throw new RuntimeException("Failed to fetch object by global object id '" + globalObjectId + "': Error code: " + result.get("code"));
        }
        if (result.has("objects") && result.getJSONArray("objects").length() == 0) {
            return null;
        }
        if (result.has("objects")) {
            // TODO: we can expect that for what we use it currently, there will never be more than one.
            // however, that's not a general case
            return result.getJSONArray("objects").getJSONObject(0);
        }
        return result;
    }

    /**
     * Small utility method to speed some things up,
     * by loading from this local cache (keyword: memoization)
     *
     * @param id         the cache identifier
     * @param objectType the type of object to retrieve
     * @return the cached object
     * @see NahimaManager::storeInCache
     */
    protected JSONObject loadFromCache(String id, String objectType) {
        Map<String, JSONObject> typeCache = resolveCache.get(objectType);
        if (typeCache != null) {
            return typeCache.get(id);
        }
        return null;
    }

    /**
     * Small utility method to speed some things up,
     * by storing to this local cache (keyword: memoization)
     *
     * @param id         the cache identifier
     * @param objectType the type of object to retrieve
     * @param result     the object to cache/memoize
     * @see NahimaManager::loadFromCache
     */
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
    public JSONObject searchByString(String searchString, ArrayList<String> fields, String objectType, String searchMode, boolean ignoreCache, JSONObject additionalFilter) throws IOException, InterruptedException {
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
            if (fields != null && fields.size() > 0) {
                JSONArray fieldsArray = new JSONArray();
                fields.forEach(field -> fieldsArray.put(field));
                namePartSearch.put("fields", fieldsArray);
            }
            search.put(namePartSearch);
        }

        if (additionalFilter != null) {
            search.put(additionalFilter);
        }

        JSONObject superSearch = new JSONObject();
        superSearch.put("search", search);
        superSearch.put("type", "complex");
        JSONArray superSuperSearch = new JSONArray();
        superSuperSearch.put(superSearch);
        JSONObject query = new JSONObject();
        query.put("type", "object");
        query.put("language", "en-US");
        query.put("objecttypes", new JSONArray(new String[]{objectType}));
        query.put("search", superSuperSearch);

        // store result in cache
        JSONObject result = this.postRequest(queryURL, query);
        storeInCache(searchString, objectType, result);

        return result;
    }

    public JSONObject searchByString(String searchString, String objectType, String searchMode, boolean ignoreCache) throws IOException, InterruptedException {
        return this.searchByString(searchString, null, objectType, searchMode, ignoreCache, null);
    }

    public JSONObject searchByString(String searchString, String objectType) throws IOException, InterruptedException {
        return searchByString(searchString, objectType, false);
    }

    public JSONObject searchByString(String searchString, String objectType, String searchMode) throws IOException, InterruptedException {
        return searchByString(searchString, objectType, searchMode, false);
    }

    public JSONObject searchByString(String searchString, String objectType, boolean ignoreCache) throws IOException, InterruptedException {
        return searchByString(searchString, objectType, "fulltext", ignoreCache);
    }

    /**
     * Search in Nahima for one string, get result only if just one result though
     *
     * @param search     the string to search for
     * @param objectType the object type that is searched
     * @return the object if there is only one, null if not
     */
    public JSONObject resolveStringSearchToOne(String search, String objectType, boolean ignoreCache) throws IOException, InterruptedException {
        return resolveStringSearchToOne(search, objectType, ignoreCache, null);
    }

    public JSONObject resolveStringSearchToOne(String search, String objectType, boolean ignoreCache, JSONObject selectionHelper) throws IOException, InterruptedException {
        if (search == null || objectType == null) {
            log.warn("Cannot search as search " + search + " or objectType " + objectType + " is null");
            return null;
        }

        JSONObject results = this.searchByString(search, objectType, ignoreCache);

        if (results.has("code") && results.getString("code").startsWith("error")) {
            throw new RuntimeException("Failed to resolve search. Error code: " + results.getString("code"));
        }

        JSONArray foundObjects = (JSONArray) results.get("objects");
        if (foundObjects.length() == 1) {
            return (JSONObject) foundObjects.get(0);
        } else {
            // TODO: create / select correct
            log.info("Got " + String.valueOf(foundObjects.length()) + " != 1 " + objectType + " objects when searching for '" + search + "'. {}", results);
            if (selectionHelper != null) {
                log.info("Using selectionHelper {}", selectionHelper);
                return findSimilarMatch(foundObjects, objectType, selectionHelper, true);
            }
            return null;
        }
    }

    public JSONObject resolveStringSearchToOne(String search, String objectType) throws IOException, InterruptedException {
        return resolveStringSearchToOne(search, objectType, false);
    }

    /**
     * Compares an array of JSON objects to an ideal JSON object,
     * returns a match if there is one
     *
     * @param foundObjects    the array of JSON objects to compare to
     * @param objectType      the type of the JSON object
     * @param selectionHelper the ideal JSON object
     * @param allowDuplicates whether to return a match if more than one matches
     * @return the matching object from the found objects
     */
    protected JSONObject findSimilarMatch(JSONArray foundObjects, String objectType, JSONObject selectionHelper, boolean allowDuplicates) {
        ArrayList<Integer> possiblyCorrectIndices = new ArrayList<Integer>();
        ArrayList<Integer> nrOfAdditionalKeysForPossiblyCorrectIndices = new ArrayList<Integer>();
        ArrayList<Integer> carefulPossiblyCorrectIndices = new ArrayList<Integer>();
        ArrayList<JSONObject> objectsToCompare = new ArrayList<JSONObject>();
        HashMap<Integer, Integer> newIndicesToOldIndices = new HashMap<Integer, Integer>();
        for (int i = 0; i < foundObjects.length(); ++i) {
            JSONObject testObj = foundObjects.getJSONObject(i);
            objectsToCompare.add(testObj);
            newIndicesToOldIndices.put(objectsToCompare.size() - 1, i);
            if (testObj.has(objectType)) {
                objectsToCompare.add(testObj.getJSONObject(objectType));
                newIndicesToOldIndices.put(objectsToCompare.size() - 1, i);
            }
        }
        int requiredMatches = 0;
        ArrayList<String> keysToMatch = new ArrayList<String>();
        for (Iterator<String> it = selectionHelper.keys(); it.hasNext(); ) {
            String key = it.next();
            if (key.startsWith("_")) {
                continue;
            }
            if (selectionHelper.get(key) instanceof JSONObject && selectionHelper.getJSONObject(key).has("en-US")) {
                requiredMatches += 1;
                keysToMatch.add(key);
            } else if (selectionHelper.get(key) instanceof String && !((String) selectionHelper.get(key)).contains("DataShot")) {
                keysToMatch.add(key);
                requiredMatches += 1;
            }
        }

        for (int i = 0; i < objectsToCompare.size(); ++i) {
            JSONObject testObj = objectsToCompare.get(i);
            int matches = 0;
            for (String key : keysToMatch) {
                if (selectionHelper.get(key) instanceof JSONObject && selectionHelper.getJSONObject(key).has("en-US")) {
                    if (testObj.has(key) && Objects.equals(testObj.getJSONObject(key).getString("en-US"), selectionHelper.getJSONObject(key).getString("en-US"))) {
                        matches += 1;
                    }
                }
                // TODO: compare more than just Strings, Integers and
                if (selectionHelper.get(key) instanceof String && !((String) selectionHelper.get(key)).contains("DataShot")) {
                    if (selectionHelper.get(key).equals(testObj.has(key) ? testObj.get(key) : null)) {
                        matches += 1;
                    }
                }
                if (selectionHelper.get(key) instanceof Integer) {
                    if (testObj.has(key) && testObj.getInt(key) == selectionHelper.getInt(key)) {
                        matches += 1;
                    }
                }
            }
            if (matches >= requiredMatches && requiredMatches > 0) {
                int numAdditionalKeys = -matches;
                for (Iterator<String> it = testObj.keys(); it.hasNext(); ) {
                    String key = it.next();
                    if (key.startsWith("_")) {
                        continue;
                    }
                    numAdditionalKeys += 1;
                }

                try {
                    JSONObject pool = (JSONObject) testObj.get("_pool");
                    if (((JSONObject) pool.get("pool")).getInt("_id") == NahimaManager.defaultPoolId) {
                        possiblyCorrectIndices.add(newIndicesToOldIndices.get(i));
                        nrOfAdditionalKeysForPossiblyCorrectIndices.add(numAdditionalKeys);
                    }
                } catch (Exception e) {
                    log.error("Could not verify pool", e);
                    carefulPossiblyCorrectIndices.add(newIndicesToOldIndices.get(i));
                }
            }
        }

        if (possiblyCorrectIndices.size() == 1) {
            return foundObjects.getJSONObject(possiblyCorrectIndices.get(0));
        } else if (possiblyCorrectIndices.size() > 1 && allowDuplicates) {
            log.warn("Found multiple " + objectType + " that match all fields. Using first one.");
            return foundObjects.getJSONObject(possiblyCorrectIndices.get(0));
        } else if (carefulPossiblyCorrectIndices.size() > 0) {
            log.warn("Found " + carefulPossiblyCorrectIndices.size() + " " + objectType + " that match all fields, but without verifying pool. Using first one.");
            if (allowDuplicates || (possiblyCorrectIndices.size() == 0 && carefulPossiblyCorrectIndices.size() == 1)) {
                return foundObjects.getJSONObject(carefulPossiblyCorrectIndices.get(0));
            }
        } else {
            log.warn("Did not find any similar matches. Required matches: " + requiredMatches);
        }
        return null;
    }

    protected JSONObject findExactMatch(JSONArray foundObjects, String objectType, JSONObject selectionHelper) {
        for (int i = 0; i < foundObjects.length(); ++i) {
            JSONObject testObj = foundObjects.getJSONObject(i);
            if (JSONUtility.areEqualIgnoringUnderscore(testObj, selectionHelper)) {
                return testObj;
            }
            if (testObj.has(objectType) && JSONUtility.areEqualIgnoringUnderscore(selectionHelper, testObj.getJSONObject(objectType))) {
                return testObj;
            }
        }
        return null;
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
    public JSONObject resolveOrCreate(String name, String objectType, String mask, JSONObject inner, JSONObject pool) throws IOException, InterruptedException {
        if (name == null || name.equals("")) {
            return null;
        }

        JSONObject results = this.resolveStringSearchToOne(name, objectType, false, inner);
        // TODO: select correct
        if (results == null) {
            // create
            JSONObject toCreate = wrapForCreation(inner, objectType, mask, pool);
            JSONObject createdResponse = this.createObjectInNahima(toCreate, objectType);
            Thread.sleep(50); // this is a heuristic number and is here to improve reliability, as Nahima does not usually claim creation immediately.
            if (createdResponse.has("_uuid")) {
                try {
                    results = this.findObjectByUuid(createdResponse.getString("_uuid"));
                } catch (IOException ex) {
                    // maybe handle? don't know...
                    log.error("Failed to find object with name '" + name + "' of type '" + objectType + "' by uuid: {}", ex);
                }
            } else {
                // TODO: it would be simpler to store the created in cache. But well... current format does not allow
                results = this.resolveStringSearchToOne(name, objectType, true, inner);
            }
            if (results == null) {
                log.error("Created " + objectType + " in Nahima, but did not find it afterwards.");
                results = createdResponse;
            }
        }
        return results;
    }

    /**
     * Remove duplicate entries (ignoring underscores in JSON Object' keys) from a JSONArray
     *
     * @param results
     * @return
     */
    public JSONArray removeDuplicates(JSONArray results) {
        JSONArray uniqueResults = new JSONArray();
        for (int i = 0; i < results.length(); i++) {
            JSONObject entry = results.getJSONObject(i);
            boolean found = false;
            // check that this is not present yet
            for (int j = 0; j < uniqueResults.length(); j++) {
                JSONObject compareTo = uniqueResults.getJSONObject(j);
                if (JSONUtility.areEqualIgnoringUnderscore(entry, compareTo)) {
                    found = true;
                }
            }
            if (!found) {
                uniqueResults.put(entry);
            }
        }
        return uniqueResults;
    }

    /**
     * Search an object in Nahima by a string.
     * Ask the user whether to create it if it is not found,
     * or which to use if more than one is found.
     *
     * @param name       the string to search by
     * @param objectType the type of object to search
     * @param mask       the mask of the field (the mapping), required for creation
     * @param inner      the object with the properties intended for the newly created object
     * @param pool       the pool to create the new object in, if creating anew
     * @param recurse    the number of times we are searching already for the object
     * @return the matching or new object
     */
    public JSONObject resolveOrCreateInteractive(String name, String objectType, String mask, JSONObject inner, JSONObject pool, int recurse) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        if (name == null || objectType == null || name.equals("")) {
            log.warn("Cannot search as search " + name + " or objectType " + objectType + " is null or empty");
            return null;
        }

        JSONObject results = this.searchByString(name, objectType, false);

        if (results == null || (results.has("code") && results.getString("code").startsWith("error")) || !results.has("objects")) {
            throw new RuntimeException("Failed to resolve search for '" + name + "' (" + objectType + ", " + mask + "). Error code: " +
                    ((results != null && results.has("code")) ? results.getString("code") : "Unknown"));
        }

        JSONArray foundObjects = removeDuplicates(results.getJSONArray("objects"));
        if (foundObjects.length() == 1) {
            // TODO: check compliance, does it really match well? We use "must", so let's hope so, but the resolution could still go wrong.
            return (JSONObject) foundObjects.get(0);
        } else {
            boolean createSpecimen = false;
            log.info("Got " + foundObjects.length() + " != 1 objects of type '" + objectType + "'. {}", results);
            // create / select correct
            if (foundObjects.length() > 1) {
                JSONObject perfectMatch = findExactMatch(foundObjects, objectType, inner);
                if (perfectMatch != null) {
                    return perfectMatch;
                }
                // TODO: maybe, we only want the perfect match, and nothing else?!?
                // first, loop objects to see whether we can find exactly one exact match
                JSONObject bestMatch = findSimilarMatch(foundObjects, objectType, inner, !this.interactive);
                if (bestMatch != null) {
                    log.info("Found 1 best match, using it.");
                    return bestMatch;
                } else {
                    log.info("Did not find similar match");
                }
                log.info("Asking user to select " + name + " (" + objectType + ") from results.", results);
                // otherwise, ask the user to select the correct one
                if (!this.interactive && ListUtility.allEqual(
                        foundObjects.toList().stream().map(el -> ChooseFromJArrayDialog.simplifyJSONObjectText(el, objectType)).collect(Collectors.toList())
                )) {
                    return foundObjects.getJSONObject(0);
                }

                if (!this.interactive) {
                    createSpecimen = true;
                } else {
                    return this.askToChooseObject(foundObjects, name, objectType, mask, inner, pool);
                }
            } else {
                assert (foundObjects.length() == 0);
                createSpecimen = true;
            }

            assert (createSpecimen);
            JSONObject created = askToCreate(inner, name, objectType, mask, pool);
            // TODO: the following could fail if the search and created object do not really align
            JSONObject found = this.resolveStringSearchToOne(name, objectType, true);
            if (found == null && created != null) {
                return created;
            }
            return found;
        }
    }

    public JSONObject resolveOrCreateInteractive(String name, String objectType, String mask, JSONObject inner, JSONObject pool) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(name, objectType, mask, inner, pool, 0);
    }

    /**
     * Opens a dialog for the user to verify that an entity would be created correctly
     *
     * @param inner      the object prototype
     * @param name       the name of the object
     * @param objectType the type of the object
     * @param mask       the mask to create the object with
     * @param pool       the pool to create the object in, if creating anew
     * @return the created object
     * @throws IOException
     * @throws InterruptedException
     * @throws SkipSpecimenException
     * @throws InvocationTargetException
     */
    public JSONObject askToCreate(JSONObject inner, String name, String objectType, String mask, JSONObject pool) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        // ask whether to create the object like this
        final int[] choice = new int[1];
        final JSONObject[] resultingObj = new JSONObject[1];
        JSONObject finalInner = inner;
        if (interactive) {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    VerifyJSONDialog dialog = new VerifyJSONDialog(Singleton.getSingletonInstance().getMainFrame(), finalInner.toString(), name);
                    dialog.setVisible(true);
                    int result = dialog.getReturnDecision();
                    choice[0] = result;
                    resultingObj[0] = new JSONObject(dialog.getResultingJSON());
                }
            });
            switch (choice[0]) {
                case VerifyJSONDialog.RETURN_ACCEPT:
                    inner = resultingObj[0];
                    break;
                case VerifyJSONDialog.RETURN_CHANGE_SEARCH:
                    return askToChangeSearch(name, objectType, inner, mask, pool);
                case VerifyJSONDialog.RETURN_SKIP:
                    log.debug("NoExport: User requested skip specimen");
                    throw new SkipSpecimenException();
            }
        } else {
            resultingObj[0] = inner;
        }
        JSONObject toCreate = wrapForCreation(inner, objectType, mask, pool);
        // It would be simpler to store the created in cache. But well... current format does not allow
        return this.createObjectInNahima(toCreate, objectType);
    }

    /**
     * Interactively ask the user which of multiple JSONObjects is the correct one
     *
     * @param foundObjects the objects in a JSONArray to decide between
     * @param name         the search string
     * @param objectType   the object type
     * @param mask         the mask for when creating the object
     * @param inner        the object to use for creation when applicable
     * @param pool         the pool to use, to save the object in, when creating the object
     */
    public JSONObject askToChooseObject(JSONArray foundObjects, String name, String objectType, String mask, JSONObject inner, JSONObject pool) throws SkipSpecimenException, IOException, InterruptedException, InvocationTargetException {
        // TODO: implement a better auto-choose algorithm
        if (!this.interactive) {
            log.debug("NoExport: askToChooseObject will not be run without interactive. To choose from: " + foundObjects.toString() + " for " + inner.toString());
//            throw new SkipSpecimenException();
        }

        final int[] choice = new int[1];
        final JSONObject[] selection = new JSONObject[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ChooseFromJArrayDialog dialog = new ChooseFromJArrayDialog(Singleton.getSingletonInstance().getMainFrame(), foundObjects, objectType, name);
                dialog.setVisible(true);
                int result = dialog.getReturnDecision();
                choice[0] = result;
                selection[0] = dialog.getSelectedItem();
            }
        });
        switch (choice[0]) {
            case ChooseFromJArrayDialog.RETURN_ACCEPT:
                log.debug("User chose to accept selection");
                assert (selection[0] != null && !selection[0].isEmpty());
                return selection[0];
            case ChooseFromJArrayDialog.RETURN_SKIP:
                log.debug("NoExport: User chose to skip specimen");
                throw new SkipSpecimenException();
            case ChooseFromJArrayDialog.RETURN_CREATE_NEW:
                log.debug("User chose to create new");
                return askToCreate(inner, name, objectType, mask, pool);
            case ChooseFromJArrayDialog.RETURN_CHANGE_SEARCH:
                log.debug("User chose to change search");
                return askToChangeSearch(name, objectType, inner, mask, pool);
            default:
                throw new RuntimeException("Undefined result type: " + choice[0]);
        }
    }

    /**
     * Opens a dialog to ask the user for a different search string
     *
     * @param name       the current search
     * @param objectType the type of object to search for
     * @param inner      the object to use to create anew if applicable
     * @param mask       the mask to use when creating anew
     * @param pool       the pool to use, to save the object in, when creating anew
     */
    private JSONObject askToChangeSearch(String name, String objectType, JSONObject inner, String mask, JSONObject pool) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        final String[] newSearch = new String[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                newSearch[0] = (String) JOptionPane.showInputDialog(Singleton.getSingletonInstance().getMainFrame(), "Search for " + objectType, "Change Search", JOptionPane.QUESTION_MESSAGE, null, null, name);
            }
        });
        return resolveOrCreateInteractive(newSearch[0], objectType, mask, inner, pool, 1);
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
        }}), null);
    }

    public JSONObject resolveNameBemerkungObject(String name, String objectType) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(name, objectType, objectType + "_all_fields");
    }

    /**
     * Find a person in Nahima
     *
     * @param personName the name to search for
     */
    public JSONObject resolvePerson(String personName) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        if (personName == null) {
            return null;
        }
        String[] splitName;
        String actualNamePart = personName;
        String birthdate = "";
        String deathdate = "";
        String lastName = "";
        String firstName = "";
        if (personName.contains("(")) {
            String[] personNameSplit = personName.split("\\(");
            actualNamePart = personNameSplit[0];
            String datePart = personNameSplit[1];
            String[] splitDatePart = Arrays.stream(datePart.split("-")).map(String::trim).toArray(String[]::new);
            birthdate = splitDatePart[0].replace("(", "").replace(")", "").replace("*", "").trim();
            if (splitDatePart.length > 1) {
                deathdate = splitDatePart[1].replace(")", "").trim();
            }
        }
        if (actualNamePart.contains(",")) {
            splitName = Arrays.stream(actualNamePart.split(",")).map(String::trim).toArray(String[]::new);
            lastName = splitName[0];
            firstName = splitName[1];
        } else {
            splitName = Arrays.stream(actualNamePart.split(" ")).map(String::trim).toArray(String[]::new);
            firstName = splitName[0];
            lastName = splitName.length > 1 ? splitName[splitName.length - 1] : "";
        }
        // inner class stuff
        String finalActualNamePart = actualNamePart.trim();
        String finalFirstName = firstName;
        String finalLastName = lastName;
        HashMap<String, Object> personHashMap = new HashMap<>() {{
            put("vollername", finalActualNamePart);
            put("vorname", finalFirstName.equals("") ? JSONObject.NULL : finalFirstName);
            put("nachname", finalLastName.equals("") ? JSONObject.NULL : finalLastName);
            put("mitlerername", JSONObject.NULL); // TODO: split first name into these here where appropriate
            put("nismpersonid", JSONObject.NULL);
        }};
        if (!birthdate.equals("")) {
            String finalBirthdate = birthdate;
            personHashMap.put("geburtstag", new JSONObject(new HashMap<>() {{
                put("value", finalBirthdate);
            }}));
        }
        if (!deathdate.equals("")) {
            String finalDeathdate = deathdate;
            personHashMap.put("sterbetag", new JSONObject(new HashMap<>() {{
                put("value", finalDeathdate);
            }}));
        }
        log.debug("Resolved person " + personName + " to first name " + finalFirstName + ", last name " + finalLastName + ", birth date " + birthdate + ", and death date " + deathdate);
        return this.resolveOrCreateInteractive(personName, "person", "person__public", new JSONObject(personHashMap), null, 0);
    }

    public JSONObject resolveRegionType(String type) throws IOException, InterruptedException {
        return this.resolveStringSearchToOne(type, "region_verwaltungseinheiten");
    }

    public JSONObject resolveCountry(String countryName) throws IOException, InterruptedException, SkipSpecimenException {
        // need to make the filter as specific as possible
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("gazetteer.ortsname");
        JSONObject additionalFilter = new JSONObject();
        additionalFilter.put("type", "in");
        additionalFilter.put("bool", "must");
        JSONArray filterFieldsFields = new JSONArray();
        filterFieldsFields.put("gazetteer.region_verwaltungseinheit._global_object_id");
        additionalFilter.put("fields", filterFieldsFields);
        JSONArray filterFieldsIn = new JSONArray();
        filterFieldsIn.put(resolveRegionType("Land").getString("_global_object_id"));
        additionalFilter.put("in", filterFieldsIn);

        JSONObject results = this.searchByString(countryName, fields, "gazetteer", "fulltext", false, additionalFilter);

        if (results.has("code") && results.getString("code").startsWith("error")) {
            throw new RuntimeException("Failed to resolve search for '" + countryName + "'. Error code: " + results.getString("code"));
        }

        JSONArray foundObjects = (JSONArray) results.get("objects");
        if (foundObjects != null && foundObjects.length() == 1) {
            return (JSONObject) foundObjects.get(0);
        }
        return null;
    }

    /**
     * Find a location in Nahima
     *
     * @param specimen the specimen to search the location for
     * @return the Nahima returned object if only one
     */
    public JSONObject resolveLocation(Specimen specimen) throws IOException, InterruptedException, SkipSpecimenException, NullPointerException, InvocationTargetException {
        if (specimen == null) {
            return null;
        }
        String searchString = "";
        if (specimen.getPrimaryDivison() != null && !specimen.getPrimaryDivison().equals("unknown")) {
            searchString = specimen.getPrimaryDivison();
        }
        if (specimen.getCountry() != null && !specimen.getCountry().equals("unknown")) {
            searchString += " " + specimen.getCountry();
        }
        if (Objects.equals(searchString.trim(), "")) {
            if (specimen.getPrimaryDivisonISO() != null && !specimen.getPrimaryDivisonISO().equals("unknown")) {
                searchString = specimen.getPrimaryDivisonISO();
            }
        }

        searchString = searchString.trim();

        if (searchString.equals("")) {
            return new JSONObject();
        }

        JSONObject parent = null;
        try {
            parent = resolveCountry(specimen.getCountry());
        } catch (Exception ignored) {
        }

        JSONObject finalParent = parent;
        Integer parentId = resolveId(finalParent, "gazetteer");

        HashMap<String, Object> paramMap = new HashMap<>() {{
            put("ortsname", wrapInLan(stringTrimOrJSONNull(specimen.getPrimaryDivison())));
            put("_id_parent", parentId == null ? JSONObject.NULL : parentId); // Might throw NullPointerException. If we find the location, it is never called, therefore, we only have a problem if neither location nor country are found
//            put("isocode3166_2", specimen.getPrimaryDivisonISO());
        }};
        if (specimen.getPrimaryDivisonISO() != null) {
            paramMap.put("isocode3166_alpha_2", stringTrimOrJSONNull(specimen.getPrimaryDivisonISO()));
        }

        return this.resolveOrCreateInteractive(searchString, "gazetteer", "gazetteer__all_fields", new JSONObject(paramMap), null);
    }

    private Object stringTrimOrJSONNull(String val) {
        if (val == null) {
            return JSONObject.NULL;
        }
        return val.trim();
    }

    /**
     * Find a typusstatus in Nahima
     *
     * @param status the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveTypeStatus(String status) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return this.resolveOrCreateInteractive(status, "typusstatus", "typusstatus_all_fields_1", new JSONObject(new HashMap<>() {{
            put("name", status.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null, 0);
    }

    /**
     * Generic function to resolve unit for a certain field
     *
     * @param unit        the unit to find in Nahima
     * @param unitSubject the unit type
     */
    public JSONObject resolveUnitFor(String unit, String unitSubject) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return this.resolveOrCreateInteractive(unit, unitSubject, unitSubject + "_all_fields", new JSONObject(new HashMap<>() {{
            put("name", unit.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null);
    }

    /**
     * Find a typusstatus in Nahima
     *
     * @param nrType the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveOtherNrType(String nrType) throws IOException, InterruptedException {
        return this.resolveNameBemerkungObject(nrType.trim(), "id_typen", "id_typen_all_fields");
    }

    /**
     * Find the collection method in Nahima
     *
     * @param method the collection method to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveCollectionMethod(String method) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(method.trim(), "sammlungsmethoden", "sammlungsmethoden__all_fields");
    }

    /**
     * Find a unit in Nahima
     *
     * @param unit the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveUnitForHeight(String unit) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveUnitFor(unit.trim(), "einheitenfuerhoehe");
    }

    /**
     * Find a unit in Nahima
     *
     * @param unit the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveUnitForErrorRadius(String unit) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveUnitFor(unit.trim(), "einheitenfuerdenfehlerradius");
    }

    /**
     * Find a family in Nahima
     *
     * @param family the family to search for resp. create
     */
    public JSONObject resolveFamily(String family) throws IOException, InterruptedException {
        return resolveOrCreate(family.trim(), "familien", "familien_all_fields", new JSONObject(new HashMap<>() {{
            put("familielat", family);
        }}), zoologyPool);
    }

    /**
     * Find a subfamily in Nahima
     *
     * @param family the subfamily's name to resolve
     */
    public JSONObject resolveSubFamily(String family) throws IOException, InterruptedException {
        return resolveOrCreate(family.trim(), "unterfamilien", "unterfamilien_all_fields", new JSONObject(new HashMap<>() {{
            put("unterfamilielat", family);
        }}), zoologyPool);
    }

    /**
     * Find a specific epithet ("art") in Nahima
     *
     * @param specificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveSpecificEpithet(String specificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(specificEpithet, "art", "art_all_fields", new JSONObject(new HashMap<>() {{
            put("artlat", specificEpithet.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    /**
     * Find a subspecific epithet in Nahima
     *
     * @param subspecificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveSubSpecificEpithet(String subspecificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(subspecificEpithet, "subspezifischeart", "subspezifischeart_all_fields", new JSONObject(new HashMap<>() {{
            put("subspezifischeart", subspecificEpithet.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    /**
     * Find an author in Nahima
     *
     * @param authorship the name to search for
     * @return the existing or new author
     */
    public JSONObject resolveAuthorship(String authorship) throws IOException, InterruptedException {
        return resolveOrCreate(authorship, "authors", "author__all_fields", new JSONObject(new HashMap<>() {{
            put("autor", authorship.trim());
            put("_nested:authors__personen", new JSONArray());
        }}), null);
    }

    /**
     * Find an infraspecific epithet in Nahima
     *
     * @param infraspecificEpithet the subfamily's name to resolve
     */
    public JSONObject resolveInfraspecificEpithet(String infraspecificEpithet) throws IOException, InterruptedException {
        return resolveOrCreate(infraspecificEpithet, "infraspezifischetaxon", "infraspezifischetaxon_all_fields", new JSONObject(new HashMap<>() {{
            put("infraspezifischetaxon", infraspecificEpithet.trim());
            put("abkuerzung", JSONObject.NULL);
            put("_nested:infraspezifischetaxon__trivialnamen", new JSONArray());
            put("_nested:infraspezifischetaxon__referenzenfuerintraspezifischestaxon", new JSONArray());
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    /**
     * Find (or create) a publication in Nahima
     *
     * @param citedInPublication
     * @param citedInPublicationLink
     * @param citedInPublicationYear
     * @param citedInPublicationComment
     * @return
     */
    public JSONObject resolvePublication(String citedInPublication, String citedInPublicationLink, String citedInPublicationYear, String citedInPublicationComment) throws IOException, InterruptedException {
        return resolveOrCreate(citedInPublication, "publikation", "publikation__public", new JSONObject(new HashMap<>() {{
            put("publikationsjahr", new JSONObject(new HashMap<>() {{
                put("value", (citedInPublicationYear != null && !citedInPublication.equals("")) ? citedInPublicationYear : "0000");
            }}));
            put("publikationstitel", new JSONObject(new HashMap<>() {{
                put("en-US", citedInPublication.trim());
            }}));
            put("publikationsreferenzzbdoi", citedInPublicationLink.trim());
            put("publikationskommentar", citedInPublicationComment.trim());
        }}), entomologyPool);
    }

    /**
     * Find an infraspecific rank in Nahima
     *
     * @param infraspecificRank the subfamily's name to resolve
     */
    public JSONObject resolveInfraspecificRank(String infraspecificRank) throws IOException, InterruptedException {
        return resolveNameBemerkungObject(infraspecificRank.trim(), "infraspezifischerrang", "id_typen_all_fields_1");
    }

    /**
     * Find a genus in Nahima
     *
     * @param genus the genus to search for or create (lat)
     */
    public JSONObject resolveGenus(String genus) throws IOException, InterruptedException {
        return resolveOrCreate(genus.trim(), "genus", "genus_all_fields", new JSONObject(new HashMap<>() {{
            put("genuslat", genus);
            put("_nested:genus__trivialnamen", new JSONArray());
            put("_nested:genus__referenzenfuergenus", new JSONArray());
            put("abkuerzung", JSONObject.NULL);
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    /**
     * Find or create a sex in Nahima
     */
    public JSONObject resolveSex(String sex) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(sex.trim(), "geschlechter", "geschlechter_all_fields_1", new JSONObject(new HashMap<>() {{
            put("name", sex);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null, 0);
    }

    /**
     * Find or create a life stage in Nahima
     */
    public JSONObject resolveLifeStage(String lifeStage) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(lifeStage.trim(), "lebensabschnitte", "lebensabschnitte_all_fields", new JSONObject(new HashMap<>() {{
            put("name", lifeStage);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null, 0);
    }

    /**
     *
     */
    public JSONObject resolveTaxon(String associatedTaxon) throws IOException, InterruptedException {
        return resolveOrCreate(associatedTaxon, "taxonnamen", "taxonnamen__all_fields", new JSONObject(new HashMap<>() {{
            put("dddtaxonnamelat", associatedTaxon.trim());
            put("taxonnamelat", new JSONObject(new HashMap<>() {{
                put("scientificName", associatedTaxon.trim());
                put("redList", false); // TODO: use the redlist api to check
                put("unclear", true);
                put("_standard", new JSONObject(new HashMap<>() {{
                    put("text", associatedTaxon.trim());
                }}));
            }}));
            put("_nested:taxonnamen__trivialnamen", new JSONArray());
            put("_nested:taxonnamen__referenzenfuertribus", new JSONArray());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    /**
     *
     */
    public JSONObject resolveTribe(String tribe) throws IOException, InterruptedException {
        return resolveOrCreate(tribe, "tribus", "tribus_all_fields", new JSONObject(new HashMap<>() {{
            put("tribuslat", tribe.trim());
            put("_nested:tribus__trivialnamen", new JSONArray());
            put("_nested:tribus__referenzenfuertribus", new JSONArray());
            put("abkuerzung", JSONObject.NULL);
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}), zoologyPool);
    }

    public JSONObject resolveOrder(String higherOrder) throws IOException, InterruptedException {
        return resolveOrCreate(higherOrder, "ordnungen", "ordnungen_all_fields", new JSONObject(new HashMap<>() {{
            put("ordnunglat", higherOrder.trim());
        }}), zoologyPool);
    }

    public JSONObject resolveCollectionDateIndicator(String indicator) throws IOException, InterruptedException {
        return resolveOrCreate(indicator, "indikatorfuersammeldatum", "indikatorfuersammeldatum_all_fields", new JSONObject(new HashMap<>() {{
            put("name", indicator.trim());
        }}), null);
    }

    /**
     * Find or create the location "Datum" in Nahima
     *
     * @param format the datum to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveDatumFormat(String format) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(format, "datumsformate", "datumsformate", new JSONObject(new HashMap<>() {{
            put("name", format.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null);
    }

    /**
     * Find or create a preparation part in Nahima
     */
    public JSONObject resolvePreparationPart(String partName) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(partName, "praeparatteile", "praeparatteile_all_fields", new JSONObject(new HashMap<>() {{
            put("name", partName.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null, 0);
    }

    /**
     * Find or create a preparation part attribute in Nahima
     */
    public JSONObject resolvePreparationPartAttribute(String attributeName, String attributeValue) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        if (attributeName == null && attributeValue == null) {
            return new JSONObject();
        }
        return resolveOrCreateInteractive(NullHandlingUtility.joinNonNull(" ", attributeName, attributeValue), "probenteilattribute", "probenteilattribute_all_fields", new JSONObject(new HashMap<>() {{
            put("attribute", attributeName.trim());
            put("einheit", attributeValue == null ? JSONObject.NULL : attributeValue.trim());
        }}), null, 0);
    }

    /**
     * Find or create a preparation part in Nahima
     */
    public JSONObject resolveMountingMethod(String method) throws IOException, InterruptedException, SkipSpecimenException, InvocationTargetException {
        return resolveOrCreateInteractive(method, "montierungsmethoden", "montierungsmethoden_all_fields", new JSONObject(new HashMap<>() {{
            put("name", method.trim());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), null, 0);
    }

    /**
     * This function translates a full-hydrated EasyDB object into an object that can be used to reference the
     * fully-hydrated object when creating another entity
     *
     * @param associate the object from easyDB to reduce
     * @return the reduced associate, ideal to reference on creation
     */
    public JSONObject reduceAssociateForAssociation(JSONObject associate) {
        return reduceAssociateForAssociation(associate, null);
    }

    public JSONObject reduceAssociateForAssociation(JSONObject associate, String objectType) {
        if (associate == null || associate.isEmpty()) {
            return null;
        }
        JSONObject reduced = new JSONObject();
        boolean isWrapped = false;
        String wrapperKey = "";
        if (associate.keySet().size() == 1) {
            isWrapped = true;
            wrapperKey = (String) associate.keySet().toArray()[0];
            JSONObject newAssociate = associate.optJSONObject(wrapperKey);
            if (newAssociate == null) {
                // e.g. if the JSON Object is just {value: "value"}, e.g. for dates
                return associate;
            } else {
                associate = newAssociate;
            }
        }
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

        int childId;
        if (associate.has("_id")) {
            childId = associate.getInt("_id");
        } else if (associate.has("_objecttype")) {
            childId = (associate.getJSONObject(associate.getString("_objecttype"))).getInt("_id");
        } else if (isWrapped && associate.has(wrapperKey)) {
            childId = associate.getJSONObject(wrapperKey).getInt("_id");
        } else {
            log.error("Failed to resolve _id in object: {}", associate);
            throw new IllegalStateException("Failed to resolve _id in object");
        }
        child.put("_id", childId);
        if (associate.has("_objecttype")) {
            reduced.put(associate.getString("_objecttype"), child);
        } else if (isWrapped) {
            reduced.put(wrapperKey, child);
        } else {
            assert (objectType != null);
            reduced.put(objectType, child);
        }
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

    public JSONObject wrapForCreation(JSONObject inner, JSONObject existing, String objectType, String mask, JSONObject pool) {
        inner = this.addDefaultValuesForCreation(inner, pool, existing);
        JSONObject result = new JSONObject(new HashMap<>() {{
            put("_mask", mask);
            put("_objecttype", objectType);
            put("_idx_in_objects", 1);
        }});
        result.put(objectType, inner);
        return result;
    }

    public JSONObject wrapForCreation(JSONObject inner, String objectType, String mask, JSONObject pool) {
        return wrapForCreation(inner, null, objectType, mask, pool);
    }

    /**
     * Add fields with values that are required when creating a new entry in Nahima
     *
     * @param inner the object to add the fields to
     * @return the object with the adjusted/added fields
     */
    public JSONObject addDefaultValuesForCreation(JSONObject inner, JSONObject pool) {
        return addDefaultValuesForCreation(inner, pool);
    }

    /**
     * Add default values, such as _id and _version, for creation
     *
     * @param inner the object to add the
     * @param pool  what to add as the property _pool
     * @return the object with the new values
     */
    public JSONObject addDefaultValuesForCreation(JSONObject inner, JSONObject pool, JSONObject existing) {
        if (pool != null && !pool.equals(JSONObject.NULL)) {
            inner.put("_pool", pool);
        }
        inner.put("_id", existing == null ? JSONObject.NULL : resolveId(existing));
        inner.put("_version", existing == null ? 1 : existing.getInt("_version") + 1);
        return inner;
    }


    /**
     * Wrap a value in a JSON object indicating the language (en-US) of the value
     *
     * @param value the JSON Object
     */
    public JSONObject wrapInLan(Object value) {
        return wrapInLan(value, "en-US");
    }

    /**
     * Wrap a value in a JSON object indicating the language of the value
     *
     * @param value the JSON Object
     */
    public JSONObject wrapInLan(Object value, String language) {
        assert language != null;
        JSONObject returnValue = new JSONObject();
        returnValue.put(language, value);
        return returnValue;
    }

    public String getCreatedByThisSoftwareIndication() {
        return "Created by " + ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion();
    }

    public JSONObject findTagWithName(String name) throws IOException {
        JSONObject tagFromCache = this.loadFromCache(name, "_tag");
        if (tagFromCache != null) {
            return tagFromCache;
        }

        String queryUrl = this.url + "tags?token=" + this.token;

        String response = this.getRequest(queryUrl);
        if (!response.startsWith("[")) {
            JSONObject responseObject = new JSONObject(response);
            if (responseObject.has("code")) {
                if (((String) responseObject.get("code")).startsWith("error")) {
                    throw new RuntimeException("Failed to fetch tags. Error code: " + responseObject.get("code") + ". " + (responseObject.has("description") ? responseObject.getString("description") : ""));
                }
            }
        }
        JSONObject responseObject = (new JSONArray(response)).getJSONObject(0);
        JSONArray tagArray = responseObject.getJSONArray("_tags");
        for (int tagIdx = 0; tagIdx < tagArray.length(); ++tagIdx) {
            JSONObject tag = tagArray.getJSONObject(tagIdx).getJSONObject("tag");
            JSONObject tag_names = tag.getJSONObject("displayname");
            Iterator<String> keys = tag_names.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String tag_name = tag_names.getString(key);
                if (Objects.equals(tag_name.toLowerCase(), name.toLowerCase())) {
                    storeInCache(name, "_tag", tag);
                    return tag;
                }
            }
        }
        throw new RuntimeException("Could not find tag with name " + name);
    }

    public JSONArray resolveWorkflowStatusTags(String workFlowStatus) throws IOException {
        if (Objects.equals(workFlowStatus, WorkFlowStatus.STAGE_CLEAN) || Objects.equals(workFlowStatus, WorkFlowStatus.STAGE_DONE)) {
            return new JSONArray();
        }

        JSONObject tag = findTagWithName("Entwurf");
        if (tag != null) {
            return new JSONArray() {{
                put(new JSONObject() {{
                    put("_id", tag.getInt("_id"));
                }});
            }};
        } else {
            throw new RuntimeException("Cound not find Entwurf tag");
        }
    }
}
