/**
 * PositionTemplateEditor.java
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

import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.io.File
import java.net.URL

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * PositionTemplateEditor allows viewing and creation of position templates indicating which portions
 * of an image file contain a barcode, OCR text, a specimen, labels from the unit tray, and labels from
 * the specimen's pin.
 */
class PositionTemplateEditor : JFrame {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var jButtonSave: JButton? = null
    private var jLabel: JLabel? = null
    private var jLabel1: JLabel? = null
    private var jTextFieldTemplateId: JTextField? = null
    private var jTextFieldName: JTextField? = null
    private var jJMenuBar: JMenuBar? = null
    private var jMenu: JMenu? = null
    private var jMenuItem: JMenuItem? = null
    private var jMenuItem1: JMenuItem? = null
    private var jMenuItem2: JMenuItem? = null
    private var jLabel2: JLabel? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jLabel5: JLabel? = null
    private var jLabel6: JLabel? = null
    private var jLabel7: JLabel? = null
    private var jTextField2: JTextField? = null
    private var controlBarcode: JButton? = null
    private var controlText: JButton? = null
    private var controlLabel: JButton? = null
    private var controlUTLabels: JButton? = null
    private var controlSpecimen: JButton? = null
    private var jLabel8: JLabel? = null
    private var jTextFieldImageFileName: JTextField? = null
    private var jPanel2: JPanel? = null
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    private var imagePanelForDrawing: ImagePanelForDrawing? = null
    private var thisFrame: PositionTemplateEditor?
    private var runningFromMain = false
    private var jPanel1: JPanel? = null
    private var template: PositionTemplate? = null //  @jve:decl-index=0:
    private var jScrollPane1: JScrollPane? = null
    private var referenceImageFilename // name of the currently loaded image file.  //  @jve:decl-index=0:
            : String? = null
    private var referenceImagePath //  @jve:decl-index=0:
            : String? = null
    private var jLabelFeedback: JLabel? = null
    private var jButton: JButton? = null
    private var jTextFieldBarcodeScan: JTextField? = null
    private var jButtonUnitTrayBarcode: JButton? = null
    private var jTextFieldUnitTrayBarcode: JTextField? = null
    private var jLabel9: JLabel? = null
    private var controlUTBarcode: JButton? = null

    /**
     * This is the default constructor
     */
    constructor() : super() {
        thisFrame = this
        initialize()
        pack()
        setBlankBackgroundImage()
    }

    /**
     * Constructor called from main method when running as stand alone application.
     *
     * @param runningAsApplication true to display file/exit menu option
     */
    constructor(runningAsApplication: Boolean) : super() {
        thisFrame = this
        runningFromMain = true
        initialize()
        pack()
        setBlankBackgroundImage()
    }

    fun setBlankBackgroundImage() {
        try { // Can't retrieve resource as a file from Jar file, unless giving File to ImageIcon....
            val url = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/blank2848x4272.jpg")
            log!!.error(url)
            log.error(url!!.toExternalForm())
            setImageFile(url)
        } catch (e: IOException) {
            log!!.error("Can't load blank template image")
            log.error(e)
        }
    }

    @Throws(NoSuchTemplateException::class)
    fun setTemplate(aTemplateId: String) {
        template = PositionTemplate(aTemplateId)
        jTextFieldTemplateId.setText(aTemplateId)
        jTextFieldName.setText(template.getName())
        if (template.getImageSize() == null) {
            jTextField2.setText("Any Size")
            controlBarcode.setText("No Value")
            controlText.setText("No Value")
            controlLabel.setText("No Value")
            controlUTLabels.setText("No Value")
            controlSpecimen.setText("No Value")
            controlUTBarcode.setText("No Value")
        } else {
            jTextField2.setText("Width=" + template.getImageSize().width + " Height=" + template.getImageSize().height)
            setButtonTexts()
            if (template.getReferenceImage() != null) {
                try {
                    setImageFile(File(template.getReferenceImageFilePath()))
                } catch (e: IOException) {
                    log!!.error("Failed to load default image for template.")
                    log.error(e)
                }
            }
        }
        thisFrame.pack()
        jButtonSave.setEnabled(template.isEditable())
        controlBarcode.setEnabled(template.isEditable())
        controlText.setEnabled(template.isEditable())
        controlLabel.setEnabled(template.isEditable())
        controlUTLabels.setEnabled(template.isEditable())
        controlSpecimen.setEnabled(template.isEditable())
        controlUTBarcode.setEnabled(template.isEditable())
        drawLayers()
    }

