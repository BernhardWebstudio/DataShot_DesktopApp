/**
 *
 */
package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.ICImage;
import edu.harvard.mcz.imagecapture.entity.Specimen;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.Tracking;
import edu.harvard.mcz.imagecapture.entity.fixed.CountValue;
import edu.harvard.mcz.imagecapture.entity.fixed.GenusSpeciesCount;
import edu.harvard.mcz.imagecapture.entity.fixed.VerbatimCount;
import edu.harvard.mcz.imagecapture.entity.fixed.WorkFlowStatus;
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.PersistenceException;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA

/**
 * Home object for domain model class Specimen.
 * @see Specimen
 *
 */
public class SpecimenLifeCycle extends GenericLifeCycle<Specimen> {

  private static final Logger log =
      LoggerFactory.getLogger(SpecimenLifeCycle.class);

  public SpecimenLifeCycle() { super(Specimen.class, SpecimenLifeCycle.log); }

  /**
   * Barcodes are assigned in order (from 50,000). Find barcode numbers that are
   * missing from the sequence of specimen records.
   *
   * @return a string array containing a list of barcodes that are missing from
   *     the sequence.
   */
  public static String[] getMissingBarcodes() {
    ArrayList<String> missing = new ArrayList<String>();
    try {
      String sql =
          "select "
          + "        (cast(substr(a.barcode,-8) as decimal(8,0)) + 1) "
          + "        from Specimen a "
          + "        WHERE "
          + "        not exists "
          + "        ("
          + "            select 1 from Specimen b "
          + "            WHERE "
          +
          "            cast(substr(b.barcode,-8) as decimal(8,0)) = (cast(substr(a.barcode,-8) as decimal(8,0)) + 1)"
          + "        ) "
          + "        and "
          + "        cast(substr(a.barcode,-8) as decimal(8,0)) not in "
          + "        ( "
          +
          "          select max(cast(substr(c.barcode,-8) as decimal(8,0))) from Specimen c WHERE cast(substr(a.barcode,-8) as decimal(8,0)) > 49999 "
          + "        ) "
          + "        order by 1";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Query q = session.createQuery(sql);
        List results = q.list();
        Iterator i = results.iterator();
        while (i.hasNext()) {
          BigDecimal value = (BigDecimal)i.next();
          BarcodeBuilder builder =
              Singleton.getSingletonInstance().getBarcodeBuilder();
          missing.add(builder.makeFromNumber(value.toBigInteger().intValue()));
          log.debug("Debug", value);
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      String[] result = missing.toArray(new String[] {});
      return result;
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public static void main(String[] args) {
    SpecimenLifeCycle s = new SpecimenLifeCycle();
    s.getFieldSize("");
  }

  /**Log who changed this specimen when and what its current status is to the
   * tracking table.
   *
   * @param specimen the specimen for which the change is to be logged to the
   *     tracking table.
   */
  protected void track(Specimen specimen) {
    Tracking t = new Tracking(
        specimen, Singleton.getSingletonInstance().getUserFullName(),
        specimen.getWorkFlowStatus(), new Date());
    TrackingLifeCycle tls = new TrackingLifeCycle();
    try {
      tls.persist(t);
    } catch (SaveFailedException e) {
      // TODO Handle save error in UI
      log.error("Error", e);
    }
  }

  /**
   * Save a new specimen record, and add an entry in the tracking table.
   *
   * @param transientInstance instance of a Specimen that doesn't have a
   *     matching
   * database record which is to be saved as a new record in the database.
   *
   * @throws SaveFailedException
   * @throws SpecimenExistsException
   */
  public void persist(Specimen transientInstance)
      throws SaveFailedException, SpecimenExistsException {
    log.debug("persisting Specimen instance");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      try {
        session.persist(transientInstance);
        session.getTransaction().commit();
        log.debug("persist successful");
        track(transientInstance);
      } catch (PersistenceException e) {
        session.getTransaction().rollback();
        try {
          session.close();
        } catch (SessionException se) {
        }
        String message = e.getMessage();
        String pmessage = "";
        try {
          pmessage = e.getCause().getMessage();
        } catch (Exception ex) {
          // expected if e doesn't have a cause.
        }
        log.error("persist failed", e);
        String existsPattern = "^Duplicate entry '.*' for key 'Barcode'$";
        if (message.matches(existsPattern) || pmessage.matches(existsPattern) ||
            e instanceof ConstraintViolationException) {
          log.debug("specimen exists");
          throw new SpecimenExistsException("Specimen record exists for " +
                                            transientInstance.getBarcode());
        } else {
          log.debug("specimen save failed");
          throw new SaveFailedException("Unable to save specimen " +
                                        transientInstance.getBarcode());
        }
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (SpecimenExistsException | SaveFailedException see) {
      // Pass on upwards unchanged
      log.error("persist failed see", see);
      throw see;
    } catch (RuntimeException re) {
      // Catch, log, and pass on any other exception.
      log.error("persist failed", re);
      throw re;
    }
  }

  /**
   * Save or update an existing specimen record.
   *
   * @param instance of a Specimen that that is to be saved.
   * @throws SaveFailedException
   */
  public void attachDirty(Specimen instance) throws SaveFailedException {
    log.debug("attaching dirty Specimen instance with id " +
              instance.getSpecimenId());
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      try {
        session.saveOrUpdate(instance);
        session.getTransaction().commit();
        log.debug("attach successful");
        track(instance);
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("attach failed", e);
        throw new SaveFailedException("Unable to save specimen " +
                                      instance.getBarcode());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (RuntimeException re) {
      log.error("attach failed", re);
      throw re;
    }
  }

  /**
   * Re-associate a transient instance with a session.
   *
   * @param instance
   */
  public void attachClean(Specimen instance) {
    try {
      log.debug("Debug: Specimen parts size", instance.getSpecimenParts().size());
      log.debug("Debug: First Specimen part",((SpecimenPart)instance.getSpecimenParts().toArray()[0])
                    .getPartAttributeValuesConcat());
    } catch (Exception e) {
      log.debug("Debug", e.getMessage());
    }
    log.debug("attaching clean Specimen instance");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      try {
        session.lock(instance, LockMode.NONE);
        session.flush();
        session.getTransaction().commit();
        log.debug("attach successful");
        log.debug("Debug: Specimen parts size", instance.getSpecimenParts().size());
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.debug(e.getMessage(), e);
        log.debug("Trying to attach dirty");
        try {
          attachDirty(instance);
        } catch (SaveFailedException e1) {
          log.error("attach failed", e1);
          e1.printStackTrace();
        }
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (RuntimeException re) {
      log.error("attach failed", re);
      throw re;
    }
    try {
      log.debug("Debug: Specimen parts size", instance.getSpecimenParts().size());
      log.debug("Debug: Specimen parts first", ((SpecimenPart)instance.getSpecimenParts().toArray()[0])
                    .getPartAttributeValuesConcat());
    } catch (Exception e) {
      log.debug("Debug", e.getMessage());
    }
  }

  public void delete(Specimen persistentInstance) {
    log.debug("deleting Specimen instance");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      try {
        session.delete(persistentInstance);
        session.getTransaction().commit();
        log.debug("delete successful");
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("delete failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (RuntimeException re) {
      log.error("delete failed", re);
      throw re;
    }
  }

  /**
   * Update db record and log current status of record.
   *
   * @param detachedInstance
   * @return the current specimen record.
   */
  public Specimen merge(Specimen detachedInstance) {
    log.debug("merging Specimen instance");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      try {
        Specimen result = (Specimen)session.merge(detachedInstance);
        session.getTransaction().commit();
        log.debug("merge successful");
        try {
          session.close();
        } catch (SessionException e) {
        }
        track(result);
        return result;
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        try {
          session.close();
        } catch (SessionException e1) {
        }
        log.error("merge failed", e);
        throw e;
      }
    } catch (RuntimeException re) {
      log.error("merge failed", re);
      throw re;
    }
  }

  public Specimen findById(java.lang.Long id) {
    log.debug("getting Specimen instance with id: " + id);
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      Specimen instance = null;
      try {
        instance = session.get(Specimen.class, id);
        session.getTransaction().commit();
        if (instance == null) {
          log.debug("get successful, no instance found");
        } else {
          log.debug("get successful, instance found");
          log.debug("Debug: Specimen parts size", instance.getSpecimenParts().size());
        }
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("get failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return instance;
    } catch (RuntimeException re) {
      log.error("get failed", re);
      throw re;
    }
  }

  public List<Specimen> findByBarcode(String barcode) {
    log.debug("findByBarcode '" + barcode + "' start");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<Specimen> results = null;
      try {
        Query query = session.createQuery(
            "From Specimen s WHERE s.barcode LIKE :barcode");
        query.setParameter("barcode", barcode);
        results = (List<Specimen>)query.list();
        log.debug("find query successful, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("findByBarcode failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return results;
    } catch (RuntimeException re) {
      log.error("findByBarcode failed.  ", re);
      throw re;
    }
  }

  public List<Specimen> findAll() {
    log.debug("finding all Specimens");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<Specimen> results = null;
      try {
        results = (List<Specimen>)session
                      .createQuery("From Specimen s order by s.barcode")
                      .list();
        log.debug("find all successful, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find all failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return results;
    } catch (RuntimeException re) {
      log.error("Find all failed.  ", re);
      throw re;
    }
  }

  public List<Specimen> findAllPage(int startAt, int fetchSize) {
    log.debug("finding " + fetchSize + " Specimens from " + startAt + ".");
    try {
      if (startAt < 0 || fetchSize < 0) {
        throw new RuntimeException("Negative value given for page size");
      }
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<Specimen> results = null;
      try {
        Query query = session.createQuery("From Specimen s order by s.barcode");
        query.setFirstResult(startAt);
        query.setFetchSize(fetchSize);
        results = (List<Specimen>)query.list();
        log.debug("find all paged successful, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find all failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return results;
    } catch (RuntimeException re) {
      log.error("Find all paged failed. ", re);
      throw re;
    }
  }

  // Select distinct path from ICImage im WHERE im.path is not null order by
  // im.path

  public List<ICImage> findImagesByPath(String path) {
    log.debug("finding images by path " + path);
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<ICImage> results = null;
      try {
        // System.out.println("path is " + path);
        // this works
        String sql = "";
        if (path.contains("\\")) {
          sql =
              "From ICImage im WHERE im.path='" + path + "\\' order by imageId";
        } else {
          sql = "From ICImage im WHERE im.path='" + path + "' order by imageId";
        }

        // String sql = "From ICImage im WHERE im.path='"+path+"\\\' order by
        // imageId";
        Query query = session.createQuery(sql);
        results = (List<ICImage>)query.list();
        // log.debug("found images, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find images failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return results;
    } catch (RuntimeException re) {
      log.error("Find images failed. ", re);
      throw re;
    }
  }

  // get all image paths (folder name / date imaged) available

  public String[] getDistinctPaths() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct im.path from ICImage im order by im.imageId";
      return loadStringsBySQL(collections, sql);
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  private String[] loadStringsBySQL(ArrayList<String> collections, String sql) {
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      Query q = session.createQuery(sql);
      Iterator i = q.iterate();
      while (i.hasNext()) {
        String value = (String)i.next();
        // add, only if value isn't the "" put at top of list above.
        if (!value.equals("")) {
          collections.add(value.trim());
        }
      }
      session.getTransaction().commit();
    } catch (HibernateException e) {
      session.getTransaction().rollback();
      log.error(e.getMessage());
    }
    try {
      session.close();
    } catch (SessionException e) {
    }
    String[] result = collections.toArray(new String[] {});
    return result;
  }

  public String findSpecimenCount() {
    try {
      return findSpecimenCountThrows();
    } catch (ConnectionException e) {
      log.error(e.getMessage(), e);
    }
    return "";
  }

  public String findSpecimenCountThrows() throws ConnectionException {
    StringBuffer result = new StringBuffer();
    try {
      String sql =
          "Select count(*), workFlowStatus from Specimen group by workFlowStatus ";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        result.append("Specimen records: \n");
        session.beginTransaction();
        Iterator results = session.createQuery(sql).list().iterator();
        while (results.hasNext()) {
          Object[] row = (Object[])results.next();
          Long count = (Long)row[0];
          String status = (String)row[1];
          result.append(" " + status + "=" + count.toString() + "\n");
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return result.toString();
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      throw new ConnectionException(re.getMessage(), re);
    }
  }

  /**
   * Obtain counts of the number of specimens needing verbatim transcription by
   * genus and species.
   *
   * @return
   */
  public List<GenusSpeciesCount> countSpecimensForVerbatim() {
    ArrayList<GenusSpeciesCount> result = new ArrayList<GenusSpeciesCount>();
    try {
      String sql =
          "Select count(S), genus, specificEpithet from Specimen S WHERE S.workFlowStatus = 'Taxon Entered' group by S.genus, S.specificEpithet ";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Iterator results = session.createQuery(sql).list().iterator();
        while (results.hasNext()) {
          Object[] row = (Object[])results.next();
          Long count = Long.parseLong(row[0].toString());
          String genus = (String)row[1];
          String specificEpithet = (String)row[2];
          result.add(new GenusSpeciesCount(count, genus, specificEpithet));
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
    }
    return result;
  }

  /**
   * Get counts of distinct values of verbatim data that has not yet been
   * classified.
   *
   * @return List of counts and distinct verbatim field values.
   */
  public List<VerbatimCount> countDistinctVerbatimValues() {
    ArrayList<VerbatimCount> result = new ArrayList<VerbatimCount>();
    try {
      String sql =
          "Select count(S), S.verbatimLocality, S.dateNos, S.verbatimCollector, S.verbatimCollection, "
          + "S.verbatimNumbers, S.verbatimUnclassifiedText "
          + "from Specimen S "
          + "where S.workFlowStatus = 'Verbatim Entered' "
          +
          "group by S.verbatimLocality, S.dateNos, S.verbatimCollector, S.verbatimCollection, "
          + "S.verbatimNumbers, S.verbatimUnclassifiedText ";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Iterator results = session.createQuery(sql).list().iterator();
        while (results.hasNext()) {
          Object[] row = (Object[])results.next();
          Long count = Long.parseLong(row[0].toString());
          String verbatimLocality = (String)row[1];
          String dateNos = (String)row[2];
          String verbatimCollector = (String)row[3];
          String verbatimCollection = (String)row[4];
          String verbatimNumbers = (String)row[5];
          String verbatimUnclassifiedText = (String)row[6];
          result.add(new VerbatimCount(
              count.intValue(), verbatimLocality, dateNos, verbatimCollector,
              verbatimCollection, verbatimNumbers, verbatimUnclassifiedText));
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
    }
    return result;
  }

  public List<Specimen> findForVerbatim(String genus, String specificEpithet,
                                        String workflowStatus) {
    log.debug("finding Specimen instances for verbatim capture");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      List<Specimen> results = null;
      try {
        session.beginTransaction();
        Query query = session.createQuery(
            "From Specimen as s WHERE s.genus = ? and s.specificEpithet = ? and s.workFlowStatus = ? ");
        query.setParameter(0, genus);
        query.setParameter(1, specificEpithet);
        query.setParameter(2, workflowStatus);
        results = (List<Specimen>)query.list();
        log.debug("find query successful, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find by example failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return results;
    } catch (RuntimeException re) {
      log.error("find by example failed", re);
      throw re;
    }
  }

  public List<CountValue> findTaxaFromVerbatim(VerbatimCount verbatim) {
    log.debug("finding counts of taxa for verbatim values");
    List<CountValue> result = new ArrayList<CountValue>();
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Query query = session.createQuery(
            "Select count(s), s.genus, s.specificEpithet "
            + "From Specimen as s "
            + "where s.verbatimLocality = ? and s.dateNos = ? "
            + "   and s.verbatimCollector = ? and s.verbatimCollection = ? "
            +
            "   and s.verbatimNumbers = ? and s.verbatimUnclassifiedText = ? ");
        query.setParameter(0, verbatim.getVerbatimLocality());
        query.setParameter(1, verbatim.getVerbatimDate());
        query.setParameter(2, verbatim.getVerbatimCollector());
        query.setParameter(3, verbatim.getVerbatimCollection());
        query.setParameter(4, verbatim.getVerbatimNumbers());
        query.setParameter(5, verbatim.getVerbatimUnclassfiedText());
        Iterator i = query.list().iterator();
        while (i.hasNext()) {
          Object[] row = (Object[])i.next();
          Long count = Long.parseLong(row[0].toString());
          String genus = (String)row[1];
          String specificEpithet = (String)row[2];
          StringBuffer taxon =
              new StringBuffer().append(genus).append(" ").append(
                  specificEpithet);
          result.add(new CountValue(count.intValue(), taxon.toString().trim()));
        }
        log.debug("count query successful, result size: " + result.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find for verbatim failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return result;
    } catch (RuntimeException re) {
      log.error("find for verbatim failed", re);
      throw re;
    }
  }

  /**
   * Find specimen records that are currently in state verbatim captured and
   * which have the provided values for verbatim fields.
   *
   * @param verbatim
   * @return
   */
  public List<Specimen> findSpecimensFromVerbatim(VerbatimCount verbatim) {
    log.debug("finding specimens from verbatim values");
    List<Specimen> result = new ArrayList<Specimen>();
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Query query = session.createQuery(
            "From Specimen as s "
            + "where s.verbatimLocality = ? and s.dateNos = ? "
            + "   and s.verbatimCollector = ? and s.verbatimCollection = ? "
            + "   and s.verbatimNumbers = ? and s.verbatimUnclassifiedText = ? "
            + "   and s.workFlowStatus = '" + WorkFlowStatus.STAGE_VERBATIM +
            "' ");
        query.setParameter(0, verbatim.getVerbatimLocality());
        query.setParameter(1, verbatim.getVerbatimDate());
        query.setParameter(2, verbatim.getVerbatimCollector());
        query.setParameter(3, verbatim.getVerbatimCollection());
        query.setParameter(4, verbatim.getVerbatimNumbers());
        query.setParameter(5, verbatim.getVerbatimUnclassfiedText());
        result = query.list();

        log.debug("specimen query successful, result size: " + result.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find specimens for verbatim failed", e);
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      return result;
    } catch (RuntimeException re) {
      log.error("find specimens for verbatim failed", re);
      throw re;
    }
  }

  public int getFieldSize(String fieldName) {
    int returnValue = 0;
    //		Specimen s = new Specimen();
    // ClassMetadata specimenMetadata =
    // HibernateUtil.getSessionFactory().getClassMetadata(Specimen.class);
    // String[] p = specimenMetadata.getPropertyNames();
    // for (int i=0; i < p.length; i++ ) {
    // 	System.out.println(p[i]);
    // 	specimenMetadata.getPropertyValue(p[i], "length", EntityMode.MAP);
    // }

    //		Object[] propertyValues = specimenMetadata.getPropertyValues(s,
    // EntityMode.POJO); 		String[] propertyNames =
    // specimenMetadata.getPropertyNames(); 		Type[] propertyTypes =
    // specimenMetadata.getPropertyTypes();
    //
    //		// get a Map of all properties which are not collections or
    // associations 		Map namedValues = new HashMap();
    // for ( int i=0; i<propertyNames.length; i++ ) { System.out.println(
    // propertyNames[i] + " : " + propertyValues[i] );
    //
    //		}

    return returnValue;
  }

  public String[] getDistinctCountries() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct country from Specimen spe WHERE spe.country is not null order by spe.country  ";
      return loadStringsBySQL(collections, sql);
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public int deleteSpecimenByBarcode(String barcode) {
    log.debug("Deleting record with barcode " + barcode);
    List<Specimen> specimens = findByBarcode(barcode);
    if (specimens.size() == 0) {
      return 0; // specimen not found
    } else if (specimens.size() >= 1) {
      Specimen specimen = specimens.get(0);

      // this does not work - need to do it manually as below!!
      // this.delete(specimen);

      Long specimenId = specimen.getSpecimenId();
      try {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
          Query query = session.createQuery(
              "DELETE FROM Tracking WHERE specimen = :specimen");
          query.setParameter("specimen", specimen);
          query.executeUpdate();

          query = session.createQuery(
              "DELETE FROM LatLong WHERE specimenId = :specimen");
          query.setParameter("specimen", specimen);
          query.executeUpdate();

          query = session.createQuery(
              "DELETE FROM ICImage WHERE specimen = :specimen");
          query.setParameter("specimen", specimen);
          query.executeUpdate();

          query = session.createQuery(
              "DELETE FROM SpecimenPart WHERE specimenId = :specimen");
          query.setParameter("specimen", specimen);
          query.executeUpdate();

          query = session.createQuery(
              "DELETE FROM Specimen WHERE specimenId = :specimenId");
          query.setParameter("specimenId", specimenId);
          query.executeUpdate();

          transaction.commit();
        } catch (HibernateException e) {
          transaction.rollback();
          log.error("DeleteByBarcode failed Hibernate Exception", e);
        } catch (Exception e) {
          transaction.rollback();
          log.error("findByBarcode failed general Exception", e);
        }
        try {
          session.close();
        } catch (SessionException e) {
        }
      } catch (RuntimeException re) {
        log.error("findByBarcode failed.  ", re);
        throw re;
      }
    }
    List<Specimen> specimensAfterDelete = findByBarcode(barcode);
    if (specimensAfterDelete.size() == 0) {
      return 1; // success
    }
    return 2; // delete failed for unknown reason
  }

  public String[] getDistinctCollections() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct collection from Specimen spe WHERE spe.collection is not null order by spe.collection  ";
      return loadStringsBySQL(collections, sql);
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public String[] getDistinctDeterminers() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct identifiedBy from Specimen spe WHERE spe.identifiedBy is not null order by spe.identifiedBy  ";
      // String sql = "Select distinct identifiedby from Specimen";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Query q = session.createQuery(sql);
        Iterator i = q.iterate();
        while (i.hasNext()) {
          String value = (String)i.next();
          // add, only if value isn't the "" put at top of list above.
          if (!value.equals("")) {
            collections.add(value.trim());
          }
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      String[] result = collections.toArray(new String[] {});
      return result;
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public String[] getDistinctPrimaryDivisions() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct primaryDivison from Specimen spe WHERE spe.primaryDivison is not null order by spe.primaryDivison  ";
      // String sql = "Select distinct identifiedby from Specimen";
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      try {
        session.beginTransaction();
        Query q = session.createQuery(sql);
        Iterator i = q.iterate();
        while (i.hasNext()) {
          String value = (String)i.next();
          // add, only if value isn't the "" put at top of list above.
          if (!value.equals("")) {
            collections.add(value.trim());
          }
        }
        session.getTransaction().commit();
      } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        log.error(e.getMessage());
      }
      try {
        session.close();
      } catch (SessionException e) {
      }
      String[] result = collections.toArray(new String[] {});
      return result;
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public String[] getDistinctQuestions() {
    ArrayList<String> collections = new ArrayList<String>();
    collections.add(""); // put blank at top of list.
    try {
      String sql =
          "Select distinct questions from Specimen spe WHERE spe.questions is not null order by spe.questions  ";
      return loadStringsBySQL(collections, sql);
    } catch (RuntimeException re) {
      log.error("Error", re);
      ;
      return new String[] {};
    }
  }

  public List<Specimen> findByExampleLike(Specimen instance) {
    return this.findByExampleLike(instance, 0, 0);
  }

  /**
   * Find specimens with values matching those found in an example specimen
   * instance, including links out to related entities.  Like matching is
   * enabled, so strings containing '%' will generate like where criteria with
   * the % as a wild card.  Matches are case insensitive.
   *
   * @param instance
   * @return
   */
  public List<Specimen> findByExampleLike(Specimen instance, int maxResults,
                                          int offset) {
    log.debug(
        "finding Specimen instance by example with trackings, collectors, and images and like criteria");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<Specimen> results = null;
      try {
        Criteria criteria = session.createCriteria(Specimen.class);
        instance.setFlagAncilaryAlsoInMCZbase(null);
        instance.setFlagInBulkloader(null);
        instance.setFlagInMCZbase(null);

        Example example = Example.create(instance).enableLike().ignoreCase();
        // Note: Criteria in example instance can be excluded here, or by
        // setting their values to null.  See the invocation of
        // Specimen.clearDefaults in SearchDialog.getJButtonSearch()'s
        // actionPerformed method.
        // example.excludeProperty("flagInBulkloader");
        criteria.add(example);
        if (instance.getTrackings() != null &&
            instance.getTrackings().size() > 0) {
          criteria.createCriteria("trackings", Criteria.INNER_JOIN)
              .add(Example.create(instance.getTrackings().toArray()[0])
                       .enableLike());
        }
        if (instance.getICImages() != null &&
            instance.getICImages().size() > 0) {
          criteria.createCriteria("ICImages", Criteria.INNER_JOIN)
              .add(Example.create(instance.getICImages().toArray()[0])
                       .enableLike()
                       .ignoreCase());
        }
        if (instance.getCollectors() != null &&
            instance.getCollectors().size() > 0) {
          criteria.createCriteria("collectors", Criteria.INNER_JOIN)
              .add(Example.create(instance.getCollectors().toArray()[0])
                       .enableLike()
                       .ignoreCase());
        }
        if (offset != 0) {
          criteria.setFirstResult(offset);
        }
        if (maxResults != 0) {
          criteria.setMaxResults(maxResults);
        }
        criteria.setProjection(Projections.distinct(Projections.id()));
        List<?> ids = criteria.list();
        if (ids.size() > 0) {
          criteria = session.createCriteria(Specimen.class)
                         .add(Restrictions.in("id", ids))
                         .setFetchMode("trackings", FetchMode.JOIN)
                         .setFetchMode("ICImages", FetchMode.JOIN)
                         .setFetchMode("collectors", FetchMode.JOIN)
                         .setFetchMode("LatLong", FetchMode.JOIN)
                         .setFetchMode("numbers", FetchMode.JOIN)
                         .setFetchMode("parts", FetchMode.JOIN)
                         .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

          results = (List<Specimen>)criteria.list();
        } else {
          results = new ArrayList<>();
        }
        log.debug("find by example like successful, result size: " +
                  results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find by example like failed", e);
      }
      if (results != null) {
        for (int i = 0; i < results.size(); i++) {
          /* try {
               log.debug("Parts: " + results.get(i).getSpecimenParts().size());
               log.debug("Parts: " + ((SpecimenPart)
           results.get(i).getSpecimenParts().toArray()[0]).getPartAttributeValuesConcat());
               log.debug("Part Attribute: " + ((SpecimenPartAttribute)
           ((SpecimenPart)
           results.get(i).getSpecimenParts().toArray()[0]).getAttributeCollection().toArray()[0]).getSpecimenPartAttributeId());
           } catch (Exception e) {
               log.debug("Debug", e.getMessage());
           }*/
        }
      } else {
        results = new ArrayList<>();
      }
      return results;
    } catch (RuntimeException re) {
      log.error("find by example like failed", re);
      throw re;
    }
  }

  public List<Specimen> findByExample(Specimen instance) {
    return this.findByExample(instance, 0, 0);
  }

  /**
   * Find Specimen records based on an example specimen, don't use if you are
   * only searching by barcode.
   *
   * @param instance Specimen instance to use as a pattern for the search
   * @return list of Specimens matching instance
   *
   * @see SpecimenLifeCycle#findByBarcode(String)
   */
  public List<Specimen> findByExample(Specimen instance, int maxResults,
                                      int offset) {
    log.debug("finding Specimen instance by example");
    try {
      Session session = HibernateUtil.getSessionFactory().getCurrentSession();
      session.beginTransaction();
      List<Specimen> results = null;
      try {
        Criteria criteria = session.createCriteria(Specimen.class);
        criteria.add(Example.create(instance));
        if (instance.getTrackings() != null &&
            instance.getTrackings().size() > 0) {
          criteria.createCriteria("trackings")
              .add(Example.create(instance.getTrackings().toArray()[0]));
        }
        if (instance.getICImages() != null &&
            instance.getICImages().size() > 0) {
          criteria.createCriteria("ICImages")
              .add(Example.create(instance.getICImages().toArray()[0]));
        }
        if (offset != 0) {
          criteria.setFirstResult(offset);
        }
        if (maxResults != 0) {
          criteria.setMaxResults(maxResults);
        }
        results = (List<Specimen>)criteria.list();
        log.debug("find by example successful, result size: " + results.size());
        session.getTransaction().commit();
      } catch (HibernateException e) {
        session.getTransaction().rollback();
        log.error("find by example failed", e);
      }
      return results;
    } catch (RuntimeException re) {
      log.error("find by example failed", re);
      throw re;
    }
  }
}
