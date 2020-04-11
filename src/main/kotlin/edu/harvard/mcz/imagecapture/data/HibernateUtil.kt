package edu.harvard.mcz.imagecapture.dataimport

import org.apache.commons.logging.Logimport

org.apache.commons.logging.LogFactoryimport org.hibernate.Sessionimport org.hibernate.SessionFactoryimport org.hibernate.cfg.Configurationimport org.hibernate.service.spi.ServiceExceptionimport java.awt.Cursorimport java.lang.Exceptionimport java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * Singleton class to obtain access to Hibernate sessions, used in the *LifeCycle classes.
 *
 *
 * Modified from the hibernate tutorial
 * http://www.hibernate.org/hib_docs/v3/reference/en-US/html/tutorial-firstapp.html
 * Changed singleton implementation to allow loading of credentials from config and dialog at runtime
 */
object HibernateUtil {
    private val log: Log? = LogFactory.getLog(HibernateUtil::class.java)
    /**
     * Call this method to obtain a Hibernate Session.
     *
     *
     * Usage:
     * <pre>
     * Session session = HibernateUtil.getSessionFactory().getCurrentSession();
     * session.beginTransaction();
    </pre> *
     *
     * @return the Hibernate SessionFactory.
     */
    val sessionFactory: SessionFactory? = null
        get() {
            if (edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory == null) {
                edu.harvard.mcz.imagecapture.data.HibernateUtil.createSessionFactory()
            }
            return edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory
        }
    val properties: Properties = Properties()
        get() = edu.harvard.mcz.imagecapture.data.HibernateUtil.properties

    fun terminateSessionFactory() {
        try {
            edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.currentSession.cancelQuery()
            edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.currentSession.clear()
            edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.currentSession.close()
        } catch (e: Exception) {
        } finally {
            try {
                edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.close()
            } catch (e1: Exception) {
            } finally {
                edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = null
            }
        }
    }

    /**
     * Reset the session factory by terminating & starting a new one
     */
    fun restartSessionFactory() {
        edu.harvard.mcz.imagecapture.data.HibernateUtil.terminateSessionFactory()
        val configuration: Configuration = Configuration().configure().setProperties(edu.harvard.mcz.imagecapture.data.HibernateUtil.properties)
        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = configuration.buildSessionFactory()
    }

