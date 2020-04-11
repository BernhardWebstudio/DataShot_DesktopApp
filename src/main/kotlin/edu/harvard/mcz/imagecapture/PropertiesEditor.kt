/**
 * PropertiesEditor.java
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
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import javax.swing.*

/**
 * Frame to display list of property key/value pairs where the values for each
 * key can be edited and the changes can be saved to the imagecapture.properties
 * file.
 */
class PropertiesEditor : JFrame() {
    private var jContentPane: JPanel? = null
    private var jPanel: JPanel? = null
    private var jPanel1: JPanel? = null
    private var jButton: JButton? = null
    private var jButtonSave: JButton? = null
    private var jTextField: JTextField? = null
    private var properties: ImageCaptureProperties? = null
    private var thisEditor: PropertiesEditor? = null
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    /**
     * This method initializes this, an instance of PropertiesEditor setting up the frame.
     */
    private fun initialize() {
        this.setSize(669, 347)
        this.setContentPane(getJContentPane())
        this.setTitle(ImageCaptureApp.APP_NAME + " Preferences")
        jTextField.setText(Singleton.Properties.PropertiesSource)
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
            jContentPane.add(getJTextField(), BorderLayout.NORTH)
            jContentPane.add(getJPanel(), BorderLayout.CENTER)
            jContentPane.add(getJPanel1(), BorderLayout.SOUTH)
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
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.fill = GridBagConstraints.BOTH
            gridBagConstraints.gridy = 0
            gridBagConstraints.weightx = 1.0
            gridBagConstraints.weighty = 1.0
            gridBagConstraints.gridx = 0
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(getJScrollPane(), gridBagConstraints)
        }
        return jPanel
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel1(): JPanel? {
        if (jPanel1 == null) {
            jPanel1 = JPanel()
            jPanel1.setLayout(GridBagLayout())
            jPanel1.add(getJButton(), GridBagConstraints())
            jPanel1.add(getJButtonSave(), GridBagConstraints())
        }
        return jPanel1
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJButton(): JButton? {
        if (jButton == null) {
            jButton = JButton()
            jButton.setText("Cancel")
            jButton.setMnemonic(KeyEvent.VK_C)
            jButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    thisEditor.setVisible(false)
                }
            })
        }
        return jButton
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJButtonSave(): JButton? {
        if (jButtonSave == null) {
            jButtonSave = JButton()
            jButtonSave.setText("Save")
            jButtonSave.setMnemonic(KeyEvent.VK_S)
            jButtonSave.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try {
                        if (jTable.isEditing()) {
                            jTable.CellEditor.stopCellEditing()
                        }
                        Singleton.setProperties(jTable.Model as ImageCaptureProperties)
                        Singleton.Properties.saveProperties()
                        // Set up a barcode (text read from barcode label for pin) matcher/builder
                        if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_MCZENT) { // ** Configured for the MCZ Entomology Collection, use MCZ assumptions.
                            val barcodeTextBuilderMatcher = MCZENTBarcode()
                            Singleton.setBarcodeBuilder(barcodeTextBuilderMatcher)
                            Singleton.setBarcodeMatcher(barcodeTextBuilderMatcher)
                        } else if (Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) { // ** Configured for the ETHZ Entomology Collection, use MCZ assumptions.
                            val barcodeTextBuilderMatcher = ETHZBarcode()
                            Singleton.setBarcodeBuilder(barcodeTextBuilderMatcher)
                            Singleton.setBarcodeMatcher(barcodeTextBuilderMatcher)
                        } else {
                            throw Exception("Configured collection not recognized.")
                        }
                        //Singleton.SingletonInstance.MainFrame.updateTitle();
                        Singleton.MainFrame.updateForPropertiesChange()
                        thisEditor.dispose()
                    } catch (e1: Exception) {
                        println("Save Failed")
                    }
                }
            })
        }
        return jButtonSave
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextField(): JTextField? {
        if (jTextField == null) {
            jTextField = JTextField()
            jTextField.setEditable(false)
        }
        return jTextField
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            jScrollPane = JScrollPane()
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
            jTable = JTable(properties)
        }
        return jTable
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    /**
     * This is the default constructor
     */
    init {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
        properties = ImageCaptureProperties()
        thisEditor = this // make this visible to anonymous methods of button action events.
        initialize()
        jScrollPane.setPreferredSize(Dimension(669, 347))
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
