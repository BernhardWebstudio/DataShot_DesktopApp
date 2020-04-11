/**
 * ImageCaptureProperties.java
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
import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import org.apache.commons.logging.LogFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*
import javax.swing.table.AbstractTableModel

/**
 * Filesystem persistence and retrieval of properties for ImageCapture Application.
 * Includes constants for key names to use in properties file, and definition of default
 * values for properties to go with these keys if they aren't defined in the persistent file.
 */
class ImageCaptureProperties : AbstractTableModel() {
    var properties: Properties? = null
        private set
    private var propertiesFilename: String? = null
    private var propertiesFilePath: StringBuffer? = null
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    override fun getColumnClass(columnIndex: Int): Class<*> {
        return String::class.java
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    override fun getColumnName(column: Int): String {
        var returnValue = ""
        if (column == 0) {
            returnValue = "Key"
        }
        if (column == 1) {
            returnValue = "Property value"
        }
        return returnValue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        var returnValue = false
        if (columnIndex == 1) {
            returnValue = true
        }
        return returnValue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        if (columnIndex == 1) {
            val p = properties!!.keys()
            var element = 0
            while (p!!.hasMoreElements()) {
                val e = (p.nextElement() as String)
                if (element == rowIndex) {
                    properties!!.setProperty(e, value as String?)
                }
                element++
            }
        }
    }

    /**
     * Test to see if the KEY_TEMPLATEDEFAULT matches a valid PositionTemplate.
     * Change to PositionTemplate.TEMPLATE_DEFAULT and Log error if it does not.
     * Note: if the KEY_TEMPLATEDEFAULT property does not match a hardcoded
     * PositionTemplate, a database lookup will be triggered and, if the request
     * is being made at application launch, a login dialog will be launched.
     *
     *
     * Take no action if there is no match to the KEY_TEMPLATEDEFAULT
     *
     * @return true if template in properties exists, false if no match to key or
     * if template was changed.
     */
    fun testDefaultTemplate(): Boolean {
        var result = false
        if (properties!!.containsKey(KEY_DEFAULT_TEMPLATES)) {
            val templateId = properties!!.getProperty(KEY_DEFAULT_TEMPLATES)
            try {
                val template = PositionTemplate(templateId!!)
                template.javaClass // added to suppress findbugs DLS_DEAD_LOCAL_STORE
                // no exception thrown, this template is OK.
                result = true
            } catch (e: NoSuchTemplateException) { // Template isn't recognized, set property to default template.
                properties!!.setProperty(KEY_DEFAULT_TEMPLATES, PositionTemplate.Companion.TEMPLATE_DEFAULT)
            }
        }
        return result
    }

    /**
     * Make sure required properties are present as keys, if they aren't add
     * them with default values.  This is where the default properties are defined.
     */
    private fun checkDefaults() {
        if (!properties!!.containsKey(KEY_SPECIFIC_COLLECTION)) { // location in collection to use, if not default provided from KEY_COLLECTION
// in LocationInCollection.
// default to ETHZ; maybe '""' would be a better choice? ;)
            properties!!.setProperty(KEY_SPECIFIC_COLLECTION, "ETHZ-ENT")
            log!!.debug("Forced configuration: ETHZ-ENT")
        }
        if (!properties!!.containsKey(KEY_COLLECTION)) { // Root of the path of the place where all image files should be stored.
            properties!!.setProperty(KEY_COLLECTION, COLLECTION_MCZENT)
        } else {
            when (properties!![KEY_COLLECTION].toString().trim { it <= ' ' }) {
                COLLECTION_ETHZENT -> log!!.debug("Configured for $COLLECTION_ETHZENT")
                COLLECTION_MCZENT -> log!!.debug("Configured for $COLLECTION_MCZENT")
                else -> {
                    log!!.error("Unrecognized collection: " + properties!![KEY_COLLECTION].toString())
                    log.error("Allowed values for " + KEY_COLLECTION + " are " +
                            COLLECTION_MCZENT + " or " +
                            COLLECTION_ETHZENT)
                }
            }
        }
        if (!properties!!.containsKey(KEY_IMAGEBASE)) { // Root of the path of the place where all image files should be stored.
//properties.setProperty(KEY_IMAGEBASE,"/mount/lepidopteraimages");
            properties!!.setProperty(KEY_IMAGEBASE, "C:\\Users\\Digitizing\\Desktop\\images-test\\")
            //properties.setProperty(KEY_IMAGEBASE,"/Users/altheaparker/Desktop/entomology-builds/TestImages");
        }
        if (!properties!!.containsKey(KEY_IMAGEBASEURI)) { // URI to the root of the path of the place where all image files should be stored.
            properties!!.setProperty(KEY_IMAGEBASEURI, "http://mczbase.mcz.harvard.edu/specimen_images/")
        }
        if (!properties!!.containsKey(KEY_IMAGEREGEX)) { // Regular expression to identify image filenames for processing.
            properties!!.setProperty(KEY_IMAGEREGEX, ImageCaptureApp.REGEX_IMAGEFILE)
        }
        if (!properties!!.containsKey(KEY_IMAGERESCALE)) { // Sizes to which to rescale width of unit tray label barcode to on retry.
            properties!!.setProperty(KEY_IMAGERESCALE, "400,600sharpen,600brighter,600dimmer,400sharpenbrighter")
        }
        if (!properties!!.containsKey(KEY_IMAGEZXINGALSOTRYHARDER)) { // Default value for choosing whether or not to also try harder with xzing.
            properties!!.setProperty(KEY_IMAGEZXINGALSOTRYHARDER, "true")
        }
        if (!properties!!.containsKey(KEY_TESSERACT_EXECUTABLE)) { // name of the tesseract executable, probably tesseract on unix, tesseract.exe on windows
            properties!!.setProperty(KEY_TESSERACT_EXECUTABLE, "tesseract ")
        }
        if (!properties!!.containsKey(KEY_CONVERT_EXECUTABLE)) {
            properties!!.setProperty(KEY_CONVERT_EXECUTABLE, "convert ")
        }
        if (!properties!!.containsKey(KEY_MOGRIFY_EXECUTABLE)) {
            properties!!.setProperty(KEY_MOGRIFY_EXECUTABLE, "mogrify ")
        }
        if (!properties!!.containsKey(KEY_CONVERT_PARAMETERS)) { // default ImageMagick convert properties used for JPG to TIFF conversion to
// prepare a file for tesseract.
            properties!!.setProperty(KEY_CONVERT_PARAMETERS, " -depth 8 -compress None -type Grayscale ")
        }
        if (!properties!!.containsKey(KEY_DETAILS_SCROLL)) { // default value is no scroll bars for SpecimenDetailsViewPane.
            properties!!.setProperty(KEY_DETAILS_SCROLL, "none")
        }
        if (!properties!!.containsKey(KEY_ENABLE_BROWSE)) { // default value is disabled browse on main menu.
            properties!!.setProperty(KEY_ENABLE_BROWSE, "false")
        }
        if (!properties!!.containsKey(KEY_DEFAULT_PREPARATION)) { // default preparation type
            properties!!.setProperty(KEY_DEFAULT_PREPARATION, "pinned")
        }
        if (!properties!!.containsKey(KEY_FILTER_LENGTH_THRESHOLD)) { // default value is disabled browse on main menu.
            properties!!.setProperty(KEY_FILTER_LENGTH_THRESHOLD, "3")
        }
        if (!properties!!.containsKey(KEY_SHOW_ALL_NUMBER_TYPES)) { // default value is disabled browse on main menu.
            properties!!.setProperty(KEY_SHOW_ALL_NUMBER_TYPES, "false")
        }
        if (!properties!!.containsKey(KEY_THUMBNAIL_HEIGHT)) { // default value is 120 pixels.
            properties!!.setProperty(KEY_THUMBNAIL_HEIGHT, "120")
        }
        if (!properties!!.containsKey(KEY_THUMBNAIL_WIDTH)) { // default value is 120 pixels.
            properties!!.setProperty(KEY_THUMBNAIL_WIDTH, "80")
        }
        if (!properties!!.containsKey(KEY_GENERATE_THUMBNAILS)) {
            properties!!.setProperty(KEY_GENERATE_THUMBNAILS, "true")
        }
        if (!properties!!.containsKey(KEY_REGEX_DRAWERNUMBER)) { // default value is 120 pixels.
            properties!!.setProperty(KEY_REGEX_DRAWERNUMBER, ImageCaptureApp.REGEX_DRAWERNUMBER)
        }
        if (!properties!!.containsKey(KEY_REDUNDANT_COMMENT_BARCODE)) { // default value is that images are expected to contain the barcode number
// in both the image and in its metadata.
            properties!!.setProperty(KEY_REDUNDANT_COMMENT_BARCODE, "true")
        }
        if (!properties!!.containsKey(KEY_LOGIN_SHOW_ADVANCED)) { // default value is closed advanced options (server) on login dialog.
            properties!!.setProperty(KEY_LOGIN_SHOW_ADVANCED, "false")
        }
        if (!properties!!.containsKey(KEY_DEFAULT_TEMPLATES)) {
            properties!!.setProperty(KEY_DEFAULT_TEMPLATES, PositionTemplate.Companion.TEMPLATE_DEFAULT)
        }
        if (!properties!!.containsKey(KEY_MAX_FIELD_HEIGHT)) {
            properties!!.setProperty(KEY_MAX_FIELD_HEIGHT, "70")
        }
        if (!properties!!.containsKey(KEY_EDITOR_IMPORTANCE)) {
            properties!!.setProperty(KEY_EDITOR_IMPORTANCE, "0.6")
        }
        if (!properties!!.containsKey(KEY_CONCURRENCY_LEVEL)) {
            properties!!.setProperty(KEY_CONCURRENCY_LEVEL, "16")
        }
    }

    /* Place where properties in this instance are persisted.
     *
     * @returns a text string representing the storage location from which this instance of
     * properties was loaded such ast the path and filename of the file from which the values for
     * this instance of properties was retrieved.
     */
    val propertiesSource: String
        get() = propertiesFilePath.toString()

    @Throws(Exception::class)
    protected fun loadProperties() {
        if (properties == null) {
            properties = Properties()
            var propertiesStream: FileInputStream? = null
            log!!.debug("Opening properties file: " + propertiesFilePath.toString())
            try {
                propertiesStream = FileInputStream(propertiesFilePath.toString())
                properties.load(propertiesStream)
                // test dump
/*Enumeration<Object> keys = properties.keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    log.debug("Got property: " + key + " with value " + properties.getProperty(key));
                }*/
// Test to see if all properties are set in the loaded file
                checkDefaults()
            } catch (e: FileNotFoundException) {
                println("Error: Properties file not found.")
                throw e
            } catch (ex: Exception) {
                println("Error loading properties.")
                println(ex.message)
                throw ex
            } finally {
                if (propertiesStream != null) {
                    propertiesStream.close()
                }
            }
        } else {
            println("Skipping properties loading as they are already loaded")
        }
    }

    @Throws(Exception::class)
    fun saveProperties() {
        var propertiesStream: FileOutputStream? = null
        try {
            println("Saving properties file: " + propertiesFilePath.toString())
            propertiesStream = FileOutputStream(propertiesFilePath.toString())
            properties.store(propertiesStream, ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.AppVersion + " Properties")
            propertiesStream.close()
        } catch (e: Exception) {
            println("Error saving properties.")
            println(e.message)
            e.printStackTrace()
            throw e
        } finally {
            if (propertiesStream != null) {
                propertiesStream.close()
            }
        }
    }

    val columnCount: Int
        get() = 2

    val rowCount: Int
        get() = properties!!.size()

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        var value: String? = ""
        val p = properties!!.keys()
        var element = 0
        while (p!!.hasMoreElements()) {
            val e = (p.nextElement() as String)
            if (element == rowIndex) {
                value = if (columnIndex == 0) {
                    e
                } else {
                    properties!!.getProperty(e)
                }
            }
            element++
        }
        return value
    }

