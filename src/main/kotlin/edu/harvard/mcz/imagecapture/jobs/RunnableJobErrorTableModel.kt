/**
 * RunnableJobErrorTableModel.java
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
package edu.harvard.mcz.imagecapture.jobs


import javax.swing.table.AbstractTableModel

/**
 * RunnableJobErrorTableModel, table model for displaying error reports about preprocessing images.
 * Can handle different types of reports by specifying a TYPE_ constant in the constructor.
 */
class RunnableJobErrorTableModel : AbstractTableModel {
    private var errors: MutableList<RunnableJobError?>?
    private var type: Int

    constructor(errorList: MutableList<RunnableJobError?>?) {
        errors = errorList
        type = TYPE_PREPROCESS
    }

    constructor(errorList: MutableList<RunnableJobError?>?, listType: Int) {
        errors = errorList
        type = TYPE_PREPROCESS
        if (listType == TYPE_MISSING_BARCODES) {
            type = TYPE_MISSING_BARCODES
        }
        if (listType == TYPE_LOAD) {
            type = TYPE_LOAD
        }
        if (listType == TYPE_FILE_RECONCILIATION) {
            type = TYPE_FILE_RECONCILIATION
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() {
            var result = 10
            when (type) {
                TYPE_PREPROCESS -> result = 9
                TYPE_MISSING_BARCODES -> result = 4
                TYPE_LOAD -> result = 6
                TYPE_FILE_RECONCILIATION -> result = 5
            }
            return result
        }// TODO Auto-generated method stub

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() =// TODO Auto-generated method stub
            errors!!.size

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val error: RunnableJobError? = errors!![rowIndex]
        var result: Any? = null
        when (type) {
            TYPE_PREPROCESS -> when (columnIndex) {
                0 -> result = error!!.FailureType
                1 -> result = error.Filename
                2 -> result = error.Barcode
                3 -> result = error.QrBarcode
                4 -> result = error.CommentBarcode
                5 -> result = error.ErrorMessage
                6 -> result = if (error.Exception != null) {
                    error.Exception.message
                } else {
                    ""
                }
                7 -> result = if (error!!.DrawerParser != null) {
                    error!!.DrawerParser.DrawerNumber
                } else {
                    ""
                }
                8 -> result = if (error!!.TaxonParser != null) {
                    error!!.TaxonParser.Family
                } else {
                    ""
                }
            }
            TYPE_MISSING_BARCODES -> when (columnIndex) {
                0 -> result = error!!.FailureType
                1 -> result = error.Barcode
                2 -> result = error.Previous
                3 -> result = error.PreviousPath
            }
            TYPE_LOAD -> when (columnIndex) {
                0 -> result = error!!.FailureType
                1 -> result = error.Filename
                2 -> result = error.LineNumber
                3 -> result = error.Barcode
                4 -> result = error.ErrorMessage
                5 -> result = if (error.Exception != null) {
                    error.Exception.message
                } else {
                    ""
                }
            }
            TYPE_FILE_RECONCILIATION -> when (columnIndex) {
                0 -> result = error!!.FailureType
                1 -> result = error.QrBarcode // overloaded, path to file
                2 -> result = error.Filename
                3 -> result = error.Barcode
                4 -> result = error.ErrorMessage
            }
        }
        return result
    }

    override fun getColumnName(columnIndex: Int): String {
        var result = ""
        when (type) {
            TYPE_PREPROCESS -> when (columnIndex) {
                0 -> result = "Type"
                1 -> result = "Filename"
                2 -> result = "Barcode"
                3 -> result = "QR Barcode"
                4 -> result = "Comment Barcode"
                5 -> result = "Error"
                6 -> result = "Exception"
                7 -> result = "Drawer"
                8 -> result = "Family"
            }
            TYPE_MISSING_BARCODES -> when (columnIndex) {
                0 -> result = "Type"
                1 -> result = "Barcode"
                2 -> result = "Previous File"
                3 -> result = "Path"
            }
            TYPE_FILE_RECONCILIATION -> when (columnIndex) {
                0 -> result = "Type"
                1 -> result = "Path"
                2 -> result = "Filename"
                3 -> result = "Barcode"
                4 -> result = "Error"
            }
            TYPE_LOAD -> when (columnIndex) {
                0 -> result = "Type"
                1 -> result = "Filename"
                2 -> result = "Linenumber"
                3 -> result = "Barcode"
                4 -> result = "Exception"
                5 -> result = "Error Message"
            }
        }
        return result
    }

    companion object {
        /**
         * Constant for reporting on errors in preprocessing.
         */
        const val TYPE_PREPROCESS = 0
        /**
         * Constant for reporting on errors in rechecking for missing barcodes.
         */
        const val TYPE_MISSING_BARCODES = 1
        /**
         * Constant for reporting on errors in loading data.
         */
        const val TYPE_LOAD = 2
        const val TYPE_FILE_RECONCILIATION = 3
        private const val serialVersionUID = 3407074726845800411L
    }
}
