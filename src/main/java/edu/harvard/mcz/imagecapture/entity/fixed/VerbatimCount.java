/**
 * VerbatimCount.java
 * edu.harvard.mcz.imagecapture.data
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
package edu.harvard.mcz.imagecapture.entity.fixed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Structure to hold counts and unique values of verbatim fields.
 */
public class VerbatimCount {
    private static final Logger log =
            LoggerFactory.getLogger(VerbatimCount.class);

    private Integer count;
    private String verbatimLocality;
    private String verbatimDate;
    private String verbatimCollector;
    private String verbatimCollection;
    private String verbatimNumbers;
    private String verbatimUnclassfiedText;

    /**
     * @param count
     * @param verbatimLocality
     * @param verbatimDate
     * @param verbatimCollector
     * @param verbatimCollection
     * @param verbatimNumbers
     * @param verbatimUnclassfiedText
     */
    public VerbatimCount(Integer count, String verbatimLocality,
                         String verbatimDate, String verbatimCollector,
                         String verbatimCollection, String verbatimNumbers,
                         String verbatimUnclassfiedText) {
        this.count = count;
        this.verbatimLocality = verbatimLocality;
        this.verbatimDate = verbatimDate;
        this.verbatimCollector = verbatimCollector;
        this.verbatimCollection = verbatimCollection;
        this.verbatimNumbers = verbatimNumbers;
        this.verbatimUnclassfiedText = verbatimUnclassfiedText;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the verbatimLocality
     */
    public String getVerbatimLocality() {
        return verbatimLocality;
    }

    /**
     * @param verbatimLocality the verbatimLocality to set
     */
    public void setVerbatimLocality(String verbatimLocality) {
        this.verbatimLocality = verbatimLocality;
    }

    /**
     * @return the verbatimDate
     */
    public String getVerbatimDate() {
        return verbatimDate;
    }

    /**
     * @param verbatimDate the verbatimDate to set
     */
    public void setVerbatimDate(String verbatimDate) {
        this.verbatimDate = verbatimDate;
    }

    /**
     * @return the verbatimCollector
     */
    public String getVerbatimCollector() {
        return verbatimCollector;
    }

    /**
     * @param verbatimCollector the verbatimCollector to set
     */
    public void setVerbatimCollector(String verbatimCollector) {
        this.verbatimCollector = verbatimCollector;
    }

    /**
     * @return the verbatimCollection
     */
    public String getVerbatimCollection() {
        return verbatimCollection;
    }

    /**
     * @param verbatimCollection the verbatimCollection to set
     */
    public void setVerbatimCollection(String verbatimCollection) {
        this.verbatimCollection = verbatimCollection;
    }

    /**
     * @return the verbatimNumbers
     */
    public String getVerbatimNumbers() {
        return verbatimNumbers;
    }

    /**
     * @param verbatimNumbers the verbatimNumbers to set
     */
    public void setVerbatimNumbers(String verbatimNumbers) {
        this.verbatimNumbers = verbatimNumbers;
    }

    /**
     * @return the verbatimUnclassfiedText
     */
    public String getVerbatimUnclassfiedText() {
        return verbatimUnclassfiedText;
    }

    /**
     * @param verbatimUnclassfiedText the verbatimUnclassfiedText to set
     */
    public void setVerbatimUnclassfiedText(String verbatimUnclassfiedText) {
        this.verbatimUnclassfiedText = verbatimUnclassfiedText;
    }
}
