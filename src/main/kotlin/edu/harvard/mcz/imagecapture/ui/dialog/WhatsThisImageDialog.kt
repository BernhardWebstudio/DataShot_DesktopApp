/**
 * WhatsThisImageDialog.java
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

org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactory
import java.awt.Frameimport

java.awt.Insetsimport java.awt.event.ActionEventimport java.awt.event.KeyAdapterimport java.awt.event.KeyEventimport java.io.Fileimport java.net.URL

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * WhatsThisImageDialog is a dialog to allow users to identify the
 * nature (specimen, drawer) of images for which barcode detection and
 * OCR isn't able to detect the nature of the image.
 */
class WhatsThisImageDialog : JDialog {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var imagePanel: ImageZoomPanel? = null
    private var jLabel: JLabel? = null
    private var jLabel1: JLabel? = null
    private var jLabel2: JLabel? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jLabel3: JLabel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jButton: JButton? = null
    private var jPanel2: JPanel? = null
    private var jLabel4: JLabel? = null
    private var jComboBox: JComboBox<*>? = null
    private var thisDialog: WhatsThisImageDialog? = null

    /**
     * Default constructor, probably not the one to use,
     * as image needs to be provided.
     *
     * @param owner the parent frame for this dialog.
     */
    constructor(owner: Frame?) : super(owner, true) {
        thisDialog = this
        initialize()
    }

    /**
     * Constructor with the image to display as a parameter.
     *
     * @param owner       the parent frame for this dialog.
     * @param imageToShow the image of unknown nature to display.
     */
    constructor(owner: Frame?, imageToShow: BufferedImage?) : super(owner, true) {
        thisDialog = this
        initialize()
        setImage(imageToShow)
    }

    /**
     * Constructor with an image File to display as a parameter.  Will
     * display a broken image icon if File can't be read as an
     * image.
     *
     * @param owner           the parent frame for this dialog.
     * @param imageFileToShow the image file of unknown nature to display.
     */
    constructor(owner: Frame?, imageFileToShow: File) : super(owner, true) {
        thisDialog = this
        initialize()
        val image: BufferedImage
        try {
            image = ImageIO.read(imageFileToShow)
            setImage(image)
        } catch (e: IOException) {
            log.error("Unable to open selected image " + imageFileToShow.getName())
            log.debug(e)
            val errorFilename: URL? = this.javaClass.getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/unableToLoadImage.jpg")
            try {
                setImage(ImageIO.read(errorFilename))
            } catch (e1: IOException) {
                log.error("Unable to open resource image")
                log.error(e1)
            }
        }
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setSize(755, 357)
        this.setTitle("What is this image")
        this.setContentPane(getJContentPane())
    }

    val barcode: String?
        get() = jTextFieldBarcode.getText()

    val drawerNumber: String?
        get() = jTextFieldDrawerNumber.getText()

    fun setImage(anImage: BufferedImage?) {
        imagePanel.setImage(anImage)
    }

