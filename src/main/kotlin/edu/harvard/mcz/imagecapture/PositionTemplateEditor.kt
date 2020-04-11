/**
 * PositionTemplateEditor.java
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
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.PositionTemplate
import edu.harvard.mcz.imagecapture.PositionTemplateEditor
import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.exceptions.BadTemplateException
import edu.harvard.mcz.imagecapture.exceptions.NoSuchTemplateException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.ui.ButtonEditor
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer
import edu.harvard.mcz.imagecapture.ui.dialog.PositionTemplateBoxDialog
import edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawing
import edu.harvard.mcz.imagecapture.ui.tablemodel.PositionTemplateTableModel
import edu.harvard.mcz.imagecapture.utility.FileUtility
import org.apache.commons.logging.LogFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*

/**
 * PositionTemplateEditor allows viewing and creation of position templates indicating which portions
 * of an image file contain a barcode, OCR text, a specimen, labels from the unit tray, and labels from
 * the specimen's pin.
 */
class PositionTemplateEditor : JFrame {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var jButtonSave: JButton? = null
    private var jLabel: JLabel? = null
    private var jLabel1: JLabel? = null
    private var jTextFieldTemplateId: JTextField? = null
    private var jTextFieldName: JTextField? = null
    private var jJMenuBar: JMenuBar? = null
    private var jMenu: JMenu? = null
    private var jMenuItem: JMenuItem? = null
    private var jMenuItem1: JMenuItem? = null
    private var jMenuItem2: JMenuItem? = null
    private var jLabel2: JLabel? = null
    private var jLabel3: JLabel? = null
    private var jLabel4: JLabel? = null
    private var jLabel5: JLabel? = null
    private var jLabel6: JLabel? = null
    private var jLabel7: JLabel? = null
    private var jTextField2: JTextField? = null
    private var controlBarcode: JButton? = null
    private var controlText: JButton? = null
    private var controlLabel: JButton? = null
    private var controlUTLabels: JButton? = null
    private var controlSpecimen: JButton? = null
    private var jLabel8: JLabel? = null
    private var jTextFieldImageFileName: JTextField? = null
    private var jPanel2: JPanel? = null
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    private var imagePanelForDrawing: ImagePanelForDrawing? = null
    private var thisFrame: PositionTemplateEditor?
    private var runningFromMain = false
    private var jPanel1: JPanel? = null
    private var template: PositionTemplate? = null //  @jve:decl-index=0:
    private var jScrollPane1: JScrollPane? = null
    private var referenceImageFilename // name of the currently loaded image file.  //  @jve:decl-index=0:
            : String? = null
    private var referenceImagePath //  @jve:decl-index=0:
            : String? = null
    private var jLabelFeedback: JLabel? = null
    private var jButton: JButton? = null
    private var jTextFieldBarcodeScan: JTextField? = null
    private var jButtonUnitTrayBarcode: JButton? = null
    private var jTextFieldUnitTrayBarcode: JTextField? = null
    private var jLabel9: JLabel? = null
    private var controlUTBarcode: JButton? = null

    /**
     * This is the default constructor
     */
    constructor() : super() {
        thisFrame = this
        initialize()
        pack()
        setBlankBackgroundImage()
    }

    /**
     * Constructor called from main method when running as stand alone application.
     *
     * @param runningAsApplication true to display file/exit menu option
     */
    constructor(runningAsApplication: Boolean) : super() {
        thisFrame = this
        runningFromMain = true
        initialize()
        pack()
        setBlankBackgroundImage()
    }

    fun setBlankBackgroundImage() {
        try { // Can't retrieve resource as a file from Jar file, unless giving File to ImageIcon....
            val url = this.javaClass.getResource("/edu/harvard/mcz/imagecapture/resources/images/blank2848x4272.jpg")
            log!!.error(url)
            log.error(url!!.toExternalForm())
            setImageFile(url)
        } catch (e: IOException) {
            log!!.error("Can't load blank template image")
            log.error(e)
        }
    }

