/**
 * PositionTemplate.java
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
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Template
import edu.harvard.mcz.imagecapture.exceptions.*
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector
import edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycle
import org.apache.commons.logging.LogFactory
import java.awt.Dimension
import java.io.File
import java.util.*

/**
 * Description of the coordinates within an image where parts of the image to pass to
 * barcode reading software, OCR software, and which parts are to be displayed as containing
 * a specimen image or specimen labels.   Wrapper for persistent Template class.  Provides special
 * case template TEMPLATE_NO_COMPONENT_PARTS for handling any size image that isn't templated.
 * Provides hard coded default template.  Provides methods to get/set coordinates as Dimension
 * objects as a convenience over Template's get/set of individual heights and widths as integers.
 *
 *
 * DefaultPositionTemplateDetector makes the assumption that a template can be uniquely identified
 * by the location of the barcode in the image.
 *
 *
 * Each template must have the barcode in a uniquely different place.
 */
class PositionTemplate {
    /**
     * @param imageSize the imageSize to set
     */
    var imageSize // Pixel dimensions of the image
            : Dimension? = null
    /**
     * @return the barcodePosition
     */
    /**
     * @param barcodePosition the barcodePosition to set
     *///TODO: add define/save templates
//TODO: add retrieval of template from file.
    var barcodeULPosition // Location of upper left corner of area to scan for barcode in image.
            : Dimension? = null
        set
        get() = field
    /**
     * @param barcodeSize the barcodeSize to set
     */
    var barcodeSize // Size of area in image to scan for barcode.
            : Dimension? = null
    /**
     * @return the specimenPosition
     */
    /**
     * @param specimenPosition the specimenPosition to set
     */
    var specimenPosition // Location of upper left corner of part of image containing specimen.
            : Dimension? = null
    /**
     * @return the specimenSize
     */
    /**
     * @param specimenSize the specimenSize to set
     */
    var specimenSize: Dimension? = null
    /**
     * @return the textPosition
     */
    /**
     * @param textPosition the textPosition to set
     */
    var textPosition // Unit tray label with current determination
            : Dimension? = null
    /**
     * @return the textSize
     */
    /**
     * @param textSize the textSize to set
     */
    var textSize: Dimension? = null
    /**
     * @return the labelSize
     */
    /**
     * @return the labelSize
     */
    /**
     * @param utLabelSize the utLabelSize to set
     */
    /**
     * @param labelSize the labelSize to set
     */
    var uTLabelsSize // Specimen labels from pin.
            : Dimension? = null
        get() = utLabelSize
        set(utLabelSize) {
            this.utLabelSize = utLabelSize
        }
    /**
     * @return the labelPosition
     */
    /**
     * @return the labelPosition
     */
    /**
     * @param utLabelPosition the utLabelPosition to set
     */
    /**
     * @param labelPosition the labelPosition to set
     */
    var uTLabelsPosition: Dimension? = null
        get() = utLabelPosition
        set(utLabelPosition) {
            this.utLabelPosition = utLabelPosition
        }
    private var utLabelSize // Specimen labels from unit tray.
            : Dimension? = null
    private var utLabelPosition: Dimension? = null
    var utBarcodeSize // Barcode on UnitTrayLabel
            : Dimension? = null
    var utBarcodePosition: Dimension? = null
    private var referenceImage // Filename of reference image for this template.
            : ICImage? = null
    /**
     * @return the templateName
     */
    /**
     * @param templateName the templateName to set
     */
    /**
     * Get the free text descriptive name of the position template for potential display to a person.
     * Use getTemplateIdentifier() to identify templates in code.
     *
     * @return the descriptive name of the template.
     */
    var name // free text description of the template
            : String? = null
        get() = field
        set
    /**
     * Get the identifying name of this position template.  This name is fixed for an instance
     * during its construction.   This name corresponds to one of the strings returned by
     * PositionTemplate.getTemplates();  Redundant with getTemplateIdentifier().
     *
     * @return the identifier of the template in use in this instance of PositionTemplate.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     */
    /**
     * @param templateId the templateId to set
     */
    var templateId // the identifying string for this template = TEMPLATE_ constants
            : String? = null
    /**
     * @return true if this PositionTemplate can be edited by the user.
     */
    var isEditable // false for TEMPLATE_s, true for DB records. = false
        private set

    /**
     * Use a (currently hardcoded) default template for which pixel coordinates on the
     * image contain which information.
     */
    constructor() {
        useDefaultValues()
    }

