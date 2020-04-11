/**
 * ImageCaptureApp.java
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
 *
 *
 *
 * File last changed on $Date: 2012-01-06 18:51:55 -0500 (Fri, 06 Jan 2012) $ by $Author: mole $ in $Rev$.
 * $Id: ImageCaptureApp.java 305 2012-01-06 23:51:55Z mole $
 */
package edu.harvard.mcz.imagecaptureimport

import org.apache.commons.logging.LogFactory
import java.awt.Cursor
import java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * for experimental chat support
 * import javax.jms.ConnectionFactory;
 * import javax.jms.JMSException;
 * import javax.jms.Message;
 * import javax.jms.MessageConsumer;
 * import javax.jms.Session;
 * import javax.jms.TextMessage;
 * import javax.jms.Topic;
 * import javax.jms.TopicConnection;
 * import javax.jms.TopicSession;
 * import javax.jms.TopicSubscriber;
 * import javax.naming.Context;
 * import javax.naming.InitialContext;
 * import javax.naming.NamingException;
 */
/**
 * Main entry point for user interface of ImageCapture Java Application. Creates
 * a SingletonObject, loads the properties file, and opens a MainFrame
 *
 * @see MainFrame
 *
 * @see edu.harvard.mcz.imagecapture.Singleton
 *
 * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties
 */
