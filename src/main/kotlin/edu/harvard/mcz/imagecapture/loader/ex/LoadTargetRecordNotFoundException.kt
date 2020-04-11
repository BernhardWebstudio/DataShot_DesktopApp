/**
 * LoadTargetRecordNotFoundException.java
 * edu.harvard.mcz.imagecapture.loader.ex
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
package edu.harvard.mcz.imagecapture.loader.ex

/**
 *
 */
class LoadTargetRecordNotFoundException : LoadException {
    /**
     * Default Constructor
     */
    constructor() : super("Can't find target record to update.") {}

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    constructor(message: String?, cause: Throwable?,
                enableSuppression: Boolean,
                writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) { // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) { // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    constructor(message: String?) : super(message) { // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    constructor(cause: Throwable?) : super(cause) { // TODO Auto-generated constructor stub
    }

    companion object {
        private const val serialVersionUID = 4325987464754990086L
    }
}
