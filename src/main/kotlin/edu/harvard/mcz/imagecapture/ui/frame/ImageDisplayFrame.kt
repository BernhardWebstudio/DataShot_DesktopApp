/**
 * ImageDisplayFrame.java
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
package edu.harvard.mcz.imagecapture.ui.frameimportimport

org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport java.awt.*import java.awt.event.ActionEventimport

java.awt.event.ComponentAdapter
import java.io.Fileimport

java.lang.Exceptionimport java.net.URLimport javax.swing.border.Border

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * Display parts of images (and possibly a data entry form) of a specimen and its labels.
 */
class ImageDisplayFrame() : JFrame() {
    // Preferences to save the window location etc.
    var prefs: Preferences? = null
    // The displayed image read from a file.
// Set of image files managed by loading one at a time and
// displaying its components in a set of tabs.
    var imagefile: BufferedImage? = null //  @jve:decl-index=0:
    private var jContentPane: JPanel? = null
    private var jTabbedPane: JTabbedPane? = null // Tab pane to display image components
    // The specimen
    private var jPanelSpecimen: JPanel? = null
    private var jScrollPane: JScrollPane? = null
    private var imagePanelSpecimen: ImageZoomPanel? = null
    // the barcode
    private var imagePanelBarcode: JLabel? = null // The barcode label.
    private var jPanel: JPanel? = null
    private var jTextBarcode: JTextField? = null // Text read from the barcode.
    // pin labels
    private var jPanelLabels: JPanel? = null
    private var imagePanelPinLabels: ImageZoomPanel? = null // the specimen labels from the pin.
    // unit tray taxon name header label
    private var jPanelUnitTray: JPanel? = null //  @jve:decl-index=0:visual-constraint="574,225"
    private var imagePanelUnitTrayTaxon: ImageZoomPanel? = null // The current determination new unit tray label for OCR.
    private var jTextFieldRawOCR: JTextField? = null
    // old unit tray labels
    private var jPanelUnitTrayLabels: JPanel? = null //  @jve:decl-index=0:visual-constraint="574,225"
    private var imagePanelTrayLabels: ImageZoomPanel? = null // small labels present in the unit tray.
    // full image
    private var imagePaneFullImage: ImageZoomPanel? = null
    private var jPanelImagePicker: JPanel? = null
    private var jLabelImageCountTxt: JLabel? = null
    private var jComboBoxImagePicker: JComboBox<*>? = null
    private var jLabelImageCountNr: JLabel? = null
    private var jPanelImagesPanel: JPanel? = null
    private var targetSpecimen: Specimen? = null
    private var targetSpecimenController: SpecimenController? = null
    private var selectedImage: ICImage? = null
    private var templatePicker: JButton? = null
    private var btnVerbatimtranscription: JButton? = null

    /**
     * This is the default constructor
     *
     * @param specimen
     */
    constructor(specimen: Specimen?, specimenController: SpecimenController?) : this() {
        targetSpecimen = specimen
        targetSpecimenController = specimenController
        //this.center();
    }

    fun setTargetSpecimen(targetSpecimen: Specimen?) { // TODO: remove setter necessity, fetch via SpecimenController
        this.targetSpecimen = targetSpecimen
    }

