/**
 * MetadataRetriever.java
 * edu.harvard.mcz.imagecapture.data
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
package edu.harvard.mcz.imagecapture.dataimport

import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.entity.Number
import java.awt.Color
import java.text.ParseException

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * MetadataRetriever produces metadata (field lengths, tooltip texts, input masks, input verifiers)
 * for fields in tables in database.
 */
object MetadataRetriever {
    private fun repeat(s: String?, count: Int): String {
        val result = StringBuffer()
        for (i in 0 until count) {
            result.append(s)
        }
        return result.toString()
    }

    /**
     * Generates a MaskFormatter for a JFormattedTextField based upon the length (and potentially the type) of the
     * underlying text field.  Doesn't work well for normal varchar() fields, as the JTextField appears to be full
     * of spaces.
     *
     *
     * Usage:
     * <pre>
     * JFormattedTextField jtext_for_fieldname = new JFormattedTextField(MetadataRetriever(tablename.class,"fieldname"));
    </pre> *
     *
     * @param aTableClass
     * @param fieldname
     * @return a MaskFormatter for a jFormattedTextField
     *
     *
     * TODO: add field type recognition, currently returns only "****" masks.
     */
    fun getMask(aTableClass: Class<*>?, fieldname: String): MaskFormatter? {
        var formatter: MaskFormatter? = null
        try {
            formatter = MaskFormatter(repeat("*", getFieldLength(aTableClass, fieldname)))
        } catch (e: ParseException) { // Shouldn't end up here unless tables have been redesigned and
// MetadataRetriever.getFieldLength isn't returning a value.
            println("Bad Mask format string")
            e.printStackTrace()
        }
        return formatter
    }

    /**
     * Generates an InputVerifier for a JTextField
     *
     *
     * Usage:
     * <pre>
     * JTextField jText_for_fieldname = new JTextField();
     * jText_for_fieldname.addInputVerifier(MetadataRetriever.getInputVerifier(tablename.class,"fieldname",jText_for_fieldname));
    </pre> *
     *
     * @param aTableClass table proxy object for fieldname
     * @param fieldname   field for which to check the fieldlength
     * @param field       JTextField to which the InputVerifier is being added.
     * @return an InputVerifier for the JTextField
     * TODO: implement tests for more than just length.
     */
    fun getInputVerifier(aTableClass: Class<*>?, fieldname: String, field: JTextField): InputVerifier? {
        var result: InputVerifier? = null
        if (aTableClass == Specimen::class.java && (fieldname.equals("ISODate", ignoreCase = true) || fieldname.equals("DateIdentified", ignoreCase = true))
                || aTableClass == Determination::class.java && fieldname.equals("DateIdentified", ignoreCase = true)) {
            result = object : InputVerifier() {
                override fun verify(comp: JComponent?): Boolean {
                    var returnValue = true
                    val textField: JTextField = comp as JTextField?
                    val content: String = textField.getText()
                    if (content.length > getFieldLength(aTableClass, fieldname)) {
                        returnValue = false
                    } else {
                        if (!content.matches(ImageCaptureApp.REGEX_DATE)) {
                            returnValue = false
                        }
                    }
                    return returnValue
                }

                override fun shouldYieldFocus(input: JComponent?): Boolean {
                    val valid: Boolean = super.shouldYieldFocus(input)
                    if (valid) {
                        field.setBackground(Color.WHITE)
                    } else {
                        field.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                    }
                    field.revalidate()
                    return valid
                }
            }
        } else {
            result = object : InputVerifier() {
                override fun verify(comp: JComponent?): Boolean {
                    var returnValue = true
                    val textField: JTextField = comp as JTextField?
                    val content: String = textField.getText()
                    if (content.length > getFieldLength(aTableClass, fieldname)) {
                        returnValue = false
                    }
                    return returnValue
                }

                override fun shouldYieldFocus(input: JComponent?): Boolean {
                    val valid: Boolean = super.shouldYieldFocus(input)
                    if (valid) {
                        if (fieldname.equals("Inferences", ignoreCase = true)) {
                            field.setBackground(MainFrame.Companion.BG_COLOR_ENT_FIELD)
                        } else {
                            field.setBackground(Color.WHITE)
                        }
                    } else {
                        field.setBackground(MainFrame.Companion.BG_COLOR_ERROR)
                    }
                    field.revalidate()
                    return valid
                }
            }
        }
        return result
    }

