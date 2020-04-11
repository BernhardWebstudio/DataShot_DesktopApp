/**
 * VerbatimCaptureDialog.java
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

org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport java.awt.Dimensionimport java.awt.Imageimport java.awt.Insetsimport java.awt.event.ActionEvent
import java.io.File

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 *
 */
class VerbatimCaptureDialog : JDialog, DataChangeListener {
    private val contentPanel: JPanel? = JPanel()
    private var specimen: Specimen? = null
    private var specimenController: SpecimenController? = null
    private var lblBarcode: JLabel? = null
    private var lblCurrentid: JLabel? = null
    private var imagePanelPinLabels: ImageZoomPanel? = null // the specimen labels from the pin.
    private var textFieldVerbLocality: JTextArea? = null
    private var textFieldVerbDate: JTextField? = null
    private var textFieldVerbCollector: JTextField? = null
    private var textFieldVerbCollection: JTextField? = null
    private var textFieldVerbNumbers: JTextField? = null
    private var textFieldVerbUnclassifiedText: JTextArea? = null
    private var textFieldQuestions: JTextField? = null
    private var comboBoxWorkflowStatus: JComboBox<String?>? = null
    private var btnPrevious: JButton? = null
    private var btnNext: JButton? = null

    /**
     * Create the dialog.
     */
    constructor() {
        init()
    }

    /**
     * Create the dialog for a specimen.
     *
     * @param targetSpecimen
     */
    constructor(targetSpecimen: Specimen?, targetSpecimenController: SpecimenController?) {
        specimen = targetSpecimen
        specimenController = targetSpecimenController
        specimenController.addListener(this)
        init()
        if (specimen != null) {
            setValues()
        }
    }

