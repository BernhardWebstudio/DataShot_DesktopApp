/**
 * VerbatimClassifyDialog.java
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


import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialog
import edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBox
import edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBox
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel
import edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModel
import edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModel
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.filteredpush.qc.date.DateUtils
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.BoxLayout
import javax.swing.border.EmptyBorder
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableColumn

java.awt.Insets
import  java.awt.event.ActionEvent

java.util.*import javax.swing.table.TableColumn


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
class VerbatimClassifyDialog : JDialog {
    private val contentPanel: JPanel? = JPanel()
    private var verbatimData: VerbatimCount? = null
    private var comboBoxHigherGeog: FilteringGeogJComboBox? = null
    private var textFieldVerbLocality: JTextArea? = null
    private var textFieldMinElevation: JTextField? = null
    private var textFieldMaxElevation: JTextField? = null
    private var comboBoxElevUnits: JComboBox<*>? = null
    private var lblCount: JLabel? = null
    private var textFieldVerbDate: JTextField? = null
    private var textFieldISODate: JTextField? = null
    private var textFieldVerbCollector: JTextArea? = null
    private var textFieldVerbCollection: JTextArea? = null
    private var textFieldVerbNumbers: JTextArea? = null
    private var textFieldVerbUnclassifiedText: JTextArea? = null
    private var textFieldSpecificLocality: JTextField? = null
    private var jScrollPaneCollectors: JScrollPane? = null
    private var jTableCollectors: JTable? = null
    private var jComboBoxCollection: JComboBox<*>? = null
    private var jScrollPaneNumbers: JScrollPane? = null
    private var jTableNumbers: JTable? = null
    private var textFieldHabitat: JTextField? = null
    private var textFieldMicrohabitat: JTextField? = null
    private var comboBoxWorkflowStatus: JComboBox<*>? = null
    private var tableTaxa: JTable? = null
    private var lastEditedSpecimen: Specimen? = null

    /**
     * Create the dialog.
     */
    constructor() {
        verbatimData = VerbatimCount(0, "", "", "", "", "", "")
        init()
        setValues()
    }

    /**
     * Create the dialog for a particular instance of a set of verbatim values.
     *
     * @param verbatimCount the set of verbatim values to display.
     */
    constructor(verbatimCount: VerbatimCount?) {
        verbatimData = verbatimCount
        init()
        setValues()
    }

    protected fun setValues() {
        lblCount.setText("Number of records with these verbatim values: " + verbatimData.getCount())
        textFieldVerbLocality.setText(verbatimData.getVerbatimLocality())
        textFieldVerbDate.setText(verbatimData.getVerbatimDate())
        textFieldVerbDate.setEditable(false)
        textFieldVerbCollection.setText(verbatimData.getVerbatimCollection())
        textFieldVerbCollector.setText(verbatimData.getVerbatimCollector())
        textFieldVerbNumbers.setText(verbatimData.getVerbatimNumbers())
        textFieldVerbUnclassifiedText.setText(verbatimData.getVerbatimUnclassfiedText())
    }

    protected fun init() {
        setTitle("Interpret verbatim data into fields.")
        setBounds(100, 100, 1203, 899)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(BoxLayout(contentPanel, BoxLayout.Y_AXIS))
        run {
            val panelVerbatimValues = JPanel()
            contentPanel.add(panelVerbatimValues)
            val gbl_panelVerbatimValues = GridBagLayout()
            gbl_panelVerbatimValues.columnWidths = intArrayOf(70, 0, 0, 0, 35, 0, 58, 0, 0, 0)
            gbl_panelVerbatimValues.rowHeights = intArrayOf(15, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 83, 0)
            gbl_panelVerbatimValues.columnWeights = doubleArrayOf(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0)
            gbl_panelVerbatimValues.rowWeights = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE)
            panelVerbatimValues.setLayout(gbl_panelVerbatimValues)
            {
                lblCount = JLabel("New label")
                val gbc_lblNewLabel = GridBagConstraints()
                gbc_lblNewLabel.gridwidth = 2
                gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST
                gbc_lblNewLabel.gridx = 0
                gbc_lblNewLabel.gridy = 0
                panelVerbatimValues.add(lblCount, gbc_lblNewLabel)
            }
            val buttonFillFromLast = JButton("Fill From Last")
            val gbc_buttonFillFromLast = GridBagConstraints()
            gbc_buttonFillFromLast.anchor = GridBagConstraints.SOUTH
            gbc_buttonFillFromLast.insets = Insets(0, 0, 5, 5)
            gbc_buttonFillFromLast.gridx = 3
            gbc_buttonFillFromLast.gridy = 0
            panelVerbatimValues.add(buttonFillFromLast, gbc_buttonFillFromLast)
            {
                val lblNewLabel_1 = JLabel("Field values to apply to all records.")
                val gbc_lblNewLabel_1 = GridBagConstraints()
                gbc_lblNewLabel_1.anchor = GridBagConstraints.SOUTHEAST
                gbc_lblNewLabel_1.gridwidth = 4
                gbc_lblNewLabel_1.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel_1.gridx = 4
                gbc_lblNewLabel_1.gridy = 0
                panelVerbatimValues.add(lblNewLabel_1, gbc_lblNewLabel_1)
            }
            {
                val lblNewLabel_2 = JLabel("Verbatim Locality")
                val gbc_lblNewLabel_2 = GridBagConstraints()
                gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST
                gbc_lblNewLabel_2.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel_2.gridx = 0
                gbc_lblNewLabel_2.gridy = 1
                panelVerbatimValues.add(lblNewLabel_2, gbc_lblNewLabel_2)
            }
            {
                textFieldVerbLocality = JTextArea()
                textFieldVerbLocality.setEditable(false)
                textFieldVerbLocality.setRows(5)
                val gbc_textField = GridBagConstraints()
                gbc_textField.gridheight = 3
                gbc_textField.insets = Insets(0, 0, 5, 5)
                gbc_textField.fill = GridBagConstraints.BOTH
                gbc_textField.gridx = 1
                gbc_textField.gridy = 1
                panelVerbatimValues.add(textFieldVerbLocality, gbc_textField)
                textFieldVerbLocality.setColumns(30)
            }
            val lblNewLabel = JLabel("HigherGeography")
            val gbc_lblNewLabel = GridBagConstraints()
            gbc_lblNewLabel.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel.gridx = 3
            gbc_lblNewLabel.gridy = 1
            panelVerbatimValues.add(lblNewLabel, gbc_lblNewLabel)
            val gbc_textFieldHigherGeography = GridBagConstraints()
            gbc_textFieldHigherGeography.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldHigherGeography.gridwidth = 5
            gbc_textFieldHigherGeography.insets = Insets(0, 0, 5, 5)
            gbc_textFieldHigherGeography.gridx = 4
            gbc_textFieldHigherGeography.gridy = 1
            panelVerbatimValues.add(comboBoxHighGeog, gbc_textFieldHigherGeography)
            val btnCopyLocality = JButton(">")
            btnCopyLocality.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (textFieldSpecificLocality.getText().isEmpty()) {
                        textFieldSpecificLocality.setText(textFieldVerbLocality.getText().replace("\n", "").trim({ it <= ' ' }))
                    }
                }
            })
            val gbc_btnCopyLocality = GridBagConstraints()
            gbc_btnCopyLocality.insets = Insets(0, 0, 5, 5)
            gbc_btnCopyLocality.gridx = 2
            gbc_btnCopyLocality.gridy = 2
            panelVerbatimValues.add(btnCopyLocality, gbc_btnCopyLocality)
            val lblSpecificLocality = JLabel("Specific Locality")
            val gbc_lblSpecificLocality = GridBagConstraints()
            gbc_lblSpecificLocality.anchor = GridBagConstraints.EAST
            gbc_lblSpecificLocality.insets = Insets(0, 0, 5, 5)
            gbc_lblSpecificLocality.gridx = 3
            gbc_lblSpecificLocality.gridy = 2
            panelVerbatimValues.add(lblSpecificLocality, gbc_lblSpecificLocality)
            val lblElevation = JLabel("Elevation")
            val gbc_lblElevation = GridBagConstraints()
            gbc_lblElevation.anchor = GridBagConstraints.EAST
            gbc_lblElevation.insets = Insets(0, 0, 5, 5)
            gbc_lblElevation.gridx = 3
            gbc_lblElevation.gridy = 3
            panelVerbatimValues.add(lblElevation, gbc_lblElevation)
            textFieldMinElevation = JTextField()
            textFieldMinElevation.setMinimumSize(Dimension(60, 19))
            val gbc_textFieldMinElevation = GridBagConstraints()
            gbc_textFieldMinElevation.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMinElevation.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldMinElevation.gridx = 4
            gbc_textFieldMinElevation.gridy = 3
            panelVerbatimValues.add(textFieldMinElevation, gbc_textFieldMinElevation)
            textFieldMinElevation.setColumns(4)
            val lblTo = JLabel("to")
            val gbc_lblTo = GridBagConstraints()
            gbc_lblTo.anchor = GridBagConstraints.EAST
            gbc_lblTo.insets = Insets(0, 0, 5, 5)
            gbc_lblTo.gridx = 5
            gbc_lblTo.gridy = 3
            panelVerbatimValues.add(lblTo, gbc_lblTo)
            textFieldMaxElevation = JTextField()
            textFieldMaxElevation.setMinimumSize(Dimension(60, 19))
            val gbc_textFieldMaxElevation = GridBagConstraints()
            gbc_textFieldMaxElevation.anchor = GridBagConstraints.WEST
            gbc_textFieldMaxElevation.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMaxElevation.gridx = 6
            gbc_textFieldMaxElevation.gridy = 3
            panelVerbatimValues.add(textFieldMaxElevation, gbc_textFieldMaxElevation)
            textFieldMaxElevation.setColumns(5)
            comboBoxElevUnits = JComboBox<Any?>()
            val gbc_comboBoxElevUnits = GridBagConstraints()
            gbc_comboBoxElevUnits.insets = Insets(0, 0, 5, 5)
            gbc_comboBoxElevUnits.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxElevUnits.gridx = 7
            gbc_comboBoxElevUnits.gridy = 3
            panelVerbatimValues.add(comboBoxElevUnits, gbc_comboBoxElevUnits)
            {
                val lblVerbatimdate = JLabel("VerbatimDate")
                val gbc_lblVerbatimdate = GridBagConstraints()
                gbc_lblVerbatimdate.anchor = GridBagConstraints.EAST
                gbc_lblVerbatimdate.insets = Insets(0, 0, 5, 5)
                gbc_lblVerbatimdate.gridx = 0
                gbc_lblVerbatimdate.gridy = 4
                panelVerbatimValues.add(lblVerbatimdate, gbc_lblVerbatimdate)
            }
            textFieldVerbDate = JTextField()
            val gbc_textFieldVerbDate = GridBagConstraints()
            gbc_textFieldVerbDate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbDate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbDate.gridx = 1
            gbc_textFieldVerbDate.gridy = 4
            panelVerbatimValues.add(textFieldVerbDate, gbc_textFieldVerbDate)
            textFieldVerbDate.setColumns(30)
            val btnCopyDate = JButton(">")
            btnCopyDate.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (textFieldISODate.getText().isEmpty()) {
                        val extractResult: MutableMap<String?, String?> = DateUtils.extractDateFromVerbatim(textFieldVerbDate.getText().trim({ it <= ' ' }))
                        if (extractResult.containsKey("result")) {
                            textFieldISODate.setText(extractResult["result"])
                        }
                        if (extractResult.containsKey("resultState")) { // TODO: Report suspect etc
                        }
                    }
                }
            })
            val gbc_btnCopyDate = GridBagConstraints()
            gbc_btnCopyDate.insets = Insets(0, 0, 5, 5)
            gbc_btnCopyDate.gridx = 2
            gbc_btnCopyDate.gridy = 4
            panelVerbatimValues.add(btnCopyDate, gbc_btnCopyDate)
            val lblNewLabel_3 = JLabel("ISO Date")
            val gbc_lblNewLabel_3 = GridBagConstraints()
            gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_3.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_3.gridx = 3
            gbc_lblNewLabel_3.gridy = 4
            panelVerbatimValues.add(lblNewLabel_3, gbc_lblNewLabel_3)
            textFieldISODate = JTextField()
            textFieldISODate.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", textFieldISODate))
            textFieldISODate.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ISODate"))
            val gbc_textFieldISODate = GridBagConstraints()
            gbc_textFieldISODate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldISODate.gridwidth = 5
            gbc_textFieldISODate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldISODate.gridx = 4
            gbc_textFieldISODate.gridy = 4
            panelVerbatimValues.add(textFieldISODate, gbc_textFieldISODate)
            textFieldISODate.setColumns(10)
            textFieldSpecificLocality = JTextField()
            val gbc_textFieldSpecificLocality = GridBagConstraints()
            gbc_textFieldSpecificLocality.gridwidth = 5
            gbc_textFieldSpecificLocality.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldSpecificLocality.insets = Insets(0, 0, 5, 5)
            gbc_textFieldSpecificLocality.gridx = 4
            gbc_textFieldSpecificLocality.gridy = 2
            panelVerbatimValues.add(textFieldSpecificLocality, gbc_textFieldSpecificLocality)
            textFieldSpecificLocality.setColumns(25)
            {
                val lblVerbatimCollector = JLabel("Verbatim Collector")
                val gbc_lblVerbatimCollector = GridBagConstraints()
                gbc_lblVerbatimCollector.anchor = GridBagConstraints.EAST
                gbc_lblVerbatimCollector.insets = Insets(0, 0, 5, 5)
                gbc_lblVerbatimCollector.gridx = 0
                gbc_lblVerbatimCollector.gridy = 6
                panelVerbatimValues.add(lblVerbatimCollector, gbc_lblVerbatimCollector)
            }
            textFieldVerbCollector = JTextArea()
            textFieldVerbCollector.setEditable(false)
            textFieldVerbCollector.setRows(3)
            val gbc_textFieldVerbCollector = GridBagConstraints()
            gbc_textFieldVerbCollector.gridheight = 2
            gbc_textFieldVerbCollector.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollector.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbCollector.gridx = 1
            gbc_textFieldVerbCollector.gridy = 6
            panelVerbatimValues.add(textFieldVerbCollector, gbc_textFieldVerbCollector)
            textFieldVerbCollector.setColumns(30)
            val lblCollectors = JLabel("Collector(s)")
            val gbc_lblCollectors = GridBagConstraints()
            gbc_lblCollectors.anchor = GridBagConstraints.EAST
            gbc_lblCollectors.insets = Insets(0, 0, 5, 5)
            gbc_lblCollectors.gridx = 3
            gbc_lblCollectors.gridy = 6
            panelVerbatimValues.add(lblCollectors, gbc_lblCollectors)
            val btnAddCollector = JButton("+")
            btnAddCollector.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    (jTableCollectors.getModel() as CollectorTableModel).addCollector(Collector(null, ""))
                }
            })
            val gbc_btnAddCollector = GridBagConstraints()
            gbc_btnAddCollector.anchor = GridBagConstraints.EAST
            gbc_btnAddCollector.insets = Insets(0, 0, 5, 5)
            gbc_btnAddCollector.gridx = 3
            gbc_btnAddCollector.gridy = 7
            panelVerbatimValues.add(btnAddCollector, gbc_btnAddCollector)
            val gbc_collectorTable = GridBagConstraints()
            gbc_collectorTable.insets = Insets(0, 0, 5, 5)
            gbc_collectorTable.fill = GridBagConstraints.BOTH
            gbc_collectorTable.gridx = 4
            gbc_collectorTable.gridy = 6
            gbc_collectorTable.gridheight = 2
            gbc_collectorTable.gridwidth = 5
            panelVerbatimValues.add(getJScrollPaneCollectors(), gbc_collectorTable)
            val lblVerbatimCollection = JLabel("Verbatim Collection")
            val gbc_lblVerbatimCollection = GridBagConstraints()
            gbc_lblVerbatimCollection.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimCollection.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimCollection.gridx = 0
            gbc_lblVerbatimCollection.gridy = 8
            panelVerbatimValues.add(lblVerbatimCollection, gbc_lblVerbatimCollection)
            textFieldVerbCollection = JTextArea()
            textFieldVerbCollection.setEditable(false)
            textFieldVerbCollection.setRows(3)
            val gbc_textFieldVerbCollection = GridBagConstraints()
            gbc_textFieldVerbCollection.gridheight = 2
            gbc_textFieldVerbCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollection.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbCollection.gridx = 1
            gbc_textFieldVerbCollection.gridy = 8
            panelVerbatimValues.add(textFieldVerbCollection, gbc_textFieldVerbCollection)
            textFieldVerbCollection.setColumns(30)
            val lblCollection = JLabel("Collection")
            val gbc_lblCollection = GridBagConstraints()
            gbc_lblCollection.anchor = GridBagConstraints.EAST
            gbc_lblCollection.insets = Insets(0, 0, 5, 5)
            gbc_lblCollection.gridx = 3
            gbc_lblCollection.gridy = 8
            panelVerbatimValues.add(lblCollection, gbc_lblCollection)
            val gbc_textFieldCollection = GridBagConstraints()
            gbc_textFieldCollection.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldCollection.gridwidth = 5
            gbc_textFieldCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldCollection.gridx = 4
            gbc_textFieldCollection.gridy = 8
            panelVerbatimValues.add(getJComboBoxCollection(), gbc_textFieldCollection)
            val lblVerbatimNumbers = JLabel("Verbatim Numbers")
            val gbc_lblVerbatimNumbers = GridBagConstraints()
            gbc_lblVerbatimNumbers.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimNumbers.gridx = 0
            gbc_lblVerbatimNumbers.gridy = 10
            panelVerbatimValues.add(lblVerbatimNumbers, gbc_lblVerbatimNumbers)
            textFieldVerbNumbers = JTextArea()
            textFieldVerbNumbers.setRows(3)
            textFieldVerbNumbers.setEditable(false)
            val gbc_textFieldVerbNumbers = GridBagConstraints()
            gbc_textFieldVerbNumbers.gridheight = 2
            gbc_textFieldVerbNumbers.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbNumbers.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbNumbers.gridx = 1
            gbc_textFieldVerbNumbers.gridy = 10
            panelVerbatimValues.add(textFieldVerbNumbers, gbc_textFieldVerbNumbers)
            textFieldVerbNumbers.setColumns(30)
            val lblNumbers = JLabel("Numbers")
            val gbc_lblNumbers = GridBagConstraints()
            gbc_lblNumbers.anchor = GridBagConstraints.EAST
            gbc_lblNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblNumbers.gridx = 3
            gbc_lblNumbers.gridy = 10
            panelVerbatimValues.add(lblNumbers, gbc_lblNumbers)
            val btnAddNumber = JButton("+")
            btnAddNumber.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    (jTableNumbers.getModel() as NumberTableModel).addNumber(Number(null, "", ""))
                }
            })
            val gbc_btnAddNumber = GridBagConstraints()
            gbc_btnAddNumber.anchor = GridBagConstraints.EAST
            gbc_btnAddNumber.insets = Insets(0, 0, 5, 5)
            gbc_btnAddNumber.gridx = 3
            gbc_btnAddNumber.gridy = 11
            panelVerbatimValues.add(btnAddNumber, gbc_btnAddNumber)
            val gbc_numberTable = GridBagConstraints()
            gbc_numberTable.insets = Insets(0, 0, 5, 5)
            gbc_numberTable.fill = GridBagConstraints.BOTH
            gbc_numberTable.gridx = 4
            gbc_numberTable.gridy = 10
            gbc_numberTable.gridheight = 2
            gbc_numberTable.gridwidth = 5
            panelVerbatimValues.add(this.getJScrollPaneNumbers(), gbc_numberTable)
            val lblVerbatimOtherText = JLabel("Verbatim Other Text")
            val gbc_lblVerbatimOtherText = GridBagConstraints()
            gbc_lblVerbatimOtherText.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimOtherText.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimOtherText.gridx = 0
            gbc_lblVerbatimOtherText.gridy = 12
            panelVerbatimValues.add(lblVerbatimOtherText, gbc_lblVerbatimOtherText)
            textFieldVerbUnclassifiedText = JTextArea()
            textFieldVerbUnclassifiedText.setEditable(false)
            textFieldVerbUnclassifiedText.setRows(5)
            val gbc_textFieldVerbUnclassifiedText = GridBagConstraints()
            gbc_textFieldVerbUnclassifiedText.gridheight = 3
            gbc_textFieldVerbUnclassifiedText.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbUnclassifiedText.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbUnclassifiedText.gridx = 1
            gbc_textFieldVerbUnclassifiedText.gridy = 12
            panelVerbatimValues.add(textFieldVerbUnclassifiedText, gbc_textFieldVerbUnclassifiedText)
            textFieldVerbUnclassifiedText.setColumns(30)
            val lblHabitat = JLabel("Habitat")
            val gbc_lblHabitat = GridBagConstraints()
            gbc_lblHabitat.anchor = GridBagConstraints.EAST
            gbc_lblHabitat.insets = Insets(0, 0, 5, 5)
            gbc_lblHabitat.gridx = 3
            gbc_lblHabitat.gridy = 12
            panelVerbatimValues.add(lblHabitat, gbc_lblHabitat)
            textFieldHabitat = JTextField()
            val gbc_textFieldHabitat = GridBagConstraints()
            gbc_textFieldHabitat.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldHabitat.gridwidth = 4
            gbc_textFieldHabitat.insets = Insets(0, 0, 5, 5)
            gbc_textFieldHabitat.gridx = 4
            gbc_textFieldHabitat.gridy = 12
            panelVerbatimValues.add(textFieldHabitat, gbc_textFieldHabitat)
            textFieldHabitat.setColumns(10)
            val lblMicrohabitat = JLabel("Microhabitat")
            val gbc_lblMicrohabitat = GridBagConstraints()
            gbc_lblMicrohabitat.anchor = GridBagConstraints.EAST
            gbc_lblMicrohabitat.insets = Insets(0, 0, 5, 5)
            gbc_lblMicrohabitat.gridx = 3
            gbc_lblMicrohabitat.gridy = 13
            panelVerbatimValues.add(lblMicrohabitat, gbc_lblMicrohabitat)
            textFieldMicrohabitat = JTextField()
            val gbc_textFieldMicrohabitat = GridBagConstraints()
            gbc_textFieldMicrohabitat.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldMicrohabitat.gridwidth = 4
            gbc_textFieldMicrohabitat.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMicrohabitat.gridx = 4
            gbc_textFieldMicrohabitat.gridy = 13
            panelVerbatimValues.add(textFieldMicrohabitat, gbc_textFieldMicrohabitat)
            textFieldMicrohabitat.setColumns(10)
            val lblNewLabel_4 = JLabel("Workflow Status")
            val gbc_lblNewLabel_4 = GridBagConstraints()
            gbc_lblNewLabel_4.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_4.gridx = 3
            gbc_lblNewLabel_4.gridy = 15
            panelVerbatimValues.add(lblNewLabel_4, gbc_lblNewLabel_4)
            comboBoxWorkflowStatus = JComboBox<Any?>(WorkFlowStatus.getVerbatimClassifiedWorkFlowStatusValues())
            comboBoxWorkflowStatus.setSelectedItem(WorkFlowStatus.STAGE_CLASSIFIED)
            val gbc_comboBoxWorkflowStatus = GridBagConstraints()
            gbc_comboBoxWorkflowStatus.gridwidth = 4
            gbc_comboBoxWorkflowStatus.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxWorkflowStatus.insets = Insets(0, 0, 5, 5)
            gbc_comboBoxWorkflowStatus.gridx = 4
            gbc_comboBoxWorkflowStatus.gridy = 15
            panelVerbatimValues.add(comboBoxWorkflowStatus, gbc_comboBoxWorkflowStatus)
            val btnApplyToAll = JButton("Apply To All Records")
            val gbc_btnApplyToAll = GridBagConstraints()
            gbc_btnApplyToAll.gridwidth = 4
            gbc_btnApplyToAll.insets = Insets(0, 0, 5, 5)
            gbc_btnApplyToAll.gridx = 4
            gbc_btnApplyToAll.gridy = 16
            panelVerbatimValues.add(btnApplyToAll, gbc_btnApplyToAll)
            btnApplyToAll.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    saveChanges()
                }
            })
        }
        run {
            val panelRelatedData = JPanel()
            contentPanel.add(panelRelatedData)
            val gbl_panelRelatedData = GridBagLayout()
            gbl_panelRelatedData.maximumLayoutSize(panelRelatedData)
            gbl_panelRelatedData.columnWidths = intArrayOf(20, 33, 1, 0)
            gbl_panelRelatedData.rowHeights = intArrayOf(25, 0)
            gbl_panelRelatedData.columnWeights = doubleArrayOf(0.0, 1.0, 0.0, Double.MIN_VALUE)
            gbl_panelRelatedData.rowWeights = doubleArrayOf(1.0, Double.MIN_VALUE)
            panelRelatedData.setLayout(gbl_panelRelatedData)
            {
                val lblTaxa = JLabel("Taxa")
                val gbc_lblTaxa = GridBagConstraints()
                gbc_lblTaxa.anchor = GridBagConstraints.NORTHEAST
                gbc_lblTaxa.insets = Insets(0, 0, 0, 5)
                gbc_lblTaxa.gridx = 0
                gbc_lblTaxa.gridy = 0
                panelRelatedData.add(lblTaxa, gbc_lblTaxa)
            }
            val scrollPane = JScrollPane()
            // scrollPane.setPreferredSize(new Dimension(1000,100));
            val gbc_scrollPane = GridBagConstraints()
            gbc_scrollPane.anchor = GridBagConstraints.NORTH
            gbc_scrollPane.insets = Insets(0, 0, 0, 5)
            gbc_scrollPane.fill = GridBagConstraints.BOTH
            gbc_scrollPane.gridx = 1
            gbc_scrollPane.gridy = 0
            panelRelatedData.add(scrollPane, gbc_scrollPane)
            tableTaxa = JTable()
            val sls = SpecimenLifeCycle()
            tableTaxa.setModel(CountValueTableModel(sls.findTaxaFromVerbatim(this.verbatimData), "Current Idenfifications for these verbatim values."))
            scrollPane.setViewportView(tableTaxa)
        }
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.RIGHT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            {
                val cancelButton = JButton("Close")
                cancelButton.setActionCommand("Close")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
                buttonPane.add(cancelButton)
            }
        }
    }

    private fun getJScrollPaneCollectors(): JScrollPane? {
        if (jScrollPaneCollectors == null) {
            jScrollPaneCollectors = JScrollPane()
            jScrollPaneCollectors.setViewportView(getJTableCollectors())
            jScrollPaneCollectors.setPreferredSize(Dimension(jScrollPaneCollectors.getWidth(), 150))
        }
        return jScrollPaneCollectors
    }

    private val comboBoxHighGeog: FilteringGeogJComboBox?
        private get() {
            if (comboBoxHigherGeog == null) {
                val mls = MCZbaseGeogAuthRecLifeCycle()
                comboBoxHigherGeog = FilteringGeogJComboBox()
                comboBoxHigherGeog.setHGModel(HigherGeographyComboBoxModel(mls.findAll()))
                comboBoxHigherGeog.setEditable(true)
            }
            return comboBoxHigherGeog
        }

    private fun getJTableCollectors(): JTable? {
        if (jTableCollectors == null) {
            jTableCollectors = JTable(CollectorTableModel())
            // Note: When setting the values, the table column editor needs to be reset there, as the model is replaced.
            val field = FilteringAgentJComboBox()
            jTableCollectors.getColumnModel().getColumn(0).setCellEditor(ComboBoxCellEditor(field))
            jTableCollectors.setRowHeight(jTableCollectors.getRowHeight() + 4)
        }
        return jTableCollectors
    }

    /**
     * This method initializes jComboBoxCollection for entering collections  with a controled vocabulary
     *
     * @return javax.swing.JComboBox for collections.
     */
    private fun getJComboBoxCollection(): JComboBox<*>? {
        if (jComboBoxCollection == null) {
            val sls = SpecimenLifeCycle()
            jComboBoxCollection = JComboBox<Any?>()
            jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.getDistinctCollections()))
            jComboBoxCollection.setEditable(true)
            jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
            AutoCompleteDecorator.decorate(jComboBoxCollection)
        }
        return jComboBoxCollection
    }

    /**
     * This method initializes jScrollPaneNumbers
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneNumbers(): JScrollPane? {
        if (jScrollPaneNumbers == null) {
            jScrollPaneNumbers = JScrollPane()
            jScrollPaneNumbers.setViewportView(getJTableNumbers())
            jScrollPaneNumbers.setPreferredSize(Dimension(jScrollPaneCollectors.getWidth(), 150))
        }
        return jScrollPaneNumbers
    }

    /**
     * This method initializes jTableNumbers
     *
     * @return javax.swing.JTable
     */
    private fun getJTableNumbers(): JTable? {
        if (jTableNumbers == null) {
            jTableNumbers = JTable(NumberTableModel())
            val jComboNumberTypes: JComboBox<String?> = JComboBox<String?>()
            jComboNumberTypes.setModel(DefaultComboBoxModel<String?>(NumberLifeCycle.Companion.getDistinctTypes()))
            jComboNumberTypes.setEditable(true)
            val typeColumn: TableColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.Companion.COLUMN_TYPE)
            val comboBoxEditor = DefaultCellEditor(jComboNumberTypes)
            //TODO: enable autocomplete for numbertypes picklist.
//AutoCompleteDecorator.decorate((JComboBox) comboBoxEditor.getComponent());
            typeColumn.cellEditor = comboBoxEditor
            val renderer = DefaultTableCellRenderer()
            renderer.setToolTipText("Click for pick list of number types.")
            typeColumn.cellRenderer = renderer
        }
        return jTableNumbers
    }

    protected fun saveChanges() {
        if (jTableCollectors.isEditing()) {
            jTableCollectors.getCellEditor().stopCellEditing()
        }
        if (jTableNumbers.isEditing()) {
            jTableNumbers.getCellEditor().stopCellEditing()
        }
        log.debug("Saving verbatim data changes to all records with shared verbatim data.")
        log.debug("Should affect " + verbatimData.getCount() + " Specimen records.")
        val sls = SpecimenLifeCycle()
        val specimens: MutableList<Specimen?> = sls.findSpecimensFromVerbatim(verbatimData)
        val i: MutableIterator<Specimen?> = specimens.iterator()
        while (i.hasNext()) {
            val specimen: Specimen? = i.next()
            // populate fields from parsed (10 fields, 2 lists)
            if (comboBoxHigherGeog.getSelectedIndex() == -1 && comboBoxHigherGeog.getSelectedItem() == null) {
                specimen.setHigherGeography("")
            } else { // combo box contains a geography object, obtain the higher geography string.
                specimen.setHigherGeography((comboBoxHigherGeog.getModel() as HigherGeographyComboBoxModel).getSelectedItemHigherGeography())
            }
            specimen.setSpecificLocality(textFieldSpecificLocality.getText())
            if (jTableCollectors.getModel().getRowCount() > 0) { // add collectors
                val rows: Int = jTableCollectors.getModel().getRowCount()
                for (row in 0 until rows) {
                    val collector = (jTableCollectors.getModel().getValueAt(row, 1) as String)
                    specimen.getCollectors().add(Collector(specimen, collector))
                }
            }
            if (jComboBoxCollection.getSelectedIndex() == -1 && jComboBoxCollection.getSelectedItem() == null) {
                specimen.setCollection("")
            } else {
                specimen.setCollection(jComboBoxCollection.getSelectedItem().toString())
            }
            // Elevations
            var min_elev: Long?
            min_elev = if (textFieldMinElevation.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMinElevation.getText().toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMinimum_elevation(min_elev)
            var max_elev: Long?
            max_elev = if (textFieldMaxElevation.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMaxElevation.getText().toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMaximum_elevation(max_elev)
            if (comboBoxElevUnits.getSelectedIndex() == -1 && comboBoxElevUnits.getSelectedItem() == null) {
                specimen.setElev_units("")
            } else {
                specimen.setElev_units(comboBoxElevUnits.getSelectedItem().toString())
            }
            specimen.setIsoDate(textFieldISODate.getText().trim({ it <= ' ' }))
            specimen.setHabitat(textFieldHabitat.getText())
            specimen.setMicrohabitat(textFieldMicrohabitat.getText())
            specimen.setWorkFlowStatus(comboBoxWorkflowStatus.getSelectedItem() as String)
            // store values for reuse
            storeLastEditedValues(specimen)
            try {
                sls.attachDirty(specimen)
            } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    private fun storeLastEditedValues(lastSpecimen: Specimen) {
        if (lastEditedSpecimen == null) {
            lastEditedSpecimen = Specimen()
            lastEditedSpecimen.setDateCreated(Date())
        }
        lastEditedSpecimen.setSpecificLocality(lastSpecimen.getSpecificLocality())
        lastEditedSpecimen.setHigherGeography(lastSpecimen.getHigherGeography())
        lastEditedSpecimen.setCollection(lastSpecimen.getCollection())
    }

    companion object {
        private const val serialVersionUID = 2718225599980885040L
        private val log: Log = LogFactory.getLog(VerbatimClassifyDialog::class.java)
    }
}
