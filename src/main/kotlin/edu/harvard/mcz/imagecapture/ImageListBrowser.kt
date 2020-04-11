/**
 * SpecimenBrowser.java
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


import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.lifecycle.ICImageLifeCycle
import edu.harvard.mcz.imagecapture.ui.ButtonEditor
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer
import edu.harvard.mcz.imagecapture.ui.tablemodel.ICImageListTableModel
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import java.util.regex.PatternSyntaxException
import javax.swing.*
import javax.swing.table.TableColumn
import javax.swing.table.TableRowSorter

/**
 * ImageListBrowser is a Searchable, Sortable, tabular view of multiple specimen images with Edit buttons
 * to display the details for a specimen found in an image.
 */
class ImageListBrowser : JPanel {
    private var showJustMissmatches = false
    private var jScrollPane: JScrollPane? = null
    private var jTableImages: JTable? = null
    private var jPanel: JPanel? = null
    private var jLabel: JLabel? = null
    private var jTextFieldFilename: JTextField? = null
    private var jComboBox: JComboBox<*>? = null
    private var jLabel1: JLabel? = null
    private var sorter: TableRowSorter<ICImageListTableModel?>? = null
    private var jTextFieldBarcode: JTextField? = null
    private var jLabel2: JLabel? = null
    private var jLabel3: JLabel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var searchCriteria: ICImage? = null

    /**
     * This method initializes an instance of SpecimenBrowser
     */
    constructor() : super() {
        showJustMissmatches = false
        searchCriteria = null
        initialize()
    }

    constructor(onlyShowMissmatches: Boolean) : super() {
        showJustMissmatches = onlyShowMissmatches
        searchCriteria = null
        initialize()
    }

    constructor(imageSearchCriteria: ICImage?) : super() {
        showJustMissmatches = false
        searchCriteria = imageSearchCriteria
        initialize()
    }

