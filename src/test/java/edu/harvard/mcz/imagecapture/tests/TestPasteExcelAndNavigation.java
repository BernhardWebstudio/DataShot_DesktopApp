package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.GeoreferenceDialog;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPasteExcelAndNavigation {

	private SpecimenLifeCycle sls;
	private final List<Specimen> createdSpecimens = new ArrayList<>();

	@Before
	public void setUp() {
		sls = new SpecimenLifeCycle();
	}

	@After
	public void tearDown() {
		for (Specimen s : createdSpecimens) {
			try {
				if (s != null && s.getBarcode() != null) {
					sls.deleteSpecimenByBarcode(s.getBarcode());
				}
			} catch (Exception ignored) {
			}
		}
		createdSpecimens.clear();
	}

	private Specimen createSpecimen(String barcode) throws Exception {
		Specimen s = new Specimen();
		s.setBarcode(barcode);
		s.setGenus("Papilio");
		s.setSpecificEpithet("glaucus");
		s.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
		s.setDateCreated(new Date());
		sls.persist(s);
		createdSpecimens.add(s);
		return s;
	}

	@Test
	public void testPasteExcelDirectlyOnGeoreferenceDialog() {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		LatLong geo = new LatLong();
		GeoreferenceDialog dialog = new GeoreferenceDialog(geo);
		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		dialog.pasteFromExcel(excelData);

		assertEquals(new BigDecimal("47.3769"), dialog.getGeoReference().getDecLat());
		assertEquals(new BigDecimal("8.5417"), dialog.getGeoReference().getDecLong());
	}

	@Test
	public void testPasteExcelOnSpecimenDetailsViewPaneAfterEditingField() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_EXCEL_001");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Copy data to clipboard
		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		// Edit something else first (e.g. status or notes or locality)
		Field f = SpecimenDetailsViewPane.class.getDeclaredField("jTextFieldLocality");
		f.setAccessible(true);
		JTextField localityField = (JTextField) f.get(pane);
		if (localityField != null) {
			localityField.setText("Initial Locality");
		}

		// Click Paste Excel button
		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		Method getLongFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLong");
		getLongFieldMethod.setAccessible(true);
		JTextField longField = (JTextField) getLongFieldMethod.invoke(pane);

		assertEquals("47.3769", latField.getText());
		assertEquals("8.5417", longField.getText());
	}

	@Test
	public void testPasteExcelAfterDatumSelection() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_EXCEL_DATUM");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		Method getDatumCbMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getDatumComboBox");
		getDatumCbMethod.setAccessible(true);
		JComboBox datumCb = (JComboBox) getDatumCbMethod.invoke(pane);
		datumCb.setSelectedItem("WGS84");

		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		Method getLongFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLong");
		getLongFieldMethod.setAccessible(true);
		JTextField longField = (JTextField) getLongFieldMethod.invoke(pane);

		assertEquals("47.3769", latField.getText());
		assertEquals("8.5417", longField.getText());
	}

	@Test
	public void testPasteExcelAfterSave() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_EXCEL_SAVE");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Edit status to Text Entered and save
		Method saveMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("save");
		saveMethod.setAccessible(true);
		saveMethod.invoke(pane);

		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		Method getLongFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLong");
		getLongFieldMethod.setAccessible(true);
		JTextField longField = (JTextField) getLongFieldMethod.invoke(pane);

		System.out.println("DEBUG after save: latField='" + latField.getText() + "'");
		assertEquals("47.3769", latField.getText());
		assertEquals("8.5417", longField.getText());
	}

	@Test
	public void testPasteExcelAfterMethodOrDatumEdit() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		// Test editing method or datum first
		Specimen s = createSpecimen("TEST_EX_M1");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		Method getMethodCb = SpecimenDetailsViewPane.class.getDeclaredMethod("getMethodComboBox");
		getMethodCb.setAccessible(true);
		JComboBox cbMethod = (JComboBox) getMethodCb.invoke(pane);
		cbMethod.setSelectedItem("GPS");

		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		System.out.println("After editing method: latField='" + latField.getText() + "'");
		assertEquals("Lat should be transferred after method edit", "47.3769", latField.getText());
	}

	@Test
	public void testPasteExcelAfterLatFocusLost() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_EX_F1");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		// Simulate focus lost on empty decimal lat field (e.g. tabbing through)
		for (java.awt.event.FocusListener fl : latField.getFocusListeners()) {
			fl.focusLost(new java.awt.event.FocusEvent(latField, java.awt.event.FocusEvent.FOCUS_LOST));
		}

		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		System.out.println("After lat focus lost: latField='" + latField.getText() + "'");
		assertEquals("Lat should be transferred after lat focus lost", "47.3769", latField.getText());
	}

	@Test
	public void testPasteExcelWhenSpecimenAlreadyHasLatLongInDB() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_EX_DB1");
		LatLong existingGeo = new LatLong();
		existingGeo.setSpecimen(s);
		existingGeo.setGeorefmethod("unknown");
		// Coordinates not set or empty
		s.getLatLong().add(existingGeo);
		sls.attachDirty(s);

		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Edit something first
		Field f = SpecimenDetailsViewPane.class.getDeclaredField("jTextFieldLocality");
		f.setAccessible(true);
		JTextField tf = (JTextField) f.get(pane);
		tf.setText("Some locality");

		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		System.out.println("When specimen has LatLong in DB: latField='" + latField.getText() + "'");
		assertEquals("Lat should be transferred", "47.3769", latField.getText());
	}

	/**
	 * Bug 1: Setting status to "Text Entered" and saving should keep the Next and
	 * Previous navigation buttons enabled (when next/prev records exist).
	 */
	@Test
	public void testNextPreviousButtonsEnabledAfterSave() throws Exception {
		Specimen s1 = createSpecimen("TEST_NAV_001");
		Specimen s2 = createSpecimen("TEST_NAV_002");
		Specimen s3 = createSpecimen("TEST_NAV_003");

		List<Specimen> list = new ArrayList<>();
		list.add(s1);
		list.add(s2);
		list.add(s3);

		SpecimenListTableModel tableModel = new SpecimenListTableModel(list);
		JTable table = new JTable(tableModel);

		// Open second specimen (row 1), so both previous and next exist
		SpecimenController controller = new SpecimenController(s2, tableModel, table, 1, 0);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		Field nextBtnField = SpecimenDetailsViewPane.class.getDeclaredField("jButtonNext");
		nextBtnField.setAccessible(true);
		JButton nextBtn = (JButton) nextBtnField.get(pane);

		Field prevBtnField = SpecimenDetailsViewPane.class.getDeclaredField("jButtonPrevious");
		prevBtnField.setAccessible(true);
		JButton prevBtn = (JButton) prevBtnField.get(pane);

		Field workflowBoxField = SpecimenDetailsViewPane.class.getDeclaredField("jComboBoxWorkflowStatus");
		workflowBoxField.setAccessible(true);
		JComboBox<?> workflowBox = (JComboBox<?>) workflowBoxField.get(pane);

		// Initially, buttons should be enabled
		assertTrue("Previous button should be enabled before edit", prevBtn.isEnabled());
		assertTrue("Next button should be enabled before edit", nextBtn.isEnabled());

		// Change status to "Text Entered"
		workflowBox.setSelectedItem(WorkFlowStatus.STAGE_1);

		// Save the specimen
		Method saveMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("save");
		saveMethod.setAccessible(true);
		boolean saved = (boolean) saveMethod.invoke(pane);
		assertTrue("Save should succeed", saved);

		// After save, form must be clean and Next / Previous buttons must remain
		// enabled!
		assertTrue("Form should be in clean state after save", pane.isClean());
		assertTrue("Previous button should remain enabled after save", prevBtn.isEnabled());
		assertTrue("Next button should remain enabled after save", nextBtn.isEnabled());
	}

	/**
	 * Bug 3: Date format in "Last edit date" field should remain consistent before
	 * and after save.
	 */
	@Test
	public void testLastEditDateFormatConsistentOnSave() throws Exception {
		Specimen s = createSpecimen("TEST_DATE_001");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		Field dateField = SpecimenDetailsViewPane.class.getDeclaredField("jTextFieldDateLastUpdated");
		dateField.setAccessible(true);
		JTextField dateTxtField = (JTextField) dateField.get(pane);

		// Save the specimen
		Method saveMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("save");
		saveMethod.setAccessible(true);
		boolean saved = (boolean) saveMethod.invoke(pane);
		assertTrue("Save should succeed", saved);

		String dateAfterSave = dateTxtField.getText();
		assertNotNull(dateAfterSave);
		assertFalse("Date should not be empty after save", dateAfterSave.isEmpty());
		// Must match consistent format yyyy-MM-dd HH:mm:ss
		assertTrue("Date format after save should match yyyy-MM-dd HH:mm:ss, but was: " + dateAfterSave,
				dateAfterSave.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
	}

	@Test
	public void testPasteExcelAfterEditingAndSavePersistsCoordinates() throws Exception {
		if (GraphicsEnvironment.isHeadless()) {
			return;
		}
		Specimen s = createSpecimen("TEST_ALL_001");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Edit a field first
		Field f = SpecimenDetailsViewPane.class.getDeclaredField("jTextFieldLocality");
		f.setAccessible(true);
		JTextField locField = (JTextField) f.get(pane);
		locField.setText("Edited Before Paste");

		// Paste Excel data
		String excelData = "VerbatimLoc\tCol1\tCol2\tSwitzerland\tZurich\tZurich City\t47.3769, 8.5417\t50\tGPS";
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(excelData), null);

		Method getPasteBtnMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getJButtonPasteExcel");
		getPasteBtnMethod.setAccessible(true);
		JButton pasteBtn = (JButton) getPasteBtnMethod.invoke(pane);
		pasteBtn.doClick();

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		Method getLongFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLong");
		getLongFieldMethod.setAccessible(true);
		JTextField longField = (JTextField) getLongFieldMethod.invoke(pane);

		assertEquals("47.3769", latField.getText());
		assertEquals("8.5417", longField.getText());

		// Save
		Method saveMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("save");
		saveMethod.setAccessible(true);
		boolean saved = (boolean) saveMethod.invoke(pane);
		assertTrue("Save should succeed", saved);

		// Form should be clean
		assertTrue("Form should be clean after save", pane.isClean());

		// Verify database persistence of coordinates
		Specimen loaded = sls.findById(s.getSpecimenId());
		assertNotNull(loaded);
		assertNotNull(loaded.getLatLong());
		assertFalse("LatLong set should not be empty", loaded.getLatLong().isEmpty());
		LatLong savedGeo = loaded.getLatLong().iterator().next();
		assertEquals(0, new BigDecimal("47.3769").compareTo(savedGeo.getDecLat()));
		assertEquals(0, new BigDecimal("8.5417").compareTo(savedGeo.getDecLong()));
	}

	@Test
	public void testSetLocationDataAndSaveCoordinates() throws Exception {
		Specimen s = createSpecimen("TEST_LOC_001");
		SpecimenController controller = new SpecimenController(s);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Transfer location data including coordinates
		pane.setLocationData("VerbatimLoc", "Zurich City", "Switzerland", "Zurich", "47.3769", "8.5417");

		Method getLatFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLat");
		getLatFieldMethod.setAccessible(true);
		JTextField latField = (JTextField) getLatFieldMethod.invoke(pane);

		Method getLongFieldMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("getTextFieldDecimalLong");
		getLongFieldMethod.setAccessible(true);
		JTextField longField = (JTextField) getLongFieldMethod.invoke(pane);

		assertEquals("47.3769", latField.getText());
		assertEquals("8.5417", longField.getText());

		// Save
		Method saveMethod = SpecimenDetailsViewPane.class.getDeclaredMethod("save");
		saveMethod.setAccessible(true);
		boolean saved = (boolean) saveMethod.invoke(pane);
		assertTrue("Save should succeed", saved);

		// Form should be clean
		assertTrue("Form should be clean after save", pane.isClean());

		// Verify database persistence of coordinates
		Specimen loaded = sls.findById(s.getSpecimenId());
		assertNotNull(loaded);
		assertNotNull(loaded.getLatLong());
		assertFalse("LatLong set should not be empty", loaded.getLatLong().isEmpty());
		LatLong savedGeo = loaded.getLatLong().iterator().next();
		assertEquals(0, new BigDecimal("47.3769").compareTo(savedGeo.getDecLat()));
		assertEquals(0, new BigDecimal("8.5417").compareTo(savedGeo.getDecLong()));
	}
}
