/**
 * VerbatimClassifyDialog.java
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

edu.harvard.mcz.imagecapture.entity.Collectorimport edu.harvard.mcz.imagecapture.entity.Numberimport org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport org.filteredpush.qc.date.DateUtilsimport java.awt.Dimensionimport java.awt.Insets
import java.awt.event.ActionEventimport

java.util.*import javax.swing.table.TableColumn

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 *
 */
class VerbatimClassifyDialog : JDialog {
    private val contentPanel: JPanel? = JPanel()
    private var verbatimData: VerbatimCount? = null
    private var comboBoxHigherGeog: FilteringGeogJComboBox? = null
    private var textFieldVerbLocality: JTextArea? = null
    private var textFieldMinElevation: JTextField? = null
    private var textFieldMaxElevation: JTextField? = null
    private var comboBoxElevUnits: JComboBox<*>? = null
    private var lblCount: JLabel? = null
    private var textFieldVerbDate: JTextField? = null
    private var textFieldISODate: JTextField? = null
    private var textFieldVerbCollector: JTextArea? = null
    private var textFieldVerbCollection: JTextArea? = null
    private var textFieldVerbNumbers: JTextArea? = null
    private var textFieldVerbUnclassifiedText: JTextArea? = null
    private var textFieldSpecificLocality: JTextField? = null
    private var jScrollPaneCollectors: JScrollPane? = null
    private var jTableCollectors: JTable? = null
    private var jComboBoxCollection: JComboBox<*>? = null
    private var jScrollPaneNumbers: JScrollPane? = null
    private var jTableNumbers: JTable? = null
    private var textFieldHabitat: JTextField? = null
    private var textFieldMicrohabitat: JTextField? = null
    private var comboBoxWorkflowStatus: JComboBox<*>? = null
    private var tableTaxa: JTable? = null
    private var lastEditedSpecimen: Specimen? = null

    /**
     * Create the dialog.
     */
    constructor() {
        verbatimData = VerbatimCount(0, "", "", "", "", "", "")
        init()
        setValues()
    }

    /**
     * Create the dialog for a particular instance of a set of verbatim values.
     *
     * @param verbatimCount the set of verbatim values to display.
     */
    constructor(verbatimCount: VerbatimCount?) {
        verbatimData = verbatimCount
        init()
        setValues()
    }

    protected fun setValues() {
        lblCount.setText("Number of records with these verbatim values: " + verbatimData.getCount())
        textFieldVerbLocality.setText(verbatimData.getVerbatimLocality())
        textFieldVerbDate.setText(verbatimData.getVerbatimDate())
        textFieldVerbDate.setEditable(false)
        textFieldVerbCollection.setText(verbatimData.getVerbatimCollection())
        textFieldVerbCollector.setText(verbatimData.getVerbatimCollector())
        textFieldVerbNumbers.setText(verbatimData.getVerbatimNumbers())
        textFieldVerbUnclassifiedText.setText(verbatimData.getVerbatimUnclassfiedText())
    }

