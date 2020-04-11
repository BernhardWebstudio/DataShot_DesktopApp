/**
 * MainFrame.java
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
package edu.harvard.mcz.imagecapture.ui.frame


import edu.harvard.mcz.imagecapture.*
import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.data.LocationInCollection
import edu.harvard.mcz.imagecapture.encoder.UnitTrayLabelBrowser
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.*
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.loader.JobVerbatimFieldLoad
import edu.harvard.mcz.imagecapture.ui.dialog.*
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.io.File
import java.net.URL
import java.util.*
import javax.swing.*
import javax.swing.text.DefaultEditorKit.CopyAction
import javax.swing.text.DefaultEditorKit.PasteAction

/**
 * Main user interface window for image capture application when run as a java application from the desktop.
 */
class MainFrame : JFrame(), RunnerListener {
    private val thisMainFrame: MainFrame?
    private var state = STATE_INIT
    private var ilb: ImageListBrowser? = null
    private var slb: SpecimenBrowser? = null
    private var ulb: UserListBrowser? = null
    //private JPanel listBrowser = null;
    private var jJMenuBar: JMenuBar? = null
    private var jMenuFile: JMenu? = null
    private var jMenuItemExit: JMenuItem? = null
    private var jMenuAction: JMenu? = null
    private var jMenuHelp: JMenu? = null
    private var jMenuItemAbout: JMenuItem? = null
    private var jMenuItemPreprocess: JMenuItem? = null
    private var jMenuItemDelete: JMenuItem? = null
    private var jMenuItemFindMissingImages: JMenuItem? = null
    private var jMenuItemLoadData: JMenuItem? = null
    private var jMenuItemVersion: JMenuItem? = null
    private var jMenuItemScanOneBarcode: JMenuItem? = null
    private var jMenuItemScanOneBarcodeSave: JMenuItem? = null
    private var jMenuConfig: JMenu? = null
    private var jMenuEdit: JMenu? = null
    private var jMenuItemPreferences: JMenuItem? = null
    private var jMenuItemCopy: JMenuItem? = null
    private var jMenuItemPaste: JMenuItem? = null
    private var jMenuItemLog: JMenuItem? = null
    private var jPanel: JPanel? = null
    private var jProgressBar: JProgressBar? = null
    private var jPanel1: JPanel? = null
    private var jLabelStatus: JLabel? = null
    private var jMenuItemVerbatimTranscription: JMenuItem? = null
    private var jMenuItemVerbatimClassification: JMenuItem? = null
    private var jMenuItemBrowseSpecimens: JMenuItem? = null
    private var jPanelCenter: JPanel? = null
    private var jMenuItemEditTemplates: JMenuItem? = null
    private var jMenuItemBrowseImages: JMenuItem? = null
    private var jLabelCount: JLabel? = null
    private var jMenuItemPreprocessOneDir: JMenuItem? = null
    private var jMenuData: JMenu? = null
    private var jMenuQualityControl: JMenu? = null
    private var jMenuItemCheckForABarcode: JMenuItem? = null
    private var jMenuItemQCBarcodes: JMenuItem? = null
    private var jMenuItemSearch: JMenuItem? = null
    private var jMenuItemUsers: JMenuItem? = null
    private var jMenuItemLogout: JMenuItem? = null
    private var jMenuItemChangePassword: JMenuItem? = null
    private var jMenuItemCreateLabels: JMenuItem? = null
    private var jMenuItemReconcileFiles: JMenuItem? = null
    private var jMenuItemStats: JMenuItem? = null
    private var jMenuItemRepeatOCR: JMenuItem? = null
    private var jMenuItemListRunningJobs: JMenuItem? = null
    private var jMenuItemRedoOCROne: JMenuItem? = null
    private var jMenuItemCleanupDirectory: JMenuItem? = null
    private var jMenuItemRecheckTemplates: JMenuItem? = null
    private var jMenuItemRecheckAllTemplates: JMenuItem? = null
    override fun setState(aState: Int) {
        when (aState) {
            STATE_INIT ->  // can't return to state_init.
                if (state == STATE_INIT) { // do initial setup, disable most menus
                    jMenuItemLog.setEnabled(false)
                    jMenuEdit.setEnabled(false)
                    jMenuAction.setEnabled(false)
                    jMenuConfig.setEnabled(false)
                    jMenuData.setEnabled(false)
                    jMenuQualityControl.setEnabled(false)
                    jMenuItemUsers.setEnabled(false)
                    jMenuItemChangePassword.setEnabled(false)
                    jMenuItemStats.setEnabled(false)
                }
            STATE_RESET -> {
                // state when user logs out.
                jMenuEdit.setEnabled(true)
                jMenuHelp.setEnabled(true)
                jMenuItemLogout.setEnabled(true)
                // disable all but edit/help menus
                jMenuItemLog.setEnabled(false)
                jMenuAction.setEnabled(false)
                jMenuConfig.setEnabled(false)
                jMenuData.setEnabled(false)
                jMenuQualityControl.setEnabled(false)
                jMenuItemUsers.setEnabled(false)
                jMenuItemChangePassword.setEnabled(false)
                // disable stats item on help menu
                jMenuItemStats.setEnabled(false)
            }
            STATE_RUNNING -> {
                if (state == STATE_INIT) {
                    state = STATE_RUNNING
                    jMenuItemLog.setEnabled(true)
                    jMenuEdit.setEnabled(true)
                    activateMenuItemsByUser()
                }
                if (state == STATE_RUNNING) {
                    activateMenuItemsByUser()
                }
            }
        }
    }

