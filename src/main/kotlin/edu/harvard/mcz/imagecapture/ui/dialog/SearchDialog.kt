/**
 * SearchDialog.java
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

edu.harvard.mcz.imagecapture.entity.Collectorimport java.awt.*
import java.awt.event.ActionEventimport

java.awt.event.KeyEventimport java.util.ArrayList

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * SearchDialog
 */
class SearchDialog(owner: Frame?) : JDialog(owner) {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var jButton: JButton? = null
    private var jPanel1: JPanel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jTextFieldFamily: JTextField? = null
    private var jTextFieldGenus: JTextField? = null
    private var jTextFieldSpecies: JTextField? = null
    private var jComboBoxCollection: JComboBox<*>? = null
    private var jComboBoxWorkflowStatus: JComboBox<*>? = null
    private var jTextFieldImageFilename: JTextField? = null
    private var jComboBoxPath: JComboBox<*>? = null
    private var jComboBoxEntryBy: JComboBox<*>? = null
    private var jComboBoxIdentifiedBy: JComboBox<*>? = null
    private var jTextFieldSubfamily: JTextField? = null
    private var jTextFieldSubspecies: JTextField? = null
    private var jButton1: JButton? = null
    private var jComboBoxCollector: JComboBox<*>? = null
    private var jTextFieldVerbatimLocality: JTextField? = null
    private var jComboBoxCountry: JComboBox<*>? = null
    private var jComboBoxQuestions: JComboBox<*>? = null
    private var jOffsetNumberField: JIntegerField? = null
    private var jLimitNumberField: JIntegerField? = null
    private var jTextFieldTribe: JTextField? = null
    //private JTextField jTextFieldPrimaryDivision = null;
    private var jComboBoxPrimaryDivision: JComboBox<String?>? = null
    private var textFieldHigherGeog: JTextField? = null
    private var lblHigherGeography: JLabel? = null private get() {
        if (field == null) {
            field = JLabel("Higher Geography")
        }
        return field
    }
    private var jTextFieldInterpretedDate: JTextField? = null
    private val maxComboBoxDims: Dimension = Dimension(350, 250)
    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() { //        this.setSize(500, 750);
        this.setTitle("Search For Specimens")
        this.setContentPane(getJContentPane())
        //        this.setPreferredSize(new Dimension(500, 750));
        this.pack()
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
            jContentPane.add(getJPanel(), BorderLayout.SOUTH)
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
            val gridBagConstraints17 = GridBagConstraints()
            gridBagConstraints17.gridx = 1
            gridBagConstraints17.gridy = 0
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jButtonSearch, GridBagConstraints())
            jPanel.add(closeJButton, gridBagConstraints17)
        }
        return jPanel
    }// it the path = date imaged has content, add it// if filename has content, add it// Either image filename or date imaged or both have content
