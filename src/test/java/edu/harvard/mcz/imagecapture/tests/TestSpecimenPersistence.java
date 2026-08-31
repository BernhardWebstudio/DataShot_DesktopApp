package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.Determination;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 * Tests for Specimen persistence, cascading, and GenericLifeCycle
 * findByExample.
 */
public class TestSpecimenPersistence extends TestCase {

	@Test
	public void testSpecimenCRUDAndFindByExample() throws SaveFailedException {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		Specimen specimen = new Specimen();
		specimen.setBarcode("ETHZ_SPEC_42");
		specimen.setFamily("Nymphalidae");
		specimen.setSubfamily("Satyrinae");
		specimen.setGenus("Erebia");
		specimen.setSpecificEpithet("ligea");
		specimen.setCountry("Switzerland");
		specimen.setPrimaryDivison("Valais");
		specimen.setDateCreated(new Date());

		// Add collector
		Collector collector = new Collector(specimen, "E. Fischer");
		specimen.getCollectors().add(collector);

		// Add determination
		Determination det = new Determination();
		det.setSpecimen(specimen);
		det.setGenus("Erebia");
		det.setSpecificEpithet("ligea");
		det.setAuthorship("(Linnaeus, 1758)");
		det.setIdentifiedBy("E. Fischer");
		specimen.getDeterminations().add(det);

		// Add specimen part
		SpecimenPart part = new SpecimenPart();
		part.setSpecimen(specimen);
		part.setPartName("whole animal");
		part.setPreserveMethod("pinned");
		specimen.getSpecimenParts().add(part);

		session.persist(specimen);
		transaction.commit();
		session.close();

		SpecimenLifeCycle sls = new SpecimenLifeCycle();

		// 1. Find by barcode
		List<Specimen> foundByBarcode = sls.findByBarcode("ETHZ_SPEC_42");
		assertNotNull(foundByBarcode);
		assertEquals(1, foundByBarcode.size());
		Specimen loaded = foundByBarcode.get(0);
		assertEquals("Nymphalidae", loaded.getFamily());
		assertEquals("Erebia", loaded.getGenus());
		assertEquals("ligea", loaded.getSpecificEpithet());
		assertEquals(1, loaded.getCollectors().size());
		assertEquals("E. Fischer", loaded.getCollectors().iterator().next().getCollectorName());
		assertEquals(1, loaded.getDeterminations().size());
		assertEquals(1, loaded.getSpecimenParts().size());

		// 2. Scientific name assembly
		assertEquals("Erebia ligea", loaded.assembleScientificName());

		// 3. Find by example using GenericLifeCycle (tests string comparison and
		// filtering)
		Specimen example = new Specimen();
		example.clearDefaults();
		example.setFamily("Nymphalidae");
		example.setGenus("Erebia");
		List<Specimen> foundByExample = sls.findByExample(example);
		assertNotNull(foundByExample);
		assertTrue(foundByExample.size() >= 1);
		assertEquals("ETHZ_SPEC_42", foundByExample.get(0).getBarcode());

		// 4. Test findByIds with single and multiple IDs
		List<Specimen> foundByIds = sls.findByIds(List.of(loaded.getSpecimenId()));
		assertNotNull(foundByIds);
		assertEquals(1, foundByIds.size());
		assertEquals("ETHZ_SPEC_42", foundByIds.get(0).getBarcode());

		// Test findByIds with empty list
		List<Specimen> foundByEmpty = sls.findByIds(List.of());
		assertNotNull(foundByEmpty);
		assertEquals(0, foundByEmpty.size());

		// 5. Test equals & hashCode
		Specimen loadedCopy = sls.findById(loaded.getSpecimenId());
		assertEquals(loaded, loadedCopy);
		assertEquals(loaded.hashCode(), loadedCopy.hashCode());

		// 6. Test clean up / deletion
		sls.delete(loaded);
		Specimen deleted = sls.findById(loaded.getSpecimenId());
		assertNull(deleted);
	}
}
