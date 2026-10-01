package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.ui.component.JTableCellTabbing;
import edu.harvard.mcz.imagecapture.ui.component.JTableWithRowBorder;
import edu.harvard.mcz.imagecapture.ui.tablemodel.AbstractDeleteableTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for JTableCellTabbing to ensure smooth cell traversal and that users
 * are not trapped inside table structures.
 */
public class TestTableCellTabbing {

	private JTextField beforeField;
	private JTable table;
	private JTextField afterField;
	private Action forwardAction;
	private Action backwardAction;

	@Before
	public void setUp() {
		JPanel panel = new JPanel(new BorderLayout());

		beforeField = new JTextField("Before");
		DefaultTableModel model = new DefaultTableModel(new Object[][]{{"R0C0", "R0C1"}, {"R1C0", "R1C1"}},
				new Object[]{"Col 0", "Col 1"});
		table = new JTable(model);
		JTableCellTabbing.install(table);

		afterField = new JTextField("After");

		panel.add(beforeField, BorderLayout.NORTH);
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(afterField, BorderLayout.SOUTH);

		forwardAction = table.getActionMap().get(table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)));
		backwardAction = table.getActionMap().get(table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK)));

		assertNotNull("Forward tab action should be registered", forwardAction);
		assertNotNull("Backward shift-tab action should be registered", backwardAction);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testForwardTabbingTraversesCellsAndExitsTable() {
		// Start at (0, 0)
		table.changeSelection(0, 0, false, false);
		assertEquals(0, table.getSelectedRow());
		assertEquals(0, table.getSelectedColumn());

		// Tab to (0, 1)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(0, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());

		// Tab to (1, 0)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(1, table.getSelectedRow());
		assertEquals(0, table.getSelectedColumn());

		// Tab to (1, 1) - last cell
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(1, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());

		// Tab on last cell -> must EXIT table rather than loop back to (0, 0)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals("Selection should be cleared when transferring focus out", -1, table.getSelectedRow());
	}

	@Test
	public void testBackwardTabbingTraversesCellsAndExitsTable() {
		// Start at (1, 1)
		table.changeSelection(1, 1, false, false);
		assertEquals(1, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());

		// Shift-Tab to (1, 0)
		backwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(1, table.getSelectedRow());
		assertEquals(0, table.getSelectedColumn());

		// Shift-Tab to (0, 1)
		backwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(0, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());

		// Shift-Tab to (0, 0) - first cell
		backwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(0, table.getSelectedRow());
		assertEquals(0, table.getSelectedColumn());

		// Shift-Tab on first cell -> must EXIT table backward rather than loop to (1,
		// 1)
		backwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals("Selection should be cleared when transferring focus backward out", -1, table.getSelectedRow());
	}

	@Test
	public void testTabbingWhenNothingSelectedStartsAtFirstOrLastCell() {
		table.clearSelection();
		assertEquals(-1, table.getSelectedRow());

		// Forward tab with no selection -> selects (0, 0)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(0, table.getSelectedRow());
		assertEquals(0, table.getSelectedColumn());

		table.clearSelection();
		assertEquals(-1, table.getSelectedRow());

		// Backward tab with no selection -> selects last cell (1, 1)
		backwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(1, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());
	}

	@Test
	public void testEmptyTableTabbingExitsImmediatelyWithoutErrors() {
		DefaultTableModel emptyModel = new DefaultTableModel(new Object[][]{}, new Object[]{"Col 0", "Col 1"});
		JTable emptyTable = new JTable(emptyModel);
		JTableCellTabbing.install(emptyTable);

		Action emptyForward = emptyTable.getActionMap()
				.get(emptyTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
						.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)));
		Action emptyBackward = emptyTable.getActionMap()
				.get(emptyTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
						.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK)));

		// Should not throw exception and should transfer focus
		emptyForward.actionPerformed(new ActionEvent(emptyTable, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(-1, emptyTable.getSelectedRow());

		emptyBackward.actionPerformed(new ActionEvent(emptyTable, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(-1, emptyTable.getSelectedRow());
	}

	@Test
	public void testEditingStopsAndCommitsOnTab() {
		table.changeSelection(0, 0, false, false);
		table.editCellAt(0, 0);
		assertTrue("Table should be editing", table.isEditing());

		Component editorComp = table.getEditorComponent();
		assertTrue("Editor should be JTextField", editorComp instanceof JTextField);
		((JTextField) editorComp).setText("EditedValue");

		// Tab should commit edit to model and advance to next cell
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null));
		assertEquals("EditedValue", table.getValueAt(0, 0));
		assertEquals(0, table.getSelectedRow());
		assertEquals(1, table.getSelectedColumn());

		// Tab to end and out -> editing must stop completely
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null)); // (1, 0)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null)); // (1, 1)
		forwardAction.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, null)); // exit
		assertFalse("Editing should stop completely after exiting table", table.isEditing());
	}

	@Test
	public void testJTableWithRowBorderHasTabbingInstalledByDefault() {
		class TestDeleteableModel extends AbstractDeleteableTableModel {
			private List<String> data = new ArrayList<>(List.of("Row 1", "Row 2"));

			@Override
			public int getRowCount() {
				return data.size();
			}

			@Override
			public int getColumnCount() {
				return 1;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				return data.get(rowIndex);
			}

			@Override
			public void deleteRow(int rowIndex) {
				data.remove(rowIndex);
				fireTableRowsDeleted(rowIndex, rowIndex);
			}
		}

		JTableWithRowBorder borderedTable = new JTableWithRowBorder(new TestDeleteableModel());
		Action borderedForward = borderedTable.getActionMap()
				.get(borderedTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
						.get(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0)));
		assertNotNull("JTableWithRowBorder must have forward tab action installed", borderedForward);

		// Single column table: Row 0 -> Row 1 -> exit!
		borderedTable.changeSelection(0, 0, false, false);
		borderedForward.actionPerformed(new ActionEvent(borderedTable, ActionEvent.ACTION_PERFORMED, null));
		assertEquals(1, borderedTable.getSelectedRow());

		borderedForward.actionPerformed(new ActionEvent(borderedTable, ActionEvent.ACTION_PERFORMED, null));
		assertEquals("Single-column table must exit when tabbing past last row", -1, borderedTable.getSelectedRow());
	}
}