    /**
     * Enable/disable menu items based on current user
     * and their login state.
     */
    private fun activateMenuItemsByUser() { // ***********************************************************************************************
// ********* Important bit: This is where user rights are actually applied.     ******************
// ********* Other than limits on editing/creating users, this only place where ******************
// ********* the application level users and rights are controlled.             ******************
// ***********************************************************************************************
// Disable some menu items if user canceled login dialog.
        if (Singleton.User == null) {
            jMenuData.setEnabled(false)
            jMenuItemChangePassword.setEnabled(false)
            jMenuItemPreferences.setEnabled(false)
            jMenuItemPreprocess.setEnabled(false)
            jMenuItemLoadData.setEnabled(false)
            jMenuItemPreprocessOneDir.setEnabled(false)
            jMenuItemCreateLabels.setEnabled(false)
            jMenuItemStats.setEnabled(false)
            jMenuItemLog.setEnabled(false)
        } else { // Anyone authenticated user can change their own password.
            jMenuConfig.setEnabled(true)
            jMenuItemChangePassword.setEnabled(true)
            // Set levels for data entry personnel.
            jMenuData.setEnabled(true)
            jMenuAction.setEnabled(false)
            jMenuItemUsers.setEnabled(false)
            jMenuItemPreprocess.setEnabled(false)
            jMenuItemDelete.setEnabled(false)
            jMenuItemLoadData.setEnabled(false)
            jMenuItemPreprocessOneDir.setEnabled(false)
            jMenuItemCreateLabels.setEnabled(true)
            jMenuItemPreferences.setEnabled(false)
            jMenuItemEditTemplates.setEnabled(false)
            jMenuQualityControl.setEnabled(true)
            jMenuItemCheckForABarcode.setEnabled(true)
            jMenuItemQCBarcodes.setEnabled(false)
            jMenuItemSearch.setEnabled(true)
            jMenuItemStats.setEnabled(true)
            jMenuItemLog.setEnabled(true)
            jMenuItemCleanupDirectory.setEnabled(false)
            jMenuItemRecheckTemplates.setEnabled(false)
            jMenuItemRecheckAllTemplates.setEnabled(false)
            try { // Enable some menu items only for administrators.
                if (UsersLifeCycle.Companion.isUserAdministrator(Singleton.user.userid)) { //jMenuItemUsers.setEnabled(true);
                    jMenuItemPreprocess.setEnabled(true)
                }
                // User privileges and some other items to the chief editor.
                if (UsersLifeCycle.Companion.isUserChiefEditor(Singleton.User.Userid)) {
                    jMenuItemUsers.setEnabled(true)
                    jMenuItemEditTemplates.setEnabled(true)
                    jMenuItemLoadData.setEnabled(true)
                    jMenuItemCleanupDirectory.setEnabled(true)
                }
                // Enable other menu items only for those with full access rights
// Administrator and full roles both have full access rights
                if (Singleton.User.isUserRole(Users.Companion.ROLE_FULL)) {
                    jMenuAction.setEnabled(true)
                    jMenuItemPreprocessOneDir.setEnabled(true)
                    jMenuConfig.setEnabled(true)
                    jMenuItemDelete.setEnabled(true)
                    jMenuItemPreferences.setEnabled(true)
                    jMenuQualityControl.setEnabled(true)
                    jMenuItemQCBarcodes.setEnabled(true)
                    jMenuItemRecheckTemplates.setEnabled(true)
                    jMenuItemRecheckAllTemplates.setEnabled(true)
                }
            } catch (e: Exception) { // catch any problem with testing administration or user rights and do nothing.
            }
        }
    }

