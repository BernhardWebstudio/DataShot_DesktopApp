/**
 * EditUserPanel.java
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

import java.awt.event.ActionEvent

javax.swing.JTextFieldimport java.awt.event.ActionListenerimport java.text.DecimalFormatimport java.lang.NumberFormatExceptionimport javax.swing.JComboBoximport edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRecimport java.awt.event.FocusListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.HigherGeographyComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringGeogJComboBoximport javax.swing.SwingUtilitiesimport java.lang.Runnableimport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycleimport edu.harvard.mcz.imagecapture.Singletonimport edu.harvard.mcz.imagecapture.ImageCapturePropertiesimport java.awt.event.FocusEventimport edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentNameimport edu.harvard.mcz.imagecapture.ui.tablemodel.AgentNameComboBoxModelimport edu.harvard.mcz.imagecapture.ui.field.FilteringAgentJComboBoximport edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycleimport javax.swing.JFrameimport edu.harvard.mcz.imagecapture.interfaces.RunnerListenerimport edu.harvard.mcz.imagecapture.ui.frame.MainFrameimport edu.harvard.mcz.imagecapture.ImageListBrowserimport edu.harvard.mcz.imagecapture.SpecimenBrowserimport edu.harvard.mcz.imagecapture.UserListBrowserimport javax.swing.JMenuBarimport javax.swing.JMenuimport javax.swing.JMenuItemimport javax.swing.JPanelimport javax.swing.JProgressBarimport javax.swing.JLabelimport edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycleimport edu.harvard.mcz.imagecapture.entity.Usersimport javax.swing.WindowConstantsimport java.awt.Taskbarimport java.lang.UnsupportedOperationExceptionimport java.lang.SecurityExceptionimport javax.swing.ImageIconimport edu.harvard.mcz.imagecapture.ImageCaptureAppimport java.lang.NullPointerExceptionimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycleimport edu.harvard.mcz.imagecapture.exceptions.ConnectionExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.AboutDialogimport javax.swing.JOptionPaneimport edu.harvard.mcz.imagecapture.jobs.JobAllImageFilesScanimport java.lang.Threadimport javax.swing.JComponentimport edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoadimport edu.harvard.mcz.imagecapture.jobs.JobSingleBarcodeScanimport java.awt.BorderLayoutimport edu.harvard.mcz.imagecapture.PropertiesEditorimport javax.swing.text.DefaultEditorKit.CopyActionimport javax.swing.KeyStrokeimport javax.swing.text.DefaultEditorKit.PasteActionimport edu.harvard.mcz.imagecapture.ui.frame.EventLogFrameimport java.awt.GridBagConstraintsimport java.awt.GridBagLayoutimport edu.harvard.mcz.imagecapture.interfaces.RunnableJobimport edu.harvard.mcz.imagecapture.entity.Specimenimport edu.harvard.mcz.imagecapture.PositionTemplateEditorimport edu.harvard.mcz.imagecapture.data .LocationInCollectionimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimToTranscribeDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimListDialogimport edu.harvard.mcz.imagecapture.jobs.JobFileReconciliationimport edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilderimport edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcherimport edu.harvard.mcz.imagecapture.entity.ICImageimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorimport edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialogimport edu.harvard.mcz.imagecapture.jobs.RunnableJobErrorTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SearchDialogimport java.lang.StringBuilderimport edu.harvard.mcz.imagecapture.ui.dialog.ChangePasswordDialogimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowserimport edu.harvard.mcz.imagecapture.jobs.JobRepeatOCRimport edu.harvard.mcz.imagecapture.ui.frame.RunnableJobFrameimport edu.harvard.mcz.imagecapture.jobs.JobCleanDirectoryimport edu.harvard.mcz.imagecapture.jobs.JobRecheckForTemplatesimport java.awt.Graphics2Dimport edu.harvard.mcz.imagecapture.utility.MathUtilityimport javax.swing.JViewportimport javax.swing.JScrollPaneimport javax.swing.JButtonimport javax.swing.JTableimport edu.harvard.mcz.imagecapture.entity.Trackingimport edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.TrackingTableModelimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrameimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.ScanDirectoryTaskimport edu.harvard.mcz.imagecapture.ui.frame.BulkMediaFrame.PrepareDirectoryActionimport javax.swing.border.EmptyBorderimport java.awt.GridLayoutimport javax.swing.SwingConstantsimport java.beans.PropertyChangeEventimport java.lang.Voidimport javax.swing.JFileChooserimport edu.harvard.mcz.imagecapture.ThumbnailBuilderimport java.io.PrintWriterimport edu.harvard.mcz.imagecapture.data .BulkMediaimport edu.harvard.mcz.imagecapture.CandidateImageFileimport java.io.IOExceptionimport java.io.FileNotFoundExceptionimport java.awt.event.MouseListenerimport java.awt.image.BufferedImageimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelimport javax.imageio.ImageIOimport edu.harvard.mcz.imagecapture.ui.frame.ImageZoomPanelimport edu.harvard.mcz.imagecapture.ui.ButtonRendererimport edu.harvard.mcz.imagecapture.ui.ButtonEditorimport edu.harvard.mcz.imagecapture.ui.ProgressBarRendererimport java.util.prefs.Preferencesimport javax.swing.JTabbedPaneimport edu.harvard.mcz.imagecapture.SpecimenControllerimport edu.harvard.mcz.imagecapture.ui.frame.ImageDisplayFrameimport edu.harvard.mcz.imagecapture.PositionTemplateimport edu.harvard.mcz.imagecapture.exceptions.BadTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.ImageLoadExceptionimport kotlin.jvm.Throwsimport java.awt.event.ComponentEventimport java.util.prefs.BackingStoreExceptionimport java.lang.IllegalStateExceptionimport net.miginfocom.swing.MigLayoutimport java.lang.IndexOutOfBoundsExceptionimport javax.swing.JSplitPaneimport javax.swing.BorderFactoryimport javax.swing.plaf.basic.BasicSplitPaneUIimport javax.swing.plaf.basic.BasicSplitPaneDividerimport edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycleimport java.util.HashSetimport java.util.HashMapimport edu.harvard.mcz.imagecapture.ui.dialog.TemplatePickerDialogimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimCaptureDialogimport edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatusimport javax.swing.JPopupMenuimport edu.harvard.mcz.imagecapture.ui.tablemodel.DeterminationTableModelimport edu.harvard.mcz.imagecapture.ui.frame.DeterminationFrameimport java.awt.event.MouseAdapterimport edu.harvard.mcz.imagecapture.entity.fixed.NatureOfIdimport javax.swing.DefaultCellEditorimport edu.harvard.mcz.imagecapture.entity.fixed.TypeStatusimport edu.harvard.mcz.imagecapture.data .MetadataRetrieverimport edu.harvard.mcz.imagecapture.entity.Determinationimport edu.harvard.mcz.imagecapture.ui.ValidatingTableCellEditorimport java.awt.Strokeimport javax.swing.JCheckBoximport edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorderimport javax.swing.JTextAreaimport edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPaneimport java.lang.StringBufferimport edu.harvard.mcz.imagecapture.exceptions.SaveFailedExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.NumberTableModelimport edu.harvard.mcz.imagecapture.entity.SpecimenPartimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModelimport edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModelimport edu.harvard.mcz.imagecapture.entity.LatLongimport javax.swing.DefaultComboBoxModelimport org.jdesktop.swingx.autocomplete.AutoCompleteDecoratorimport edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycleimport org.jdesktop.swingx.autocomplete.ComboBoxCellEditorimport edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycleimport javax.swing.table.DefaultTableCellRendererimport edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialogimport edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycleimport edu.harvard.mcz.imagecapture.entity.fixed.Seximport edu.harvard.mcz.imagecapture.entity.fixed.LifeStageimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycleimport javax.swing.table.AbstractTableModelimport edu.harvard.mcz.imagecapture.ui.MouseWheelScrollListenerimport edu.harvard.mcz.imagecapture.utility.OpenStreetMapUtilityimport java.util.Arraysimport org.hibernate.SessionExceptionimport javax.swing.JDialogimport edu.harvard.mcz.imagecapture.ui.dialog.UserDialogimport edu.harvard.mcz.imagecapture.ui.dialog.LoginDialogimport javax.swing.JPasswordFieldimport edu.harvard.mcz.imagecapture.utility.HashUtilityimport edu.harvard.mcz.imagecapture.ui.field.JIntegerFieldimport javax.swing.JFormattedTextFieldimport java.text.SimpleDateFormatimport java.math.BigDecimalimport javax.swing.ComboBoxModelimport org.jdesktop.swingx.combobox.ListComboBoxModelimport javax.swing.text.MaskFormatterimport java.awt.FlowLayoutimport edu.harvard.mcz.imagecapture.interfaces.DataChangeListenerimport edu.harvard.mcz.imagecapture.ui.tablemodel.VerbatimCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCountimport edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateExceptionimport edu.harvard.mcz.imagecapture.exceptions.UnreadableFileExceptionimport edu.harvard.mcz.imagecapture.ui.dialog.WhatsThisImageDialogimport edu.harvard.mcz.imagecapture.loader.Verbatimimport javax.swing.BoxLayoutimport edu.harvard.mcz.imagecapture.ui.tablemodel.CountValueTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.VerbatimClassifyDialogimport javax.swing.table.TableModelimport edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.GenusSpeciesCountTableModelimport edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCountimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttributeDialogimport com.jgoodies.forms.layout.FormLayoutimport com.jgoodies.forms.layout.ColumnSpecimport com.jgoodies.forms.layout.FormSpecsimport com.jgoodies.forms.layout.RowSpecimport edu.harvard.mcz.imagecapture.entity.fixed.Casteimport edu.harvard.mcz.imagecapture.entity.fixed.PartAssociationimport edu.harvard.mcz.imagecapture.entity.SpecimenPartAttributeimport edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsAttrTableModelimport edu.harvard.mcz.imagecapture.ui.dialog.SpecimenPartAttribEditDialogimport edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModelimport javax.swing.table.TableCellRendererimport edu.harvard.mcz.imagecapture.ui.DataShotColorsimport javax.swing.border.MatteBorderimport edu.harvard.mcz.imagecapture.entity.fixed.CountValueimport edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModelimport java.text.DateFormatimport edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModelimport java.lang.ArrayIndexOutOfBoundsExceptionimport javax.swing.event.ListDataListenerimport javax.swing.event.ListDataEventimport java.lang.ClassCastExceptionimport edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModelimport edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycleimport edu.harvard.mcz.imagecapture.entity.UnitTrayLabelimport edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycleimport edu.harvard.mcz.imagecapture.ui.tablemodel.UnitTrayLabelTableModelimport javax.swing.AbstractCellEditorimport javax.swing.table.TableCellEditorimport edu.harvard.mcz.imagecapture.exceptions.NoSuchRecordExceptionimport java.awt.event.MouseWheelListenerimport java.awt.event.MouseWheelEventimport javax.swing.JScrollBarimport edu.harvard.mcz.imagecapture.data .RunStatusimport org.hibernate.exception.JDBCConnectionExceptionimport java.lang.ExceptionInInitializerErrorimport java.util.Enumerationimport java.awt.Dialog.ModalityTypeimport javax.swing.InputVerifierimport edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterfaceimport edu.harvard.mcz.imagecapture.DefaultPositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.TaxonNameReturnerimport edu.harvard.mcz.imagecapture.UnitTrayLabelParserimport edu.harvard.mcz.imagecapture.interfaces.DrawerNameReturnerimport java.util.concurrent.atomic.AtomicIntegerimport edu.harvard.mcz.imagecapture.jobs.AbstractFileScanJobimport edu.harvard.mcz.imagecapture.exceptions.OCRReadExceptionimport org.apache.commons.codec.digest.DigestUtilsimport java.io.FileInputStreamimport edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsExceptionimport edu.harvard.mcz.imagecapture.interfaces.CollectionReturnerimport edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJobimport java.lang.Runtimeimport java.lang.Processimport java.io.BufferedReaderimport java.lang.InterruptedExceptionimport edu.harvard.mcz.imagecapture.utility.FileUtilityimport edu.harvard.mcz.imagecapture.jobs.AtomicCounterimport java.util.concurrent.ExecutorServiceimport java.util.concurrent.Executorsimport java.util.concurrent.locks.ReentrantLockimport java.util.concurrent.TimeUnitimport org.hibernate.HibernateExceptionimport edu.harvard.mcz.imagecapture.ConfiguredBarcodePositionTemplateDetectorimport edu.harvard.mcz.imagecapture.interfaces.ValueListerimport edu.harvard.mcz.imagecapture.entity.ExternalHistoryimport kotlin.jvm.JvmOverloadsimport edu.harvard.mcz.imagecapture.entity.AllowedVersionimport edu.harvard.mcz.imagecapture.loader.FieldLoaderimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetMovedOnExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetPopulatedExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetSaveExceptionimport edu.harvard.mcz.imagecapture.loader.ex.LoadTargetRecordNotFoundExceptionimport org.filteredpush.qc.date.EventResultimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.IllegalArgumentExceptionimport java.lang.reflect.InvocationTargetExceptionimport edu.harvard.mcz.imagecapture.loader.HeaderCheckResultimport edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycleimport java.io.FileReaderimport kotlin.jvm.JvmStaticimport edu.harvard.mcz.imagecapture.encoder.LabelEncoderimport java.util.Hashtableimport com.google.zxing.BarcodeFormatimport com.google.zxing.client.j2se.MatrixToImageWriterimport com.itextpdf.text.BadElementExceptionimport edu.harvard.mcz.imagecapture.exceptions.PrintFailedExceptionimport com.itextpdf.text.pdf.PdfWriterimport java.io.FileOutputStreamimport com.itextpdf.text.PageSizeimport com.itextpdf.text.pdf.PdfPTableimport com.itextpdf.text.pdf.PdfPCellimport com.itextpdf.text.BaseColorimport com.itextpdf.text.Paragraphimport java.lang.OutOfMemoryErrorimport javax.swing.undo.UndoManagerimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.UndoActionimport edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser.RedoActionimport javax.swing.table.TableRowSorterimport edu.harvard.mcz.imagecapture.utility.DragDropJTableimport javax.swing.DropModeimport javax.swing.event.UndoableEditListenerimport javax.swing.event.UndoableEditEventimport javax.swing.undo.CannotUndoExceptionimport javax.swing.undo.CannotRedoExceptionimport javax.swing.filechooser.FileNameExtensionFilterimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport edu.harvard.mcz.imagecapture.utility.DragDropJTable.DdTransferHandlerimport javax.swing.table.TableColumnModelimport javax.swing.ListSelectionModelimport java.awt.datatransfer.StringSelectionimport javax.swing.TransferHandlerimport javax.swing.TransferHandler.TransferSupportimport java.awt.datatransfer.DataFlavorimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.UnsupportedFlavorExceptionimport org.json.JSONObjectimport org.json.JSONExceptionimport edu.harvard.mcz.imagecapture.exceptions.NoSuchValueExceptionimport java.lang.RuntimeExceptionimport org.hibernate.LockModeimport javax.persistence.criteria.CriteriaBuilderimport javax.persistence.criteria.Rootimport org.hibernate.metadata.ClassMetadataimport edu.harvard.mcz.imagecapture.lifecycle.GenericLifeCycleimport javax.persistence.PersistenceExceptionimport org.hibernate.exception.ConstraintViolationExceptionimport org.hibernate.criterion.Exampleimport org.hibernate.criterion.Projectionsimport org.hibernate.criterion.Restrictionsimport edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycleimport edu.harvard.mcz.imagecapture.entity.HigherTaxonimport edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycleimport org.flywaydb.core.Flywayimport org.flywaydb.core.api.FlywayExceptionimport edu.harvard.mcz.imagecapture.ETHZBarcodeimport edu.harvard.mcz.imagecapture.interfaces.OCRimport edu.harvard.mcz.imagecapture.TesseractOCRimport edu.harvard.mcz.imagecapture.MCZENTBarcodeimport javax.swing.UIManagerimport javax.swing.UnsupportedLookAndFeelExceptionimport java.lang.ClassNotFoundExceptionimport edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditorimport java.util.regex.PatternSyntaxExceptionimport javax.swing.JToolBarimport edu.harvard.mcz.imagecapture.EditUserPanelimport edu.harvard.mcz.imagecapture.ui.tablemodel.UsersTableModelimport java.util.StringTokenizerimport edu.harvard.mcz.imagecapture.interfaces.PositionTemplateDetectorimport org.imgscalr.Scalrimport com.google.zxing.LuminanceSourceimport com.google.zxing.client.j2se.BufferedImageLuminanceSourceimport edu.harvard.mcz.imagecapture.CandidateImageFile.TextStatusimport com.google.zxing.BinaryBitmapimport com.google.zxing.common.HybridBinarizerimport java.awt.image.Kernelimport java.awt.image.ConvolveOpimport java.awt.image.AffineTransformOpimport java.awt.image.RescaleOpimport com.google.zxing.qrcode.QRCodeReaderimport com.google.zxing.DecodeHintTypeimport boofcv.struct.image.GrayU8import boofcv.io.image.ConvertBufferedImageimport boofcv.abst.fiducial.QrCodeDetectorimport boofcv.factory.fiducial.FactoryFiducialimport boofcv.alg.fiducial.qrcode.QrCodeimport georegression.struct.shapes.Polygon2D_F64import edu.harvard.mcz.imagecapture.ConvertTesseractOCRimport com.drew.imaging.jpeg.JpegMetadataReaderimport com.drew.metadata.exif.ExifSubIFDDirectoryimport com.drew.metadata.exif.ExifSubIFDDescriptorimport com.drew.imaging.jpeg.JpegProcessingExceptionimport com.drew.metadata.xmp.XmpDirectoryimport com.drew.imaging.ImageMetadataReaderimport com.drew.metadata.exif.ExifIFD0Directoryimport com.drew.metadata.jpeg.JpegDirectoryimport com.drew.metadata.MetadataExceptionimport com.drew.imaging.ImageProcessingExceptionimport com.google.zxing.ChecksumExceptionimport javax.imageio.ImageTypeSpecifierimport javax.imageio.ImageWriterimport edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawingimport edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
/**
 * EditUserPanel
 */
