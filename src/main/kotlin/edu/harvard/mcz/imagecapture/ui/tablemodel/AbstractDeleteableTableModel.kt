package edu.harvard.mcz.imagecapture.ui.tablemodel


import javax.swing.table.AbstractTableModel

abstract class AbstractDeleteableTableModel : AbstractTableModel() {
    abstract fun deleteRow(rowIndex: Int)
}
