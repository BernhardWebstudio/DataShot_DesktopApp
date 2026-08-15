package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.LifeStage;
import edu.harvard.mcz.imagecapture.entity.fixed.Sex;
import edu.harvard.mcz.imagecapture.ui.binding.FormBindingContext;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import junit.framework.TestCase;

/**
 * Unit tests for {@link FormBindingContext} and declarative Swing form
 * bindings.
 */
public class TestFormBindingContext extends TestCase {

	public TestFormBindingContext(String name) {
		super(name);
	}

	/**
	 * Test two-way binding of text fields (Model -> UI and UI -> Model).
	 */
	public void testTextFieldBindingReadAndWrite() {
		AtomicBoolean dirtyCalled = new AtomicBoolean(false);
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true,
				() -> dirtyCalled.set(true));

		JTextField genusField = context.bindTextField("Genus", Specimen::getGenus, Specimen::setGenus);
		JTextField speciesField = context.bindTextField("SpecificEpithet", Specimen::getSpecificEpithet,
				Specimen::setSpecificEpithet);

		// Tooltip should be auto-populated from MetadataRetriever
		assertNotNull(genusField.getToolTipText());
		assertTrue(genusField.isEditable());

		// Populate model
		Specimen specimen = new Specimen();
		specimen.setGenus("Papilio");
		specimen.setSpecificEpithet("machaon");

		// Synchronize Model -> UI
		context.readFrom(specimen);
		assertEquals("Papilio", genusField.getText());
		assertEquals("machaon", speciesField.getText());
		// dirty callback must NOT be triggered by readFrom
		assertFalse(dirtyCalled.get());

		// Update UI and synchronize UI -> Model
		genusField.setText("Morpho");
		speciesField.setText("hecuba");
		assertTrue(dirtyCalled.get());

