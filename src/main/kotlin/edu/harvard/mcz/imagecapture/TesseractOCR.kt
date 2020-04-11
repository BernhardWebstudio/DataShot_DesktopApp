/**
 * TesseractOCR.java
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


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.TesseractOCR
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException
import edu.harvard.mcz.imagecapture.interfaces.OCR
import org.apache.commons.logging.LogFactory
import java.io.*
import java.nio.charset.StandardCharsets

/**
 * Uses a system runtime call to tesseract to obtain OCR text from a TIFF image file.
 *
 *
 * Usage:
 * <pre>
 * String output = null;
 * try {
 * TesseractOCR t = new TesseractOCR();
 * t.setTarget("filenameToOCR.tif");
 * output = t.OCRText;
 * } catch (OCRReadException e) {
 * // handle exception
 * }
</pre> *
 */
class TesseractOCR : OCR {
    private var target = "/home/mole/stuff/MCZ/mcz/insects/testImages/text.tif"
    private val tempFileBase = "tempOCRtextOutput"
    //private String language = "-l eng";
//private String language = "-l spa";
    private var language = "-l lepid" // Lepidoptera training set

    /**
     * Sets the name of the TIFF image file from which tesseract is to try to
     * extract text.
     *
     * @param aTargetFile filename, including path, of the file that is to be OCRed.
     * @throws OCRReadException if the file cannot be read.
     */
    @Throws(OCRReadException::class)
    fun setTarget(aTargetFile: String) {
        target = if (File(aTargetFile).canRead()) {
            aTargetFile
        } else {
            throw OCRReadException("Can't read file: $aTargetFile")
        }
    }

    /**
     * Set the language to use with Tesseract, use "eng" for english, passed
     * to tesseract as "-l lang".  Represents the leading string for the tesseract
     * training files (e.g. eng for eng.normproto, eng.word-dawg, etc).  Default is "lepid"
     * for custom scientific name/author training files.
     *
     * @param lang
     */
    fun setLang(lang: String?) {
        language = "-l $lang"
    }

    @Throws(OCRReadException::class)
    fun getOCRText(lang: String?): String {
        setLang(lang)
        return oCRText
    }// $ is returned by tesseract to indicate italics
// @ is returned by tesseract to indicate bold
// | and _ are common interpretations of QR barcode blocks
// Run tesseract to OCR the target file.
    // Create and start a reader for the error stream
    // run the process and wait for the exit value

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.OCR#getOCRText()
     */
    @get:Throws(OCRReadException::class)
    val oCRText: String
        get() {
            var result = StringBuffer()
            var resultReader: BufferedReader? = null
            log!!.debug("in TesseractOCR.OCRText 1")
            try {
                log.debug("in TesseractOCR.OCRText 2")
                val r = Runtime.Runtime
                log.debug("in TesseractOCR.OCRText 3")
                // Run tesseract to OCR the target file.
                val runCommand: String = (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_TESSERACT_EXECUTABLE)
                        + target + " " + tempFileBase + " " + language)
                log.debug("in TesseractOCR.OCRText 4")
                val proc = r!!.exec(runCommand)
                log.debug("in TesseractOCR.OCRText 5")
                println(runCommand)
                log.debug("in TesseractOCR.OCRText 6 runCommand is $runCommand")
                // Create and start a reader for the error stream
                val errorReader = StreamReader(proc!!.errorStream, "ERROR")
                errorReader.run()
                log.debug("in TesseractOCR.OCRText 7")
                // run the process and wait for the exit value
                val exitVal = proc.waitFor()
                println("Tesseract Exit Value: $exitVal")
                log.debug("in TesseractOCR.OCRText 8 exitVal is $exitVal")
                if (exitVal == 0) {
                    resultReader = BufferedReader(
                            InputStreamReader(
                                    FileInputStream("$tempFileBase.txt"),
                                    StandardCharsets.UTF_8)
                    )
                    var s: String?
                    while (resultReader.readLine().also({ s = it }) != null) {
                        result.append(s)
                        result.append("\n")
                    }
                    resultReader.close()
                } else {
                    throw OCRReadException("OCR process failed (exit value>0).")
                }
                log.debug("in TesseractOCR.OCRText 9")
                if (result.length > 0) {
                    val resString = result.toString()
                    // $ is returned by tesseract to indicate italics
// @ is returned by tesseract to indicate bold
// | and _ are common interpretations of QR barcode blocks
                    result = StringBuffer()
                    result.append(resString.replace("[|@\\$_]".toRegex(), ""))
                }
            } catch (eio: IOException) {
                log.error(eio)
                throw OCRReadException("OCR process failed with IO Exception.")
            } catch (ei: InterruptedException) {
                log.error(ei)
                throw OCRReadException("OCR process failed with Interrupted Exception.")
            } finally {
                if (resultReader != null) {
                    try {
                        resultReader.close()
                    } catch (e: IOException) {
                        log.debug(e)
                    }
                }
            }
            log.debug("in TesseractOCR.OCRText 10 result is $result")
            log.debug(result.toString())
            return result.toString()
        }

    companion object {
        private val log = LogFactory.getLog(TesseractOCR::class.java)
        /**
         * Run TesseractOCR from the command line to OCR a single file, intended as a test of installation.
         * Displays the content of the tesseract output file to standard output.
         * Could be used as a shell wrapper to run tesseract and put the results on standard output to be
         * piped elsewhere.
         *
         * @param args name of the TIFF file to try to OCR.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size < 1) {
                println("Usage:")
                println("java TesseractOCR <inputfilename> [<lang>]")
                System.exit(1)
            } else {
                val o = TesseractOCR()
                try {
                    if (args.size > 1) {
                        o.setLang(args[1])
                    }
                    o.setTarget(args[0])
                    println(o.oCRText)
                    System.exit(0)
                } catch (e: OCRReadException) {
                    System.err.println(e.message)
                    e.printStackTrace()
                    System.exit(2)
                }
            }
        }
    }
}