    @Throws(NoSuchTemplateException::class)
    fun setTemplate(aTemplateId: String) {
        template = PositionTemplate(aTemplateId)
        jTextFieldTemplateId.setText(aTemplateId)
        jTextFieldName.setText(template.Name)
        if (template.ImageSize == null) {
            jTextField2.setText("Any Size")
            controlBarcode.setText("No Value")
            controlText.setText("No Value")
            controlLabel.setText("No Value")
            controlUTLabels.setText("No Value")
            controlSpecimen.setText("No Value")
            controlUTBarcode.setText("No Value")
        } else {
            jTextField2.setText("Width=" + template.ImageSize.width + " Height=" + template.ImageSize.height)
            setButtonTexts()
            if (template.ReferenceImage != null) {
                try {
                    setImageFile(File(template.ReferenceImageFilePath))
                } catch (e: IOException) {
                    log!!.error("Failed to load default image for template.")
                    log.error(e)
                }
            }
        }
        thisFrame.pack()
        jButtonSave.setEnabled(template.isEditable())
        controlBarcode.setEnabled(template.isEditable())
        controlText.setEnabled(template.isEditable())
        controlLabel.setEnabled(template.isEditable())
        controlUTLabels.setEnabled(template.isEditable())
        controlSpecimen.setEnabled(template.isEditable())
        controlUTBarcode.setEnabled(template.isEditable())
        drawLayers()
    }

