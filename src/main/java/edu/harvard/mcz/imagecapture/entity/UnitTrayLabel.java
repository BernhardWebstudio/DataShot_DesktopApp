/**
 * UnitTrayLabel.java
 * edu.harvard.mcz.imagecapture.encoder
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.entity;

import edu.harvard.mcz.imagecapture.interfaces.CollectionReturner;
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner;
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * UnitTrayLabel
 * <p>
 * Includes factory method for decoding a set of key-value pairs in JSON, and a
 * method for constructing such a string with a JSON encoding.
 */
public class UnitTrayLabel
        implements TaxonNameReturner, DrawerNameReturner, CollectionReturner {

    private static final Logger log =
            LoggerFactory.getLogger(UnitTrayLabel.class);

    private Long id;
    private String drawerNumber;
    private String family;
    private String subfamily;
    private String tribe;
    private String genus;
    private String specificEpithet;
    private String subspecificEpithet;
    private String infraspecificEpithet;
    private String infraspecificRank;
    private String authorship;
    private String unNamedForm;
    private int printed;
    private int numberToPrint;
    private Date dateCreated;
    private Date dateLastUpdated;
    private String collection; // collection from which the material came
    private Integer ordinal;   // order in which to print
    private String identifiedBy;
    private String identifiedDate;
    private String sex;

    public UnitTrayLabel() {
        this.printed = 0;
        this.numberToPrint = 1;
        this.drawerNumber = "";
        this.family = "";
        this.subfamily = "";
        this.tribe = "";
        this.genus = "";
        this.specificEpithet = "";
        this.subspecificEpithet = "";
        this.infraspecificEpithet = "";
        this.infraspecificRank = "";
        this.authorship = "";
        this.unNamedForm = "";
        this.collection = "";
        this.sex = "";
        this.ordinal = 1;
        this.identifiedDate = "";
        this.dateCreated = new Date();
        this.dateLastUpdated = new Date();
    }

    /**
     * Constructor with all fields
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param unnamedForm
     * @param printed
     * @param collection
     */
    public UnitTrayLabel(Long id, String drawerNumber, String family,
                         String subfamily, String tribe, String genus,
                         String specificEpithet, String subspecificEpithet,
                         String infraspecificEpithet, String infraspecificRank,
                         String authorship, String unnamedForm, int printed,
                         Date dateCreated, Date dateLastUpdated,
                         String collection, Integer ordinal) {
        super();
        this.id = id;
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = subspecificEpithet;
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.unNamedForm = unnamedForm;
        this.printed = printed;
        this.dateCreated = dateCreated;
        this.dateLastUpdated = dateLastUpdated;
        this.collection = collection;
        this.ordinal = ordinal;
        this.dateCreated = new Date();
        this.dateLastUpdated = new Date();
    }

    /**
     * Constructor for infraspcific trinomial with explicit rank.
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param collection
     */
    public UnitTrayLabel(String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet,
                         String infraspecificEpithet, String infraspecificRank,
                         String authorship, String collection) {
        super();
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = "";
        this.infraspecificEpithet = infraspecificEpithet;
        this.infraspecificRank = infraspecificRank;
        this.authorship = authorship;
        this.collection = collection;
        this.dateCreated = new Date();
        this.dateLastUpdated = new Date();
    }

    /**
     * Constructor for species
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param authorship
     * @param collection
     */
    public UnitTrayLabel(String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet,
                         String authorship, String collection) {
        super();
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = "";
        this.infraspecificEpithet = "";
        this.infraspecificRank = "";
        this.authorship = authorship;
        this.collection = collection;
        this.dateCreated = new Date();
        this.dateLastUpdated = new Date();
    }

    /**
     * Constructor for subspecies
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param authorship
     * @param collection
     */
    public UnitTrayLabel(String drawerNumber, String family, String subfamily,
                         String tribe, String genus, String specificEpithet,
                         String subspecificEpithet, String authorship,
                         String collection) {
        super();
        this.drawerNumber = drawerNumber;
        this.family = family;
        this.subfamily = subfamily;
        this.tribe = tribe;
        this.genus = genus;
        this.specificEpithet = specificEpithet;
        this.subspecificEpithet = subspecificEpithet;
        this.infraspecificEpithet = "";
        this.infraspecificRank = "";
        this.authorship = authorship;
        this.collection = collection;
        this.dateCreated = new Date();
        this.dateLastUpdated = new Date();
    }

    /**
     * Factory method, given a JSON encoded string, as encoded with
     * toJSONString(), extract the values from that string into a new instance of
     * UnitTrayLabel so that they can be obtained by the appropriate returner
     * interface (taxonnameReturner, drawernumberReturner, collectionReturner).
     *
     * @param jsonEncodedLabel the JSON to decode.
     * @return a new UnitTrayLabel populated with the values found in the supplied
     * jsonEncodedLabel text or null on a failure.
     * @see toJSONString
     */
    public static UnitTrayLabel createFromJSONString(String jsonEncodedLabel) {
        UnitTrayLabel result = null;
        if (jsonEncodedLabel.matches("\\{.*\\}")) {
            String originalJsonEncodedLabel = jsonEncodedLabel;
            jsonEncodedLabel =
                    jsonEncodedLabel.replaceFirst("^\\{", ""); // Strip off leading  {
            jsonEncodedLabel =
                    jsonEncodedLabel.replaceFirst("\\}$", ""); // Strip off trailing }
            if (jsonEncodedLabel.contains("}")) {
                // nested json, not expected.
                log.error(
                        "JSON for UnitTrayLabel contains unexpected nesting { { } }.  JSON is: " +
                                originalJsonEncodedLabel);
            } else {
                log.debug("Debug {}", jsonEncodedLabel);
                result = new UnitTrayLabel();
                // Beginning and end are special case for split on '", "'
                // remove leading quotes and spaces (e.g. either trailing '" ' or
                // trailing '"').
                jsonEncodedLabel =
                        jsonEncodedLabel.replaceFirst("^ ", ""); // Strip off leading space
                jsonEncodedLabel =
                        jsonEncodedLabel.replaceFirst(" $", ""); // Strip off trailing space
                jsonEncodedLabel =
                        jsonEncodedLabel.replaceFirst("^\"", ""); // Strip off leading quote
                jsonEncodedLabel = jsonEncodedLabel.replaceFirst(
                        "\"$", ""); // Strip off trailing quote
                // Convert any ", " into "," then split on ","
                jsonEncodedLabel = jsonEncodedLabel.replaceAll("\",\\s\"", "\",\"");
                log.debug("Debug {}", jsonEncodedLabel);
                // split into key value parts by '","'
                String[] pairs = jsonEncodedLabel.split("\",\"");
                for (int x = 0; x < pairs.length; x++) {
                    // split each key value pair
                    String[] keyValuePair = pairs[x].split("\":\"");
                    if (keyValuePair.length == 2) {
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
                        log.debug("key=[" + key + "], value=[" + value + "]");

                        if (key.equals("m1p")) {
                            log.debug("Configured for Project: " + value);
                            if (!value.equals("MCZ Lepidoptera") &&
                                    !value.equals("ETHZ Entomology") &&
                                    !value.equals("[ETHZ Entomology]")) {
                                log.error("Project specified in JSON is not recognized: " +
                                        value);
                                log.error(
                                        "Warning: Keys in JSON may not be correctly interpreted.");
                            }
                        }

                        // Note: Adding values here isn't sufficient to populate specimen
                        // records, you still need to invoke the relevant returner interface
                        // on the parser.
                        if (key.equals("f")) {
                            result.setFamily(value);
                        }
                        if (key.equals("b")) {
                            result.setSubfamily(value);
                        }
                        if (key.equals("t")) {
                            result.setTribe(value);
                        }
                        if (key.equals("g")) {
                            result.setGenus(value);
                        }
                        if (key.equals("s")) {
                            result.setSpecificEpithet(value);
                        }
                        if (key.equals("u")) {
                            result.setSubspecificEpithet(value);
                        }
                        if (key.equals("i")) {
                            result.setInfraspecificEpithet(value);
                        }
                        if (key.equals("r")) {
                            result.setInfraspecificRank(value);
                        }
                        if (key.equals("a")) {
                            result.setAuthorship(value);
                        }
                        if (key.equals("d")) {
                            result.setDrawerNumber(value);
                        }
                        if (key.equals("c")) {
                            result.setCollection(value);
                            log.debug("Debug {}", result.getCollection());
                        }
                        if (key.equals("id")) {
                            result.setIdentifiedBy(value);
                        }
                        if (key.equals("idD")) {
                            result.setIdentifiedDate(value);
                        }
                        if (key.equals("x")) {
                            result.setSex(value);
                        }
                    }
                }
                log.debug("Debug {}", result.toJSONString());
            }
        } else {
            log.debug("JSON not matched to { }");
        }
        return result;
    }

    /**
     * Retuns a JSON encoding of the list of fields that can appear on a unit tray
     * label using key-value pairs where the keys are f,b,t,g,s,u,i,r,a,d, and
     * optionally c, and the values are respectively for the family,
     * subfamily,tribe, genus, specificepithet, subspecificepithet,
     * infraspecificepithet, infraspecificrank, authorship, drawernumber and
     * optionally collection.
     *
     * @return String containing JSON in the form { "f":"familyname", .... }
     * @see createFromJSONString
     */
    public String toJSONString() {
        StringBuffer result = new StringBuffer();
        result.append("{");
        result.append(" \"f\":\"").append(family).append("\"");
        result.append(", \"b\":\"").append(subfamily).append("\"");
        result.append(", \"t\":\"").append(tribe).append("\"");
        result.append(", \"g\":\"").append(genus).append("\"");
        result.append(", \"s\":\"").append(specificEpithet).append("\"");
        result.append(", \"u\":\"").append(subspecificEpithet).append("\"");
        result.append(", \"i\":\"").append(infraspecificEpithet).append("\"");
        result.append(", \"r\":\"").append(infraspecificRank).append("\"");
        result.append(", \"a\":\"").append(authorship).append("\"");
        result.append(", \"d\":\"").append(drawerNumber).append("\"");
        if (collection != null) {
            if (!collection.isEmpty()) {
                result.append(", \"c\":\"").append(collection).append("\"");
            }
        }
        if (identifiedBy != null && identifiedBy.trim().length() > 0) {
            result.append(", \"id\":\"").append(identifiedBy).append("\"");
        }
        result.append(" }");
        return result.toString();
    }

    /**
     * @return the drawerNumber
     */
    public String getDrawerNumber() {
        return drawerNumber;
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    public void setDrawerNumber(String drawerNumber) {
        this.drawerNumber = drawerNumber;
        if (this.drawerNumber != null) {
            this.drawerNumber = this.drawerNumber.trim();
        }
    }

    /**
     * @return the family
     */
    public String getFamily() {
        return family;
    }

    /**
     * @param family the family to set
     */
    public void setFamily(String family) {
        this.family = family;
        if (this.family != null) {
            this.family = this.family.trim();
        }
    }

    /**
     * @return the subfamily
     */
    public String getSubfamily() {
        return subfamily;
    }

    /**
     * @param subfamily the subfamily to set
     */
    public void setSubfamily(String subfamily) {
        this.subfamily = subfamily;
        if (this.subfamily != null) {
            this.subfamily = this.subfamily.trim();
        }
    }

    /**
     * @return the tribe
     */
    public String getTribe() {
        if (tribe == null) {
            tribe = "";
        }
        return tribe;
    }

    /**
     * @param tribe the tribe to set
     */
    public void setTribe(String tribe) {
        this.tribe = tribe;
        if (this.tribe != null) {
            this.tribe = this.tribe.trim();
        }
    }

    /**
     * @return the genus
     */
    public String getGenus() {
        if (genus == null) {
            genus = "";
        }
        return genus;
    }

    /**
     * @param genus the genus to set
     */
    public void setGenus(String genus) {
        this.genus = genus;
        if (this.genus != null) {
            this.genus = this.genus.trim();
        }
    }

    /**
     * @return the specificEpithet
     */
    public String getSpecificEpithet() {
        if (specificEpithet == null) {
            specificEpithet = "";
        }
        return specificEpithet;
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    public void setSpecificEpithet(String specificEpithet) {
        this.specificEpithet = specificEpithet;
        if (this.specificEpithet != null) {
            this.specificEpithet = this.specificEpithet.trim();
        }
    }

    /**
     * @return the subspecificEpithet
     */
    public String getSubspecificEpithet() {
        if (subspecificEpithet == null) {
            subspecificEpithet = "";
        }
        return subspecificEpithet;
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    public void setSubspecificEpithet(String subspecificEpithet) {
        this.subspecificEpithet = subspecificEpithet;
        if (this.subspecificEpithet != null) {
            this.subspecificEpithet = this.subspecificEpithet.trim();
        }
    }

    /**
     * @return the infraspecificEpithet
     */
    public String getInfraspecificEpithet() {
        if (infraspecificEpithet == null) {
            infraspecificEpithet = "";
        }
        return infraspecificEpithet;
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    public void setInfraspecificEpithet(String infraspecificEpithet) {
        this.infraspecificEpithet = infraspecificEpithet;
        if (this.infraspecificEpithet != null) {
            this.infraspecificEpithet = this.infraspecificEpithet.trim();
        }
    }

    /**
     * @return the infraspecifcRank
     */
    public String getInfraspecificRank() {
        if (infraspecificRank == null) {
            infraspecificRank = "";
        }
        return infraspecificRank;
    }

    /**
     * @param infraspecifcRank the infraspecifcRank to set
     */
    public void setInfraspecificRank(String infraspecifcRank) {
        this.infraspecificRank = infraspecifcRank;
        if (this.infraspecificRank != null) {
            this.infraspecificRank = this.infraspecificRank.trim();
        }
    }

    /**
     * @return the authorship
     */
    public String getAuthorship() {
        if (authorship == null) {
            authorship = "";
        }
        return authorship;
    }

    /**
     * @param authorship the authorship to set
     */
    public void setAuthorship(String authorship) {
        this.authorship = authorship;
        if (this.authorship != null) {
            this.authorship = this.authorship.trim();
        }
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the unnamedForm
     */
    public String getUnNamedForm() {
        if (unNamedForm == null) {
            unNamedForm = "";
        }
        return unNamedForm;
    }

    /**
     * @param unnamedForm the unnamedForm to set
     */
    public void setUnNamedForm(String unNamedForm) {
        this.unNamedForm = unNamedForm;
    }

    public int getPrinted() {
        return printed;
    }

    public void setPrinted(int printed) {
        this.printed = printed;
    }

    public int getNumberToPrint() {
        return this.numberToPrint;
    }

    public void setNumberToPrint(int numberToPrint) {
        this.numberToPrint = numberToPrint;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }

    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }

    @Override
    public String getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(String identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String getCollection() {
        return collection;
    }

    /**
     * @param collection the collection to set
     */
    public void setCollection(String collection) {
        this.collection = collection;
    }

    /**
     * @return the ordinal
     */
    public Integer getOrdinal() {
        if (ordinal == null) {
            ordinal = new Integer(0);
        }
        return ordinal;
    }

    /**
     * @param ordinal the ordinal to set
     */
    public void setOrdinal(Integer ordinal) {
        if (ordinal == null) {
            this.ordinal = new Integer(0);
        } else {
            this.ordinal = ordinal;
        }
    }

    /**
     * @return the identifiedBy
     */
    public String getIdentifiedBy() {
        return identifiedBy;
    }

    /**
     * @param identifiedBy the identifiedBy to set
     */
    public void setIdentifiedBy(String identifiedBy) {
        this.identifiedBy = identifiedBy;
    }
}
