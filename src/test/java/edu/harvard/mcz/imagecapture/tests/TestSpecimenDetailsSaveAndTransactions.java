package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.CollectorTableModel;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenPartsTableModel;
import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for SpecimenDetailsViewPane saving functionality and database
 * transaction safety: 1. Updating coordinates and georeference metadata on an
 * existing Specimen (testing the UPDATE LAT_LONG path). 2. Clearing coordinates
 * on an existing Specimen. 3. Saving modified SpecimenParts from the parts
 * table. 4. Saving modified Collectors from the collectors table. 5. Error
 * handling when save encounters SaveFailedException (dirty state retained,
 * warning displayed). 6. SpecimenLifeCycle.attachDirty cascaded transaction
 * updates and rollback behavior. 7. Verification that database schema migration
 * V2.0.5 converts legacy MyISAM tables to InnoDB to avoid MySQL GTID errors.
 */
public class TestSpecimenDetailsSaveAndTransactions {

	private SpecimenLifeCycle sls;
	private final List<Specimen> createdSpecimens = new ArrayList<>();

	@Before
	public void setUp() {
		SpecimenCache.clear();
		sls = new SpecimenLifeCycle();
	}

	@After
	public void tearDown() {
		SpecimenCache.clear();
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

	private Specimen createAndPersistSpecimenWithCoords(String barcode, BigDecimal lat, BigDecimal lon)
			throws Exception {
		Specimen s = new Specimen();
		s.setBarcode(barcode);
		s.setGenus("Pieris");
		s.setSpecificEpithet("rapae");
		s.setFamily("Pieridae");
		s.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
		s.setDateCreated(new Date());

		LatLong geo = new LatLong();
		geo.setSpecimen(s);
		geo.setDecLat(lat);
		geo.setDecLong(lon);
		geo.setDatum("WGS84");
		geo.setGeorefmethod("GPS");
		geo.setMaxErrorDistance(100);
		geo.setMaxErrorUnits("m");

		Set<LatLong> geos = new HashSet<>();
		geos.add(geo);
		s.setLatLong(geos);

		sls.persist(s);
		createdSpecimens.add(s);
		return s;
	}

	private JTextField getTextField(SpecimenDetailsViewPane pane, String methodName) throws Exception {
		Method m = SpecimenDetailsViewPane.class.getDeclaredMethod(methodName);
		m.setAccessible(true);
		return (JTextField) m.invoke(pane);
	}

	@SuppressWarnings("unchecked")
	private JComboBox<String> getComboBox(SpecimenDetailsViewPane pane, String methodName) throws Exception {
		Method m = SpecimenDetailsViewPane.class.getDeclaredMethod(methodName);
		m.setAccessible(true);
		return (JComboBox<String>) m.invoke(pane);
	}

	private JTable getTable(SpecimenDetailsViewPane pane, String methodName) throws Exception {
		Method m = SpecimenDetailsViewPane.class.getDeclaredMethod(methodName);
		m.setAccessible(true);
		return (JTable) m.invoke(pane);
	}

	/**
	 * Test that updating coordinates on an existing Specimen in
	 * SpecimenDetailsViewPane properly performs an update on the cascaded LatLong
	 * entity and persists into the database.
	 */
	@Test
	public void testSaveExistingSpecimenWithUpdatedLatLong() throws Exception {
		Specimen s = createAndPersistSpecimenWithCoords("SAVE_TEST_COORD_001", new BigDecimal("46.500000"),
				new BigDecimal("8.500000"));
		Long id = s.getSpecimenId();

		// Load fresh from database
		Specimen loaded = sls.findById(id);
		assertNotNull(loaded);
		assertEquals(1, loaded.getLatLong().size());

		SpecimenController controller = new SpecimenController(loaded);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Verify initial form values
		JTextField latField = getTextField(pane, "getTextFieldDecimalLat");
		JTextField longField = getTextField(pane, "getTextFieldDecimalLong");
		assertEquals(0, new BigDecimal("46.500000").compareTo(new BigDecimal(latField.getText().trim())));
		assertEquals(0, new BigDecimal("8.500000").compareTo(new BigDecimal(longField.getText().trim())));

		// Update coordinates and metadata in UI
		latField.setText("47.376900");
		longField.setText("8.541700");
		getComboBox(pane, "getDatumComboBox").setSelectedItem("NAD83");
		getComboBox(pane, "getMethodComboBox").setSelectedItem("OpenStreetMap Nominatim");
		getTextField(pane, "getTxtErrorRadius").setText("50");
		getComboBox(pane, "getErrorUnitComboBox").setSelectedItem("m");

		// Execute save
		boolean saved = pane.save();
		assertTrue("Save should succeed", saved);
		assertTrue("Pane should be clean after save", pane.isClean());
		assertEquals("Status should be Saved", "Saved", pane.getStatusText());

		// Verify in database
		Specimen reloaded = sls.findById(id);
		assertNotNull(reloaded);
		assertNotNull(reloaded.getLatLong());
		assertEquals(1, reloaded.getLatLong().size());
		LatLong updatedGeo = reloaded.getLatLong().iterator().next();
		assertEquals(0, new BigDecimal("47.376900").compareTo(updatedGeo.getDecLat()));
		assertEquals(0, new BigDecimal("8.541700").compareTo(updatedGeo.getDecLong()));
		assertEquals("NAD83", updatedGeo.getDatum());
		assertEquals("OpenStreetMap Nominatim", updatedGeo.getGeorefmethod());
		assertEquals(Integer.valueOf(50), updatedGeo.getMaxErrorDistance());
		assertEquals("m", updatedGeo.getMaxErrorUnits());
	}

	/**
	 * Test that clearing coordinate text fields in SpecimenDetailsViewPane
	 * nullifies the latitude, longitude, and error distance in the database.
	 */
	@Test
	public void testClearCoordinatesOnExistingSpecimenAndSave() throws Exception {
		Specimen s = createAndPersistSpecimenWithCoords("SAVE_TEST_CLEAR_001", new BigDecimal("46.500000"),
				new BigDecimal("8.500000"));
		Long id = s.getSpecimenId();

		Specimen loaded = sls.findById(id);
		SpecimenController controller = new SpecimenController(loaded);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		// Clear lat and long fields
		JTextField latField = getTextField(pane, "getTextFieldDecimalLat");
		JTextField longField = getTextField(pane, "getTextFieldDecimalLong");
		JTextField errRadField = getTextField(pane, "getTxtErrorRadius");
		latField.setText("");
		longField.setText("");
		errRadField.setText("");

		boolean saved = pane.save();
		assertTrue("Save should succeed when coordinates cleared", saved);
		assertTrue("Pane should be clean after save", pane.isClean());

		// Verify in database
		Specimen reloaded = sls.findById(id);
		assertNotNull(reloaded);
		assertEquals(1, reloaded.getLatLong().size());
		LatLong clearedGeo = reloaded.getLatLong().iterator().next();
		assertNull("decLat should be null after clearing", clearedGeo.getDecLat());
		assertNull("decLong should be null after clearing", clearedGeo.getDecLong());
		assertNull("maxErrorDistance should be null after clearing", clearedGeo.getMaxErrorDistance());
	}

	/**
	 * Test that saving updates to SpecimenParts from SpecimenDetailsViewPane
	 * persists into the database properly.
	 */
	@Test
	public void testSaveSpecimenWithSpecimenPartsModification() throws Exception {
		Specimen s = new Specimen();
		s.setBarcode("SAVE_TEST_PART_001");
		s.setGenus("Colias");
		s.setSpecificEpithet("croceus");
		s.setFamily("Pieridae");
		s.setDateCreated(new Date());

		SpecimenPart part = new SpecimenPart();
		part.setSpecimen(s);
		part.setPartName("whole animal");
		part.setPreserveMethod("pinned");
		part.setLotCount(1);
		s.getSpecimenParts().add(part);

		sls.persist(s);
		createdSpecimens.add(s);
		Long id = s.getSpecimenId();

		Specimen loaded = sls.findById(id);
		SpecimenController controller = new SpecimenController(loaded);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		JTable partsTable = getTable(pane, "getJTableSpecimenParts");
		assertNotNull(partsTable);
		SpecimenPartsTableModel model = (SpecimenPartsTableModel) partsTable.getModel();
		assertEquals(1, model.getRowCount());

		// Modify part in table model
		model.setValueAt("wing", 0, 0); // PartName
		model.setValueAt("envelope", 0, 1); // PreserveMethod
		model.setValueAt(5, 0, 2); // LotCount

		boolean saved = pane.save();
		assertTrue("Save should succeed after modifying specimen part", saved);
		assertTrue("Pane should be clean", pane.isClean());

		// Verify in database
		Specimen reloaded = sls.findById(id);
		assertNotNull(reloaded);
		assertEquals(1, reloaded.getSpecimenParts().size());
		SpecimenPart updatedPart = reloaded.getSpecimenParts().iterator().next();
		assertEquals("wing", updatedPart.getPartName());
		assertEquals("envelope", updatedPart.getPreserveMethod());
		assertEquals(5, (int) updatedPart.getLotCount());
	}

	/**
	 * Test that saving updates to Collectors from SpecimenDetailsViewPane properly
	 * persists into the database.
	 */
	@Test
	public void testSaveSpecimenWithCollectorsModification() throws Exception {
		Specimen s = new Specimen();
		s.setBarcode("SAVE_TEST_COL_001");
		s.setGenus("Vanessa");
		s.setSpecificEpithet("cardui");
		s.setFamily("Nymphalidae");
		s.setDateCreated(new Date());

		Collector col1 = new Collector(s, "Collector Alpha");
		s.getCollectors().add(col1);

		sls.persist(s);
		createdSpecimens.add(s);
		Long id = s.getSpecimenId();

		Specimen loaded = sls.findById(id);
		SpecimenController controller = new SpecimenController(loaded);
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);

		JTable collectorsTable = getTable(pane, "getJTableCollectors");
		assertNotNull(collectorsTable);
		CollectorTableModel colModel = (CollectorTableModel) collectorsTable.getModel();
		assertEquals(1, colModel.getRowCount());

		// Add a second collector
		colModel.addCollector(new Collector(loaded, "Collector Beta"));

		boolean saved = pane.save();
		assertTrue("Save should succeed with new collector", saved);
		assertTrue("Pane should be clean", pane.isClean());

		// Verify in database
		Specimen reloaded = sls.findById(id);
		assertNotNull(reloaded);
		assertEquals(2, reloaded.getCollectors().size());
	}