    /**
     * Create a new (probably editable) template based on the default template for which pixel coordinates on the
     * image contain which information.
     *
     * @param editable true for an editable template.
     */
    constructor(editable: Boolean) {
        useDefaultValues()
        isEditable = editable
    }

    /**
     * Use a template defined by a PositionTemplate.TEMPLATE_* constant or potentially from
     * another valid source.  A template defines which pixel coordinates on the image contain
     * which information.   The template cannot be changed for an instance of PositionTemplate once
     * it has been instantiated.  This constructor is the only means of setting the template to use.
     * Create a new instance of PositionTemplate if you wish to use a different template.
     *
     *
     * The list of available templateIds can be retrieved with PositionTemplate.getTemplates().
     *
     * @param templateToUse the templateID of the template to use in this instance of PositionTemplate.
     * @throws NoSuchTemplateException when templateToUse doesn't exist.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     * @see edu.harvard.mcz.imagecapture.exceptions.NoComponentPartsTemplateException
     */
    constructor(templateToUse: String) {
        val found = loadTemplateValues(templateToUse)
        if (!found) {
            throw NoSuchTemplateException("No such template as $templateToUse")
        }
    }

    /**
     * Construct a PositionTemplate from a Template.
     *
     * @param templateInstance
     */
    constructor(templateInstance: Template) {
        name = templateInstance.name
        templateId = templateInstance.templateId
        imageSize = Dimension(templateInstance.imageSizeX, templateInstance.imageSizeY)
        barcodeULPosition = Dimension(templateInstance.barcodePositionX, templateInstance.barcodePositionY)
        barcodeSize = Dimension(templateInstance.barcodeSizeX, templateInstance.barcodeSizeY)
        specimenPosition = Dimension(templateInstance.specimenPositionX, templateInstance.specimenPositionY)
        specimenSize = Dimension(templateInstance.specimenSizeX, templateInstance.specimenSizeY)
        uTLabelsPosition = Dimension(templateInstance.labelPositionX, templateInstance.labelPositionY)
        uTLabelsSize = Dimension(templateInstance.labelSizeX, templateInstance.labelSizeY)
        utLabelPosition = Dimension(templateInstance.utLabelPositionX, templateInstance.utLabelPositionY)
        utLabelSize = Dimension(templateInstance.utLabelSizeX, templateInstance.utLabelSizey)
        utBarcodePosition = Dimension(templateInstance.utBarcodePositionX, templateInstance.utBarcodePositionY)
        utBarcodeSize = Dimension(templateInstance.utBarcodeSizeX, templateInstance.utBarcodeSizeY)
        textPosition = Dimension(templateInstance.textPositionX, templateInstance.textPositionY)
        textSize = Dimension(templateInstance.textSizeX, templateInstance.textSizeY)
    }

    /**
     * Hardcoded default template values are defined here.
     */
    private fun useDefaultValues() {
        name = "Default template for initial test image."
        templateId = TEMPLATE_DEFAULT
        // Set default values
        imageSize = Dimension(2848, 4272)
        // Approximately 300 x 300 pixel area in upper right corner of 12 mega-pixel image.
        barcodeULPosition = Dimension(2490, 90)
        barcodeSize = Dimension(300, 300)
        // lower half of image
        specimenPosition = Dimension(0, 2200)
        specimenSize = Dimension(2848, 1900)
        // text to ocr at top left
        textPosition = Dimension(110, 105)
        textSize = Dimension(1720, 700)
        // pin labels on right above half
        uTLabelsPosition = Dimension(1300, 700)
        uTLabelsSize = Dimension(1500, 1300)
        // labels on left above half
        utLabelPosition = Dimension(0, 850) // not defined as different from labels in test image
        utLabelSize = Dimension(1500, 1161)
        // QRCode barcode on unit tray label encoding unit tray label fields.
        utBarcodePosition = Dimension(1200, 105)
        utBarcodeSize = Dimension(950, 800)
        referenceImage = null
        isEditable = false
    }

    /**
     * Get the identifying name of this position template.  This name is fixed for an instance
     * during its construction.   This name corresponds to one of the strings returned by
     * PositionTemplate.getTemplates();   Redundant with getTemplateId()
     *
     * @return the identifier of the template in use in this instance of PositionTemplate.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateId
     */
    @get:Deprecated("")
    val templateIdentifier: String?
        get() = templateId

    //	/**
//	 * @return the utLabelSize
//	 */
//	public Dimension getUtLabelSize() {
//		return utLabelSize;
//	}
//
    //	/**
//	 * @return the utLabelPosition
//	 */
//	public Dimension getUtLabelPosition() {
//		return utLabelPosition;
//	}

