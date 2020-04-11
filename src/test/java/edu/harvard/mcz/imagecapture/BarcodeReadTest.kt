/**
 * BarcodeReadTest.java
 * edu.harvard.mcz.imagecapture
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
 */
package edu.harvard.mcz.imagecapture

import com.google.zxing.*
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import org.apache.commons.logging.LogFactory
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * Allows testing of barcode detection by ZXing in an image file.
 */
object BarcodeReadTest {
    private val log = LogFactory.getLog(BarcodeReadTest::class.java)
    /**
     * Launches the application, checking one image file for a barcode.
     *
     * @param args takes filename to read as the first argument.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size == 0) {
            println("java BarcodeReadTest filename")
        } else {
            val filename = args[0]
            val file = File(filename)
            try {
                log.debug(file.canonicalPath)
                val image = ImageIO.read(file)
                println(checkForBarcode(image))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun checkForBarcode(image: BufferedImage): String { // Check the entire image for a barcode and return.
        var returnValue = ""
        val source: LuminanceSource = BufferedImageLuminanceSource(image)
        val result: Result
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            val reader = QRCodeReader()
            var hints: Hashtable<DecodeHintType?, Any?>? = null
            hints = Hashtable(3)
            hints[DecodeHintType.TRY_HARDER] = true
            result = reader.decode(bitmap, hints)
            returnValue = result.text
        } catch (e: ReaderException) {
            e.printStackTrace()
            returnValue = ""
        }
        return returnValue
    }
}