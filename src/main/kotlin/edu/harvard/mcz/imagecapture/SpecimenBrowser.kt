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


import edu.harvard.mcz.imagecapture.SpecimenBrowser
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus
import edu.harvard.mcz.imagecapture.interfaces.DataChangeListener
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.ButtonEditor
import edu.harvard.mcz.imagecapture.ui.ButtonRenderer
import edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditor
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel
import org.apache.commons.logging.LogFactory
import org.hibernate.SessionException
import org.hibernate.TransactionException
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.*
import java.util.regex.PatternSyntaxException
import javax.swing.*
import javax.swing.table.TableRowSorter

/**
 * SpecimenBrowser is a Searchable, Sortable, tabular view of multiple
 * specimens.
 */
class SpecimenBrowser : JPanel, DataChangeListener {
    private var jScrollPane: JScrollPane? = null
    private var jTable: JTable? = null
    private var jPanel: JPanel? = null
    private var jLabel: JLabel? = null
    private var jTextField: JTextField? = null
    private var jComboBox: JComboBox<*>? = null
    private var jLabel1: JLabel? = null
    private var sorter: TableRowSorter<SpecimenListTableModel?>? = null
    private var jLabel2: JLabel? = null
    private var jTextFieldFamily: JTextField? = null
    private var jLabel3: JLabel? = null
    private var jTextFieldDrawerNumber: JTextField? = null
    private var searchCriteria: Specimen? = null
    private var useLike = false
    private var maxResults = 0
    private var offset = 0

    /**
     * This method initializes an instance of SpecimenBrowser
     */
    constructor() : super() {
        searchCriteria = null
        useLike = false
        initialize()
    }