    /**
     * This method initializes the main frame.
     */
    private fun initialize() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        this.setSize(Dimension(1280, 750))
        this.setPreferredSize(Dimension(1280, 800))
        this.setMinimumSize(Dimension(300, 200))
        val screenSize: Dimension = Toolkit.DefaultToolkit.ScreenSize
        this.setLocation((screenSize.width - this.Width) / 2,
                (screenSize.height - this.Height) / 2)
        //String iconFile = this.Class.getResource("resources/icon.ico").File;
        val iconFile: URL? = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/icon.png")
        //loading an image from a file
        val defaultToolkit: Toolkit = Toolkit.DefaultToolkit
        val image: Image = defaultToolkit.getImage(iconFile)
        //this is new since JDK 9
        val taskbar: Taskbar = Taskbar.Taskbar
        try { //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(image)
        } catch (e: UnsupportedOperationException) {
            println("The os does not support: 'taskbar.setIconImage'")
        } catch (e: SecurityException) {
            println("There was a security exception for: 'taskbar.setIconImage'")
        }
        //set icon for windows os (and other systems which do support this method)
        this.setIconImage(image)
        try {
            setIconImage(ImageIcon(iconFile).Image)
        } catch (e: Exception) {
            log.error("Can't open icon file: $iconFile")
            log.error(e)
        }
        this.setTitle(ImageCaptureApp.APP_NAME + ": MCZ Rapid Data Capture Application.  Configured For: " +
                Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
        )
        this.setJMenuBar(getJJMenuBar())
        this.setContentPane(getJPanel())
    }

    protected fun updateTitle() {
        this.setTitle(ImageCaptureApp.APP_NAME + ": MCZ Rapid Data Capture Application.  Configured For: " +
                Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION)
        )
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private fun getJJMenuBar(): JMenuBar? {
        if (jJMenuBar == null) {
            jJMenuBar = JMenuBar()
            jJMenuBar.add(getJMenuFile())
            jJMenuBar.add(getJMenuEdit())
            jJMenuBar.add(getJMenuAction())
            jJMenuBar.add(getJMenuData())
            jJMenuBar.add(getJMenuQualityControl())
            jJMenuBar.add(getJMenuConfig())
            jJMenuBar.add(getJMenuHelp())
        }
        return jJMenuBar
    }

    /**
     * This method initializes jMenuFile
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuFile(): JMenu? {
        if (jMenuFile == null) {
            jMenuFile = JMenu()
            jMenuFile.setText("File")
            jMenuFile.setMnemonic(KeyEvent.VK_F)
            jMenuFile.add(getJMenuItemLog())
            jMenuFile.add(getJMenuItemLogout())
            jMenuFile.add(getJMenuItemExit())
        }
        return jMenuFile
    }

    /**
     * This method initializes jMenuItemExit
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemExit(): JMenuItem? {
        if (jMenuItemExit == null) {
            jMenuItemExit = JMenuItem()
            jMenuItemExit.setText("Exit")
            jMenuItemExit.setMnemonic(KeyEvent.VK_E)
            jMenuItemExit.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    ImageCaptureApp.cleanUp()
                    println("Exit by user from main menu.")
                    ImageCaptureApp.exit(ImageCaptureApp.EXIT_NORMAL)
                }
            })
        }
        return jMenuItemExit
    }

    /**
     * This method initializes jMenuItemLogout
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemLogout(): JMenuItem? {
        if (jMenuItemLogout == null) {
            jMenuItemLogout = JMenuItem()
            jMenuItemLogout.setText("Logout & change user")
            jMenuItemLogout.setMnemonic(KeyEvent.VK_U)
            jMenuItemLogout.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // remove the current user's browse (important if this is a userbrowse by an
// administrator.
                    jPanelCenter.removeAll()
                    var oldUser = "anon"
                    try {
                        oldUser = Singleton.UserFullName
                    } catch (ex: NullPointerException) { // no one was logged in
                    }
                    setState(STATE_RESET)
                    Singleton.unsetCurrentUser()
                    HibernateUtil.terminateSessionFactory()
                    Singleton.MainFrame.setStatusMessage("Logged out $oldUser")
                    // Force a login dialog by connecting to obtain record count;
                    val sls = SpecimenLifeCycle()
                    try {
                        setCount(sls.findSpecimenCountThrows())
                        ImageCaptureApp.doStartUp()
                    } catch (e1: ConnectionException) {
                        log.error(e1.message, e1)
                        ImageCaptureApp.doStartUpNot()
                    }
                }
            })
        }
        return jMenuItemLogout
    }

    /**
     * This method initializes jMenuAction
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuAction(): JMenu? {
        if (jMenuAction == null) {
            jMenuAction = JMenu()
            jMenuAction.setText("Action")
            jMenuAction.setMnemonic(KeyEvent.VK_A)
            jMenuAction.add(getJMenuItemScanOneBarcodeSave())
            jMenuAction.add(getJMenuItemPreprocess())
            jMenuAction.add(jMenuItemPreprocessOne)
            jMenuAction.add(getJMenuItemDelete())
            jMenuAction.add(getJMenuItemFindMissingImages())
            jMenuAction.add(getJMenuItemRedoOCROne())
            jMenuAction.add(getJMenuItemRepeatOCR())
            jMenuAction.add(getJMenuItemRecheckTemplates())
            jMenuAction.add(getJMenuItemRecheckAllTemplates())
            jMenuAction.add(getJMenuItemScanOneBarcode())
            jMenuAction.add(getJMenuItemCleanupDirectory())
            jMenuAction.add(getJMenuItemLoadData())
            jMenuAction.add(getJMenuItemListRunningJobs())
            jMenuAction.add(getJMenuItemCreateLabels())
        }
        return jMenuAction
    }

    /**
     * This method initializes jMenuHelp
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuHelp(): JMenu? {
        if (jMenuHelp == null) {
            jMenuHelp = JMenu()
            jMenuHelp.setText("Help")
            jMenuHelp.setMnemonic(KeyEvent.VK_H)
            jMenuHelp.add(getJMenuItemAbout())
            jMenuHelp.addSeparator()
            jMenuHelp.add(jMenuItem)
            jMenuHelp.add(getJMenuItemVersion())
        }
        return jMenuHelp
    }

    /**
     * This method initializes jMenuItemAbout
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemAbout(): JMenuItem? {
        if (jMenuItemAbout == null) {
            jMenuItemAbout = JMenuItem()
            jMenuItemAbout.setText("About")
            jMenuItemAbout.setMnemonic(KeyEvent.VK_B)
            jMenuItemAbout.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val a = AboutDialog()
                    a.pack()
                    a.setVisible(true)
                }
            })
        }
        return jMenuItemAbout
    }

    /**
     * This method initializes jMenuItemPreprocess: the menu item to start preprocessing all
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPreprocess(): JMenuItem? {
        if (jMenuItemPreprocess == null) {
            jMenuItemPreprocess = JMenuItem()
            jMenuItemPreprocess.setText("Preprocess All")
            jMenuItemPreprocess.setEnabled(true)
            try {
                jMenuItemPreprocess.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemPreprocess.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val result: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame, "Are you sure, this will check all image files and may take some time.", "Preprocess All?", JOptionPane.YES_NO_OPTION)
                    if (result == JOptionPane.YES_OPTION) {
                        val scan = JobAllImageFilesScan()
                        Thread(scan).start()
                    } else {
                        Singleton.MainFrame.setStatusMessage("Preprocess canceled.")
                    }
                }
            })
        }
        return jMenuItemPreprocess
    }

    /**
     * This method initializes the jMenuItem to delete a specimen record
     *
     * @return
     */
    private fun getJMenuItemDelete(): JMenuItem? {
        if (jMenuItemDelete == null) {
            jMenuItemDelete = JMenuItem()
            jMenuItemDelete.setText("Delete a specimen record")
            jMenuItemDelete.setEnabled(true)
            try {
                jMenuItemDelete.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/red-warning-icon.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemDelete.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val barcode = JTextField()
                    val inputs: Array<JComponent?> = arrayOf<JComponent?>(
                            JLabel("<html>WARNING: ACTION IRREVERSIBLE!<br/><br/>Is there only one image associated with this record?<br/>Have you recorded the image file number/s and date imaged?<br/>Have you recorded the genus and species name?<br/><br/>Please enter the barcode of the specimen record you would like to delete:<br/>"),
                            barcode
                    )
                    val result: Int = JOptionPane.showConfirmDialog(null, inputs, "Delete a specimen record", JOptionPane.CANCEL_OPTION)
                    if (result == JOptionPane.YES_OPTION) {
                        val barcodeEntered: String = barcode.Text
                        val sls = SpecimenLifeCycle()
                        val delete_result: Int = sls.deleteSpecimenByBarcode(barcodeEntered)
                        if (delete_result == 0) {
                            JOptionPane.showConfirmDialog(null, "Error: specimen record not found.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        } else if (delete_result == 1) {
                            JOptionPane.showConfirmDialog(null, "Specimen has been deleted successfully.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        } else {
                            JOptionPane.showConfirmDialog(null, "Error: delete failed.", "Delete specimen record", JOptionPane.PLAIN_MESSAGE)
                        }
                    }
                }
            })
        }
        return jMenuItemDelete
    }

    /**
     * This method initializes the jMenuItem to load a verbatim field/data
     *
     * @return
     */
    private fun getJMenuItemLoadData(): JMenuItem? {
        if (jMenuItemLoadData == null) {
            jMenuItemLoadData = JMenuItem()
            jMenuItemLoadData.setText("Load Data")
            try {
                jMenuItemLoadData.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/cycle_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckAllTemplates.")
                log.error(e)
            }
            jMenuItemLoadData.setEnabled(true)
            jMenuItemLoadData.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //TODO: Launch FieldLoaderWizard and have it launch job.
                    val scan = JobVerbatimFieldLoad()
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemLoadData
    }

    /**
     * This method initializes jMenuItemVersion
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemVersion(): JMenuItem? {
        if (jMenuItemVersion == null) {
            jMenuItemVersion = JMenuItem(ImageCaptureApp.APP_NAME + " Ver: " + ImageCaptureApp.AppVersion)
            jMenuItemVersion.setEnabled(false)
        }
        return jMenuItemVersion
    }

    /**
     * This method initializes jMenuItemScanOneBarcode
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemScanOneBarcode(): JMenuItem? {
        if (jMenuItemScanOneBarcode == null) {
            jMenuItemScanOneBarcode = JMenuItem()
            jMenuItemScanOneBarcode.setText("Scan A File For Barcodes")
            jMenuItemScanOneBarcode.setMnemonic(KeyEvent.VK_S)
            try {
                jMenuItemScanOneBarcode.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                println("Can't open icon file for jMenuItemScanOneBarcode.")
                println(e.localizedMessage)
            }
            jMenuItemScanOneBarcode.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val scan = JobSingleBarcodeScan(false)
                    scan.registerListener(thisMainFrame)
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemScanOneBarcode
    }

    /**
     * This method initializes jMenuItemScanOneBarcodeSave
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemScanOneBarcodeSave(): JMenuItem? {
        if (jMenuItemScanOneBarcodeSave == null) {
            jMenuItemScanOneBarcodeSave = JMenuItem()
            jMenuItemScanOneBarcodeSave.setMnemonic(KeyEvent.VK_D)
            jMenuItemScanOneBarcodeSave.setText("Database One File")
            try {
                jMenuItemScanOneBarcodeSave.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemScanOneBarcode.")
                log.error(e.localizedMessage)
            }
            jMenuItemScanOneBarcodeSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val scan = JobSingleBarcodeScan(true)
                    Thread(scan).start()
                }
            })
        }
        return jMenuItemScanOneBarcodeSave
    }

    /**
     * This method initializes jMenuConfig
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuConfig(): JMenu? {
        if (jMenuConfig == null) {
            jMenuConfig = JMenu()
            jMenuConfig.setText("Configuration")
            jMenuConfig.setMnemonic(KeyEvent.VK_C)
            jMenuConfig.setEnabled(true)
            jMenuConfig.add(getJMenuItemEditTemplates())
            jMenuConfig.add(getJMenuItemPreferences())
            jMenuConfig.add(getJMenuItemUsers())
            jMenuConfig.add(getJMenuItemChangePassword())
        }
        return jMenuConfig
    }

    /**
     * This method initializes the menu to access the users overview
     *
     * @return
     */
    private fun getJMenuItemUsers(): JMenuItem? {
        if (jMenuItemUsers == null) {
            jMenuItemUsers = JMenuItem()
            jMenuItemUsers.setText("Users")
            jMenuItemUsers.setMnemonic(KeyEvent.VK_U)
            jMenuItemUsers.setEnabled(false)
            try {
                jMenuItemUsers.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/people_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemUsers.")
                log.error(e)
            }
            jMenuItemUsers.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    ulb = UserListBrowser()
                    if (slb != null) {
                        jPanelCenter.remove(slb)
                    }
                    if (ilb != null) {
                        jPanelCenter.remove(ilb)
                    }
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ulb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                }
            })
        }
        return jMenuItemUsers
    }

    /**
     * This method initializes jMenuEdit
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuEdit(): JMenu? {
        if (jMenuEdit == null) {
            jMenuEdit = JMenu()
            jMenuEdit.setText("Edit")
            jMenuEdit.setMnemonic(KeyEvent.VK_E)
            jMenuEdit.add(getJMenuItemCopy())
            jMenuEdit.add(getJMenuItemPaste())
        }
        return jMenuEdit
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPreferences(): JMenuItem? {
        if (jMenuItemPreferences == null) {
            jMenuItemPreferences = JMenuItem()
            jMenuItemPreferences.setText("Preferences")
            jMenuItemPreferences.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val p = PropertiesEditor()
                    p.pack()
                    p.setVisible(true)
                }
            })
        }
        return jMenuItemPreferences
    }

    /**
     * This method initializes jMenuItem1
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemCopy(): JMenuItem? {
        if (jMenuItemCopy == null) {
            jMenuItemCopy = JMenuItem(CopyAction())
            jMenuItemCopy.setText("Copy")
            jMenuItemCopy.setMnemonic(KeyEvent.VK_C)
            jMenuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_C, ActionEvent.CTRL_MASK))
            jMenuItemCopy.setEnabled(true)
        }
        return jMenuItemCopy
    }

    /**
     * This method initializes jMenuItem2
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemPaste(): JMenuItem? {
        if (jMenuItemPaste == null) {
            jMenuItemPaste = JMenuItem(PasteAction())
            jMenuItemPaste.setText("Paste")
            jMenuItemPaste.setMnemonic(KeyEvent.VK_P)
            jMenuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_V, ActionEvent.CTRL_MASK))
            jMenuItemPaste.setEnabled(true)
        }
        return jMenuItemPaste
    }

    /**
     * This method initializes jMenuItemLog
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemLog(): JMenuItem? {
        if (jMenuItemLog == null) {
            jMenuItemLog = JMenuItem()
            jMenuItemLog.setText("View History")
            jMenuItemLog.setMnemonic(KeyEvent.VK_H)
            jMenuItemLog.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val logWindow = EventLogFrame()
                    logWindow.pack()
                    logWindow.setVisible(true)
                    System.gc()
                }
            })
        }
        return jMenuItemLog
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            jPanel = JPanel()
            jPanel.setLayout(BorderLayout())
            jPanel.add(getJPanel1(), BorderLayout.SOUTH)
            jPanel.add(getJPanelCenter(), BorderLayout.CENTER)
        }
        return jPanel
    }

    /**
     * This method initializes jProgressBar
     *
     * @return javax.swing.JProgressBar
     */
    private fun getJProgressBar(): JProgressBar? {
        if (jProgressBar == null) {
            jProgressBar = JProgressBar()
        }
        return jProgressBar
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints1.weightx = 4.0
            gridBagConstraints1.gridx = 0
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 1
            gridBagConstraints.anchor = GridBagConstraints.EAST
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.gridy = 0
            jLabelStatus = JLabel()
            jLabelStatus.setText("Status:")
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            jPanel1.add(jLabelStatus, gridBagConstraints1)
            jPanel1.add(getJProgressBar(), gridBagConstraints)
        }
        return jPanel1
    }

    override fun notifyListener(anEvent: Int, notifyingJob: RunnableJob) {
        jProgressBar.setValue(notifyingJob.percentComplete())
        log.debug(notifyingJob.percentComplete())
        jProgressBar.validate()
    }

    /**
     * Sets the message on the status bar with an up to 60 character string.
     *
     * @param aMessage the message to display on the status bar.
     */
    fun setStatusMessage(aMessage: String) {
        var maxLength = 100
        if (aMessage.length < maxLength) {
            maxLength = aMessage.length
        }
        jLabelStatus.setText("Status: " + aMessage.substring(0, maxLength))
    }

    fun setSpecimenBrowseList(searchCriteria: Specimen?, limit: Int, offset: Int) {
        Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        slb = SpecimenBrowser(searchCriteria, true, limit, offset)
        adaptJpanelToSpecimenBrowser(slb)
        Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
    }

    fun adaptJpanelToSpecimenBrowser(slb: SpecimenBrowser) {
        jPanelCenter.removeAll()
        jPanelCenter.add(slb, BorderLayout.CENTER)
        jPanelCenter.revalidate()
        jPanelCenter.repaint()
        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseSpecimens.setEnabled(false)
            jMenuItemBrowseImages.setEnabled(false)
        } else {
            jMenuItemBrowseSpecimens.setEnabled(true)
            jMenuItemBrowseImages.setEnabled(true)
        }
        setStatusMessage("Found " + slb.RowCount + " matching specimens")
    }

    fun setSpecimenBrowseList(searchCriteria: Specimen?) {
        this.setSpecimenBrowseList(searchCriteria, 0, 0)
    }

    /**
     * This method initializes jMenuItemBrowseSpecimens
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemBrowseSpecimens(): JMenuItem? {
        if (jMenuItemBrowseSpecimens == null) {
            jMenuItemBrowseSpecimens = JMenuItem()
            jMenuItemBrowseSpecimens.setText("Browse Specimens")
            jMenuItemBrowseSpecimens.setMnemonic(KeyEvent.VK_B)
            try {
                jMenuItemBrowseSpecimens.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/butterfly_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemBrowseImages.")
                log.error(e)
            }
            jMenuItemBrowseSpecimens.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // Create a SpecimenBrowser jpanel and replace the
// the content of the center of jPanelCenter with it.
//TODO: extend beyond switching between ilb and slb.
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    slb = SpecimenBrowser()
                    adaptJpanelToSpecimenBrowser(slb)
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    System.gc()
                }
            })
        }
        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseSpecimens.setEnabled(false)
        }
        return jMenuItemBrowseSpecimens
    }

    /**
     * This method initializes jPanelCenter
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanelCenter(): JPanel? {
        if (jPanelCenter == null) {
            jLabelCount = JLabel()
            jLabelCount.setText("")
            jPanelCenter = JPanel()
            jPanelCenter.setLayout(BorderLayout())
            jPanelCenter.add(jLabelCount, BorderLayout.SOUTH)
        }
        return jPanelCenter
    }

    fun setCount(recordCountText: String?) {
        if (jLabelCount != null) {
            jLabelCount.setText(recordCountText)
        }
    }

    /**
     * This method initializes jMenuItem3
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemEditTemplates(): JMenuItem? {
        if (jMenuItemEditTemplates == null) {
            jMenuItemEditTemplates = JMenuItem()
            jMenuItemEditTemplates.setText("Edit Templates")
            jMenuItemEditTemplates.setMnemonic(KeyEvent.VK_T)
            jMenuItemEditTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val templateEditor = PositionTemplateEditor()
                    templateEditor.pack()
                    templateEditor.setVisible(true)
                }
            })
        }
        return jMenuItemEditTemplates
    }

    /**
     * This method initializes jMenuItemBrowseImages
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemBrowseImages(): JMenuItem? {
        if (jMenuItemBrowseImages == null) {
            jMenuItemBrowseImages = JMenuItem()
            jMenuItemBrowseImages.setText("Browse Image Files")
            jMenuItemBrowseImages.setMnemonic(KeyEvent.VK_I)
            try {
                jMenuItemBrowseImages.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/image_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemBrowseImages.")
                log.error(e)
            }
            jMenuItemBrowseImages.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { // Create a ImageListBrowser jpanel and replace the
// the content of the center of jPanelCenter with it.
//TODO: extend beyond switching between ilb and slb.
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    ilb = ImageListBrowser()
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ilb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
                        jMenuItemBrowseSpecimens.setEnabled(false)
                        jMenuItemBrowseImages.setEnabled(false)
                    } else {
                        jMenuItemBrowseSpecimens.setEnabled(true)
                        jMenuItemBrowseImages.setEnabled(true)
                    }
                    setStatusMessage("Found " + ilb.RowCount + " images.")
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    System.gc()
                }
            })
        }
        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_ENABLE_BROWSE) == "false") {
            jMenuItemBrowseImages.setEnabled(false)
        }
        return jMenuItemBrowseImages
    }

    /**
     * Update UI elements to reflect change in properties.
     */
    fun updateForPropertiesChange() {
        updateTitle()
        jMenuItemPreprocessOneDir.setText("Preprocess A Directory (as " + LocationInCollection.DefaultLocation + ")")
    }

    /**
     * This method initializes jMenuItemPreprocessOneDir: menu to preprocess one directory
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemPreprocessOne: JMenuItem?
        private get() {
            if (jMenuItemPreprocessOneDir == null) {
                jMenuItemPreprocessOneDir = JMenuItem()
                jMenuItemPreprocessOneDir.setText("Preprocess A Directory (as " + LocationInCollection.DefaultLocation + ")")
                jMenuItemPreprocessOneDir.setMnemonic(KeyEvent.VK_P)
                jMenuItemPreprocessOneDir.setEnabled(true)
                try {
                    jMenuItemPreprocessOneDir.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
                } catch (e: Exception) {
                    log.error("Can't open icon file for getJMenuItemPreprocessOne.")
                    log.error(e.localizedMessage)
                }
                jMenuItemPreprocessOneDir.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val scan = JobAllImageFilesScan(
                                JobAllImageFilesScan.Companion.SCAN_SELECT,
                                File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                        )
                        Thread(scan).start()
                    }
                })
            }
            return jMenuItemPreprocessOneDir
        }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuData(): JMenu? {
        if (jMenuData == null) {
            jMenuData = JMenu()
            jMenuData.setText("Data")
            jMenuData.setMnemonic(KeyEvent.VK_D)
            jMenuData.add(getJMenuItemSearch())
            jMenuData.add(getJMenuItemVerbatimTranscription())
            jMenuData.add(getJMenuItemVerbatimClassification())
            jMenuData.add(getJMenuItemBrowseImages())
            jMenuData.add(getJMenuItemBrowseSpecimens())
        }
        return jMenuData
    }

    /**
     * This method initializes jMenu1
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenuQualityControl(): JMenu? {
        if (jMenuQualityControl == null) {
            jMenuQualityControl = JMenu()
            jMenuQualityControl.setText("QualityControl")
            jMenuQualityControl.setMnemonic(KeyEvent.VK_Q)
            jMenuQualityControl.add(jMenuItemValidateImageNoDB)
            jMenuQualityControl.add(getJMenuItemQCBarcodes())
            jMenuQualityControl.add(getJMenuItemReconcileFiles())
        }
        return jMenuQualityControl
    }

    private fun getJMenuItemVerbatimTranscription(): JMenuItem? {
        if (jMenuItemVerbatimTranscription == null) {
            jMenuItemVerbatimTranscription = JMenuItem()
            jMenuItemVerbatimTranscription.setText("Verbatim Transcription")
            jMenuItemVerbatimTranscription.setEnabled(true)
            jMenuItemVerbatimTranscription.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = VerbatimToTranscribeDialog()
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemVerbatimTranscription
    }

    private fun getJMenuItemVerbatimClassification(): JMenuItem? {
        if (jMenuItemVerbatimClassification == null) {
            jMenuItemVerbatimClassification = JMenuItem()
            jMenuItemVerbatimClassification.setText("Fill in from Verbatim")
            jMenuItemVerbatimClassification.setEnabled(true)
            jMenuItemVerbatimClassification.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = VerbatimListDialog()
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemVerbatimClassification
    }

    /**
     * This method initializes jMenuItem4
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemValidateImageNoDB: JMenuItem?
        private get() {
            if (jMenuItemCheckForABarcode == null) {
                jMenuItemCheckForABarcode = JMenuItem()
                jMenuItemCheckForABarcode.setText("Preprocess One Image File")
                jMenuItemCheckForABarcode.setMnemonic(KeyEvent.VK_C)
                try {
                    jMenuItemCheckForABarcode.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/barcode_icon_16px.jpg")))
                } catch (e: Exception) {
                    log.error("Can't open icon file for jMenuItemCheckForABarcode.")
                    log.error(e)
                }
                jMenuItemCheckForABarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val scan = JobSingleBarcodeScan(false)
                        scan.registerListener(thisMainFrame)
                        Thread(scan).start()
                    }
                })
            }
            return jMenuItemCheckForABarcode
        }

    private fun getJMenuItemReconcileFiles(): JMenuItem? {
        if (jMenuItemReconcileFiles == null) {
            jMenuItemReconcileFiles = JMenuItem()
            jMenuItemReconcileFiles.setText("Reconcile image files with database")
            jMenuItemReconcileFiles.setMnemonic(KeyEvent.VK_R)
            jMenuItemReconcileFiles.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobFileReconciliation()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemReconcileFiles
    }

    /**
     * This method initializes jMenuItem5
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemQCBarcodes(): JMenuItem? {
        if (jMenuItemQCBarcodes == null) {
            jMenuItemQCBarcodes = JMenuItem()
            jMenuItemQCBarcodes.setText("QC Barcodes")
            jMenuItemQCBarcodes.setMnemonic(KeyEvent.VK_B)
            jMenuItemQCBarcodes.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                    Singleton.MainFrame.setStatusMessage("Running barcode QC checks")
                    val missingBarcodes: Array<String?> = SpecimenLifeCycle.Companion.MissingBarcodes
                    ilb = ImageListBrowser(true)
                    if (slb != null) {
                        jPanelCenter.remove(slb)
                    }
                    if (ulb != null) {
                        jPanelCenter.remove(ulb)
                    }
                    jPanelCenter.removeAll()
                    jPanelCenter.add(ilb, BorderLayout.CENTER)
                    jPanelCenter.revalidate()
                    jPanelCenter.repaint()
                    Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    log.debug(missingBarcodes.size)
                    if (missingBarcodes.size > 0) {
                        val errorCount = Counter()
                        for (i in missingBarcodes.indices) {
                            val builder: BarcodeBuilder = Singleton.BarcodeBuilder
                            val matcher: BarcodeMatcher = Singleton.BarcodeMatcher
                            val previous: String = builder.makeFromNumber(matcher.extractNumber(missingBarcodes[i]) - 1)
                            var previousFile = ""
                            var previousPath = ""
                            val sls = SpecimenLifeCycle()
                            val result: MutableList<Specimen?> = sls.findByBarcode(previous)
                            if (result != null && !result.isEmpty()) {
                                val images: MutableSet<ICImage?> = result[0].ICImages
                                if (images != null && !images.isEmpty()) {
                                    val it: MutableIterator<ICImage?> = images.iterator()
                                    if (it.hasNext()) {
                                        val image: ICImage? = it.next()
                                        previousFile = image.Filename
                                        previousPath = image.Path
                                    }
                                }
                            }
                            val err = RunnableJobError(previousFile, missingBarcodes[i], previousPath, "", "Barcode not found", null, null, null,
                                    RunnableJobError.Companion.TYPE_BARCODE_MISSING_FROM_SEQUENCE,
                                    previousFile,
                                    previousPath)
                            errorCount.appendError(err)
                        }
                        val report = "There are at least " + missingBarcodes.size + " barcodes missing from the sequence.\nMissing numbers are shown below.\nIf two or more numbers are missing in sequence, only the first will be listed here.\n\nFiles with mismmatched barcodes are shown in main window.\n"
                        val errorReportDialog = RunnableJobReportDialog(
                                Singleton.MainFrame,
                                report,
                                errorCount.Errors,
                                RunnableJobErrorTableModel.Companion.TYPE_MISSING_BARCODES,
                                "QC Barcodes Report")
                        errorReportDialog.setVisible(true)
                    } else {
                        JOptionPane.showMessageDialog(Singleton.MainFrame, "No barcodes are missing from the sequence.\nAny missmatches are shown in table.", "Barcode QC Report", JOptionPane.OK_OPTION)
                    }
                    System.gc()
                }
            })
        }
        return jMenuItemQCBarcodes
    }

    /**
     * This method initializes jMenuItem6
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemSearch(): JMenuItem? {
        if (jMenuItemSearch == null) {
            jMenuItemSearch = JMenuItem()
            jMenuItemSearch.setText("Search")
            jMenuItemSearch.setEnabled(true)
            jMenuItemSearch.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val s = SearchDialog(thisMainFrame)
                    s.setVisible(true)
                }
            })
        }
        return jMenuItemSearch
    }

    private fun getJMenuItemFindMissingImages(): JMenuItem? {
        if (jMenuItemFindMissingImages == null) {
            jMenuItemFindMissingImages = JMenuItem()
            jMenuItemFindMissingImages.setText("Find Missing Images")
            jMenuItemFindMissingImages.setEnabled(true)
            /*try {
				jMenuItemFindMissingImages.setIcon(new ImageIcon(this.Class.getResource("/edu/harvard/mcz/imagecapture/resources/images/red-warning-icon.png")));
			} catch (Exception e) {
				log.error("Can't open icon file for jMenuItemScanOneBarcode.");
				log.error(e.LocalizedMessage);
			}*/jMenuItemFindMissingImages.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) { //JTextField dateimaged = new JTextField();
                    val sls2 = SpecimenLifeCycle()
                    val paths: Array<String?> = sls2.DistinctPaths
                    //log.debug("num paths" + paths.length);
                    val pathCombo: JComboBox<String?> = JComboBox<String?>(paths)
                    pathCombo.setEditable(true)
                    val inputs: Array<JComponent?> = arrayOf<JComponent?>(
                            JLabel("<html>Find missing images<br/><br/>Please select the date imaged:<br/>"),
                            pathCombo
                    )
                    val result: Int = JOptionPane.showConfirmDialog(null, inputs, "Find missing images", JOptionPane.CANCEL_OPTION)
                    //
                    if (result == JOptionPane.YES_OPTION) { //String dateEntered = dateimaged.Text;
                        val dateEntered: String = pathCombo.SelectedItem.toString()
                        //dateEntered = dateEntered.replaceAll("\\", "\\\\");
//System.out.println("BEFORE DATE ENTERED IS " + dateEntered);
//dateEntered = dateEntered.replaceAll("\\", "\\\\");
                        println("DATE ENTERED IS $dateEntered")
                        val sls = SpecimenLifeCycle()
                        val results: MutableList<ICImage?> = sls.findImagesByPath(dateEntered)
                        val seqvals: ArrayList<Int?> = ArrayList<Any?>()
                        val missingvals: ArrayList<Int?> = ArrayList<Any?>()
                        var img_prefix = ""
                        for (im in results) {
                            val last_underscore: Int = im.Filename.lastIndexOf("_")
                            img_prefix = im.Filename.substring(0, last_underscore)
                            val dot: Int = im.Filename.indexOf(".")
                            val seqnum: String = im.Filename.substring(last_underscore + 1, dot)
                            //log.debug("seqnum: " + seqnum);
                            try {
                                val seqint = seqnum.toInt()
                                seqvals.add(seqint)
                                //log.debug("seqint: " +seqint);
                            } catch (e1: Exception) {
                            }
                        }
                        for (i in seqvals.indices) {
                            var current = seqvals[i]
                            if (i + 1 < seqvals.size) {
                                val next = seqvals[i + 1]
                                if (next!! - current!! != 1) {
                                    val gap = next - current
                                    for (j in 1 until gap) {
                                        missingvals.add(current + j)
                                    }
                                }
                                current = next
                            }
                        }
                        val sb = StringBuilder()
                        /*for(Integer cint : missingvals){
							if(cint < 10000){
								sb.append("ETHZ_ENT01_2017_03_15_00");
							}else if(cint < 100000){
								sb.append("ETHZ_ENT01_2017_03_15_0");
							}else if(cint < 1000000){
								sb.append("ETHZ_ENT01_2017_03_15_");
							}
							sb.append(cint);
							sb.append(".JPG");
							sb.append("\n");
						}*/for (cint in missingvals) {
                            sb.append(img_prefix)
                            sb.append(cint)
                            sb.append(".JPG")
                            sb.append("\n")
                        }
                        /*for(ICImage im : results){
							sb.append(im.Filename);
							sb.append("\n");
						}*/JOptionPane.showConfirmDialog(null, "Found " + results.size + " images in the database for the date " + dateEntered + ".\n\nPossible missing images:\n" + sb.toString(), "Find missing images", JOptionPane.PLAIN_MESSAGE)
                    }
                }
            })
        }
        return jMenuItemFindMissingImages
    }

    /**
     * This method initializes jMenuItemChangePassword
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemChangePassword(): JMenuItem? {
        if (jMenuItemChangePassword == null) {
            jMenuItemChangePassword = JMenuItem()
            jMenuItemChangePassword.setText("Change My Password")
            jMenuItemChangePassword.setMnemonic(KeyEvent.VK_M)
            try {
                jMenuItemChangePassword.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/key_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemChangePassword.")
                log.error(e)
            }
            jMenuItemChangePassword.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (Singleton.User != null) {
                        val cpd = ChangePasswordDialog(thisMainFrame, Singleton.User)
                        cpd.setVisible(true)
                    }
                }
            })
        }
        return jMenuItemChangePassword
    }

    /**
     * This method initializes jMenuItemCreateLabels
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemCreateLabels(): JMenuItem? {
        if (jMenuItemCreateLabels == null) {
            jMenuItemCreateLabels = JMenuItem()
            jMenuItemCreateLabels.setText("Create Unit Tray Labels")
            jMenuItemCreateLabels.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val lb = UnitTrayLabelBrowser()
                    lb.pack()
                    lb.setVisible(true)
                }
            })
        }
        return jMenuItemCreateLabels
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItem: JMenuItem?
        private get() {
            if (jMenuItemStats == null) {
                jMenuItemStats = JMenuItem()
                jMenuItemStats.setText("Statistics")
                jMenuItemStats.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val sls = SpecimenLifeCycle()
                        JOptionPane.showMessageDialog(Singleton.MainFrame,
                                sls.findSpecimenCount(),
                                "Record counts",
                                JOptionPane.INFORMATION_MESSAGE)
                    }
                })
            }
            return jMenuItemStats
        }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemRepeatOCR(): JMenuItem? {
        if (jMenuItemRepeatOCR == null) {
            jMenuItemRepeatOCR = JMenuItem()
            jMenuItemRepeatOCR.setText("Redo OCR for All")
            try {
                jMenuItemRepeatOCR.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRepeatOCR.")
                log.error(e)
            }
            jMenuItemRepeatOCR.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRepeatOCR()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRepeatOCR
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemListRunningJobs(): JMenuItem? {
        if (jMenuItemListRunningJobs == null) {
            jMenuItemListRunningJobs = JMenuItem()
            jMenuItemListRunningJobs.setText("List Running Jobs")
            jMenuItemListRunningJobs.setMnemonic(KeyEvent.VK_L)
            try {
                jMenuItemListRunningJobs.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/tools_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemListRunningJobs.")
                log.error(e)
            }
            jMenuItemListRunningJobs.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val jobFrame = RunnableJobFrame()
                    jobFrame.setVisible(true)
                }
            })
        }
        return jMenuItemListRunningJobs
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItemRedoOCROne(): JMenuItem? {
        if (jMenuItemRedoOCROne == null) {
            jMenuItemRedoOCROne = JMenuItem()
            jMenuItemRedoOCROne.setText("Redo OCR for A Directory")
            jMenuItemRedoOCROne.setMnemonic(KeyEvent.VK_R)
            try {
                jMenuItemRedoOCROne.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRedoOCROne.")
                log.error(e)
            }
            jMenuItemRedoOCROne.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val target = File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    val r = JobRepeatOCR(JobRepeatOCR.Companion.SCAN_SELECT, target)
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRedoOCROne
    }

    private fun getJMenuItemCleanupDirectory(): JMenuItem? {
        if (jMenuItemCleanupDirectory == null) {
            jMenuItemCleanupDirectory = JMenuItem()
            jMenuItemCleanupDirectory.setText("Cleanup Deleted Images")
            try {
                jMenuItemCleanupDirectory.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/bb_trsh_icon_16px.png")))
            } catch (e: Exception) {
                println("Can't open icon file for jMenuItemCleanupDirectory.")
                println(e.localizedMessage)
            }
            jMenuItemCleanupDirectory.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobCleanDirectory(
                            JobCleanDirectory.Companion.SCAN_SELECT,
                            File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    )
                    Thread(r).start()
                }
            })
        }
        return jMenuItemCleanupDirectory
    }

    private fun getJMenuItemRecheckTemplates(): JMenuItem? {
        if (jMenuItemRecheckTemplates == null) {
            jMenuItemRecheckTemplates = JMenuItem()
            jMenuItemRecheckTemplates.setText("Recheck cases of WholeImageOnly")
            jMenuItemRecheckTemplates.setMnemonic(KeyEvent.VK_W)
            try {
                jMenuItemRecheckTemplates.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckTemplates.")
                log.error(e)
            }
            jMenuItemRecheckTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRecheckForTemplates(
                            JobRecheckForTemplates.Companion.SCAN_SELECT,
                            File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE))
                    )
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRecheckTemplates
    }

    private fun getJMenuItemRecheckAllTemplates(): JMenuItem? {
        if (jMenuItemRecheckAllTemplates == null) {
            jMenuItemRecheckAllTemplates = JMenuItem()
            jMenuItemRecheckAllTemplates.setText("Recheck All cases of WholeImageOnly")
            try {
                jMenuItemRecheckAllTemplates.setIcon(ImageIcon(this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/reload_icon_16px.png")))
            } catch (e: Exception) {
                log.error("Can't open icon file for jMenuItemRecheckAllTemplates.")
                log.error(e)
            }
            jMenuItemRecheckAllTemplates.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val r = JobRecheckForTemplates()
                    Thread(r).start()
                }
            })
        }
        return jMenuItemRecheckAllTemplates
    }

    companion object {
        const val STATE_RUNNING = 1 // setup actions complete, menu items enabled.
        const val STATE_RESET = 2 // user logged out, menu items disabled.
        val BG_COLOR_QC_FIELD: Color? = Color(204, 204, 255) // Sun 4 //  @jve:decl-index=0:
        val BG_COLOR_ENT_FIELD: Color? = Color(255, 246, 213) //  @jve:decl-index=0:
        val BG_COLOR_ERROR: Color? = Color(255, 73, 43) // highlight fields with data errors  //  @jve:decl-index=0:
        private const val serialVersionUID = 536567118673854270L
        private val log: Log = LogFactory.getLog(MainFrame::class.java) //  @jve:decl-index=0:
        private const val STATE_INIT = 0 // initial state of application - most menu items disabled
    }

    init {
        thisMainFrame = this
        initialize()
        setState(STATE_INIT)
        this.pack()
        this.setVisible(true)
    }
} //  @jve:decl-index=0:visual-constraint="21,12"
