/**
 * SpecimenDetailsViewPane.java
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

edu.harvard.mcz.imagecapture.data .HibernateUtilimport edu.harvard.mcz.imagecapture.entity.Collectorimport edu.harvard.mcz.imagecapture.entity.Numberimport edu.harvard.mcz.imagecapture.entity.fixed.Featuresimport org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport org.hibernate.TransactionExceptionimport java.awt.Colorimport java.awt.Cursorimport java.awt.Dimensionimport java.awt.event.ActionEventimport java.awt.event.ComponentAdapterimport java.awt.event.KeyAdapterimport java.awt.event.KeyEvent
import java.lang.Exceptionimport

java.net.URLimport java.util.*import javax.persistence.OptimisticLockExceptionimport

javax.swing.table.TableColumnimport kotlin.collections.HashSet

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * JPanel for editing a record of a Specimen in a details view for that specimen.
 *
 *
 * TODO: BugID: 10 add length limits (remaining to do for Number/Collector tables,
 * and for JComboBox fields).
 */
class SpecimenDetailsViewPane(aSpecimenInstance: Specimen?, aController: SpecimenController?) : JPanel() {
    private var previousSpecimen: Specimen? = null
    private val specimen //  @jve:decl-index=0:
            : Specimen?
    private var specimenController: SpecimenController? = null
    private var state // dirty if data in controls has been changed and not saved to specimen. = 0
    // private JTextField jTextFieldPreparationType = null;
//allie change
//private JTextField jTextFieldCountry = null;
//private JTextField jTextFieldPrimaryDivision = null;
    private val comboBoxHigherGeog: FilteringGeogJComboBox? = null
    private var dateEmergedButton: JButton? = null
    private var dateCollectedButton: JButton? = null
    private var jButtonAddPreparationType: JButton? = null
    private var jButtonCollectorAdd: JButton? = null
    private var jButtonCopy: JButton? = null
    private var jButtonDeterminations: JButton? = null
    private var jButtonGeoReference: JButton? = null
    private var jButtonGetHistory: JButton? = null
    private var jButtonNext: JButton? = null
    private var jButtonNumbersAdd: JButton? = null
    private var jButtonPaste: JButton? = null
    private var jButtonPrevious: JButton? = null
    private var jButtonSave: JButton? = null
    private var jButtonSpecificLocality: JButton? = null
    private var jCheckBoxValidDistributionFlag: JCheckBox? = null
    private var cbTypeStatus: JComboBox<String?>? = null
    private var comboBoxElevUnits: JComboBox<String?>? = null
    private var jCBDeterminer: JComboBox<String?>? = null
    private var jComboBoxCollection: JComboBox<String?>? = null
    private var jComboBoxCountry: JComboBox<String?>? = null
    private var jComboBoxFamily: JComboBox<String?>? = null
    private var jComboBoxFeatures: JComboBox<String?>? = null
    private var jComboBoxLifeStage: JComboBox<String?>? = null
    private var jComboBoxLocationInCollection: JComboBox<String?>? = null
    private var jComboBoxNatureOfId: JComboBox<String?>? = null
    private var jComboBoxPrimaryDivision: JComboBox<String?>? = null
    private var jComboBoxSex: JComboBox<String?>? = null
    private var jComboBoxSubfamily: JComboBox<String?>? = null
    private var jComboBoxWorkflowStatus: JComboBox<String?>? = null
    private var jLabelDBId: JLabel? = null
    private var jPanel: JPanel? = null
    private val jPanel1: JPanel? = null // panel for navigation buttons
    private val jPopupCollectors: JPopupMenu? = null
    private val jPopupNumbers: JPopupMenu? = null
    private val jPopupSpecimenParts: JPopupMenu? = null
    private var jScrollPaneCollectors: JScrollPane? = null
    private var jScrollPaneNotes: JScrollPane? = null
    private var jScrollPaneNumbers: JScrollPane? = null
    private var jScrollPaneSpecimenParts: JScrollPane? = null
    private var jTableCollectors: JTableWithRowBorder? = null
    private var jTableNumbers: JTableWithRowBorder? = null
    private var jTableSpecimenParts: JTableWithRowBorder? = null
    private var jTextAreaSpecimenNotes: JTextArea? = null
    private var jTextFieldAssociatedTaxon: JTextField? = null
    private var jTextFieldAuthorship: JTextField? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jTextFieldCitedInPub: JTextField? = null
    private var jTextFieldCollectingMethod: JTextField? = null
    private var jTextFieldCreator: JTextField? = null
    private var jTextFieldDateCollected: JTextField? = null
    private var jTextFieldDateCollectedIndicator: JTextField? = null
    private var jTextFieldDateCreated: JTextField? = null
    private var jTextFieldDateDetermined: JTextField? = null
    private var jTextFieldDateEmerged: JTextField? = null
    private var jTextFieldDateEmergedIndicator: JTextField? = null
    private var jTextFieldDateLastUpdated: JTextField? = null
    private var jTextFieldDateNos: JTextField? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var jTextFieldGenus: JTextField? = null
    private var jTextFieldHabitat: JTextField? = null
    private var jTextFieldISODate: JTextField? = null
    private var jTextFieldIdRemarks: JTextField? = null
    private var jTextFieldImageCount: JTextField? = null
    private var jTextFieldInferences: JTextField? = null
    private var jTextFieldInfraspecificEpithet: JTextField? = null
    private var jTextFieldInfraspecificRank: JTextField? = null
    private var jTextFieldLastUpdatedBy: JTextField? = null
    private var jTextFieldLocality: JTextField? = null
    private var jTextFieldMigrationStatus: JTextField? = null
    private var jTextFieldMinElevation: JTextField? = null
    private var jTextFieldQuestions: JTextField? = null
    private var jTextFieldSpecies: JTextField? = null
    private var jTextFieldStatus: JTextField? = null
    private var jTextFieldSubspecies: JTextField? = null
    private var jTextFieldTribe: JTextField? = null
    private var jTextFieldUnnamedForm: JTextField? = null
    private var jTextFieldVerbatimLocality: JTextField? = null
    private var textFieldMaxElev: JTextField? = null
    private var textFieldMicrohabitat: JTextField? = null
    private var thisPane: SpecimenDetailsViewPane? = null
    private val higherGeogNotFoundWarning = StringBuffer()
    private val clickedOnCollsRow = 0
    private val clickedOnNumsRow = 0
    private val clickedOnPartsRow = 0
    /**
     * Initializes the specimen details view pane.
     * Note, contains comments indicating how to enable visual designer with this class.
     */
    private fun initialize() {
        thisPane = this
        val borderLayout = BorderLayout()
        borderLayout.setHgap(0)
        borderLayout.setVgap(0)
        this.setLayout(borderLayout)
        this.add(getJTextFieldStatus(), BorderLayout.SOUTH)
        // Un-comment this line to use design tool.
//    this.add(getJPanel(), BorderLayout.CENTER);
// Comment this block out to use design tool.
//   see also getCbTypeStatus
        val scrollPane = JScrollPane(getJPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
        scrollPane.getVerticalScrollBar().setUnitIncrement(16)
        scrollPane.setBorder(BorderFactory.createEmptyBorder())
        this.add(scrollPane, BorderLayout.CENTER)
        this.setMinimumSize(Dimension(100, 100))
    }

    fun setWarning(warning: String?) {
        jTextFieldStatus.setText(warning)
        jTextFieldStatus.setForeground(Color.RED)
    }

    private fun setWarnings() {
        log.debug("In set warnings")
        if (specimen.getICImages() != null) {
            log.debug("specimen.getICImages is not null")
            val i: MutableIterator<ICImage?> = specimen.getICImages().iterator()
            log.debug(i.hasNext())
            while (i.hasNext()) {
                log.debug("Checking image $i")
                val im: ICImage? = i.next()
                var rbc = ""
                if (im.getRawBarcode() != null) {
                    rbc = im.getRawBarcode()
                }
                var ebc = ""
                if (im.getRawExifBarcode() != null) {
                    ebc = im.getRawExifBarcode()
                }
                if (rbc != ebc) { // warn of mismatch, but only if configured to expect both to be present.
                    if (Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REDUNDANT_COMMENT_BARCODE) == "true") {
                        setWarning("Warning: An image has mismatch between Comment and Barcode.")
                        log.debug("Setting: Warning: Image has mismatch between Comment and Barcode.")
                    }
                }
            }
        }
        if (higherGeogNotFoundWarning != null && higherGeogNotFoundWarning.length > 0) {
            setWarning(higherGeogNotFoundWarning.toString())
        }
    }

    fun setStatus(status: String) {
        log.info("Setting status to: $status")
        jTextFieldStatus.setText(status)
        jTextFieldStatus.setForeground(Color.BLACK)
    }