    val isSpecimen: Boolean
        get() {
            var result = false
            if (jComboBox.getSelectedItem() == SEL_SPECIMEN &&
                    Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                            jTextFieldBarcode.getText())) {
                result = true
            }
            return result
        }

    val isDrawerImage: Boolean
        get() {
            var result = false
            if (jComboBox.getSelectedItem() == SEL_DRAWER &&
                    jTextFieldDrawerNumber.getText().matches(
                            Singleton.Companion.getSingletonInstance()
                                    .getProperties()
                                    .getProperties()
                                    .getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                result = true
            }
            return result
        }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel? {
        if (jContentPane == null) {
            jContentPane = JPanel()
            jContentPane.setLayout(BorderLayout())
            jContentPane.add(getJPanel(), BorderLayout.WEST)
            jContentPane.add(getImagePanel(), BorderLayout.CENTER)
        }
        return jContentPane
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.fill = GridBagConstraints.BOTH
            gridBagConstraints9.gridy = 4
            gridBagConstraints9.weightx = 1.0
            gridBagConstraints9.anchor = GridBagConstraints.WEST
            gridBagConstraints9.gridx = 1
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.gridx = 0
            gridBagConstraints8.anchor = GridBagConstraints.EAST
            gridBagConstraints8.gridy = 4
            jLabel4 = JLabel()
            jLabel4.setText("Image Of:")
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 1
            gridBagConstraints7.weighty = 1.0
            gridBagConstraints7.gridy = 6
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 1
            gridBagConstraints6.gridy = 5
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 3
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.anchor = GridBagConstraints.WEST
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.gridx = 0
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.weighty = 0.0
            gridBagConstraints4.gridy = 3
            jLabel3 = JLabel()
            jLabel3.setText("DrawerNumber:")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.gridwidth = 2
            gridBagConstraints3.weighty = 0.0
            gridBagConstraints3.anchor = GridBagConstraints.NORTH
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.fill = GridBagConstraints.BOTH
            gridBagConstraints2.gridy = 2
            gridBagConstraints2.weightx = 1.0
            gridBagConstraints2.anchor = GridBagConstraints.WEST
            gridBagConstraints2.gridx = 1
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridx = 0
            gridBagConstraints1.anchor = GridBagConstraints.EAST
            gridBagConstraints1.weighty = 0.0
            gridBagConstraints1.gridy = 2
            jLabel2 = JLabel()
            jLabel2.setText("Barcode:")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.insets = Insets(0, 0, 5, 0)
            gridBagConstraints.weighty = 0.0
            gridBagConstraints.gridy = 1
            jLabel1 = JLabel()
            jLabel1.setText("Please Identify this Image.")
            jLabel = JLabel()
            jLabel.setText("No Barcode or drawer number found.")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, gridBagConstraints3)
            jPanel.add(jLabel1, gridBagConstraints)
            jPanel.add(jLabel2, gridBagConstraints1)
            jPanel.add(getJTextFieldBarcode(), gridBagConstraints2)
            jPanel.add(jLabel3, gridBagConstraints4)
            jPanel.add(getJTextFieldDrawerNumber(), gridBagConstraints5)
            jPanel.add(getJButton(), gridBagConstraints6)
            jPanel.add(getJPanel2(), gridBagConstraints7)
            jPanel.add(jLabel4, gridBagConstraints8)
            jPanel.add(getJComboBox(), gridBagConstraints9)
        }
        return jPanel
    }

    /**
     * This method initializes imagePanel
     *
     * @return javax.swing.JPanel
     */
    private fun getImagePanel(): ImageZoomPanel? {
        if (imagePanel == null) {
            imagePanel = ImageZoomPanel()
        }
        return imagePanel
    }

    /**
     * This method initializes jTextFieldBarcode
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldBarcode(): JTextField? {
        if (jTextFieldBarcode == null) {
            jTextFieldBarcode = JTextField()
            jTextFieldBarcode.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    checkValues()
                }
            })
        }
        return jTextFieldBarcode
    }

    /**
     * This method initializes jTextFieldDrawerNumber
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDrawerNumber(): JTextField? {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = JTextField()
            jTextFieldDrawerNumber.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    checkValues()
                }
            })
        }
        return jTextFieldDrawerNumber
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Continue")
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // test for required values based on the selection on the combo box
                    var okToClose = false
                    if (jComboBox.getSelectedItem() == SEL_SPECIMEN) {
                        if (Singleton.Companion.getSingletonInstance()
                                        .getBarcodeMatcher()
                                        .matchesPattern(jTextFieldBarcode.getText())) {
                            okToClose = true
                        } else {
                            jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                        }
                    }
                    if (jComboBox.getSelectedItem() == SEL_DRAWER) {
                        if (jTextFieldDrawerNumber.getText().matches(
                                        Singleton.Companion.getSingletonInstance()
                                                .getProperties()
                                                .getProperties()
                                                .getProperty(
                                                        ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                            okToClose = true
                        } else {
                            jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                        }
                    }
                    if (jComboBox.getSelectedItem() == SEL_DRAWER) {
                        okToClose = true
                    }
                    // Only close if set of values makes sense.
                    if (okToClose) {
                        thisDialog.setVisible(false)
                    }
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel2(): JPanel? {
        if (jPanel2 == null) {
            jPanel2 = JPanel()
            jPanel2.setLayout(GridBagLayout())
        }
        return jPanel2
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBox(): JComboBox<*>? {
        if (jComboBox == null) {
            jComboBox = JComboBox<Any?>()
            jComboBox.addItem("")
            jComboBox.addItem(SEL_SPECIMEN)
            jComboBox.addItem(SEL_DRAWER)
            jComboBox.addItem(SEL_OTHER)
            jComboBox.setSelectedItem("")
        }
        return jComboBox
    }

    private fun checkValues() {
        if (jComboBox.getSelectedItem() == SEL_SPECIMEN) {
            if (Singleton.Companion.getSingletonInstance().getBarcodeMatcher().matchesPattern(
                            jTextFieldBarcode.getText())) {
                jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            } else {
                jTextFieldBarcode.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
            }
        }
        if (jComboBox.getSelectedItem() == SEL_DRAWER) {
            if (jTextFieldDrawerNumber.getText().matches(
                            Singleton.Companion.getSingletonInstance()
                                    .getProperties()
                                    .getProperties()
                                    .getProperty(
                                            ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            } else {
                jTextFieldDrawerNumber.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
            }
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private val SEL_SPECIMEN: String? = "Specimen"
        private val SEL_DRAWER: String? = "Paper in Drawer"
        private val SEL_OTHER: String? = "Other"
        private val log: Log? = LogFactory.getLog(WhatsThisImageDialog::class.java) //  @jve:decl-index=0:
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
