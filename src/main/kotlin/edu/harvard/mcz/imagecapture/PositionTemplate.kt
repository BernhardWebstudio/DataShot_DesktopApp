/**
 * PositionTemplate.java
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

import edu.harvard.mcz.imagecapture.entity.Template
import org.apache.commons.logging.LogFactory
import java.awt.Dimension
import java.io.File
import java.util.ArrayList

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * Description of the coordinates within an image where parts of the image to pass to
 * barcode reading software, OCR software, and which parts are to be displayed as containing
 * a specimen image or specimen labels.   Wrapper for persistent Template class.  Provides special
 * case template TEMPLATE_NO_COMPONENT_PARTS for handling any size image that isn't templated.
 * Provides hard coded default template.  Provides methods to get/set coordinates as Dimension
 * objects as a convenience over Template's get/set of individual heights and widths as integers.
 *
 *
 * DefaultPositionTemplateDetector makes the assumption that a template can be uniquely identified
 * by the location of the barcode in the image.
 *
 *
 * Each template must have the barcode in a uniquely different place.
 */
class PositionTemplate {
    /**
     * @param imageSize the imageSize to set
     */
    var imageSize // Pixel dimensions of the image
            : Dimension? = null
    /**
     * @return the barcodePosition
     */
    /**
     * @param barcodePosition the barcodePosition to set
     *///TODO: add define/save templates
//TODO: add retrieval of template from file.
    var barcodeULPosition // Location of upper left corner of area to scan for barcode in image.
            : Dimension? = null
        set
        get() = field
    /**
     * @param barcodeSize the barcodeSize to set
     */
    var barcodeSize // Size of area in image to scan for barcode.
            : Dimension? = null
    /**
     * @return the specimenPosition
     */
    /**
     * @param specimenPosition the specimenPosition to set
     */
    var specimenPosition // Location of upper left corner of part of image containing specimen.
            : Dimension? = null
    /**
     * @return the specimenSize
     */
    /**
     * @param specimenSize the specimenSize to set
     */
    var specimenSize: Dimension? = null
    /**
     * @return the textPosition
     */
    /**
     * @param textPosition the textPosition to set
     */
    var textPosition // Unit tray label with current determination
            : Dimension? = null
    /**
     * @return the textSize
     */
    /**
     * @param textSize the textSize to set
     */
    var textSize: Dimension? = null
    /**
     * @return the labelSize
     */
    /**
     * @return the labelSize
     */
    /**
     * @param utLabelSize the utLabelSize to set
     */
    /**
     * @param labelSize the labelSize to set
     */
    var uTLabelsSize // Specimen labels from pin.
            : Dimension? = null
        get() = utLabelSize
        set(utLabelSize) {
            this.utLabelSize = utLabelSize
        }
    /**
     * @return the labelPosition
     */
    /**
     * @return the labelPosition
     */
    /**
     * @param utLabelPosition the utLabelPosition to set
     */
    /**
     * @param labelPosition the labelPosition to set
     */
    var uTLabelsPosition: Dimension? = null
        get() = utLabelPosition
        set(utLabelPosition) {
            this.utLabelPosition = utLabelPosition
        }
    private var utLabelSize // Specimen labels from unit tray.
            : Dimension? = null
    private var utLabelPosition: Dimension? = null
    var utBarcodeSize // Barcode on UnitTrayLabel
            : Dimension? = null
    var utBarcodePosition: Dimension? = null
    private var referenceImage // Filename of reference image for this template.
            : ICImage? = null
    /**
     * @return the templateName
     */
    /**
     * @param templateName the templateName to set
     */
    /**
     * Get the free text descriptive name of the position template for potential display to a person.
     * Use getTemplateIdentifier() to identify templates in code.
     *
     * @return the descriptive name of the template.
     */
    var name // free text description of the template
            : String? = null
        get() = field
        set
    /**
     * Get the identifying name of this position template.  This name is fixed for an instance
     * during its construction.   This name corresponds to one of the strings returned by
     * PositionTemplate.getTemplates();  Redundant with getTemplateIdentifier().
     *
     * @return the identifier of the template in use in this instance of PositionTemplate.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     */
    /**
     * @param templateId the templateId to set
     */
    var templateId // the identifying string for this template = TEMPLATE_ constants
            : String? = null
    /**
     * @return true if this PositionTemplate can be edited by the user.
     */
    var isEditable // false for TEMPLATE_s, true for DB records. = false
        private set

    /**
     * Use a (currently hardcoded) default template for which pixel coordinates on the
     * image contain which information.
     */
    constructor() {
        useDefaultValues()
    }

