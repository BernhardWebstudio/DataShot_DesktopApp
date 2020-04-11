/**
 * JobRepeatOCR.java
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

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * JobRepeatOCR
 */
class JobRepeatOCR : RunnableJob, Runnable {
    var counter: Counter? = null // For reporting results
    var listeners: ArrayList<RunnerListener?>? = null
    private var scan = SCAN_ALL // default scan all
    private var startPointSpecific: File? = null // place to start for scan_specific
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStatus()
     */ var status: Int = RunStatus.STATUS_NEW
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getStartTime()
     */ var startTime: Date? = null public get() {
        return field
    }
        private set
    private var percentComplete = 0

    /**
     * Default constructor.  Creates an OCR job to repeat the OCR on all
     * images for all specimens in state OCR.
     */
    constructor() {
        scan = SCAN_ALL
        startPointSpecific = null
        status = RunStatus.STATUS_NEW
        init()
    }

    /**
     * Create a repeat OCR job to bring up dialog to pick a specific directory
     * on which to repeat OCR for specimens in state OCR or to repeat OCR
     * for a specific directory specified by startAt, again for specimens in
     * state OCR.
     *
     *
     * Behavior:
     *
     *
     * whatToScan=SCAN_ALL, startAt is ignored, equivalent to default constructor.
     * whatToScan=SCAN_SELECT, startAt is used as starting point for directory chooser dialog.
     * whatToScan=SCAN_SPECIFIC, startAt is used as starting point for repeat (if null falls back to SCAN_SELECT).
     *
     * @param whatToScan one of SCAN_ALL, SCAN_SPECIFIC, SCAN_SELECT
     * @param startAt    null or a directory starting point.
     */
    constructor(whatToScan: Int, startAt: File?) {
        scan = SCAN_SELECT
        // store startPoint as base for dialog if SCAN_SELECT, or directory to scan if SCAN_SPECIFIC
        if (startAt != null && startAt.canRead()) {
            startPointSpecific = startAt
        }
        if (whatToScan == SCAN_ALL) { // equivalent to default constructor
            scan = SCAN_ALL
            startPointSpecific = null
        }
        if (whatToScan == SCAN_SPECIFIC) {
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

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        log!!.debug("JobRepeatOCR $this  recieved cancel signal")
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#percentComplete()
     */
    override fun percentComplete(): Int {
        return percentComplete
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#registerListener(edu.harvard.mcz.imagecapture.interfaces.RunnerListener)
     */
    override fun registerListener(jobListener: RunnerListener?): Boolean {
        if (listeners == null) {
            init()
        }
        log!!.debug(jobListener)
        return listeners!!.add(jobListener)
    }// Add image for specimen to list to check// run in separate thread and allow cancellation and status reporting
// walk through directory tree
    // Retrieve a list of all specimens in state OCR
//TODO: handle error condition
    //TODO: Filechooser to pick path, then save (if SCAN_ALL) imagebase property.
//Perhaps.  Might be undesirable behavior.
//Probably better to warn that imagebase is null;
    // Check that startPoint is or is within imagebase.
// launch a file chooser dialog to select the directory to scan// A scan start point has been provided, don't launch a dialog.// If it can't be read, null out imagebase// place to start the scan from, imagebase directory for SCAN_ALL
    // If it isn't null, retrieve the image base directory from properties, and test for read access.

    // Find the path in which to include files.
    private val fileList: MutableList<File?>
        private get() {
            var pathToCheck = ""
            if (scan != SCAN_ALL) { // Find the path in which to include files.
                var imagebase: File? = null // place to start the scan from, imagebase directory for SCAN_ALL
                var startPoint: File? = null
                // If it isn't null, retrieve the image base directory from properties, and test for read access.
                if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE) == null) {
                    JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Can't start scan.  Don't know where images are stored.  Set imagbase property.", "Can't Scan.", JOptionPane.ERROR_MESSAGE)
                } else {
                    imagebase = File(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    if (imagebase != null) {
                        if (imagebase.canRead()) {
                            startPoint = imagebase
                        } else { // If it can't be read, null out imagebase
                            imagebase = null
                        }
                    }
                    if (scan == SCAN_SPECIFIC && startPointSpecific != null && startPointSpecific!!.canRead()) { // A scan start point has been provided, don't launch a dialog.
                        startPoint = startPointSpecific
                    }
                    if (imagebase == null || scan == SCAN_SELECT) { // launch a file chooser dialog to select the directory to scan
                        val fileChooser = JFileChooser()
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
                        if (scan == SCAN_SELECT && startPointSpecific != null && startPointSpecific!!.canRead()) {
                            fileChooser.setCurrentDirectory(startPointSpecific)
                        } else {
                            if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH) != null) {
                                fileChooser.setCurrentDirectory(File(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
                            }
                        }
                        val returnValue: Int = fileChooser.showOpenDialog(Singleton.Companion.getSingletonInstance().getMainFrame())
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            val file: File = fileChooser.getSelectedFile()
                            log!!.debug("Selected base directory: " + file.name + ".")
                            startPoint = file
                        } else { //TODO: handle error condition
                            log!!.error("Directory selection cancelled by user.")
                        }
                        //TODO: Filechooser to pick path, then save (if SCAN_ALL) imagebase property.
//Perhaps.  Might be undesirable behavior.
//Probably better to warn that imagebase is null;
                    }
                    // Check that startPoint is or is within imagebase.
                    if (!ImageCaptureProperties.Companion.isInPathBelowBase(startPoint)) {
                        val base: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(
                                ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                        log!!.error("Tried to scan directory (" + startPoint!!.path + ") outside of base image directory (" + base + ")")
                        val message = "Can't scan and database files outside of base image directory ($base)"
                        JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), message, "Can't Scan outside image base directory.", JOptionPane.YES_NO_OPTION)
                    } else { // run in separate thread and allow cancellation and status reporting
// walk through directory tree
                        if (!startPoint!!.canRead()) {
                            JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Can't start scan.  Unable to read selected directory: " + startPoint.path, "Can't Scan.", JOptionPane.YES_NO_OPTION)
                        } else {
                            pathToCheck = ImageCaptureProperties.Companion.getPathBelowBase(startPoint)
                        }
                    }
                }
            }
            // Retrieve a list of all specimens in state OCR
            val sls = SpecimenLifeCycle()
            val specimens: MutableList<Specimen?> = sls.findBy("workFlowStatus", WorkFlowStatus.STAGE_0)
            val files = ArrayList<File?>()
            for (i in specimens.indices) {
                val s: Specimen? = specimens[i]
                val images: MutableSet<ICImage?> = s.getICImages()
                val iter: MutableIterator<ICImage?> = images.iterator()
                while (iter.hasNext() && status != RunStatus.STATUS_TERMINATED) {
                    val image: ICImage? = iter.next()
                    if (scan == SCAN_ALL || image.getPath().startsWith(pathToCheck)) { // Add image for specimen to list to check
                        val imageFile = File(assemblePathWithBase(image.getPath(), image.getFilename()))
                        files.add(imageFile)
                        counter!!.incrementFilesSeen()
                    }
                }
            }
            val message = "Found " + files.size + " Specimen records on which to repeat OCR."
            log!!.debug(message)
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage(message)
            return files
        }

