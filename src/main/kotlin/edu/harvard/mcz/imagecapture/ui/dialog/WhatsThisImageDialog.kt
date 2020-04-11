/**
 * WhatsThisImageDialog.java
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
package edu.harvard.mcz.imagecapture.ui.dialog


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialog
import edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import org.apache.commons.logging.Log
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.*

org.apache.commons.logging.LogFactory
import  java.awt.Frame


import java.awt.Insets 
import java.awt.event.ActionEvent 
import java.awt.event.KeyAdapter 
import java.awt.event.KeyEvent 
import java.io.File java.net.URL


import javax.swing.JTextField 
import java.awt.event.ActionListener 
import java.text.DecimalFormat 
import java.lang.NumberFormatException 
import javax.swing.JComboBox 
import edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRec 
import java.awt.event.FocusListener 
import edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModel 
import edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBox 
import javax.swing.SwingUtilities 
import java.lang.Runnable 
import edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycle 
import edu.harvard.mcz.imagecapture.Singleton 
import edu.harvard.mcz.imagecapture.ImageCaptureProperties 
import java.awt.event.FocusEvent 
import edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentName 
import edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModel 
import edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBox 
import edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycle 
import javax.swing.JFrame 
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener 
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame 
import edu.harvard.mcz.imagecapture.ImageListBrowser 
import edu.harvard.mcz.imagecapture.SpecimenBrowser 
import edu.harvard.mcz.imagecapture.UserListBrowser 
import javax.swing.JMenuBar 
import javax.swing.JMenu 
import javax.swing.JMenuItem 
import javax.swing.JPanel 
import javax.swing.JProgressBar 
import javax.swing.JLabel 
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle 
import edu.harvard.mcz.imagecapture.entity.Users 
import javax.swing.WindowConstants 
import java.awt.Taskbar 
import java.lang.UnsupportedOperationException 
import java.lang.SecurityException 
import javax.swing.ImageIcon 
import edu.harvard.mcz.imagecapture.ImageCaptureApp 
import java.lang.NullPointerException 
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle 
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException 
import edu.harvard.mcz.imagecapture.ui.dialog.AboutDialog 
import javax.swing.JOptionPane 
import edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScan 
import java.lang.Thread 
import javax.swing.JComponent 
import edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoad 
import edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScan 
import java.awt.BorderLayout 
import edu.harvard.mcz.imagecapture.PropertiesEditor 
import javax.swing.text.DefaultEditorKit.CopyAction 
import javax.swing.KeyStroke 
import javax.swing.text.DefaultEditorKit.PasteAction 
import edu.harvard.mcz.imagecapture.ui.frame.EventLogFrame 
import java.awt.GridBagConstraints 
import java.awt.GridBagLayout 
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob 
import edu.harvard.mcz.imagecapture.entity.Specimen 
import edu.harvard.mcz.imagecapture.PositionTemplateEditor import edu.harvard.mcz.imagecapture.data.LocationInCollection 
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialog 
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialog 
import edu.harvard.mcz.imagecapture.jobs.JobFileReconciliation 
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder 
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher 
import edu.harvard.mcz.imagecapture.entity.ICImage 
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError 
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog 
import edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModel 
import edu.harvard.mcz.imagecapture.ui.dialog.SearchDialog 
import java.lang.StringBuilder 
import edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialog 
import edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser 
import edu.harvard.mcz.imagecapture.jobs.JobRepeatOCR 
import edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrame 
import edu.harvard.mcz.imagecapture.jobs.JobCleanDirectory 
import edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplates 
import java.awt.Graphics2D 
import edu.harvard.mcz.imagecapture.utility.MathUtility 
import javax.swing.JViewport 
import javax.swing.JScrollPane 
import javax.swing.JButton 
import javax.swing.JTable 
import edu.harvard.mcz.imagecapture.entity.Tracking 
import edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycle 
import edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModel 
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame 
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTask 
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryAction 
import javax.swing.border.EmptyBorder 
import java.awt.GridLayout 
import javax.swing.SwingConstants 
import java.beans.PropertyChangeEvent 
import java.lang.Void 
import javax.swing.JFileChooser 
import edu.harvard.mcz.imagecapture.ThumbnailBuilder 
import java.io.PrintWriter 
import edu.harvard.mcz.imagecapture.data.BulkMedia 
import edu.harvard.mcz.imagecapture.CandidateImageFile 
import java.io.IOException 
import java.io.FileNotFoundException 
import java.awt.event.MouseListener 
import java.awt.image.BufferedImage 
import edu.harvard.mcz.imagecapture.ui.frame.ImagePanel 
import javax.imageio.ImageIO 
import edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel 
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer 
import edu.harvard.mcz.imagecapture.ui.ButtonEditor 
import edu.harvard.mcz.imagecapture.ui.ProgressBarRenderer 
import java.util.prefs.Preferences 
import javax.swing.JTabbedPane 
import edu.harvard.mcz.imagecapture.SpecimenController 
import edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrame 
import edu.harvard.mcz.imagecapture.PositionTemplate 
import edu.harvard.mcz.imagecapture.exceptions.BadTemplateException 
import edu.harvard.mcz.imagecapture.exceptions.ImageLoadException 
import kotlin.jvm.Throws 
import java.awt.event.ComponentEvent 
import java.util.prefs.BackingStoreException 
import java.lang.IllegalStateException 
import net.miginfocom.swing.MigLayout 
import java.lang.IndexOutOfBoundsException 
import javax.swing.JSplitPane 
import javax.swing.BorderFactory 
import javax.swing.plaf.basic.BasicSplitPaneUI 
import javax.swing.plaf.basic.BasicSplitPaneDivider 
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle 
import java.util.HashSet 
import java.util.HashMap 
import edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialog 
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialog 
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus 
import javax.swing.JPopupMenu 
import edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModel 
import edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrame 
import java.awt.event.MouseAdapter 
import edu.harvard.mcz.imagecapture.entity.fixed.NatureOfId 
import javax.swing.DefaultCellEditor 
import edu.harvard.mcz.imagecapture.entity.fixed.TypeStatus 
import edu.harvard.mcz.imagecapture.data.MetadataRetriever 
import edu.harvard.mcz.imagecapture.entity.Determination 
import edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditor 
import java.awt.Stroke 
import javax.swing.JCheckBox 
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder 
import javax.swing.JTextArea 
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane 
import java.lang.StringBuffer 
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException 
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel 
import edu.harvard.mcz.imagecapture.entity.SpecimenPart 
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel 
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel 
import edu.harvard.mcz.imagecapture.entity.LatLong 
import javax.swing.DefaultComboBoxModel 
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator 
import edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycle 
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor 
import edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycle 
import javax.swing.table.DefaultTableCellRenderer 
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog 
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle 
import edu.harvard.mcz.imagecapture.entity.fixed.Sex 
import edu.harvard.mcz.imagecapture.entity.fixed.LifeStage 
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycle 
import javax.swing.table.AbstractTableModel 
import edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListener 
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility 
import java.util.Arrays 
import org.hibernate.SessionException 
import javax.swing.JDialog 
import edu.harvard.mcz.imagecapture.ui.dialog.UserDialog 
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog 
import javax.swing.JPasswordField 
import edu.harvard.mcz.imagecapture.utility.HashUtility 
import edu.harvard.mcz.imagecapture.ui.field.JIntegerField 
import javax.swing.JFormattedTextField 
import java.text.SimpleDateFormat 
import java.math.BigDecimal 
import javax.swing.ComboBoxModel 
import org.jdesktop.swingx.combobox.ListComboBoxModel 
import javax.swing.text.MaskFormatter 
import java.awt.FlowLayout 
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener 
import edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModel 
import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount 
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException 
import edu.harvard.mcz.imagecapture.exceptions.UnreadableFileException 
import edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialog 
import edu.harvard.mcz.imagecapture.loader.Verbatim 
import javax.swing.BoxLayout 
import edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModel 
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialog 
import javax.swing.table.TableModel 
import edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialog 
import edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModel 
import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount 
import edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialog 
import com.jgoodies.forms.layout.FormLayout 
import com.jgoodies.forms.layout.ColumnSpec 
import com.jgoodies.forms.layout.FormSpecs 
import com.jgoodies.forms.layout.RowSpec 
import edu.harvard.mcz.imagecapture.entity.fixed.Caste 
import edu.harvard.mcz.imagecapture.entity.fixed.PartAssociation 
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute 
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycle 
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModel 
import edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialog 
import edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModel 
import javax.swing.table.TableCellRenderer 
import edu.harvard.mcz.imagecapture.ui.DataShotColors 
import javax.swing.border.MatteBorder 
import edu.harvard.mcz.imagecapture.entity.fixed.CountValue 
import edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModel 
import java.text.DateFormat 
import edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModel 
import java.lang.ArrayIndexOutOfBoundsException 
import javax.swing.event.ListDataListener 
import javax.swing.event.ListDataEvent 
import java.lang.ClassCastException 
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel 
import edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycle 
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel 
import edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycle 
import edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModel 
import javax.swing.AbstractCellEditor 
import javax.swing.table.TableCellEditor 
import edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordException 
import java.awt.event.MouseWheelListener 
import java.awt.event.MouseWheelEvent 
import javax.swing.JScrollBar 
import edu.harvard.mcz.imagecapture.data.RunStatus 
import org.hibernate.exception.JDBCConnectionException 
import java.lang.ExceptionInInitializerError 
import java.util.Enumeration 
import java.awt.Dialog.ModalityType 
import javax.swing.InputVerifier 
import edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterface 
import edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetector 
import edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturner 
import edu.harvard.mcz.imagecapture.UnitTrayLabelParser 
import edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner 
import java.util.concurrent.atomic.AtomicInteger 
import edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJob 
import edu.harvard.mcz.imagecapture.exceptions.OCRReadException 
import org.apache.commons.codec.digest.DigestUtils 
import java.io.FileInputStream 
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException 
import edu.harvard.mcz.imagecapture.interfaces.CollectionReturner 
import edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJob 
import java.lang.Runtime 
import java.lang.Process 
import java.io.BufferedReader 
import java.lang.InterruptedException 
import edu.harvard.mcz.imagecapture.utility.FileUtility 
import edu.harvard.mcz.imagecapture.jobs.AtomicCounter 
import java.util.concurrent.ExecutorService 
import java.util.concurrent.Executors 
import java.util.concurrent.locks.ReentrantLock 
import java.util.concurrent.TimeUnit 
import org.hibernate.HibernateException 
import edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetector 
import edu.harvard.mcz.imagecapture.interfaces.ValueLister 
import edu.harvard.mcz.imagecapture.entity.ExternalHistory 
import kotlin.jvm.JvmOverloads 
import edu.harvard.mcz.imagecapture.entity.AllowedVersion 
import edu.harvard.mcz.imagecapture.loader.FieldLoader 
import edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnException 
import edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedException 
import edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveException 
import edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundException 
import org.filteredpush.qc.date.EventResult 
import java.lang.NoSuchMethodException 
import java.lang.IllegalAccessException 
import java.lang.IllegalArgumentException 
import java.lang.reflect.InvocationTargetException 
import edu.harvard.mcz.imagecapture.loader.HeaderCheckResult 
import edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycle 
import java.io.FileReader 
import kotlin.jvm.JvmStatic 
import edu.harvard.mcz.imagecapture.encoder.LabelEncoder 
import java.util.Hashtable 
import com.google.zxing.BarcodeFormat 
import com.google.zxing.client.j2se.MatrixToImageWriter 
import com.itextpdf.text.BadElementException 
import edu.harvard.mcz.imagecapture.exceptions.PrintFailedException 
import com.itextpdf.text.pdf.PdfWriter 
import java.io.FileOutputStream 
import com.itextpdf.text.PageSize 
import com.itextpdf.text.pdf.PdfPTable 
import com.itextpdf.text.pdf.PdfPCell 
import com.itextpdf.text.BaseColor 
import com.itextpdf.text.Paragraph 
import java.lang.OutOfMemoryError 
import javax.swing.undo.UndoManager 
import edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoAction 
import edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoAction 
import javax.swing.table.TableRowSorter 
import edu.harvard.mcz.imagecapture.utility.DragDropJTable 
import javax.swing.DropMode 
import javax.swing.event.UndoableEditListener 
import javax.swing.event.UndoableEditEvent 
import javax.swing.undo.CannotUndoException 
import javax.swing.undo.CannotRedoException 
import javax.swing.filechooser.FileNameExtensionFilter 
import java.security.MessageDigest 
import java.security.NoSuchAlgorithmException 
import edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandler 
import javax.swing.table.TableColumnModel 
import javax.swing.ListSelectionModel 
import java.awt.datatransfer.StringSelection 
import javax.swing.TransferHandler 
import javax.swing.TransferHandler.TransferSupport 
import java.awt.datatransfer.DataFlavor 
import java.awt.datatransfer.Transferable 
import java.awt.datatransfer.UnsupportedFlavorException 
import org.json.JSONObject 
import org.json.JSONException 
import edu.harvard.mcz.imagecapture.exceptions.NoSuchValueException 
import java.lang.RuntimeException 
import org.hibernate.LockMode 
import javax.persistence.criteria.CriteriaBuilder 
import javax.persistence.criteria.Root 
import org.hibernate.metadata.ClassMetadata 
import edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycle 
import javax.persistence.PersistenceException 
import org.hibernate.exception.ConstraintViolationException 
import org.hibernate.criterion.Example 
import org.hibernate.criterion.Projections 
import org.hibernate.criterion.Restrictions 
import edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycle 
import edu.harvard.mcz.imagecapture.entity.HigherTaxon 
import edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycle 
import org.flywaydb.core.Flyway 
import org.flywaydb.core.api.FlywayException 
import edu.harvard.mcz.imagecapture.ETHZBarcode 
import edu.harvard.mcz.imagecapture.interfaces.OCR 
import edu.harvard.mcz.imagecapture.TesseractOCR 
import edu.harvard.mcz.imagecapture.MCZENTBarcode 
import javax.swing.UIManager 
import javax.swing.UnsupportedLookAndFeelException 
import java.lang.ClassNotFoundException 
import edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditor 
import java.util.regex.PatternSyntaxException 
import javax.swing.JToolBar 
import edu.harvard.mcz.imagecapture.EditUserPanel 
import edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModel 
import java.util.StringTokenizer 
import edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetector 
import org.imgscalr.Scalr 
import com.google.zxing.LuminanceSource 
import com.google.zxing.client.j2se.BufferedImageLuminanceSource 
import edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatus 
import com.google.zxing.BinaryBitmap 
import com.google.zxing.common.HybridBinarizer 
import java.awt.image.Kernel 
import java.awt.image.ConvolveOp 
import java.awt.image.AffineTransformOp 
import java.awt.image.RescaleOp 
import com.google.zxing.qrcode.QRCodeReader 
import com.google.zxing.DecodeHintType 
import boofcv.struct.image.GrayU8 
import boofcv.io.image.ConvertBufferedImage 
import boofcv.abst.fiducial.QrCodeDetector 
import boofcv.factory.fiducial.FactoryFiducial 
import boofcv.alg.fiducial.qrcode.QrCode 
import georegression.struct.shapes.Polygon2D_F64 
import edu.harvard.mcz.imagecapture.ConvertTesseractOCR 
import com.drew.imaging.jpeg.JpegMetadataReader 
import com.drew.metadata.exif.ExifSubIFDDirectory 
import com.drew.metadata.exif.ExifSubIFDDescriptor 
import com.drew.imaging.jpeg.JpegProcessingException 
import com.drew.metadata.xmp.XmpDirectory 
import com.drew.imaging.ImageMetadataReader 
import com.drew.metadata.exif.ExifIFD0Directory 
import com.drew.metadata.jpeg.JpegDirectory 
import com.drew.metadata.MetadataException 
import com.drew.imaging.ImageProcessingException 
import com.google.zxing.ChecksumException 
import javax.imageio.ImageTypeSpecifier 
import javax.imageio.ImageWriter 
import edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawing 
import edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * WhatsThisImageDialog is a dialog to allow users to identify the
 * nature (specimen, drawer) of images for which barcode detection and
 * OCR isn't able to detect the nature of the image.
 */
