/**
 * MainFrame.java
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

import HibernateUtil
import edu.harvard.mcz.imagecapture.*
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.data.LocationInCollection
import edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.Counter
import edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScan
import edu.harvard.mcz.imagecapture.jobs.JobCleanDirectory
import edu.harvard.mcz.imagecapture.jobs.JobFileReconciliation
import edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplates
import edu.harvard.mcz.imagecapture.jobs.JobRepeatOCR
import edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScan
import edu.harvard.mcz.imagecapture.jobs.RunnableJobError
import edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModel
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoad
import edu.harvard.mcz.imagecapture.ui.dialog.*
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Taskbar
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.net.URL
import javax.swing.*
import javax.swing.text.DefaultEditorKit.CopyAction
import javax.swing.text.DefaultEditorKit.PasteAction

edu.harvard.mcz.imagecapture.data .
import HibernateUtil 
import edu.harvard.mcz.imagecapture.jobs.Counter 
import org.apache.commons.logging.Log 
import org.apache.commons.logging.LogFactory java.awt.*import  java.awt.event.ActionEvent


import java.awt.event.KeyEvent java.io.File
import  java.lang.Exception


import java.net.URL java.util.ArrayList


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
 * Main user interface window for image capture application when run as a java application from the desktop.
 */
class MainFrame : JFrame(), RunnerListener {
    private val thisMainFrame: MainFrame?
    private var state = STATE_INIT
    private var ilb: ImageListBrowser? = null
    private var slb: SpecimenBrowser? = null
    private var ulb: UserListBrowser? = null
    //private JPanel listBrowser = null;
    private var jJMenuBar: JMenuBar? = null
    private var jMenuFile: JMenu? = null
    private var jMenuItemExit: JMenuItem? = null
    private var jMenuAction: JMenu? = null
    private var jMenuHelp: JMenu? = null
    private var jMenuItemAbout: JMenuItem? = null
    private var jMenuItemPreprocess: JMenuItem? = null
    private var jMenuItemDelete: JMenuItem? = null
    private var jMenuItemFindMissingImages: JMenuItem? = null
    private var jMenuItemLoadData: JMenuItem? = null
    private var jMenuItemVersion: JMenuItem? = null
    private var jMenuItemScanOneBarcode: JMenuItem? = null
    private var jMenuItemScanOneBarcodeSave: JMenuItem? = null
    private var jMenuConfig: JMenu? = null
    private var jMenuEdit: JMenu? = null
    private var jMenuItemPreferences: JMenuItem? = null
    private var jMenuItemCopy: JMenuItem? = null
    private var jMenuItemPaste: JMenuItem? = null
    private var jMenuItemLog: JMenuItem? = null
    private var jPanel: JPanel? = null
    private var jProgressBar: JProgressBar? = null
    private var jPanel1: JPanel? = null
    private var jLabelStatus: JLabel? = null
    private var jMenuItemVerbatimTranscription: JMenuItem? = null
    private var jMenuItemVerbatimClassification: JMenuItem? = null
    private var jMenuItemBrowseSpecimens: JMenuItem? = null
    private var jPanelCenter: JPanel? = null
    private var jMenuItemEditTemplates: JMenuItem? = null
    private var jMenuItemBrowseImages: JMenuItem? = null
    private var jLabelCount: JLabel? = null
    private var jMenuItemPreprocessOneDir: JMenuItem? = null
    private var jMenuData: JMenu? = null
    private var jMenuQualityControl: JMenu? = null
    private var jMenuItemCheckForABarcode: JMenuItem? = null
    private var jMenuItemQCBarcodes: JMenuItem? = null
    private var jMenuItemSearch: JMenuItem? = null
    private var jMenuItemUsers: JMenuItem? = null
    private var jMenuItemLogout: JMenuItem? = null
    private var jMenuItemChangePassword: JMenuItem? = null
    private var jMenuItemCreateLabels: JMenuItem? = null
    private var jMenuItemReconcileFiles: JMenuItem? = null
    private var jMenuItemStats: JMenuItem? = null
    private var jMenuItemRepeatOCR: JMenuItem? = null
    private var jMenuItemListRunningJobs: JMenuItem? = null
    private var jMenuItemRedoOCROne: JMenuItem? = null
    private var jMenuItemCleanupDirectory: JMenuItem? = null
    private var jMenuItemRecheckTemplates: JMenuItem? = null
    private var jMenuItemRecheckAllTemplates: JMenuItem? = null
    override fun setState(aState: Int) {
        when (aState) {
            STATE_INIT ->  // can't return to state_init.
                if (state == STATE_INIT) { // do initial setup, disable most menus
                    jMenuItemLog.setEnabled(false)
                    jMenuEdit.setEnabled(false)
                    jMenuAction.setEnabled(false)
                    jMenuConfig.setEnabled(false)
                    jMenuData.setEnabled(false)
                    jMenuQualityControl.setEnabled(false)
                    jMenuItemUsers.setEnabled(false)
                    jMenuItemChangePassword.setEnabled(false)
                    jMenuItemStats.setEnabled(false)
                }
            STATE_RESET -> {
                // state when user logs out.
                jMenuEdit.setEnabled(true)
                jMenuHelp.setEnabled(true)
                jMenuItemLogout.setEnabled(true)
                // disable all but edit/help menus
                jMenuItemLog.setEnabled(false)
                jMenuAction.setEnabled(false)
                jMenuConfig.setEnabled(false)
                jMenuData.setEnabled(false)
                jMenuQualityControl.setEnabled(false)
                jMenuItemUsers.setEnabled(false)
                jMenuItemChangePassword.setEnabled(false)
                // disable stats item on help menu
                jMenuItemStats.setEnabled(false)
            }
            STATE_RUNNING -> {
                if (state == STATE_INIT) {
                    state = STATE_RUNNING
                    jMenuItemLog.setEnabled(true)
                    jMenuEdit.setEnabled(true)
                    activateMenuItemsByUser()
                }
                if (state == STATE_RUNNING) {
                    activateMenuItemsByUser()
                }
            }
        }
    }

