package edu.harvard.mcz.imagecapture.entity;

import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Proxy object for SpecimenPart
 */
public class SpecimenPart implements Cloneable {

    public static final String[] PART_NAMES = {"whole animal",
            "partial animal",
            "partial animal: abdomen",
            "partial animal: body",
            "partial animal: legs",
            "partial animal: wings",
            "cocoon",
            "frass",
            "frass chain",
            "genitalia",
            "head capsule",
            "head capsule hat",
            "larval case",
            "larval shelter",
            "molt",
            "other",
            "pupal exuvia",
            "pupal shelter",
            "puparium",
            "sphragis"};
    public static final String[] PRESERVATION_NAMES = {
            "pinned", "pointed", "carded", "envelope", "capsule", "alcohol"};

    private static final Logger log = LoggerFactory.getLogger(SpecimenPart.class);

    private Long specimenPartId;
    private Specimen specimenId;
    private String partName = "whole animal";
    private String preserveMethod = "pinned";
    private int lotCount = 1;        // Coll_Object.lot_count
    private String lotCountModifier; // Coll_Object.lot_count_modifier
    private Collection<SpecimenPartAttribute> attributeCollection;

    public SpecimenPart() {
    }

    /**
     * @param specimenPartId
     * @param specimenId
     * @param partName
     * @param preserveMethod
     * @param lotCount
     * @param lotCountModifier
     * @param attributeCollection
     */
    public SpecimenPart(Long specimenPartId, Specimen specimenId, String partName,
                        String preserveMethod, int lotCount,
                        String lotCountModifier,
                        Set<SpecimenPartAttribute> attributeCollection) {
        this(specimenId, partName, preserveMethod, lotCount, lotCountModifier,
                attributeCollection);
        this.specimenPartId = specimenPartId;
    }

    /**
     * @param specimenId
     * @param partName
     * @param preserveMethod
     * @param lotCount
     * @param lotCountModifier
     * @param attributeCollection
     */
    public SpecimenPart(Specimen specimenId, String partName,
                        String preserveMethod, int lotCount,
                        String lotCountModifier,
                        Set<SpecimenPartAttribute> attributeCollection) {
        super();
        this.specimenId = specimenId;
        this.partName = partName;
        this.preserveMethod = preserveMethod;
        this.lotCount = lotCount;
        this.lotCountModifier = lotCountModifier;
        this.attributeCollection = attributeCollection;
    }

    /**
     * @return the specimenPartId
     */
    public Long getSpecimenPartId() {
        return specimenPartId;
    }

    /**
     * @param specimenPartId the specimenPartId to set
     */
    public void setSpecimenPartId(Long specimenPartId) {
        this.specimenPartId = specimenPartId;
    }

    /**
     * @return the specimenId
     */
    public Specimen getSpecimen() {
        return specimenId;
    }

    /**
     * @param specimenId the specimenId to set
     */
    public void setSpecimen(Specimen specimenId) {
        this.specimenId = specimenId;
    }

    // legacy as long as the hibernate property is called specimenId
    public Specimen getSpecimenId() {
        return specimenId;
    }

    // legacy as long as the hibernate property is called specimenId
    public void setSpecimenId(Specimen specimenId) {
        this.specimenId = specimenId;
    }

    /**
     * @return the partName
     */
    public String getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * @return the preserveMethod
     */
    public String getPreserveMethod() {
        return preserveMethod;
    }

    /**
     * @param preserveMethod the preserveMethod to set
     */
    public void setPreserveMethod(String preserveMethod) {
        this.preserveMethod = preserveMethod;
    }

    /**
     * @return the lotCount
     */
    public int getLotCount() {
        return lotCount;
    }

    /**
     * @param lotCount the lotCount to set
     */
    public void setLotCount(int lotCount) {
        this.lotCount = lotCount;
    }

    /**
     * @return the lotCountModifier
     */
    public String getLotCountModifier() {
        return lotCountModifier;
    }

    /**
     * @param lotCountModifier the lotCountModifier to set
     */
    public void setLotCountModifier(String lotCountModifier) {
        this.lotCountModifier = lotCountModifier;
    }

    /**
     * @return the attributeCollection
     */
    public Collection<SpecimenPartAttribute> getAttributeCollection() {
        if (attributeCollection == null) {
            attributeCollection = new HashSet<>();
            SpecimenPartAttributeLifeCycle spals =
                    new SpecimenPartAttributeLifeCycle();
            attributeCollection.addAll(spals.findBySpecimenPart(this));
        }
        return attributeCollection;
    }

    /**
     * @param attributeCollection the attributeCollection to set
     */
    public void setAttributeCollection(
            Collection<SpecimenPartAttribute> attributeCollection) {
        this.attributeCollection = attributeCollection;
    }

    public Collection<SpecimenPartAttribute> getSpecimenPartAttributes() {
        return getAttributeCollection();
    }

    public void setSpecimenPartAttributes(
            Collection<SpecimenPartAttribute> attributeCollection) {
        setAttributeCollection(attributeCollection);
    }

    /**
     * Obtain human readable list of attribute types and values for the
     * specimen part attributes associated with this specimen part.
     *
     * @return string containing concatenated list of attribute types, values, and
     * units.
     * If attribute collection is empty, returns an empty string.
     */
    public String getPartAttributeValuesConcat() {
        StringBuffer result = new StringBuffer();
        Iterator<SpecimenPartAttribute> i = getAttributeCollection().iterator();
        int counter = 0;
        while (i.hasNext()) {
            SpecimenPartAttribute attribute = i.next();
            if (counter > 0) {
                result.append(", ");
            }
            result.append(attribute.getAttributeType())
                    .append(':')
                    .append(attribute.getAttributeValue());
            if (attribute.getAttributeUnits() != null) {
                result.append(attribute.getAttributeUnits());
            }
            counter++;
            log.debug("Debug", counter);
        }
        return result.toString();
    }

    @Override
    public Object clone() {
        SpecimenPart newPart =
                new SpecimenPart(specimenId, partName, preserveMethod, lotCount,
                        lotCountModifier, new HashSet<>());
        Set<SpecimenPartAttribute> newAttributeCollection = new HashSet<>();
        if (this.attributeCollection != null) {
            Iterator<SpecimenPartAttribute> iterator =
                    this.attributeCollection.iterator();
            while (iterator.hasNext()) {
                SpecimenPartAttribute newSpecPartAttr =
                        (SpecimenPartAttribute) iterator.next().clone();
                newSpecPartAttr.setSpecimenPart(newPart);
                newAttributeCollection.add(newSpecPartAttr);
            }
        }
        newPart.attributeCollection = newAttributeCollection;
        return newPart;
    }
}