    /**
     * Create a new (probably editable) template based on the default template for which pixel coordinates on the
     * image contain which information.
     *
     * @param editable true for an editable template.
     */
    constructor(editable: Boolean) {
        useDefaultValues()
        isEditable = editable
    }

    /**
     * Use a template defined by a PositionTemplate.TEMPLATE_* constant or potentially from
     * another valid source.  A template defines which pixel coordinates on the image contain
     * which information.   The template cannot be changed for an instance of PositionTemplate once
     * it has been instantiated.  This constructor is the only means of setting the template to use.
     * Create a new instance of PositionTemplate if you wish to use a different template.
     *
     *
     * The list of available templateIds can be retrieved with PositionTemplate.getTemplates().
     *
     * @param templateToUse the templateID of the template to use in this instance of PositionTemplate.
     * @throws NoSuchTemplateException when templateToUse doesn't exist.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     * @see edu.harvard.mcz.imagecapture.exceptions.NoComponentPartsTemplateException
     */
    constructor(templateToUse: String) {
        val found = loadTemplateValues(templateToUse)
        if (!found) {
            throw NoSuchTemplateException("No such template as $templateToUse")
        }
    }

    /**
     * Construct a PositionTemplate from a Template.
     *
     * @param templateInstance
     */
    constructor(templateInstance: Template) {
        name = templateInstance.name
        templateId = templateInstance.templateId
        imageSize = Dimension(templateInstance.imageSizeX, templateInstance.imageSizeY)
        barcodeULPosition = Dimension(templateInstance.barcodePositionX, templateInstance.barcodePositionY)
        barcodeSize = Dimension(templateInstance.barcodeSizeX, templateInstance.barcodeSizeY)
        specimenPosition = Dimension(templateInstance.specimenPositionX, templateInstance.specimenPositionY)
        specimenSize = Dimension(templateInstance.specimenSizeX, templateInstance.specimenSizeY)
        uTLabelsPosition = Dimension(templateInstance.labelPositionX, templateInstance.labelPositionY)
        uTLabelsSize = Dimension(templateInstance.labelSizeX, templateInstance.labelSizeY)
        utLabelPosition = Dimension(templateInstance.utLabelPositionX, templateInstance.utLabelPositionY)
        utLabelSize = Dimension(templateInstance.utLabelSizeX, templateInstance.utLabelSizey)
        utBarcodePosition = Dimension(templateInstance.utBarcodePositionX, templateInstance.utBarcodePositionY)
        utBarcodeSize = Dimension(templateInstance.utBarcodeSizeX, templateInstance.utBarcodeSizeY)
        textPosition = Dimension(templateInstance.textPositionX, templateInstance.textPositionY)
        textSize = Dimension(templateInstance.textSizeX, templateInstance.textSizeY)
    }

    /**
     * Hardcoded default template values are defined here.
     */
    private fun useDefaultValues() {
        name = "Default template for initial test image."
        templateId = TEMPLATE_DEFAULT
        // Set default values
        imageSize = Dimension(2848, 4272)
        // Approximately 300 x 300 pixel area in upper right corner of 12 mega-pixel image.
        barcodeULPosition = Dimension(2490, 90)
        barcodeSize = Dimension(300, 300)
        // lower half of image
        specimenPosition = Dimension(0, 2200)
        specimenSize = Dimension(2848, 1900)
        // text to ocr at top left
        textPosition = Dimension(110, 105)
        textSize = Dimension(1720, 700)
        // pin labels on right above half
        uTLabelsPosition = Dimension(1300, 700)
        uTLabelsSize = Dimension(1500, 1300)
        // labels on left above half
        utLabelPosition = Dimension(0, 850) // not defined as different from labels in test image
        utLabelSize = Dimension(1500, 1161)
        // QRCode barcode on unit tray label encoding unit tray label fields.
        utBarcodePosition = Dimension(1200, 105)
        utBarcodeSize = Dimension(950, 800)
        referenceImage = null
        isEditable = false
    }

    /**
     * Get the identifying name of this position template.  This name is fixed for an instance
     * during its construction.   This name corresponds to one of the strings returned by
     * PositionTemplate.getTemplates();   Redundant with getTemplateId()
     *
     * @return the identifier of the template in use in this instance of PositionTemplate.
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateIds
     * @see edu.harvard.mcz.imagecapture.PositionTemplate.getTemplateId
     */
    @get:Deprecated("")
    val templateIdentifier: String?
        get() = templateId

