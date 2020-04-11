package edu.harvard.mcz.imagecapture.entityimport

import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.Number
import org.apache.commons.logging.LogFactory
import java.io.Serializable
import java.util.*
import kotlin.collections.HashSet

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Specimen generated by hbm2java
 */
class Specimen : Serializable {
    var specimenId: Long? = null
    var barcode: String? = null
    private var drawerNumber: String? = null
    var typeStatus // Types only
            : String? = null
    var typeNumber // Types only, raw number for crosscheck
            : Long? = null
    var citedInPublication // (title) Types only
            : String? = null
    //private String citedInPublicationPage;  // Types only
//private String citedInPublicationYear;  // Types only
    var features: String? = null
    var family: String? = null
        set(family) {
            field = family
            if (this.family != null && this.family!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Family")) {
                field = this.family!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Family"))
            }
            if (this.family != null) {
                field = this.family!!.trim { it <= ' ' }
            }
        }
    private var subfamily: String? = null
    private var tribe: String? = null
    private var genus: String? = null
    private var specificEpithet: String? = null
    private var subspecificEpithet: String? = null
    private var infraspecificEpithet: String? = null
    private var infraspecificRank: String? = null
    private var authorship: String? = null
    private var unNamedForm: String? = null
    //private String identificationQualifier;
    var identifiedBy: String? = null
    /**
     * @return the natureOfId
     */
    /**
     * @param natureOfId the natureOfId to set
     */
    var natureOfId: String? = null
    /**
     * @return the dateIdentified
     */
    /**
     * @param dateIdentified the dateIdentified to set
     */
    var dateIdentified: String? = null
    /**
     * @return the identificationRemarks
     */
    /**
     * @param identificationRemarks the identificationRemarks to set
     */
    var identificationRemarks: String? = null
    /**
     * @return the higherGeography
     */
    /**
     * @param higherGeography the higherGeography to set
     */
    var higherGeography: String? = null
    var country: String? = null
    var primaryDivison: String? = null
    var specificLocality: String? = null
    var verbatimLocality: String? = null
    /**
     * @return the verbatimCollector
     */
    /**
     * @param verbatimCollector the verbatimCollector to set
     */
    var verbatimCollector: String? = null
    /**
     * @return the verbatimCollection
     */
    /**
     * @param verbatimCollection the verbatimCollection to set
     */
    var verbatimCollection: String? = null
    /**
     * @return the verbatimNumbers
     */
    /**
     * @param verbatimNumbers the verbatimNumbers to set
     */
    var verbatimNumbers: String? = null
    /**
     * @return the verbatimUnclassifiedText
     */
    /**
     * @param verbatimUnclassifiedText the verbatimUnclassifiedText to set
     */
    var verbatimUnclassifiedText: String? = null
    /**
     * @return the verbatimClusterIdentifier
     */
    /**
     * @param verbatimClusterIdentifier the verbatimClusterIdentifier to set
     */
    var verbatimClusterIdentifier: String? = null
    /**
     * @return the externalWorkflowProcess
     */
    /**
     * @param externalWorkflowProcess the externalWorkflowProcess to set
     */
    var externalWorkflowProcess: String? = null
    /**
     * @return the externalWorkflowDate
     */
    /**
     * @param externalWorkflowDate the externalWorkflowDate to set
     */
    var externalWorkflowDate: Date? = null
    /**
     * @return the minimum_elevation
     */
    /**
     * @param minimum_elevation the minimum_elevation to set
     */
    // private String verbatimElevation;   // removed
    var minimum_elevation: Long? = null
    /**
     * @return the maximum_elevation
     */
    /**
     * @param maximum_elevation the maximum_elevation to set
     */
    var maximum_elevation: Long? = null
    /**
     * @return the elev_units
     */
    /**
     * @param elev_units the elev_units to set
     */
    var elev_units: String? = null
    var collectingMethod: String? = null
    var isoDate: String? = null
    var dateNos: String? = null
    var dateEmerged: String? = null
    var dateEmergedIndicator: String? = null
    var dateCollected: String? = null
    var dateCollectedIndicator: String? = null
    var collection: String? = null
    var specimenNotes: String? = null
    var lifeStage: String? = null
    var sex: String? = null
    // private String preparationType;
    var habitat: String? = null
    /**
     * @return the microhabitat
     */
    /**
     * @param microhabitat the microhabitat to set
     */
    var microhabitat: String? = null
    var associatedTaxon: String? = null
    var questions: String? = null
    var inferences: String? = null
    var locationInCollection: String? = null
    var workFlowStatus: String? = null
    var createdBy: String? = null
    private var dateCreated: Date? = null
    private var dateLastUpdated: Date? = null
    var lastUpdatedBy: String? = null
    var validDistributionFlag: Boolean? = null
    /**
     * @return the flagInBulkloader
     */
    /**
     * @param flagInBulkloader the flagInBulkloader to set
     */
    var flagInBulkloader: Boolean? = null
    /**
     * @return the flagInMCZbase
     */
    /**
     * @param flagInMCZbase the flagInMCZbase to set
     */
    var flagInMCZbase: Boolean? = null
    /**
     * @return the flagAncilaryAlsoInMCZbase
     */
    /**
     * @param flagAncilaryAlsoInMCZbase the flagAncilaryAlsoInMCZbase to set
     */
    var flagAncilaryAlsoInMCZbase: Boolean? = null
    /**
     * @return the path
     */
    /**
     * @param path the path to set
     */
    var creatingPath // A path for image file, denormalized from Image.path for JPA query without join to Image.
            : String? = null
    /**
     * @return the creatingFilename
     */
    /**
     * @param creatingFilename the creatingFilename to set
     */
    var creatingFilename: String? = null
    var collectors: MutableSet<Collector?>? = HashSet(0)
    private var determinations: MutableSet<Determination?>? = HashSet<Determination?>(0)
    private var trackings: MutableSet<Tracking?>? = HashSet<Tracking?>(0)
    var numbers: MutableSet<Number?>? = HashSet(0)
    private var ICImages: MutableSet<ICImage?>? = HashSet<ICImage?>(0)
    private var specimenParts: MutableSet<SpecimenPart?>? = HashSet<SpecimenPart?>(0)
    private var georeferences: MutableSet<LatLong?>? = HashSet<LatLong?>(0)
    /**
     * @return the externalHistory
     */
    /**
     * @param externalHistory the externalHistory to set
     */
    var externalHistory: MutableSet<ExternalHistory?>? = HashSet<ExternalHistory?>(0)