    private fun save() {
        try {
            thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        } catch (ex: Exception) {
            log.error(ex)
        }
        try {
            setStatus("Saving")
            if (jTableCollectors.isEditing()) {
                jTableCollectors.getCellEditor().stopCellEditing()
            }
            if (jTableSpecimenParts.isEditing()) {
                jTableSpecimenParts.getCellEditor().stopCellEditing()
            }
            if (jTableNumbers.isEditing()) {
                jTableNumbers.getCellEditor().stopCellEditing()
            }
            if (cbTypeStatus.getSelectedIndex() == -1 && cbTypeStatus.getSelectedItem() == null) {
                specimen.setTypeStatus(Specimen.Companion.STATUS_NOT_A_TYPE)
            } else {
                specimen.setTypeStatus(cbTypeStatus.getSelectedItem() as String)
            }
            specimen.setMicrohabitat(textFieldMicrohabitat.getText())
            if (jComboBoxLocationInCollection.getSelectedItem() != null) {
                specimen.setLocationInCollection(jComboBoxLocationInCollection.getSelectedItem().toString())
            }
            specimen.setDrawerNumber(jTextFieldDrawerNumber.getText())
            if (jComboBoxFamily.getSelectedIndex() == -1 && jComboBoxFamily.getSelectedItem() == null) {
                specimen.setFamily("")
            } else {
                specimen.setFamily(jComboBoxFamily.getSelectedItem().toString())
            }
            if (jComboBoxSubfamily.getSelectedIndex() == -1 && jComboBoxSubfamily.getSelectedItem() == null) {
                specimen.setSubfamily("")
            } else {
                specimen.setSubfamily(jComboBoxSubfamily.getSelectedItem().toString())
            }
            specimen.setTribe(jTextFieldTribe.getText())
            specimen.setGenus(jTextFieldGenus.getText())
            specimen.setSpecificEpithet(jTextFieldSpecies.getText())
            specimen.setSubspecificEpithet(jTextFieldSubspecies.getText())
            specimen.setInfraspecificEpithet(jTextFieldInfraspecificEpithet.getText())
            specimen.setInfraspecificRank(jTextFieldInfraspecificRank.getText())
            specimen.setAuthorship(jTextFieldAuthorship.getText())
            //TODO: handle the collectors set!
//this returns TRUE for the copied item!!
            log.debug("in save(). specimen numbers size: " + specimen.getNumbers().size)
            log.debug("okok in save(), specimenid is " + specimen.getSpecimenId())
            if (previousSpecimen != null && previousSpecimen.getNumbers() != null) {
                log.debug("in save(). prev specimen numbers size: " + previousSpecimen.getNumbers().size)
                //specimen.setNumbers(previousSpecimen.getNumbers()); - this gives hibernate exceptions here too!
                log.debug("okok in save(), prev specimenid is " + previousSpecimen.getSpecimenId())
            }
            specimen.setIdentifiedBy(jCBDeterminer.getSelectedItem() as String)
            specimen.setDateIdentified(jTextFieldDateDetermined.getText())
            specimen.setIdentificationRemarks(jTextFieldIdRemarks.getText())
            if (jComboBoxNatureOfId.getSelectedIndex() == -1 && jComboBoxNatureOfId.getSelectedItem() == null) { //specimen.setNatureOfId(NatureOfId.LEGACY);
                specimen.setNatureOfId(NatureOfId.EXPERT_ID)
            } else {
                specimen.setNatureOfId(jComboBoxNatureOfId.getSelectedItem() as String)
            }
            specimen.setUnNamedForm(jTextFieldUnnamedForm.getText())
            specimen.setVerbatimLocality(jTextFieldVerbatimLocality.getText())
            specimen.setCountry(jComboBoxCountry.getSelectedItem() as String)
            specimen.setValidDistributionFlag(jCheckBoxValidDistributionFlag.isSelected())
            specimen.setPrimaryDivison(jComboBoxPrimaryDivision.getSelectedItem() as String)
            specimen.setSpecificLocality(jTextFieldLocality.getText())
            // Elevations
            val min_elev: Long?
            min_elev = if (jTextFieldMinElevation.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    jTextFieldMinElevation.getText().toLong()
                } catch (e: NumberFormatException) {
                    null
                }
            }
            specimen.setMinimum_elevation(min_elev)
            val max_elev: Long?
            max_elev = if (textFieldMaxElev.getText().trim({ it <= ' ' }).length == 0) {
                null
            } else {
                try {
                    textFieldMaxElev.getText().toLong()
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
            specimen.setCollectingMethod(jTextFieldCollectingMethod.getText())
            specimen.setIsoDate(jTextFieldISODate.getText())
            specimen.setDateNos(jTextFieldDateNos.getText())
            specimen.setDateCollected(jTextFieldDateCollected.getText())
            specimen.setDateEmerged(jTextFieldDateEmerged.getText())
            specimen.setDateCollectedIndicator(jTextFieldDateCollectedIndicator.getText())
            specimen.setDateEmergedIndicator(jTextFieldDateEmergedIndicator.getText())
            if (jComboBoxCollection.getSelectedIndex() == -1 && jComboBoxCollection.getSelectedItem() == null) {
                specimen.setCollection("")
            } else {
                specimen.setCollection(jComboBoxCollection.getSelectedItem().toString())
            }
            if (jComboBoxFeatures.getSelectedIndex() == -1 && jComboBoxFeatures.getSelectedItem() == null) {
                specimen.setFeatures("")
            } else {
                specimen.setFeatures(jComboBoxFeatures.getSelectedItem().toString())
            }
            if (jComboBoxLifeStage.getSelectedIndex() == -1 && jComboBoxLifeStage.getSelectedItem() == null) {
                specimen.setLifeStage("")
            } else {
                specimen.setLifeStage(jComboBoxLifeStage.getSelectedItem().toString())
            }
            if (jComboBoxSex.getSelectedIndex() == -1 && jComboBoxSex.getSelectedItem() == null) {
                specimen.setSex("")
            } else {
                specimen.setSex(jComboBoxSex.getSelectedItem().toString())
                log.debug("jComboBoxSex selectedIndex=" + jComboBoxSex.getSelectedIndex())
            }
            log.debug("sex=" + specimen.getSex())
            specimen.setCitedInPublication(jTextFieldCitedInPub.getText())
            //specimen.setPreparationType(jTextFieldPreparationType.getText());
            specimen.setAssociatedTaxon(jTextFieldAssociatedTaxon.getText())
            specimen.setHabitat(jTextFieldHabitat.getText())
            specimen.setMicrohabitat(textFieldMicrohabitat.getText())
            specimen.setSpecimenNotes(jTextAreaSpecimenNotes.getText())
            specimen.setInferences(jTextFieldInferences.getText())
            specimen.setLastUpdatedBy(Singleton.Companion.getSingletonInstance().getUserFullName())
            specimen.setDateLastUpdated(Date())
            specimen.setWorkFlowStatus(jComboBoxWorkflowStatus.getSelectedItem().toString())
            specimen.setQuestions(jTextFieldQuestions.getText())
            try { // make sure specimen controller does not throw null pointer exception – whyever
                if (specimenController.getSpecimen() == null) {
                    specimenController.setSpecimen(specimen)
                }
                specimenController.save() // save the record
                setStateToClean() // enable the navigation buttons
                setStatus("Saved") // inform the user
                jTextFieldStatus.setForeground(Color.BLACK)
                setWarnings()
                jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy())
                jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString())
            } catch (e: SaveFailedException) {
                setStateToDirty() // disable the navigation buttons
                setWarning("Error: " + e.message)
            }
            val sls = SpecimenLifeCycle()
            Singleton.Companion.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCount())
        } catch (e: OptimisticLockException) { // Oh, well. Issues with foreign keys already deleting items, which are not found afterwards.
// We catch these here and silence them. TODO: resolve by changing database structure
// We might also catch unwanted ones; böh, too bad – alert the user just in case.
            log.error(e)
            setWarning("Error: " + e.message)
        } catch (e: Exception) { // trap any exception and notify the user
            setStateToDirty() // disable the navigation buttons
            setWarning("Error: " + e.message)
            log.error(e)
            throw e
        }
        updateContentDependentLabels()
        try {
            thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        } catch (ex: Exception) {
            log.error(ex)
        }
    }

    /**
     * Set the fields values to the ones of the previous specimen
     */
    private fun copyPreviousRecord() {
        log.debug("calling copyPreviousRecord()")
        //thisPane.setStateToDirty();
        jTextFieldDateDetermined.setText(previousSpecimen.getDateIdentified())
        jCBDeterminer.setSelectedItem(previousSpecimen.getIdentifiedBy())
        jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality())
        jComboBoxCountry.setSelectedItem(previousSpecimen.getCountry())
        jComboBoxPrimaryDivision.setSelectedItem(previousSpecimen.getPrimaryDivison())
        // Elevations
        try {
            jTextFieldMinElevation.setText(java.lang.Long.toString(previousSpecimen.getMinimum_elevation()))
        } catch (e: Exception) {
            jTextFieldMinElevation.setText("")
        }
        try {
            textFieldMaxElev.setText(java.lang.Long.toString(previousSpecimen.getMaximum_elevation()))
        } catch (e: Exception) {
            textFieldMaxElev.setText("")
        }
        if (previousSpecimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(previousSpecimen.getElev_units())
        } else {
            comboBoxElevUnits.setSelectedItem("")
        }
        jTextFieldLocality.setText(previousSpecimen.getSpecificLocality())
        jComboBoxCollection.setSelectedItem(previousSpecimen.getCollection())
        jTextFieldDateNos.setText(previousSpecimen.getDateNos())
        jTextFieldISODate.setText(previousSpecimen.getIsoDate())
        jTextFieldDateEmerged.setText(previousSpecimen.getDateEmerged())
        jTextFieldDateCollectedIndicator.setText(previousSpecimen.getDateCollectedIndicator())
        jTextFieldDateEmergedIndicator.setText(previousSpecimen.getDateEmergedIndicator())
        jTextFieldDateCollected.setText(previousSpecimen.getDateCollected())
        jComboBoxLifeStage.setSelectedItem(previousSpecimen.getLifeStage())
        jComboBoxSex.setSelectedItem(previousSpecimen.getSex())
        jTextFieldAssociatedTaxon.setText(previousSpecimen.getAssociatedTaxon())
        jTextFieldHabitat.setText(previousSpecimen.getHabitat())
        textFieldMicrohabitat.setText(previousSpecimen.getMicrohabitat())
        jTextAreaSpecimenNotes.setText(previousSpecimen.getSpecimenNotes())
        jTextFieldInferences.setText(previousSpecimen.getInferences())
        //+numbers
        specimen.getNumbers().clear()
        for (number in previousSpecimen.getNumbers()) { //specimen.getNumbers().add((Number.class)iter.next());
            val n = (number.clone() as Number)
            n.setSpecimen(specimen)
            specimen.getNumbers().add(n)
        }
        jTableNumbers.setModel(NumberTableModel(specimen.getNumbers()))
        setupNumberJTableRenderer()
        //+ verify the georeference data (we do want it all copied)