    /**
     * Enable/disable menu items based on current user
     * and their login state.
     */
    private fun activateMenuItemsByUser() { // ***********************************************************************************************
// ********* Important bit: This is where user rights are actually applied.     ******************
// ********* Other than limits on editing/creating users, this only place where ******************
// ********* the application level users and rights are controlled.             ******************
// ***********************************************************************************************
// Disable some menu items if user canceled login dialog.
        if (Singleton.getUser() == null) {
            jMenuData.setEnabled(false)
            jMenuItemChangePassword.setEnabled(false)
            jMenuItemPreferences.setEnabled(false)
            jMenuItemPreprocess.setEnabled(false)
            jMenuItemLoadData.setEnabled(false)
            jMenuItemPreprocessOneDir.setEnabled(false)
            jMenuItemCreateLabels.setEnabled(false)
            jMenuItemStats.setEnabled(false)
            jMenuItemLog.setEnabled(false)
        } else { // Anyone authenticated user can change their own password.
            jMenuConfig.setEnabled(true)
            jMenuItemChangePassword.setEnabled(true)
            // Set levels for data entry personnel.
            jMenuData.setEnabled(true)
            jMenuAction.setEnabled(false)
            jMenuItemUsers.setEnabled(false)
            jMenuItemPreprocess.setEnabled(false)
            jMenuItemDelete.setEnabled(false)
            jMenuItemLoadData.setEnabled(false)
            jMenuItemPreprocessOneDir.setEnabled(false)
            jMenuItemCreateLabels.setEnabled(true)
            jMenuItemPreferences.setEnabled(false)
            jMenuItemEditTemplates.setEnabled(false)
            jMenuQualityControl.setEnabled(true)
            jMenuItemCheckForABarcode.setEnabled(true)
            jMenuItemQCBarcodes.setEnabled(false)
            jMenuItemSearch.setEnabled(true)
            jMenuItemStats.setEnabled(true)
            jMenuItemLog.setEnabled(true)
            jMenuItemCleanupDirectory.setEnabled(false)
            jMenuItemRecheckTemplates.setEnabled(false)
            jMenuItemRecheckAllTemplates.setEnabled(false)
            try { // Enable some menu items only for administrators.
                if (UsersLifeCycle.Companion.isUserAdministrator(Singleton.getUser().getUserid())) { //jMenuItemUsers.setEnabled(true);
                    jMenuItemPreprocess.setEnabled(true)
                }
                // User privileges and some other items to the chief editor.
                if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.getUser().getUserid())) {
                    jMenuItemUsers.setEnabled(true)
                    jMenuItemEditTemplates.setEnabled(true)
                    jMenuItemLoadData.setEnabled(true)
                    jMenuItemCleanupDirectory.setEnabled(true)
                }
                // Enable other menu items only for those with full access rights
// Administrator and full roles both have full access rights
                if (Singleton.getUser().isUserRole(Users.Companion.ROLE_FULL)) {
                    jMenuAction.setEnabled(true)
                    jMenuItemPreprocessOneDir.setEnabled(true)
                    jMenuConfig.setEnabled(true)
                    jMenuItemDelete.setEnabled(true)
                    jMenuItemPreferences.setEnabled(true)
                    jMenuQualityControl.setEnabled(true)
                    jMenuItemQCBarcodes.setEnabled(true)
                    jMenuItemRecheckTemplates.setEnabled(true)
                    jMenuItemRecheckAllTemplates.setEnabled(true)
                }
            } catch (e: Exception) { // catch any problem with testing administration or user rights and do nothing.
            }
        }
    }

    /**
     * This method initializes the main frame.
     */
    private fun initialize() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        this.setSize(Dimension(1280, 750))
        this.setPreferredSize(Dimension(1280, 800))
        this.setMinimumSize(Dimension(300, 200))
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
        //String iconFile = this.getClass().getResource("resources/icon.ico").getFile();
        val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/icon.png")
        //loading an image from a file
        val defaultToolkit: Toolkit = Toolkit.getDefaultToolkit()
        val image: Image = defaultToolkit.getImage(iconFile)
        //this is new since JDK 9
        val taskbar: Taskbar = Taskbar.getTaskbar()
        try { //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(image)
        } catch (e: UnsupportedOperationException) {
            println("The os does not support: 'taskbar.setIconImage'")
        } catch (e: SecurityException) {
            println("There was a security exception for: 'taskbar.setIconImage'")
        }
        //set icon for windows os (and other systems which do support this method)
        this.setIconImage(image)
        try {
            setIconImage(ImageIcon(iconFile).getImage())
        } catch (e: Exception) {
            log.error("Can't open icon file: $iconFile")
            log.error(e)
        }
        this.setTitle(ImageCaptureApp.APP_NAME + ": MCZ Rapid Data Capture Application.  Configured For: " +
                Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
        )
        this.setJMenuBar(getJJMenuBar())
        this.setContentPane(getJPanel())
    }

    protected fun updateTitle() {
        this.setTitle(ImageCaptureApp.APP_NAME + ": MCZ Rapid Data Capture Application.  Configured For: " +
                Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
        )
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private fun getJJMenuBar(): JMenuBar? {
        if (jJMenuBar == null) {
            jJMenuBar = JMenuBar()
            jJMenuBar.add(getJMenuFile())
            jJMenuBar.add(getJMenuEdit())
            jJMenuBar.add(getJMenuAction())
            jJMenuBar.add(getJMenuData())
            jJMenuBar.add(getJMenuQualityControl())
            jJMenuBar.add(getJMenuConfig())
            jJMenuBar.add(getJMenuHelp())
        }
        return jJMenuBar
    }

    /**
     * This method initializes jMenuFile
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuFile(): JMenu? {
        if (jMenuFile == null) {
            jMenuFile = JMenu()
            jMenuFile.setText("File")
            jMenuFile.setMnemonic(KeyEvent.VK_F)
            jMenuFile.add(getJMenuItemLog())
            jMenuFile.add(getJMenuItemLogout())
            jMenuFile.add(getJMenuItemExit())
        }
        return jMenuFile
    }

    /**
     * This method initializes jMenuItemExit
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemExit(): JMenuItem? {
        if (jMenuItemExit == null) {
            jMenuItemExit = JMenuItem()
            jMenuItemExit.setText("Exit")
            jMenuItemExit.setMnemonic(KeyEvent.VK_E)
            jMenuItemExit.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    ImageCaptureApp.cleanUp()
                    println("Exit by user from main menu.")
                    ImageCaptureApp.exit(ImageCaptureApp.EXIT_NORMAL)
                }
            })
        }
        return jMenuItemExit
    }

    /**
     * This method initializes jMenuItemLogout
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemLogout(): JMenuItem? {
        if (jMenuItemLogout == null) {
            jMenuItemLogout = JMenuItem()
            jMenuItemLogout.setText("Logout & change user")
            jMenuItemLogout.setMnemonic(KeyEvent.VK_U)
            jMenuItemLogout.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // remove the current user's browse (important if this is a userbrowse by an
// administrator.
                    jPanelCenter.removeAll()
                    var oldUser = "anon"
                    try {
                        oldUser = Singleton.getUserFullName()
                    } catch (ex: NullPointerException) { // no one was logged in
                    }
                    setState(STATE_RESET)
                    Singleton.unsetCurrentUser()
                    HibernateUtil.terminateSessionFactory()
                    Singleton.getMainFrame().setStatusMessage("Logged out $oldUser")
                    // Force a login dialog by connecting to obtain record count;
                    val sls = SpecimenLifeCycle()
                    try {
                        setCount(sls.findSpecimenCountThrows())
                        ImageCaptureApp.doStartUp()
                    } catch (e1: ConnectionException) {
                        log.error(e1.message, e1)
                        ImageCaptureApp.doStartUpNot()
                    }
                }
            })
        }
        return jMenuItemLogout
    }

    /**
     * This method initializes jMenuAction
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuAction(): JMenu? {
        if (jMenuAction == null) {
            jMenuAction = JMenu()
            jMenuAction.setText("Action")
            jMenuAction.setMnemonic(KeyEvent.VK_A)
            jMenuAction.add(getJMenuItemScanOneBarcodeSave())
            jMenuAction.add(getJMenuItemPreprocess())
            jMenuAction.add(jMenuItemPreprocessOne)
            jMenuAction.add(getJMenuItemDelete())
            jMenuAction.add(getJMenuItemFindMissingImages())
            jMenuAction.add(getJMenuItemRedoOCROne())
            jMenuAction.add(getJMenuItemRepeatOCR())
            jMenuAction.add(getJMenuItemRecheckTemplates())
            jMenuAction.add(getJMenuItemRecheckAllTemplates())
            jMenuAction.add(getJMenuItemScanOneBarcode())
            jMenuAction.add(getJMenuItemCleanupDirectory())
            jMenuAction.add(getJMenuItemLoadData())
            jMenuAction.add(getJMenuItemListRunningJobs())
            jMenuAction.add(getJMenuItemCreateLabels())
        }
        return jMenuAction
    }

    /**
     * This method initializes jMenuHelp
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuHelp(): JMenu? {
        if (jMenuHelp == null) {
            jMenuHelp = JMenu()
            jMenuHelp.setText("Help")
            jMenuHelp.setMnemonic(KeyEvent.VK_H)
            jMenuHelp.add(getJMenuItemAbout())
            jMenuHelp.addSeparator()
            jMenuHelp.add(jMenuItem)
            jMenuHelp.add(getJMenuItemVersion())
        }
        return jMenuHelp
    }

    /**
     * This method initializes jMenuItemAbout
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemAbout(): JMenuItem? {
        if (jMenuItemAbout == null) {
            jMenuItemAbout = JMenuItem()
            jMenuItemAbout.setText("About")
            jMenuItemAbout.setMnemonic(KeyEvent.VK_B)
            jMenuItemAbout.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val a = AboutDialog()
                    a.pack()
                    a.setVisible(true)
                }
            })
        }
        return jMenuItemAbout
    }

    /**
     * This method initializes jMenuItemPreprocess: the menu item to start preprocessing all
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPreprocess(): JMenuItem? {
        if (jMenuItemPreprocess == null) {
            jMenuItemPreprocess = JMenuItem()
            jMenuItemPreprocess.setText("Preprocess All")
            jMenuItemPreprocess.setEnabled(true)
            try {
                jMenuItemPreprocess.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemPreprocess.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val result: Int = JOptionPane.showConfirmDialog(Singleton.getMainFrame(), "Are you sure, this will check all image files and may take some time.", "Preprocess All?", JOptionPane.YES_NO_OPTION)
                    if (result == JOptionPane.YES_OPTION) {
                        val scan = JobAllImageFilesScan()
                        Thread(scan).start()
                    } else {
                        Singleton.getMainFrame().setStatusMessage("Preprocess canceled.")
                    }
                }
            })
        }
        return jMenuItemPreprocess
    }

    /**
     * This method initializes the jMenuItem to delete a specimen record
     *
     * @return
     */
    private fun getJMenuItemDelete(): JMenuItem? {
        if (jMenuItemDelete == null) {
            jMenuItemDelete = JMenuItem()
            jMenuItemDelete.setText("Delete a specimen record")
            jMenuItemDelete.setEnabled(true)
            try {
                jMenuItemDelete.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/red-warning-icon.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemDelete.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val barcode = JTextField()
                    val inputs: Array<JComponent?> = arrayOf<JComponent?>(
                            JLabel("<html>WARNING: ACTION IRREVERSIBLE!<br/><br/>Is there only one image associated with this record?<br/>Have you recorded the image file number/s and date imaged?<br/>Have you recorded the genus and species name?<br/><br/>Please enter the barcode of the specimen record you would like to delete:<br/>"),
                            barcode
                    )
                    val result: Int = JOptionPane.showConfirmDialog(null, inputs, "Delete a specimen record", JOptionPane.CANCEL_OPTION)
                    if (result == JOptionPane.YES_OPTION) {
                        val barcodeEntered: String = barcode.getText()
                        val sls = SpecimenLifeCycle()
                        val delete_result: Int = sls.deleteSpecimenByBarcode(barcodeEntered)
                        if (delete_result == 0) {
                            JOptionPane.showConfirmDialog(null, "Error: specimen record not found.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        } else if (delete_result == 1) {
                            JOptionPane.showConfirmDialog(null, "Specimen has been deleted successfully.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        } else {
                            JOptionPane.showConfirmDialog(null, "Error: delete failed.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        }
                    }
                }
            })
        }
        return jMenuItemDelete
    }

    /**
     * This method initializes the jMenuItem to load a verbatim field/data
     *
     * @return
     */
    private fun getJMenuItemLoadData(): JMenuItem? {
        if (jMenuItemLoadData == null) {
            jMenuItemLoadData = JMenuItem()
            jMenuItemLoadData.setText("Load Data")
            try {
                jMenuItemLoadData.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/cycle_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckAllTemplates.")
                log.error(e)
            }
            jMenuItemLoadData.setEnabled(true)
            jMenuItemLoadData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //TODO: Launch FieldLoaderWizard and have it launch job.
                    val scan = JobVerbatimFieldLoad()
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemLoadData
    }

    /**
     * This method initializes jMenuItemVersion
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemVersion(): JMenuItem? {
        if (jMenuItemVersion == null) {
            jMenuItemVersion = JMenuItem(ImageCaptureApp.APP_NAME + " Ver: " + ImageCaptureApp.getAppVersion())
            jMenuItemVersion.setEnabled(false)
        }
        return jMenuItemVersion
    }

    /**
     * This method initializes jMenuItemScanOneBarcode
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemScanOneBarcode(): JMenuItem? {
        if (jMenuItemScanOneBarcode == null) {
            jMenuItemScanOneBarcode = JMenuItem()
            jMenuItemScanOneBarcode.setText("Scan A File For Barcodes")
            jMenuItemScanOneBarcode.setMnemonic(KeyEvent.VK_S)
            try {
                jMenuItemScanOneBarcode.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                println("Can't open icon file for jMenuItemScanOneBarcode.")
                println(e.localizedMessage)
            }
            jMenuItemScanOneBarcode.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val scan = JobSingleBarcodeScan(false)
                    scan.registerListener(thisMainFrame)
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemScanOneBarcode
    }

    /**
     * This method initializes jMenuItemScanOneBarcodeSave
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemScanOneBarcodeSave(): JMenuItem? {
        if (jMenuItemScanOneBarcodeSave == null) {
            jMenuItemScanOneBarcodeSave = JMenuItem()
            jMenuItemScanOneBarcodeSave.setMnemonic(KeyEvent.VK_D)
            jMenuItemScanOneBarcodeSave.setText("Database One File")
            try {
                jMenuItemScanOneBarcodeSave.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemScanOneBarcodeSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val scan = JobSingleBarcodeScan(true)
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemScanOneBarcodeSave
    }

    /**
     * This method initializes jMenuConfig
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuConfig(): JMenu? {
        if (jMenuConfig == null) {
            jMenuConfig = JMenu()
            jMenuConfig.setText("Configuration")
            jMenuConfig.setMnemonic(KeyEvent.VK_C)
            jMenuConfig.setEnabled(true)
            jMenuConfig.add(getJMenuItemEditTemplates())
            jMenuConfig.add(getJMenuItemPreferences())
            jMenuConfig.add(getJMenuItemUsers())
            jMenuConfig.add(getJMenuItemChangePassword())
        }
        return jMenuConfig
    }

    /**
     * This method initializes the menu to access the users overview
     *
     * @return
     */
    private fun getJMenuItemUsers(): JMenuItem? {
        if (jMenuItemUsers == null) {
            jMenuItemUsers = JMenuItem()
            jMenuItemUsers.setText("Users")
            jMenuItemUsers.setMnemonic(KeyEvent.VK_U)
            jMenuItemUsers.setEnabled(false)
            try {
                jMenuItemUsers.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/people_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemUsers.")
                log.error(e)
            }
            jMenuItemUsers.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    ulb = UserListBrowser()
                    if (slb != null) {
                        jPanelCenter.remove(slb)
                    }
                    if (ilb != null) {
                        jPanelCenter.remove(ilb)
                    }
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ulb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                }
            })
        }
        return jMenuItemUsers
    }

    /**
     * This method initializes jMenuEdit
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuEdit(): JMenu? {
        if (jMenuEdit == null) {
            jMenuEdit = JMenu()
            jMenuEdit.setText("Edit")
            jMenuEdit.setMnemonic(KeyEvent.VK_E)
            jMenuEdit.add(getJMenuItemCopy())
            jMenuEdit.add(getJMenuItemPaste())
        }
        return jMenuEdit
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPreferences(): JMenuItem? {
        if (jMenuItemPreferences == null) {
            jMenuItemPreferences = JMenuItem()
            jMenuItemPreferences.setText("Preferences")
            jMenuItemPreferences.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val p = PropertiesEditor()
                    p.pack()
                    p.setVisible(true)
                }
            })
        }
        return jMenuItemPreferences
    }

    /**
     * This method initializes jMenuItem1
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemCopy(): JMenuItem? {
        if (jMenuItemCopy == null) {
            jMenuItemCopy = JMenuItem(CopyAction())
            jMenuItemCopy.setText("Copy")
            jMenuItemCopy.setMnemonic(KeyEvent.VK_C)
            jMenuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_C, ActionEvent.CTRL_MASK))
            jMenuItemCopy.setEnabled(true)
        }
        return jMenuItemCopy
    }

    /**
     * This method initializes jMenuItem2
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPaste(): JMenuItem? {
        if (jMenuItemPaste == null) {
            jMenuItemPaste = JMenuItem(PasteAction())
            jMenuItemPaste.setText("Paste")
            jMenuItemPaste.setMnemonic(KeyEvent.VK_P)
            jMenuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_V, ActionEvent.CTRL_MASK))
            jMenuItemPaste.setEnabled(true)
        }
        return jMenuItemPaste
    }

    /**
     * This method initializes jMenuItemLog
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemLog(): JMenuItem? {
        if (jMenuItemLog == null) {
            jMenuItemLog = JMenuItem()
            jMenuItemLog.setText("View History")
            jMenuItemLog.setMnemonic(KeyEvent.VK_H)
            jMenuItemLog.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val logWindow = EventLogFrame()
                    logWindow.pack()
                    logWindow.setVisible(true)
                    System.gc()
                }
            })
        }
        return jMenuItemLog
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel()
            jPanel.setLayout(BorderLayout())
            jPanel.add(getJPanel1(), BorderLayout.SOUTH)
            jPanel.add(getJPanelCenter(), BorderLayout.CENTER)
        }
        return jPanel
    }

    /**
     * This method initializes jProgressBar
     *
     * @return javax.swing.JProgressBar
     */
    private fun getJProgressBar(): JProgressBar? {
        if (jProgressBar == null) {
            jProgressBar = JProgressBar()
        }
        return jProgressBar
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints1.weightx = 4.0
            gridBagConstraints1.gridx = 0
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 1
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.gridy = 0
            jLabelStatus = JLabel()
            jLabelStatus.setText("Status:")
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            jPanel1.add(jLabelStatus, gridBagConstraints1)
            jPanel1.add(getJProgressBar(), gridBagConstraints)
        }
        return jPanel1
    }

    override fun notifyListener(anEvent: Int, notifyingJob: RunnableJob) {
        jProgressBar.setValue(notifyingJob.percentComplete())
        log.debug(notifyingJob.percentComplete())
        jProgressBar.validate()
    }

    /**
     * Sets the message on the status bar with an up to 60 character string.
     *
     * @param aMessage the message to display on the status bar.
     */
    fun setStatusMessage(aMessage: String) {
        var maxLength = 100
        if (aMessage.length < maxLength) {
            maxLength = aMessage.length
        }
        jLabelStatus.setText("Status: " + aMessage.substring(0, maxLength))
    }

    fun setSpecimenBrowseList(searchCriteria: Specimen?, limit: Int, offset: Int) {
        Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        slb = SpecimenBrowser(searchCriteria, true, limit, offset)
        adaptJpanelToSpecimenBrowser(slb)
        Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
    }

    fun adaptJpanelToSpecimenBrowser(slb: SpecimenBrowser) {
        jPanelCenter.removeAll()
        jPanelCenter.add(slb, BorderLayout.CENTER)
        jPanelCenter.revalidate()
        jPanelCenter.repaint()
        if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseSpecimens.setEnabled(false)
            jMenuItemBrowseImages.setEnabled(false)
        } else {
            jMenuItemBrowseSpecimens.setEnabled(true)
            jMenuItemBrowseImages.setEnabled(true)
        }
        setStatusMessage("Found " + slb.getRowCount() + " matching specimens")
    }

    fun setSpecimenBrowseList(searchCriteria: Specimen?) {
        this.setSpecimenBrowseList(searchCriteria, 0, 0)
    }

    /**
     * This method initializes jMenuItemBrowseSpecimens
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemBrowseSpecimens(): JMenuItem? {
        if (jMenuItemBrowseSpecimens == null) {
            jMenuItemBrowseSpecimens = JMenuItem()
            jMenuItemBrowseSpecimens.setText("Browse Specimens")
            jMenuItemBrowseSpecimens.setMnemonic(KeyEvent.VK_B)
            try {
                jMenuItemBrowseSpecimens.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/butterfly_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemBrowseImages.")
                log.error(e)
            }
            jMenuItemBrowseSpecimens.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // Create a SpecimenBrowser jpanel and replace the
// the content of the center of jPanelCenter with it.
//TODO: extend beyond switching between ilb and slb.
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    slb = SpecimenBrowser()
                    adaptJpanelToSpecimenBrowser(slb)
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    System.gc()
                }
            })
        }
        if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseSpecimens.setEnabled(false)
        }
        return jMenuItemBrowseSpecimens
    }

    /**
     * This method initializes jPanelCenter
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelCenter(): JPanel? {
        if (jPanelCenter == null) {
            jLabelCount = JLabel()
            jLabelCount.setText("")
            jPanelCenter = JPanel()
            jPanelCenter.setLayout(BorderLayout())
            jPanelCenter.add(jLabelCount, BorderLayout.SOUTH)
        }
        return jPanelCenter
    }

    fun setCount(recordCountText: String?) {
        if (jLabelCount != null) {
            jLabelCount.setText(recordCountText)
        }
    }

    /**
     * This method initializes jMenuItem3
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemEditTemplates(): JMenuItem? {
        if (jMenuItemEditTemplates == null) {
            jMenuItemEditTemplates = JMenuItem()
            jMenuItemEditTemplates.setText("Edit Templates")
            jMenuItemEditTemplates.setMnemonic(KeyEvent.VK_T)
            jMenuItemEditTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val templateEditor = PositionTemplateEditor()
                    templateEditor.pack()
                    templateEditor.setVisible(true)
                }
            })
        }
        return jMenuItemEditTemplates
    }

    /**
     * This method initializes jMenuItemBrowseImages
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemBrowseImages(): JMenuItem? {
        if (jMenuItemBrowseImages == null) {
            jMenuItemBrowseImages = JMenuItem()
            jMenuItemBrowseImages.setText("Browse Image Files")
            jMenuItemBrowseImages.setMnemonic(KeyEvent.VK_I)
            try {
                jMenuItemBrowseImages.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/image_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemBrowseImages.")
                log.error(e)
            }
            jMenuItemBrowseImages.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // Create a ImageListBrowser jpanel and replace the
// the content of the center of jPanelCenter with it.
//TODO: extend beyond switching between ilb and slb.
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    ilb = ImageListBrowser()
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ilb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
                        jMenuItemBrowseSpecimens.setEnabled(false)
                        jMenuItemBrowseImages.setEnabled(false)
                    } else {
                        jMenuItemBrowseSpecimens.setEnabled(true)
                        jMenuItemBrowseImages.setEnabled(true)
                    }
                    setStatusMessage("Found " + ilb.getRowCount() + " images.")
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    System.gc()
                }
            })
        }
        if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseImages.setEnabled(false)
        }
        return jMenuItemBrowseImages
    }

    /**
     * Update UI elements to reflect change in properties.
     */
    fun updateForPropertiesChange() {
        updateTitle()
        jMenuItemPreprocessOneDir.setText("Preprocess A Directory (as " + LocationInCollection.getDefaultLocation() + ")")
    }

    /**
     * This method initializes jMenuItemPreprocessOneDir: menu to preprocess one directory
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemPreprocessOne: JMenuItem?
        private get() {
            if (jMenuItemPreprocessOneDir == null) {
                jMenuItemPreprocessOneDir = JMenuItem()
                jMenuItemPreprocessOneDir.setText("Preprocess A Directory (as " + LocationInCollection.getDefaultLocation() + ")")
                jMenuItemPreprocessOneDir.setMnemonic(KeyEvent.VK_P)
                jMenuItemPreprocessOneDir.setEnabled(true)
                try {
                    jMenuItemPreprocessOneDir.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
                } catch (e: Exception) {
                    log.error("Can't open icon file for getJMenuItemPreprocessOne.")
                    log.error(e.localizedMessage)
                }
                jMenuItemPreprocessOneDir.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val scan = JobAllImageFilesScan(
                                JobAllImageFilesScan.Companion.SCAN_SELECT,
                                File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                        )
                        Thread(scan).start()
                    }
                })
            }
            return jMenuItemPreprocessOneDir
        }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuData(): JMenu? {
        if (jMenuData == null) {
            jMenuData = JMenu()
            jMenuData.setText("Data")
            jMenuData.setMnemonic(KeyEvent.VK_D)
            jMenuData.add(getJMenuItemSearch())
            jMenuData.add(getJMenuItemVerbatimTranscription())
            jMenuData.add(getJMenuItemVerbatimClassification())
            jMenuData.add(getJMenuItemBrowseImages())
            jMenuData.add(getJMenuItemBrowseSpecimens())
        }
        return jMenuData
    }

    /**
     * This method initializes jMenu1
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuQualityControl(): JMenu? {
        if (jMenuQualityControl == null) {
            jMenuQualityControl = JMenu()
            jMenuQualityControl.setText("QualityControl")
            jMenuQualityControl.setMnemonic(KeyEvent.VK_Q)
            jMenuQualityControl.add(jMenuItemValidateImageNoDB)
            jMenuQualityControl.add(getJMenuItemQCBarcodes())
            jMenuQualityControl.add(getJMenuItemReconcileFiles())
        }
        return jMenuQualityControl
    }

    private fun getJMenuItemVerbatimTranscription(): JMenuItem? {
        if (jMenuItemVerbatimTranscription == null) {
            jMenuItemVerbatimTranscription = JMenuItem()
            jMenuItemVerbatimTranscription.setText("Verbatim Transcription")
            jMenuItemVerbatimTranscription.setEnabled(true)
            jMenuItemVerbatimTranscription.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = VerbatimToTranscribeDialog()
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemVerbatimTranscription
    }

    private fun getJMenuItemVerbatimClassification(): JMenuItem? {
        if (jMenuItemVerbatimClassification == null) {
            jMenuItemVerbatimClassification = JMenuItem()
            jMenuItemVerbatimClassification.setText("Fill in from Verbatim")
            jMenuItemVerbatimClassification.setEnabled(true)
            jMenuItemVerbatimClassification.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = VerbatimListDialog()
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemVerbatimClassification
    }

    /**
     * This method initializes jMenuItem4
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemValidateImageNoDB: JMenuItem?
        private get() {
            if (jMenuItemCheckForABarcode == null) {
                jMenuItemCheckForABarcode = JMenuItem()
                jMenuItemCheckForABarcode.setText("Preprocess One Image File")
                jMenuItemCheckForABarcode.setMnemonic(KeyEvent.VK_C)
                try {
                    jMenuItemCheckForABarcode.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
                } catch (e: Exception) {
                    log.error("Can't open icon file for jMenuItemCheckForABarcode.")
                    log.error(e)
                }
                jMenuItemCheckForABarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val scan = JobSingleBarcodeScan(false)
                        scan.registerListener(thisMainFrame)
                        Thread(scan).start()
                    }
                })
            }
            return jMenuItemCheckForABarcode
        }

    private fun getJMenuItemReconcileFiles(): JMenuItem? {
        if (jMenuItemReconcileFiles == null) {
            jMenuItemReconcileFiles = JMenuItem()
            jMenuItemReconcileFiles.setText("Reconcile image files with database")
            jMenuItemReconcileFiles.setMnemonic(KeyEvent.VK_R)
            jMenuItemReconcileFiles.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobFileReconciliation()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemReconcileFiles
    }

    /**
     * This method initializes jMenuItem5
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemQCBarcodes(): JMenuItem? {
        if (jMenuItemQCBarcodes == null) {
            jMenuItemQCBarcodes = JMenuItem()
            jMenuItemQCBarcodes.setText("QC Barcodes")
            jMenuItemQCBarcodes.setMnemonic(KeyEvent.VK_B)
            jMenuItemQCBarcodes.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    Singleton.getMainFrame().setStatusMessage("Running barcode QC checks")
                    val missingBarcodes: Array<String?> = SpecimenLifeCycle.Companion.getMissingBarcodes()
                    ilb = ImageListBrowser(true)
                    if (slb != null) {
                        jPanelCenter.remove(slb)
                    }
                    if (ulb != null) {
                        jPanelCenter.remove(ulb)
                    }
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ilb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    Singleton.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    log.debug(missingBarcodes.size)
                    if (missingBarcodes.size > 0) {
                        val errorCount = Counter()
                        for (i in missingBarcodes.indices) {
                            val builder: BarcodeBuilder = Singleton.getBarcodeBuilder()
                            val matcher: BarcodeMatcher = Singleton.getBarcodeMatcher()
                            val previous: String = builder.makeFromNumber(matcher.extractNumber(missingBarcodes[i]) - 1)
                            var previousFile = ""
                            var previousPath = ""
                            val sls = SpecimenLifeCycle()
                            val result: MutableList<Specimen?> = sls.findByBarcode(previous)
                            if (result != null && !result.isEmpty()) {
                                val images: MutableSet<ICImage?> = result[0].getICImages()
                                if (images != null && !images.isEmpty()) {
                                    val it: MutableIterator<ICImage?> = images.iterator()
                                    if (it.hasNext()) {
                                        val image: ICImage? = it.next()
                                        previousFile = image.getFilename()
                                        previousPath = image.getPath()
                                    }
                                }
                            }
                            val err = RunnableJobError(previousFile, missingBarcodes[i], previousPath, "", "Barcode not found", null, null, null,
                                    RunnableJobError.Companion.TYPE_BARCODE_MISSING_FROM_SEQUENCE,
                                    previousFile,
                                    previousPath)
                            errorCount.appendError(err)
                        }
                        val report = "There are at least " + missingBarcodes.size + " barcodes missing from the sequence.\nMissing numbers are shown below.\nIf two or more numbers are missing in sequence, only the first will be listed here.\n\nFiles with mismmatched barcodes are shown in main window.\n"
                        val errorReportDialog = RunnableJobReportDialog(
                                Singleton.getMainFrame(),
                                report,
                                errorCount.getErrors(),
                                RunnableJobErrorTableModel.Companion.TYPE_MISSING_BARCODES,
                                "QC Barcodes Report")
                        errorReportDialog.setVisible(true)
                    } else {
                        JOptionPane.showMessageDialog(Singleton.getMainFrame(), "No barcodes are missing from the sequence.\nAny missmatches are shown in table.", "Barcode QC Report", JOptionPane.OK_OPTION)
                    }
                    System.gc()
                }
            })
        }
        return jMenuItemQCBarcodes
    }

    /**
     * This method initializes jMenuItem6
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemSearch(): JMenuItem? {
        if (jMenuItemSearch == null) {
            jMenuItemSearch = JMenuItem()
            jMenuItemSearch.setText("Search")
            jMenuItemSearch.setEnabled(true)
            jMenuItemSearch.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = SearchDialog(thisMainFrame)
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemSearch
    }

    private fun getJMenuItemFindMissingImages(): JMenuItem? {
        if (jMenuItemFindMissingImages == null) {
            jMenuItemFindMissingImages = JMenuItem()
            jMenuItemFindMissingImages.setText("Find Missing Images")
            jMenuItemFindMissingImages.setEnabled(true)
            /*try {
				jMenuItemFindMissingImages.setIcon(new ImageIcon(this.getClass().getResource("/edu/harvard/mcz/imagecapture/resources/images/red-warning-icon.png")));
			} catch (Exception e) {
				log.error("Can't open icon file for jMenuItemScanOneBarcode.");
				log.error(e.getLocalizedMessage());
			}*/jMenuItemFindMissingImages.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //JTextField dateimaged = new JTextField();
                    val sls2 = SpecimenLifeCycle()
                    val paths: Array<String?> = sls2.getDistinctPaths()
                    //log.debug("num paths" + paths.length);
                    val pathCombo: JComboBox<String?> = JComboBox<String?>(paths)
                    pathCombo.setEditable(true)
                    val inputs: Array<JComponent?> = arrayOf<JComponent?>(
                            JLabel("<html>Find missing images<br/><br/>Please select the date imaged:<br/>"),
                            pathCombo
                    )
                    val result: Int = JOptionPane.showConfirmDialog(null, inputs, "Find missing images", JOptionPane.CANCEL_OPTION)
                    //
                    if (result == JOptionPane.YES_OPTION) { //String dateEntered = dateimaged.getText();
                        val dateEntered: String = pathCombo.getSelectedItem().toString()
                        //dateEntered = dateEntered.replaceAll("\\", "\\\\");
//System.out.println("BEFORE DATE ENTERED IS " + dateEntered);
//dateEntered = dateEntered.replaceAll("\\", "\\\\");
                        println("DATE ENTERED IS $dateEntered")
                        val sls = SpecimenLifeCycle()
                        val results: MutableList<ICImage?> = sls.findImagesByPath(dateEntered)
                        val seqvals: ArrayList<Int?> = ArrayList<Any?>()
                        val missingvals: ArrayList<Int?> = ArrayList<Any?>()
                        var img_prefix = ""
                        for (im in results) {
                            val last_underscore: Int = im.getFilename().lastIndexOf("_")
                            img_prefix = im.getFilename().substring(0, last_underscore)
                            val dot: Int = im.getFilename().indexOf(".")
                            val seqnum: String = im.getFilename().substring(last_underscore + 1, dot)
                            //log.debug("seqnum: " + seqnum);
                            try {
                                val seqint = seqnum.toInt()
                                seqvals.add(seqint)
                                //log.debug("seqint: " +seqint);
                            } catch (e1: Exception) {
                            }
                        }
                        for (i in seqvals.indices) {
                            var current = seqvals[i]
                            if (i + 1 < seqvals.size) {
                                val next = seqvals[i + 1]
                                if (next!! - current!! != 1) {
                                    val gap = next - current
                                    for (j in 1 until gap) {
                                        missingvals.add(current + j)
                                    }
                                }
                                current = next
                            }
                        }
                        val sb = StringBuilder()
                        /*for(Integer cint : missingvals){
							if(cint < 10000){
								sb.append("ETHZ_ENT01_2017_03_15_00");
							}else if(cint < 100000){
								sb.append("ETHZ_ENT01_2017_03_15_0");
							}else if(cint < 1000000){
								sb.append("ETHZ_ENT01_2017_03_15_");
							}
							sb.append(cint);
							sb.append(".JPG");
							sb.append("\n");
						}*/for (cint in missingvals) {
                            sb.append(img_prefix)
                            sb.append(cint)
                            sb.append(".JPG")
                            sb.append("\n")
                        }
                        /*for(ICImage im : results){
							sb.append(im.getFilename());
							sb.append("\n");
						}*/JOptionPane.showConfirmDialog(null, "Found " + results.size + " images in the database for the date " + dateEntered + ".\n\nPossible missing images:\n" + sb.toString(), "Find missing images", JOptionPane.PLAIN_MESSAGE)
                    }
                }
            })
        }
        return jMenuItemFindMissingImages
    }

    /**
     * This method initializes jMenuItemChangePassword
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemChangePassword(): JMenuItem? {
        if (jMenuItemChangePassword == null) {
            jMenuItemChangePassword = JMenuItem()
            jMenuItemChangePassword.setText("Change My Password")
            jMenuItemChangePassword.setMnemonic(KeyEvent.VK_M)
            try {
                jMenuItemChangePassword.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/key_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemChangePassword.")
                log.error(e)
            }
            jMenuItemChangePassword.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (Singleton.getUser() != null) {
                        val cpd = ChangePasswordDialog(thisMainFrame, Singleton.getUser())
                        cpd.setVisible(true)
                    }
                }
            })
        }
        return jMenuItemChangePassword
    }

    /**
     * This method initializes jMenuItemCreateLabels
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemCreateLabels(): JMenuItem? {
        if (jMenuItemCreateLabels == null) {
            jMenuItemCreateLabels = JMenuItem()
            jMenuItemCreateLabels.setText("Create Unit Tray Labels")
            jMenuItemCreateLabels.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val lb = UnitTrayLabelBrowser()
                    lb.pack()
                    lb.setVisible(true)
                }
            })
        }
        return jMenuItemCreateLabels
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItem: JMenuItem?
        private get() {
            if (jMenuItemStats == null) {
                jMenuItemStats = JMenuItem()
                jMenuItemStats.setText("Statistics")
                jMenuItemStats.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val sls = SpecimenLifeCycle()
                        JOptionPane.showMessageDialog(Singleton.getMainFrame(),
                                sls.findSpecimenCount(),
                                "Record counts",
                                JOptionPane.INFORMATION_MESSAGE)
                    }
                })
            }
            return jMenuItemStats
        }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemRepeatOCR(): JMenuItem? {
        if (jMenuItemRepeatOCR == null) {
            jMenuItemRepeatOCR = JMenuItem()
            jMenuItemRepeatOCR.setText("Redo OCR for All")
            try {
                jMenuItemRepeatOCR.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRepeatOCR.")
                log.error(e)
            }
            jMenuItemRepeatOCR.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRepeatOCR()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRepeatOCR
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemListRunningJobs(): JMenuItem? {
        if (jMenuItemListRunningJobs == null) {
            jMenuItemListRunningJobs = JMenuItem()
            jMenuItemListRunningJobs.setText("List Running Jobs")
            jMenuItemListRunningJobs.setMnemonic(KeyEvent.VK_L)
            try {
                jMenuItemListRunningJobs.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/tools_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemListRunningJobs.")
                log.error(e)
            }
            jMenuItemListRunningJobs.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val jobFrame = RunnableJobFrame()
                    jobFrame.setVisible(true)
                }
            })
        }
        return jMenuItemListRunningJobs
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemRedoOCROne(): JMenuItem? {
        if (jMenuItemRedoOCROne == null) {
            jMenuItemRedoOCROne = JMenuItem()
            jMenuItemRedoOCROne.setText("Redo OCR for A Directory")
            jMenuItemRedoOCROne.setMnemonic(KeyEvent.VK_R)
            try {
                jMenuItemRedoOCROne.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRedoOCROne.")
                log.error(e)
            }
            jMenuItemRedoOCROne.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val target = File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    val r = JobRepeatOCR(JobRepeatOCR.Companion.SCAN_SELECT, target)
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRedoOCROne
    }

    private fun getJMenuItemCleanupDirectory(): JMenuItem? {
        if (jMenuItemCleanupDirectory == null) {
            jMenuItemCleanupDirectory = JMenuItem()
            jMenuItemCleanupDirectory.setText("Cleanup Deleted Images")
            try {
                jMenuItemCleanupDirectory.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/bb_trsh_icon_16px.png")))
            } catch (e: Exception) {
                println("Can't open icon file for jMenuItemCleanupDirectory.")
                println(e.localizedMessage)
            }
            jMenuItemCleanupDirectory.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobCleanDirectory(
                            JobCleanDirectory.Companion.SCAN_SELECT,
                            File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    )
                    Thread(r).start()
                }
            })
        }
        return jMenuItemCleanupDirectory
    }

    private fun getJMenuItemRecheckTemplates(): JMenuItem? {
        if (jMenuItemRecheckTemplates == null) {
            jMenuItemRecheckTemplates = JMenuItem()
            jMenuItemRecheckTemplates.setText("Recheck cases of WholeImageOnly")
            jMenuItemRecheckTemplates.setMnemonic(KeyEvent.VK_W)
            try {
                jMenuItemRecheckTemplates.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckTemplates.")
                log.error(e)
            }
            jMenuItemRecheckTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRecheckForTemplates(
                            JobRecheckForTemplates.Companion.SCAN_SELECT,
                            File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    )
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRecheckTemplates
    }

    private fun getJMenuItemRecheckAllTemplates(): JMenuItem? {
        if (jMenuItemRecheckAllTemplates == null) {
            jMenuItemRecheckAllTemplates = JMenuItem()
            jMenuItemRecheckAllTemplates.setText("Recheck All cases of WholeImageOnly")
            try {
                jMenuItemRecheckAllTemplates.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckAllTemplates.")
                log.error(e)
            }
            jMenuItemRecheckAllTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRecheckForTemplates()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRecheckAllTemplates
    }

    companion object {
        const val STATE_RUNNING = 1 // setup actions complete, menu items enabled.
        const val STATE_RESET = 2 // user logged out, menu items disabled.
        val BG_COLOR_QC_FIELD: Color? = Color(204, 204, 255) // Sun 4 //  @jve:decl-index=0:
        val BG_COLOR_ENT_FIELD: Color? = Color(255, 246, 213) //  @jve:decl-index=0:
        val BG_COLOR_ERROR: Color? = Color(255, 73, 43) // highlight fields with data errors  //  @jve:decl-index=0:
        private const val serialVersionUID = 536567118673854270L
        private val log: Log = LogFactory.getLog(MainFrame::class.java) //  @jve:decl-index=0:
        private const val STATE_INIT = 0 // initial state of application - most menu items disabled
    }

    init {
        thisMainFrame = this
        initialize()
        setState(STATE_INIT)
        this.pack()
        this.setVisible(true)
    }
} //  @jve:decl-index=0:visual-constraint="21,12"
