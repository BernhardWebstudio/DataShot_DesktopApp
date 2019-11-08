/**
 * ButtonRenderer.java
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

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * ButtonRenderer works together with ButonEditor and a TableModel that implements
 * isCellEditable() for relevant cells to produce a clickable button in a cell.
 *
 * @see edu.harvard.mcz.imagecapture.ui.ButtonEditor
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    private static final long serialVersionUID = 7522359895927187088L;
    JButton thisButton = null;
    private String buttonText = "Edit";

    public ButtonRenderer() {
    }

    public ButtonRenderer(String aButtonText) {
        buttonText = aButtonText;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (thisButton == null) {
            // only need to create one instance of a button for this
            // instance of ButtonRenderer.
            thisButton = new JButton();
            thisButton.setText(buttonText);
        }
        thisButton.setEnabled(isSelected);
        return thisButton;
    }

}