    constructor() {
        setDefaults()
    }

    constructor(barcode: String?, typeStatus: String?, dateCreated: Date?) {
        validDistributionFlag = true
        this.barcode = barcode
        this.typeStatus = typeStatus
        if (dateCreated == null) {
            this.dateCreated = Date()
        } else {
            this.dateCreated = dateCreated.clone() as Date
        }
    }

    constructor(barcode: String?, drawerNumber: String?, typeStatus: String?,
                typeNumber: Long?, citedInPublication: String?, features: String?,
                family: String?, subfamily: String?, tribe: String?, genus: String?,
                specificEpithet: String?, subspecificEpithet: String?,
                infraspecificEpithet: String?, infraspecificRank: String?,
                authorship: String?, unNamedForm: String?,
                identificationQualifier: String?, identifiedBy: String?,
                country: String?, primaryDivison: String?, specificLocality: String?,
                verbatimLocality: String?,
                minimum_elevation: Long?, maximum_elevation: Long?, elev_units: String?,
                collectingMethod: String?, dateNos: String?, dateEmerged: String?,
                dateEmergedIndicator: String?, dateCollected: String?,
                dateCollectedIndicator: String?, collection: String?,
                specimenNotes: String?, lifeStage: String?, preparationType: String?,
                sex: String?, microhabitat: String?,
                habitat: String?, associatedTaxon: String?, questions: String?,
                inferences: String?, locationInCollection: String?,
                workFlowStatus: String?, createdBy: String?, dateCreated: Date?,
                dateLastUpdated: Date?, lastUpdatedBy: String?,
                validDistributionFlag: Boolean?, collectors: MutableSet<Collector?>?,
                determinations: MutableSet<Determination?>?, trackings: MutableSet<Tracking?>?,
                numbers: MutableSet<Number?>?, ICImages: MutableSet<ICImage?>?,
                specimenParts: MutableSet<SpecimenPart?>?) {
        this.barcode = barcode
        this.drawerNumber = drawerNumber
        this.typeStatus = typeStatus
        this.typeNumber = typeNumber
        this.citedInPublication = citedInPublication
        this.features = features
        family = family
        this.subfamily = subfamily
        this.tribe = tribe
        this.genus = genus
        this.specificEpithet = specificEpithet
        this.subspecificEpithet = subspecificEpithet
        this.infraspecificEpithet = infraspecificEpithet
        this.infraspecificRank = infraspecificRank
        this.authorship = authorship
        this.unNamedForm = unNamedForm
        this.identifiedBy = identifiedBy
        this.country = country
        this.primaryDivison = primaryDivison
        this.specificLocality = specificLocality
        this.verbatimLocality = verbatimLocality
        //this.verbatimElevation = verbatimElevation;
        this.minimum_elevation = minimum_elevation
        this.maximum_elevation = maximum_elevation
        this.elev_units = elev_units
        this.collectingMethod = collectingMethod
        this.dateNos = dateNos
        this.dateEmerged = dateEmerged
        this.dateEmergedIndicator = dateEmergedIndicator
        this.dateCollected = dateCollected
        this.dateCollectedIndicator = dateCollectedIndicator
        this.collection = collection
        this.specimenNotes = specimenNotes
        this.lifeStage = lifeStage
        this.sex = sex
        //this.preparationType = preparationType;
        this.habitat = habitat
        this.microhabitat = microhabitat
        this.associatedTaxon = associatedTaxon
        this.questions = questions
        this.inferences = inferences
        this.locationInCollection = locationInCollection
        this.workFlowStatus = workFlowStatus
        this.createdBy = createdBy
        if (dateCreated != null) {
            this.dateCreated = dateCreated.clone() as Date
        }
        if (dateLastUpdated != null) {
            this.dateLastUpdated = dateLastUpdated.clone() as Date
        }
        this.lastUpdatedBy = lastUpdatedBy
        this.validDistributionFlag = validDistributionFlag
        this.collectors = collectors
        //allie new
//collectors.add(new Collector(this, "the name"));
//
        this.determinations = determinations
        this.trackings = trackings
        this.numbers = numbers
        this.ICImages = ICImages
        this.specimenParts = specimenParts
    }

