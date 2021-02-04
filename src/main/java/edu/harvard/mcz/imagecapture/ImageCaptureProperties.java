/**
 * ImageCaptureProperties.java
 * edu.harvard.mcz.imagecapture
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import javax.swing.table.AbstractTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filesystem persistence and retrieval of properties for ImageCapture
 * Application. Includes constants for key names to use in properties file, and
 * definition of default values for properties to go with these keys if they
 * aren't defined in the persistent file.
 */
public class ImageCaptureProperties extends AbstractTableModel {

  public static final String COLLECTION_MCZENT = "MCZ-ENT";
  public static final String COLLECTION_ETHZENT = "ETHZ-ENT";

  /**
   * The collection for which this deployment is configured to work with.
   */
  public static final String KEY_COLLECTION = "configuration.collection";
  /**
   * Value to use for the specific collection, empty string will use default for
   * the configured KEY_COLLECTION.
   */
  public static final String KEY_SPECIFIC_COLLECTION =
      "configuration.specificcollection";

  /**
   * The most recent location selected for scanning a barcode.
   */
  public static final String KEY_LASTPATH = "scanonebarcode.lastpath";

  /**
   * The most recent location selected for loading data from a file.
   */
  public static final String KEY_LASTLOADPATH = "fileload.lastpath";

  /**
   * Root of the path of the place where all image files should be stored.
   */
  public static final String KEY_IMAGEBASE = "images.basedirectory";
  /**
   * URI to the root of the path of the place where all image files should be
   * stored, that is, the URI on the image server that points to the same
   * location as KEY_IMAGEBASE does on the local filesystem.
   */
  public static final String KEY_IMAGEBASEURI = "images.basedirectoryurimap";
  /**
   * Regular expression that image files to be preprocessed must match.
   */
  public static final String KEY_IMAGEREGEX = "images.filenameregex";
  /**
   * Sizes to which to rescale width of unit tray label barcode to on retry of
   * barcode read.  Value can be a comma delimited list of integers, or a comma
   * delimited list of integers and the words sharpen, brighter, dimmer, for
   * example '400' or '400,500' or
   * '400,400brighter,400sharpenbrighter,500dimmer'.
   */
  public static final String KEY_IMAGERESCALE = "images.barcoderescalesize";
  /**
   * If true, after scanning for a barcode in an image with zxing and failing to
   * find one, repeat with the zxing property TryHarder set to true, otherwise
   * just try once with TryHarder set to false.  Setting to true will slow down
   * preprocessing, but is more likely to find problematic barcodes.  The
   * configured sequence is applied for every check for a barcode, including
   * each individual entry in the images.barcodescalesize list.
   */
  public static final String KEY_IMAGEZXINGALSOTRYHARDER =
      "images.zxingalsotryharder";
  /**
   * The path and name of the tesseract executable for OCR failover.
   */
  public static final String KEY_TESSERACT_EXECUTABLE = "program.tesseract";
  /**
   * Path and executable for the ImageMagick program convert.
   */
  public static final String KEY_CONVERT_EXECUTABLE = "program.convert";
  /**
   * Default ImageMagick convert properties used for JPG to TIFF conversion to
   * prepare a file for tesseract.
   */
  public static final String KEY_CONVERT_PARAMETERS =
      "program.convert.parameters";
  /**
   * Path and executable for the ImageMagick program mogrify.  If blank,
   * thumbnails will be generated using Java.
   */
  public static final String KEY_MOGRIFY_EXECUTABLE = "program.mogrify";
  /**
   * Should the specimen details view pane have the scroll bars forced to be
   * turned on.
   */
  public static final String KEY_DETAILS_SCROLL = "details.scroll";
  /**
   * Enable or disable the browse option on the main menu.  It is recommended
   * that browse be disabled in production deployments.
   */
  public static final String KEY_ENABLE_BROWSE = "browse.enabled";
  /**
   * The default value for preparation type (e.g. pinned).
   *
   * @see SpecimenPart->preserveMethod
   */
  public static final String KEY_DEFAULT_PREPARATION = "default.preparation";
  /**
   * How many characters need to be typed before a filtering select picklist
   * will start filtering on the entered string.
   */
  public static final String KEY_FILTER_LENGTH_THRESHOLD =
      "picklist.filterlength";
  /**
   * Should all other number types (select distinct on all values) be shown, or
   * just a controlled vocabulary of number types on the other number type
   * picklist.
   */
  public static final String KEY_SHOW_ALL_NUMBER_TYPES = "numbertypes.showall";
  /**
   * Pixel height for generated thumbnail images.
   */
  public static final String KEY_THUMBNAIL_HEIGHT = "images.thumbnailheight";
  /**
   * Pixel width for generated thumbnail images.
   */
  public static final String KEY_THUMBNAIL_WIDTH = "images.thumbnailwidth";
  /**
   * Whether to generate thumbnails at all
   */
  public static final String KEY_GENERATE_THUMBNAILS =
      "images.generate.thumbnails";
  /**
   * Regular expression to identify drawer numbers in strings.
   */
  public static final String KEY_REGEX_DRAWERNUMBER =
      "images.regexdrawernumber";

