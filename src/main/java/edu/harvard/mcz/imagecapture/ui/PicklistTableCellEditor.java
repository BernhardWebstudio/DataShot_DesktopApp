/**
 * ValidatingTableCellEditor.java
 * edu.harvard.mcz.imagecapture
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.ui;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import javax.swing.*;
import java.awt.*;


/**
 * ValidatingTableCellEditor provides a JTextField with an inputVerifier as a
 * cell editor that can be used as an editor for cells in a JTable.
 * <p>
 * Example:
 * <pre>
 * JTextField field = new JTextField();
 * field.setInputVerifier(MetadataRetriever.getInputVerifier(Collector.class, "CollectorName", field));
 * jTableCollectors.getColumnModel().getColumn(0).setCellEditor(new ValidatingTableCellEditor(field));
 * </pre>
 */
public class PicklistTableCellEditor extends ComboBoxCellEditor {

    private static final long serialVersionUID = -4777010317672887845L;

    private final JComboBox field;

    /**
     * Constructor
     *
     * @param textField JComboBox to support the picklist
     */
    public PicklistTableCellEditor(JComboBox textField) {
        super(textField);
        field = textField;
    }

    /**
     * Constructor
     *
     * @param textField JComboBox to support the picklist
     * @param editable  value to set for editable of the jcombobox
     */
    public PicklistTableCellEditor(JComboBox textField, boolean editable) {
        super(textField);
        field = textField;
        field.setEditable(editable);
    }

    @Override
    public Object getCellEditorValue() {
        return field.getSelectedItem();
    }


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
    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    /* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value != null) {
            field.setSelectedItem(value);
        } else {
            field.setSelectedItem("");
        }
        return field;
    }

}
