/**
 * GeoreferenceDialog.java
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
package edu.harvard.mcz.imagecapture.ui.dialogimportimport

org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport java.awt.event.ActionEvent
import java.text.ParseException

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 *
 */
class GeoreferenceDialog : JDialog {
    private val contentPanel: JPanel? = JPanel()
    private var comboBoxOrigUnits: JComboBox<String?>? = null
    private var comboBoxErrorUnits: JComboBox<String?>? = null
    private var txtGPSAccuracy: JTextField? = null
    private var textFieldDecimalLat: JTextField? = null
    private var textFieldDecimalLong: JTextField? = null
    private var txtLatDegrees: JTextField? = null
    private var txtLatDecMin: JTextField? = null
    private var txtLatMin: JTextField? = null
    private var txtLatSec: JTextField? = null
    private var cbLatDir: JComboBox<String?>? = null
    private var txtLongDegrees: JTextField? = null
    private var txtLongDecMin: JTextField? = null
    private var txtLongMin: JTextField? = null
    private var txtLongSec: JTextField? = null
    private var cbLongDir: JComboBox<String?>? = null
    private var cbDatum: JComboBox<String?>? = null
    private var cbMethod: JComboBox<String?>? = null
    private var georeference: LatLong? = null
    private var okButton: JButton? = null
    private var lblErrorLabel: JLabel? = null
    private var lblNewLabel: JLabel? = null
    private var textFieldRemarks: JTextField? = null
    private var lblErrorRadius: JLabel? = null
    private var txtErrorRadius: JTextField? = null
    private var textFieldDetBy: JTextField? = null
    private var textDetDate: JFormattedTextField? = null
    private var textRefSource: JTextField? = null

    /**
     * Create the dialog.
     */
    constructor() {
        init()
    }

    constructor(georeference: LatLong?) {
        this.georeference = georeference
        init()
        loadData()
        setState()
    }

    private fun loadData() {
        lblErrorLabel.setText("")
        textFieldDecimalLat.setText(georeference.getDecLatString())
        textFieldDecimalLong.setText(georeference.getDecLongString())
        log.debug("load geo data: lat: " + georeference.getDecLatString() +
                ", long: " + georeference.getDecLongString())
        if (georeference.getDatum() != "") {
            cbDatum.setSelectedItem(georeference.getDatum())
        }
        cbMethod.setSelectedItem(georeference.getGeorefmethod())
        txtGPSAccuracy.setText(georeference.getGpsaccuracyString())
        comboBoxOrigUnits.setSelectedItem(georeference.getOrigLatLongUnits())
        txtLatDegrees.setText(georeference.getLatDegString())
        txtLatDecMin.setText(georeference.getDecLatMinString())
        txtLatMin.setText(georeference.getLatMinString())
        txtLatSec.setText(georeference.getLatSecString())
        cbLatDir.setSelectedItem(georeference.getLatDir())
        txtLongDegrees.setText(georeference.getLongDegString())
        txtLongDecMin.setText(georeference.getDecLongMinString())
        txtLongMin.setText(georeference.getLongMinString())
        txtLongSec.setText(georeference.getLongSecString())
        cbLongDir.setSelectedItem(georeference.getLongDir())
        txtErrorRadius.setText(georeference.getMaxErrorDistanceString())
        comboBoxErrorUnits.setSelectedItem(georeference.getMaxErrorUnits())
        textFieldDetBy.setText(georeference.getDeterminedByAgent())
        textDetDate.setValue(SimpleDateFormat("yyyy-MM-dd")
                .format(georeference.getDeterminedDate()))
        textRefSource.setText(georeference.getLatLongRefSource())
        textFieldRemarks.setText(georeference.getLatLongRemarks())
    }

