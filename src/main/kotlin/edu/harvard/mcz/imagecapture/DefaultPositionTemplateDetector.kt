/**
 * DefaultPositionTemplateDetector.java
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


import edu.harvard.mcz.imagecapture.CandidateImageFile
import edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetector
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector
import org.apache.commons.logging.LogFactory
import java.io.File

/**
 * DefaultPositionTemplateDetector find a template by the position of a barcode in an image file.
 * This class makes the assumption that a template can be uniqely idenfified by the location of the
 * barcode in the image.  Each template must have the bardcode in a uniquely different place.
 *
 * @see edu.harvard.mcz.imagecapture.PositionTemplate
 */
class DefaultPositionTemplateDetector : PositionTemplateDetector {
    @Throws(UnreadableFileException::class)
    override fun detectTemplateForImage(anImageFile: File): String? {
        return detectTemplateForImage(anImageFile, null)
    }

    @Throws(UnreadableFileException::class)
    override fun detectTemplateForImage(scannableFile: CandidateImageFile): String? {
        return detectTemplateForImage(scannableFile.File, scannableFile)
    }

    @Throws(UnreadableFileException::class)
    protected fun detectTemplateForImage(anImageFile: File, scannableFile: CandidateImageFile?): String? {
        var scannableFile: CandidateImageFile? = scannableFile
        var result: String = PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS
        // We will be calling getBarcodeText(PositionTemplate aTemplate) below, so it doesn't matter
// that we are instatiating the scannable file here with a default template.
//CandidateImageFile scannableFile = null;
        if (scannableFile == null) {
            scannableFile = CandidateImageFile(anImageFile, PositionTemplate())
        }
        val templates: MutableList<String?> = PositionTemplate.Companion.TemplateIds
        // iterate through templates and check until the first template where a barcode is found
        val i = templates.listIterator()
        var found = false
        while (i.hasNext() && !found) {
            try { // get the next template from the list
                val template = PositionTemplate(i.next()!!)
                log!!.debug("Testing template: " + template.TemplateId)
                if (template.TemplateId == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // skip, this is the default result if no other is found.
                } else { // Check to see if the barcode is in the part of the template
// defined by getBarcodeULPosition and getBarcodeSize.
                    val text: String = scannableFile.getBarcodeText(template)
                    log.debug("Found:[" + text + "] status=" + scannableFile.CatalogNumberBarcodeStatus)
                    if (scannableFile.CatalogNumberBarcodeStatus == CandidateImageFile.Companion.RESULT_BARCODE_SCANNED) { // RESULT_BARCODE_SCANNED is only returned if the reader read a QR code barcode inside
// the area defined by the template for containing the barcode.
// If we got here, we found a barcode in the expected place and know which template
// to return.
                        found = true
                        log.debug("Match to:" + template.TemplateId)
                        result = template.TemplateId
                    }
                }
            } catch (e: NoSuchTemplateException) { // Ending up here means a serious error in PositionTemplate
// as the list of position templates returned by getTemplates() includes
// an entry that isn't recognized as a valid template.
                log!!.fatal("Fatal error.  PositionTemplate.Templates includes an item that isn't a valid template.")
                log.trace(e)
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
            }
        }
        return result
    }

    companion object {
        private val log = LogFactory.getLog(DefaultPositionTemplateDetector::class.java)
    }
}
