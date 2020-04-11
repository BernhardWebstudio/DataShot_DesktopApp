/**
 *
 */
package edu.harvard.mcz.imagecaptureimport

import org.apache.commons.logging.LogFactory

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**Parses the text on an MCZ Lepidoptera unit tray label into atomic higher taxon and species group name
 * elements.
 *
 *
 *
 */
class UnitTrayLabelParser(private var text: String?) : TaxonNameReturner, DrawerNameReturner, CollectionReturner {
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getFamily()
     */ var family: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSubfamily()
     */ var subfamily: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getTribe()
     */ var tribe: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getGenus()
     */ var genus: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSpecificEpithet()
     */ var specificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getSubspecificEpithet()
     */ var subspecificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getAuthorship()
     */ var authorship: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getInfraspecificEpithet()
     */ var infraspecificEpithet: String? = null public get() {
        return field
    }
        private set
    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.TaxonNameParser#getInfraspecificRank()
     */ var infraspecificRank: String? = null public get() {
        return field
    }
        private set
    private var drawerNumber: String? = null
    /**
     * @param collection the collection to set
     */
    var collection // collection from which the material came
            : String? = null public get() {
        return field
    }
    /**
     * @return the identifiedBy
     */
    /**
     * @param identifiedBy the identifiedBy to set
     */
    var identifiedBy: String? = null public get() {
        return field
    }
    /**
     * Was this Parse done from JSON.
     *
     * @return true if parsed from json, otherwise false.
     */
    var isParsedFromJSON = false
        private set

    protected fun parseFromJSON(json: String): Boolean {
        var result = false
        val label: UnitTrayLabel = UnitTrayLabel.Companion.createFromJSONString(json)
        if (label != null) {
            result = true
            family = label.getFamily()
            subfamily = label.getSubfamily()
            tribe = label.getTribe()
            genus = label.getGenus()
            specificEpithet = label.getSpecificEpithet()
            subspecificEpithet = label.getSubspecificEpithet()
            authorship = label.getAuthorship()
            infraspecificEpithet = label.getInfraspecificEpithet()
            infraspecificRank = label.getInfraspecificRank()
            drawerNumber = label.getDrawerNumber()
            isParsedFromJSON = true
        }
        return result
    }

