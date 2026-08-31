package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.AllowedVersion;
import edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycle;
import java.util.List;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

public class TestAllowedVersionLifeCycle extends TestCase {

	@Test
	public void testAllowedVersionPersistenceAndLookup() {
		Session session = HibernateUtil.getTestSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		AllowedVersion av = new AllowedVersion();
		av.setVersion("2.0.2");
		session.persist(av);

		transaction.commit();
		session.close();

		AllowedVersionLifeCycle lifeCycle = new AllowedVersionLifeCycle();
		List<AllowedVersion> versions = lifeCycle.findAll();
		assertNotNull(versions);
		assertTrue(versions.stream().anyMatch(v -> "2.0.2".equals(v.getVersion())));
	}

	@Test
	public void testListAllowedVersions() {
		String list = AllowedVersionLifeCycle.listAllowedVersions();
		assertNotNull(list);
	}
}
