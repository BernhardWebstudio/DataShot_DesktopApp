/**
 * JobVerbatimFieldLoad.java
 *
 *
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
package edu.harvard.mcz.imagecapture.loaderimport

import edu.harvard.mcz.imagecapture.jobs.Counter
import edu.harvard.mcz.imagecapture.loader.ex.LoadException
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.logging.LogFactory
import java.io.File
import java.io.Reader
import java.util.*
import kotlin.collections.HashMap

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 *
 */
class JobVerbatimFieldLoad : RunnableJob, Runnable {
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
    private var listeners: MutableList<RunnerListener?>? = null
    private var counter: Counter? = null
    private var errors: StringBuffer? = null
    private var file: File? = null

    constructor() {
        init()
    }

    constructor(fileToLoad: File?) {
        file = fileToLoad
        init()
    }

    protected fun init() {
        listeners = ArrayList<RunnerListener?>()
        counter = Counter()
        status = RunStatus.STATUS_NEW
        percentComplete = 0
        startTime = null
        errors = StringBuffer()
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    override fun run() {
        log!!.debug(this.toString())
        start()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#start()
     */
    override fun start() {
        startTime = Date()
        Singleton.Companion.getSingletonInstance().getJobList().addJob(this)
        status = RunStatus.STATUS_RUNNING
        var selectedFilename = ""
        if (file == null) {
            val fileChooser = JFileChooser()
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
            if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH) != null) {
                fileChooser.setCurrentDirectory(File(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH)))
            }
            val returnValue: Int = fileChooser.showOpenDialog(Singleton.Companion.getSingletonInstance().getMainFrame())
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile()
            }
        }
        if (file != null) {
            log!!.debug("Selected file to load: " + file!!.name + ".")
            if (file!!.exists() && file!!.isFile && file!!.canRead()) { // Save location
                Singleton.Companion.getSingletonInstance().getProperties().getProperties().setProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH, file!!.path)
                selectedFilename = file!!.name
                var headers = arrayOf<String?>()
                var csvFormat = CSVFormat.DEFAULT.withHeader(*headers)
                var rows = 0
                try {
                    rows = readRows(file!!, csvFormat)
                } catch (e: FileNotFoundException) {
                    JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Unable to load data, file not found: " + e.message, "Error: File Not Found", JOptionPane.OK_OPTION)
                    errors!!.append("File not found ").append(e.message).append("\n")
                    log.error(e.message, e)
                } catch (e: IOException) {
                    errors!!.append("Error loading csv format, trying tab delimited: ").append(e.message).append("\n")
                    log.debug(e.message)
                    try { // try reading as tab delimited format, if successful, use that format.
                        val tabFormat = CSVFormat.newFormat('\t')
                                .withIgnoreSurroundingSpaces(true)
                                .withHeader(*headers)
                                .withQuote('"')
                        rows = readRows(file!!, tabFormat)
                        csvFormat = tabFormat
                    } catch (e1: IOException) {
                        errors!!.append("Error Loading data: ").append(e1.message).append("\n")
                        log.error(e.message, e1)
                    }
                }
                try {
                    val reader: Reader = FileReader(file)
                    val csvParser = CSVParser(reader, csvFormat)
                    val csvHeader = csvParser.headerMap
                    headers = arrayOfNulls<String?>(csvHeader!!.size)
                    var i = 0
                    for (header in csvHeader.keys) {
                        headers[i++] = header
                        log.debug(header)
                    }
                    var okToRun = true
                    //TODO: Work picking/checking responsibility into a FieldLoaderWizard
                    val headerList = Arrays.asList(*headers)
                    if (!headerList.contains("barcode")) {
                        log.error("Input file " + file!!.name + " header does not contain required field 'barcode'.")
                        // no barcode field, we can't match the input to specimen records.
                        errors!!.append("Field \"barcode\" not found in csv file headers.  Unable to load data.").append("\n")
                        okToRun = false
                    }
                    if (okToRun) {
                        val iterator = csvParser.iterator()
                        val fl = FieldLoader()
                        if (headerList.size == 3 && headerList.contains("verbatimUnclassifiedText")
                                && headerList.contains("questions") && headerList.contains("barcode")) {
                            log.debug("Input file matches case 1: Unclassified text only.")
                            // Allowed case 1a: unclassified text only
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatimUnclassifiedText", "Verbatim unclassified Field found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimUnclassifiedText = record!!["verbatimUnclassifiedText"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimUnclassifiedText, questions, true)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else if (headerList.size == 4 && headerList.contains("verbatimUnclassifiedText")
                                && headerList.contains("questions") && headerList.contains("barcode")
                                && headerList.contains("verbatimClusterIdentifier")) {
                            log.debug("Input file matches case 1: Unclassified text only (with cluster identifier).")
                            // Allowed case 1b: unclassified text only (including cluster identifier)
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatimUnclassifiedText", "Verbatim unclassified Field found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimUnclassifiedText = record!!["verbatimUnclassifiedText"]
                                        val verbatimClusterIdentifier = record["verbatimClusterIdentifier"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimUnclassifiedText, verbatimClusterIdentifier, questions, true)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else if (headerList.size == 8 && headerList.contains("verbatimUnclassifiedText") && headerList.contains("questions") && headerList.contains("barcode")
                                && headerList.contains("verbatimLocality") && headerList.contains("verbatimDate") && headerList.contains("verbatimNumbers")
                                && headerList.contains("verbatimCollector") && headerList.contains("verbatimCollection")) { // Allowed case two, transcription into verbatim fields, must be exact list of all
// verbatim fields, not including cluster identifier or other metadata.
                            log.debug("Input file matches case 2: Full list of verbatim fields.")
                            val confirm: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                    "Confirm load from file $selectedFilename ($rows rows) with just barcode and verbatim fields.", "Verbatim Fields found for load", JOptionPane.OK_CANCEL_OPTION)
                            if (confirm == JOptionPane.OK_OPTION) {
                                var barcode = ""
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    counter!!.incrementSpecimenDatabased()
                                    val record = iterator.next()
                                    try {
                                        val verbatimLocality = record!!["verbatimLocality"]
                                        val verbatimDate = record["verbatimDate"]
                                        val verbatimCollector = record["verbatimCollector"]
                                        val verbatimCollection = record["verbatimCollection"]
                                        val verbatimNumbers = record["verbatimNumbers"]
                                        val verbatimUnclasifiedText = record["verbatimUnclassifiedText"]
                                        barcode = record["barcode"]
                                        val questions = record["questions"]
                                        fl.load(barcode, verbatimLocality, verbatimDate, verbatimCollector, verbatimCollection, verbatimNumbers, verbatimUnclasifiedText, questions)
                                        counter!!.incrementSpecimensUpdated()
                                    } catch (e: IllegalArgumentException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    } catch (e: LoadException) {
                                        val error = RunnableJobError(file!!.name,
                                                barcode, Integer.toString(lineNumber),
                                                e.javaClass.simpleName, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                        counter!!.appendError(error)
                                        log.error(e.message, e)
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                errors!!.append("Load canceled by user.").append("\n")
                            }
                        } else { // allowed case three, transcription into arbitrary sets verbatim or other fields
                            log.debug("Input file case 3: Arbitrary set of fields.")
                            // Check column headers before starting run.
                            var headersOK = false
                            try {
                                val headerCheck: HeaderCheckResult = fl.checkHeaderList(headerList)
                                if (headerCheck.isResult()) {
                                    val confirm: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                            "Confirm load from file " + selectedFilename + " (" + rows + " rows) with headers: \n" + headerCheck.getMessage().replace(":".toRegex(), ":\n"), "Fields found for load", JOptionPane.OK_CANCEL_OPTION)
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        headersOK = true
                                    } else {
                                        errors!!.append("Load canceled by user.").append("\n")
                                    }
                                } else {
                                    val confirm: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                            "Problem found with headers in file, try to load anyway?\nHeaders: \n" + headerCheck.getMessage().replace(":".toRegex(), ":\n"), "Problem in fields for load", JOptionPane.OK_CANCEL_OPTION)
                                    if (confirm == JOptionPane.OK_OPTION) {
                                        headersOK = true
                                    } else {
                                        errors!!.append("Load canceled by user.").append("\n")
                                    }
                                }
                            } catch (e: LoadException) {
                                errors!!.append("Error loading data: \n").append(e.message).append("\n")
                                JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), e.message!!.replace(":".toRegex(), ":\n"), "Error Loading Data: Problem Fields", JOptionPane.ERROR_MESSAGE)
                                log.error(e.message, e)
                            }
                            if (headersOK) {
                                var lineNumber = 0
                                while (iterator.hasNext()) {
                                    lineNumber++
                                    val data: MutableMap<String?, String?> = HashMap()
                                    val record = iterator.next()
                                    val barcode = record!!["barcode"]
                                    val hi = headerList.iterator()
                                    var containsNonVerbatim = false
                                    while (hi.hasNext()) {
                                        val header = hi.next()
                                        // Skip any fields prefixed by the underscore character _
                                        if (header != "barcode" && !header!!.startsWith("_")) {
                                            data[header] = record[header]
                                            if (header != "questions" && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, header) && !MetadataRetriever.isFieldVerbatim(Specimen::class.java, header)) {
                                                containsNonVerbatim = true
                                            }
                                        }
                                    }
                                    if (data.size > 0) {
                                        try {
                                            var updated = false
                                            updated = if (containsNonVerbatim) {
                                                fl.loadFromMap(barcode, data, WorkFlowStatus.STAGE_CLASSIFIED, true)
                                            } else {
                                                fl.loadFromMap(barcode, data, WorkFlowStatus.STAGE_VERBATIM, true)
                                            }
                                            counter!!.incrementSpecimenDatabased()
                                            if (updated) {
                                                counter!!.incrementSpecimensUpdated()
                                            }
                                        } catch (e1: HibernateException) { // Catch (should just be development) problems with the underlying query
                                            val message = StringBuilder()
                                            message.append("Query Error loading row (").append(lineNumber).append(")[").append(barcode).append("]").append(e1.message)
                                            val err = RunnableJobError(selectedFilename, barcode, Integer.toString(lineNumber), e1.message, e1, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                            counter!!.appendError(err)
                                            log.error(e1.message, e1)
                                        } catch (e: LoadException) {
                                            val message = StringBuilder()
                                            message.append("Error loading row (").append(lineNumber).append(")[").append(barcode).append("]").append(e.message)
                                            val err = RunnableJobError(selectedFilename, barcode, Integer.toString(lineNumber), e.message, e, RunnableJobError.Companion.TYPE_LOAD_FAILED)
                                            counter!!.appendError(err)
                                            // errors.append(message.append("\n").toString());
                                            log.error(e.message, e)
                                        }
                                    }
                                    percentComplete = (lineNumber * 100f / rows).toInt()
                                    setPercentComplete(percentComplete)
                                }
                            } else {
                                val message = "Can't load data, problem with headers."
                                errors!!.append(message).append("\n")
                                log.error(message)
                            }
                        }
                    }
                    csvParser.close()
                    reader.close()
                } catch (e: FileNotFoundException) {
                    JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), "Unable to load data, file not found: " + e.message, "Error: File Not Found", JOptionPane.OK_OPTION)
                    errors!!.append("File not found ").append(e.message).append("\n")
                    log.error(e.message, e)
                } catch (e: IOException) {
                    errors!!.append("Error Loading data: ").append(e.message).append("\n")
                    log.error(e.message, e)
                }
            }
        } else { //TODO: handle error condition
            log!!.error("File selection cancelled by user.")
        }
        report(selectedFilename)
        done()
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#stop()
     */
    override fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#cancel()
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_TERMINATED
        log!!.debug(this.javaClass.simpleName + " " + this.toString() + "  recieved cancel signal")
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
    override fun registerListener(aJobListener: RunnerListener?): Boolean {
        return listeners!!.add(aJobListener)
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.RunnableJob#getName()
     */
    val name: String
        get() = "Ingest Data obtained from an external process"

    /**
     * Cleanup when job is complete.
     */
    protected fun done() {
        status = RunStatus.STATUS_DONE
        notifyListeners(RunStatus.STATUS_DONE)
        Singleton.Companion.getSingletonInstance().getJobList().removeJob(this)
    }

    private fun report(selectedFilename: String?) {
        var report = "Results for loading data from file $selectedFilename.\n"
        report += "Found  " + counter!!.specimens + " rows in input file.\n"
        report += "Examined " + counter!!.specimens + " specimens.\n"
        report += "Saved updated values for " + counter!!.specimensUpdated + " specimens.\n"
        report += errors.toString()
        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Load data from file complete.")
        val errorReportDialog = RunnableJobReportDialog(
                Singleton.Companion.getSingletonInstance().getMainFrame(),
                report, counter!!.errors,
                RunnableJobErrorTableModel.Companion.TYPE_LOAD,
                "Load Data from file Report"
        )
        errorReportDialog.setVisible(true)
    }

    protected fun notifyListeners(anEvent: Int) {
        Singleton.Companion.getSingletonInstance().getMainFrame().notifyListener(anEvent, this)
        val i: MutableIterator<RunnerListener?> = listeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(anEvent, this)
        }
    }

    protected fun setPercentComplete(aPercentage: Int) { //set value
        percentComplete = aPercentage
        log!!.debug(percentComplete)
        //notify listeners
        notifyListeners(percentComplete)
    }

    /**
     * Attempt to read file with a given CSV format, and if successful, return
     * the number of rows in the file.
     *
     * @param file        to check for csv rows.
     * @param formatToTry the CSV format to try to read the file with.
     * @return number of rows in the file.
     * @throws IOException           on a problem reading the header.
     * @throws FileNotFoundException on not finding the file.
     */
    @Throws(IOException::class, FileNotFoundException::class)
    protected fun readRows(file: File, formatToTry: CSVFormat): Int {
        var rows = 0
        val reader: Reader = FileReader(file)
        val csvParser = CSVParser(reader, formatToTry)
        val iterator = csvParser.iterator()
        while (iterator.hasNext()) {
            iterator.next()
            rows++
        }
        csvParser.close()
        reader.close()
        return rows
    }

    companion object {
        private val log = LogFactory.getLog(JobVerbatimFieldLoad::class.java)
    }
}