package edu.harvard.mcz.imagecapture.data;

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA

import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Home object for domain model class Template.
 *
 * @see edu.harvard.mcz.imagecapture.data.Template
 */
public class TemplateLifeCycle {

    private static final Log log = LogFactory.getLog(TemplateLifeCycle.class);

    public void cleanUpReferenceImage() {
        log.debug("Trying to set all Template.referenceimage values to null.");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                String queryString = "update Template set referenceImage = null";
                Query query = session.createQuery(queryString);
                int rows = query.executeUpdate();
                session.getTransaction().commit();
                log.debug("changed " + rows + " rows");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            try {
                session.close();
            } catch (SessionException e) {
            }

        } catch (RuntimeException re) {
            log.error("Set all Template.referenceimage to null failed", re);
            throw re;
        }
    }

    public void persist(Template transientInstance) throws SaveFailedException {
        log.debug("persisting Template instance");
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
                throw new SaveFailedException("Save to template table failed. " +
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

    public void attachDirty(Template instance) {
        log.debug("attaching dirty Template instance");
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

    public void attachClean(Template instance) throws SaveFailedException {
        log.debug("attaching clean Template instance");
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
                throw new SaveFailedException("Save to template table failed. " +
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

    public void delete(Template persistentInstance) throws SaveFailedException {
        log.debug("deleting Template instance");
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
                throw new SaveFailedException("Save to template table failed. " +
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

    public Template merge(Template detachedInstance) throws SaveFailedException {
        log.debug("merging Template instance");
        try {
            Template result = detachedInstance;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                result = (Template) session.merge(detachedInstance);
                session.getTransaction().commit();
                log.debug("merge successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Save to template table failed. " +
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

    public Template findById(java.lang.String id) {
        // log.debug("getting Template instance with id: " + id);
        try {
            Template instance = null;
            // log.debug("getting Template instance 1");
            // this causes infinite loop
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            // never reaches here
            log.debug("getting Template instance 2");
            session.beginTransaction();
            try {
                instance = (Template) session.get(
                        "edu.harvard.mcz.imagecapture.data.Template", id);
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


    public List<Template> findAll() {
        log.debug("finding all Template instances");
        try {
            List<Template> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                results = session.createQuery("From Template t order by t.name").list();
                log.debug("find all successful, result size: " + results.size());
                System.out.println("find all successful, result size: " +
                        results.size());
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                System.out.println(e.getMessage());
            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
}
