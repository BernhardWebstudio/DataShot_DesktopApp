package edu.harvard.mcz.imagecapture.jobsimport

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.io.File
import java.util.*
import java.util.concurrent.locks.Lock

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
abstract class AbstractFileScanJob : RunnableJob, Runnable {
    /**
     * @see java.lang.Runnable.run
     */
    override fun run() {
        start()
    }

    /**
     * Cleanup when job is complete.
     */
    protected fun done() {
        Singleton.Companion.getSingletonInstance().getJobList().removeJob(this)
    }

    companion object {
        private val log = LogFactory.getLog(AbstractFileScanJob::class.java)
        /**
         * Log errors associated with confusion between exifComment & detected barcode
         *
         * @param counter                to apply the error to
         * @param filename               the errored file
         * @param barcode                the barcode as detected
         * @param exifComment            the comment, exif as read from file
         * @param parser                 the parser used
         * @param barcodeInImageMetadata whether the barcode was extracted from metadata
         * @param log
         */
        fun logMismatch(counter: ScanCounterInterface, filename: String?, barcode: String?, exifComment: String?, parser: TaxonNameReturner?, barcodeInImageMetadata: Boolean, log: Log) {
            if (barcodeInImageMetadata || Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") { // If so configured, or if image metadata contains a barcode that doesn't match the barcode in the image
// report on barcode/comment missmatch as an error condition.
                try {
                    val error = RunnableJobError(filename, barcode,
                            barcode, exifComment, "Barcode/Comment mismatch.",
                            parser, parser as DrawerNameReturner?,
                            null, RunnableJobError.Companion.TYPE_MISMATCH)
                    counter.appendError(error)
                } catch (e: Exception) { // we don't want an exception to stop processing
                    log.error(e)
                }
            } else { // Just write into debug log
// This would normally the case where the image metadata doesn't contain a barcode but the image does, and reporting of this state as an error has been turned off.
                log.debug("Barcode/Comment mismatch: [$barcode]!=[$exifComment]")
            }
        }

        /**
         * Set family, subfamily, based on a taxon returner
         *
         * @param parser source of family, subfamily
         * @param s      the specimen to set the values on
         */
        fun extractFamilyToSpecimen(parser: TaxonNameReturner, s: Specimen) {
            val hls = HigherTaxonLifeCycle()
            if (parser.getTribe().trim({ it <= ' ' }) == "") {
                if (hls.isMatched(parser.getFamily(), parser.getSubfamily())) { // If there is a match, use it.
                    val higher: Array<String?> = hls.findMatch(parser.getFamily(), parser.getSubfamily())
                    s.setFamily(higher[0])
                    s.setSubfamily(higher[1])
                } else { // otherwise use the raw OCR output.
                    s.setFamily(parser.getFamily())
                    s.setSubfamily(parser.getSubfamily())
                }
                s.setTribe("")
            } else {
                if (hls.isMatched(parser.getFamily(), parser.getSubfamily(), parser.getTribe())) {
                    val higher: Array<String?> = hls.findMatch(parser.getFamily(), parser.getSubfamily(), parser.getTribe())
                    s.setFamily(higher[0])
                    s.setSubfamily(higher[1])
                    s.setTribe(higher[2])
                } else {
                    s.setFamily(parser.getFamily())
                    s.setSubfamily(parser.getSubfamily())
                    s.setTribe(parser.getTribe())
                }
            }
        }

        /**
         * Do actual processing of one file.
         * Static to enable threaded, state-less execution
         *
         * @param containedFile the file to process
         * @param counter       the counter to keep track of success/failure
         * @param locks         the locks to use to prevent concurrency issues
         */
        protected fun checkFile(containedFile: File, counter: ScanCounterInterface, locks: Array<Lock?>?) {
            Singleton.Companion.getSingletonInstance().getProperties().getProperties().setProperty(ImageCaptureProperties.Companion.KEY_LASTPATH, containedFile.path)
            val filename = containedFile.name
            counter.incrementFilesSeen()
            log!!.debug("Checking image file: $filename")
            CandidateImageFile.Companion.debugCheckHeightWidth(containedFile)
            // scan file for barcodes and ocr of unit tray label text
            try {
                var reattach = false // image is detached instance and should be reattached instead of persisted denovo.
                // Check for an existing image record.
                val imageLifeCycle = ICImageLifeCycle()
                var existingTemplate = ICImage()
                existingTemplate.setFilename(filename)
                val path: String = ImageCaptureProperties.Companion.getPathBelowBase(containedFile)
                existingTemplate.setPath(path)
                val matches: MutableList<ICImage?> = imageLifeCycle.findBy(object : HashMap<String?, Any?>() {
                    init {
                        put("path", path)
                        put("filename", filename)
                    }
                })
                log.debug((matches?.size ?: "no").toString() + " matches found for " + filename)
                if (matches != null && matches.size == 1 && matches[0].getRawBarcode() == null && matches[0].getRawExifBarcode() == null && (matches[0].getDrawerNumber() == null || matches[0].getDrawerNumber().trim({ it <= ' ' }).length == 0)) { // likely case for a failure to read data out of the image file
// try to update the image file record.
                    try {
                        existingTemplate = imageLifeCycle.merge(matches[0])
                        matches.removeAt(0)
                        reattach = true
                        log.debug(existingTemplate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                    }
                } else if (matches != null && matches.size == 1 && matches[0].getSpecimen() == null) { // likely case for a failure to create a specimen record in a previous run
// try to update the image file record
                    try {
                        existingTemplate = imageLifeCycle.merge(matches[0])
                        matches.removeAt(0)
                        reattach = true
                        log.debug(existingTemplate)
                    } catch (e: SaveFailedException) {
                        log.error(e.message, e)
                    }
                }
                if (matches == null || matches.size == 0) {
                    createDatabaseRecordForFile(containedFile, counter, reattach, imageLifeCycle, existingTemplate, locks)
                } else { // found an already databased file (where we have barcode/specimen or drawer number data).
                    log.debug("Record exists, skipping file $filename")
                    counter.incrementFilesExisting()
                }
            } catch (e: UnreadableFileException) {
                counter.incrementFilesFailed()
                counter.appendError(RunnableJobError(containedFile.name, "", "Could not read file", UnreadableFileException(), RunnableJobError.Companion.TYPE_FILE_READ))
                log.error("Couldn't read file " + filename + "." + e.message)
            }
        }

        /**
         * Create a new image database record.
         * Static to enable threaded, state-less execution
         *
         * @param containedFile  the file path relative to the start
         * @param counter        to count errors
         * @param reattach       whether there is already a database record existing, to be overridden
         * @param imageLifeCycle the repository to sage to
         * @param image          providing access to path & filename
         * @throws UnreadableFileException if the file could not be read
         */
        @Throws(UnreadableFileException::class)
        protected fun createDatabaseRecordForFile(containedFile: File, counter: ScanCounterInterface, reattach: Boolean, imageLifeCycle: ICImageLifeCycle, image: ICImage, locks: Array<Lock?>?) {
            var reattach = reattach
            var isSpecimenImage = false
            var isDrawerImage = false
            val matcher: BarcodeMatcher = Singleton.Companion.getSingletonInstance().getBarcodeMatcher()
            // ** Identify the template.
// String templateId = detector.detectTemplateForImage(fileToCheck);
// log.debug("Detected Template: " + templateId);
// PositionTemplate template = new PositionTemplate(templateId);
// // Found a barcode in a templated position in the image.
// // ** Scan the file based on this template.
// candidateImageFile = new CandidateImageFile(fileToCheck, template);
// Construct a CandidateImageFile with constructor that self detects template
            val candidateImageFile = CandidateImageFile(containedFile)
            val template: PositionTemplate = candidateImageFile.getTemplateUsed()
            val templateId: String = template.getName()
            log!!.debug("Detected Template: $templateId")
            log.debug(candidateImageFile.getCatalogNumberBarcodeStatus())
            var barcode: String = candidateImageFile.getBarcodeTextAtFoundTemplate()
            if (candidateImageFile.getCatalogNumberBarcodeStatus() != CandidateImageFile.Companion.RESULT_BARCODE_SCANNED) {
                log.error("Error scanning for barcode on file '" + containedFile.name + "': " + barcode)
                barcode = ""
            }
            log.debug("Barcode: of file '" + containedFile.name + "':" + barcode)
            val exifComment: String = candidateImageFile.getExifUserCommentText()
            log.debug("ExifComment: $exifComment")
            val labelRead: UnitTrayLabel = candidateImageFile.getTaxonLabelQRText(template)
            val parser: TaxonNameReturner?
            var rawOCR: String
            val state: String
            if (labelRead != null) {
                rawOCR = labelRead.toJSONString()
                log.debug("UnitTrayLabel: of file '" + containedFile.name + "':" + rawOCR)
                state = WorkFlowStatus.STAGE_1
                parser = labelRead
            } else {
                try {
                    rawOCR = candidateImageFile.getTaxonLabelOCRText(template)
                } catch (e: OCRReadException) {
                    log.error(e)
                    rawOCR = ""
                    log.error("Couldn't OCR file '" + containedFile.name + "':." + e.message)
                    val error = RunnableJobError(image.getFilename(), "OCR Failed",
                            barcode, exifComment, "Couldn't find text to OCR for taxon label.",
                            null, null,
                            e, RunnableJobError.Companion.TYPE_NO_TEMPLATE)
                    counter.appendError(error)
                }
                if (rawOCR == null) {
                    rawOCR = ""
                }
                state = WorkFlowStatus.STAGE_0
                parser = UnitTrayLabelParser(rawOCR)
                // Provide error message to distinguish between entirely OCR or
                if ((parser as UnitTrayLabelParser?).isParsedFromJSON()) {
                    val error = RunnableJobError(image.getFilename(), "OCR Failover found barcode.",
                            barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR, but OCR found taxon barcode.",
                            parser, null,
                            null, RunnableJobError.Companion.TYPE_FAILOVER_TO_OCR)
                    counter.appendError(error)
                } else {
                    val error = RunnableJobError(image.getFilename(), "TaxonLabel read failed.",
                            barcode, exifComment, "Couldn't read Taxon barcode, failed over to OCR only.",
                            parser, null,
                            null, RunnableJobError.Companion.TYPE_FAILOVER_TO_OCR)
                    counter.appendError(error)
                }
            }
            // Test: is exifComment a barcode:-
// Case 1: This is an image of papers associated with a container (a unit tray or a box).
// This case can be identified by there being no barcode data associated with the image.
// Action:
// A) Check the exifComment to see what metadata is there, if blank, User needs to fix.
//    exifComment may contain a drawer number, identifying this as a drawer image.  Save as such.
// Options: A drawer, for which number is captured.  A unit tray, capture ?????????.  A specimen
// where barcode wasn't read, allow capture of barcode and treat as Case 2.
// B) Create an image record and store the image metadata (with a null specimen_id).
// Case 2: This is an image of a specimen and associated labels or an image assocated with
// a specimen with the specimen's barcode label in the image.
// This case can be identified by there being a barcode in a templated position or there
// being a barcode in the exif comment tag.
// Action:
// A) Check if a specimen record exists, if not, create one from the barcode and OCR data.
// B) Create an image record and store the image metadata.
            if (matcher.matchesPattern(exifComment)
                    || matcher.matchesPattern(barcode)) {
                isSpecimenImage = true
                println(containedFile.name + " is a Specimen Image")
            } else {
                if (exifComment != null && exifComment.matches(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                    isDrawerImage = true
                    println(containedFile.name + " is a Drawer Image")
                } else { // no way we could continue from here on
                    val errorType: Int = if (templateId == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) RunnableJobError.Companion.TYPE_NO_TEMPLATE else RunnableJobError.Companion.TYPE_UNKNOWN
                    val error = RunnableJobError(image.getFilename(), barcode,
                            barcode, exifComment, "Image doesn't appear to contain a barcode in a templated position.",
                            null, null,
                            null, errorType)
                    counter.appendError(error)
                    counter.incrementFilesFailed()
                    return
                }
            }
            image.setRawBarcode(barcode)
            if (isSpecimenImage) {
                createDatabaseRecordForSpecimen(containedFile, counter, image, barcode, exifComment, parser, labelRead, state, locks)
                // reattach as we force a save by having cascade= all
                reattach = true
            }
            if (isDrawerImage) {
                image.setDrawerNumber(exifComment)
            } else {
                image.setRawExifBarcode(exifComment)
                image.setDrawerNumber((parser as DrawerNameReturner?).getDrawerNumber())
            }
            image.setRawOcr(rawOCR)
            image.setTemplateId(template.getTemplateId())
            image.setPath(image.getPath())
            // Create md5hash of image file, persist with image
            if (image.getMd5sum() == null || image.getMd5sum().length == 0) {
                try {
                    image.setMd5sum(DigestUtils.md5Hex(FileInputStream(containedFile)))
                } catch (e: IOException) {
                    log.error(e.message)
                }
            }
            try {
                if (reattach) { // Update image file record
                    imageLifeCycle.attachDirty(image)
                    log.debug("Updated " + image.toString())
                    counter.incrementFilesUpdated()
                } else { // *** Save a database record of the image file.
                    imageLifeCycle.persist(image)
                    log.debug("Saved " + image.toString())
                    counter.incrementFilesDatabased()
                }
            } catch (e: SaveFailedException) {
                log.error(e.message, e)
                counter.incrementFilesFailed()
                val failureMessage = "Failed to save image record for image " + containedFile.name + ".  " + e.message
                val error = RunnableJobError(image.getFilename(), "Save Failed",
                        image.getFilename(), image.getPath(), failureMessage,
                        null, null,
                        null, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                counter.appendError(error)
            }
            if (isSpecimenImage) {
                if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") { // If so configured, log as error
                    if (image.getRawBarcode() != image.getRawExifBarcode()) {
                        log.error("Warning: Scanned Image '" + containedFile.name + "' has missmatch between barcode and comment.")
                    }
                }
            }
        }

        /**
         * Create or update an existing Specimen record to associate it with the ICImage.
         * Static to enable threaded, state-less execution.
         * BUT, since multiple images could reference the same specimen, we have to lock stuff
         *
         * @param containedFile the file of the image
         * @param counter       counter to add errors
         * @param image         the image to associate with the specimen
         * @param barcode       the barcode associated with the specimen
         * @param exifComment   the exif metadata comment to enable additional data extraction
         * @param unitTrayLabel the unit tray label of the Specimen
         * @param state         the workflow state we are in with the specimen/image
         */
        protected fun createDatabaseRecordForSpecimen(containedFile: File, counter: ScanCounterInterface, image: ICImage, barcode: String?, exifComment: String?, parser: TaxonNameReturner, unitTrayLabel: UnitTrayLabel?, state: String, locks: Array<Lock?>?) {
            var barcode = barcode
            val matcher: BarcodeMatcher = Singleton.Companion.getSingletonInstance().getBarcodeMatcher()
            val rawBarcode = barcode
            if (rawBarcode != exifComment) { // Log the missmatch
                logMismatch(counter, image.getFilename(), barcode, exifComment, parser, matcher.matchesPattern(exifComment), log!!)
            }
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Creating new specimen record for file " + containedFile.name + ".")
            var s: Specimen? = Specimen()
            if (!matcher.matchesPattern(barcode)
                    && matcher.matchesPattern(exifComment)) { // special case: couldn't read QR code barcode from image, but it was present in exif comment.
                s.setBarcode(exifComment)
                barcode = exifComment
            } else {
                if (!matcher.matchesPattern(barcode)) { // Won't be able to save the specimen record if we end up here.
                    log!!.error("Neither exifComment nor QR Code barcode match the expected pattern for a barcode, but isSpecimenImage got set to true.")
                }
                s.setBarcode(barcode)
            }
            // check if there already exists a specimen to add the image to
// but make sure you are the only to try.
            val lock = locks!![barcode.hashCode() and locks.size - 1]
            if (lock == null) {
                log!!.debug("Lock null, fetched from " + barcode.hashCode() + ", " + locks.size + " and " + (barcode.hashCode() and locks.size - 1))
            }
            lock!!.lock()
            try {
                val specimenLifeCycle = SpecimenLifeCycle()
                val existingSpecimens: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(s.getBarcode())
                if (existingSpecimens != null && existingSpecimens.size > 0) {
                    counter.incrementSpecimenExisting()
                    assert(existingSpecimens.size == 1)
                    for (specimen in existingSpecimens) {
                        image.setSpecimen(specimen)
                        specimen.getICImages().add(image)
                        try {
                            specimenLifeCycle.attachDirty(specimen)
                        } catch (e: SaveFailedException) {
                            counter.appendError(RunnableJobError(containedFile.name, barcode, specimen.getSpecimenId().toString(), "Failed adding image to existing Specimen", e, RunnableJobError.Companion.TYPE_SAVE_FAILED))
                        }
                    }
                    return
                }
                s.setWorkFlowStatus(state)
                if (unitTrayLabel != null) { //  We got json data from a barcode.
                    s.setFamily(parser.getFamily())
                    s.setSubfamily(parser.getSubfamily())
                    s.setTribe(parser.getTribe())
                } else { // We failed over to OCR, try lookup in DB.
                    s.setFamily("") // make sure there's a a non-null value in family.
                    extractFamilyToSpecimen(parser, s)
                }
                if (state == WorkFlowStatus.STAGE_0) { // Look up likely matches for the OCR of the higher taxa in the HigherTaxon authority file.
                    if (parser.getFamily() != "") { // check family against database (with a soundex match)
                        val hls = HigherTaxonLifeCycle()
                        val match: String = hls.findMatch(parser.getFamily())
                        if (match != null && match.trim { it <= ' ' } != "") {
                            s.setFamily(match)
                        }
                    }
                }
                // trim family to fit (in case multiple parts of taxon name weren't parsed
// and got concatenated into family field.
                setBasicSpecimenFromParser(parser, s)
                s.setCreatingPath(ImageCaptureProperties.Companion.getPathBelowBase(containedFile))
                s.setCreatingFilename(containedFile.name)
                if (parser.getIdentifiedBy() != null && parser.getIdentifiedBy().length > 0) {
                    s.setIdentifiedBy(parser.getIdentifiedBy())
                }
                log!!.debug(s.getCollection())
                s.setCreatedBy(ImageCaptureApp.APP_NAME + " " + ImageCaptureApp.getAppVersion())
                s.setDateCreated(Date())
                try { // *** Save a database record of the specimen.
                    specimenLifeCycle.persist(s)
                    counter.incrementSpecimenDatabased()
                    s.attachNewPart()
                } catch (e: SpecimenExistsException) {
                    log.debug(e.message)
                    // Expected case on scanning a second image for a specimen.
// Doesn't need to be reported as a parsing error.
//
// Look up the existing record to link this specimen to it.
                    try {
                        val checkResult: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(barcode)
                        if (checkResult.size == 1) {
                            s = checkResult[0]
                        }
                    } catch (e2: Exception) {
                        s = null // so that saving the image record doesn't fail on trying to save linked transient specimen record.
                        val errorMessage = "Linking Error: \nFailed to link image to existing specimen record.\n"
                        val error = RunnableJobError(image.getFilename(), barcode,
                                rawBarcode, exifComment, errorMessage,
                                parser, parser as DrawerNameReturner,
                                e2, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                        counter.appendError(error)
                    }
                } catch (e: SaveFailedException) { // Couldn't save for some reason other than the
// specimen record already existing.  Check for possible
// save problems resulting from parsing errors.
                    log.error(e)
                    try {
                        val checkResult: MutableList<Specimen?> = specimenLifeCycle.findByBarcode(barcode)
                        if (checkResult.size == 1) {
                            s = checkResult[0]
                        }
                        // Drawer number with length limit (and specimen that fails to save at over this length makes
// a good canary for labels that parse very badly.
                        if ((parser as DrawerNameReturner).getDrawerNumber().length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) {
                            var badParse = ""
                            badParse = "Parsing problem. \nDrawer number is too long: " + s.getDrawerNumber() + "\n"
                            val error = RunnableJobError(image.getFilename(), barcode,
                                    rawBarcode, exifComment, badParse,
                                    parser, parser as DrawerNameReturner,
                                    e, RunnableJobError.Companion.TYPE_BAD_PARSE)
                            counter.appendError(error)
                        } else {
                            val error = RunnableJobError(image.getFilename(), barcode,
                                    rawBarcode, exifComment, e.message,
                                    parser, parser as DrawerNameReturner,
                                    e, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                            counter.appendError(error)
                        }
                    } catch (err: Exception) {
                        log.error(err)
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
                        val error = RunnableJobError(image.getFilename(), barcode,
                                rawBarcode, exifComment, badParse,
                                parser, parser as DrawerNameReturner,
                                err, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                        counter.appendError(error)
                        counter.incrementFilesFailed()
                        s = null
                    }
                } catch (ex: Exception) {
                    log.error(ex)
                    val error = RunnableJobError(image.getFilename(), barcode,
                            rawBarcode, exifComment, ex.message,
                            parser, parser as DrawerNameReturner,
                            ex, RunnableJobError.Companion.TYPE_SAVE_FAILED)
                    counter.appendError(error)
                }
            } finally {
                lock.unlock()
            }
            if (s != null) {
                image.setSpecimen(s)
            }
        }

        protected fun setBasicSpecimenFromParser(parser: TaxonNameReturner?, s: Specimen): Specimen {
            if (s.getFamily().length > 40) {
                s.setFamily(s.getFamily().substring(0, 40))
            }
            if (parser != null) {
                s.setGenus(parser.getGenus())
                s.setSpecificEpithet(parser.getSpecificEpithet())
                s.setSubspecificEpithet(parser.getSubspecificEpithet())
                s.setInfraspecificEpithet(parser.getInfraspecificEpithet())
                s.setInfraspecificRank(parser.getInfraspecificRank())
                s.setAuthorship(parser.getAuthorship())
                s.setDrawerNumber((parser as DrawerNameReturner?).getDrawerNumber())
                s.setCollection((parser as CollectionReturner?).getCollection())
            }
            return s
        }
    }
}