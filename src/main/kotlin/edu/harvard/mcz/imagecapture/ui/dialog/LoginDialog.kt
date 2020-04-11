/**
 * LoginDialog.java
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
 */
package edu.harvard.mcz.imagecapture.ui.dialogimportimport

org.apache.commons.logging.Logimport org.apache.commons.logging.LogFactoryimport java.awt.Dimensionimport java.awt.Fontimport java.awt.Insetsimport java.awt.Toolkitimport java.awt.event.ActionEventimport java.awt.event.KeyEventimport java.lang.Exception
import java.net.URL

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * A database login dialog, including both username/password credentials and
 * specification of the database connection parameters.
 */
class LoginDialog : JDialog() {
    private var self: JDialog? = null
    var result = RESULT_LOGIN
        private set
    private var jPanel: JPanel? = null
    private var jTextFieldDBUsername: JTextField? = null
    private var jPasswordFieldDB: JPasswordField? = null
    private var jTextFieldDriver: JTextField? = null
    private var jTextFieldConnection: JTextField? = null
    private var jTextFieldDialect: JTextField? = null
    private var jButtonLogin: JButton? = null
    private var jButtonCancel: JButton? = null
    private var jTextFieldEmail: JTextField? = null
    private var jPasswordFieldUser: JPasswordField? = null
    private var jButton2: JButton? = null
    private var jPanelAdvanced: JPanel? = null
    private var jLabelStatus: JLabel? = null
    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setContentPane(getJPanel())
        this.setTitle("DataShot Login Dialog: Configured For: " +
                Singleton.Companion.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION))
        val iconFile = this.javaClass.getResource(
                "/edu/harvard/mcz/imagecapture/resources/images/icon.png")
        try {
            setIconImage(ImageIcon(iconFile).getImage())
        } catch (e: Exception) {
            log.error(e)
        }
        if (Singleton.Companion.getSingletonInstance()
                        .getProperties()
                        .getProperties()
                        .getProperty(ImageCaptureProperties.Companion.KEY_LOGIN_SHOW_ADVANCED)
                        .equals("false", ignoreCase = true)) {
            jPanelAdvanced.setVisible(false)
            this.setSize(Dimension(650, 225))
        } else {
            jPanelAdvanced.setVisible(true)
            this.setSize(Dimension(650, 355))
        }
        this.getRootPane().setDefaultButton(jButtonLogin)
        val screenSize: Dimension = Toolkit.getDefaultToolkit().getScreenSize()
        this.setLocation((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2)
        //    this.pack();
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel()
            jPanel.setLayout(MigLayout("wrap 2, fill"))
            val keyImageLabel = JLabel()
            val iconFile = this.javaClass.getResource(
                    "/edu/harvard/mcz/imagecapture/resources/images/key_small.png")
            try { // this.setIconImage(new ImageIcon(iconFile).getImage());
                keyImageLabel.setIcon(ImageIcon(iconFile))
            } catch (e: Exception) {
                println("Can't open icon file: $iconFile")
            }
            // row
            jPanel.add(keyImageLabel)
            val loginPrompt = JLabel("Login & connect to database")
            val f: Font = loginPrompt.getFont()
            // bold
            loginPrompt.setFont(f.deriveFont(f.getStyle() or Font.BOLD))
            jPanel.add(loginPrompt)
            // row
            val emailLabel = JLabel("E-Mail/Username")
            jPanel.add(emailLabel)
            println(getJTextFieldEmail())
            jPanel.add(getJTextFieldEmail(), "growx")
            // row
            val passwordLabel = JLabel("Password")
            jPanel.add(passwordLabel)
            jPanel.add(getJPasswordFieldUser(), "growx")
            // row
            jLabelStatus = JLabel()
            jPanel.add(jLabelStatus, "span 2, wrap")
            // row
            val dbLabel = JLabel("Database")
            jPanel.add(dbLabel)
            jPanel.add(advancedSettingsJButton, "wrap")
            // row
            jPanel.add(getJButtonCancel(), "tag cancel, align left")
            jPanel.add(getJButtonLogin(), "tag ok, align right")
            // row
            jPanel.add(getJPanelAdvanced(), "grow, span 2, wrap")
        }
        return jPanel
    }

    /**
     * This method initializes jTextFieldSchemaName if necessary
     *
     * @return javax.swing.JTextField
     */
    private val jTextFieldSchemaName: JTextField?
        private get() {
            if (jTextFieldDBUsername == null) {
                jTextFieldDBUsername = JTextField()
            }
            return jTextFieldDBUsername
        }

    /**
     * This method initializes jPasswordFieldDB
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordFieldDB(): JPasswordField? {
        if (jPasswordFieldDB == null) {
            jPasswordFieldDB = JPasswordField()
        }
        return jPasswordFieldDB
    }

    /**
     * This method initializes jTextFieldDriver
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDriver(): JTextField? {
        if (jTextFieldDriver == null) {
            jTextFieldDriver = JTextField("com.mysql.cj.jdbc.Driver")
        }
        return jTextFieldDriver
    }

    /**
     * This method initializes jTextFieldConnection
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldConnection(): JTextField? {
        if (jTextFieldConnection == null) {
            jTextFieldConnection = JTextField("jdbc:mysql://localhost:3306/lepidoptera")
        }
        return jTextFieldConnection
    }

    /**
     * This method initializes jTextFieldDialect
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDialect(): JTextField? {
        if (jTextFieldDialect == null) {
            jTextFieldDialect = JTextField("org.hibernate.dialect.MySQLDialect")
        }
        return jTextFieldDialect
    }

    /**
     * This method initializes jButtonLogin
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonLogin(): JButton? {
        if (jButtonLogin == null) {
            jButtonLogin = JButton("Login")
            jButtonLogin.setMnemonic(KeyEvent.VK_L)
            jButtonLogin.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    result = RESULT_LOGIN
                    jButtonLogin.grabFocus()
                    userPasswordHash
                    self.setVisible(false)
                }
            })
        }
        return jButtonLogin
    }

    /**
     * This method initializes jButtonCancel
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonCancel(): JButton? {
        if (jButtonCancel == null) {
            jButtonCancel = JButton("Cancel")
            jButtonCancel.setMnemonic(KeyEvent.VK_C)
            jButtonCancel.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    result = RESULT_CANCEL
                    self.setVisible(false)
                }
            })
        }
        return jButtonCancel
    }

    /**
     * Obtain sha1 hash of the text in the user's Password field.  Assumes that
     * the text is in utf-8 encoding.  If SHA-1 isn't an available MessageDigest,
     * returns the plain text of the password.
     *
     * @return the SHA1 hash of the text in the (user)Password field.
     */
    val userPasswordHash: String?
        get() = hashPassword(jPasswordFieldUser)

    val username: String?
        get() = jTextFieldEmail.getText()

    var dBUserName: String?
        get() = jTextFieldDBUsername.getText()
        set(aDBSchemaName) {
            jTextFieldDBUsername.setText(aDBSchemaName)
        }

    // Force advanced panel to open if no database password is stored.
    var dBPassword: String?
        get() = String(jPasswordFieldDB.getPassword())
        set(aDBPassword) {
            jPasswordFieldDB.setText(aDBPassword)
            // Force advanced panel to open if no database password is stored.
            if (aDBPassword == null || aDBPassword.length == 0) {
                jPanelAdvanced.setVisible(true)
            }
        }

    /**
     * @param textFieldDriver the jTextFieldDriver to set
     */
    var driver: String?
        get() = jTextFieldDriver.getText()
        set(textFieldDriver) {
            jTextFieldDriver.setText(textFieldDriver)
        }

    /**
     * @param textFieldConnection the jTextFieldConnection to set
     */
    var connection: String?
        get() = jTextFieldConnection.getText()
        set(textFieldConnection) {
            jTextFieldConnection.setText(textFieldConnection)
        }

    /**
     * @param textFieldDialect the jTextFieldDialect to set
     */
    var dialect: String?
        get() = jTextFieldDialect.getText()
        set(textFieldDialect) {
            jTextFieldDialect.setText(textFieldDialect)
        }

    fun setStatus(aStatus: String?) {
        jLabelStatus.setText(aStatus)
        jLabelStatus.updateUI()
    }

    /**
     * This method initializes jTextFieldEmail
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldEmail(): JTextField? {
        if (jTextFieldEmail == null) {
            jTextFieldEmail = JTextField()
        }
        return jTextFieldEmail
    }

    /**
     * This method initializes jPasswordFieldUser
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordFieldUser(): JPasswordField? {
        if (jPasswordFieldUser == null) {
            jPasswordFieldUser = JPasswordField()
        }
        return jPasswordFieldUser
    }

    /**
     * This method initializes jButton2
     *
     * @return javax.swing.JButton
     */
    private val advancedSettingsJButton: JButton?
        private get() {
            if (jButton2 == null) {
                jButton2 = JButton()
                jButton2.setText("Advanced")
                jButton2.setMnemonic(KeyEvent.VK_A)
                jButton2.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        toggleAdvanced()
                    }
                })
            }
            return jButton2
        }

    fun toggleAdvanced() {
        if (jPanelAdvanced.isVisible()) {
            this.setSize(Dimension(650, 225))
            jPanelAdvanced.setVisible(false)
        } else {
            this.setSize(Dimension(650, 355))
            jPanelAdvanced.setVisible(true)
        }
        //    this.pack();
    }

    /**
     * This method initializes jPanelAdvanced
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelAdvanced(): JPanel? {
        if (jPanelAdvanced == null) {
            val jLabel4 = JLabel()
            jLabel4.setText("Dialect")
            val jLabel3 = JLabel()
            jLabel3.setText("Connection")
            val jLabel2 = JLabel()
            jLabel2.setText("Driver")
            val jLabel1 = JLabel()
            jLabel1.setText("DBPassword")
            val jLabel = JLabel()
            jLabel.setText("Username")
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.anchor = GridBagConstraints.WEST
            gridBagConstraints3.gridx = 1
            gridBagConstraints3.gridy = 1
            gridBagConstraints3.weightx = 1.0
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.anchor = GridBagConstraints.EAST
            gridBagConstraints4.gridy = 1
            gridBagConstraints4.insets = Insets(0, 15, 0, 0)
            gridBagConstraints4.gridx = 0
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.gridx = 1
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.gridy = 0
            gridBagConstraints.gridx = 0
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.anchor = GridBagConstraints.WEST
            gridBagConstraints10.gridx = 1
            gridBagConstraints10.gridy = 3
            gridBagConstraints10.weightx = 1.0
            gridBagConstraints10.fill = GridBagConstraints.BOTH
            val gridBagConstraints14 = GridBagConstraints()
            gridBagConstraints14.anchor = GridBagConstraints.WEST
            gridBagConstraints14.gridx = 1
            gridBagConstraints14.gridy = 2
            gridBagConstraints14.fill = GridBagConstraints.BOTH
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.anchor = GridBagConstraints.EAST
            gridBagConstraints9.gridy = 3
            gridBagConstraints9.gridx = 0
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.anchor = GridBagConstraints.WEST
            gridBagConstraints6.gridx = 1
            gridBagConstraints6.gridy = 2
            gridBagConstraints6.weightx = 1.0
            gridBagConstraints6.fill = GridBagConstraints.BOTH
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.anchor = GridBagConstraints.EAST
            gridBagConstraints5.gridy = 2
            gridBagConstraints5.gridx = 0
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.anchor = GridBagConstraints.WEST
            gridBagConstraints8.gridx = 1
            gridBagConstraints8.gridy = 5
            gridBagConstraints8.weightx = 1.0
            gridBagConstraints8.fill = GridBagConstraints.BOTH
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.gridy = 5
            gridBagConstraints7.insets = Insets(0, 5, 0, 0)
            jPanelAdvanced = JPanel()
            jPanelAdvanced.setLayout(GridBagLayout())
            jPanelAdvanced.add(jLabel3, gridBagConstraints7)
            jPanelAdvanced.add(getJTextFieldConnection(), gridBagConstraints8)
            jPanelAdvanced.add(jLabel2, gridBagConstraints5)
            jPanelAdvanced.add(getJTextFieldDriver(), gridBagConstraints6)
            jPanelAdvanced.add(jLabel4, gridBagConstraints9)
            jPanelAdvanced.add(getJTextFieldDialect(), gridBagConstraints10)
            jPanelAdvanced.add(jLabel, gridBagConstraints)
            jPanelAdvanced.add(jTextFieldSchemaName, gridBagConstraints1)
            jPanelAdvanced.add(jLabel1, gridBagConstraints4)
            jPanelAdvanced.add(getJPasswordFieldDB(), gridBagConstraints3)
        }
        return jPanelAdvanced
    }

    companion object {
        const val RESULT_CANCEL = 0
        const val RESULT_LOGIN = 1
        private const val serialVersionUID = -2016769537635603794L
        private val log: Log? = LogFactory.getLog(LoginDialog::class.java)
        fun hashPassword(ajPasswordField: JPasswordField): String? {
            return HashUtility.getSHA1Hash(String(ajPasswordField.getPassword()))
        }
    }

    /**
     * Default constructor.  Produces a login dialog.
     */
    init {
        self = this
        initialize()
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
