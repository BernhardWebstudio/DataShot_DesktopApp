/**
 * ValidatingTableCellEditor.java
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


import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor
import java.awt.Component
import javax.swing.JComboBox
import javax.swing.JTable

/**
 * ValidatingTableCellEditor provides a JTextField with an inputVerifier as a
 * cell editor that can be used as an editor for cells in a JTable.
 *
 *
 * Example:
 * <pre>
 * JTextField field = new JTextField();
 * field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
 * jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ValidatingTableCellEditor(field));
</pre> *
 */
class PicklistTableCellEditor : ComboBoxCellEditor {
    private var field: JComboBox<*>?

    /**
     * Constructor
     *
     * @param textField JComboBox to support the picklist
     */
    constructor(textField: JComboBox<*>) : super(textField) {
        field = textField
    }

    /**
     * Constructor
     *
     * @param textField JComboBox to support the picklist
     * @param editable  value to set for editable of the jcombobox
     */
    constructor(textField: JComboBox<*>, editable: Boolean) : super(textField) {
        field = textField
        field.setEditable(editable)
    }

    val cellEditorValue: Any?
        get() = field.getSelectedItem()

    //	/* (non-Javadoc)
//	 * @see javax.swing.DefaultCellEditor#cancelCellEditing()
//	 */
//	@Override
//	public void cancelCellEditing() {
//		if (field.isValid()) {
//			field.setBackground(MainFrame.BG_COLOR_ENT_FIELD);
//		   super.cancelCellEditing();
//		} else {
//			field.setBackground(MainFrame.BG_COLOR_ERROR);
//		}
//	}
/* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#stopCellEditing()
     */
    override fun stopCellEditing(): Boolean {
        return super.stopCellEditing()
    }

    /* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    override fun getTableCellEditorComponent(table: JTable?, value: Any?,
                                             isSelected: Boolean, row: Int, column: Int): Component? {
        if (value != null) {
            field.setSelectedItem(value)
        } else {
            field.setSelectedItem("")
        }
        return field
    }

    companion object {
        private const val serialVersionUID = -4777010317672887845L
    }
}
