/**
 * VerbatimCaptureDialog.java
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


import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.SpecimenController
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.ImageLoadException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener
import edu.harvard.mcz.imagecapture.loader.Verbatim
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialog
import edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.border.EmptyBorder

java.awt.event.ActionEvent
import java.io.File


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
 *
 */
class VerbatimCaptureDialog : JDialog, DataChangeListener {
    private val contentPanel: JPanel? = JPanel()
    private var specimen: Specimen? = null
    private var specimenController: SpecimenController? = null
    private var lblBarcode: JLabel? = null
    private var lblCurrentid: JLabel? = null
    private var imagePanelPinLabels: ImageZoomPanel? = null // the specimen labels from the pin.
    private var textFieldVerbLocality: JTextArea? = null
    private var textFieldVerbDate: JTextField? = null
    private var textFieldVerbCollector: JTextField? = null
    private var textFieldVerbCollection: JTextField? = null
    private var textFieldVerbNumbers: JTextField? = null
    private var textFieldVerbUnclassifiedText: JTextArea? = null
    private var textFieldQuestions: JTextField? = null
    private var comboBoxWorkflowStatus: JComboBox<String?>? = null
    private var btnPrevious: JButton? = null
    private var btnNext: JButton? = null

    /**
     * Create the dialog.
     */
    constructor() {
        init()
    }

    /**
     * Create the dialog for a specimen.
     *
     * @param targetSpecimen
     */
    constructor(targetSpecimen: Specimen?, targetSpecimenController: SpecimenController?) {
        specimen = targetSpecimen
        specimenController = targetSpecimenController
        specimenController.addListener(this)
        init()
        if (specimen != null) {
            setValues()
        }
    }

