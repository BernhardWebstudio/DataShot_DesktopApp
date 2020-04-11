/**
 * TemplateTableModel.java
 * edu.harvard.mcz.imagecapture.data
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
package edu.harvard.mcz.imagecapture.ui.tablemodel


import edu.harvard.mcz.imagecapture.PositionTemplate
import java.util.*
import javax.swing.table.AbstractTableModel

/**
 * TemplateTableModel
 */
class PositionTemplateTableModel : AbstractTableModel {
    private var templates: MutableList<PositionTemplate?>? = null

    /**
     * Default constructor with an initially empty set of templates.
     */
    constructor() {
        templates = ArrayList<PositionTemplate?>()
    }

    /**
     * Constructor with an initial set of templates.
     */
    constructor(aTemplateSet: MutableList<PositionTemplate?>?) {
        templates = aTemplateSet
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() = 3

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() = templates!!.size

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        var returnvalue: Any? = null
        when (columnIndex) {
            0 -> returnvalue = (templates!!.toTypedArray().get(rowIndex) as PositionTemplate).getTemplateId()
            1 -> returnvalue = (templates!!.toTypedArray().get(rowIndex) as PositionTemplate).getTemplateId()
            2 -> returnvalue = (templates!!.toTypedArray().get(rowIndex) as PositionTemplate).getName()
        }
        return returnvalue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    override fun getColumnName(columnIndex: Int): String? {
        var returnvalue: String? = null
        when (columnIndex) {
            0 -> returnvalue = ""
            1 -> returnvalue = "TemplateId"
            2 -> returnvalue = "Name"
        }
        return returnvalue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        var result = false
        if (columnIndex == 0) {
            result = true
        }
        return result
    }

    companion object {
        private const val serialVersionUID = -6485594139102438531L
    }
}
