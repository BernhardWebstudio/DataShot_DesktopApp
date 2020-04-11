/**
 * ImageDisplayFrame.java
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
package edu.harvard.mcz.imagecapture.ui.frame


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.SpecimenController
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.exceptions.BadTemplateException
import edu.harvard.mcz.imagecapture.exceptions.ImageLoadException
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialog
import edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialog
import edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrame
import net.miginfocom.swing.MigLayout
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.ComponentEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneDivider
import javax.swing.plaf.basic.BasicSplitPaneUI

java.awt.*import  java.awt.event.ActionEvent

java.awt.event.ComponentAdapter
import  java.io.File


import java.lang.Exception 
import java.net.URL javax.swing.border.Border


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
 * Display parts of images (and possibly a data entry form) of a specimen and its labels.
 */
class ImageDisplayFrame() : JFrame() {
    // Preferences to save the window location etc.
    var prefs: Preferences? = null
    // The displayed image read from a file.
// Set of image files managed by loading one at a time and
// displaying its components in a set of tabs.
    var imagefile: BufferedImage? = null //  @jve:decl-index=0:
    private var jContentPane: JPanel? = null
    private var jTabbedPane: JTabbedPane? = null // Tab pane to display image components
    // The specimen
    private var jPanelSpecimen: JPanel? = null
    private var jScrollPane: JScrollPane? = null
    private var imagePanelSpecimen: ImageZoomPanel? = null
    // the barcode
    private var imagePanelBarcode: JLabel? = null // The barcode label.
    private var jPanel: JPanel? = null
    private var jTextBarcode: JTextField? = null // Text read from the barcode.
    // pin labels
    private var jPanelLabels: JPanel? = null
    private var imagePanelPinLabels: ImageZoomPanel? = null // the specimen labels from the pin.
    // unit tray taxon name header label
    private var jPanelUnitTray: JPanel? = null //  @jve:decl-index=0:visual-constraint="574,225"
    private var imagePanelUnitTrayTaxon: ImageZoomPanel? = null // The current determination new unit tray label for OCR.
    private var jTextFieldRawOCR: JTextField? = null
    // old unit tray labels
    private var jPanelUnitTrayLabels: JPanel? = null //  @jve:decl-index=0:visual-constraint="574,225"
    private var imagePanelTrayLabels: ImageZoomPanel? = null // small labels present in the unit tray.
    // full image
    private var imagePaneFullImage: ImageZoomPanel? = null
    private var jPanelImagePicker: JPanel? = null
    private var jLabelImageCountTxt: JLabel? = null
    private var jComboBoxImagePicker: JComboBox<*>? = null
    private var jLabelImageCountNr: JLabel? = null
    private var jPanelImagesPanel: JPanel? = null
    private var targetSpecimen: Specimen? = null
    private var targetSpecimenController: SpecimenController? = null
    private var selectedImage: ICImage? = null
    private var templatePicker: JButton? = null
    private var btnVerbatimtranscription: JButton? = null

    /**
     * This is the default constructor
     *
     * @param specimen
     */
    constructor(specimen: Specimen?, specimenController: SpecimenController?) : this() {
        targetSpecimen = specimen
        targetSpecimenController = specimenController
        //this.center();
    }

    fun setTargetSpecimen(targetSpecimen: Specimen?) { // TODO: remove setter necessity, fetch via SpecimenController
        this.targetSpecimen = targetSpecimen
    }