    /**
     * private method, to use a different template, instantiate a new PositionTemplate.
     * Contains hardcoded templates other than the default
     *
     *
     * TODO: Extend to external persistent templates that aren't hard coded.
     *
     * @param templateToUse
     * @return true if the template was found.
     */
    private fun loadTemplateValues(templateToUse: String): Boolean {
        var found = false
        if (templateToUse == TEMPLATE_NO_COMPONENT_PARTS) {
            name = "Whole image only."
            templateId = templateToUse
            // Set all values to null
            imageSize = null
            barcodeULPosition = null
            barcodeSize = null
            specimenPosition = null
            specimenSize = null
            textPosition = null
            textSize = null
            uTLabelsPosition = null
            uTLabelsSize = null
            utLabelPosition = null
            utLabelSize = null
            utBarcodePosition = null
            utBarcodeSize = null
            isEditable = false
            referenceImage = null
            found = true
        }
        if (templateToUse == TEMPLATE_TEST_1) {
            name = "Cannon DigitalRebel L with small test carrier."
            templateId = templateToUse
            // Set default values
            imageSize = Dimension(2848, 4272)
            // Approximately 300 x 300 pixel area in upper right corner of 12 mega-pixel image.
            barcodeULPosition = Dimension(2280, 0)
            barcodeSize = Dimension(550, 310)
            // lower half of image
            specimenPosition = Dimension(0, 2140)
            specimenSize = Dimension(2847, 2130)
            // text to ocr at top left of image
            textPosition = Dimension(110, 105)
            textSize = Dimension(1720, 700)
            // pin labels on right side of upper half
            uTLabelsPosition = Dimension(1500, 780)
            uTLabelsSize = Dimension(1348, 1360)
            // unit tray labels on left side of upper half
            utLabelPosition = Dimension(0, 780)
            utLabelSize = Dimension(1560, 1360)
            // QRCode barcode on unit tray label encoding unit tray label fields.
            utBarcodePosition = Dimension(1200, 105)
            utBarcodeSize = Dimension(950, 800)
            isEditable = false
            referenceImage = null
            found = true
        }
        if (templateToUse == TEMPLATE_DEFAULT) {
            useDefaultValues()
            isEditable = false
            found = true
        }
        if (!found) {
            found = loadByTemplateId(templateToUse)
        }
        return found
    }

    fun loadByTemplateId(aTemplateId: String?): Boolean {
        var result = false
        var t_result: Template? = null
        val tls = TemplateLifeCycle()
        t_result = tls.findById(aTemplateId)
        if (t_result != null) { // We know this is a valid template, so set the id
            templateId = aTemplateId
            // retrieve the rest of the values
            name = t_result.name
            imageSize = Dimension(t_result.imageSizeX, t_result.imageSizeY)
            barcodeULPosition = Dimension(t_result.barcodePositionX, t_result.barcodePositionY)
            barcodeSize = Dimension(t_result.barcodeSizeX, t_result.barcodeSizeY)
            specimenPosition = Dimension(t_result.specimenPositionX, t_result.specimenPositionY)
            specimenSize = Dimension(t_result.specimenSizeX, t_result.specimenSizeY)
            textPosition = Dimension(t_result.textPositionX, t_result.textPositionY)
            textSize = Dimension(t_result.textSizeX, t_result.textSizeY)
            uTLabelsPosition = Dimension(t_result.labelPositionX, t_result.labelPositionY)
            uTLabelsSize = Dimension(t_result.labelSizeX, t_result.labelSizeY)
            utLabelPosition = Dimension(t_result.utLabelPositionX, t_result.utLabelPositionY)
            utLabelSize = Dimension(t_result.utLabelSizeX, t_result.utLabelSizey)
            utBarcodePosition = Dimension(t_result.utBarcodePositionX, t_result.utBarcodePositionY)
            utBarcodeSize = Dimension(t_result.utBarcodeSizeX, t_result.utBarcodeSizeY)
            isEditable = true
            referenceImage = t_result.referenceImage
            result = true
        }
        return result
    }