    /**
     * Given a set of ICImages, display one of them in the tabs of the ImageDisplayFrame, and
     * populate the image chooser pick list with a list of all the images.  Call this method to display
     * more than one image in an ImageDisplayFrame.  Single image is displayed with a call to loadImagesFromFileSingle().
     *
     * @param imageFiles the image files to display in the tabs of the frame.
     * @throws ImageLoadException   if there is a problem with the image.
     * @throws BadTemplateException
     */
    fun loadImagesFromFiles(imageFiles: MutableSet<ICImage?>) {
        log.debug(imageFiles.size)
        jComboBoxImagePicker.removeAllItems()
        val i: MutableIterator<ICImage?> = imageFiles.iterator()
        var image: ICImage? = null
        val fileCount = imageFiles.size
        while (i.hasNext()) {
            image = i.next()
            jComboBoxImagePicker.addItem(image.getFilename())
            log.debug("Adding image to picklist: " + image.getPath() + image.getFilename())
        }
        //TODO: stored path may need separator conversion for different systems.
//String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
        var path: String = image.getPath()
        if (path == null) {
            path = ""
        }
        //File fileToCheck = new File(startPointName + path + image.getFilename());
        val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
        jLabelImageCountNr.setText("($fileCount)")
        jLabelImageCountNr.setForeground(if (fileCount > 1) Color.RED else Color.BLACK)
        jComboBoxImagePicker.setEnabled(fileCount > 1)
        jComboBoxImagePicker.setSelectedItem(image.getFilename())
        try {
            val defaultTemplate: PositionTemplate = PositionTemplate.Companion.findTemplateForImage(image)
            loadImagesFromFileSingle(fileToCheck, defaultTemplate, image)
        } catch (e: BadTemplateException) {
            log.error(e)
        } catch (e: ImageLoadException) {
            log.error(e)
        }
        jTabbedPane.setSelectedIndex(0) // move focus to full image tab
    }