    /**
     * Given a set of ICImages, display one of them in the tabs of the ImageDisplayFrame, and
     * populate the image chooser pick list with a list of all the images.  Call this method to display
     * more than one image in an ImageDisplayFrame.  Single image is displayed with a call to loadImagesFromFileSingle().
     *
     * @param imageFiles the image files to display in the tabs of the frame.
     * @throws ImageLoadException   if there is a problem with the image.
     * @throws BadTemplateException
     */
    fun loadImagesFromFiles(imageFiles: MutableSet<ICImage?>) {
        log.debug(imageFiles.size)
        jComboBoxImagePicker.removeAllItems()
        val i: MutableIterator<ICImage?> = imageFiles.iterator()
        var image: ICImage? = null
        val fileCount = imageFiles.size
        while (i.hasNext()) {
            image = i.next()
            jComboBoxImagePicker.addItem(image.getFilename())
            log.debug("Adding image to picklist: " + image.getPath() + image.getFilename())
        }
        //TODO: stored path may need separator conversion for different systems.
//String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
        var path: String = image.getPath()
        if (path == null) {
            path = ""
        }
        //File fileToCheck = new File(startPointName + path + image.getFilename());
        val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
        jLabelImageCountNr.setText("($fileCount)")
        jLabelImageCountNr.setForeground(if (fileCount > 1) Color.RED else Color.BLACK)
        jComboBoxImagePicker.setEnabled(fileCount > 1)
        jComboBoxImagePicker.setSelectedItem(image.getFilename())
        try {
            val defaultTemplate: PositionTemplate = PositionTemplate.Companion.findTemplateForImage(image)
            loadImagesFromFileSingle(fileToCheck, defaultTemplate, image)
        } catch (e: BadTemplateException) {
            log.error(e)
        } catch (e: ImageLoadException) {
            log.error(e)
        }
        jTabbedPane.setSelectedIndex(0) // move focus to full image tab
    }

