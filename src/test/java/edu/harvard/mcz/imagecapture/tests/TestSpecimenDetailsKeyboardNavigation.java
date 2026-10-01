package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests asserting that the SpecimenDetailsViewPane form is completely keyboard
 * navigable and free of focus traps (e.g. multiline text fields and tables).
 */
public class TestSpecimenDetailsKeyboardNavigation {

	private SpecimenDetailsViewPane pane;

	@Before
	public void setUp() {
		Specimen specimen = new Specimen();
		specimen.setBarcode("KB-" + java.util.UUID.randomUUID().toString().substring(0, 8));
		specimen.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
		pane = new SpecimenDetailsViewPane(specimen, null);
	}

	private List<Component> getAllComponents(Container container) {
		List<Component> components = new ArrayList<>();
		for (Component c : container.getComponents()) {
			components.add(c);
			if (c instanceof Container) {
				components.addAll(getAllComponents((Container) c));
			}
		}
		return components;
	}

	@Test
	public void testSpecimenNotesTextAreaHasFocusTraversalKeysEnabled() {
		List<Component> all = getAllComponents(pane);
		List<JTextArea> textAreas = new ArrayList<>();
		for (Component c : all) {
			if (c instanceof JTextArea) {
				textAreas.add((JTextArea) c);
			}
		}

		assertFalse("SpecimenDetailsViewPane should contain at least one JTextArea for notes", textAreas.isEmpty());
		for (JTextArea ta : textAreas) {
			assertTrue("JTextArea must have focus traversal keys enabled so Tab does not trap the user",
					ta.getFocusTraversalKeysEnabled());
		}
	}

	@Test
	public void testAllTextComponentsAllowKeyboardTraversal() {
		List<Component> all = getAllComponents(pane);
		for (Component c : all) {
			if (c instanceof JTextComponent) {
				JTextComponent textComp = (JTextComponent) c;
				if (textComp.isFocusable() && textComp.isEditable()) {
					assertTrue(
							"Text component " + textComp.getClass().getSimpleName()
									+ " must have focus traversal keys enabled to prevent keyboard traps",
							textComp.getFocusTraversalKeysEnabled());
				}
			}
		}
	}

	@Test
	public void testAllTablesHaveCellTabbingInstalled() {
		List<Component> all = getAllComponents(pane);
		List<JTable> tables = new ArrayList<>();
		for (Component c : all) {
			if (c instanceof JTable) {
				tables.add((JTable) c);
			}
		}

		assertFalse("SpecimenDetailsViewPane should contain table components", tables.isEmpty());
		KeyStroke tabStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
		KeyStroke shiftTabStroke = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK);

		for (JTable table : tables) {
			Object tabActionKey = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(tabStroke);
			Object shiftTabActionKey = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).get(shiftTabStroke);

			assertNotNull("Table " + table + " must have a Tab key binding registered", tabActionKey);
			assertNotNull("Table " + table + " must have a Shift-Tab key binding registered", shiftTabActionKey);
			assertNotNull("Table " + table + " must have an action for Tab in its ActionMap",
					table.getActionMap().get(tabActionKey));
			assertNotNull("Table " + table + " must have an action for Shift-Tab in its ActionMap",
					table.getActionMap().get(shiftTabActionKey));
		}
	}
}