class EditUserPanel : JPanel() {
    private var user: Users? = null //  @jve:decl-index=0:
    private var jLabel: JLabel? = null
    private var jTextFieldUsername: JTextField? = null
    private var jLabel1: JLabel? = null
    private var jLabel2: JLabel? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jPasswordField: JPasswordField? = null
    private var jPasswordField1: JPasswordField? = null
    private var jTextFieldFullName: JTextField? = null
    private var jTextFieldAbout: JTextField? = null
    private var jLabel5: JLabel? = null
    private var jComboBox: JComboBox<*>? = null
    private var jButton: JButton? = null
    private var jButtonSetPassword: JButton? = null
    private fun setEditingState(enable: Boolean) {
        jTextFieldUsername.setEnabled(enable)
        jTextFieldFullName.setEnabled(enable)
        jTextFieldAbout.setEnabled(enable)
        jComboBox.setEnabled(enable)
        jComboBox.setEditable(false)
        jPasswordField.setEnabled(enable)
        jPasswordField1.setEnabled(enable)
        jButton.setEnabled(enable)
    }

    fun setUser(aUser: Users?) {
        setEditingState(true)
        if (aUser != null) {
            user = aUser
            jTextFieldUsername.setText(user.getUsername())
            jTextFieldFullName.setText(user.getFullname())
            jTextFieldAbout.setText(user.getDescription())
            jComboBox.setSelectedItem(user.getRole())
        }
        if (user.getUserid() == null) {
            jPasswordField.setVisible(true)
            jPasswordField1.setVisible(true)
            jPasswordField.setEnabled(true)
            jPasswordField1.setEnabled(true)
            jButtonSetPassword.setEnabled(false)
            jButtonSetPassword.setVisible(false)
            jLabel2.setVisible(true)
        } else {
            jPasswordField.setEnabled(false)
            jPasswordField1.setEnabled(false)
            jPasswordField.setVisible(false)
            jPasswordField1.setVisible(false)
            jButtonSetPassword.setEnabled(true)
            jButtonSetPassword.setVisible(true)
            jLabel2.setVisible(false)
        }
    }