    protected fun setValues() {
        lblBarcode.setText(specimen.getBarcode())
        lblCurrentid.setText(specimen.assembleScientificName())
        textFieldVerbLocality.setText(specimen.getVerbatimLocality())
        textFieldVerbDate.setText(specimen.getDateNos())
        textFieldVerbCollector.setText(specimen.getVerbatimCollector())
        textFieldVerbCollection.setText(specimen.getVerbatimCollection())
        textFieldVerbNumbers.setText(specimen.getVerbatimNumbers())
        textFieldVerbUnclassifiedText.setText(specimen.getVerbatimUnclassifiedText())
        comboBoxWorkflowStatus.setSelectedItem(specimen.getWorkFlowStatus())
        try {
            val i: MutableIterator<ICImage?> = specimen.getICImages().iterator()
            var image: ICImage? = null
            var gotImg = false
            while (i.hasNext() && !gotImg) {
                image = i.next()
                gotImg = true
            }
            var path: String = image.getPath()
            if (path == null) {
                path = ""
            }
            val anImageFile = File(assemblePathWithBase(path, image.getFilename()))
            val defaultTemplate: PositionTemplate = PositionTemplate.Companion.findTemplateForImage(image)
            val imagefile: BufferedImage = ImageIO.read(anImageFile)
            val x: Int = defaultTemplate.getLabelPosition().width
            val y: Int = defaultTemplate.getLabelPosition().height
            val w: Int = defaultTemplate.getLabelSize().width
            val h: Int = defaultTemplate.getLabelSize().height
            setPinLabelImage(imagefile.getSubimage(x, y, w, h))
            fitPinLabels()
        } catch (e: ImageLoadException) {
            log.error(e.message, e)
            println(e.message)
        } catch (e: IOException) {
            log.error(e.message, e)
            println(e.message)
        }
        if (specimenController != null) {
            btnNext.setEnabled(specimenController.hasNextSpecimenInTable())
            btnPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable())
        } else {
            btnNext.setEnabled(false)
            btnPrevious.setEnabled(false)
        }
    }

    protected fun init() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        setTitle("Transcribe Verbatim Data")
        setMinimumSize(Dimension(1020, 640))
        setBounds(100, 100, 1020, 640)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        contentPanel.setLayout(BorderLayout(0, 0))
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.NORTH)
            panel.setLayout(FlowLayout(FlowLayout.CENTER, 5, 5))
            val lblVerbatimDataFor = JLabel("Verbatim Data for:")
            panel.add(lblVerbatimDataFor)
            lblBarcode = JLabel("Barcode")
            panel.add(lblBarcode)
            lblCurrentid = JLabel("CurrentID")
            panel.add(lblCurrentid)
        }
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.WEST)
            val gbl_panel = GridBagLayout()
            gbl_panel.columnWidths = intArrayOf(0, 0, 0, 0)
            gbl_panel.rowHeights = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            gbl_panel.columnWeights = doubleArrayOf(0.0, 1.0, 1.0, Double.MIN_VALUE)
            gbl_panel.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE)
            panel.setLayout(gbl_panel)
            val lblNewLabel = JLabel("Locality")
            val gbc_lblNewLabel = GridBagConstraints()
            gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST
            gbc_lblNewLabel.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel.gridx = 0
            gbc_lblNewLabel.gridy = 0
            panel.add(lblNewLabel, gbc_lblNewLabel)
            textFieldVerbLocality = JTextArea()
            textFieldVerbLocality.setRows(3)
            val gbc_textFieldVerbLocality = GridBagConstraints()
            gbc_textFieldVerbLocality.gridheight = 2
            gbc_textFieldVerbLocality.gridwidth = 2
            gbc_textFieldVerbLocality.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbLocality.fill = GridBagConstraints.BOTH
            gbc_textFieldVerbLocality.gridx = 1
            gbc_textFieldVerbLocality.gridy = 0
            panel.add(textFieldVerbLocality, gbc_textFieldVerbLocality)
            textFieldVerbLocality.setColumns(10)
            val lblVerbatimDate = JLabel("Date")
            val gbc_lblVerbatimDate = GridBagConstraints()
            gbc_lblVerbatimDate.anchor = GridBagConstraints.EAST
            gbc_lblVerbatimDate.insets = Insets(0, 0, 5, 5)
            gbc_lblVerbatimDate.gridx = 0
            gbc_lblVerbatimDate.gridy = 2
            panel.add(lblVerbatimDate, gbc_lblVerbatimDate)
            textFieldVerbDate = JTextField()
            val gbc_textFieldVerbDate = GridBagConstraints()
            gbc_textFieldVerbDate.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbDate.gridwidth = 2
            gbc_textFieldVerbDate.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbDate.gridx = 1
            gbc_textFieldVerbDate.gridy = 2
            panel.add(textFieldVerbDate, gbc_textFieldVerbDate)
            textFieldVerbDate.setColumns(10)
            val lblCollector = JLabel("Collector")
            val gbc_lblCollector = GridBagConstraints()
            gbc_lblCollector.anchor = GridBagConstraints.EAST
            gbc_lblCollector.insets = Insets(0, 0, 5, 5)
            gbc_lblCollector.gridx = 0
            gbc_lblCollector.gridy = 3
            panel.add(lblCollector, gbc_lblCollector)
            textFieldVerbCollector = JTextField()
            val gbc_textFieldVerbCollector = GridBagConstraints()
            gbc_textFieldVerbCollector.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollector.gridwidth = 2
            gbc_textFieldVerbCollector.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbCollector.gridx = 1
            gbc_textFieldVerbCollector.gridy = 3
            panel.add(textFieldVerbCollector, gbc_textFieldVerbCollector)
            textFieldVerbCollector.setColumns(10)
            val lblNewLabel_1 = JLabel("Collection")
            val gbc_lblNewLabel_1 = GridBagConstraints()
            gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_1.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_1.gridx = 0
            gbc_lblNewLabel_1.gridy = 4
            panel.add(lblNewLabel_1, gbc_lblNewLabel_1)
            textFieldVerbCollection = JTextField()
            val gbc_textFieldVerbCollection = GridBagConstraints()
            gbc_textFieldVerbCollection.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbCollection.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbCollection.gridx = 2
            gbc_textFieldVerbCollection.gridy = 4
            panel.add(textFieldVerbCollection, gbc_textFieldVerbCollection)
            textFieldVerbCollection.setColumns(10)
            val lblNumbers = JLabel("Numbers")
            val gbc_lblNumbers = GridBagConstraints()
            gbc_lblNumbers.anchor = GridBagConstraints.EAST
            gbc_lblNumbers.insets = Insets(0, 0, 5, 5)
            gbc_lblNumbers.gridx = 0
            gbc_lblNumbers.gridy = 5
            panel.add(lblNumbers, gbc_lblNumbers)
            textFieldVerbNumbers = JTextField()
            val gbc_textFieldVerbNumbers = GridBagConstraints()
            gbc_textFieldVerbNumbers.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbNumbers.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbNumbers.gridx = 2
            gbc_textFieldVerbNumbers.gridy = 5
            panel.add(textFieldVerbNumbers, gbc_textFieldVerbNumbers)
            textFieldVerbNumbers.setColumns(10)
            val lblNewLabel_2 = JLabel("Other Text")
            val gbc_lblNewLabel_2 = GridBagConstraints()
            gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST
            gbc_lblNewLabel_2.insets = Insets(0, 0, 5, 5)
            gbc_lblNewLabel_2.gridx = 0
            gbc_lblNewLabel_2.gridy = 6
            panel.add(lblNewLabel_2, gbc_lblNewLabel_2)
            textFieldVerbUnclassifiedText = JTextArea()
            textFieldVerbUnclassifiedText.setRows(3)
            val gbc_textFieldVerbUnclassifiedText = GridBagConstraints()
            gbc_textFieldVerbUnclassifiedText.gridheight = 2
            gbc_textFieldVerbUnclassifiedText.insets = Insets(0, 0, 5, 5)
            gbc_textFieldVerbUnclassifiedText.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldVerbUnclassifiedText.gridx = 2
            gbc_textFieldVerbUnclassifiedText.gridy = 6
            panel.add(textFieldVerbUnclassifiedText, gbc_textFieldVerbUnclassifiedText)
            textFieldVerbUnclassifiedText.setColumns(10)
            val lblQuestions = JLabel("Questions")
            val gbc_lblQuestions = GridBagConstraints()
            gbc_lblQuestions.anchor = GridBagConstraints.EAST
            gbc_lblQuestions.insets = Insets(0, 0, 5, 5)
            gbc_lblQuestions.gridx = 0
            gbc_lblQuestions.gridy = 8
            panel.add(lblQuestions, gbc_lblQuestions)
            textFieldQuestions = JTextField()
            val gbc_textFieldQuestions = GridBagConstraints()
            gbc_textFieldQuestions.insets = Insets(0, 0, 5, 5)
            gbc_textFieldQuestions.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldQuestions.gridx = 2
            gbc_textFieldQuestions.gridy = 8
            panel.add(textFieldQuestions, gbc_textFieldQuestions)
            textFieldQuestions.setColumns(30)
            val lblWorkflowStatus = JLabel("Workflow Status")
            val gbc_lblWorkflowStatus = GridBagConstraints()
            gbc_lblWorkflowStatus.anchor = GridBagConstraints.EAST
            gbc_lblWorkflowStatus.insets = Insets(0, 0, 5, 5)
            gbc_lblWorkflowStatus.gridx = 0
            gbc_lblWorkflowStatus.gridy = 9
            panel.add(lblWorkflowStatus, gbc_lblWorkflowStatus)
            comboBoxWorkflowStatus = JComboBox<String?>(WorkFlowStatus.getVerbatimWorkFlowStatusValues())
            val gbc_comboBoxWorkflowStatus = GridBagConstraints()
            gbc_comboBoxWorkflowStatus.insets = Insets(0, 0, 5, 0)
            gbc_comboBoxWorkflowStatus.gridwidth = 2
            gbc_comboBoxWorkflowStatus.fill = GridBagConstraints.HORIZONTAL
            gbc_comboBoxWorkflowStatus.gridx = 2
            gbc_comboBoxWorkflowStatus.gridy = 9
            panel.add(comboBoxWorkflowStatus, gbc_comboBoxWorkflowStatus)
            val panel_1 = JPanel()
            val gbc_panel_1 = GridBagConstraints()
            gbc_panel_1.gridwidth = 2
            gbc_panel_1.insets = Insets(0, 0, 0, 5)
            gbc_panel_1.fill = GridBagConstraints.BOTH
            gbc_panel_1.gridx = 2
            gbc_panel_1.gridy = 11
            panel.add(panel_1, gbc_panel_1)
            val gbl_panel_1 = GridBagLayout()
            gbl_panel_1.columnWidths = intArrayOf(150, 143, 0)
            gbl_panel_1.rowHeights = intArrayOf(25, 0, 0, 0, 0)
            gbl_panel_1.columnWeights = doubleArrayOf(0.0, 0.0, 0.0)
            gbl_panel_1.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE)
            panel_1.setLayout(gbl_panel_1)
            val btnPartiallyIllegible = JButton("Partially Illegible")
            btnPartiallyIllegible.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.PARTLY_ILLEGIBLE)
                }
            })
            val gbc_btnPartiallyIllegible = GridBagConstraints()
            gbc_btnPartiallyIllegible.fill = GridBagConstraints.HORIZONTAL
            gbc_btnPartiallyIllegible.anchor = GridBagConstraints.NORTH
            gbc_btnPartiallyIllegible.insets = Insets(0, 0, 5, 5)
            gbc_btnPartiallyIllegible.gridx = 0
            gbc_btnPartiallyIllegible.gridy = 0
            panel_1.add(btnPartiallyIllegible, gbc_btnPartiallyIllegible)
            val btnNewButton = JButton("No Locality Data")
            btnNewButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbLocality.setText(Verbatim.NO_LOCALITY_DATA)
                }
            })
            val gbc_btnNewButton = GridBagConstraints()
            gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST
            gbc_btnNewButton.insets = Insets(0, 0, 5, 5)
            gbc_btnNewButton.gridx = 1
            gbc_btnNewButton.gridy = 0
            panel_1.add(btnNewButton, gbc_btnNewButton)
            btnNewButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {}
            })
            val btnNewButton_1 = JButton("Entirely Illegible")
            btnNewButton_1.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.ENTIRELY_ILLEGIBLE)
                }
            })
            val gbc_btnNewButton_1 = GridBagConstraints()
            gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL
            gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH
            gbc_btnNewButton_1.insets = Insets(0, 0, 5, 5)
            gbc_btnNewButton_1.gridx = 0
            gbc_btnNewButton_1.gridy = 1
            panel_1.add(btnNewButton_1, gbc_btnNewButton_1)
            val btnNoDateData = JButton("No Date Data")
            btnNoDateData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbDate.setText("[No date data]")
                }
            })
            val gbc_btnNoDateData = GridBagConstraints()
            gbc_btnNoDateData.fill = GridBagConstraints.HORIZONTAL
            gbc_btnNoDateData.insets = Insets(0, 0, 5, 5)
            gbc_btnNoDateData.gridx = 1
            gbc_btnNoDateData.gridy = 1
            panel_1.add(btnNoDateData, gbc_btnNoDateData)
            val btnLabelTruncatedIn = JButton("Label Truncated in Image")
            btnLabelTruncatedIn.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.TRUNCATED_BY_IMAGE)
                }
            })
            val gbc_btnLabelTruncatedIn = GridBagConstraints()
            gbc_btnLabelTruncatedIn.insets = Insets(0, 0, 5, 5)
            gbc_btnLabelTruncatedIn.anchor = GridBagConstraints.NORTHWEST
            gbc_btnLabelTruncatedIn.gridx = 0
            gbc_btnLabelTruncatedIn.gridy = 2
            panel_1.add(btnLabelTruncatedIn, gbc_btnLabelTruncatedIn)
            val btnNoCollectorData = JButton("No Collector Data")
            btnNoCollectorData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    textFieldVerbCollector.setText("[No collector data]")
                }
            })
            val gbc_btnNoCollectorData = GridBagConstraints()
            gbc_btnNoCollectorData.insets = Insets(0, 0, 5, 5)
            gbc_btnNoCollectorData.gridx = 1
            gbc_btnNoCollectorData.gridy = 2
            panel_1.add(btnNoCollectorData, gbc_btnNoCollectorData)
            val btnNoPinLabels = JButton("No Pin Labels")
            btnNoPinLabels.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    appendToQuestions(Verbatim.NO_PIN_LABELS)
                }
            })
            val gbc_btnNoPinLabels = GridBagConstraints()
            gbc_btnNoPinLabels.insets = Insets(0, 0, 0, 5)
            gbc_btnNoPinLabels.gridx = 0
            gbc_btnNoPinLabels.gridy = 3
            panel_1.add(btnNoPinLabels, gbc_btnNoPinLabels)
        }
        run {
            val panel = JPanel()
            contentPanel.add(panel, BorderLayout.CENTER)
            panel.setLayout(BorderLayout(0, 0))
            panel.add(imagePanePinLabels)
        }
        run {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.LEFT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            btnPrevious = JButton("Previous")
            btnPrevious.setEnabled(false)
            if (specimenController != null && specimenController.isInTable()) {
                btnPrevious.setEnabled(true)
            }
            btnPrevious.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    save()
                    if (specimenController.switchToPreviousSpecimenInTable()) {
                        specimen = specimenController.getSpecimen()
                        setValues()
                    }
                }
            })
            buttonPane.add(btnPrevious)
            btnNext = JButton("Next")
            btnNext.setEnabled(false)
            if (specimenController != null && specimenController.isInTable()) {
                btnNext.setEnabled(true)
            }
            btnNext.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    save()
                    if (specimenController.switchToNextSpecimenInTable()) {
                        specimen = specimenController.getSpecimen()
                        setValues()
                    }
                }
            })
            buttonPane.add(btnNext)
            {
                val okButton = JButton("OK")
                okButton.setActionCommand("OK")
                okButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (save()) {
                            setVisible(false)
                        }
                    }
                })
                buttonPane.add(okButton)
                getRootPane().setDefaultButton(okButton)
            }
            {
                val cancelButton = JButton("Cancel")
                cancelButton.setActionCommand("Cancel")
                cancelButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
                buttonPane.add(cancelButton)
            }
        }
    }

    fun fitPinLabels() {
        imagePanelPinLabels.zoomToFit()
    }

    private val imagePanePinLabels: ImageZoomPanel?
        private get() {
            if (imagePanelPinLabels == null) {
                imagePanelPinLabels = ImageZoomPanel()
            }
            return imagePanelPinLabels
        }

    fun setPinLabelImage(anImage: Image?) {
        imagePanelPinLabels.setImage(anImage as BufferedImage?)
        imagePanelPinLabels.zoomToFit()
        this.pack()
        if (imagePanelPinLabels.getPreferredSize().height > 500 || imagePanelPinLabels.getPreferredSize().width > 500) {
            imagePanelPinLabels.setPreferredSize(Dimension(500, 500))
        }
        imagePanelPinLabels.setMaximumSize(Dimension(500, 500))
    }

    protected fun save(): Boolean {
        var result = false
        try {
            specimen.setVerbatimLocality(textFieldVerbLocality.getText())
            specimen.setDateNos(textFieldVerbDate.getText())
            specimen.setVerbatimCollector(textFieldVerbCollector.getText())
            specimen.setVerbatimCollection(textFieldVerbCollection.getText())
            specimen.setVerbatimNumbers(textFieldVerbNumbers.getText())
            specimen.setVerbatimUnclassifiedText(textFieldVerbUnclassifiedText.getText())
            val questions = StringBuffer()
            questions.append(specimen.getQuestions())
            if (textFieldQuestions.getText() != null && textFieldQuestions.getText().trim({ it <= ' ' }).length > 0) {
                if (!questions.toString().contains(textFieldQuestions.getText())) {
                    questions.append(Verbatim.SEPARATOR).append(textFieldQuestions.getText())
                }
            }
            val workflowstatus = (comboBoxWorkflowStatus.getSelectedItem() as String)
            specimen.setWorkFlowStatus(workflowstatus)
            specimen.setQuestions(questions.toString())
            specimenController.save()
            result = true
        } catch (e: SaveFailedException) {
            log.error(e.message, e)
            // TODO: Notify user
        }
        return result
    }

    protected fun appendToQuestions(newQuestion: String) {
        if (!textFieldQuestions.getText().contains(newQuestion)) {
            val questions = StringBuffer()
            questions.append(textFieldQuestions.getText()).append(Verbatim.SEPARATOR).append(newQuestion)
            textFieldQuestions.setText(questions.toString())
        }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#notifyDataHasChanged()
     */
    override fun notifyDataHasChanged() { // TODO Auto-generated method stub
    }

    companion object {
        private const val serialVersionUID = 4462958599102371519L
        private val log: Log? = LogFactory.getLog(VerbatimCaptureDialog::class.java)
    }
}