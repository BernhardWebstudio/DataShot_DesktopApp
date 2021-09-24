package edu.harvard.mcz.imagecapture.serializer.nahima;

import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.Determination;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Specimen;
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
        // here, it already starts to get complicated, fuu.
        JSONArray reverseNestedDeterminations = new JSONArray();
        for (Determination det : toSerialize.getDeterminations()) {
            JSONObject reverseNestedDetermination = new JSONObject();

            reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));
            reverseNestedDetermination.put("bestimmungsdatumtrans", det.getDateIdentified());
            // try to parse and set the date correctly
            try {
                reverseNestedDetermination.put("bestimmungsdatum", this.dateToNahima(det.getDateIdentified()));
            } catch (ParseException e) {
                log.error("Failed to parse bestimmungsdatum", e);
            }

            reverseNestedDetermination.put("autortrans", det.getAuthorship()); // TODO: resolve
            reverseNestedDetermination.put("taxonnametrans", toSerialize.getAssociatedTaxon()); // TODO: resolve
            reverseNestedDetermination.put("typusid", det.getTypeStatus());
            reverseNestedDetermination.put("familietrans", toSerialize.getFamily()); // TODO: resolve
            reverseNestedDetermination.put("genustrans", det.getGenus()); // TODO: resolve
            reverseNestedDetermination.put("subspezifischesarttrans", det.getSubspecificEpithet()); // TODO: resolve
            reverseNestedDetermination.put("bestimmertrans", det.getIdentifiedBy());
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
            // TODO: other fields

            // finally,
            reverseNestedDeterminations.put(reverseNestedDetermination);
        }
        result.put("_reverse_nested:bestimmung:entomologie", reverseNestedDeterminations);

        JSONArray reverseNestedCollections = new JSONArray();
        LatLong georef = toSerialize.getLatLong().isEmpty() ? null : (LatLong) toSerialize.getLatLong().toArray()[0];
        // some other basic reverse nested values for "aufsammlung" (collection)
        Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
            put("sammlertrans", toSerialize.getCollectors().stream().map(Collector::getCollectorName).collect(Collectors.joining(", "))); // TODO: resolve
            put("_nested:aufsammlung__indikatorenfuersammeldatum", new JSONArray(new String[]{toSerialize.getDateCollectedIndicator()}));
            put("sammlungstitel", toSerialize.getCollection());
            put("kommentare", toSerialize.getSpecimenNotes());
            put("habitattrans", toSerialize.getHabitat());
            put("neuhabitat", wrapInLan(toSerialize.getHabitat()));
            put("mikrohabitat", wrapInLan(toSerialize.getMicrohabitat()));
            put("traegerorganismustrans", toSerialize.getAssociatedTaxon());
            put("traegerorganismus", wrapInLan(toSerialize.getAssociatedTaxon()));
            put("breitengraddezimal", georef == null ? null : georef.getLatDeg());
            put("laengengraddezimal", georef == null ? null : georef.getLongDeg());
            put("fehlerradius", georef == null ? null : georef.getMaxErrorDistance());
            put("hoehemin", toSerialize.getMinimum_elevation());
            put("hoehemax", toSerialize.getMaximum_elevation());
        }};
        JSONObject reverseNestedCollection = new JSONObject(reverseNestedCollectionMap);

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
            log.error("Failed to parse datum " + toSerialize.getDateEmerged(), e);
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


        // TODO: other fields

        // finally, wrap everything in the pool (might want to do somewhere else)
        JSONObject wrapper = new JSONObject();
        wrapper.put("entomologie", result);
        wrapper.put("mask", "entomologie_public_unrestricted");
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
