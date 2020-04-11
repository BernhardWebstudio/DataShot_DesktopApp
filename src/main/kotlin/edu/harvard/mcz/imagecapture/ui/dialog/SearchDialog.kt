/**
 * SearchDialog.java
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


import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.Tracking
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycle
import edu.harvard.mcz.imagecapture.ui.field.JIntegerField
import net.miginfocom.swing.MigLayout
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.*
import kotlin.collections.ArrayList

java.util.ArrayList


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
 * SearchDialog
 */
class SearchDialog(owner: Frame?) : JDialog(owner) {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var jButton: JButton? = null
    private var jPanel1: JPanel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jTextFieldFamily: JTextField? = null
    private var jTextFieldGenus: JTextField? = null
    private var jTextFieldSpecies: JTextField? = null
    private var jComboBoxCollection: JComboBox<*>? = null
    private var jComboBoxWorkflowStatus: JComboBox<*>? = null
    private var jTextFieldImageFilename: JTextField? = null
    private var jComboBoxPath: JComboBox<*>? = null
    private var jComboBoxEntryBy: JComboBox<*>? = null
    private var jComboBoxIdentifiedBy: JComboBox<*>? = null
    private var jTextFieldSubfamily: JTextField? = null
    private var jTextFieldSubspecies: JTextField? = null
    private var jButton1: JButton? = null
    private var jComboBoxCollector: JComboBox<*>? = null
    private var jTextFieldVerbatimLocality: JTextField? = null
    private var jComboBoxCountry: JComboBox<*>? = null
    private var jComboBoxQuestions: JComboBox<*>? = null
    private var jOffsetNumberField: JIntegerField? = null
    private var jLimitNumberField: JIntegerField? = null
    private var jTextFieldTribe: JTextField? = null
    //private JTextField jTextFieldPrimaryDivision = null;
    private var jComboBoxPrimaryDivision: JComboBox<String?>? = null
    private var textFieldHigherGeog: JTextField? = null
    private var lblHigherGeography: JLabel? = null private get() {
        if (field == null) {
            field = JLabel("Higher Geography")
        }
        return field
    }
    private var jTextFieldInterpretedDate: JTextField? = null
    private val maxComboBoxDims: Dimension = Dimension(350, 250)
    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() { //        this.setSize(500, 750);
        this.setTitle("Search For Specimens")
        this.setContentPane(getJContentPane())
        //        this.setPreferredSize(new Dimension(500, 750));
        this.pack()
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
            jContentPane.add(getJPanel(), BorderLayout.SOUTH)
            jContentPane.add(getJPanel1(), BorderLayout.CENTER)
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
            val gridBagConstraints17 = GridBagConstraints()
            gridBagConstraints17.gridx = 1
            gridBagConstraints17.gridy = 0
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jButtonSearch, GridBagConstraints())
            jPanel.add(closeJButton, gridBagConstraints17)
        }
        return jPanel
    }// it the path = date imaged has content, add it// if filename has content, add it// Either image filename or date imaged or both have content
