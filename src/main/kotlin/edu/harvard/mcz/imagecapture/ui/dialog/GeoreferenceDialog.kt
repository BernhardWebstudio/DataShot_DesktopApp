/**
 * GeoreferenceDialog.java
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


import edu.harvard.mcz.imagecapture.entity.LatLong
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.jdesktop.swingx.combobox.ListComboBoxModel
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionListener
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.text.MaskFormatter

java.awt.event.ActionEvent
import java.text.ParseException


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
class GeoreferenceDialog : JDialog {
    private val contentPanel: JPanel? = JPanel()
    private var comboBoxOrigUnits: JComboBox<String?>? = null
    private var comboBoxErrorUnits: JComboBox<String?>? = null
    private var txtGPSAccuracy: JTextField? = null
    private var textFieldDecimalLat: JTextField? = null
    private var textFieldDecimalLong: JTextField? = null
    private var txtLatDegrees: JTextField? = null
    private var txtLatDecMin: JTextField? = null
    private var txtLatMin: JTextField? = null
    private var txtLatSec: JTextField? = null
    private var cbLatDir: JComboBox<String?>? = null
    private var txtLongDegrees: JTextField? = null
    private var txtLongDecMin: JTextField? = null
    private var txtLongMin: JTextField? = null
    private var txtLongSec: JTextField? = null
    private var cbLongDir: JComboBox<String?>? = null
    private var cbDatum: JComboBox<String?>? = null
    private var cbMethod: JComboBox<String?>? = null
    private var georeference: LatLong? = null
    private var okButton: JButton? = null
    private var lblErrorLabel: JLabel? = null
    private var lblNewLabel: JLabel? = null
    private var textFieldRemarks: JTextField? = null
    private var lblErrorRadius: JLabel? = null
    private var txtErrorRadius: JTextField? = null
    private var textFieldDetBy: JTextField? = null
    private var textDetDate: JFormattedTextField? = null
    private var textRefSource: JTextField? = null

    /**
     * Create the dialog.
     */
    constructor() {
        init()
    }

    constructor(georeference: LatLong?) {
        this.georeference = georeference
        init()
        loadData()
        setState()
    }

    private fun loadData() {
        lblErrorLabel.setText("")
        textFieldDecimalLat.setText(georeference.getDecLatString())
        textFieldDecimalLong.setText(georeference.getDecLongString())
        log.debug("load geo data: lat: " + georeference.getDecLatString() +
                ", long: " + georeference.getDecLongString())
        if (georeference.getDatum() != "") {
            cbDatum.setSelectedItem(georeference.getDatum())
        }
        cbMethod.setSelectedItem(georeference.getGeorefmethod())
        txtGPSAccuracy.setText(georeference.getGpsaccuracyString())
        comboBoxOrigUnits.setSelectedItem(georeference.getOrigLatLongUnits())
        txtLatDegrees.setText(georeference.getLatDegString())
        txtLatDecMin.setText(georeference.getDecLatMinString())
        txtLatMin.setText(georeference.getLatMinString())
        txtLatSec.setText(georeference.getLatSecString())
        cbLatDir.setSelectedItem(georeference.getLatDir())
        txtLongDegrees.setText(georeference.getLongDegString())
        txtLongDecMin.setText(georeference.getDecLongMinString())
        txtLongMin.setText(georeference.getLongMinString())
        txtLongSec.setText(georeference.getLongSecString())
        cbLongDir.setSelectedItem(georeference.getLongDir())
        txtErrorRadius.setText(georeference.getMaxErrorDistanceString())
        comboBoxErrorUnits.setSelectedItem(georeference.getMaxErrorUnits())
        textFieldDetBy.setText(georeference.getDeterminedByAgent())
        textDetDate.setValue(SimpleDateFormat("yyyy-MM-dd")
                .format(georeference.getDeterminedDate()))
        textRefSource.setText(georeference.getLatLongRefSource())
        textFieldRemarks.setText(georeference.getLatLongRemarks())
    }

    private fun setState() {
        val acc: String = cbMethod.getSelectedItem().toString()
        if (acc == "GPS") {
            txtGPSAccuracy.setEnabled(true)
        } else {
            txtGPSAccuracy.setEnabled(false)
        }
        val state: String = comboBoxOrigUnits.getSelectedItem().toString()
        when (state) {
            "degrees dec. minutes" -> {
                textFieldDecimalLat.setEnabled(false)
                textFieldDecimalLong.setEnabled(false)
                txtLatDegrees.setEnabled(true)
                txtLatDecMin.setEnabled(true)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(true)
                txtLongDegrees.setEnabled(true)
                txtLongDecMin.setEnabled(true)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(true)
            }
            "deg. min. sec." -> {
                textFieldDecimalLat.setEnabled(false)
                textFieldDecimalLong.setEnabled(false)
                txtLatDegrees.setEnabled(true)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(true)
                txtLatSec.setEnabled(true)
                cbLatDir.setEnabled(true)
                txtLongDegrees.setEnabled(true)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(true)
                txtLongSec.setEnabled(true)
                cbLongDir.setEnabled(true)
            }
            "decimal degrees", "unknown" -> {
                textFieldDecimalLat.setEnabled(true)
                textFieldDecimalLong.setEnabled(true)
                txtLatDegrees.setEnabled(false)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(false)
                txtLongDegrees.setEnabled(false)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(false)
            }
            else -> {
                textFieldDecimalLat.setEnabled(true)
                textFieldDecimalLong.setEnabled(true)
                txtLatDegrees.setEnabled(false)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(false)
                txtLongDegrees.setEnabled(false)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(false)
            }
        }
    }

    private fun saveData(): Boolean {
        var result = true
        okButton.grabFocus()
        if (textFieldDecimalLat.getText().length > 0) {
            try {
                georeference.setDecLat(BigDecimal.valueOf(textFieldDecimalLat.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Latitude number format")
                result = false
            }
        } else {
            georeference.setDecLat(null)
        }
        if (textFieldDecimalLong.getText().length > 0) {
            try {
                georeference.setDecLong(BigDecimal.valueOf(textFieldDecimalLong.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Longitude number format")
                result = false
            }
        } else {
            georeference.setDecLong(null)
        }
        if (cbDatum.getSelectedItem() != null) {
            georeference.setDatum(cbDatum.getSelectedItem().toString())
        }
        if (cbMethod.getSelectedItem() != null) {
            georeference.setGeorefmethod(cbMethod.getSelectedItem().toString())
        }
        if (txtGPSAccuracy.getText().length > 0) {
            try {
                georeference.setGpsaccuracy(
                        BigDecimal.valueOf(txtGPSAccuracy.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: GPS Accuracy number format")
                result = false
            }
        }
        if (comboBoxOrigUnits.getSelectedItem() != null) {
            georeference.setOrigLatLongUnits(
                    comboBoxOrigUnits.getSelectedItem().toString())
        }
        if (txtLatDegrees.getText().length > 0) {
            try {
                georeference.setLatDeg(txtLatDegrees.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Degrees number format")
                result = false
            }
        }
        if (txtLatDecMin.getText().length > 0) {
            try {
                georeference.setDecLatMin(
                        BigDecimal.valueOf(txtLatDecMin.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Dec Min number format")
                result = false
            }
        }
        if (txtLatMin.getText().length > 0) {
            try {
                georeference.setLatMin(txtLatMin.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Min number format")
                result = false
            }
        }
        if (txtLatSec.getText().length > 0) {
            try {
                georeference.setLatSec(
                        BigDecimal.valueOf(txtLatSec.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Degrees number format")
                result = false
            }
        }
        if (cbLatDir.getSelectedItem() != null) {
            georeference.setLatDir(cbLatDir.getSelectedItem().toString())
        }
        if (txtLongDegrees.getText().length > 0) {
            try {
                georeference.setLongDeg(txtLongDegrees.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Degrees number format")
                result = false
            }
        }
        if (txtLongDecMin.getText().length > 0) {
            try {
                georeference.setDecLongMin(
                        BigDecimal.valueOf(txtLongDecMin.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Dec Min number format")
                result = false
            }
        }
        if (txtLongMin.getText().length > 0) {
            try {
                georeference.setLongMin(txtLongMin.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Min number format")
                result = false
            }
        }
        if (txtLongSec.getText().length > 0) {
            try {
                georeference.setLongSec(
                        BigDecimal.valueOf(txtLongSec.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Degrees number format")
                result = false
            }
        }
        if (cbLongDir.getSelectedItem() != null) {
            georeference.setLongDir(cbLongDir.getSelectedItem().toString())
        }
        if (txtErrorRadius.getText().length > 0) {
            try {
                georeference.setMaxErrorDistance(txtErrorRadius.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Error radius number format")
                result = false
            }
        }
        if (comboBoxErrorUnits.getSelectedItem() != null) {
            georeference.setMaxErrorUnits(
                    comboBoxErrorUnits.getSelectedItem().toString())
        }
        georeference.setDeterminedByAgent(textFieldDetBy.getText())
        try {
            georeference.setDeterminedDate(
                    SimpleDateFormat("yyyy-MM-dd").parse(textDetDate.getText()))
        } catch (e: ParseException) {
            lblErrorLabel.setText("Error: Error date determined format")
            result = false
        }
        georeference.setLatLongRefSource(textRefSource.getText())
        georeference.setLatLongRemarks(textFieldRemarks.getText())
        return result
    }

    private fun init() {
        setBounds(100, 100, 450, 560)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(GridLayout(0, 2, 0, 0))
        run {
            val lblLatitude = JLabel("Latitude")
            lblLatitude.setHorizontalAlignment(SwingConstants.RIGHT)
            contentPanel.add(lblLatitude)
        }
        textFieldDecimalLat = JTextField()
        contentPanel.add(textFieldDecimalLat)
        textFieldDecimalLat.setColumns(10)
        val lblLongitude = JLabel("Longitude")
        lblLongitude.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongitude)
        textFieldDecimalLong = JTextField()
        contentPanel.add(textFieldDecimalLong)
        textFieldDecimalLong.setColumns(10)
        val lblMethod = JLabel("Method")
        lblMethod.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblMethod)
        val methodModel: ComboBoxModel<String?> = ListComboBoxModel<String?>(LatLong.Companion.getGeorefMethodValues())
        cbMethod = JComboBox<String?>(DefaultComboBoxModel<String?>(arrayOf<String?>(
                "not recorded", "unknown", "GEOLocate", "Geoportal", "Google Earth",
                "Google Maps", "Gazeteer", "GPS", "Label Data", "Wikipedia",
                "MaNIS/HertNet/ORNIS Georeferencing Guidelines")))
        cbMethod.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                setState()
            }
        })
        contentPanel.add(cbMethod)
        val lblDatum = JLabel("Datum")
        lblDatum.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDatum)
        val datumModel: ComboBoxModel<String?> = ListComboBoxModel<String?>(LatLong.Companion.getDatumValues())
        cbDatum = JComboBox<String?>(datumModel)
        // set default
        cbDatum.setSelectedItem("WGS84")
        contentPanel.add(cbDatum)
        val lblAccuracy = JLabel("GPS Accuracy")
        lblAccuracy.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblAccuracy)
        txtGPSAccuracy = JTextField()
        txtGPSAccuracy.setColumns(10)
        contentPanel.add(txtGPSAccuracy)
        val lblNewLabel_1 = JLabel("Original Units")
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblNewLabel_1)
        comboBoxOrigUnits = JComboBox<String?>()
        comboBoxOrigUnits.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                setState()
            }
        })
        comboBoxOrigUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("decimal degrees", "deg. min. sec.",
                "degrees dec. minutes", "unknown")))
        contentPanel.add(comboBoxOrigUnits)
        lblErrorRadius = JLabel("Error Radius")
        lblErrorRadius.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblErrorRadius)
        txtErrorRadius = JTextField()
        txtErrorRadius.setColumns(10)
        contentPanel.add(txtErrorRadius)
        val lblErrorRadiusUnits = JLabel("Error Radius Units")
        lblErrorRadiusUnits.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblErrorRadiusUnits)
        comboBoxErrorUnits = JComboBox<String?>()
        comboBoxErrorUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("m", "ft", "km", "mi", "yd")))
        contentPanel.add(comboBoxErrorUnits)
        val lblLatDegrees = JLabel("Lat Degrees")
        lblLatDegrees.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDegrees)
        txtLatDegrees = JTextField()
        txtLatDegrees.setColumns(4)
        contentPanel.add(txtLatDegrees)
        val lblLatDecMin = JLabel("Lat Dec Min")
        lblLatDecMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDecMin)
        txtLatDecMin = JTextField()
        txtLatDecMin.setColumns(6)
        contentPanel.add(txtLatDecMin)
        val lblLatMin = JLabel("Lat Min")
        lblLatMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatMin)
        txtLatMin = JTextField()
        txtLatMin.setColumns(6)
        contentPanel.add(txtLatMin)
        val lblLatSec = JLabel("Lat Sec")
        lblLatSec.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatSec)
        txtLatSec = JTextField()
        txtLatSec.setColumns(6)
        contentPanel.add(txtLatSec)
        val lblLatDir = JLabel("Lat N/S")
        lblLatDir.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDir)
        cbLatDir = JComboBox<String?>()
        cbLatDir.setModel(
                DefaultComboBoxModel<String?>(arrayOf<String?>("N", "S")))
        contentPanel.add(cbLatDir)
        val lblLongDegrees = JLabel("Long Degrees")
        lblLongDegrees.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDegrees)
        txtLongDegrees = JTextField()
        txtLongDegrees.setColumns(4)
        contentPanel.add(txtLongDegrees)
        val lblLongDecMin = JLabel("Long Dec Min")
        lblLongDecMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDecMin)
        txtLongDecMin = JTextField()
        txtLongDecMin.setColumns(6)
        contentPanel.add(txtLongDecMin)
        val lblLongMin = JLabel("Long Min")
        lblLongMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongMin)
        txtLongMin = JTextField()
        txtLongMin.setColumns(6)
        contentPanel.add(txtLongMin)
        val lblLongSec = JLabel("Long Sec")
        lblLongSec.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongSec)
        txtLongSec = JTextField()
        txtLongSec.setColumns(6)
        contentPanel.add(txtLongSec)
        val lblLongDir = JLabel("Long E/W")
        lblLongDir.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDir)
        cbLongDir = JComboBox<String?>()
        cbLongDir.setModel(
                DefaultComboBoxModel<String?>(arrayOf<String?>("E", "W")))
        contentPanel.add(cbLongDir)
        val lblDetBy = JLabel("Determined By")
        lblDetBy.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDetBy)
        textFieldDetBy = JTextField()
        contentPanel.add(textFieldDetBy)
        textFieldDetBy.setColumns(10)
        val lblDetDate = JLabel("Date Determined")
        lblDetDate.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDetDate)
        try {
            textDetDate = JFormattedTextField(MaskFormatter("####-##-##"))
        } catch (e1: ParseException) {
            textDetDate = JFormattedTextField()
        }
        textDetDate.setToolTipText(
                "Date on which georeference was made yyyy-mm-dd")
        contentPanel.add(textDetDate)
        val lblRef = JLabel("Reference Source")
        lblRef.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblRef)
        textRefSource = JTextField()
        contentPanel.add(textRefSource)
        textRefSource.setColumns(10)
        lblNewLabel = JLabel("Remarks")
        lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblNewLabel)
        textFieldRemarks = JTextField()
        contentPanel.add(textFieldRemarks)
        textFieldRemarks.setColumns(10)
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.RIGHT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            {
                lblErrorLabel = JLabel("Message")
                buttonPane.add(lblErrorLabel)
            }
            {
                okButton = JButton("OK")
                okButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        lblErrorLabel.setText("")
                        if (saveData()) {
                            setVisible(false)
                        }
                    }
                })
                okButton.setActionCommand("OK")
                buttonPane.add(okButton)
                getRootPane().setDefaultButton(okButton)
            }
            {
                val cancelButton = JButton("Cancel")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        loadData()
                        setVisible(false)
                    }
                })
                cancelButton.setActionCommand("Cancel")
                buttonPane.add(cancelButton)
            }
        }
    }

    companion object {
        private const val serialVersionUID = -257199970146455008L
        private val log: Log = LogFactory.getLog(GeoreferenceDialog::class.java)
    }
}
