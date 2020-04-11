/**
 * LabelEncoder.java
 * edu.harvard.mcz.imagecapture.encoder
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
package edu.harvard.mcz.imagecapture.encoderimport

import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.itextpdf.text.*
import org.apache.commons.logging.LogFactory
import java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * LabelEncoder
 */
class LabelEncoder(aLabel: UnitTrayLabel?) {
    private val label: UnitTrayLabel?// TODO Auto-generated catch block
    // set ErrorCorrectionLevel here
    private val qRCodeMatrix: BitMatrix?
        private get() {
            var result: BitMatrix? = null
            val writer = QRCodeWriter()
            try {
                val data: String = label.toJSONString()
                val hints: Hashtable<EncodeHintType?, ErrorCorrectionLevel?> = Hashtable<EncodeHintType?, ErrorCorrectionLevel?>() // set ErrorCorrectionLevel here
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                result = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints)
            } catch (e: WriterException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return result
        }// TODO Auto-generated catch block// TODO Auto-generated catch block

    // ZXing now has MatrixToImageWriter to avoid looping through byte arrays.
    val image: Image?
        get() {
            val barcode = qRCodeMatrix
            val bufferedImage: BufferedImage = MatrixToImageWriter.toBufferedImage(barcode)
            // ZXing now has MatrixToImageWriter to avoid looping through byte arrays.
            var image: Image? = null
            try {
                image = Image.getInstance(bufferedImage, null)
            } catch (e: BadElementException) { // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return image
        }

    companion object {
        private val log = LogFactory.getLog(LabelEncoder::class.java)
        @Throws(PrintFailedException::class)
        fun printList(taxa: MutableList<UnitTrayLabel?>?): Boolean {
            var taxa: MutableList<UnitTrayLabel?>? = taxa
            var result = false
            var label = UnitTrayLabel()
            var encoder = LabelEncoder(label)
            var image = encoder.image
            var counter = 0
            try {
                val document = Document()
                PdfWriter.getInstance(document, FileOutputStream("labels.pdf"))
                document.pageSize = PageSize.LETTER
                document.open()
                var table = PdfPTable(4)
                table.setWidthPercentage(100f)
                //table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                val cellWidths = floatArrayOf(30f, 20f, 30f, 20f)
                table.setWidths(cellWidths)
                val uls = UnitTrayLabelLifeCycle()
                if (taxa == null) {
                    taxa = uls.findAll()
                }
                var i: MutableIterator<UnitTrayLabel?> = taxa.iterator()
                var cell: PdfPCell? = null
                var cell_barcode: PdfPCell? = null
                // Create two lists of 12 cells, the first 6 of each representing
// the left hand column of 6 labels, the second 6 of each
// representing the right hand column.
// cells holds the text for each label, cells_barcode the barcode.
                val cells: ArrayList<PdfPCell?> = ArrayList<PdfPCell?>(12)
                val cells_barcode: ArrayList<PdfPCell?> = ArrayList<PdfPCell?>(12)
                for (x in 0..11) {
                    cells.add(null)
                    cells_barcode.add(null)
                }
                var cellCounter = 0
                while (i.hasNext()) { // Loop through all of the taxa (unit tray labels) found to print
                    label = i.next()
                    for (toPrint in 0 until label.getNumberToPrint()) { // For each taxon, loop through the number of requested copies
// Generate a text and a barcode cell for each, and add to array for page
                        log!!.debug("Label " + toPrint + " of " + label.getNumberToPrint())
                        cell = PdfPCell()
                        cell.setBorderColor(BaseColor.LIGHT_GRAY)
                        cell.setVerticalAlignment(PdfPCell.ALIGN_TOP)
                        cell.disableBorderSide(PdfPCell.RIGHT)
                        cell.setPaddingLeft(3f)
                        var higherNames = ""
                        higherNames = if (label.getTribe().trim({ it <= ' ' }).length > 0) {
                            label.getFamily() + ": " + label.getSubfamily() + ": " + label.getTribe()
                        } else {
                            label.getFamily() + ": " + label.getSubfamily()
                        }
                        val higher = Paragraph()
                        higher.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        higher.add(Chunk(higherNames))
                        cell.addElement(higher)
                        val name = Paragraph()
                        val genus = Chunk(label.getGenus().trim({ it <= ' ' }) + " ")
                        genus.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        var species = Chunk(label.getSpecificEpithet().trim({ it <= ' ' }))
                        var normal: Chunk? = null // normal font prefix to preceed specific epithet (nr. <i>epithet</i>)
                        if (label.getSpecificEpithet().contains(".") || label.getSpecificEpithet().contains("[")) {
                            if (label.getSpecificEpithet().startsWith("nr. ")) {
                                normal = Chunk("nr. ")
                                normal.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                                species = Chunk(label.getSpecificEpithet().trim({ it <= ' ' }).substring(4))
                                species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                            } else {
                                species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                            }
                        } else {
                            species.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        }
                        var s = ""
                        s = if (label.getSubspecificEpithet().trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val subspecies = Chunk(s + label.getSubspecificEpithet().trim({ it <= ' ' }))
                        if (label.getSubspecificEpithet().contains(".") || label.getSubspecificEpithet().contains("[")) {
                            subspecies.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        } else {
                            subspecies.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        }
                        s = if (label.getInfraspecificRank().trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val infraRank = Chunk(s + label.getInfraspecificRank().trim({ it <= ' ' }))
                        infraRank.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        s = if (label.getInfraspecificEpithet().trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val infra = Chunk(s + label.getInfraspecificEpithet().trim({ it <= ' ' }))
                        infra.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.ITALIC)
                        s = if (label.getUnNamedForm().trim({ it <= ' ' }).length > 0) {
                            " "
                        } else {
                            ""
                        }
                        val unNamed = Chunk(s + label.getUnNamedForm().trim({ it <= ' ' }))
                        unNamed.font = Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)
                        name.add(genus)
                        if (normal != null) {
                            name.add(normal)
                        }
                        name.add(species)
                        name.add(subspecies)
                        name.add(infraRank)
                        name.add(infra)
                        name.add(unNamed)
                        cell.addElement(name)
                        val authorship = Paragraph()
                        authorship.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                        if (label.getAuthorship() != null && label.getAuthorship().length > 0) {
                            val c_authorship = Chunk(label.getAuthorship())
                            authorship.add(c_authorship)
                        }
                        cell.addElement(authorship)
                        //cell.addElement(new Paragraph(" "));
                        if (label.getDrawerNumber() != null && label.getDrawerNumber().length > 0) {
                            val drawerNumber = Paragraph()
                            drawerNumber.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                            val c_drawerNumber = Chunk(label.getDrawerNumber())
                            drawerNumber.add(c_drawerNumber)
                            cell.addElement(drawerNumber)
                        } else {
                            if (label.getCollection() != null && label.getCollection().length > 0) {
                                val collection = Paragraph()
                                collection.font = Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)
                                val c_collection = Chunk(label.getCollection())
                                collection.add(c_collection)
                                cell.addElement(collection)
                            }
                        }
                        cell_barcode = PdfPCell()
                        cell_barcode.setBorderColor(BaseColor.LIGHT_GRAY)
                        cell_barcode.disableBorderSide(PdfPCell.LEFT)
                        cell_barcode.setVerticalAlignment(PdfPCell.ALIGN_TOP)
                        encoder = LabelEncoder(label)
                        image = encoder.image
                        image!!.alignment = Image.ALIGN_TOP
                        cell_barcode.addElement(image)
                        cells.add(cellCounter, cell)
                        cells_barcode.add(cellCounter, cell_barcode)
                        cellCounter++
                        // If we have hit a full set of 12 labels, add them to the document
// in two columns, filling left column first, then right
                        if (cellCounter == 12) { // add a page of 12 cells in columns of two.
                            for (x in 0..5) {
                                if (cells[x] == null) {
                                    val c = PdfPCell()
                                    c.setBorder(0)
                                    table.addCell(c)
                                    table.addCell(c)
                                } else {
                                    table.addCell(cells[x])
                                    table.addCell(cells_barcode[x])
                                }
                                if (cells[x + 6] == null) {
                                    val c = PdfPCell()
                                    c.setBorder(0)
                                    table.addCell(c)
                                    table.addCell(c)
                                } else {
                                    table.addCell(cells[x + 6])
                                    table.addCell(cells_barcode[x + 6])
                                }
                            }
                            // Reset to begin next page
                            cellCounter = 0
                            document.add(table)
                            table = PdfPTable(4)
                            table.setWidthPercentage(100f)
                            table.setWidths(cellWidths)
                            for (x in 0..11) {
                                cells[x] = null
                                cells_barcode[x] = null
                            }
                        }
                    } // end loop through toPrint (for a taxon)
                    counter++
                } // end while results has next (for all taxa requested)
                // get any remaining cells in pairs
                for (x in 0..5) {
                    if (cells[x] == null) {
                        val c = PdfPCell()
                        c.setBorder(0)
                        table.addCell(c)
                        table.addCell(c)
                    } else {
                        table.addCell(cells[x])
                        table.addCell(cells_barcode[x])
                    }
                    if (cells[x + 6] == null) {
                        val c = PdfPCell()
                        c.setBorder(0)
                        table.addCell(c)
                        table.addCell(c)
                    } else {
                        table.addCell(cells[x + 6])
                        table.addCell(cells_barcode[x + 6])
                    }
                }
                // add any remaining cells
                document.add(table)
                try {
                    document.close()
                } catch (e: Exception) {
                    throw PrintFailedException("No labels to print." + e.message)
                }
                // Check to see if there was content in the document.
                if (counter == 0) {
                    result = false
                } else { // Printed to pdf ok.
                    result = true
                    // Increment number printed.
                    i = taxa.iterator()
                    while (i.hasNext()) {
                        label = i.next()
                        for (toPrint in 0 until label.getNumberToPrint()) {
                            label.setPrinted(label.getPrinted() + 1)
                        }
                        label.setNumberToPrint(0)
                        try {
                            uls.attachDirty(label)
                        } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: FileNotFoundException) { // TODO Auto-generated catch block
                e.printStackTrace()
                throw PrintFailedException("File not found.")
            } catch (e: DocumentException) { // TODO Auto-generated catch block
                e.printStackTrace()
                throw PrintFailedException("Error buiding PDF document.")
            } catch (e: OutOfMemoryError) {
                println("Out of memory error. " + e.message)
                println("Failed.  Too many labels.")
                throw PrintFailedException("Ran out of memory, too many labels at once.")
            }
            return result
        }

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val uls = UnitTrayLabelLifeCycle()
                val taxa: MutableList<UnitTrayLabel?> = uls.findAll()
                val ok = printList(taxa)
            } catch (e: PrintFailedException) {
                println("Failed to print all.  " + e.message)
            }
            println("Done.")
        }
    }

    init {
        label = aLabel
    }
}