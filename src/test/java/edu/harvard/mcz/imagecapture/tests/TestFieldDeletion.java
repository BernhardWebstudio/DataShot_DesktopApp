package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import edu.harvard.mcz.imagecapture.ui.component.AutoCompleteHelper;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import org.junit.Test;

/**
 * Tests verifying that deleting from editable fields (text fields and
 * auto-complete combo boxes) works with both Backspace and Delete keys,
 * specifically in SpecimenDetailsViewPane and FormBindingContext.
 */
public class TestFieldDeletion {

	/**
	 * Helper to simulate pressing a key stroke on a text component.
	 */
	private void simulateKey(JTextComponent component, KeyStroke keyStroke) {
		Object actionKey = component.getInputMap().get(keyStroke);
		assertNotNull("InputMap should contain action for key " + keyStroke, actionKey);
		Action action = component.getActionMap().get(actionKey);
		assertNotNull("ActionMap should contain action for " + actionKey, action);
		action.actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, ""));
	}

	@Test
	public void testAutoCompleteHelperBackspaceAndEscape() throws Exception {
		JComboBox<String> comboBox = new JComboBox<>(new String[]{"Pieridae", "Papilionidae", "Nymphalidae"});
		comboBox.setEditable(true);
		AutoCompleteHelper.decorate(comboBox);

		JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		comboBox.setSelectedItem("Pieridae");
		assertEquals("Pieridae", editor.getText());

		KeyStroke backspace = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);
		KeyStroke delete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

		// 1. Backspace on selected text should delete the selection
		editor.selectAll();
		simulateKey(editor, backspace);
		assertEquals("", editor.getText());

		// 2. Backspace at the end of text should delete the last character
		comboBox.setSelectedItem("Pieridae");
		editor.setCaretPosition(editor.getText().length());
		simulateKey(editor, backspace);
		assertEquals("Pierida", editor.getText());

		// 3. Delete on selected text should delete the selection
		comboBox.setSelectedItem("Papilionidae");
		editor.selectAll();
		simulateKey(editor, delete);
		assertEquals("", editor.getText());

		// 4. Auto-completion still functions when typing
		editor.setText("");
		editor.getDocument().insertString(0, "N", null);
		assertEquals("Nymphalidae", editor.getText());
		assertEquals("Nymphalidae", comboBox.getSelectedItem());
	}

	@Test
	public void testFormBindingContextFieldsSupportBackspace() throws Exception {
		Specimen s = new Specimen();
		s.setCountry("Switzerland");
		s.setSpecificLocality("Zurich");

		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true);

		// Combo box binding (uses AutoCompleteHelper)
		JComboBox<String> countryBox = context.bindComboBox("Country",
				new String[]{"Switzerland", "Germany", "Austria"}, Specimen::getCountry, Specimen::setCountry);
		context.readFrom(s);

		JTextComponent cbEditor = (JTextComponent) countryBox.getEditor().getEditorComponent();
		assertEquals("Switzerland", cbEditor.getText());

		KeyStroke backspace = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);

		// Backspace on entire selection
		cbEditor.selectAll();
		simulateKey(cbEditor, backspace);
		assertEquals("", cbEditor.getText());

		// Backspace single character
		countryBox.setSelectedItem("Germany");
		cbEditor.setCaretPosition(cbEditor.getText().length());
		simulateKey(cbEditor, backspace);
		assertEquals("German", cbEditor.getText());

		// Text field binding
		JTextField localityField = context.bindTextField("SpecificLocality", Specimen::getSpecificLocality,
				Specimen::setSpecificLocality);
		context.readFrom(s);
		assertEquals("Zurich", localityField.getText());

		localityField.setCaretPosition(localityField.getText().length());
		simulateKey(localityField, backspace);
		assertEquals("Zuric", localityField.getText());
	}

	@Test
	public void testSpecimenDetailsViewPaneComboBoxBackspace() throws Exception {
		Specimen specimen = new Specimen();
		specimen.setBarcode("TEST_BARCODE_001");
		specimen.setGenus("Pieris");
		specimen.setSpecificEpithet("rapae");
		specimen.setCountry("United States");
		specimen.setWorkFlowStatus(WorkFlowStatus.STAGE_1);

		// SpecimenDetailsViewPane can be initialized with specimen and null controller
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(specimen, null);

		// Check jComboBoxCountry
		Field countryField = SpecimenDetailsViewPane.class.getDeclaredField("jComboBoxCountry");
		countryField.setAccessible(true);
		@SuppressWarnings("unchecked")
		JComboBox<String> jComboBoxCountry = (JComboBox<String>) countryField.get(pane);
		assertNotNull("jComboBoxCountry should be present", jComboBoxCountry);

		JTextComponent editor = (JTextComponent) jComboBoxCountry.getEditor().getEditorComponent();
		assertNotNull("Editor component should be present", editor);

		KeyStroke backspace = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0);

		// Verify Backspace deletes selected text
		jComboBoxCountry.setSelectedItem("United States");
		editor.selectAll();
		simulateKey(editor, backspace);
		assertEquals("Selected text in combo box should be deleted by Backspace", "", editor.getText());

		// Verify Backspace deletes previous character at end of item
		jComboBoxCountry.setSelectedItem("United States");
		editor.setCaretPosition(editor.getText().length());
		simulateKey(editor, backspace);
		assertEquals("Previous character should be deleted by Backspace", "United State", editor.getText());
	}

	@Test
	public void testCasePreservationAndModification() throws Exception {
		// Picklist contains "Cabo verde" with lowercase 'v'
		JComboBox<String> comboBox = new JComboBox<>(new String[]{"Cabo verde", "United States"});
		comboBox.setEditable(true);
		AutoCompleteHelper.decorate(comboBox);

		JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		comboBox.setSelectedItem("Cabo verde");
		assertEquals("Cabo verde", editor.getText());

		// 1. In-place edit: replace 'v' with capital 'V' in "Cabo verde"
		// Offset 5, length 1 is 'v'
		editor.getDocument().remove(5, 1);
		assertEquals("Cabo erde", editor.getText());
		editor.getDocument().insertString(5, "V", null);
		assertEquals("Cabo Verde", editor.getText());
		assertEquals("Cabo Verde", comboBox.getSelectedItem());

		// 2. Select 'v' and replace directly with 'V'
		comboBox.setSelectedItem("Cabo verde");
		editor.select(5, 6);
		// Simulate typing 'V' over selection
		KeyStroke keyV = KeyStroke.getKeyStroke('V');
		Action keyAction = editor.getActionMap().get(DefaultEditorKit.defaultKeyTypedAction);
		if (keyAction != null) {
			keyAction.actionPerformed(new ActionEvent(editor, ActionEvent.ACTION_PERFORMED, "V"));
		} else {
			// Direct document replacement corresponding to typing 'V' over selection
			editor.getDocument().remove(5, 1);
			editor.getDocument().insertString(5, "V", null);
		}
		assertEquals("Cabo Verde", editor.getText());
		assertEquals("Cabo Verde", comboBox.getSelectedItem());

		// 3. Typing at end: user types capital 'V' after "Cabo " when picklist has
		// "Cabo verde"
		editor.setText("Cabo ");
		editor.getDocument().insertString(5, "V", null);
		assertEquals("Cabo Verde", editor.getText());
		assertEquals("Cabo Verde", comboBox.getSelectedItem());

		// 4. FormBindingContext test: writing back to Specimen entity preserves capital
		// 'V'
		Specimen s = new Specimen();
		s.setCountry("Cabo verde");
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true);
		JComboBox<String> countryBox = context.bindComboBox("Country", new String[]{"Cabo verde", "United States"},
				Specimen::getCountry, Specimen::setCountry);
		context.readFrom(s);
		assertEquals("Cabo verde", s.getCountry());

		JTextComponent cbEditor = (JTextComponent) countryBox.getEditor().getEditorComponent();
		assertEquals("Cabo verde", cbEditor.getText());

		// Change 'v' to 'V'
		cbEditor.getDocument().remove(5, 1);
		cbEditor.getDocument().insertString(5, "V", null);
		assertEquals("Cabo Verde", cbEditor.getText());

		// Write to entity
		context.writeTo(s);
		assertEquals("Specimen country should be updated with capital V", "Cabo Verde", s.getCountry());
	}
}
