/**
 * ButtonRenderer.java
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
package edu.harvard.mcz.imagecapture.ui


import java.awt.Component
import javax.swing.JButton
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

/**
 * ButtonRenderer works together with ButonEditor and a TableModel that implements
 * isCellEditable() for relevant cells to produce a clickable button in a cell.
 *
 * @see edu.harvard.mcz.imagecapture.ui.ButtonEditor
 */
class ButtonRenderer : JButton, TableCellRenderer {
    var thisButton: JButton? = null
    private var buttonText: String? = "Edit"

    constructor() {}
    constructor(aButtonText: String?) {
        buttonText = aButtonText
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    override fun getTableCellRendererComponent(table: JTable?, value: Any?,
                                               isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component? {
        if (thisButton == null) { // only need to create one instance of a button for this
// instance of ButtonRenderer.
            thisButton = JButton()
            thisButton.setText(buttonText)
        }
        thisButton.setEnabled(isSelected)
        return thisButton
    }

    companion object {
        private const val serialVersionUID = 7522359895927187088L
    }
}
