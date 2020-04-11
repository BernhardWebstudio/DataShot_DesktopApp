/**
 * java
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
package edu.harvard.mcz.imagecaptureimport

import com.adobe.internal.xmp.XMPException
import com.adobe.internal.xmp.XMPMeta
import com.drew.metadata.Directory
import com.drew.metadata.Metadata
import com.google.zxing.FormatException
import com.google.zxing.NotFoundException
import com.google.zxing.ReaderException
import com.google.zxing.Result
import org.apache.commons.cli.*
import org.apache.commons.logging.LogFactory
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.io.File
import java.util.*
import javax.swing.SwingUtilities

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * Image File that might contain text that can be extracted by OCR or a barcode that
 * can be extracted by barcode recognition.
 *
 *
 * TODO: extract few methods to some DataExtractor class or something.
 * This class clearly does more than what's expected of a ("Candidate", Image) File
 */
class CandidateImageFile {
    /**
     * Obtain the File for this candidate image file
     *
     * @return the candidateFile
     */
    var file: File? = null
        private set
//   directly from barcode scanning calls.
    /**
     * Obtain the result of the most recent attempt to check this image for any barcode.
     * Use with caution, may not return what you think is the most recent attempt.
     *
     * @return int one of RESULT_ERROR, RESULT_NOT_CHECKED, or RESULT_BARCODE_SCANNED
     */
    /**
     * Status of last attempted barcode read.
     */
    var barcodeStatus = 0
        private set
    /**
     * @return the catalogNumberBarcodeStatus
     */
    var catalogNumberBarcodeStatus = 0
        private set
    private var catalogNumberBarcodeText: String? = null
    private var exifCommentText: String? = null
    /**
     * @return the unitTrayTaxonLabelTextStatus
     */
    var unitTrayTaxonLabelTextStatus = 0
        private set
    private var labelText: String? = null
    private var unitTrayLabel: UnitTrayLabel? = null
    private var dateCreated: Date? = null
    private var templateUsed: PositionTemplate? = null

    /**
     * Constructor which detects the template to be used with the candidate image file.
     *
     * @param aFile
     * @throws UnreadableFileException
     */
    constructor(aFile: File) {
        if (!aFile.canRead()) {
            try {
                throw UnreadableFileException("Can't read file " + aFile.canonicalPath)
            } catch (e: IOException) {
                throw UnreadableFileException("IOException on trying to get filename.")
            }
        }
        var template = PositionTemplate()
        init()
        file = aFile
        // detect template to use.
        val detector = ConfiguredBarcodePositionTemplateDetector()
        // First try a quick check of all the templates
        var templateName: String = detector.detectTemplateForImage(aFile, this, true)
        if (templateName == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // If that fails, try each template more exhaustively.
            templateName = detector.detectTemplateForImage(aFile, this, false)
        }
        try {
            template = PositionTemplate(templateName)
            log!!.info("template ID is " + template.getTemplateId())
        } catch (e: NoSuchTemplateException) {
            log!!.error("Position template detector returned an unknown template name: $templateName.", e)
        }
        // check the file for an exif comment
        exifUserCommentText // scan for exif when handed file.
        if (template.getTemplateId() != PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
            try {
                if (getTaxonLabelQRText(template) == null) {
                    getTaxonLabelOCRText(template)
                }
            } catch (e: OCRReadException) {
                log!!.error("Unable to OCR file: " + file!!.name + " " + e.message)
            }
        }
        templateUsed = template
    }

    /**
     * Constructor
     *
     * @param aFile     the image file that may contain a barcode or text.
     * @param aTemplate the PositionTemplate to use to identify where a barcode or OCR text may occur
     * in the image provided by aFile.
     * @throws UnreadableFileException if aFile cannot be read.
     * @throws OCRReadException
     */
    constructor(aFile: File, aTemplate: PositionTemplate) {
        setFile(aFile, aTemplate)
        if (!aFile.canRead()) {
            try {
                throw UnreadableFileException("Can't read file " + aFile.canonicalPath)
            } catch (e: IOException) {
                throw UnreadableFileException("IOException on trying to get filename.")
            }
        }
    }

    /**
     * Constructor with no parameters to use to access convenience static methods.
     * Must follow with setFile() to use for processing images.
     *
     * @see this.setFile
     */
    constructor() {}

    /**
     * Convenience method to check an image for a barcode.  Does not set any instance variables of CandidateImageFile,
     * and does not behave precisely as the getBarcodeText() methods.  Result state is not available from getBarcodeStatus()
     * and both errors and the absence of a barcode in the image result in an empty string being returned.
     * If a template is specified and no barcode is detected, tries again with some image scaling and contrast variations.
     *
     * @param image            The BufferedImage to check for a barcode.
     * @param positionTemplate The position template specifying where in the image to check for the barcode, if
     * TEMPLATE_NO_COMPONENT_PARTS, the entire image is checked for a barcode, otherwise only the part of the image
     * specified by the template is checked.
     * @return the text of the barcode found in the barcode portion of the position template, or an empty string.
     */
    fun getBarcodeTextFromImage(image: BufferedImage, positionTemplate: PositionTemplate, quickCheck: Boolean): String? {
        log!!.debug(positionTemplate.getName())
        var returnValue: String? = ""
        if (positionTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // Check the entire image for a barcode and return.
            log.debug(image.getType())
            val source: LuminanceSource = BufferedImageLuminanceSource(image)
            val temp = CandidateImageFile()
            val checkResult = temp.checkSourceForBarcode(source, true)
            returnValue = checkResult.text
        } else { // Check the part of the image specified by the template for the barcode.
            if (image != null) {
                if (image.getWidth() > positionTemplate.getBarcodeULPosition().width) { // image might plausibly match template
                    val left: Int = positionTemplate.getBarcodeULPosition().width //* @param left x coordinate of leftmost pixels to decode
                    val top: Int = positionTemplate.getBarcodeULPosition().height //* @param top y coordinate of topmost pixels to decode
                    val right: Int = left + positionTemplate.getBarcodeSize().width //* @param right one more than the x coordinate of rightmost pixels to decode. That is, we will decode
                    val width: Int = positionTemplate.getBarcodeSize().width
                    //*  pixels whose x coordinate is in [left,right)
                    val bottom: Int = top + positionTemplate.getBarcodeSize().height //* @param bottom likewise, one more than the y coordinate of the bottommost pixels to decode
                    val height: Int = positionTemplate.getBarcodeSize().height
                    returnValue = readBarcodeFromLocation(image, left, top, width, height, quickCheck)
                }
            }
        }
        return returnValue
    }