    /**
     * Based on the position template, display the full image in one tab, and parts of the image
     * described by the template in other tabs.
     *
     * @param anImageFile
     * @param defaultTemplate
     * @throws ImageLoadException
     * @throws BadTemplateException
     */
    @Throws(ImageLoadException::class, BadTemplateException::class)
    fun loadImagesFromFileSingle(anImageFile: File, defaultTemplate: PositionTemplate, image: ICImage?) {
        log.debug(anImageFile.getName())
        var templateProblem = false
        selectedImage = image
        //TODO: template detection?
        try {
            imagefile = ImageIO.read(anImageFile)
            log.debug(anImageFile.getPath())
            // Show the component parts of the image as defined by the position template.
            if (defaultTemplate.getTemplateId() == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) { // clear component parts
                val url: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/gnome-mime-image.png")
                val anImage: BufferedImage = ImageIO.read(url)
                setBarcodeImage(anImage)
                setSpecimenImage(anImage)
                setPinLabelImage(anImage)
                setUnitTrayImage(anImage)
                setUnitTrayLabelsImage(anImage)
            } else {
                if (imagefile.getHeight() != defaultTemplate.getImageSize().height || imagefile.getWidth() != defaultTemplate.getImageSize().width) { // TODO: template strategy
                    throw BadTemplateException("Template size doesn't match image size. " + defaultTemplate.getName())
                }
                try {
                    val x: Int = defaultTemplate.getBarcodeULPosition().width
                    val y: Int = defaultTemplate.getBarcodeULPosition().height
                    val w: Int = defaultTemplate.getBarcodeSize().width
                    val h: Int = defaultTemplate.getBarcodeSize().height
                    setBarcodeImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    try {
                        setBarcodeImage(imagefile)
                    } catch (ex2: Exception) {
                        println(ex.message)
                        throw ImageLoadException("Unable to load images from " + anImageFile.getPath() + " " + ex2.message)
                    }
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getSpecimenPosition().width
                    val y: Int = defaultTemplate.getSpecimenPosition().height
                    val w: Int = defaultTemplate.getSpecimenSize().width
                    val h: Int = defaultTemplate.getSpecimenSize().height
                    val img: BufferedImage = imagefile.getSubimage(x, y, w, h)
                    setSpecimenImage(img)
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getLabelPosition().width
                    val y: Int = defaultTemplate.getLabelPosition().height
                    val w: Int = defaultTemplate.getLabelSize().width
                    val h: Int = defaultTemplate.getLabelSize().height
                    setPinLabelImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getTextPosition().width
                    val y: Int = defaultTemplate.getTextPosition().height
                    val w: Int = defaultTemplate.getTextSize().width
                    val h: Int = defaultTemplate.getTextSize().height
                    setUnitTrayImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
                try {
                    val x: Int = defaultTemplate.getUTLabelsPosition().width
                    val y: Int = defaultTemplate.getUTLabelsPosition().height
                    val w: Int = defaultTemplate.getUTLabelsSize().width
                    val h: Int = defaultTemplate.getUTLabelsSize().height
                    setUnitTrayLabelsImage(imagefile.getSubimage(x, y, w, h))
                } catch (ex: Exception) {
                    templateProblem = true
                    println(ex.message)
                }
            }
            // Display the full image (also packs!)
            setFullImage()
        } catch (e1: IOException) {
            log.debug("IOException!")
            println("Error reading image file: " + e1.message)
            throw ImageLoadException("Unable to read image file " + anImageFile.getPath() + " " + e1.message)
        } catch (e: Exception) {
            log.debug("Image loading exception")
            e.printStackTrace()
        }
        if (templateProblem) {
            throw BadTemplateException("Template doesn't fit file " + anImageFile.getPath())
        }
        log.debug(anImageFile.getPath())
        if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.Companion.getSingletonInstance().getUser().getUserid())) {
            updateTemplateList()
        }
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setContentPane(getJContentPane())
        this.setTitle("Image File and Barcode Value")
        setWindowLocationSize()
        val self = this
        this.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                super.componentResized(e)
                self.saveWindowSize()
            }

            override fun componentMoved(e: ComponentEvent?) {
                super.componentMoved(e)
                self.saveWindowLocation()
            }
        })
    }

    private fun setWindowLocationSize() {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        // set size of window
        val sizeDimensionWidth: Int = preferences.getInt("SizeDimensionWidth", Math.min(1600, screenSize.width))
        val sizeDimensionHeight: Int = prefs.getInt("SizeDimensionHeight", Math.min(1250, screenSize.height))
        log.debug("Setting width = $sizeDimensionWidth, height = $sizeDimensionHeight")
        this.setPreferredSize(Dimension(sizeDimensionWidth, sizeDimensionHeight))
        // set location of window
        val locationX: Int = prefs.getInt("LocationX", (screenSize.width - this.getWidth()) / 2)
        val locationY: Int = prefs.getInt("LocationY", (screenSize.height - this.getHeight()) / 2)
        this.setLocation(locationX, locationY)
    }

    private fun saveWindowLocation() {
        prefs.putInt("LocationX", this.getLocation().x)
        prefs.putInt("LocationY", this.getLocation().y)
    }

    private fun saveWindowSize() {
        prefs.putInt("SizeDimensionWidth", this.getWidth())
        prefs.putInt("SizeDimensionHeight", this.getHeight())
        log.debug("Stored width = " + this.getWidth() + ", height = " + this.getHeight())
        try {
            prefs.sync()
        } catch (e: BackingStoreException) {
            log.error(e)
        } catch (e: IllegalStateException) {
            log.error(e)
        }
    }

    private val preferences: Preferences?
        private get() {
            if (prefs == null) {
                prefs = Preferences.userRoot().node(this.javaClass.name)
            }
            return prefs
        }

    fun center() {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel? {
        if (jContentPane == null) {
            jContentPane = JPanel()
            jContentPane.setLayout(MigLayout("wrap 2, fill", "[grow]", "[grow]"))
            jContentPane.add(getJPanelImagesPanel(), "grow")
        }
        return jContentPane
    }

    fun setActiveTab(tab: Int) {
        try {
            jTabbedPane.setSelectedIndex(tab)
        } catch (e: IndexOutOfBoundsException) {
            println("Failed to activate tab. " + e.message)
        }
    }

    fun setWest(westPanel: JPanel?) {
        jContentPane.removeAll()
        jContentPane.setLayout(MigLayout("wrap 2, fill, insets 0", "[grow]", "[grow]"))
        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        splitPane.setLeftComponent(westPanel)
        splitPane.setRightComponent(getJPanelImagesPanel())
        jContentPane.add(splitPane, "push, grow")
        setWindowLocationSize()
        this.pack()
        // remove borders
        splitPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
        val flatDividerSplitPaneUI: BasicSplitPaneUI = object : BasicSplitPaneUI() {
            override fun createDefaultDivider(): BasicSplitPaneDivider {
                return object : BasicSplitPaneDivider(this) {
                    override fun setBorder(b: Border?) {}
                }
            }
        }
        splitPane.setUI(flatDividerSplitPaneUI)
        splitPane.setBorder(null)
        // set the sizes of the two panes
        splitPane.setDividerLocation(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_EDITOR_IMPORTANCE, "0.6").toDouble())
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private val jPanelBarcode: JPanel?
        private get() {
            if (jPanel == null) {
                jPanel = JPanel()
                jPanel.setLayout(BorderLayout())
                jTextBarcode = JTextField()
                jTextBarcode.setText("Barcode")
                jPanel.add(getJScrollPane(), BorderLayout.CENTER)
                jPanel.add(jTextBarcode, BorderLayout.SOUTH)
            }
            return jPanel
        }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelSpecimen(): JPanel? {
        if (jPanelSpecimen == null) {
            jPanelSpecimen = JPanel()
            jPanelSpecimen.setLayout(BorderLayout())
            jPanelSpecimen.add(jLabelSpecimen, BorderLayout.CENTER)
        }
        return jPanelSpecimen
    }

    fun setSpecimenImage(anImage: BufferedImage?) {
        imagePanelSpecimen!!.setImage(anImage)
        imagePanelSpecimen!!.zoomToFit()
        if (imagePanelSpecimen.getPreferredSize().height > 500 || imagePanelSpecimen.getPreferredSize().width > 500) {
            imagePanelSpecimen.setPreferredSize(Dimension(1000, 900))
        }
    }

    /**
     * Fit the image in the PinLabels tab to the size of the imagePanel it is shown in.
     */
    fun fitPinLabels() {
        imagePanelPinLabels!!.zoomToFit()
    }

    /**
     * Center the specimen image in the imagePanel it is shown in.
     */
    fun centerSpecimen() {
        imagePanelSpecimen!!.center()
    }

    fun setUnitTrayImage(anImage: Image?) {
        imagePanelUnitTrayTaxon!!.setImage(anImage as BufferedImage?)
        imagePanelUnitTrayTaxon!!.zoomToFit()
        //		jLabelUnitTray.setIcon(new ImageIcon(anImage));
//		this.pack();
        if (imagePanelUnitTrayTaxon.getPreferredSize().height > 500 || imagePanelUnitTrayTaxon.getPreferredSize().width > 500) {
            imagePanelUnitTrayTaxon.setPreferredSize(Dimension(500, 500))
        }
        //		jLabelUnitTray.setMaximumSize(new Dimension(500,500));
    }

    fun setUnitTrayLabelsImage(anImage: Image?) {
        imagePanelTrayLabels!!.setImage(anImage as BufferedImage?)
        imagePanelTrayLabels!!.zoomToFit()
        if (imagePanelTrayLabels.getPreferredSize().height > 500 || imagePanelTrayLabels.getPreferredSize().width > 500) {
            imagePanelTrayLabels.setPreferredSize(Dimension(500, 500))
        }
    }

    fun setPinLabelImage(anImage: Image?) {
        imagePanelPinLabels!!.setImage(anImage as BufferedImage?)
        imagePanelPinLabels!!.zoomToFit()
        this.pack()
        if (imagePanelPinLabels.getPreferredSize().height > 500 || imagePanelPinLabels.getPreferredSize().width > 500) {
            imagePanelPinLabels.setPreferredSize(Dimension(500, 500))
        }
        imagePanelPinLabels.setMaximumSize(Dimension(500, 500))
    }

    fun setBarcodeImage(anImage: Image) {
        imagePanelBarcode.setIcon(ImageIcon(anImage))
        this.pack()
        if (imagePanelBarcode.getPreferredSize().height > 500 || imagePanelBarcode.getPreferredSize().width > 500) {
            imagePanelBarcode.setPreferredSize(Dimension(500, 500))
        }
        imagePanelBarcode.setMaximumSize(Dimension(500, 500))
    }

    fun setBarcode(someText: String?) {
        jTextBarcode.setText(someText)
        jTextBarcode.setEditable(false)
        jTextBarcode.setEnabled(true)
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            imagePanelBarcode = JLabel()
            imagePanelBarcode.setText("")
            jScrollPane = JScrollPane()
            jScrollPane.setViewportView(imagePanelBarcode)
        }
        return jScrollPane
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private val jLabelSpecimen: edu.harvard.mcz.imagecapture.ui.frameimportimport.ImageZoomPanel?
        private get() {
            if (imagePanelSpecimen == null) {
                imagePanelSpecimen = ImageZoomPanel()
            }
            return imagePanelSpecimen
        }

    private fun getJPanelLabels(): JPanel? {
        if (jPanelLabels == null) {
            jPanelLabels = JPanel()
            jPanelLabels.setLayout(BorderLayout())
            jPanelLabels.add(imagePanePinLabels, BorderLayout.CENTER)
        }
        return jPanelLabels
    }

    private val imagePanePinLabels: edu.harvard.mcz.imagecapture.ui.frameimportimport.ImageZoomPanel?
        private get() {
            if (imagePanelPinLabels == null) {
                imagePanelPinLabels = ImageZoomPanel()
            }
            return imagePanelPinLabels
        }

    /**
     * This method initializes jTabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private fun getJTabbedPane(): JTabbedPane? {
        if (jTabbedPane == null) {
            jTabbedPane = JTabbedPane()
            jTabbedPane.insertTab("FullImage", null, panelFullImage, null, TAB_FULLIMAGE)
            jTabbedPane.insertTab("Specimen", null, getJPanelSpecimen(), null, TAB_SPECIMEN)
            jTabbedPane.insertTab("PinLabels", null, getJPanelLabels(), null, TAB_LABELS)
            jTabbedPane.insertTab("UnitTray Labels", null, jPanelUTL, null, TAB_UNITTRAYLABELS)
            jTabbedPane.insertTab("Taxon Label", null, jPanelUnitTrayTaxon, null, TAB_UNITTRAY)
            jTabbedPane.insertTab("Barcode", null, jPanelBarcode, null, TAB_BARCODE)
        }
        return jTabbedPane
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private val jPanelUnitTrayTaxon: JPanel?
        private get() {
            if (jPanelUnitTray == null) {
                jPanelUnitTray = JPanel()
                jPanelUnitTray.setLayout(BorderLayout())
                jPanelUnitTray.setSize(Dimension(108, 72))
                jPanelUnitTray.add(jTextField, BorderLayout.SOUTH)
                jPanelUnitTray.add(getImagePanelUnitTrayTaxon(), BorderLayout.CENTER)
            }
            return jPanelUnitTray
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getImagePanelUnitTrayTaxon(): ImageZoomPanel? {
        if (imagePanelUnitTrayTaxon == null) {
            imagePanelUnitTrayTaxon = ImageZoomPanel()
        }
        return imagePanelUnitTrayTaxon
    }

    private fun setFullImage() {
        if (imagefile != null) {
            panelFullImage!!.setImage(imagefile)
            // We need to make sure the container hierarchy holding the image knows what
// size the image pane is before zoom to fit will work.
            this.pack()
            imagePaneFullImage!!.zoomToFit()
        }
    }

    private val panelFullImage: edu.harvard.mcz.imagecapture.ui.frameimportimport.ImageZoomPanel?
        private get() {
            if (imagePaneFullImage == null) {
                if (imagefile == null) {
                    imagePaneFullImage = ImageZoomPanel()
                } else {
                    imagePaneFullImage = ImageZoomPanel(imagefile)
                }
            }
            return imagePaneFullImage
        }

    /**
     * This method initializes jPanelUTL
     *
     * @return javax.swing.JPanel
     */
    private val jPanelUTL: JPanel?
        private get() {
            if (jPanelUnitTrayLabels == null) {
                jPanelUnitTrayLabels = JPanel()
                jPanelUnitTrayLabels.setLayout(BorderLayout())
                jPanelUnitTrayLabels.setSize(Dimension(108, 72))
                jPanelUnitTrayLabels.add(getImagePanelTrayLabels(), BorderLayout.CENTER)
            }
            return jPanelUnitTrayLabels
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getImagePanelTrayLabels(): ImageZoomPanel? {
        if (imagePanelTrayLabels == null) {
            imagePanelTrayLabels = ImageZoomPanel()
        }
        return imagePanelTrayLabels
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField: JTextField?
        private get() {
            if (jTextFieldRawOCR == null) {
                jTextFieldRawOCR = JTextField()
                jTextFieldRawOCR.setEditable(false)
            }
            return jTextFieldRawOCR
        }

    fun setRawOCRLabel(text: String?) {
        jTextFieldRawOCR.setText(text)
    }

    /**
     * This method initializes jPanelImagePicker
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelImagePicker(): JPanel? {
        if (jPanelImagePicker == null) {
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.insets = Insets(0, 0, 5, 5)
            gridBagConstraints1.gridx = 2
            gridBagConstraints1.gridy = 0
            jLabelImageCountNr = JLabel()
            jLabelImageCountNr.setText("(0)")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.insets = Insets(0, 0, 5, 0)
            gridBagConstraints.fill = GridBagConstraints.BOTH
            gridBagConstraints.gridy = 0
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.gridx = 3
            jLabelImageCountTxt = JLabel()
            jLabelImageCountTxt.setText("Images: ")
            jPanelImagePicker = JPanel()
            val gbl_jPanelImagePicker = GridBagLayout()
            gbl_jPanelImagePicker.columnWeights = doubleArrayOf(0.0, 0.0, 0.0, 1.0)
            jPanelImagePicker.setLayout(gbl_jPanelImagePicker)
            val gbc_btnVerbatimtranscription = GridBagConstraints()
            gbc_btnVerbatimtranscription.insets = Insets(0, 0, 5, 5)
            gbc_btnVerbatimtranscription.gridx = 0
            gbc_btnVerbatimtranscription.gridy = 0
            jPanelImagePicker.add(getBtnVerbatimtranscription(), gbc_btnVerbatimtranscription)
            val gbc_jLabel = GridBagConstraints()
            gbc_jLabel.insets = Insets(0, 0, 5, 5)
            gbc_jLabel.gridx = 1
            gbc_jLabel.gridy = 0
            jPanelImagePicker.add(jLabelImageCountTxt, gbc_jLabel)
            jPanelImagePicker.add(getJComboBoxImagePicker(), gridBagConstraints)
            jPanelImagePicker.add(jLabelImageCountNr, gridBagConstraints1)
        }
        return jPanelImagePicker
    }

    /**
     * This method initializes jComboBoxImagePicker
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxImagePicker(): JComboBox<*>? {
        if (jComboBoxImagePicker == null) {
            jComboBoxImagePicker = JComboBox<Any?>()
            if (targetSpecimen != null) {
                val i: MutableIterator<ICImage?> = targetSpecimen.getICImages().iterator()
                while (i.hasNext()) {
                    val filename: String = i.next().getFilename()
                    jComboBoxImagePicker.addItem(filename)
                    log.debug(filename)
                }
            }
            jComboBoxImagePicker.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent) { // Intended to be fired when picklist item is selected, is
// being fired on other events as well.
                    log.debug(e.getActionCommand())
                    // If there is no selection, then we shouldn't be doing anything.
                    if (jComboBoxImagePicker.getSelectedItem() == null) {
                        log.debug("No selected item")
                    } else {
                        try {
                            val filename: String? = if (jComboBoxImagePicker.getSelectedItem() != null) jComboBoxImagePicker.getSelectedItem().toString() else null
                            if (filename != null && targetSpecimen != null) { // find matching images, set first one as the display image.
                                val images: MutableSet<ICImage?>?
                                if (targetSpecimen.getICImages() == null) {
                                    val ils = ICImageLifeCycle()
                                    images = HashSet<ICImage?>(ils.findBy(object : HashMap<String?, Any?>() {
                                        init {
                                            put("Filename", filename)
                                            put("SPECIMENID", targetSpecimen.getSpecimenId())
                                        }
                                    }))
                                } else {
                                    images = targetSpecimen.getICImages()
                                }
                                if (images != null && images.size > 0) {
                                    log.debug("Found images: " + images.size)
                                    val ii: MutableIterator<ICImage?> = images.iterator()
                                    var found = false
                                    while (ii.hasNext() && !found) {
                                        val image: ICImage? = ii.next()
                                        log.debug("image is $image")
                                        log.debug("image specimen is " + image.getSpecimen())
                                        log.debug("image path is " + image.getPath())
                                        log.debug("target specimen bar code is " + targetSpecimen.getBarcode())
                                        log.debug("image specimen barcode is " + image.getSpecimen().getBarcode())
                                        if (image.getFilename() != filename || image.getPath() == "" || image.getPath().toUpperCase().contains(".JPG") || image.getSpecimen() == null || image.getSpecimen().getBarcode() != targetSpecimen.getBarcode()) { // wrong path or filename
                                            log.debug("WrongFile: " + image.getPath())
                                        } else {
                                            found = true
                                            //String startPointName = Singleton.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.KEY_IMAGEBASE);
                                            var path: String = image.getPath()
                                            if (path == null) {
                                                path = ""
                                            }
                                            val fileToCheck = File(assemblePathWithBase(path, image.getFilename()))
                                            var defaultTemplate: PositionTemplate
                                            try {
                                                defaultTemplate = PositionTemplate.Companion.findTemplateForImage(image)
                                                loadImagesFromFileSingle(fileToCheck, defaultTemplate, image)
                                            } catch (ex: ImageLoadException) {
                                                log.error(ex)
                                            } catch (ex: BadTemplateException) {
                                                log.error(ex)
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (e2: NullPointerException) { // Probably means an empty jComboBoxImagePicker
                            e2.printStackTrace()
                            log.error(e2.message, e2)
                        }
                    }
                }
            })
        }
        return jComboBoxImagePicker
    }

    private fun getTemplatePicker(): JButton? {
        if (templatePicker == null) {
            templatePicker = JButton()
            var template = "No Template Selected"
            if (selectedImage != null) {
                template = selectedImage.getTemplateId()
            }
            templatePicker.setText(template)
            log.debug(template)
            log.debug(PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS)
            if (template == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
                templatePicker.setEnabled(true)
            } else {
                templatePicker.setEnabled(false)
            }
            templatePicker.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val dialog = TemplatePickerDialog(selectedImage)
                    dialog.setVisible(true)
                }
            })
        }
        return templatePicker
    }

    private fun updateTemplateList() {
        getTemplatePicker()
        var template = "No Template Selected"
        if (selectedImage != null) {
            template = selectedImage.getTemplateId()
        }
        templatePicker.setText(template)
        if (template == PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS) {
            templatePicker.setEnabled(true)
        } else {
            templatePicker.setEnabled(false)
        }
    }

    /**
     * This method initializes jPanelImagesPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelImagesPanel(): JPanel? {
        if (jPanelImagesPanel == null) {
            jPanelImagesPanel = JPanel()
            jPanelImagesPanel.setLayout(BorderLayout())
            jPanelImagesPanel.add(getJTabbedPane(), BorderLayout.CENTER)
            jPanelImagesPanel.add(getJPanelImagePicker(), BorderLayout.NORTH)
            if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.Companion.getSingletonInstance().getUser().getUserid())) {
                jPanelImagesPanel.add(getTemplatePicker(), BorderLayout.SOUTH)
            }
        }
        return jPanelImagesPanel
    }

    private fun getBtnVerbatimtranscription(): JButton? {
        if (btnVerbatimtranscription == null) {
            btnVerbatimtranscription = JButton("VerbatimTranscription")
            btnVerbatimtranscription.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // TODO Auto-generated method stub
                    val dialog = VerbatimCaptureDialog(targetSpecimen, targetSpecimenController)
                    dialog.setVisible(true)
                    setVisible(false)
                }
            })
            btnVerbatimtranscription.setEnabled(false)
            if (targetSpecimen != null && WorkFlowStatus.allowsVerbatimUpdate(targetSpecimen.getWorkFlowStatus())) {
                btnVerbatimtranscription.setEnabled(true)
            }
        }
        return btnVerbatimtranscription
    }

    companion object {
        // display order for tabs
        const val TAB_BARCODE = 5
        const val TAB_SPECIMEN = 1
        const val TAB_LABELS = 2
        const val TAB_UNITTRAY = 4
        const val TAB_UNITTRAYLABELS = 3
        const val TAB_FULLIMAGE = 0
        private const val serialVersionUID = 6208387412508034014L
        private val log: Log? = LogFactory.getLog(ImageDisplayFrame::class.java)
    }

    init {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        targetSpecimen = null
        initialize()
        prefs = Preferences.userRoot().node(this.javaClass.name)
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