//+ preparation type (the whole table!) = specimen parts
        specimen.getSpecimenParts().clear()
        for (specimenPart in previousSpecimen.getSpecimenParts()) {
            val part: SpecimenPart = specimenPart.clone() as SpecimenPart
            part.setSpecimen(specimen)
            specimen.getSpecimenParts().add(part)
        }
        jTableSpecimenParts.setModel(SpecimenPartsTableModel(specimen.getSpecimenParts()))
        setupSpecimenPartsJTableRenderer()
        //+collectors
        specimen.getCollectors().clear()
        for (collector in previousSpecimen.getCollectors()) {
            val c: Collector = collector.clone() as Collector
            c.setSpecimen(specimen)
            specimen.getCollectors().add(c)
        }
        jTableCollectors.setModel(CollectorTableModel(specimen.getCollectors()))
        setupCollectorJTableRenderer()
        //+determinations
        specimen.getDeterminations().clear()
        for (prevdet in previousSpecimen.getDeterminations()) {
            val newdet: Determination = prevdet.clone()
            newdet.setSpecimen(specimen)
            specimen.getDeterminations().add(newdet)
        }
        //+georeference
        specimen.getLatLong().clear()
        // prepare hash set as otherwise, in getLatLong(), an empty LatLong is returned
        val latLongs: HashSet<LatLong?> = HashSet<LatLong?>()
        for (prevgeo in previousSpecimen.getLatLong()) {
            val newgeo: LatLong = prevgeo.clone()
            log.debug("Got newgeo with lat " + newgeo.getDecLat())
            newgeo.setSpecimen(specimen)
            latLongs.add(newgeo)
        }
        specimen.setLatLong(latLongs)
        //new - verbatim locality
        jTextFieldVerbatimLocality.setText(previousSpecimen.getVerbatimLocality())
        //new - publications
        jTextFieldCitedInPub.setText(previousSpecimen.getCitedInPublication())
        //new - features
        jComboBoxFeatures.setSelectedItem(previousSpecimen.getFeatures())
        //new - collecting method
        jTextFieldCollectingMethod.setText(previousSpecimen.getCollectingMethod())
        updateContentDependentLabels()
    }

    /**
     * Set the values of the fields to the ones of the specimen
     * TODO: refactor to unused: move to instantiation of fields, e.g.
     */
    private fun setValues() {
        log.debug("okok setting values, specimenid is " + specimen.getSpecimenId())
        setStatus("Setting values")
        jTextFieldBarcode.setText(specimen.getBarcode())
        //alliefix - set to value from properties
//jComboBoxLocationInCollection.setSelectedItem(specimen.getLocationInCollection());
        val locationInCollectionPropertiesVal: String = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(
                ImageCaptureProperties.Companion.KEY_DISPLAY_COLLECTION)
        jComboBoxLocationInCollection.setSelectedItem(locationInCollectionPropertiesVal)
        //allie try
/*Set<LatLong> georeferences = specimen.getLatLong();
		log.debug("setvalues: georeferences size is : + " + georeferences.size());
		LatLong georeference_pre = georeferences.iterator().next();
		log.debug("lat is : + " + georeference_pre.getLatDegString());
		log.debug("long is : + " + georeference_pre.getLongDegString());*/cbTypeStatus.setSelectedItem(specimen.getTypeStatus())
        jTextFieldDrawerNumber.setText(specimen.getDrawerNumber())
        jComboBoxFamily.setSelectedItem(specimen.getFamily())
        jComboBoxSubfamily.setSelectedItem(specimen.getSubfamily())
        jTextFieldTribe.setText(specimen.getTribe())
        jTextFieldGenus.setText(specimen.getGenus())
        jTextFieldSpecies.setText(specimen.getSpecificEpithet())
        jTextFieldSubspecies.setText(specimen.getSubspecificEpithet())
        jTextFieldInfraspecificEpithet.setText(specimen.getInfraspecificEpithet())
        jTextFieldInfraspecificRank.setText(specimen.getInfraspecificRank())
        jTextFieldAuthorship.setText(specimen.getAuthorship())
        //allie new - bugfix
        textFieldMicrohabitat.setText(specimen.getMicrohabitat())
        jTextFieldIdRemarks.setText(specimen.getIdentificationRemarks())
        jTextFieldDateDetermined.setText(specimen.getDateIdentified())
        //allie change
//log.debug("jComboBoxLifeStage here!!! specimen life stage is " + specimen.getLifeStage());
        if (specimen.getLifeStage() == null || specimen.getLifeStage() == "") {
            specimen.setLifeStage("adult")
            jComboBoxLifeStage.setSelectedIndex(0)
        }
        //allie change - removed this
//MCZbaseAuthAgentName selection = new MCZbaseAuthAgentName();
//selection.setAgent_name(specimen.getIdentifiedBy());
//((AgentNameComboBoxModel)jCBDeterminer.getModel()).setSelectedItem(selection);
//jCBDeterminer.getEditor().setItem(jCBDeterminer.getModel().getSelectedItem());
//allie change - added this
//jCBDeterminer.setText(specimen.getIdentifiedBy());
        jCBDeterminer.setSelectedItem(specimen.getIdentifiedBy())
        jComboBoxNatureOfId.setSelectedItem(specimen.getNatureOfId())
        jTextFieldUnnamedForm.setText(specimen.getUnNamedForm())
        jTextFieldVerbatimLocality.setText(specimen.getVerbatimLocality())
        // Specimen record contains a string, delegate handling of lookup of object to the combo box model.
//allieremove
// 		log.debug(specimen.getHigherGeography());
// 		((HigherGeographyComboBoxModel)comboBoxHigherGeog.getModel()).setSelectedItem(specimen.getHigherGeography());
// //TODO ? set model not notifying listeners?
// 		higherGeogNotFoundWarning = new StringBuffer();
// 		comboBoxHigherGeog.getEditor().setItem(comboBoxHigherGeog.getModel().getSelectedItem());
// 		if (specimen.getHigherGeography()==null) {
// 			comboBoxHigherGeog.setBackground(Color.YELLOW);
// 		} else {
// 			if (comboBoxHigherGeog.getModel().getSelectedItem()==null) {
// 				comboBoxHigherGeog.setBackground(Color.RED);
// 				higherGeogNotFoundWarning.append("Higher Geog: [").append(specimen.getHigherGeography()).append("] not found. Fix before saving.");
// 			}
// 		}
// 		jTextFieldCountry.setText(specimen.getCountry());
        jComboBoxCountry.setSelectedItem(specimen.getCountry())
        if (specimen.getValidDistributionFlag() != null) {
            jCheckBoxValidDistributionFlag.setSelected(specimen.getValidDistributionFlag())
        } else {
            jCheckBoxValidDistributionFlag.setSelected(false)
        }
        jComboBoxPrimaryDivision.setSelectedItem(specimen.getPrimaryDivison())
        jTextFieldLocality.setText(specimen.getSpecificLocality())
        // Elevations  **********************************************************************
        try {
            jTextFieldMinElevation.setText(java.lang.Long.toString(specimen.getMinimum_elevation()))
        } catch (e: Exception) {
            jTextFieldMinElevation.setText("")
        }
        try {
            textFieldMaxElev.setText(java.lang.Long.toString(specimen.getMaximum_elevation()))
        } catch (e: Exception) {
            textFieldMaxElev.setText("")
        }
        if (specimen.getElev_units() != null) {
            comboBoxElevUnits.setSelectedItem(specimen.getElev_units())
        } else {
            comboBoxElevUnits.setSelectedItem("")
        }
        jTextFieldCollectingMethod.setText(specimen.getCollectingMethod())
        jTextFieldISODate.setText(specimen.getIsoDate())
        jTextFieldDateNos.setText(specimen.getDateNos())
        jTextFieldDateCollected.setText(specimen.getDateCollected())
        jTextFieldDateEmerged.setText(specimen.getDateEmerged())
        jTextFieldDateCollectedIndicator.setText(specimen.getDateCollectedIndicator())
        jTextFieldDateEmergedIndicator.setText(specimen.getDateEmergedIndicator())
        jComboBoxCollection.setSelectedItem(specimen.getCollection())
        //jTextFieldPreparationType.setText(specimen.getPreparationType());
        jTextFieldAssociatedTaxon.setText(specimen.getAssociatedTaxon())
        jTextFieldHabitat.setText(specimen.getHabitat())
        textFieldMicrohabitat.setText(specimen.getMicrohabitat())
        jTextAreaSpecimenNotes.setText(specimen.getSpecimenNotes())
        jComboBoxFeatures.setSelectedItem(specimen.getFeatures())
        jComboBoxLifeStage.setSelectedItem(specimen.getLifeStage())
        jComboBoxSex.setSelectedItem(specimen.getSex())
        jTextFieldCitedInPub.setText(specimen.getCitedInPublication())
        jTextFieldQuestions.setText(specimen.getQuestions())
        jComboBoxWorkflowStatus.setSelectedItem(specimen.getWorkFlowStatus())
        if (specimen.isStateDone()) {
            jTextFieldMigrationStatus.setText("http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.getCatNum())
        } else {
            jTextFieldMigrationStatus.setText("")
        }
        jTextFieldInferences.setText(specimen.getInferences())
        jTextFieldCreator.setText(specimen.getCreatedBy())
        if (specimen.getDateCreated() != null) {
            jTextFieldDateCreated.setText(specimen.getDateCreated().toString())
        }
        jTextFieldLastUpdatedBy.setText(specimen.getLastUpdatedBy())
        if (specimen.getDateLastUpdated() != null) {
            jTextFieldDateLastUpdated.setText(specimen.getDateLastUpdated().toString())
        }
        //allie change
        if (specimen.getNatureOfId() == null || specimen.getNatureOfId() == "") {
            specimen.setLifeStage("expert ID")
            jComboBoxNatureOfId.setSelectedIndex(0)
        }
        //without this, it does save the 1st record, and it does not copy the next record!
        log.debug("setValues calling jTableNumbers.setModel(new NumberTableModel(specimen.getNumbers()));")
        jTableNumbers.setModel(NumberTableModel(specimen.getNumbers()))
        setupNumberJTableRenderer()
        jTableCollectors.setModel(CollectorTableModel(specimen.getCollectors()))
        setupCollectorJTableRenderer()
        jTableSpecimenParts.setModel(SpecimenPartsTableModel(specimen.getSpecimenParts()))
        setupSpecimenPartsJTableRenderer()
        updateContentDependentLabels()
        setWarnings()
        setStateToClean()
        setStatus("Loaded")
    }

    private fun updateDeterminationCount() {
        if (specimen.getDeterminations() == null) {
            setDeterminationCount(0)
        } else {
            setDeterminationCount(specimen.getDeterminations().size)
        }
    }

    private fun setDeterminationCount(count: Int) {
        val detSuffix = if (count == 1) "s" else ""
        jButtonDeterminations.setText("$count Det$detSuffix.")
        jButtonDeterminations.updateUI()
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldStatus(): JTextField? {
        if (jTextFieldStatus == null) {
            jTextFieldStatus = JTextField("Status")
            jTextFieldStatus.setEditable(false)
            jTextFieldStatus.setEnabled(true)
        }
        return jTextFieldStatus
    }

    /**
     * This method initializes jPanel, laying out the UI components.
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel(MigLayout("wrap 4, fillx")) // 4 col layout
            // section: top information
            addBasicJLabel(jPanel, "Barcode")
            jPanel.add(barcodeJTextField, "grow")
            addBasicJLabel(jPanel, "Collection")
            jPanel.add(locationInCollectionJComboBox)
            // row
            addBasicJLabel(jPanel, "Number of Images")
            jPanel.add(jTextFieldImgCount, "grow")
            addBasicJLabel(jPanel, "Migration Status")
            jPanel.add(getJTextFieldMigrationStatus(), "grow")
            // section: family, classification
// row
            addBasicJLabel(jPanel, "Family")
            jPanel.add(familyJTextField, "grow")
            addBasicJLabel(jPanel, "Subfamily")
            jPanel.add(jTextFieldSubfamily, "grow")
            // row
            addBasicJLabel(jPanel, "Genus")
            jPanel.add(genusJTextField, "grow")
            addBasicJLabel(jPanel, "Species")
            jPanel.add(specificEpithetJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Subspecies")
            jPanel.add(subspecifcEpithetJTextField, "grow")
            addBasicJLabel(jPanel, "Tribe")
            jPanel.add(getJTextFieldTribe(), "grow")
            //row
            addBasicJLabel(jPanel, "Infrasubspecific Name")
            jPanel.add(jTextFieldInfraspecificName, "grow")
            addBasicJLabel(jPanel, "Infrasubspecific Rank")
            jPanel.add(getJTextFieldInfraspecificRank(), "grow")
            // section: identification/determination
// row
            addBasicJLabel(jPanel, "ID by")
            jPanel.add(getJCBDeterminer())
            addBasicJLabel(jPanel, "Nature of ID")
            jPanel.add(getJComboBoxNatureOfId())
            // row
            addBasicJLabel(jPanel, "ID Date")
            jPanel.add(getJTextFieldDateDetermined(), "grow")
            jPanel.add(detsJButton)
            jPanel.add(dBIdLabel)
            // row
            addBasicJLabel(jPanel, "ID Remark")
            jPanel.add(getJTextFieldIdRemarks(), "grow, span 3")
            // row
            addBasicJLabel(jPanel, "Author")
            jPanel.add(getJTextFieldAuthorship(), "grow")
            addBasicJLabel(jPanel, "TypeStatus")
            jPanel.add(getCbTypeStatus())
            // section: locale
// row
            addBasicJLabel(jPanel, "Verbatim Locality")
            jPanel.add(verbatimLocalityJTextField, "grow")
            addBasicJLabel(jPanel, "Country")
            jPanel.add(countryJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "State/Province")
            jPanel.add(primaryDivisionJTextField, "grow")
            addBasicJLabel(jPanel, "Valid Dist.")
            jPanel.add(validDistributionJCheckBox)
            // row
            jPanel.add(getJButtonSpecificLocality(), "align label")
            jPanel.add(specificLocalityJTextField, "grow, span 2")
            jPanel.add(jButtonGeoreference)
            // row
            jPanel.add(JLabel("Elevation"), "span, split 6, sizegroup elevation")
            jPanel.add(JLabel("from:"), "align label, sizegroup elevation, right")
            jPanel.add(verbatimElevationJTextField, "grow, sizegroup elevation")
            jPanel.add(JLabel("to:"), "align label, sizegroup elevation, right")
            jPanel.add(getTextFieldMaxElev(), "grow, sizegroup elevation")
            jPanel.add(getComboBoxElevUnits(), "wrap, sizegroup elevation")
            // section: collection
// row
            addBasicJLabel(jPanel, "Collection")
            jPanel.add(jTextFieldCollection, "grow, span 3")
            // double row:
            addBasicJLabel(jPanel, "Collectors")
            jPanel.add(getJScrollPaneCollectors(), "span 2 2, grow")
            addBasicJLabel(jPanel, "Collecting Method")
            jPanel.add(getJButtonCollectorAdd())
            jPanel.add(getJTextFieldCollectingMethod(), "growx, top")
            // row
            addBasicJLabel(jPanel, "Verbatim date")
            jPanel.add(jTextFieldVerbatimDate, "grow")
            addBasicJLabel(jPanel, "yyyy/mm/dd")
            jPanel.add(getJTextFieldISODate(), "grow")
            // row
            jPanel.add(dateEmergedJButton, "align label")
            jPanel.add(getJTextFieldDateEmerged(), "grow")
            addBasicJLabel(jPanel, "Date emerged note")
            jPanel.add(getJTextFieldDateEmergedIndicator(), "grow")
            // row
            jPanel.add(dateCollectedJButton, "align label")
            jPanel.add(getJTextFieldDateCollected(), "grow")
            addBasicJLabel(jPanel, "Date collected note")
            jPanel.add(getJTextFieldDateCollectedIndicator(), "grow")
            // section: pictured specifics
// row
            addBasicJLabel(jPanel, "Habitat")
            jPanel.add(getJTextFieldHabitat(), "grow")
            addBasicJLabel(jPanel, "Microhabitat")
            jPanel.add(getTextFieldMicrohabitat(), "grow")
            // row
            addBasicJLabel(jPanel, "Life Stage")
            jPanel.add(getJComboBoxLifeStage(), "grow")
            addBasicJLabel(jPanel, "Sex")
            jPanel.add(getJComboBoxSex(), "grow")
            // row
            addBasicJLabel(jPanel, "Features")
            jPanel.add(getJComboBoxFeatures(), "wrap")
            // double row:
            addBasicJLabel(jPanel, "Specimen Parts")
            jPanel.add(getJScrollPaneSpecimenParts(), "span 3 2, grow")
            jPanel.add(jButtonAddPrep)
            // row
            addBasicJLabel(jPanel, "Publications")
            jPanel.add(citedInPublicationJTextField, "grow")
            addBasicJLabel(jPanel, "Associated Taxon")
            jPanel.add(associatedTaxonJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Specimen Notes")
            jPanel.add(getJScrollPaneNotes(), "span 3, grow")
            // double row
            addBasicJLabel(jPanel, "Numbers & more")
            jPanel.add(numbersJScrollPane, "span 3 2, grow")
            jPanel.add(getJButtonNumbersAdd())
            // section: other fields
// row
            addBasicJLabel(jPanel, "Drawer Number")
            jPanel.add(drawerNumberJTextField, "grow")
            addBasicJLabel(jPanel, "Inferences")
            jPanel.add(getJTextFieldInferences(), "grow")
            // row
            addBasicJLabel(jPanel, "Created by")
            jPanel.add(creatorJTextField, "grow")
            addBasicJLabel(jPanel, "Creation date")
            jPanel.add(dateCreatedJTextField, "grow")
            // row
            addBasicJLabel(jPanel, "Last updated by")
            jPanel.add(lastUpdatedByJTextField, "grow")
            addBasicJLabel(jPanel, "Last edit date")
            jPanel.add(jTextFieldDateUpdated, "grow")
            // row
            addBasicJLabel(jPanel, "Workflow Status")
            jPanel.add(getJComboBoxWorkflowStatus())
            addBasicJLabel(jPanel, "Unnamed Form")
            jPanel.add(getJTextFieldUnnamedForm(), "grow")
            // row
            addBasicJLabel(jPanel, "Questions")
            jPanel.add(questionsJTextField, "grow, span 3")
            // section: controls
            jPanel.add(getJButtonPaste(), "span, split 6") //, sizegroup controls");
            jPanel.add(jButtonHistory) //, "sizegroup controls");
            jPanel.add(getJButtonPrevious(), "tag back")
            jPanel.add(getJButtonNext(), "tag next")
            jPanel.add(jButtonCopySave, "tag apply") //, "sizegroup controls");
            jPanel.add(saveJButton, "tag apply") //, "sizegroup controls");
        }
        return jPanel
    }

    /**
     * Get the label field containing the id of the specimen
     *
     * @return JLabel the label with the database id
     */
    private val dBIdLabel: JLabel?
        private get() {
            if (jLabelDBId == null) {
                jLabelDBId = JLabel()
            }
            updateDBIdLabel()
            return jLabelDBId
        }

    /**
     * Update the field: data base ID to match the assigned specimen
     */
    private fun updateDBIdLabel() {
        jLabelDBId.setText("DataBase ID: " + specimen.getSpecimenId())
    }

    private fun addBasicJLabel(target: JPanel, labelText: String) {
        val label = JLabel()
        label.setText("$labelText:")
        target.add(label, "tag label, right") //"align label" was removed as requested
    }

    /**
     * This method initializes jTextFieldBarcode
     *
     * @return javax.swing.JTextField
     */
    private val barcodeJTextField: JTextField?
        private get() {
            if (jTextFieldBarcode == null) {
                jTextFieldBarcode = JTextField()
                jTextFieldBarcode.setEditable(false)
                jTextFieldBarcode.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Barcode"))
            }
            return jTextFieldBarcode
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val genusJTextField: JTextField?
        private get() {
            if (jTextFieldGenus == null) {
                jTextFieldGenus = JTextField()
                jTextFieldGenus.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Genus", jTextFieldGenus))
                jTextFieldGenus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Genus"))
                jTextFieldGenus.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldGenus
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val specificEpithetJTextField: JTextField?
        private get() {
            if (jTextFieldSpecies == null) {
                jTextFieldSpecies = JTextField()
                jTextFieldSpecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SpecificEpithet", jTextFieldSpecies))
                jTextFieldSpecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecificEpithet"))
                jTextFieldSpecies.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldSpecies
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val subspecifcEpithetJTextField: JTextField?
        private get() {
            if (jTextFieldSubspecies == null) {
                jTextFieldSubspecies = JTextField()
                jTextFieldSubspecies.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SubspecificEpithet", jTextFieldSubspecies))
                jTextFieldSubspecies.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SubspecificEpithet"))
                jTextFieldSubspecies.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldSubspecies
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val specificLocalityJTextField: JTextField?
        private get() {
            if (jTextFieldLocality == null) {
                jTextFieldLocality = JTextField()
                jTextFieldLocality.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "SpecificLocality", jTextFieldLocality))
                jTextFieldLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecificLocality"))
                jTextFieldLocality.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldLocality
        }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val saveJButton: JButton?
        private get() {
            if (jButtonSave == null) {
                jButtonSave = JButton("Save")
                if (specimen.isStateDone()) {
                    jButtonSave.setEnabled(false)
                    jButtonSave.setText("Migrated " + specimen.getLoadFlags())
                }
                jButtonSave.setMnemonic(KeyEvent.VK_S)
                jButtonSave.setToolTipText("Save changes to this record to the database. No fields should have red backgrounds before you save.")
                jButtonSave.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        save()
                    }
                })
            }
            return jButtonSave
        }//jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldCollection: JComboBox<String?>?
        private get() {
            if (jComboBoxCollection == null) {
                log.debug("init jComboBoxCollection")
                val sls = SpecimenLifeCycle()
                jComboBoxCollection = JComboBox<String?>()
                jComboBoxCollection.setModel(DefaultComboBoxModel<String?>(sls.getDistinctCollections()))
                jComboBoxCollection.setEditable(true)
                //jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
                jComboBoxCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Collection"))
                jComboBoxCollection.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxCollection)
            }
            return jComboBoxCollection
        }//jTextFieldLastUpdatedBy.setEnabled(false);

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val lastUpdatedByJTextField: JTextField?
        private get() {
            if (jTextFieldLastUpdatedBy == null) {
                jTextFieldLastUpdatedBy = JTextField()
                jTextFieldLastUpdatedBy.setEditable(false)
                jTextFieldLastUpdatedBy.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "LastUpdatedBy"))
                //jTextFieldLastUpdatedBy.setEnabled(false);
                jTextFieldLastUpdatedBy.setForeground(Color.BLACK)
            }
            return jTextFieldLastUpdatedBy
        }

    /**
     * This method initializes jScrollPaneCollectors
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneCollectors(): JScrollPane? {
        if (jScrollPaneCollectors == null) {
            jScrollPaneCollectors = basicWrapperJScrollPane
            jScrollPaneCollectors.setViewportView(getJTableCollectors())
            jScrollPaneCollectors.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jScrollPaneCollectors
    }

    /**
     * This method initializes jTableCollectors
     *
     * @return javax.swing.JTable
     */
    private fun getJTableCollectors(): JTable? {
        if (jTableCollectors == null) {
            try {
                jTableCollectors = JTableWithRowBorder(CollectorTableModel(specimen.getCollectors()))
            } catch (e: NullPointerException) {
                jTableCollectors = JTableWithRowBorder(CollectorTableModel())
            }
            setupCollectorJTableRenderer()
            jTableCollectors.setRowHeight(jTableCollectors.getRowHeight() + 5)
            jTableCollectors.setObjectName("Collector")
            jTableCollectors.setParentPane(thisPane)
            jTableCollectors.addListener(object : ActionListener {
                override fun actionPerformed(actionEvent: ActionEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            jTableCollectors.enableDeleteability()
        }
        return jTableCollectors
    }

    /**
     * Setup from time to time the editor
     */
    private fun setupCollectorJTableRenderer() {
        val cls = CollectorLifeCycle()
        val jComboBoxCollector: JComboBox<String?> = JComboBox<String?>(cls.getDistinctCollectors())
        jComboBoxCollector.setEditable(true)
        //field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
        jTableCollectors.getColumnModel().getColumn(0).setCellEditor(ComboBoxCellEditor(jComboBoxCollector))
        AutoCompleteDecorator.decorate(jComboBoxCollector)
    }

    private fun getJScrollPaneSpecimenParts(): JScrollPane? {
        if (jScrollPaneSpecimenParts == null) {
            jScrollPaneSpecimenParts = basicWrapperJScrollPane
            jScrollPaneSpecimenParts.setViewportView(getJTableSpecimenParts())
            jScrollPaneSpecimenParts.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jScrollPaneSpecimenParts
    }

    private fun getJTableSpecimenParts(): JTable? {
        if (jTableSpecimenParts == null) {
            try {
                jTableSpecimenParts = JTableWithRowBorder(SpecimenPartsTableModel(specimen.getSpecimenParts()))
            } catch (e: NullPointerException) {
                jTableSpecimenParts = JTableWithRowBorder(SpecimenPartsTableModel())
            }
            jTableSpecimenParts.getColumnModel().getColumn(0).setPreferredWidth(90)
            jTableSpecimenParts.setRowHeight(jTableSpecimenParts.getRowHeight() + 5)
            setupSpecimenPartsJTableRenderer()
            log.debug(specimen.getSpecimenParts().size)
            jTableSpecimenParts.setObjectName("Specimen Part")
            jTableSpecimenParts.setParentPane(thisPane)
            jTableSpecimenParts.addListener(object : ActionListener {
                override fun actionPerformed(actionEvent: ActionEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            jTableSpecimenParts.enableDeleteability()
        }
        return jTableSpecimenParts
    }

    private fun setupSpecimenPartsJTableRenderer() {
        log.debug("Setting specimen part cell editors")
        val comboBoxPart: JComboBox<String?> = JComboBox<String?>(SpecimenPart.Companion.PART_NAMES)
        getJTableSpecimenParts().getColumnModel().getColumn(0).setCellEditor(DefaultCellEditor(comboBoxPart))
        val comboBoxPrep: JComboBox<String?> = JComboBox<String?>(SpecimenPart.Companion.PRESERVATION_NAMES)
        getJTableSpecimenParts().getColumnModel().getColumn(1).setCellEditor(DefaultCellEditor(comboBoxPrep))
        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellRenderer(ButtonRenderer())
        getJTableSpecimenParts().getColumnModel().getColumn(4).setCellEditor(ButtonEditor(ButtonEditor.Companion.OPEN_SPECIMENPARTATTRIBUTES, this))
    }//jTextFieldDateLastUpdated.setEnabled(false);

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldDateUpdated: JTextField?
        private get() {
            if (jTextFieldDateLastUpdated == null) {
                jTextFieldDateLastUpdated = JTextField()
                jTextFieldDateLastUpdated.setEditable(false)
                //jTextFieldDateLastUpdated.setEnabled(false);
                jTextFieldDateLastUpdated.setForeground(Color.BLACK)
            }
            return jTextFieldDateLastUpdated
        }//jTextFieldCreator.setEnabled(false);

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val creatorJTextField: JTextField?
        private get() {
            if (jTextFieldCreator == null) {
                jTextFieldCreator = JTextField()
                jTextFieldCreator.setEditable(false)
                //jTextFieldCreator.setEnabled(false);
                jTextFieldCreator.setForeground(Color.BLACK)
                jTextFieldCreator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Creator"))
            }
            return jTextFieldCreator
        }//jTextFieldDateCreated.setEnabled(false);

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val dateCreatedJTextField: JTextField?
        private get() {
            if (jTextFieldDateCreated == null) {
                jTextFieldDateCreated = JTextField()
                jTextFieldDateCreated.setEditable(false)
                //jTextFieldDateCreated.setEnabled(false);
                jTextFieldDateCreated.setForeground(Color.BLACK)
            }
            return jTextFieldDateCreated
        }

    /**
     * This method initializes jScrollPaneNumbers
     *
     * @return javax.swing.JScrollPane
     */
    private val numbersJScrollPane: JScrollPane?
        private get() {
            if (jScrollPaneNumbers == null) {
                jScrollPaneNumbers = basicWrapperJScrollPane
                jScrollPaneNumbers.setViewportView(numberJTable)
                jScrollPaneNumbers.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jScrollPaneNumbers
        }

    /**
     * This method initializes jTable for numbers fields
     *
     * @return javax.swing.JTable
     */
    private val numberJTable: JTable?
        private get() {
            if (jTableNumbers == null) {
                try {
                    jTableNumbers = JTableWithRowBorder(NumberTableModel(specimen.getNumbers()))
                } catch (e: NullPointerException) {
                    jTableNumbers = JTableWithRowBorder(NumberTableModel())
                }
                jTableNumbers.setRowHeight(jTableNumbers.getRowHeight() + 5)
                setupNumberJTableRenderer()
                jTableNumbers.setObjectName("Number")
                jTableNumbers.setParentPane(thisPane)
                jTableNumbers.addListener(object : ActionListener {
                    override fun actionPerformed(actionEvent: ActionEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                jTableNumbers.enableDeleteability()
            }
            return jTableNumbers
        }

    /**
     * Setting the model will overwrite the existing cell editor bound
     * to the column model, so we might need to add it again som times.
     * That's what this function does
     */
    private fun setupNumberJTableRenderer() { // First, setup the number field
        val field1 = JTextField()
        field1.setInputVerifier(MetadataRetriever.getInputVerifier(Number::class.java, "Number", field1))
        field1.setVerifyInputWhenFocusTarget(true)
        jTableNumbers.getColumnModel().getColumn(NumberTableModel.Companion.COLUMN_NUMBER).setCellEditor(ValidatingTableCellEditor(field1))
        // Then, setup the type field
        val jComboNumberTypes: JComboBox<String?> = JComboBox<String?>(NumberLifeCycle.Companion.getDistinctTypes())
        jComboNumberTypes.setEditable(true)
        val typeColumn: TableColumn = jTableNumbers.getColumnModel().getColumn(NumberTableModel.Companion.COLUMN_TYPE)
        val comboBoxEditor = ComboBoxCellEditor(jComboNumberTypes)
        AutoCompleteDecorator.decorate(jComboNumberTypes)
        typeColumn.setCellEditor(ComboBoxCellEditor(jComboNumberTypes))
        val renderer = DefaultTableCellRenderer()
        renderer.setToolTipText("Click for pick list of number types.")
        typeColumn.setCellRenderer(renderer)
    }

    /**
     * This method initializes jButtonNumbersAdd
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonNumbersAdd(): JButton? {
        if (jButtonNumbersAdd == null) {
            jButtonNumbersAdd = JButton()
            jButtonNumbersAdd.setText("+")
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png")
            try {
                jButtonNumbersAdd.setText("")
                jButtonNumbersAdd.setIcon(ImageIcon(iconFile))
                jButtonNumbersAdd.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        (jTableNumbers.getModel() as NumberTableModel).addNumber(Number(specimen, "", ""))
                        thisPane!!.setStateToDirty()
                    }
                })
            } catch (e: Exception) {
                jButtonNumbersAdd.setText("+")
            }
        }
        return jButtonNumbersAdd
    }//log.debug("the lat long is : " + specimen.getLatLong().);

    //allie add
/*try {
				Set<LatLong> georeferences = specimen.getLatLong();
				log.debug("getJButtonGeoreference georeferences size is : + " + georeferences.size());
				LatLong georeference_pre = georeferences.iterator().next();
				log.debug("lat is : + " + georeference_pre.getLatDegString());
				log.debug("long is : + " + georeference_pre.getLongDegString());
				if ((!("").equals(georeference_pre.getLatDegString())) ||
					(!("").equals(georeference_pre.getLongDegString()))){
					jButtonGeoreference.setText("1.0 Georeference");
				}else{
					jButtonGeoreference.setText("0.0 Georeference");
				}
	        } catch (Exception e) {
	        	log.error(e.getMessage(), e);
	        }	*/
    private val jButtonGeoreference: JButton?
        private get() {
            if (jButtonGeoReference == null) {
                jButtonGeoReference = JButton()
                //allie add
/*try {
				Set<LatLong> georeferences = specimen.getLatLong();
				log.debug("getJButtonGeoreference georeferences size is : + " + georeferences.size());
				LatLong georeference_pre = georeferences.iterator().next();
				log.debug("lat is : + " + georeference_pre.getLatDegString());
				log.debug("long is : + " + georeference_pre.getLongDegString());
				if ((!("").equals(georeference_pre.getLatDegString())) ||
					(!("").equals(georeference_pre.getLongDegString()))){
					jButtonGeoreference.setText("1.0 Georeference");
				}else{
					jButtonGeoreference.setText("0.0 Georeference");
				}
	        } catch (Exception e) {
	        	log.error(e.getMessage(), e);
	        }	*/try {
                    updateJButtonGeoreference()
                    jButtonGeoReference.addActionListener(object : ActionListener {
                        override fun actionPerformed(e: ActionEvent?) {
                            thisPane!!.setStateToDirty()
                            val georeferences: MutableSet<LatLong?> = specimen.getLatLong()
                            //log.debug("the lat long is : " + specimen.getLatLong().);
                            val georeference: LatLong? = georeferences.iterator().next()
                            georeference.setSpecimen(specimen)
                            val georefDialog = GeoreferenceDialog(georeference)
                            georefDialog.setVisible(true)
                            georefDialog.addComponentListener(object : ComponentAdapter() {
                                override fun componentHidden(e: ComponentEvent?) {
                                    updateJButtonGeoreference()
                                    super.componentHidden(e)
                                    autocompleteGeoDataFromGeoreference()
                                }
                            })
                        }
                    })
                } catch (e: Exception) {
                    log.error(e.message, e)
                }
            }
            return jButtonGeoReference
        }

    private fun updateJButtonGeoreference() {
        if (specimen.getLatLong() != null && !specimen.getLatLong().isEmpty() && !specimen.getLatLong().iterator().next().isEmpty()) {
            jButtonGeoReference.setText("✅ Georeference (1)")
        } else {
            jButtonGeoReference.setText("❔ Georeference (0)")
        }
        jButtonGeoReference.updateUI()
    }

    /**
     * This method initializes jButtonCollsAdd
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonCollectorAdd(): JButton? {
        if (jButtonCollectorAdd == null) {
            jButtonCollectorAdd = JButton()
            jButtonCollectorAdd.setText("+")
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/b_plus.png")
            try {
                jButtonCollectorAdd.setText("")
                jButtonCollectorAdd.setIcon(ImageIcon(iconFile))
                jButtonCollectorAdd.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        log.debug("adding a new collector........")
                        (jTableCollectors.getModel() as CollectorTableModel).addCollector(Collector(specimen, ""))
                        thisPane!!.setStateToDirty()
                    }
                })
            } catch (e: Exception) {
                jButtonCollectorAdd.setText("+")
            }
        }
        return jButtonCollectorAdd
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
                jTextFieldDrawerNumber.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "DrawerNumber", jTextFieldDrawerNumber))
                jTextFieldDrawerNumber.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DrawerNumber"))
                jTextFieldDrawerNumber.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldDrawerNumber
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
                jTextFieldVerbatimLocality.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "VerbatimLocality", jTextFieldVerbatimLocality))
                jTextFieldVerbatimLocality.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "VerbatimLocality"))
                jTextFieldVerbatimLocality.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldVerbatimLocality
        }//jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
    //return jTextFieldCountry;
//if (jTextFieldCountry == null) {
/*jTextFieldCountry = new JTextField();
			jTextFieldCountry.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Country", jTextFieldCountry));
			jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
			jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});*/
//allie fix

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val countryJTextField: JComboBox<String?>?
        private get() { //if (jTextFieldCountry == null) {
/*jTextFieldCountry = new JTextField();
			jTextFieldCountry.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "Country", jTextFieldCountry));
			jTextFieldCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Country"));
			jTextFieldCountry.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});*/
//allie fix
            if (jComboBoxCountry == null) {
                log.debug("init jComboBoxCountry")
                val sls = SpecimenLifeCycle()
                jComboBoxCountry = JComboBox<String?>()
                //jComboBoxCountry.setModel(new DefaultComboBoxModel<String>(HigherTaxonLifeCycle.selectDistinctSubfamily("")));
                jComboBoxCountry.setModel(DefaultComboBoxModel<String?>(sls.getDistinctCountries()))
                jComboBoxCountry.setEditable(true)
                jComboBoxCountry.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Country"))
                jComboBoxCountry.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxCountry)
            }
            return jComboBoxCountry
            //return jTextFieldCountry;
        }//jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
/*private JTextField getJTextField23() {
		if (jTextFieldPrimaryDivision == null) {
			jTextFieldPrimaryDivision = new JTextField();
			jTextFieldPrimaryDivision.setInputVerifier(
					MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
			jTextFieldPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "primaryDivison"));
			jTextFieldPrimaryDivision.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					thisPane.setStateToDirty();
				}
			});
		}
		return jTextFieldPrimaryDivision;
	}*/
//allie change
    private val primaryDivisionJTextField: JComboBox<String?>?
        private get() {
            if (jComboBoxPrimaryDivision == null) {
                jComboBoxPrimaryDivision = JComboBox<String?>()
                jComboBoxPrimaryDivision.setEditable(true)
                //jComboBoxPrimaryDivision.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "primaryDivison", jTextFieldPrimaryDivision));
                val sls = SpecimenLifeCycle()
                jComboBoxPrimaryDivision.setModel(DefaultComboBoxModel<String?>(sls.getDistinctPrimaryDivisions()))
                jComboBoxPrimaryDivision.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "primaryDivison"))
                jComboBoxPrimaryDivision.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxPrimaryDivision)
            }
            return jComboBoxPrimaryDivision
        }//jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Family", jTextFieldFamily));

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val familyJTextField: JComboBox<String?>?
        private get() {
            if (jComboBoxFamily == null) {
                jComboBoxFamily = JComboBox<String?>()
                jComboBoxFamily.setModel(DefaultComboBoxModel<String?>(HigherTaxonLifeCycle.Companion.selectDistinctFamily()))
                jComboBoxFamily.setEditable(true)
                //jTextFieldFamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Family", jTextFieldFamily));
                jComboBoxFamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Family"))
                jComboBoxFamily.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxFamily)
            }
            return jComboBoxFamily
        }//jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Subfamily", jTextFieldSubfamily));

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldSubfamily: JComboBox<String?>?
        private get() {
            if (jComboBoxSubfamily == null) {
                jComboBoxSubfamily = JComboBox<String?>()
                jComboBoxSubfamily.setModel(DefaultComboBoxModel<String?>(HigherTaxonLifeCycle.Companion.selectDistinctSubfamily("")))
                jComboBoxSubfamily.setEditable(true)
                //jTextFieldSubfamily.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Subfamily", jTextFieldSubfamily));
                jComboBoxSubfamily.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Subfamily"))
                jComboBoxSubfamily.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxSubfamily)
            }
            return jComboBoxSubfamily
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldTribe(): JTextField? {
        if (jTextFieldTribe == null) {
            jTextFieldTribe = JTextField()
            jTextFieldTribe.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Tribe", jTextFieldTribe))
            jTextFieldTribe.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Tribe"))
            jTextFieldTribe.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldTribe
    }

    /**
     * This method initializes jComboBoxSex
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxSex(): JComboBox<String?>? {
        if (jComboBoxSex == null) {
            jComboBoxSex = JComboBox<String?>()
            jComboBoxSex.setModel(DefaultComboBoxModel<String?>(Sex.getSexValues()))
            jComboBoxSex.setEditable(true)
            jComboBoxSex.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Sex"))
            jComboBoxSex.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxSex)
        }
        return jComboBoxSex
    }

    /**
     * This method initializes jComboBoxFeatures
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxFeatures(): JComboBox<String?>? {
        if (jComboBoxFeatures == null) {
            jComboBoxFeatures = JComboBox<String?>()
            jComboBoxFeatures.setModel(DefaultComboBoxModel<String?>(Features.getFeaturesValues()))
            jComboBoxFeatures.setEditable(true)
            jComboBoxFeatures.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Features"))
            jComboBoxFeatures.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxFeatures)
            // TODO: validate input length
        }
        return jComboBoxFeatures
    }

    private fun getJComboBoxNatureOfId(): JComboBox<String?>? {
        if (jComboBoxNatureOfId == null) {
            jComboBoxNatureOfId = JComboBox<String?>()
            jComboBoxNatureOfId.setModel(DefaultComboBoxModel<String?>(NatureOfId.getNatureOfIdValues()))
            jComboBoxNatureOfId.setEditable(false)
            jComboBoxNatureOfId.setToolTipText(MetadataRetriever.getFieldHelp(Determination::class.java, "NatureOfId"))
            jComboBoxNatureOfId.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxNatureOfId)
            jComboBoxNatureOfId.setSelectedItem(NatureOfId.EXPERT_ID)
            jComboBoxNatureOfId.setSelectedIndex(0)
        }
        return jComboBoxNatureOfId
    }

    /**
     * This method initializes jComboBoxLifeStage
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxLifeStage(): JComboBox<String?>? {
        if (jComboBoxLifeStage == null) {
            jComboBoxLifeStage = JComboBox<String?>()
            jComboBoxLifeStage.setModel(DefaultComboBoxModel<String?>(LifeStage.Companion.getLifeStageValues()))
            jComboBoxLifeStage.setEditable(true)
            jComboBoxLifeStage.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Lifestage"))
            jComboBoxLifeStage.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jComboBoxLifeStage)
            jComboBoxLifeStage.setSelectedItem("adult")
            jComboBoxLifeStage.setSelectedIndex(0)
        }
        return jComboBoxLifeStage
    }//jTextFieldDateNos.setToolTipText("Date found on labels where date might be either date collected or date emerged, or some other date");

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldVerbatimDate: JTextField?
        private get() {
            if (jTextFieldDateNos == null) {
                jTextFieldDateNos = JTextField()
                //jTextFieldDateNos.setToolTipText("Date found on labels where date might be either date collected or date emerged, or some other date");
                jTextFieldDateNos.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "DateNOS", jTextFieldDateNos))
                jTextFieldDateNos.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateNOS"))
                jTextFieldDateNos.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldDateNos
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateEmerged(): JTextField? {
        if (jTextFieldDateEmerged == null) {
            jTextFieldDateEmerged = JTextField()
            jTextFieldDateEmerged.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "DateEmerged", jTextFieldDateEmerged))
            jTextFieldDateEmerged.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateEmerged"))
            jTextFieldDateEmerged.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateEmerged
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateEmergedIndicator(): JTextField? {
        if (jTextFieldDateEmergedIndicator == null) {
            jTextFieldDateEmergedIndicator = JTextField()
            jTextFieldDateEmergedIndicator.setToolTipText("Verbatim text indicating that this is a date emerged.")
            jTextFieldDateEmergedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "DateEmergedIndicator", jTextFieldDateEmergedIndicator))
            jTextFieldDateEmergedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateEmergedIndicator"))
            jTextFieldDateEmergedIndicator.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateEmergedIndicator
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateCollected(): JTextField? {
        if (jTextFieldDateCollected == null) {
            jTextFieldDateCollected = JTextField()
            jTextFieldDateCollected.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateCollected"))
            jTextFieldDateCollected.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateCollected
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateCollectedIndicator(): JTextField? {
        if (jTextFieldDateCollectedIndicator == null) {
            jTextFieldDateCollectedIndicator = JTextField()
            jTextFieldDateCollectedIndicator.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "DateCollectedIndicator", jTextFieldDateCollectedIndicator))
            jTextFieldDateCollectedIndicator.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateCollectedIndicator"))
            jTextFieldDateCollectedIndicator.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateCollectedIndicator
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldInfraspecificName: JTextField?
        private get() {
            if (jTextFieldInfraspecificEpithet == null) {
                jTextFieldInfraspecificEpithet = JTextField()
                jTextFieldInfraspecificEpithet.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "InfraspecificEpithet", jTextFieldInfraspecificEpithet))
                jTextFieldInfraspecificEpithet.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "InfraspecificEpithet"))
                jTextFieldInfraspecificEpithet.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldInfraspecificEpithet
        }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldInfraspecificRank(): JTextField? {
        if (jTextFieldInfraspecificRank == null) {
            jTextFieldInfraspecificRank = JTextField()
            jTextFieldInfraspecificRank.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "InfraspecificRank", jTextFieldInfraspecificRank))
            jTextFieldInfraspecificRank.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "InfraspecificRank"))
            jTextFieldInfraspecificRank.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldInfraspecificRank
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldAuthorship(): JTextField? {
        if (jTextFieldAuthorship == null) {
            jTextFieldAuthorship = JTextField()
            jTextFieldAuthorship.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Authorship", jTextFieldAuthorship))
            jTextFieldAuthorship.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Authorship"))
            jTextFieldAuthorship.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldAuthorship
    }

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldUnnamedForm(): JTextField? {
        if (jTextFieldUnnamedForm == null) {
            jTextFieldUnnamedForm = JTextField()
            jTextFieldUnnamedForm.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "UnnamedForm", jTextFieldUnnamedForm))
            jTextFieldUnnamedForm.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "UnnamedForm"))
            jTextFieldUnnamedForm.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldUnnamedForm
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val verbatimElevationJTextField: JTextField?
        private get() {
            if (jTextFieldMinElevation == null) {
                jTextFieldMinElevation = JTextField()
                jTextFieldMinElevation.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "VerbatimElevation", jTextFieldMinElevation))
                jTextFieldMinElevation.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "VerbatimElevation"))
                jTextFieldMinElevation.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldMinElevation
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldCollectingMethod(): JTextField? {
        if (jTextFieldCollectingMethod == null) {
            jTextFieldCollectingMethod = JTextField()
            jTextFieldCollectingMethod.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "CollectingMethod", jTextFieldCollectingMethod))
            jTextFieldCollectingMethod.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "CollectingMethod"))
            jTextFieldCollectingMethod.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldCollectingMethod
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private val jTextAreaNotes: JTextArea?
        private get() {
            if (jTextAreaSpecimenNotes == null) {
                jTextAreaSpecimenNotes = JTextArea()
                jTextAreaSpecimenNotes.setRows(3)
                jTextAreaSpecimenNotes.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "SpecimenNotes"))
                jTextAreaSpecimenNotes.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextAreaSpecimenNotes
        }//jCheckBoxValidDistributionFlag.setToolTipText("Check if locality represents natural biological range.  Uncheck for Specimens that came from a captive breeding program");

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private val validDistributionJCheckBox: JCheckBox?
        private get() {
            if (jCheckBoxValidDistributionFlag == null) {
                jCheckBoxValidDistributionFlag = JCheckBox()
                //jCheckBoxValidDistributionFlag.setToolTipText("Check if locality represents natural biological range.  Uncheck for Specimens that came from a captive breeding program");
                jCheckBoxValidDistributionFlag.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ValidDistributionFlag"))
                jCheckBoxValidDistributionFlag.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jCheckBoxValidDistributionFlag
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val questionsJTextField: JTextField?
        private get() {
            if (jTextFieldQuestions == null) {
                jTextFieldQuestions = JTextField()
                jTextFieldQuestions.setBackground(MainFrame.Companion.BG_COLOR_QC_FIELD)
                jTextFieldQuestions.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "Questions", jTextFieldQuestions))
                jTextFieldQuestions.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Questions"))
                jTextFieldQuestions.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldQuestions
        }// TODO Auto-generated catch block

    /**
     * This method initializes jTextField1
     *
     * @return javax.swing.JTextField
     */
    private val jButtonAddPrep: JButton?
        private get() {
            if (jButtonAddPreparationType == null) {
                jButtonAddPreparationType = JButton("Add Prep")
                jButtonAddPreparationType.setMnemonic(KeyEvent.VK_P)
                jButtonAddPreparationType.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        log.debug("Adding new SpecimenPart")
                        val newPart = SpecimenPart()
                        newPart.setPreserveMethod(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_DEFAULT_PREPARATION))
                        newPart.setSpecimen(specimen)
                        val spls = SpecimenPartLifeCycle()
                        log.debug("Attaching new SpecimenPart")
                        try {
                            spls.persist(newPart)
                            specimen.getSpecimenParts().add(newPart)
                            (jTableSpecimenParts.getModel() as AbstractTableModel).fireTableDataChanged()
                            log.debug("Added new SpecimenPart")
                        } catch (e1: SaveFailedException) { // TODO Auto-generated catch block
                            e1.printStackTrace()
                        }
                    }
                })
            }
            return jButtonAddPreparationType
        }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private val associatedTaxonJTextField: JTextField?
        private get() {
            if (jTextFieldAssociatedTaxon == null) {
                jTextFieldAssociatedTaxon = JTextField()
                jTextFieldAssociatedTaxon.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "AssociatedTaxon", jTextFieldAssociatedTaxon))
                jTextFieldAssociatedTaxon.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "AssociatedTaxon"))
                jTextFieldAssociatedTaxon.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldAssociatedTaxon
        }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldHabitat(): JTextField? {
        if (jTextFieldHabitat == null) {
            jTextFieldHabitat = JTextField()
            jTextFieldHabitat.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Habitat", jTextFieldHabitat))
            jTextFieldHabitat.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Habitat"))
            jTextFieldHabitat.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldHabitat
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBoxWorkflowStatus(): JComboBox<String?>? {
        if (jComboBoxWorkflowStatus == null) {
            jComboBoxWorkflowStatus = JComboBox<String?>()
            jComboBoxWorkflowStatus.setModel(DefaultComboBoxModel<String?>(WorkFlowStatus.getWorkFlowStatusValues()))
            jComboBoxWorkflowStatus.setEditable(false)
            jComboBoxWorkflowStatus.setBackground(MainFrame.Companion.BG_COLOR_QC_FIELD)
            jComboBoxWorkflowStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "WorkflowStatus"))
            AutoCompleteDecorator.decorate(jComboBoxWorkflowStatus)
        }
        return jComboBoxWorkflowStatus
    }//alliefix - set default from properties file
//jComboBoxLocationInCollection.setSelectedIndex(1);

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val locationInCollectionJComboBox: JComboBox<String?>?
        private get() {
            if (jComboBoxLocationInCollection == null) {
                jComboBoxLocationInCollection = JComboBox<String?>()
                jComboBoxLocationInCollection.setModel(DefaultComboBoxModel<String?>(LocationInCollection.getLocationInCollectionValues()))
                jComboBoxLocationInCollection.setEditable(false)
                jComboBoxLocationInCollection.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "LocationInCollection"))
                //alliefix - set default from properties file
//jComboBoxLocationInCollection.setSelectedIndex(1);
                jComboBoxLocationInCollection.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
                AutoCompleteDecorator.decorate(jComboBoxLocationInCollection)
            }
            return jComboBoxLocationInCollection
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldInferences(): JTextField? {
        if (jTextFieldInferences == null) {
            jTextFieldInferences = JTextField()
            jTextFieldInferences.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
            jTextFieldInferences.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen::class.java, "Inferences", jTextFieldInferences))
            jTextFieldInferences.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "Inferences"))
            jTextFieldInferences.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldInferences
    }// retrieve and display the tracking events for this specimen
