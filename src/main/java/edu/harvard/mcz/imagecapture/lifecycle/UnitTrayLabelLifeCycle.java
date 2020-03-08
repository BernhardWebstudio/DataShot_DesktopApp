/**
 * UnitTrayLabelLifeCycle.java
 * edu.harvard.mcz.imagecapture.data
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel;
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
 * UnitTrayLabelLifeCycle
 */
public class UnitTrayLabelLifeCycle {

    private static final Log log = LogFactory.getLog(UnitTrayLabelLifeCycle.class);


    public void persist(UnitTrayLabel transientInstance) throws SaveFailedException {
        log.debug("persisting UnitTrayLabel instance");
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
                throw new SaveFailedException("Save to UnitTrayLabel table failed. " + e.getMessage());
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

    public void attachDirty(UnitTrayLabel instance) throws SaveFailedException {
        log.debug("attaching dirty UnitTrayLabel instance");
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
                throw new SaveFailedException("Save to UnitTrayLabel table failed. " + e.getMessage());
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

    public void attachClean(UnitTrayLabel instance) {
        log.debug("attaching clean UnitTrayLabel instance");
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

    public void delete(UnitTrayLabel persistentInstance) throws SaveFailedException {
        log.debug("deleting UnitTrayLabel instance");
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
                throw new SaveFailedException("Delete from UnitTrayLabel table failed. " + e.getMessage());
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

    public UnitTrayLabel merge(UnitTrayLabel detachedInstance) throws SaveFailedException {
        log.debug("merging UnitTrayLabel instance");
        try {
            UnitTrayLabel result = detachedInstance;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                result = (UnitTrayLabel) session.merge(detachedInstance);
                session.getTransaction().commit();
                log.debug("merge successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Save to UnitTrayLabel table failed. " + e.getMessage());
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

    public UnitTrayLabel findById(java.lang.Integer id) {
        log.debug("getting UnitTrayLabel instance with id: " + id);
        try {
            UnitTrayLabel instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                instance = session.get(UnitTrayLabel.class, id);
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

    /**
     * @return
     */

    public List<UnitTrayLabel> findAll() {
        log.debug("finding all UnitTrayLabel");
        try {
            List<UnitTrayLabel> results = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                results = (List<UnitTrayLabel>) session.createQuery("from UnitTrayLabel u order by u.ordinal, u.family, u.subfamily, u.tribe, u.genus, u.specificEpithet ").list();
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

    public Integer findMaxOrdinal() {
        log.debug("finding max ordinal in UnitTrayLabel");
        Integer result = 0;

        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                String queryString = "select max(ordinal) from UnitTrayLabel";
                Query query = session.createQuery(queryString);
                List queryResult = query.list();
                if (!queryResult.isEmpty()) {
                    // MySQL returns an integer, Oracle returns a BigDecimal
                    // Need to cast from either in a system independent way.
                    // NOTE: This will fail if maximum value of ordinal exceeds the size of Integer.
                    String temp = queryResult.get(0).toString();
                    result = Integer.valueOf(temp);
                    log.debug(result);
                }
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
        } catch (RuntimeException re) {
            log.error("find max ordinal failed", re);
            throw re;
        }


        return result;
    }

}
