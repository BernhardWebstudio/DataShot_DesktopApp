/**
 * VerbatimCount.java
 * edu.harvard.mcz.imagecapture.data
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 */
package edu.harvard.mcz.imagecapture.entity.fixed


import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount
import org.apache.commons.logging.LogFactory

/**
 * Data Structure to hold counts and unique values of verbatim fields.
 */
class VerbatimCount
/**
 * @param count
 * @param verbatimLocality
 * @param verbatimDate
 * @param verbatimCollector
 * @param verbatimCollection
 * @param verbatimNumbers
 * @param verbatimUnclassfiedText
 */(
        /**
         * @param count the count to set
         */
        var count: Int?,
        /**
         * @param verbatimLocality the verbatimLocality to set
         */
        var verbatimLocality: String?,
        /**
         * @param verbatimDate the verbatimDate to set
         */
        var verbatimDate: String?,
        /**
         * @param verbatimCollector the verbatimCollector to set
         */
        var verbatimCollector: String?,
        /**
         * @param verbatimCollection the verbatimCollection to set
         */
        var verbatimCollection: String?,
        /**
         * @param verbatimNumbers the verbatimNumbers to set
         */
        var verbatimNumbers: String?,
        /**
         * @param verbatimUnclassfiedText the verbatimUnclassfiedText to set
         */
        var verbatimUnclassfiedText: String?) {
    /**
     * @return the count
     */
    /**
     * @return the verbatimLocality
     */
    /**
     * @return the verbatimDate
     */
    /**
     * @return the verbatimCollector
     */
    /**
     * @return the verbatimCollection
     */
    /**
     * @return the verbatimNumbers
     */
    /**
     * @return the verbatimUnclassfiedText
     */

    companion object {
        private val log = LogFactory.getLog(VerbatimCount::class.java)
    }

}
