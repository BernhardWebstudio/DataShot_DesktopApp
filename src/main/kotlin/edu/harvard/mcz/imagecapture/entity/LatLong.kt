/**
 * LatLong.java
 * edu.harvard.mcz.imagecapture.data
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
package edu.harvard.mcz.imagecapture.entityimport

import java.io.Serializable
import java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * The persistent class for the LAT_LONG database table.
 */
class LatLong : Serializable, Cloneable {
    var latLongId: Long? = null
    var acceptedLatLongFg: Boolean? = true
    var datum: String? = null
        get() = if (field == null) "" else field
    private var decLat: BigDecimal? = null
    private var decLatMin: BigDecimal? = null
    private var decLong: BigDecimal? = null
    private var decLongMin: BigDecimal? = null
    var determinedByAgent: String? = null
    var determinedDate: Date? = Date()
    private var extent: BigDecimal? = null
    var fieldVerifiedFg: Boolean? = null
    var georefmethod: String? = ""
    private var gpsaccuracy: BigDecimal? = null
    var latDeg: Int? = null
    var latDir: String? = null
    var latLongForNnpFg: Boolean? = null
    var latLongRefSource: String? = "unknown"
    var latLongRemarks: String? = null
    var latMin: Int? = null
    private var latSec: BigDecimal? = null
    var longDeg: Int? = null
    var longDir: String? = null
    var longMin: Int? = null
    private var longSec: BigDecimal? = null
    var maxErrorDistance: Int? = null
    private var maxErrorUnits: String? = null
    var nearestNamedPlace: String? = null
    var origLatLongUnits: String? = "decimal degrees"
    /* Valid values for origLatLongUnits:
        decimal degrees
        deg. min. sec.
        degrees dec. minutes
        unknown
                 */
    var utmEw: Int? = null
    var utmNs: Int? = null
    var utmZone: String? = null
    var verificationstatus: String? = "unknown"
    private var specimenId: Specimen? = null
    val isEmpty: Boolean
        get() = equalsOneD(this, LatLong())

    fun getDecLat(): BigDecimal? {
        return decLat
    }

    fun setDecLat(decLat: BigDecimal?) {
        this.decLat = decLat
    }

    val decLatString: String?
        get() = if (decLat == null) {
            ""
        } else {
            decLat.toPlainString()
        }

    fun getDecLatMin(): BigDecimal? {
        return decLatMin
    }

    fun setDecLatMin(decLatMin: BigDecimal?) {
        this.decLatMin = decLatMin
    }

    val decLatMinString: String?
        get() = if (decLatMin == null) {
            ""
        } else {
            decLatMin.toPlainString()
        }

    fun getDecLong(): BigDecimal? {
        return decLong
    }

    fun setDecLong(decLong: BigDecimal?) {
        this.decLong = decLong
    }

    val decLongString: String?
        get() = if (decLong == null) {
            ""
        } else {
            decLong.toPlainString()
        }

    fun getDecLongMin(): BigDecimal? {
        return decLongMin
    }

    fun setDecLongMin(decLongMin: BigDecimal?) {
        this.decLongMin = decLongMin
    }

    fun getExtent(): BigDecimal? {
        return extent
    }

    fun setExtent(extent: BigDecimal?) {
        this.extent = extent
    }

    fun getGpsaccuracy(): BigDecimal? {
        return gpsaccuracy
    }

    fun setGpsaccuracy(gpsaccuracy: BigDecimal?) {
        this.gpsaccuracy = gpsaccuracy
    }

    fun getLatSec(): BigDecimal? {
        return latSec
    }

    fun setLatSec(latSec: BigDecimal?) {
        this.latSec = latSec
    }

    fun getLongSec(): BigDecimal? {
        return longSec
    }

    fun setLongSec(longSec: BigDecimal?) {
        this.longSec = longSec
    }

    fun getMaxErrorUnits(): String? {
        if (maxErrorUnits == null || maxErrorUnits!!.length == 0) {
            maxErrorUnits = "m"
        }
        return maxErrorUnits
    }

    fun setMaxErrorUnits(maxErrorUnits: String?) {
        this.maxErrorUnits = maxErrorUnits
    }

    var specimen: edu.harvard.mcz.imagecapture.entityimport.Specimen?
        get() = specimenId
        set(specimen) {
            specimenId = specimen
        }

    // legacy as long as the hibernate property is called specimenId
    fun getSpecimenId(): Specimen? {
        return specimenId
    }

    // legacy as long as the hibernate property is called specimenId
    fun setSpecimenId(specimenId: Specimen?) {
        this.specimenId = specimenId
    }

    /**
     * @return
     */
    val latDegString: String
        get() = if (latDeg == null) {
            ""
        } else {
            latDeg.toString()
        }

    /**
     * @return
     */
    val latMinString: String
        get() = if (latMin == null) {
            ""
        } else {
            latMin.toString()
        }

    /**
     * @return
     */
    val latSecString: String?
        get() = if (latSec == null) {
            ""
        } else {
            latSec.toPlainString()
        }

    /**
     * @return
     */
    val longDegString: String
        get() = if (longDeg == null) {
            ""
        } else {
            longDeg.toString()
        }

    /**
     * @return
     */
    val decLongMinString: String?
        get() = if (decLongMin == null) {
            ""
        } else {
            decLongMin.toPlainString()
        }

    /**
     * @return
     */
    val longMinString: String
        get() = if (longMin == null) {
            ""
        } else {
            longMin.toString()
        }

    /**
     * @return
     */
    val longSecString: String?
        get() = if (longSec == null) {
            ""
        } else {
            longSec.toPlainString()
        }