    @JvmOverloads
    constructor(specimenSearchCriteria: Specimen?, like: Boolean = false, maxResults: Int = 0, offset: Int = 0) : super() {
        searchCriteria = specimenSearchCriteria
        useLike = like
        this.offset = offset
        this.maxResults = maxResults
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
            try {
                jScrollPane.setViewportView(getJTable())
            } catch (e: SessionException) {
                log!!.debug(e.message, e)
                Singleton.getMainFrame().setStatusMessage(
                        "Database Connection Error.")
                HibernateUtil.terminateSessionFactory()
                this.setVisible(false)
            } catch (e: TransactionException) {
                log!!.debug(e.message, e)
                Singleton.getMainFrame().setStatusMessage(
                        "Database Connection Error.")
                HibernateUtil.terminateSessionFactory()
                this.setVisible(false)
            }
            jScrollPane.setPreferredSize(Dimension(444, 290))
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
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS)
            // jTable.setAutoCreateRowSorter(true);
            val s = SpecimenLifeCycle()
            var model: SpecimenListTableModel? = null
            if (searchCriteria == null) {
                model = SpecimenListTableModel(s.findAll())
            } else {
                if (useLike) {
                    model = SpecimenListTableModel(s.findByExampleLike(searchCriteria, maxResults, offset))
                } else {
                    model = SpecimenListTableModel(s.findByExample(searchCriteria, maxResults, offset))
                }
            }
            jTable.setModel(model)
            sorter = TableRowSorter<SpecimenListTableModel?>(model)
            jTable.setRowSorter(sorter)
            jTable.setDefaultRenderer(Specimen::class.java, ButtonRenderer())
            jTable.setDefaultEditor(Specimen::class.java, ButtonEditor())
            jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.Companion.COL_COPY)).setCellRenderer(ButtonRenderer("Copy"))
            jTable.getColumn(jTable.getColumnName(SpecimenListTableModel.Companion.COL_COPY)).setCellEditor(CopyRowButtonEditor(JCheckBox()))
            // set some column widths
            val characterWidth: Int = Singleton.getCharacterWidth()
            jTable.getColumnModel().getColumn(0).setPreferredWidth(characterWidth *
                    3)
            jTable.getColumnModel().getColumn(1).setPreferredWidth(characterWidth *
                    3)
            jTable.getColumnModel().getColumn(2).setPreferredWidth(characterWidth *
                    14)
        }
        return jTable
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private fun getJPanel(): JPanel? {
        if (jPanel == null) {
            val gridBagConstraints4 = GridBagConstraints()
            gridBagConstraints4.fill = GridBagConstraints.BOTH
            gridBagConstraints4.gridy = 0
            gridBagConstraints4.weightx = 1.0
            gridBagConstraints4.gridx = 7
            val gridBagConstraints3 = GridBagConstraints()
            gridBagConstraints3.gridx = 6
            gridBagConstraints3.gridy = 0
            jLabel3 = JLabel()
            jLabel3.setText("Drawer:")
            val gridBagConstraints21 = GridBagConstraints()
            gridBagConstraints21.fill = GridBagConstraints.BOTH
            gridBagConstraints21.gridy = 0
            gridBagConstraints21.weightx = 1.0
            gridBagConstraints21.gridx = 5
            val gridBagConstraints11 = GridBagConstraints()
            gridBagConstraints11.gridx = 4
            gridBagConstraints11.gridy = 0
            jLabel2 = JLabel()
            jLabel2.setText("Family:")
            val gridBagConstraints2 = GridBagConstraints()
            gridBagConstraints2.gridx = 2
            gridBagConstraints2.insets = Insets(0, 5, 0, 0)
            gridBagConstraints2.gridy = 0
            jLabel1 = JLabel()
            jLabel1.setText("Workflow:")
            val gridBagConstraints1 = GridBagConstraints()
            gridBagConstraints1.fill = GridBagConstraints.BOTH
            gridBagConstraints1.gridy = 0
            gridBagConstraints1.weightx = 1.0
            gridBagConstraints1.anchor = GridBagConstraints.WEST
            gridBagConstraints1.gridx = 3
            val gridBagConstraints = GridBagConstraints()
            gridBagConstraints.fill = GridBagConstraints.BOTH
            gridBagConstraints.anchor = GridBagConstraints.WEST
            gridBagConstraints.weightx = 1.0
            jLabel = JLabel()
            jLabel.setText("Find Barcode:")
            jPanel = JPanel()
            jPanel.setLayout(GridBagLayout())
            jPanel.add(jLabel, GridBagConstraints())
            jPanel.add(getJTextField(), gridBagConstraints)
            jPanel.add(getJComboBox(), gridBagConstraints1)
            jPanel.add(jLabel1, gridBagConstraints2)
            jPanel.add(jLabel2, gridBagConstraints11)
            jPanel.add(getJTextFieldFamily(), gridBagConstraints21)
            jPanel.add(jLabel3, gridBagConstraints3)
            jPanel.add(getJTextFieldDrawerNumber(), gridBagConstraints4)
        }
        return jPanel
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextField(): JTextField? {
        if (jTextField == null) {
            jTextField = JTextField()
            jTextField.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    newFilter()
                }
            })
        }
        return jTextField
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private fun getJComboBox(): JComboBox<*>? {
        if (jComboBox == null) {
            jComboBox = JComboBox<Any?>(WorkFlowStatus.getWorkFlowStatusValues())
            jComboBox.addItem("")
            jComboBox.setSelectedItem("")
            jComboBox.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    jTextField.setText("")
                    newFilter()
                }
            })
        }
        return jComboBox
    }

    private fun newFilter() {
        var rf: RowFilter<SpecimenListTableModel?, Any?>? = null
        // If current expression doesn't parse, don't update.
        try {
            var rf_family: RowFilter<SpecimenListTableModel?, Any?>? = null
            var rf_barcode: RowFilter<SpecimenListTableModel?, Any?>? = null
            val rf_drawer: RowFilter<SpecimenListTableModel?, Any?>? = null
            var rf_workflow: RowFilter<SpecimenListTableModel?, Any?>? = null
            rf_family = RowFilter.regexFilter(jTextFieldFamily.getText(),
                    SpecimenListTableModel.Companion.COL_FAMILY)
            rf_barcode = RowFilter.regexFilter(jTextField.getText(),
                    SpecimenListTableModel.Companion.COL_BARCODE)
            // rf_drawer = RowFilter.regexFilter(jTextFieldDrawerNumber.getText(),
// SpecimenListTableModel.COL_DRAWER);
            rf_workflow = RowFilter.regexFilter(jComboBox.getSelectedItem().toString(),
                    SpecimenListTableModel.Companion.COL_WORKFLOW)
            val i: ArrayList<RowFilter<SpecimenListTableModel?, Any?>?> = ArrayList<RowFilter<SpecimenListTableModel?, Any?>?>()
            i.add(rf_family)
            i.add(rf_barcode)
            // i.add(rf_drawer);
            i.add(rf_workflow)
            rf = RowFilter.andFilter(
                    i)
        } catch (e: PatternSyntaxException) {
            return
        }
        sorter.setRowFilter(rf)
    }

    /* (non-Javadoc)
     * @see
     *     edu.harvard.mcz.imagecapture.interfaces.DataChangeListener#notifyDataHasChanged()
     */
    override fun notifyDataHasChanged() {
        (jTable.getModel() as SpecimenListTableModel).fireTableDataChanged()
        log!!.debug("Data change notified.")
    }

    /**
     * This method initializes jTextFieldFamily
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldFamily(): JTextField? {
        if (jTextFieldFamily == null) {
            jTextFieldFamily = JTextField()
            jTextFieldFamily.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent?) {
                    newFilter()
                }
            })
        }
        return jTextFieldFamily
    }

    /**
     * This method initializes jTextFieldDrawerNumber
     *
     * @return javax.swing.JTextField
     */
    private fun getJTextFieldDrawerNumber(): JTextField? {
        if (jTextFieldDrawerNumber == null) {
            jTextFieldDrawerNumber = JTextField()
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
            if (jTable != null) {
                result = jTable.getRowCount()
            }
            return result
        }

    companion object {
        private const val serialVersionUID = 1336228109288304785L
        private val log = LogFactory.getLog(SpecimenBrowser::class.java)
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
