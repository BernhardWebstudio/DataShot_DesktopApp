/**
 * PositionTemplateBoxDialog.java
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
package edu.harvard.mcz.imagecapture.ui.dialogimport

import java.awt.Dimensionimport

java.awt.Frameimport java.awt.Insetsimport java.awt.event.ActionEventimport java.awt.event.KeyEvent

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * PositionTemplateBoxDialog
 */
class PositionTemplateBoxDialog : JDialog {
    private var jContentPane: JPanel? = null
    // maximum bounds in units of pixels
    private var maxX = 0 // image width
    private var maxY = 0 // image height
    private var ul: Dimension? = null // upper left corner of box  //  @jve:decl-index=0:
    private var size: Dimension? = null // height,width dimensions of box  //  @jve:decl-index=0:
    private var jPanel: JPanel? = null
    private var jLabel: JLabel? = null
    private var jTextFieldDescription: JTextField? = null
    private var jLabel1: JLabel? = null
    private var jTextFieldULX: JTextField? = null
    private var jLabel2: JLabel? = null
    private var jTextFieldULY: JTextField? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jTextFieldSizeWidth: JTextField? = null
    private var jTextFieldSizeHeight: JTextField? = null
    private var jPanel1: JPanel? = null
    private var jButton: JButton? = null
    private var jButtonSave: JButton? = null
    private var jLabel6: JLabel? = null
    private var jLabelImageHeight: JLabel? = null
    private var jLabelImageWidth1: JLabel? = null
    private var jLabelImageHeight1: JLabel? = null
    private var jLabelImageWidth: JLabel? = null
    private var thisDialog: PositionTemplateBoxDialog? = null
    /**
     * Result state of the dialog.
     *
     * @return RESULT_SAVE if user closed by pressing save, RESULT_CANCEL otherwise.
     */
    var result = RESULT_CANCEL
        private set
    private var jLabelFeedback: JLabel? = null

    /**
     * Don't call this constructor, use PositionTemplateBoxDialog(Frame owner, Dimension ImageSize, Dimension aULToChange, Dimension aSizeToChange,  String description)
     * and provide values to edit instead.  Protected so that it can be overridden in a class that extends this dialog (presumbably to create a new box with some default values).
     *
     * @param owner
     */
    protected constructor(owner: Frame?) : super(owner) {
        thisDialog = this
    }

