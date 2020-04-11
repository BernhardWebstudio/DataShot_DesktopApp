/**
 * NatureOfId.java
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


import org.apache.commons.logging.LogFactory

/**
 *
 */
object NatureOfId {
    val LEGACY: String? = "legacy"
    val EXPERT_ID: String? = "expert ID"
    private val log = LogFactory.getLog(NatureOfId::class.java)
    val natureOfIdValues: Array<String?>?
        get() = arrayOf(
                EXPERT_ID,
                "type ID",
                LEGACY,
                "ID based on molecular data",
                "ID to species group",
                "erroneous citation",
                "field ID",
                "non-expert ID",
                "taxonomic revision"
        )
}