    private fun setState() {
        val acc: String = cbMethod.getSelectedItem().toString()
        if (acc == "GPS") {
            txtGPSAccuracy.setEnabled(true)
        } else {
            txtGPSAccuracy.setEnabled(false)
        }
        val state: String = comboBoxOrigUnits.getSelectedItem().toString()
        when (state) {
            "degrees dec. minutes" -> {
                textFieldDecimalLat.setEnabled(false)
                textFieldDecimalLong.setEnabled(false)
                txtLatDegrees.setEnabled(true)
                txtLatDecMin.setEnabled(true)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(true)
                txtLongDegrees.setEnabled(true)
                txtLongDecMin.setEnabled(true)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(true)
            }
            "deg. min. sec." -> {
                textFieldDecimalLat.setEnabled(false)
                textFieldDecimalLong.setEnabled(false)
                txtLatDegrees.setEnabled(true)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(true)
                txtLatSec.setEnabled(true)
                cbLatDir.setEnabled(true)
                txtLongDegrees.setEnabled(true)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(true)
                txtLongSec.setEnabled(true)
                cbLongDir.setEnabled(true)
            }
            "decimal degrees", "unknown" -> {
                textFieldDecimalLat.setEnabled(true)
                textFieldDecimalLong.setEnabled(true)
                txtLatDegrees.setEnabled(false)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(false)
                txtLongDegrees.setEnabled(false)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(false)
            }
            else -> {
                textFieldDecimalLat.setEnabled(true)
                textFieldDecimalLong.setEnabled(true)
                txtLatDegrees.setEnabled(false)
                txtLatDecMin.setEnabled(false)
                txtLatMin.setEnabled(false)
                txtLatSec.setEnabled(false)
                cbLatDir.setEnabled(false)
                txtLongDegrees.setEnabled(false)
                txtLongDecMin.setEnabled(false)
                txtLongMin.setEnabled(false)
                txtLongSec.setEnabled(false)
                cbLongDir.setEnabled(false)
            }
        }
    }

