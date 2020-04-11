/**
 * JobAllImageFilesScan.java
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
package edu.harvard.mcz.imagecapture.jobsimport

import edu.harvard.mcz.imagecapture.jobs.Counter
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import java.util.concurrent.locks.Lock

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * Check all image files either under the image root directory or in a selected directory
 * and add records for files that aren't yet known to the database that contain barcode
 * information and add corresponding specimen records for new specimens.
 */
class JobAllImageFilesScan : AbstractFileScanJob {
    protected var percentComplete = 0
    protected var listeners: ArrayList<RunnerListener?> = ArrayList<RunnerListener?>()
    private var scan = SCAN_ALL // default scan all
    private var startPointSpecific: File? = null // place to start for scan_specific
    private var startPoint: File? = null // start point used.
    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getStatus
     */
    var status: Int = RunStatus.STATUS_NEW
        private set
    private var thumbnailCounter: AtomicInteger? = null
    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getStartTime
     */
    var startTime: Date? = null
        private set

    /**
     * Default constructor, creates a job to scan all of imagebase, unless imagebase is
     * unreadable or undefined, in which case a directory chooser dialog is launched.
     */
    constructor() : super() {
        startPointSpecific = null
        thumbnailCounter = AtomicInteger(0)
        init()
    }

    /**
     * Create a scan job to bring up dialog to pick a specific directory to scan, or
     * to scan a specific directory specified by startAt.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for scan (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) : super() {
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_ALL) { // equivalent to default constructor
            scan = SCAN_ALL
            startPointSpecific = null
        } else if (whatToScan == SCAN_SPECIFIC) {
            scan = if (startAt != null && startAt.canRead()) {
                SCAN_SPECIFIC
            } else {
                SCAN_SELECT
            }
        }
        status = RunStatus.STATUS_NEW
        init()
    }

    protected fun init() {
        listeners = ArrayList<RunnerListener?>()
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.cancel
     */
    fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.registerListener
     */
    fun registerListener(jobListener: RunnerListener?): Boolean {
        if (listeners == null) {
            init()
        }
        return listeners.add(jobListener)
    }

