package edu.harvard.mcz.imagecapture.ui.component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class JTableCellTabbing {
    /**
     * Creates a new {@code JTableCellTabbing} object.
     */
    private JTableCellTabbing() {
    }

    /**
     * Set Action Map for tabbing and shift-tabbing for the JTable
     *
     * @param theTable - Jtable with NRows and MCols of cells
     * @param startRow - valid start row for tabbing [ 0 - (numRows-1) ]
     * @param numRows  - Number of rows for tabbing
     * @param startCol - valid start col for tabbing [ 0 - (numCols-1) ]
     * @param numCols  -  Number of columns for tabbing
     */
    @SuppressWarnings("serial")
    static public void setTabMapping(final JTable theTable, final int startRow, final int numRows, final int startCol, final int numCols) {
        if (theTable == null) {
            throw new IllegalArgumentException("theTable is null");
        }

        // Calculate last row and column for tabbing
        final int endRow = startRow + (numRows - 1);
        final int endCol = startCol + (numCols - 1);

        // Check for valid range
        if ((startRow > endRow) || (startCol > endCol)) {
            throw new IllegalArgumentException("Table Size incorrect: endRow=" + endRow + ", startRow = " + startRow + ", startCol=" + startCol + ", endCol = " + endCol);
        }

        // Get Input and Action Map to set tabbing order on the JTable
        InputMap im = theTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = theTable.getActionMap();

        // Get Tab Keystroke
        KeyStroke tabKey = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        am.put(im.get(tabKey), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = theTable.getSelectedRow();
                int col = theTable.getSelectedColumn();

                col++;

                // Move to next row and left column
                if (col > endCol) {
                    col = startCol;
                    row++;
                }

                // Move to top row
                if (row > endRow) {
                    row = startRow;
                }

                // Move cell selection
                theTable.changeSelection(row, col, false, false);
                theTable.editCellAt(row, col);
//                theTable.getCellEditor(row, col).getTableCellEditorComponent(theTable, ).requestFocus();
            }
        });

        // Get Shift tab Keystroke
        KeyStroke shiftTab =
                KeyStroke.getKeyStroke(KeyEvent.VK_TAB, java.awt.event.InputEvent.SHIFT_DOWN_MASK);
        am.put(im.get(shiftTab), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = theTable.getSelectedRow();
                int col = theTable.getSelectedColumn();

                col--;

                // Move to top right cell
                if (col < startCol) {
                    col = endCol;
                    row--;
                }

                // Move to bottom row
                if (row < startRow) {
                    row = endRow;
                }

                // Move cell selection
                if (theTable.editCellAt(row, col)) {
                    theTable.changeSelection(row, col, false, false);
                    theTable.editCellAt(row, col);
                };
            }
        });
    }

}