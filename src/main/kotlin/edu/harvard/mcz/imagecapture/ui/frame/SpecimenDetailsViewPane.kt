/**
 * SpecimenDetailsViewPane.java
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
import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.SpecimenController
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.data.LocationInCollection
import edu.harvard.mcz.imagecapture.data.MetadataRetriever
import edu.harvard.mcz.imagecapture.entity.*
import edu.harvard.mcz.imagecapture.entity.Determination
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import edu.harvard.mcz.imagecapture.entity.fixed.*
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.*
import edu.harvard.mcz.imagecapture.ui.ButtonEditor
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer
import edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListener
import edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditor
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog
import edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBox
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel
import edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModel
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel
import edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtility
import net.miginfocom.swing.MigLayout
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.hibernate.SessionException
import org.hibernate.TransactionException
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.*
import java.net.URL
import java.util.*
import javax.persistence.OptimisticLockException
import javax.swing.*
import javax.swing.table.AbstractTableModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.TableColumn
import kotlin.collections.ArrayList

java.awt.event.KeyEvent
import  java.lang.Exception


import java.net.URL java.util.*import  javax.persistence.OptimisticLockException


import javax.swing.table.TableColumn kotlin.collections.HashSet


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
 * JPanel for editing a record of a Specimen in a details view for that specimen.
 *
 *
 * TODO: BugID: 10 add length limits (remaining to do for Number/Collector tables,
 * and for JComboBox fields).
 */
class SpecimenDetailsViewPane(aSpecimenInstance: Specimen?, aController: SpecimenController?) : JPanel() {
    private var previousSpecimen: Specimen? = null
    private val specimen //  @jve:decl-index=0:
            : Specimen?
    private var specimenController: SpecimenController? = null
    private var state // dirty if data in controls has been changed and not saved to specimen. = 0
    // private JTextField jTextFieldPreparationType = null;
//allie change
//private JTextField jTextFieldCountry = null;
//private JTextField jTextFieldPrimaryDivision = null;
    private val comboBoxHigherGeog: FilteringGeogJComboBox? = null
    private var dateEmergedButton: JButton? = null
    private var dateCollectedButton: JButton? = null
    private var jButtonAddPreparationType: JButton? = null
    private var jButtonCollectorAdd: JButton? = null
    private var jButtonCopy: JButton? = null
    private var jButtonDeterminations: JButton? = null
    private var jButtonGeoReference: JButton? = null
    private var jButtonGetHistory: JButton? = null
    private var jButtonNext: JButton? = null
    private var jButtonNumbersAdd: JButton? = null
    private var jButtonPaste: JButton? = null
    private var jButtonPrevious: JButton? = null
    private var jButtonSave: JButton? = null
    private var jButtonSpecificLocality: JButton? = null
    private var jCheckBoxValidDistributionFlag: JCheckBox? = null
    private var cbTypeStatus: JComboBox<String?>? = null
    private var comboBoxElevUnits: JComboBox<String?>? = null
    private var jCBDeterminer: JComboBox<String?>? = null
    private var jComboBoxCollection: JComboBox<String?>? = null
    private var jComboBoxCountry: JComboBox<String?>? = null
    private var jComboBoxFamily: JComboBox<String?>? = null
    private var jComboBoxFeatures: JComboBox<String?>? = null
    private var jComboBoxLifeStage: JComboBox<String?>? = null
    private var jComboBoxLocationInCollection: JComboBox<String?>? = null
    private var jComboBoxNatureOfId: JComboBox<String?>? = null
    private var jComboBoxPrimaryDivision: JComboBox<String?>? = null
    private var jComboBoxSex: JComboBox<String?>? = null
    private var jComboBoxSubfamily: JComboBox<String?>? = null
    private var jComboBoxWorkflowStatus: JComboBox<String?>? = null
    private var jLabelDBId: JLabel? = null
    private var jPanel: JPanel? = null
    private val jPanel1: JPanel? = null // panel for navigation buttons
    private val jPopupCollectors: JPopupMenu? = null
    private val jPopupNumbers: JPopupMenu? = null
    private val jPopupSpecimenParts: JPopupMenu? = null
    private var jScrollPaneCollectors: JScrollPane? = null
    private var jScrollPaneNotes: JScrollPane? = null
    private var jScrollPaneNumbers: JScrollPane? = null
    private var jScrollPaneSpecimenParts: JScrollPane? = null
    private var jTableCollectors: JTableWithRowBorder? = null
    private var jTableNumbers: JTableWithRowBorder? = null
    private var jTableSpecimenParts: JTableWithRowBorder? = null
    private var jTextAreaSpecimenNotes: JTextArea? = null
    private var jTextFieldAssociatedTaxon: JTextField? = null
    private var jTextFieldAuthorship: JTextField? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jTextFieldCitedInPub: JTextField? = null
    private var jTextFieldCollectingMethod: JTextField? = null
    private var jTextFieldCreator: JTextField? = null
    private var jTextFieldDateCollected: JTextField? = null
    private var jTextFieldDateCollectedIndicator: JTextField? = null
    private var jTextFieldDateCreated: JTextField? = null
    private var jTextFieldDateDetermined: JTextField? = null
    private var jTextFieldDateEmerged: JTextField? = null
    private var jTextFieldDateEmergedIndicator: JTextField? = null
    private var jTextFieldDateLastUpdated: JTextField? = null
    private var jTextFieldDateNos: JTextField? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jTextFieldGenus: JTextField? = null
    private var jTextFieldHabitat: JTextField? = null
    private var jTextFieldISODate: JTextField? = null
    private var jTextFieldIdRemarks: JTextField? = null
    private var jTextFieldImageCount: JTextField? = null
    private var jTextFieldInferences: JTextField? = null
    private var jTextFieldInfraspecificEpithet: JTextField? = null
    private var jTextFieldInfraspecificRank: JTextField? = null
    private var jTextFieldLastUpdatedBy: JTextField? = null
    private var jTextFieldLocality: JTextField? = null
    private var jTextFieldMigrationStatus: JTextField? = null
    private var jTextFieldMinElevation: JTextField? = null
    private var jTextFieldQuestions: JTextField? = null
    private var jTextFieldSpecies: JTextField? = null
    private var jTextFieldStatus: JTextField? = null
    private var jTextFieldSubspecies: JTextField? = null
    private var jTextFieldTribe: JTextField? = null
    private var jTextFieldUnnamedForm: JTextField? = null
    private var jTextFieldVerbatimLocality: JTextField? = null
    private var textFieldMaxElev: JTextField? = null
    private var textFieldMicrohabitat: JTextField? = null
    private var thisPane: SpecimenDetailsViewPane? = null
    private val higherGeogNotFoundWarning = StringBuffer()
    private val clickedOnCollsRow = 0
    private val clickedOnNumsRow = 0
    private val clickedOnPartsRow = 0
    /**
     * Initializes the specimen details view pane.
     * Note, contains comments indicating how to enable visual designer with this class.
     */
    private fun initialize() {
        thisPane = this
        val borderLayout = BorderLayout()
        borderLayout.setHgap(0)
        borderLayout.setVgap(0)
        this.setLayout(borderLayout)
        this.add(getJTextFieldStatus(), BorderLayout.SOUTH)
        // Un-comment this line to use design tool.
//    this.add(getJPanel(), BorderLayout.CENTER);
// Comment this block out to use design tool.
//   see also getCbTypeStatus
        val scrollPane = JScrollPane(getJPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        scrollPane.VerticalScrollBar.setUnitIncrement(16)
        scrollPane.setBorder(BorderFactory.createEmptyBorder())
        this.add(scrollPane, BorderLayout.CENTER)
        this.setMinimumSize(Dimension(100, 100))
    }

    fun setWarning(warning: String?) {
        jTextFieldStatus.setText(warning)
        jTextFieldStatus.setForeground(Color.RED)
    }

    private fun setWarnings() {
        log.debug("In set warnings")
        if (specimen.ICImages != null) {
            log.debug("specimen.getICImages is not null")
            val i: MutableIterator<ICImage?> = specimen.ICImages.iterator()
            log.debug(i.hasNext())
            while (i.hasNext()) {
                log.debug("Checking image $i")
                val im: ICImage? = i.next()
                var rbc = ""
                if (im.RawBarcode != null) {
                    rbc = im.RawBarcode
                }
                var ebc = ""
                if (im.RawExifBarcode != null) {
                    ebc = im.RawExifBarcode
                }
                if (rbc != ebc) { // warn of mismatch, but only if configured to expect both to be present.
                    if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") {
                        setWarning("Warning: An image has mismatch between Comment and Barcode.")
                        log.debug("Setting: Warning: Image has mismatch between Comment and Barcode.")
                    }
                }
            }
        }
        if (higherGeogNotFoundWarning != null && higherGeogNotFoundWarning.length > 0) {
            setWarning(higherGeogNotFoundWarning.toString())
        }
    }

    fun setStatus(status: String) {
        log.info("Setting status to: $status")
        jTextFieldStatus.setText(status)
        jTextFieldStatus.setForeground(Color.BLACK)
    }

