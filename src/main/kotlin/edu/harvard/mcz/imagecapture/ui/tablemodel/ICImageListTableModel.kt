/**
 * ICImageListTableModel.java
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


import edu.harvard.mcz.imagecapture.entity.ICImage
import edu.harvard.mcz.imagecapture.entity.Specimen
import javax.swing.table.AbstractTableModel

/**
 * ICImageListTableModel model to display image metadata in a table.
 */
class ICImageListTableModel(anImageList: MutableList<ICImage?>?) : AbstractTableModel() {
    private var images: MutableList<ICImage?>? = null// TODO Auto-generated method stub
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    val columnCount: Int
        get() =// TODO Auto-generated method stub
            9

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    val rowCount: Int
        get() = images!!.size

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val image: ICImage? = images!![rowIndex]
        var result: Any? = null
        when (columnIndex) {
            COL_ID -> if (image.Specimen != null) { // result = image.Specimen.SpecimenId;
                result = image.Specimen
            } else { // Kludge - work around for images not bound to specimens.
// result = -1L;
                result = Specimen()
            }
            COL_PATH -> result = image.Path
            COL_FILENAME -> result = image.Filename
            3 -> result = image.RawBarcode
            4 -> result = image.RawExifBarcode
            5 -> result = image.RawOcr
            COL_BARCODE -> result = if (image.Specimen != null) {
                image.Specimen.Barcode
            } else {
                ""
            }
            7 -> result = image.TemplateId
            COL_DRAWER -> result = image.DrawerNumber
        }
        return result
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    override fun getColumnName(columnIndex: Int): String? {
        var result: String? = null
        when (columnIndex) {
            COL_ID -> result = ""
            COL_PATH -> result = "Path"
            COL_FILENAME -> result = "Filename"
            3 -> result = "Barcode (ocr)"
            4 -> result = "Barcode (scan)"
            5 -> result = "Raw OCR"
            COL_BARCODE -> result = "Barcode"
            7 -> result = "Template"
            COL_DRAWER -> result = "Drawer#"
        }
        return result
    }

    /**
     * Must be implemented for ButtonEditor to work.  Needs to return Long for
     * ID column that is to contain button to work with ButtonEditor.
     */
    override fun getColumnClass(c: Int): Class<*>? { // Given current implementation of button in SpecimenBrowser,
// needs to return Long for ID column that is to contain button
// and ** Must Not ** return Long for any other column).
        var result: Class<*> = String::class.java // Default value to return when table is empty.
        try {
            result = getValueAt(0, c).javaClass
        } catch (e: NullPointerException) { // continue
        }
        return result
    }

    /**
     * Must be implemented for ButtonEditor to work.
     *
     * @see javax.swing.table.AbstractTableModel.isCellEditable
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        var result = false
        if (columnIndex == COL_ID) {
            result = true
        }
        return result
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int,
     *     int)
     */
    override fun setValueAt(value: Any?, rowIndex: Int, columnIndex: Int) {
        super.setValueAt(value, rowIndex, columnIndex)
        // don't do anything particular, edit is only to enable a buttonEditor as a
// renderer that can hear mouse clicks.
    }

    companion object {
        const val COL_ID = 0
        const val COL_PATH = 1
        const val COL_FILENAME = 2
        const val COL_BARCODE = 6
        const val COL_DRAWER = 8
        private const val serialVersionUID = -1122512383053371L
    }

    //	public ICImageListTableModel() {
//		images = new ArrayList<ICImage>();
//	}
    init {
        images = anImageList
    }
}
