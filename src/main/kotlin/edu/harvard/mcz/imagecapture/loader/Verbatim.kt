/**
 * Verbatim.java
 * edu.harvard.mcz.imagecapture.loader
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
package edu.harvard.mcz.imagecapture.loader


import org.apache.commons.logging.LogFactory

/**
 *
 */
object Verbatim {
    val NO_PIN_LABELS: String? = "No Pin Labels"
    val PARTLY_ILLEGIBLE: String? = "Partly illegible"
    val ENTIRELY_ILLEGIBLE: String? = "Entirely illegible"
    val TRUNCATED_BY_IMAGE: String? = "Edge of image truncates label"
    val NO_LOCALITY_DATA: String? = "[No specific locality data][No higher geography data]"
    val SEPARATOR: String? = "|"
    private val log = LogFactory.getLog(Verbatim::class.java)
}