    private fun saveData(): Boolean {
        var result = true
        okButton.grabFocus()
        if (textFieldDecimalLat.getText().length > 0) {
            try {
                georeference.setDecLat(BigDecimal.valueOf(textFieldDecimalLat.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Latitude number format")
                result = false
            }
        } else {
            georeference.setDecLat(null)
        }
        if (textFieldDecimalLong.getText().length > 0) {
            try {
                georeference.setDecLong(BigDecimal.valueOf(textFieldDecimalLong.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Longitude number format")
                result = false
            }
        } else {
            georeference.setDecLong(null)
        }
        if (cbDatum.getSelectedItem() != null) {
            georeference.setDatum(cbDatum.getSelectedItem().toString())
        }
        if (cbMethod.getSelectedItem() != null) {
            georeference.setGeorefmethod(cbMethod.getSelectedItem().toString())
        }
        if (txtGPSAccuracy.getText().length > 0) {
            try {
                georeference.setGpsaccuracy(
                        BigDecimal.valueOf(txtGPSAccuracy.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: GPS Accuracy number format")
                result = false
            }
        }
        if (comboBoxOrigUnits.getSelectedItem() != null) {
            georeference.setOrigLatLongUnits(
                    comboBoxOrigUnits.getSelectedItem().toString())
        }
        if (txtLatDegrees.getText().length > 0) {
            try {
                georeference.setLatDeg(txtLatDegrees.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Degrees number format")
                result = false
            }
        }
        if (txtLatDecMin.getText().length > 0) {
            try {
                georeference.setDecLatMin(
                        BigDecimal.valueOf(txtLatDecMin.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Dec Min number format")
                result = false
            }
        }
        if (txtLatMin.getText().length > 0) {
            try {
                georeference.setLatMin(txtLatMin.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Min number format")
                result = false
            }
        }
        if (txtLatSec.getText().length > 0) {
            try {
                georeference.setLatSec(
                        BigDecimal.valueOf(txtLatSec.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Lat Degrees number format")
                result = false
            }
        }
        if (cbLatDir.getSelectedItem() != null) {
            georeference.setLatDir(cbLatDir.getSelectedItem().toString())
        }
        if (txtLongDegrees.getText().length > 0) {
            try {
                georeference.setLongDeg(txtLongDegrees.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Degrees number format")
                result = false
            }
        }
        if (txtLongDecMin.getText().length > 0) {
            try {
                georeference.setDecLongMin(
                        BigDecimal.valueOf(txtLongDecMin.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Dec Min number format")
                result = false
            }
        }
        if (txtLongMin.getText().length > 0) {
            try {
                georeference.setLongMin(txtLongMin.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Min number format")
                result = false
            }
        }
        if (txtLongSec.getText().length > 0) {
            try {
                georeference.setLongSec(
                        BigDecimal.valueOf(txtLongSec.getText().toDouble()))
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Long Degrees number format")
                result = false
            }
        }
        if (cbLongDir.getSelectedItem() != null) {
            georeference.setLongDir(cbLongDir.getSelectedItem().toString())
        }
        if (txtErrorRadius.getText().length > 0) {
            try {
                georeference.setMaxErrorDistance(txtErrorRadius.getText().toInt())
            } catch (e: NumberFormatException) {
                lblErrorLabel.setText("Error: Error radius number format")
                result = false
            }
        }
        if (comboBoxErrorUnits.getSelectedItem() != null) {
            georeference.setMaxErrorUnits(
                    comboBoxErrorUnits.getSelectedItem().toString())
        }
        georeference.setDeterminedByAgent(textFieldDetBy.getText())
        try {
            georeference.setDeterminedDate(
                    SimpleDateFormat("yyyy-MM-dd").parse(textDetDate.getText()))
        } catch (e: ParseException) {
            lblErrorLabel.setText("Error: Error date determined format")
            result = false
        }
        georeference.setLatLongRefSource(textRefSource.getText())
        georeference.setLatLongRemarks(textFieldRemarks.getText())
        return result
    }

    private fun init() {
        setBounds(100, 100, 450, 560)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(GridLayout(0, 2, 0, 0))
        run {
            val lblLatitude = JLabel("Latitude")
            lblLatitude.setHorizontalAlignment(SwingConstants.RIGHT)
            contentPanel.add(lblLatitude)
        }
        textFieldDecimalLat = JTextField()
        contentPanel.add(textFieldDecimalLat)
        textFieldDecimalLat.setColumns(10)
        val lblLongitude = JLabel("Longitude")
        lblLongitude.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongitude)
        textFieldDecimalLong = JTextField()
        contentPanel.add(textFieldDecimalLong)
        textFieldDecimalLong.setColumns(10)
        val lblMethod = JLabel("Method")
        lblMethod.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblMethod)
        val methodModel: ComboBoxModel<String?> = ListComboBoxModel<String?>(LatLong.Companion.getGeorefMethodValues())
        cbMethod = JComboBox<String?>(DefaultComboBoxModel<String?>(arrayOf<String?>(
                "not recorded", "unknown", "GEOLocate", "Geoportal", "Google Earth",
                "Google Maps", "Gazeteer", "GPS", "Label Data", "Wikipedia",
                "MaNIS/HertNet/ORNIS Georeferencing Guidelines")))
        cbMethod.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                setState()
            }
        })
        contentPanel.add(cbMethod)
        val lblDatum = JLabel("Datum")
        lblDatum.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDatum)
        val datumModel: ComboBoxModel<String?> = ListComboBoxModel<String?>(LatLong.Companion.getDatumValues())
        cbDatum = JComboBox<String?>(datumModel)
        // set default
        cbDatum.setSelectedItem("WGS84")
        contentPanel.add(cbDatum)
        val lblAccuracy = JLabel("GPS Accuracy")
        lblAccuracy.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblAccuracy)
        txtGPSAccuracy = JTextField()
        txtGPSAccuracy.setColumns(10)
        contentPanel.add(txtGPSAccuracy)
        val lblNewLabel_1 = JLabel("Original Units")
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblNewLabel_1)
        comboBoxOrigUnits = JComboBox<String?>()
        comboBoxOrigUnits.addActionListener(object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                setState()
            }
        })
        comboBoxOrigUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("decimal degrees", "deg. min. sec.",
                "degrees dec. minutes", "unknown")))
        contentPanel.add(comboBoxOrigUnits)
        lblErrorRadius = JLabel("Error Radius")
        lblErrorRadius.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblErrorRadius)
        txtErrorRadius = JTextField()
        txtErrorRadius.setColumns(10)
        contentPanel.add(txtErrorRadius)
        val lblErrorRadiusUnits = JLabel("Error Radius Units")
        lblErrorRadiusUnits.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblErrorRadiusUnits)
        comboBoxErrorUnits = JComboBox<String?>()
        comboBoxErrorUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("m", "ft", "km", "mi", "yd")))
        contentPanel.add(comboBoxErrorUnits)
        val lblLatDegrees = JLabel("Lat Degrees")
        lblLatDegrees.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDegrees)
        txtLatDegrees = JTextField()
        txtLatDegrees.setColumns(4)
        contentPanel.add(txtLatDegrees)
        val lblLatDecMin = JLabel("Lat Dec Min")
        lblLatDecMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDecMin)
        txtLatDecMin = JTextField()
        txtLatDecMin.setColumns(6)
        contentPanel.add(txtLatDecMin)
        val lblLatMin = JLabel("Lat Min")
        lblLatMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatMin)
        txtLatMin = JTextField()
        txtLatMin.setColumns(6)
        contentPanel.add(txtLatMin)
        val lblLatSec = JLabel("Lat Sec")
        lblLatSec.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatSec)
        txtLatSec = JTextField()
        txtLatSec.setColumns(6)
        contentPanel.add(txtLatSec)
        val lblLatDir = JLabel("Lat N/S")
        lblLatDir.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLatDir)
        cbLatDir = JComboBox<String?>()
        cbLatDir.setModel(
                DefaultComboBoxModel<String?>(arrayOf<String?>("N", "S")))
        contentPanel.add(cbLatDir)
        val lblLongDegrees = JLabel("Long Degrees")
        lblLongDegrees.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDegrees)
        txtLongDegrees = JTextField()
        txtLongDegrees.setColumns(4)
        contentPanel.add(txtLongDegrees)
        val lblLongDecMin = JLabel("Long Dec Min")
        lblLongDecMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDecMin)
        txtLongDecMin = JTextField()
        txtLongDecMin.setColumns(6)
        contentPanel.add(txtLongDecMin)
        val lblLongMin = JLabel("Long Min")
        lblLongMin.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongMin)
        txtLongMin = JTextField()
        txtLongMin.setColumns(6)
        contentPanel.add(txtLongMin)
        val lblLongSec = JLabel("Long Sec")
        lblLongSec.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongSec)
        txtLongSec = JTextField()
        txtLongSec.setColumns(6)
        contentPanel.add(txtLongSec)
        val lblLongDir = JLabel("Long E/W")
        lblLongDir.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblLongDir)
        cbLongDir = JComboBox<String?>()
        cbLongDir.setModel(
                DefaultComboBoxModel<String?>(arrayOf<String?>("E", "W")))
        contentPanel.add(cbLongDir)
        val lblDetBy = JLabel("Determined By")
        lblDetBy.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDetBy)
        textFieldDetBy = JTextField()
        contentPanel.add(textFieldDetBy)
        textFieldDetBy.setColumns(10)
        val lblDetDate = JLabel("Date Determined")
        lblDetDate.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblDetDate)
        try {
            textDetDate = JFormattedTextField(MaskFormatter("####-##-##"))
        } catch (e1: ParseException) {
            textDetDate = JFormattedTextField()
        }
        textDetDate.setToolTipText(
                "Date on which georeference was made yyyy-mm-dd")
        contentPanel.add(textDetDate)
        val lblRef = JLabel("Reference Source")
        lblRef.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblRef)
        textRefSource = JTextField()
        contentPanel.add(textRefSource)
        textRefSource.setColumns(10)
        lblNewLabel = JLabel("Remarks")
        lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT)
        contentPanel.add(lblNewLabel)
        textFieldRemarks = JTextField()
        contentPanel.add(textFieldRemarks)
        textFieldRemarks.setColumns(10)
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.RIGHT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            {
                lblErrorLabel = JLabel("Message")
                buttonPane.add(lblErrorLabel)
            }
            {
                okButton = JButton("OK")
                okButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        lblErrorLabel.setText("")
                        if (saveData()) {
                            setVisible(false)
                        }
                    }
                })
                okButton.setActionCommand("OK")
                buttonPane.add(okButton)
                getRootPane().setDefaultButton(okButton)
            }
            {
                val cancelButton = JButton("Cancel")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        loadData()
                        setVisible(false)
                    }
                })
                cancelButton.setActionCommand("Cancel")
                buttonPane.add(cancelButton)
            }
        }
    }

    companion object {
        private const val serialVersionUID = -257199970146455008L
        private val log: Log? = LogFactory.getLog(GeoreferenceDialog::class.java)
    }
}