//Tracking t = new Tracking();
//t.setSpecimen(specimen);
    //Request by specimen doesn't work with Oracle.  Why?
//EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimen(specimen)));

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val jButtonHistory: JButton?
        private get() {
            if (jButtonGetHistory == null) {
                jButtonGetHistory = JButton()
                jButtonGetHistory.setText("History")
                jButtonGetHistory.setToolTipText("Show the history of who edited this record")
                jButtonGetHistory.setMnemonic(KeyEvent.VK_H)
                jButtonGetHistory.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) { // retrieve and display the tracking events for this specimen
//Tracking t = new Tracking();
//t.setSpecimen(specimen);
                        val tls = TrackingLifeCycle()
                        //Request by specimen doesn't work with Oracle.  Why?
//EventLogFrame logViewer = new EventLogFrame(new ArrayList<Tracking>(tls.findBySpecimen(specimen)));
                        val logViewer = EventLogFrame(ArrayList<Tracking?>(tls.findBySpecimenId(specimen.getSpecimenId())))
                        logViewer.pack()
                        logViewer.setVisible(true)
                    }
                })
            }
            return jButtonGetHistory
        }

    /**
     * Instantiate - if necessary - the button to paste the copied specimen on the current one
     *
     * @return
     */
    private fun getJButtonPaste(): JButton? {
        log.debug("prev spec:::")
        if (jButtonPaste == null) {
            jButtonPaste = JButton()
            jButtonPaste.setText("Paste")
            jButtonPaste.setToolTipText("Paste previous record values into this screen")
            //TODO: decide on keyboard shortcut
//jButtonPaste.setMnemonic(KeyEvent.VK_H);
            jButtonPaste.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //populate the fields with the data.
                    previousSpecimen = ImageCaptureApp.lastEditedSpecimenCache
                    copyPreviousRecord()
                }
            })
            updateJButtonPaste()
        }
        return jButtonPaste
    }

    private fun updateJButtonPaste() {
        jButtonPaste.setEnabled(!(previousSpecimen == null && ImageCaptureApp.lastEditedSpecimenCache == null))
    }// TODO: rather clone the specimen to prevent external/later changes//TODO: decide on keyboard shortcut
