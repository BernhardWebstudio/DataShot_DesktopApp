/**
 * SpecimenPartsTableModel
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
package edu.harvard.mcz.imagecapture.ui.tablemodel


import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycle
import java.util.*
import javax.swing.table.AbstractTableModel

/**
 *
 */
class SpecimenPartsAttrTableModel : AbstractTableModel {
    private var specimenPartAttributes: MutableCollection<SpecimenPartAttribute?>?

    constructor() : super() {
        specimenPartAttributes = HashSet<SpecimenPartAttribute?>()
    }

    /**
     * @param specimenParts
     */
    constructor(
            specimenPartAttributes: MutableCollection<SpecimenPartAttribute?>?) : super() {
        this.specimenPartAttributes = specimenPartAttributes
    }// TODO Auto-generated method stub

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() =// TODO Auto-generated method stub
            specimenPartAttributes!!.size// TODO Auto-generated method stub

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() =// TODO Auto-generated method stub
            4

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    override fun getColumnName(columnIndex: Int): String? {
        var returnvalue: String? = null
        when (columnIndex) {
            0 -> returnvalue = "Type"
            1 -> returnvalue = "Value"
            2 -> returnvalue = "Units"
            3 -> returnvalue = "Remarks"
        }
        return returnvalue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        var result: Any? = null
        when (columnIndex) {
            0 -> result = (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .AttributeType
            1 -> result = (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .AttributeValue
            2 -> result = (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .AttributeUnits
            3 -> result = (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .AttributeRemark
        }
        return result
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
     *     int)
     */
    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        when (columnIndex) {
            0 -> (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .setAttributeType(aValue as String?)
            1 -> (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .setAttributeValue(aValue as String?)
            2 -> (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .setAttributeUnits(aValue as String?)
            3 -> (specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute)
                    .setAttributeRemark(aValue as String?)
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return false
    }

    /**
     * @param rowIndex row to be deleted
     */
    fun deleteRow(rowIndex: Int) {
        val toRemove: SpecimenPartAttribute = specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute
        val spals = SpecimenPartAttributeLifeCycle()
        try {
            spals.remove(toRemove)
            specimenPartAttributes!!.remove(toRemove)
            fireTableDataChanged()
        } catch (e: SaveFailedException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun getRowObject(rowIndex: Int): SpecimenPartAttribute {
        return specimenPartAttributes!!.toTypedArray().get(rowIndex) as SpecimenPartAttribute
    }

    companion object {
        private const val serialVersionUID = -4139892019645663114L
    }
}