    /**
     * Convenience method to check an image for a barcode.  Does not set any instance variables of CandidateImageFile,
     * and does not behave precisely as the getBarcodeText() methods.  Result state is not available from getBarcodeStatus()
     * and both errors and the absence of a barcode in the image result in an empty string being returned.
     *
     *
     * Attempts read of relevant crop from image, then attempts this with crop area scaled down, then attempts it
     * with crop area sharpened.  Does not include shifts of location of crop area.
     *
     * @param image            The BufferedImage to check for a barcode.
     * @param positionTemplate The position template specifying where in the image to check for the barcode, if
     * TEMPLATE_NO_COMPONENT_PARTS, the entire image is checked for a barcode, otherwise only the part of the image
     * specified by the template for the UnitTrayLabel is checked.
     * @return the text of the barcode found in the UnitTrayLabel (text) portion of the position template, or an empty string.
     */
    fun getBarcodeUnitTrayTextFromImage(image: BufferedImage, positionTemplate: PositionTemplate): String? {
        var returnValue: String? = ""
        if (positionTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // Check the entire image for a barcode and return.
            val source: LuminanceSource = BufferedImageLuminanceSource(image)
            var result: Result
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            returnValue = try {
                getQRCodeText(bitmap)
            } catch (e: ReaderException) {
                ""
            }
        } else { // Check the part of the image specified by the template for the barcode.
            if (image != null) {
                if (image.getWidth() > positionTemplate.getUtBarcodePosition().width) { // image might plausibly match template
                    val left: Int = positionTemplate.getUtBarcodePosition().width //* @param left x coordinate of leftmost pixels to decode
                    val top: Int = positionTemplate.getUtBarcodePosition().height //* @param top y coordinate of topmost pixels to decode
                    val right: Int = left + positionTemplate.getUtBarcodeSize().width //* @param right one more than the x coordinate of rightmost pixels to decode. That is, we will decode
                    //*  pixels whose x coordinate is in [left,right)
                    val bottom: Int = top + positionTemplate.getUtBarcodeSize().height //* @param bottom likewise, one more than the y coordinate of the bottommost pixels to decode
                    val width: Int = positionTemplate.getUtBarcodeSize().width
                    val height: Int = positionTemplate.getUtBarcodeSize().height
                    returnValue = readBarcodeFromLocation(image, left, top, width, height, false)
                }
            }
        }
        return returnValue
    }

