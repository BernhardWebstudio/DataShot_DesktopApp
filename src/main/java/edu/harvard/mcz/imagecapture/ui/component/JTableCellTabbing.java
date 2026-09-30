package edu.harvard.mcz.imagecapture.ui.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 * Handles Tab and Shift-Tab navigation across cells in a JTable. When reaching
 * the boundaries (end of table on Tab, beginning of table on Shift-Tab, or on
 * an empty table), focus cleanly transfers out of the table to the next or
 * previous focusable component, preventing focus traps.
 */
public class JTableCellTabbing {

	/**
	 * Creates a new {@code JTableCellTabbing} object.
	 */
	private JTableCellTabbing() {
	}

	/**
	 * Installs dynamic forward/backward tab navigation on the table across all rows
	 * and columns, transferring focus out when reaching the boundary.
	 *
	 * @param theTable
	 *            the JTable to configure
	 */
	public static void install(final JTable theTable) {
		setTabMapping(theTable, -1, -1, -1, -1);
	}

	/**
	 * Set Action Map for tabbing and shift-tabbing for the JTable. If bounds are
	 * specified (>= 0), navigation is restricted to those bounds; otherwise, the
	 * full dynamic table size is used.
	 *
	 * @param theTable
	 *            - JTable with rows and columns of cells
	 * @param startRow
	 *            - start row for tabbing (or -1 for dynamic start row 0)
	 * @param numRows
	 *            - Number of rows for tabbing (or -1 for dynamic total row count)
	 * @param startCol
	 *            - start col for tabbing (or -1 for dynamic start col 0)
	 * @param numCols
	 *            - Number of columns for tabbing (or -1 for dynamic total col
	 *            count)
	 */
	@SuppressWarnings("serial")
	public static void setTabMapping(final JTable theTable, final int startRow, final int numRows, final int startCol,
			final int numCols) {
		if (theTable == null) {
			throw new IllegalArgumentException("theTable is null");
		}

		InputMap im = theTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = theTable.getActionMap();

		Object tabActionKey = im.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		if (tabActionKey == null) {
			tabActionKey = "selectNextColumnCell";
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), tabActionKey);
		}

		Object shiftTabActionKey = im.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK));
		if (shiftTabActionKey == null) {
			shiftTabActionKey = "selectPreviousColumnCell";
			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK), shiftTabActionKey);
		}

		am.put(tabActionKey, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigateForward(theTable, startRow, numRows, startCol, numCols);
			}
		});

		am.put(shiftTabActionKey, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigateBackward(theTable, startRow, numRows, startCol, numCols);
			}
		});

		// Attach focus listener once to select cell or pass through empty table
		boolean hasFocusListener = false;
		for (java.awt.event.FocusListener fl : theTable.getFocusListeners()) {
			if (fl instanceof TableTabFocusListener) {
				hasFocusListener = true;
				break;
			}
		}
		if (!hasFocusListener) {
			theTable.addFocusListener(new TableTabFocusListener(theTable));
		}
	}

	private static boolean stopCellEditing(JTable table) {
		if (table.isEditing()) {
			TableCellEditor editor = table.getCellEditor();
			if (editor != null) {
				return editor.stopCellEditing();
			}
		}
		return true;
	}

	private static void navigateForward(JTable table, int startRow, int numRows, int startCol, int numCols) {
		if (!stopCellEditing(table)) {
			return;
		}

		int totalRows = table.getRowCount();
		int totalCols = table.getColumnCount();

		if (totalRows == 0 || totalCols == 0) {
			table.clearSelection();
			table.transferFocus();
			return;
		}

		int minRow = Math.max(0, startRow >= 0 ? startRow : 0);
		int maxRow = (numRows > 0 && startRow >= 0 && numRows < totalRows)
				? Math.min(startRow + numRows - 1, totalRows - 1)
				: totalRows - 1;
		int minCol = Math.max(0, startCol >= 0 ? startCol : 0);
		int maxCol = (numCols > 0 && startCol >= 0 && numCols < totalCols)
				? Math.min(startCol + numCols - 1, totalCols - 1)
				: totalCols - 1;

		if (minRow > maxRow || minCol > maxCol) {
			table.clearSelection();
			table.transferFocus();
			return;
		}

		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (row < minRow || row > maxRow || col < minCol || col > maxCol) {
			row = minRow;
			col = minCol;
		} else {
			col++;
			if (col > maxCol) {
				col = minCol;
				row++;
			}
			if (row > maxRow) {
				table.clearSelection();
				table.transferFocus();
				return;
			}
		}

		table.changeSelection(row, col, false, false);
		if (table.isCellEditable(row, col)) {
			table.editCellAt(row, col);
			Component editorComp = table.getEditorComponent();
			if (editorComp != null) {
				editorComp.requestFocusInWindow();
			}
		}
	}

	private static void navigateBackward(JTable table, int startRow, int numRows, int startCol, int numCols) {
		if (!stopCellEditing(table)) {
			return;
		}

		int totalRows = table.getRowCount();
		int totalCols = table.getColumnCount();

		if (totalRows == 0 || totalCols == 0) {
			table.clearSelection();
			table.transferFocusBackward();
			return;
		}

		int minRow = Math.max(0, startRow >= 0 ? startRow : 0);
		int maxRow = (numRows > 0 && startRow >= 0 && numRows < totalRows)
				? Math.min(startRow + numRows - 1, totalRows - 1)
				: totalRows - 1;
		int minCol = Math.max(0, startCol >= 0 ? startCol : 0);
		int maxCol = (numCols > 0 && startCol >= 0 && numCols < totalCols)
				? Math.min(startCol + numCols - 1, totalCols - 1)
				: totalCols - 1;

		if (minRow > maxRow || minCol > maxCol) {
			table.clearSelection();
			table.transferFocusBackward();
			return;
		}

		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		if (row < minRow || row > maxRow || col < minCol || col > maxCol) {
			row = maxRow;
			col = maxCol;
		} else {
			col--;
			if (col < minCol) {
				col = maxCol;
				row--;
			}
			if (row < minRow) {
				table.clearSelection();
				table.transferFocusBackward();
				return;
			}
		}

		table.changeSelection(row, col, false, false);
		if (table.isCellEditable(row, col)) {
			table.editCellAt(row, col);
			Component editorComp = table.getEditorComponent();
			if (editorComp != null) {
				editorComp.requestFocusInWindow();
			}
		}
	}

	private static class TableTabFocusListener extends FocusAdapter {
		private final JTable table;

		TableTabFocusListener(JTable table) {
			this.table = table;
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (table.getRowCount() == 0 || table.getColumnCount() == 0) {
				FocusEvent.Cause cause = e.getCause();
				if (cause == FocusEvent.Cause.TRAVERSAL_BACKWARD) {
					table.transferFocusBackward();
				} else if (cause == FocusEvent.Cause.TRAVERSAL_FORWARD) {
					table.transferFocus();
				}
				return;
			}
			if (table.getSelectedRow() < 0) {
				boolean backward = (e.getCause() == FocusEvent.Cause.TRAVERSAL_BACKWARD);
				int targetRow = backward ? table.getRowCount() - 1 : 0;
				int targetCol = backward ? table.getColumnCount() - 1 : 0;
				table.changeSelection(targetRow, targetCol, false, false);
				if (table.isCellEditable(targetRow, targetCol)) {
					table.editCellAt(targetRow, targetCol);
					Component comp = table.getEditorComponent();
					if (comp != null) {
						comp.requestFocusInWindow();
					}
				}
			}
		}
	}
}
