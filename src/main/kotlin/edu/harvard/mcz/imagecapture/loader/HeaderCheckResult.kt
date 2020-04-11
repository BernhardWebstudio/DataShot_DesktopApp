/**
 * HeaderCheckResult.java
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
package edu.harvard.mcz.imagecapture.loader


import edu.harvard.mcz.imagecapture.loader.HeaderCheckResult
import org.apache.commons.logging.LogFactory

/**
 *
 */
class HeaderCheckResult {
    /**
     * @return the result
     */
    /**
     * @param result the result to set
     */
    var isResult = false
    private var message: StringBuilder?

    /**
     * @return the message as a string.
     */
    fun getMessage(): String {
        if (message == null) {
            message = StringBuilder()
        }
        return message.toString()
    }

    /**
     * @param message the message to append to the message for this
     * HeaderCheckResult
     */
    fun addToMessage(message: String?) {
        if (this.message == null) {
            this.message = StringBuilder()
        }
        if (this.message!!.length > 0) {
            this.message!!.append(":")
        }
        this.message!!.append(message)
    }

    companion object {
        private val log = LogFactory.getLog(HeaderCheckResult::class.java)
    }

    init {
        message = StringBuilder()
    }
}