    /**
     * Based on the position template, display the full image in one tab, and parts of the image
     * described by the template in other tabs.
     *
     * @param anImageFile
     * @param defaultTemplate
     * @throws ImageLoadException
     * @throws BadTemplateException
     */
    @Throws(ImageLoadException::class, BadTemplateException::class)
    fun loadImagesFromFileSingle(anImageFile: File, defaultTemplate: PositionTemplate, image: ICImage?) {
        log.debug(anImageFile.getName())
        var templateProblem = false
        selectedImage = image
        //TODO: template detection?
        try {
            imagefile = ImageIO.read(anImageFile)
            log.debug(anImageFile.getPath())
            // Show the component parts of the image as defined by the position template.
            if (defaultTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // clear component parts
                val url: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/gnome-mime-image.png")
                val anImage: BufferedImage = ImageIO.read(url)
                setBarcodeImage(anImage)
                setSpecimenImage(anImage)
                setPinLabelImage(anImage)
                setUnitTrayImage(anImage)
                setUnitTrayLabelsImage(anImage)
            } else {
                if (imagefile.getHeight() != defaultTemplate.getImageSize().height || imagefile.getWidth() != defaultTemplate.getImageSize().width) { // TODO: template strategy
                    throw BadTemplateException("Template size doesn't match image size. " + defaultTemplate.getName())
                }
                try {
                    val x: Int = defaultTemplate.getBarcodeULPosition().width
                    val y: Int = defaultTemplate.getBarcodeULPosition().height
                    val w: Int = defaultTemplate.getBarcodeSize().width
                    val h: Int = defaultTemplate.getBarcodeSize().height
                    setBarcodeImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    try {
                        setBarcodeImage(imagefile)
                    } catch (ex2: Exception) {
                        println(ex.message)
                        throw ImageLoadException("Unable to load images from " + anImageFile.getPath() + " " + ex2.message)
                    }
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getSpecimenPosition().width
                    val y: Int = defaultTemplate.getSpecimenPosition().height
                    val w: Int = defaultTemplate.getSpecimenSize().width
                    val h: Int = defaultTemplate.getSpecimenSize().height
                    val img: BufferedImage = imagefile.getSubimage(x, y, w, h)
                    setSpecimenImage(img)
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getLabelPosition().width
                    val y: Int = defaultTemplate.getLabelPosition().height
                    val w: Int = defaultTemplate.getLabelSize().width
                    val h: Int = defaultTemplate.getLabelSize().height
                    setPinLabelImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getTextPosition().width
                    val y: Int = defaultTemplate.getTextPosition().height
                    val w: Int = defaultTemplate.getTextSize().width
                    val h: Int = defaultTemplate.getTextSize().height
                    setUnitTrayImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getUTLabelsPosition().width
                    val y: Int = defaultTemplate.getUTLabelsPosition().height
                    val w: Int = defaultTemplate.getUTLabelsSize().width
                    val h: Int = defaultTemplate.getUTLabelsSize().height
                    setUnitTrayLabelsImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
            }
            // Display the full image (also packs!)
            setFullImage()
        } catch (e1: IOException) {
            log.debug("IOException!")
            println("Error reading image file: " + e1.message)
            throw ImageLoadException("Unable to read image file " + anImageFile.getPath() + " " + e1.message)
        } catch (e: Exception) {
            log.debug("Image loading exception")
            e.printStackTrace()
        }
        if (templateProblem) {
            throw BadTemplateException("Template doesn't fit file " + anImageFile.getPath())
        }
        log.debug(anImageFile.getPath())
        if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.getUser().getUserid())) {
            updateTemplateList()
        }
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setContentPane(getJContentPane())
        this.setTitle("Image File and Barcode Value")
        setWindowLocationSize()
        val self = this
        this.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                self.saveWindowSize()
            }

            override fun componentMoved(e: ComponentEvent?) {
                super.componentMoved(e)
                self.saveWindowLocation()
            }
        })
    }

    private fun setWindowLocationSize() {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        // set size of window
        val sizeDimensionWidth: Int = preferences.getInt("SizeDimensionWidth", Math.min(1600, screenSize.width))
        val sizeDimensionHeight: Int = prefs.getInt("SizeDimensionHeight", Math.min(1250, screenSize.height))
        log.debug("Setting width = $sizeDimensionWidth, height = $sizeDimensionHeight")
        this.setPreferredSize(Dimension(sizeDimensionWidth, sizeDimensionHeight))
        // set location of window
        val locationX: Int = prefs.getInt("LocationX", (screenSize.width - this.getWidth()) / 2)
        val locationY: Int = prefs.getInt("LocationY", (screenSize.height - this.getHeight()) / 2)
        this.setLocation(locationX, locationY)
    }

    private fun saveWindowLocation() {
        prefs.putInt("LocationX", this.getLocation().x)
        prefs.putInt("LocationY", this.getLocation().y)
    }

    private fun saveWindowSize() {
        prefs.putInt("SizeDimensionWidth", this.getWidth())
        prefs.putInt("SizeDimensionHeight", this.getHeight())
        log.debug("Stored width = " + this.getWidth() + ", height = " + this.getHeight())
        try {
            prefs.sync()
        } catch (e: BackingStoreException) {
            log.error(e)
        } catch (e: IllegalStateException) {
            log.error(e)
        }
    }

    private val preferences: Preferences?
        private get() {
            if (prefs == null) {
                prefs = Preferences.userRoot().node(this.javaClass.name)
            }
            return prefs
        }

    fun center() {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel? {
        if (jContentPane == null) {
            jContentPane = JPanel()
            jContentPane.setLayout(MigLayout("wrap 2, fill", "[grow]", "[grow]"))
            jContentPane.add(getJPanelImagesPanel(), "grow")
        }
        return jContentPane
    }

    fun setActiveTab(tab: Int) {
        try {
            jTabbedPane.setSelectedIndex(tab)
        } catch (e: IndexOutOfBoundsException) {
            println("Failed to activate tab. " + e.message)
        }
    }

    fun setWest(westPanel: JPanel?) {
        jContentPane.removeAll()
        jContentPane.setLayout(MigLayout("wrap 2, fill, insets 0", "[grow]", "[grow]"))
        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        splitPane.setLeftComponent(westPanel)
        splitPane.setRightComponent(getJPanelImagesPanel())
        jContentPane.add(splitPane, "push, grow")
        setWindowLocationSize()
        this.pack()
        // remove borders
        splitPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
        val flatDividerSplitPaneUI: BasicSplitPaneUI = object : BasicSplitPaneUI() {
            override fun createDefaultDivider(): BasicSplitPaneDivider {
                return object : BasicSplitPaneDivider(this) {
                    override fun setBorder(b: Border?) {}
                }
            }
        }
        splitPane.setUI(flatDividerSplitPaneUI)
        splitPane.setBorder(null)
        // set the sizes of the two panes
        splitPane.setDividerLocation(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_EDITOR_IMPORTANCE, "0.6").toDouble())
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private val jPanelBarcode: JPanel?
        private get() {
            if (jPanel == null) {
                jPanel = JPanel()
                jPanel.setLayout(BorderLayout())
                jTextBarcode = JTextField()
                jTextBarcode.setText("Barcode")
                jPanel.add(getJScrollPane(), BorderLayout.CENTER)
                jPanel.add(jTextBarcode, BorderLayout.SOUTH)
            }
            return jPanel
        }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelSpecimen(): JPanel? {
        if (jPanelSpecimen == null) {
            jPanelSpecimen = JPanel()
            jPanelSpecimen.setLayout(BorderLayout())
            jPanelSpecimen.add(jLabelSpecimen, BorderLayout.CENTER)
        }
        return jPanelSpecimen
    }

    fun setSpecimenImage(anImage: BufferedImage?) {
        imagePanelSpecimen!!.setImage(anImage)
        imagePanelSpecimen!!.zoomToFit()
        if (imagePanelSpecimen.getPreferredSize().height > 500 || imagePanelSpecimen.getPreferredSize().width > 500) {
            imagePanelSpecimen.setPreferredSize(Dimension(1000, 900))
        }
    }

    /**
     * Fit the image in the PinLabels tab to the size of the imagePanel it is shown in.
     */
    fun fitPinLabels() {
        imagePanelPinLabels!!.zoomToFit()
    }

    /**
     * Center the specimen image in the imagePanel it is shown in.
     */
    fun centerSpecimen() {
        imagePanelSpecimen!!.center()
    }

    fun setUnitTrayImage(anImage: Image?) {
        imagePanelUnitTrayTaxon!!.setImage(anImage as BufferedImage?)
        imagePanelUnitTrayTaxon!!.zoomToFit()
        //		jLabelUnitTray.setIcon(new ImageIcon(anImage));
//		this.pack();
        if (imagePanelUnitTrayTaxon.getPreferredSize().height > 500 || imagePanelUnitTrayTaxon.getPreferredSize().width > 500) {
            imagePanelUnitTrayTaxon.setPreferredSize(Dimension(500, 500))
        }
        //		jLabelUnitTray.setMaximumSize(new Dimension(500,500));
    }

    fun setUnitTrayLabelsImage(anImage: Image?) {
        imagePanelTrayLabels!!.setImage(anImage as BufferedImage?)
        imagePanelTrayLabels!!.zoomToFit()
        if (imagePanelTrayLabels.getPreferredSize().height > 500 || imagePanelTrayLabels.getPreferredSize().width > 500) {
            imagePanelTrayLabels.setPreferredSize(Dimension(500, 500))
        }
    }

    fun setPinLabelImage(anImage: Image?) {
        imagePanelPinLabels!!.setImage(anImage as BufferedImage?)
        imagePanelPinLabels!!.zoomToFit()
        this.pack()
        if (imagePanelPinLabels.getPreferredSize().height > 500 || imagePanelPinLabels.getPreferredSize().width > 500) {
            imagePanelPinLabels.setPreferredSize(Dimension(500, 500))
        }
        imagePanelPinLabels.setMaximumSize(Dimension(500, 500))
    }

    fun setBarcodeImage(anImage: Image) {
        imagePanelBarcode.setIcon(ImageIcon(anImage))
        this.pack()
        if (imagePanelBarcode.getPreferredSize().height > 500 || imagePanelBarcode.getPreferredSize().width > 500) {
            imagePanelBarcode.setPreferredSize(Dimension(500, 500))
        }
        imagePanelBarcode.setMaximumSize(Dimension(500, 500))
    }

    fun setBarcode(someText: String?) {
        jTextBarcode.setText(someText)
        jTextBarcode.setEditable(false)
        jTextBarcode.setEnabled(true)
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            imagePanelBarcode = JLabel()
            imagePanelBarcode.setText("")
            jScrollPane = JScrollPane()
            jScrollPane.setViewportView(imagePanelBarcode)
        }
        return jScrollPane
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private val jLabelSpecimen: 
import  edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel?
        private get() {
            if (imagePanelSpecimen == null) {
                imagePanelSpecimen = ImageZoomPanel()
            }
            return imagePanelSpecimen
        }

    private fun getJPanelLabels(): JPanel? {
        if (jPanelLabels == null) {
            jPanelLabels = JPanel()
            jPanelLabels.setLayout(BorderLayout())
            jPanelLabels.add(imagePanePinLabels, BorderLayout.CENTER)
        }
        return jPanelLabels
    }

    private val imagePanePinLabels: 
import  edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel?
        private get() {
            if (imagePanelPinLabels == null) {
                imagePanelPinLabels = ImageZoomPanel()
            }
            return imagePanelPinLabels
        }

    /**
     * This method initializes jTabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private fun getJTabbedPane(): JTabbedPane? {
        if (jTabbedPane == null) {
            jTabbedPane = JTabbedPane()
            jTabbedPane.insertTab("FullImage", null, panelFullImage, null, TAB_FULLIMAGE)
            jTabbedPane.insertTab("Specimen", null, getJPanelSpecimen(), null, TAB_SPECIMEN)
            jTabbedPane.insertTab("PinLabels", null, getJPanelLabels(), null, TAB_LABELS)
            jTabbedPane.insertTab("UnitTray Labels", null, jPanelUTL, null, TAB_UNITTRAYLABELS)
            jTabbedPane.insertTab("Taxon Label", null, jPanelUnitTrayTaxon, null, TAB_UNITTRAY)
            jTabbedPane.insertTab("Barcode", null, jPanelBarcode, null, TAB_BARCODE)
        }
        return jTabbedPane
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private val jPanelUnitTrayTaxon: JPanel?
        private get() {
            if (jPanelUnitTray == null) {
                jPanelUnitTray = JPanel()
                jPanelUnitTray.setLayout(BorderLayout())
                jPanelUnitTray.setSize(Dimension(108, 72))
                jPanelUnitTray.add(jTextField, BorderLayout.SOUTH)
                jPanelUnitTray.add(getImagePanelUnitTrayTaxon(), BorderLayout.CENTER)
            }
            return jPanelUnitTray
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getImagePanelUnitTrayTaxon(): ImageZoomPanel? {
        if (imagePanelUnitTrayTaxon == null) {
            imagePanelUnitTrayTaxon = ImageZoomPanel()
        }
        return imagePanelUnitTrayTaxon
    }

    private fun setFullImage() {
        if (imagefile != null) {
            panelFullImage!!.setImage(imagefile)
            // We need to make sure the container hierarchy holding the image knows what
// size the image pane is before zoom to fit will work.
            this.pack()
            imagePaneFullImage!!.zoomToFit()
        }
    }

    private val panelFullImage: 
import  edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanel?
        private get() {
            if (imagePaneFullImage == null) {
                if (imagefile == null) {
                    imagePaneFullImage = ImageZoomPanel()
                } else {
                    imagePaneFullImage = ImageZoomPanel(imagefile)
                }
            }
            return imagePaneFullImage
        }

    /**
     * This method initializes jPanelUTL
     *
     * @return javax.swing.JPanel
     */
    private val jPanelUTL: JPanel?
        private get() {
            if (jPanelUnitTrayLabels == null) {
                jPanelUnitTrayLabels = JPanel()
                jPanelUnitTrayLabels.setLayout(BorderLayout())
                jPanelUnitTrayLabels.setSize(Dimension(108, 72))
                jPanelUnitTrayLabels.add(getImagePanelTrayLabels(), BorderLayout.CENTER)
            }
            return jPanelUnitTrayLabels
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getImagePanelTrayLabels(): ImageZoomPanel? {
        if (imagePanelTrayLabels == null) {
            imagePanelTrayLabels = ImageZoomPanel()
        }
        return imagePanelTrayLabels
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField: JTextField?
        private get() {
            if (jTextFieldRawOCR == null) {
                jTextFieldRawOCR = JTextField()
                jTextFieldRawOCR.setEditable(false)
            }
            return jTextFieldRawOCR
        }

    fun setRawOCRLabel(text: String?) {
        jTextFieldRawOCR.setText(text)
    }

    /**
     * This method initializes jPanelImagePicker
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelImagePicker(): JPanel? {
        if (jPanelImagePicker == null) {
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.insets = Insets(0, 0, 5, 5)
            gridBagConstraints1.gridx = 2
            gridBagConstraints1.gridy = 0
            jLabelImageCountNr = JLabel()
            jLabelImageCountNr.setText("(0)")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.insets = Insets(0, 0, 5, 0)
            gridBagConstraints.fill = GridBagConstraints.BOTH
            gridBagConstraints.gridy = 0
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.gridx = 3
            jLabelImageCountTxt = JLabel()
            jLabelImageCountTxt.setText("Images: ")
            jPanelImagePicker = JPanel()
            val gbl_jPanelImagePicker = GridBagLayout()
            gbl_jPanelImagePicker.columnWeights = doubleArrayOf(0.0, 0.0, 0.0, 1.0)
            jPanelImagePicker.setLayout(gbl_jPanelImagePicker)
            val gbc_btnVerbatimtranscription = GridBagConstraints()
            gbc_btnVerbatimtranscription.insets = Insets(0, 0, 5, 5)
            gbc_btnVerbatimtranscription.gridx = 0
            gbc_btnVerbatimtranscription.gridy = 0
            jPanelImagePicker.add(getBtnVerbatimtranscription(), gbc_btnVerbatimtranscription)
            val gbc_jLabel = GridBagConstraints()
            gbc_jLabel.insets = Insets(0, 0, 5, 5)
            gbc_jLabel.gridx = 1
            gbc_jLabel.gridy = 0
            jPanelImagePicker.add(jLabelImageCountTxt, gbc_jLabel)
            jPanelImagePicker.add(getJComboBoxImagePicker(), gridBagConstraints)
            jPanelImagePicker.add(jLabelImageCountNr, gridBagConstraints1)
        }
        return jPanelImagePicker
    }

    /**
     * This method initializes jComboBoxImagePicker
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxImagePicker(): JComboBox<*>? {
        if (jComboBoxImagePicker == null) {
            jComboBoxImagePicker = JComboBox<Any?>()
            if (targetSpecimen != null) {
                val i: MutableIterator<ICImage?> = targetSpecimen.getICImages().iterator()
                while (i.hasNext()) {
                    val filename: String = i.next().getFilename()
                    jComboBoxImagePicker.addItem(filename)
                    log.debug(filename)
                }
            }
            jComboBoxImagePicker.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) { // Intended to be fired when picklist item is selected, is
// being fired on other events as well.
                    log.debug(e.getActionCommand())
                    // If there is no selection, then we shouldn't be doing anything.
                    if (jComboBoxImagePicker.getSelectedItem() == null) {
                        log.debug("No selected item")
                    } else {
                        try {
                            val filename: String? = if (jComboBoxImagePicker.getSelectedItem() != null) jComboBoxImagePicker.getSelectedItem().toString() else null
                            if (filename != null && targetSpecimen != null) { // find matching images, set first one as the display image.
                                val images: MutableSet<ICImage?>?
                                if (targetSpecimen.getICImages() == null) {
                                    val ils = ICImageLifeCycle()
                                    images = HashSet<ICImage?>(ils.findBy(object : HashMap<String?, Any?>() {
                                        init {
                                            put("Filename", filename)
                                            put("SPECIMENID", targetSpecimen.getSpecimenId())
                                        }
                                    }))
                                } else {
                                    images = targetSpecimen.getICImages()
                                }
                                if (images != null && images.size > 0) {
                                    log.debug("Found images: " + images.size)
                                    val ii: MutableIterator<ICImage?> = images.iterator()
                                    var found = false
                                    while (ii.hasNext() && !found) {
                                        val image: ICImage? = ii.next()
                                        log.debug("image is $image")
                                        log.debug("image specimen is " + image.getSpecimen())
                                        log.debug("image path is " + image.getPath())
                                        log.debug("target specimen bar code is " + targetSpecimen.getBarcode())
                                        log.debug("image specimen barcode is " + image.getSpecimen().getBarcode())
                                        if (image.getFilename() != filename || image.getPath() == "" || image.getPath().toUpperCase().contains(".JPG") || image.getSpecimen() == null || image.getSpecimen().getBarcode() != targetSpecimen.getBarcode()) { // wrong path or filename
                                            log.debug("WrongFile: " + image.getPath())
                                        } else {
                                            found = true
                                            //String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
                                            var path: String = image.getPath()
                                            if (path == null) {
                                                path = ""
                                            }
                                            val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
                                            var defaultTemplate: PositionTemplate
                                            try {
                                                defaultTemplate = PositionTemplate.Companion.findTemplateForImage(image)
                                                loadImagesFromFileSingle(fileToCheck, defaultTemplate, image)
                                            } catch (ex: ImageLoadException) {
                                                log.error(ex)
                                            } catch (ex: BadTemplateException) {
                                                log.error(ex)
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (e2: NullPointerException) { // Probably means an empty jComboBoxImagePicker
                            e2.printStackTrace()
                            log.error(e2.message, e2)
                        }
                    }
                }
            })
        }
        return jComboBoxImagePicker
    }

    private fun getTemplatePicker(): JButton? {
        if (templatePicker == null) {
            templatePicker = JButton()
            var template = "No Template Selected"
            if (selectedImage != null) {
                template = selectedImage.getTemplateId()
            }
            templatePicker.setText(template)
            log.debug(template)
            log.debug(PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS)
            if (template == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
                templatePicker.setEnabled(true)
            } else {
                templatePicker.setEnabled(false)
            }
            templatePicker.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val dialog = TemplatePickerDialog(selectedImage)
                    dialog.setVisible(true)
                }
            })
        }
        return templatePicker
    }

    private fun updateTemplateList() {
        getTemplatePicker()
        var template = "No Template Selected"
        if (selectedImage != null) {
            template = selectedImage.getTemplateId()
        }
        templatePicker.setText(template)
        if (template == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
            templatePicker.setEnabled(true)
        } else {
            templatePicker.setEnabled(false)
        }
    }

    /**
     * This method initializes jPanelImagesPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelImagesPanel(): JPanel? {
        if (jPanelImagesPanel == null) {
            jPanelImagesPanel = JPanel()
            jPanelImagesPanel.setLayout(BorderLayout())
            jPanelImagesPanel.add(getJTabbedPane(), BorderLayout.CENTER)
            jPanelImagesPanel.add(getJPanelImagePicker(), BorderLayout.NORTH)
            if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.getUser().getUserid())) {
                jPanelImagesPanel.add(getTemplatePicker(), BorderLayout.SOUTH)
            }
        }
        return jPanelImagesPanel
    }

    private fun getBtnVerbatimtranscription(): JButton? {
        if (btnVerbatimtranscription == null) {
            btnVerbatimtranscription = JButton("VerbatimTranscription")
            btnVerbatimtranscription.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // TODO Auto-generated method stub
                    val dialog = VerbatimCaptureDialog(targetSpecimen, targetSpecimenController)
                    dialog.setVisible(true)
                    setVisible(false)
                }
            })
            btnVerbatimtranscription.setEnabled(false)
            if (targetSpecimen != null && WorkFlowStatus.allowsVerbatimUpdate(targetSpecimen.getWorkFlowStatus())) {
                btnVerbatimtranscription.setEnabled(true)
            }
        }
        return btnVerbatimtranscription
    }

    companion object {
        // display order for tabs
        const val TAB_BARCODE = 5
        const val TAB_SPECIMEN = 1
        const val TAB_LABELS = 2
        const val TAB_UNITTRAY = 4
        const val TAB_UNITTRAYLABELS = 3
        const val TAB_FULLIMAGE = 0
        private const val serialVersionUID = 6208387412508034014L
        private val log: Log = LogFactory.getLog(ImageDisplayFrame::class.java)
    }

    init {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        targetSpecimen = null
        initialize()
        prefs = Preferences.userRoot().node(this.javaClass.name)
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