	/**
	 * Test error handling in SpecimenDetailsViewPane.save() when
	 * SaveFailedException occurs: ensures form remains dirty, warning status is
	 * shown, and save returns false.
	 */
	@Test
	public void testSaveFailureErrorHandlingAndWarning() throws Exception {
		Specimen s = new Specimen();
		s.setBarcode("SAVE_TEST_FAIL_001");
		s.setGenus("Danaus");
		s.setSpecificEpithet("plexippus");
		s.setFamily("Nymphalidae");
		s.setDateCreated(new Date());
		sls.persist(s);
		createdSpecimens.add(s);

		Specimen loaded = sls.findById(s.getSpecimenId());

		// Create a mock controller that throws SaveFailedException
		SpecimenController failingController = new SpecimenController(loaded) {
			@Override
			public void save() throws SaveFailedException {
				throw new SaveFailedException(
						"Statement violates GTID consistency: Updates to non-transactional tables");
			}
		};

		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(failingController.getSpecimen(), failingController);

		// Execute save
		boolean saved = pane.save();
		assertFalse("Save must return false when SaveFailedException occurs", saved);
		assertFalse("Form must NOT be clean after failed save (protect user work)", pane.isClean());
		assertNotNull(pane.getStatusText());
		assertTrue("Warning must mention the save failure",
				pane.getStatusText().contains("Statement violates GTID consistency")
						|| pane.getStatusText().contains("Error:"));
	}

