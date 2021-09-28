package edu.harvard.mcz.imagecapture.serializer.nahima;

import edu.harvard.mcz.imagecapture.data.NahimaManager;
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
        // EasyDB specific fields
        result.put("_version", 1);
        result.put("_id", JSONObject.NULL);
        result.put("_pool", NahimaManager.defaultPool);
        // here, it already starts to get complicated, fuu.
        JSONArray reverseNestedDeterminations = new JSONArray();
        for (Determination det : toSerialize.getDeterminations()) {
            Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
                put("_version", 1);
                put("_pool", NahimaManager.defaultPool);
                put("_id", JSONObject.NULL);
                put("typusid", det.getTypeStatus());
                put("typusstatustrans", det.getTypeStatus());
                put("autortrans", det.getAuthorship()); // TODO: resolve
                put("taxonnametrans", toSerialize.getAssociatedTaxon()); // TODO: resolve
                put("familietrans", toSerialize.getFamily()); // TODO: resolve
                put("genustrans", det.getGenus()); // TODO: resolve
                put("subspezifischesarttrans", det.getSubspecificEpithet()); // TODO: resolve
                put("bestimmertrans", det.getIdentifiedBy());
                put("infraspezifischestaxontrans", det.getInfraspecificEpithet()); // TODO: resolve
                put("infrapezifischerrangtrans", det.getInfraspecificRank()); // TODO: resolve
                put("arttrans", det.getSpecificEpithet()); // TODO: resolve
            }};

            JSONObject reverseNestedDetermination = new JSONObject(reverseNestedCollectionMap);

            reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));
            reverseNestedDetermination.put("bestimmungsdatumtrans", det.getDateIdentified());
            // try to parse and set the date correctly
            try {
                reverseNestedDetermination.put("bestimmungsdatum", this.dateToNahima(det.getDateIdentified()));
            } catch (ParseException e) {
                log.error("Failed to parse bestimmungsdatum", e);
            }

            JSONArray identifierPersons = new JSONArray();
            try {
                JSONObject identifierPerson = nahimaManager.resolvePerson(det.getIdentifiedBy());
                if (identifierPerson != null) {
                    identifierPersons.put(nahimaManager.reduceAssociateForAssociation(identifierPerson));
                }
            } catch (IOException | InterruptedException e) {
                log.info("Failed to resolve person", e);
            }
            reverseNestedDetermination.put("_nested:bestimmung__bestimmer_person", identifierPersons);

            try {
                JSONObject resolvedTypeStatus = nahimaManager.resolveTypeStatus(det.getTypeStatus());
                reverseNestedDetermination.put("typusstatus", resolvedTypeStatus);
            } catch (IOException | InterruptedException e) {
                log.info("Failed to resolve type status", e);
            }
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

        try {
            reverseNestedCollection.put("sammelort", nahimaManager.reduceAssociateForAssociation(nahimaManager.resolveLocation(String.join(" ", toSerialize.getCountry(), toSerialize.getPrimaryDivison(), toSerialize.getSpecificLocality()))));
        } catch (IOException | InterruptedException e) {
            log.error("Failed to resolve location", e);
        }

        try {
            reverseNestedCollection.put("einheitderhoehe", nahimaManager.reduceAssociateForAssociation(nahimaManager.resolveUnitForHeight(toSerialize.getElev_units())));
        } catch (IOException | InterruptedException e) {
            log.error("Failed to resolve unit", e);
        }

        if (georef != null) {
            try {
                reverseNestedCollection.put("einheitdesfehlerradius", nahimaManager.reduceAssociateForAssociation(nahimaManager.resolveUnitForErrorRadius(georef.getMaxErrorUnits())));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve unit", e);
            }

            try {
                reverseNestedCollection.put("datumsformatgeodaeischeskooordinatensystem", nahimaManager.reduceAssociateForAssociation(nahimaManager.resolveDatumFormat(georef.getDatum())));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve date format", e);
            }
        }

        try {
            reverseNestedCollection.put("_nested:aufsammlung__sammelmethoden", new JSONArray(new JSONObject[]{
                    new JSONObject(new HashMap<>() {{
                        put("sammlungsmethode", nahimaManager.resolveCollectionMethod(toSerialize.getCollectingMethod()));
                    }})
            }));
        } catch (IOException | InterruptedException e) {
            log.error("Failed to resolve collection method", e);
        }

        JSONArray collectors = new JSONArray();
        for (Collector collector : toSerialize.getCollectors()) {
            try {
                JSONObject person = nahimaManager.reduceAssociateForAssociation(nahimaManager.resolvePerson(collector.getCollectorName()));
                collectors.put(new JSONObject(new HashMap<>() {{
                    put("sammler", person);
                }}));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve date format", e);
            }
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
        reverseNestedCollection.put("sammeldatumtrans", toSerialize.getDateCollected());
        try {
            reverseNestedCollection.put("sammeldatum", dateToNahima(toSerialize.getDateCollected()));
        } catch (ParseException e) {
            log.error("Failed to parse datum " + toSerialize.getDateCollected(), e);
        }
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
        JSONObject wrapper = new JSONObject();
        wrapper.put("entomologie", result);
        wrapper.put("_mask", "entomologie_public_unrestricted");
        wrapper.put("_objecttype", "entomologie");
        wrapper.put("_idx_in_objects", 1);
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

    protected JSONObject wrapInLan(Object value) {
        JSONObject returnValue = new JSONObject();
        returnValue.put("en-US", value);
        return returnValue;
    }

    @Override
    public boolean supportsSerializationOf(Object target) {
        return target instanceof Specimen;
    }
}