  /**
   * Are images expected to contain the barcode number in exif or xmp metadata?
   */
  public static final String KEY_REDUNDANT_COMMENT_BARCODE =
      "images.metadatacontainsbarcode";

  /**
   * The display name of the collection
   */
  public static final String KEY_DISPLAY_COLLECTION = "display.collection";

  /**
   * Set this value to skip the intensive barcode finder check
   */
  public static final String KEY_DEFAULT_TEMPLATES = "template.default";

  /**
   * Show the login dialog with the advanced options open by default (desirable
   * for developers working with development/test/production databases) if true.
   */
  public static final String KEY_LOGIN_SHOW_ADVANCED = "login.showadvanced";

  /**
   * The maximum height for editor fields
   */
  public static final String KEY_MAX_FIELD_HEIGHT = "editor.fields.maxHeight";

  /**
   * The percentage of the editor window to be used for editor vs. image view
   */
  public static final String KEY_EDITOR_IMPORTANCE = "editor.window.division";

  /**
   * The level of concurrency, resp. the max. number of different threads to use
   * for one job
   */
  public static final String KEY_CONCURRENCY_LEVEL = "concurrency.level";

  public static final String KEY_DB_URL = "hibernate.connection.url";
  public static final String KEY_DB_PASSWORD = "hibernate.connection.password";
  public static final String KEY_DB_USER = "hibernate.connection.username";
  public static final String KEY_DB_DIALECT = "hibernate.dialect";
  public static final String KEY_DB_DRIVER =
      "hibernate.connection.driver_class";

  /**
   * The keys for setting the columns to read for the Excel Georeference import
   *
   * @see edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog#pasteFromExcel(String)
   */
  public static String KEY_EXCEL_COL_LAT = "georef.import.lat.col";
  public static String KEY_EXCEL_COL_LONG = "georef.import.long.col";
  public static String KEY_EXCEL_COL_ERR_RAD = "georef.import.err_rad.col";
  public static String KEY_EXCEL_COL_METHOD = "georef.import.method.col";
  public static String KEY_EXCEL_COL_VERBATIM_LOC = "georef.import.verbatim_loc.col";
  public static String KEY_EXCEL_COL_COUNTRY = "georef.import.country.col";
  public static String KEY_EXCEL_COL_STATE_PROVINCE = "georef.import.state_province.col";
  public static String KEY_EXCEL_COL_SPECIFIC_LOC = "georef.import.specific_loc.col";

  private static final Logger log =
      LoggerFactory.getLogger(ImageCaptureProperties.class);

  private static final long serialVersionUID = -8078524277278506689L;
  private Properties properties = null;
  private String propertiesFilename = "imagecapture.properties";
  private String propertiesFilePath = null;