    /**
     * Set default values for a new specimen object with no other data.
     */
    fun setDefaults() { // NOTE: Any default set here should also be cleared in clearDefaults
// or searches by example may not return expected results.
        typeStatus = STATUS_NOT_A_TYPE
        validDistributionFlag = true
        flagInBulkloader = false
        flagInMCZbase = false
        flagAncilaryAlsoInMCZbase = false
        //this.preparationType = "Pinned";
//allie change
//this.natureOfId = NatureOfId.LEGACY;
        natureOfId = NatureOfId.EXPERT_ID
        //collectors.add(new Collector(this,"first collector"));
/*
		if (this.verbatimCollection==null) { this.verbatimCollection = ""; }
		if (this.verbatimCollector==null) { this.verbatimCollector = ""; }
		if (this.verbatimLocality==null) { this.verbatimLocality = ""; }
		if (this.verbatimNumbers==null) { this.verbatimNumbers = ""; }
		if (this.verbatimUnclassifiedText==null) { this.verbatimUnclassifiedText = ""; }
		*/
// this.dateCreated = new Date();
    }

    /**
     * Clear the default values for a new specimen object, as in one that
     * is to be used as the search criteria for a search by pattern.
     */
    fun clearDefaults() { // Note: these could also be ignored in SpecimenLifeCycle.findByExampleLike().
        typeStatus = null
        validDistributionFlag = null
        flagInBulkloader = null
        flagInMCZbase = null
        flagAncilaryAlsoInMCZbase = null
        //this.preparationType = null;
        natureOfId = null
    }

    /**
     * non-zero padded catalog number extracted from the barcode, equivalent to
     * arctos/MCZbase cat_num for lepidoptera.
     *
     * @return numeric part of barcode without zero padding
     */
    val catNum: String
        get() = Integer.toString(barcode!!.substring(8).toInt())

    fun getDrawerNumber(): String? {
        return drawerNumber
    }

