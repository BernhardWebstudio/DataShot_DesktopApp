/**
 * BadTemplateException.java
 * edu.harvard.mcz.imagecapture.exceptions
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


/**
 * BadTemplateException to be thrown when a template does not fit an image file to which it is being applied.
 * This exception should be thrown when there is a problem applying a template, as opposed to NoSuchTemplateException
 * which should be thrown when trying to construct a PositionTemplate.
 *
 * @see edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
 */
class BadTemplateException : Exception {
    /**
     * Default Constructor
     */
    constructor() { // TODO Auto-generated constructor stub
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

    /**
     * @param message
     * @param cause
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause) { // TODO Auto-generated constructor stub
    }

    companion object {
        private const val serialVersionUID = -3569211932963119505L
    }
}