    /**
     * Set the image displayed in the editor given an URL (needed to load from resource inside jar).
     *
     * @param anImageURL an URL pointing to an image file.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun setImageFile(anImageURL: URL) {
        referenceImageFilename = anImageURL.path
        //TODO: Won't work with referenceImageFilename lookup.
        loadImage(ImageIO.read(anImageURL))
    }

    /**
     * Set the image displayed in the editor given a File
     *
     * @param anImageFile the image file to display.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun setImageFile(anImageFile: File?) {
        if (anImageFile != null) {
            referenceImageFilename = anImageFile.name
            referenceImagePath = anImageFile.path
            loadImage(ImageIO.read(anImageFile))
            jTextFieldImageFileName.setText(anImageFile.name)
        }
    }

    private fun loadImage(anImage: Image?) {
        imagePanelForDrawing.setImage(anImage)
        imagePanelForDrawing.zoomToFit()
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setPreferredSize(Dimension(1100, 900))
        this.setJMenuBar(getJJMenuBar())
        this.setContentPane(getJContentPane())
        this.setTitle("Image Template Editor")
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
            val gridBagConstraints24 = GridBagConstraints()
            gridBagConstraints24.fill = GridBagConstraints.BOTH
            gridBagConstraints24.gridy = 10
            gridBagConstraints24.weightx = 1.0
            gridBagConstraints24.anchor = GridBagConstraints.WEST
            gridBagConstraints24.gridx = 1
            val gridBagConstraints113 = GridBagConstraints()
            gridBagConstraints113.gridx = 0
            gridBagConstraints113.anchor = GridBagConstraints.EAST
            gridBagConstraints113.gridy = 10
            jLabel9 = JLabel()
            jLabel9.setText("Taxon Name Barcode")
            val gridBagConstraints23 = GridBagConstraints()
            gridBagConstraints23.fill = GridBagConstraints.BOTH
            gridBagConstraints23.gridy = 13
            gridBagConstraints23.weightx = 1.0
            gridBagConstraints23.anchor = GridBagConstraints.WEST
            gridBagConstraints23.gridx = 1
            val gridBagConstraints112 = GridBagConstraints()
            gridBagConstraints112.gridx = 0
            gridBagConstraints112.gridy = 13
            val gridBagConstraints22 = GridBagConstraints()
            gridBagConstraints22.fill = GridBagConstraints.BOTH
            gridBagConstraints22.gridy = 12
            gridBagConstraints22.weightx = 1.0
            gridBagConstraints22.anchor = GridBagConstraints.WEST
            gridBagConstraints22.gridx = 1
            val gridBagConstraints111 = GridBagConstraints()
            gridBagConstraints111.gridx = 0
            gridBagConstraints111.gridy = 12
            val gridBagConstraints110 = GridBagConstraints()
            gridBagConstraints110.gridx = 1
            gridBagConstraints110.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints110.gridwidth = 1
            gridBagConstraints110.anchor = GridBagConstraints.NORTH
            gridBagConstraints110.gridy = 14
            jLabelFeedback = JLabel()
            jLabelFeedback.setText(" ")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.fill = GridBagConstraints.BOTH
            gridBagConstraints21.gridy = 0
            gridBagConstraints21.weightx = 2.0
            gridBagConstraints21.anchor = GridBagConstraints.NORTHWEST
            gridBagConstraints21.gridx = 1
            val gridBagConstraints13 = GridBagConstraints()
            gridBagConstraints13.gridx = 0
            gridBagConstraints13.anchor = GridBagConstraints.NORTHEAST
            gridBagConstraints13.gridy = 0
            jLabel8 = JLabel()
            jLabel8.setText("ImageFile")
            val gridBagConstraints18 = GridBagConstraints()
            gridBagConstraints18.fill = GridBagConstraints.BOTH
            gridBagConstraints18.gridy = 9
            gridBagConstraints18.weightx = 1.0
            gridBagConstraints18.anchor = GridBagConstraints.WEST
            gridBagConstraints18.gridx = 1
            val gridBagConstraints17 = GridBagConstraints()
            gridBagConstraints17.fill = GridBagConstraints.BOTH
            gridBagConstraints17.gridy = 8
            gridBagConstraints17.weightx = 1.0
            gridBagConstraints17.anchor = GridBagConstraints.WEST
            gridBagConstraints17.gridx = 1
            val gridBagConstraints16 = GridBagConstraints()
            gridBagConstraints16.fill = GridBagConstraints.BOTH
            gridBagConstraints16.gridy = 7
            gridBagConstraints16.weightx = 1.0
            gridBagConstraints16.anchor = GridBagConstraints.WEST
            gridBagConstraints16.gridx = 1
            val gridBagConstraints15 = GridBagConstraints()
            gridBagConstraints15.fill = GridBagConstraints.BOTH
            gridBagConstraints15.gridy = 6
            gridBagConstraints15.weightx = 1.0
            gridBagConstraints15.anchor = GridBagConstraints.WEST
            gridBagConstraints15.gridx = 1
            val gridBagConstraints14 = GridBagConstraints()
            gridBagConstraints14.fill = GridBagConstraints.BOTH
            gridBagConstraints14.gridy = 5
            gridBagConstraints14.weightx = 1.0
            gridBagConstraints14.anchor = GridBagConstraints.WEST
            gridBagConstraints14.gridx = 1
            val gridBagConstraints12 = GridBagConstraints()
            gridBagConstraints12.fill = GridBagConstraints.BOTH
            gridBagConstraints12.gridy = 3
            gridBagConstraints12.weightx = 1.0
            gridBagConstraints12.anchor = GridBagConstraints.WEST
            gridBagConstraints12.gridx = 1
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.gridx = 0
            gridBagConstraints11.anchor = GridBagConstraints.EAST
            gridBagConstraints11.gridy = 9
            jLabel7 = JLabel()
            jLabel7.setText("Specimen")
            jLabel7.setForeground(Color.ORANGE)
            val gridBagConstraints10 = GridBagConstraints()
            gridBagConstraints10.gridx = 0
            gridBagConstraints10.anchor = GridBagConstraints.EAST
            gridBagConstraints10.gridy = 8
            jLabel6 = JLabel()
            jLabel6.setText("Tray Labels")
            jLabel6.setForeground(Color.CYAN)
            val gridBagConstraints9 = GridBagConstraints()
            gridBagConstraints9.gridx = 0
            gridBagConstraints9.anchor = GridBagConstraints.EAST
            gridBagConstraints9.gridy = 7
            jLabel5 = JLabel()
            jLabel5.setText("Pin Labels")
            jLabel5.setForeground(Color.MAGENTA)
            val gridBagConstraints8 = GridBagConstraints()
            gridBagConstraints8.gridx = 0
            gridBagConstraints8.anchor = GridBagConstraints.EAST
            gridBagConstraints8.insets = Insets(0, 3, 0, 0)
            gridBagConstraints8.gridy = 6
            jLabel4 = JLabel()
            jLabel4.setText("Taxon Name Label")
            jLabel4.setForeground(Color.BLUE)
            val gridBagConstraints7 = GridBagConstraints()
            gridBagConstraints7.gridx = 0
            gridBagConstraints7.anchor = GridBagConstraints.EAST
            gridBagConstraints7.gridy = 5
            jLabel3 = JLabel()
            jLabel3.setText("Barcode")
            jLabel3.setForeground(Color.RED)
            val gridBagConstraints6 = GridBagConstraints()
            gridBagConstraints6.gridx = 0
            gridBagConstraints6.anchor = GridBagConstraints.EAST
            gridBagConstraints6.gridy = 3
            jLabel2 = JLabel()
            jLabel2.setText("Image Size")
            val gridBagConstraints5 = GridBagConstraints()
            gridBagConstraints5.fill = GridBagConstraints.BOTH
            gridBagConstraints5.gridy = 2
            gridBagConstraints5.weightx = 1.0
            gridBagConstraints5.anchor = GridBagConstraints.WEST
            gridBagConstraints5.gridx = 1
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.fill = GridBagConstraints.BOTH
            gridBagConstraints4.gridy = 1
            gridBagConstraints4.weightx = 1.0
            gridBagConstraints4.anchor = GridBagConstraints.WEST
            gridBagConstraints4.gridx = 1
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 0
            gridBagConstraints2.anchor = GridBagConstraints.EAST
            gridBagConstraints2.gridy = 2
            jLabel1 = JLabel()
            jLabel1.setText("Name")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.gridx = 0
            gridBagConstraints1.anchor = GridBagConstraints.EAST
            gridBagConstraints1.gridy = 1
            jLabel = JLabel()
            jLabel.setText("Template ID")
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.gridx = 1
            gridBagConstraints.gridy = 11
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(getJButtonSave(), gridBagConstraints)
            jPanel.add(jLabel, gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(getJTextFieldTemplateId(), gridBagConstraints4)
            jPanel.add(getJTextFieldName(), gridBagConstraints5)
            jPanel.add(jLabel2, gridBagConstraints6)
            jPanel.add(jLabel3, gridBagConstraints7)
            jPanel.add(jLabel4, gridBagConstraints8)
            jPanel.add(jLabel5, gridBagConstraints9)
            jPanel.add(jLabel6, gridBagConstraints10)
            jPanel.add(jLabel7, gridBagConstraints11)
            jPanel.add(getJTextField2(), gridBagConstraints12)
            jPanel.add(jTextField3, gridBagConstraints14)
            jPanel.add(jTextField4, gridBagConstraints15)
            jPanel.add(jTextField5, gridBagConstraints16)
            jPanel.add(jTextField6, gridBagConstraints17)
            jPanel.add(jTextField7, gridBagConstraints18)
            jPanel.add(jLabel8, gridBagConstraints13)
            jPanel.add(jTextField8, gridBagConstraints21)
            jPanel.add(jLabelFeedback, gridBagConstraints110)
            jPanel.add(getJButton(), gridBagConstraints111)
            jPanel.add(getJTextFieldBarcodeScan(), gridBagConstraints22)
            jPanel.add(jButton1, gridBagConstraints112)
            jPanel.add(jTextField, gridBagConstraints23)
            jPanel.add(jLabel9, gridBagConstraints113)
            jPanel.add(jTextField9, gridBagConstraints24)
        }
        return jPanel
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSave(): JButton? {
        if (jButtonSave == null) {
            jButtonSave = JButton()
            jButtonSave.setText("Save Template")
            jButtonSave.setEnabled(false)
            jButtonSave.setMnemonic(KeyEvent.VK_S)
            jButtonSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    jLabelFeedback.setText(" ")
                    if (template!!.isEditable()) {
                        template.setTemplateId(jTextFieldTemplateId.Text)
                        template.setTemplateName(jTextFieldName.Text)
                        template.setImageSize(imagePanelForDrawing.ImageSize)
                        try {
                            if (template.TemplateId.trim({ it <= ' ' }) == "") {
                                throw BadTemplateException("Template ID can't be blank.")
                            }
                            if (template!!.ReferenceImage == null) {
                                val ils = ICImageLifeCycle()
                                val imageToFind = ICImage()
                                imageToFind.setFilename(referenceImageFilename)
                                val images: MutableList<ICImage?> = ils.findByExample(imageToFind)
                                if (!images.isEmpty()) {
                                    template!!.setReferenceImage(images[0])
                                } else { //create a new image record
                                    val newImage = ICImage()
                                    newImage.setFilename(referenceImageFilename)
                                    // path should be relative to the base path
// just substituting won't work for images off the base path.
                                    val startPointName: String = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEBASE)
                                    newImage.setPath(
                                            referenceImagePath!!.replace(startPointName.toRegex(), "").replace(referenceImageFilename + "$".toRegex(), "")
                                    )
                                    newImage.setTemplateId(template.TemplateId)
                                    ils.persist(newImage)
                                    if (referenceImageFilename != null) {
                                        jTextFieldImageFileName.setText(referenceImageFilename)
                                    }
                                }
                            }
                            //TODO: Check that template is valid, not overlapping with existing template.
//Test images IMG_00005.jpg and IMG_00001.jpg suggest that overlapping templates might
//be needed - where all parameters except extent of barcode are the same.
                            template!!.persist()
                            jLabelFeedback.setText("Saved " + template.TemplateId)
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to save template, invalid data. " + e1.message,
                                    "Error:BadTemplateData",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.debug(e1)
                        } catch (e2: SaveFailedException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to save template. " + e2.message,
                                    "Error:SaveFailed",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.debug(e2)
                        }
                    }
                }
            })
        }
        return jButtonSave
    }

    /**
     * This method initializes jTextFieldTemplateId
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldTemplateId(): JTextField? {
        if (jTextFieldTemplateId == null) {
            jTextFieldTemplateId = JTextField(50)
        }
        return jTextFieldTemplateId
    }

    /**
     * This method initializes jTextFieldName
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldName(): JTextField? {
        if (jTextFieldName == null) {
            jTextFieldName = JTextField()
        }
        return jTextFieldName
    }

    /**
     * This method initializes jJMenuBar
     *
     * @return javax.swing.JMenuBar
     */
    private fun getJJMenuBar(): JMenuBar? {
        if (jJMenuBar == null) {
            jJMenuBar = JMenuBar()
            jJMenuBar.add(getJMenu())
        }
        return jJMenuBar
    }

