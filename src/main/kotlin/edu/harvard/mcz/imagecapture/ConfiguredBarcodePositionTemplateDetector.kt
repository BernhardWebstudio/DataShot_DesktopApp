/**
 * ConfiguredBarcodePositionTemplateDetector.java
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
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector
import org.apache.commons.logging.LogFactory
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

/**
 * ConfiguredBarcodePositionTemplateDetector find a template by the position of a barcode in an image file
 * without recourse to construction of a a CandidateImageFile instance, and checks for a catalog
 * number barcode that follows the configured pattern in the templated position, not just any readable barcode.
 *
 *
 * This class makes the assumption that a template can be uniquely identified by the location of the
 * barcode in the image.  Each template must have the barcode in a uniquely different place.
 *
 * @see edu.harvard.mcz.imagecapture.PositionTemplate
 */
class ConfiguredBarcodePositionTemplateDetector : PositionTemplateDetector {
    @Throws(UnreadableFileException::class)
    override fun detectTemplateForImage(anImageFile: File): String? {
        return detectTemplateForImage(anImageFile, null, false)
    }

    @Throws(UnreadableFileException::class)
    override fun detectTemplateForImage(scannableFile: CandidateImageFile): String? {
        return detectTemplateForImage(scannableFile.getFile(), scannableFile, false)
    }

    @Throws(UnreadableFileException::class)
    fun detectTemplateForImage(anImageFile: File, scannableFile: CandidateImageFile?, quickCheck: Boolean): String? { // Set default response if no template is found.
        var result: String = PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS
        // Read the image file, if possible, otherwise throw exception.
        if (!anImageFile.canRead()) {
            throw UnreadableFileException("Unable to read " + anImageFile.name)
        }
        // skip all following intensive, unnecessary computation if possible
        val defaultTemplate: String = Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_DEFAULT_TEMPLATES)
        if (Singleton.getProperties().testDefaultTemplate() && defaultTemplate !== PositionTemplate.Companion.TEMPLATE_DEFAULT) {
            return defaultTemplate
        }
        var image: BufferedImage? = null
        image = try {
            ImageIO.read(anImageFile)
        } catch (e: IOException) {
            throw UnreadableFileException("IOException trying to read " + anImageFile.name)
        }
        // iterate through templates and check until the first template where a barcode is found
        val templates: MutableList<String?> = PositionTemplate.Companion.getTemplateIds()
        log!!.debug("Detecting template for file: " + anImageFile.absolutePath)
        log.debug("list of templates: $templates")
        val i = templates.listIterator()
        var found = false
        while (i.hasNext() && !found) {
            try { // get the next template from the list
                val template = PositionTemplate(i.next()!!)
                log.debug("Testing template: " + template.getTemplateId())
                if (template.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // skip, this is the default result if no other is found.
                } else {
                    if (image.getWidth().toDouble() == template.getImageSize().getWidth()) { // Check to see if the barcode is in the part of the template
// defined by getBarcodeULPosition and getBarcodeSize.
                        var text: String
                        text = if (scannableFile == null) {
                            scannableFile!!.getBarcodeTextFromImage(image, template, quickCheck)
                        } else {
                            scannableFile.getBarcodeText(template)
                        }
                        log.debug("Found:[$text] ")
                        if (text.length > 0) { // a barcode was scanned
// Check to see if it matches the expected pattern.
// Use the configured barcode matcher.
                            if (Singleton.getBarcodeMatcher().matchesPattern(text)) {
                                found = true
                                log.debug("Match to:" + template.getTemplateId())
                                result = template.getTemplateId()
                            }
                        }
                    } else {
                        log.debug("Skipping as template " + template.getTemplateId() + " is not same size as image. ")
                    }
                }
            } catch (e: NoSuchTemplateException) { // Ending up here means a serious error in PositionTemplate
// as the list of position templates returned by getTemplates() includes
// an entry that isn't recognized as a valid template.
                log.fatal("Fatal error.  PositionTemplate.getTemplates() includes an item that isn't a valid template.")
                log.trace(e)
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
            }
        }
        return result
    }

    companion object {
        private val log = LogFactory.getLog(ConfiguredBarcodePositionTemplateDetector::class.java)
    }
}