    /**
     * @return
     */
    val maxErrorDistanceString: String
        get() = if (maxErrorDistance == null) {
            ""
        } else {
            maxErrorDistance.toString()
        }

    /**
     * @return
     */
    val gpsaccuracyString: String?
        get() = if (gpsaccuracy == null) {
            ""
        } else {
            gpsaccuracy.toPlainString()
        }

    /**
     * Check whether this object has the same properties as a similar object.
     * Note that due to historical reasons, not every property is compared.
     * If need to check for strict equality, compare the id's!
     * Examples of uncompaired properties:
     *
     * @param coord
     * @return whether these few properties are equal.
     * @see LatLong.equalsOneD
     */
    fun equals(coord: LatLong): Boolean {
        return equalsOneD(coord, this) && equalsOneD(this, coord)
    }

    /**
     * Check whether two objects are similar in the sense where one of the objects
     * Examples of uncompaired properties:
     * - georefmethod: had different defaults over time, so we could get false
     * positives
     * - fieldVerifiedFg: had undefined behaviour in some cases
     * - latLongForNnpFg: had undefined behaviour in some cases
     *
     * @param subject    this object may have null/""/0.0 values to return true
     * @param defaultVal this object has the acceptable default values to return true
     * @return
     */
    private fun equalsOneD(subject: LatLong, defaultVal: LatLong): Boolean {
        return subject.acceptedLatLongFg === defaultVal.acceptedLatLongFg &&
                emptyOrEqual(subject.decLat, defaultVal.decLat) &&
                emptyOrEqual(subject.decLatMin, defaultVal.decLatMin) &&
                emptyOrEqual(subject.decLong, defaultVal.decLong) &&
                emptyOrEqual(subject.decLongMin, defaultVal.decLongMin) &&
                emptyOrEqual(subject.determinedByAgent, defaultVal.determinedByAgent) &&
                emptyOrEqual(subject.extent, defaultVal.extent) &&
                emptyOrEqual(subject.latDeg, defaultVal.latDeg) &&
                emptyOrEqual(subject.latDir, defaultVal.latDir) &&
                emptyOrEqual(subject.latMin, defaultVal.latMin) &&
                emptyOrEqual(subject.latSec, defaultVal.latSec) &&
                emptyOrEqual(subject.gpsaccuracy, defaultVal.gpsaccuracy) &&
                emptyOrEqual(subject.maxErrorDistance, defaultVal.maxErrorDistance)
    }

    private fun emptyOrEqual(subject: String?, defaultVal: String?): Boolean {
        return subject == null || subject == "" || subject == defaultVal
    }

    private fun emptyOrEqual(subject: Number?, defaultVal: Number?): Boolean {
        return subject == null || subject.doubleValue() == 0.0 || subject == defaultVal
    }

    public override fun clone(): LatLong {
        val newgeo = LatLong()
        newgeo.acceptedLatLongFg = acceptedLatLongFg
        newgeo.datum = datum
        newgeo.setDecLat(getDecLat())
        newgeo.setDecLatMin(getDecLatMin())
        newgeo.setDecLong(getDecLong())
        newgeo.setDecLongMin(getDecLongMin())
        newgeo.determinedByAgent = determinedByAgent
        newgeo.determinedDate = determinedDate
        newgeo.setExtent(getExtent())
        newgeo.fieldVerifiedFg = fieldVerifiedFg
        newgeo.georefmethod = georefmethod
        newgeo.setGpsaccuracy(getGpsaccuracy())
        newgeo.latDeg = latDeg
        newgeo.latDir = latDir
        newgeo.latLongForNnpFg = latLongForNnpFg
        newgeo.latLongRefSource = latLongRefSource
        newgeo.latLongRemarks = latLongRemarks
        newgeo.latMin = latMin
        newgeo.setLatSec(getLatSec())
        newgeo.longDeg = longDeg
        newgeo.longDir = longDir
        newgeo.longMin = longMin
        newgeo.setLongSec(getLongSec())
        newgeo.maxErrorDistance = maxErrorDistance
        newgeo.setMaxErrorUnits(getMaxErrorUnits())
        newgeo.nearestNamedPlace = nearestNamedPlace
        newgeo.origLatLongUnits = origLatLongUnits
        newgeo.utmEw = utmEw
        newgeo.utmNs = utmNs
        newgeo.utmZone = utmZone
        newgeo.verificationstatus = verificationstatus
        return newgeo
    }

    companion object {
        private const val serialVersionUID = 1L
        val georefMethodValues: MutableList<String?>
            get() {
                val result: MutableList<String?> = ArrayList()
                result.add("not recorded")
                result.add("unknown")
                result.add("GEOLocate")
                result.add("Google Earth")
                result.add("Gazeteer")
                result.add("GPS")
                result.add("MaNIS/HertNet/ORNIS Georeferencing Guidelines")
                return result
            }

        val datumValues: MutableList<String?>
            get() {
                val result: MutableList<String?> = ArrayList()
                result.add("not recorded")
                result.add("unknown")
                result.add("WGS84")
                result.add("NAD27")
                result.add("WGS 1972")
                result.add("North American 1983")
                result.add("North American 1928")
                result.add("North American 1929")
                result.add("Australian Geodetic 1966")
                result.add("Bogota Observatory")
                result.add("Corrego Alegre")
                result.add("Provisional South American 1956")
                result.add("PRP_M")
                result.add("POS")
                result.add("GRA")
                result.add("GDA94")
                result.add("Fundamental de Ocotepeque")
                result.add("AGD66")
                result.add("Clarke 1958")
                result.add("Japanese Geodetic Datum 2000")
                return result
            }
    }
}