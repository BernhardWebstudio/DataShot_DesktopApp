package edu.harvard.mcz.imagecapture.tests;

import static org.junit.Assert.*;

import edu.harvard.mcz.imagecapture.SpecimenController;
import edu.harvard.mcz.imagecapture.data.SpecimenCache;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import edu.harvard.mcz.imagecapture.ui.frame.SpecimenDetailsViewPane;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for: 1. Deferred freshness verification of cached specimens in
 * SpecimenDetailsViewPane. 2. Optimistic locking handling when concurrent
 * updates occur across devices. 3. Graceful UI handling (no crash, clear
 * notification to user) upon optimistic lock conflict.
 */
public class TestSpecimenOptimisticLockingAndVerification {

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

	private Specimen createTestSpecimen(String barcode, String drawer) throws Exception {
		Specimen s = new Specimen();
		s.setBarcode(barcode);
		s.setGenus("Papilio");
		s.setSpecificEpithet("machaon");
		s.setFamily("Papilionidae");
		s.setDrawerNumber(drawer);
		s.setTypeStatus("Not a Type");
		s.setWorkFlowStatus(WorkFlowStatus.STAGE_1);
		s.setDateCreated(new Date());
		sls.persist(s);
		createdSpecimens.add(s);
		return s;
	}

	/**
	 * Test that when a specimen is opened from cache, the save button is disabled
	 * until freshness is verified, and once verified against an up-to-date DB, the
	 * save button is enabled.
	 */
	@Test
	public void testSpecimenFreshnessVerificationWhenFresh() throws Exception {
		Specimen original = createTestSpecimen("TEST_FRESH_001", "DRAWER_1");
		Long specimenId = original.getSpecimenId();

		// Put fully loaded specimen into cache
		Specimen cached = sls.findById(specimenId);
		SpecimenCache.put(cached);

		// Create lightweight projection as would be in the table
		Specimen projection = new Specimen(specimenId, "TEST_FRESH_001", WorkFlowStatus.STAGE_1, "Papilionidae", null,
				null, "Papilio", "machaon", null, null, null, null, null, null);

		SpecimenController controller = new SpecimenController(projection);
		assertTrue("Controller should report loaded from cache", controller.isLoadedFromCache());

		// Open pane from cache
		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);
		assertFalse("Save button must be disabled while verifying", pane.isSaveButtonEnabled());
		assertFalse("Pane should not be verified yet", pane.isVerified());