    @Throws(BadTemplateException::class)
    fun populateTemplateFromPositionTemplate(templateInstance: Template?) {
        if (templateId == null || templateId!!.trim { it <= ' ' } == "") {
            throw BadTemplateException("Can't save a template with a blank templateID")
        }
        if (imageSize == null) { // Note: if persistence of TEMPLATE_NO_COMPONENT_PARTS is desired, this
// needs to be changed to test for that template.
            throw BadTemplateException("Can't save a template with no image size.")
        }
        if (templateInstance == null) {
            throw BadTemplateException("Can't save a null template.")
        }
        templateInstance.templateId = templateId
        templateInstance.name = name
        templateInstance.editable = isEditable
        templateInstance.referenceImage = referenceImage
        templateInstance.imageSizeX = imageSize!!.width
        templateInstance.imageSizeY = imageSize!!.height
        templateInstance.barcodePositionX = barcodeULPosition!!.width
        templateInstance.barcodePositionY = barcodeULPosition!!.height
        templateInstance.barcodeSizeX = barcodeSize!!.width
        templateInstance.barcodeSizeY = barcodeSize!!.height
        templateInstance.specimenPositionX = specimenPosition!!.width
        templateInstance.specimenPositionY = specimenPosition!!.height
        templateInstance.specimenSizeX = specimenSize!!.width
        templateInstance.specimenSizeY = specimenSize!!.height
        templateInstance.textPositionX = textPosition!!.width
        templateInstance.textPositionY = textPosition!!.height
        templateInstance.textSizeX = textSize!!.width
        templateInstance.textSizeY = textSize!!.height
        templateInstance.labelPositionX = uTLabelsPosition!!.width
        templateInstance.labelPositionY = uTLabelsPosition!!.height
        templateInstance.labelSizeX = uTLabelsSize!!.width
        templateInstance.labelSizeY = uTLabelsSize!!.height
        templateInstance.utLabelPositionX = utLabelPosition!!.width
        templateInstance.utLabelPositionY = utLabelPosition!!.height
        templateInstance.utLabelSizeX = utLabelSize!!.width
        templateInstance.utLabelSizey = utLabelSize!!.height
        templateInstance.utBarcodePositionX = utBarcodePosition!!.width
        templateInstance.utBarcodePositionY = utBarcodePosition!!.height
        templateInstance.utBarcodeSizeX = utBarcodeSize!!.width
        templateInstance.utBarcodeSizeY = utBarcodeSize!!.height
    }

    @Throws(BadTemplateException::class, SaveFailedException::class)
    fun persist() {
        if (templateId == null || templateId!!.trim { it <= ' ' } == "") {
            throw BadTemplateException("Can't save a template with a blank templateID")
        }
        if (imageSize == null) { // Note: if persistence of TEMPLATE_NO_COMPONENT_PARTS is desired, this
// needs to be changed to test for that template.
            throw BadTemplateException("Can't save a template with no image size.")
        }
        val tls = TemplateLifeCycle()
        var templateInstance: Template = tls.findById(templateId)
        if (templateInstance == null) {
            templateInstance = Template()
        }
        populateTemplateFromPositionTemplate(templateInstance)
        // save/update
        tls.attachDirty(templateInstance)
    }

    /**
     * @return a String representing the filename of the referenceImage for this
     * PoistionTemplate.
     */
    fun getReferenceImage(): ICImage? {
        return referenceImage
    }

    fun setReferenceImage(anImage: ICImage?) {
        referenceImage = anImage
    }

    val referenceImageFilePath: String
        get() {
            var result = ""
            val base: String = Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
            if (referenceImage != null && referenceImage.getPath() != null && referenceImage.getFilename() != null) {
                result = base + referenceImage.getPath() + referenceImage.getFilename()
            }
            return result
        }

