/**
 * LoadTargetMovedOnException.java
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
 * Exception on data loading indicating that the target record for update
 * from the data load has moved further down the the workflow within the
 * DataShot system and is past the point where the desired load is allowed.
 */
class LoadTargetMovedOnException : LoadException {
    /**
     * Default Constructor.
     */
    constructor() : super("Target record has been updated to a workflow state that can't be updated by this load.") {}

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    constructor(message: String?, cause: Throwable?,
                enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) { // TODO Auto-generated constructor stub
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
        private const val serialVersionUID = 5171194333707975248L
    }
}