    private fun redoOCR(file: File) {
        log!!.debug(file)
        val filename = file.name
        var rawOCR = ""
        // scan selected file
        var templateToUse: PositionTemplate? = null
        // Figure out which template to use.
        val detector = DefaultPositionTemplateDetector()
        var template = ""
        try {
            template = detector.detectTemplateForImage(file)
        } catch (e3: UnreadableFileException) { // TODO Auto-generated catch block
            e3.printStackTrace()
        }
        try {
            templateToUse = PositionTemplate(template)
            log.debug("Set template to: " + templateToUse.getTemplateId())
        } catch (e1: NoSuchTemplateException) {
            try {
                templateToUse = PositionTemplate(PositionTemplate.Companion.TEMPLATE_DEFAULT)
                log.error("Template not recongised, reset template to: " + templateToUse.getTemplateId())
            } catch (e2: Exception) { // We shouldn't end up here - we just asked for the default template by its constant.
                log.fatal("PositionTemplate doesn't recognize TEMPLATE_DEFAULT")
                log.trace(e2)
                ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
            }
        }
        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Repeat OCR $filename.")
        val scannableFile: CandidateImageFile?
        try {
            scannableFile = CandidateImageFile(file, templateToUse)
            try { // OCR and parse UnitTray Label
                var parser: TaxonNameReturner? = null
                var labelRead: UnitTrayLabel? = null
                var foundQRText = false
                try {
                    labelRead = scannableFile.getTaxonLabelQRText(PositionTemplate("Test template 2"))
                } catch (e: NoSuchTemplateException) {
                    try {
                        labelRead = scannableFile.getTaxonLabelQRText(PositionTemplate("Small template 2"))
                    } catch (e1: NoSuchTemplateException) {
                        log.error("Neither Test template 2 nor Small template 2 found")
                    }
                }
                if (labelRead != null) {
                    rawOCR = labelRead.toJSONString()
                    foundQRText = true
                    parser = labelRead
                } else {
                    log.debug("Failing over to OCR with tesseract")
                    rawOCR = scannableFile.getTaxonLabelOCRText(templateToUse)
                    parser = UnitTrayLabelParser(rawOCR)
                    foundQRText = (parser as UnitTrayLabelParser?).isParsedFromJSON()
                }
                log.debug(rawOCR)
                // Test this image to see if is a specimen image
                var barcode: String = scannableFile.getBarcodeText(templateToUse)
                Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Checking $barcode.")
                if (scannableFile.getCatalogNumberBarcodeStatus() != CandidateImageFile.Companion.RESULT_BARCODE_SCANNED) {
                    log.error("Error scanning for barcode: $barcode")
                    barcode = ""
                }
                println("Barcode=$barcode")
                val exifComment: String = scannableFile.getExifUserCommentText()
                var isSpecimenImage = false
                if (Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(exifComment)
                        || Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(barcode)) {
                    isSpecimenImage = true
                    println("Specimen Image")
                }
                var rawBarcode = barcode
                // Check for mismatch in barcode and comment
                if (rawBarcode != exifComment) { // Use the exifComment if it is a barcode
                    var barcodeInImageMetadata = false
                    if (Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(exifComment)) {
                        rawBarcode = exifComment
                        barcodeInImageMetadata = true
                    }
                    // Log the mismatch
                    logMismatch(counter, filename, barcode, exifComment, parser, barcodeInImageMetadata, log)
                }
                if (isSpecimenImage && Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(rawBarcode)) { // Parse and store OCR in an updated specimen record.
                    Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Updating $barcode.")
                    val sls = SpecimenLifeCycle()
                    val specimenSearch = Specimen()
                    specimenSearch.setBarcode(rawBarcode)
                    val specimens: MutableList<Specimen?> = sls.findByExample(specimenSearch)
                    log.debug("Found " + specimens.size + " for barcode " + rawBarcode)
                    if (specimens.size == 1) { // Only update if we got a single match back on the barcode search.
                        var s: Specimen? = specimens[0]
                        log.debug("Found " + s.getBarcode() + " at state " + s.getWorkFlowStatus())
                        if (s.getWorkFlowStatus() == WorkFlowStatus.STAGE_0) { // Only update if the result was in state OCR.
//
// Look up likely matches for the OCR of the higher taxa in the HigherTaxon authority file.
                            AbstractFileScanJob.extractFamilyToSpecimen(parser, s)
                            if (parser.getFamily() != "") { // check family against database (with a soundex match)
                                val hls = HigherTaxonLifeCycle()
                                val match: String = hls.findMatch(parser.getFamily())
                                if (match != null && match.trim { it <= ' ' } != "") {
                                    s.setFamily(match)
                                }
                            }
                            // trim family to fit (in case multiple parts of taxon name weren't parsed
// and got concatenated into family field.
                            setBasicSpecimenFromParser(parser, s)
                            if (s.getCreatingPath() == null || s.getCreatingPath().length == 0) {
                                s.setCreatingPath(ImageCaptureProperties.Companion.getPathBelowBase(file))
                            }
                            if (s.getCreatingFilename() == null || s.getCreatingFilename().length == 0) {
                                s.setCreatingFilename(file.name)
                            }
                            if (parser.getIdentifiedBy() != null && parser.getIdentifiedBy().length > 0) {
                                s.setIdentifiedBy(parser.getIdentifiedBy())
                            }
                            log.debug(s.getCollection())
                            // TODO: non-general workflows
                            s.setLocationInCollection(LocationInCollection.getDefaultLocation())
                            if (s.getFamily() == "Formicidae") {
                                s.setLocationInCollection(LocationInCollection.GENERALANT)
                            }
                            s.setCreatedBy(ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion())
                            val sh = SpecimenLifeCycle()
                            try { // *** Save a database record of the specimen.
                                log.debug("Saving changes for barcode $barcode")
                                if (foundQRText) { // if we managed to read JSON, then we can move the specimen to text entered.
                                    s.setWorkFlowStatus(WorkFlowStatus.STAGE_1)
                                    log.debug(s.getWorkFlowStatus())
                                }
                                sh.attachDirty(s)
                                counter!!.incrementSpecimensUpdated()
                            } catch (e: SaveFailedException) { // couldn't save, try to figure out why and report
                                log.debug(e)
                                try {
                                    var badParse = ""
                                    // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                                    if ((parser as DrawerNameReturner?).getDrawerNumber().length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) {
                                        badParse = "Parsing problem. \nDrawer number is too long: " + s.getDrawerNumber() + "\n"
                                    }
                                    val error = RunnableJobError(filename, barcode,
                                            rawBarcode, exifComment, badParse,
                                            parser, parser as DrawerNameReturner?,
                                            null, RunnableJobError.Companion.TYPE_BAD_PARSE)
                                    counter!!.appendError(error)
                                } catch (err: Exception) {
                                    log.error(e)
                                    log.error(err)
                                    // TODO: Add a general error handling/inform user class.
                                    var badParse = ""
                                    // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                                    if (s.getDrawerNumber() == null) {
                                        badParse = "Parsing problem. \nDrawer number is null: \n"
                                    } else {
                                        if (s.getDrawerNumber().length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) { // This was an OK test for testing OCR, but in production ends up in records not being
// created for files, which ends up being a larger quality control problem than records
// with bad OCR.
// Won't fail this way anymore - drawer number is now enforced in Specimen.setDrawerNumber()
                                            badParse = "Parsing problem. \nDrawer number is too long: " + s.getDrawerNumber() + "\n"
                                        }
                                    }
                                    val error = RunnableJobError(filename, barcode,
                                            rawBarcode, exifComment, badParse,
                                            parser, parser as DrawerNameReturner?,
                                            err, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                                    counter!!.appendError(error)
                                    counter!!.incrementFilesFailed()
                                    s = null
                                }
                            }
                        } else {
                            log.debug("Didn't try to save, not at workflow status OCR.")
                            val error = RunnableJobError(filename, barcode,
                                    rawBarcode, exifComment, "Didn't try to save, not at workflow status OCR",
                                    parser, parser as DrawerNameReturner?,
                                    null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                            counter!!.appendError(error)
                        }
                    }
                } else {
                    log.debug("Didn't try to save, not a specimen image.")
                    val error = RunnableJobError(filename, barcode,
                            rawBarcode, exifComment, "Didn't try to save, not a specimen image, or rawBarcode doesn't match pattern",
                            parser, parser as DrawerNameReturner?,
                            null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                    counter!!.appendError(error)
                }
            } catch (ex: Exception) {
                println(ex.message)
            }
        } catch (e1: UnreadableFileException) {
            log.error("Unable to read selected file." + e1.message)
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.Companion.getSingletonInstance().getJobList().addJob(this)
        counter = Counter()
        // Obtain a list of image files to repeat OCR
// (by querying specimens in state OCR and getting list of linked images).
        val files = fileList
        log!!.debug("repeatOCRJob started$this")
        var i = 0
        while (i < files.size && status != RunStatus.STATUS_TERMINATED) { // Find out how far along the process is
            val seen = 0.0f + i
            val total = 0.0f + files.size
            percentComplete = (seen / total * 100).toInt()
            setPercentComplete(percentComplete)
            // Repeat the OCR for the present file.
            redoOCR(files[i]!!)
            i++
        }
        if (status != RunStatus.STATUS_TERMINATED) {
            setPercentComplete(100)
        }
        Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(status, this)
        report()
        done()
    }

    private fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        log!!.debug(percentComplete)
        //notify listeners
        Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(percentComplete, this)
        val i: MutableIterator<RunnerListener?> = listeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(percentComplete, this)
        }
    }

    /**
     * Cleanup when job is complete.
     */
    private fun done() {
        Singleton.Companion.getSingletonInstance().getJobList().removeJob(this)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    override fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    override fun run() {
        start()
    }

    private fun report() {
        var report = "Results of redone OCR on Image files.\n"
        report += "Found  " + counter!!.filesSeen + " specimen database records in state OCR.\n"
        report += "Saved new OCR for " + counter!!.specimensUpdated + " specimens.\n"
        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("OCR re-do complete.")
        val errorReportDialog = RunnableJobReportDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), report, counter!!.errors, "Repeat OCR Results")
        errorReportDialog.setVisible(true)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = if (scan == SCAN_ALL) {
            "Redo OCR for All specimens in state " + WorkFlowStatus.STAGE_0
        } else {
            "Redo OCR for specimens in state " + WorkFlowStatus.STAGE_0
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
        private val log = LogFactory.getLog(JobRepeatOCR::class.java)
    }
}