    /**
     * This method initializes jMenu
     *
     * @return javax.swing.JMenu
     */
    private fun getJMenu(): JMenu? {
        if (jMenu == null) {
            jMenu = JMenu()
            jMenu.setText("File")
            jMenu.setMnemonic(KeyEvent.VK_F)
            jMenu.add(jMenuItemLoadImage)
            jMenu.add(jMenuItemCreateTemplate)
            jMenu.add(getJMenuItem2())
        }
        return jMenu
    }

    /**
     * Set the text for each button as the position and size of the relevant portion of the template.
     */
    private fun setButtonTexts() {
        controlBarcode.setText("UL=" + template.BarcodePosition.width + "," + template.BarcodePosition.height + " W/H=" + template.BarcodeSize.width + "," + template.BarcodeSize.height)
        controlText.setText("UL=" + template.TextPosition.width + "," + template.TextPosition.height + " W/H=" + template.TextSize.width + "," + template.TextSize.height)
        controlLabel.setText("UL=" + template.LabelPosition.width + "," + template.LabelPosition.height + " W/H=" + template.LabelSize.width + "," + template.LabelSize.height)
        controlUTLabels.setText("UL=" + template.UTLabelsPosition.width + "," + template.UTLabelsPosition.height + " W/H=" + template.UTLabelsSize.width + "," + template.UTLabelsSize.height)
        controlSpecimen.setText("UL=" + template.SpecimenPosition.width + "," + template.SpecimenPosition.height + " W/H=" + template.SpecimenSize.width + "," + template.SpecimenSize.height)
        controlUTBarcode.setText("UL=" + template.UtBarcodePosition.width + "," + template.UtBarcodePosition.height + " W/H=" + template.UtBarcodeSize.width + "," + template.UtBarcodeSize.height)
    }