    /**
     * Start this job by setting up the directories, making sure all are readable and start pointers are set correctly
     *
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.start
     */
    fun start() {
        startTime = Date()
        Singleton.Companion.getSingletonInstance().getJobList().addJob(this)
        status = RunStatus.STATUS_RUNNING
        startPoint = null
        // place to start the scan from, imageBase directory for SCAN_ALL
        val imageBasePath: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
        var imageBase: File? = null
        if (imageBasePath != null) {
            imageBase = File(imageBasePath)
        }
        // If it isn't null, retrieve the image base directory from properties, and test for read access.
        if (imageBase == null || !imageBase.canRead()) {
            JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Can't start scan. Don't know where images are stored or can't read. Set imagebase property '" + ImageCaptureProperties.Companion.KEY_IMAGEBASE + "'. Does the path exist?", "Can't Scan.", JOptionPane.ERROR_MESSAGE)
            return
        }
        startPoint = imageBase
        // setup directory scan in case of SCAN_SPECIFIC
        if (scan == SCAN_SPECIFIC && startPointSpecific != null && startPointSpecific!!.canRead()) { // A scan start point has been provided, don't launch a dialog.
            startPoint = startPointSpecific
        }
        // ask for file directory to scan in case of SCAN_SELECT
        if (scan == SCAN_SELECT) { // launch a file chooser dialog to select the directory to scan
            var searchStartPoint: File? = null
            if (startPointSpecific != null && startPointSpecific!!.canRead()) {
                searchStartPoint = startPointSpecific
            } else {
                if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH) != null) {
                    searchStartPoint = File(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH))
                }
            }
            startPoint = FileUtility.askForDirectory(searchStartPoint)
            if (startPoint == null) {
                JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Can't scan without a directory.", "Error: No scanning possible.", JOptionPane.ERROR_MESSAGE)
                return
            }
        }
        // Check that fileToCheck is within imageBase.
        if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
            val base: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(
                    ImageCaptureProperties.Companion.KEY_IMAGEBASE)
            log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
            val message = "Should and will not scan and database files outside of base image directory ($base)"
            JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), message, "Won't scan outside image base directory.", JOptionPane.ERROR_MESSAGE)
            return
        } else {
            if (!startPoint!!.canRead()) {
                JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Can't start scan.  Unable to read selected directory: " + startPoint!!.path, "Can't Scan.", JOptionPane.ERROR_MESSAGE)
                return
            }
            // walk through directory tree
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Scanning path: " + startPoint!!.path)
            val counter = Counter()
            // count files to scan
            countFiles(startPoint!!, counter)
            setPercentComplete(0)
            Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(status, this)
            counter.incrementDirectories()
            // scan
            if (status != RunStatus.STATUS_TERMINATED) {
                checkFiles(startPoint!!, counter)
            }
            // report
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Preprocess scan complete")
            setPercentComplete(100)
            Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(status, this)
            val errorReportDialog = RunnableJobReportDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), counter.toString(), counter.errors, "Preprocess Results")
            errorReportDialog.setVisible(true)
        }
        val sls = SpecimenLifeCycle()
        Singleton.Companion.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount())
        done()
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.stop
     */
    fun stop(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        return false
    }

    fun percentComplete(): Int {
        return percentComplete
    }

    /**
     * Count the files in a directory, recursively
     *
     * @param startPoint the path to start
     * @param counter    the counter to increment
     */
    private fun countFiles(startPoint: File, counter: Counter) { // count files to preprocess
        val containedFiles = startPoint.listFiles()
        if (containedFiles != null) {
            for (fileToCheck in containedFiles) {
                if (fileToCheck.isDirectory) {
                    if (fileToCheck.canRead()) {
                        countFiles(fileToCheck, counter)
                    }
                } else {
                    counter.incrementTotal()
                }
            }
        }
    }

    /**
     * Do the actual processing of each files
     *
     * @param startPoint the directory with all files to handle
     * @param counter    the counter to increment
     */
    private fun checkFiles(startPoint: File, counter: Counter) { // pick jpeg files
// for each file check name against database, if not found, check barcodes, scan and parse text, create records.
        val containedFiles = startPoint.listFiles()
        if (containedFiles == null) {
            log!!.error("Directory " + startPoint.path + " contains 0 entries.")
            return
        }
        log!!.debug("Scanning directory: " + startPoint.path + " containing " + containedFiles.size + " files.")
        val localCounter = AtomicCounter()
        // limit number of parallel threads because we can. And to help Hibernate from dying
        val concurrencyLevel: Int = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_CONCURRENCY_LEVEL, "16").toInt()
        val es: ExecutorService = Executors.newFixedThreadPool(concurrencyLevel)
        val locks: Array<Lock?> = arrayOfNulls<ReentrantLock?>(concurrencyLevel)
        for (i in locks.indices) {
            locks[i] = ReentrantLock()
        }
        // create thumbnails in a separate thread (if requested)
        if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_GENERATE_THUMBNAILS) == "true") {
            es.execute(ThumbnailBuilderJob(startPoint, thumbnailCounter))
        }
        // loop files
        for (containedFile in containedFiles) {
            if (status != RunStatus.STATUS_TERMINATED) {
                Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Scanning: " + containedFile.name)
                log.debug("Scanning: " + containedFile.name)
                if (containedFile.isDirectory) { // recursive read for all files: start anew for directories
                    if (containedFile.canRead()) { // Skip thumbs directories
                        if (containedFile.name != "thumbs") {
                            checkFiles(containedFile, counter)
                            counter.incrementDirectories()
                        }
                    } else {
                        counter.appendError(RunnableJobError(containedFile.name, "", "Could not read directory", UnreadableFileException(), RunnableJobError.Companion.TYPE_FILE_READ))
                        counter.incrementDirectoriesFailed()
                    }
                } else { // check JPEG files for barcodes
                    if (!containedFile.name.matches(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))) {
                        log.debug("Skipping file [" + containedFile.name + "], doesn't match expected filename pattern " + Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))
                    } else { // images belonging to the same specimen creating the same specimen resp. looking for it
// that's why we have here a rather ugly lock handling mechanism
                        es.execute(Runnable { checkFile(containedFile, localCounter, locks) })
                    }
                }
                // report progress
                Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Scanned: " + containedFile.name)
                val seen: Float = 0.0f + localCounter.getFilesSeen()
                val total = 0.0f + counter.total
                // thumbPercentComplete = (int) ((seen/total)*100);
                setPercentComplete((seen / total * 100).toInt())
                log.debug("Setting percent complete: " + seen / total * 100 + " based on seen: " + seen + ", total: " + total)
            }
            Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(status, this)
        }
        // assemble all threads
        es.shutdown() // Disable new tasks from being submitted
        try {
            if (!es.awaitTermination(60, TimeUnit.MINUTES)) {
                es.shutdownNow() // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!es.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Execution pool did not terminate")
                }
            }
        } catch (e: InterruptedException) {
            log.error("Execution pool did not terminate", e)
            es.shutdownNow()
            Thread.currentThread().interrupt()
        }
        counter.mergeIn(localCounter)
    }

    /**
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob.getName
     */
    val name: String
        get() = if (scan == SCAN_ALL) {
            "Preprocess all image files"
        } else {
            var sp = startPoint!!.path
            if (sp == null || sp.length == 0) {
                sp = startPointSpecific!!.path
            }
            "Preprocess images in $sp"
        }

    /**
     * Set the completeness percentage in main frame & notify listeners
     *
     * @param aPercentage the progress percentage
     */
    protected fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        //notify listeners
        Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(percentComplete, this)
        for (listener in listeners) {
            listener.notifyListener(percentComplete, this)
        }
    }

    companion object {
        /**
         * Scan all of image base directory tree.
         */
        const val SCAN_ALL = 0
        /**
         * Open a dialog and scan a specific directory.
         */
        const val SCAN_SELECT = 1
        /**
         * From a file, scan a specific directory.
         */
        const val SCAN_SPECIFIC = 2
        private val log = LogFactory.getLog(JobAllImageFilesScan::class.java)
    }
}