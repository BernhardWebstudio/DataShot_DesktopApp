/**
 * FieldLoader.java
 * edu.harvard.mcz.imagecapture.loader
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

import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.loader.ex.LoadException
import org.apache.commons.logging.LogFactory
import org.filteredpush.qc.date.DateUtils
import java.lang.reflect.Method
import java.util.*
import kotlin.collections.HashMap

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 *
 */
class FieldLoader {
    protected var sls: SpecimenLifeCycle? = null
    protected var knownFields: MutableMap<String?, String?>? = null
    /**
     * Setup initial conditions, construct list of known fields into which data can be put.
     */
    protected fun init() {
        sls = SpecimenLifeCycle()
        // Key: lower case of field, Value actual case of Field.
        knownFields = HashMap()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                knownFields[specimenMethods[j]!!.name.replace("^set".toRegex(), "").toLowerCase()] = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        val ipf = toParseFields.iterator()
        while (ipf.hasNext()) {
            val parseField = ipf.next()
            knownFields[parseField!!.toLowerCase()] = parseField
        }
        if (log!!.isDebugEnabled) {
            val i = knownFields.keys.iterator()
            while (i.hasNext()) {
                log.debug(i.next())
            }
        }
    }

    /**
     * Check whether or not a header is in the list of known fields.
     *
     * @param possibleField header to check
     * @return true if possibleField is (case insensitive) in the list of known
     * fields, false if not.  Throws a null pointer exception if possibleField is null.
     */
    fun isFieldKnown(possibleField: String): Boolean {
        return knownFields!!.containsKey(possibleField.toLowerCase())
    }

    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimUnclassifiedText: String?, questions: String?, overwriteExisting: Boolean): Boolean {
        return this.load(barcode, verbatimUnclassifiedText, null, questions, overwriteExisting)
    }

    /**
     * Given a barcode number and a value for verbatimUnclassifiedText, update the verbatim value for the matching
     * Specimen record, leaves the Specimen record in workflow state WorkFlowStatus.STAGE_VERBATIM.
     *
     * @param barcode                  must match exactly one Specimen record.
     * @param verbatimUnclassifiedText value for this field in Specimen.
     * @param questions                value to append to this field in Specimen.
     * @param overwriteExisting        if true, overwrite any value of verbatimUnclassifiedText in the matching Specimen record.
     * @return if the new value was saved
     * @throws LoadException on an error
     */
    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimUnclassifiedText: String?, verbatimClusterIdentifier: String?, questions: String?, overwriteExisting: Boolean): Boolean {
        var result = false
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (!overwriteExisting && !WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())
                    ||
                    overwriteExisting && !WorkFlowStatus.allowsVerbatimUpdateOverwrite(match.getWorkFlowStatus())) {
                throw LoadTargetMovedOnException()
            } else {
                if (match.getVerbatimUnclassifiedText() == null || match.getVerbatimUnclassifiedText().trim({ it <= ' ' }).length == 0 || overwriteExisting) {
                    match.setVerbatimUnclassifiedText(verbatimUnclassifiedText)
                } else {
                    throw LoadTargetPopulatedException()
                }
                match.setVerbatimClusterIdentifier(verbatimClusterIdentifier)
                // append any questions to current questions.
                if (questions != null && questions.trim { it <= ' ' }.length > 0) {
                    var currentQuestions: String = match.getQuestions()
                    if (currentQuestions == null) {
                        currentQuestions = ""
                    }
                    if (currentQuestions.trim { it <= ' ' }.length > 0) {
                        currentQuestions = "$currentQuestions | "
                    }
                    match.setQuestions(currentQuestions + questions)
                }
                match.setWorkFlowStatus(WorkFlowStatus.STAGE_VERBATIM)
                try {
                    sls.attachDirty(match)
                    result = true
                    logHistory(match, "Load:" + WorkFlowStatus.STAGE_VERBATIM + ":VerbatimUnclassifiedText", Date())
                } catch (e: SaveFailedException) {
                    log!!.error(e.message, e)
                    throw LoadTargetSaveException("Error saving updated target record: " + e.message)
                }
            }
        } else {
            throw LoadTargetRecordNotFoundException()
        }
        return result
    }

    /**
     * Give a barcode number and the set of verbatim fields, attempt to set the values for those verbatim fields for a record.
     * Does not overwrite existing non-empty values, does not modify record if any verbatim field contains a value.
     *
     * @param barcode                  field, must match on exactly one Specimen record.
     * @param verbatimLocality         value for this field in Specimen.
     * @param verbatimDate             value for this field in Specimen.
     * @param verbatimCollector        value for this field in Specimen.
     * @param verbatimCollection       value for this field in Specimen.
     * @param verbatimNumbers          value for this field in Specimen.
     * @param verbatimUnclassifiedText value for this field in Specimen.
     * @param questions                value to append to this field in Specimen.
     * @return true if record with the provided barcode number was updated.
     * @throws LoadException on an error, including any existing value for any of the verbatim fields.
     */
    @Throws(LoadException::class)
    fun load(barcode: String?, verbatimLocality: String?, verbatimDate: String?, verbatimCollector: String?, verbatimCollection: String?, verbatimNumbers: String?, verbatimUnclassifiedText: String?, questions: String?): Boolean {
        val result = false
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())) {
                if (match.getVerbatimLocality() == null || match.getVerbatimLocality().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimLocality(verbatimLocality)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getDateNos() == null || match.getDateNos().trim({ it <= ' ' }).length == 0) {
                    match.setDateNos(verbatimDate)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimCollector() == null || match.getVerbatimCollector().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimCollector(verbatimCollector)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimCollection() == null || match.getVerbatimCollection().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimCollection(verbatimCollection)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimNumbers() == null || match.getVerbatimNumbers().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimNumbers(verbatimNumbers)
                } else {
                    throw LoadTargetPopulatedException()
                }
                if (match.getVerbatimUnclassifiedText() == null || match.getVerbatimUnclassifiedText().trim({ it <= ' ' }).length == 0) {
                    match.setVerbatimUnclassifiedText(verbatimUnclassifiedText)
                } else {
                    throw LoadTargetPopulatedException()
                }
                // append any questions to current questions.
                if (questions != null && questions.trim { it <= ' ' }.length > 0) {
                    var currentQuestions: String = match.getQuestions()
                    if (currentQuestions == null) {
                        currentQuestions = ""
                    }
                    if (currentQuestions.trim { it <= ' ' }.length > 0) {
                        currentQuestions = "$currentQuestions | "
                    }
                    match.setQuestions(currentQuestions + questions)
                }
                match.setWorkFlowStatus(WorkFlowStatus.STAGE_VERBATIM)
                try {
                    sls.attachDirty(match)
                    logHistory(match, "VerbatimFields:" + WorkFlowStatus.STAGE_VERBATIM + ":", Date())
                } catch (e: SaveFailedException) {
                    log!!.error(e.message, e)
                    throw LoadTargetSaveException("Error saving updated target record: " + e.message)
                }
            } else {
                throw LoadTargetMovedOnException()
            }
        } else {
            throw LoadTargetRecordNotFoundException()
        }
        return result
    }

    /**
     * Give a barcode number and an arbitrary set of fields in Specimen, attempt to set the values for those fields for a record.
     *
     * @param barcode                     field, must match on exactly one Specimen record.
     * @param data                        map of field names and data values
     * @param questions                   value to append to this field in Specimen.
     * @param newWorkflowStatus           to set Specimen.workflowStatus to.
     * @param allowUpdateExistingVerbatim if true can load can overwrite the value in an existing verbatim field.
     * @return true if one or more fields were updated.
     * @throws LoadException on an error (particularly from inability to map keys in data to fields in Specimen.
     */
    @Throws(LoadException::class)
    fun loadFromMap(barcode: String?, data: MutableMap<String?, String?>, newWorkflowStatus: String?, allowUpdateExistingVerbatim: Boolean): Boolean {
        var result = false
        log!!.debug(barcode)
        val knownFields = ArrayList<String?>()
        val knownFieldsLowerUpper = HashMap<String?, String?>()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                val actualCase = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
                knownFields.add(specimenMethods[j]!!.name.replace("^set".toRegex(), ""))
                knownFieldsLowerUpper[actualCase.toLowerCase()] = actualCase
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        // Check that the proposed new state is allowed.
        if (newWorkflowStatus == null ||
                newWorkflowStatus != WorkFlowStatus.STAGE_VERBATIM &&
                newWorkflowStatus != WorkFlowStatus.STAGE_CLASSIFIED) {
            throw LoadException("Trying to load into unallowed new state.$newWorkflowStatus")
        }
        // Retrieve existing record for update (thus not blanking existing fields, and allowing for not updating fields with values, or appending comments).
        val matches: MutableList<Specimen?> = sls.findByBarcode(barcode)
        if (matches != null && matches.size == 1) {
            val match: Specimen? = matches[0]
            if (newWorkflowStatus == WorkFlowStatus.STAGE_VERBATIM && !WorkFlowStatus.allowsVerbatimUpdate(match.getWorkFlowStatus())
                    ||
                    newWorkflowStatus == WorkFlowStatus.STAGE_CLASSIFIED && !WorkFlowStatus.allowsClassifiedUpdate(match.getWorkFlowStatus())) { // The target Specimen record has moved on past the state where it can be altered by a data load.
                throw LoadTargetMovedOnException(barcode + " is in state " + match.getWorkFlowStatus() + " and can't be altered by this data load (to " + newWorkflowStatus + ").")
            } else { // Target Specimen record is eligible to be updated by a data load.
                var foundData = false
                var hasChange = false
                var hasExternalWorkflowProcess = false
                var hasExternalWorkflowDate = false
                val i = data.keys.iterator()
                var separator = ""
                val keys = StringBuilder()
                while (i.hasNext()) {
                    val keyOrig = i.next()
                    val key = keyOrig!!.toLowerCase()
                    val actualCase = knownFieldsLowerUpper[key]
                    if (toParseFields.contains(key) ||
                            actualCase != null && knownFields.contains(actualCase) && key != "barcode" && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key)) {
                        var datavalue = data[keyOrig]
                        keys.append(separator).append(key)
                        separator = ","
                        var setMethod: Method
                        try {
                            if (key == "collectors") { // Special case, parse collectors to associated Collector table.
                                datavalue = "$datavalue|"
                                val collectors: Array<String?> = datavalue.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                log.debug(collectors.size)
                                for (j in collectors.indices) {
                                    val collector = collectors[j]
                                    log.debug(collector)
                                    if (collector!!.trim { it <= ' ' }.length > 0) { // Check to see if Collector exists
                                        val existingCollectors: MutableSet<Collector?> = match.getCollectors()
                                        val ic = existingCollectors.iterator()
                                        var exists = false
                                        while (ic.hasNext()) {
                                            val c = ic.next()
                                            if (c!!.collectorName == collector) {
                                                exists = true
                                            }
                                        }
                                        if (!exists) { // only add if it isn't allready present.
                                            val col = Collector()
                                            col.specimen = match
                                            col.collectorName = collector
                                            val cls = CollectorLifeCycle()
                                            cls.persist(col)
                                            match.getCollectors().add(col)
                                            foundData = true
                                            hasChange = true
                                        }
                                    }
                                }
                            } else if (key.toLowerCase() == "numbers") { // Special case, parse numbers to associated Number table.
                                datavalue = "$datavalue|"
                                val numbers: Array<String?> = datavalue.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (j in numbers.indices) {
                                    val numberKV = numbers[j]
                                    if (numberKV!!.trim { it <= ' ' }.length > 0) {
                                        var number = numberKV
                                        var numType = "unknown"
                                        if (numberKV.contains(":")) {
                                            val numbits: Array<String?> = numberKV.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                            number = numbits[0]
                                            numType = numbits[1]
                                            if (numType == null || numType.trim { it <= ' ' }.length == 0) {
                                                numType = "unknown"
                                            }
                                        }
                                        // check to see if number exists
                                        val existingNumbers: MutableSet<Number?> = match.getNumbers()
                                        val ic = existingNumbers.iterator()
                                        var exists = false
                                        while (ic.hasNext()) {
                                            val c = ic.next()
                                            if (c!!.number == number || c.numberType == numType) {
                                                exists = true
                                            }
                                        }
                                        if (!exists) {
                                            val nls = NumberLifeCycle()
                                            // only add if it isn't already present.
                                            val num = Number()
                                            num.number = number
                                            num.numberType = numType
                                            num.specimen = match
                                            nls.persist(num)
                                            hasChange = true
                                            match.getNumbers().add(num)
                                            foundData = true
                                        }
                                    }
                                }
                            } else { // Find the Specimen get and set methods for the current key
                                setMethod = Specimen::class.java.getMethod("set$actualCase", String::class.java)
                                val getMethod: Method = Specimen::class.java.getMethod("get$actualCase", null)
                                // Obtain the current value in the Specimen record for the field matching the current key.
                                val currentValue = (getMethod.invoke(match, null) as String)
                                // Assess whether changes to existing data are allowed for that field,
// make changes only if they are allowed.
                                if (key == "externalworkflowprocess") {
                                    hasExternalWorkflowProcess = true
                                }
                                if (key == "externalworkflowdate") {
                                    hasExternalWorkflowDate = true
                                }
                                if (key == "questions") { // append
                                    if (currentValue != null && currentValue.trim { it <= ' ' }.length > 0) {
                                        datavalue = "$currentValue | $datavalue"
                                    }
                                    setMethod.invoke(match, datavalue)
                                    foundData = true
                                    hasChange = true
                                } else if (key == "externalworkflowprocess" || key == "externalworkflowdate" || key == "verbatimclusteridentifier") { // overwrite existing metadata
                                    setMethod.invoke(match, datavalue)
                                    foundData = true
                                    hasChange = true
                                } else {
                                    if (currentValue == null || currentValue.trim { it <= ' ' }.length == 0) { // Handle ISO date formatting variants
                                        if (key.equals("ISODate", ignoreCase = true)) {
                                            val parseResult: EventResult? = DateUtils.extractDateFromVerbatimER(datavalue)
                                            if (parseResult.getResultState() == EventResult.EventQCResultState.DATE || parseResult.getResultState() == EventResult.EventQCResultState.RANGE) {
                                                val correctISOFormat: String = parseResult.getResult()
                                                // switch from correct ISO format to the internally stored incorrect format that switches - and /.
                                                datavalue = correctISOFormat.replace("/", "^") // change / to placeholder
                                                datavalue = datavalue.replace("-", "/") // change - to /
                                                datavalue = datavalue.replace("^", "-") // change placeholder to -
                                            }
                                        }
                                        // overwrite verbatim fields if update is allowed, otherwise no overwite of existing data.
                                        log.debug("Set: $actualCase = $datavalue")
                                        setMethod.invoke(match, datavalue)
                                        foundData = true
                                    } else if (MetadataRetriever.isFieldVerbatim(Specimen::class.java, key) && allowUpdateExistingVerbatim) {
                                        setMethod.invoke(match, datavalue)
                                        foundData = true
                                        hasChange = true
                                    } else {
                                        log.error("Skipped set$actualCase = $datavalue")
                                    }
                                }
                            }
                        } catch (e: NoSuchMethodException) {
                            throw LoadException(e.message, e)
                        } catch (e: SaveFailedException) {
                            throw LoadException(e.message, e)
                        } catch (e: SecurityException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: IllegalAccessException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: IllegalArgumentException) {
                            log.error(e.message, e)
                            throw LoadException(e.message)
                        } catch (e: InvocationTargetException) {
                            log.error(e.message, e)
                            throw LoadException(e.message, e)
                        }
                    } else {
                        log.error("Key: $key")
                        log.error("Key (actual case of method): $actualCase")
                        log.error("knownFields.contains(actualCase): " + knownFields.contains(actualCase))
                        log.error("toParseFields.contains(key): " + toParseFields.contains(key))
                        log.error("isFieldExternallyUpdatable:" + MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key))
                        throw LoadException("Column $key is not an externally updateable field of Specimen.")
                    }
                }
                if (foundData) {
                    try { // save the updated specimen record
                        match.setWorkFlowStatus(newWorkflowStatus)
                        // with the user running the load job and the current date as last update.
                        match.setLastUpdatedBy(Singleton.Companion.getSingletonInstance().getUserFullName())
                        match.setDateLastUpdated(Date())
                        log.debug("Updating:" + match.getBarcode())
                        sls.attachDirty(match)
                        result = hasChange
                        // If we were provided
                        var ewProcess = "ArbitraryFieldLoad:" + match.getWorkFlowStatus() + ":" + keys.toString()
                        if (hasExternalWorkflowProcess) {
                            ewProcess = match.getExternalWorkflowProcess()
                        }
                        var ewDate = Date()
                        if (hasExternalWorkflowDate) {
                            ewDate = match.getExternalWorkflowDate()
                        }
                        logHistory(match, ewProcess, ewDate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                        throw LoadTargetSaveException()
                    }
                }
            }
        }
        return result
    }

    /**
     * Test the headers of a file for conformance with the expectations of loadFromMap
     *
     * @param headers the headers to check against allowed fields in List<String> form.
     * @return a HeaderCheckResult object containing a result (true) and a message, the
     * result is true if there are no unmatched fields in the load, currently exceptions
     * are thrown instead of any false cases for result.
     * @throws LoadException if no barcode field is found, if no data fields are found, or if
     * one or more unknown (not mapped to DataShot specimen) fields are found.
     * @see HeaderCheckResult.loadFromMap
    </String> */
    @Throws(LoadException::class)
    fun checkHeaderList(headers: MutableList<String?>): HeaderCheckResult {
        val result = HeaderCheckResult()
        val knownFields = ArrayList<String?>()
        val knownFieldsLowerUpper = HashMap<String?, String?>()
        val specimenMethods: Array<Method?> = Specimen::class.java.getDeclaredMethods()
        for (j in specimenMethods.indices) {
            if (specimenMethods[j]!!.name.startsWith("set") && specimenMethods[j]!!.parameterTypes.size == 1 && specimenMethods[j]!!.parameterTypes[0].name == String::class.java.name) {
                val actualCase = specimenMethods[j]!!.name.replace("^set".toRegex(), "")
                knownFields.add(specimenMethods[j]!!.name.replace("^set".toRegex(), ""))
                knownFieldsLowerUpper[actualCase.toLowerCase()] = actualCase
                log!!.debug(actualCase)
            }
        }
        // List of input fields that will need to be parsed into relational tables
        val toParseFields = ArrayList<String?>()
        toParseFields.add("collectors")
        toParseFields.add("numbers")
        val i = headers.iterator()
        var containsBarcode = false
        var containsAField = false
        var containsUnknownField = false
        while (i.hasNext()) {
            val keyOrig = i.next()
            val key = keyOrig!!.toLowerCase()
            if (key == "barcode") {
                containsBarcode = true
                result.addToMessage(keyOrig)
            } else {
                if (key.startsWith("_")) {
                    result.addToMessage("[$keyOrig=Skipped]")
                } else {
                    val actualCase = knownFieldsLowerUpper[key]
                    if (toParseFields.contains(key) ||
                            actualCase != null && knownFields.contains(actualCase) && MetadataRetriever.isFieldExternallyUpdatable(Specimen::class.java, key)) {
                        result.addToMessage(keyOrig)
                        containsAField = true
                    } else {
                        result.addToMessage("[$keyOrig=Unknown]")
                        containsUnknownField = true
                    }
                }
            }
        }
        if (!containsBarcode) {
            throw LoadException("Header does not contain a barcode field.  \nFields:" + result.getMessage())
        }
        if (!containsAField) {
            throw LoadException("Header contains no recognized data fields. \nFields: " + result.getMessage())
        }
        if (containsUnknownField) {
            throw LoadException("Header contains at least one unknown field. \nFields: " + result.getMessage())
        }
        result.setResult(true)
        return result
    }

    protected fun logHistory(match: Specimen?, externalWorkflowProcess: String?, externalWorkflowDate: Date?) {
        try { // log the external data import
            val history = ExternalHistory()
            history.setExternalWorkflowProcess(externalWorkflowProcess)
            history.setExternalWorkflowDate(externalWorkflowDate)
            history.setSpecimen(match)
            val els = ExternalHistoryLifeCycle()
            els.attachDirty(history)
        } catch (ex: SaveFailedException) {
            log!!.error(ex.message, ex)
        }
    }

    companion object {
        private val log = LogFactory.getLog(FieldLoader::class.java)
    }

    init {
        init()
    }
}