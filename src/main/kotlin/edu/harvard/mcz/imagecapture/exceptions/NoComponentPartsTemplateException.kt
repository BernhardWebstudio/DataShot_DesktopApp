/**
 * NoComponentPartsTemplateExeption.java
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
 * NoComponentPartsTemplateExeption is to handle the special case of an image that should be shown only
 * as an entire image without being divided into component parts using a template.  The PositionTemplate constant
 * TEMPLATE_NO_COMPONENT_PARTS can be used to identify such images, and when a PositionTemplate is instantiated
 * with this constant as the template, a NoComponentPartsTemplateExeption will be thrown, allowing code to determine
 * that the entire image should be shown.   This is an alternative to passing the image to the PositionTemplate so that
 * the image size is returned from all PositionTemplate methods.
 *
 */
@Deprecated("")
class NoComponentPartsTemplateException : Exception {
    /**
     *
     */
    constructor() : super() { // TODO Auto-generated constructor stub
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
        private const val serialVersionUID = 8393319549685966260L
    }
}