    protected fun setValues() {
        lblBarcode.setText(specimen.Barcode)
        lblCurrentid.setText(specimen.assembleScientificName())
        textFieldVerbLocality.setText(specimen.VerbatimLocality)
        textFieldVerbDate.setText(specimen.DateNos)
        textFieldVerbCollector.setText(specimen.VerbatimCollector)
        textFieldVerbCollection.setText(specimen.VerbatimCollection)
        textFieldVerbNumbers.setText(specimen.VerbatimNumbers)
        textFieldVerbUnclassifiedText.setText(specimen.VerbatimUnclassifiedText)
        comboBoxWorkflowStatus.setSelectedItem(specimen.WorkFlowStatus)
        try {
            val i: MutableIterator<ICImage?> = specimen.ICImages.iterator()
            var image: ICImage? = null
            var gotImg = false
            while (i.hasNext() && !gotImg) {
                image = i.next()
                gotImg = true
            }
            var path: String = image.Path
            if (path == null) {
                path = ""
            }
            val anImageFile = File(assemblePathWithBase(path, image.Filename))
            val defaultTemplate: PositionTemplate = PositionTemplate.Companion.findTemplateForImage(image)
            val imagefile: BufferedImage = ImageIO.read(anImageFile)
            val x: Int = defaultTemplate.LabelPosition.width
            val y: Int = defaultTemplate.LabelPosition.height
            val w: Int = defaultTemplate.LabelSize.width
            val h: Int = defaultTemplate.LabelSize.height
            setPinLabelImage(imagefile.getSubimage(x, y, w, h))
            fitPinLabels()
        } catch (e: ImageLoadException) {
            log.error(e.message, e)
            println(e.message)
        } catch (e: IOException) {
            log.error(e.message, e)
            println(e.message)
        }
        if (specimenController != null) {
            btnNext.setEnabled(specimenController.hasNextSpecimenInTable())
            btnPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable())
        } else {
            btnNext.setEnabled(false)
            btnPrevious.setEnabled(false)
        }
    }

    protected fun init() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        setTitle("Transcribe Verbatim Data")
        setMinimumSize(Dimension(1020, 640))
        setBounds(100, 100, 1020, 640)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(BorderLayout(0, 0))
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.NORTH)
            panel.setLayout(FlowLayout(FlowLayout.CENTER, 5, 5))
            val lblVerbatimDataFor = JLabel("Verbatim Data for:")
            panel.add(lblVerbatimDataFor)
            lblBarcode = JLabel("Barcode")
            panel.add(lblBarcode)
            lblCurrentid = JLabel("CurrentID")
            panel.add(lblCurrentid)
        }
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.WEST)
            val gbl_panel = GridBagLayout()
            gbl_panel.columnWidths = intArrayOf(0, 0, 0, 0)
            gbl_panel.rowHeights = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            gbl_panel.columnWeights = doubleArrayOf(0.0, 1.0, 1.0, Double.MIN_VALUE)
            gbl_panel.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE)
            panel.setLayout(gbl_panel)
            val lblNewLabel = JLabel("Locality")
            val gbc_lblNewLabel = GridBagConstraints()
            gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST
            gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel.gridx = 0
            gbc_lblNewLabel.gridy = 0
            panel.add(lblNewLabel, gbc_lblNewLabel)
            textFieldVerbLocality = JTextArea()
            textFieldVerbLocality.setRows(3)
            val gbc_textFieldVerbLocality = GridBagConstraints()
            gbc_textFieldVerbLocality.gridheight = 2
            gbc_textFieldVerbLocality.gridwidth = 2
            gbc_textFieldVerbLocality.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbLocality.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbLocality.gridx = 1
            gbc_textFieldVerbLocality.gridy = 0
            panel.add(textFieldVerbLocality, gbc_textFieldVerbLocality)
            textFieldVerbLocality.setColumns(10)
            val lblVerbatimDate = JLabel("Date")
            val gbc_lblVerbatimDate = GridBagConstraints()
            gbc_lblVerbatimDate.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimDate.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimDate.gridx = 0
            gbc_lblVerbatimDate.gridy = 2
            panel.add(lblVerbatimDate, gbc_lblVerbatimDate)
            textFieldVerbDate = JTextField()
            val gbc_textFieldVerbDate = GridBagConstraints()
            gbc_textFieldVerbDate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbDate.gridwidth = 2
            gbc_textFieldVerbDate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbDate.gridx = 1
            gbc_textFieldVerbDate.gridy = 2
            panel.add(textFieldVerbDate, gbc_textFieldVerbDate)
            textFieldVerbDate.setColumns(10)
            val lblCollector = JLabel("Collector")
            val gbc_lblCollector = GridBagConstraints()
            gbc_lblCollector.anchor = GridBagConstraints.EAST
            gbc_lblCollector.insets = Insets(0, 0, 5, 5)
            gbc_lblCollector.gridx = 0
            gbc_lblCollector.gridy = 3
            panel.add(lblCollector, gbc_lblCollector)
            textFieldVerbCollector = JTextField()
            val gbc_textFieldVerbCollector = GridBagConstraints()
            gbc_textFieldVerbCollector.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollector.gridwidth = 2
            gbc_textFieldVerbCollector.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbCollector.gridx = 1
            gbc_textFieldVerbCollector.gridy = 3
            panel.add(textFieldVerbCollector, gbc_textFieldVerbCollector)
            textFieldVerbCollector.setColumns(10)
            val lblNewLabel_1 = JLabel("Collection")
            val gbc_lblNewLabel_1 = GridBagConstraints()
            gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_1.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_1.gridx = 0
            gbc_lblNewLabel_1.gridy = 4
            panel.add(lblNewLabel_1, gbc_lblNewLabel_1)
            textFieldVerbCollection = JTextField()
            val gbc_textFieldVerbCollection = GridBagConstraints()
            gbc_textFieldVerbCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollection.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbCollection.gridx = 2
            gbc_textFieldVerbCollection.gridy = 4
            panel.add(textFieldVerbCollection, gbc_textFieldVerbCollection)
            textFieldVerbCollection.setColumns(10)
            val lblNumbers = JLabel("Numbers")
            val gbc_lblNumbers = GridBagConstraints()
            gbc_lblNumbers.anchor = GridBagConstraints.EAST
            gbc_lblNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblNumbers.gridx = 0
            gbc_lblNumbers.gridy = 5
            panel.add(lblNumbers, gbc_lblNumbers)
            textFieldVerbNumbers = JTextField()
            val gbc_textFieldVerbNumbers = GridBagConstraints()
            gbc_textFieldVerbNumbers.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbNumbers.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbNumbers.gridx = 2
            gbc_textFieldVerbNumbers.gridy = 5
            panel.add(textFieldVerbNumbers, gbc_textFieldVerbNumbers)
            textFieldVerbNumbers.setColumns(10)
            val lblNewLabel_2 = JLabel("Other Text")
            val gbc_lblNewLabel_2 = GridBagConstraints()
            gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_2.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_2.gridx = 0
            gbc_lblNewLabel_2.gridy = 6
            panel.add(lblNewLabel_2, gbc_lblNewLabel_2)
            textFieldVerbUnclassifiedText = JTextArea()
            textFieldVerbUnclassifiedText.setRows(3)
            val gbc_textFieldVerbUnclassifiedText = GridBagConstraints()
            gbc_textFieldVerbUnclassifiedText.gridheight = 2
            gbc_textFieldVerbUnclassifiedText.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbUnclassifiedText.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbUnclassifiedText.gridx = 2
            gbc_textFieldVerbUnclassifiedText.gridy = 6
            panel.add(textFieldVerbUnclassifiedText, gbc_textFieldVerbUnclassifiedText)
            textFieldVerbUnclassifiedText.setColumns(10)
            val lblQuestions = JLabel("Questions")
            val gbc_lblQuestions = GridBagConstraints()
            gbc_lblQuestions.anchor = GridBagConstraints.EAST
            gbc_lblQuestions.insets = Insets(0, 0, 5, 5)
            gbc_lblQuestions.gridx = 0
            gbc_lblQuestions.gridy = 8
            panel.add(lblQuestions, gbc_lblQuestions)
            textFieldQuestions = JTextField()
            val gbc_textFieldQuestions = GridBagConstraints()
            gbc_textFieldQuestions.insets = Insets(0, 0, 5, 5)
            gbc_textFieldQuestions.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldQuestions.gridx = 2
            gbc_textFieldQuestions.gridy = 8
            panel.add(textFieldQuestions, gbc_textFieldQuestions)
            textFieldQuestions.setColumns(30)
            val lblWorkflowStatus = JLabel("Workflow Status")
            val gbc_lblWorkflowStatus = GridBagConstraints()
            gbc_lblWorkflowStatus.anchor = GridBagConstraints.EAST
            gbc_lblWorkflowStatus.insets = Insets(0, 0, 5, 5)
            gbc_lblWorkflowStatus.gridx = 0
            gbc_lblWorkflowStatus.gridy = 9
            panel.add(lblWorkflowStatus, gbc_lblWorkflowStatus)
            comboBoxWorkflowStatus = JComboBox<String?>(WorkFlowStatus.VerbatimWorkFlowStatusValues)
            val gbc_comboBoxWorkflowStatus = GridBagConstraints()
            gbc_comboBoxWorkflowStatus.insets = Insets(0, 0, 5, 0)
            gbc_comboBoxWorkflowStatus.gridwidth = 2
            gbc_comboBoxWorkflowStatus.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxWorkflowStatus.gridx = 2
            gbc_comboBoxWorkflowStatus.gridy = 9
            panel.add(comboBoxWorkflowStatus, gbc_comboBoxWorkflowStatus)
            val panel_1 = JPanel()
            val gbc_panel_1 = GridBagConstraints()
            gbc_panel_1.gridwidth = 2
            gbc_panel_1.insets = Insets(0, 0, 0, 5)
            gbc_panel_1.fill = GridBagConstraints.BOTH
            gbc_panel_1.gridx = 2
            gbc_panel_1.gridy = 11
            panel.add(panel_1, gbc_panel_1)
            val gbl_panel_1 = GridBagLayout()
            gbl_panel_1.columnWidths = intArrayOf(150, 143, 0)
            gbl_panel_1.rowHeights = intArrayOf(25, 0, 0, 0, 0)
            gbl_panel_1.columnWeights = doubleArrayOf(0.0, 0.0, 0.0)
            gbl_panel_1.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE)
            panel_1.setLayout(gbl_panel_1)
            val btnPartiallyIllegible = JButton("Partially Illegible")
            btnPartiallyIllegible.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.PARTLY_ILLEGIBLE)
                }
            })
            val gbc_btnPartiallyIllegible = GridBagConstraints()
            gbc_btnPartiallyIllegible.fill = GridBagConstraints.HORIZONTAL
            gbc_btnPartiallyIllegible.anchor = GridBagConstraints.NORTH
            gbc_btnPartiallyIllegible.insets = Insets(0, 0, 5, 5)
            gbc_btnPartiallyIllegible.gridx = 0
            gbc_btnPartiallyIllegible.gridy = 0
            panel_1.add(btnPartiallyIllegible, gbc_btnPartiallyIllegible)
            val btnNewButton = JButton("No Locality Data")
            btnNewButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbLocality.setText(Verbatim.NO_LOCALITY_DATA)
                }
            })
            val gbc_btnNewButton = GridBagConstraints()
            gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST
            gbc_btnNewButton.insets = Insets(0, 0, 5, 5)
            gbc_btnNewButton.gridx = 1
            gbc_btnNewButton.gridy = 0
            panel_1.add(btnNewButton, gbc_btnNewButton)
            btnNewButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {}
            })
            val btnNewButton_1 = JButton("Entirely Illegible")
            btnNewButton_1.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.ENTIRELY_ILLEGIBLE)
                }
            })
            val gbc_btnNewButton_1 = GridBagConstraints()
            gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL
            gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH
            gbc_btnNewButton_1.insets = Insets(0, 0, 5, 5)
            gbc_btnNewButton_1.gridx = 0
            gbc_btnNewButton_1.gridy = 1
            panel_1.add(btnNewButton_1, gbc_btnNewButton_1)
            val btnNoDateData = JButton("No Date Data")
            btnNoDateData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbDate.setText("[No date data]")
                }
            })
            val gbc_btnNoDateData = GridBagConstraints()
            gbc_btnNoDateData.fill = GridBagConstraints.HORIZONTAL
            gbc_btnNoDateData.insets = Insets(0, 0, 5, 5)
            gbc_btnNoDateData.gridx = 1
            gbc_btnNoDateData.gridy = 1
            panel_1.add(btnNoDateData, gbc_btnNoDateData)
            val btnLabelTruncatedIn = JButton("Label Truncated in Image")
            btnLabelTruncatedIn.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.TRUNCATED_BY_IMAGE)
                }
            })
            val gbc_btnLabelTruncatedIn = GridBagConstraints()
            gbc_btnLabelTruncatedIn.insets = Insets(0, 0, 5, 5)
            gbc_btnLabelTruncatedIn.anchor = GridBagConstraints.NORTHWEST
            gbc_btnLabelTruncatedIn.gridx = 0
            gbc_btnLabelTruncatedIn.gridy = 2
            panel_1.add(btnLabelTruncatedIn, gbc_btnLabelTruncatedIn)
            val btnNoCollectorData = JButton("No Collector Data")
            btnNoCollectorData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbCollector.setText("[No collector data]")
                }
            })
            val gbc_btnNoCollectorData = GridBagConstraints()
            gbc_btnNoCollectorData.insets = Insets(0, 0, 5, 5)
            gbc_btnNoCollectorData.gridx = 1
            gbc_btnNoCollectorData.gridy = 2
            panel_1.add(btnNoCollectorData, gbc_btnNoCollectorData)
            val btnNoPinLabels = JButton("No Pin Labels")
            btnNoPinLabels.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.NO_PIN_LABELS)
                }
            })
            val gbc_btnNoPinLabels = GridBagConstraints()
            gbc_btnNoPinLabels.insets = Insets(0, 0, 0, 5)
            gbc_btnNoPinLabels.gridx = 0
            gbc_btnNoPinLabels.gridy = 3
            panel_1.add(btnNoPinLabels, gbc_btnNoPinLabels)
        }
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.CENTER)
            panel.setLayout(BorderLayout(0, 0))
            panel.add(imagePanePinLabels)
        }
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.LEFT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            btnPrevious = JButton("Previous")
            btnPrevious.setEnabled(false)
            if (specimenController != null && specimenController.isInTable()) {
                btnPrevious.setEnabled(true)
            }
            btnPrevious.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    save()
                    if (specimenController.switchToPreviousSpecimenInTable()) {
                        specimen = specimenController.Specimen
                        setValues()
                    }
                }
            })
            buttonPane.add(btnPrevious)
            btnNext = JButton("Next")
            btnNext.setEnabled(false)
            if (specimenController != null && specimenController.isInTable()) {
                btnNext.setEnabled(true)
            }
            btnNext.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    save()
                    if (specimenController.switchToNextSpecimenInTable()) {
                        specimen = specimenController.Specimen
                        setValues()
                    }
                }
            })
            buttonPane.add(btnNext)
            {
                val okButton = JButton("OK")
                okButton.setActionCommand("OK")
                okButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (save()) {
                            setVisible(false)
                        }
                    }
                })
                buttonPane.add(okButton)
                getRootPane().setDefaultButton(okButton)
            }
            {
                val cancelButton = JButton("Cancel")
                cancelButton.setActionCommand("Cancel")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
                buttonPane.add(cancelButton)
            }
        }
    }

    fun fitPinLabels() {
        imagePanelPinLabels.zoomToFit()
    }

    private val imagePanePinLabels: ImageZoomPanel?
        private get() {
            if (imagePanelPinLabels == null) {
                imagePanelPinLabels = ImageZoomPanel()
            }
            return imagePanelPinLabels
        }

    fun setPinLabelImage(anImage: Image?) {
        imagePanelPinLabels.setImage(anImage as BufferedImage?)
        imagePanelPinLabels.zoomToFit()
        this.pack()
        if (imagePanelPinLabels.PreferredSize.height > 500 || imagePanelPinLabels.PreferredSize.width > 500) {
            imagePanelPinLabels.setPreferredSize(Dimension(500, 500))
        }
        imagePanelPinLabels.setMaximumSize(Dimension(500, 500))
    }

    protected fun save(): Boolean {
        var result = false
        try {
            specimen.setVerbatimLocality(textFieldVerbLocality.Text)
            specimen.setDateNos(textFieldVerbDate.Text)
            specimen.setVerbatimCollector(textFieldVerbCollector.Text)
            specimen.setVerbatimCollection(textFieldVerbCollection.Text)
            specimen.setVerbatimNumbers(textFieldVerbNumbers.Text)
            specimen.setVerbatimUnclassifiedText(textFieldVerbUnclassifiedText.Text)
            val questions = StringBuffer()
            questions.append(specimen.Questions)
            if (textFieldQuestions.Text != null && textFieldQuestions.Text.trim({ it <= ' ' }).length > 0) {
                if (!questions.toString().contains(textFieldQuestions.Text)) {
                    questions.append(Verbatim.SEPARATOR).append(textFieldQuestions.Text)
                }
            }
            val workflowstatus = (comboBoxWorkflowStatus.SelectedItem as String)
            specimen.setWorkFlowStatus(workflowstatus)
            specimen.setQuestions(questions.toString())
            specimenController.save()
            result = true
        } catch (e: SaveFailedException) {
            log.error(e.message, e)
            // TODO: Notify user
        }
        return result
    }

    protected fun appendToQuestions(newQuestion: String) {
        if (!textFieldQuestions.Text.contains(newQuestion)) {
            val questions = StringBuffer()
            questions.append(textFieldQuestions.Text).append(Verbatim.SEPARATOR).append(newQuestion)
            textFieldQuestions.setText(questions.toString())
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#notifyDataHasChanged()
     */
    override fun notifyDataHasChanged() { // TODO Auto-generated method stub
    }

    companion object {
        private const val serialVersionUID = 4462958599102371519L
        private val log: Log = LogFactory.getLog(VerbatimCaptureDialog::class.java)
    }
}