// so we need to add an image to the search criteria.
// Default specimen is created with valid distribution flag = true, etc. need to remove this
// from the search criteria for a search by example.
    /*if (jTextFieldPrimaryDivision.Text!=null && jTextFieldPrimaryDivision.Text.length() > 0) {
        searchCriteria.setPrimaryDivison(jTextFieldPrimaryDivision.Text);
    }*/
    // TODO: Add higher geography

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val jButtonSearch: JButton?
        private get() {
            if (jButton == null) {
                jButton = JButton()
                jButton.setText("Search")
                jButton.setMnemonic(KeyEvent.VK_S)
                this.RootPane.setDefaultButton(jButton)
                jButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        Singleton.MainFrame.setStatusMessage("Searching...")
                        val searchCriteria = Specimen()
                        // Default specimen is created with valid distribution flag = true, etc. need to remove this
// from the search criteria for a search by example.
                        searchCriteria.clearDefaults()
                        if (jTextFieldDrawerNumber.Text != null && jTextFieldDrawerNumber.Text.length > 0) {
                            searchCriteria.setDrawerNumber(jTextFieldDrawerNumber.Text)
                        }
                        if (jTextFieldBarcode.Text != null && jTextFieldBarcode.Text.length > 0) {
                            searchCriteria.setBarcode(jTextFieldBarcode.Text)
                        }
                        if (jTextFieldFamily.Text != null && jTextFieldFamily.Text.length > 0) {
                            searchCriteria.setFamily(jTextFieldFamily.Text)
                        }
                        if (jTextFieldSubfamily.Text != null && jTextFieldSubfamily.Text.length > 0) {
                            searchCriteria.setSubfamily(jTextFieldSubfamily.Text)
                        }
                        if (jTextFieldTribe.Text != null && jTextFieldTribe.Text.length > 0) {
                            searchCriteria.setTribe(jTextFieldTribe.Text)
                        }
                        if (jTextFieldGenus.Text != null && jTextFieldGenus.Text.length > 0) {
                            searchCriteria.setGenus(jTextFieldGenus.Text)
                        }
                        if (jTextFieldSpecies.Text != null && jTextFieldSpecies.Text.length > 0) {
                            searchCriteria.setSpecificEpithet(jTextFieldSpecies.Text)
                        }
                        if (jTextFieldSubspecies.Text != null && jTextFieldSubspecies.Text.length > 0) {
                            searchCriteria.setSubspecificEpithet(jTextFieldSubspecies.Text)
                        }
                        if (jTextFieldVerbatimLocality.Text != null && jTextFieldVerbatimLocality.Text.length > 0) {
                            searchCriteria.setVerbatimLocality(jTextFieldVerbatimLocality.Text)
                        }
                        /*if (jTextFieldPrimaryDivision.Text!=null && jTextFieldPrimaryDivision.Text.length() > 0) {
                            searchCriteria.setPrimaryDivison(jTextFieldPrimaryDivision.Text);
                        }*/if (jComboBoxWorkflowStatus.SelectedItem != null) {
                            if (jComboBoxWorkflowStatus.SelectedItem.toString() != "") {
                                searchCriteria.setWorkFlowStatus(jComboBoxWorkflowStatus.SelectedItem.toString())
                            }
                        }
                        // TODO: Add higher geography
                        if (jComboBoxCountry.SelectedItem != null) {
                            if (jComboBoxCountry.SelectedItem.toString() != "") {
                                searchCriteria.setCountry(jComboBoxCountry.SelectedItem.toString())
                            }
                        }
                        if (jComboBoxPrimaryDivision.SelectedItem != null) {
                            if (jComboBoxPrimaryDivision.SelectedItem.toString() != "") {
                                searchCriteria.setPrimaryDivison(jComboBoxPrimaryDivision.SelectedItem.toString())
                            }
                        }
                        if (jTextFieldInterpretedDate.Text != null && jTextFieldInterpretedDate.Text.length > 0) {
                            searchCriteria.setIsoDate(jTextFieldInterpretedDate.Text)
                        }
                        if (jComboBoxCollector.SelectedItem != null) {
                            if (jComboBoxCollector.SelectedItem.toString() != "") {
                                val c = Collector()
                                c.setCollectorName(jComboBoxCollector.SelectedItem.toString())
                                val collectors: MutableSet<Collector?> = HashSet<Collector?>()
                                collectors.add(c)
                                searchCriteria.setCollectors(collectors)
                            }
                        }
                        if (jTextFieldImageFilename.Text != null && jTextFieldImageFilename.Text.length > 0 ||
                                jComboBoxEntryBy.SelectedItem != null) { // Either image filename or date imaged or both have content
// so we need to add an image to the search criteria.
                            val i = ICImage()
                            if (jTextFieldImageFilename.Text != null && jTextFieldImageFilename.Text.length > 0) { // if filename has content, add it
                                i.setFilename(jTextFieldImageFilename.Text)
                            }
                            if (jComboBoxPath.SelectedItem != null) {
                                if (jComboBoxPath.SelectedItem.toString() != "") { // it the path = date imaged has content, add it
                                    i.setPath(jComboBoxPath.SelectedItem.toString())
                                }
                            }
                            val im: MutableSet<ICImage?> = HashSet<ICImage?>()
                            im.add(i)
                            searchCriteria.setICImages(im)
                        }
                        if (jComboBoxCollection.SelectedItem != null) {
                            if (jComboBoxCollection.SelectedItem.toString() != "") {
                                searchCriteria.setCollection(jComboBoxCollection.SelectedItem.toString())
                            }
                        }
                        if (jComboBoxEntryBy.SelectedItem != null) {
                            if (jComboBoxEntryBy.SelectedItem.toString() != "") {
                                val tc = Tracking()
                                tc.setUser(jComboBoxEntryBy.SelectedItem.toString())
                                val trackings: MutableSet<Tracking?> = HashSet<Tracking?>()
                                trackings.add(tc)
                                searchCriteria.setTrackings(trackings)
                            }
                        }
                        if (jComboBoxIdentifiedBy.SelectedItem != null) {
                            if (jComboBoxIdentifiedBy.SelectedItem.toString() != "") {
                                searchCriteria.setIdentifiedBy(jComboBoxIdentifiedBy.SelectedItem.toString())
                            }
                        }
                        if (jComboBoxQuestions.SelectedItem != null) {
                            if (jComboBoxQuestions.SelectedItem.toString() != "") {
                                searchCriteria.setQuestions(jComboBoxQuestions.SelectedItem.toString())
                            }
                        }
                        Singleton.MainFrame.setSpecimenBrowseList(searchCriteria, jLimitNumberField.IntValue, jOffsetNumberField.IntValue)
                    }
                })
            }
            return jButton
        }

    /**
     * This method initializes the main JPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) { // set titles etc.
            jPanel1 = JPanel(MigLayout("wrap 2, fillx"))
            val jLabelInstructions = JLabel("Search for specimens. Use % as a wildcard.")
            val f: Font = jLabelInstructions.Font
            // bold
            jLabelInstructions.setFont(f.deriveFont(f.Style or Font.BOLD))
            jPanel1.add(jLabelInstructions, "span 2")
            // set fields
            val labels = arrayOf<String?>(
                    "Image Filename",
                    "Imaged Date/Path",
                    "Drawer Number",
                    "Barcode",
                    "Family",
                    "Tribe",
                    "Subfamily",
                    "Genus",
                    "Species",
                    "Subspecies",
                    "Verbatim Locality",
                    "Country",
                    "State/Province",
                    "Collection",
                    "Collector",
                    "Interpreted Date",
                    "Workflow Status",
                    "Questions",
                    "Entry By",
                    "Identified By",
                    "Limit",
                    "Offset"
            )
            val fields: Array<Component?> = arrayOf<Component?>(
                    imageFileJTextField,
                    imagePathJComboBox,
                    drawerNumberJTextField,
                    barcodeJTextField,
                    familyJTextField,
                    tribeJTextField,
                    subfamilyJTextField,
                    genusJTextField,
                    speciesJTextField,
                    subspeciesJTextField,
                    verbatimLocalityJTextField,
                    countryJComboBox,
                    primaryDivisionJComboBox,
                    collectionJComboBox,
                    collectorsJComboBox,
                    interpretedDateTextField,
                    workflowsJComboBox,
                    questionJComboBox,
                    usersJComboBox,
                    identifiedByComboBox,
                    limitJIntegerField,
                    offsetJIntegerField
            )
            assert(fields.size == labels.size)
            for (i in labels.indices) {
                val label = JLabel()
                label.setText(labels[i] + ":")
                jPanel1.add(label, "right") //"align label");
                jPanel1.add(fields[i], "grow")
            }
            /*
						GridBagConstraints gbc_lblHigherGeography = new GridBagConstraints();
						gbc_lblHigherGeography.insets = new Insets(0, 0, 5, 5);
						gbc_lblHigherGeography.anchor = GridBagConstraints.EAST;
						gbc_lblHigherGeography.gridx = 0;
						gbc_lblHigherGeography.gridy = 12;
						jPanel1.add(getLblHigherGeography(), gbc_lblHigherGeography);
						GridBagConstraints gbc_textFieldHigherGeog = new GridBagConstraints();
						gbc_textFieldHigherGeog.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldHigherGeog.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldHigherGeog.gridx = 1;
						gbc_textFieldHigherGeog.gridy = 12;
						*/
