package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.Determination;
import edu.harvard.mcz.imagecapture.entity.LatLong;
import edu.harvard.mcz.imagecapture.entity.Number;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.CopyRowButtonEditor;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import edu.harvard.mcz.imagecapture.ui.tablemodel.SpecimenListTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * End-to-end tests for the search-to-Specimen pipeline:
 * 1. Verifies that searching produces projection items, but opening them via SpecimenController
 *    and SpecimenDetailsViewPane fully hydrates all scalar and relational fields.
 * 2. Verifies that the Save button is disabled when specimen data is not fully loaded.
 * 3. Verifies that table navigation (Next/Prev) loads fully populated Specimen entities.
 * 4. Verifies that CopyRowButtonEditor copies fully populated Specimen entities.
 */
public class TestSearchToSpecimenPipeline {

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
				if (s != null && s.getSpecimenId() != null) {
					Specimen found = sls.findById(s.getSpecimenId());
					if (found != null) {
						sls.delete(found);
					}
				}
			} catch (Exception ignored) {
			}
		}
		createdSpecimens.clear();
	}

	private Specimen createComprehensiveSpecimen(String barcode, String genus, String species) throws Exception {
		Specimen s = new Specimen();
		s.setBarcode(barcode);
		s.setGenus(genus);
		s.setSpecificEpithet(species);
		s.setSubspecificEpithet("plexippus");
		s.setFamily("Nymphalidae");
		s.setSubfamily("Danainae");
		s.setTribe("Danaini");
		s.setHigherOrder("Lepidoptera");
		s.setAuthorship("Linnaeus, 1758");
		s.setCountry("United States");
		s.setPrimaryDivison("California");
		s.setSpecificLocality("Monarch Grove Sanctuary");
		s.setVerbatimLocality("Pacific Grove eucalyptus grove");
		s.setHabitat("Coastal eucalyptus grove");
		s.setMicrohabitat("High tree canopy cluster");
		s.setMinimum_elevation(15L);
		s.setMaximum_elevation(45L);
		s.setElev_units("m");
		s.setCollectingMethod("Aerial net");
		s.setIsoDate("2021-10-12");
		s.setDateCollected("12-Oct-2021");
		s.setDateNos("Fall 2021");
		s.setLifeStage("Adult");
		s.setSex("Female");
		s.setTypeStatus("Not a Type");
		s.setIdentifiedBy("L. Monarch");
		s.setNatureOfId("expert identification");
		s.setDateIdentified("2021-11-01");
		s.setCollection("MCZ-ENT");
		s.setLocationInCollection("Drawer 14B");
		s.setAssociatedTaxon("Eucalyptus globulus");
		s.setQuestions("None noted");
		s.setInferences("Overwintering site");
		s.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
		s.setDateCreated(new Date());

		// Relational child entities
		Set<Number> numbers = new HashSet<>();
		Number num = new Number();
		num.setNumber("COLL-9876");
		num.setNumberType("Collection Number");
		num.setSpecimen(s);
		numbers.add(num);
		s.setNumbers(numbers);

		Set<Collector> collectors = new HashSet<>();
		Collector col = new Collector();
		col.setCollectorName("Alexander von Humboldt");
		col.setSpecimen(s);
		collectors.add(col);
		s.setCollectors(collectors);

		Set<SpecimenPart> parts = new HashSet<>();
		SpecimenPart part = new SpecimenPart();
		part.setPartName("pinned specimen");
		part.setPreserveMethod("pin");
		part.setSpecimenId(s);
		parts.add(part);
		s.setSpecimenParts(parts);

		Set<Determination> dets = new HashSet<>();
		Determination det = new Determination();
		det.setGenus(genus);
		det.setSpecificEpithet(species);
		det.setIdentifiedBy("Expert Entomologist");
		det.setSpecimen(s);
		dets.add(det);
		s.setDeterminations(dets);

		Set<LatLong> latLongs = new HashSet<>();
		LatLong ll = new LatLong();
		ll.setDecLat(new BigDecimal("36.6177"));
		ll.setDecLong(new BigDecimal("-121.9166"));
		ll.setDatum("WGS84");
		ll.setSpecimenId(s);
		latLongs.add(ll);
		s.setLatLong(latLongs);

		sls.persist(s);
		createdSpecimens.add(s);
		return s;
	}

	/**
	 * Requirement 2: Full pipeline test from search to Specimen, asserting that all fields are populated.
	 */
	@Test
	public void testFullPipelineSearchToSpecimenWithAllFieldsPopulated() throws Exception {
		String barcode = "TP_" + (System.currentTimeMillis() % 1000000000L);
		Specimen saved = createComprehensiveSpecimen(barcode, "Danaus", "plexippus");
		assertNotNull(saved.getSpecimenId());

		// 1. Search pipeline (as executed by SearchDialog -> SpecimenBrowser)
		Map<String, Object> criteria = new HashMap<>();
		criteria.put("barcode", barcode);
		List<Specimen> searchResults = sls.findSpecimensForTable(criteria, 10, 0, false, "barcode", true);

		assertEquals(1, searchResults.size());
		Specimen tableProjection = searchResults.get(0);

		// Table projection only has 13 table columns populated; isFullyLoaded must be false
		assertFalse(tableProjection.isFullyLoaded());
		assertNull(tableProjection.getHabitat());
		assertNull(tableProjection.getMicrohabitat());
		assertNull(tableProjection.getSpecificLocality());
		assertNull(tableProjection.getCollectingMethod());
		assertNull(tableProjection.getIdentifiedBy());
		assertNull(tableProjection.getMinimum_elevation());

		// 2. Open specimen via SpecimenController (pipeline transition from table to details)
		SpecimenCache.clear(); // ensure we test loading through the controller
		SpecimenController controller = new SpecimenController(tableProjection);
		Specimen loadedSpecimen = controller.getSpecimen();

		// Specimen must now be fully loaded with all fields populated
		assertTrue("Specimen must be marked as fully loaded", loadedSpecimen.isFullyLoaded());
		assertEquals(saved.getSpecimenId(), loadedSpecimen.getSpecimenId());
		assertEquals(barcode, loadedSpecimen.getBarcode());
		assertEquals("Danaus", loadedSpecimen.getGenus());
		assertEquals("plexippus", loadedSpecimen.getSpecificEpithet());
		assertEquals("plexippus", loadedSpecimen.getSubspecificEpithet());
		assertEquals("Nymphalidae", loadedSpecimen.getFamily());
		assertEquals("Danainae", loadedSpecimen.getSubfamily());
		assertEquals("Danaini", loadedSpecimen.getTribe());
		assertEquals("Lepidoptera", loadedSpecimen.getHigherOrder());
		assertEquals("Linnaeus, 1758", loadedSpecimen.getAuthorship());
		assertEquals("United States", loadedSpecimen.getCountry());
		assertEquals("California", loadedSpecimen.getPrimaryDivison());
		assertEquals("Monarch Grove Sanctuary", loadedSpecimen.getSpecificLocality());
		assertEquals("Pacific Grove eucalyptus grove", loadedSpecimen.getVerbatimLocality());
		assertEquals("Coastal eucalyptus grove", loadedSpecimen.getHabitat());
		assertEquals("High tree canopy cluster", loadedSpecimen.getMicrohabitat());
		assertEquals(Long.valueOf(15), loadedSpecimen.getMinimum_elevation());
		assertEquals(Long.valueOf(45), loadedSpecimen.getMaximum_elevation());
		assertEquals("m", loadedSpecimen.getElev_units());
		assertEquals("Aerial net", loadedSpecimen.getCollectingMethod());
		assertEquals("2021-10-12", loadedSpecimen.getIsoDate());
		assertEquals("12-Oct-2021", loadedSpecimen.getDateCollected());
		assertEquals("Adult", loadedSpecimen.getLifeStage());
		assertEquals("Female", loadedSpecimen.getSex());
		assertEquals("Not a Type", loadedSpecimen.getTypeStatus());
		assertEquals("L. Monarch", loadedSpecimen.getIdentifiedBy());
		assertEquals("expert identification", loadedSpecimen.getNatureOfId());
		assertEquals("2021-11-01", loadedSpecimen.getDateIdentified());
		assertEquals("MCZ-ENT", loadedSpecimen.getCollection());
		assertEquals("Drawer 14B", loadedSpecimen.getLocationInCollection());
		assertEquals("Eucalyptus globulus", loadedSpecimen.getAssociatedTaxon());
		assertEquals("None noted", loadedSpecimen.getQuestions());
		assertEquals("Overwintering site", loadedSpecimen.getInferences());

		// Assert all collections are populated
		assertNotNull(loadedSpecimen.getCollectors());
		assertEquals(1, loadedSpecimen.getCollectors().size());
		assertEquals("Alexander von Humboldt", loadedSpecimen.getCollectors().iterator().next().getCollectorName());

		assertNotNull(loadedSpecimen.getNumbers());
		assertEquals(1, loadedSpecimen.getNumbers().size());
		assertEquals("COLL-9876", loadedSpecimen.getNumbers().iterator().next().getNumber());

		assertNotNull(loadedSpecimen.getSpecimenParts());
		assertEquals(1, loadedSpecimen.getSpecimenParts().size());
		assertEquals("pinned specimen", loadedSpecimen.getSpecimenParts().iterator().next().getPartName());

		assertNotNull(loadedSpecimen.getDeterminations());
		assertEquals(1, loadedSpecimen.getDeterminations().size());

		assertNotNull(loadedSpecimen.getLatLong());
		assertEquals(1, loadedSpecimen.getLatLong().size());

		// 3. Open in SpecimenDetailsViewPane
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(loadedSpecimen, controller);
		assertTrue("Pane should report data loaded successfully", pane.isDataLoadedSuccessfully());
		assertTrue("Save button should be enabled for fully loaded editable record", pane.isSaveButtonEnabled());
	}

	/**
	 * Requirement 1: Disable the save button if not all data was loaded/set correctly.
	 */
	@Test
	public void testSaveButtonDisabledWhenSpecimenNotFullyLoaded() {
		// Create a projection specimen with an ID that does NOT exist in the database
		Specimen incomplete = new Specimen(999999999L, "INCOMPLETE_BARCODE", WorkFlowStatus.STAGE_1,
				"Family", "Subfamily", "Tribe", "Genus", "species", "subspecies",
				"Country", "Division", "Locality", "Collection", "123");
		assertFalse(incomplete.isFullyLoaded());

		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(incomplete, null);
		assertFalse("Data should NOT be loaded successfully for an incomplete specimen", pane.isDataLoadedSuccessfully());
		assertNotNull(pane.getSaveJButton());
		assertFalse("Save button must be disabled when specimen data is incomplete", pane.getSaveJButton().isEnabled());
		assertFalse("isSaveButtonEnabled() must return false", pane.isSaveButtonEnabled());
	}

	/**
	 * Verifies table navigation (Next/Prev) transitions always produce fully populated Specimen entities.
	 */
	@Test
	public void testTableNavigationPipelineLoadsFullSpecimens() throws Exception {
		long ts = System.currentTimeMillis() % 1000000000L;
		Specimen s1 = createComprehensiveSpecimen("N1_" + ts, "Pieris", "rapae");
		Specimen s2 = createComprehensiveSpecimen("N2_" + ts, "Pieris", "brassicae");

		Map<String, Object> criteria = new HashMap<>();
		criteria.put("genus", "Pieris");
		List<Specimen> projections = sls.findSpecimensForTable(criteria, 10, 0, false, "barcode", true);
		assertTrue(projections.size() >= 2);

		SpecimenListTableModel model = new SpecimenListTableModel(projections);
		JTable table = new JTable(model);

		SpecimenCache.clear();
		SpecimenController controller = new SpecimenController(projections.get(0), model, table, 0, 0);

		// Current specimen in controller must be fully loaded
		assertTrue(controller.getSpecimen().isFullyLoaded());
		assertNotNull(controller.getSpecimen().getHabitat());

		// Navigate to next
		boolean moved = controller.switchToNextSpecimenInTable();
		assertTrue("Should move to next specimen", moved);
		assertTrue("Next specimen must be fully loaded", controller.getSpecimen().isFullyLoaded());
		assertNotNull("Next specimen habitat must not be null", controller.getSpecimen().getHabitat());
		assertEquals("Coastal eucalyptus grove", controller.getSpecimen().getHabitat());
	}

	/**
	 * Verifies CopyRowButtonEditor copies the fully loaded entity instead of the lightweight projection.
	 */
	@Test
	public void testCopyRowButtonEditorCopiesFullSpecimen() throws Exception {
		String barcode = "CP_" + (System.currentTimeMillis() % 1000000000L);
		createComprehensiveSpecimen(barcode, "Colias", "eurytheme");

		Map<String, Object> criteria = new HashMap<>();
		criteria.put("barcode", barcode);
		List<Specimen> projections = sls.findSpecimensForTable(criteria, 1, 0, false, "barcode", true);
		assertEquals(1, projections.size());

		Specimen projection = projections.get(0);
		assertFalse(projection.isFullyLoaded());

		CopyRowButtonEditor editor = new CopyRowButtonEditor(new JCheckBox());
		JTable table = new JTable(new SpecimenListTableModel(projections));
		editor.getTableCellEditorComponent(table, projection, false, 0, 0);
		editor.getCellEditorValue();

		Specimen cached = ImageCaptureApp.lastEditedSpecimenCache;
		assertNotNull("Cached specimen should not be null", cached);
		assertTrue("Cached specimen must be fully loaded", cached.isFullyLoaded());
		assertEquals("Coastal eucalyptus grove", cached.getHabitat());
		assertEquals("Alexander von Humboldt", cached.getCollectors().iterator().next().getCollectorName());
	}
}
