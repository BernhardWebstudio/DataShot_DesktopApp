package edu.harvard.mcz.imagecapture.lifecycleimport

import edu.harvard.mcz.imagecapture.data.HibernateUtil
import org.apache.commons.logging.LogFactory
import java.util.*

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class ICImage.
 * Refactored from generated EJB home class to use with local singleton
 * HibernateUtil for sessions instead of EJB. Renamed from Home to LifeCycle to
 * prevent overwriting by Hibernate Tools
 *
 * @see ICImage
 */
class ICImageLifeCycle : GenericLifeCycle<ICImage?>(ICImage::class.java, log) {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: ICImage?) {
        log!!.debug("persisting ICImage instance")
        try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(transientInstance)
                session.transaction.commit()
                log!!.debug("persist successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to Image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("persist failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun attachDirty(instance: ICImage?) {
        log!!.debug("attaching dirty ICImage instance")
        try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                session.transaction.commit()
                log!!.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("attach failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun attachClean(instance: ICImage?) {
        log!!.debug("attaching clean ICImage instance")
        try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                session.transaction.commit()
                log!!.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("attach failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun delete(persistentInstance: ICImage?) {
        log!!.debug("deleting ICImage instance")
        try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                session.delete(persistentInstance)
                session.transaction.commit()
                log!!.debug("delete successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Delete from image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log!!.error("delete failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun merge(detachedInstance: ICImage?): ICImage? {
        log!!.debug("merging ICImage instance")
        return try {
            var result: ICImage? = detachedInstance
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as ICImage
                session.transaction.commit()
                log!!.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
                throw SaveFailedException("Save to image table failed. " +
                        e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            result
        } catch (re: RuntimeException) {
            log!!.error("merge failed", re)
            throw re
        }
    }

    fun findById(id: Long?): ICImage? {
        log!!.debug("getting ICImage instance with id: $id")
        return try {
            var instance: ICImage? = null
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session["edu.harvard.mcz.imagecapture.entity.ICImage", id] as ICImage
                session.transaction.commit()
                if (instance == null) {
                    log!!.debug("get successful, no instance found")
                } else {
                    log!!.debug("get successful, instance found")
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error(e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            instance
        } catch (re: RuntimeException) {
            log!!.error("get failed", re)
            throw re
        }
    }

    fun findOneBySpecimen(specimen: Specimen): ICImage? {
        return super.findOneBy("SPECIMENID", specimen.getSpecimenId())
    }

    fun findOneByPath(path: String?): ICImage? {
        return super.findOneBy("path", path)
    }

    fun findBySpecimen(specimen: Specimen): MutableList<ICImage?>? {
        return super.findBy("SPECIMENID", specimen.getSpecimenId())
    }

    fun findByPath(path: String?): MutableList<ICImage?>? {
        return super.findBy("path", path)
    }

    /**
     * @return list of all image files sorted by filename
     */
    fun findAll(): MutableList<ICImage?>? {
        log!!.debug("finding all Images")
        return try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<ICImage?>? = null
            try {
                results = session
                        .createQuery("From ICImage i order by i.filename")
                        .list() as MutableList<ICImage?>
                log!!.debug("find all successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find all failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find all failed", re)
            throw re
        }
    }

    /**
     * Find images with a particular path.
     *
     * @return list of all image files with a particular path.
     */
    fun findAllInDir(path: String?): MutableList<ICImage?>? {
        log!!.debug("finding all Images with path $path")
        return try {
            val session = HibernateUtil.getSessionFactory()!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<ICImage?>? = null
            try {
                results = session
                        .createQuery("From ICImage i where path = '" + path +
                                "' order by i.filename")
                        .list() as MutableList<ICImage?>
                log!!.debug("find in directory successful, result size: " +
                        results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log!!.error("find in dir failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find in dir failed", re)
            throw re
        }
    }// put blank at top of list.

    /**
     * Obtain an array of distinct values of paths suitable for populating a
     * picklist.
     * Example usage:
     * `
     * ICImageLifeCycle ils = new ICImageLifeCycle();
     * JComboBox jComboPaths = new JComboBox(ils.getDistinctPaths());
     * jComboPaths.setEditable(true);
     * TableColumn pathColumn =
     * jTableImages.getColumnModel().getColumn(ICImageListTableModel.COL_PATH);
     * pathColumn.setCellEditor(new DefaultCellEditor(jComboPaths));
    ` *
     *
     * @return a string array containing the distinct values of IMAGE path with a
     * "" as the
     * first item in the array.
     */
    val distinctPaths: Array<String?>?
        get() {
            val types = ArrayList<String?>()
            types.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct path from ICImage im where im.path is not null order by im.path  "
                val session = HibernateUtil.getSessionFactory()!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        types.add(value)
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                }
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
                types.toArray(arrayOf())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    companion object {
        private val log = LogFactory.getLog(ICImageLifeCycle::class.java)
        fun findMismatchedImages(): MutableList<ICImage?>? {
            log!!.debug("finding Images with barcode missmatches ")
            return try {
                val session = HibernateUtil.getSessionFactory()!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where (rawBarcode <> rawExifBarcode or (rawBarcode is null) or (rawExifBarcode is null)) order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug("find missmatches successful, result size: " +
                            results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find missmatches failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find missmatches failed", re)
                throw re
            }
        }

        fun findDrawerImages(aDrawerNumber: String): MutableList<ICImage?>? {
            log!!.debug("finding Images for drawer $aDrawerNumber")
            return try {
                val session = HibernateUtil.getSessionFactory()!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where specimen is null and drawerNumber = '" +
                                            aDrawerNumber.trim { it <= ' ' } + "' order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug("find all successful, result size: " + results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find all failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find all failed", re)
                throw re
            }
        }

        fun findNotDrawerNoTemplateImages(): MutableList<ICImage?>? {
            log!!.debug("finding Images for template " +
                    PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS)
            return try {
                val session = HibernateUtil.getSessionFactory()!!.currentSession
                session!!.beginTransaction()
                var results: MutableList<ICImage?>? = null
                try {
                    results = session
                            .createQuery(
                                    "From ICImage i where specimen is not null and templateId = '" +
                                            PositionTemplate.Companion.TEMPLATE_NO_COMPONENT_PARTS.trim({ it <= ' ' }) +
                                            "' order by i.filename")
                            .list() as MutableList<ICImage?>
                    log.debug(
                            "find all whole image only records with specimens successful, result size: " +
                                    results!!.size)
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error("find all whole image only records failed", e)
                }
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                results
            } catch (re: RuntimeException) {
                log.error("find all whole image only records failed", re)
                throw re
            }
        }
    }
}