// so we need to add an image to the search criteria.
// Default specimen is created with valid distribution flag = true, etc. need to remove this
// from the search criteria for a search by example.
    /*if (jTextFieldPrimaryDivision.getText()!=null && jTextFieldPrimaryDivision.getText().length() > 0) {
        searchCriteria.setPrimaryDivison(jTextFieldPrimaryDivision.getText());
    }*/
    // TODO: Add higher geography

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val jButtonSearch: JButton?
        private get() {
            if (jButton == null) {
                jButton = JButton()
                jButton.setText("Search")
                jButton.setMnemonic(KeyEvent.VK_S)
                this.getRootPane().setDefaultButton(jButton)
                jButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Searching...")
                        val searchCriteria = Specimen()
                        // Default specimen is created with valid distribution flag = true, etc. need to remove this
// from the search criteria for a search by example.
                        searchCriteria.clearDefaults()
                        if (jTextFieldDrawerNumber.getText() != null && jTextFieldDrawerNumber.getText().length > 0) {
                            searchCriteria.setDrawerNumber(jTextFieldDrawerNumber.getText())
                        }
                        if (jTextFieldBarcode.getText() != null && jTextFieldBarcode.getText().length > 0) {
                            searchCriteria.setBarcode(jTextFieldBarcode.getText())
                        }
                        if (jTextFieldFamily.getText() != null && jTextFieldFamily.getText().length > 0) {
                            searchCriteria.setFamily(jTextFieldFamily.getText())
                        }
                        if (jTextFieldSubfamily.getText() != null && jTextFieldSubfamily.getText().length > 0) {
                            searchCriteria.setSubfamily(jTextFieldSubfamily.getText())
                        }
                        if (jTextFieldTribe.getText() != null && jTextFieldTribe.getText().length > 0) {
                            searchCriteria.setTribe(jTextFieldTribe.getText())
                        }
                        if (jTextFieldGenus.getText() != null && jTextFieldGenus.getText().length > 0) {
                            searchCriteria.setGenus(jTextFieldGenus.getText())
                        }
                        if (jTextFieldSpecies.getText() != null && jTextFieldSpecies.getText().length > 0) {
                            searchCriteria.setSpecificEpithet(jTextFieldSpecies.getText())
                        }
                        if (jTextFieldSubspecies.getText() != null && jTextFieldSubspecies.getText().length > 0) {
                            searchCriteria.setSubspecificEpithet(jTextFieldSubspecies.getText())
                        }
                        if (jTextFieldVerbatimLocality.getText() != null && jTextFieldVerbatimLocality.getText().length > 0) {
                            searchCriteria.setVerbatimLocality(jTextFieldVerbatimLocality.getText())
                        }
                        /*if (jTextFieldPrimaryDivision.getText()!=null && jTextFieldPrimaryDivision.getText().length() > 0) {
                            searchCriteria.setPrimaryDivison(jTextFieldPrimaryDivision.getText());
                        }*/if (jComboBoxWorkflowStatus.getSelectedItem() != null) {
                            if (jComboBoxWorkflowStatus.getSelectedItem().toString() != "") {
                                searchCriteria.setWorkFlowStatus(jComboBoxWorkflowStatus.getSelectedItem().toString())
                            }
                        }
                        // TODO: Add higher geography
                        if (jComboBoxCountry.getSelectedItem() != null) {
                            if (jComboBoxCountry.getSelectedItem().toString() != "") {
                                searchCriteria.setCountry(jComboBoxCountry.getSelectedItem().toString())
                            }
                        }
                        if (jComboBoxPrimaryDivision.getSelectedItem() != null) {
                            if (jComboBoxPrimaryDivision.getSelectedItem().toString() != "") {
                                searchCriteria.setPrimaryDivison(jComboBoxPrimaryDivision.getSelectedItem().toString())
                            }
                        }
                        if (jTextFieldInterpretedDate.getText() != null && jTextFieldInterpretedDate.getText().length > 0) {
                            searchCriteria.setIsoDate(jTextFieldInterpretedDate.getText())
                        }
                        if (jComboBoxCollector.getSelectedItem() != null) {
                            if (jComboBoxCollector.getSelectedItem().toString() != "") {
                                val c = Collector()
                                c.setCollectorName(jComboBoxCollector.getSelectedItem().toString())
                                val collectors: MutableSet<Collector?> = HashSet<Collector?>()
                                collectors.add(c)
                                searchCriteria.setCollectors(collectors)
                            }
                        }
                        if (jTextFieldImageFilename.getText() != null && jTextFieldImageFilename.getText().length > 0 ||
                                jComboBoxEntryBy.getSelectedItem() != null) { // Either image filename or date imaged or both have content
// so we need to add an image to the search criteria.
                            val i = ICImage()
                            if (jTextFieldImageFilename.getText() != null && jTextFieldImageFilename.getText().length > 0) { // if filename has content, add it
                                i.setFilename(jTextFieldImageFilename.getText())
                            }
                            if (jComboBoxPath.getSelectedItem() != null) {
                                if (jComboBoxPath.getSelectedItem().toString() != "") { // it the path = date imaged has content, add it
                                    i.setPath(jComboBoxPath.getSelectedItem().toString())
                                }
                            }
                            val im: MutableSet<ICImage?> = HashSet<ICImage?>()
                            im.add(i)
                            searchCriteria.setICImages(im)
                        }
                        if (jComboBoxCollection.getSelectedItem() != null) {
                            if (jComboBoxCollection.getSelectedItem().toString() != "") {
                                searchCriteria.setCollection(jComboBoxCollection.getSelectedItem().toString())
                            }
                        }
                        if (jComboBoxEntryBy.getSelectedItem() != null) {
                            if (jComboBoxEntryBy.getSelectedItem().toString() != "") {
                                val tc = Tracking()
                                tc.setUser(jComboBoxEntryBy.getSelectedItem().toString())
                                val trackings: MutableSet<Tracking?> = HashSet<Tracking?>()
                                trackings.add(tc)
                                searchCriteria.setTrackings(trackings)
                            }
                        }
                        if (jComboBoxIdentifiedBy.getSelectedItem() != null) {
                            if (jComboBoxIdentifiedBy.getSelectedItem().toString() != "") {
                                searchCriteria.setIdentifiedBy(jComboBoxIdentifiedBy.getSelectedItem().toString())
                            }
                        }
                        if (jComboBoxQuestions.getSelectedItem() != null) {
                            if (jComboBoxQuestions.getSelectedItem().toString() != "") {
                                searchCriteria.setQuestions(jComboBoxQuestions.getSelectedItem().toString())
                            }
                        }
                        Singleton.Companion.getSingletonInstance().getMainFrame().setSpecimenBrowseList(searchCriteria, jLimitNumberField.getIntValue(), jOffsetNumberField.getIntValue())
                    }
                })
            }
            return jButton
        }

    /**
     * This method initializes the main JPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) { // set titles etc.
            jPanel1 = JPanel(MigLayout("wrap 2, fillx"))
            val jLabelInstructions = JLabel("Search for specimens. Use % as a wildcard.")
            val f: Font = jLabelInstructions.getFont()
            // bold
            jLabelInstructions.setFont(f.deriveFont(f.getStyle() or Font.BOLD))
            jPanel1.add(jLabelInstructions, "span 2")
            // set fields
            val labels = arrayOf<String?>(
                    "Image Filename",
                    "Imaged Date/Path",
                    "Drawer Number",
                    "Barcode",
                    "Family",
                    "Tribe",
                    "Subfamily",
                    "Genus",
                    "Species",
                    "Subspecies",
                    "Verbatim Locality",
                    "Country",
                    "State/Province",
                    "Collection",
                    "Collector",
                    "Interpreted Date",
                    "Workflow Status",
                    "Questions",
                    "Entry By",
                    "Identified By",
                    "Limit",
                    "Offset"
            )
            val fields: Array<Component?> = arrayOf<Component?>(
                    imageFileJTextField,
                    imagePathJComboBox,
                    drawerNumberJTextField,
                    barcodeJTextField,
                    familyJTextField,
                    tribeJTextField,
                    subfamilyJTextField,
                    genusJTextField,
                    speciesJTextField,
                    subspeciesJTextField,
                    verbatimLocalityJTextField,
                    countryJComboBox,
                    primaryDivisionJComboBox,
                    collectionJComboBox,
                    collectorsJComboBox,
                    interpretedDateTextField,
                    workflowsJComboBox,
                    questionJComboBox,
                    usersJComboBox,
                    identifiedByComboBox,
                    limitJIntegerField,
                    offsetJIntegerField
            )
            assert(fields.size == labels.size)
            for (i in labels.indices) {
                val label = JLabel()
                label.setText(labels[i] + ":")
                jPanel1.add(label, "right") //"align label");
                jPanel1.add(fields[i], "grow")
            }
            /*
						GridBagConstraints gbc_lblHigherGeography = new GridBagConstraints();
						gbc_lblHigherGeography.insets = new Insets(0, 0, 5, 5);
						gbc_lblHigherGeography.anchor = GridBagConstraints.EAST;
						gbc_lblHigherGeography.gridx = 0;
						gbc_lblHigherGeography.gridy = 12;
						jPanel1.add(getLblHigherGeography(), gbc_lblHigherGeography);
						GridBagConstraints gbc_textFieldHigherGeog = new GridBagConstraints();
						gbc_textFieldHigherGeog.insets = new Insets(0, 0, 5, 0);
						gbc_textFieldHigherGeog.fill = GridBagConstraints.HORIZONTAL;
						gbc_textFieldHigherGeog.gridx = 1;
						gbc_textFieldHigherGeog.gridy = 12;
						*/