//jButtonCopy.setMnemonic(KeyEvent.VK_H);

    /**
     * Instantiate - if necessary - the button to save and then copy the current specimen
     *
     * @return
     */
    private val jButtonCopySave: JButton?
        private get() {
            if (jButtonCopy == null) {
                jButtonCopy = JButton()
                jButtonCopy.setText("Save & Copy")
                jButtonCopy.setToolTipText("Copy the values of this record after saving it")
                //TODO: decide on keyboard shortcut
//jButtonCopy.setMnemonic(KeyEvent.VK_H);
                jButtonCopy.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        thisPane!!.save()
                        // TODO: rather clone the specimen to prevent external/later changes
                        ImageCaptureApp.lastEditedSpecimenCache = thisPane!!.specimen
                        thisPane!!.setStatus("Saved & copied specimen with id " + thisPane!!.specimen.getSpecimenId())
                    }
                })
            }
            return jButtonCopy
        }

    /**
     * This method initializes jButtonNext
     *
     * @return javax.swing.JButton
     */
//this is not the right arrow button!
    private fun getJButtonNext(): JButton? {
        if (jButtonNext == null) {
            jButtonNext = JButton()
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/next.png")
            if (iconFile != null) {
                jButtonNext.setIcon(ImageIcon(iconFile))
            } else {
                jButtonNext.setText("Next")
            }
            jButtonNext.setMnemonic(KeyEvent.VK_N)
            jButtonNext.setEnabled(specimenController.hasNextSpecimenInTable())
            jButtonNext.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try { // try to move to the next specimen in the table.
                        thisPane!!.setStatus("Switching to next specimen...")
                        if (thisPane!!.specimenController.openNextSpecimenInTable()) {
                            thisPane.setVisible(false)
                            thisPane.invalidate()
                        } else {
                            thisPane!!.setWarning("No next specimen available.")
                        }
                    } catch (e1: Exception) { // TODO Auto-generated catch block
                        e1.printStackTrace()
                    } finally {
                        try {
                            thisPane.getParent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                        } catch (ex: Exception) {
                            log.error(ex)
                        }
                    }
                }
            })
        }
        log.debug("SpecimenDetailsViewPane.getJButtonNext(): 9")
        return jButtonNext
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonPrevious(): JButton? {
        if (jButtonPrevious == null) {
            jButtonPrevious = JButton()
            val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/back.png")
            if (iconFile != null) {
                jButtonPrevious.setIcon(ImageIcon(iconFile))
            } else {
                jButtonPrevious.setText("Previous")
            }
            jButtonPrevious.setMnemonic(KeyEvent.VK_P)
            jButtonPrevious.setToolTipText("Move to Previous Specimen")
            jButtonPrevious.setEnabled(specimenController.hasPreviousSpecimenInTable())
            jButtonPrevious.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try { // try to move to the previous specimen in the table.
                        thisPane!!.setStatus("Switching to previous specimen...")
                        if (thisPane!!.specimenController.openPreviousSpecimenInTable()) {
                            thisPane.setVisible(false)
                            thisPane.invalidate()
                        } else {
                            thisPane!!.setWarning("No previous specimen available.")
                        }
                    } catch (e1: Exception) { // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }
                }
            })
        }
        return jButtonPrevious
    }

    private fun setStateToClean() {
        state = STATE_CLEAN
        // if this is a record that is part of a navigateable set, enable the navigation buttons
        if (specimenController != null) {
            log.debug("Has controller")
            if (specimenController.isInTable()) {
                log.debug("Controller is in table")
                // test to make sure the buttons have been created before trying to enable them.
                if (jButtonNext != null) {
                    jButtonNext.setEnabled(true)
                }
                if (jButtonPrevious != null) {
                    jButtonPrevious.setEnabled(true)
                }
            }
        }
    }

    private fun setStateToDirty() {
        state = STATE_DIRTY
        if (jButtonNext != null) {
            jButtonNext.setEnabled(false)
        }
        if (jButtonPrevious != null) {
            jButtonPrevious.setEnabled(false)
        }
    }

    /**
     * State of the data in the forms as compared to the specimen from which the data was loaded.
     *
     * @return true if the data as displayed in the forms hasn't changed since the data was last loaded from
     * or saved to the specimen, otherwise false indicating a dirty record.
     */
    private val isClean: Boolean
        private get() {
            var result = false
            if (state == STATE_CLEAN) {
                result = true
            }
            return result
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldISODate(): JTextField? {
        if (jTextFieldISODate == null) {
            jTextFieldISODate = JTextField()
            jTextFieldISODate.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", jTextFieldISODate))
            jTextFieldISODate.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "ISODate"))
            jTextFieldISODate.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldISODate
    }// update the text of the dets as soon as the component is closed

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val detsJButton: JButton?
        private get() {
            if (jButtonDeterminations == null) {
                jButtonDeterminations = JButton()
                jButtonDeterminations.setText("Dets.")
                jButtonDeterminations.setMnemonic(KeyEvent.VK_D)
                jButtonDeterminations.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val dets = DeterminationFrame(specimen)
                        // update the text of the dets as soon as the component is closed
                        dets.addComponentListener(object : ComponentAdapter() {
                            override fun componentHidden(e: ComponentEvent?) {
                                updateDeterminationCount()
                                super.componentHidden(e)
                            }
                        })
                        dets.setVisible(true)
                    }
                })
            }
            return jButtonDeterminations
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val citedInPublicationJTextField: JTextField?
        private get() {
            if (jTextFieldCitedInPub == null) {
                jTextFieldCitedInPub = JTextField()
                jTextFieldCitedInPub.setInputVerifier(
                        MetadataRetriever.getInputVerifier(Specimen::class.java, "CitedInPublication", jTextFieldCitedInPub))
                jTextFieldCitedInPub.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "CitedInPublication"))
                jTextFieldCitedInPub.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        thisPane!!.setStateToDirty()
                    }
                })
            }
            return jTextFieldCitedInPub
        }

    private val basicWrapperJScrollPane: JScrollPane
        private get() {
            val pane = JScrollPane()
            pane.addMouseWheelListener(MouseWheelScrollListener(pane))
            val maxHeight: Int = Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_MAX_FIELD_HEIGHT).toInt()
            pane.setMaximumSize(Dimension(1000, maxHeight))
            return pane
        }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPaneNotes(): JScrollPane? {
        if (jScrollPaneNotes == null) {
            jScrollPaneNotes = basicWrapperJScrollPane
            jScrollPaneNotes.setViewportView(jTextAreaNotes)
            //jScrollPaneNotes.add(getJTextAreaNotes()); //allie!!!
        }
        return jScrollPaneNotes
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val dateEmergedJButton: JButton?
        private get() {
            if (dateEmergedButton == null) {
                dateEmergedButton = JButton()
                dateEmergedButton.setText("Date Emerged")
                dateEmergedButton.setToolTipText("Fill date emerged with data from verbatim date")
                dateEmergedButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (jTextFieldDateNos.getText() == "") {
                            jTextFieldDateNos.setText(jTextFieldDateEmerged.getText())
                        } else {
                            jTextFieldDateEmerged.setText(jTextFieldDateNos.getText())
                        }
                    }
                })
            }
            return dateEmergedButton
        }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private val dateCollectedJButton: JButton?
        private get() {
            if (dateCollectedButton == null) {
                dateCollectedButton = JButton()
                dateCollectedButton.setText("Date Collected")
                dateCollectedButton.setToolTipText("Fill date collected with data from verbatim date")
                dateCollectedButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        if (jTextFieldDateNos.getText() == "") {
                            jTextFieldDateNos.setText(jTextFieldDateCollected.getText())
                        } else {
                            jTextFieldDateCollected.setText(jTextFieldDateNos.getText())
                        }
                    }
                })
            }
            return dateCollectedButton
        }

    /**
     * This method initializes jButtonSpecificLocality
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSpecificLocality(): JButton? {
        if (jButtonSpecificLocality == null) {
            jButtonSpecificLocality = JButton()
            jButtonSpecificLocality.setText("Specific Locality")
            jButtonSpecificLocality.setToolTipText("Fill specific locality with data from verbatim locality")
            jButtonSpecificLocality.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (jTextFieldVerbatimLocality.getText() == "") {
                        if (jTextFieldLocality.getText() == "") { // If both are blank, set the blank value string.
                            jTextFieldLocality.setText("[no specific locality data]")
                        }
                        jTextFieldVerbatimLocality.setText(jTextFieldLocality.getText())
                    } else {
                        jTextFieldLocality.setText(jTextFieldVerbatimLocality.getText())
                    }
                }
            })
        }
        return jButtonSpecificLocality
    }

    private fun getJTextFieldMigrationStatus(): JTextField? {
        if (jTextFieldMigrationStatus == null) {
            jTextFieldMigrationStatus = JTextField()
            //jLabelMigrationStatus.setBackground(null);
//jLabelMigrationStatus.setBorder(null);
            jTextFieldMigrationStatus.setEditable(false)
            jTextFieldMigrationStatus.setText("")
            if (specimen.isStateDone()) {
                val uri = "http://mczbase.mcz.harvard.edu/guid/MCZ:Ent:" + specimen.getCatNum()
                jTextFieldMigrationStatus.setText(uri)
            }
        }
        return jTextFieldMigrationStatus
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldImgCount: JTextField?
        private get() {
            if (jTextFieldImageCount == null) {
                jTextFieldImageCount = JTextField()
                jTextFieldImageCount.setForeground(Color.BLACK)
                jTextFieldImageCount.setEnabled(false)
                updateImageCount()
            }
            return jTextFieldImageCount
        }

    /**
     * Set the image count value into the corresponding field.
     * Set the color to red if there is more than 1 image
     */
    private fun updateImageCount() {
        var imageCount = 0
        if (specimen.getICImages() != null) {
            imageCount = specimen.getICImages().size
        }
        jTextFieldImageCount.setText(Integer.toString(imageCount))
        if (imageCount > 1) {
            jTextFieldImageCount.setForeground(Color.RED)
        } else {
            jTextFieldImageCount.setForeground(Color.BLACK)
        }
    }

    private fun getTextFieldMaxElev(): JTextField? {
        if (textFieldMaxElev == null) {
            textFieldMaxElev = JTextField()
        }
        return textFieldMaxElev
    }

    private fun getComboBoxElevUnits(): JComboBox<String?>? {
        if (comboBoxElevUnits == null) {
            comboBoxElevUnits = JComboBox<String?>()
            comboBoxElevUnits.setModel(DefaultComboBoxModel<String?>(arrayOf<String?>("", "?", "m", "ft")))
        }
        return comboBoxElevUnits
    }

    private fun getTextFieldMicrohabitat(): JTextField? {
        if (textFieldMicrohabitat == null) {
            textFieldMicrohabitat = JTextField()
        }
        return textFieldMicrohabitat
    }

    private fun autocompleteGeoDataFromGeoreference() {
        val georeff: LatLong = specimen.getLatLong().iterator().next()
        if (georeff.getDecLat() != null && georeff.getDecLong() != null) { // do it async as the request could take longer than desired
            Thread(Runnable {
                log.debug("Fetching address from openstreetmap")
                val data: MutableMap<String?, Any?> = OpenStreetMapUtility.Companion.getInstance().reverseSearchValues(georeff.getDecLat(), georeff.getDecLong(), ArrayList<String?>(Arrays.asList(
                        "address.state",
                        "address.country"
                )))
                if (data != null) {
                    log.debug("Got address from openstreetmap: $data")
                    if (countryJTextField.getSelectedItem() == "") {
                        countryJTextField.setSelectedItem(data["address.country"])
                    } else {
                        log.debug("Won't set country as is '" + countryJTextField.getSelectedItem() + "'.")
                    }
                    if (primaryDivisionJTextField.getSelectedItem() == "") {
                        primaryDivisionJTextField.setSelectedItem(data["address.state"])
                    }
                }
            }).start()
        }
    }

    /**
     * This method initializes jTextFieldDateDetermined
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDateDetermined(): JTextField? {
        if (jTextFieldDateDetermined == null) {
            jTextFieldDateDetermined = JTextField()
            jTextFieldDateDetermined.setInputVerifier(
                    MetadataRetriever.getInputVerifier(Specimen::class.java, "ISODate", jTextFieldDateDetermined))
            jTextFieldDateDetermined.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "DateIdentified"))
            jTextFieldDateDetermined.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldDateDetermined
    }

    /**
     * This method initializes the Determier pick list.
     *
     * @return FilteringAgentJComboBox
     */
    private fun getJCBDeterminer(): JComboBox<String?>? {
        log.debug("calling getJCBDeterminer() ... it is $jCBDeterminer")
        if (jCBDeterminer == null) {
            log.debug("init jCBDeterminer determiner null, making a new one")
            val sls = SpecimenLifeCycle()
            jCBDeterminer = JComboBox<String?>()
            jCBDeterminer.setModel(DefaultComboBoxModel<String?>(sls.getDistinctDeterminers()))
            jCBDeterminer.setEditable(true)
            //jComboBoxCollection.setInputVerifier(MetadataRetriever.getInputVerifier(Specimen.class, "Collection", jComboBoxCollection));
//jCBDeterminer.setToolTipText(MetadataRetriever.getFieldHelp(Specimen.class, "Collection"));
            jCBDeterminer.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
            AutoCompleteDecorator.decorate(jCBDeterminer)
        }
        return jCBDeterminer
    }

    /**
     * This method initializes type status pick list
     *
     * @return javax.swing.JTextField
     */
    private fun getCbTypeStatus(): JComboBox<String?>? {
        if (cbTypeStatus == null) {
            cbTypeStatus = JComboBox<String?>(TypeStatus.Companion.getTypeStatusValues())
            // cbTypeStatus = new JComboBox(TypeStatus.getTypeStatusValues());  // for visual editor
            cbTypeStatus.setEditable(true)
            cbTypeStatus.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "TypeStatus"))
            cbTypeStatus.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return cbTypeStatus
    }

    /**
     * This method initializes jTextFieldIdRemarks
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldIdRemarks(): JTextField? {
        if (jTextFieldIdRemarks == null) {
            jTextFieldIdRemarks = JTextField()
            jTextFieldIdRemarks.setToolTipText(MetadataRetriever.getFieldHelp(Specimen::class.java, "IdentificationRemarks"))
            jTextFieldIdRemarks.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    thisPane!!.setStateToDirty()
                }
            })
        }
        return jTextFieldIdRemarks
    }

    private fun updateContentDependentLabels() {
        updateJButtonGeoreference()
        updateDeterminationCount()
        updateJButtonPaste()
        updateDBIdLabel()
    }

    companion object {
        private val log: Log? = LogFactory.getLog(SpecimenDetailsViewPane::class.java)
        private const val serialVersionUID = 3716072190995030749L
        private const val STATE_CLEAN = 0
        private const val STATE_DIRTY = 1
    }

    /**
     * Construct an instance of a SpecimenDetailsViewPane showing the data present
     * in aSpecimenInstance.
     *
     * @param aSpecimenInstance the Specimen instance to display for editing.
     */
    init {
        specimen = aSpecimenInstance
        val s = SpecimenLifeCycle()
        setStateToClean()
        //		SpecimenPartAttributeLifeCycle spals = new SpecimenPartAttributeLifeCycle();
//		Iterator<SpecimenPart> i = specimen.getSpecimenParts().iterator();
//		while (i.hasNext()) {
//			Iterator<SpecimenPartAttribute> ia = i.next().getAttributeCollection().iterator();
//			while (ia.hasNext()) {
//				try {
//					SpecimenPartAttribute spa = ia.next();
//					log.debug(spa.getSpecimenPartAttributeId());
//					spals.attachDirty(spa);
//					log.debug(spa.getSpecimenPartAttributeId());
//				} catch (SaveFailedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
        try {
            s.attachClean(specimen)
            specimenController = aController
            initialize()
            setValues()
        } catch (e: Exception) {
            var status = "Undefined error initializing SpecimenDetails. Restarting Database connection..."
            if (e is SessionException || e is TransactionException) {
                status = "Database Connection Error. Resetting connection... Try again."
            } else if (e is IllegalStateException) {
                status = "Illegal state exception. Last edit possibly lost. Try again."
            } else if (e is OptimisticLockException) {
                status = "Error: last edited entry has been modified externally. Try again."
            }
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage(status)
            log.debug(e.message, e)
            HibernateUtil.restartSessionFactory()
            this.setVisible(false)
        }
    }
}