    /**
     * This method initializes jMenuItem
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemCreateTemplate: JMenuItem?
        private get() {
            if (jMenuItem == null) {
                jMenuItem = JMenuItem()
                jMenuItem.setText("Create New Template From Image")
                jMenuItem.setMnemonic(KeyEvent.VK_N)
                jMenuItem.setEnabled(true)
                jMenuItem.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        template = PositionTemplate(true)
                        template.setImageSize(imagePanelForDrawing.ImageSize)
                        jTextField2.setText("Width=" + template.ImageSize.width + " Height=" + template.ImageSize.height)
                        setButtonTexts()
                        drawLayers()
                        jButtonSave.setEnabled(template.isEditable())
                        controlBarcode.setEnabled(template.isEditable())
                        controlText.setEnabled(template.isEditable())
                        controlLabel.setEnabled(template.isEditable())
                        controlUTLabels.setEnabled(template.isEditable())
                        controlSpecimen.setEnabled(template.isEditable())
                        controlUTBarcode.setEnabled(template.isEditable())
                        drawLayers()
                    }
                })
            }
            return jMenuItem
        }

    /**
     * This method initializes jMenuItem1
     *
     * @return javax.swing.JMenuItem
     */
    private val jMenuItemLoadImage: JMenuItem?
        private get() {
            if (jMenuItem1 == null) {
                jMenuItem1 = JMenuItem()
                jMenuItem1.setText("Load Image")
                jMenuItem1.setMnemonic(KeyEvent.VK_L)
                jMenuItem1.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        jLabelFeedback.setText("")
                        val imageFile: File = FileUtility.askForImageFile(File(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_LASTPATH)))
                        if (imageFile != null) {
                            jLabelFeedback.setText("Loading...")
                            try {
                                setImageFile(imageFile)
                                jLabelFeedback.setText("")
                                drawLayers()
                            } catch (e1: IOException) {
                                log!!.debug(e1)
                                jLabelFeedback.setText("Unable to load image.")
                            }
                        }
                        drawLayers()
                    }
                })
            }
            return jMenuItem1
        }

    /**
     * This method initializes jMenuItem2
     *
     * @return javax.swing.JMenuItem
     */
    private fun getJMenuItem2(): JMenuItem? {
        if (jMenuItem2 == null) {
            jMenuItem2 = JMenuItem()
            if (runningFromMain) {
                jMenuItem2.setText("Exit")
                jMenuItem2.setMnemonic(KeyEvent.VK_E)
            } else {
                jMenuItem2.setText("Close Window")
                jMenuItem2.setMnemonic(KeyEvent.VK_C)
            }
            jMenuItem2.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (runningFromMain) {
                        ImageCaptureApp.exit(ImageCaptureApp.EXIT_NORMAL)
                    } else {
                        thisFrame.setVisible(false)
                    }
                }
            })
        }
        return jMenuItem2
    }

    /**
     * This method initializes jTextField2
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextField2(): JTextField? {
        if (jTextField2 == null) {
            jTextField2 = JTextField()
            jTextField2.setEditable(false)
        }
        return jTextField2
    }

    /**
     * This method initializes jTextField3
     *
     * @return javax.swing.JTextField
     */
    private val jTextField3: JButton?
        private get() {
            if (controlBarcode == null) {
                controlBarcode = JButton()
                controlBarcode.setEnabled(false)
                controlBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.BarcodeULPosition,
                                    template.BarcodeSize,
                                    "Barcode in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setBarcodePosition(dialog.UL)
                                template.setBarcodeSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlBarcode
        }

    /**
     * This method initializes jTextField4
     *
     * @return javax.swing.JTextField
     */
    private val jTextField4: JButton?
        private get() {
            if (controlText == null) {
                controlText = JButton()
                controlText.setEnabled(false)
                controlText.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.TextPosition,
                                    template.TextSize,
                                    "Taxon Name Label in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setTextPosition(dialog.UL)
                                template.setTextSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlText
        }

    /**
     * This method initializes jTextField5
     *
     * @return javax.swing.JTextField
     */
    private val jTextField5: JButton?
        private get() {
            if (controlLabel == null) {
                controlLabel = JButton()
                controlLabel.setEnabled(false)
                controlLabel.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.LabelPosition,
                                    template.LabelSize,
                                    "Pin Labels in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setLabelPosition(dialog.UL)
                                template.setLabelSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlLabel
        }

    /**
     * This method initializes jTextField6
     *
     * @return javax.swing.JTextField
     */
    private val jTextField6: JButton?
        private get() {
            if (controlUTLabels == null) {
                controlUTLabels = JButton()
                controlUTLabels.setEnabled(false)
                controlUTLabels.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.UTLabelsPosition,
                                    template.UTLabelsSize,
                                    "Unit Tray Labels in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setUTLabelsPosition(dialog.UL)
                                template.setUTLabelsSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlUTLabels
        }

    /**
     * This method initializes jTextField7
     *
     * @return javax.swing.JTextField
     */
    private val jTextField7: JButton?
        private get() {
            if (controlSpecimen == null) {
                controlSpecimen = JButton()
                controlSpecimen.setEnabled(false)
                controlSpecimen.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.SpecimenPosition,
                                    template.SpecimenSize,
                                    "Specimen in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setSpecimenPosition(dialog.UL)
                                template.setSpecimenSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlSpecimen
        }

    /**
     * This method initializes jTextField8
     *
     * @return javax.swing.JTextField
     */
    private val jTextField8: JTextField?
        private get() {
            if (jTextFieldImageFileName == null) {
                jTextFieldImageFileName = JTextField(50)
                jTextFieldImageFileName.setEditable(false)
            }
            return jTextFieldImageFileName
        }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel2(): JPanel? {
        if (jPanel2 == null) {
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.fill = GridBagConstraints.BOTH
            gridBagConstraints3.weighty = 1.0
            gridBagConstraints3.anchor = GridBagConstraints.NORTH
            gridBagConstraints3.weightx = 1.0
            jPanel2 = JPanel()
            jPanel2.setLayout(GridBagLayout())
            jPanel2.add(getJScrollPane(), gridBagConstraints3)
        }
        return jPanel2
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            jScrollPane = JScrollPane()
            //jScrollPane.setPreferredSize(new Dimension(600,150));
            jScrollPane.setViewportView(getJTable())
        }
        return jScrollPane
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private fun getJTable(): JTable? {
        if (jTable == null) {
            jTable = JTable()
            val templates: MutableList<PositionTemplate?> = PositionTemplate.Companion.Templates
            jTable.setModel(PositionTemplateTableModel(templates))
            jTable.getColumn("").setCellRenderer(ButtonRenderer())
            jTable.getColumn("").setCellEditor(ButtonEditor(ButtonEditor.Companion.OPEN_TEMPLATE, thisFrame))
        }
        return jTable
    }

    /**
     * This method initializes imagePanelForDrawing
     *
     * @return edu.harvard.mcz.imagecapture.ui.frame.ImagePanelForDrawing
     */
    private fun getImagePanelForDrawing(): ImagePanelForDrawing? {
        if (imagePanelForDrawing == null) {
            imagePanelForDrawing = ImagePanelForDrawing()
            imagePanelForDrawing.setPreferredSize(Dimension(600, 600))
        }
        return imagePanelForDrawing
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            val gridBagConstraints19 = GridBagConstraints()
            gridBagConstraints19.fill = GridBagConstraints.BOTH
            gridBagConstraints19.weighty = 1.0
            gridBagConstraints19.gridx = 2
            gridBagConstraints19.gridy = 1
            gridBagConstraints19.weightx = 1.0
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            val g1 = GridBagConstraints()
            g1.gridx = 1
            g1.anchor = GridBagConstraints.NORTHEAST
            g1.weightx = 0.1
            g1.weighty = 0.75
            g1.fill = GridBagConstraints.HORIZONTAL
            g1.gridy = 1
            val g2 = GridBagConstraints()
            g2.gridx = 1
            g2.anchor = GridBagConstraints.NORTH
            g2.fill = GridBagConstraints.BOTH
            g2.gridwidth = 2
            g2.weighty = 0.2
            g2.weightx = 0.0
            g2.gridy = 0
            jPanel1.add(getJPanel(), g1)
            jPanel1.add(getJPanel2(), g2)
            jPanel1.add(getJScrollPane1(), gridBagConstraints19)
        }
        return jPanel1
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane1(): JScrollPane? {
        if (jScrollPane1 == null) {
            jScrollPane1 = JScrollPane()
            jScrollPane1.setPreferredSize(Dimension(800, 800))
            jScrollPane1.setViewportView(getImagePanelForDrawing())
        }
        return jScrollPane1
    }

    /**
     * Draw boxes delimiting the various parts of the template on the current image.
     * Public so that it can be invoked from ButtonEditor().
     */
    fun drawLayers() { // draw the image bounds in black.
        if (template != null) {
            imagePanelForDrawing.clearOverlay()
            imagePanelForDrawing.drawBox(Dimension(0, 0), template.ImageSize, Color.BLACK, BasicStroke(2f))
            // draw each template layer in a distinct color (keyed to UI text).
            imagePanelForDrawing.drawBox(template.BarcodeULPosition, template.BarcodeSize, Color.RED)
            imagePanelForDrawing.drawBox(template.TextPosition, template.TextSize, Color.BLUE)
            imagePanelForDrawing.drawBox(template.SpecimenPosition, template.SpecimenSize, Color.ORANGE)
            imagePanelForDrawing.drawBox(template.UTLabelsPosition, template.UTLabelsSize, Color.CYAN)
            imagePanelForDrawing.drawBox(template.LabelPosition, template.LabelSize, Color.MAGENTA)
            imagePanelForDrawing.drawBox(template.UtBarcodePosition, template.UtBarcodeSize, Color.BLACK)
        }
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Check for Barcode")
            jButton.setMnemonic(KeyEvent.VK_C)
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    if (template != null && imagePanelForDrawing.Image != null) {
                        val candidateImageFile = CandidateImageFile()
                        jTextFieldBarcodeScan.setText(candidateImageFile.getBarcodeTextFromImage(imagePanelForDrawing.Image as BufferedImage, template, false))
                    }
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldBarcodeScan(): JTextField? {
        if (jTextFieldBarcodeScan == null) {
            jTextFieldBarcodeScan = JTextField()
            jTextFieldBarcodeScan.setEditable(false)
        }
        return jTextFieldBarcodeScan
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private val jButton1: JButton?
        private get() {
            if (jButtonUnitTrayBarcode == null) {
                jButtonUnitTrayBarcode = JButton()
                jButtonUnitTrayBarcode.setText("Check Taxon Barcode")
                jButtonUnitTrayBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        val candidateImageFile = CandidateImageFile()
                        jTextFieldUnitTrayBarcode.setText(candidateImageFile.getBarcodeUnitTrayTextFromImage(imagePanelForDrawing.Image as BufferedImage, template!!))
                    }
                })
            }
            return jButtonUnitTrayBarcode
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField: JTextField?
        private get() {
            if (jTextFieldUnitTrayBarcode == null) {
                jTextFieldUnitTrayBarcode = JTextField()
                jTextFieldUnitTrayBarcode.setEditable(false)
            }
            return jTextFieldUnitTrayBarcode
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField9: JButton?
        private get() {
            if (controlUTBarcode == null) {
                controlUTBarcode = JButton()
                controlUTBarcode.setEnabled(false)
                controlUTBarcode.addActionListener(object : ActionListener {
                    override fun actionPerformed(e: ActionEvent?) {
                        try {
                            val dialog = PositionTemplateBoxDialog(
                                    thisFrame, template.ImageSize,
                                    template.UtBarcodePosition,
                                    template.UtBarcodeSize,
                                    "UnitTray/Taxon Barcode in " + template.TemplateId)
                            dialog.setVisible(true)
                            if (template!!.isEditable()) {
                                template.setUtBarcodePosition(dialog.UL)
                                template.setUtBarcodeSize(dialog.Size)
                                setButtonTexts()
                            }
                        } catch (e1: BadTemplateException) {
                            JOptionPane.showMessageDialog(thisFrame,
                                    "Error. Unable to edit, invalid template data. " + e1.message,
                                    "Error:BadTemplate",
                                    JOptionPane.ERROR_MESSAGE)
                            log!!.error(e1)
                        }
                        drawLayers()
                    }
                })
            }
            return controlUTBarcode
        }

    companion object {
        private const val serialVersionUID = -6969168467927467337L
        private val log = LogFactory.getLog(PositionTemplateEditor::class.java)
        /**
         * @param args
         */
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater(Runnable {
                val thisClass = PositionTemplateEditor(true)
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
                thisClass.setVisible(true)
            })
        }
    }
} //  @jve:decl-index=0:visual-constraint="9,-1"
