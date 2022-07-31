package edu.harvard.mcz.imagecapture.data;

import com.github.mizosoft.methanol.MediaType;
import com.github.mizosoft.methanol.MultipartBodyPublisher;
import com.github.mizosoft.methanol.MutableRequest;
import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
import edu.harvard.mcz.imagecapture.ui.dialog.ChooseFromJArrayDialog;
import edu.harvard.mcz.imagecapture.ui.dialog.VerifyJSONDialog;
import edu.harvard.mcz.imagecapture.utility.AbstractRestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
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
     * Somehow, the ids of objects are done inconsistently in Nahima.
     * Instead of finding out why/how, this function just goes through the possibilites
     *
     * @param objWithId the object whose id is searched
     * @return the object id, null if not found
     */
    protected static Integer resolveId(JSONObject objWithId) {
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
    public ArrayList<JSONObject> uploadImagesForSpecimen(Specimen specimen) throws IOException, InterruptedException, RuntimeException {
        // docs:
        // https://docs.easydb.de/en/technical/api/eas/
        // https://docs.easydb.de/en/sysadmin/eas/api/put/
        String baseQueryUrl = this.url + "eas/put?token=" + this.token;
        ArrayList<JSONObject> results = new ArrayList<>();

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
            log.info("Got != 1 " + objectType + " status. {}", results);
            if (selectionHelper != null) {
                ArrayList<Integer> possiblyCorrectIndices = new ArrayList<Integer>();
                for (int i = 0; i < foundObjects.length(); ++i) {
                    JSONObject testObj = foundObjects.getJSONObject(i);
                    int matches = 0;
                    int requiredMatches = 0;
                    for (Iterator<String> it = selectionHelper.keys(); it.hasNext(); ) {
                        String key = it.next();
                        requiredMatches += 1;
                        if (selectionHelper.get(key).equals(testObj.has(key) ? testObj.get(key) : null)) {
                            matches += 1;
                        }
                    }
                    if (matches == requiredMatches) {
                        possiblyCorrectIndices.add(i);
                    }
                }

                if (possiblyCorrectIndices.size() == 1) {
                    return foundObjects.getJSONObject(possiblyCorrectIndices.get(0));
                } else if (possiblyCorrectIndices.size() > 1) {
                    log.warn("Found multiple " + objectType + " that match all fields. Using first one.");
                    return foundObjects.getJSONObject(possiblyCorrectIndices.get(0));
                }
            }
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

        JSONObject results = this.resolveStringSearchToOne(name, objectType, false, inner);
        // TODO: select correct
        if (results == null) {
            // create
            JSONObject toCreate = wrapForCreation(inner, objectType, mask, omitPool);
            this.createObjectInNahima(toCreate, objectType);
            // TODO: it would be simpler to store the created in cache. But well... current format does not allow
            return this.resolveStringSearchToOne(name, objectType, true, inner);
        }
        return results;
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
     * @param omitPool   whether to add a pool field when creating the object new
     * @return the matching or new object
     */
    public JSONObject resolveOrCreateInteractive(String name, String objectType, String mask, JSONObject inner, boolean omitPool, int recurse) throws IOException, InterruptedException, SkipSpecimenException {
        if (name == null || objectType == null) {
            log.warn("Cannot search as search " + name + " or objectType " + objectType + " is null");
            return null;
        }

        JSONObject results = this.searchByString(name, objectType, false);

        if (results == null || (results.has("code") && results.getString("code").startsWith("error")) || !results.has("objects")) {
            throw new RuntimeException("Failed to resolve search for '" + name + "' (" + objectType + ", " + mask + "). Error code: " +
                    ((results != null && results.has("code")) ? results.getString("code") : "Unknown"));
        }

        JSONArray foundObjects = (JSONArray) results.get("objects");
        if (foundObjects.length() == 1) {
            // TODO: check compliance, does it really match well? We use "must", so let's hope so, but the resolution could still go wrong.
            return (JSONObject) foundObjects.get(0);
        } else {
            log.info("Got != 1 " + objectType + " status. {}", results);
            // create / select correct
            if (foundObjects.length() > 1) {
                return this.askToChooseObject(foundObjects, name, objectType, mask, inner, omitPool);
            } else {
                assert (foundObjects.length() == 0);
                askToCreate(inner, name, objectType, mask, omitPool);
                // TODO: the following could fail if the search and created object do not really align
                return this.resolveStringSearchToOne(name, objectType, true);
            }
        }
        // return null;
    }

    public JSONObject resolveOrCreateInteractive(String name, String objectType, String mask, JSONObject inner) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(name, objectType, mask, inner, false, 0);
    }

    public JSONObject askToCreate(JSONObject inner, String name, String objectType, String mask, boolean omitPool) throws IOException, InterruptedException, SkipSpecimenException {
        // ask whether to create the object like this
        VerifyJSONDialog dialog = new VerifyJSONDialog(Singleton.getSingletonInstance().getMainFrame(), inner.toString(), name);
        int result = dialog.getReturnDecision();
        switch (result) {
            case VerifyJSONDialog.RETURN_ACCEPT:
                inner = new JSONObject(dialog.getResultingJSON());
            case VerifyJSONDialog.RETURN_CHANGE_SEARCH:
                return askToChangeSearch(name, objectType, inner, mask, omitPool);
            case VerifyJSONDialog.RETURN_SKIP:
                throw new SkipSpecimenException();
        }
        JSONObject toCreate = wrapForCreation(inner, objectType, mask, omitPool);
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
     * @param omitPool     whether to omit the pool when wrapping the object for creation
     */
    public JSONObject askToChooseObject(JSONArray foundObjects, String name, String objectType, String mask, JSONObject inner, boolean omitPool) throws SkipSpecimenException, IOException, InterruptedException {
        ChooseFromJArrayDialog dialog = new ChooseFromJArrayDialog(Singleton.getSingletonInstance().getMainFrame(), foundObjects, objectType, name);
        int result = dialog.getReturnDecision();
        switch (result) {
            case ChooseFromJArrayDialog.RETURN_ACCEPT:
                return dialog.getSelectedItem();
            case ChooseFromJArrayDialog.RETURN_SKIP:
                throw new SkipSpecimenException();
            case ChooseFromJArrayDialog.RETURN_CREATE_NEW:
                return askToCreate(inner, name, objectType, mask, omitPool);
            case ChooseFromJArrayDialog.RETURN_CHANGE_SEARCH:
                return askToChangeSearch(name, objectType, inner, mask, omitPool);
            default:
                throw new RuntimeException("Undefined result type.");
        }
    }

    /**
     * Opens a dialog to ask the user for a different search string
     *
     * @param name       the current search
     * @param objectType the type of object to search for
     * @param inner      the object to use to create anew if applicable
     * @param mask       the mask to use when creating anew
     * @param omitPool   whether to omit the pool when wrapping the object for creation
     */
    private JSONObject askToChangeSearch(String name, String objectType, JSONObject inner, String mask, boolean omitPool) throws IOException, InterruptedException, SkipSpecimenException {
        String newSearch = (String) JOptionPane.showInputDialog(Singleton.getSingletonInstance().getMainFrame(), "Search for " + objectType, "Change Search", JOptionPane.QUESTION_MESSAGE, null, null, name);
        return resolveOrCreateInteractive(newSearch, objectType, mask, inner, omitPool, 1);
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
    public JSONObject resolvePerson(String personName) throws IOException, InterruptedException, SkipSpecimenException {
        if (personName == null) {
            return null;
        }
        String[] splitName = personName.split(" ");
        JSONObject results = this.resolveOrCreateInteractive(personName, "person", "person__public", new JSONObject(new HashMap<>() {{
            put("vollername", personName);
            put("vorname", splitName[0]);
            put("nachname", splitName.length > 1 ? splitName[1] : JSONObject.NULL);
            put("mitlerername", JSONObject.NULL);
            put("nismpersonid", JSONObject.NULL);
        }}), true, 0);
        return results;
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
    public JSONObject resolveLocation(Specimen specimen) throws IOException, InterruptedException, SkipSpecimenException, NullPointerException {
        String searchString = specimen.getPrimaryDivisonISO() != null ? specimen.getPrimaryDivisonISO() : String.join(" ", specimen.getPrimaryDivison(), specimen.getCountry());

        JSONObject parent = null;
        try {
            parent = resolveCountry(specimen.getCountry());
        } catch (Exception e) {
        }
        ;
        JSONObject finalParent = parent;
        return this.resolveOrCreateInteractive(searchString, "gazetteer", "gazetteer_all_fields", new JSONObject(new HashMap<>() {{
            put("ortsname", wrapInLan(specimen.getPrimaryDivison()));
            put("_id_parent", resolveId(finalParent)); // Might throw NullPointerException. If we find the location, it is never called, therefore, we only have a problem if neither location nor country are found
            put("isocode3166_2", specimen.getPrimaryDivisonISO());
        }}));
    }

    /**
     * Find a typusstatus in Nahima
     *
     * @param status the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveTypeStatus(String status) throws IOException, InterruptedException, SkipSpecimenException {
        JSONObject result = this.resolveOrCreateInteractive(status, "typusstatus", "typusstatus_all_fields_1", new JSONObject(new HashMap<>() {{
            put("name", status);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), false, 0);
        return result;
    }

    /**
     * Generic function to resolve unit for a certain field
     *
     * @param unit        the unit to find in Nahima
     * @param unitSubject the unit type
     */
    public JSONObject resolveUnitFor(String unit, String unitSubject) throws IOException, InterruptedException, SkipSpecimenException {
        JSONObject results = this.resolveOrCreateInteractive(unit, unitSubject, unitSubject + "_all_fields", new JSONObject(new HashMap<>() {{
            put("name", unit);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
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
    public JSONObject resolveUnitForHeight(String unit) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveUnitFor(unit, "einheitenfuerhoehe");
    }

    /**
     * Find a unit in Nahima
     *
     * @param unit the search string
     * @return the nahima returned object if only one
     */
    public JSONObject resolveUnitForErrorRadius(String unit) throws IOException, InterruptedException, SkipSpecimenException {
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
     * Find a genus in Nahima
     *
     * @param genus the genus to search for or create (lat)
     */
    public JSONObject resolveGenus(String genus) throws IOException, InterruptedException {
        return resolveOrCreate(genus, "genus", "genus_all_fields", new JSONObject(new HashMap<>() {{
            put("genuslat", genus);
            put("_nested:genus__trivialnamen", new JSONArray());
            put("_nested:genus__referenzenfuergenus", new JSONArray());
            put("abkuerzung", JSONObject.NULL);
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find or create a sex in Nahima
     */
    public JSONObject resolveSex(String sex) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(sex, "geschlechter", "geschlechter_all_fields_1", new JSONObject(new HashMap<>() {{
            put("name", sex);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), true, 0);
    }

    /**
     * Find or create a life stage in Nahima
     */
    public JSONObject resolveLifeStage(String lifeStage) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(lifeStage, "lebensabschnitte", "lebensabschnitte_all_fields", new JSONObject(new HashMap<>() {{
            put("name", lifeStage);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), true, 0);
    }

    /**
     *
     */
    public JSONObject resolveAssociatedTaxon(String associatedTaxon) throws IOException, InterruptedException {
        return resolveOrCreate(associatedTaxon, "taxonnamen", "taxonnamen__all_fields", new JSONObject(new HashMap<>() {{
            put("dddtaxonnamelat", associatedTaxon);
            put("_nested:taxonnamen__trivialnamen", new JSONArray());
            put("_nested:taxonnamen__referenzenfuertribus", new JSONArray());
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     *
     */
    public JSONObject resolveTribe(String tribe) throws IOException, InterruptedException {
        return resolveOrCreate(tribe, "tribus", "tribus_all_fields", new JSONObject(new HashMap<>() {{
            put("tribuslat", tribe);
            put("_nested:tribus__trivialnamen", new JSONArray());
            put("_nested:tribus__referenzenfuertribus", new JSONArray());
            put("abkuerzung", JSONObject.NULL);
//            put("beschreibung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find or create the location "Datum" in Nahima
     *
     * @param format the datum to search for
     * @return the nahima returned object if only one
     */
    public JSONObject resolveDatumFormat(String format) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(format, "datumsformate", "datumsformate", new JSONObject(new HashMap<>() {{
            put("name", format);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}));
    }

    /**
     * Find or create a preparation part in Nahima
     */
    public JSONObject resolvePreparationPart(String partName) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(partName, "praeparatteile", "praeparatteile_all_fields", new JSONObject(new HashMap<>() {{
            put("name", partName);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), true, 0);
    }

    /**
     * Find or create a preparation part attribute in Nahima
     */
    public JSONObject resolvePreparationPartAttribute(String attributeName, String attributeValue) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(attributeName + " " + attributeValue, "probenteilattribute", "probenteilattribute_all_fields", new JSONObject(new HashMap<>() {{
            put("attribute", attributeName);
            put("einheit", attributeValue);
        }}), true, 0);
    }

    /**
     * Find or create a preparation part in Nahima
     */
    public JSONObject resolveMountingMethod(String method) throws IOException, InterruptedException, SkipSpecimenException {
        return resolveOrCreateInteractive(method, "montierungsmethoden", "montierungsmethoden_all_fields", new JSONObject(new HashMap<>() {{
            put("name", method);
            put("bemerkung", getCreatedByThisSoftwareIndication());
        }}), true, 0);
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

    /**
     * Add default values, such as _id and _version, for creation
     *
     * @param inner    the object to add the
     * @param omitPool whether to *not* add the property _pool
     * @return the object with the new values
     */
    public JSONObject addDefaultValuesForCreation(JSONObject inner, boolean omitPool) {
        if (!omitPool) {
            inner.put("_pool", defaultPool);
        }
        inner.put("_id", JSONObject.NULL);
        inner.put("_version", 1);
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
}