    /**
     * Using the Hibernate configuration in Configuration from hibernate.cfg.xml
     * create a Hibernate sessionFactory.  Method is private as the the session factory
     * should be a singleton, invoke getSessionFactory() to create or access a session.
     *
     * @see edu.harvard.mcz.imagecapture.data.HibernateUtil.getSessionFactory
     */
    private fun createSessionFactory() {
        try {
            if (edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory != null) {
                edu.harvard.mcz.imagecapture.data.HibernateUtil.terminateSessionFactory()
            }
        } catch (e: Exception) {
            edu.harvard.mcz.imagecapture.data.HibernateUtil.log.error(e.message)
        }
        try { // Create the Configuration from hibernate.cfg.xml
            var configuration: Configuration = Configuration().configure()
            // Add authentication properties obtained from the user
            var success = false
            // retrieve the connection parameters from hibernate.cfg.xml and load into the LoginDialog
            var loginDialog: LoginDialog = edu.harvard.mcz.imagecapture.data.HibernateUtil.getLoginDialog(configuration, null)
            while (!success && loginDialog.getResult() != LoginDialog.Companion.RESULT_CANCEL) { // Check authentication (starting with the database user(schema)/password.
                if (loginDialog.getResult() == LoginDialog.Companion.RESULT_LOGIN) {
                    if (Singleton.Companion.getSingletonInstance().getMainFrame() != null) {
                        Singleton.Companion.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                        Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Connecting to database")
                    }
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_PASSWORD, loginDialog.getDBPassword())
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_USER, loginDialog.getDBUserName())
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_URL, loginDialog.getConnection())
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_DIALECT, loginDialog.getDialect())
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_DRIVER, loginDialog.getDriver())
                    configuration.setProperties(edu.harvard.mcz.imagecapture.data.HibernateUtil.properties)
                    // Now create the SessionFactory from this configuration
                    edu.harvard.mcz.imagecapture.data.HibernateUtil.log.debug(configuration.getProperty(ImageCaptureProperties.Companion.KEY_DB_URL))
                    try {
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = configuration.buildSessionFactory()
                    } catch (ex: JDBCConnectionException) {
                        configuration = Configuration().configure()
                        success = false
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = null
                        loginDialog = edu.harvard.mcz.imagecapture.data.HibernateUtil.getLoginDialog(configuration, "Initial SessionFactory creation failed. Database not connectable: " + ex.message)
                        try {
                            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Database connection failed.")
                        } catch (e: NullPointerException) { // expected if we haven't instantiated a main frame.
                        }
                        println("Initial SessionFactory creation failed.$ex")
                    } catch (ex: ServiceException) {
                        configuration = Configuration().configure()
                        success = false
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = null
                        loginDialog = edu.harvard.mcz.imagecapture.data.HibernateUtil.getLoginDialog(configuration, "Initial SessionFactory creation failed. Database not connectable: " + ex.message)
                        try {
                            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Database connection failed.")
                        } catch (e: NullPointerException) {
                        }
                        println("Initial SessionFactory creation failed.$ex")
                    } catch (ex: Throwable) { // Make sure you log the exception, as it might be swallowed
                        println("Initial SessionFactory creation failed.$ex")
                        throw ex
                        //throw new ExceptionInInitializerError(ex);
                    }
                    try { // Check database authentication by beginning a transaction.
                        val session: Session? = edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.currentSession
                        session.beginTransaction()
                        session.close()
                        // If an exception hasn't been thrown, dbuser/dbpassword has
// successfully authenticated against the database.
// Now try authenticating the individual user by the email address/password that they provided.
                        val uls = UsersLifeCycle()
                        val foundUser: MutableList<Users?> = uls.findByCredentials(loginDialog.getUsername(), loginDialog.getUserPasswordHash())
                        if (foundUser.size == 1) { // There should be one and only one user returned.
                            if (foundUser[0].getUsername() == loginDialog.getUsername() && foundUser[0].getHash() == loginDialog.getUserPasswordHash()) { // and that user must have exactly the username/password hash provided in the dialog.
                                Singleton.Companion.getSingletonInstance().setCurrentUser(foundUser[0])
                                success = true
                                try {
                                    Singleton.Companion.getSingletonInstance().getMainFrame().setState(MainFrame.Companion.STATE_RUNNING)
                                } catch (ex: NullPointerException) { // expected if we haven't instantiated a main frame.
                                }
                            }
                        }
                        if (!success) {
                            loginDialog = edu.harvard.mcz.imagecapture.data.HibernateUtil.getLoginDialog(configuration, "Login failed: Incorrect Email and/or Password.")
                            success = false
                            if (loginDialog.getUsername() != null) {
                                edu.harvard.mcz.imagecapture.data.HibernateUtil.log.debug("Login failed for " + loginDialog.getUsername())
                            }
                            edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.close()
                            edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = null
                            configuration = Configuration().configure()
                            try {
                                Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Login failed.")
                            } catch (ex: NullPointerException) { // expected if we haven't instantiated a main frame.
                            }
                        }
                    } catch (e: Throwable) {
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.log.error(e.message)
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.log.trace(e.message, e)
                        println("Initial SessionFactory creation failed." + e.message)
                        loginDialog = edu.harvard.mcz.imagecapture.data.HibernateUtil.getLoginDialog(configuration, "Login failed: " + e.cause)
                        success = false
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory!!.close()
                        edu.harvard.mcz.imagecapture.data.HibernateUtil.sessionFactory = null
                        configuration = Configuration().configure()
                        if (Singleton.Companion.getSingletonInstance().getMainFrame() != null) {
                            Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage("Login failed.")
                        }
                    }
                    if (Singleton.Companion.getSingletonInstance().getMainFrame() != null) {
                        Singleton.Companion.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    }
                }
            }
        } catch (ex: Throwable) { // Make sure you log the exception, as it might be swallowed
            ex.printStackTrace()
            println("Initial SessionFactory creation failed.$ex")
            println("Cause" + ex.cause!!.message)
            throw ExceptionInInitializerError(ex)
        }
    }

    /**
     * Get the login dialog, initialized with properties for the advanced, database (DB) configuration
     *
     * @param config the Hibernate configuration to use as a base
     * @param status the login status to display
     * @return
     */
    private fun getLoginDialog(config: Configuration, status: String?): LoginDialog {
        val loginDialog = LoginDialog()
        val settings: Properties = Singleton.Companion.getSingletonInstance().getProperties().getProperties()
        val keys: Enumeration<Any?> = settings.keys()
        // Detect usage of placeholders, replace with settings if available
        loginDialog.setConnection(edu.harvard.mcz.imagecapture.data.HibernateUtil.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_URL, "URL_PLACEHOLDER"))
        loginDialog.setDialect(edu.harvard.mcz.imagecapture.data.HibernateUtil.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_DIALECT, "DIALECT_PLACEHOLDER"))
        loginDialog.setDriver(edu.harvard.mcz.imagecapture.data.HibernateUtil.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_DRIVER, "DRIVER_CLASS_PLACEHOLDER"))
        // If the database username(schema) and password are present load them as well.
        loginDialog.setDBUserName(edu.harvard.mcz.imagecapture.data.HibernateUtil.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_USER, "USER_PLACEHOLDER"))
        loginDialog.setDBPassword(edu.harvard.mcz.imagecapture.data.HibernateUtil.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_PASSWORD, "PASSWORD_PLACEHOLDER"))
        // Display the LoginDialog as a modal dialog
        loginDialog.setModalityType(ModalityType.APPLICATION_MODAL)
        loginDialog.setVisible(true)
        if (status != null) {
            loginDialog.setStatus(status)
        }
        return loginDialog
    }

    /**
     * Get a value from settings by key if it has the value, else from config
     *
     * @param config   the config to be treated as a better default
     * @param settings the properties overwriting config, but only if config is still default ("placeholder")
     * @param key      the property key to get the config/setting by
     * @param value    the default we do not want, except we have nothing else
     * @return the value of the property
     */
    private fun getConfigOrSettingsValue(config: Configuration, settings: Properties, key: String?, value: String?): String? {
        val keys: Enumeration<Any?> = settings.keys()
        //        if (config.getProperty(key) == null || config.getProperty(key).equals(value)) {
        return if (settings.getProperty(key, value) != value) { //          log.debug("Found value = '" + value + "' for key " + key + ", getting " + settings.getProperty(key, value));
            settings.getProperty(key, value)
        } else { //          log.debug("Did not find value = '" + value + "' for key " + key + ", getting " + config.getProperty(key) + " vs. " + settings.getProperty(key, value));
            config.getProperty(key)
        }
    }

}