object ImageCaptureApp {
    val APP_NAME: String? = "DataShot"
    val APP_DESCRIPTION: String? = "Rapid capture of data from images of pin Labels and pinned insect \nspecimens developed for the MCZ Lepidoptera collection"
    val APP_COPYRIGHT: String? = "Copyright © 2009-2017 President and Fellows of Harvard College"
    val APP_LICENSE: String? = ("This program is free software; you can redistribute it and/or modify \n "
            + "it under the terms of Version 2 of the GNU General Public License \n"
            + "as published by the Free Software Foundation" + " \n "
            + "This program is distributed in the hope that it will be useful,\n "
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n "
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n "
            + "GNU General Public License for more details.\n " + "\n "
            + "You should have received a copy of the GNU General Public License along\n "
            + "with this program; if not, write to the Free Software Foundation, Inc.,\n "
            + "51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n ")
    val APP_CONTRIBUTORS: String? = "Brendan Haley, Linda Ford, Rodney Eastwood, Paul J. Morris, Tim Bernhard."
    val APP_LIBRARIES: String? = "Hibernate, Tesseract, ZXing, Log4J, drew.metadata.exif, iText, event_date_qc"
    val APP_REV: String? = "\$Rev$" // ImageCapture.jar file built before commit will be one revision
    /**
     * Default regular expression for recognizing drawer numbers and unit tray
     * numbers, used to set default value of property
     * ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER, use that property instead of
     * this hard coded constant.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER
     */
    val REGEX_DRAWERNUMBER: String? = "[0-9]{3}\\Q.\\E[0-9]+"
    /**
     * Default regular expression for recognizing image filenames in pattern decided
     * on for project. Used to set the default value of property
     * ImageCaptureProperties.KEY_IMAGEREGEX, use that property instead of this hard
     * coded constant.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_IMAGEREGEX
     */
// public static final String REGEX_IMAGEFILE = "^IMG_[0-9]{6}\\.JPG$";
    val REGEX_IMAGEFILE: String? = "^ETHZ_ENT[0-9]{2}_[0-9]{4}_[0-9]{2}_[0-9]{2}_[0-9]{6}\\.JPG$"
    /**
     * Match blank, or year or year/month or year/month/day.
     */
    val REGEX_DATE: String? = "^([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?(\\-([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?)??$"
    /**
     * Use MCZEntBarcode class instead.
     *
     */
// public static final String REGEX_BARCODE = "^MCZ-ENT[0-9]{8}$";
    /**
     * Code for a normal exit, pass to ImageCaptureApp.exit(EXIT_NORMAL).
     */
    const val EXIT_NORMAL = 0
    /**
     * Error code for an exit after a fatal error. Pass to
     * ImageCaptureApp.exit(EXIT_ERROR);
     */
    const val EXIT_ERROR = 1
    // ^([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?\-?([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)??$
    private val log = LogFactory.getLog(ImageCaptureApp::class.java)
    // behind latest commit with changes to this file.
    var lastEditedSpecimenCache: Specimen? = null
    private var APP_VERSION: String? = null
    /**
     * Main method for starting the application.
     *
     * @param args are not used.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            UIManager.setLookAndFeel( // Use cross platform if native uses space on forms much too inefficiently
// UIManager.getCrossPlatformLookAndFeelClassName());
                    UIManager.getSystemLookAndFeelClassName())
        } catch (e: UnsupportedLookAndFeelException) {
            log!!.error(e)
        } catch (e: ClassNotFoundException) {
            log!!.error(e)
        } catch (e: InstantiationException) {
            log!!.error(e)
        } catch (e: IllegalAccessException) {
            log!!.error(e)
        }
        log!!.debug(UIManager.getLookAndFeel().getID())
        println("Starting $APP_NAME $appVersion")
        println(APP_COPYRIGHT)
        println(APP_LICENSE)
        log.debug("Starting $APP_NAME $appVersion")
        // open UI and start
        val mainFrame = MainFrame()
        mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        mainFrame.setStatusMessage("Starting....")
        Singleton.Companion.getSingletonInstance().setMainFrame(mainFrame)
        Singleton.Companion.getSingletonInstance().unsetCurrentUser()
        log.debug("User interface started")
        // Set up a barcode (text read from barcode label for pin) matcher/builder
        if (Singleton.Companion.getSingletonInstance().getProperties().getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_MCZENT) { // ** Configured for the MCZ Entomology Collection, use MCZ assumptions.
            val barcodeTextBuilderMatcher = MCZENTBarcode()
            Singleton.Companion.getSingletonInstance().setBarcodeBuilder(barcodeTextBuilderMatcher)
            Singleton.Companion.getSingletonInstance().setBarcodeMatcher(barcodeTextBuilderMatcher)
        } else if (Singleton.Companion.getSingletonInstance().getProperties().getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) { // ** Configured for the ETHZ Entomology Collection, use MCZ assumptions.
            val barcodeTextBuilderMatcher = ETHZBarcode()
            Singleton.Companion.getSingletonInstance().setBarcodeBuilder(barcodeTextBuilderMatcher)
            Singleton.Companion.getSingletonInstance().setBarcodeMatcher(barcodeTextBuilderMatcher)
        } else {
            log.error("Configured collection not recognized. Unable to Start")
            exit(EXIT_ERROR)
        }
        // Force a login dialog by connecting to obtain record count;
        val sls = SpecimenLifeCycle()
        try {
            Singleton.Companion.getSingletonInstance().getMainFrame().setCount(sls.findSpecimenCountThrows())
            doStartUp()
        } catch (e: ConnectionException) {
            log.error(e.message)
            doStartUpNot()
        }
        // Experimental chat support, working on localhost.
        /**
         *
         * Context context = null; Hashtable contextProperties = new Hashtable(2);
         *
         * contextProperties.put(Context.PROVIDER_URL,"iiop://127.0.0.1:3700");
         * contextProperties.put("java.naming.factory.initial",
         * "com.sun.enterprise.naming.SerialInitContextFactory");
         * contextProperties.put("java.naming.factory.url.pkgs",
         * "com.sun.enterprise.naming");
         * contextProperties.put("java.naming.factory.state",
         * "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl"); try { context
         * = new InitialContext(contextProperties); } catch (NamingException ex) {
         * ex.printStackTrace(); } if (context!=null) { ConnectionFactory
         * connectionFactory; try { connectionFactory =
         * (ConnectionFactory)context.lookup("jms/InsectChatTopicFactory"); Topic
         * chatTopic = (Topic)context.lookup("jms/InsectChatTopic"); TopicConnection
         * connection = (TopicConnection) connectionFactory.createConnection();
         * TopicSession session =
         * connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
         * TopicSubscriber subscriber = session.createSubscriber(chatTopic);
         * connection.start(); while (true) { Message m = subscriber.receive(1); if (m
         * != null) { if (m instanceof TextMessage) { TextMessage message =
         * (TextMessage) m; String originator = message.getStringProperty("Originator");
         * String text = message.getText(); System.out.println("Message: " + originator
         * + ": " + text); } else { break; } } } } catch (NamingException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); } catch (JMSException e) { //
         * TODO Auto-generated catch block e.printStackTrace(); } } }
         */
    }

    // fetch version
    val appVersion: String?
        get() {
            if (APP_VERSION == null) { // fetch version
                APP_VERSION = try {
                    val privateProperties = Properties()
                    privateProperties.load(ImageCaptureApp::class.java.classLoader.getResourceAsStream("imagecapture.private.properties"))
                    privateProperties.getProperty("project.version")
                } catch (e: IOException) {
                    "failed-loading-version"
                }
            }
            return APP_VERSION
        }

    /**
     * Carry out actions to set user interface into nobody logged in yet state.
     */
    fun doStartUpNot() {
        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Select File/Change User to login.")
        Singleton.Companion.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        Singleton.Companion.getSingletonInstance().getMainFrame().setState(MainFrame.Companion.STATE_RESET)
    }

    /**
     * Carry out actions to set up system after a successful login.
     */
    fun doStartUp() {
        var isCurrentAllowed = false
        try {
            isCurrentAllowed = AllowedVersionLifeCycle.Companion.isCurrentAllowed()
        } catch (e: HibernateException) {
            log!!.error(e.message, e)
            val allowed: String = AllowedVersionLifeCycle.Companion.listAllowedVersions()
            if (allowed == null || allowed.trim { it <= ' ' }.length == 0) {
                Singleton.Companion.getSingletonInstance().getMainFrame()
                        .setStatusMessage("Database does not support this version, schema update needed.")
                JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                        "The database does not support" + APP_NAME + " version " + APP_VERSION
                                + ".  A database schema update to version 1.3 is required.",
                        "Schema Update Required", JOptionPane.OK_OPTION)
                log.error("Database does not test for versioning. Added in 1.3.0 Schema, and required by "
                        + APP_VERSION + "  Unable to Start")
                exit(EXIT_ERROR)
            }
        }
        if (isCurrentAllowed) {
            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("$APP_VERSION OK")
        } else {
            val allowed: String = AllowedVersionLifeCycle.Companion.listAllowedVersions()
            Singleton.Companion.getSingletonInstance().getMainFrame()
                    .setStatusMessage("Database does not support version, update needed.")
            val response: Int = JOptionPane.showConfirmDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                    "The database does not support" + APP_NAME + " version " + APP_VERSION
                            + ".  A software (or database) update from " + allowed + " is required. "
                            + "Are you ready to try the upgrade of the database? Make sure no one else will need the old version.",
                    "Update Required", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
            log!!.error("Database does not allow version " + APP_VERSION + ". Upgrade will "
                    + (if (response == JOptionPane.YES_OPTION) "" else "not happen") + "happen.")
            log.error("Database recognises version(s): $allowed")
            if (response == JOptionPane.YES_OPTION) { // try to upgrade the database scheme
                try {
                    AllowedVersionLifeCycle.Companion.upgrade()
                    Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("DB Upgrade OK")
                } catch (e: Exception) {
                    log.error("Upgrade failed", e)
                    JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                            "Upgrade failed with error: " + e.message, "DB Upgrade failed.",
                            JOptionPane.ERROR_MESSAGE)
                }
            }
            // ImageCaptureApp.exit(EXIT_ERROR);
        }
        // Setup to store a list of running RunnableJobs.
        Singleton.Companion.getSingletonInstance().setJobList(RunnableJobTableModel())
        log!!.debug("Set runnable job table.")
        Singleton.Companion.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        Singleton.Companion.getSingletonInstance().getMainFrame().setState(MainFrame.Companion.STATE_RUNNING)
    }

    /**
     * Initiate actions to be taken on shutting down the application.
     */
    fun cleanUp() {
        try {
            Singleton.Companion.getSingletonInstance().getProperties().saveProperties()
        } catch (e: Exception) {
            println("Properties file save failed.  " + e.localizedMessage)
        }
    }

    /**
     * Shut down the application. Calls cleanUp() on normal exit. Constants
     * EXIT_NORMAL and EXIT_ERROR are available to be passed as the parameter
     * status.
     *
     * @param status 0 for normal exit, positive integer for error condition.
     */
    fun exit(status: Int) {
        if (status == EXIT_NORMAL) {
            cleanUp()
            log!!.debug("Exiting Application.")
        } else {
            log!!.error("Exiting Application.")
        }
        System.exit(status)
    }
}