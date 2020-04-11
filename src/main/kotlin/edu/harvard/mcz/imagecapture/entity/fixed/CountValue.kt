/**
 * CountValue.java
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


import edu.harvard.mcz.imagecapture.entity.fixed.CountValue
import org.apache.commons.logging.LogFactory

/**
 * Data Structure to hold counts and values for arbitrary select count(*), field from table queries
 * for arbitrary fields.
 */
class CountValue
/**
 * @param count
 * @param value
 */(
        /**
         * @param count the count to set
         */
        var count: Int?,
        /**
         * @param value the value to set
         */
        var value: String?) {
    /**
     * @return the count
     */
    /**
     * @return the value
     */

    companion object {
        private val log = LogFactory.getLog(CountValue::class.java)
    }

}