    private fun save() {
        var okToSave = true
        var message = ""
        // Check required values
        if (jPasswordField.isEnabled()) { // Passwords must match
            if (LoginDialog.Companion.hashPassword(jPasswordField) == LoginDialog.Companion.hashPassword(jPasswordField1)) {
                user.setHash(LoginDialog.Companion.hashPassword(jPasswordField))
            } else {
                okToSave = false
                message = message + "Password and Password Again don't match.\n"
            }
            // Check for sufficiently complex password for new users.
            val pw = String(jPasswordField.getPassword())
            if (Users.Companion.testProposedPassword(pw, user.getUsername())) {
                user.setHash(HashUtility.getSHA1Hash(pw))
            } else {
                okToSave = false
                message = message + "Password is not sufficiently complex.  " + Users.Companion.PASSWORD_RULES_MESSAGE + " \n"
            }
        }
        // Don't check here yet for sufficiently complex password for
// existing users, as that would force a password change by the
// admin when changing other aspects of a user.
// This will become desirable, but not yet.
// Are required fields populated?
        if (jTextFieldUsername.getText() == "") {
            okToSave = false
            message = message + "An email is required.\n"
        }
        if (jTextFieldFullName.getText() == "") {
            okToSave = false
            message = message + "A full name is required.\n"
        }
        if (!okToSave) { // warn
            message = "Error. Can't Save.\n$message"
            JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(), message, "Error. Can't save.", JOptionPane.OK_OPTION)
        } else { // Save
            val uls = UsersLifeCycle()
            user.setUsername(jTextFieldUsername.getText())
            user.setFullname(jTextFieldFullName.getText())
            user.setDescription(jTextFieldAbout.getText())
            if (jComboBox.getSelectedIndex() == -1 && jComboBox.getSelectedItem() == null) {
                user.setRole(Users.Companion.ROLE_DATAENTRY)
            } else {
                user.setRole(jComboBox.getSelectedItem().toString())
            }
            try {
                if (user.getUserid() == null) {
                    uls.persist(user)
                } else {
                    uls.attachDirty(user)
                }
                message = "Saved " + user.getFullname()
                Singleton.Companion.getSingletonInstance().getMainFrame().setStatusMessage(message)
            } catch (e: SaveFailedException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() {
        val gridBagConstraints13 = GridBagConstraints()
        gridBagConstraints13.gridx = 1
        gridBagConstraints13.gridy = 3
        val gridBagConstraints12 = GridBagConstraints()
        gridBagConstraints12.gridx = 0
        gridBagConstraints12.gridwidth = 2
        gridBagConstraints12.weightx = 0.0
        gridBagConstraints12.anchor = GridBagConstraints.NORTH
        gridBagConstraints12.weighty = 1.0
        gridBagConstraints12.gridy = 7
        val gridBagConstraints11 = GridBagConstraints()
        gridBagConstraints11.fill = GridBagConstraints.BOTH
        gridBagConstraints11.gridy = 6
        gridBagConstraints11.weightx = 1.0
        gridBagConstraints11.gridx = 1
        val gridBagConstraints10 = GridBagConstraints()
        gridBagConstraints10.gridx = 0
        gridBagConstraints10.anchor = GridBagConstraints.EAST
        gridBagConstraints10.gridy = 6
        jLabel5 = JLabel()
        jLabel5.setText("Role")
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
        gridBagConstraints7.fill = GridBagConstraints.BOTH
        gridBagConstraints7.gridy = 2
        gridBagConstraints7.weightx = 1.0
        gridBagConstraints7.gridx = 1
        val gridBagConstraints6 = GridBagConstraints()
        gridBagConstraints6.fill = GridBagConstraints.BOTH
        gridBagConstraints6.gridy = 1
        gridBagConstraints6.weightx = 1.0
        gridBagConstraints6.gridx = 1
        val gridBagConstraints5 = GridBagConstraints()
        gridBagConstraints5.gridx = 0
        gridBagConstraints5.anchor = GridBagConstraints.EAST
        gridBagConstraints5.gridy = 5
        jLabel4 = JLabel()
        jLabel4.setText("About")
        val gridBagConstraints4 = GridBagConstraints()
        gridBagConstraints4.gridx = 0
        gridBagConstraints4.anchor = GridBagConstraints.EAST
        gridBagConstraints4.gridy = 4
        jLabel3 = JLabel()
        jLabel3.setText("Full Name")
        val gridBagConstraints3 = GridBagConstraints()
        gridBagConstraints3.gridx = 0
        gridBagConstraints3.anchor = GridBagConstraints.EAST
        gridBagConstraints3.gridy = 2
        jLabel2 = JLabel()
        jLabel2.setText("Password Again")
        val gridBagConstraints2 = GridBagConstraints()
        gridBagConstraints2.gridx = 0
        gridBagConstraints2.anchor = GridBagConstraints.EAST
        gridBagConstraints2.gridy = 1
        jLabel1 = JLabel()
        jLabel1.setText("Password")
        val gridBagConstraints1 = GridBagConstraints()
        gridBagConstraints1.fill = GridBagConstraints.BOTH
        gridBagConstraints1.gridy = 0
        gridBagConstraints1.weightx = 1.0
        gridBagConstraints1.gridx = 1
        val gridBagConstraints = GridBagConstraints()
        gridBagConstraints.gridx = 0
        gridBagConstraints.anchor = GridBagConstraints.EAST
        gridBagConstraints.gridy = 0
        jLabel = JLabel()
        jLabel.setText("email")
        this.setSize(300, 200)
        this.setLayout(GridBagLayout())
        this.add(jLabel, gridBagConstraints)
        this.add(getJTextFieldUsername(), gridBagConstraints1)
        this.add(jLabel1, gridBagConstraints2)
        this.add(jLabel2, gridBagConstraints3)
        this.add(jLabel3, gridBagConstraints4)
        this.add(jLabel4, gridBagConstraints5)
        this.add(getJPasswordField(), gridBagConstraints6)
        this.add(getJPasswordField1(), gridBagConstraints7)
        this.add(getJTextFieldFullName(), gridBagConstraints8)
        this.add(getJTextFieldAbout(), gridBagConstraints9)
        this.add(jLabel5, gridBagConstraints10)
        this.add(roleJComboBox, gridBagConstraints11)
        this.add(saveJButton, gridBagConstraints12)
        this.add(passwordChangeJButton, gridBagConstraints13)
    }

    /**
     * This method initializes jTextFieldUsername
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldUsername(): JTextField? {
        if (jTextFieldUsername == null) {
            jTextFieldUsername = JTextField()
        }
        return jTextFieldUsername
    }

    /**
     * This method initializes jPasswordField
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField(): JPasswordField? {
        if (jPasswordField == null) {
            jPasswordField = JPasswordField()
        }
        return jPasswordField
    }

    /**
     * This method initializes jPasswordField1
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField1(): JPasswordField? {
        if (jPasswordField1 == null) {
            jPasswordField1 = JPasswordField()
        }
        return jPasswordField1
    }

    /**
     * This method initializes jTextFieldFullName
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldFullName(): JTextField? {
        if (jTextFieldFullName == null) {
            jTextFieldFullName = JTextField()
        }
        return jTextFieldFullName
    }

    /**
     * This method initializes jTextFieldAbout
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldAbout(): JTextField? {
        if (jTextFieldAbout == null) {
            jTextFieldAbout = JTextField()
        }
        return jTextFieldAbout
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private val roleJComboBox: JComboBox<*>?
        private get() {
            if (jComboBox == null) {
                jComboBox = JComboBox<Any?>(Users.Companion.ROLES)
            }
            return jComboBox
        }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private val saveJButton: JButton?
        private get() {
            if (jButton == null) {
                jButton = JButton()
                jButton.setEnabled(false)
                jButton.setText("Save")
                jButton.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        save()
                    }
                })
            }
            return jButton
        }// user provided some input.

    /**
     * This method initializes jButtonSetPassword
     *
     * @return javax.swing.JButton
     */
    private val passwordChangeJButton: JButton?
        private get() {
            if (jButtonSetPassword == null) {
                jButtonSetPassword = JButton()
                jButtonSetPassword.setEnabled(false)
                jButtonSetPassword.setText("Set New Password")
                jButtonSetPassword.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        var suggestion: String = user.getUsername() + user.getFullname() + Math.random()
                        suggestion = HashUtility.getSHA1Hash(suggestion)
                        suggestion.substring(1, 15)
                        val pw = (JOptionPane.showInputDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                "Set a new password for " + user.getFullname() + " (at least 10 characters)",
                                "Change password for " + user.getUsername(),
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null,
                                "") as String)
                        if (pw != null && !pw.isEmpty()) { // user provided some input.
                            if (Users.Companion.testProposedPassword(pw, user.getUsername())) {
                                user.setHash(HashUtility.getSHA1Hash(pw))
                            } else {
                                JOptionPane.showMessageDialog(Singleton.Companion.getSingletonInstance().getMainFrame(),
                                        "Password is not complex enough" + Users.Companion.PASSWORD_RULES_MESSAGE, "Password Not Changed.",
                                        JOptionPane.ERROR_MESSAGE)
                            }
                        }
                    }
                })
            }
            return jButtonSetPassword
        }

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * This is the default constructor
     */
    init {
        initialize()
        setEditingState(false)
    }
}