    protected fun init() {
        setTitle("Interpret verbatim data into fields.")
        setBounds(100, 100, 1203, 899)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(BoxLayout(contentPanel, BoxLayout.Y_AXIS))
        run {
            val panelVerbatimValues = JPanel()
            contentPanel.add(panelVerbatimValues)
            val gbl_panelVerbatimValues = GridBagLayout()
            gbl_panelVerbatimValues.columnWidths = intArrayOf(70, 0, 0, 0, 35, 0, 58, 0, 0, 0)
            gbl_panelVerbatimValues.rowHeights = intArrayOf(15, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 83, 0)
            gbl_panelVerbatimValues.columnWeights = doubleArrayOf(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0)
            gbl_panelVerbatimValues.rowWeights = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE)
            panelVerbatimValues.setLayout(gbl_panelVerbatimValues)
            {
                lblCount = JLabel("New label")
                val gbc_lblNewLabel = GridBagConstraints()
                gbc_lblNewLabel.gridwidth = 2
                gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST
                gbc_lblNewLabel.gridx = 0
                gbc_lblNewLabel.gridy = 0
                panelVerbatimValues.add(lblCount, gbc_lblNewLabel)
            }
            val buttonFillFromLast = JButton("Fill From Last")
            val gbc_buttonFillFromLast = GridBagConstraints()
            gbc_buttonFillFromLast.anchor = GridBagConstraints.SOUTH
            gbc_buttonFillFromLast.insets = Insets(0, 0, 5, 5)
            gbc_buttonFillFromLast.gridx = 3
            gbc_buttonFillFromLast.gridy = 0
            panelVerbatimValues.add(buttonFillFromLast, gbc_buttonFillFromLast)
            {
                val lblNewLabel_1 = JLabel("Field values to apply to all records.")
                val gbc_lblNewLabel_1 = GridBagConstraints()
                gbc_lblNewLabel_1.anchor = GridBagConstraints.SOUTHEAST
                gbc_lblNewLabel_1.gridwidth = 4
                gbc_lblNewLabel_1.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel_1.gridx = 4
                gbc_lblNewLabel_1.gridy = 0
                panelVerbatimValues.add(lblNewLabel_1, gbc_lblNewLabel_1)
            }
            {
                val lblNewLabel_2 = JLabel("Verbatim Locality")
                val gbc_lblNewLabel_2 = GridBagConstraints()
                gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST
                gbc_lblNewLabel_2.insets = Insets(0, 0, 5, 5)
                gbc_lblNewLabel_2.gridx = 0
                gbc_lblNewLabel_2.gridy = 1
                panelVerbatimValues.add(lblNewLabel_2, gbc_lblNewLabel_2)
            }
            {
                textFieldVerbLocality = JTextArea()
                textFieldVerbLocality.setEditable(false)
                textFieldVerbLocality.setRows(5)
                val gbc_textField = GridBagConstraints()
                gbc_textField.gridheight = 3
                gbc_textField.insets = Insets(0, 0, 5, 5)
                gbc_textField.fill = GridBagConstraints.BOTH
                gbc_textField.gridx = 1
                gbc_textField.gridy = 1
                panelVerbatimValues.add(textFieldVerbLocality, gbc_textField)
                textFieldVerbLocality.setColumns(30)
            }
            val lblNewLabel = JLabel("HigherGeography")
            val gbc_lblNewLabel = GridBagConstraints()
            gbc_lblNewLabel.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel.gridx = 3
            gbc_lblNewLabel.gridy = 1
            panelVerbatimValues.add(lblNewLabel, gbc_lblNewLabel)
            val gbc_textFieldHigherGeography = GridBagConstraints()
            gbc_textFieldHigherGeography.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldHigherGeography.gridwidth = 5
            gbc_textFieldHigherGeography.insets = Insets(0, 0, 5, 5)
            gbc_textFieldHigherGeography.gridx = 4
            gbc_textFieldHigherGeography.gridy = 1
            panelVerbatimValues.add(comboBoxHighGeog, gbc_textFieldHigherGeography)
            val btnCopyLocality = JButton(">")
            btnCopyLocality.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (textFieldSpecificLocality.getText().isEmpty()) {
                        textFieldSpecificLocality.setText(textFieldVerbLocality.getText().replace("\n", "").trim({ it <= ' ' }))
                    }
                }
            })
            val gbc_btnCopyLocality = GridBagConstraints()
            gbc_btnCopyLocality.insets = Insets(0, 0, 5, 5)
            gbc_btnCopyLocality.gridx = 2
            gbc_btnCopyLocality.gridy = 2
            panelVerbatimValues.add(btnCopyLocality, gbc_btnCopyLocality)
            val lblSpecificLocality = JLabel("Specific Locality")
            val gbc_lblSpecificLocality = GridBagConstraints()
            gbc_lblSpecificLocality.anchor = GridBagConstraints.EAST
            gbc_lblSpecificLocality.insets = Insets(0, 0, 5, 5)
            gbc_lblSpecificLocality.gridx = 3
            gbc_lblSpecificLocality.gridy = 2
            panelVerbatimValues.add(lblSpecificLocality, gbc_lblSpecificLocality)
            val lblElevation = JLabel("Elevation")
            val gbc_lblElevation = GridBagConstraints()
            gbc_lblElevation.anchor = GridBagConstraints.EAST
            gbc_lblElevation.insets = Insets(0, 0, 5, 5)
            gbc_lblElevation.gridx = 3
            gbc_lblElevation.gridy = 3
            panelVerbatimValues.add(lblElevation, gbc_lblElevation)
            textFieldMinElevation = JTextField()
            textFieldMinElevation.setMinimumSize(Dimension(60, 19))
            val gbc_textFieldMinElevation = GridBagConstraints()
            gbc_textFieldMinElevation.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMinElevation.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldMinElevation.gridx = 4
            gbc_textFieldMinElevation.gridy = 3
            panelVerbatimValues.add(textFieldMinElevation, gbc_textFieldMinElevation)
            textFieldMinElevation.setColumns(4)
            val lblTo = JLabel("to")
            val gbc_lblTo = GridBagConstraints()
            gbc_lblTo.anchor = GridBagConstraints.EAST
            gbc_lblTo.insets = Insets(0, 0, 5, 5)
            gbc_lblTo.gridx = 5
            gbc_lblTo.gridy = 3
            panelVerbatimValues.add(lblTo, gbc_lblTo)
            textFieldMaxElevation = JTextField()
            textFieldMaxElevation.setMinimumSize(Dimension(60, 19))
            val gbc_textFieldMaxElevation = GridBagConstraints()
            gbc_textFieldMaxElevation.anchor = GridBagConstraints.WEST
            gbc_textFieldMaxElevation.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMaxElevation.gridx = 6
            gbc_textFieldMaxElevation.gridy = 3
            panelVerbatimValues.add(textFieldMaxElevation, gbc_textFieldMaxElevation)
            textFieldMaxElevation.setColumns(5)
            comboBoxElevUnits = JComboBox<Any?>()
            val gbc_comboBoxElevUnits = GridBagConstraints()
            gbc_comboBoxElevUnits.insets = Insets(0, 0, 5, 5)
            gbc_comboBoxElevUnits.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxElevUnits.gridx = 7
            gbc_comboBoxElevUnits.gridy = 3
            panelVerbatimValues.add(comboBoxElevUnits, gbc_comboBoxElevUnits)
            {
                val lblVerbatimdate = JLabel("VerbatimDate")
                val gbc_lblVerbatimdate = GridBagConstraints()
                gbc_lblVerbatimdate.anchor = GridBagConstraints.EAST
                gbc_lblVerbatimdate.insets = Insets(0, 0, 5, 5)
                gbc_lblVerbatimdate.gridx = 0
                gbc_lblVerbatimdate.gridy = 4
                panelVerbatimValues.add(lblVerbatimdate, gbc_lblVerbatimdate)
            }
            textFieldVerbDate = JTextField()
            val gbc_textFieldVerbDate = GridBagConstraints()
            gbc_textFieldVerbDate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbDate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbDate.gridx = 1
            gbc_textFieldVerbDate.gridy = 4
            panelVerbatimValues.add(textFieldVerbDate, gbc_textFieldVerbDate)
            textFieldVerbDate.setColumns(30)
            val btnCopyDate = JButton(">")
            btnCopyDate.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (textFieldISODate.getText().isEmpty()) {
                        val extractResult: MutableMap<String?, String?> = DateUtils.extractDateFromVerbatim(textFieldVerbDate.getText().trim({ it <= ' ' }))
                        if (extractResult.containsKey("result")) {
                            textFieldISODate.setText(extractResult["result"])
                        }
                        if (extractResult.containsKey("resultState")) { // TODO: Report suspect etc
                        }
                    }
                }
            })
            val gbc_btnCopyDate = GridBagConstraints()
            gbc_btnCopyDate.insets = Insets(0, 0, 5, 5)
            gbc_btnCopyDate.gridx = 2
            gbc_btnCopyDate.gridy = 4
            panelVerbatimValues.add(btnCopyDate, gbc_btnCopyDate)
            val lblNewLabel_3 = JLabel("ISO Date")
            val gbc_lblNewLabel_3 = GridBagConstraints()
            gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_3.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_3.gridx = 3
            gbc_lblNewLabel_3.gridy = 4
            panelVerbatimValues.add(lblNewLabel_3, gbc_lblNewLabel_3)
            textFieldISODate = JTextField()
            textFieldISODate.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", textFieldISODate))
            textFieldISODate.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ISODate"))
            val gbc_textFieldISODate = GridBagConstraints()
            gbc_textFieldISODate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldISODate.gridwidth = 5
            gbc_textFieldISODate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldISODate.gridx = 4
            gbc_textFieldISODate.gridy = 4
            panelVerbatimValues.add(textFieldISODate, gbc_textFieldISODate)
            textFieldISODate.setColumns(10)
            textFieldSpecificLocality = JTextField()
            val gbc_textFieldSpecificLocality = GridBagConstraints()
            gbc_textFieldSpecificLocality.gridwidth = 5
            gbc_textFieldSpecificLocality.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldSpecificLocality.insets = Insets(0, 0, 5, 5)
            gbc_textFieldSpecificLocality.gridx = 4
            gbc_textFieldSpecificLocality.gridy = 2
            panelVerbatimValues.add(textFieldSpecificLocality, gbc_textFieldSpecificLocality)
            textFieldSpecificLocality.setColumns(25)
            {
                val lblVerbatimCollector = JLabel("Verbatim Collector")
                val gbc_lblVerbatimCollector = GridBagConstraints()
                gbc_lblVerbatimCollector.anchor = GridBagConstraints.EAST
                gbc_lblVerbatimCollector.insets = Insets(0, 0, 5, 5)
                gbc_lblVerbatimCollector.gridx = 0
                gbc_lblVerbatimCollector.gridy = 6
                panelVerbatimValues.add(lblVerbatimCollector, gbc_lblVerbatimCollector)
            }
            textFieldVerbCollector = JTextArea()
            textFieldVerbCollector.setEditable(false)
            textFieldVerbCollector.setRows(3)
            val gbc_textFieldVerbCollector = GridBagConstraints()
            gbc_textFieldVerbCollector.gridheight = 2
            gbc_textFieldVerbCollector.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollector.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbCollector.gridx = 1
            gbc_textFieldVerbCollector.gridy = 6
            panelVerbatimValues.add(textFieldVerbCollector, gbc_textFieldVerbCollector)
            textFieldVerbCollector.setColumns(30)
            val lblCollectors = JLabel("Collector(s)")
            val gbc_lblCollectors = GridBagConstraints()
            gbc_lblCollectors.anchor = GridBagConstraints.EAST
            gbc_lblCollectors.insets = Insets(0, 0, 5, 5)
            gbc_lblCollectors.gridx = 3
            gbc_lblCollectors.gridy = 6
            panelVerbatimValues.add(lblCollectors, gbc_lblCollectors)
            val btnAddCollector = JButton("+")
            btnAddCollector.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    (jTableCollectors.getModel() as CollectorTableModel).addCollector(Collector(null, ""))
                }
            })
            val gbc_btnAddCollector = GridBagConstraints()
            gbc_btnAddCollector.anchor = GridBagConstraints.EAST
            gbc_btnAddCollector.insets = Insets(0, 0, 5, 5)
            gbc_btnAddCollector.gridx = 3
            gbc_btnAddCollector.gridy = 7
            panelVerbatimValues.add(btnAddCollector, gbc_btnAddCollector)
            val gbc_collectorTable = GridBagConstraints()
            gbc_collectorTable.insets = Insets(0, 0, 5, 5)
            gbc_collectorTable.fill = GridBagConstraints.BOTH
            gbc_collectorTable.gridx = 4
            gbc_collectorTable.gridy = 6
            gbc_collectorTable.gridheight = 2
            gbc_collectorTable.gridwidth = 5
            panelVerbatimValues.add(getJScrollPaneCollectors(), gbc_collectorTable)
            val lblVerbatimCollection = JLabel("Verbatim Collection")
            val gbc_lblVerbatimCollection = GridBagConstraints()
            gbc_lblVerbatimCollection.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimCollection.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimCollection.gridx = 0
            gbc_lblVerbatimCollection.gridy = 8
            panelVerbatimValues.add(lblVerbatimCollection, gbc_lblVerbatimCollection)
            textFieldVerbCollection = JTextArea()
            textFieldVerbCollection.setEditable(false)
            textFieldVerbCollection.setRows(3)
            val gbc_textFieldVerbCollection = GridBagConstraints()
            gbc_textFieldVerbCollection.gridheight = 2
            gbc_textFieldVerbCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollection.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbCollection.gridx = 1
            gbc_textFieldVerbCollection.gridy = 8
            panelVerbatimValues.add(textFieldVerbCollection, gbc_textFieldVerbCollection)
            textFieldVerbCollection.setColumns(30)
            val lblCollection = JLabel("Collection")
            val gbc_lblCollection = GridBagConstraints()
            gbc_lblCollection.anchor = GridBagConstraints.EAST
            gbc_lblCollection.insets = Insets(0, 0, 5, 5)
            gbc_lblCollection.gridx = 3
            gbc_lblCollection.gridy = 8
            panelVerbatimValues.add(lblCollection, gbc_lblCollection)
            val gbc_textFieldCollection = GridBagConstraints()
            gbc_textFieldCollection.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldCollection.gridwidth = 5
            gbc_textFieldCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldCollection.gridx = 4
            gbc_textFieldCollection.gridy = 8
            panelVerbatimValues.add(getJComboBoxCollection(), gbc_textFieldCollection)
            val lblVerbatimNumbers = JLabel("Verbatim Numbers")
            val gbc_lblVerbatimNumbers = GridBagConstraints()
            gbc_lblVerbatimNumbers.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimNumbers.gridx = 0
            gbc_lblVerbatimNumbers.gridy = 10
            panelVerbatimValues.add(lblVerbatimNumbers, gbc_lblVerbatimNumbers)
            textFieldVerbNumbers = JTextArea()
            textFieldVerbNumbers.setRows(3)
            textFieldVerbNumbers.setEditable(false)
            val gbc_textFieldVerbNumbers = GridBagConstraints()
            gbc_textFieldVerbNumbers.gridheight = 2
            gbc_textFieldVerbNumbers.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbNumbers.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbNumbers.gridx = 1
            gbc_textFieldVerbNumbers.gridy = 10
            panelVerbatimValues.add(textFieldVerbNumbers, gbc_textFieldVerbNumbers)
            textFieldVerbNumbers.setColumns(30)
            val lblNumbers = JLabel("Numbers")
            val gbc_lblNumbers = GridBagConstraints()
            gbc_lblNumbers.anchor = GridBagConstraints.EAST
            gbc_lblNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblNumbers.gridx = 3
            gbc_lblNumbers.gridy = 10
            panelVerbatimValues.add(lblNumbers, gbc_lblNumbers)
            val btnAddNumber = JButton("+")
            btnAddNumber.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    (jTableNumbers.getModel() as NumberTableModel).addNumber(Number(null, "", ""))
                }
            })
            val gbc_btnAddNumber = GridBagConstraints()
            gbc_btnAddNumber.anchor = GridBagConstraints.EAST
            gbc_btnAddNumber.insets = Insets(0, 0, 5, 5)
            gbc_btnAddNumber.gridx = 3
            gbc_btnAddNumber.gridy = 11
            panelVerbatimValues.add(btnAddNumber, gbc_btnAddNumber)
            val gbc_numberTable = GridBagConstraints()
            gbc_numberTable.insets = Insets(0, 0, 5, 5)
            gbc_numberTable.fill = GridBagConstraints.BOTH
            gbc_numberTable.gridx = 4
            gbc_numberTable.gridy = 10
            gbc_numberTable.gridheight = 2
            gbc_numberTable.gridwidth = 5
            panelVerbatimValues.add(this.getJScrollPaneNumbers(), gbc_numberTable)
            val lblVerbatimOtherText = JLabel("Verbatim Other Text")
            val gbc_lblVerbatimOtherText = GridBagConstraints()
            gbc_lblVerbatimOtherText.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimOtherText.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimOtherText.gridx = 0
            gbc_lblVerbatimOtherText.gridy = 12
            panelVerbatimValues.add(lblVerbatimOtherText, gbc_lblVerbatimOtherText)
            textFieldVerbUnclassifiedText = JTextArea()
            textFieldVerbUnclassifiedText.setEditable(false)
            textFieldVerbUnclassifiedText.setRows(5)
            val gbc_textFieldVerbUnclassifiedText = GridBagConstraints()
            gbc_textFieldVerbUnclassifiedText.gridheight = 3
            gbc_textFieldVerbUnclassifiedText.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbUnclassifiedText.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbUnclassifiedText.gridx = 1
            gbc_textFieldVerbUnclassifiedText.gridy = 12
            panelVerbatimValues.add(textFieldVerbUnclassifiedText, gbc_textFieldVerbUnclassifiedText)
            textFieldVerbUnclassifiedText.setColumns(30)
            val lblHabitat = JLabel("Habitat")
            val gbc_lblHabitat = GridBagConstraints()
            gbc_lblHabitat.anchor = GridBagConstraints.EAST
            gbc_lblHabitat.insets = Insets(0, 0, 5, 5)
            gbc_lblHabitat.gridx = 3
            gbc_lblHabitat.gridy = 12
            panelVerbatimValues.add(lblHabitat, gbc_lblHabitat)
            textFieldHabitat = JTextField()
            val gbc_textFieldHabitat = GridBagConstraints()
            gbc_textFieldHabitat.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldHabitat.gridwidth = 4
            gbc_textFieldHabitat.insets = Insets(0, 0, 5, 5)
            gbc_textFieldHabitat.gridx = 4
            gbc_textFieldHabitat.gridy = 12
            panelVerbatimValues.add(textFieldHabitat, gbc_textFieldHabitat)
            textFieldHabitat.setColumns(10)
            val lblMicrohabitat = JLabel("Microhabitat")
            val gbc_lblMicrohabitat = GridBagConstraints()
            gbc_lblMicrohabitat.anchor = GridBagConstraints.EAST
            gbc_lblMicrohabitat.insets = Insets(0, 0, 5, 5)
            gbc_lblMicrohabitat.gridx = 3
            gbc_lblMicrohabitat.gridy = 13
            panelVerbatimValues.add(lblMicrohabitat, gbc_lblMicrohabitat)
            textFieldMicrohabitat = JTextField()
            val gbc_textFieldMicrohabitat = GridBagConstraints()
            gbc_textFieldMicrohabitat.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldMicrohabitat.gridwidth = 4
            gbc_textFieldMicrohabitat.insets = Insets(0, 0, 5, 5)
            gbc_textFieldMicrohabitat.gridx = 4
            gbc_textFieldMicrohabitat.gridy = 13
            panelVerbatimValues.add(textFieldMicrohabitat, gbc_textFieldMicrohabitat)
            textFieldMicrohabitat.setColumns(10)
            val lblNewLabel_4 = JLabel("Workflow Status")
            val gbc_lblNewLabel_4 = GridBagConstraints()
            gbc_lblNewLabel_4.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_4.gridx = 3
            gbc_lblNewLabel_4.gridy = 15
            panelVerbatimValues.add(lblNewLabel_4, gbc_lblNewLabel_4)
            comboBoxWorkflowStatus = JComboBox<Any?>(WorkFlowStatus.getVerbatimClassifiedWorkFlowStatusValues())
            comboBoxWorkflowStatus.setSelectedItem(WorkFlowStatus.STAGE_CLASSIFIED)
            val gbc_comboBoxWorkflowStatus = GridBagConstraints()
            gbc_comboBoxWorkflowStatus.gridwidth = 4
            gbc_comboBoxWorkflowStatus.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxWorkflowStatus.insets = Insets(0, 0, 5, 5)
            gbc_comboBoxWorkflowStatus.gridx = 4
            gbc_comboBoxWorkflowStatus.gridy = 15
            panelVerbatimValues.add(comboBoxWorkflowStatus, gbc_comboBoxWorkflowStatus)
            val btnApplyToAll = JButton("Apply To All Records")
            val gbc_btnApplyToAll = GridBagConstraints()
            gbc_btnApplyToAll.gridwidth = 4
            gbc_btnApplyToAll.insets = Insets(0, 0, 5, 5)
            gbc_btnApplyToAll.gridx = 4
            gbc_btnApplyToAll.gridy = 16
            panelVerbatimValues.add(btnApplyToAll, gbc_btnApplyToAll)
            btnApplyToAll.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    saveChanges()
                }
            })
        }
        run {
            val panelRelatedData = JPanel()
            contentPanel.add(panelRelatedData)
            val gbl_panelRelatedData = GridBagLayout()
            gbl_panelRelatedData.maximumLayoutSize(panelRelatedData)
            gbl_panelRelatedData.columnWidths = intArrayOf(20, 33, 1, 0)
            gbl_panelRelatedData.rowHeights = intArrayOf(25, 0)
            gbl_panelRelatedData.columnWeights = doubleArrayOf(0.0, 1.0, 0.0, Double.MIN_VALUE)
            gbl_panelRelatedData.rowWeights = doubleArrayOf(1.0, Double.MIN_VALUE)
            panelRelatedData.setLayout(gbl_panelRelatedData)
            {
                val lblTaxa = JLabel("Taxa")
                val gbc_lblTaxa = GridBagConstraints()
                gbc_lblTaxa.anchor = GridBagConstraints.NORTHEAST
                gbc_lblTaxa.insets = Insets(0, 0, 0, 5)
                gbc_lblTaxa.gridx = 0
                gbc_lblTaxa.gridy = 0
                panelRelatedData.add(lblTaxa, gbc_lblTaxa)
            }
            val scrollPane = JScrollPane()
            // scrollPane.setPreferredSize(new Dimension(1000,100));
            val gbc_scrollPane = GridBagConstraints()
            gbc_scrollPane.anchor = GridBagConstraints.NORTH
            gbc_scrollPane.insets = Insets(0, 0, 0, 5)
            gbc_scrollPane.fill = GridBagConstraints.BOTH
            gbc_scrollPane.gridx = 1
            gbc_scrollPane.gridy = 0
            panelRelatedData.add(scrollPane, gbc_scrollPane)
            tableTaxa = JTable()
            val sls = SpecimenLifeCycle()
            tableTaxa.setModel(CountValueTableModel(sls.findTaxaFromVerbatim(this.verbatimData), "Current Idenfifications for these verbatim values."))
            scrollPane.setViewportView(tableTaxa)
        }
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.RIGHT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            {
                val cancelButton = JButton("Close")
                cancelButton.setActionCommand("Close")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
                buttonPane.add(cancelButton)
            }
        }
    }

    private fun getJScrollPaneCollectors(): JScrollPane? {
        if (jScrollPaneCollectors == null) {
            jScrollPaneCollectors = JScrollPane()
            jScrollPaneCollectors.setViewportView(getJTableCollectors())
            jScrollPaneCollectors.setPreferredSize(Dimension(jScrollPaneCollectors.getWidth(), 150))
        }
        return jScrollPaneCollectors
    }

    private val comboBoxHighGeog: FilteringGeogJComboBox?
        private get() {
            if (comboBoxHigherGeog == null) {
                val mls = MCZbaseGeogAuthRecLifeCycle()
                comboBoxHigherGeog = FilteringGeogJComboBox()
                comboBoxHigherGeog.setHGModel(HigherGeographyComboBoxModel(mls.findAll()))
                comboBoxHigherGeog.setEditable(true)
            }
            return comboBoxHigherGeog
        }

    private fun getJTableCollectors(): JTable? {
        if (jTableCollectors == null) {
            jTableCollectors = JTable(CollectorTableModel())
            // Note: When setting the values, the table column editor needs to be reset there, as the model is replaced.
            val field = FilteringAgentJComboBox()
            jTableCollectors.getColumnModel().getColumn(0).setCellEditor(ComboBoxCellEditor(field))
            jTableCollectors.setRowHeight(jTableCollectors.getRowHeight() + 4)
        }
        return jTableCollectors
    }

    /**
     * This method initializes jComboBoxCollection for entering collections  with a controled vocabulary
     *
     * @return javax.swing.JComboBox for collections.
     */
    private fun getJComboBoxCollection(): JComboBox<*>? {
        if (jComboBoxCollection == null) {
            val sls = SpecimenLifeCycle()
            jComboBoxCollection = JComboBox<Any?>()
            jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.getDistinctCollections()))
            jComboBoxCollection.setEditable(true)
            jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
            AutoCompleteDecorator.decorate(jComboBoxCollection)
        }
        return jComboBoxCollection
    }

    /**
     * This method initializes jScrollPaneNumbers
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneNumbers(): JScrollPane? {
        if (jScrollPaneNumbers == null) {
            jScrollPaneNumbers = JScrollPane()
            jScrollPaneNumbers.setViewportView(getJTableNumbers())
            jScrollPaneNumbers.setPreferredSize(Dimension(jScrollPaneCollectors.getWidth(), 150))
        }
        return jScrollPaneNumbers
    }

    /**
     * This method initializes jTableNumbers
     *
     * @return javax.swing.JTable
     */
    private fun getJTableNumbers(): JTable? {
        if (jTableNumbers == null) {
            jTableNumbers = JTable(NumberTableModel())
            val jComboNumberTypes: JComboBox<String?> = JComboBox<String?>()
            jComboNumberTypes.setModel(DefaultComboBoxModel<String?>(NumberLifeCycle.Companion.getDistinctTypes()))
            jComboNumberTypes.setEditable(true)
            val typeColumn: TableColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.Companion.COLUMN_TYPE)
            val comboBoxEditor = DefaultCellEditor(jComboNumberTypes)
            //TODO: enable autocomplete for numbertypes picklist.
//AutoCompleteDecorator.decorate((JComboBox) comboBoxEditor.getComponent());
            typeColumn.cellEditor = comboBoxEditor
            val renderer = DefaultTableCellRenderer()
            renderer.setToolTipText("Click for pick list of number types.")
            typeColumn.cellRenderer = renderer
        }
        return jTableNumbers
    }

    protected fun saveChanges() {
        if (jTableCollectors.isEditing()) {
            jTableCollectors.getCellEditor().stopCellEditing()
        }
        if (jTableNumbers.isEditing()) {
            jTableNumbers.getCellEditor().stopCellEditing()
        }
        log.debug("Saving verbatim data changes to all records with shared verbatim data.")
        log.debug("Should affect " + verbatimData.getCount() + " Specimen records.")
        val sls = SpecimenLifeCycle()
        val specimens: MutableList<Specimen?> = sls.findSpecimensFromVerbatim(verbatimData)
        val i: MutableIterator<Specimen?> = specimens.iterator()
        while (i.hasNext()) {
            val specimen: Specimen? = i.next()
            // populate fields from parsed (10 fields, 2 lists)
            if (comboBoxHigherGeog.getSelectedIndex() == -1 && comboBoxHigherGeog.getSelectedItem() == null) {
                specimen.setHigherGeography("")
            } else { // combo box contains a geography object, obtain the higher geography string.
                specimen.setHigherGeography((comboBoxHigherGeog.getModel() as HigherGeographyComboBoxModel).getSelectedItemHigherGeography())
            }
            specimen.setSpecificLocality(textFieldSpecificLocality.getText())
            if (jTableCollectors.getModel().getRowCount() > 0) { // add collectors
                val rows: Int = jTableCollectors.getModel().getRowCount()
                for (row in 0 until rows) {
                    val collector = (jTableCollectors.getModel().getValueAt(row, 1) as String)
                    specimen.getCollectors().add(Collector(specimen, collector))
                }
            }
            if (jComboBoxCollection.getSelectedIndex() == -1 && jComboBoxCollection.getSelectedItem() == null) {
                specimen.setCollection("")
            } else {
                specimen.setCollection(jComboBoxCollection.getSelectedItem().toString())
            }
            // Elevations
            var min_elev: Long?
            min_elev = if (textFieldMinElevation.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMinElevation.getText().toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMinimum_elevation(min_elev)
            var max_elev: Long?
            max_elev = if (textFieldMaxElevation.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMaxElevation.getText().toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMaximum_elevation(max_elev)
            if (comboBoxElevUnits.getSelectedIndex() == -1 && comboBoxElevUnits.getSelectedItem() == null) {
                specimen.setElev_units("")
            } else {
                specimen.setElev_units(comboBoxElevUnits.getSelectedItem().toString())
            }
            specimen.setIsoDate(textFieldISODate.getText().trim({ it <= ' ' }))
            specimen.setHabitat(textFieldHabitat.getText())
            specimen.setMicrohabitat(textFieldMicrohabitat.getText())
            specimen.setWorkFlowStatus(comboBoxWorkflowStatus.getSelectedItem() as String)
            // store values for reuse
            storeLastEditedValues(specimen)
            try {
                sls.attachDirty(specimen)
            } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    private fun storeLastEditedValues(lastSpecimen: Specimen) {
        if (lastEditedSpecimen == null) {
            lastEditedSpecimen = Specimen()
            lastEditedSpecimen.setDateCreated(Date())
        }
        lastEditedSpecimen.setSpecificLocality(lastSpecimen.getSpecificLocality())
        lastEditedSpecimen.setHigherGeography(lastSpecimen.getHigherGeography())
        lastEditedSpecimen.setCollection(lastSpecimen.getCollection())
    }

    companion object {
        private const val serialVersionUID = 2718225599980885040L
        private val log: Log? = LogFactory.getLog(VerbatimClassifyDialog::class.java)
    }
}