		// Await freshness verification
		boolean verified = pane.verifyFreshness();
		assertTrue("Verification should succeed", verified);
		assertTrue("Pane should now be verified", pane.isVerified());
		assertTrue("Save button should now be enabled after verification passes", pane.isSaveButtonEnabled());
	}

	/**
	 * Test that when a cached specimen is stale (modified by another
	 * device/session), freshness verification detects it, reloads the fresh entity,
	 * updates the form and cache, and enables the save button with fresh data.
	 */
	@Test
	public void testSpecimenFreshnessVerificationReloadsWhenStale() throws Exception {
		Specimen original = createTestSpecimen("TEST_STALE_001", "OLD_DRW");
		Long specimenId = original.getSpecimenId();

		// Put old specimen into cache
		Specimen cached = sls.findById(specimenId);
		SpecimenCache.put(cached);

		// Simulate another user updating the specimen in the database
		Specimen otherUserCopy = sls.findById(specimenId);
		otherUserCopy.setDrawerNumber("NEW_DRW");
		otherUserCopy.setDateLastUpdated(new Date(System.currentTimeMillis() + 5000));
		sls.attachDirty(otherUserCopy);

		// Create projection and controller from cache
		Specimen projection = new Specimen(specimenId, "TEST_STALE_001", WorkFlowStatus.STAGE_1, "Papilionidae", null,
				null, "Papilio", "machaon", null, null, null, null, null, null);
		SpecimenController controller = new SpecimenController(projection);
		assertTrue(controller.isLoadedFromCache());

		// Initially, cached copy has OLD_DRW
		assertEquals("OLD_DRW", controller.getSpecimen().getDrawerNumber());

		SpecimenDetailsViewPane pane = new SpecimenDetailsViewPane(controller.getSpecimen(), controller);
		assertFalse("Save button must be disabled initially while verifying", pane.isSaveButtonEnabled());

		// Run freshness verification: should detect version/timestamp change and reload
		boolean verified = pane.verifyFreshness();
		assertTrue("Verification should complete successfully", verified);
		assertTrue("Pane should be marked verified", pane.isVerified());
		assertTrue("Save button should be enabled after reloading fresh data", pane.isSaveButtonEnabled());

		// Verify that pane and controller now hold the reloaded, fresh data
		assertEquals("NEW_DRW", pane.getSpecimen().getDrawerNumber());
		assertEquals("NEW_DRW", controller.getSpecimen().getDrawerNumber());
		assertEquals("NEW_DRW", SpecimenCache.get(specimenId).getDrawerNumber());
	}

	/**
	 * Test that if concurrent edits occur and optimistic locking fails when saving:
	 * 1. The application does not crash (no unhandled exception). 2. pane.save()
	 * returns false. 3. The user is clearly notified with an appropriate warning on
	 * the UI status bar. 4. SpecimenCache entry for the conflicted record is
	 * invalidated.
	 */
	@Test
	public void testOptimisticLockingFailureHandling() throws Exception {
		Specimen original = createTestSpecimen("TEST_CONCURRENT_001", "INIT_DRW");
		Long specimenId = original.getSpecimenId();

		// Device A loads instance A (version 0)
		Specimen instanceA = sls.findById(specimenId);
		assertNotNull(instanceA.getVersion());
		assertEquals(Integer.valueOf(0), instanceA.getVersion());

		// Device B loads instance B (version 0)
		Specimen instanceB = sls.findById(specimenId);
		assertEquals(Integer.valueOf(0), instanceB.getVersion());

		// Device B modifies and saves (version becomes 1 in database)
		instanceB.setDrawerNumber("DRW_B");
		sls.attachDirty(instanceB);

		// Verify database has version 1
		Specimen inDb = sls.findById(specimenId);
		assertEquals(Integer.valueOf(1), inDb.getVersion());
		assertEquals("DRW_B", inDb.getDrawerNumber());

		// Device A opens instance A in SpecimenDetailsViewPane (still holding version
		// 0)
		SpecimenController controllerA = new SpecimenController(instanceA);
		SpecimenDetailsViewPane paneA = new SpecimenDetailsViewPane(instanceA, controllerA);

		// Ensure pane is verified so save is attempted
		paneA.setVerified(true);
		assertTrue(paneA.isSaveButtonEnabled());

		// Device A modifies instance A and attempts to save
		instanceA.setDrawerNumber("DRW_A");

		// Attempt save from Device A: must NOT crash, must return false
		boolean saveResult = false;
		try {
			saveResult = paneA.save();
		} catch (Exception ex) {
			fail("Application must NOT crash on optimistic lock failure, but threw: " + ex);
		}

		assertFalse("Save must fail due to optimistic lock conflict", saveResult);

		// Verify that user is clearly informed via status text
		String statusText = paneA.getStatusText();
		assertNotNull("Status text must not be null", statusText);
		assertTrue("Status text must inform user of concurrent modification, but was: " + statusText,
				statusText.contains("modified externally") || statusText.contains("another user"));

		// Verify that SpecimenCache for this ID is invalidated
		assertNull("Conflicted specimen in cache should be invalidated", SpecimenCache.get(specimenId));

		// Verify database was NOT overwritten with Device A's stale changes
		Specimen stillInDb = sls.findById(specimenId);
		assertEquals("DRW_B", stillInDb.getDrawerNumber());
		assertEquals(Integer.valueOf(1), stillInDb.getVersion());
	}
}