    //	/**
//	 * @return the utLabelSize
//	 */
//	public Dimension getUtLabelSize() {
//		return utLabelSize;
//	}
//
    //	/**
//	 * @return the utLabelPosition
//	 */
//	public Dimension getUtLabelPosition() {
//		return utLabelPosition;
//	}

    /**
     * private method, to use a different template, instantiate a new PositionTemplate.
     * Contains hardcoded templates other than the default
     *
     *
     * TODO: Extend to external persistent templates that aren't hard coded.
     *
     * @param templateToUse
     * @return true if the template was found.
     */
    private fun loadTemplateValues(templateToUse: String): Boolean {
        var found = false
        if (templateToUse == TEMPLATE_NO_COMPONENT_PARTS) {
            name = "Whole image only."
            templateId = templateToUse
            // Set all values to null
            imageSize = null
            barcodeULPosition = null
            barcodeSize = null
            specimenPosition = null
            specimenSize = null
            textPosition = null
            textSize = null
            uTLabelsPosition = null
            uTLabelsSize = null
            utLabelPosition = null
            utLabelSize = null
            utBarcodePosition = null
            utBarcodeSize = null
            isEditable = false
            referenceImage = null
            found = true
        }
        if (templateToUse == TEMPLATE_TEST_1) {
            name = "Cannon DigitalRebel L with small test carrier."
            templateId = templateToUse
            // Set default values
            imageSize = Dimension(2848, 4272)
            // Approximately 300 x 300 pixel area in upper right corner of 12 mega-pixel image.
            barcodeULPosition = Dimension(2280, 0)
            barcodeSize = Dimension(550, 310)
            // lower half of image
            specimenPosition = Dimension(0, 2140)
            specimenSize = Dimension(2847, 2130)
            // text to ocr at top left of image
            textPosition = Dimension(110, 105)
            textSize = Dimension(1720, 700)
            // pin labels on right side of upper half
            uTLabelsPosition = Dimension(1500, 780)
            uTLabelsSize = Dimension(1348, 1360)
            // unit tray labels on left side of upper half
            utLabelPosition = Dimension(0, 780)
            utLabelSize = Dimension(1560, 1360)
            // QRCode barcode on unit tray label encoding unit tray label fields.
            utBarcodePosition = Dimension(1200, 105)
            utBarcodeSize = Dimension(950, 800)
            isEditable = false
            referenceImage = null
            found = true
        }
        if (templateToUse == TEMPLATE_DEFAULT) {
            useDefaultValues()
            isEditable = false
            found = true
        }
        if (!found) {
            found = loadByTemplateId(templateToUse)
        }
        return found
    }

    fun loadByTemplateId(aTemplateId: String?): Boolean {
        var result = false
        var t_result: Template? = null
        val tls = TemplateLifeCycle()
        t_result = tls.findById(aTemplateId)
        if (t_result != null) { // We know this is a valid template, so set the id
            templateId = aTemplateId
            // retrieve the rest of the values
            name = t_result.name
            imageSize = Dimension(t_result.imageSizeX, t_result.imageSizeY)
            barcodeULPosition = Dimension(t_result.barcodePositionX, t_result.barcodePositionY)
            barcodeSize = Dimension(t_result.barcodeSizeX, t_result.barcodeSizeY)
            specimenPosition = Dimension(t_result.specimenPositionX, t_result.specimenPositionY)
            specimenSize = Dimension(t_result.specimenSizeX, t_result.specimenSizeY)
            textPosition = Dimension(t_result.textPositionX, t_result.textPositionY)
            textSize = Dimension(t_result.textSizeX, t_result.textSizeY)
            uTLabelsPosition = Dimension(t_result.labelPositionX, t_result.labelPositionY)
            uTLabelsSize = Dimension(t_result.labelSizeX, t_result.labelSizeY)
            utLabelPosition = Dimension(t_result.utLabelPositionX, t_result.utLabelPositionY)
            utLabelSize = Dimension(t_result.utLabelSizeX, t_result.utLabelSizey)
            utBarcodePosition = Dimension(t_result.utBarcodePositionX, t_result.utBarcodePositionY)
            utBarcodeSize = Dimension(t_result.utBarcodeSizeX, t_result.utBarcodeSizeY)
            isEditable = true
            referenceImage = t_result.referenceImage
            result = true
        }
        return result
    }