//jPanel1.add(getTextFieldHigherGeog(), gbc_textFieldHigherGeog);
        }
        return jPanel1
    }

    private fun initializeBasicGridBag(): GridBagConstraints {
        val c = GridBagConstraints()
        c.insets = Insets(0, 0, 5, 0)
        return c
    }

    private fun initializeBasicGridBag(grid_x: Int, grid_y: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag()
        c.gridx = grid_x
        c.gridy = grid_y
        return c
    }

    private fun initializeBasicGridBag(grid_x: Int, grid_y: Int, anchor: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y)
        c.anchor = anchor
        return c
    }

    private fun initializeBasicResizableGridBag(grid_x: Int, grid_y: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y)
        c.fill = GridBagConstraints.BOTH
        return c
    }

    private fun initializeBasicResizableGridBag(grid_x: Int, grid_y: Int, anchor: Int): GridBagConstraints {
        val c: GridBagConstraints = this.initializeBasicGridBag(grid_x, grid_y, anchor)
        c.fill = GridBagConstraints.BOTH
        return c
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val drawerNumberJTextField: JTextField?
        private get() {
            if (jTextFieldDrawerNumber == null) {
                jTextFieldDrawerNumber = JTextField()
            }
            return jTextFieldDrawerNumber
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val barcodeJTextField: JTextField?
        private get() {
            if (jTextFieldBarcode == null) {
                jTextFieldBarcode = JTextField()
            }
            return jTextFieldBarcode
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val familyJTextField: JTextField?
        private get() {
            if (jTextFieldFamily == null) {
                jTextFieldFamily = JTextField()
            }
            return jTextFieldFamily
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val genusJTextField: JTextField?
        private get() {
            if (jTextFieldGenus == null) {
                jTextFieldGenus = JTextField()
            }
            return jTextFieldGenus
        }

    /**
     * This method initializes jTextField4
     *
     * @return javax.swing.JTextField
     */
    private val speciesJTextField: JTextField?
        private get() {
            if (jTextFieldSpecies == null) {
                jTextFieldSpecies = JTextField()
            }
            return jTextFieldSpecies
        }

    /**
     * This method initializes jComboBox labelled collection
     *
     * @return javax.swing.JComboBox
     */
    private val collectionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCollection == null) {
                val sls = SpecimenLifeCycle()
                jComboBoxCollection = JComboBox<String?>()
                jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.getDistinctCollections()))
                jComboBoxCollection.setEditable(true)
                jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
                jComboBoxCollection.setMaximumSize(maxComboBoxDims)
                AutoCompleteDecorator.decorate(jComboBoxCollection)
            }
            return jComboBoxCollection
        }

    /**
     * This method initializes jComboBox1 labelled workflow
     *
     * @return javax.swing.JComboBox
     */
    private val workflowsJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxWorkflowStatus == null) {
                val values = ArrayList<String?>()
                values.add("")
                val wfsv: Array<String?> = WorkFlowStatus.getWorkFlowStatusValues()
                for (x in wfsv.indices) {
                    values.add(wfsv[x])
                }
                jComboBoxWorkflowStatus = JComboBox<Any?>(values.toTypedArray())
                jComboBoxWorkflowStatus.getModel().setSelectedItem("")
                jComboBoxWorkflowStatus.setEditable(true)
                jComboBoxWorkflowStatus.setMaximumSize(maxComboBoxDims)
                AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus)
            }
            return jComboBoxWorkflowStatus
        }

    /**
     * This method initializes jTextField5
     *
     * @return javax.swing.JTextField
     */
    private val imageFileJTextField: JTextField?
        private get() {
            if (jTextFieldImageFilename == null) {
                jTextFieldImageFilename = JTextField()
            }
            return jTextFieldImageFilename
        }

    /**
     * This method initializes jComboBox2
     *
     * @return javax.swing.JComboBox
     */
    private val imagePathJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxPath == null) {
                val ils = ICImageLifeCycle()
                jComboBoxPath = JComboBox<Any?>(ils.getDistinctPaths())
                jComboBoxPath.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxPath)
                jComboBoxPath.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxPath
        }

    /**
     * This method initializes the combobox containing the users labelled "entry by"
     *
     * @return javax.swing.JComboBox
     */
    private val usersJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxEntryBy == null) {
                val tls = TrackingLifeCycle()
                jComboBoxEntryBy = JComboBox<Any?>(tls.getDistinctUsers())
                jComboBoxEntryBy.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxEntryBy)
                jComboBoxEntryBy.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxEntryBy
        }

    /**
     * This method initializes the tracking user combo box
     *
     * @return java.swing.JComboBox
     */
    private val identifiedByComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxIdentifiedBy == null) {
                val sls = SpecimenLifeCycle()
                jComboBoxIdentifiedBy = JComboBox<Any?>(sls.getDistinctDeterminers())
                jComboBoxIdentifiedBy.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxIdentifiedBy)
                jComboBoxIdentifiedBy.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxIdentifiedBy
        }

    /**
     * This method initializes jTextField providing subfamily info
     *
     * @return javax.swing.JTextField
     */
    private val subfamilyJTextField: JTextField?
        private get() {
            if (jTextFieldSubfamily == null) {
                jTextFieldSubfamily = JTextField()
            }
            return jTextFieldSubfamily
        }

    /**
     * This method initializes jTextField labelled subspecies
     *
     * @return javax.swing.JTextField
     */
    private val subspeciesJTextField: JTextField?
        private get() {
            if (jTextFieldSubspecies == null) {
                jTextFieldSubspecies = JTextField()
            }
            return jTextFieldSubspecies
        }

    private val interpretedDateTextField: JTextField?
        private get() {
            if (jTextFieldInterpretedDate == null) {
                jTextFieldInterpretedDate = JTextField()
            }
            return jTextFieldInterpretedDate
        }

    /**
     * This method initializes the button to close the form
     * it might be slightly unnecessary as the behaviour is the same as the OS's window close button
     *
     * @return javax.swing.JButton
     */
    private val closeJButton: JButton?
        private get() {
            if (jButton1 == null) {
                jButton1 = JButton()
                jButton1.setText("Close")
                jButton1.setMnemonic(KeyEvent.VK_C)
                jButton1.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        setVisible(false)
                    }
                })
            }
            return jButton1
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val collectorsJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCollector == null) {
                val cls = CollectorLifeCycle()
                jComboBoxCollector = JComboBox<Any?>(cls.getDistinctCollectors())
                jComboBoxCollector.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxCollector)
                jComboBoxCollector.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxCollector
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val verbatimLocalityJTextField: JTextField?
        private get() {
            if (jTextFieldVerbatimLocality == null) {
                jTextFieldVerbatimLocality = JTextField()
            }
            return jTextFieldVerbatimLocality
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val countryJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxCountry == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val cv: Array<String?> = sls.getDistinctCountries()
                for (x in cv.indices) {
                    values.add(cv[x])
                }
                jComboBoxCountry = JComboBox<Any?>(values.toTypedArray())
                jComboBoxCountry.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxCountry)
                jComboBoxCountry.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxCountry
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val primaryDivisionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxPrimaryDivision == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val cv: Array<String?> = sls.getDistinctPrimaryDivisions()
                for (x in cv.indices) {
                    values.add(cv[x])
                }
                jComboBoxPrimaryDivision = JComboBox<Any?>(values.toTypedArray())
                jComboBoxPrimaryDivision.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision)
                jComboBoxPrimaryDivision.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxPrimaryDivision
        }

    /**
     * This method initializes jComboBox1
     *
     * @return javax.swing.JComboBox
     */
    private val questionJComboBox: JComboBox<*>?
        private get() {
            if (jComboBoxQuestions == null) {
                val sls = SpecimenLifeCycle()
                val values = ArrayList<String?>()
                values.add("")
                values.add("%_%")
                val qv: Array<String?> = sls.getDistinctQuestions()
                for (x in qv.indices) {
                    values.add(qv[x])
                }
                jComboBoxQuestions = JComboBox<Any?>(values.toTypedArray())
                jComboBoxQuestions.setEditable(true)
                AutoCompleteDecorator.decorate(jComboBoxQuestions)
                jComboBoxQuestions.setMaximumSize(maxComboBoxDims)
            }
            return jComboBoxQuestions
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val tribeJTextField: JTextField?
        private get() {
            if (jTextFieldTribe == null) {
                jTextFieldTribe = JTextField()
            }
            return jTextFieldTribe
        }

    private val offsetJIntegerField: JIntegerField?
        private get() {
            if (jOffsetNumberField == null) {
                jOffsetNumberField = JIntegerField()
                jOffsetNumberField.setValue(0)
            }
            return jOffsetNumberField
        }

    private val limitJIntegerField: JIntegerField?
        private get() {
            if (jLimitNumberField == null) {
                jLimitNumberField = JIntegerField()
                jLimitNumberField.setValue(100)
            }
            return jLimitNumberField
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
/*private JTextField getJTextFieldPrimaryDivision() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
		}
		return jTextFieldPrimaryDivision;
	}*/
    private fun getTextFieldHigherGeog(): JTextField? {
        if (textFieldHigherGeog == null) {
            textFieldHigherGeog = JTextField()
            textFieldHigherGeog.setColumns(10)
        }
        return textFieldHigherGeog
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * @param owner
     */
    init {
        initialize()
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
