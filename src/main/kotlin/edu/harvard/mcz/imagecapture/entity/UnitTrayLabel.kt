/**
 * UnitTrayLabel.java
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
package edu.harvard.mcz.imagecapture.entityimport

import org.apache.commons.logging.LogFactory
import java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * UnitTrayLabel
 *
 *
 * Includes factory method for decoding a set of key-value pairs in JSON, and a
 * method for constructing such a string with a JSON encoding.
 */
class UnitTrayLabel : TaxonNameReturner, DrawerNameReturner, CollectionReturner {
    /**
     * @return the id
     */
    /**
     * @param id the id to set
     */
    var id: Long? = null
    private var drawerNumber: String?
    private var family: String?
    private var subfamily: String?
    private var tribe: String?
    private var genus: String?
    private var specificEpithet: String?
    private var subspecificEpithet: String?
    private var infraspecificEpithet: String?
    private var infraspecificRank: String?
    private var authorship: String?
    private var unNamedForm: String? = null
    var printed = 0
    var numberToPrint = 0
    var dateCreated: Date?
    var dateLastUpdated: Date?
    /**
     * @param collection the collection to set
     */
    var collection // collection from which the material came
            : String?
    private var ordinal // order in which to print
            : Int? = null
    /**
     * @return the identifiedBy
     */
    /**
     * @param identifiedBy the identifiedBy to set
     */
    var identifiedBy: String? = null public get() {
        return field
    }

