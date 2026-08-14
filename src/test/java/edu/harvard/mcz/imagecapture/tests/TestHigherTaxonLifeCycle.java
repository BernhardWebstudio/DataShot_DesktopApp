package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.HigherTaxon;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle;
import java.util.Date;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

/**
 * Tests for HigherTaxonLifeCycle query and soundex matching methods.
 */
public class TestHigherTaxonLifeCycle extends TestCase {

	@Test
	public void testHigherTaxonMatchingAndQueries() {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		HigherTaxon ht = new HigherTaxon();
		ht.setFamily("Papilionidae");
		ht.setSubfamily("Papilioninae");
		ht.setTribe("Papilionini");
		ht.setHasCastes(0);
		session.persist(ht);

		HigherTaxon ht2 = new HigherTaxon();
		ht2.setFamily("Formicidae");
		ht2.setSubfamily("Myrmicinae");
		ht2.setTribe("Attini");
		ht2.setHasCastes(1);
		session.persist(ht2);

		Specimen specimen = new Specimen();
		specimen.setBarcode("ETHZ_TEST_001");
		specimen.setDateCreated(new Date());
		specimen.setFamily("Papilionidae");
		specimen.setSubfamily("Papilioninae");
		specimen.setTribe("Papilionini");
		session.persist(specimen);

		transaction.commit();
		session.close();

		HigherTaxonLifeCycle lifeCycle = new HigherTaxonLifeCycle();

		// Test matching
		assertTrue(lifeCycle.isMatched("Papilionidae", "Papilioninae"));
		assertTrue(lifeCycle.isMatched("Papilionidae", "Papilioninae", "Papilionini"));
		assertFalse(lifeCycle.isMatched("Nymphalidae", "Papilioninae"));

		// Test findMatch
		String matchedFamily = lifeCycle.findMatch("Papilionidae");
		assertEquals("Papilionidae", matchedFamily);

		String[] matchFamSubfam = lifeCycle.findMatch("Papilionidae", "Papilioninae");
		assertNotNull(matchFamSubfam);
		assertEquals(2, matchFamSubfam.length);
		assertEquals("Papilionidae", matchFamSubfam[0]);
		assertEquals("Papilioninae", matchFamSubfam[1]);

		String[] matchTriple = lifeCycle.findMatch("Papilionidae", "Papilioninae", "Papilionini");
		assertNotNull(matchTriple);
		assertEquals(3, matchTriple.length);
		assertEquals("Papilionidae", matchTriple[0]);
		assertEquals("Papilioninae", matchTriple[1]);
		assertEquals("Papilionini", matchTriple[2]);

		// Test distinct queries
		String[] subfamilies = HigherTaxonLifeCycle.selectDistinctSubfamily("Papilionidae");
		assertNotNull(subfamilies);
		assertTrue(subfamilies.length >= 1);
		assertEquals("Papilioninae", subfamilies[0]);

		String[] tribes = HigherTaxonLifeCycle.selectDistinctTribe("Papilioninae");
		assertNotNull(tribes);
		assertTrue(tribes.length >= 1);
		assertEquals("Papilionini", tribes[0]);

		// Test caste check
		assertTrue(lifeCycle.isFamilyWithCastes("Formicidae"));
		assertFalse(lifeCycle.isFamilyWithCastes("Papilionidae"));
	}
}