    /**
     * Determine the length of a field from the class of the proxy object
     * for the table and the name of the field.
     *
     *
     * Usage:
     * <pre>
     * int genusSize = MetadataRetriever.getFieldLength(Specimen.class, "genus");
    </pre> *
     *
     * @param aTableClass the class of the proxy object over the table.
     * @param fieldname   the name of the field in that table (case insensitive).
     * @return the number of characters that can be put into the field.
     */
    fun getFieldLength(aTableClass: Class<*>?, fieldname: String): Int {
        var length = 0
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("Barcode", ignoreCase = true)) {
                length = 20
            }
            if (fieldname.equals("DrawerNumber", ignoreCase = true)) {
                length = 10
            }
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Family", ignoreCase = true)) {
                length = 40
            }
            if (fieldname.equals("Subfamily", ignoreCase = true)) {
                length = 40
            }
            if (fieldname.equals("Tribe", ignoreCase = true)) {
                length = 40
            }
            length = getDetFieldLength(fieldname, length)
            if (fieldname.equals("NatureOfID", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("Country", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("PrimaryDivison", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("VerbatimElevation", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                length = 21
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                length = 32
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("WorkFlowStatus", ignoreCase = true)) {
                length = 30
            }
            if (fieldname.equals("CreatedBy", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("DateLastUpdated", ignoreCase = true)) {
                length = 0
            }
            if (fieldname.equals("LastUpdatedBy", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                length = 1
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                length = 50
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                length = 255
            }
        }
        if (aTableClass == Determination::class.java) {
            length = getDetFieldLength(fieldname, length)
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("NatureOfID", ignoreCase = true)) {
                length = 255
            }
        }
        if (aTableClass == Users::class.java) {
            if (fieldname.equals("username", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("fullname", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("description", ignoreCase = true)) {
                length = 255
            }
            if (fieldname.equals("role", ignoreCase = true)) {
                length = 20
            }
        }
        if (aTableClass == ICImage::class.java) {
            if (fieldname.equals("rawBarcode", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("rawExifBarcode", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("filename", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("rawOcr", ignoreCase = true)) {
                length = 65535
            }
            if (fieldname.equals("path", ignoreCase = true)) {
                length = 900
            }
            if (fieldname.equals("uri", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("templateId", ignoreCase = true)) {
                length = 50
            }
            if (fieldname.equals("drawerNumber", ignoreCase = true)) {
                length = 10
            }
        }
        return length
    }

    /**
     * Extracted from above: field lengths for both Specimen & Determination
     *
     * @param fieldname
     * @param length
     * @return
     */
    private fun getDetFieldLength(fieldname: String, length: Int): Int {
        var length = length
        if (fieldname.equals("Genus", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
            length = 40
        }
        if (fieldname.equals("Authorship", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
            length = 50
        }
        if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
            length = 50
        }
        if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
            length = 255
        }
        if (fieldname.equals("DateIdentified", ignoreCase = true)) {
            length = 21
        }
        return length
    }

    /**
     * Given a proxy class for a table and the name of a field return a help text for that field.
     *
     * @param aTableClass
     * @param fieldname
     * @return a String containing a help text suitable for use as a tooltip or other context help display.
     */
    fun getFieldHelp(aTableClass: Class<*>?, fieldname: String): String {
        var help = "No help available"
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("Barcode", ignoreCase = true)) {
                help = "The barcode of the specimen, in the form 'MCZ-ENT01234567'"
            }
            if (fieldname.equals("DrawerNumber", ignoreCase = true)) {
                help = "The drawer number from the specimen unit tray label."
            }
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                help = "Not a type, or the kind of type (e.g. Holotype) that this specimen is.  Normal workflow uses value 'not a type'.  This type status refers to the type status of this specimen for the name in the specimen record (secondary types may have type status set in other determinations)."
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                help = "The number from the type catalog number series that applies to this specimen"
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                help = "Publications this specimen is cited in, as found on labels.  Separate publications with a semicolon ' ; '"
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                help = "Description of features of this specimen that aren't named forms, un-named forms, sex, or life stage.  Examples: features could include eclosion defect, runt (unusually small), deformed, faded colours, scales stripped for venation, greasy, stained, psocid damaged."
            }
            if (fieldname.equals("Family", ignoreCase = true)) {
                help = "The family into which this specimen is placed, from the unit tray label"
            }
            if (fieldname.equals("Subfamily", ignoreCase = true)) {
                help = "The subfamily into which this specimen is placed, from the unit tray label"
            }
            if (fieldname.equals("Tribe", ignoreCase = true)) {
                help = "The tribe into which this specimen is placed, if any, from the unit tray label."
            }
            if (fieldname.equals("Genus", ignoreCase = true)) {
                help = "The generic name from the unit tray label.  The current identification for non-primary types, the type name for primary types.  Example: 'Delias' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
                help = "The specific part of the species group name from the unit tray label. The current identification for non-primary types, the type name for primary types.  Include indicators of uncertanty associated with this part of the name such as nr, cf, ?.  May be 'sp.'  Example: 'argenthoma' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
                help = "The subspecific part (if present) of the species group name from the unit tray label.  Include indicators of uncertanty associated with this part of the name such as nr, cf, ?.  May be 'ssp.'"
            }
            if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
                help = "The form, varietal, or other part of a name with a rank below subspecies from the unit tray label"
            }
            if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
                help = "The rank (e.g. form, variety) of the infraspecific name from the unit tray label."
            }
            if (fieldname.equals("Authorship", ignoreCase = true)) {
                help = "The author of the species group name from the unit tray label.  Include year and parenthesies if present.  Example: 'Fabricius, 1793' from 'Delias argenthona Fabricius, 1793'"
            }
            if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
                help = "e.g. 'Winter form', informal descriptive name of the form of this specimen (for informal form names not regulated by the ICZN)."
            }
            if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
                help = "Don't use this field."
            }
            if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
                help = "Name of the person, if known, who made this identification."
            }
            if (fieldname.equals("DateIdentified", ignoreCase = true)) {
                help = "Date at which this identification was made, if known.  Use ISO format yyyy/mm/dd-yyyy/mm/dd."
            }
            if (fieldname.equals("IdentificationRemarks", ignoreCase = true)) {
                help = "Remarks about this identification."
            }
            if (fieldname.equals("NatureOfId", ignoreCase = true)) {
                help = "Nature of the Identification: expert ID=made by known expert; legacy=on label with no data on identifier. "
            }
            if (fieldname.equals("Country", ignoreCase = true)) {
                help = "The country from which this specimen was collected.  Infer if you have specialist knowledge and annotate in Inferences"
            }
            if (fieldname.equals("PrimaryDivison", ignoreCase = true)) {
                help = "The state, province, or other primary geopolitical division of the country from which this specimen was collected.  Infer if you have specialist knowlege and annotate in Inferences"
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                help = "Placenames, offsets, and other text describing where this specimen was collected.  Press button to use '[no specific locality data]' when there are no specific locality data."
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                help = "Verbatim transcription of locality information found on this specimen's labels."
            }
            if (fieldname.equals("VerbatimElevation", ignoreCase = true)) {
                help = "Verbatim transcription of elevation information, including units, found on this specimen's labels"
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                help = "If specified on a label, the method by which this specimen was collected."
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                help = "The default date field, a verbatim date associated with this specimen that isn't marked as either a date collected or date emerged, and might be either of these or some other date."
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                help = "The date collected or the default date in ISO date format yyyy/mm/dd. Optionally, a range yyyy/mm/dd-yyyy/mm/dd"
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                help = "The date at which this butterfly emerged."
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                help = "The verbatim text from the label that indicates that this is a date emerged."
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                help = "The date at which this butterfly was collected from the wild."
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                help = "The verbatim text from the label that indicates that this is a date collected."
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                help = "The name of a collection of which this specimen was a part.  Use a standard format rather than verbatim text."
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                help = "Notes from the labels about this specimen.  Use a semicolon ; to separate multiple notes."
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                help = "The life stage of this specimen."
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                help = "The sex of this specimen."
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                help = "The preparation type of this specimen."
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                help = "Text from the labels descrbing the habitat from which this specimen was collected."
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                help = "If this specimen represents an associated taxon such as a host ant, put the actual identification of this specimen, to whatever level it is available here, and put the name of the butterfly from the unit tray label in the identification (i.e. genus, species, etc. fields)."
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                help = "Describe any questions or problems you have with the transcription of the data from this specimen."
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                help = "If you have specialist knowledge about this specimen, briefly describe the basis for any inferences you are making in adding data to this record that isn't present on the specimen labels.  Example: 'Locality for this species known to be in Australia, pers obs.'"
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                help = "General Collection, Type Collection, or other major storage division of the Lepidoptera collection."
            }
            if (fieldname.equals("WorkFlowStatus", ignoreCase = true)) {
                help = "The current state of this database record in the workflow."
            }
            if (fieldname.equals("CreatedBy", ignoreCase = true)) {
                help = "The name of the person or automated process that created this record."
            }
            if (fieldname.equals("DateLastUpdated", ignoreCase = true)) {
                help = "The date and time at which this record was most recently updated."
            }
            if (fieldname.equals("LastUpdatedBy", ignoreCase = true)) {
                help = "The name of the person who most recenly updated this record."
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                help = "Uncheck if the locality does not reflect the collection of this specimen from nature (e.g. uncheck for specimens that came from a captive breeding program).  Leave checked if locality represents natural biological range. "
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                help = "A number (including alphanumeric identifiers) found on a label of this specimen."
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                help = "If known, what sort of number this is and where it came from."
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                help = "The name of a collector."
            }
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                help = "Not a type, or the kind of type (e.g. Topotype) that this specimen is for this particular name."
            }
            if (fieldname.equals("Genus", ignoreCase = true)) {
                help = "The generic name used in the identification."
            }
            if (fieldname.equals("SpecificEpithet", ignoreCase = true)) {
                help = "The specific part of the species group name used in the identification."
            }
            if (fieldname.equals("SubspecificEpithet", ignoreCase = true)) {
                help = "The subspecific part (if present) of the species group name used in the identification."
            }
            if (fieldname.equals("InfraspecificEpithet", ignoreCase = true)) {
                help = "The form, varietal, or other part of a name with a rank below subspecies used in the identification."
            }
            if (fieldname.equals("InfraspecificRank", ignoreCase = true)) {
                help = "The rank (e.g. form, variety, abberation, morph, lusus, natio) of the infrasubspecific name."
            }
            if (fieldname.equals("Authorship", ignoreCase = true)) {
                help = "The author of the species group name used in the determination."
            }
            if (fieldname.equals("UnNamedForm", ignoreCase = true)) {
                help = "e.g. 'Winter form', informal descriptive name of the form of this specimen (not regulated by the ICZN)."
            }
            if (fieldname.equals("IdentificationQualifier", ignoreCase = true)) {
                help = "A question mark or other qualifier that indicates the identification of this specimen is uncertain."
            }
            if (fieldname.equals("IdentifiedBy", ignoreCase = true)) {
                help = "Name of the person, if known, who made this identification."
            }
            if (fieldname.equals("DateIdentified", ignoreCase = true)) {
                help = "Date at which this identification was made, if known.  Use ISO Format yyyy/mm/dd-yyyy/mm/dd."
            }
            if (fieldname.equals("IdentificationRemarks", ignoreCase = true)) {
                help = "Remarks about this identification."
            }
            if (fieldname.equals("NatureOfId", ignoreCase = true)) {
                help = "Nature of the Identification: expert ID=made by known expert; legacy=on label with no data on identifier. "
            }
        }
        if (aTableClass == Users::class.java) {
            if (fieldname.equals("username", ignoreCase = true)) {
                help = "Database username of this person."
            }
            if (fieldname.equals("fullname", ignoreCase = true)) {
                help = "The full name of this person."
            }
            if (fieldname.equals("description", ignoreCase = true)) {
                help = "What this person's role in the project and specialties are."
            }
            if (fieldname.equals("role", ignoreCase = true)) {
                help = "Unused"
            }
        }
        return help
    }

    /**
     * Test to see whether a field allowed to be updated by an external process.
     *
     * @param aTableClass the class for the table in which the field occurs.
     * @param fieldname   the name of the field (case insensitive).
     * @return true if the field is allowed to be updated by an external process, false otherwise.
     */
    fun isFieldExternallyUpdatable(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("TypeStatus", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("TypeNumber", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("CitedInPublication", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Features", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("HigherGeography", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("SpecificLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollector", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimNumbers", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimUnclassifiedText", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimClusterIdentifier", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowProcess", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowDate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Minimum_Elevation", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Maximum_Elevation", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("MinimumElevationSt", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("MaximumElevationSt", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Elev_Units", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("CollectingMethod", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ISODate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateEmerged", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateEmergedIndicator", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateCollected", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateCollectedIndicator", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Collection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("SpecimenNotes", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("LifeStage", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Sex", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("PreparationType", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Habitat", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Microhabitat", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("AssociatedTaxon", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Questions", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("Inferences", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("LocationInCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ValidDistributionFlag", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
            if (fieldname.equals("CollectorName", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("VerbatimText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Number::class.java) {
            if (fieldname.equals("Number", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("NumberType", ignoreCase = true)) {
                result = true
            }
        }
        return result
    }

    /**
     * Test to see whether a field in a table is intended to hold verbatim values.
     *
     * @param aTableClass the class for the table.
     * @param fieldname   the field to check (not case sensitive)
     * @return true if the field is intended to hold verbatim data, false otherwise.
     */
    fun isFieldVerbatim(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("VerbatimLocality", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollector", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimCollection", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimNumbers", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("DateNOS", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("VerbatimUnclassifiedText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
        }
        if (aTableClass == Determination::class.java) {
            if (fieldname.equals("VerbatimText", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Number::class.java) {
        }
        return result
    }

    /**
     * Identify whether a field in a table contains values that are process metadata.
     *
     * @param aTableClass table to check
     * @param fieldname   field in aTableClass
     * @return true if process metadata field, false otherwise.
     */
    fun isFieldProcessMetadata(aTableClass: Class<*>?, fieldname: String): Boolean {
        var result = false
        if (aTableClass == Specimen::class.java) {
            if (fieldname.equals("VerbatimClusterIdentifier", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowProcess", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("ExternalWorkflowDate", ignoreCase = true)) {
                result = true
            }
            if (fieldname.equals("WorkflowStatus", ignoreCase = true)) {
                result = true
            }
        }
        if (aTableClass == Collector::class.java) {
        }
        if (aTableClass == Determination::class.java) {
        }
        if (aTableClass == Number::class.java) {
        }
        return result
    }
}