    /**
     * Read the barcode content from a portion of an image.
     *
     * @param image  to crop a portion out of to detect a barcode
     * @param left   x coordinate in image of leftmost pixels to decode
     * @param top    y coordinate in image of topmost pixels to decode,  left and top are the upper left x,y coordinate
     * @param width  in pixels of the area to decode
     * @param height in pixels of the area to decode
     * @return string content of barcode found, or an empty string
     */
    fun readBarcodeFromLocation(image: BufferedImage?, left: Int, top: Int, width: Int, height: Int, quickCheck: Boolean): String? {
        var returnValue = TextStatus("", RESULT_ERROR)
        if (image == null) {
            log!!.error("Image null")
            return ""
        }
        val fileName = if (file != null) file!!.name else ""
        if (image.getWidth() >= left + width && image.getHeight() >= top + height) { // provided crop area falls within image.
            log!!.debug("Attempting to detect barcode in TL: " + left + "x" + top + " +" + width + "x" + height)
            var source: LuminanceSource? = null
            try { // Create a crop of the image, test this cropped area
                source = BufferedImageLuminanceSource(image, left, top, width, height)
            } catch (e: IllegalArgumentException) {
                log.error("Provided coordinates fall outside bounds of image; Error: " + e.message)
                return ""
            }
            // 1, try the source crop directly.
            returnValue = this.checkSourceForBarcode(source, log.isDebugEnabled)
            log.debug("result of source barcode check: '" + returnValue.text + "' at status: " + returnValue.status)
            if (quickCheck) {
                if (returnValue.text == null) {
                    returnValue = checkWithDisplacing(image, left, top, width, height)
                }
                return returnValue.text
            }
            // If this is not a quick check, keep trying harder
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read from location using method #1")
                return returnValue.text
            }
            // 2, try rescaling (to a 800 pixel width)
            log.debug("Trying again with scaled image crop: " + INITIAL_SCALING_WIDTH + "px.")
            val scale = INITIAL_SCALING_WIDTH / width
            returnValue = checkWithScale(image, left, top, width, height, scale)
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '$fileName' from location using method #2")
                return returnValue.text
            }
            // 3, try another barcode scanner
            returnValue = checkSourceForBarcode(image.getSubimage(left, top, width, height))
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '$fileName' from location using method #3")
                return returnValue.text
            }
            // 4, try another barcode scanner, but this time "global"
            returnValue = checkSourceForBarcodeAt(image, (top + height / 2.0).toInt(), (left + width / 2.0).toInt(), (Math.max(width, height) + 0.1 * Math.min(image.getWidth(), image.getHeight())) as Int)
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '$fileName' from location using method #4")
                return returnValue.text
            }
            // 5, try rescaling to configured scaling widths
            val scaling: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGERESCALE)
            val scalingBits = ArrayList<String?>()
            if (scaling.contains(",")) {
                scalingBits.addAll(Arrays.asList(*scaling.split(",".toRegex()).toTypedArray()))
            } else {
                scalingBits.add(scaling)
            }
            for (transform in scalingBits) {
                returnValue = checkWithTransform(image, left, top, width, height, transform)
                if (returnValue.status == RESULT_BARCODE_SCANNED) {
                    log.debug("Successful preprocess barcode read of image '$fileName' from location using method #5")
                    log.info("Success at image '$fileName' with transform = $transform.")
                    return returnValue.text
                }
            } // end while loop
            // 6, try again with a sharpened image
            try {
                log.debug("Trying again with image sharpened.")
                val kernel = Kernel(3, 3, matrix)
                val convolver = ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null)
                var sharpened = BufferedImage(image.getWidth(), image.getHeight(), image.getType())
                sharpened = convolver.filter(image, sharpened)
                source = BufferedImageLuminanceSource(sharpened, left, top, width, height)
                returnValue = this.checkSourceForBarcode(source, true)
            } catch (e: IllegalArgumentException) {
                log.error(e.message, e)
            }
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '$fileName' from location using method #6")
                return returnValue.text
            }
            // 7, try again with scale factors & offsets
            val crop: BufferedImage = image.getSubimage(left, top, width, height)
            val scaleFactors = floatArrayOf(1.2f, 0.8f, 0.6f, 1.4f, 1.6f)
            val offsets = intArrayOf(15, -15, -30, 30, 45)
            for (i in scaleFactors.indices) {
                for (o in offsets.indices) {
                    returnValue = checkWithConfiguration(crop, scaleFactors[i], offsets[o], null)
                    if (returnValue.status == RESULT_BARCODE_SCANNED) {
                        log.debug("Successful preprocess barcode read of image '$fileName' from location using method #7")
                        log.info("Success at image '" + fileName + "' with offset = " + offsets[o] + " and scaleFactor = " + scaleFactors[i])
                        return returnValue.text
                    }
                }
            }
            // 8, Try again with some small displacements of window
            returnValue = checkWithDisplacing(image, left, top, width, height)
            if (returnValue.status == RESULT_BARCODE_SCANNED) {
                log.debug("Successful preprocess barcode read of image '$fileName' from location using method #8")
                return returnValue.text
            }
        }
        return returnValue.text
    }

    /**
     * Check an image for a barcode at a certain displacement
     *
     * @param image          the image to check
     * @param left           the original left to find the barcode at
     * @param top            the original top to find the barcode at
     * @param width          the original with of the image
     * @param height         the original height of the image
     * @param transformToTry the specification of the transformation which should be applied to check
     * @return the status of the check
     */
    private fun checkWithTransform(image: BufferedImage, left: Int, top: Int, width: Int, height: Int, transformToTry: String): TextStatus {
        var transformToTry = transformToTry
        val kernel = Kernel(3, 3, matrix)
        val convolver = ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null)
        var checkResult = TextStatus("", RESULT_NOT_CHECKED)
        val source: LuminanceSource?
        var sharpen = false
        var brighter = false
        var dimmer = false
        //	assert (!(widthToTry.contains("sharpen") && widthToTry.contains("brighter") && widthToTry.contains("dimmer")));
        if (transformToTry.contains("sharpen")) {
            sharpen = true
            transformToTry = transformToTry.replace("sharpen", "")
        } else if (transformToTry.contains("brighter")) {
            brighter = true
            dimmer = false
            transformToTry = transformToTry.replace("brighter", "")
        } else if (transformToTry.contains("dimmer")) {
            brighter = false
            dimmer = true
            transformToTry = transformToTry.replace("dimmer", "")
        }
        // strip out any remaining non-numeric characters (sharpen'ed')
        transformToTry = transformToTry.replace("[^0-9.]".toRegex(), "")
        log!!.debug("TransformToTry: $transformToTry")
        // Try rescaling to a configured scaling width value.
        var scalingWidth = 0.0
        try {
            scalingWidth = transformToTry.toDouble()
            if (scalingWidth < 1) {
                scalingWidth = INITIAL_SCALING_WIDTH / 2
            }
        } catch (e: NumberFormatException) {
            log.error(e.message)
        }
        if (width.toDouble() != scalingWidth && scalingWidth != INITIAL_SCALING_WIDTH || sharpen || brighter || dimmer) {
            try { // if a rescaling different from what's been done in steps 1 and 2 has been configured, rescale and try again.
// skip any configured transforms that are identical to steps 1 or 2.
                log.debug("Trying again with scaled image crop to: " + scalingWidth + "px.")
                val scale = scalingWidth / width
                val scaledHeight = (height * scale).toInt()
                val scaledWidth = (width * scale).toInt()
                val cropped: BufferedImage = image.getSubimage(left, top, width, height)
                val initialH: Int = cropped.getHeight()
                val initialW: Int = cropped.getWidth()
                // BufferedImage scaled = new BufferedImage(initialW, initialH, cropped.getType());
                var scaled = BufferedImage(scaledWidth, scaledHeight, cropped.getType())
                val rescaleTransform = AffineTransform()
                rescaleTransform.scale(scale, scale)
                val scaleOp = AffineTransformOp(rescaleTransform, AffineTransformOp.TYPE_BILINEAR)
                scaled = scaleOp.filter(cropped, scaled)
                if (sharpen) {
                    log.debug("Also sharpening the crop")
                    val dest = BufferedImage(scaledWidth, scaledHeight, cropped.getType())
                    scaled = convolver.filter(scaled, dest)
                }
                if (brighter) {
                    log.debug("Also brightening the crop")
                    val rescaleOp = RescaleOp(1.2f, 15, null)
                    val src: BufferedImage = scaled
                    scaled = rescaleOp.filter(src, scaled)
                }
                if (dimmer) {
                    log.debug("Also dimming the crop")
                    val rescaleOp = RescaleOp(0.80f, -15, null)
                    val src: BufferedImage = scaled
                    scaled = rescaleOp.filter(src, scaled)
                }
                source = BufferedImageLuminanceSource(scaled)
                checkResult = this.checkSourceForBarcode(source, true)
            } catch (e: Exception) {
                log.error(e.message, e)
            }
        }
        return checkResult
    }

    /**
     * Check an image source for barcode, trysing
     *
     * @param image
     * @param left
     * @param top
     * @param width
     * @param height
     * @return
     */
    private fun checkWithDisplacing(image: BufferedImage, left: Int, top: Int, width: Int, height: Int): TextStatus {
        var source: LuminanceSource?
        var checkResult = TextStatus("", RESULT_ERROR)
        var shiftLeft = left - 3
        doubleLoop@ while (shiftLeft <= left + 3) {
            var shiftTop = top - 3
            while (shiftTop <= top + 3) {
                try {
                    log!!.debug("Trying displacement of crop: $shiftLeft,$shiftTop")
                    source = BufferedImageLuminanceSource(image, shiftLeft, shiftTop, width, height)
                    checkResult = this.checkSourceForBarcode(source, true)
                    if (checkResult.status != RESULT_ERROR) {
                        break@doubleLoop
                    }
                } catch (e: ArrayIndexOutOfBoundsException) { // shift falls outside image, inBounds = false
                } catch (e: IllegalArgumentException) {
                }
                shiftTop = shiftTop + 6
            }
            shiftLeft = shiftLeft + 6
        }
        return checkResult
    }

    private fun checkWithConfiguration(crop: BufferedImage, scaleFactor: Float, offset: Int, hints: RenderingHints?): TextStatus {
        val rescaleOp = RescaleOp(scaleFactor, offset, hints)
        rescaleOp.filter(crop, crop)
        val cropSource = BufferedImageLuminanceSource(crop)
        return checkSourceForBarcode(cropSource, log!!.isDebugEnabled)
    }

    private fun checkWithScale(image: BufferedImage, left: Int, top: Int, width: Int, height: Int, scale: Double): TextStatus {
        val returnValue: String? = null
        val scaledHeight = (height * scale).toInt()
        val scaledWidth = (width * scale).toInt()
        val cropped: BufferedImage = image.getSubimage(left, top, width, height)
        val initialH: Int = cropped.getHeight()
        val initialW: Int = cropped.getWidth()
        var scaled = BufferedImage(scaledWidth, scaledHeight, cropped.getType())
        val rescaleTransform = AffineTransform()
        rescaleTransform.scale(scale, scale)
        val scaleOp = AffineTransformOp(rescaleTransform, AffineTransformOp.TYPE_BILINEAR)
        scaled = scaleOp.filter(cropped, scaled)
        val cropSource = BufferedImageLuminanceSource(scaled)
        val temp = CandidateImageFile()
        return temp.checkSourceForBarcode(cropSource, log!!.isDebugEnabled)
    }

    /**
     * Test to see if the file provided in the constructor or the setFile method is readable.  This method
     * is called from both the CandidateImageFile(File aFile, PositionTemplate aTemplate) constructor and the
     * setFile(File aFile, PositionTemplate aTemplate) method, so it shouldn't be necessary to call it externally.
     *
     * @return true if file is readable, throws UnreadableFileException exception rather than returning false
     * if file can't be read.
     * @throws UnreadableFileException if file is null, or if it doesn't exist or if it can't be read.
     */
    @get:Throws(UnreadableFileException::class)
    val isFileReadable: Boolean
        get() {
            try {
                if (file == null) {
                    throw UnreadableFileException("No such file. CandidateImageFile given null for a filename.")
                }
                if (file!!.exists() == false) {
                    throw UnreadableFileException("No such file as: " + file!!.absolutePath)
                }
                if (file!!.canRead() == false) {
                    throw UnreadableFileException("Can't read file: " + file!!.absolutePath)
                }
            } catch (e: SecurityException) {
                throw UnreadableFileException("Can't read file: " + file!!.absolutePath + " Security problem." + e.message)
            }
            return true
        }

    private fun init() {
        barcodeStatus = RESULT_NOT_CHECKED
        catalogNumberBarcodeStatus = RESULT_NOT_CHECKED
        catalogNumberBarcodeText = null
        exifCommentText = null
        unitTrayTaxonLabelTextStatus = RESULT_NOT_CHECKED
        unitTrayLabel = null
        labelText = null
    }

    /**
     * Change the image file and position template.
     *
     * @param aFile
     * @param aTemplate
     * @throws UnreadableFileException
     * @throws OCRReadException
     */
    @Throws(UnreadableFileException::class)
    fun setFile(aFile: File?, aTemplate: PositionTemplate) {
        file = aFile
        isFileReadable
        // Set initial state
        init()
        // check the file for an exif comment
        exifUserCommentText // scan for exif when handed file.
        if (aTemplate.getTemplateId() != PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
            try {
                if (getTaxonLabelQRText(aTemplate) == null) {
                    getTaxonLabelOCRText(aTemplate)
                }
            } catch (e: OCRReadException) {
                log!!.error("Unable to OCR file: " + file!!.name + " " + e.message)
            }
        }
        templateUsed = aTemplate
    }

    /**
     * Check a LuminanceSource for a barcode, handle exceptions, and return
     * an object containing the text read (or an error message) and the corresponding
     * value to use for barcodeStatus.
     *
     * @param source LuminanceSource to check for a barcode.
     * @return a TextStatus object containing the barcodeStatus value and the
     * text found (or the error message).
     */
    private fun checkSourceForBarcode(source: LuminanceSource?, generateDebugImage: Boolean): TextStatus {
        val returnValue = TextStatus("", RESULT_NOT_CHECKED)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        if (generateDebugImage) {
            try {
                val h: Int = bitmap.getBlackMatrix().getHeight()
                val w: Int = bitmap.getBlackMatrix().getWidth()
                val temp = BufferedImage(h, w, BufferedImage.TYPE_BYTE_GRAY)
                val g: Graphics = temp.getGraphics()
                g.color = Color.WHITE
                g.drawRect(0, 0, w, h)
                g.color = Color.BLACK
                for (i in 0 until h) {
                    for (j in 0 until w) {
                        try {
                            if (bitmap.getBlackMatrix().get(i, j)) {
                                g.color = Color.BLACK
                                g.drawRect(i, j, 1, 1)
                            } else {
                                g.color = Color.WHITE
                                g.drawRect(i, j, 1, 1)
                            }
                        } catch (e: ArrayIndexOutOfBoundsException) { //
                        }
                    }
                }
                val d = Date()
                if (log!!.isTraceEnabled) {
                    log.debug("Trace is enabled!!!!")
                    val t = java.lang.Long.toString(d.time)
                    val out = File("TempBarcodeCrop$t.png")
                    ImageIO.write(temp, "png", out)
                    log.trace("Wrote: " + out.path)
                } else {
                    ImageIO.write(temp, "png", File("TempBarcodeCrop.png"))
                }
            } catch (e1: NotFoundException) {
                log!!.error(e1.message, e1)
            } catch (e: IOException) {
                log!!.error(e.message, e)
            }
        } else {
            log!!.trace("Generate Debug Image: $generateDebugImage")
        }
        var result: Result
        val reader = QRCodeReader()
        try {
            var hints: Hashtable<DecodeHintType?, Any?>? = null
            hints = Hashtable<DecodeHintType?, Any?>(3)
            hints[DecodeHintType.TRY_HARDER] = java.lang.Boolean.FALSE
            //Probable bug in xzing, reader.decode can throw ArrayIndexOutOfBoundsException
//as well as the expected ReaderException.  It looks like there's an assumption
//hidden in the bitmapMatrix that the height and width are the same.
            result = reader.decode(bitmap, hints)
            returnValue.text = result.text
            returnValue.status = RESULT_BARCODE_SCANNED
        } catch (e: ReaderException) {
            log!!.error(e.javaClass.toString() + " " + e.message)
            if (!Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEZXINGALSOTRYHARDER).equals("true", ignoreCase = true)) {
                returnValue.text = e.toString() + " " + e.message
                returnValue.status = RESULT_ERROR
            } else {
                try {
                    var hints: Hashtable<DecodeHintType?, Any?>? = null
                    hints = Hashtable<DecodeHintType?, Any?>(3)
                    hints[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE
                    //Probable bug in xzing, reader.decode can throw ArrayIndexOutOfBoundsException
//as well as the expected ReaderException.  It looks like there's an assumption
//hidden in the bitmapMatrix that the height and width are the same.
                    result = reader.decode(bitmap, hints)
                    returnValue.text = result.text
                    returnValue.status = RESULT_BARCODE_SCANNED
                } catch (e1: ReaderException) {
                    log.debug(e1.toString() + " " + e1.message)
                    returnValue.text = e.toString() + " " + e1.message
                    returnValue.status = RESULT_ERROR
                } catch (e1: ArrayIndexOutOfBoundsException) {
                    log.error(e.message)
                    returnValue.text = e1.toString() + " " + e1.message
                    returnValue.status = RESULT_ERROR
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            log!!.error(e.message)
            returnValue.text = e.toString() + " " + e.message
            returnValue.status = RESULT_ERROR
        }
        return returnValue
    }

    /**
     * Check an image source for a barcode using BoofCV scanner
     *
     * @param source the image to check
     * @return the status tuple
     */
    private fun checkSourceForBarcode(source: BufferedImage): TextStatus {
        val gray: GrayU8 = ConvertBufferedImage.convertFrom(source, null as GrayU8?)
        val detector: QrCodeDetector<GrayU8?> = FactoryFiducial.qrcode(null, GrayU8::class.java)
        detector.process(gray)
        // Get's a list of all the qr codes it could successfully detect and decode
        val detections: MutableList<QrCode?> = detector.getDetections()
        log!!.debug("BoofCV QRScanner found " + detections.size + " barcodes. Success only if 1. Additionally, failures: " + detector.getFailures().size)
        if (detections.size == 1) {
            for (detection in detections) {
                return TextStatus(detection.message, RESULT_BARCODE_SCANNED)
            }
        }
        return TextStatus("", RESULT_ERROR)
    }

    /**
     * Check an image source for a barcode using BoofCV scanner
     *
     * @param source   the image to check
     * @param fromLeft the expected x distance
     * @param fromTop  the expected y distance
     * @return the status tuple
     */
    private fun checkSourceForBarcodeAt(source: BufferedImage, fromLeft: Int, fromTop: Int, tol: Int): TextStatus {
        val gray: GrayU8 = ConvertBufferedImage.convertFrom(source, null as GrayU8?)
        val detector: QrCodeDetector<GrayU8?> = FactoryFiducial.qrcode(null, GrayU8::class.java)
        detector.process(gray)
        // Get's a list of all the qr codes it could successfully detect and decode
        val detections: MutableList<QrCode?> = detector.getDetections()
        log!!.debug("BoofCV QRScanner found " + detections.size + " barcodes. Will search for nearest. Additionally, failures: " + detector.getFailures().size)
        val toleranceBounds = Rectangle(fromTop, fromLeft, tol, tol)
        for (detection in detections) { //
            if (this.intersect(detection.bounds, toleranceBounds)) {
                return TextStatus(detection.message, RESULT_BARCODE_SCANNED)
            }
        }
        return TextStatus("", RESULT_ERROR)
    }

    private fun intersect(first: Polygon2D_F64, second: Rectangle): Boolean {
        assert(first.vertexes.size == 4)
        log!!.debug("BoofCV found QR with top-left: x0 = " + first.get(0).x + ", y0 = " + first.get(0).y)
        val first_p = Rectangle(first.get(0).x as Int, first.get(0).y as Int, Math.abs(first.get(2).x - first.get(0).x) as Int, Math.abs(first.get(2).y - first.get(0).y) as Int)
        log.debug("Translating to rectangle: x0 = " + first_p.x + ", y0 = " + first_p.y + ", x3 = " + (first_p.x + first_p.height) + ", y3 = " + (first_p.y + first_p.width))
        log.debug("Using tolerance bounds: x0 = " + second.x + ", y0 = " + second.y + ", x3 = " + (second.x + second.height) + ", y3 = " + (second.y + second.width))
        log.debug("Intersection: " + first_p.intersects(second))
        return first_p.intersects(second)
    }

    /**
     * If the image contains a taxon label text encoded in a QRCode barcode in the position specified
     * for the Taxon/UnitTrayLabel Barcode by the PositionTemplate, return that text as a UnitTrayLabel
     * object.
     *
     * @param positionTemplate, the template specifying the location of the barcode via getUtBarcodePosition.
     * @return null or a UnitTrayLabel containing the parsed text of the taxon label read from the barcode.
     */
    fun getTaxonLabelQRText(positionTemplate: PositionTemplate): UnitTrayLabel? {
        log!!.info("2 template ID is.........." + positionTemplate.getTemplateId())
        log.debug(unitTrayLabel)
        log.debug(unitTrayTaxonLabelTextStatus)
        log.debug(labelText)
        if (unitTrayLabel != null && unitTrayTaxonLabelTextStatus == RESULT_BARCODE_SCANNED && labelText != null && labelText!!.length > 0) {
            return unitTrayLabel
        }
        var resultLabel: UnitTrayLabel? = null
        var returnValue: String? = ""
        if (positionTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // Check the entire image for a barcode and return.
            returnValue = barcodeText
        } else { // Check the part of the image specified by the template for the barcode.
            var image: BufferedImage? = null
            var error = ""
            barcodeStatus = RESULT_ERROR
            try {
                image = ImageIO.read(file)
                log.debug(file!!.canonicalFile)
            } catch (e: IOException) {
                error = e.toString() + " " + e.message
                returnValue = error
            }
            if (image == null) {
                returnValue = "Could not decode image. $error"
                barcodeStatus = RESULT_ERROR
            } else { // Could read image.  Try reading barcode from templated location.
                if (image.getWidth() >= positionTemplate.getUtBarcodePosition().width && image.getWidth().toLong() == Math.round(positionTemplate.getImageSize().getWidth())) { // image might plausibly match template
                    val left: Int = positionTemplate.getUtBarcodePosition().width //* @param left x coordinate of leftmost pixels to decode
                    val top: Int = positionTemplate.getUtBarcodePosition().height //* @param top y coordinate of topmost pixels to decode
                    val right: Int = left + positionTemplate.getUtBarcodeSize().width //* @param right one more than the x coordinate of rightmost pixels to decode. That is, we will decode
                    val width: Int = positionTemplate.getUtBarcodeSize().width
                    //*  pixels whose x coordinate is in [left,right)
                    val bottom: Int = top + positionTemplate.getUtBarcodeSize().height //* @param bottom likewise, one more than the y coordinate of the bottommost pixels to decode
                    val height: Int = positionTemplate.getUtBarcodeSize().height
                    returnValue = readBarcodeFromLocation(image, left, top, width, height, false)
                    if (returnValue!!.length > 0) {
                        barcodeStatus = RESULT_BARCODE_SCANNED
                    } else {
                        returnValue = "Failed to read a barcode from templated location."
                        barcodeStatus = RESULT_ERROR
                    }
                    log.debug(returnValue)
                    log.debug("barcodeStatus=$barcodeStatus")
                } else { // image is narrower than templated area.
                    log.debug("Skipping Template.  ImageWidth=" + image.getWidth() + "; TemplateWidth=" + Math.round(positionTemplate.getImageSize().getWidth()))
                }
            } // image is readable
        }
        unitTrayTaxonLabelTextStatus = barcodeStatus
        if (returnValue != "" && barcodeStatus == RESULT_BARCODE_SCANNED) {
            log.debug("Found QR Barcode on unit tray label containing: $returnValue")
            resultLabel = UnitTrayLabel.Companion.createFromJSONString(returnValue)
            labelText = returnValue
            unitTrayLabel = resultLabel
            templateUsed = positionTemplate
        } else {
            labelText = null
        }
        return resultLabel
    }

    /**
     * Return the text found by OCR of the taxon label (getTextPosition) region of the
     * image according to the specified template.
     *
     * @param aTemplate
     * @return a string
     * @throws OCRReadException
     */
    @Throws(OCRReadException::class)
    fun getTaxonLabelOCRText(aTemplate: PositionTemplate?): String? { // Actual read attempt is only invoked once,
// subsequent calls return cached value.
        log!!.debug("in getLabelOCRText() 1 labelText is $labelText")
        if (labelText == null) {
            var image: BufferedImage? = null
            var error = ""
            barcodeStatus = RESULT_ERROR
            try {
                image = ImageIO.read(file)
                log.debug("image is $image")
            } catch (e: IOException) {
                error = e.toString() + " " + e.message
                log.error(error)
            }
            if (image != null) {
                try {
                    if (aTemplate != null && aTemplate.getTextPosition() != null) { //allie edit
                        val x: Int = aTemplate.getTextPosition().width
                        val y: Int = aTemplate.getTextPosition().height
                        val w: Int = aTemplate.getTextSize().width
                        val h: Int = aTemplate.getTextSize().height
                        // OCR and parse UnitTray Label
                        val o = ConvertTesseractOCR(image.getSubimage(x, y, w, h))
                        labelText = ""
                        try {
                            labelText = o.getOCRText()
                        } catch (e: OCRReadException) {
                            log.error(e.message)
                            e.printStackTrace()
                        }
                    }
                } catch (ex: Exception) {
                    log.error("Exception thrown in OCR of unit tray label.")
                    log.error(ex)
                    log.trace(ex)
                    throw OCRReadException(ex.message)
                }
                if ((labelText == null || labelText == "") && aTemplate != null && aTemplate.getTextPosition() != null) { //this line throws an exception?!
                    try { // try again
                        val x: Int = aTemplate.getTextPosition().width + 1
                        val y: Int = aTemplate.getTextPosition().height + 1
                        val w: Int = aTemplate.getTextSize().width + 1
                        val h: Int = aTemplate.getTextSize().height + 1
                        log.debug("in getLabelOCRText() 10")
                        // OCR and parse UnitTray Label
                        val o = ConvertTesseractOCR(image.getSubimage(x, y, w, h))
                        labelText = ""
                        try {
                            labelText = o.getOCRText()
                        } catch (e: OCRReadException) {
                            log.error(e.message)
                            e.printStackTrace()
                        }
                    } catch (ex: Exception) {
                        log.error("Exception thrown in OCR of unit tray label.")
                        log.error(ex)
                        log.trace(ex)
                        throw OCRReadException(ex.message)
                    }
                }
            }
        }
        return labelText
    }/*
				    http://purl.org/dc/elements/1.1/ = "dc:"	(0x80000000 : SCHEMA_NODE)
					    dc:description	(0x1e00 : ARRAY | ARRAY_ORDERED | ARRAY_ALTERNATE | ARRAY_ALT_TEXT)
						    [1] = "MCZ-ENT00597110"	(0x50 : HAS_QUALIFIER | HAS_LANGUAGE)
								?xml:lang = "x-default"	(0x20 : QUALIFIER)
					 */
    // cache the comment if one was found, otherwise an empty string.
// Try to see if there is an xmp dc:description block// [Exif] User Comment// read, or re-read the file for a comment// Actual read attempt is only invoked once,
// subsequent calls return cached value.

    /**
     * Get the text, if any, from the Exif UserComment of the image file.
     *
     * @return the content of the Exif UserComment decoded as a string.
     */
    val exifUserCommentText: String?
        get() { // Actual read attempt is only invoked once,
// subsequent calls return cached value.
            if (exifCommentText == null) { // read, or re-read the file for a comment
                var exifComment: String? = ""
                try {
                    val metadata: Metadata = JpegMetadataReader.readMetadata(file, JpegMetadataReader.ALL_READERS)
                    // [Exif] User Comment
                    val exifDirectory: Directory? = metadata.getFirstDirectoryOfType<ExifSubIFDDirectory?>(ExifSubIFDDirectory::class.java)
                    val descriptor = ExifSubIFDDescriptor(exifDirectory as ExifSubIFDDirectory?)
                    exifComment = descriptor.getUserCommentDescription()
                    log!!.debug("Exif UserComment = $exifComment")
                } catch (e2: JpegProcessingException) {
                    log!!.error("Error reading exif metadata.")
                    log.error(e2.message)
                } catch (e1: NullPointerException) {
                    log!!.error("Error reading exif metadata, ExifSubIFDDirectory not found.")
                    log.error(e1.message)
                } catch (e: IOException) {
                    log!!.error("Error reading file for exif metadata.")
                    log.error(e.message)
                }
                if (exifComment == null || exifComment.trim { it <= ' ' }.length == 0) { // Try to see if there is an xmp dc:description block
                    val metadata: Metadata
                    try {
                        metadata = JpegMetadataReader.readMetadata(file, JpegMetadataReader.ALL_READERS)
                        val xmpDirectory: XmpDirectory? = metadata.getFirstDirectoryOfType<XmpDirectory?>(XmpDirectory::class.java)
                        if (xmpDirectory != null && xmpDirectory.getXMPMeta() != null) {
                            log!!.debug(xmpDirectory.getXMPMeta().dumpObject())
                            /*
                        http://purl.org/dc/elements/1.1/ = "dc:"	(0x80000000 : SCHEMA_NODE)
                            dc:description	(0x1e00 : ARRAY | ARRAY_ORDERED | ARRAY_ALTERNATE | ARRAY_ALT_TEXT)
                                [1] = "MCZ-ENT00597110"	(0x50 : HAS_QUALIFIER | HAS_LANGUAGE)
                                    ?xml:lang = "x-default"	(0x20 : QUALIFIER)
                         */
                            val xmpMeta: XMPMeta = xmpDirectory.getXMPMeta()
                            try {
                                val descriptionProp = xmpMeta.getArrayItem("http://purl.org/dc/elements/1.1/", "dc:description", 1)
                                if (descriptionProp != null) {
                                    val description = descriptionProp.value
                                    log.debug(description)
                                    if (description != null && description.trim { it <= ' ' }.length > 0) {
                                        exifComment = description
                                    }
                                }
                            } catch (e1: NullPointerException) {
                                log.error(e1.message, e1)
                            } catch (e1: XMPException) {
                                log.error(e1.message, e1)
                            }
                        }
                    } catch (e1: JpegProcessingException) {
                        log!!.debug(e1.message, e1)
                    } catch (e1: IOException) {
                        log!!.error(e1.message, e1)
                    }
                }
                // cache the comment if one was found, otherwise an empty string.
                exifCommentText = exifComment
            }
            return exifCommentText
        }

    /**
     * Get the Metatdata: created from this file
     *
     * @param format the format of the desired date
     * @return the date, formated as specified by parameter format, or null if none is found
     */
    fun getExifDateCreated(format: String): String? { // Actual read attempt is only invoked once,
// subsequent calls return cached value.
        if (dateCreated == null) { // read, or re-read the file for a comment
            try {
                val metadata: Metadata = JpegMetadataReader.readMetadata(file)
                // [Exif] date
                val exifDirectory: Directory? = metadata.getFirstDirectoryOfType<ExifSubIFDDirectory?>(ExifSubIFDDirectory::class.java)
                dateCreated = exifDirectory!!.getDate(ExifSubIFDDirectory.TAG_DATETIME)
                if (dateCreated == null) {
                    dateCreated = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)
                }
                if (dateCreated == null) {
                    dateCreated = exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED)
                }
                log!!.debug("Exif DateTime = " + SimpleDateFormat.getDateInstance().format(dateCreated))
            } catch (e2: JpegProcessingException) {
                log!!.error("Error reading exif metadata.")
                log.error(e2.message)
            } catch (e: IOException) {
                log!!.error("Error reading file for exif metadata.")
                log.error(e.message)
            }
        }
        if (dateCreated != null) { // date format shown on the media bulkloader example page.
            val simpleDateFormat = SimpleDateFormat(format)
            return simpleDateFormat.format(dateCreated)
        }
        return null
    }

    /**
     * Get the Metatdata: created from this file
     *
     * @return the created date, formated dd MMMMM yyyy
     */
    val exifDateCreated: String?
        get() = getExifDateCreated("dd MMMMM yyyy")

    /**
     * Get the identifying barcode
     *
     * @return the barcode/identifier
     */
    val barcodeTextAtFoundTemplate: String?
        get() = getBarcodeText(templateUsed!!)

    /**
     * Scan part of an image for a catalogNumberBarcode, as specified by a the BarcodePosition
     * of a PositionTemplate for a QR Code barcode.
     *
     * @param positionTemplate the position template to use to identify the part of the image that may contain
     * a barcode.
     * @return a text string representing the content of the barcode, if any.
     * Check for error states with a call to getBarcodeStatus();
     */
    fun getBarcodeText(positionTemplate: PositionTemplate): String? {
        log!!.debug(catalogNumberBarcodeText)
        log.debug(catalogNumberBarcodeStatus)
        if (catalogNumberBarcodeText != null && catalogNumberBarcodeText!!.trim { it <= ' ' }.length > 0 && catalogNumberBarcodeStatus == RESULT_BARCODE_SCANNED) {
            return catalogNumberBarcodeText
        }
        var returnValue: String? = null
        if (positionTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // Check the entire image for a barcode and return.
            returnValue = barcodeText
        } else { // Check the part of the image specified by the template for the barcode.
            var image: BufferedImage? = null
            val error = ""
            barcodeStatus = RESULT_ERROR
            try {
                image = ImageIO.read(file)
            } catch (e: IOException) {
                log.error(e)
            }
            if (image == null) {
                barcodeStatus = RESULT_ERROR
                log.error("Image null. Could not decode...")
            } else {
                if (image.getWidth() > positionTemplate.getBarcodeULPosition().width && image.getWidth().toLong() == Math.round(positionTemplate.getImageSize().getWidth())) { // image might plausibly match template
                    val left: Int = positionTemplate.getBarcodeULPosition().width //* @param left x coordinate of leftmost pixels to decode
                    val top: Int = positionTemplate.getBarcodeULPosition().height //* @param top y coordinate of topmost pixels to decode
                    //*  pixels whose x coordinate is in [left,right)
                    val width: Int = positionTemplate.getBarcodeSize().width
                    val height: Int = positionTemplate.getBarcodeSize().height
                    returnValue = readBarcodeFromLocation(image, left, top, width, height, false)
                    if (returnValue != null && returnValue.length > 0) {
                        barcodeStatus = RESULT_BARCODE_SCANNED
                    } else {
                        returnValue = "Failed to read a barcode from templated location."
                        barcodeStatus = RESULT_ERROR
                    }
                    log.debug(returnValue)
                    log.debug("barcodeStatus=$barcodeStatus")
                } else { // image is narrower than templated area.
                    returnValue = "Image is different size from Template."
                    log.debug("ImageWidth=" + image.getWidth() + "; TemplateWidth=" + Math.round(positionTemplate.getImageSize().getWidth()))
                }
            }
        }
        catalogNumberBarcodeStatus = barcodeStatus
        if (barcodeStatus == RESULT_BARCODE_SCANNED) {
            catalogNumberBarcodeText = returnValue
        }
        return returnValue
    }

    /**
     * Scan the entire image for any QR Code barcode (could find a taxon barcode or a catalog number barcode).
     * Check for error states with a call to getBarcodeStatus().  Does not store or set the state with any read
     * barcode value.
     *
     * @return a string representing the text of the barcode, if any.
     */
    val barcodeText: String?
        get() {
            var returnValue: String?
            var image: BufferedImage? = null
            barcodeStatus = RESULT_ERROR
            try {
                image = ImageIO.read(file)
            } catch (e: IOException) {
                log!!.error(e)
            }
            if (image == null) {
                returnValue = null
                log!!.error("Image could not be decoded: is null")
                barcodeStatus = RESULT_ERROR
            } else {
                val source: LuminanceSource = BufferedImageLuminanceSource(image)
                try {
                    val bitmap = BinaryBitmap(HybridBinarizer(source))
                    returnValue = getQRCodeText(bitmap)
                    barcodeStatus = RESULT_BARCODE_SCANNED
                } catch (e: Exception) {
                    returnValue = null
                    log!!.error(e)
                    barcodeStatus = RESULT_ERROR
                }
            }
            return returnValue
        }

    /**
     * @return the templateUsed
     */
    fun getTemplateUsed(): PositionTemplate? {
        return templateUsed
    }

    /**
     * @param templateUsed the templateUsed to set
     */
    fun setTemplateUsed(templateUsed: PositionTemplate?) {
        this.templateUsed = templateUsed
    }

    /**
     * Utility inner class to carry text and status values from barcode reader
     * methods.
     */
    private inner class TextStatus
    /**
     * @param text
     * @param status
     */(
            /**
             * @param text the text to set
             */
            var text: String?,
            /**
             * @param status the status to set
             */
            var status: Int) {
        /**
         * @return the text
         */
        /**
         * @return the status
         */

    }

    companion object {
        const val RESULT_NOT_CHECKED = 0
        const val RESULT_BARCODE_SCANNED = 1
        const val RESULT_ERROR = 2
        // initial pixel width to try rescaling barcode areas to
        const val INITIAL_SCALING_WIDTH = 800.0
        /**
         * Matrix used for sharpening images for barcode detection.
         */
        private val matrix: FloatArray? = floatArrayOf(0f, -.5f, 0f,
                -.5f, 2.8f, -.5f,
                0f, -.5f, 0f)
        private val log = LogFactory.getLog(CandidateImageFile::class.java)
        /**
         * Self standing starting point to extract barcodes from image files.
         *
         * @param args command line arguments, run with none or -h for help.
         */
        @JvmStatic
        fun main(args: Array<String>) { // Load properties
            val properties = ImageCaptureProperties()
            Singleton.Companion.getSingletonInstance().setProperties(properties)
            log!!.debug("Properties loaded")
            val parser: CommandLineParser = PosixParser()
            val options = Options()
            options.addOption("f", "file", true, "Check one file for Barcodes.")
            options.addOption("h", "help", false, "Get help.")
            options.addOption("u", "ui", false, "Launch UI to check a directory.")
            try {
                val cmd = parser.parse(options, args)
                val hasFile = cmd!!.hasOption("file")
                val hasHelp = cmd.hasOption("help")
                if (hasFile) {
                    var exit = 1
                    val filename = cmd.getOptionValue("file")
                    val line = parseOneFile(filename!!)
                    if (line != null) {
                        println(line)
                        exit = 0
                    }
                    System.exit(exit)
                } else if (hasHelp) {
                    throw ParseException("No option specified.")
                } else { // by default, run ui.
                    try {
                        showBulkMediaGUI()
                    } catch (e: Exception) {
                        log.error(e.message, e)
                    }
                }
            } catch (e: ParseException) {
                val formatter = HelpFormatter()
                formatter.printHelp("CandidateImageFile", "Check files for a barcodes.", options, "Specify filename to check.  \nDefault if no options are selected is to launch a GUI.", true)
                System.exit(1)
            }
        }

        /**
         * Checks image height, width and exif metadata height width and orientation, writes to debug log.
         * Does nothing if logging level does not include debug.
         *
         * @param aFile to check
         */
        fun debugCheckHeightWidth(aFile: File) {
            if (log!!.isDebugEnabled) { // Only take action if log level includes debug.
                try {
                    var iHeight = 0
                    var iWidth = 0
                    try {
                        val image: BufferedImage = ImageIO.read(aFile)
                        iHeight = image.getHeight()
                        iWidth = image.getWidth()
                        log.debug("Image Height:$iHeight")
                        log.debug("Image Width:$iWidth")
                    } catch (e: IOException) {
                        log.error(e.message)
                    }
                    try {
                        val metadata: Metadata = ImageMetadataReader.readMetadata(aFile)
                        val directory: Directory? = metadata.getFirstDirectoryOfType<ExifIFD0Directory?>(ExifIFD0Directory::class.java)
                        val jpegDirectory: JpegDirectory? = metadata.getFirstDirectoryOfType<JpegDirectory?>(JpegDirectory::class.java)
                        try {
                            val orientation = directory!!.getInt(ExifIFD0Directory.TAG_ORIENTATION)
                            val width: Int = jpegDirectory.getImageWidth()
                            val height: Int = jpegDirectory.getImageHeight()
                            log.debug("Orientation: $orientation")
                            log.debug("Exif Height: $height")
                            log.debug("Exif Width: $width")
                            if (height != iHeight || width != iWidth) {
                                log.error("Warning: Image orientation height/width does not match image height/width.  Image will not display as expected.")
                            }
                            if (orientation > 1) {
                                if (orientation == 6 || orientation == 8) {
                                    if (height > width) {
                                        log.error("Warning: Image exif specifies a transformed orientation: $orientation. Image will not display as expected. ")
                                    } else {
                                        log.debug("Image exif specifies a transformed orientation: $orientation, which matches aspect ratio. Image may or may not display as expected. ")
                                    }
                                } else {
                                    log.error("Warning: Image exif specifies a transformed orientation: $orientation. Image will not display as expected. ")
                                }
                            }
                        } catch (e: MetadataException) {
                            log.debug("Error reading EXIF orientation metadata." + e.message)
                        }
                    } catch (e1: NullPointerException) {
                        log.debug("Error processing EXIF data." + e1.message)
                    } catch (e1: ImageProcessingException) {
                        log.debug("Error processing EXIF data." + e1.message)
                    } catch (e1: IOException) {
                        log.error("Error reading file. " + e1.message)
                    }
                } catch (eCatchAll: Exception) { // Eat any exception raised to make sure this debugging routine doesn't stop
// a working production process.
                    log.error("Error checking orientiation. " + eCatchAll.message)
                }
            }
        }

        protected fun showBulkMediaGUI() {
            SwingUtilities.invokeLater {
                val frame = BulkMediaFrame()
                frame.pack()
                frame.setVisible(true)
            }
        }

        protected fun parseOneFile(filename: String): String? {
            var result: String? = null
            val f = File(filename)
            debugCheckHeightWidth(f)
            try {
                val file = CandidateImageFile(f, PositionTemplate(PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS!!))
                val exif = file.exifUserCommentText
                val scan = file.barcodeText
                result = if (scan!!.startsWith("{\"m1p\":")) {
                    '"'.toString() + f.name + "\",\"" + exif + '"'
                } else {
                    if (scan == exif) {
                        '"'.toString() + f.name + "\",\"" + scan + '"'
                    } else {
                        '"'.toString() + f.name + "\",\"" + exif + '"'
                    }
                }
            } catch (e: UnreadableFileException) {
                log!!.error(e.message)
                println("Unable To Read  $filename")
            } catch (e: NoSuchTemplateException) {
                log!!.error(e.message, e)
            }
            return result
        }

        /**
         * Produce a BulkMedia record suitable for loading into the MCZbase bulk media bulkloader
         * from an image file.
         *
         * @param filename the filename to load as bulk media.
         * @return BulkMedia objeect for that file.
         */
        fun parseOneFileToBulkMedia(filename: String): BulkMedia {
            val result = BulkMedia()
            val f = File(filename)
            try {
                val file = CandidateImageFile(f, PositionTemplate(PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS!!))
                val exif = file.exifUserCommentText
                val scan = file.barcodeText
                val madeDate = file.exifDateCreated
                log!!.debug(madeDate)
                if (madeDate != null) {
                    result.setMadeDate(madeDate)
                }
                var barcode: String? = null
                result.setOriginalFilename(f.name)
                barcode = if (scan!!.startsWith("{\"m1p\":")) {
                    exif
                } else {
                    if (scan == exif) {
                        scan
                    } else {
                        exif
                    }
                }
                if (Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(barcode)) {
                    result.setCatalogNumber(Singleton.Companion.getSingletonInstance().getBarcodeBuilder().makeGuidFromBarcode(barcode))
                }
                if (filename.startsWith("http://")) {
                    result.setMedia_URI(filename)
                } else {
                    val preview_file: File = ThumbnailBuilder.Companion.getThumbFileForFile(f)
                    // TODO: Add preview uri.
                    if (!result.setURI(f)) {
                        println("Can't extract URI from path for $filename")
                    }
                    if (!result.setPreviewURI(preview_file)) {
                        println("Can't extract URI from path for preview file " + preview_file.name)
                    }
                }
                log.debug(madeDate)
            } catch (e: UnreadableFileException) {
                log!!.error(e.message)
                println("Unable To Read  $filename")
            } catch (e: NoSuchTemplateException) {
                log!!.error(e.message)
            }
            log!!.debug(result.toString())
            return result
        }

        @Throws(NotFoundException::class, ChecksumException::class, FormatException::class)
        private fun getQRCodeText(bitmap: BinaryBitmap): String? {
            val result: Result
            val returnValue: String?
            val reader = QRCodeReader()
            var hints: Hashtable<DecodeHintType?, Any?>? = null
            hints = Hashtable<DecodeHintType?, Any?>(3)
            hints[DecodeHintType.TRY_HARDER] = java.lang.Boolean.TRUE
            result = reader.decode(bitmap, hints)
            returnValue = result.text
            return returnValue
        }
    }
}