/**
 * PositionTemplateBoxDialog.java
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


import edu.harvard.mcz.imagecapture.exceptions.BadTemplateException
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

java.awt.event.KeyEvent


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
 * PositionTemplateBoxDialog
 */
class PositionTemplateBoxDialog : JDialog {
    private var jContentPane: JPanel? = null
    // maximum bounds in units of pixels
    private var maxX = 0 // image width
    private var maxY = 0 // image height
    private var ul: Dimension? = null // upper left corner of box  //  @jve:decl-index=0:
    private var size: Dimension? = null // height,width dimensions of box  //  @jve:decl-index=0:
    private var jPanel: JPanel? = null
    private var jLabel: JLabel? = null
    private var jTextFieldDescription: JTextField? = null
    private var jLabel1: JLabel? = null
    private var jTextFieldULX: JTextField? = null
    private var jLabel2: JLabel? = null
    private var jTextFieldULY: JTextField? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jTextFieldSizeWidth: JTextField? = null
    private var jTextFieldSizeHeight: JTextField? = null
    private var jPanel1: JPanel? = null
    private var jButton: JButton? = null
    private var jButtonSave: JButton? = null
    private var jLabel6: JLabel? = null
    private var jLabelImageHeight: JLabel? = null
    private var jLabelImageWidth1: JLabel? = null
    private var jLabelImageHeight1: JLabel? = null
    private var jLabelImageWidth: JLabel? = null
    private var thisDialog: PositionTemplateBoxDialog? = null
    /**
     * Result state of the dialog.
     *
     * @return RESULT_SAVE if user closed by pressing save, RESULT_CANCEL otherwise.
     */
    var result = RESULT_CANCEL
        private set
    private var jLabelFeedback: JLabel? = null

    /**
     * Don't call this constructor, use PositionTemplateBoxDialog(Frame owner, Dimension ImageSize, Dimension aULToChange, Dimension aSizeToChange,  String description)
     * and provide values to edit instead.  Protected so that it can be overridden in a class that extends this dialog (presumbably to create a new box with some default values).
     *
     * @param owner
     */
    protected constructor(owner: Frame?) : super(owner) {
        thisDialog = this
    }

