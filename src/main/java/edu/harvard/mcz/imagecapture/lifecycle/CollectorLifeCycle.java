package edu.harvard.mcz.imagecapture.lifecycle;

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.Collector;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Home object for domain model class Collector.
 *
 * @see Collector
 */
public class CollectorLifeCycle extends GenericLifeCycle {

    private static final Logger log =
            LoggerFactory.getLogger(CollectorLifeCycle.class);

    public CollectorLifeCycle() {
        super(CollectorLifeCycle.class, log);
    }

    public void persist(Collector transientInstance) throws SaveFailedException {
        if (transientInstance.getCollectorName() != "") {
            log.debug("persisting Collector instance");
            try {
                Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                session.beginTransaction();
                try {
                    session.persist(transientInstance);
                    session.getTransaction().commit();
                    log.debug("persist successful");
                } catch (HibernateException e) {
                    session.getTransaction().rollback();
                    log.error(e.getMessage());
                    throw new SaveFailedException("Unable to save collector record. " +
                            e.getMessage());
                }
                try {
                    session.close();
                } catch (SessionException e) {
                }
            } catch (RuntimeException re) {
                log.error("persist failed", re);
                throw re;
            }
        }
    }

    public void attachDirty(Collector instance) throws SaveFailedException {
        log.debug("attaching dirty Collector instance");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.saveOrUpdate(instance);
                session.getTransaction().commit();
                log.debug("attach successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Unable to save collector record. " +
                        e.getMessage());
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

    public void attachClean(Collector instance) {
        log.debug("attaching clean Collector instance");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.lock(instance, LockMode.NONE);
                session.getTransaction().commit();
                log.debug("attach successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
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

    public void delete(Collector persistentInstance) throws SaveFailedException {
        log.debug("Deleting Collector instance with id " +
                persistentInstance.getCollectorId());
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.delete(persistentInstance);
                session.getTransaction().commit();
                log.debug("delete successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Unable to Delete collector record. " +
                        e.getMessage());
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

    public Collector merge(Collector detachedInstance)
            throws SaveFailedException {
        log.debug("merging Collector instance");
        try {
            Collector result = detachedInstance;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                result = (Collector) session.merge(detachedInstance);
                session.getTransaction().commit();
                log.debug("merge successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Unable to save collector record. " +
                        e.getMessage());
            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Collector findById(java.lang.Long id) {
        log.debug("getting Collector instance with id: " + id);
        try {
            Collector instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                instance = session.get(Collector.class, id);
                session.getTransaction().commit();
                if (instance == null) {
                    log.debug("get successful, no instance found");
                } else {
                    log.debug("get successful, instance found");
                }
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
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

    public String[] getDistinctCollectors() {
        ArrayList<String> collections = new ArrayList<String>();
        collections.add(""); // put blank at top of list.
        try {
            String sql =
                    "Select distinct collectorName from Collector col where col.collectorName is not null order by col.collectorName";
            return runQueryToGetStrings(collections, sql, log);
        } catch (RuntimeException re) {
            log.error("Error", re);
            return new String[]{};
        }
    }
}
