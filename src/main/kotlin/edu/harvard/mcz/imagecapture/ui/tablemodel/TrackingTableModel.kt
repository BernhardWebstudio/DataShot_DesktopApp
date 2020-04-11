/**
 *
 */
package edu.harvard.mcz.imagecapture.ui.tablemodel


import edu.harvard.mcz.imagecapture.entity.Tracking
import java.util.*
import javax.swing.table.AbstractTableModel

/** Table model for displaying Tracking records in a JTable.
 *
 *
 *
 */
class TrackingTableModel : AbstractTableModel {
    private var events: MutableList<Tracking?>? = null

    constructor() {
        events = ArrayList<Tracking?>()
    }

    constructor(aTrackingSet: MutableList<Tracking?>?) {
        events = aTrackingSet
    }

    fun setTrackings(aTrackingSet: MutableList<Tracking?>?) {
        events = aTrackingSet
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() = 4

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() = events!!.size

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    override fun getColumnName(columnIndex: Int): String? {
        var returnvalue: String? = null
        when (columnIndex) {
            0 -> returnvalue = "Specimen"
            1 -> returnvalue = "User"
            2 -> returnvalue = "Event"
            3 -> returnvalue = "Timestamp"
        }
        return returnvalue
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        var returnvalue: String? = null
        when (columnIndex) {
            0 -> returnvalue = events!![rowIndex].getSpecimen().getBarcode()
            1 -> returnvalue = events!![rowIndex].getUser()
            2 -> returnvalue = events!![rowIndex].getEventType()
            3 -> returnvalue = events!![rowIndex].getEventDateTime().toString()
        }
        return returnvalue
    }

    companion object {
        private const val serialVersionUID = -391531601684450925L
    }
}