    /**
     * Set the image displayed in the editor given an URL (needed to load from resource inside jar).
     *
     * @param anImageURL an URL pointing to an image file.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun setImageFile(anImageURL: URL) {
        referenceImageFilename = anImageURL.path
        //TODO: Won't work with referenceImageFilename lookup.
        loadImage(ImageIO.read(anImageURL))
    }

    /**
     * Set the image displayed in the editor given a File
     *
     * @param anImageFile the image file to display.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun setImageFile(anImageFile: File?) {
        if (anImageFile != null) {
            referenceImageFilename = anImageFile.name
            referenceImagePath = anImageFile.path
            loadImage(ImageIO.read(anImageFile))
            jTextFieldImageFileName.setText(anImageFile.name)
        }
    }

    private fun loadImage(anImage: Image?) {
        imagePanelForDrawing.setImage(anImage)
        imagePanelForDrawing.zoomToFit()
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setPreferredSize(Dimension(1100, 900))
        this.setJMenuBar(getJJMenuBar())
        this.setContentPane(getJContentPane())
        this.setTitle("Image Template Editor")
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
            jContentPane.add(getJPanel1(), BorderLayout.CENTER)
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
            val gridBagConstraints24 = GridBagConstraints()
            gridBagConstraints24.fill = GridBagConstraints.BOTH
            gridBagConstraints24.gridy = 10
            gridBagConstraints24.weightx = 1.0
            gridBagConstraints24.anchor = GridBagConstraints.WEST
            gridBagConstraints24.gridx = 1
            val gridBagConstraints113 = GridBagConstraints()
            gridBagConstraints113.gridx = 0
            gridBagConstraints113.anchor = GridBagConstraints.EAST
            gridBagConstraints113.gridy = 10
            jLabel9 = JLabel()
            jLabel9.setText("Taxon Name Barcode")
            val gridBagConstraints23 = GridBagConstraints()
            gridBagConstraints23.fill = GridBagConstraints.BOTH
            gridBagConstraints23.gridy = 13
            gridBagConstraints23.weightx = 1.0
            gridBagConstraints23.anchor = GridBagConstraints.WEST
            gridBagConstraints23.gridx = 1
            val gridBagConstraints112 = GridBagConstraints()
            gridBagConstraints112.gridx = 0
            gridBagConstraints112.gridy = 13
            val gridBagConstraints22 = GridBagConstraints()
            gridBagConstraints22.fill = GridBagConstraints.BOTH
            gridBagConstraints22.gridy = 12
            gridBagConstraints22.weightx = 1.0
            gridBagConstraints22.anchor = GridBagConstraints.WEST
            gridBagConstraints22.gridx = 1
            val gridBagConstraints111 = GridBagConstraints()
            gridBagConstraints111.gridx = 0
            gridBagConstraints111.gridy = 12
            val gridBagConstraints110 = GridBagConstraints()
            gridBagConstraints110.gridx = 1
            gridBagConstraints110.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints110.gridwidth = 1
            gridBagConstraints110.anchor = GridBagConstraints.NORTH
            gridBagConstraints110.gridy = 14
            jLabelFeedback = JLabel()
            jLabelFeedback.setText(" ")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.fill = GridBagConstraints.BOTH
            gridBagConstraints21.gridy = 0
            gridBagConstraints21.weightx = 2.0
            gridBagConstraints21.anchor = GridBagConstraints.NORTHWEST
            gridBagConstraints21.gridx = 1
            val gridBagConstraints13 = GridBagConstraints()
            gridBagConstraints13.gridx = 0
            gridBagConstraints13.anchor = GridBagConstraints.NORTHEAST
            gridBagConstraints13.gridy = 0
            jLabel8 = JLabel()
            jLabel8.setText("ImageFile")
            val gridBagConstraints18 = GridBagConstraints()
            gridBagConstraints18.fill = GridBagConstraints.BOTH
            gridBagConstraints18.gridy = 9
            gridBagConstraints18.weightx = 1.0
            gridBagConstraints18.anchor = GridBagConstraints.WEST
            gridBagConstraints18.gridx = 1
            val gridBagConstraints17 = GridBagConstraints()
            gridBagConstraints17.fill = GridBagConstraints.BOTH
            gridBagConstraints17.gridy = 8
            gridBagConstraints17.weightx = 1.0
            gridBagConstraints17.anchor = GridBagConstraints.WEST
            gridBagConstraints17.gridx = 1
            val gridBagConstraints16 = GridBagConstraints()
            gridBagConstraints16.fill = GridBagConstraints.BOTH
            gridBagConstraints16.gridy = 7
            gridBagConstraints16.weightx = 1.0
            gridBagConstraints16.anchor = GridBagConstraints.WEST
            gridBagConstraints16.gridx = 1
            val gridBagConstraints15 = GridBagConstraints()
            gridBagConstraints15.fill = GridBagConstraints.BOTH
            gridBagConstraints15.gridy = 6
            gridBagConstraints15.weightx = 1.0
            gridBagConstraints15.anchor = GridBagConstraints.WEST
            gridBagConstraints15.gridx = 1
            val gridBagConstraints14 = GridBagConstraints()
            gridBagConstraints14.fill = GridBagConstraints.BOTH
            gridBagConstraints14.gridy = 5
            gridBagConstraints14.weightx = 1.0
            gridBagConstraints14.anchor = GridBagConstraints.WEST
            gridBagConstraints14.gridx = 1
            val gridBagConstraints12 = GridBagConstraints()
            gridBagConstraints12.fill = GridBagConstraints.BOTH
            gridBagConstraints12.gridy = 3
            gridBagConstraints12.weightx = 1.0
            gridBagConstraints12.anchor = GridBagConstraints.WEST
            gridBagConstraints12.gridx = 1
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.gridx = 0
            gridBagConstraints11.anchor = GridBagConstraints.EAST
            gridBagConstraints11.gridy = 9
            jLabel7 = JLabel()
            jLabel7.setText("Specimen")
            jLabel7.setForeground(Color.ORANGE)
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.gridx = 0
            gridBagConstraints10.anchor = GridBagConstraints.EAST
            gridBagConstraints10.gridy = 8
            jLabel6 = JLabel()
            jLabel6.setText("Tray Labels")
            jLabel6.setForeground(Color.CYAN)
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.gridx = 0
            gridBagConstraints9.anchor = GridBagConstraints.EAST
            gridBagConstraints9.gridy = 7
            jLabel5 = JLabel()
            jLabel5.setText("Pin Labels")
            jLabel5.setForeground(Color.MAGENTA)
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.gridx = 0
            gridBagConstraints8.anchor = GridBagConstraints.EAST
            gridBagConstraints8.insets = Insets(0, 3, 0, 0)
            gridBagConstraints8.gridy = 6
            jLabel4 = JLabel()
            jLabel4.setText("Taxon Name Label")
            jLabel4.setForeground(Color.BLUE)
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridy = 5
            jLabel3 = JLabel()
            jLabel3.setText("Barcode")
            jLabel3.setForeground(Color.RED)
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 0
            gridBagConstraints6.anchor = GridBagConstraints.EAST
            gridBagConstraints6.gridy = 3
            jLabel2 = JLabel()
            jLabel2.setText("Image Size")
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 2
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.anchor = GridBagConstraints.WEST
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.fill = GridBagConstraints.BOTH
            gridBagConstraints4.gridy = 1
            gridBagConstraints4.weightx = 1.0
            gridBagConstraints4.anchor = GridBagConstraints.WEST
            gridBagConstraints4.gridx = 1
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 0
            gridBagConstraints2.anchor = GridBagConstraints.EAST
            gridBagConstraints2.gridy = 2
            jLabel1 = JLabel()
            jLabel1.setText("Name")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridx = 0
            gridBagConstraints1.anchor = GridBagConstraints.EAST
            gridBagConstraints1.gridy = 1
            jLabel = JLabel()
            jLabel.setText("Template ID")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 1
            gridBagConstraints.gridy = 11
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(getJButtonSave(), gridBagConstraints)
            jPanel.add(jLabel, gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(getJTextFieldTemplateId(), gridBagConstraints4)
            jPanel.add(getJTextFieldName(), gridBagConstraints5)
            jPanel.add(jLabel2, gridBagConstraints6)
            jPanel.add(jLabel3, gridBagConstraints7)
            jPanel.add(jLabel4, gridBagConstraints8)
            jPanel.add(jLabel5, gridBagConstraints9)
            jPanel.add(jLabel6, gridBagConstraints10)
            jPanel.add(jLabel7, gridBagConstraints11)
            jPanel.add(getJTextField2(), gridBagConstraints12)
            jPanel.add(jTextField3, gridBagConstraints14)
            jPanel.add(jTextField4, gridBagConstraints15)
            jPanel.add(jTextField5, gridBagConstraints16)
            jPanel.add(jTextField6, gridBagConstraints17)
            jPanel.add(jTextField7, gridBagConstraints18)
            jPanel.add(jLabel8, gridBagConstraints13)
            jPanel.add(jTextField8, gridBagConstraints21)
            jPanel.add(jLabelFeedback, gridBagConstraints110)
            jPanel.add(getJButton(), gridBagConstraints111)
            jPanel.add(getJTextFieldBarcodeScan(), gridBagConstraints22)
            jPanel.add(jButton1, gridBagConstraints112)
            jPanel.add(jTextField, gridBagConstraints23)
            jPanel.add(jLabel9, gridBagConstraints113)
            jPanel.add(jTextField9, gridBagConstraints24)
        }
        return jPanel
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSave(): JButton? {
        if (jButtonSave == null) {
            jButtonSave = JButton()
            jButtonSave.setText("Save Template")
            jButtonSave.setEnabled(false)
            jButtonSave.setMnemonic(KeyEvent.VK_S)
            jButtonSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    jLabelFeedback.setText(" ")
                    if (template!!.isEditable()) {
                        template.setTemplateId(jTextFieldTemplateId.getText())
                        template.setTemplateName(jTextFieldName.getText())
                        template.setImageSize(imagePanelForDrawing.getImageSize())
                        try {
                            if (template.getTemplateId().trim({ it <= ' ' }) == "") {
                                throw BadTemplateException("Template ID can't be blank.")
                            }
                            if (template!!.getReferenceImage() == null) {
                                val ils = ICImageLifeCycle()
                                val imageToFind = ICImage()
                                imageToFind.setFilename(referenceImageFilename)
                                val images: MutableList<ICImage?> = ils.findByExample(imageToFind)
                                if (!images.isEmpty()) {
                                    template!!.setReferenceImage(images[0])
                                } else { //create a new image record
                                    val newImage = ICImage()
                                    newImage.setFilename(referenceImageFilename)
                                    // path should be relative to the base path
// just substituting won't work for images off the base path.
                                    val startPointName: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                                    newImage.setPath(
                                            referenceImagePath!!.replace(startPointName.toRegex(), "").replace(referenceImageFilename + "$".toRegex(), "")
                                    )
                                    newImage.setTemplateId(template.getTemplateId())
                                    ils.persist(newImage)
                                    if (referenceImageFilename != null) {
                                        jTextFieldImageFileName.setText(referenceImageFilename)
                                    }
                                }
                            }
                            //TODO: Check that template is valid, not overlapping with existing template.
//Test images IMG_00005.jpg and IMG_00001.jpg suggest that overlapping templates might
//be needed - where all parameters except extent of barcode are the same.
                            template!!.persist()
                            jLabelFeedback.setText("Saved " + template.getTemplateId())
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to save template, invalid data. " + e1.message,
                                    "Error:BadTemplateData",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.debug(e1)
                        } catch (e2: SaveFailedException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to save template. " + e2.message,
                                    "Error:SaveFailed",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.debug(e2)
                        }
                    }
                }
            })
        }
        return jButtonSave
    }

    /**
     * This method initializes jTextFieldTemplateId
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldTemplateId(): JTextField? {
        if (jTextFieldTemplateId == null) {
            jTextFieldTemplateId = JTextField(50)
        }
        return jTextFieldTemplateId
    }

    /**
     * This method initializes jTextFieldName
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldName(): JTextField? {
        if (jTextFieldName == null) {
            jTextFieldName = JTextField()
        }
        return jTextFieldName
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private fun getJJMenuBar(): JMenuBar? {
        if (jJMenuBar == null) {
            jJMenuBar = JMenuBar()
            jJMenuBar.add(getJMenu())
        }
        return jJMenuBar
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenu(): JMenu? {
        if (jMenu == null) {
            jMenu = JMenu()
            jMenu.setText("File")
            jMenu.setMnemonic(KeyEvent.VK_F)
            jMenu.add(jMenuItemLoadImage)
            jMenu.add(jMenuItemCreateTemplate)
            jMenu.add(getJMenuItem2())
        }
        return jMenu
    }

    /**
     * Set the text for each button as the position and size of the relevant portion of the template.
     */
    private fun setButtonTexts() {
        controlBarcode.setText("UL=" + template.getBarcodePosition().width + "," + template.getBarcodePosition().height + " W/H=" + template.getBarcodeSize().width + "," + template.getBarcodeSize().height)
        controlText.setText("UL=" + template.getTextPosition().width + "," + template.getTextPosition().height + " W/H=" + template.getTextSize().width + "," + template.getTextSize().height)
        controlLabel.setText("UL=" + template.getLabelPosition().width + "," + template.getLabelPosition().height + " W/H=" + template.getLabelSize().width + "," + template.getLabelSize().height)
        controlUTLabels.setText("UL=" + template.getUTLabelsPosition().width + "," + template.getUTLabelsPosition().height + " W/H=" + template.getUTLabelsSize().width + "," + template.getUTLabelsSize().height)
        controlSpecimen.setText("UL=" + template.getSpecimenPosition().width + "," + template.getSpecimenPosition().height + " W/H=" + template.getSpecimenSize().width + "," + template.getSpecimenSize().height)
        controlUTBarcode.setText("UL=" + template.getUtBarcodePosition().width + "," + template.getUtBarcodePosition().height + " W/H=" + template.getUtBarcodeSize().width + "," + template.getUtBarcodeSize().height)
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemCreateTemplate: JMenuItem?
        private get() {
            if (jMenuItem == null) {
                jMenuItem = JMenuItem()
                jMenuItem.setText("Create New Template From Image")
                jMenuItem.setMnemonic(KeyEvent.VK_N)
                jMenuItem.setEnabled(true)
                jMenuItem.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        template = PositionTemplate(true)
                        template.setImageSize(imagePanelForDrawing.getImageSize())
                        jTextField2.setText("Width=" + template.getImageSize().width + " Height=" + template.getImageSize().height)
                        setButtonTexts()
                        drawLayers()
                        jButtonSave.setEnabled(template.isEditable())
                        controlBarcode.setEnabled(template.isEditable())
                        controlText.setEnabled(template.isEditable())
                        controlLabel.setEnabled(template.isEditable())
                        controlUTLabels.setEnabled(template.isEditable())
                        controlSpecimen.setEnabled(template.isEditable())
                        controlUTBarcode.setEnabled(template.isEditable())
                        drawLayers()
                    }
                })
            }
            return jMenuItem
        }

    /**
     * This method initializes jMenuItem1
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemLoadImage: JMenuItem?
        private get() {
            if (jMenuItem1 == null) {
                jMenuItem1 = JMenuItem()
                jMenuItem1.setText("Load Image")
                jMenuItem1.setMnemonic(KeyEvent.VK_L)
                jMenuItem1.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        jLabelFeedback.setText("")
                        val imageFile: File = FileUtility.askForImageFile(File(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
                        if (imageFile != null) {
                            jLabelFeedback.setText("Loading...")
                            try {
                                setImageFile(imageFile)
                                jLabelFeedback.setText("")
                                drawLayers()
                            } catch (e1: IOException) {
                                log!!.debug(e1)
                                jLabelFeedback.setText("Unable to load image.")
                            }
                        }
                        drawLayers()
                    }
                })
            }
            return jMenuItem1
        }

    /**
     * This method initializes jMenuItem2
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItem2(): JMenuItem? {
        if (jMenuItem2 == null) {
            jMenuItem2 = JMenuItem()
            if (runningFromMain) {
                jMenuItem2.setText("Exit")
                jMenuItem2.setMnemonic(KeyEvent.VK_E)
            } else {
                jMenuItem2.setText("Close Window")
                jMenuItem2.setMnemonic(KeyEvent.VK_C)
            }
            jMenuItem2.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (runningFromMain) {
                        ImageCaptureApp.exit(ImageCaptureApp.EXIT_NORMAL)
                    } else {
                        thisFrame.setVisible(false)
                    }
                }
            })
        }
        return jMenuItem2
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextField2(): JTextField? {
        if (jTextField2 == null) {
            jTextField2 = JTextField()
            jTextField2.setEditable(false)
        }
        return jTextField2
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val jTextField3: JButton?
        private get() {
            if (controlBarcode == null) {
                controlBarcode = JButton()
                controlBarcode.setEnabled(false)
                controlBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getBarcodeULPosition(),
                                    template.getBarcodeSize(),
                                    "Barcode in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setBarcodePosition(dialog.getUL())
                                template.setBarcodeSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlBarcode
        }

    /**
     * This method initializes jTextField4
     *
     * @return javax.swing.JTextField
     */
    private val jTextField4: JButton?
        private get() {
            if (controlText == null) {
                controlText = JButton()
                controlText.setEnabled(false)
                controlText.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getTextPosition(),
                                    template.getTextSize(),
                                    "Taxon Name Label in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setTextPosition(dialog.getUL())
                                template.setTextSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlText
        }

    /**
     * This method initializes jTextField5
     *
     * @return javax.swing.JTextField
     */
    private val jTextField5: JButton?
        private get() {
            if (controlLabel == null) {
                controlLabel = JButton()
                controlLabel.setEnabled(false)
                controlLabel.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getLabelPosition(),
                                    template.getLabelSize(),
                                    "Pin Labels in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setLabelPosition(dialog.getUL())
                                template.setLabelSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlLabel
        }

    /**
     * This method initializes jTextField6
     *
     * @return javax.swing.JTextField
     */
    private val jTextField6: JButton?
        private get() {
            if (controlUTLabels == null) {
                controlUTLabels = JButton()
                controlUTLabels.setEnabled(false)
                controlUTLabels.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getUTLabelsPosition(),
                                    template.getUTLabelsSize(),
                                    "Unit Tray Labels in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setUTLabelsPosition(dialog.getUL())
                                template.setUTLabelsSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlUTLabels
        }

    /**
     * This method initializes jTextField7
     *
     * @return javax.swing.JTextField
     */
    private val jTextField7: JButton?
        private get() {
            if (controlSpecimen == null) {
                controlSpecimen = JButton()
                controlSpecimen.setEnabled(false)
                controlSpecimen.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getSpecimenPosition(),
                                    template.getSpecimenSize(),
                                    "Specimen in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setSpecimenPosition(dialog.getUL())
                                template.setSpecimenSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlSpecimen
        }

    /**
     * This method initializes jTextField8
     *
     * @return javax.swing.JTextField
     */
    private val jTextField8: JTextField?
        private get() {
            if (jTextFieldImageFileName == null) {
                jTextFieldImageFileName = JTextField(50)
                jTextFieldImageFileName.setEditable(false)
            }
            return jTextFieldImageFileName
        }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel2(): JPanel? {
        if (jPanel2 == null) {
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            gridBagConstraints3.weighty = 1.0
            gridBagConstraints3.anchor = GridBagConstraints.NORTH
            gridBagConstraints3.weightx = 1.0
            jPanel2 = JPanel()
            jPanel2.setLayout(GridBagLayout())
            jPanel2.add(getJScrollPane(), gridBagConstraints3)
        }
        return jPanel2
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            jScrollPane = JScrollPane()
            //jScrollPane.setPreferredSize(new Dimension(600,150));
            jScrollPane.setViewportView(getJTable())
        }
        return jScrollPane
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private fun getJTable(): JTable? {
        if (jTable == null) {
            jTable = JTable()
            val templates: MutableList<PositionTemplate?> = PositionTemplate.Companion.getTemplates()
            jTable.setModel(PositionTemplateTableModel(templates))
            jTable.getColumn("").setCellRenderer(ButtonRenderer())
            jTable.getColumn("").setCellEditor(ButtonEditor(ButtonEditor.Companion.OPEN_TEMPLATE, thisFrame))
        }
        return jTable
    }

    /**
     * This method initializes imagePanelForDrawing
     *
     * @return edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawing
     */
    private fun getImagePanelForDrawing(): ImagePanelForDrawing? {
        if (imagePanelForDrawing == null) {
            imagePanelForDrawing = ImagePanelForDrawing()
            imagePanelForDrawing.setPreferredSize(Dimension(600, 600))
        }
        return imagePanelForDrawing
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints19 = GridBagConstraints()
            gridBagConstraints19.fill = GridBagConstraints.BOTH
            gridBagConstraints19.weighty = 1.0
            gridBagConstraints19.gridx = 2
            gridBagConstraints19.gridy = 1
            gridBagConstraints19.weightx = 1.0
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            val g1 = GridBagConstraints()
            g1.gridx = 1
            g1.anchor = GridBagConstraints.NORTHEAST
            g1.weightx = 0.1
            g1.weighty = 0.75
            g1.fill = GridBagConstraints.HORIZONTAL
            g1.gridy = 1
            val g2 = GridBagConstraints()
            g2.gridx = 1
            g2.anchor = GridBagConstraints.NORTH
            g2.fill = GridBagConstraints.BOTH
            g2.gridwidth = 2
            g2.weighty = 0.2
            g2.weightx = 0.0
            g2.gridy = 0
            jPanel1.add(getJPanel(), g1)
            jPanel1.add(getJPanel2(), g2)
            jPanel1.add(getJScrollPane1(), gridBagConstraints19)
        }
        return jPanel1
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane1(): JScrollPane? {
        if (jScrollPane1 == null) {
            jScrollPane1 = JScrollPane()
            jScrollPane1.setPreferredSize(Dimension(800, 800))
            jScrollPane1.setViewportView(getImagePanelForDrawing())
        }
        return jScrollPane1
    }

    /**
     * Draw boxes delimiting the various parts of the template on the current image.
     * Public so that it can be invoked from ButtonEditor().
     */
    fun drawLayers() { // draw the image bounds in black.
        if (template != null) {
            imagePanelForDrawing.clearOverlay()
            imagePanelForDrawing.drawBox(Dimension(0, 0), template.getImageSize(), Color.BLACK, BasicStroke(2f))
            // draw each template layer in a distinct color (keyed to UI text).
            imagePanelForDrawing.drawBox(template.getBarcodeULPosition(), template.getBarcodeSize(), Color.RED)
            imagePanelForDrawing.drawBox(template.getTextPosition(), template.getTextSize(), Color.BLUE)
            imagePanelForDrawing.drawBox(template.getSpecimenPosition(), template.getSpecimenSize(), Color.ORANGE)
            imagePanelForDrawing.drawBox(template.getUTLabelsPosition(), template.getUTLabelsSize(), Color.CYAN)
            imagePanelForDrawing.drawBox(template.getLabelPosition(), template.getLabelSize(), Color.MAGENTA)
            imagePanelForDrawing.drawBox(template.getUtBarcodePosition(), template.getUtBarcodeSize(), Color.BLACK)
        }
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Check for Barcode")
            jButton.setMnemonic(KeyEvent.VK_C)
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (template != null && imagePanelForDrawing.getImage() != null) {
                        val candidateImageFile = CandidateImageFile()
                        jTextFieldBarcodeScan.setText(candidateImageFile.getBarcodeTextFromImage(imagePanelForDrawing.getImage() as BufferedImage, template, false))
                    }
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldBarcodeScan(): JTextField? {
        if (jTextFieldBarcodeScan == null) {
            jTextFieldBarcodeScan = JTextField()
            jTextFieldBarcodeScan.setEditable(false)
        }
        return jTextFieldBarcodeScan
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val jButton1: JButton?
        private get() {
            if (jButtonUnitTrayBarcode == null) {
                jButtonUnitTrayBarcode = JButton()
                jButtonUnitTrayBarcode.setText("Check Taxon Barcode")
                jButtonUnitTrayBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val candidateImageFile = CandidateImageFile()
                        jTextFieldUnitTrayBarcode.setText(candidateImageFile.getBarcodeUnitTrayTextFromImage(imagePanelForDrawing.getImage() as BufferedImage, template!!))
                    }
                })
            }
            return jButtonUnitTrayBarcode
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField: JTextField?
        private get() {
            if (jTextFieldUnitTrayBarcode == null) {
                jTextFieldUnitTrayBarcode = JTextField()
                jTextFieldUnitTrayBarcode.setEditable(false)
            }
            return jTextFieldUnitTrayBarcode
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField9: JButton?
        private get() {
            if (controlUTBarcode == null) {
                controlUTBarcode = JButton()
                controlUTBarcode.setEnabled(false)
                controlUTBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.getImageSize(),
                                    template.getUtBarcodePosition(),
                                    template.getUtBarcodeSize(),
                                    "UnitTray/Taxon Barcode in " + template.getTemplateId())
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setUtBarcodePosition(dialog.getUL())
                                template.setUtBarcodeSize(dialog.getSize())
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlUTBarcode
        }

    companion object {
        private const val serialVersionUID = -6969168467927467337L
        private val log = LogFactory.getLog(PositionTemplateEditor::class.java)
        /**
         * @param args
         */
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater(Runnable {
                val thisClass = PositionTemplateEditor(true)
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
                thisClass.setVisible(true)
            })
        }
    }
} //  @jve:decl-index=0:visual-constraint="9,-1"
