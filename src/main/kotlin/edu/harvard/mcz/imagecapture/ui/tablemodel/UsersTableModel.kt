/**
 * UsersTableModel.java
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


import edu.harvard.mcz.imagecapture.entity.Users
import java.util.*
import javax.swing.table.AbstractTableModel

/**
 * UsersTableModel
 */
class UsersTableModel : AbstractTableModel {
    private var users: MutableList<Users?>? = null

    constructor() {
        users = ArrayList<Users?>()
    }

    /**
     * @param findAll
     */
    constructor(userList: MutableList<Users?>?) {
        users = userList ?: ArrayList<Users?>()
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() = 5

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() = users!!.size

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        var result: Any? = null
        when (columnIndex) {
            0 -> result = users!![rowIndex]
            1 -> result = users!![rowIndex].username
            2 -> result = users!![rowIndex].getFullname()
            3 -> result = users!![rowIndex].getDescription()
            4 -> result = users!![rowIndex].getRole()
        }
        return result
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    override fun getColumnName(column: Int): String {
        var result = ""
        when (column) {
            0 -> result = ""
            1 -> result = "Email"
            2 -> result = "Full Name"
            3 -> result = "About this person"
            4 -> result = "Role"
        }
        return result
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return columnIndex == 0
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) { // TODO Auto-generated method stub
        super.setValueAt(value, rowIndex, columnIndex)
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    override fun getColumnClass(columnIndex: Int): Class<*> {
        return if (columnIndex == 0) {
            Users::class.java
        } else {
            String::class.java
        }
    }

    companion object {
        private const val serialVersionUID = 2713593770437005589L
    }
}
