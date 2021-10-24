package edu.harvard.mcz.imagecapture.serializer.nahima;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.*;
import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
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
    public JSONObject serialize2JSON(Object target) throws SkipSpecimenException {
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
                put("arttrans", det.getSpecificEpithet());
                put("autortrans", det.getAuthorship());
                put("bestimmertrans", det.getIdentifiedBy());
                put("bestimmungsdatumtrans", det.getDateIdentified());
                put("familietrans", toSerialize.getFamily());
                put("genustrans", det.getGenus());
                put("infrapezifischerrangtrans", det.getInfraspecificRank());
                put("infraspezifischestaxontrans", det.getInfraspecificEpithet());
                put("subspezifischesarttrans", det.getSubspecificEpithet());
                put("taxonnametrans", toSerialize.getAssociatedTaxon());
                put("typusid", det.getTypeStatus());
                put("typusstatustrans", det.getTypeStatus());
                put("unterfamilietrans", toSerialize.getSubfamily());
            }};

            JSONObject reverseNestedDetermination = new JSONObject(reverseNestedCollectionMap);
            reverseNestedDetermination = nahimaManager.addDefaultValuesForCreation(reverseNestedDetermination);

            // resolutions
            tryNonSkippableResolve(reverseNestedDetermination, "art", () -> nahimaManager.resolveSpecificEpithet(det.getSpecificEpithet()));
            tryNonSkippableResolve(reverseNestedDetermination, "autor", () -> nahimaManager.resolveAuthorship(det.getAuthorship()));
            tryNonSkippableResolve(reverseNestedDetermination, "familie", () -> nahimaManager.resolveFamily(toSerialize.getFamily()));
            tryNonSkippableResolve(reverseNestedDetermination, "genus", () -> nahimaManager.resolveGenus(det.getGenus()));
            tryNonSkippableResolve(reverseNestedDetermination, "infraspezifischerrang", () -> nahimaManager.resolveInfraspecificRank(det.getInfraspecificRank()));
            tryNonSkippableResolve(reverseNestedDetermination, "infraspezifischestaxon", () -> nahimaManager.resolveInfraspecificEpithet(det.getInfraspecificEpithet()));
            tryNonSkippableResolve(reverseNestedDetermination, "subspezifischeart", () -> nahimaManager.resolveSubSpecificEpithet(det.getSubspecificEpithet()));
            tryNonSkippableResolve(reverseNestedDetermination, "taxonname", () -> nahimaManager.resolveAssociatedTaxon(toSerialize.getAssociatedTaxon()));
            tryNonSkippableResolve(reverseNestedDetermination, "unterfamilie", () -> nahimaManager.resolveSubFamily(toSerialize.getSubfamily()));
            tryNonSkippableResolve(reverseNestedDetermination, "tribus", () -> nahimaManager.resolveTribe(toSerialize.getTribe()));

            reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));
            // try to parse and set the date correctly
            tryNonSkippableResolve(reverseNestedDetermination, "bestimmungsdatum", () -> this.dateToNahima(det.getDateIdentified()));

            JSONArray identifierPersons = new JSONArray();
            trySkippableResolve(identifierPersons, () -> nahimaManager.resolvePerson(det.getIdentifiedBy()), "person");
            reverseNestedDetermination.put("_nested:bestimmung__bestimmer_person", identifierPersons);


            try {
                trySkippableResolve(reverseNestedDetermination, "typusstatus", () -> nahimaManager.resolveTypeStatus(det.getTypeStatus()));
            } catch (SkipSpecimenException e) {
                return null;
            }
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
            put("sammlungtrans", toSerialize.getCollection());
            put("sammlungstitel", toSerialize.getCollection());
            put("kommentare", toSerialize.getSpecimenNotes());
            put("habitattrans", toSerialize.getHabitat());
            put("sammelorttrans", toSerialize.getVerbatimLocality());
            put("lokalitaet", toSerialize.getSpecificLocality());
            put("neuhabitat", nahimaManager.wrapInLan(toSerialize.getHabitat()));
            put("mikrohabitat", nahimaManager.wrapInLan(toSerialize.getMicrohabitat()));
            put("traegerorganismustrans", toSerialize.getAssociatedTaxon());
            put("traegerorganismus", nahimaManager.wrapInLan(toSerialize.getAssociatedTaxon()));
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

        trySkippableResolve(reverseNestedCollection, "sammelort", () -> nahimaManager.resolveLocation(toSerialize));
        trySkippableResolve(reverseNestedCollection, "einheitderhoehe", () -> nahimaManager.resolveUnitForHeight(toSerialize.getElev_units()));


        if (georef != null) {
            trySkippableResolve(reverseNestedCollection, "einheitdesfehlerradius", () ->
                    nahimaManager.resolveUnitForErrorRadius(georef.getMaxErrorUnits()));
            trySkippableResolve(reverseNestedCollection, "datumsformatgeodaeischeskooordinatensystem", () -> nahimaManager.resolveDatumFormat(georef.getDatum()));
        }

        tryNonSkippableResolve(reverseNestedCollection, "_nested:aufsammlung__sammelmethoden", () -> new JSONArray(new JSONObject[]{
                new JSONObject(new HashMap<>() {{
                    put("sammlungsmethode", nahimaManager.resolveCollectionMethod(toSerialize.getCollectingMethod()));
                }})
        }));

        JSONArray collectors = new JSONArray();
        for (Collector collector : toSerialize.getCollectors()) {
            trySkippableResolve(collectors, () -> new JSONObject(new HashMap<>() {{
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
        tryNonSkippableResolve(reverseNestedCollection, "sammeldatum", () -> dateToNahima(toSerialize.getDateCollected()));

        reverseNestedCollections.put(reverseNestedCollection);
        result.put("_reverse_nested:aufsammlung:entomologie", reverseNestedCollections);

        JSONArray parts = new JSONArray();
        for (SpecimenPart part : toSerialize.getSpecimenParts()) {
            JSONObject partJson = new JSONObject(new HashMap<>() {{
                put("anzahlpraeparatteile", part.getLotCount());
            }});
            trySkippableResolve(partJson, "praeparatteil", () -> nahimaManager.resolvePreparationPart(part.getPartName()));
            trySkippableResolve(partJson, "montierungsmethode", () -> nahimaManager.resolveMountingMethod(part.getPreserveMethod()));
            JSONArray attributes = new JSONArray();
            for (SpecimenPartAttribute attribute : part.getSpecimenPartAttributes()) {
                trySkippableResolve(attributes, () -> nahimaManager.resolvePreparationPartAttribute(String.join(" ", attribute.getAttributeRemark(), attribute.getAttributeType(), attribute.getAttributeValue(), nahimaDateFormat.format(attribute.getAttributeDate()), attribute.getAttributeDeterminer()), attribute.getAttributeUnits()), "specimenAttribute");
            }
            partJson.put("attribute", attributes);
            parts.put(partJson);
        }
        result.put("_nested:entomologie__praeparatteile", parts);

        trySkippableResolve(reverseNestedCollection, "geschlecht", () -> nahimaManager.resolveSex(toSerialize.getSex()));
        trySkippableResolve(reverseNestedCollection, "lebensabschnitt", () -> nahimaManager.resolveLifeStage(toSerialize.getLifeStage()));

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

    protected void tryNonSkippableResolve(JSONObject target, String key, ResolverMethodInterface resolver) {
        // try to parse and set correctly
        try {
            trySkippableResolve(target, key, resolver);
        } catch (Exception e) {
            log.error("Failed to resolve " + key, e);
        }
    }

    protected void tryNonSkippableResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint) {
        // try to parse and set correctly
        try {
            trySkippableResolve(target, resolver, debugHint);
        } catch (Exception e) {
            log.error("Failed to resolve " + debugHint, e);
        }
    }

    protected void trySkippableResolve(JSONObject target, String key, ResolverMethodInterface resolver) throws SkipSpecimenException {
        try {
            Object returnValue = resolver.doResolve();
            if (returnValue != null) {
                if (returnValue instanceof JSONObject) {
                    target.put(key, nahimaManager.reduceAssociateForAssociation((JSONObject) returnValue));
                } else {
                    target.put(key, returnValue);
                }
            }
        } catch (IOException | InterruptedException | ParseException e) {
            log.error("Failed to resolve " + key, e);
        }
    }

    protected void trySkippableResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint) throws SkipSpecimenException {
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
        } catch (IOException | InterruptedException | ParseException e) {
            log.error("Failed to resolve " + debugHint, e);
        }
    }

    @Override
    public boolean supportsSerializationOf(Object target) {
        return target instanceof Specimen;
    }

    protected interface ResolverMethodInterface {
        Object doResolve() throws IOException, InterruptedException, ParseException, SkipSpecimenException;
    }

    protected interface JSONObjectResolverMethodInterface extends ResolverMethodInterface {
        JSONObject doResolve() throws IOException, InterruptedException, ParseException, SkipSpecimenException;
    }

    protected interface JSONArrayResolverMethodInterface extends ResolverMethodInterface {
        JSONArray doResolve() throws IOException, InterruptedException, ParseException, SkipSpecimenException;
    }
}
