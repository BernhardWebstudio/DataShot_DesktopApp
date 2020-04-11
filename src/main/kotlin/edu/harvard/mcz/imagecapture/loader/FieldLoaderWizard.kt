/**
 * FieldLoaderWizard.java
 *
 *
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
package edu.harvard.mcz.imagecapture.loader


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder

class FieldLoaderWizard : JDialog() {
    private val contentPanel: JPanel? = JPanel()
    private var filenameField: JTextField? = null
    private var table: JTable? = null
    private var textFieldOKToLoad: JTextField? = null
    private var selectedFile: File? = null
    protected fun pickFile() {
        val fileChooser = JFileChooser()
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
        if (Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH) != null) {
            fileChooser.setCurrentDirectory(File(Singleton.getProperties().getProperties().getProperty(ImageCaptureProperties.Companion.KEY_LASTLOADPATH)))
        }
        val returnValue: Int = fileChooser.showOpenDialog(Singleton.getMainFrame())
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile()
        }
    }

    companion object {
        private const val serialVersionUID = 6643976186857446662L
    }

    /**
     * Create a dialog to manage loading transcribed verbatim data or verbatim classified data from an external file..
     */
    init {
        setTitle("Load Data")
        setBounds(100, 100, 665, 345)
        getContentPane().setLayout(BorderLayout())
        contentPanel.setBorder(EmptyBorder(5, 5, 5, 5))
        getContentPane().add(contentPanel, BorderLayout.CENTER)
        val gbl_contentPanel = GridBagLayout()
        gbl_contentPanel.columnWidths = intArrayOf(0, 0, 0)
        gbl_contentPanel.rowHeights = intArrayOf(0, 0, 0, 0, 0)
        gbl_contentPanel.columnWeights = doubleArrayOf(1.0, 1.0, Double.MIN_VALUE)
        gbl_contentPanel.rowWeights = doubleArrayOf(0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE)
        contentPanel.setLayout(gbl_contentPanel)
        {
            val btnNewButton = JButton("Pick File")
            val gbc_btnNewButton = GridBagConstraints()
            gbc_btnNewButton.anchor = GridBagConstraints.EAST
            gbc_btnNewButton.insets = Insets(0, 0, 5, 5)
            gbc_btnNewButton.gridx = 0
            gbc_btnNewButton.gridy = 0
            contentPanel.add(btnNewButton, gbc_btnNewButton)
        }
        {
            filenameField = JTextField()
            filenameField.setEditable(false)
            val gbc_filename = GridBagConstraints()
            gbc_filename.insets = Insets(0, 0, 5, 0)
            gbc_filename.fill = GridBagConstraints.HORIZONTAL
            gbc_filename.gridx = 1
            gbc_filename.gridy = 0
            contentPanel.add(filenameField, gbc_filename)
            filenameField.setColumns(10)
        }
        val lblOverwriteExistingVerbatim = JLabel("Existing Verbatim Values:")
        val gbc_lblOverwriteExistingVerbatim = GridBagConstraints()
        gbc_lblOverwriteExistingVerbatim.anchor = GridBagConstraints.EAST
        gbc_lblOverwriteExistingVerbatim.insets = Insets(0, 0, 5, 5)
        gbc_lblOverwriteExistingVerbatim.gridx = 0
        gbc_lblOverwriteExistingVerbatim.gridy = 1
        contentPanel.add(lblOverwriteExistingVerbatim, gbc_lblOverwriteExistingVerbatim)
        val chckbxOvewrite = JCheckBox("Ovewrite")
        val gbc_chckbxOvewrite = GridBagConstraints()
        gbc_chckbxOvewrite.anchor = GridBagConstraints.WEST
        gbc_chckbxOvewrite.insets = Insets(0, 0, 5, 0)
        gbc_chckbxOvewrite.gridx = 1
        gbc_chckbxOvewrite.gridy = 1
        contentPanel.add(chckbxOvewrite, gbc_chckbxOvewrite)
        {
            table = JTable()
            val gbc_table = GridBagConstraints()
            gbc_table.insets = Insets(0, 0, 5, 0)
            gbc_table.gridwidth = 2
            gbc_table.fill = GridBagConstraints.BOTH
            gbc_table.gridx = 0
            gbc_table.gridy = 2
            contentPanel.add(table, gbc_table)
        }
        {
            textFieldOKToLoad = JTextField()
            textFieldOKToLoad.setEditable(false)
            textFieldOKToLoad.setBackground(Color.RED)
            val gbc_textFieldOKToLoad = GridBagConstraints()
            gbc_textFieldOKToLoad.insets = Insets(0, 0, 0, 5)
            gbc_textFieldOKToLoad.fill = GridBagConstraints.HORIZONTAL
            gbc_textFieldOKToLoad.gridx = 0
            gbc_textFieldOKToLoad.gridy = 3
            contentPanel.add(textFieldOKToLoad, gbc_textFieldOKToLoad)
            textFieldOKToLoad.setColumns(10)
        }
        {
            val okButton = JButton("Load Data")
            okButton.setEnabled(false)
            val gbc_okButton = GridBagConstraints()
            gbc_okButton.anchor = GridBagConstraints.WEST
            gbc_okButton.gridx = 1
            gbc_okButton.gridy = 3
            contentPanel.add(okButton, gbc_okButton)
            okButton.setActionCommand("OK")
            getRootPane().setDefaultButton(okButton)
            okButton.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    val scan = JobVerbatimFieldLoad(selectedFile)
                    Thread(scan).start()
                }
            })
        }
        {
            val buttonPane = JPanel()
            buttonPane.setLayout(FlowLayout(FlowLayout.RIGHT))
            getContentPane().add(buttonPane, BorderLayout.SOUTH)
            {
                val cancelButton = JButton("Cancel")
                cancelButton.setActionCommand("Cancel")
                buttonPane.add(cancelButton)
            }
        }
    }
}