    /**
     * This method initializes this
     */
    private fun initialize() {
        this.setLayout(BorderLayout())
        this.setSize(Dimension(444, 290))
        this.add(getJScrollPane(), BorderLayout.CENTER)
        this.add(getJPanel(), BorderLayout.NORTH)
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private fun getJScrollPane(): JScrollPane? {
        if (jScrollPane == null) {
            jScrollPane = JScrollPane()
            jScrollPane.setViewportView(jTable)
            jScrollPane.setPreferredSize(Dimension(444, 290))
        }
        return jScrollPane
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private val jTable: JTable?
        private get() {
            if (jTableImages == null) {
                jTableImages = JTable()
                val ilc = ICImageLifeCycle()
                var model: ICImageListTableModel? = null
                if (showJustMissmatches) {
                    model = ICImageListTableModel(ICImageLifeCycle.Companion.findMismatchedImages())
                } else {
                    if (searchCriteria == null) {
                        model = ICImageListTableModel(ilc.findAll())
                    } else {
                        model = ICImageListTableModel(ilc.findByExample(searchCriteria))
                    }
                }
                if (model != null) {
                    jTableImages.setModel(model)
                    sorter = TableRowSorter<ICImageListTableModel?>(model)
                    jTableImages.setRowSorter(sorter)
                }
                jTableImages.setDefaultRenderer(Specimen::class.java, ButtonRenderer())
                jTableImages.setDefaultEditor(Specimen::class.java, ButtonEditor())
            }
            return jTableImages
        }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints22 = GridBagConstraints()
            gridBagConstraints22.fill = GridBagConstraints.BOTH
            gridBagConstraints22.gridy = 0
            gridBagConstraints22.weightx = 1.0
            gridBagConstraints22.anchor = GridBagConstraints.WEST
            gridBagConstraints22.gridx = 5
            val gridBagConstraints12 = GridBagConstraints()
            gridBagConstraints12.gridx = 4
            gridBagConstraints12.gridy = 0
            jLabel3 = JLabel()
            jLabel3.setText("Drawer:")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.gridx = 2
            gridBagConstraints21.gridy = 0
            jLabel2 = JLabel()
            jLabel2.setText("Barcode:")
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.fill = GridBagConstraints.BOTH
            gridBagConstraints11.gridy = 0
            gridBagConstraints11.weightx = 1.0
            gridBagConstraints11.anchor = GridBagConstraints.WEST
            gridBagConstraints11.gridx = 3
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 6
            gridBagConstraints2.insets = Insets(0, 5, 0, 0)
            gridBagConstraints2.gridy = 0
            jLabel1 = JLabel()
            jLabel1.setText("Path:")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.gridx = 7
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.fill = GridBagConstraints.BOTH
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.weightx = 1.0
            jLabel = JLabel()
            jLabel.setText("Find Filename:")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, GridBagConstraints())
            jPanel.add(jTextField, gridBagConstraints)
            jPanel.add(getJComboBox(), gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(jTextField2, gridBagConstraints11)
            jPanel.add(jLabel2, gridBagConstraints21)
            jPanel.add(jLabel3, gridBagConstraints12)
            jPanel.add(jTextField3, gridBagConstraints22)
        }
        return jPanel
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField: JTextField?
        private get() {
            if (jTextFieldFilename == null) {
                jTextFieldFilename = JTextField()
                jTextFieldFilename.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        newFilter()
                    }
                })
            }
            return jTextFieldFilename
        }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBox(): JComboBox<*>? {
        if (jComboBox == null) {
            val ils = ICImageLifeCycle()
            jComboBox = JComboBox<Any?>(ils.DistinctPaths)
            jComboBox.setEditable(true)
            val pathColumn: TableColumn = jTableImages.ColumnModel.getColumn(ICImageListTableModel.Companion.COL_PATH)
            pathColumn.cellEditor = DefaultCellEditor(jComboBox)
            jComboBox.addItem("")
            jComboBox.setSelectedItem("")
            jComboBox.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    jTextFieldFilename.setText("")
                    newFilter()
                }
            })
        }
        return jComboBox
    }

    private fun newFilter() {
        var rf: RowFilter<ICImageListTableModel?, Any?>? = null
        //If current expression doesn't parse, don't update.
        try {
            if (jTextFieldFilename.Text == "" && jTextFieldBarcode.Text == "" && jTextFieldDrawerNumber.Text == "") {
                rf = RowFilter.regexFilter(jComboBox.SelectedItem.toString(), ICImageListTableModel.Companion.COL_PATH)
            } else {
                var rf_filename: RowFilter<ICImageListTableModel?, Any?>? = null
                var rf_barcode: RowFilter<ICImageListTableModel?, Any?>? = null
                var rf_drawer: RowFilter<ICImageListTableModel?, Any?>? = null
                rf_filename = RowFilter.regexFilter(jTextFieldFilename.Text, ICImageListTableModel.Companion.COL_FILENAME)
                rf_barcode = RowFilter.regexFilter(jTextFieldBarcode.Text, ICImageListTableModel.Companion.COL_BARCODE)
                rf_drawer = RowFilter.regexFilter(jTextFieldDrawerNumber.Text, ICImageListTableModel.Companion.COL_DRAWER)
                val i: ArrayList<RowFilter<ICImageListTableModel?, Any?>?> = ArrayList<RowFilter<ICImageListTableModel?, Any?>?>()
                i.add(rf_filename)
                i.add(rf_barcode)
                i.add(rf_drawer)
                rf = RowFilter.andFilter(i)
            }
        } catch (e: PatternSyntaxException) {
            return
        }
        sorter.setRowFilter(rf)
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField2: JTextField?
        private get() {
            if (jTextFieldBarcode == null) {
                jTextFieldBarcode = JTextField()
                jTextFieldBarcode.setText("")
                jTextFieldBarcode.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        newFilter()
                    }
                })
            }
            return jTextFieldBarcode
        }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private val jTextField3: JTextField?
        private get() {
            if (jTextFieldDrawerNumber == null) {
                jTextFieldDrawerNumber = JTextField()
                jTextFieldDrawerNumber.setText("")
                jTextFieldDrawerNumber.addKeyListener(object : KeyAdapter() {
                    override fun keyTyped(e: KeyEvent?) {
                        newFilter()
                    }
                })
            }
            return jTextFieldDrawerNumber
        }

    val rowCount: Int
        get() {
            var result = 0
            if (jTableImages != null) {
                result = jTableImages.RowCount
            }
            return result
        }

    companion object {
        private const val serialVersionUID = 1336228109288304785L
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
