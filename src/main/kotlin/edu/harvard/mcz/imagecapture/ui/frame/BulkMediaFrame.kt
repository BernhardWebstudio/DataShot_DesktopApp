/**
 * BulkMediaFrame.java
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


import edu.harvard.mcz.imagecapture.CandidateImageFile
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PropertiesEditor
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.ThumbnailBuilder
import edu.harvard.mcz.imagecapture.data.BulkMedia
import edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.QuoteMode
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.InputEvent
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.PrintWriter
import javax.swing.*
import javax.swing.border.EmptyBorder

java.awt.event.KeyEvent
import  java.beans.PropertyChangeListener


import java.io.File 
import java.lang.Exception javax.swing.SwingWorker


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
 * Frame to manage creating BulkMedia files from image files.
 */
class BulkMediaFrame : JFrame(), PropertyChangeListener {
    private var contentPane: JPanel? = null
    private var textField: JTextField? = null
    private var textField_1: JTextField? = null
    private var progressBar: JProgressBar? = null
    private var thisFrame: BulkMediaFrame? = null
    private var task: ScanDirectoryTask? = null
    private fun done() {
        thisFrame.setVisible(false)
        System.exit(0)
    }

    private fun init() {
        thisFrame = this
        setTitle("BulkMedia Preparation")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        setBounds(100, 100, 902, 174)
        val menuBar = JMenuBar()
        setJMenuBar(menuBar)
        val mnFile = JMenu("File")
        mnFile.setMnemonic(KeyEvent.VK_F)
        menuBar.add(mnFile)
        val mntmPrepareDirectory = JMenuItem("Prepare Directory")
        mntmPrepareDirectory.setMnemonic(KeyEvent.VK_D)
        mntmPrepareDirectory.addActionListener(PrepareDirectoryAction())
        mnFile.add(mntmPrepareDirectory)
        val mntmNewMenuItem = JMenuItem("Exit")
        mntmNewMenuItem.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                done()
            }
        })
        val mntmNewMenuItem_1 = JMenuItem("Edit Properties")
        mntmNewMenuItem_1.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                val p = PropertiesEditor()
                p.pack()
                p.setVisible(true)
            }
        })
        mnFile.add(mntmNewMenuItem_1)
        mntmNewMenuItem.setMnemonic(KeyEvent.VK_X)
        mntmNewMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK))
        mnFile.add(mntmNewMenuItem)
        contentPane = JPanel()
        contentPane.setBorder(EmptyBorder(5, 5, 5, 5))
        setContentPane(contentPane)
        contentPane.setLayout(BorderLayout(0, 0))
        val panel = JPanel()
        contentPane.add(panel, BorderLayout.CENTER)
        panel.setLayout(GridLayout(3, 2, 0, 0))
        val lblBaseUri = JLabel("Base URI (first part of path to images on the web)")
        lblBaseUri.setHorizontalAlignment(SwingConstants.TRAILING)
        panel.add(lblBaseUri, "2, 2")
        textField = JTextField()
        textField.setEditable(false)
        textField.setText(
                Singleton
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASEURI))
        panel.add(textField)
        textField.setColumns(10)
        val lblNewLabel = JLabel(
                "Local Path To Base (local mount path that maps to base URI)")
        lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING)
        panel.add(lblNewLabel)
        textField_1 = JTextField()
        textField_1.setEditable(false)
        textField_1.setText(Singleton
                .getProperties()
                .getProperties()
                .getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
        panel.add(textField_1)
        textField_1.setColumns(10)
        val lblBeforeExitingWait = JLabel(
                "Before exiting wait for both the Done and Thumbnails Built messages.")
        panel.add(lblBeforeExitingWait)
        val lblThumbnailGenerationIs = JLabel("Thumbnail generation is not reported on the progress bar.")
        panel.add(lblThumbnailGenerationIs)
        progressBar = JProgressBar()
        progressBar.setStringPainted(false)
        contentPane.add(progressBar, BorderLayout.NORTH)
        val panel_1 = JPanel()
        contentPane.add(panel_1, BorderLayout.SOUTH)
        val btnPrepareDirectory = JButton("Run")
        btnPrepareDirectory.setToolTipText(
                "Select a directory and prepare a bulk media file for images therein.")
        panel_1.add(btnPrepareDirectory)
        btnPrepareDirectory.addActionListener(PrepareDirectoryAction())
        btnPrepareDirectory.setPreferredSize(Dimension(80, 25))
        val btnExit = JButton("Exit")
        btnExit.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                done()
            }
        })
        panel_1.add(btnExit)
    }

    /* (non-Javadoc)
     * @see
     *     java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    override fun propertyChange(evt: PropertyChangeEvent) {
        log.debug(evt.getPropertyName())
        if (evt.getPropertyName() === "progress") {
            val progress = (evt.getNewValue() as Int)
            progressBar.setValue(progress)
            progressBar.setStringPainted(true)
            log.debug(progress)
        }
    }

    internal inner class PrepareDirectoryAction : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            task = ScanDirectoryTask()
            task.addPropertyChangeListener(thisFrame)
            task.execute()
        }
    }

    internal inner class ScanDirectoryTask : SwingWorker<Void?, Void?>() {
        /*
         * Main task. Executed in background thread.
         */
        override fun doInBackground(): Void? {
            var progress = 0
            setProgress(progress)
            val fileChooser = JFileChooser()
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
            var startAt: File? = null
            try {
                startAt = File(Singleton
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
            } catch (e: Exception) {
                log.debug(e.message)
            }
            fileChooser.setCurrentDirectory(startAt)
            fileChooser.setDialogTitle(
                    "Pick a directory of image files to check for barcodes.")
            val returnValue: Int = fileChooser.showOpenDialog(
                    Singleton.getMainFrame())
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val directory: File = fileChooser.getSelectedFile()
                log.debug("Selected base directory: " + directory.getName() + ".")
                val files: Array<File?> = directory.listFiles()
                val fileCount = files.size
                var matchCount = 0
                if (fileCount > 0) { // create thumbnails in a separate thread
                    Thread(ThumbnailBuilder(directory)).start()
                }
                if (fileCount > 0) {
                    var position = 0
                    var okToRun = false
                    val output = File(directory.getAbsolutePath() + File.separatorChar +
                            BULKFILENAME)
                    log.debug(output.getPath())
                    if (output.exists()) {
                        val option: Int = JOptionPane.showConfirmDialog(
                                thisFrame,
                                "Output file " + output.getName() +
                                        " already exists in selected directory.  Overwrite it?.",
                                "File Exists.  Overwrite?", JOptionPane.WARNING_MESSAGE)
                        if (option == JOptionPane.OK_OPTION) { // no need to take action to truncate output file, PrintWriter
// will truncate rather than appending.
                            okToRun = true
                        }
                    }
                    if (!output.exists()) {
                        okToRun = true
                    }
                    if (okToRun) {
                        try {
                            val format: CSVFormat = CSVFormat.RFC4180.withDelimiter(',')
                                    .withQuote('"')
                                    .withQuoteMode(QuoteMode.ALL)
                            val writer = PrintWriter(output)
                            val printer = CSVPrinter(writer, format)
                            printer.printRecord(BulkMedia.Companion.getHeaders())
                            for (candidate in files) {
                                progress = Math.round(position.toFloat() / fileCount.toFloat() * 100.0f)
                                log.debug(progress)
                                setProgress(progress)
                                if (candidate.getName().matches(
                                                Singleton
                                                        .getProperties()
                                                        .getProperties()
                                                        .getProperty(
                                                                ImageCaptureProperties.Companion.KEY_IMAGEREGEX))) {
                                    try {
                                        val line: BulkMedia = CandidateImageFile.Companion.parseOneFileToBulkMedia(
                                                candidate.getCanonicalPath())
                                        log.debug(line.getData())
                                        printer.printRecord(line.getData())
                                        matchCount++
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                }
                                position++
                            }
                            printer.close()
                            writer.close()
                        } catch (e1: FileNotFoundException) {
                            JOptionPane.showMessageDialog(
                                    thisFrame,
                                    "Output file " + output.getName() +
                                            " was not writable or could not be created.",
                                    "File Not writable.", JOptionPane.ERROR_MESSAGE)
                            log.error(e1.message)
                        } catch (e1: IOException) {
                            JOptionPane.showMessageDialog(
                                    thisFrame,
                                    "Error writing to file " + output.getName() + ". " +
                                            e1.message,
                                    "File write error.", JOptionPane.ERROR_MESSAGE)
                            log.error(e1.message, e1)
                        }
                    } else {
                        log.error(output.exists())
                        JOptionPane.showMessageDialog(
                                thisFrame,
                                "Output file " + output.getName() +
                                        " already exists in selected directory.",
                                "File Exists", JOptionPane.ERROR_MESSAGE)
                    }
                } else {
                    log.error("No files in selected directory. " + directory.toString())
                }
                setProgress(100)
                JOptionPane.showMessageDialog(
                        null,
                        "Done.  Checked " + matchCount + "  files out of " + fileCount +
                                ".\nResult is in " + BULKFILENAME,
                        "Done.", JOptionPane.INFORMATION_MESSAGE)
            } else {
                log.error("Directory selection cancelled by user.")
            }
            return null
        }

        /*
         * Executed in event dispatching thread
         */
        override fun done() {
            progressBar.setString("Done")
        }
    }

    companion object {
        val BULKFILENAME: String? = "media_bulkload.csv"
        private const val serialVersionUID = 1307585080820001695L
        private val log: Log = LogFactory.getLog(BulkMediaFrame::class.java)
    }

    /**
     * Create the frame.
     */
    init {
        init()
    }
}