    fun setDrawerNumber(drawerNumber: String?) {
        this.drawerNumber = drawerNumber
        if (this.drawerNumber != null && this.drawerNumber!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber")) {
            this.drawerNumber = this.drawerNumber!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "DrawerNumber"))
        }
    }

    fun getSubfamily(): String? {
        return subfamily
    }

    fun setSubfamily(subfamily: String?) {
        this.subfamily = subfamily
        if (this.subfamily != null) {
            this.subfamily = this.subfamily!!.trim { it <= ' ' }
        }
    }

    fun getTribe(): String? {
        return tribe
    }

    fun setTribe(tribe: String?) {
        this.tribe = tribe
        if (this.tribe != null) {
            this.tribe = this.tribe!!.trim { it <= ' ' }
        }
    }

    fun getGenus(): String? {
        return genus
    }

    fun setGenus(genus: String?) {
        this.genus = genus
        if (this.genus != null) {
            this.genus = this.genus!!.trim { it <= ' ' }
        }
    }

    fun getSpecificEpithet(): String? {
        return specificEpithet
    }

    fun setSpecificEpithet(specificEpithet: String?) {
        this.specificEpithet = specificEpithet
        if (this.specificEpithet != null) {
            this.specificEpithet = this.specificEpithet!!.trim { it <= ' ' }
        }
    }

    fun getSubspecificEpithet(): String? {
        return subspecificEpithet
    }

    fun setSubspecificEpithet(subspecificEpithet: String?) {
        this.subspecificEpithet = subspecificEpithet
        if (this.subspecificEpithet != null) {
            this.subspecificEpithet = this.subspecificEpithet!!.trim { it <= ' ' }
        }
    }

    fun getInfraspecificEpithet(): String? {
        return infraspecificEpithet
    }

    fun setInfraspecificEpithet(infraspecificEpithet: String?) {
        this.infraspecificEpithet = infraspecificEpithet
        if (this.infraspecificEpithet != null) {
            this.infraspecificEpithet = this.infraspecificEpithet!!.trim { it <= ' ' }
        }
    }

    fun getInfraspecificRank(): String? {
        return infraspecificRank
    }

    fun setInfraspecificRank(infraspecificRank: String?) {
        this.infraspecificRank = infraspecificRank
        if (this.infraspecificRank != null) {
            this.infraspecificRank = this.infraspecificRank!!.trim { it <= ' ' }
        }
    }

    fun getAuthorship(): String? {
        return authorship
    }

    fun setAuthorship(authorship: String?) {
        this.authorship = authorship
        if (this.authorship != null) {
            this.authorship = this.authorship!!.trim { it <= ' ' }
        }
    }

    fun getUnNamedForm(): String? {
        return unNamedForm
    }

    fun setUnNamedForm(unNamedForm: String?) {
        this.unNamedForm = unNamedForm
        if (this.unNamedForm != null) {
            this.unNamedForm = this.unNamedForm!!.trim { it <= ' ' }
        }
    }

    //	public String getVerbatimElevation() {
