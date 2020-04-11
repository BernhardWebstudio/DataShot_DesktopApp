/**
 * ConvertTesseractOCR.java
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


import edu.harvard.mcz.imagecapture.ConvertTesseractOCR
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException
import edu.harvard.mcz.imagecapture.interfaces.OCR
import org.apache.commons.logging.LogFactory
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.imageio.ImageTypeSpecifier
import javax.imageio.ImageWriter

/**
 * Takes a buffered image, generates a TIFF file from it with a call
 * out to ImageMagick or GraphicsMagick convert, then passes that tiff
 * file on to TesseractOCR for Tesseract to OCR the tiff tile.
 *
 *
 * Usage:
 * <pre>
 * ConvertTesseractOCR ocr = new ConvertTesseractOCR(aBufferedImage);
 * String result = ocr.getOCRText();
</pre> *
 */
class ConvertTesseractOCR(anImageToOCR: BufferedImage?) : OCR {
    private var subimage: BufferedImage? = null
    private val debug = false// Run imagemagick to convert the target file to TIFF for
    //String runCommand = "convert temptiff.jpg -depth 8 temptiff.tif";
    // OCR and parse UnitTray Label
    // TODO: pull out labelbit to tiff for scanning
// Print out list of supported write formats//it is entering here and never reaches the else below. when it executes the else below, it can't find the convert program because there are references to /usr/bin// write to tiff file for ocr
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.OCR#getOCRText()
     */
    @get:Throws(OCRReadException::class)
    val oCRText: String
        get() {
            var returnValue = ""
            // write to tiff file for ocr
            log!!.debug("in ConvertTesseractOCR.getOCRText()")
            val typeSpecifier = ImageTypeSpecifier(subimage)
            log.debug("in ConvertTesseractOCR.getOCRText() 1")
            val iter: MutableIterator<ImageWriter?> = ImageIO.getImageWriters(typeSpecifier, "tif")
            log.debug("in ConvertTesseractOCR.getOCRText() 2")
            if (iter.hasNext()) { //it is entering here and never reaches the else below. when it executes the else below, it can't find the convert program because there are references to /usr/bin
                val tifffile = File("temptiff.tif")
                log.debug("in ConvertTesseractOCR.getOCRText() 3")
                try {
                    ImageIO.write(subimage, "tif", tifffile)
                    log.debug("in ConvertTesseractOCR.getOCRText() 4")
                } catch (e: IOException) {
                    println(e.message)
                    log.debug("in ConvertTesseractOCR.getOCRText() 5")
                    throw OCRReadException("Native tiff file creation failed." + e.message)
                }
            } else { // Print out list of supported write formats
                if (debug) {
                    log.debug("in ConvertTesseractOCR.getOCRText() 6")
                    val formatNames: Array<String?> = ImageIO.getWriterFormatNames()
                    log.debug("in ConvertTesseractOCR.getOCRText() 7")
                    for (i in formatNames.indices) {
                        println("Write: " + formatNames[i])
                        log.debug("in ConvertTesseractOCR.getOCRText() 8 Write: + formatNames[i]")
                    }
                }
                log.debug("in ConvertTesseractOCR.getOCRText() 9")
                println("No tiff image writer.")
                println("trying convert from jpg")
                val jpgFile = File("temptiff.jpg")
                try {
                    ImageIO.write(subimage, "jpg", jpgFile)
                    log.debug("in ConvertTesseractOCR.getOCRText() 10")
                    // Run imagemagick to convert the target file to TIFF for
//String runCommand = "convert temptiff.jpg -depth 8 temptiff.tif";
                    val runCommand: String = (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_CONVERT_EXECUTABLE)
                            + " temptiff.jpg "
                            + Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_CONVERT_PARAMETERS)
                            + " temptiff.tif")
                    log.debug("in ConvertTesseractOCR.getOCRText() 11 runCommand is $runCommand")
                    val r = Runtime.getRuntime()
                    log.debug("in ConvertTesseractOCR.getOCRText() 12")
                    val proc = r!!.exec(runCommand)
                    log.debug("in ConvertTesseractOCR.getOCRText() 13")
                    println(runCommand)
                    val exitVal = proc!!.waitFor()
                    log.debug("in ConvertTesseractOCR.getOCRText() 14 exitVal is $exitVal")
                    if (exitVal > 0) {
                        throw OCRReadException("ImageMagick temporary TIFF file creation failed.")
                    }
                } catch (e: IOException) {
                    println(e.message)
                    log.debug("in ConvertTesseractOCR.getOCRText() 15 IOException")
                    throw OCRReadException("Native temporary jpeg file creation failed. " + e.message)
                } catch (e: InterruptedException) {
                    println(e.message)
                    log.debug("in ConvertTesseractOCR.getOCRText() 16 InterruptedException")
                    throw OCRReadException("Call to ImageMagick failed." + e.message)
                }
                // OCR and parse UnitTray Label
                val o = TesseractOCR()
                log.debug("in ConvertTesseractOCR.getOCRText() 17")
                // TODO: pull out labelbit to tiff for scanning
                try {
                    o.setTarget("temptiff.tif")
                    log.debug("in ConvertTesseractOCR.getOCRText() 18")
                } catch (e1: OCRReadException) {
                    log.debug("in ConvertTesseractOCR.getOCRText() 19 OCRReadException " + e1.message)
                    println(e1.message)
                    e1.printStackTrace()
                }
                var rawOCR = ""
                try {
                    rawOCR = o.getOCRText()
                    log.debug("in ConvertTesseractOCR.getOCRText() 20 rawOCR is $rawOCR")
                    returnValue = rawOCR
                } catch (e: OCRReadException) {
                    log.debug("in ConvertTesseractOCR.getOCRText() 21 OCRReadException " + e.message)
                    println(e.message)
                    e.printStackTrace()
                }
            }
            log.debug("in ConvertTesseractOCR.getOCRText() 22 returnValue is $returnValue")
            return returnValue
        }

    companion object {
        private val log = LogFactory.getLog(ConvertTesseractOCR::class.java)
        /**
         * Command line test for this class.
         *
         * @param args will take first argument as path and name of file,
         * will extract a portion of this image to test using the default
         * position template.
         * @see edu.harvard.mcz.imagecapture.PositionTemplate
         */
        @JvmStatic
        fun main(args: Array<String>) { // TODO Auto-generated method stub
            var target = "/home/mole/stuff/MCZ/mcz/insects/testImages/base/photo_plate_test_good_4_template.JPG"
            if (args.size == 1) {
                target = args[0]
            }
            val targetFile = File(target)
            val defaultTemplate = PositionTemplate()
            val imagefile: BufferedImage
            try {
                imagefile = ImageIO.read(targetFile)
                val x: Int = defaultTemplate.getTextPosition().width
                val y: Int = defaultTemplate.getTextPosition().height
                val w: Int = defaultTemplate.getTextSize().width
                val h: Int = defaultTemplate.getTextSize().height
                val converter = ConvertTesseractOCR(imagefile.getSubimage(x, y, w, h))
                try {
                    val output = converter.oCRText
                    println(output)
                } catch (e: OCRReadException) {
                    println("OCR Failed." + e.message)
                }
            } catch (e1: IOException) {
                println("Couldn't read imagefile " + target + "." + e1.message)
            }
        }
    }

    init {
        subimage = anImageToOCR
    }
}
