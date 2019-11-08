package edu.harvard.mcz.imagecapture.data;

import edu.harvard.mcz.imagecapture.exceptions.NoSuchValueException;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

// Generated Feb 5, 2009 5:23:55 PM by Hibernate Tools 3.2.2.GA

/**
 * Home object for domain model class Users.
 *
 * @see Users
 */
public class UsersLifeCycle {

    private static final Log log = LogFactory.getLog(UsersLifeCycle.class);


    public static boolean isUserAdministrator(int aUserID) {
        boolean returnValue = false;

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            List<Users> results = (List<Users>) session.createQuery("from Users as u where role = '" + Users.ROLE_ADMINISTRATOR + "' and u.userid = '" + aUserID + "'").list();
            session.getTransaction().commit();
            if (results.size() == 1) {
                returnValue = results.get(0).getRole().equals("Administrator");
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.close();
            log.error("Can't retrieve username. " + e.getMessage());
        }

        return returnValue;
    }


    public static boolean isUserChiefEditor(int aUserID) {
        boolean returnValue = false;

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            List<Users> results = (List<Users>) session.createQuery("from Users as u where u.userid = '" + aUserID + "'").list();
            session.getTransaction().commit();
            if (results.size() == 1) {
                returnValue = results.get(0).isUserRole(Users.ROLE_CHIEF_EDITOR);
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.close();
            log.error("Can't retrieve username. " + e.getMessage());
        }

        return returnValue;

    }

    /**
     * @return
     */
    public static List<Users> findAll() {
        log.debug("finding all Users");
        try {
            List<Users> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                results = (List<Users>) session.createQuery("from Users u order by u.username ").list();
                session.getTransaction().commit();
                log.debug("find by example successful, result size: "
                        + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }


    public String getFullNameForUserName(String aUsername) throws NoSuchValueException {
        String returnValue = "";

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        try {
            List<Users> results = (List<Users>) session.createQuery("from Users as u where u.username = '" + aUsername + "'").list();
            session.getTransaction().commit();
            if (results.size() > 0) {
                returnValue = results.get(0).getFullname();
            } else {
                throw new NoSuchValueException("Couldn't find a person with a username of [" + aUsername + "]");
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            session.close();
            log.error("Can't retrieve username. " + e.getMessage());
        }

        return returnValue;
    }

    public void persist(Users transientInstance) throws SaveFailedException {
        log.debug("persisting Users instance");
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
                throw new SaveFailedException("Save to Users table failed. " + e.getMessage());
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

    public void attachDirty(Users instance) throws SaveFailedException {
        log.debug("attaching dirty Users instance");
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
                throw new SaveFailedException("Save to Users table failed. " + e.getMessage());
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

    public void attachClean(Users instance) {
        log.debug("attaching clean Users instance");
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

    public void delete(Users persistentInstance) throws SaveFailedException {
        log.debug("deleting Users instance");
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
                throw new SaveFailedException("Delete from Users table failed. " + e.getMessage());
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

    public Users merge(Users detachedInstance) throws SaveFailedException {
        log.debug("merging Users instance");
        try {
            Users result = detachedInstance;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                result = (Users) session.merge(detachedInstance);
                session.getTransaction().commit();
                log.debug("merge successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Save to Users table failed. " + e.getMessage());
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

    public Users findById(java.lang.Integer id) {
        log.debug("getting Users instance with id: " + id);
        try {
            Users instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                instance = (Users) session.get("edu.harvard.mcz.imagecapture.data.Users", id);
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

    public List<Users> findByNames(String username, String fullName) {
        assert (username != null || fullName != null);
        log.debug("finding Users instance by names");
        try {
            List<Users> results = null;

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery cr = cb.createQuery(Users.class);
                Root<Users> root = cr.from(Users.class);
                cr.select(root);
                List<Predicate> propertyValueRelations = new ArrayList<>();
                if (username != null) {
                    propertyValueRelations.add(cb.equal(root.get("username"), username));
                }
                if (fullName != null) {
                    propertyValueRelations.add(cb.equal(root.get("fullname"), fullName));
                }
                cr.where(cb.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()])));
                results = session.createQuery(cr).list();
                log.debug("find by name successful, result size: "
                        + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());

            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find users by name failed", re);
            throw re;
        }
    }

    public List<Users> findByCredentials(String username, String password) {
        try {
            List<Users> results = null;

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery query = builder.createQuery(Users.class);
                Root<Users> root = query.from(Users.class);
                query = query.select(root);
                List<Predicate> propertyValueRelations = new ArrayList<Predicate>();
                propertyValueRelations.add(builder.like(root.get("username"), username));
                propertyValueRelations.add(builder.like(root.get("hash"), password));
                Predicate res = builder.and(propertyValueRelations.toArray(new Predicate[propertyValueRelations.size()]));
                query = query.where(res);
                Query q = session.createQuery(query);
                results = (List<Users>) q.list();
                log.debug("find by credentials successful, result size: "
                        + results.size());
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find users by credentials failed", re);
            throw re;
        }
    }
}
