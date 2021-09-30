package edu.harvard.mcz.imagecapture.serializer.nahima;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.*;
import edu.harvard.mcz.imagecapture.serializer.ToJSONSerializerInterface;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Specimen2JSONSerializer implements ToJSONSerializerInterface {
    private static final Logger log = LoggerFactory.getLogger(Specimen2JSONSerializer.class);
    private final SimpleDateFormat nahimaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final NahimaManager nahimaManager;

    public Specimen2JSONSerializer(NahimaManager manager) {
        this.nahimaManager = manager;
    }

    @Override
    public JSONObject serialize2JSON(Object target) {
        assert target instanceof Specimen;
        JSONObject result = new JSONObject();
        Specimen toSerialize = (Specimen) target;
        // easy, first level fields
        result.put("barcodeqrcode", toSerialize.getBarcode());
        result.put("erschliessungsfragen", toSerialize.getQuestions());
        result.put("folgerung", toSerialize.getInferences());
        // here, it already starts to get complicated, fuu.
        // first, other ids, incl. specimen ID
        JSONArray otherIds = new JSONArray();
        for (Number otherNr : toSerialize.getNumbers()) {
            Map<String, Object> otherNrMap = new HashMap<>() {{
                put("andereid", otherNr.getNumber());
                put("bemerkung", String.join(" ", otherNr.getNumber(), String.valueOf(otherNr.getNumberId()), otherNr.getNumberType()));
            }};
            try {
                otherNrMap.put("typ", nahimaManager.resolveOtherNrType(otherNr.getNumberType()));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve nr type", e);
            }
            otherIds.put(new JSONObject(otherNrMap));
        }
        // also add specimen id as other id
        JSONObject dataShotId = new JSONObject(new HashMap<String, Object>() {{
            put("andereid", String.valueOf(toSerialize.getSpecimenId()));
        }});
        try {
            dataShotId.put("typ", nahimaManager.resolveOtherNrType(ImageCaptureApp.APP_NAME + "-ID"));
        } catch (IOException | InterruptedException e) {
            log.error("Failed to resolve nr type", e);
        }
        otherIds.put(dataShotId);
        result.put("_nested:entomologie__andereids", otherIds);
        // set determinations
        JSONArray reverseNestedDeterminations = new JSONArray();
        // adding specimen's own det by using `getAllDeterminations` instead of `getDeterminations`
        for (Determination det : toSerialize.getAllDeterminations()) {
            Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
                put("typusid", det.getTypeStatus());
                put("typusstatustrans", det.getTypeStatus());
                put("autortrans", det.getAuthorship());
                put("taxonnametrans", toSerialize.getAssociatedTaxon()); // TODO: resolve
                put("familietrans", toSerialize.getFamily());
                put("unterfamilietrans", toSerialize.getSubfamily());
                put("genustrans", det.getGenus()); // TODO: resolve
                put("subspezifischesarttrans", det.getSubspecificEpithet());
                put("bestimmertrans", det.getIdentifiedBy());
                put("infraspezifischestaxontrans", det.getInfraspecificEpithet());
                put("infrapezifischerrangtrans", det.getInfraspecificRank());
                put("arttrans", det.getSpecificEpithet());
                put("bestimmungsdatumtrans", det.getDateIdentified());
            }};

            JSONObject reverseNestedDetermination = new JSONObject(reverseNestedCollectionMap);
            reverseNestedDetermination = nahimaManager.addDefaultValuesForCreation(reverseNestedDetermination);

            // resolutions
            tryNonInteractiveResolve(reverseNestedDetermination, "familie", () -> nahimaManager.resolveFamily(toSerialize.getFamily()));
            tryNonInteractiveResolve(reverseNestedDetermination, "unterfamilie", () -> nahimaManager.resolveSubFamily(toSerialize.getSubfamily()));
            tryNonInteractiveResolve(reverseNestedDetermination, "autor", () -> nahimaManager.resolveAuthorship(det.getAuthorship()));
            tryNonInteractiveResolve(reverseNestedDetermination, "art", () -> nahimaManager.resolveSpecificEpithet(det.getSpecificEpithet()));
            tryNonInteractiveResolve(reverseNestedDetermination, "subspezifischeart", () -> nahimaManager.resolveSubSpecificEpithet(det.getSubspecificEpithet()));
            tryNonInteractiveResolve(reverseNestedDetermination, "infraspezifischestaxon", () -> nahimaManager.resolveInfraspecificEpithet(det.getInfraspecificEpithet()));
            tryNonInteractiveResolve(reverseNestedDetermination, "infraspezifischerrang", () -> nahimaManager.resolveInfraspecificRank(det.getInfraspecificRank()));

            reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));
            // try to parse and set the date correctly
            tryNonInteractiveResolve(reverseNestedDetermination, "bestimmungsdatum", () -> this.dateToNahima(det.getDateIdentified()));

            JSONArray identifierPersons = new JSONArray();
            tryNonInteractiveResolve(identifierPersons, () -> nahimaManager.resolvePerson(det.getIdentifiedBy()), "person");
            reverseNestedDetermination.put("_nested:bestimmung__bestimmer_person", identifierPersons);

            tryNonInteractiveResolve(reverseNestedDetermination, "typusstatus", () -> nahimaManager.resolveTypeStatus(det.getTypeStatus()));
            // TODO: other fields

            // finally,
            reverseNestedDeterminations.put(reverseNestedDetermination);
        }


        result.put("_reverse_nested:bestimmung:entomologie", reverseNestedDeterminations);

        JSONArray reverseNestedCollections = new JSONArray();
        LatLong georef = toSerialize.getLatLong().isEmpty() ? null : (LatLong) toSerialize.getLatLong().toArray()[0];
        // some other basic reverse nested values for "aufsammlung" (collection)
        Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
            put("sammlertrans", toSerialize.getCollectors().stream().map(Collector::getCollectorName).collect(Collectors.joining(", ")));
            put("_nested:aufsammlung__indikatorenfuersammeldatum", new JSONArray(new String[]{toSerialize.getDateCollectedIndicator()}));
            put("sammlungstitel", toSerialize.getCollection());
            put("kommentare", toSerialize.getSpecimenNotes());
            put("habitattrans", toSerialize.getHabitat());
            put("sammelorttrans", toSerialize.getVerbatimLocality());
            put("lokalitaet", toSerialize.getSpecificLocality());
            put("neuhabitat", wrapInLan(toSerialize.getHabitat()));
            put("mikrohabitat", wrapInLan(toSerialize.getMicrohabitat()));
            put("traegerorganismustrans", toSerialize.getAssociatedTaxon());
            put("traegerorganismus", wrapInLan(toSerialize.getAssociatedTaxon()));
            put("breitengraddezimal", georef == null ? null : georef.getLatDeg());
            put("laengengraddezimal", georef == null ? null : georef.getLongDeg());
            put("fehlerradius", georef == null ? null : georef.getMaxErrorDistance());
            put("hoehemin", toSerialize.getMinimum_elevation());
            put("hoehemax", toSerialize.getMaximum_elevation());
            put("lokalitaettrans", toSerialize.getVerbatimLocality());
            put("__idx", 0);
            put("_version", 1);
            put("_id", JSONObject.NULL);
            put("_pool", NahimaManager.defaultPool);
        }};
        JSONObject reverseNestedCollection = new JSONObject(reverseNestedCollectionMap);

        tryNonInteractiveResolve(reverseNestedCollection, "sammelort", () -> nahimaManager.resolveLocation(String.join(" ", toSerialize.getCountry(), toSerialize.getPrimaryDivison(), toSerialize.getSpecificLocality())));
        tryNonInteractiveResolve(reverseNestedCollection, "einheitderhoehe", () -> nahimaManager.resolveUnitForHeight(toSerialize.getElev_units()));

        if (georef != null) {
            tryNonInteractiveResolve(reverseNestedCollection, "einheitdesfehlerradius", () ->
                    nahimaManager.resolveUnitForErrorRadius(georef.getMaxErrorUnits()));
            tryNonInteractiveResolve(reverseNestedCollection, "datumsformatgeodaeischeskooordinatensystem", () -> nahimaManager.resolveDatumFormat(georef.getDatum()));
        }

        tryNonInteractiveResolve(reverseNestedCollection, "_nested:aufsammlung__sammelmethoden", () -> new JSONArray(new JSONObject[]{
                new JSONObject(new HashMap<>() {{
                    put("sammlungsmethode", nahimaManager.resolveCollectionMethod(toSerialize.getCollectingMethod()));
                }})
        }));

        JSONArray collectors = new JSONArray();
        for (Collector collector : toSerialize.getCollectors()) {
            tryNonInteractiveResolve(collectors, () -> new JSONObject(new HashMap<>() {{
                put("sammler", nahimaManager.reduceAssociateForAssociation(
                        nahimaManager.resolvePerson(collector.getCollectorName()))
                );
            }}), "sammler");
        }
        reverseNestedCollection.put("_nested:aufsammlung__sammler", collectors);

        try {
            if (toSerialize.getDateNos().contains("-")) {
                String[] dateSplit = toSerialize.getDateNos().split("-");
                reverseNestedCollection.put("zeitraumtrans", toSerialize.getDateNos());
                reverseNestedCollection.put("zeitraum", dateRangeToNahima(dateSplit[0].trim(), dateSplit[1].trim()));
            } else {
                reverseNestedCollection.put("datumtrans", toSerialize.getDateNos());
                reverseNestedCollection.put("datum", this.dateToNahima(toSerialize.getDateEmerged()));
            }
        } catch (ParseException e) {
            log.error("Failed to parse datum " + toSerialize.getDateEmerged() + " " + toSerialize.getDateNos(), e);
        }
        reverseNestedCollection.put("indikator_fuer_kultur_zucht", toSerialize.getDateEmergedIndicator());
        reverseNestedCollection.put("kultur_zucht", !Objects.equals(toSerialize.getDateEmerged(), "") && toSerialize.getDateEmerged() != null);
        reverseNestedCollection.put("sammeldatumtrans", toSerialize.getDateCollected());
        tryNonInteractiveResolve(reverseNestedCollection, "sammeldatum", () -> dateToNahima(toSerialize.getDateCollected()));

        // TODO: other fields

        reverseNestedCollections.put(reverseNestedCollection);
        result.put("_reverse_nested:aufsammlung:entomologie", reverseNestedCollections);

        JSONArray parts = new JSONArray();
        for (SpecimenPart part : toSerialize.getSpecimenParts()) {
            JSONObject partJson = new JSONObject(new HashMap<>() {{
                put("anzahlpraeparatteile", part.getLotCount());
            }});
            // TODO: (resolve) other fields
            parts.put(partJson);
        }
        result.put("_nested:entomologie__praeparatteile", parts);

        // TODO: other fields

        // finally, wrap everything in the pool (might want to do somewhere else)
        JSONObject wrapper = nahimaManager.wrapForCreation(result, "entomologie", "entomologie_public_unrestricted");
        return wrapper;
    }

    protected JSONObject dateRangeToNahima(String from, String to) throws ParseException {
        Date parsedDateFrom = DateUtils.parseDate(from);
        Date parsedDateTo = DateUtils.parseDate(to);
        JSONObject returnValue = new JSONObject();
        returnValue.put("from", nahimaDateFormat.format(parsedDateFrom));
        returnValue.put("to", nahimaDateFormat.format(parsedDateTo));
        return returnValue;
    }

    protected JSONObject dateToNahima(String date) throws ParseException {
        log.debug("Parsing date from \"" + date + "\"");
        Date parsedDate = DateUtils.parseDate(date);
        return dateToNahima(parsedDate);
    }

    protected JSONObject dateToNahima(Date date) {
        JSONObject returnValue = new JSONObject();
        returnValue.put("value", nahimaDateFormat.format(date));
        return returnValue;
    }

    /**
     * Wrap a value in a JSON object indicating the language (en-US) of the value
     *
     * @param value the JSON Object
     */
    protected JSONObject wrapInLan(Object value) {
        return wrapInLan(value, "en-US");
    }

    /**
     * Wrap a value in a JSON object indicating the language of the value
     *
     * @param value the JSON Object
     */
    protected JSONObject wrapInLan(Object value, String language) {
        assert language != null;
        JSONObject returnValue = new JSONObject();
        returnValue.put(language, value);
        return returnValue;
    }

    protected void tryNonInteractiveResolve(JSONObject target, String key, ResolverMethodInterface resolver) {
        // try to parse and set correctly
        try {
            Object returnValue = resolver.doResolve();
            if (returnValue != null) {
                if (returnValue instanceof JSONObject) {
                    target.put(key, nahimaManager.reduceAssociateForAssociation((JSONObject) returnValue));
                } else {
                    target.put(key, returnValue);
                }
            }
        } catch (Exception e) {
            log.error("Failed to resolve " + key, e);
        }
    }

    protected void tryNonInteractiveResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint) {
        // try to parse and set correctly
        try {
            Object returnValue = resolver.doResolve();
            if (returnValue != null) {
                if (returnValue instanceof JSONObject) {
                    target.put(nahimaManager.reduceAssociateForAssociation((JSONObject) returnValue));
                } else {
                    target.put(returnValue);
                }
            }
        } catch (Exception e) {
            log.error("Failed to resolve " + debugHint, e);
        }
    }

    @Override
    public boolean supportsSerializationOf(Object target) {
        return target instanceof Specimen;
    }

    protected interface ResolverMethodInterface {
        Object doResolve() throws Exception;
    }

    protected interface JSONObjectResolverMethodInterface extends ResolverMethodInterface {
        JSONObject doResolve() throws Exception;
    }

    protected interface JSONArrayResolverMethodInterface extends ResolverMethodInterface {
        JSONArray doResolve() throws Exception;
    }
}