    companion object {
        val COLLECTION_MCZENT: String? = "MCZ-ENT"
        val COLLECTION_ETHZENT: String? = "ETHZ-ENT"
        /**
         * The collection for which this deployment is configured to work with.
         */
        val KEY_COLLECTION: String? = "configuration.collection"
        /**
         * Value to use for the specific collection, empty string will use default for
         * the configured KEY_COLLECTION.
         */
        val KEY_SPECIFIC_COLLECTION: String? = "configuration.specificcollection"
        /**
         * The most recent location selected for scanning a barcode.
         */
        val KEY_LASTPATH: String? = "scanonebarcode.lastpath"
        /**
         * The most recent location selected for loading data from a file.
         */
        val KEY_LASTLOADPATH: String? = "fileload.lastpath"
        /**
         * Root of the path of the place where all image files should be stored.
         */
        val KEY_IMAGEBASE: String? = "images.basedirectory"
        /**
         * URI to the root of the path of the place where all image files should be stored,
         * that is, the URI on the image server that points to the same location as
         * KEY_IMAGEBASE does on the local filesystem.
         */
        val KEY_IMAGEBASEURI: String? = "images.basedirectoryurimap"
        /**
         * Regular expression that image files to be preprocessed must match.
         */
        val KEY_IMAGEREGEX: String? = "images.filenameregex"
        /**
         * Sizes to which to rescale width of unit tray label barcode to on retry of
         * barcode read.  Value can be a comma delimited list of integers, or a comma
         * delimited list of integers and the words sharpen, brighter, dimmer, for
         * example '400' or '400,500' or '400,400brighter,400sharpenbrighter,500dimmer'.
         */
        val KEY_IMAGERESCALE: String? = "images.barcoderescalesize"
        /**
         * If true, after scanning for a barcode in an image with zxing and failing to
         * find one, repeat with the zxing property TryHarder set to true, otherwise
         * just try once with TryHarder set to false.  Setting to true will slow down
         * preprocessing, but is more likely to find problematic barcodes.  The configured
         * sequence is applied for every check for a barcode, including each individual
         * entry in the images.barcodescalesize list.
         */
        val KEY_IMAGEZXINGALSOTRYHARDER: String? = "images.zxingalsotryharder"
        /**
         * The path and name of the tesseract executable for OCR failover.
         */
        val KEY_TESSERACT_EXECUTABLE: String? = "program.tesseract"
        /**
         * Path and executable for the ImageMagick program convert.
         */
        val KEY_CONVERT_EXECUTABLE: String? = "program.convert"
        /**
         * Default ImageMagick convert properties used for JPG to TIFF conversion to
         * prepare a file for tesseract.
         */
        val KEY_CONVERT_PARAMETERS: String? = "program.convert.parameters"
        /**
         * Path and executable for the ImageMagick program mogrify.  If blank,
         * thumbnails will be generated using Java.
         */
        val KEY_MOGRIFY_EXECUTABLE: String? = "program.mogrify"
        /**
         * Should the specimen details view pane have the scroll bars forced to be
         * turned on.
         */
        val KEY_DETAILS_SCROLL: String? = "details.scroll"
        /**
         * Enable or disable the browse option on the main menu.  It is recommended
         * that browse be disabled in production deployments.
         */
        val KEY_ENABLE_BROWSE: String? = "browse.enabled"
        /**
         * The default value for preparation type (e.g. pinned).
         *
         * @see SpecimenPart->preserveMethod
         */
        val KEY_DEFAULT_PREPARATION: String? = "default.preparation"
        /**
         * How many characters need to be typed before a filtering select picklist will
         * start filtering on the entered string.
         */
        val KEY_FILTER_LENGTH_THRESHOLD: String? = "picklist.filterlength"
        /**
         * Should all other number types (select distinct on all values) be shown, or just
         * a controlled vocabulary of number types on the other number type picklist.
         */
        val KEY_SHOW_ALL_NUMBER_TYPES: String? = "numbertypes.showall"
        /**
         * Pixel height for generated thumbnail images.
         */
        val KEY_THUMBNAIL_HEIGHT: String? = "images.thumbnailheight"
        /**
         * Pixel width for generated thumbnail images.
         */
        val KEY_THUMBNAIL_WIDTH: String? = "images.thumbnailwidth"
        /**
         * Whether to generate thumbnails at all
         */
        val KEY_GENERATE_THUMBNAILS: String? = "images.generate.thumbnails"
        /**
         * Regular expression to identify drawer numbers in strings.
         */
        val KEY_REGEX_DRAWERNUMBER: String? = "images.regexdrawernumber"
        /**
         * Are images expected to contain the barcode number in exif or xmp metadata?
         */
        val KEY_REDUNDANT_COMMENT_BARCODE: String? = "images.metadatacontainsbarcode"
        /**
         * The display name of the collection
         */
        val KEY_DISPLAY_COLLECTION: String? = "display.collection"
        /**
         * Set this value to skip the intensive barcode finder check
         */
        val KEY_DEFAULT_TEMPLATES: String? = "template.default"
        /**
         * Show the login dialog with the advanced options open by default (desirable for
         * developers working with development/test/production databases) if true.
         */
        val KEY_LOGIN_SHOW_ADVANCED: String? = "login.showadvanced"
        /**
         * The maximum height for editor fields
         */
        val KEY_MAX_FIELD_HEIGHT: String? = "editor.fields.maxHeight"
        /**
         * The percentage of the editor window to be used for editor vs. image view
         */
        val KEY_EDITOR_IMPORTANCE: String? = "editor.window.division"
        /**
         * The level of concurrency, resp. the max. number of different threads to use for one job
         */
        val KEY_CONCURRENCY_LEVEL: String? = "concurrency.level"
        val KEY_DB_URL: String? = "hibernate.connection.url"
        val KEY_DB_PASSWORD: String? = "hibernate.connection.password"
        val KEY_DB_USER: String? = "hibernate.connection.username"
        val KEY_DB_DIALECT: String? = "hibernate.dialect"
        val KEY_DB_DRIVER: String? = "hibernate.connection.driver_class"
        private val log = LogFactory.getLog(ImageCaptureProperties::class.java)
        private const val serialVersionUID = -8078524277278506689L
        /**
         * Given a File (which could be a directory path as a File object), return
         * the portion of the path to that file (directory) that is below the path
         * described by KEY_IMAGEBASE.
         *
         * @param aFilename The file or directory (File object) from which to extract the path.
         * @return a string representation of a path from imagebase using the system
         * path separator character.
         */
        fun getPathBelowBase(aFilename: File): String {
            return getPathBelowBase(aFilename, File.separator)!!
        }

        /**
         * Given a file, is that file inside the path described by ImageCaptureProperties.KEY_IMAGEBASE
         *
         * @param aFile
         * @return true if aFile is inside imagebase, false otherwise.
         */
        fun isInPathBelowBase(aFile: File): Boolean {
            var result = false
            var base: String = Singleton.Properties.Properties.getProperty(
                    KEY_IMAGEBASE)
            var filePath = aFile.path
            if (aFile.isFile) {
                filePath = aFile.parent
            }
            log!!.debug("Provided path to test: $filePath")
            if (File.separator == "\\") {
                if (!base.endsWith("\\")) {
                    base = base + "\\"
                }
                // the separator "\" is represented in java as "\\" and in a java regular expression as "\\\\"
                base = base.replace("\\\\".toRegex(), "\\\\\\\\")
                filePath = filePath.replace("\\\\".toRegex(), "\\\\\\\\")
            } else {
                if (!base.endsWith("/")) {
                    base = "$base/"
                }
                if (!filePath.endsWith("/")) {
                    filePath = "$filePath/"
                }
            }
            log.debug("Base path for test: $base")
            if (filePath.startsWith(base)) {
                result = true
            }
            return result
        }

        /**
         * Warning: For unit testing only.  Do not invoke this method.  Always use getPathBelowBase(File aFilename) instead.
         *
         * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.getPathBelowBase
         */
        fun getPathBelowBase(aFilename: File, fileSeparator: String): String? {
            var result: String? = ""
            var base: String = Singleton.Properties.Properties.getProperty(
                    KEY_IMAGEBASE) // this is what we are going to strip off aFilename
            //String filename = "";  // name of file if aFilename is a file rather than a directory
            result = aFilename.path
            log!!.debug("Local path to file: $result")
            if (aFilename.isFile) {
                result = aFilename.parent
            }
            if (fileSeparator == "\\") {
                if (!base.endsWith("\\")) {
                    base = base + "\\\\"
                }
                // the separator "\" is represented in java as "\\" and in a java regular expression as "\\\\"
                base = base.replace("\\\\".toRegex(), "\\\\\\\\")
            } else {
                if (!base.endsWith("/")) {
                    base = "$base/"
                }
                if (!result.endsWith("/")) {
                    result = "$result/"
                }
            }
            log.debug("Base path to remove: $base")
            // strip base out of canonical form of aFilename
            result = if (base == result) {
                ""
            } else {
                result!!.replaceFirst(base.toRegex(), "")
            }
            // make sure that path ends with fileSeparator
            if (!result.endsWith(fileSeparator)) {
                result = result + fileSeparator
            }
            // if result is only a separator set it to an empty string
            if (fileSeparator == "\\") {
                if (result == "\\") {
                    result = ""
                }
            } else {
                if (result == "/") {
                    result = ""
                }
            }
            log.debug("Path below basepath: $result")
            return result
        }
        /**
         * Warning: For unit testing only.  Do not invoke this method.  Use assemblePathWithBase(String aDirectoryPath, String aFileName) instead.
         *
         * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.assemblePathWithBase
         */
        /**
         * Given a path from the image base (property ImageCaptureProperties.KEY_IMAGEBASE)
         * and a filename, returns the full path to that file, including the image base
         * using the file separators for the current system.  For "images/testimages/" and
         * "imagefile.jpg" returns a string like "/mount/lepidoptera/images/testimages/imagefile.jpg"
         * or "Z:\\lepidoptera\images\testimages\imagefile.jpg"
         *
         * @param aDirectoryPath
         * @param aFileName
         * @return String containing the full path to the file
         */
        @JvmOverloads
        fun assemblePathWithBase(aDirectoryPath: String?, aFileName: String?, fileSeparator: String = File.separator): String {
            var result = ""
            var base: String = Singleton.Properties.Properties.getProperty(
                    KEY_IMAGEBASE)
            var path = aDirectoryPath
            // First, correct the aDirectoryPath to the local file separator.
//log.debug("File separator = '" + fileSeparator + "'");
//log.debug(path);
//log.debug(base);
            path = if (fileSeparator == "/") { // unix filesystem
                path!!.replace("\\\\".toRegex(), "/")
            } else { // windows filesystem
                path!!.replace("/".toRegex(), "\\\\")
            }
            // Second, if base path doesn't end with a file separator, add one.
            if (!base.endsWith(fileSeparator)) {
                base = base + fileSeparator
            }
            // Third, assemble the components.
            result = if (path.endsWith(fileSeparator)) {
                base + path + aFileName
            } else {
                base + path + fileSeparator + aFileName
            }
            log!!.debug(result)
            return result
        }
    }

    init {
        propertiesFilename = "imagecapture.properties"
        propertiesFilePath = StringBuffer(System.getProperty("user.dir"))
        propertiesFilePath.append(System.getProperty("file.separator"))
        propertiesFilePath.append(propertiesFilename)
        try {
            loadProperties()
        } catch (e: Exception) { // thrown if properties can't be loaded, create default properties.
            properties = Properties()
            println("Failed loading properties. Error message: " + e.message)
        }
        checkDefaults()
    }
}