    /**
     * Dialog to obtain new values for the upper left corner and size of a rectangular box drawn on an image
     * identified by the upper left corner of the box and the height and width of the box in all in units of
     * pixels.
     *
     * @param owner         the parent frame of this dialog.
     * @param imageSize     the height and width of the image onto which this box is placed
     * @param aULToChange   the upper left corner of the box, in units of pixels of the image
     * @param aSizeToChange the height and width of the box, in units of pixels of the image
     * @param description   a text description of the box to display on the dialog.
     * @throws BadTemplateException if a dimension parameter has a height or width less than or equal to zero (aULtoChange can be 0).
     */
    constructor(owner: Frame, imageSize: Dimension, aULToChange: Dimension, aSizeToChange: Dimension, description: String?) : super(owner, true) // create as modal over parent frame.
    {
        thisDialog = this
        // store values (and throw exception if they are out of range)
        setImageSize(imageSize)
        uL = aULToChange
        setSize(aSizeToChange)
        // set up the form
        initialize()
        // display values on form
        jLabelImageWidth.setText(Integer.toString(maxX))
        jLabelImageHeight.setText(Integer.toString(maxY))
        jLabelImageWidth1.setText(Integer.toString(maxX))
        jLabelImageHeight1.setText(Integer.toString(maxY))
        jTextFieldULX.setText(Integer.toString(uL.width))
        jTextFieldULY.setText(Integer.toString(uL.height))
        jTextFieldSizeWidth.setText(Integer.toString(getSize().width))
        jTextFieldSizeHeight.setText(Integer.toString(getSize().height))
        jTextFieldDescription.setText(description)
        jLabelFeedback.setText("")
        this.pack()
        val screenSize: Dimension = owner.Size
        this.setLocation((screenSize.width - this.Width) / 2,
                (screenSize.height - this.Height) / 2)
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        setSize(342, 200)
        this.setTitle("Edit a template component")
        this.setContentPane(getJContentPane())
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
            jContentPane.add(getJPanel(), BorderLayout.CENTER)
        }
        return jContentPane
    }

    /**
     * Get the upper left coordinate of the box in pixels on the
     * original image.
     *
     * @return ul the upper left coordinate of the box.
     */
    /**
     * @param ul the upper left coordinate of the box to set
     * @throws BadTemplateException if ul has a height or width less than zero (zero is ok).
     */
    @set:Throws(BadTemplateException::class)
    var uL: Dimension?
        get() = ul
        set(ul) {
            if (ul.width < 0 || ul.height < 0) {
                throw BadTemplateException("Upper left coordinate can't be less than 0.")
            }
            this.ul = ul
        }

    /**
     * Get the height and width of the box in pixels.
     *
     * @return the size of the box
     */
    override fun getSize(): Dimension? {
        return size
    }

    /**
     * @param size the size to set
     * @throws BadTemplateException if the size has a height or width less than or equal to 0.
     */
    override fun setSize(size: Dimension) {
        if (size.width <= 0 || size.height <= 0) {
            try {
                throw BadTemplateException("Box size can't be 0 or less.")
            } catch (e: BadTemplateException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        this.size = size
    }

    /**
     * @param imageSize the imageSize to set
     * @throws BadTemplateException
     */
    @Throws(BadTemplateException::class)
    fun setImageSize(imageSize: Dimension) {
        if (imageSize.width <= 0 || imageSize.height <= 0) {
            throw BadTemplateException("Image height and width can't be 0 or less.")
        }
        maxX = imageSize.width
        maxY = imageSize.height
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints71 = GridBagConstraints()
            gridBagConstraints71.gridx = 1
            gridBagConstraints71.gridy = 7
            jLabelFeedback = JLabel()
            jLabelFeedback.setText("JLabel")
            val gridBagConstraints61 = GridBagConstraints()
            gridBagConstraints61.gridx = 2
            gridBagConstraints61.gridy = 2
            jLabelImageWidth = JLabel()
            jLabelImageWidth.setText("JLabel")
            val gridBagConstraints51 = GridBagConstraints()
            gridBagConstraints51.gridx = 2
            gridBagConstraints51.gridy = 5
            jLabelImageHeight1 = JLabel()
            jLabelImageHeight1.setText("JLabel")
            val gridBagConstraints41 = GridBagConstraints()
            gridBagConstraints41.gridx = 2
            gridBagConstraints41.gridy = 4
            jLabelImageWidth1 = JLabel()
            jLabelImageWidth1.setText("JLabel")
            val gridBagConstraints31 = GridBagConstraints()
            gridBagConstraints31.gridx = 2
            gridBagConstraints31.gridy = 3
            jLabelImageHeight = JLabel()
            jLabelImageHeight.setText("JLabel")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.gridx = 2
            gridBagConstraints21.gridy = 1
            jLabel6 = JLabel()
            jLabel6.setText("Image (max)")
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.gridx = 1
            gridBagConstraints10.gridy = 6
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.fill = GridBagConstraints.BOTH
            gridBagConstraints9.gridy = 5
            gridBagConstraints9.weightx = 1.0
            gridBagConstraints9.gridx = 1
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.fill = GridBagConstraints.BOTH
            gridBagConstraints8.gridy = 4
            gridBagConstraints8.weightx = 1.0
            gridBagConstraints8.gridx = 1
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridy = 5
            jLabel4 = JLabel()
            jLabel4.setText("Height")
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 0
            gridBagConstraints6.anchor = GridBagConstraints.EAST
            gridBagConstraints6.gridy = 4
            jLabel3 = JLabel()
            jLabel3.setText("Width")
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 3
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.gridx = 0
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.gridy = 3
            jLabel2 = JLabel()
            jLabel2.setText("Upper Left Y")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            gridBagConstraints3.gridy = 2
            gridBagConstraints3.weightx = 1.0
            gridBagConstraints3.gridx = 1
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 0
            gridBagConstraints2.anchor = GridBagConstraints.EAST
            gridBagConstraints2.insets = Insets(0, 5, 0, 0)
            gridBagConstraints2.gridy = 2
            jLabel1 = JLabel()
            jLabel1.setText("Upper Left X")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            gridBagConstraints1.gridy = 1
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.ipadx = 0
            gridBagConstraints1.insets = Insets(0, 0, 0, 0)
            gridBagConstraints1.gridwidth = 1
            gridBagConstraints1.gridx = 1
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridy = 0
            jLabel = JLabel()
            jLabel.setText("Edit a template component.")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, gridBagConstraints)
            jPanel.add(getJTextFieldDescription(), gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(getJTextFieldULX(), gridBagConstraints3)
            jPanel.add(jLabel2, gridBagConstraints4)
            jPanel.add(getJTextFieldULY(), gridBagConstraints5)
            jPanel.add(jLabel3, gridBagConstraints6)
            jPanel.add(jLabel4, gridBagConstraints7)
            jPanel.add(getJTextFieldSizeWidth(), gridBagConstraints8)
            jPanel.add(getJTextFieldSizeHeight(), gridBagConstraints9)
            jPanel.add(getJPanel1(), gridBagConstraints10)
            jPanel.add(jLabel6, gridBagConstraints21)
            jPanel.add(jLabelImageHeight, gridBagConstraints31)
            jPanel.add(jLabelImageWidth1, gridBagConstraints41)
            jPanel.add(jLabelImageHeight1, gridBagConstraints51)
            jPanel.add(jLabelImageWidth, gridBagConstraints61)
            jPanel.add(jLabelFeedback, gridBagConstraints71)
        }
        return jPanel
    }

    /**
     * This method initializes jTextFieldDescription
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDescription(): JTextField? {
        if (jTextFieldDescription == null) {
            jTextFieldDescription = JTextField()
            jTextFieldDescription.setEditable(false)
            jTextFieldDescription.setText("")
        }
        return jTextFieldDescription
    }

    /**
     * This method initializes jTextFieldULX
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldULX(): JTextField? {
        if (jTextFieldULX == null) {
            jTextFieldULX = JTextField()
        }
        return jTextFieldULX
    }

    /**
     * This method initializes jTextFieldULY
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldULY(): JTextField? {
        if (jTextFieldULY == null) {
            jTextFieldULY = JTextField()
        }
        return jTextFieldULY
    }

    /**
     * This method initializes jTextFieldSizeWidth
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldSizeWidth(): JTextField? {
        if (jTextFieldSizeWidth == null) {
            jTextFieldSizeWidth = JTextField()
        }
        return jTextFieldSizeWidth
    }

    /**
     * This method initializes jTextFieldSizeHeight
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldSizeHeight(): JTextField? {
        if (jTextFieldSizeHeight == null) {
            jTextFieldSizeHeight = JTextField()
        }
        return jTextFieldSizeHeight
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.gridx = 1
            gridBagConstraints11.gridy = 0
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            jPanel1.add(getJButton(), GridBagConstraints())
            jPanel1.add(getJButtonSave(), gridBagConstraints11)
        }
        return jPanel1
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Cancel")
            jButton.setMnemonic(KeyEvent.VK_C)
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    thisDialog.setVisible(false)
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jButtonSave
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSave(): JButton? {
        if (jButtonSave == null) {
            jButtonSave = JButton()
            jButtonSave.setText("Change")
            jButtonSave.setMnemonic(KeyEvent.VK_H)
            jButtonSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try {
                        uL = Dimension(Integer.valueOf(jTextFieldULX.Text), Integer.valueOf(jTextFieldULY.Text))
                        setSize(Dimension(Integer.valueOf(jTextFieldSizeWidth.Text), Integer.valueOf(jTextFieldSizeHeight.Text)))
                        if (validateValues()) {
                            result = RESULT_SAVE
                        }
                    } catch (e1: NumberFormatException) { // failed...
                    } catch (e1: BadTemplateException) { // failed...
                    }
                    if (result != RESULT_SAVE) {
                        jLabelFeedback.setText("Can't save those values.")
                    } else {
                        jLabelFeedback.setText("")
                        thisDialog.setVisible(false)
                    }
                }
            })
        }
        return jButtonSave
    }

    /**
     * validate form fields
     *
     * @return true if valid, false otherwise.
     */
    private fun validateValues(): Boolean {
        var result = true
        // Check that UL coordinate is on image.
        if (ul.height < minY || ul.height > maxY) {
            result = false
        }
        if (ul.width < minX || ul.width > maxX) {
            result = false
        }
        // check that size is smaller that image
        if (size.height <= minY || size.height > maxY) {
            result = false
        }
        if (size.width <= minX || size.width > maxX) {
            result = false
        }
        // check that box fits in image
        if (ul.height + size.height > maxY) {
            result = false
        }
        if (ul.width + size.width > maxX) {
            result = false
        }
        return result
    }

    companion object {
        private const val serialVersionUID = 6798207249250029852L
        private const val RESULT_SAVE = 0
        private const val RESULT_CANCEL = 1
        // 0,0  (upper left corner of image)
        private const val minX = 0
        private const val minY = 0
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