    @Throws(BadTemplateException::class)
    fun populateTemplateFromPositionTemplate(templateInstance: Template?) {
        if (templateId == null || templateId!!.trim { it <= ' ' } == "") {
            throw BadTemplateException("Can't save a template with a blank templateID")
        }
        if (imageSize == null) { // Note: if persistence of TEMPLATE_NO_COMPONENT_PARTS is desired, this
// needs to be changed to test for that template.
            throw BadTemplateException("Can't save a template with no image size.")
        }
        if (templateInstance == null) {
            throw BadTemplateException("Can't save a null template.")
        }
        templateInstance.templateId = templateId
        templateInstance.name = name
        templateInstance.editable = isEditable
        templateInstance.referenceImage = referenceImage
        templateInstance.imageSizeX = imageSize!!.width
        templateInstance.imageSizeY = imageSize!!.height
        templateInstance.barcodePositionX = barcodeULPosition!!.width
        templateInstance.barcodePositionY = barcodeULPosition!!.height
        templateInstance.barcodeSizeX = barcodeSize!!.width
        templateInstance.barcodeSizeY = barcodeSize!!.height
        templateInstance.specimenPositionX = specimenPosition!!.width
        templateInstance.specimenPositionY = specimenPosition!!.height
        templateInstance.specimenSizeX = specimenSize!!.width
        templateInstance.specimenSizeY = specimenSize!!.height
        templateInstance.textPositionX = textPosition!!.width
        templateInstance.textPositionY = textPosition!!.height
        templateInstance.textSizeX = textSize!!.width
        templateInstance.textSizeY = textSize!!.height
        templateInstance.labelPositionX = uTLabelsPosition!!.width
        templateInstance.labelPositionY = uTLabelsPosition!!.height
        templateInstance.labelSizeX = uTLabelsSize!!.width
        templateInstance.labelSizeY = uTLabelsSize!!.height
        templateInstance.utLabelPositionX = utLabelPosition!!.width
        templateInstance.utLabelPositionY = utLabelPosition!!.height
        templateInstance.utLabelSizeX = utLabelSize!!.width
        templateInstance.utLabelSizey = utLabelSize!!.height
        templateInstance.utBarcodePositionX = utBarcodePosition!!.width
        templateInstance.utBarcodePositionY = utBarcodePosition!!.height
        templateInstance.utBarcodeSizeX = utBarcodeSize!!.width
        templateInstance.utBarcodeSizeY = utBarcodeSize!!.height
    }

    @Throws(BadTemplateException::class, SaveFailedException::class)
    fun persist() {
        if (templateId == null || templateId!!.trim { it <= ' ' } == "") {
            throw BadTemplateException("Can't save a template with a blank templateID")
        }
        if (imageSize == null) { // Note: if persistence of TEMPLATE_NO_COMPONENT_PARTS is desired, this
// needs to be changed to test for that template.
            throw BadTemplateException("Can't save a template with no image size.")
        }
        val tls = TemplateLifeCycle()
        var templateInstance: Template = tls.findById(templateId)
        if (templateInstance == null) {
            templateInstance = Template()
        }
        populateTemplateFromPositionTemplate(templateInstance)
        // save/update
        tls.attachDirty(templateInstance)
    }

    /**
     * @return a String representing the filename of the referenceImage for this
     * PoistionTemplate.
     */
    fun getReferenceImage(): ICImage? {
        return referenceImage
    }

    fun setReferenceImage(anImage: ICImage?) {
        referenceImage = anImage
    }

    val referenceImageFilePath: String
        get() {
            var result = ""
            val base: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
            if (referenceImage != null && referenceImage.getPath() != null && referenceImage.getFilename() != null) {
                result = base + referenceImage.getPath() + referenceImage.getFilename()
            }
            return result
        }