    /**
     * Dialog to obtain new values for the upper left corner and size of a rectangular box drawn on an image
     * identified by the upper left corner of the box and the height and width of the box in all in units of
     * pixels.
     *
     * @param owner         the parent frame of this dialog.
     * @param imageSize     the height and width of the image onto which this box is placed
     * @param aULToChange   the upper left corner of the box, in units of pixels of the image
     * @param aSizeToChange the height and width of the box, in units of pixels of the image
     * @param description   a text description of the box to display on the dialog.
     * @throws BadTemplateException if a dimension parameter has a height or width less than or equal to zero (aULtoChange can be 0).
     */
    constructor(owner: Frame, imageSize: Dimension, aULToChange: Dimension, aSizeToChange: Dimension, description: String?) : super(owner, true) // create as modal over parent frame.
    {
        thisDialog = this
        // store values (and throw exception if they are out of range)
        setImageSize(imageSize)
        uL = aULToChange
        setSize(aSizeToChange)
        // set up the form
        initialize()
        // display values on form
        jLabelImageWidth.setText(Integer.toString(maxX))
        jLabelImageHeight.setText(Integer.toString(maxY))
        jLabelImageWidth1.setText(Integer.toString(maxX))
        jLabelImageHeight1.setText(Integer.toString(maxY))
        jTextFieldULX.setText(Integer.toString(uL.width))
        jTextFieldULY.setText(Integer.toString(uL.height))
        jTextFieldSizeWidth.setText(Integer.toString(getSize().width))
        jTextFieldSizeHeight.setText(Integer.toString(getSize().height))
        jTextFieldDescription.setText(description)
        jLabelFeedback.setText("")
        this.pack()
        val screenSize: Dimension = owner.getSize()
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        setSize(342, 200)
        this.setTitle("Edit a template component")
        this.setContentPane(getJContentPane())
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
            jContentPane.add(getJPanel(), BorderLayout.CENTER)
        }
        return jContentPane
    }

    /**
     * Get the upper left coordinate of the box in pixels on the
     * original image.
     *
     * @return ul the upper left coordinate of the box.
     */
    /**
     * @param ul the upper left coordinate of the box to set
     * @throws BadTemplateException if ul has a height or width less than zero (zero is ok).
     */
    @set:Throws(BadTemplateException::class)
    var uL: Dimension?
        get() = ul
        set(ul) {
            if (ul.width < 0 || ul.height < 0) {
                throw BadTemplateException("Upper left coordinate can't be less than 0.")
            }
            this.ul = ul
        }

    /**
     * Get the height and width of the box in pixels.
     *
     * @return the size of the box
     */
    override fun getSize(): Dimension? {
        return size
    }

    /**
     * @param size the size to set
     * @throws BadTemplateException if the size has a height or width less than or equal to 0.
     */
    override fun setSize(size: Dimension) {
        if (size.width <= 0 || size.height <= 0) {
            try {
                throw BadTemplateException("Box size can't be 0 or less.")
            } catch (e: BadTemplateException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        this.size = size
    }

    /**
     * @param imageSize the imageSize to set
     * @throws BadTemplateException
     */
    @Throws(BadTemplateException::class)
    fun setImageSize(imageSize: Dimension) {
        if (imageSize.width <= 0 || imageSize.height <= 0) {
            throw BadTemplateException("Image height and width can't be 0 or less.")
        }
        maxX = imageSize.width
        maxY = imageSize.height
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints71 = GridBagConstraints()
            gridBagConstraints71.gridx = 1
            gridBagConstraints71.gridy = 7
            jLabelFeedback = JLabel()
            jLabelFeedback.setText("JLabel")
            val gridBagConstraints61 = GridBagConstraints()
            gridBagConstraints61.gridx = 2
            gridBagConstraints61.gridy = 2
            jLabelImageWidth = JLabel()
            jLabelImageWidth.setText("JLabel")
            val gridBagConstraints51 = GridBagConstraints()
            gridBagConstraints51.gridx = 2
            gridBagConstraints51.gridy = 5
            jLabelImageHeight1 = JLabel()
            jLabelImageHeight1.setText("JLabel")
            val gridBagConstraints41 = GridBagConstraints()
            gridBagConstraints41.gridx = 2
            gridBagConstraints41.gridy = 4
            jLabelImageWidth1 = JLabel()
            jLabelImageWidth1.setText("JLabel")
            val gridBagConstraints31 = GridBagConstraints()
            gridBagConstraints31.gridx = 2
            gridBagConstraints31.gridy = 3
            jLabelImageHeight = JLabel()
            jLabelImageHeight.setText("JLabel")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.gridx = 2
            gridBagConstraints21.gridy = 1
            jLabel6 = JLabel()
            jLabel6.setText("Image (max)")
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.gridx = 1
            gridBagConstraints10.gridy = 6
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.fill = GridBagConstraints.BOTH
            gridBagConstraints9.gridy = 5
            gridBagConstraints9.weightx = 1.0
            gridBagConstraints9.gridx = 1
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.fill = GridBagConstraints.BOTH
            gridBagConstraints8.gridy = 4
            gridBagConstraints8.weightx = 1.0
            gridBagConstraints8.gridx = 1
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridy = 5
            jLabel4 = JLabel()
            jLabel4.setText("Height")
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 0
            gridBagConstraints6.anchor = GridBagConstraints.EAST
            gridBagConstraints6.gridy = 4
            jLabel3 = JLabel()
            jLabel3.setText("Width")
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 3
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.gridx = 0
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.gridy = 3
            jLabel2 = JLabel()
            jLabel2.setText("Upper Left Y")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            gridBagConstraints3.gridy = 2
            gridBagConstraints3.weightx = 1.0
            gridBagConstraints3.gridx = 1
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 0
            gridBagConstraints2.anchor = GridBagConstraints.EAST
            gridBagConstraints2.insets = Insets(0, 5, 0, 0)
            gridBagConstraints2.gridy = 2
            jLabel1 = JLabel()
            jLabel1.setText("Upper Left X")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            gridBagConstraints1.gridy = 1
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.ipadx = 0
            gridBagConstraints1.insets = Insets(0, 0, 0, 0)
            gridBagConstraints1.gridwidth = 1
            gridBagConstraints1.gridx = 1
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 0
            gridBagConstraints.gridwidth = 2
            gridBagConstraints.gridy = 0
            jLabel = JLabel()
            jLabel.setText("Edit a template component.")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, gridBagConstraints)
            jPanel.add(getJTextFieldDescription(), gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(getJTextFieldULX(), gridBagConstraints3)
            jPanel.add(jLabel2, gridBagConstraints4)
            jPanel.add(getJTextFieldULY(), gridBagConstraints5)
            jPanel.add(jLabel3, gridBagConstraints6)
            jPanel.add(jLabel4, gridBagConstraints7)
            jPanel.add(getJTextFieldSizeWidth(), gridBagConstraints8)
            jPanel.add(getJTextFieldSizeHeight(), gridBagConstraints9)
            jPanel.add(getJPanel1(), gridBagConstraints10)
            jPanel.add(jLabel6, gridBagConstraints21)
            jPanel.add(jLabelImageHeight, gridBagConstraints31)
            jPanel.add(jLabelImageWidth1, gridBagConstraints41)
            jPanel.add(jLabelImageHeight1, gridBagConstraints51)
            jPanel.add(jLabelImageWidth, gridBagConstraints61)
            jPanel.add(jLabelFeedback, gridBagConstraints71)
        }
        return jPanel
    }

    /**
     * This method initializes jTextFieldDescription
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDescription(): JTextField? {
        if (jTextFieldDescription == null) {
            jTextFieldDescription = JTextField()
            jTextFieldDescription.setEditable(false)
            jTextFieldDescription.setText("")
        }
        return jTextFieldDescription
    }

    /**
     * This method initializes jTextFieldULX
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldULX(): JTextField? {
        if (jTextFieldULX == null) {
            jTextFieldULX = JTextField()
        }
        return jTextFieldULX
    }

    /**
     * This method initializes jTextFieldULY
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldULY(): JTextField? {
        if (jTextFieldULY == null) {
            jTextFieldULY = JTextField()
        }
        return jTextFieldULY
    }

    /**
     * This method initializes jTextFieldSizeWidth
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldSizeWidth(): JTextField? {
        if (jTextFieldSizeWidth == null) {
            jTextFieldSizeWidth = JTextField()
        }
        return jTextFieldSizeWidth
    }

    /**
     * This method initializes jTextFieldSizeHeight
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldSizeHeight(): JTextField? {
        if (jTextFieldSizeHeight == null) {
            jTextFieldSizeHeight = JTextField()
        }
        return jTextFieldSizeHeight
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.gridx = 1
            gridBagConstraints11.gridy = 0
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            jPanel1.add(getJButton(), GridBagConstraints())
            jPanel1.add(getJButtonSave(), gridBagConstraints11)
        }
        return jPanel1
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Cancel")
            jButton.setMnemonic(KeyEvent.VK_C)
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    thisDialog.setVisible(false)
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jButtonSave
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSave(): JButton? {
        if (jButtonSave == null) {
            jButtonSave = JButton()
            jButtonSave.setText("Change")
            jButtonSave.setMnemonic(KeyEvent.VK_H)
            jButtonSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try {
                        uL = Dimension(Integer.valueOf(jTextFieldULX.getText()), Integer.valueOf(jTextFieldULY.getText()))
                        setSize(Dimension(Integer.valueOf(jTextFieldSizeWidth.getText()), Integer.valueOf(jTextFieldSizeHeight.getText())))
                        if (validateValues()) {
                            result = RESULT_SAVE
                        }
                    } catch (e1: NumberFormatException) { // failed...
                    } catch (e1: BadTemplateException) { // failed...
                    }
                    if (result != RESULT_SAVE) {
                        jLabelFeedback.setText("Can't save those values.")
                    } else {
                        jLabelFeedback.setText("")
                        thisDialog.setVisible(false)
                    }
                }
            })
        }
        return jButtonSave
    }

    /**
     * validate form fields
     *
     * @return true if valid, false otherwise.
     */
    private fun validateValues(): Boolean {
        var result = true
        // Check that UL coordinate is on image.
        if (ul.height < minY || ul.height > maxY) {
            result = false
        }
        if (ul.width < minX || ul.width > maxX) {
            result = false
        }
        // check that size is smaller that image
        if (size.height <= minY || size.height > maxY) {
            result = false
        }
        if (size.width <= minX || size.width > maxX) {
            result = false
        }
        // check that box fits in image
        if (ul.height + size.height > maxY) {
            result = false
        }
        if (ul.width + size.width > maxX) {
            result = false
        }
        return result
    }

    companion object {
        private const val serialVersionUID = 6798207249250029852L
        private const val RESULT_SAVE = 0
        private const val RESULT_CANCEL = 1
        // 0,0  (upper left corner of image)
        private const val minX = 0
        private const val minY = 0
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