    private fun save() {
        try {
            thisPane.Parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        } catch (ex: Exception) {
            log.error(ex)
        }
        try {
            setStatus("Saving")
            if (jTableCollectors.isEditing()) {
                jTableCollectors.CellEditor.stopCellEditing()
            }
            if (jTableSpecimenParts.isEditing()) {
                jTableSpecimenParts.CellEditor.stopCellEditing()
            }
            if (jTableNumbers.isEditing()) {
                jTableNumbers.CellEditor.stopCellEditing()
            }
            if (cbTypeStatus.SelectedIndex == -1 && cbTypeStatus.SelectedItem == null) {
                specimen.setTypeStatus(Specimen.Companion.STATUS_NOT_A_TYPE)
            } else {
                specimen.setTypeStatus(cbTypeStatus.SelectedItem as String)
            }
            specimen.setMicrohabitat(textFieldMicrohabitat.Text)
            if (jComboBoxLocationInCollection.SelectedItem != null) {
                specimen.setLocationInCollection(jComboBoxLocationInCollection.SelectedItem.toString())
            }
            specimen.setDrawerNumber(jTextFieldDrawerNumber.Text)
            if (jComboBoxFamily.SelectedIndex == -1 && jComboBoxFamily.SelectedItem == null) {
                specimen.setFamily("")
            } else {
                specimen.setFamily(jComboBoxFamily.SelectedItem.toString())
            }
            if (jComboBoxSubfamily.SelectedIndex == -1 && jComboBoxSubfamily.SelectedItem == null) {
                specimen.setSubfamily("")
            } else {
                specimen.setSubfamily(jComboBoxSubfamily.SelectedItem.toString())
            }
            specimen.setTribe(jTextFieldTribe.Text)
            specimen.setGenus(jTextFieldGenus.Text)
            specimen.setSpecificEpithet(jTextFieldSpecies.Text)
            specimen.setSubspecificEpithet(jTextFieldSubspecies.Text)
            specimen.setInfraspecificEpithet(jTextFieldInfraspecificEpithet.Text)
            specimen.setInfraspecificRank(jTextFieldInfraspecificRank.Text)
            specimen.setAuthorship(jTextFieldAuthorship.Text)
            //TODO: handle the collectors set!
//this returns TRUE for the copied item!!
            log.debug("in save(). specimen numbers size: " + specimen.Numbers.size)
            log.debug("okok in save(), specimenid is " + specimen.SpecimenId)
            if (previousSpecimen != null && previousSpecimen.Numbers != null) {
                log.debug("in save(). prev specimen numbers size: " + previousSpecimen.Numbers.size)
                //specimen.setNumbers(previousSpecimen.Numbers); - this gives hibernate exceptions here too!
                log.debug("okok in save(), prev specimenid is " + previousSpecimen.SpecimenId)
            }
            specimen.setIdentifiedBy(jCBDeterminer.SelectedItem as String)
            specimen.setDateIdentified(jTextFieldDateDetermined.Text)
            specimen.setIdentificationRemarks(jTextFieldIdRemarks.Text)
            if (jComboBoxNatureOfId.SelectedIndex == -1 && jComboBoxNatureOfId.SelectedItem == null) { //specimen.setNatureOfId(NatureOfId.LEGACY);
                specimen.setNatureOfId(NatureOfId.EXPERT_ID)
            } else {
                specimen.setNatureOfId(jComboBoxNatureOfId.SelectedItem as String)
            }
            specimen.setUnNamedForm(jTextFieldUnnamedForm.Text)
            specimen.setVerbatimLocality(jTextFieldVerbatimLocality.Text)
            specimen.setCountry(jComboBoxCountry.SelectedItem as String)
            specimen.setValidDistributionFlag(jCheckBoxValidDistributionFlag.isSelected())
            specimen.setPrimaryDivison(jComboBoxPrimaryDivision.SelectedItem as String)
            specimen.setSpecificLocality(jTextFieldLocality.Text)
            // Elevations
            val min_elev: Long?
            min_elev = if (jTextFieldMinElevation.Text.trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    jTextFieldMinElevation.Text.toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMinimum_elevation(min_elev)
            val max_elev: Long?
            max_elev = if (textFieldMaxElev.Text.trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMaxElev.Text.toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMaximum_elevation(max_elev)
            if (comboBoxElevUnits.SelectedIndex == -1 && comboBoxElevUnits.SelectedItem == null) {
                specimen.setElev_units("")
            } else {
                specimen.setElev_units(comboBoxElevUnits.SelectedItem.toString())
            }
            specimen.setCollectingMethod(jTextFieldCollectingMethod.Text)
            specimen.setIsoDate(jTextFieldISODate.Text)
            specimen.setDateNos(jTextFieldDateNos.Text)
            specimen.setDateCollected(jTextFieldDateCollected.Text)
            specimen.setDateEmerged(jTextFieldDateEmerged.Text)
            specimen.setDateCollectedIndicator(jTextFieldDateCollectedIndicator.Text)
            specimen.setDateEmergedIndicator(jTextFieldDateEmergedIndicator.Text)
            if (jComboBoxCollection.SelectedIndex == -1 && jComboBoxCollection.SelectedItem == null) {
                specimen.setCollection("")
            } else {
                specimen.setCollection(jComboBoxCollection.SelectedItem.toString())
            }
            if (jComboBoxFeatures.SelectedIndex == -1 && jComboBoxFeatures.SelectedItem == null) {
                specimen.setFeatures("")
            } else {
                specimen.setFeatures(jComboBoxFeatures.SelectedItem.toString())
            }
            if (jComboBoxLifeStage.SelectedIndex == -1 && jComboBoxLifeStage.SelectedItem == null) {
                specimen.setLifeStage("")
            } else {
                specimen.setLifeStage(jComboBoxLifeStage.SelectedItem.toString())
            }
            if (jComboBoxSex.SelectedIndex == -1 && jComboBoxSex.SelectedItem == null) {
                specimen.setSex("")
            } else {
                specimen.setSex(jComboBoxSex.SelectedItem.toString())
                log.debug("jComboBoxSex selectedIndex=" + jComboBoxSex.SelectedIndex)
            }
            log.debug("sex=" + specimen.Sex)
            specimen.setCitedInPublication(jTextFieldCitedInPub.Text)
            //specimen.setPreparationType(jTextFieldPreparationType.Text);
            specimen.setAssociatedTaxon(jTextFieldAssociatedTaxon.Text)
            specimen.setHabitat(jTextFieldHabitat.Text)
            specimen.setMicrohabitat(textFieldMicrohabitat.Text)
            specimen.setSpecimenNotes(jTextAreaSpecimenNotes.Text)
            specimen.setInferences(jTextFieldInferences.Text)
            specimen.setLastUpdatedBy(Singleton.UserFullName)
            specimen.setDateLastUpdated(Date())
            specimen.setWorkFlowStatus(jComboBoxWorkflowStatus.SelectedItem.toString())
            specimen.setQuestions(jTextFieldQuestions.Text)
            try { // make sure specimen controller does not throw null pointer exception – whyever
                if (specimenController.Specimen == null) {
                    specimenController.setSpecimen(specimen)
                }
                specimenController.save() // save the record
                setStateToClean() // enable the navigation buttons
                setStatus("Saved") // inform the user
                jTextFieldStatus.setForeground(Color.BLACK)
                setWarnings()
                jTextFieldLastUpdatedBy.setText(specimen.LastUpdatedBy)
                jTextFieldDateLastUpdated.setText(specimen.DateLastUpdated.toString())
            } catch (e: SaveFailedException) {
                setStateToDirty() // disable the navigation buttons
                setWarning("Error: " + e.message)
            }
            val sls = SpecimenLifeCycle()
            Singleton.MainFrame.setCount(sls.findSpecimenCount())
        } catch (e: OptimisticLockException) { // Oh, well. Issues with foreign keys already deleting items, which are not found afterwards.
// We catch these here and silence them. TODO: resolve by changing database structure
// We might also catch unwanted ones; böh, too bad – alert the user just in case.
            log.error(e)
            setWarning("Error: " + e.message)
        } catch (e: Exception) { // trap any exception and notify the user
            setStateToDirty() // disable the navigation buttons
            setWarning("Error: " + e.message)
            log.error(e)
            throw e
        }
        updateContentDependentLabels()
        try {
            thisPane.Parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        } catch (ex: Exception) {
            log.error(ex)
        }
    }

    /**
     * Set the fields values to the ones of the previous specimen
     */
    private fun copyPreviousRecord() {
        log.debug("calling copyPreviousRecord()")
        //thisPane.setStateToDirty();
        jTextFieldDateDetermined.setText(previousSpecimen.DateIdentified)
        jCBDeterminer.setSelectedItem(previousSpecimen.IdentifiedBy)
        jTextFieldVerbatimLocality.setText(previousSpecimen.VerbatimLocality)
        jComboBoxCountry.setSelectedItem(previousSpecimen.Country)
        jComboBoxPrimaryDivision.setSelectedItem(previousSpecimen.PrimaryDivison)
        // Elevations
        try {
            jTextFieldMinElevation.setText(java.lang.Long.toString(previousSpecimen.getMinimum_elevation()))
        } catch (e: Exception) {
            jTextFieldMinElevation.setText("")
        }
        try {
            textFieldMaxElev.setText(java.lang.Long.toString(previousSpecimen.getMaximum_elevation()))
        } catch (e: Exception) {
            textFieldMaxElev.setText("")
        }
        if (previousSpecimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(previousSpecimen.getElev_units())
        } else {
            comboBoxElevUnits.setSelectedItem("")
        }
        jTextFieldLocality.setText(previousSpecimen.SpecificLocality)
        jComboBoxCollection.setSelectedItem(previousSpecimen.Collection)
        jTextFieldDateNos.setText(previousSpecimen.DateNos)
        jTextFieldISODate.setText(previousSpecimen.IsoDate)
        jTextFieldDateEmerged.setText(previousSpecimen.DateEmerged)
        jTextFieldDateCollectedIndicator.setText(previousSpecimen.DateCollectedIndicator)
        jTextFieldDateEmergedIndicator.setText(previousSpecimen.DateEmergedIndicator)
        jTextFieldDateCollected.setText(previousSpecimen.DateCollected)
        jComboBoxLifeStage.setSelectedItem(previousSpecimen.LifeStage)
        jComboBoxSex.setSelectedItem(previousSpecimen.Sex)
        jTextFieldAssociatedTaxon.setText(previousSpecimen.AssociatedTaxon)
        jTextFieldHabitat.setText(previousSpecimen.Habitat)
        textFieldMicrohabitat.setText(previousSpecimen.Microhabitat)
        jTextAreaSpecimenNotes.setText(previousSpecimen.SpecimenNotes)
        jTextFieldInferences.setText(previousSpecimen.Inferences)
        //+numbers
        specimen.Numbers.clear()
        for (number in previousSpecimen.Numbers) { //specimen.Numbers.add((Number.class)iter.next());
            val n = (number.clone() as Number)
            n.setSpecimen(specimen)
            specimen.Numbers.add(n)
        }
        jTableNumbers.setModel(NumberTableModel(specimen.Numbers))
        setupNumberJTableRenderer()
        //+ verify the georeference data (we do want it all copied)
//+ preparation type (the whole table!) = specimen parts
        specimen.SpecimenParts.clear()
        for (specimenPart in previousSpecimen.SpecimenParts) {
            val part: SpecimenPart = specimenPart.clone() as SpecimenPart
            part.setSpecimen(specimen)
            specimen.SpecimenParts.add(part)
        }
        jTableSpecimenParts.setModel(SpecimenPartsTableModel(specimen.SpecimenParts))
        setupSpecimenPartsJTableRenderer()
        //+collectors
        specimen.Collectors.clear()
        for (collector in previousSpecimen.Collectors) {
            val c: Collector = collector.clone() as Collector
            c.setSpecimen(specimen)
            specimen.Collectors.add(c)
        }
        jTableCollectors.setModel(CollectorTableModel(specimen.Collectors))
        setupCollectorJTableRenderer()
        //+determinations
        specimen.Determinations.clear()
        for (prevdet in previousSpecimen.Determinations) {
            val newdet: Determination = prevdet.clone()
            newdet.setSpecimen(specimen)
            specimen.Determinations.add(newdet)
        }
        //+georeference
        specimen.LatLong.clear()
        // prepare hash set as otherwise, in getLatLong(), an empty LatLong is returned
        val latLongs: HashSet<LatLong?> = HashSet<LatLong?>()
        for (prevgeo in previousSpecimen.LatLong) {
            val newgeo: LatLong = prevgeo.clone()
            log.debug("Got newgeo with lat " + newgeo.DecLat)
            newgeo.setSpecimen(specimen)
            latLongs.add(newgeo)
        }
        specimen.setLatLong(latLongs)
        //new - verbatim locality
        jTextFieldVerbatimLocality.setText(previousSpecimen.VerbatimLocality)
        //new - publications
        jTextFieldCitedInPub.setText(previousSpecimen.CitedInPublication)
        //new - features
        jComboBoxFeatures.setSelectedItem(previousSpecimen.Features)
        //new - collecting method
        jTextFieldCollectingMethod.setText(previousSpecimen.CollectingMethod)
        updateContentDependentLabels()
    }

    /**
     * Set the values of the fields to the ones of the specimen
     * TODO: refactor to unused: move to instantiation of fields, e.g.
     */
    private fun setValues() {
        log.debug("okok setting values, specimenid is " + specimen.SpecimenId)
        setStatus("Setting values")
        jTextFieldBarcode.setText(specimen.Barcode)
        //alliefix - set to value from properties
//jComboBoxLocationInCollection.setSelectedItem(specimen.LocationInCollection);
        val locationInCollectionPropertiesVal: String = Singleton.Properties.Properties.getProperty(
                ImageCaptureProperties.Companion.KEY_DISPLAY_COLLECTION)
        jComboBoxLocationInCollection.setSelectedItem(locationInCollectionPropertiesVal)
        //allie try
/*Set<LatLong> georeferences = specimen.LatLong;
		log.debug("setvalues: georeferences size is : + " + georeferences.size());
		LatLong georeference_pre = georeferences.iterator().next();
		log.debug("lat is : + " + georeference_pre.LatDegString);
		log.debug("long is : + " + georeference_pre.LongDegString);*/cbTypeStatus.setSelectedItem(specimen.TypeStatus)
        jTextFieldDrawerNumber.setText(specimen.DrawerNumber)
        jComboBoxFamily.setSelectedItem(specimen.Family)
        jComboBoxSubfamily.setSelectedItem(specimen.Subfamily)
        jTextFieldTribe.setText(specimen.Tribe)
        jTextFieldGenus.setText(specimen.Genus)
        jTextFieldSpecies.setText(specimen.SpecificEpithet)
        jTextFieldSubspecies.setText(specimen.SubspecificEpithet)
        jTextFieldInfraspecificEpithet.setText(specimen.InfraspecificEpithet)
        jTextFieldInfraspecificRank.setText(specimen.InfraspecificRank)
        jTextFieldAuthorship.setText(specimen.Authorship)
        //allie new - bugfix
        textFieldMicrohabitat.setText(specimen.Microhabitat)
        jTextFieldIdRemarks.setText(specimen.IdentificationRemarks)
        jTextFieldDateDetermined.setText(specimen.DateIdentified)
        //allie change
//log.debug("jComboBoxLifeStage here!!! specimen life stage is " + specimen.LifeStage);
        if (specimen.LifeStage == null || specimen.LifeStage == "") {
            specimen.setLifeStage("adult")
            jComboBoxLifeStage.setSelectedIndex(0)
        }
        //allie change - removed this
//MCZbaseAuthAgentName selection = new MCZbaseAuthAgentName();
//selection.setAgent_name(specimen.IdentifiedBy);
//((AgentNameComboBoxModel)jCBDeterminer.Model).setSelectedItem(selection);
//jCBDeterminer.Editor.setItem(jCBDeterminer.Model.SelectedItem);
//allie change - added this
//jCBDeterminer.setText(specimen.IdentifiedBy);
        jCBDeterminer.setSelectedItem(specimen.IdentifiedBy)
        jComboBoxNatureOfId.setSelectedItem(specimen.NatureOfId)
        jTextFieldUnnamedForm.setText(specimen.UnNamedForm)
        jTextFieldVerbatimLocality.setText(specimen.VerbatimLocality)
        // Specimen record contains a string, delegate handling of lookup of object to the combo box model.
//allieremove
// 		log.debug(specimen.HigherGeography);
// 		((HigherGeographyComboBoxModel)comboBoxHigherGeog.Model).setSelectedItem(specimen.HigherGeography);
// //TODO ? set model not notifying listeners?
// 		higherGeogNotFoundWarning = new StringBuffer();
// 		comboBoxHigherGeog.Editor.setItem(comboBoxHigherGeog.Model.SelectedItem);
// 		if (specimen.HigherGeography==null) {
// 			comboBoxHigherGeog.setBackground(Color.YELLOW);
// 		} else {
// 			if (comboBoxHigherGeog.Model.SelectedItem==null) {
// 				comboBoxHigherGeog.setBackground(Color.RED);
// 				higherGeogNotFoundWarning.append("Higher Geog: [").append(specimen.HigherGeography).append("] not found. Fix before saving.");
// 			}
// 		}
// 		jTextFieldCountry.setText(specimen.Country);
        jComboBoxCountry.setSelectedItem(specimen.Country)
        if (specimen.ValidDistributionFlag != null) {
            jCheckBoxValidDistributionFlag.setSelected(specimen.ValidDistributionFlag)
        } else {
            jCheckBoxValidDistributionFlag.setSelected(false)
        }
        jComboBoxPrimaryDivision.setSelectedItem(specimen.PrimaryDivison)
        jTextFieldLocality.setText(specimen.SpecificLocality)
        // Elevations  **********************************************************************
        try {
            jTextFieldMinElevation.setText(java.lang.Long.toString(specimen.getMinimum_elevation()))
        } catch (e: Exception) {
            jTextFieldMinElevation.setText("")
        }
        try {
            textFieldMaxElev.setText(java.lang.Long.toString(specimen.getMaximum_elevation()))
        } catch (e: Exception) {
            textFieldMaxElev.setText("")
        }
        if (specimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(specimen.getElev_units())
        } else {
            comboBoxElevUnits.setSelectedItem("")
        }
        jTextFieldCollectingMethod.setText(specimen.CollectingMethod)
        jTextFieldISODate.setText(specimen.IsoDate)
        jTextFieldDateNos.setText(specimen.DateNos)
        jTextFieldDateCollected.setText(specimen.DateCollected)
        jTextFieldDateEmerged.setText(specimen.DateEmerged)
        jTextFieldDateCollectedIndicator.setText(specimen.DateCollectedIndicator)
        jTextFieldDateEmergedIndicator.setText(specimen.DateEmergedIndicator)
        jComboBoxCollection.setSelectedItem(specimen.Collection)
        //jTextFieldPreparationType.setText(specimen.PreparationType);
        jTextFieldAssociatedTaxon.setText(specimen.AssociatedTaxon)
        jTextFieldHabitat.setText(specimen.Habitat)
        textFieldMicrohabitat.setText(specimen.Microhabitat)
        jTextAreaSpecimenNotes.setText(specimen.SpecimenNotes)
        jComboBoxFeatures.setSelectedItem(specimen.Features)
        jComboBoxLifeStage.setSelectedItem(specimen.LifeStage)
        jComboBoxSex.setSelectedItem(specimen.Sex)
        jTextFieldCitedInPub.setText(specimen.CitedInPublication)
        jTextFieldQuestions.setText(specimen.Questions)
        jComboBoxWorkflowStatus.setSelectedItem(specimen.WorkFlowStatus)
        if (specimen.isStateDone()) {
            jTextFieldMigrationStatus.setText("http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.CatNum)
        } else {
            jTextFieldMigrationStatus.setText("")
        }
        jTextFieldInferences.setText(specimen.Inferences)
        jTextFieldCreator.setText(specimen.CreatedBy)
        if (specimen.DateCreated != null) {
            jTextFieldDateCreated.setText(specimen.DateCreated.toString())
        }
        jTextFieldLastUpdatedBy.setText(specimen.LastUpdatedBy)
        if (specimen.DateLastUpdated != null) {
            jTextFieldDateLastUpdated.setText(specimen.DateLastUpdated.toString())
        }
        //allie change
        if (specimen.NatureOfId == null || specimen.NatureOfId == "") {
            specimen.setLifeStage("expert ID")
            jComboBoxNatureOfId.setSelectedIndex(0)
        }
        //without this, it does save the 1st record, and it does not copy the next record!
        log.debug("setValues calling jTableNumbers.setModel(new NumberTableModel(specimen.Numbers));")
        jTableNumbers.setModel(NumberTableModel(specimen.Numbers))
        setupNumberJTableRenderer()
        jTableCollectors.setModel(CollectorTableModel(specimen.Collectors))
        setupCollectorJTableRenderer()
        jTableSpecimenParts.setModel(SpecimenPartsTableModel(specimen.SpecimenParts))
        setupSpecimenPartsJTableRenderer()
        updateContentDependentLabels()
        setWarnings()
        setStateToClean()
        setStatus("Loaded")
    }

    private fun updateDeterminationCount() {
        if (specimen.Determinations == null) {
            setDeterminationCount(0)
        } else {
            setDeterminationCount(specimen.Determinations.size)
        }
    }

    private fun setDeterminationCount(count: Int) {
        val detSuffix = if (count == 1) "s" else ""
        jButtonDeterminations.setText("$count Det$detSuffix.")
        jButtonDeterminations.updateUI()
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldStatus(): JTextField? {
        if (jTextFieldStatus == null) {
            jTextFieldStatus = JTextField("Status")
            jTextFieldStatus.setEditable(false)
            jTextFieldStatus.setEnabled(true)
        }
        return jTextFieldStatus
    }

    /**
     * This method initializes jPanel, laying out the UI components.
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel(MigLayout("wrap 4, fillx")) // 4 col layout
            // section: top information
            addBasicJLabel(jPanel, "Barcode")
            jPanel.add(barcodeJTextField, "grow")
            addBasicJLabel(jPanel, "Collection")
            jPanel.add(locationInCollectionJComboBox)
            // row
            addBasicJLabel(jPanel, "Number of Images")
            jPanel.add(jTextFieldImgCount, "grow")
            addBasicJLabel(jPanel, "Migration Status")
            jPanel.add(getJTextFieldMigrationStatus(), "grow")
            // section: family, classification
// row
            addBasicJLabel(jPanel, "Family")
            jPanel.add(familyJTextField, "grow")
            addBasicJLabel(jPanel, "Subfamily")
            jPanel.add(jTextFieldSubfamily, "grow")
            // row
            addBasicJLabel(jPanel, "Genus")
            jPanel.add(genusJTextField, "grow")
            addBasicJLabel(jPanel, "Species")
            jPanel.add(specificEpithetJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Subspecies")
            jPanel.add(subspecifcEpithetJTextField, "grow")
            addBasicJLabel(jPanel, "Tribe")
            jPanel.add(getJTextFieldTribe(), "grow")
            //row
            addBasicJLabel(jPanel, "Infrasubspecific Name")
            jPanel.add(jTextFieldInfraspecificName, "grow")
            addBasicJLabel(jPanel, "Infrasubspecific Rank")
            jPanel.add(getJTextFieldInfraspecificRank(), "grow")
            // section: identification/determination
// row
            addBasicJLabel(jPanel, "ID by")
            jPanel.add(getJCBDeterminer())
            addBasicJLabel(jPanel, "Nature of ID")
            jPanel.add(getJComboBoxNatureOfId())
            // row
            addBasicJLabel(jPanel, "ID Date")
            jPanel.add(getJTextFieldDateDetermined(), "grow")
            jPanel.add(detsJButton)
            jPanel.add(dBIdLabel)
            // row
            addBasicJLabel(jPanel, "ID Remark")
            jPanel.add(getJTextFieldIdRemarks(), "grow, span 3")
            // row
            addBasicJLabel(jPanel, "Author")
            jPanel.add(getJTextFieldAuthorship(), "grow")
            addBasicJLabel(jPanel, "TypeStatus")
            jPanel.add(getCbTypeStatus())
            // section: locale
// row
            addBasicJLabel(jPanel, "Verbatim Locality")
            jPanel.add(verbatimLocalityJTextField, "grow")
            addBasicJLabel(jPanel, "Country")
            jPanel.add(countryJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "State/Province")
            jPanel.add(primaryDivisionJTextField, "grow")
            addBasicJLabel(jPanel, "Valid Dist.")
            jPanel.add(validDistributionJCheckBox)
            // row
            jPanel.add(getJButtonSpecificLocality(), "align label")
            jPanel.add(specificLocalityJTextField, "grow, span 2")
            jPanel.add(jButtonGeoreference)
            // row
            jPanel.add(JLabel("Elevation"), "span, split 6, sizegroup elevation")
            jPanel.add(JLabel("from:"), "align label, sizegroup elevation, right")
            jPanel.add(verbatimElevationJTextField, "grow, sizegroup elevation")
            jPanel.add(JLabel("to:"), "align label, sizegroup elevation, right")
            jPanel.add(getTextFieldMaxElev(), "grow, sizegroup elevation")
            jPanel.add(getComboBoxElevUnits(), "wrap, sizegroup elevation")
            // section: collection
// row
            addBasicJLabel(jPanel, "Collection")
            jPanel.add(jTextFieldCollection, "grow, span 3")
            // double row:
            addBasicJLabel(jPanel, "Collectors")
            jPanel.add(getJScrollPaneCollectors(), "span 2 2, grow")
            addBasicJLabel(jPanel, "Collecting Method")
            jPanel.add(getJButtonCollectorAdd())
            jPanel.add(getJTextFieldCollectingMethod(), "growx, top")
            // row
            addBasicJLabel(jPanel, "Verbatim date")
            jPanel.add(jTextFieldVerbatimDate, "grow")
            addBasicJLabel(jPanel, "yyyy/mm/dd")
            jPanel.add(getJTextFieldISODate(), "grow")
            // row
            jPanel.add(dateEmergedJButton, "align label")
            jPanel.add(getJTextFieldDateEmerged(), "grow")
            addBasicJLabel(jPanel, "Date emerged note")
            jPanel.add(getJTextFieldDateEmergedIndicator(), "grow")
            // row
            jPanel.add(dateCollectedJButton, "align label")
            jPanel.add(getJTextFieldDateCollected(), "grow")
            addBasicJLabel(jPanel, "Date collected note")
            jPanel.add(getJTextFieldDateCollectedIndicator(), "grow")
            // section: pictured specifics
// row
            addBasicJLabel(jPanel, "Habitat")
            jPanel.add(getJTextFieldHabitat(), "grow")
            addBasicJLabel(jPanel, "Microhabitat")
            jPanel.add(getTextFieldMicrohabitat(), "grow")
            // row
            addBasicJLabel(jPanel, "Life Stage")
            jPanel.add(getJComboBoxLifeStage(), "grow")
            addBasicJLabel(jPanel, "Sex")
            jPanel.add(getJComboBoxSex(), "grow")
            // row
            addBasicJLabel(jPanel, "Features")
            jPanel.add(getJComboBoxFeatures(), "wrap")
            // double row:
            addBasicJLabel(jPanel, "Specimen Parts")
            jPanel.add(getJScrollPaneSpecimenParts(), "span 3 2, grow")
            jPanel.add(jButtonAddPrep)
            // row
            addBasicJLabel(jPanel, "Publications")
            jPanel.add(citedInPublicationJTextField, "grow")
            addBasicJLabel(jPanel, "Associated Taxon")
            jPanel.add(associatedTaxonJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Specimen Notes")
            jPanel.add(getJScrollPaneNotes(), "span 3, grow")
            // double row
            addBasicJLabel(jPanel, "Numbers & more")
            jPanel.add(numbersJScrollPane, "span 3 2, grow")
            jPanel.add(getJButtonNumbersAdd())
            // section: other fields
// row
            addBasicJLabel(jPanel, "Drawer Number")
            jPanel.add(drawerNumberJTextField, "grow")
            addBasicJLabel(jPanel, "Inferences")
            jPanel.add(getJTextFieldInferences(), "grow")
            // row
            addBasicJLabel(jPanel, "Created by")
            jPanel.add(creatorJTextField, "grow")
            addBasicJLabel(jPanel, "Creation date")
            jPanel.add(dateCreatedJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Last updated by")
            jPanel.add(lastUpdatedByJTextField, "grow")
            addBasicJLabel(jPanel, "Last edit date")
            jPanel.add(jTextFieldDateUpdated, "grow")
            // row
            addBasicJLabel(jPanel, "Workflow Status")
            jPanel.add(getJComboBoxWorkflowStatus())
            addBasicJLabel(jPanel, "Unnamed Form")
            jPanel.add(getJTextFieldUnnamedForm(), "grow")
            // row
            addBasicJLabel(jPanel, "Questions")
            jPanel.add(questionsJTextField, "grow, span 3")
            // section: controls
            jPanel.add(getJButtonPaste(), "span, split 6") //, sizegroup controls");
            jPanel.add(jButtonHistory) //, "sizegroup controls");
            jPanel.add(getJButtonPrevious(), "tag back")
            jPanel.add(getJButtonNext(), "tag next")
            jPanel.add(jButtonCopySave, "tag apply") //, "sizegroup controls");
            jPanel.add(saveJButton, "tag apply") //, "sizegroup controls");
        }
        return jPanel
    }

    /**
     * Get the label field containing the id of the specimen
     *
     * @return JLabel the label with the database id
     */
    private val dBIdLabel: JLabel?
        private get() {
            if (jLabelDBId == null) {
                jLabelDBId = JLabel()
            }
            updateDBIdLabel()
            return jLabelDBId
        }

    /**
     * Update the field: data base ID to match the assigned specimen
     */
    private fun updateDBIdLabel() {
        jLabelDBId.setText("DataBase ID: " + specimen.SpecimenId)
    }

    private fun addBasicJLabel(target: JPanel, labelText: String) {
        val label = JLabel()
        label.setText("$labelText:")
        target.add(label, "tag label, right") //"align label" was removed as requested
    }

    /**
     * This method initializes jTextFieldBarcode
     *
     * @return javax.swing.JTextField
     */
    private val barcodeJTextField: JTextField?
        private get() {
            if (jTextFieldBarcode == null) {
                jTextFieldBarcode = JTextField()
                jTextFieldBarcode.setEditable(false)
                jTextFieldBarcode.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Barcode"))
            }
            return jTextFieldBarcode
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val genusJTextField: JTextField?
        private get() {
            if (jTextFieldGenus == null) {
                jTextFieldGenus = JTextField()
                jTextFieldGenus.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Genus", jTextFieldGenus))
                jTextFieldGenus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Genus"))
                jTextFieldGenus.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldGenus
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val specificEpithetJTextField: JTextField?
        private get() {
            if (jTextFieldSpecies == null) {
                jTextFieldSpecies = JTextField()
                jTextFieldSpecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SpecificEpithet", jTextFieldSpecies))
                jTextFieldSpecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecificEpithet"))
                jTextFieldSpecies.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldSpecies
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val subspecifcEpithetJTextField: JTextField?
        private get() {
            if (jTextFieldSubspecies == null) {
                jTextFieldSubspecies = JTextField()
                jTextFieldSubspecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SubspecificEpithet", jTextFieldSubspecies))
                jTextFieldSubspecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SubspecificEpithet"))
                jTextFieldSubspecies.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldSubspecies
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val specificLocalityJTextField: JTextField?
        private get() {
            if (jTextFieldLocality == null) {
                jTextFieldLocality = JTextField()
                jTextFieldLocality.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SpecificLocality", jTextFieldLocality))
                jTextFieldLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecificLocality"))
                jTextFieldLocality.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldLocality
        }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val saveJButton: JButton?
        private get() {
            if (jButtonSave == null) {
                jButtonSave = JButton("Save")
                if (specimen.isStateDone()) {
                    jButtonSave.setEnabled(false)
                    jButtonSave.setText("Migrated " + specimen.LoadFlags)
                }
                jButtonSave.setMnemonic(KeyEvent.VK_S)
                jButtonSave.setToolTipText("Save changes to this record to the database. No fields should have red backgrounds before you save.")
                jButtonSave.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        save()
                    }
                })
            }
            return jButtonSave
        }//jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldCollection: JComboBox<String?>?
        private get() {
            if (jComboBoxCollection == null) {
                log.debug("init jComboBoxCollection")
                val sls = SpecimenLifeCycle()
                jComboBoxCollection = JComboBox<String?>()
                jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.DistinctCollections))
                jComboBoxCollection.setEditable(true)
                //jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
                jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
                jComboBoxCollection.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxCollection)
            }
            return jComboBoxCollection
        }//jTextFieldLastUpdatedBy.setEnabled(false);

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val lastUpdatedByJTextField: JTextField?
        private get() {
            if (jTextFieldLastUpdatedBy == null) {
                jTextFieldLastUpdatedBy = JTextField()
                jTextFieldLastUpdatedBy.setEditable(false)
                jTextFieldLastUpdatedBy.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "LastUpdatedBy"))
                //jTextFieldLastUpdatedBy.setEnabled(false);
                jTextFieldLastUpdatedBy.setForeground(Color.BLACK)
            }
            return jTextFieldLastUpdatedBy
        }

    /**
     * This method initializes jScrollPaneCollectors
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneCollectors(): JScrollPane? {
        if (jScrollPaneCollectors == null) {
            jScrollPaneCollectors = basicWrapperJScrollPane
            jScrollPaneCollectors.setViewportView(getJTableCollectors())
            jScrollPaneCollectors.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jScrollPaneCollectors
    }

    /**
     * This method initializes jTableCollectors
     *
     * @return javax.swing.JTable
     */
    private fun getJTableCollectors(): JTable? {
        if (jTableCollectors == null) {
            try {
                jTableCollectors = JTableWithRowBorder(CollectorTableModel(specimen.Collectors))
            } catch (e: NullPointerException) {
                jTableCollectors = JTableWithRowBorder(CollectorTableModel())
            }
            setupCollectorJTableRenderer()
            jTableCollectors.setRowHeight(jTableCollectors.RowHeight + 5)
            jTableCollectors.setObjectName("Collector")
            jTableCollectors.setParentPane(thisPane)
            jTableCollectors.addListener(object : ActionListener {
                override fun actionPerformed(actionEvent: ActionEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            jTableCollectors.enableDeleteability()
        }
        return jTableCollectors
    }

    /**
     * Setup from time to time the editor
     */
    private fun setupCollectorJTableRenderer() {
        val cls = CollectorLifeCycle()
        val jComboBoxCollector: JComboBox<String?> = JComboBox<String?>(cls.DistinctCollectors)
        jComboBoxCollector.setEditable(true)
        //field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
        jTableCollectors.ColumnModel.getColumn(0).setCellEditor(ComboBoxCellEditor(jComboBoxCollector))
        AutoCompleteDecorator.decorate(jComboBoxCollector)
    }

    private fun getJScrollPaneSpecimenParts(): JScrollPane? {
        if (jScrollPaneSpecimenParts == null) {
            jScrollPaneSpecimenParts = basicWrapperJScrollPane
            jScrollPaneSpecimenParts.setViewportView(getJTableSpecimenParts())
            jScrollPaneSpecimenParts.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jScrollPaneSpecimenParts
    }

    private fun getJTableSpecimenParts(): JTable? {
        if (jTableSpecimenParts == null) {
            try {
                jTableSpecimenParts = JTableWithRowBorder(SpecimenPartsTableModel(specimen.SpecimenParts))
            } catch (e: NullPointerException) {
                jTableSpecimenParts = JTableWithRowBorder(SpecimenPartsTableModel())
            }
            jTableSpecimenParts.ColumnModel.getColumn(0).setPreferredWidth(90)
            jTableSpecimenParts.setRowHeight(jTableSpecimenParts.RowHeight + 5)
            setupSpecimenPartsJTableRenderer()
            log.debug(specimen.SpecimenParts.size)
            jTableSpecimenParts.setObjectName("Specimen Part")
            jTableSpecimenParts.setParentPane(thisPane)
            jTableSpecimenParts.addListener(object : ActionListener {
                override fun actionPerformed(actionEvent: ActionEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            jTableSpecimenParts.enableDeleteability()
        }
        return jTableSpecimenParts
    }

    private fun setupSpecimenPartsJTableRenderer() {
        log.debug("Setting specimen part cell editors")
        val comboBoxPart: JComboBox<String?> = JComboBox<String?>(SpecimenPart.Companion.PART_NAMES)
        getJTableSpecimenParts().ColumnModel.getColumn(0).setCellEditor(DefaultCellEditor(comboBoxPart))
        val comboBoxPrep: JComboBox<String?> = JComboBox<String?>(SpecimenPart.Companion.PRESERVATION_NAMES)
        getJTableSpecimenParts().ColumnModel.getColumn(1).setCellEditor(DefaultCellEditor(comboBoxPrep))
        getJTableSpecimenParts().ColumnModel.getColumn(4).setCellRenderer(ButtonRenderer())
        getJTableSpecimenParts().ColumnModel.getColumn(4).setCellEditor(ButtonEditor(ButtonEditor.Companion.OPEN_SPECIMENPARTATTRIBUTES, this))
    }//jTextFieldDateLastUpdated.setEnabled(false);

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldDateUpdated: JTextField?
        private get() {
            if (jTextFieldDateLastUpdated == null) {
                jTextFieldDateLastUpdated = JTextField()
                jTextFieldDateLastUpdated.setEditable(false)
                //jTextFieldDateLastUpdated.setEnabled(false);
                jTextFieldDateLastUpdated.setForeground(Color.BLACK)
            }
            return jTextFieldDateLastUpdated
        }//jTextFieldCreator.setEnabled(false);

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val creatorJTextField: JTextField?
        private get() {
            if (jTextFieldCreator == null) {
                jTextFieldCreator = JTextField()
                jTextFieldCreator.setEditable(false)
                //jTextFieldCreator.setEnabled(false);
                jTextFieldCreator.setForeground(Color.BLACK)
                jTextFieldCreator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Creator"))
            }
            return jTextFieldCreator
        }//jTextFieldDateCreated.setEnabled(false);

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val dateCreatedJTextField: JTextField?
        private get() {
            if (jTextFieldDateCreated == null) {
                jTextFieldDateCreated = JTextField()
                jTextFieldDateCreated.setEditable(false)
                //jTextFieldDateCreated.setEnabled(false);
                jTextFieldDateCreated.setForeground(Color.BLACK)
            }
            return jTextFieldDateCreated
        }

    /**
     * This method initializes jScrollPaneNumbers
     *
     * @return javax.swing.JScrollPane
     */
    private val numbersJScrollPane: JScrollPane?
        private get() {
            if (jScrollPaneNumbers == null) {
                jScrollPaneNumbers = basicWrapperJScrollPane
                jScrollPaneNumbers.setViewportView(numberJTable)
                jScrollPaneNumbers.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jScrollPaneNumbers
        }

    /**
     * This method initializes jTable for numbers fields
     *
     * @return javax.swing.JTable
     */
    private val numberJTable: JTable?
        private get() {
            if (jTableNumbers == null) {
                try {
                    jTableNumbers = JTableWithRowBorder(NumberTableModel(specimen.Numbers))
                } catch (e: NullPointerException) {
                    jTableNumbers = JTableWithRowBorder(NumberTableModel())
                }
                jTableNumbers.setRowHeight(jTableNumbers.RowHeight + 5)
                setupNumberJTableRenderer()
                jTableNumbers.setObjectName("Number")
                jTableNumbers.setParentPane(thisPane)
                jTableNumbers.addListener(object : ActionListener {
                    override fun actionPerformed(actionEvent: ActionEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                jTableNumbers.enableDeleteability()
            }
            return jTableNumbers
        }

    /**
     * Setting the model will overwrite the existing cell editor bound
     * to the column model, so we might need to add it again som times.
     * That's what this function does
     */
    private fun setupNumberJTableRenderer() { // First, setup the number field
        val field1 = JTextField()
        field1.setInputVerifier(MetadataRetriever.getInputVerifier(Number::class.java, "Number", field1))
        field1.setVerifyInputWhenFocusTarget(true)
        jTableNumbers.ColumnModel.getColumn(NumberTableModel.Companion.COLUMN_NUMBER).setCellEditor(ValidatingTableCellEditor(field1))
        // Then, setup the type field
        val jComboNumberTypes: JComboBox<String?> = JComboBox<String?>(NumberLifeCycle.Companion.DistinctTypes)
        jComboNumberTypes.setEditable(true)
        val typeColumn: TableColumn = jTableNumbers.ColumnModel.getColumn(NumberTableModel.Companion.COLUMN_TYPE)
        val comboBoxEditor = ComboBoxCellEditor(jComboNumberTypes)
        AutoCompleteDecorator.decorate(jComboNumberTypes)
        typeColumn.setCellEditor(ComboBoxCellEditor(jComboNumberTypes))
        val renderer = DefaultTableCellRenderer()
        renderer.setToolTipText("Click for pick list of number types.")
        typeColumn.setCellRenderer(renderer)
    }

    /**
     * This method initializes jButtonNumbersAdd
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonNumbersAdd(): JButton? {
        if (jButtonNumbersAdd == null) {
            jButtonNumbersAdd = JButton()
            jButtonNumbersAdd.setText("+")
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png")
            try {
                jButtonNumbersAdd.setText("")
                jButtonNumbersAdd.setIcon(ImageIcon(iconFile))
                jButtonNumbersAdd.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        (jTableNumbers.Model as NumberTableModel).addNumber(Number(specimen, "", ""))
                        thisPane!!.setStateToDirty()
                    }
                })
            } catch (e: Exception) {
                jButtonNumbersAdd.setText("+")
            }
        }
        return jButtonNumbersAdd
    }//log.debug("the lat long is : " + specimen.LatLong.);

    //allie add
/*try {
				Set<LatLong> georeferences = specimen.LatLong;
				log.debug("getJButtonGeoreference georeferences size is : + " + georeferences.size());
				LatLong georeference_pre = georeferences.iterator().next();
				log.debug("lat is : + " + georeference_pre.LatDegString);
				log.debug("long is : + " + georeference_pre.LongDegString);
				if ((!("").equals(georeference_pre.LatDegString)) ||
					(!("").equals(georeference_pre.LongDegString))){
					jButtonGeoreference.setText("1.0 Georeference");
				}else{
					jButtonGeoreference.setText("0.0 Georeference");
				}
	        } catch (Exception e) {
	        	log.error(e.Message, e);
	        }	*/
    private val jButtonGeoreference: JButton?
        private get() {
            if (jButtonGeoReference == null) {
                jButtonGeoReference = JButton()
                //allie add
/*try {
				Set<LatLong> georeferences = specimen.LatLong;
				log.debug("getJButtonGeoreference georeferences size is : + " + georeferences.size());
				LatLong georeference_pre = georeferences.iterator().next();
				log.debug("lat is : + " + georeference_pre.LatDegString);
				log.debug("long is : + " + georeference_pre.LongDegString);
				if ((!("").equals(georeference_pre.LatDegString)) ||
					(!("").equals(georeference_pre.LongDegString))){
					jButtonGeoreference.setText("1.0 Georeference");
				}else{
					jButtonGeoreference.setText("0.0 Georeference");
				}
	        } catch (Exception e) {
	        	log.error(e.Message, e);
	        }	*/try {
                    updateJButtonGeoreference()
                    jButtonGeoReference.addActionListener(object : ActionListener {
                        override fun actionPerformed(e: ActionEvent?) {
                            thisPane!!.setStateToDirty()
                            val georeferences: MutableSet<LatLong?> = specimen.LatLong
                            //log.debug("the lat long is : " + specimen.LatLong.);
                            val georeference: LatLong? = georeferences.iterator().next()
                            georeference.setSpecimen(specimen)
                            val georefDialog = GeoreferenceDialog(georeference)
                            georefDialog.setVisible(true)
                            georefDialog.addComponentListener(object : ComponentAdapter() {
                                override fun componentHidden(e: ComponentEvent?) {
                                    updateJButtonGeoreference()
                                    super.componentHidden(e)
                                    autocompleteGeoDataFromGeoreference()
                                }
                            })
                        }
                    })
                } catch (e: Exception) {
                    log.error(e.message, e)
                }
            }
            return jButtonGeoReference
        }

    private fun updateJButtonGeoreference() {
        if (specimen.LatLong != null && !specimen.LatLong.isEmpty() && !specimen.LatLong.iterator().next().isEmpty()) {
            jButtonGeoReference.setText("✅ Georeference (1)")
        } else {
            jButtonGeoReference.setText("❔ Georeference (0)")
        }
        jButtonGeoReference.updateUI()
    }

    /**
     * This method initializes jButtonCollsAdd
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonCollectorAdd(): JButton? {
        if (jButtonCollectorAdd == null) {
            jButtonCollectorAdd = JButton()
            jButtonCollectorAdd.setText("+")
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png")
            try {
                jButtonCollectorAdd.setText("")
                jButtonCollectorAdd.setIcon(ImageIcon(iconFile))
                jButtonCollectorAdd.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        log.debug("adding a new collector........")
                        (jTableCollectors.Model as CollectorTableModel).addCollector(Collector(specimen, ""))
                        thisPane!!.setStateToDirty()
                    }
                })
            } catch (e: Exception) {
                jButtonCollectorAdd.setText("+")
            }
        }
        return jButtonCollectorAdd
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
                jTextFieldDrawerNumber.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "DrawerNumber", jTextFieldDrawerNumber))
                jTextFieldDrawerNumber.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DrawerNumber"))
                jTextFieldDrawerNumber.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldDrawerNumber
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
                jTextFieldVerbatimLocality.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "VerbatimLocality", jTextFieldVerbatimLocality))
                jTextFieldVerbatimLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "VerbatimLocality"))
                jTextFieldVerbatimLocality.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldVerbatimLocality
        }//jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
    //return jTextFieldCountry;
//if (jTextFieldCountry == null) {
/*jTextFieldCountry = new JTextField();
			jTextFieldCountry.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Country", jTextFieldCountry));
			jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
			jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});*/
//allie fix

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val countryJTextField: JComboBox<String?>?
        private get() { //if (jTextFieldCountry == null) {
/*jTextFieldCountry = new JTextField();
			jTextFieldCountry.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Country", jTextFieldCountry));
			jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
			jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});*/
//allie fix
            if (jComboBoxCountry == null) {
                log.debug("init jComboBoxCountry")
                val sls = SpecimenLifeCycle()
                jComboBoxCountry = JComboBox<String?>()
                //jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
                jComboBoxCountry.setModel(DefaultComboBoxModel<String?>(sls.DistinctCountries))
                jComboBoxCountry.setEditable(true)
                jComboBoxCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Country"))
                jComboBoxCountry.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxCountry)
            }
            return jComboBoxCountry
            //return jTextFieldCountry;
        }//jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
/*private JTextField getJTextField23() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
			jTextFieldPrimaryDivision.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
			jTextFieldPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "primaryDivison"));
			jTextFieldPrimaryDivision.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jTextFieldPrimaryDivision;
	}*/
//allie change
    private val primaryDivisionJTextField: JComboBox<String?>?
        private get() {
            if (jComboBoxPrimaryDivision == null) {
                jComboBoxPrimaryDivision = JComboBox<String?>()
                jComboBoxPrimaryDivision.setEditable(true)
                //jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
                val sls = SpecimenLifeCycle()
                jComboBoxPrimaryDivision.setModel(DefaultComboBoxModel<String?>(sls.DistinctPrimaryDivisions))
                jComboBoxPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "primaryDivison"))
                jComboBoxPrimaryDivision.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision)
            }
            return jComboBoxPrimaryDivision
        }//jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Family", jTextFieldFamily));

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val familyJTextField: JComboBox<String?>?
        private get() {
            if (jComboBoxFamily == null) {
                jComboBoxFamily = JComboBox<String?>()
                jComboBoxFamily.setModel(DefaultComboBoxModel<String?>(HigherTaxonLifeCycle.Companion.selectDistinctFamily()))
                jComboBoxFamily.setEditable(true)
                //jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Family", jTextFieldFamily));
                jComboBoxFamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Family"))
                jComboBoxFamily.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxFamily)
            }
            return jComboBoxFamily
        }//jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Subfamily", jTextFieldSubfamily));

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldSubfamily: JComboBox<String?>?
        private get() {
            if (jComboBoxSubfamily == null) {
                jComboBoxSubfamily = JComboBox<String?>()
                jComboBoxSubfamily.setModel(DefaultComboBoxModel<String?>(HigherTaxonLifeCycle.Companion.selectDistinctSubfamily("")))
                jComboBoxSubfamily.setEditable(true)
                //jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Subfamily", jTextFieldSubfamily));
                jComboBoxSubfamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Subfamily"))
                jComboBoxSubfamily.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxSubfamily)
            }
            return jComboBoxSubfamily
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldTribe(): JTextField? {
        if (jTextFieldTribe == null) {
            jTextFieldTribe = JTextField()
            jTextFieldTribe.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Tribe", jTextFieldTribe))
            jTextFieldTribe.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Tribe"))
            jTextFieldTribe.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldTribe
    }

    /**
     * This method initializes jComboBoxSex
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxSex(): JComboBox<String?>? {
        if (jComboBoxSex == null) {
            jComboBoxSex = JComboBox<String?>()
            jComboBoxSex.setModel(DefaultComboBoxModel<String?>(Sex.SexValues))
            jComboBoxSex.setEditable(true)
            jComboBoxSex.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Sex"))
            jComboBoxSex.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxSex)
        }
        return jComboBoxSex
    }

    /**
     * This method initializes jComboBoxFeatures
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxFeatures(): JComboBox<String?>? {
        if (jComboBoxFeatures == null) {
            jComboBoxFeatures = JComboBox<String?>()
            jComboBoxFeatures.setModel(DefaultComboBoxModel<String?>(Features.FeaturesValues))
            jComboBoxFeatures.setEditable(true)
            jComboBoxFeatures.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Features"))
            jComboBoxFeatures.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxFeatures)
            // TODO: validate input length
        }
        return jComboBoxFeatures
    }

    private fun getJComboBoxNatureOfId(): JComboBox<String?>? {
        if (jComboBoxNatureOfId == null) {
            jComboBoxNatureOfId = JComboBox<String?>()
            jComboBoxNatureOfId.setModel(DefaultComboBoxModel<String?>(NatureOfId.NatureOfIdValues))
            jComboBoxNatureOfId.setEditable(false)
            jComboBoxNatureOfId.setToolTipText(MetadataRetriever.getFieldHelp(Determination::class.java, "NatureOfId"))
            jComboBoxNatureOfId.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxNatureOfId)
            jComboBoxNatureOfId.setSelectedItem(NatureOfId.EXPERT_ID)
            jComboBoxNatureOfId.setSelectedIndex(0)
        }
        return jComboBoxNatureOfId
    }

    /**
     * This method initializes jComboBoxLifeStage
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxLifeStage(): JComboBox<String?>? {
        if (jComboBoxLifeStage == null) {
            jComboBoxLifeStage = JComboBox<String?>()
            jComboBoxLifeStage.setModel(DefaultComboBoxModel<String?>(LifeStage.Companion.LifeStageValues))
            jComboBoxLifeStage.setEditable(true)
            jComboBoxLifeStage.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Lifestage"))
            jComboBoxLifeStage.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxLifeStage)
            jComboBoxLifeStage.setSelectedItem("adult")
            jComboBoxLifeStage.setSelectedIndex(0)
        }
        return jComboBoxLifeStage
    }//jTextFieldDateNos.setToolTipText("Date found on labels where date might be either date collected or date emerged, or some other date");

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldVerbatimDate: JTextField?
        private get() {
            if (jTextFieldDateNos == null) {
                jTextFieldDateNos = JTextField()
                //jTextFieldDateNos.setToolTipText("Date found on labels where date might be either date collected or date emerged, or some other date");
                jTextFieldDateNos.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "DateNOS", jTextFieldDateNos))
                jTextFieldDateNos.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateNOS"))
                jTextFieldDateNos.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldDateNos
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateEmerged(): JTextField? {
        if (jTextFieldDateEmerged == null) {
            jTextFieldDateEmerged = JTextField()
            jTextFieldDateEmerged.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "DateEmerged", jTextFieldDateEmerged))
            jTextFieldDateEmerged.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateEmerged"))
            jTextFieldDateEmerged.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateEmerged
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateEmergedIndicator(): JTextField? {
        if (jTextFieldDateEmergedIndicator == null) {
            jTextFieldDateEmergedIndicator = JTextField()
            jTextFieldDateEmergedIndicator.setToolTipText("Verbatim text indicating that this is a date emerged.")
            jTextFieldDateEmergedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "DateEmergedIndicator", jTextFieldDateEmergedIndicator))
            jTextFieldDateEmergedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateEmergedIndicator"))
            jTextFieldDateEmergedIndicator.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateEmergedIndicator
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateCollected(): JTextField? {
        if (jTextFieldDateCollected == null) {
            jTextFieldDateCollected = JTextField()
            jTextFieldDateCollected.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateCollected"))
            jTextFieldDateCollected.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateCollected
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateCollectedIndicator(): JTextField? {
        if (jTextFieldDateCollectedIndicator == null) {
            jTextFieldDateCollectedIndicator = JTextField()
            jTextFieldDateCollectedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "DateCollectedIndicator", jTextFieldDateCollectedIndicator))
            jTextFieldDateCollectedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateCollectedIndicator"))
            jTextFieldDateCollectedIndicator.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateCollectedIndicator
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldInfraspecificName: JTextField?
        private get() {
            if (jTextFieldInfraspecificEpithet == null) {
                jTextFieldInfraspecificEpithet = JTextField()
                jTextFieldInfraspecificEpithet.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "InfraspecificEpithet", jTextFieldInfraspecificEpithet))
                jTextFieldInfraspecificEpithet.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "InfraspecificEpithet"))
                jTextFieldInfraspecificEpithet.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldInfraspecificEpithet
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldInfraspecificRank(): JTextField? {
        if (jTextFieldInfraspecificRank == null) {
            jTextFieldInfraspecificRank = JTextField()
            jTextFieldInfraspecificRank.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "InfraspecificRank", jTextFieldInfraspecificRank))
            jTextFieldInfraspecificRank.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "InfraspecificRank"))
            jTextFieldInfraspecificRank.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldInfraspecificRank
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldAuthorship(): JTextField? {
        if (jTextFieldAuthorship == null) {
            jTextFieldAuthorship = JTextField()
            jTextFieldAuthorship.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Authorship", jTextFieldAuthorship))
            jTextFieldAuthorship.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Authorship"))
            jTextFieldAuthorship.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldAuthorship
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldUnnamedForm(): JTextField? {
        if (jTextFieldUnnamedForm == null) {
            jTextFieldUnnamedForm = JTextField()
            jTextFieldUnnamedForm.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "UnnamedForm", jTextFieldUnnamedForm))
            jTextFieldUnnamedForm.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "UnnamedForm"))
            jTextFieldUnnamedForm.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldUnnamedForm
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val verbatimElevationJTextField: JTextField?
        private get() {
            if (jTextFieldMinElevation == null) {
                jTextFieldMinElevation = JTextField()
                jTextFieldMinElevation.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "VerbatimElevation", jTextFieldMinElevation))
                jTextFieldMinElevation.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "VerbatimElevation"))
                jTextFieldMinElevation.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldMinElevation
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldCollectingMethod(): JTextField? {
        if (jTextFieldCollectingMethod == null) {
            jTextFieldCollectingMethod = JTextField()
            jTextFieldCollectingMethod.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "CollectingMethod", jTextFieldCollectingMethod))
            jTextFieldCollectingMethod.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "CollectingMethod"))
            jTextFieldCollectingMethod.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldCollectingMethod
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private val jTextAreaNotes: JTextArea?
        private get() {
            if (jTextAreaSpecimenNotes == null) {
                jTextAreaSpecimenNotes = JTextArea()
                jTextAreaSpecimenNotes.setRows(3)
                jTextAreaSpecimenNotes.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecimenNotes"))
                jTextAreaSpecimenNotes.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextAreaSpecimenNotes
        }//jCheckBoxValidDistributionFlag.setToolTipText("Check if locality represents natural biological range.  Uncheck for Specimens that came from a captive breeding program");

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private val validDistributionJCheckBox: JCheckBox?
        private get() {
            if (jCheckBoxValidDistributionFlag == null) {
                jCheckBoxValidDistributionFlag = JCheckBox()
                //jCheckBoxValidDistributionFlag.setToolTipText("Check if locality represents natural biological range.  Uncheck for Specimens that came from a captive breeding program");
                jCheckBoxValidDistributionFlag.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ValidDistributionFlag"))
                jCheckBoxValidDistributionFlag.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jCheckBoxValidDistributionFlag
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val questionsJTextField: JTextField?
        private get() {
            if (jTextFieldQuestions == null) {
                jTextFieldQuestions = JTextField()
                jTextFieldQuestions.setBackground(MainFrame.Companion.BG_COLOR_QC_FIELD)
                jTextFieldQuestions.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "Questions", jTextFieldQuestions))
                jTextFieldQuestions.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Questions"))
                jTextFieldQuestions.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldQuestions
        }// TODO Auto-generated catch block

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jButtonAddPrep: JButton?
        private get() {
            if (jButtonAddPreparationType == null) {
                jButtonAddPreparationType = JButton("Add Prep")
                jButtonAddPreparationType.setMnemonic(KeyEvent.VK_P)
                jButtonAddPreparationType.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        log.debug("Adding new SpecimenPart")
                        val newPart = SpecimenPart()
                        newPart.setPreserveMethod(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_DEFAULT_PREPARATION))
                        newPart.setSpecimen(specimen)
                        val spls = SpecimenPartLifeCycle()
                        log.debug("Attaching new SpecimenPart")
                        try {
                            spls.persist(newPart)
                            specimen.SpecimenParts.add(newPart)
                            (jTableSpecimenParts.Model as AbstractTableModel).fireTableDataChanged()
                            log.debug("Added new SpecimenPart")
                        } catch (e1: SaveFailedException) { // TODO Auto-generated catch block
                            e1.printStackTrace()
                        }
                    }
                })
            }
            return jButtonAddPreparationType
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val associatedTaxonJTextField: JTextField?
        private get() {
            if (jTextFieldAssociatedTaxon == null) {
                jTextFieldAssociatedTaxon = JTextField()
                jTextFieldAssociatedTaxon.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "AssociatedTaxon", jTextFieldAssociatedTaxon))
                jTextFieldAssociatedTaxon.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "AssociatedTaxon"))
                jTextFieldAssociatedTaxon.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldAssociatedTaxon
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldHabitat(): JTextField? {
        if (jTextFieldHabitat == null) {
            jTextFieldHabitat = JTextField()
            jTextFieldHabitat.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Habitat", jTextFieldHabitat))
            jTextFieldHabitat.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Habitat"))
            jTextFieldHabitat.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldHabitat
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxWorkflowStatus(): JComboBox<String?>? {
        if (jComboBoxWorkflowStatus == null) {
            jComboBoxWorkflowStatus = JComboBox<String?>()
            jComboBoxWorkflowStatus.setModel(DefaultComboBoxModel<String?>(WorkFlowStatus.WorkFlowStatusValues))
            jComboBoxWorkflowStatus.setEditable(false)
            jComboBoxWorkflowStatus.setBackground(MainFrame.Companion.BG_COLOR_QC_FIELD)
            jComboBoxWorkflowStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "WorkflowStatus"))
            AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus)
        }
        return jComboBoxWorkflowStatus
    }//alliefix - set default from properties file
//jComboBoxLocationInCollection.setSelectedIndex(1);

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val locationInCollectionJComboBox: JComboBox<String?>?
        private get() {
            if (jComboBoxLocationInCollection == null) {
                jComboBoxLocationInCollection = JComboBox<String?>()
                jComboBoxLocationInCollection.setModel(DefaultComboBoxModel<String?>(LocationInCollection.LocationInCollectionValues))
                jComboBoxLocationInCollection.setEditable(false)
                jComboBoxLocationInCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "LocationInCollection"))
                //alliefix - set default from properties file
//jComboBoxLocationInCollection.setSelectedIndex(1);
                jComboBoxLocationInCollection.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxLocationInCollection)
            }
            return jComboBoxLocationInCollection
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldInferences(): JTextField? {
        if (jTextFieldInferences == null) {
            jTextFieldInferences = JTextField()
            jTextFieldInferences.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            jTextFieldInferences.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Inferences", jTextFieldInferences))
            jTextFieldInferences.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Inferences"))
            jTextFieldInferences.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldInferences
    }// retrieve and display the tracking events for this specimen
//Tracking t = new Tracking();
//t.setSpecimen(specimen);
    //Request by specimen doesn't work with Oracle.  Why?
//EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimen(specimen)));

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val jButtonHistory: JButton?
        private get() {
            if (jButtonGetHistory == null) {
                jButtonGetHistory = JButton()
                jButtonGetHistory.setText("History")
                jButtonGetHistory.setToolTipText("Show the history of who edited this record")
                jButtonGetHistory.setMnemonic(KeyEvent.VK_H)
                jButtonGetHistory.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) { // retrieve and display the tracking events for this specimen
//Tracking t = new Tracking();
//t.setSpecimen(specimen);
                        val tls = TrackingLifeCycle()
                        //Request by specimen doesn't work with Oracle.  Why?
//EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimen(specimen)));
                        val logViewer = EventLogFrame(ArrayList<Tracking?>(tls.findBySpecimenId(specimen.SpecimenId)))
                        logViewer.pack()
                        logViewer.setVisible(true)
                    }
                })
            }
            return jButtonGetHistory
        }

    /**
     * Instantiate - if necessary - the button to paste the copied specimen on the current one
     *
     * @return
     */
    private fun getJButtonPaste(): JButton? {
        log.debug("prev spec:::")
        if (jButtonPaste == null) {
            jButtonPaste = JButton()
            jButtonPaste.setText("Paste")
            jButtonPaste.setToolTipText("Paste previous record values into this screen")
            //TODO: decide on keyboard shortcut
//jButtonPaste.setMnemonic(KeyEvent.VK_H);
            jButtonPaste.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //populate the fields with the data.
                    previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache
                    copyPreviousRecord()
                }
            })
            updateJButtonPaste()
        }
        return jButtonPaste
    }

    private fun updateJButtonPaste() {
        jButtonPaste.setEnabled(!(previousSpecimen == null && ImageCaptureApp.lastEditedSpecimenCache == null))
    }// TODO: rather clone the specimen to prevent external/later changes//TODO: decide on keyboard shortcut
//jButtonCopy.setMnemonic(KeyEvent.VK_H);

    /**
     * Instantiate - if necessary - the button to save and then copy the current specimen
     *
     * @return
     */
    private val jButtonCopySave: JButton?
        private get() {
            if (jButtonCopy == null) {
                jButtonCopy = JButton()
                jButtonCopy.setText("Save & Copy")
                jButtonCopy.setToolTipText("Copy the values of this record after saving it")
                //TODO: decide on keyboard shortcut
//jButtonCopy.setMnemonic(KeyEvent.VK_H);
                jButtonCopy.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        thisPane!!.save()
                        // TODO: rather clone the specimen to prevent external/later changes
                        ImageCaptureApp.lastEditedSpecimenCache = thisPane!!.specimen
                        thisPane!!.setStatus("Saved & copied specimen with id " + thisPane!!.specimen.SpecimenId)
                    }
                })
            }
            return jButtonCopy
        }

    /**
     * This method initializes jButtonNext
     *
     * @return javax.swing.JButton
     */
//this is not the right arrow button!
    private fun getJButtonNext(): JButton? {
        if (jButtonNext == null) {
            jButtonNext = JButton()
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/next.png")
            if (iconFile != null) {
                jButtonNext.setIcon(ImageIcon(iconFile))
            } else {
                jButtonNext.setText("Next")
            }
            jButtonNext.setMnemonic(KeyEvent.VK_N)
            jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable())
            jButtonNext.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try { // try to move to the next specimen in the table.
                        thisPane!!.setStatus("Switching to next specimen...")
                        if (thisPane!!.specimenController.openNextSpecimenInTable()) {
                            thisPane.setVisible(false)
                            thisPane.invalidate()
                        } else {
                            thisPane!!.setWarning("No next specimen available.")
                        }
                    } catch (e1: Exception) { // TODO Auto-generated catch block
                        e1.printStackTrace()
                    } finally {
                        try {
                            thisPane.Parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                        } catch (ex: Exception) {
                            log.error(ex)
                        }
                    }
                }
            })
        }
        log.debug("SpecimenDetailsViewPane.JButtonNext: 9")
        return jButtonNext
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonPrevious(): JButton? {
        if (jButtonPrevious == null) {
            jButtonPrevious = JButton()
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/back.png")
            if (iconFile != null) {
                jButtonPrevious.setIcon(ImageIcon(iconFile))
            } else {
                jButtonPrevious.setText("Previous")
            }
            jButtonPrevious.setMnemonic(KeyEvent.VK_P)
            jButtonPrevious.setToolTipText("Move to Previous Specimen")
            jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable())
            jButtonPrevious.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try { // try to move to the previous specimen in the table.
                        thisPane!!.setStatus("Switching to previous specimen...")
                        if (thisPane!!.specimenController.openPreviousSpecimenInTable()) {
                            thisPane.setVisible(false)
                            thisPane.invalidate()
                        } else {
                            thisPane!!.setWarning("No previous specimen available.")
                        }
                    } catch (e1: Exception) { // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }
                }
            })
        }
        return jButtonPrevious
    }

    private fun setStateToClean() {
        state = STATE_CLEAN
        // if this is a record that is part of a navigateable set, enable the navigation buttons
        if (specimenController != null) {
            log.debug("Has controller")
            if (specimenController.isInTable()) {
                log.debug("Controller is in table")
                // test to make sure the buttons have been created before trying to enable them.
                if (jButtonNext != null) {
                    jButtonNext.setEnabled(true)
                }
                if (jButtonPrevious != null) {
                    jButtonPrevious.setEnabled(true)
                }
            }
        }
    }

    private fun setStateToDirty() {
        state = STATE_DIRTY
        if (jButtonNext != null) {
            jButtonNext.setEnabled(false)
        }
        if (jButtonPrevious != null) {
            jButtonPrevious.setEnabled(false)
        }
    }

    /**
     * State of the data in the forms as compared to the specimen from which the data was loaded.
     *
     * @return true if the data as displayed in the forms hasn't changed since the data was last loaded from
     * or saved to the specimen, otherwise false indicating a dirty record.
     */
    private val isClean: Boolean
        private get() {
            var result = false
            if (state == STATE_CLEAN) {
                result = true
            }
            return result
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldISODate(): JTextField? {
        if (jTextFieldISODate == null) {
            jTextFieldISODate = JTextField()
            jTextFieldISODate.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", jTextFieldISODate))
            jTextFieldISODate.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ISODate"))
            jTextFieldISODate.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldISODate
    }// update the text of the dets as soon as the component is closed

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val detsJButton: JButton?
        private get() {
            if (jButtonDeterminations == null) {
                jButtonDeterminations = JButton()
                jButtonDeterminations.setText("Dets.")
                jButtonDeterminations.setMnemonic(KeyEvent.VK_D)
                jButtonDeterminations.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val dets = DeterminationFrame(specimen)
                        // update the text of the dets as soon as the component is closed
                        dets.addComponentListener(object : ComponentAdapter() {
                            override fun componentHidden(e: ComponentEvent?) {
                                updateDeterminationCount()
                                super.componentHidden(e)
                            }
                        })
                        dets.setVisible(true)
                    }
                })
            }
            return jButtonDeterminations
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val citedInPublicationJTextField: JTextField?
        private get() {
            if (jTextFieldCitedInPub == null) {
                jTextFieldCitedInPub = JTextField()
                jTextFieldCitedInPub.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "CitedInPublication", jTextFieldCitedInPub))
                jTextFieldCitedInPub.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "CitedInPublication"))
                jTextFieldCitedInPub.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldCitedInPub
        }

    private val basicWrapperJScrollPane: JScrollPane
        private get() {
            val pane = JScrollPane()
            pane.addMouseWheelListener(MouseWheelScrollListener(pane))
            val maxHeight: Int = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_MAX_FIELD_HEIGHT).toInt()
            pane.setMaximumSize(Dimension(1000, maxHeight))
            return pane
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneNotes(): JScrollPane? {
        if (jScrollPaneNotes == null) {
            jScrollPaneNotes = basicWrapperJScrollPane
            jScrollPaneNotes.setViewportView(jTextAreaNotes)
            //jScrollPaneNotes.add(getJTextAreaNotes()); //allie!!!
        }
        return jScrollPaneNotes
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val dateEmergedJButton: JButton?
        private get() {
            if (dateEmergedButton == null) {
                dateEmergedButton = JButton()
                dateEmergedButton.setText("Date Emerged")
                dateEmergedButton.setToolTipText("Fill date emerged with data from verbatim date")
                dateEmergedButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (jTextFieldDateNos.Text == "") {
                            jTextFieldDateNos.setText(jTextFieldDateEmerged.Text)
                        } else {
                            jTextFieldDateEmerged.setText(jTextFieldDateNos.Text)
                        }
                    }
                })
            }
            return dateEmergedButton
        }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private val dateCollectedJButton: JButton?
        private get() {
            if (dateCollectedButton == null) {
                dateCollectedButton = JButton()
                dateCollectedButton.setText("Date Collected")
                dateCollectedButton.setToolTipText("Fill date collected with data from verbatim date")
                dateCollectedButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (jTextFieldDateNos.Text == "") {
                            jTextFieldDateNos.setText(jTextFieldDateCollected.Text)
                        } else {
                            jTextFieldDateCollected.setText(jTextFieldDateNos.Text)
                        }
                    }
                })
            }
            return dateCollectedButton
        }

    /**
     * This method initializes jButtonSpecificLocality
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSpecificLocality(): JButton? {
        if (jButtonSpecificLocality == null) {
            jButtonSpecificLocality = JButton()
            jButtonSpecificLocality.setText("Specific Locality")
            jButtonSpecificLocality.setToolTipText("Fill specific locality with data from verbatim locality")
            jButtonSpecificLocality.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (jTextFieldVerbatimLocality.Text == "") {
                        if (jTextFieldLocality.Text == "") { // If both are blank, set the blank value string.
                            jTextFieldLocality.setText("[no specific locality data]")
                        }
                        jTextFieldVerbatimLocality.setText(jTextFieldLocality.Text)
                    } else {
                        jTextFieldLocality.setText(jTextFieldVerbatimLocality.Text)
                    }
                }
            })
        }
        return jButtonSpecificLocality
    }

    private fun getJTextFieldMigrationStatus(): JTextField? {
        if (jTextFieldMigrationStatus == null) {
            jTextFieldMigrationStatus = JTextField()
            //jLabelMigrationStatus.setBackground(null);
//jLabelMigrationStatus.setBorder(null);
            jTextFieldMigrationStatus.setEditable(false)
            jTextFieldMigrationStatus.setText("")
            if (specimen.isStateDone()) {
                val uri = "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.CatNum
                jTextFieldMigrationStatus.setText(uri)
            }
        }
        return jTextFieldMigrationStatus
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldImgCount: JTextField?
        private get() {
            if (jTextFieldImageCount == null) {
                jTextFieldImageCount = JTextField()
                jTextFieldImageCount.setForeground(Color.BLACK)
                jTextFieldImageCount.setEnabled(false)
                updateImageCount()
            }
            return jTextFieldImageCount
        }

    /**
     * Set the image count value into the corresponding field.
     * Set the color to red if there is more than 1 image
     */
    private fun updateImageCount() {
        var imageCount = 0
        if (specimen.ICImages != null) {
            imageCount = specimen.ICImages.size
        }
        jTextFieldImageCount.setText(Integer.toString(imageCount))
        if (imageCount > 1) {
            jTextFieldImageCount.setForeground(Color.RED)
        } else {
            jTextFieldImageCount.setForeground(Color.BLACK)
        }
    }

    private fun getTextFieldMaxElev(): JTextField? {
        if (textFieldMaxElev == null) {
            textFieldMaxElev = JTextField()
        }
        return textFieldMaxElev
    }

    private fun getComboBoxElevUnits(): JComboBox<String?>? {
        if (comboBoxElevUnits == null) {
            comboBoxElevUnits = JComboBox<String?>()
            comboBoxElevUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("", "?", "m", "ft")))
        }
        return comboBoxElevUnits
    }

    private fun getTextFieldMicrohabitat(): JTextField? {
        if (textFieldMicrohabitat == null) {
            textFieldMicrohabitat = JTextField()
        }
        return textFieldMicrohabitat
    }

    private fun autocompleteGeoDataFromGeoreference() {
        val georeff: LatLong = specimen.LatLong.iterator().next()
        if (georeff.DecLat != null && georeff.DecLong != null) { // do it async as the request could take longer than desired
            Thread(Runnable {
                log.debug("Fetching address from openstreetmap")
                val data: MutableMap<String?, Any?> = OpenStreetMapUtility.Companion.Instance.reverseSearchValues(georeff.DecLat, georeff.DecLong, ArrayList<String?>(Arrays.asList(
                        "address.state",
                        "address.country"
                )))
                if (data != null) {
                    log.debug("Got address from openstreetmap: $data")
                    if (countryJTextField.SelectedItem == "") {
                        countryJTextField.setSelectedItem(data["address.country"])
                    } else {
                        log.debug("Won't set country as is '" + countryJTextField.SelectedItem + "'.")
                    }
                    if (primaryDivisionJTextField.SelectedItem == "") {
                        primaryDivisionJTextField.setSelectedItem(data["address.state"])
                    }
                }
            }).start()
        }
    }

    /**
     * This method initializes jTextFieldDateDetermined
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateDetermined(): JTextField? {
        if (jTextFieldDateDetermined == null) {
            jTextFieldDateDetermined = JTextField()
            jTextFieldDateDetermined.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", jTextFieldDateDetermined))
            jTextFieldDateDetermined.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateIdentified"))
            jTextFieldDateDetermined.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateDetermined
    }

    /**
     * This method initializes the Determier pick list.
     *
     * @return FilteringAgentJComboBox
     */
    private fun getJCBDeterminer(): JComboBox<String?>? {
        log.debug("calling getJCBDeterminer() ... it is $jCBDeterminer")
        if (jCBDeterminer == null) {
            log.debug("init jCBDeterminer determiner null, making a new one")
            val sls = SpecimenLifeCycle()
            jCBDeterminer = JComboBox<String?>()
            jCBDeterminer.setModel(DefaultComboBoxModel<String?>(sls.DistinctDeterminers))
            jCBDeterminer.setEditable(true)
            //jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
//jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
            jCBDeterminer.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jCBDeterminer)
        }
        return jCBDeterminer
    }

    /**
     * This method initializes type status pick list
     *
     * @return javax.swing.JTextField
     */
    private fun getCbTypeStatus(): JComboBox<String?>? {
        if (cbTypeStatus == null) {
            cbTypeStatus = JComboBox<String?>(TypeStatus.Companion.TypeStatusValues)
            // cbTypeStatus = new JComboBox(TypeStatus.TypeStatusValues);  // for visual editor
            cbTypeStatus.setEditable(true)
            cbTypeStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "TypeStatus"))
            cbTypeStatus.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return cbTypeStatus
    }

    /**
     * This method initializes jTextFieldIdRemarks
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldIdRemarks(): JTextField? {
        if (jTextFieldIdRemarks == null) {
            jTextFieldIdRemarks = JTextField()
            jTextFieldIdRemarks.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "IdentificationRemarks"))
            jTextFieldIdRemarks.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldIdRemarks
    }

    private fun updateContentDependentLabels() {
        updateJButtonGeoreference()
        updateDeterminationCount()
        updateJButtonPaste()
        updateDBIdLabel()
    }

    companion object {
        private val log: Log = LogFactory.getLog(SpecimenDetailsViewPane::class.java)
        private const val serialVersionUID = 3716072190995030749L
        private const val STATE_CLEAN = 0
        private const val STATE_DIRTY = 1
    }

    /**
     * Construct an instance of a SpecimenDetailsViewPane showing the data present
     * in aSpecimenInstance.
     *
     * @param aSpecimenInstance the Specimen instance to display for editing.
     */
    init {
        specimen = aSpecimenInstance
        val s = SpecimenLifeCycle()
        setStateToClean()
        //		SpecimenPartAttributeLifeCycle spals = new SpecimenPartAttributeLifeCycle();
//		Iterator<SpecimenPart> i = specimen.SpecimenParts.iterator();
//		while (i.hasNext()) {
//			Iterator<SpecimenPartAttribute> ia = i.next().AttributeCollection.iterator();
//			while (ia.hasNext()) {
//				try {
//					SpecimenPartAttribute spa = ia.next();
//					log.debug(spa.SpecimenPartAttributeId);
//					spals.attachDirty(spa);
//					log.debug(spa.SpecimenPartAttributeId);
//				} catch (SaveFailedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
        try {
            s.attachClean(specimen)
            specimenController = aController
            initialize()
            setValues()
        } catch (e: Exception) {
            var status = "Undefined error initializing SpecimenDetails. Restarting Database connection..."
            if (e is SessionException || e is TransactionException) {
                status = "Database Connection Error. Resetting connection... Try again."
            } else if (e is IllegalStateException) {
                status = "Illegal state exception. Last edit possibly lost. Try again."
            } else if (e is OptimisticLockException) {
                status = "Error: last edited entry has been modified externally. Try again."
            }
            Singleton.MainFrame.setStatusMessage(status)
            log.debug(e.message, e)
            HibernateUtil.restartSessionFactory()
            this.setVisible(false)
        }
    }
}