    constructor() {
        printed = 0
        numberToPrint = 1
        drawerNumber = ""
        family = ""
        subfamily = ""
        tribe = ""
        genus = ""
        specificEpithet = ""
        subspecificEpithet = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        authorship = ""
        unNamedForm = ""
        collection = ""
        ordinal = 1
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor with all fields
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param unnamedForm
     * @param printed
     * @param collection
     */
    constructor(id: Long?, drawerNumber: String?, family: String?,
                subfamily: String?, tribe: String?, genus: String?,
                specificEpithet: String?, subspecificEpithet: String?,
                infraspecificEpithet: String?, infraspecificRank: String?,
                authorship: String?, unnamedForm: String?, printed: Int,
                dateCreated: Date?, dateLastUpdated: Date?,
                collection: String?, ordinal: Int?) : super() {
        this.id = id
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        this.subspecificEpithet = subspecificEpithet
        this.infraspecificEpithet = infraspecificEpithet
        this.infraspecificRank = infraspecificRank
        this.authorship = authorship
        unNamedForm = unnamedForm
        this.printed = printed
        this.dateCreated = dateCreated
        this.dateLastUpdated = dateLastUpdated
        this.collection = collection
        this.ordinal = ordinal
        this.dateCreated = Date()
        this.dateLastUpdated = Date()
    }

    /**
     * Constructor for infraspcific trinomial with explicit rank.
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param infraspecificEpithet
     * @param infraspecificRank
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                infraspecificEpithet: String?, infraspecificRank: String?,
                authorship: String?, collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        subspecificEpithet = ""
        this.infraspecificEpithet = infraspecificEpithet
        this.infraspecificRank = infraspecificRank
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor for species
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                authorship: String?, collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        subspecificEpithet = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Constructor for subspecies
     *
     * @param drawerNumber
     * @param family
     * @param subfamily
     * @param tribe
     * @param genus
     * @param specificEpithet
     * @param subspecificEpithet
     * @param authorship
     * @param collection
     */
    constructor(drawerNumber: String?, family: String?, subfamily: String?,
                tribe: String?, genus: String?, specificEpithet: String?,
                subspecificEpithet: String?, authorship: String?,
                collection: String?) : super() {
        this.drawerNumber = drawerNumber
        this.family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        this.subspecificEpithet = subspecificEpithet
        infraspecificEpithet = ""
        infraspecificRank = ""
        this.authorship = authorship
        this.collection = collection
        dateCreated = Date()
        dateLastUpdated = Date()
    }

    /**
     * Retuns a JSON encoding of the list of fields that can appear on a unit tray
     * label using key-value pairs where the keys are f,b,t,g,s,u,i,r,a,d, and
     * optionally c, and the values are respectively for the family,
     * subfamily,tribe, genus, specificepithet, subspecificepithet,
     * infraspecificepithet, infraspecificrank, authorship, drawernumber and
     * optionally collection.
     *
     * @return String containing JSON in the form { "f":"familyname", .... }
     * @see createFromJSONString
     */
    fun toJSONString(): String {
        val result = StringBuffer()
        result.append("{")
        result.append(" \"f\":\"").append(family).append("\"")
        result.append(", \"b\":\"").append(subfamily).append("\"")
        result.append(", \"t\":\"").append(tribe).append("\"")
        result.append(", \"g\":\"").append(genus).append("\"")
        result.append(", \"s\":\"").append(specificEpithet).append("\"")
        result.append(", \"u\":\"").append(subspecificEpithet).append("\"")
        result.append(", \"i\":\"").append(infraspecificEpithet).append("\"")
        result.append(", \"r\":\"").append(infraspecificRank).append("\"")
        result.append(", \"a\":\"").append(authorship).append("\"")
        result.append(", \"d\":\"").append(drawerNumber).append("\"")
        if (collection != null) {
            if (!collection!!.isEmpty()) {
                result.append(", \"c\":\"").append(collection).append("\"")
            }
        }
        if (identifiedBy != null && identifiedBy!!.trim { it <= ' ' }.length > 0) {
            result.append(", \"id\":\"").append(identifiedBy).append("\"")
        }
        result.append(" }")
        return result.toString()
    }

    /**
     * @return the drawerNumber
     */
    override fun getDrawerNumber(): String? {
        return drawerNumber
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    fun setDrawerNumber(drawerNumber: String?) {
        this.drawerNumber = drawerNumber
        if (this.drawerNumber != null) {
            this.drawerNumber = this.drawerNumber!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the family
     */
    override fun getFamily(): String? {
        return family
    }

    /**
     * @param family the family to set
     */
    fun setFamily(family: String?) {
        this.family = family
        if (this.family != null) {
            this.family = this.family!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the subfamily
     */
    override fun getSubfamily(): String? {
        return subfamily
    }

    /**
     * @param subfamily the subfamily to set
     */
    fun setSubfamily(subfamily: String?) {
        this.subfamily = subfamily
        if (this.subfamily != null) {
            this.subfamily = this.subfamily!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the tribe
     */
    override fun getTribe(): String? {
        if (tribe == null) {
            tribe = ""
        }
        return tribe
    }

    /**
     * @param tribe the tribe to set
     */
    fun setTribe(tribe: String?) {
        this.tribe = tribe
        if (this.tribe != null) {
            this.tribe = this.tribe!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the genus
     */
    override fun getGenus(): String? {
        if (genus == null) {
            genus = ""
        }
        return genus
    }

    /**
     * @param genus the genus to set
     */
    fun setGenus(genus: String?) {
        this.genus = genus
        if (this.genus != null) {
            this.genus = this.genus!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the specificEpithet
     */
    override fun getSpecificEpithet(): String? {
        if (specificEpithet == null) {
            specificEpithet = ""
        }
        return specificEpithet
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    fun setSpecificEpithet(specificEpithet: String?) {
        this.specificEpithet = specificEpithet
        if (this.specificEpithet != null) {
            this.specificEpithet = this.specificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the subspecificEpithet
     */
    override fun getSubspecificEpithet(): String? {
        if (subspecificEpithet == null) {
            subspecificEpithet = ""
        }
        return subspecificEpithet
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    fun setSubspecificEpithet(subspecificEpithet: String?) {
        this.subspecificEpithet = subspecificEpithet
        if (this.subspecificEpithet != null) {
            this.subspecificEpithet = this.subspecificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the infraspecificEpithet
     */
    override fun getInfraspecificEpithet(): String? {
        if (infraspecificEpithet == null) {
            infraspecificEpithet = ""
        }
        return infraspecificEpithet
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    fun setInfraspecificEpithet(infraspecificEpithet: String?) {
        this.infraspecificEpithet = infraspecificEpithet
        if (this.infraspecificEpithet != null) {
            this.infraspecificEpithet = this.infraspecificEpithet!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the infraspecifcRank
     */
    override fun getInfraspecificRank(): String? {
        if (infraspecificRank == null) {
            infraspecificRank = ""
        }
        return infraspecificRank
    }

    /**
     * @param infraspecifcRank the infraspecifcRank to set
     */
    fun setInfraspecificRank(infraspecifcRank: String?) {
        infraspecificRank = infraspecifcRank
        if (infraspecificRank != null) {
            infraspecificRank = infraspecificRank!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the authorship
     */
    override fun getAuthorship(): String? {
        if (authorship == null) {
            authorship = ""
        }
        return authorship
    }

    /**
     * @param authorship the authorship to set
     */
    fun setAuthorship(authorship: String?) {
        this.authorship = authorship
        if (this.authorship != null) {
            this.authorship = this.authorship!!.trim { it <= ' ' }
        }
    }

    /**
     * @return the unnamedForm
     */
    fun getUnNamedForm(): String? {
        if (unNamedForm == null) {
            unNamedForm = ""
        }
        return unNamedForm
    }

    /**
     * @param unnamedForm the unnamedForm to set
     */
    fun setUnNamedForm(unNamedForm: String?) {
        this.unNamedForm = unNamedForm
    }

    /**
     * @return the ordinal
     */
    fun getOrdinal(): Int? {
        if (ordinal == null) {
            ordinal = 0
        }
        return ordinal
    }

    /**
     * @param ordinal the ordinal to set
     */
    fun setOrdinal(ordinal: Int?) {
        if (ordinal == null) {
            this.ordinal = 0
        } else {
            this.ordinal = ordinal
        }
    }

    companion object {
        private val log = LogFactory.getLog(UnitTrayLabel::class.java)
        /**
         * Factory method, given a JSON encoded string, as encoded with
         * toJSONString(), extract the values from that string into a new instance of
         * UnitTrayLabel so that they can be obtained by the appropriate returner
         * interface (taxonnameReturner, drawernumberReturner, collectionReturner).
         *
         * @param jsonEncodedLabel the JSON to decode.
         * @return a new UnitTrayLabel populated with the values found in the supplied
         * jsonEncodedLabel text or null on a failure.
         * @see toJSONString
         */
        fun createFromJSONString(jsonEncodedLabel: String): UnitTrayLabel? {
            var jsonEncodedLabel = jsonEncodedLabel
            var result: UnitTrayLabel? = null
            if (jsonEncodedLabel.matches("\\{.*\\}")) {
                val originalJsonEncodedLabel = jsonEncodedLabel
                jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^\\{".toRegex(), "") // Strip off leading  {
                jsonEncodedLabel = jsonEncodedLabel.replaceFirst("\\}$".toRegex(), "") // Strip off trailing }
                if (jsonEncodedLabel.contains("}")) { // nested json, not expected.
                    log!!.error(
                            "JSON for UnitTrayLabel contains unexpected nesting { { } }.  JSON is: " +
                                    originalJsonEncodedLabel)
                } else {
                    log!!.debug(jsonEncodedLabel)
                    result = UnitTrayLabel()
                    // Beginning and end are special case for split on '", "'
// remove leading quotes and spaces (e.g. either trailing '" ' or
// trailing '"').
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^ ".toRegex(), "") // Strip off leading space
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst(" $".toRegex(), "") // Strip off trailing space
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst("^\"".toRegex(), "") // Strip off leading quote
                    jsonEncodedLabel = jsonEncodedLabel.replaceFirst(
                            "\"$".toRegex(), "") // Strip off trailing quote
                    // Convert any ", " into "," then split on ","
                    jsonEncodedLabel = jsonEncodedLabel.replace("\",\\s\"".toRegex(), "\",\"")
                    log.debug(jsonEncodedLabel)
                    // split into key value parts by '","'
                    val pairs: Array<String?> = jsonEncodedLabel.split("\",\"".toRegex()).toTypedArray()
                    for (x in pairs.indices) { // split each key value pair
                        val keyValuePair: Array<String?> = pairs[x]!!.split("\":\"".toRegex()).toTypedArray()
                        if (keyValuePair.size == 2) {
                            val key = keyValuePair[0]
                            val value = keyValuePair[1]
                            log.debug("key=[$key], value=[$value]")
                            if (key == "m1p") {
                                log.debug("Configured for Project: $value")
                                if (value != "MCZ Lepidoptera" &&
                                        value != "ETHZ Entomology" &&
                                        value != "[ETHZ Entomology]") {
                                    log.error("Project specified in JSON is not recognized: " +
                                            value)
                                    log.error(
                                            "Warning: Keys in JSON may not be correctly interpreted.")
                                }
                            }
                            // Note: Adding values here isn't sufficient to populate specimen
// records, you still need to invoke the relevant returner interface
// on the parser.
                            if (key == "f") {
                                result.setFamily(value)
                            }
                            if (key == "b") {
                                result.setSubfamily(value)
                            }
                            if (key == "t") {
                                result.setTribe(value)
                            }
                            if (key == "g") {
                                result.setGenus(value)
                            }
                            if (key == "s") {
                                result.setSpecificEpithet(value)
                            }
                            if (key == "u") {
                                result.setSubspecificEpithet(value)
                            }
                            if (key == "i") {
                                result.setInfraspecificEpithet(value)
                            }
                            if (key == "r") {
                                result.setInfraspecificRank(value)
                            }
                            if (key == "a") {
                                result.setAuthorship(value)
                            }
                            if (key == "d") {
                                result.setDrawerNumber(value)
                            }
                            if (key == "c") {
                                result.collection = value
                                log.debug(result.collection)
                            }
                            if (key == "id") {
                                result.identifiedBy = value
                            }
                        }
                    }
                    log.debug(result.toJSONString())
                }
            } else {
                log!!.debug("JSON not matched to { }")
            }
            return result
        }
    }
}