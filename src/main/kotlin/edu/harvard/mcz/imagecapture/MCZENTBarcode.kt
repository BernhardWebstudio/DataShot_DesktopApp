/**
 * Barcode.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.MCZENTBarcode
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import org.apache.commons.logging.LogFactory
import java.util.regex.Pattern

/**
 * Recognition and construction of text strings found in MCZ-ENT barcode labels.  This class deals with the text
 * of the decoded barcode, which is expected be in the form MCZ-ENT[0-9]{8}.  This class doesn't decode or encode
 * the text into a QRCode barcode, that is done with calls to the ZXing library.
 */
class MCZENTBarcode : BarcodeMatcher, BarcodeBuilder {
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.BarcodeBuilder#getNumber(java.lang.String)
     */
    override fun extractNumber(aBarcode: String): Int? {
        var result: Int? = null
        if (matchesPattern(aBarcode)) {
            result = Integer.valueOf(aBarcode.substring(aBarcode.length - 8))
        }
        return result
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.BarcodeMatcher#matchesPattern(java.lang.String)
     */
    override fun matchesPattern(aBarcode: String): Boolean {
        var result = false
        try {
            result = aBarcode.matches("^$PATTERN$")
        } catch (e: NullPointerException) { // if aBarcode was null, treat result as false.
        }
        return result
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.BarcodeMatcher#matchFoundIn(java.lang.String)
     */
    override fun matchFoundIn(aBarcode: String?): Boolean {
        var result = false
        if (aBarcode != null) {
            val p = Pattern.compile(PATTERN)
            result = p.matcher(aBarcode).find()
        }
        return result
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.BarcodeBuilder#makeFromNumber(java.lang.Integer)
     */
    override fun makeFromNumber(aNumber: Int?): String? {
        var result: String? = null
        if (aNumber != null) {
            if (aNumber.toString().length <= DIGITS) {
                if (aNumber >= 0) {
                    val digits = Integer.valueOf(DIGITS).toString()
                    result = PREFIX + String.format("%0" + digits + "d", aNumber)
                }
            }
        }
        return result
    }

    override fun makeGuidFromBarcode(barcode: String): String {
        var result = barcode
        if (barcode.startsWith(PREFIX!!)) {
            result = "MCZ:Ent:" + barcode.substring(7).replaceFirst("^0*".toRegex(), "")
        }
        return result
    }

    companion object {
        val PATTERN: String? = "MCZ-ENT[0-9]{8}"
        val PREFIX: String? = "MCZ-ENT"
        const val DIGITS = 8
        private val log = LogFactory.getLog(MCZENTBarcode::class.java)
    }

    init {
        log!!.debug("Instantiating " + this.javaClass.toString())
    }
}