class WhatsThisImageDialog : JDialog {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var imagePanel: ImageZoomPanel? = null
    private var jLabel: JLabel? = null
    private var jLabel1: JLabel? = null
    private var jLabel2: JLabel? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jLabel3: JLabel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jButton: JButton? = null
    private var jPanel2: JPanel? = null
    private var jLabel4: JLabel? = null
    private var jComboBox: JComboBox<*>? = null
    private var thisDialog: WhatsThisImageDialog? = null

    /**
     * Default constructor, probably not the one to use,
     * as image needs to be provided.
     *
     * @param owner the parent frame for this dialog.
     */
    constructor(owner: Frame?) : super(owner, true) {
        thisDialog = this
        initialize()
    }

    /**
     * Constructor with the image to display as a parameter.
     *
     * @param owner       the parent frame for this dialog.
     * @param imageToShow the image of unknown nature to display.
     */
    constructor(owner: Frame?, imageToShow: BufferedImage?) : super(owner, true) {
        thisDialog = this
        initialize()
        setImage(imageToShow)
    }

    /**
     * Constructor with an image File to display as a parameter.  Will
     * display a broken image icon if File can't be read as an
     * image.
     *
     * @param owner           the parent frame for this dialog.
     * @param imageFileToShow the image file of unknown nature to display.
     */
    constructor(owner: Frame?, imageFileToShow: File) : super(owner, true) {
        thisDialog = this
        initialize()
        val image: BufferedImage
        try {
            image = ImageIO.read(imageFileToShow)
            setImage(image)
        } catch (e: IOException) {
            log.error("Unable to open selected image " + imageFileToShow.getName())
            log.debug(e)
            val errorFilename: URL? = this.javaClass.getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/unableToLoadImage.jpg")
            try {
                setImage(ImageIO.read(errorFilename))
            } catch (e1: IOException) {
                log.error("Unable to open resource image")
                log.error(e1)
            }
        }
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setSize(755, 357)
        this.setTitle("What is this image")
        this.setContentPane(getJContentPane())
    }