    /**
     * Identify atomic database field elements from their position in aStringToParse as
     * given to the constructor (called by the constructor).  Invokes the protected set_ methods.
     */
    protected fun parse() {
        family = ""
        subfamily = ""
        tribe = ""
        genus = ""
        specificEpithet = ""
        subspecificEpithet = ""
        authorship = ""
        infraspecificEpithet = ""
        infraspecificRank = ""
        drawerNumber = ""
        // Can't parse text if it is null.
        if (text != null) { // trim out some likely OCR errors
            text = text!!.replace("|", "")
            text = text!!.replace("í", "i") // ocr on utf8 filesystem
            //text = text.replace("Ã-", "i");   // "tilda A -" from ocr on windows filesystem
            val higherbits: Array<String?> = text!!.split(":".toRegex()).toTypedArray()
            var nameStartsAt = 0
            // look for the higher taxon bits
            if (higherbits.size > 0) { // get text before first colon
// Check for extraneous leading line and remove if present.
                val possFamily: Array<String?> = higherbits[0]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()
                if (possFamily.size == 2) {
                    setFamily(possFamily[1]!!.trim { it <= ' ' })
                } else {
                    if (possFamily[0]!!.trim { it <= ' ' }.contains(" ")) { // Check for the case of a failed OCR of the separator colon.
// Start by setting the family.
                        setFamily(higherbits[0]!!.trim { it <= ' ' })
                        // now check.
                        val bits: Array<String?> = possFamily[0]!!.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
                        if (bits.size == 2 && bits[0]!!.length > 4 && bits[1]!!.length > 4) {
                            setFamily(bits[0]!!.trim { it <= ' ' })
                            setSubfamily(bits[1]!!.trim { it <= ' ' })
                        }
                    } else {
                        setFamily(higherbits[0]!!.trim { it <= ' ' })
                    }
                }
                nameStartsAt = 1
            }
            if (higherbits.size > 1) {
                if (higherbits.size > 2) { // get first word after colon
                    setSubfamily(higherbits[1]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray()[0]) // split on one or more spaces
                } else { // get everything else on the first line
// handles the pathological case of a space within the subfamily name.
                    setSubfamily(higherbits[1]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()[0])
                }
                nameStartsAt = 1
            }
            if (higherbits.size > 2) { // Two colons, should be a tribe.
                val temp: Array<String?> = higherbits[2]!!.split("\n".toRegex()).toTypedArray()
                if (temp.size > 0 && temp[0]!!.trim { it <= ' ' } == "") { // second colon was followed by a newline character with no preceding text.
                    if (temp.size > 1 && temp[1]!!.trim { it <= ' ' }.contains(" ")) { // likely pathological case of two colons and no tribe 'family:subfamily:\n genus'
// second line contains a genus/species
// 'family:subfamily:' ends with colon but has no tribe.
                        nameStartsAt = 2 // split of higher bits still puts genus in next element.
                        // but there is no tribe to set.
                    } else { // Likely Tribe on second line, thus an extra leading newline.
// family: subfamily: \n tribe \n genus species
                        setTribe(higherbits[2]!!.trim { it <= ' ' }.split("[ \n]".toRegex()).toTypedArray()[0]) // split on space or new line
                        nameStartsAt = 2
                        // trim the leading newline off the beginning of the stuff that follows the second colon.
                        higherbits[2] = higherbits[2]!!.trim { it <= ' ' }
                    }
                } else { // Should be 'tribe \n genus'
// get first word after second colon, if any
                    setTribe(higherbits[2]!!.trim { it <= ' ' }.split("[ \n]".toRegex()).toTypedArray()[0]) // split on space or new line
                    nameStartsAt = 2
                }
            }
            var lines: Array<String?>? = null
            // Test for species group name spread on two lines.
            try {
                lines = higherbits[nameStartsAt]!!.split("\n".toRegex()).toTypedArray()
                if (lines.size > 4) {
                    val st = 1
                    if (lines[st]!!.trim { it <= ' ' }.contains(" ") &&
                            lines[st + 1]!!.trim { it <= ' ' }.matches(".*[A-Za-z]+.*") &&
                            lines[st + 2]!!.trim { it <= ' ' }.matches(".*[A-Za-z]+.*[0-9]+.*") &&
                            lines[st + 3]!!.trim { it <= ' ' }.matches(".*[0-9]+.*")) {
                        higherbits[nameStartsAt] = lines[0].toString() + "\n" + lines[st] + " " + lines[st + 1] + "\n" + lines[st + 2] + "\n" + lines[st + 3]
                    }
                }
            } catch (e: ArrayIndexOutOfBoundsException) { // expected if species group name is on one line.
            }
            // look for the species group name
            if (higherbits.size == nameStartsAt + 1 && higherbits.size > 0) {
                lines = higherbits[nameStartsAt]!!.split("\n".toRegex()).toTypedArray()
                if (lines.size > 1) {
                    val speciesGroupName: Array<String?> = lines[1]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray() // split on one or more spaces
                    // TODO: Test for trinomial flowing onto two lines with authorship on third.
                    try {
                        parseSpeciesGroupName(speciesGroupName)
                        setAuthorship(lines[2]!!.trim { it <= ' ' })
                        setDrawerNumber(lines[3]!!.trim { it <= ' ' })
                    } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                    }
                }
            } else if (higherbits.size == 1) { // Handle pathological case of no colon found in higher taxon name string.
//System.out.println(higherbits[0]);
                lines = higherbits[0]!!.split("\n".toRegex()).toTypedArray()
                setFamily(lines[0]!!.trim { it <= ' ' })
                val possFamily: Array<String?> = higherbits[0]!!.trim { it <= ' ' }.split("\n".toRegex()).toTypedArray()
                log!!.debug(possFamily.size + possFamily[0])
                if (possFamily[0]!!.trim { it <= ' ' }.contains(" ")) { // Check for the case of a failed OCR of the separator colon.
// now check.
                    val bits: Array<String?> = possFamily[0]!!.trim { it <= ' ' }.split(" ".toRegex()).toTypedArray()
                    if (bits.size == 2 && bits[0]!!.length > 4 && bits[1]!!.length > 4) {
                        setFamily(bits[0]!!.trim { it <= ' ' })
                        setSubfamily(bits[1]!!.trim { it <= ' ' })
                    }
                }
                try {
                    setAuthorship(lines[lines.size - 2]!!.trim { it <= ' ' })
                    setDrawerNumber(lines[lines.size - 1]!!.trim { it <= ' ' })
                    if (lines[lines.size - 2]!!.trim { it <= ' ' }.matches(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                        setAuthorship(lines[lines.size - 3]!!.trim { it <= ' ' })
                        setDrawerNumber(lines[lines.size - 2]!!.trim { it <= ' ' })
                    }
                } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                    log.debug(e)
                }
                if (lines.size > 1) {
                    try {
                        val speciesGroupName: Array<String?> = lines[lines.size - 3]!!.trim { it <= ' ' }.split(" +".toRegex()).toTypedArray() // split on one or more spaces
                        parseSpeciesGroupName(speciesGroupName)
                    } catch (e: ArrayIndexOutOfBoundsException) { // unexpected, but possible if elements are missing
                        log.debug("Missing element in: " + higherbits[0]!!.replace("\n", ":"))
                    }
                    // TODO: Test for trinomial flowing onto two lines with authorship on third.
                }
            } // higherbits length == 1
            // recheck patterns in parse
            if (lines != null) {
                var drawernumberOnLine = -1
                var authorshipOnLine = -1
                for (i in lines.indices) {
                    if (lines[i]!!.trim { it <= ' ' }.matches(Singleton.Companion.getSingletonInstance().getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_REGEX_DRAWERNUMBER))) {
                        drawernumberOnLine = i
                    }
                    //   \(?[A-Za-z& ]*,[0-9]{4}\)?
                    if (lines[i]!!.trim { it <= ' ' }.matches("\\(?[A-Za-z& ]*, [0-9]{4}\\)?")) {
                        authorshipOnLine = i
                    }
                }
                if (authorshipOnLine > 0 && authorship != lines[authorshipOnLine]!!.trim { it <= ' ' }) {
                    setAuthorship(lines[authorshipOnLine]!!.trim { it <= ' ' })
                }
                if (drawernumberOnLine > 0 && getDrawerNumber() != lines[drawernumberOnLine]!!.trim { it <= ' ' }) {
                    setDrawerNumber(lines[drawernumberOnLine]!!.trim { it <= ' ' })
                }
            }
        } // text is not null
    }

    /** given a species group name in a string array, parses the component genus, species,
     * subspecies, infraspecific, and infraspecific rank parts.
     * @param speciesGroupName the species group name to parse.
     */
    private fun parseSpeciesGroupName(speciesGroupName: Array<String?>?) {
        if (speciesGroupName!![0]!!.length == 1) { // Probably an incorrect OCR inserting a space after the first letter of the Generic name
// Handle as special case by moving everything over by 1.
            speciesGroupName[0] = speciesGroupName[0] + speciesGroupName[1]
            if (speciesGroupName.size > 2) {
                speciesGroupName[1] = speciesGroupName[2]
                if (speciesGroupName.size > 3) {
                    for (i in 3 until speciesGroupName.size) {
                        speciesGroupName[i - 1] = speciesGroupName[i]
                        speciesGroupName[i] = ""
                    }
                } else {
                    speciesGroupName[2] = ""
                }
            }
        }
        setGenus(speciesGroupName[0]!!)
        if (speciesGroupName.size > 1) {
            setSpecificEpithet(speciesGroupName[1]!!)
            if (speciesGroupName.size > 2) {
                if (speciesGroupName[2]!!.matches("^var.|forma|f.|form")) {
                    try {
                        setInfraspecificEpithet(speciesGroupName[3]!!)
                        setInfraspecificRank(speciesGroupName[2]!!)
                    } catch (e: ArrayIndexOutOfBoundsException) { // Unexpected error found infrasubspecific indicator with no epithet
// following it.
                        log!!.error("Parsing error: found infrasubspecific rank with no epithet in: " + speciesGroupName.toString().trim { it <= ' ' })
                    }
                } else {
                    try {
                        setSubspecificEpithet(speciesGroupName[2]!!)
                    } catch (e: ArrayIndexOutOfBoundsException) { //expected for species
                    }
                }
            } // speciesgroupname .length > 2
        } // speciesgroupname .length > 1
    }

    /**
     * @param authorship the authorship to set
     */
    protected fun setAuthorship(authorship: String) {
        this.authorship = authorship.trim { it <= ' ' }
    }

    /**
     * @param family the family to set
     */
    protected fun setFamily(family: String) {
        this.family = family.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o').replace("\\s".toRegex(), "").replace("^1".toRegex(), "")
        // Truncate to database field size.
// Truncating family is probably the most critical, as bad OCR where colons aren't read
// will end up here.
        if (this.family!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Family")) {
            this.family = this.family!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Family"))
        }
    }

    /**
     * @param subfamily the subfamily to set
     */
    protected fun setSubfamily(subfamily: String) {
        this.subfamily = subfamily.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o').replace("\\s".toRegex(), "")
        if (this.subfamily!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Subfamily")) {
            this.subfamily = this.subfamily!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Subfamily"))
        }
    }

    /**
     * @param tribe the tribe to set
     */
    protected fun setTribe(tribe: String) {
        this.tribe = tribe.replace('\n', ' ').trim { it <= ' ' }.replace('0', 'o')
        if (this.tribe!!.length > MetadataRetriever.getFieldLength(Specimen::class.java, "Tribe")) {
            this.tribe = this.tribe!!.substring(0, MetadataRetriever.getFieldLength(Specimen::class.java, "Tribe"))
        }
    }

    /**
     * @param genus the genus to set
     */
    protected fun setGenus(genus: String) { // strip off leading/trailing whitespace
// strip off a leading [
        this.genus = genus.trim { it <= ' ' }.replaceFirst("^\\[".toRegex(), "")
    }

    /**
     * @param specificEpithet the specificEpithet to set
     */
    protected fun setSpecificEpithet(specificEpithet: String) {
        this.specificEpithet = specificEpithet.trim { it <= ' ' }
    }

    /**
     * @param subspecificEpithet the subspecificEpithet to set
     */
    protected fun setSubspecificEpithet(subspecificEpithet: String) {
        this.subspecificEpithet = subspecificEpithet.trim { it <= ' ' }
    }

    /**
     * @param infraspecificEpithet the infraspecificEpithet to set
     */
    protected fun setInfraspecificEpithet(infraspecificEpithet: String) {
        this.infraspecificEpithet = infraspecificEpithet.trim { it <= ' ' }
    }

    /**
     * @param infraspecificRank the infraspecificRank to set
     */
    protected fun setInfraspecificRank(infraspecificRank: String) {
        this.infraspecificRank = infraspecificRank.replace('\n', ' ').trim { it <= ' ' }
    }

    /* (non-Javadoc)
     * @see edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturner#getDrawerNumber()
     */
    override fun getDrawerNumber(): String {
        return drawerNumber!!.trim { it <= ' ' }
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    protected fun setDrawerNumber(drawerNumber: String) {
        this.drawerNumber = drawerNumber.replace('\n', ' ').trim { it <= ' ' }
    }

    companion object {
        private val log = LogFactory.getLog(UnitTrayLabelParser::class.java)
    }

    /**
     * Create a unit tray label parser and parse the text.  Call the get methods of the
     * instance to return the parsed text.
     *
     * Usage:
     * `
     * Specimen s = new Specimen();
     * UnitTrayLabelParser p = new UnitTrayLabelParser(aStringFromOCR);
     * s.setFamily(p.getFamily());
    ` *
     *
     * @param aStringToParse
     */
    init {
        log!!.debug(text)
        var done = false
        //allie edit
        if (text != null && text!!.endsWith("}") && text!!.startsWith("{")) {
            if (parseFromJSON(text!!)) {
                done = true
            }
        }
        if (!done) {
            parse()
        }
    }
}