//jPanel1.add(getTextFieldHigherGeog(), gbc_textFieldHigherGeog);
        }
        return jPanel1
    }

    private fun initializeBasicGridBag(): GridBagConstraints {
        val c = GridBagConstraints()
        c.insets = Insets(0, 0, 5, 0)
        return c
    }

    private fun initializeBasicGridBag(grid_x: Int, grid_y: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag()
        c.gridx = grid_x
        c.gridy = grid_y
        return c
    }

    private fun initializeBasicGridBag(grid_x: Int, grid_y: Int, anchor: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y)
        c.anchor = anchor
        return c
    }

    private fun initializeBasicResizableGridBag(grid_x: Int, grid_y: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y)
        c.fill = GridBagConstraints.BOTH
        return c
    }

    private fun initializeBasicResizableGridBag(grid_x: Int, grid_y: Int, anchor: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y, anchor)
        c.fill = GridBagConstraints.BOTH
        return c
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val drawerNumberJTextField: JTextField?
        private get() {
            if (jTextFieldDrawerNumber == null) {
                jTextFieldDrawerNumber = JTextField()
            }
            return jTextFieldDrawerNumber
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val barcodeJTextField: JTextField?
        private get() {
            if (jTextFieldBarcode == null) {
                jTextFieldBarcode = JTextField()
            }
            return jTextFieldBarcode
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val familyJTextField: JTextField?
        private get() {
            if (jTextFieldFamily == null) {
                jTextFieldFamily = JTextField()
            }
            return jTextFieldFamily
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val genusJTextField: JTextField?
        private get() {
            if (jTextFieldGenus == null) {
                jTextFieldGenus = JTextField()
            }
            return jTextFieldGenus
        }

    /**
     * This method initializes jTextField4
     *
     * @return javax.swing.JTextField
     */
    private val speciesJTextField: JTextField?
        private get() {
            if (jTextFieldSpecies == null) {
                jTextFieldSpecies = JTextField()
            }
            return jTextFieldSpecies
        }

    /**
     * This method initializes jComboBox labelled collection
     *
     * @return javax.swing.JComboBox
     */
    private val collectionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCollection == null) {
                val sls = SpecimenLifeCycle()
                jComboBoxCollection = JComboBox<String?>()
                jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.DistinctCollections))
                jComboBoxCollection.setEditable(true)
                jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
                jComboBoxCollection.setMaximumSize(maxComboBoxDims)
                AutoCompleteDecorator.decorate(jComboBoxCollection)
            }
            return jComboBoxCollection
        }

    /**
     * This method initializes jComboBox1 labelled workflow
     *
     * @return javax.swing.JComboBox
     */
    private val workflowsJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxWorkflowStatus == null) {
                val values = ArrayList<String?>()
                values.add("")
                val wfsv: Array<String?> = WorkFlowStatus.WorkFlowStatusValues
                for (x in wfsv.indices) {
                    values.add(wfsv[x])
                }
                jComboBoxWorkflowStatus = JComboBox<Any?>(values.toTypedArray())
                jComboBoxWorkflowStatus.Model.setSelectedItem("")
                jComboBoxWorkflowStatus.setEditable(true)
                jComboBoxWorkflowStatus.setMaximumSize(maxComboBoxDims)
                AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus)
            }
            return jComboBoxWorkflowStatus
        }

    /**
     * This method initializes jTextField5
     *
     * @return javax.swing.JTextField
     */
    private val imageFileJTextField: JTextField?
        private get() {
            if (jTextFieldImageFilename == null) {
                jTextFieldImageFilename = JTextField()
            }
            return jTextFieldImageFilename
        }

    /**
     * This method initializes jComboBox2
     *
     * @return javax.swing.JComboBox
     */
    private val imagePathJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxPath == null) {
                val ils = ICImageLifeCycle()
                jComboBoxPath = JComboBox<Any?>(ils.DistinctPaths)
                jComboBoxPath.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxPath)
                jComboBoxPath.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxPath
        }

    /**
     * This method initializes the combobox containing the users labelled "entry by"
     *
     * @return javax.swing.JComboBox
     */
    private val usersJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxEntryBy == null) {
                val tls = TrackingLifeCycle()
                jComboBoxEntryBy = JComboBox<Any?>(tls.DistinctUsers)
                jComboBoxEntryBy.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxEntryBy)
                jComboBoxEntryBy.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxEntryBy
        }

    /**
     * This method initializes the tracking user combo box
     *
     * @return java.swing.JComboBox
     */
    private val identifiedByComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxIdentifiedBy == null) {
                val sls = SpecimenLifeCycle()
                jComboBoxIdentifiedBy = JComboBox<Any?>(sls.DistinctDeterminers)
                jComboBoxIdentifiedBy.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxIdentifiedBy)
                jComboBoxIdentifiedBy.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxIdentifiedBy
        }

    /**
     * This method initializes jTextField providing subfamily info
     *
     * @return javax.swing.JTextField
     */
    private val subfamilyJTextField: JTextField?
        private get() {
            if (jTextFieldSubfamily == null) {
                jTextFieldSubfamily = JTextField()
            }
            return jTextFieldSubfamily
        }

    /**
     * This method initializes jTextField labelled subspecies
     *
     * @return javax.swing.JTextField
     */
    private val subspeciesJTextField: JTextField?
        private get() {
            if (jTextFieldSubspecies == null) {
                jTextFieldSubspecies = JTextField()
            }
            return jTextFieldSubspecies
        }

    private val interpretedDateTextField: JTextField?
        private get() {
            if (jTextFieldInterpretedDate == null) {
                jTextFieldInterpretedDate = JTextField()
            }
            return jTextFieldInterpretedDate
        }

    /**
     * This method initializes the button to close the form
     * it might be slightly unnecessary as the behaviour is the same as the OS's window close button
     *
     * @return javax.swing.JButton
     */
    private val closeJButton: JButton?
        private get() {
            if (jButton1 == null) {
                jButton1 = JButton()
                jButton1.setText("Close")
                jButton1.setMnemonic(KeyEvent.VK_C)
                jButton1.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
            }
            return jButton1
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val collectorsJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCollector == null) {
                val cls = CollectorLifeCycle()
                jComboBoxCollector = JComboBox<Any?>(cls.DistinctCollectors)
                jComboBoxCollector.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxCollector)
                jComboBoxCollector.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxCollector
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val verbatimLocalityJTextField: JTextField?
        private get() {
            if (jTextFieldVerbatimLocality == null) {
                jTextFieldVerbatimLocality = JTextField()
            }
            return jTextFieldVerbatimLocality
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val countryJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCountry == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val cv: Array<String?> = sls.DistinctCountries
                for (x in cv.indices) {
                    values.add(cv[x])
                }
                jComboBoxCountry = JComboBox<Any?>(values.toTypedArray())
                jComboBoxCountry.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxCountry)
                jComboBoxCountry.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxCountry
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val primaryDivisionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxPrimaryDivision == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val cv: Array<String?> = sls.DistinctPrimaryDivisions
                for (x in cv.indices) {
                    values.add(cv[x])
                }
                jComboBoxPrimaryDivision = JComboBox<Any?>(values.toTypedArray())
                jComboBoxPrimaryDivision.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision)
                jComboBoxPrimaryDivision.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxPrimaryDivision
        }

    /**
     * This method initializes jComboBox1
     *
     * @return javax.swing.JComboBox
     */
    private val questionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxQuestions == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val qv: Array<String?> = sls.DistinctQuestions
                for (x in qv.indices) {
                    values.add(qv[x])
                }
                jComboBoxQuestions = JComboBox<Any?>(values.toTypedArray())
                jComboBoxQuestions.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxQuestions)
                jComboBoxQuestions.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxQuestions
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val tribeJTextField: JTextField?
        private get() {
            if (jTextFieldTribe == null) {
                jTextFieldTribe = JTextField()
            }
            return jTextFieldTribe
        }

    private val offsetJIntegerField: JIntegerField?
        private get() {
            if (jOffsetNumberField == null) {
                jOffsetNumberField = JIntegerField()
                jOffsetNumberField.setValue(0)
            }
            return jOffsetNumberField
        }

    private val limitJIntegerField: JIntegerField?
        private get() {
            if (jLimitNumberField == null) {
                jLimitNumberField = JIntegerField()
                jLimitNumberField.setValue(100)
            }
            return jLimitNumberField
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
/*private JTextField getJTextFieldPrimaryDivision() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
		}
		return jTextFieldPrimaryDivision;
	}*/
    private fun getTextFieldHigherGeog(): JTextField? {
        if (textFieldHigherGeog == null) {
            textFieldHigherGeog = JTextField()
            textFieldHigherGeog.setColumns(10)
        }
        return textFieldHigherGeog
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * @param owner
     */
    init {
        initialize()
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