    companion object {
        /**
         * Special case template for images that aren't split into component parts
         * with a template.
         *
         * @see edu.harvard.mcz.imagecapture.exceptions.NoComponentPartsTemplateException
         */
        val TEMPLATE_NO_COMPONENT_PARTS: String? = "Whole Image Only"
        /**
         * The hardcoded default template.
         */
        val TEMPLATE_DEFAULT: String? = "Default template"
        val TEMPLATE_TEST_1: String? = "Small Template 1"
        private val log = LogFactory.getLog(PositionTemplate::class.java)//here it cycles through all the templates (if you leave template.default=Default template and template.default2=)//System.out.println(st2.nextElement());
        //templateIdList.add(defaultTemplatesPropertiesVal); //todo string tokenizer
//allie: here load the props file: leave the default and config the other var.
//template.default=Default template
//template.default2=ETHZ_template1
//template.default2=ETHZ_template1,ETHZ_template2
        /**
         * Fetch the list of valid template names (including the no component parts template).
         * Use these names in the constructor PositionTemplate(String templateToUse);
         *
         * @return a list of the identifiers of the currently available templates.
         */
        val templateIds: MutableList<String?>
            get() {
                log!!.debug("in getTemplateIds()")
                val templates = arrayOf(TEMPLATE_TEST_1, TEMPLATE_DEFAULT, TEMPLATE_NO_COMPONENT_PARTS)
                val temp: MutableList<String?> = Arrays.asList(*templates)
                val templateIdList = ArrayList<String?>()
                val defaultTemplatesPropertiesVal: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(
                        ImageCaptureProperties.Companion.KEY_DEFAULT_TEMPLATES)
                //allie: here load the props file: leave the default and config the other var.
//template.default=Default template
//template.default2=ETHZ_template1
//template.default2=ETHZ_template1,ETHZ_template2
                if ("" != defaultTemplatesPropertiesVal) {
                    val st2 = StringTokenizer(defaultTemplatesPropertiesVal, ",")
                    while (st2.hasMoreElements()) { //System.out.println(st2.nextElement());
                        val templ = (st2.nextElement() as String)
                        log.debug("next template:::$templ")
                        templateIdList.add(templ)
                    }
                    //templateIdList.add(defaultTemplatesPropertiesVal); //todo string tokenizer
                    log.debug("defaultTemplatesPropertiesVal::::: $defaultTemplatesPropertiesVal")
                } else { //here it cycles through all the templates (if you leave template.default=Default template and template.default2=)
                    log.debug("it will cycle through all templates::::: $defaultTemplatesPropertiesVal")
                    for (i in temp.indices) {
                        templateIdList.add(temp[i])
                    }
                }
                val tls = TemplateLifeCycle()
                var persistentTemplates: MutableList<Template?> = tls.findAll()
                if (persistentTemplates == null) {
                    tls.cleanUpReferenceImage()
                    persistentTemplates = tls.findAll()
                }
                val iter = persistentTemplates.listIterator()
                while (iter.hasNext()) {
                    templateIdList.add(iter.next()!!.templateId)
                }
                return templateIdList
            }// TODO Auto-generated catch block

        //TemplateLifeCycle tls = new TemplateLifeCycle();
//List<Template> templates = tls.findAll();
        val templates: MutableList<PositionTemplate?>
            get() { //TemplateLifeCycle tls = new TemplateLifeCycle();
//List<Template> templates = tls.findAll();
                val results = ArrayList<PositionTemplate?>()
                val templateIds = templateIds
                val i = templateIds.listIterator()
                while (i.hasNext()) {
                    try {
                        results.add(PositionTemplate(i.next()!!))
                    } catch (e: NoSuchTemplateException) { // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }
                return results
            }

        /**
         * Given an ICImage, look up the template for that image in the database, if none, run a template detector
         * to determine the template for the image.
         *
         * @param image the ICImage for which the PositionTemplate is to be returned.
         * @return the PositionTemplate for this image indicating what information is where in the image.
         * @throws ImageLoadException
         */
        @Throws(ImageLoadException::class)
        fun findTemplateForImage(image: ICImage): PositionTemplate? {
            var result: PositionTemplate? = null
            //TODO: stored path may need separator conversion for different systems.
//String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
            var path: String = image.getPath()
            if (path == null) {
                path = ""
            }
            //File fileToCheck = new File(startPointName + path + image.getFilename());
            val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
            var templateId: String = image.getTemplateId()
            if (templateId == null || templateId == "") { // No template is defined in the database for this image file.
// Check the image with a template detector.
                val detector: PositionTemplateDetector = DefaultPositionTemplateDetector()
                templateId = try {
                    detector.detectTemplateForImage(fileToCheck)
                } catch (e: UnreadableFileException) {
                    throw ImageLoadException(e.message)
                }
            }
            // There is a template defined in the database for this image file
// Check to see if this is a valid template.
            try {
                result = PositionTemplate(templateId)
                // Template exists, load image with it.
            } catch (e: NoSuchTemplateException) { // This template isn't known.  Log the problem.
                log!!.error("Image database record includes an unknown template. " + e.message)
                // Use the no component parts template instead.
                try {
                    result = PositionTemplate(TEMPLATE_NO_COMPONENT_PARTS!!)
                } catch (e1: NoSuchTemplateException) {
                    log.fatal("TEMPLATE_NO_COMPONENT_PARTS produced a NoSuchTemplateException.")
                    log.fatal(e1)
                    log.trace(e1)
                    ImageCaptureApp.exit(ImageCaptureApp.EXIT_ERROR)
                }
            }
            return result
        }
    }
}