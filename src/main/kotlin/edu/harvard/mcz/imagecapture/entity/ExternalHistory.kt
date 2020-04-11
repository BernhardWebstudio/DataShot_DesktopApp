/**
 * ExternalHistory.java
 *
 *
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
package edu.harvard.mcz.imagecapture.entity


import edu.harvard.mcz.imagecapture.entity.ExternalHistory
import org.apache.commons.logging.LogFactory
import java.util.*

/**
 *
 */
class ExternalHistory {
    /**
     * @return the externalHistoryId
     */
    /**
     * @param externalHistoryId the externalHistoryId to set
     */
    var externalHistoryId: Long? = null
    /**
     * @return the specimen
     */
    /**
     * @param specimen the specimen to set
     */
    var specimen: Specimen? = null
    /**
     * @return the externalWorkflowProcess
     */
    /**
     * @param externalWorkflowProcess the externalWorkflowProcess to set
     */
    var externalWorkflowProcess: String? = null
    /**
     * @return the externalWorkflowDate
     */
    /**
     * @param externalWorkflowDate the externalWorkflowDate to set
     */
    var externalWorkflowDate: Date? = null

    companion object {
        private val log = LogFactory.getLog(ExternalHistory::class.java)
    }
}