    val barcode: String?
        get() = jTextFieldBarcode.getText()

    val drawerNumber: String?
        get() = jTextFieldDrawerNumber.getText()

    fun setImage(anImage: BufferedImage?) {
        imagePanel.setImage(anImage)
    }

    val isSpecimen: Boolean
        get() {
            var result = false
            if (jComboBox.getSelectedItem() == SEL_SPECIMEN &&
                    Singleton.getBarcodeMatcher().matchesPattern(
                            jTextFieldBarcode.getText())) {
                result = true
            }
            return result
        }

    val isDrawerImage: Boolean
        get() {
            var result = false
            if (jComboBox.getSelectedItem() == SEL_DRAWER &&
                    jTextFieldDrawerNumber.getText().matches(
                            Singleton
                                    .getProperties()
                                    .getProperties()
                                    .getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                result = true
            }
            return result
        }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel? {
        if (jContentPane == null) {
            jContentPane = JPanel()
            jContentPane.setLayout(BorderLayout())
            jContentPane.add(getJPanel(), BorderLayout.WEST)
            jContentPane.add(getImagePanel(), BorderLayout.CENTER)
        }
        return jContentPane
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.fill = GridBagConstraints.BOTH
            gridBagConstraints9.gridy = 4
            gridBagConstraints9.weightx = 1.0
            gridBagConstraints9.anchor = GridBagConstraints.WEST
            gridBagConstraints9.gridx = 1
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.gridx = 0
            gridBagConstraints8.anchor = GridBagConstraints.EAST
            gridBagConstraints8.gridy = 4
            jLabel4 = JLabel()
            jLabel4.setText("Image Of:")
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 1
            gridBagConstraints7.weighty = 1.0
            gridBagConstraints7.gridy = 6
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 1
            gridBagConstraints6.gridy = 5
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 3
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.anchor = GridBagConstraints.WEST
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.gridx = 0
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.weighty = 0.0
            gridBagConstraints4.gridy = 3
            jLabel3 = JLabel()
            jLabel3.setText("DrawerNumber:")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.gridwidth = 2
            gridBagConstraints3.weighty = 0.0
            gridBagConstraints3.anchor = GridBagConstraints.NORTH
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.fill = GridBagConstraints.BOTH
            gridBagConstraints2.gridy = 2
            gridBagConstraints2.weightx = 1.0
            gridBagConstraints2.anchor = GridBagConstraints.WEST
            gridBagConstraints2.gridx = 1
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridx = 0
            gridBagConstraints1.anchor = GridBagConstraints.EAST
            gridBagConstraints1.weighty = 0.0
            gridBagConstraints1.gridy = 2
            jLabel2 = JLabel()
            jLabel2.setText("Barcode:")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.insets = Insets(0, 0, 5, 0)
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.gridy = 1
            jLabel1 = JLabel()
            jLabel1.setText("Please Identify this Image.")
            jLabel = JLabel()
            jLabel.setText("No Barcode or drawer number found.")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, gridBagConstraints3)
            jPanel.add(jLabel1, gridBagConstraints)
            jPanel.add(jLabel2, gridBagConstraints1)
            jPanel.add(getJTextFieldBarcode(), gridBagConstraints2)
            jPanel.add(jLabel3, gridBagConstraints4)
            jPanel.add(getJTextFieldDrawerNumber(), gridBagConstraints5)
            jPanel.add(getJButton(), gridBagConstraints6)
            jPanel.add(getJPanel2(), gridBagConstraints7)
            jPanel.add(jLabel4, gridBagConstraints8)
            jPanel.add(getJComboBox(), gridBagConstraints9)
        }
        return jPanel
    }

    /**
     * This method initializes imagePanel
     *
     * @return javax.swing.JPanel
     */
    private fun getImagePanel(): ImageZoomPanel? {
        if (imagePanel == null) {
            imagePanel = ImageZoomPanel()
        }
        return imagePanel
    }

    /**
     * This method initializes jTextFieldBarcode
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldBarcode(): JTextField? {
        if (jTextFieldBarcode == null) {
            jTextFieldBarcode = JTextField()
            jTextFieldBarcode.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    checkValues()
                }
            })
        }
        return jTextFieldBarcode
    }

    /**
     * This method initializes jTextFieldDrawerNumber
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDrawerNumber(): JTextField? {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = JTextField()
            jTextFieldDrawerNumber.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    checkValues()
                }
            })
        }
        return jTextFieldDrawerNumber
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Continue")
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // test for required values based on the selection on the combo box
                    var okToClose = false
                    if (jComboBox.getSelectedItem() == SEL_SPECIMEN) {
                        if (Singleton
                                        .getBarcodeMatcher()
                                        .matchesPattern(jTextFieldBarcode.getText())) {
                            okToClose = true
                        } else {
                            jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                        }
                    }
                    if (jComboBox.getSelectedItem() == SEL_DRAWER) {
                        if (jTextFieldDrawerNumber.getText().matches(
                                        Singleton
                                                .getProperties()
                                                .getProperties()
                                                .getProperty(
                                                        ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                            okToClose = true
                        } else {
                            jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                        }
                    }
                    if (jComboBox.getSelectedItem() == SEL_DRAWER) {
                        okToClose = true
                    }
                    // Only close if set of values makes sense.
                    if (okToClose) {
                        thisDialog.setVisible(false)
                    }
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel2(): JPanel? {
        if (jPanel2 == null) {
            jPanel2 = JPanel()
            jPanel2.setLayout(GridBagLayout())
        }
        return jPanel2
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBox(): JComboBox<*>? {
        if (jComboBox == null) {
            jComboBox = JComboBox<Any?>()
            jComboBox.addItem("")
            jComboBox.addItem(SEL_SPECIMEN)
            jComboBox.addItem(SEL_DRAWER)
            jComboBox.addItem(SEL_OTHER)
            jComboBox.setSelectedItem("")
        }
        return jComboBox
    }

    private fun checkValues() {
        if (jComboBox.getSelectedItem() == SEL_SPECIMEN) {
            if (Singleton.getBarcodeMatcher().matchesPattern(
                            jTextFieldBarcode.getText())) {
                jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            } else {
                jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
            }
        }
        if (jComboBox.getSelectedItem() == SEL_DRAWER) {
            if (jTextFieldDrawerNumber.getText().matches(
                            Singleton
                                    .getProperties()
                                    .getProperties()
                                    .getProperty(
                                            ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            } else {
                jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
            }
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private val SEL_SPECIMEN: String? = "Specimen"
        private val SEL_DRAWER: String? = "Paper in Drawer"
        private val SEL_OTHER: String? = "Other"
        private val log: Log = LogFactory.getLog(WhatsThisImageDialog::class.java) //  @jve:decl-index=0:
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
