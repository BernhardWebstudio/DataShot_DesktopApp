/**
 * LoginDialog.java
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
 */
package edu.harvard.mcz.imagecapture.ui.dialog


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog
import edu.harvard.mcz.imagecapture.utility.HashUtility
import net.miginfocom.swing.MigLayout
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.*

java.lang.Exception
import java.net.URL


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
 * A database login dialog, including both username/password credentials and
 * specification of the database connection parameters.
 */
class LoginDialog : JDialog() {
    private var self: JDialog? = null
    var result = RESULT_LOGIN
        private set
    private var jPanel: JPanel? = null
    private var jTextFieldDBUsername: JTextField? = null
    private var jPasswordFieldDB: JPasswordField? = null
    private var jTextFieldDriver: JTextField? = null
    private var jTextFieldConnection: JTextField? = null
    private var jTextFieldDialect: JTextField? = null
    private var jButtonLogin: JButton? = null
    private var jButtonCancel: JButton? = null
    private var jTextFieldEmail: JTextField? = null
    private var jPasswordFieldUser: JPasswordField? = null
    private var jButton2: JButton? = null
    private var jPanelAdvanced: JPanel? = null
    private var jLabelStatus: JLabel? = null
    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setContentPane(getJPanel())
        this.setTitle("DataShot Login Dialog: Configured For: " +
                Singleton
                        .Properties
                        .Properties
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION))
        val iconFile = this.javaClass.getResource(
                "/edu/harvard/mcz/imagecapture/resources/images/icon.png")
        try {
            setIconImage(ImageIcon(iconFile).Image)
        } catch (e: Exception) {
            log.error(e)
        }
        if (Singleton
                        .Properties
                        .Properties
                        .getProperty(ImageCaptureProperties.Companion.KEY_LOGIN_SHOW_ADVANCED)
                        .equals("false", ignoreCase = true)) {
            jPanelAdvanced.setVisible(false)
            this.setSize(Dimension(650, 225))
        } else {
            jPanelAdvanced.setVisible(true)
            this.setSize(Dimension(650, 355))
        }
        this.RootPane.setDefaultButton(jButtonLogin)
        val screenSize: Dimension = Toolkit.DefaultToolkit.ScreenSize
        this.setLocation((screenSize.width - this.Width) / 2,
                (screenSize.height - this.Height) / 2)
        //    this.pack();
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel()
            jPanel.setLayout(MigLayout("wrap 2, fill"))
            val keyImageLabel = JLabel()
            val iconFile = this.javaClass.getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/key_small.png")
            try { // this.setIconImage(new ImageIcon(iconFile).Image);
                keyImageLabel.setIcon(ImageIcon(iconFile))
            } catch (e: Exception) {
                println("Can't open icon file: $iconFile")
            }
            // row
            jPanel.add(keyImageLabel)
            val loginPrompt = JLabel("Login & connect to database")
            val f: Font = loginPrompt.Font
            // bold
            loginPrompt.setFont(f.deriveFont(f.Style or Font.BOLD))
            jPanel.add(loginPrompt)
            // row
            val emailLabel = JLabel("E-Mail/Username")
            jPanel.add(emailLabel)
            println(getJTextFieldEmail())
            jPanel.add(getJTextFieldEmail(), "growx")
            // row
            val passwordLabel = JLabel("Password")
            jPanel.add(passwordLabel)
            jPanel.add(getJPasswordFieldUser(), "growx")
            // row
            jLabelStatus = JLabel()
            jPanel.add(jLabelStatus, "span 2, wrap")
            // row
            val dbLabel = JLabel("Database")
            jPanel.add(dbLabel)
            jPanel.add(advancedSettingsJButton, "wrap")
            // row
            jPanel.add(getJButtonCancel(), "tag cancel, align left")
            jPanel.add(getJButtonLogin(), "tag ok, align right")
            // row
            jPanel.add(getJPanelAdvanced(), "grow, span 2, wrap")
        }
        return jPanel
    }

    /**
     * This method initializes jTextFieldSchemaName if necessary
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldSchemaName: JTextField?
        private get() {
            if (jTextFieldDBUsername == null) {
                jTextFieldDBUsername = JTextField()
            }
            return jTextFieldDBUsername
        }

    /**
     * This method initializes jPasswordFieldDB
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordFieldDB(): JPasswordField? {
        if (jPasswordFieldDB == null) {
            jPasswordFieldDB = JPasswordField()
        }
        return jPasswordFieldDB
    }

    /**
     * This method initializes jTextFieldDriver
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDriver(): JTextField? {
        if (jTextFieldDriver == null) {
            jTextFieldDriver = JTextField("com.mysql.cj.jdbc.Driver")
        }
        return jTextFieldDriver
    }

    /**
     * This method initializes jTextFieldConnection
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldConnection(): JTextField? {
        if (jTextFieldConnection == null) {
            jTextFieldConnection = JTextField("jdbc:mysql://localhost:3306/lepidoptera")
        }
        return jTextFieldConnection
    }

    /**
     * This method initializes jTextFieldDialect
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDialect(): JTextField? {
        if (jTextFieldDialect == null) {
            jTextFieldDialect = JTextField("org.hibernate.dialect.MySQLDialect")
        }
        return jTextFieldDialect
    }

    /**
     * This method initializes jButtonLogin
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonLogin(): JButton? {
        if (jButtonLogin == null) {
            jButtonLogin = JButton("Login")
            jButtonLogin.setMnemonic(KeyEvent.VK_L)
            jButtonLogin.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    result = RESULT_LOGIN
                    jButtonLogin.grabFocus()
                    userPasswordHash
                    self.setVisible(false)
                }
            })
        }
        return jButtonLogin
    }

    /**
     * This method initializes jButtonCancel
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonCancel(): JButton? {
        if (jButtonCancel == null) {
            jButtonCancel = JButton("Cancel")
            jButtonCancel.setMnemonic(KeyEvent.VK_C)
            jButtonCancel.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    result = RESULT_CANCEL
                    self.setVisible(false)
                }
            })
        }
        return jButtonCancel
    }

    /**
     * Obtain sha1 hash of the text in the user's Password field.  Assumes that
     * the text is in utf-8 encoding.  If SHA-1 isn't an available MessageDigest,
     * returns the plain text of the password.
     *
     * @return the SHA1 hash of the text in the (user)Password field.
     */
    val userPasswordHash: String?
        get() = hashPassword(jPasswordFieldUser)

    val username: String?
        get() = jTextFieldEmail.Text

    var dBUserName: String?
        get() = jTextFieldDBUsername.Text
        set(aDBSchemaName) {
            jTextFieldDBUsername.setText(aDBSchemaName)
        }

    // Force advanced panel to open if no database password is stored.
    var dBPassword: String?
        get() = String(jPasswordFieldDB.Password)
        set(aDBPassword) {
            jPasswordFieldDB.setText(aDBPassword)
            // Force advanced panel to open if no database password is stored.
            if (aDBPassword == null || aDBPassword.length == 0) {
                jPanelAdvanced.setVisible(true)
            }
        }

    /**
     * @param textFieldDriver the jTextFieldDriver to set
     */
    var driver: String?
        get() = jTextFieldDriver.Text
        set(textFieldDriver) {
            jTextFieldDriver.setText(textFieldDriver)
        }

    /**
     * @param textFieldConnection the jTextFieldConnection to set
     */
    var connection: String?
        get() = jTextFieldConnection.Text
        set(textFieldConnection) {
            jTextFieldConnection.setText(textFieldConnection)
        }

    /**
     * @param textFieldDialect the jTextFieldDialect to set
     */
    var dialect: String?
        get() = jTextFieldDialect.Text
        set(textFieldDialect) {
            jTextFieldDialect.setText(textFieldDialect)
        }

    fun setStatus(aStatus: String?) {
        jLabelStatus.setText(aStatus)
        jLabelStatus.updateUI()
    }

    /**
     * This method initializes jTextFieldEmail
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldEmail(): JTextField? {
        if (jTextFieldEmail == null) {
            jTextFieldEmail = JTextField()
        }
        return jTextFieldEmail
    }

    /**
     * This method initializes jPasswordFieldUser
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordFieldUser(): JPasswordField? {
        if (jPasswordFieldUser == null) {
            jPasswordFieldUser = JPasswordField()
        }
        return jPasswordFieldUser
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private val advancedSettingsJButton: JButton?
        private get() {
            if (jButton2 == null) {
                jButton2 = JButton()
                jButton2.setText("Advanced")
                jButton2.setMnemonic(KeyEvent.VK_A)
                jButton2.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        toggleAdvanced()
                    }
                })
            }
            return jButton2
        }

    fun toggleAdvanced() {
        if (jPanelAdvanced.isVisible()) {
            this.setSize(Dimension(650, 225))
            jPanelAdvanced.setVisible(false)
        } else {
            this.setSize(Dimension(650, 355))
            jPanelAdvanced.setVisible(true)
        }
        //    this.pack();
    }

    /**
     * This method initializes jPanelAdvanced
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelAdvanced(): JPanel? {
        if (jPanelAdvanced == null) {
            val jLabel4 = JLabel()
            jLabel4.setText("Dialect")
            val jLabel3 = JLabel()
            jLabel3.setText("Connection")
            val jLabel2 = JLabel()
            jLabel2.setText("Driver")
            val jLabel1 = JLabel()
            jLabel1.setText("DBPassword")
            val jLabel = JLabel()
            jLabel.setText("Username")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.anchor = GridBagConstraints.WEST
            gridBagConstraints3.gridx = 1
            gridBagConstraints3.gridy = 1
            gridBagConstraints3.weightx = 1.0
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.gridy = 1
            gridBagConstraints4.insets = Insets(0, 15, 0, 0)
            gridBagConstraints4.gridx = 0
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.gridx = 1
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.gridy = 0
            gridBagConstraints.gridx = 0
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.anchor = GridBagConstraints.WEST
            gridBagConstraints10.gridx = 1
            gridBagConstraints10.gridy = 3
            gridBagConstraints10.weightx = 1.0
            gridBagConstraints10.fill = GridBagConstraints.BOTH
            val gridBagConstraints14 = GridBagConstraints()
            gridBagConstraints14.anchor = GridBagConstraints.WEST
            gridBagConstraints14.gridx = 1
            gridBagConstraints14.gridy = 2
            gridBagConstraints14.fill = GridBagConstraints.BOTH
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.anchor = GridBagConstraints.EAST
            gridBagConstraints9.gridy = 3
            gridBagConstraints9.gridx = 0
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.anchor = GridBagConstraints.WEST
            gridBagConstraints6.gridx = 1
            gridBagConstraints6.gridy = 2
            gridBagConstraints6.weightx = 1.0
            gridBagConstraints6.fill = GridBagConstraints.BOTH
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.anchor = GridBagConstraints.EAST
            gridBagConstraints5.gridy = 2
            gridBagConstraints5.gridx = 0
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.anchor = GridBagConstraints.WEST
            gridBagConstraints8.gridx = 1
            gridBagConstraints8.gridy = 5
            gridBagConstraints8.weightx = 1.0
            gridBagConstraints8.fill = GridBagConstraints.BOTH
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.gridy = 5
            gridBagConstraints7.insets = Insets(0, 5, 0, 0)
            jPanelAdvanced = JPanel()
            jPanelAdvanced.setLayout(GridBagLayout())
            jPanelAdvanced.add(jLabel3, gridBagConstraints7)
            jPanelAdvanced.add(getJTextFieldConnection(), gridBagConstraints8)
            jPanelAdvanced.add(jLabel2, gridBagConstraints5)
            jPanelAdvanced.add(getJTextFieldDriver(), gridBagConstraints6)
            jPanelAdvanced.add(jLabel4, gridBagConstraints9)
            jPanelAdvanced.add(getJTextFieldDialect(), gridBagConstraints10)
            jPanelAdvanced.add(jLabel, gridBagConstraints)
            jPanelAdvanced.add(jTextFieldSchemaName, gridBagConstraints1)
            jPanelAdvanced.add(jLabel1, gridBagConstraints4)
            jPanelAdvanced.add(getJPasswordFieldDB(), gridBagConstraints3)
        }
        return jPanelAdvanced
    }

    companion object {
        const val RESULT_CANCEL = 0
        const val RESULT_LOGIN = 1
        private const val serialVersionUID = -2016769537635603794L
        private val log: Log = LogFactory.getLog(LoginDialog::class.java)
        fun hashPassword(ajPasswordField: JPasswordField): String? {
            return HashUtility.getSHA1Hash(String(ajPasswordField.Password))
        }
    }

    /**
     * Default constructor.  Produces a login dialog.
     */
    init {
        self = this
        initialize()
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