  public ImageCaptureProperties() {
    String[] directoriesToCheck = {ImageCaptureProperties.getApplicationPath(),
                                   System.getProperty("user.dir"),
                                   System.getProperty("user.home")};
    for (String dir : directoriesToCheck) {
      String propertiesTestFilePath = dir + File.separator + propertiesFilename;
      File testF = new File(propertiesTestFilePath);
      if (testF.exists()) {
        this.propertiesFilePath = propertiesTestFilePath;

        try {
          loadProperties();
        } catch (Exception e) {
          // thrown if properties can't be loaded, create default properties.
          properties = new Properties();
          System.out.println("Failed loading properties. Error message: " +
                             e.getMessage());
        }
        checkDefaults();
        return;
      } else {
        log.debug("No properties file at " + propertiesTestFilePath);
      }
    }
  }

  /*****************************************************************************
   * return application path
   * @return
   *****************************************************************************/
  public static String getApplicationPath() {
    CodeSource codeSource =
        ImageCaptureApp.class.getProtectionDomain().getCodeSource();
    File rootPath = null;
    try {
      rootPath = new File(codeSource.getLocation().toURI().getPath());
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (rootPath == null) {
      return "/";
    }
    return rootPath.getParentFile().getPath();
  } // end of getApplcatonPath()

  /**
   * Given a File (which could be a directory path as a File object), return
   * the portion of the path to that file (directory) that is below the path
   * described by KEY_IMAGEBASE.
   *
   * @param aFilename The file or directory (File object) from which to extract
   *                  the path.
   * @return a string representation of a path from imagebase using the system
   * path separator character.
   */
  public static String getPathBelowBase(File aFilename) {
    return getPathBelowBase(aFilename, File.separator);
  }

  /**
   * Given a file, is that file inside the path described by
   * ImageCaptureProperties.KEY_IMAGEBASE
   *
   * @param aFile
   * @return true if aFile is inside imagebase, false otherwise.
   */
  public static boolean isInPathBelowBase(File aFile) {
    boolean result = false;
    String base = Singleton.getSingletonInstance()
                      .getProperties()
                      .getProperties()
                      .getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
    String filePath = aFile.getPath();

    if (aFile.isFile()) {
      filePath = aFile.getParent();
    }
    log.debug("Provided path to test: " + filePath);
    if (File.separator.equals("\\")) {
      if (!base.endsWith("\\")) {
        base = base + "\\";
      }
      // the separator "\" is represented in java as "\\" and in a java regular
      // expression as "\\\\"
      base = base.replaceAll("\\\\", "\\\\\\\\");
      filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
    } else {
      if (!base.endsWith("/")) {
        base = base + "/";
      }
      if (!filePath.endsWith("/")) {
        filePath = filePath + "/";
      }
    }
    log.debug("Base path for test: " + base);
    if (filePath.startsWith(base)) {
      result = true;
    }
    return result;
  }

  /**
   * Warning: For unit testing only.  Do not invoke this method.  Always use
   * getPathBelowBase(File aFilename) instead.
   *
   * @see
   *     edu.harvard.mcz.imagecapture.ImageCaptureProperties#getPathBelowBase(File)
   */
  public static String getPathBelowBase(File aFilename, String fileSeparator) {
    String result = "";
    String base =
        Singleton.getSingletonInstance()
            .getProperties()
            .getProperties()
            .getProperty(ImageCaptureProperties
                             .KEY_IMAGEBASE); // this is what we are going to
    // strip off aFilename
    // String filename = "";  // name of file if aFilename is a file rather than
    // a directory

    result = aFilename.getPath();
    log.debug("Local path to file: " + result);
    if (aFilename.isFile()) {
      result = aFilename.getParent();
    }

    if (fileSeparator.equals("\\")) {
      if (!base.endsWith("\\")) {
        base = base + "\\\\";
      }
      // the separator "\" is represented in java as "\\" and in a java regular
      // expression as "\\\\"
      base = base.replaceAll("\\\\", "\\\\\\\\");
    } else {
      if (!base.endsWith("/")) {
        base = base + "/";
      }
      if (!result.endsWith("/")) {
        result = result + "/";
      }
    }
    log.debug("Base path to remove: " + base);
    // strip base out of canonical form of aFilename
    if (base.equals(result)) {
      result = "";
    } else {
      result = result.replaceFirst(base, "");
    }
    // make sure that path ends with fileSeparator
    if (!result.endsWith(fileSeparator)) {
      result = result + fileSeparator;
    }

    // if result is only a separator set it to an empty string
    if (fileSeparator.equals("\\")) {
      if (result.equals("\\")) {
        result = "";
      }
    } else {
      if (result.equals("/")) {
        result = "";
      }
    }

    log.debug("Path below basepath: " + result);

    return result;
  }

  /**
   * Given a path from the image base (property
   * ImageCaptureProperties.KEY_IMAGEBASE) and a filename, returns the full path
   * to that file, including the image base using the file separators for the
   * current system.  For "images/testimages/" and "imagefile.jpg" returns a
   * string like "/mount/lepidoptera/images/testimages/imagefile.jpg" or
   * "Z:\\lepidoptera\images\testimages\imagefile.jpg"
   *
   * @param aDirectoryPath
   * @param aFileName
   * @return String containing the full path to the file
   */
  public static String assemblePathWithBase(String aDirectoryPath,
                                            String aFileName) {
    return assemblePathWithBase(aDirectoryPath, aFileName, File.separator);
  }

  /**
   * Warning: For unit testing only.  Do not invoke this method.  Use
   * assemblePathWithBase(String aDirectoryPath, String aFileName) instead.
   *
   * @see
   *     edu.harvard.mcz.imagecapture.ImageCaptureProperties#assemblePathWithBase(String,
   * String)
   */
  public static String assemblePathWithBase(String aDirectoryPath,
                                            String aFileName,
                                            String fileSeparator) {
    String result = "";
    String base = Singleton.getSingletonInstance()
                      .getProperties()
                      .getProperties()
                      .getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
    String path = aDirectoryPath;
    // First, correct the aDirectoryPath to the local file separator.
    // log.debug("File separator = '" + fileSeparator + "'");
    // log.debug("Debug {}", path);
    // log.debug("Debug {}", base);
    if (fileSeparator.equals("/")) {
      // unix filesystem
      path = path.replaceAll("\\\\", "/");
    } else {
      // windows filesystem
      path = path.replaceAll("/", "\\\\");
    }
    // Second, if base path doesn't end with a file separator, add one.
    if (!base.endsWith(fileSeparator)) {
      base = base + fileSeparator;
    }
    // Third, assemble the components.
    if (path.endsWith(fileSeparator)) {
      result = base + path + aFileName;
    } else {
      result = base + path + fileSeparator + aFileName;
    }
    log.debug("Debug {}", result);
    return result;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName(int column) {
    String returnValue = "";
    if (column == 0) {
      returnValue = "Key";
    }
    if (column == 1) {
      returnValue = "Property value";
    }
    return returnValue;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    boolean returnValue = false;
    if (columnIndex == 1) {
      returnValue = true;
    }
    return returnValue;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
   *     int)
   */
  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    if (columnIndex == 1) {
      Enumeration<Object> p = properties.keys();
      int element = 0;
      while (p.hasMoreElements()) {
        String e = (String)p.nextElement();
        if (element == rowIndex) {
          properties.setProperty(e, (String)value);
        }
        element++;
      }
    }
  }

  /**
   * Test to see if the KEY_TEMPLATEDEFAULT matches a valid PositionTemplate.
   * Change to PositionTemplate.TEMPLATE_DEFAULT and Log error if it does not.
   * Note: if the KEY_TEMPLATEDEFAULT property does not match a hardcoded
   * PositionTemplate, a database lookup will be triggered and, if the request
   * is being made at application launch, a login dialog will be launched.
   * <p>
   * Take no action if there is no match to the KEY_TEMPLATEDEFAULT
   *
   * @return true if template in properties exists, false if no match to key or
   * if template was changed.
   */
  public boolean testDefaultTemplate() {
    boolean result = false;
    if (properties.containsKey(KEY_DEFAULT_TEMPLATES)) {
      String templateId = properties.getProperty(KEY_DEFAULT_TEMPLATES);
      try {
        PositionTemplate template = new PositionTemplate(templateId);
        template.getClass(); // added to suppress findbugs DLS_DEAD_LOCAL_STORE
        // no exception thrown, this template is OK.
        result = true;
      } catch (NoSuchTemplateException e) {
        // Template isn't recognized, set property to default template.
        properties.setProperty(KEY_DEFAULT_TEMPLATES,
                               PositionTemplate.TEMPLATE_DEFAULT);
      }
    }
    return result;
  }

  /**
   * Make sure required properties are present as keys, if they aren't add
   * them with default values.  This is where the default properties are
   * defined.
   */
  private void checkDefaults() {
    if (!properties.containsKey(KEY_SPECIFIC_COLLECTION)) {
      // location in collection to use, if not default provided from
      // KEY_COLLECTION in LocationInCollection. default to ETHZ; maybe '""'
      // would be a better choice? ;)
      properties.setProperty(KEY_SPECIFIC_COLLECTION, "ETHZ-ENT");
      log.debug("Forced configuration: ETHZ-ENT");
    }
    if (!properties.containsKey(KEY_COLLECTION)) {
      // Root of the path of the place where all image files should be stored.
      properties.setProperty(KEY_COLLECTION,
                             ImageCaptureProperties.COLLECTION_MCZENT);
    } else {
      switch (properties.get(KEY_COLLECTION).toString().trim()) {
      case (ImageCaptureProperties.COLLECTION_ETHZENT):
        log.debug("Configured for " +
                  ImageCaptureProperties.COLLECTION_ETHZENT);
        break;
      case (ImageCaptureProperties.COLLECTION_MCZENT):
        log.debug("Configured for " + ImageCaptureProperties.COLLECTION_MCZENT);
        break;
      default:
        log.error("Unrecognized collection: " +
                  properties.get(KEY_COLLECTION).toString());
        log.error("Allowed values for " +
                  ImageCaptureProperties.KEY_COLLECTION + " are " +
                  ImageCaptureProperties.COLLECTION_MCZENT + " or " +
                  ImageCaptureProperties.COLLECTION_ETHZENT);
      }
    }

    if (!properties.containsKey(KEY_IMAGEBASE)) {
      // Root of the path of the place where all image files should be stored.
      // properties.setProperty(KEY_IMAGEBASE,"/mount/lepidopteraimages");
      properties.setProperty(KEY_IMAGEBASE,
                             "C:\\Users\\Digitizing\\Desktop\\images-test\\");
      // properties.setProperty(KEY_IMAGEBASE,"/Users/altheaparker/Desktop/entomology-builds/TestImages");
    }
    if (!properties.containsKey(KEY_IMAGEBASEURI)) {
      // URI to the root of the path of the place where all image files should
      // be stored.
      properties.setProperty(KEY_IMAGEBASEURI,
                             "http://mczbase.mcz.harvard.edu/specimen_images/");
    }
    if (!properties.containsKey(KEY_IMAGEREGEX)) {
      // Regular expression to identify image filenames for processing.
      properties.setProperty(KEY_IMAGEREGEX, ImageCaptureApp.REGEX_IMAGEFILE);
    }
    if (!properties.containsKey(KEY_IMAGERESCALE)) {
      // Sizes to which to rescale width of unit tray label barcode to on retry.
      properties.setProperty(
          KEY_IMAGERESCALE,
          "400,600sharpen,600brighter,600dimmer,400sharpenbrighter");
    }
    if (!properties.containsKey(KEY_IMAGEZXINGALSOTRYHARDER)) {
      // Default value for choosing whether or not to also try harder with
      // xzing.
      properties.setProperty(KEY_IMAGEZXINGALSOTRYHARDER, "true");
    }
    if (!properties.containsKey(KEY_TESSERACT_EXECUTABLE)) {
      // name of the tesseract executable, probably tesseract on unix,
      // tesseract.exe on windows
      properties.setProperty(KEY_TESSERACT_EXECUTABLE, "tesseract ");
    }
    if (!properties.containsKey(KEY_CONVERT_EXECUTABLE)) {
      properties.setProperty(KEY_CONVERT_EXECUTABLE, "convert ");
    }
    if (!properties.containsKey(KEY_MOGRIFY_EXECUTABLE)) {
      properties.setProperty(KEY_MOGRIFY_EXECUTABLE, "mogrify ");
    }
    if (!properties.containsKey(KEY_CONVERT_PARAMETERS)) {
      // default ImageMagick convert properties used for JPG to TIFF conversion
      // to prepare a file for tesseract.
      properties.setProperty(KEY_CONVERT_PARAMETERS,
                             " -depth 8 -compress None -type Grayscale ");
    }
    if (!properties.containsKey(KEY_DETAILS_SCROLL)) {
      // default value is no scroll bars for SpecimenDetailsViewPane.
      properties.setProperty(KEY_DETAILS_SCROLL, "none");
    }
    if (!properties.containsKey(KEY_ENABLE_BROWSE)) {
      // default value is disabled browse on main menu.
      properties.setProperty(KEY_ENABLE_BROWSE, "false");
    }
    if (!properties.containsKey(KEY_DEFAULT_PREPARATION)) {
      // default preparation type
      properties.setProperty(KEY_DEFAULT_PREPARATION, "pinned");
    }
    if (!properties.containsKey(KEY_FILTER_LENGTH_THRESHOLD)) {
      // default value is disabled browse on main menu.
      properties.setProperty(KEY_FILTER_LENGTH_THRESHOLD, "3");
    }
    if (!properties.containsKey(KEY_SHOW_ALL_NUMBER_TYPES)) {
      // default value is disabled browse on main menu.
      properties.setProperty(KEY_SHOW_ALL_NUMBER_TYPES, "false");
    }
    if (!properties.containsKey(KEY_THUMBNAIL_HEIGHT)) {
      // default value is 120 pixels.
      properties.setProperty(KEY_THUMBNAIL_HEIGHT, "120");
    }
    if (!properties.containsKey(KEY_THUMBNAIL_WIDTH)) {
      // default value is 80 pixels.
      properties.setProperty(KEY_THUMBNAIL_WIDTH, "80");
    }
    if (!properties.containsKey(KEY_GENERATE_THUMBNAILS)) {
      properties.setProperty(KEY_GENERATE_THUMBNAILS, "true");
    }
    if (!properties.containsKey(KEY_REGEX_DRAWERNUMBER)) {
      properties.setProperty(KEY_REGEX_DRAWERNUMBER,
                             ImageCaptureApp.REGEX_DRAWERNUMBER);
    }
    if (!properties.containsKey(KEY_REDUNDANT_COMMENT_BARCODE)) {
      // default value is that images are expected to contain the barcode number
      // in both the image and in its metadata.
      properties.setProperty(KEY_REDUNDANT_COMMENT_BARCODE, "true");
    }
    if (!properties.containsKey(KEY_LOGIN_SHOW_ADVANCED)) {
      // default value is closed advanced options (server) on login dialog.
      properties.setProperty(KEY_LOGIN_SHOW_ADVANCED, "false");
    }
    if (!properties.containsKey(KEY_DEFAULT_TEMPLATES)) {
      properties.setProperty(KEY_DEFAULT_TEMPLATES,
                             PositionTemplate.TEMPLATE_DEFAULT);
    }
    if (!properties.containsKey(KEY_MAX_FIELD_HEIGHT)) {
      properties.setProperty(KEY_MAX_FIELD_HEIGHT, "70");
    }
    if (!properties.containsKey(KEY_EDITOR_IMPORTANCE)) {
      properties.setProperty(KEY_EDITOR_IMPORTANCE, "0.6");
    }
    if (!properties.containsKey(KEY_CONCURRENCY_LEVEL)) {
      properties.setProperty(KEY_CONCURRENCY_LEVEL, "16");
    }
    // the Excel ...
    Map<String, String> defaultsMap = Map.ofEntries(
            Map.entry(KEY_EXCEL_COL_LAT, "6"),
            Map.entry(KEY_EXCEL_COL_LONG, "6"),
            Map.entry(KEY_EXCEL_COL_COUNTRY, "3"),
            Map.entry(KEY_EXCEL_COL_ERR_RAD, "7"),
            Map.entry(KEY_EXCEL_COL_METHOD, "8"),
            Map.entry(KEY_EXCEL_COL_STATE_PROVINCE, "4"),
            Map.entry(KEY_EXCEL_COL_VERBATIM_LOC, "0"),
            Map.entry(KEY_EXCEL_COL_SPECIFIC_LOC, "5")
    );
    defaultsMap.forEach((key, value) -> {
      if (!properties.containsKey(key)) {
        properties.setProperty(key, value);
      }
    });
  }

  /* Place where properties in this instance are persisted.
   *
   * @returns a text string representing the storage location from which this
   *     instance of
   * properties was loaded such ast the path and filename of the file from which
   * the values for this instance of properties was retrieved.
   */
  public String getPropertiesSource() { return propertiesFilePath.toString(); }

  protected void loadProperties() throws Exception {
    if (properties == null) {
      properties = new Properties();
      FileInputStream propertiesStream = null;
      log.debug("Opening properties file: " + propertiesFilePath.toString());
      try {
        propertiesStream = new FileInputStream(propertiesFilePath.toString());
        properties.load(propertiesStream);
        // test dump
        /*Enumeration<Object> keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            log.debug("Got property: " + key + " with value " +
        properties.getProperty(key));
        }*/
        // Test to see if all properties are set in the loaded file
        checkDefaults();
      } catch (FileNotFoundException e) {
        System.out.println("Error: Properties file not found.");
        throw e;
      } catch (Exception ex) {
        System.out.println("Error loading properties.");
        System.out.println(ex.getMessage());
        throw ex;
      } finally {
        if (propertiesStream != null) {
          propertiesStream.close();
        }
      }
    } else {
      System.out.println(
          "Skipping properties loading as they are already loaded");
    }
  }

  public void saveProperties() throws Exception {
    FileOutputStream propertiesStream = null;
    try {
      System.out.println("Saving properties file: " +
                         propertiesFilePath.toString());
      propertiesStream = new FileOutputStream(propertiesFilePath.toString());
      properties.store(propertiesStream, ImageCaptureApp.APP_NAME + " " +
                                             ImageCaptureApp.getAppVersion() +
                                             " Properties");
      propertiesStream.close();
    } catch (Exception e) {
      System.out.println("Error saving properties.");
      System.out.println(e.getMessage());
      e.printStackTrace();
      throw e;
    } finally {
      if (propertiesStream != null) {
        propertiesStream.close();
      }
    }
  }

  public Properties getProperties() { return properties; }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public int getRowCount() {
    return properties.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    String value = "";
    Enumeration<Object> p = properties.keys();
    int element = 0;
    while (p.hasMoreElements()) {
      String e = (String)p.nextElement();
      if (element == rowIndex) {
        if (columnIndex == 0) {
          value = e;
        } else {
          value = properties.getProperty(e);
        }
      }
      element++;
    }
    return value;
  }
}