		context.writeTo(specimen);
		assertEquals("Morpho", specimen.getGenus());
		assertEquals("hecuba", specimen.getSpecificEpithet());
	}

	/**
	 * Test Long field binding with parsing and null/empty handling.
	 */
	public void testLongFieldBinding() {
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true, null);
		JTextField minElevField = context.bindLongField("VerbatimElevation", Specimen::getMinimum_elevation,
				Specimen::setMinimum_elevation, null);

		Specimen specimen = new Specimen();
		specimen.setMinimum_elevation(1250L);

		// Model -> UI
		context.readFrom(specimen);
		assertEquals("1250", minElevField.getText());

		// UI -> Model with new value
		minElevField.setText("3400");
		context.writeTo(specimen);
		assertEquals(Long.valueOf(3400L), specimen.getMinimum_elevation());

		// UI -> Model with invalid / blank text sets null
		minElevField.setText("");
		context.writeTo(specimen);
		assertNull(specimen.getMinimum_elevation());

		minElevField.setText("not-a-number");
		context.writeTo(specimen);
		assertNull(specimen.getMinimum_elevation());
	}

	/**
	 * Test CheckBox binding for boolean properties.
	 */
	public void testCheckBoxBinding() {
		AtomicInteger dirtyCount = new AtomicInteger(0);
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true,
				dirtyCount::incrementAndGet);

		JCheckBox validDistBox = context.bindCheckBox("ValidDistributionFlag", Specimen::getValidDistributionFlag,
				Specimen::setValidDistributionFlag);

		Specimen specimen = new Specimen();
		specimen.setValidDistributionFlag(true);

		context.readFrom(specimen);
		assertTrue(validDistBox.isSelected());
		assertEquals(0, dirtyCount.get());

		validDistBox.setSelected(false);
		context.writeTo(specimen);
		assertEquals(Boolean.FALSE, specimen.getValidDistributionFlag());
	}

	/**
	 * Test TextArea binding for multiline notes.
	 */
	public void testTextAreaBinding() {
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true, null);
		JTextArea notesArea = context.bindTextArea("SpecimenNotes", Specimen::getSpecimenNotes,
				Specimen::setSpecimenNotes, 3);

		Specimen specimen = new Specimen();
		specimen.setSpecimenNotes("Collected near river bank.\nUnder bark.");

		context.readFrom(specimen);
		assertEquals("Collected near river bank.\nUnder bark.", notesArea.getText());

		notesArea.setText("Updated notes line 1.\nLine 2.");
		context.writeTo(specimen);
		assertEquals("Updated notes line 1.\nLine 2.", specimen.getSpecimenNotes());
	}

	/**
	 * Test ComboBox binding with static pick lists.
	 */
	public void testComboBoxBinding() {
		AtomicBoolean dirtyCalled = new AtomicBoolean(false);
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true,
				() -> dirtyCalled.set(true));

		JComboBox<String> sexBox = context.bindComboBox("Sex", Sex.getSexValues(), Specimen::getSex,
				(s, val) -> s.setSex(val == null ? "" : val));

		JComboBox<String> lifeStageBox = context.bindComboBox("Lifestage", LifeStage.getLifeStageValues(),
				Specimen::getLifeStage, (s, val) -> s.setLifeStage(val == null ? "" : val));

		Specimen specimen = new Specimen();
		specimen.setSex("Female");
		specimen.setLifeStage("adult");

		context.readFrom(specimen);
		assertEquals("Female", sexBox.getSelectedItem());
		assertEquals("adult", lifeStageBox.getSelectedItem());
		assertFalse(dirtyCalled.get());

		sexBox.setSelectedItem("Male");
		assertTrue(dirtyCalled.get());

		context.writeTo(specimen);
		assertEquals("Male", specimen.getSex());
	}

	/**
	 * Test Read-Only text field binding.
	 */
	public void testReadOnlyTextFieldBinding() {
		FormBindingContext<Specimen> context = new FormBindingContext<>(Specimen.class, true, null);
		JTextField barcodeField = context.bindReadOnlyTextField("Barcode", Specimen::getBarcode);

		assertFalse(barcodeField.isEditable());

		Specimen specimen = new Specimen();
		specimen.setBarcode("MCZ-ENT00012345");

		context.readFrom(specimen);
		assertEquals("MCZ-ENT00012345", barcodeField.getText());

		// writeTo should not alter barcode on target
		Specimen target = new Specimen();
		target.setBarcode("ORIGINAL");
		barcodeField.setText("CHANGED");
		context.writeTo(target);
		assertEquals("ORIGINAL", target.getBarcode());
	}

	/**
	 * Test Users entity binding used in UserDialog.
	 */
	public void testUsersBinding() {
		FormBindingContext<edu.harvard.mcz.imagecapture.entity.Users> context = new FormBindingContext<>(
				edu.harvard.mcz.imagecapture.entity.Users.class, true);

		JTextField usernameField = context.bindTextField("username",
				edu.harvard.mcz.imagecapture.entity.Users::getUsername,
				edu.harvard.mcz.imagecapture.entity.Users::setUsername);
		JTextField fullnameField = context.bindTextField("fullname",
				edu.harvard.mcz.imagecapture.entity.Users::getFullname,
				edu.harvard.mcz.imagecapture.entity.Users::setFullname);
		JTextField descField = context.bindTextField("description",
				edu.harvard.mcz.imagecapture.entity.Users::getDescription,
				edu.harvard.mcz.imagecapture.entity.Users::setDescription);

		edu.harvard.mcz.imagecapture.entity.Users user = new edu.harvard.mcz.imagecapture.entity.Users();
		user.setUsername("jdoe");
		user.setFullname("John Doe");
		user.setDescription("Digitizer");

		context.readFrom(user);
		assertEquals("jdoe", usernameField.getText());
		assertEquals("John Doe", fullnameField.getText());
		assertEquals("Digitizer", descField.getText());

		fullnameField.setText("Jane Doe");
		descField.setText("Senior Digitizer");
		context.writeTo(user);
		assertEquals("Jane Doe", user.getFullname());
		assertEquals("Senior Digitizer", user.getDescription());
	}

	/**
	 * Test LatLong entity binding used in GeoreferenceDialog.
	 */
	public void testLatLongBinding() {
		FormBindingContext<edu.harvard.mcz.imagecapture.entity.LatLong> context = new FormBindingContext<>(
				edu.harvard.mcz.imagecapture.entity.LatLong.class, true);

		JTextField latField = context.bindTextField("DecLat",
				edu.harvard.mcz.imagecapture.entity.LatLong::getDecLatString, (g, str) -> {
					if (str != null && !str.trim().isEmpty()) {
						try {
							g.setDecLat(java.math.BigDecimal.valueOf(Double.parseDouble(str.trim())));
						} catch (NumberFormatException ignored) {
						}
					} else {
						g.setDecLat(null);
					}
				});
		JTextField longField = context.bindTextField("DecLong",
				edu.harvard.mcz.imagecapture.entity.LatLong::getDecLongString, (g, str) -> {
					if (str != null && !str.trim().isEmpty()) {
						try {
							g.setDecLong(java.math.BigDecimal.valueOf(Double.parseDouble(str.trim())));
						} catch (NumberFormatException ignored) {
						}
					} else {
						g.setDecLong(null);
					}
				});
		JComboBox<String> methodBox = context.bindComboBox("Georefmethod",
				new String[]{"GPS", "GEOLocate", "Google Maps"},
				edu.harvard.mcz.imagecapture.entity.LatLong::getGeorefmethod,
				edu.harvard.mcz.imagecapture.entity.LatLong::setGeorefmethod);

		edu.harvard.mcz.imagecapture.entity.LatLong latLong = new edu.harvard.mcz.imagecapture.entity.LatLong();
		latLong.setDecLat(java.math.BigDecimal.valueOf(42.3736));
		latLong.setDecLong(java.math.BigDecimal.valueOf(-71.1097));
		latLong.setGeorefmethod("GPS");

		context.readFrom(latLong);
		assertEquals("42.3736", latField.getText());
		assertEquals("-71.1097", longField.getText());
		assertEquals("GPS", methodBox.getSelectedItem());

		latField.setText("42.3800");
		longField.setText("-71.1200");
		methodBox.setSelectedItem("GEOLocate");
		context.writeTo(latLong);

		assertEquals(java.math.BigDecimal.valueOf(42.38), latLong.getDecLat());
		assertEquals(java.math.BigDecimal.valueOf(-71.12), latLong.getDecLong());
		assertEquals("GEOLocate", latLong.getGeorefmethod());
	}

	/**
	 * Test SpecimenPartAttribute entity binding used in
	 * SpecimenPartAttribEditDialog.
	 */
	public void testSpecimenPartAttributeBinding() {
		FormBindingContext<edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute> context = new FormBindingContext<>(
				edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute.class, true);

		JComboBox<String> typeBox = context.bindComboBox("attributeType",
				new String[]{"caste", "scientific name", "sex", "life stage"},
				edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute::getAttributeType,
				edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute::setAttributeType);
		JTextField unitsField = context.bindTextField("attributeUnits",
				edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute::getAttributeUnits,
				edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute::setAttributeUnits);

		edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute attr = new edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute();
		attr.setAttributeType("caste");
		attr.setAttributeUnits("mm");

		context.readFrom(attr);
		assertEquals("caste", typeBox.getSelectedItem());
		assertEquals("mm", unitsField.getText());

		typeBox.setSelectedItem("sex");
		unitsField.setText("cm");
		context.writeTo(attr);

		assertEquals("sex", attr.getAttributeType());
		assertEquals("cm", attr.getAttributeUnits());
	}

	/**
	 * Test CitationData binding used in CitedInDialog.
	 */
	public void testCitationDataBinding() {
		edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData citation = new edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData(
				"Nature 2024", "https://nature.com/paper", "Described new species", "2024");
		FormBindingContext<edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData> context = new FormBindingContext<>(
				edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData.class, true);

		JTextField pubField = context.bindTextField(null, "citedInPublication",
				edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData::getCitedInPublication,
				edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData::setCitedInPublication, null);
		JTextField yearField = context.bindTextField(null, "citedInYear",
				edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData::getCitedInYear,
				edu.harvard.mcz.imagecapture.ui.dialog.CitedInDialog.CitationData::setCitedInYear, null);

		context.readFrom(citation);
		assertEquals("Nature 2024", pubField.getText());
		assertEquals("2024", yearField.getText());

		pubField.setText("Science 2025");
		yearField.setText("2025");
		context.writeTo(citation);

		assertEquals("Science 2025", citation.getCitedInPublication());
		assertEquals("2025", citation.getCitedInYear());
	}
}