//		return this.verbatimElevation;
//	}
//
//	public void setVerbatimElevation(String verbatimElevation) {
//		this.verbatimElevation = verbatimElevation;
//	}

    /**
     * @return the minimum_elevation
     */
    /**
     * @param minimum_elevation the minimum_elevation to set
     */
    var minimumElevationSt: String?
        get() = if (minimum_elevation == null) {
            null
        } else minimum_elevation.toString()
        set(minimum_elevation) {
            if (minimum_elevation == null || minimum_elevation.trim { it <= ' ' }.length == 0) {
                this.minimum_elevation = null
            } else {
                this.minimum_elevation = minimum_elevation.toLong()
            }
        }

    /**
     * @return the maximum_elevation
     */
    /**
     * @param maximum_elevation the maximum_elevation to set
     */
    var maximumElevationSt: String?
        get() = if (maximum_elevation == null) {
            null
        } else maximum_elevation.toString()
        set(maximum_elevation) {
            if (maximum_elevation == null || maximum_elevation.trim { it <= ' ' }.length == 0) {
                this.maximum_elevation = null
            } else {
                this.maximum_elevation = maximum_elevation.toLong()
            }
        }

    fun getDateCreated(): Date? {
        return dateCreated
    }

    fun setDateCreated(dateCreated: Date?) {
        if (dateCreated == null) {
            this.dateCreated = null
        } else {
            this.dateCreated = dateCreated.clone() as Date
            if (dateLastUpdated == null) {
                dateLastUpdated = this.dateCreated!!.clone() as Date
            }
        }
    }

    fun getDateLastUpdated(): Date? {
        return dateLastUpdated
    }

    fun setDateLastUpdated(dateLastUpdated: Date?) {
        if (dateLastUpdated == null) {
            this.dateLastUpdated = null
        } else {
            this.dateLastUpdated = dateLastUpdated.clone() as Date
        }
    }

    fun getDeterminations(): MutableSet<Determination?>? {
        return determinations
    }

    fun setDeterminations(determinations: MutableSet<Determination?>?) {
        this.determinations = determinations
    }

    fun getTrackings(): MutableSet<Tracking?>? {
        return trackings
    }

    fun setTrackings(trackings: MutableSet<Tracking?>?) {
        this.trackings = trackings
    }

    fun getICImages(): MutableSet<ICImage?>? {
        return ICImages
    }

    fun setICImages(ICImages: MutableSet<ICImage?>?) {
        this.ICImages = ICImages
    }

    /**
     * @return the specimenParts
     */
    fun getSpecimenParts(): MutableSet<SpecimenPart?>? {
        return specimenParts
    }

    /**
     * @param specimenParts the specimenParts to set
     */
    fun setSpecimenParts(specimenParts: MutableSet<SpecimenPart?>?) {
        this.specimenParts = specimenParts
    }

    // TODO: remove the following code if possible
    var latLong: MutableSet<edu.harvard.mcz.imagecapture.entityimport.LatLong?>?
        get() {
            if (georeferences == null) {
                georeferences = HashSet<LatLong?>()
            }
            // TODO: remove the following code if possible
            if (georeferences!!.isEmpty()) {
                val georef = LatLong()
                georef.setSpecimen(this)
                georeferences!!.add(georef)
            }
            return georeferences
        }
        set(latlongs) {
            georeferences = latlongs
        }

    val isStateDone: Boolean
        get() {
            var result = false
            if (workFlowStatus == WorkFlowStatus.STAGE_DONE) {
                result = true
            }
            return result
        }

    val loadFlags: String
        get() {
            var result = "Unexpected State"
            if (!flagInBulkloader!! && !flagInMCZbase!! && !flagAncilaryAlsoInMCZbase!!) {
                result = "In DataShot"
            }
            if (flagInBulkloader!! && !flagInMCZbase!! && !flagAncilaryAlsoInMCZbase!!) {
                result = "In Bulkloader"
            }
            if (flagInBulkloader!! && flagInMCZbase!! && !flagAncilaryAlsoInMCZbase!!) {
                result = "Adding Image and Ids."
            }
            if (flagInBulkloader!! && flagInMCZbase!! && flagAncilaryAlsoInMCZbase!!) {
                result = WorkFlowStatus.STAGE_DONE
            }
            return result
        }

    fun attachNewPart() {
        val newPart = SpecimenPart()
        newPart.setPreserveMethod(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_DEFAULT_PREPARATION))
        newPart.setSpecimen(this)
        val spls = SpecimenPartLifeCycle()
        try {
            spls.persist(newPart)
            getSpecimenParts()!!.add(newPart)
        } catch (e1: SaveFailedException) {
            log!!.error(e1.message, e1)
        }
    }

    /**
     * Set the value of specificLocality to a string that has a meaning
     * of there being no specific locality data.  MCZbase requires a
     * non-null value for specific locality on bulkloader ingest.
     */
    fun noSpecificLocalityData() {
        specificLocality = "[no specific locality data]"
    }

    /**
     * Assemble relevant fields of the current identification into a scientific name.
     *
     * @return
     */
    fun assembleScientificName(): String {
        val result = StringBuffer()
        result.append(genus).append(" ").append(specificEpithet).append(" ").append(infraspecificRank).append(" ").append(infraspecificEpithet)
        return result.toString().trim { it <= ' ' }
    }

    companion object {
        val STATUS_NOT_A_TYPE: String? = "Not a Type"
        private val log = LogFactory.getLog(Specimen::class.java)
        private const val serialVersionUID = -1321141594439433313L
    }
}