    companion object {
        /**
         * Special case template for images that aren't split into component parts
         * with a template.
         *
         * @see edu.harvard.mcz.imagecapture.exceptions.NoComponentPartsTemplateException
         */
        val TEMPLATE_NO_COMPONENT_PARTS: String? = "Whole Image Only"
        /**
         * The hardcoded default template.
         */
        val TEMPLATE_DEFAULT: String? = "Default template"
        val TEMPLATE_TEST_1: String? = "Small Template 1"
        private val log = LogFactory.getLog(PositionTemplate::class.java)//here it cycles through all the templates (if you leave template.default=Default template and template.default2=)//System.out.println(st2.nextElement());
        //templateIdList.add(defaultTemplatesPropertiesVal); //todo string tokenizer
//allie: here load the props file: leave the default and config the other var.
//template.default=Default template
//template.default2=ETHZ_template1
//template.default2=ETHZ_template1,ETHZ_template2
        /**
         * Fetch the list of valid template names (including the no component parts template).
         * Use these names in the constructor PositionTemplate(String templateToUse);
         *
         * @return a list of the identifiers of the currently available templates.
         */
        val templateIds: MutableList<String?>
            get() {
                log!!.debug("in getTemplateIds()")
                val templates = arrayOf(TEMPLATE_TEST_1, TEMPLATE_DEFAULT, TEMPLATE_NO_COMPONENT_PARTS)
                val temp: MutableList<String?> = Arrays.asList(*templates)
                val templateIdList = ArrayList<String?>()
                val defaultTemplatesPropertiesVal: String = Singleton.getProperties().getProperties().getProperty(
                        ImageCaptureProperties.Companion.KEY_DEFAULT_TEMPLATES)
                //allie: here load the props file: leave the default and config the other var.
//template.default=Default template
//template.default2=ETHZ_template1
//template.default2=ETHZ_template1,ETHZ_template2
                if ("" != defaultTemplatesPropertiesVal) {
                    val st2 = StringTokenizer(defaultTemplatesPropertiesVal, ",")
                    while (st2.hasMoreElements()) { //System.out.println(st2.nextElement());
                        val templ = (st2.nextElement() as String)
                        log.debug("next template:::$templ")
                        templateIdList.add(templ)
                    }
                    //templateIdList.add(defaultTemplatesPropertiesVal); //todo string tokenizer
                    log.debug("defaultTemplatesPropertiesVal::::: $defaultTemplatesPropertiesVal")
                } else { //here it cycles through all the templates (if you leave template.default=Default template and template.default2=)
                    log.debug("it will cycle through all templates::::: $defaultTemplatesPropertiesVal")
                    for (i in temp.indices) {
                        templateIdList.add(temp[i])
                    }
                }
                val tls = TemplateLifeCycle()
                var persistentTemplates: MutableList<Template?> = tls.findAll()
                if (persistentTemplates == null) {
                    tls.cleanUpReferenceImage()
                    persistentTemplates = tls.findAll()
                }
                val iter = persistentTemplates.listIterator()
                while (iter.hasNext()) {
                    templateIdList.add(iter.next()!!.templateId)
                }
                return templateIdList
            }// TODO Auto-generated catch block

        //TemplateLifeCycle tls = new TemplateLifeCycle();
//List<Template> templates = tls.findAll();
        val templates: MutableList<PositionTemplate?>
            get() { //TemplateLifeCycle tls = new TemplateLifeCycle();
//List<Template> templates = tls.findAll();
                val results = ArrayList<PositionTemplate?>()
                val templateIds = templateIds
                val i = templateIds.listIterator()
                while (i.hasNext()) {
                    try {
                        results.add(PositionTemplate(i.next()!!))
                    } catch (e: NoSuchTemplateException) { // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
                return results
            }

        /**
         * Given an ICImage, look up the template for that image in the database, if none, run a template detector
         * to determine the template for the image.
         *
         * @param image the ICImage for which the PositionTemplate is to be returned.
         * @return the PositionTemplate for this image indicating what information is where in the image.
         * @throws ImageLoadException
         */
        @Throws(ImageLoadException::class)
        fun findTemplateForImage(image: ICImage): PositionTemplate? {
            var result: PositionTemplate? = null
            //TODO: stored path may need separator conversion for different systems.
//String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
            var path: String = image.getPath()
            if (path == null) {
                path = ""
            }
            //File fileToCheck = new File(startPointName + path + image.getFilename());
            val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
            var templateId: String = image.getTemplateId()
            if (templateId == null || templateId == "") { // No template is defined in the database for this image file.
// Check the image with a template detector.
                val detector: PositionTemplateDetector = DefaultPositionTemplateDetector()
                templateId = try {
                    detector.detectTemplateForImage(fileToCheck)
                } catch (e: UnreadableFileException) {
                    throw ImageLoadException(e.message)
                }
            }
            // There is a template defined in the database for this image file
// Check to see if this is a valid template.
            try {
                result = PositionTemplate(templateId)
                // Template exists, load image with it.
            } catch (e: NoSuchTemplateException) { // This template isn't known.  Log the problem.
                log!!.error("Image database record includes an unknown template. " + e.message)
                // Use the no component parts template instead.
                try {
                    result = PositionTemplate(TEMPLATE_NO_COMPONENT_PARTS!!)
                } catch (e1: NoSuchTemplateException) {
                    log.fatal("TEMPLATE_NO_COMPONENT_PARTS produced a NoSuchTemplateException.")
                    log.fatal(e1)
                    log.trace(e1)
                    ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
                }
            }
            return result
        }
    }
}