	/**
	 * Test SpecimenLifeCycle.attachDirty cascaded update and transaction integrity.
	 */
	@Test
	public void testAttachDirtyCascadedUpdate() throws Exception {
		Specimen s = createAndPersistSpecimenWithCoords("SAVE_TEST_ATTACH_001", new BigDecimal("10.0"),
				new BigDecimal("20.0"));
		Long id = s.getSpecimenId();

		Specimen loaded = sls.findById(id);
		loaded.setCountry("Switzerland");
		LatLong geo = loaded.getLatLong().iterator().next();
		geo.setDecLat(new BigDecimal("46.8182"));
		geo.setDecLong(new BigDecimal("8.2275"));

		// attachDirty should commit both specimen and latlong in the same transaction
		sls.attachDirty(loaded);

		Specimen verified = sls.findById(id);
		assertEquals("Switzerland", verified.getCountry());
		LatLong verifiedGeo = verified.getLatLong().iterator().next();
		assertEquals(0, new BigDecimal("46.8182").compareTo(verifiedGeo.getDecLat()));
		assertEquals(0, new BigDecimal("8.2275").compareTo(verifiedGeo.getDecLong()));
	}

	/**
	 * Test that Flyway migration V2.0.5 exists and converts all legacy MyISAM
	 * tables to InnoDB, ensuring full GTID consistency and ACID transaction
	 * compatibility.
	 */
	@Test
	public void testFlywayMigrationInnoDBConversionScript() throws Exception {
		File migrationFile = new File("src/main/resources/db/migration/V2.0.5__convert_legacy_tables_to_innodb.sql");
		assertTrue("V2.0.5 migration script must exist", migrationFile.exists());

		String sql = Files.readString(migrationFile.toPath());

		// Verify all 12 legacy MyISAM tables are converted to InnoDB
		String[] requiredTables = {"LAT_LONG", "Specimen_Part", "Specimen_Part_Attribute", "HIGHER_TAXON",
				"MCZBASE_AUTH_AGENT_NAME", "MCZBASE_GEOG_AUTH_REC", "UNIT_TRAY_LABEL", "Users", "Template", "Label",
				"LabelTag", "Tag"};

		for (String table : requiredTables) {
			assertTrue("Migration must convert table " + table + " to InnoDB",
					sql.contains("ALTER TABLE `" + table + "` ENGINE=InnoDB")
							|| sql.contains("ALTER TABLE " + table + " ENGINE=InnoDB"));
		}

		assertTrue("Migration must register version 2.0.5 in allowed_version", sql.contains("'2.0.5'"));
	}
}
