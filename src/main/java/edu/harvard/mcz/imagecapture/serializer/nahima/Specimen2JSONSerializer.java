package edu.harvard.mcz.imagecapture.serializer.nahima;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.data.NahimaManager;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.Determination;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute;
import edu.harvard.mcz.imagecapture.exceptions.SkipSpecimenException;
import edu.harvard.mcz.imagecapture.serializer.ToJSONSerializerInterface;
import edu.harvard.mcz.imagecapture.utility.NullHandlingUtility;

public class Specimen2JSONSerializer implements ToJSONSerializerInterface {
    private static final Logger log = LoggerFactory.getLogger(Specimen2JSONSerializer.class);
    private final SimpleDateFormat nahimaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final NahimaManager nahimaManager;

    public Specimen2JSONSerializer(NahimaManager manager) {
        this.nahimaManager = manager;
    }

    @Override
    public JSONObject serialize2JSON(Object target) throws SkipSpecimenException {
        return serialize2JSON(target, null);
    }

    @Override
    public JSONObject serialize2JSON(Object target, JSONObject existing) throws SkipSpecimenException {
        assert target instanceof Specimen;
        JSONObject result = new JSONObject();
        Specimen toSerialize = (Specimen) target;
        final JSONObject parentExisting = existing;
        if (existing != null) {
            if (existing.has("entomologie")) {
                existing = existing.getJSONObject("entomologie");
            }
        }
        // easy, first level fields
        result.put("barcodeqrcode", toSerialize.getBarcode());
//        result.put("erschliessungsfragen", toSerialize.getQuestions());
        result.put("folgerung", toSerialize.getInferences());
        // here, it already starts to get complicated, fuu.
        // first, other ids, incl. specimen ID
        JSONArray otherIds = new JSONArray();
        for (Number otherNr : toSerialize.getNumbers()) {
            Map<String, Object> otherNrMap = new HashMap<>() {{
                put("andereid", otherNr.getNumber());
//                put("bemerkung", String.join(" ", otherNr.getNumber(), otherNr.getNumberType()));
            }};
            if (otherNr.getTemporaryComment() != null) {
                otherNrMap.put("bemerkung", otherNr.getTemporaryComment());
            }
            try {
                otherNrMap.put("typ", nahimaManager.resolveOtherNrType(otherNr.getNumberType()));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve nr type", e);
            }
            otherIds.put(new JSONObject(otherNrMap));
        }
        // also add specimen id as other id
        if (toSerialize.getSpecimenId() != null) {
            JSONObject dataShotId = new JSONObject(new HashMap<String, Object>() {{
                put("andereid", String.valueOf(toSerialize.getSpecimenId()));
            }});
            try {
                dataShotId.put("typ", nahimaManager.resolveOtherNrType(ImageCaptureApp.APP_NAME + "-ID"));
            } catch (IOException | InterruptedException e) {
                log.error("Failed to resolve nr type", e);
            }
            otherIds.put(dataShotId);
        }
        result.put("_nested:entomologie__andereids", otherIds);
        // set determinations
        JSONArray reverseNestedDeterminations = new JSONArray();
        // adding specimen's own det,
        // only the "main" one should be resolved
        JSONObject mainReverseNestedDetermination = new JSONObject();
        mainReverseNestedDetermination = nahimaManager.addDefaultValuesForCreation(mainReverseNestedDetermination, existing == null ? null : existing.getJSONArray("_reverse_nested:bestimmung:entomologie").getJSONObject(0));

        // resolutions
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "art", () -> nahimaManager.resolveSpecificEpithet(toSerialize.getSpecificEpithet()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "autor", () -> nahimaManager.resolveAuthorship(toSerialize.getAuthorship()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "familie", () -> nahimaManager.resolveFamily(toSerialize.getFamily()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "genus", () -> nahimaManager.resolveGenus(toSerialize.getGenus()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "infraspezifischerrang", () -> nahimaManager.resolveInfraspecificRank(toSerialize.getInfraspecificRank()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "infraspezifischestaxon", () -> nahimaManager.resolveInfraspecificEpithet(toSerialize.getInfraspecificEpithet()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "subspezifischeart", () -> nahimaManager.resolveSubSpecificEpithet(toSerialize.getSubspecificEpithet()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "unterfamilie", () -> nahimaManager.resolveSubFamily(toSerialize.getSubfamily()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "tribus", () -> nahimaManager.resolveTribe(toSerialize.getTribe()));
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "ordnung", () -> nahimaManager.resolveOrder(toSerialize.getHigherOrder()));

        String taxonName = NullHandlingUtility.joinNonNull(" ", toSerialize.getGenus(), toSerialize.getSpecificEpithet(), toSerialize.getSubspecificEpithet(), toSerialize.getInfraspecificRank(), toSerialize.getInfraspecificEpithet()).trim().replace("  ", " ");
        if (!taxonName.isEmpty()) {
            tryNonUserSkippableResolve(mainReverseNestedDetermination, "taxonname", () -> nahimaManager.resolveTaxon(taxonName));
        }

        // the publication will be exported to the publication events, 
        // but since those are quite hidden, we also add it here
        // if the authorship contains a colon
        String authorship = toSerialize.getAuthorship();
        if (authorship != null && authorship.contains(":") && (
                (toSerialize.getCitedInPublication() != null && !toSerialize.getCitedInPublication().isEmpty()) ||
                        (toSerialize.getCitedInPublicationLink() != null && !toSerialize.getCitedInPublicationLink().isEmpty()) ||
                        (toSerialize.getCitedInPublicationYear() != null && !toSerialize.getCitedInPublicationYear().isEmpty()) ||
                        (toSerialize.getCitedInPublicationComment() != null && !toSerialize.getCitedInPublicationComment().isEmpty())
        )) {
            mainReverseNestedDetermination.put("_nested:bestimmung__bestimmungsreferenzen", new JSONArray(new JSONObject[]{
                    new JSONObject(new HashMap<String, String>() {{
                        // comma separated components of the publication reference
                        put("bestimmungsreferenz", String.join(", ", Arrays.stream(new String[]{
                                toSerialize.getCitedInPublication(),
                                toSerialize.getCitedInPublicationLink(),
                                toSerialize.getCitedInPublicationYear(),
                                toSerialize.getCitedInPublicationComment()
                        }).filter(s -> s != null && !s.isEmpty()).toArray(String[]::new)));
                    }})
            }));
        }

        if (toSerialize.getIdentificationRemarks() != null && !toSerialize.getIdentificationRemarks().isEmpty()) {
            mainReverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));
        }
        // try to parse and set the date correctly
        tryNonUserSkippableResolve(mainReverseNestedDetermination, "bestimmungsdatum", () -> this.dateToNahima(toSerialize.getDateIdentified(), true));

        JSONArray identifierPersons = new JSONArray();
        tryUserSkippableResolve(identifierPersons, () -> nahimaManager.resolvePerson(toSerialize.getIdentifiedBy()), "person");
        JSONArray wrappedIdentifierPersons = new JSONArray();
        identifierPersons.forEach(identifierPerson -> {
            JSONObject wrappedIdentifier = new JSONObject();
            wrappedIdentifier.put("bestimmer_person", identifierPerson);
            wrappedIdentifierPersons.put(wrappedIdentifier);
        });
        mainReverseNestedDetermination.put("_nested:bestimmung__bestimmer_person", wrappedIdentifierPersons);

        if (!Objects.equals(toSerialize.getTypeStatus(), "Not a Type")) {
            tryUserSkippableResolve(mainReverseNestedDetermination, "typusstatus", () -> nahimaManager.resolveTypeStatus(toSerialize.getTypeStatus()));
            mainReverseNestedDetermination.put("typusid", toSerialize.getTypeNumber());
        }
        // finally,
        reverseNestedDeterminations.put(mainReverseNestedDetermination);

        // all others are just verbatim
        for (Determination det : toSerialize.getDeterminations()) {
            Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
                put("arttrans", det.getSpecificEpithet());
                put("autortrans", det.getAuthorship());
                put("bestimmertrans", det.getIdentifiedBy());
                put("bestimmungsdatumtrans", det.getDateIdentified());
//                put("familietrans", toSerialize.getFamily());
                put("genustrans", det.getGenus());
                put("infrapezifischerrangtrans", det.getInfraspecificRank());
                put("infraspezifischestaxontrans", det.getInfraspecificEpithet());
                put("subspezifischesarttrans", det.getSubspecificEpithet());
//                put("taxonnametrans", toSerialize.getAssociatedTaxon());
//                put("unterfamilietrans", toSerialize.getSubfamily());
            }};
            if (det.getTypeStatus() != null && !Objects.equals(det.getTypeStatus(), "Not a Type")) {
                reverseNestedCollectionMap.put("typusid", det.getSpeciesNumber());
                reverseNestedCollectionMap.put("typusstatustrans", det.getTypeStatus());
            }

            JSONObject reverseNestedDetermination = new JSONObject(reverseNestedCollectionMap);
            reverseNestedDetermination = nahimaManager.addDefaultValuesForCreation(reverseNestedDetermination, NahimaManager.entomologyPool);
//            reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{toSerialize.getIdentificationRemarks()}));

            if (det.getRemarks() != null && !det.getRemarks().equals("")) {
                reverseNestedDetermination.put("_nested:bestimmung__kommentarezurbestimmung", new JSONArray(new String[]{det.getRemarks()}));
            }

            // try to parse and set the date correctly
//            tryNonUserSkippableResolve(reverseNestedDetermination, "bestimmungsdatum", () -> this.dateToNahima(det.getDateIdentified(), false));

//            tryUserSkippableResolve(reverseNestedDetermination, "typusstatus", () -> nahimaManager.resolveTypeStatus(det.getTypeStatus()));
            // finally,
            reverseNestedDeterminations.put(reverseNestedDetermination);
        }

        result.put("_reverse_nested:bestimmung:entomologie", reverseNestedDeterminations);

        JSONArray reverseNestedCollections = new JSONArray();
        LatLong georef = toSerialize.getLatLong().isEmpty() ? null : toSerialize.getLatLong().iterator().next();
        ;
        // some other basic reverse nested values for "aufsammlung" (collection)
        JSONObject[] comments = {
                new JSONObject(new HashMap<>() {{
                    put("kommentar", toSerialize.getSpecimenNotes());
                }})};

        if (toSerialize.getSpecimenNotes() == null || toSerialize.getSpecimenNotes().equals("")) {
            comments = null;
        }
        JSONObject finalExisting = existing;
        Map<String, Object> reverseNestedCollectionMap = new HashMap<>() {{
//            put("sammlertrans", toSerialize.getCollectors().stream().map(Collector::getCollectorName).collect(Collectors.joining(", ")));
//            put("sammlungtrans", toSerialize.getCollection());
            put("sammlungstitel", toSerialize.getCollection());
//            put("habitattrans", toSerialize.getHabitat());
//            put("sammelorttrans", toSerialize.getVerbatimLocality());
            put("lokalitaet", toSerialize.getSpecificLocality());
            put("neuhabitat", nahimaManager.wrapInLan(toSerialize.getHabitat()));
            put("mikrohabitat", nahimaManager.wrapInLan(toSerialize.getMicrohabitat()));
//            put("traegerorganismustrans", toSerialize.getAssociatedTaxon());
            put("traegerorganismus", nahimaManager.wrapInLan(toSerialize.getAssociatedTaxon()));
            put("breitengraddezimal", (georef == null) ? JSONObject.NULL : georef.getDecLatString());
            put("laengengraddezimal", (georef == null) ? JSONObject.NULL : georef.getDecLongString());
            put("breitengrad", (georef == null || georef.getLatDeg() == null) ? JSONObject.NULL : georef.getLatDeg().toString());
            put("laengengrad", (georef == null || georef.getLongDeg() == null) ? JSONObject.NULL : georef.getLongDeg().toString());
            put("fehlerradius", (georef == null || georef.getMaxErrorDistance() == null) ? JSONObject.NULL : georef.getMaxErrorDistance().toString());
            put("hoehemin", toSerialize.getMinimum_elevation() == null ? JSONObject.NULL : toSerialize.getMinimum_elevation().toString());
            put("hoehemax", toSerialize.getMaximum_elevation() == null ? JSONObject.NULL : toSerialize.getMaximum_elevation().toString());
            put("lokalitaettrans", toSerialize.getVerbatimLocality());
            put("__idx", 0);
            put("_version", finalExisting == null ? 1 : finalExisting.getJSONArray("_reverse_nested:aufsammlung:entomologie").getJSONObject(0).getInt("_version") + 1);
            put("_id", finalExisting == null ? JSONObject.NULL : NahimaManager.resolveId(finalExisting.getJSONArray("_reverse_nested:aufsammlung:entomologie").getJSONObject(0)));
            put("_pool", NahimaManager.entomologyPool);
        }};
        if (comments != null) {
            reverseNestedCollectionMap.put("_nested:aufsammlung__kommentare", new JSONArray(comments));
        }
        if (toSerialize.getDateCollectedIndicator() != null
                && !toSerialize.getDateCollectedIndicator().equals("")) {
            JSONArray indicators = new JSONArray();
            JSONObject indicator = new JSONObject();
            tryUserSkippableResolve(indicator, "indikatorfuersammeldatum", () -> nahimaManager.resolveCollectionDateIndicator(toSerialize.getDateCollectedIndicator()));
            indicators.put(indicator);
            reverseNestedCollectionMap.put("_nested:aufsammlung__indikatorenfuersammeldatum", indicators);
        }
        JSONObject reverseNestedCollection = new JSONObject(reverseNestedCollectionMap);

        tryUserSkippableResolve(reverseNestedCollection, "sammelort", () -> nahimaManager.resolveLocation(toSerialize));
        tryUserSkippableResolve(reverseNestedCollection, "einheitderhoehe", () -> nahimaManager.resolveUnitForHeight(toSerialize.getElev_units()));

        // only export if not default
        if (georef != null && !georef.equals(new LatLong())) {
            if (georef.getActualMaxErrorUnits() != null) {
                tryUserSkippableResolve(reverseNestedCollection, "einheitdesfehlerradius", () ->
                        nahimaManager.resolveUnitForErrorRadius(georef.getMaxErrorUnits()));
            }
            if (georef.getDatum() != null && !georef.getDatum().equals("")) {
                tryUserSkippableResolve(reverseNestedCollection, "datumsformatgeodaeischeskooordinatensystem", () -> nahimaManager.resolveDatumFormat(georef.getDatum()));
            }
        }

        if (toSerialize.getCollectingMethod() != null && !toSerialize.getCollectingMethod().equals("")) {
            tryNonUserSkippableResolve(reverseNestedCollection, "_nested:aufsammlung__sammelmethoden", () -> new JSONArray(new JSONObject[]{
                    new JSONObject(new HashMap<>() {{
                        put("sammlungsmethode", nahimaManager.resolveCollectionMethod(toSerialize.getCollectingMethod()));
                    }})
            }));
        }

        JSONArray collectors = new JSONArray();
        for (Collector collector : toSerialize.getCollectors()) {
            if (collector.getCollectorName() != null && !Objects.equals(collector.getCollectorName(), "")) {
                tryUserSkippableResolve(collectors, () -> new JSONObject(new HashMap<>() {{
                    put("sammler", nahimaManager.reduceAssociateForAssociation(
                            nahimaManager.resolvePerson(collector.getCollectorName()))
                    );
                }}), "sammler", false);
            }
        }
        reverseNestedCollection.put("_nested:aufsammlung__sammler", collectors);

        if (toSerialize.getIsoDate() == null) {
            toSerialize.setIsoDate("");
        }
        boolean isoDateIsPeriod = toSerialize.getIsoDate().contains("-");
        String[] dateSplit = toSerialize.getIsoDate().split("-");

        try {
            if (toSerialize.getDateNos() != null && !toSerialize.getDateNos().equals("")) {
                if (toSerialize.getDateNos().contains("-")) {
                    reverseNestedCollection.put("zeitraumtrans", toSerialize.getDateNos());
                } else {
                    reverseNestedCollection.put("datumtrans", toSerialize.getDateNos());
                }
                if (isoDateIsPeriod) {
                    assert (dateSplit.length == 2);
                    reverseNestedCollection.put("zeitraum", dateRangeToNahima(dateSplit[0].trim(), dateSplit[1].trim()));
                } else {
                    reverseNestedCollection.put("datum", this.dateToNahima(toSerialize.getIsoDate(), false));
                }

                if (Objects.equals(toSerialize.getDateNos(), toSerialize.getDateCollected())) {
                    if (isoDateIsPeriod) {
                        reverseNestedCollection.put("aufsammlungszeitraum", dateRangeToNahima(
                                dateSplit[0].trim(), dateSplit[1].trim(), false
                        ));
                    } else {
                        reverseNestedCollection.put("sammeldatum", dateToNahima(toSerialize.getIsoDate(), false));
                    }
                }
            }
        } catch (ParseException e) {
            log.error("Failed to parse datum " + toSerialize.getDateEmerged() + " " + toSerialize.getDateNos(), e);
        }

        reverseNestedCollection.put("indikator_fuer_kultur_zucht", toSerialize.getDateEmergedIndicator());
        if (!Objects.equals(toSerialize.getDateEmerged(), "") && toSerialize.getDateEmerged() != null) {
            reverseNestedCollection.put("kultur_zucht", true);
        }
        reverseNestedCollection.put("sammeldatumtrans", toSerialize.getDateCollected());

        reverseNestedCollections.put(reverseNestedCollection);
        result.put("_reverse_nested:aufsammlung:entomologie", reverseNestedCollections);

        JSONArray parts = new JSONArray();
        for (SpecimenPart part : toSerialize.getSpecimenParts()) {
            JSONObject partJson = new JSONObject(new HashMap<>() {{
                put("anzahlpraeparatteile", part.getLotCount());
            }});
            tryUserSkippableResolve(partJson, "praeparatteil", () -> nahimaManager.resolvePreparationPart(part.getPartName()));
            tryUserSkippableResolve(partJson, "montierungsmethode", () -> nahimaManager.resolveMountingMethod(part.getPreserveMethod()));

            if (part.getSpecimenPartAttributes().size() > 0) {
                for (SpecimenPartAttribute attribute : part.getSpecimenPartAttributes()) {
                    JSONObject partJsonClone = new JSONObject(partJson.toString());
//                    tryUserSkippableResolve(partJsonClone, "attribute", () -> nahimaManager.resolvePreparationPartAttribute(NullHandlingUtility.joinNonNull(" ", attribute.getAttributeRemark(), attribute.getAttributeType(), attribute.getAttributeValue(), attribute.getAttributeDate() != null ? nahimaDateFormat.format(attribute.getAttributeDate()) : "", attribute.getAttributeDeterminer()), attribute.getAttributeUnits()));
//                    tryUserSkippableResolve(partJsonClone, "attribute", () -> nahimaManager.resolvePreparationPartAttribute(attribute.getAttributeValue(), attribute.getAttributeUnits()));
                    tryUserSkippableResolve(partJsonClone, "attribute", () -> nahimaManager.resolvePreparationPartAttribute(NullHandlingUtility.joinNonNull(": ", attribute.getAttributeType(), attribute.getAttributeValue()), attribute.getAttributeUnits()));
                    parts.put(partJsonClone);
                }
            } else {
                parts.put(partJson);
            }
        }
        result.put("_nested:entomologie__praeparatteile", parts);

        // publication
        JSONArray publications = new JSONArray();
        if (toSerialize.getCitedInPublication() != null && !toSerialize.getCitedInPublication().isBlank()) {
            JSONObject publication = new JSONObject();
            tryUserSkippableResolve(publication, "publikation", () -> nahimaManager.resolvePublication(
                    toSerialize.getCitedInPublication(),
                    toSerialize.getCitedInPublicationLink(),
                    toSerialize.getCitedInPublicationYear(),
                    toSerialize.getCitedInPublicationComment()
            ));
            publications.put(publication);
        }
        result.put("_nested:entomologie__publikationen", publications);

        tryUserSkippableResolve(result, "geschlecht", () -> nahimaManager.resolveSex(toSerialize.getSex()));
        tryUserSkippableResolve(result, "lebensabschnitt", () -> nahimaManager.resolveLifeStage(toSerialize.getLifeStage()));

        // finally, wrap everything in the pool (might want to do somewhere else)
        JSONObject wrapper = nahimaManager.wrapForCreation(result, existing, "entomologie", "entomologie__complete", NahimaManager.entomologyPool);

        // and add tags if needed
        tryUserSkippableResolve(wrapper, "_tags", () -> nahimaManager.resolveAppropriateTags(toSerialize.getWorkFlowStatus(), parentExisting));

        return wrapper;
    }

    protected JSONObject dateRangeToNahima(String from, String to) throws ParseException {
        return this.dateRangeToNahima(from, to, false);
    }

    protected JSONObject dateRangeToNahima(String from, String to, boolean allowInvalid) throws ParseException {
        JSONObject returnValue = new JSONObject();

        // swap if needed to have the order correct
        JSONObject parsedDateFrom = this.dateToNahima(from.compareTo(to) < 0 ? from : to, allowInvalid);
        JSONObject parsedDateTo = this.dateToNahima(from.compareTo(to) < 0 ? to : from, allowInvalid);

        returnValue.put("from", parsedDateFrom.getString("value"));
        returnValue.put("to", parsedDateTo.getString("value"));
        return returnValue;
    }

    /**
     * Parse all "allowed" date formats, to possibly get a good, parsed, date
     * Wrap it in whatever format Nahima requires
     *
     * @param date         the date string to parse
     * @param allowInvalid whether or not to allow invalid dates. Returns the input string, wrapped, if true.
     * @return the JSON Object Nahima expects
     * @throws ParseException if invalid dates are not allowed.
     */
    protected JSONObject dateToNahima(String date, boolean allowInvalid) throws ParseException {
        log.debug("Parsing date from \"" + date + "\"");
        if (date == null || date.equals("")) {
            return null;
        }
        date = date.trim();
        try {
            Date parsedDate = DateUtils.parseDate(date);
            return dateToNahima(parsedDate);
        } catch (ParseException e) {
            JSONObject returnValue = new JSONObject();
            // remove all trailing zeros, so we don't have to deal with formats like
            // 2021/00/00
            ArrayList<String> dateSplit = new ArrayList<>(Arrays.asList(date.split("\\.|/")));
            while (dateSplit.size() > 0 && dateSplit.get(dateSplit.size() - 1).replaceAll("0", "").equals("")) {
                dateSplit.remove(dateSplit.size() - 1);
            }
            while (dateSplit.size() > 0 && dateSplit.get(0).replaceAll("0", "").equals("")) {
                dateSplit.remove(0);
            }
            // use consistent delimiter from here on, no choice between "." and "/" anymore
            date = String.join("/", dateSplit);

            // find different formats
            // year-only dates are allowed in Nahima, somehow
            if (date.matches("^[0-9]{4}$")) {
                returnValue.put("value", date);
                return returnValue;
            }
            // as are month & year
            if (date.matches("^[0-9]{4}/[0-9]{2}$")) {
                returnValue.put("value", date.replace("/", "-"));
                return returnValue;
            }
            // but ideally, we have the full year available...
            if (date.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{2,4}$")) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("d/M/y");
                    return dateToNahima(format.parse(date));
                } catch (ParseException ignored) {
                }
            }
            if (date.matches("^[0-9]{2,4}/[0-9]{1,2}/[0-9]{1,2}$")) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("y/M/d");
                    return dateToNahima(format.parse(date));
                } catch (ParseException ignored) {
                }
            }
            if (!allowInvalid) {
                log.error("Failed to convert date from " + date, e);
                throw e;
            }
            returnValue.put("value", date);
            return returnValue;
        }
    }

    protected JSONObject dateToNahima(String date) throws ParseException {
        return dateToNahima(date, true);
    }

    protected JSONObject dateToNahima(Date date) {
        JSONObject returnValue = new JSONObject();
        returnValue.put("value", nahimaDateFormat.format(date));
        return returnValue;
    }

    /**
     * Try to run a resolve function on a certain property, do nothing if it fails
     *
     * @param target   the array to append data to
     * @param key      the property of the target to set
     * @param resolver the resolver function
     */
    protected void tryNonUserSkippableResolve(JSONObject target, String key, ResolverMethodInterface resolver) {
        // try to parse and set correctly
        try {
            tryUserSkippableResolve(target, key, resolver);
        } catch (Exception e) {
            log.error("Failed to resolve " + key, e);
        }
    }

    /**
     * Try to run a resolve function on a certain property, do nothing if it fails
     *
     * @param target    the array to append data to
     * @param resolver  the resolver function
     * @param debugHint the useful info on what worked/did not work
     */
    protected void tryNonUserSkippableResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint) {
        // try to parse and set correctly
        try {
            tryUserSkippableResolve(target, resolver, debugHint);
        } catch (Exception e) {
            log.error("Failed to resolve " + debugHint, e);
        }
    }

    /**
     * Try to run a resolve function on a certain property, do nothing if it fails,
     * except when a SkipSpecimenException is thrown, i.e., when the resolver allowed user input "skip specimen"
     *
     * @param target   the array to append data to
     * @param key      the property of the target to set
     * @param resolver the resolver function
     */
    protected void tryUserSkippableResolve(JSONObject target, String key, ResolverMethodInterface resolver) throws SkipSpecimenException {
        try {
            Object returnValue = resolver.doResolve();
            if (returnValue != null) {
                if (returnValue instanceof JSONObject) {
                    target.put(key, nahimaManager.reduceAssociateForAssociation((JSONObject) returnValue));
                } else {
                    target.put(key, returnValue);
                }
            }
        } catch (IOException | InterruptedException | ParseException | RuntimeException | InvocationTargetException e) {
            log.error("Failed to resolve " + key, e);
        }
    }

    protected void tryUserSkippableResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint) throws SkipSpecimenException {
        tryUserSkippableResolve(target, resolver, debugHint, true);
    }

    /**
     * Try to run a resolve function on a certain property, do nothing if it fails,
     * except when a SkipSpecimenException is thrown, i.e., when the resolver allowed user input "skip specimen"
     *
     * @param target    the array to append data to
     * @param resolver  the resolver function
     * @param debugHint the useful info on what worked/did not work
     */
    protected void tryUserSkippableResolve(JSONArray target, ResolverMethodInterface resolver, String debugHint, boolean reduceJSONObject) throws SkipSpecimenException {
        // try to parse and set correctly
        try {
            Object returnValue = resolver.doResolve();
            if (returnValue != null) {
                if (returnValue instanceof JSONObject && reduceJSONObject) {
                    target.put(nahimaManager.reduceAssociateForAssociation((JSONObject) returnValue));
                } else {
                    target.put(returnValue);
                }
            }
        } catch (IOException | InterruptedException | ParseException | RuntimeException | InvocationTargetException e) {
            log.error("Failed to resolve " + debugHint, e);
        }
    }

    @Override
    public boolean supportsSerializationOf(Object target) {
        return target instanceof Specimen;
    }

    protected interface ResolverMethodInterface {
        Object doResolve() throws IOException, InterruptedException, ParseException, RuntimeException, SkipSpecimenException, InvocationTargetException;
    }

    protected interface JSONObjectResolverMethodInterface extends ResolverMethodInterface {
        JSONObject doResolve() throws IOException, InterruptedException, ParseException, RuntimeException, SkipSpecimenException, InvocationTargetException;
    }

    protected interface JSONArrayResolverMethodInterface extends ResolverMethodInterface {
        JSONArray doResolve() throws IOException, InterruptedException, ParseException, RuntimeException, SkipSpecimenException, InvocationTargetException;
    }
}
