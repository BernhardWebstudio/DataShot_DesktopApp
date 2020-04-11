package edu.harvard.mcz.imagecapture.ui.component


import edu.harvard.mcz.imagecapture.ui.DataShotColors
import edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModel
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import java.awt.Component
import java.awt.event.*
import java.util.*
import javax.swing.*
import javax.swing.border.MatteBorder
import javax.swing.table.TableCellRenderer

/**
 * Default JTable used for tables in SpecimenDetailsViewPane
 */
class JTableWithRowBorder(tableModel: AbstractDeleteableTableModel?) : JTable(tableModel) {
    protected var deletableRows = false
    protected var clickedRow = 0
    protected var objectName: String? = null
    protected var parentPane: JPanel? = null
    protected var listeners: ArrayList<ActionListener?> = ArrayList<ActionListener?>()
    /**
     * Add listener to listen for any events changing the content of this table
     *
     * @param listener
     */
    fun addListener(listener: ActionListener?) {
        listeners.add(listener)
    }

    fun removeListener(listener: ActionListener?) {
        listeners.remove(listener)
    }

    /**
     * Set the name of the entity in the table
     *
     * @param name the entity name
     */
    fun setObjectName(name: String?) {
        objectName = name
    }

    /**
     * Set the pane in which to display notification etc.
     *
     * @param parentPane
     */
    fun setParentPane(parentPane: JPanel?) {
        this.parentPane = parentPane
    }

    /**
     * Enable the option to delete rows.
     * Note that only the events which lead to data change are emitted.
     */
    fun enableDeleteability() {
        if (deletableRows == false) {
            deletableRows = true
            val jPopupDeletor = JPopupMenu()
            val menuItemDeleteRow = JMenuItem("Delete Row")
            val tableWithRowBorder = this
            menuItemDeleteRow.addActionListener(object : ActionListener {
                override fun actionPerformed(e: ActionEvent?) {
                    try {
                        if (clickedRow >= 0) {
                            val ok: Int = JOptionPane.showConfirmDialog(parentPane, "Delete the selected $objectName?", "Delete $objectName", JOptionPane.OK_CANCEL_OPTION)
                            if (ok == JOptionPane.OK_OPTION) {
                                log.debug("deleting " + objectName + "s row " + clickedRow)
                                (tableWithRowBorder.Model as AbstractDeleteableTableModel?).deleteRow(clickedRow)
                                tableWithRowBorder.emitEvent(e)
                            } else {
                                log.debug("$objectName row delete canceled by user.")
                            }
                        } else {
                            JOptionPane.showMessageDialog(parentPane, "Unable to select row to delete.  Try empting the value and pressing Save.")
                        }
                    } catch (ex: Exception) {
                        log.error(ex.message)
                        JOptionPane.showMessageDialog(parentPane, "Failed to delete a " + objectName + " row. " + ex.message)
                    }
                }
            })
            jPopupDeletor.add(menuItemDeleteRow)
            this.addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    if (e.isPopupTrigger()) {
                        clickedRow = (e.Component as JTable).SelectedRow
                        jPopupDeletor.show(e.Component, e.X, e.Y)
                    }
                }

                override fun mouseReleased(e: MouseEvent) {
                    if (e.isPopupTrigger()) {
                        clickedRow = (e.Component as JTable).SelectedRow
                        jPopupDeletor.show(e.Component, e.X, e.Y)
                    }
                }
            })
            this.addKeyListener(object : KeyAdapter() {
                override fun keyTyped(e: KeyEvent) {
                    tableWithRowBorder.emitEvent(ActionEvent(e, e.ID, e.toString()))
                }
            })
        }
    }

    override fun prepareRenderer(
            renderer: TableCellRenderer, row: Int, column: Int): Component? {
        val c: Component = super.prepareRenderer(renderer, row, column)
        val jc: JComponent = c as JComponent
        //  Color row based on a cell value
//  Alternate row color
        if (!isRowSelected(row)) {
            c.setBackground(if (row % 2 == 0) getBackground() else DataShotColors.VERY_LIGHT_GRAY)
        }
        jc.setBorder(MatteBorder(0, 0, 1, 0, DataShotColors.VERY_DARK_GRAY))
        //  Use bold font on selected row
        return c
    }

    /**
     * @param event
     */
    protected fun emitEvent(event: ActionEvent?) {
        for (listener in listeners) {
            listener.actionPerformed(event)
        }
    }

    companion object {
        private val log: Log = LogFactory.getLog(JTableWithRowBorder::class.java)
    }

    init {
        this.setShowGrid(true)
    }
}
