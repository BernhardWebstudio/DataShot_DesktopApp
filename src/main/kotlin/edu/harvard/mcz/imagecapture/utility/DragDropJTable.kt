/**
 * DragDropJTable.java
 * edu.harvard.mcz.imagecapture.utility
 *
 *
 * Modified from myTable class at:
 * http://forums.sun.com/thread.jspa?forumID=57&threadID=497102
 *
 *
 */
package edu.harvard.mcz.imagecapture.utility


import edu.harvard.mcz.imagecapture.utility.DragDropJTable
import org.apache.commons.logging.LogFactory
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException
import java.util.*
import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.TransferHandler
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableColumnModel
import javax.swing.table.TableModel

/**
 * DragDropJTable: a JTable that supports drag and drop
 */
class DragDropJTable : JTable {
    var thisTable: DragDropJTable? = null

    constructor(model: TableModel?) : super(model) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor(width: Int, height: Int) : super(width, height) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor() : super() {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor(rowData: Array<Array<Any?>?>?, columnNames: Array<Any?>?) : super(rowData, columnNames) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor(dm: TableModel?, cm: TableColumnModel?,
                sm: ListSelectionModel?) : super(dm, cm, sm) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor(dm: TableModel?, cm: TableColumnModel?) : super(dm, cm) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    constructor(rowData: Vector<*>?, columnNames: Vector<*>?) : super(rowData, columnNames) {
        setTransferHandler(DdTransferHandler())
        setDragEnabled(true)
        thisTable = this
    }

    private fun getStringArray(inStr: String, ctkn: Char): Array<String?>? {
        val x: Array<String?>
        if (inStr.length == 0) {
            x = arrayOfNulls<String?>(1)
            x[0] = ""
            return x
        }
        var i = 0
        var tmp = ""
        val AL = ArrayList<String?>(20)
        while (i < inStr.length) {
            if (inStr[i] == ctkn) {
                AL.add(tmp)
                tmp = ""
            } else tmp += inStr[i]
            i++
        }
        AL.add(tmp)
        x = arrayOfNulls<String?>(AL.size)
        i = 0
        while (i < AL.size) {
            x[i] = AL[i]
            i++
        }
        return x
    }

    /**
     * Convert the selection to tab delimited cells and
     * newline delimited rows.
     *
     * @return
     */
    val transferContents: StringSelection
        get() {
            val br: Int = getSelectedRow()
            val bc: Int = getSelectedColumn()
            val er: Int = br + getSelectedRowCount()
            val ec: Int = bc + getSelectedColumnCount()
            var out: String? = ""
            for (row in br until er) {
                for (col in bc until ec) {
                    out += getValueAt(row, col)
                    if (col + 1 < ec) out += "\t"
                }
                if (er - br > 1) out += "\n"
            }
            return StringSelection(out)
        }

    /**
     * DdTransferHandler Custom Drag and drop transfer handler.
     */
    internal inner class DdTransferHandler : TransferHandler() {
        override fun getSourceActions(c: JComponent?): Int {
            return TransferHandler.COPY_OR_MOVE
        }

        override fun 
import can(support: TransferSupport): Boolean { // Only import Strings
            return support.isDataFlavorSupported(DataFlavor.stringFlavor)
        }

        override fun createTransferable(comp: JComponent?): Transferable? {
            return transferContents
        }

        override fun exportDone(c: JComponent?, contents: Transferable?, action: Int) {}
        override fun importData(support: TransferSupport): Boolean {
            try {
                log!!.debug(support.Transferable.getTransferData(DataFlavor.stringFlavor))
            } catch (e1: UnsupportedFlavorException) {
                log!!.error(e1)
            } catch (e1: IOException) {
                log!!.error(e1)
            }
            if (
import can(support)) {
                var targetRow: Int = getSelectedRow()
                var targetCol: Int = getSelectedColumn()
                if (support.isDrop()) { // If this is a drop action (rather than a paste), then the
// selected row/column will be the source, not the destination.
                    val dl: JTable.DropLocation = support.DropLocation as JTable.DropLocation
                    targetRow = dl.Row
                    targetCol = dl.Column
                }
                try {
                    val line = (support.Transferable.getTransferData(DataFlavor.stringFlavor) as String)
                    var start = 0
                    var end = line.indexOf("\n")
                    if (end < 0) {
                        end = if (line.indexOf('\t') < 0) { // Single cell
                            setValueAt(line, targetRow, targetCol)
                            return true
                        } else {
                            line.length
                        }
                    }
                    var dropCells: Array<String?>?
                    while (end <= line.length) { // More than one cell
                        dropCells = getStringArray(line.substring(start, end), '\t')
                        for (j in dropCells!!.indices) {
                            setValueAt(dropCells[j], targetRow, targetCol + j)
                        }
                        targetRow++
                        start = end + 1
                        if (start >= line.length) {
                            break
                        }
                        end = line.substring(start).indexOf("\n")
                        if (end >= 0) {
                            end += start
                        } else {
                            end = line.length
                        }
                    }
                    if (isEditing()) {
                        getCellEditor().stopCellEditing()
                    }
                    (getModel() as AbstractTableModel?).fireTableDataChanged()
                    repaint()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    return false
                }
            } else { // Only import strings
                return false
            }
            return true
        }
    }

    companion object {
        private const val serialVersionUID = -8855350581096969272L
        private val log = LogFactory.getLog(DragDropJTable::class.java)
    }
}
