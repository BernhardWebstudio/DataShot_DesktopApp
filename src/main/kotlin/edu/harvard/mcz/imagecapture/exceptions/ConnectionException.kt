/**
 * ConnectionException.java
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
package edu.harvard.mcz.imagecapture.exceptions


import edu.harvard.mcz.imagecapture.exceptions.ConnectionException
import org.apache.commons.logging.LogFactory

/**
 *
 */
class ConnectionException : Exception {
    /**
     *
     */
    constructor() {
        log!!.debug(ConnectionException::class.java.name)
    }

    /**
     * @param message
     */
    constructor(message: String?) : super(message) {
        log!!.debug(message)
    }

    /**
     * @param cause
     */
    constructor(cause: Throwable) : super(cause) {
        log!!.debug(cause.toString())
    }

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) {
        log!!.debug(message)
    }

    companion object {
        private const val serialVersionUID = 5218215421281921931L
        private val log = LogFactory.getLog(ConnectionException::class.java)
    }
}
