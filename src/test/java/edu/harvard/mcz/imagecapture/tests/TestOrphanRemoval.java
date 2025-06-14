package edu.harvard.mcz.imagecapture.tests;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.Date;


public class TestOrphanRemoval extends TestCase {

    @Test
    public void testOrphanRemoval() {
        Session session = HibernateUtil.getTestSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        // Step 1: Create and persist Specimen with Collectors
        Specimen specimen = new Specimen();
        // set all values that cannot be null
        specimen.setBarcode("test_12345");
        specimen.setDateCreated(new Date());

        Collector collector1 = new Collector();
        collector1.setCollectorName("Collector One");
        collector1.setSpecimen(specimen);

        Collector collector2 = new Collector();
        collector2.setCollectorName("Collector Two");
        collector2.setSpecimen(specimen);

        specimen.getCollectors().add(collector1);
        specimen.getCollectors().add(collector2);

        session.persist(specimen);
        transaction.commit();

        // Check if the specimen and collectors are saved
        transaction = session.beginTransaction();
        Specimen retrievedSpecimen = session.get(Specimen.class, specimen.getSpecimenId());
        assertNotNull("Specimen should be saved", retrievedSpecimen);
        assertEquals("Specimen should have 2 collectors", 2, retrievedSpecimen.getCollectors().size());
        transaction.commit();

        // Step 2: Remove the specimen
        transaction = session.beginTransaction();
        session.remove(specimen);
        transaction.commit();

        // Step 3: Check if the removed Collector is deleted
        transaction = session.beginTransaction();
        Collector deletedCollector = session.get(Collector.class, collector1.getCollectorId());
        transaction.commit();

        assertNull("Orphaned collector should be deleted", deletedCollector);

        session.close();
    }
}