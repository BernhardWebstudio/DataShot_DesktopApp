package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.HigherTaxon;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HigherTaxonLifeCycle {

    private static final Logger log =
            LoggerFactory.getLogger(HigherTaxonLifeCycle.class);

    public static String[] selectDistinctFamily() {
        List<String> result = new ArrayList<String>();
        try {
            String sql =
                    " Select distinct family from Specimen s where s.family is not null ";
            return getStrings(result, sql);
        } catch (RuntimeException re) {
            log.error("Error", re);
            return new String[]{};
        }
    }

    public static String[] selectDistinctSubfamily(String family) {
        List<String> result = new ArrayList<String>();
        try {
            String sql = "";
            if (family == null || family.equals("")) {
                sql =
                        " Select distinct subfamily from Specimen s where s.subfamily is not null order by subfamily";
            } else {
                sql =
                        " Select distinct subfamily from Specimen s where s.family = '" +
                                family.trim() +
                                "' and s.subfamily is not null order by subfamily ";
            }
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator i = q.iterate();
                if (!i.hasNext()) {
                    // No results, try without where family='?'
                    sql =
                            " Select distinct subfamily from Specimen s where s.subfamily is not null ";
                    q = session.createQuery(sql);
                    i = q.iterate();
                }
                while (i.hasNext()) {
                    String value = (String) i.next();
                    result.add(value);
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result.toArray(new String[]{});
        } catch (RuntimeException re) {
            log.error("Error", re);
            return new String[]{};
        }
    }

    public static String[] selectDistinctTribe(String subfamily) {
        List<String> result = new ArrayList<String>();
        try {
            String sql =
                    " Select distinct tribe from Specimen s where s.subfamily = '" +
                            subfamily.trim() + "' and s.tribe is not null ";
            return getStrings(result, sql);
        } catch (RuntimeException re) {
            log.error("Error", re);
            return new String[]{};
        }
    }

    public static String[] selectDistinctOrder() {
        List<String> result = new ArrayList<String>();
        try {
            String sql =
                    " Select distinct higherOrder from Specimen s where s.higherOrder is not null ";
            return getStrings(result, sql);
        } catch (RuntimeException re) {
            log.error("Error", re);
            return new String[]{};
        }
    }

    private static String[] getStrings(List<String> result, String sql) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(sql);
            Iterator i = q.iterate();
            while (i.hasNext()) {
                String value = (String) i.next();
                result.add(value);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(e.getMessage());
        } finally {
            try {
                session.close();
            } catch (SessionException e) {
            }
        }
        return result.toArray(new String[]{});
    }

    public static String findOrderForFamily(String family) {
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            try {
                String sql = "Select distinct higherOrder from Specimen s where s.higherOrder is not null and s.higherOrder != :emptyStr and s.family LIKE :family";
                session.beginTransaction();
                Query q = session.createQuery(sql);
                q.setParameter("family", family);
                q.setParameter("emptyStr", "");
                String result = (String) q.getSingleResult();
                session.getTransaction().commit();
                return result;
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public void persist(HigherTaxon transientInstance)
            throws SaveFailedException {
        log.debug("persisting HigherTaxon instance");
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
                throw new SaveFailedException("Save to HigherTaxon table failed. " +
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

    public void attachDirty(HigherTaxon instance) throws SaveFailedException {
        log.debug("attaching dirty ICImage instance");
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
                throw new SaveFailedException("Save to HigherTaxon table failed. " +
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

    public void attachClean(HigherTaxon instance) throws SaveFailedException {
        log.debug("attaching clean HigherTaxon instance");
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
                throw new SaveFailedException("Save to HigherTaxon table failed. " +
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

    public void delete(HigherTaxon persistentInstance) throws SaveFailedException {
        log.debug("deleting HigherTaxon instance");
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
                throw new SaveFailedException("Delete from HigherTaxon table failed. " +
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

    public HigherTaxon merge(HigherTaxon detachedInstance)
            throws SaveFailedException {
        log.debug("merging ICImage instance");
        try {
            HigherTaxon result = detachedInstance;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                result = (HigherTaxon) session.merge(detachedInstance);
                session.getTransaction().commit();
                log.debug("merge successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
                throw new SaveFailedException("Save to HigherTaxon table failed. " +
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

    public HigherTaxon findById(java.lang.Long id) {
        log.debug("getting HigherTaxon instance with id: " + id);
        try {
            HigherTaxon instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                instance = (HigherTaxon) session.get(
                        "edu.harvard.mcz.imagecapture.data.HigherTaxon", id);
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
     * @return list of all higher files sorted by family
     */

    public List<HigherTaxon> findAll() {
        log.debug("finding all Higher Taxa");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<HigherTaxon> results = null;
            try {
                results = (List<HigherTaxon>) session
                        .createQuery("From HigherTaxon ht order by ht.family")
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
            log.error("find all failed", re);
            throw re;
        }
    }

    public boolean isMatched(String aFamily, String aSubfamily) {
        boolean result = false;
        try {
            String sql =
                    "Select distinct family, subfamily from HigherTaxon ht  where soundex(ht.family) = soundex('" +
                            aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                            "')  ";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator i = q.iterate();
                if (i.hasNext()) {
                    result = true;
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error", re);
            return false;
        }
    }

    public boolean isMatched(String aFamily, String aSubfamily, String aTribe) {
        boolean result = false;
        try {
            String sql =
                    "Select distinct family, subfamily from HigherTaxon ht where soundex(ht.family) = soundex('" +
                            aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                            "') and soundex(ht.tribe) = soundex('" + aTribe + "')  ";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator i = q.iterate();
                if (i.hasNext()) {
                    result = true;
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error", re);
            return false;
        }
    }

    /**
     * Find the first soundex match to family in the Higher Taxon authority file.
     *
     * @param aFamily
     * @return a String containing the matched family name, null
     * if no match or a connection problem.
     */
    public String findMatch(String aFamily) {
        String result = null;
        try {
            String sql =
                    "SELECT DISTINCT family FROM HigherTaxon ht WHERE soundex(ht.family) = soundex('" +
                            aFamily + "')  ";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator results = q.list().iterator();
                if (results.hasNext()) {
                    // retrieve one row.
                    result = (String) results.next();
                    // store the family and subfamily from that row in the array to
                    // return.
                    log.debug("Debug {}", result);
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error", re);
            return null;
        }
    }

    /**
     * Find the first soundex match to both family and subfamily in the Higher
     * Taxon authority file.
     *
     * @param aFamily
     * @param aSubfamily
     * @return a String array with the 0th element being the family name and the
     * 1st element being the subfamily name, null
     * if no match or a connection problem.
     */

    public String[] findMatch(String aFamily, String aSubfamily) {
        String[] result = null;
        try {
            String sql =
                    "Select distinct family, subfamily from HigherTaxon ht  where soundex(ht.family) = soundex('" +
                            aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                            "')  ";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator results = q.list().iterator();
                if (results.hasNext()) {
                    // create a two element string array.
                    result = new String[2];
                    // retrieve one row.
                    Object[] row = (Object[]) results.next();
                    // store the family and subfamily from that row in the array to
                    // return.
                    result[0] = (String) row[0];
                    result[1] = (String) row[1];
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error", re);
            return null;
        }
    }

    /**
     * Return the first soundex match to family, subfamily, and tribe from the
     * higher taxon authority file.
     *
     * @param aFamily    the text string to check for a matching family name.
     * @param aSubfamily the text string to check for a matching subfamily name.
     * @param aTribe     the text string to check for a matching tribe name.
     * @return a string array of {family,subfamily,tribe} from the database, or
     * null if no match or a connection
     * problem.
     */

    public String[] findMatch(String aFamily, String aSubfamily, String aTribe) {
        String[] result = null;
        try {
            String sql =
                    "Select distinct family, subfamily, tribe from HigherTaxon ht  where "
                            + "soundex(ht.family) = soundex('" + aFamily + "') and "
                            + "soundex(ht.subfamily) = soundex('" + aSubfamily + "')and "
                            + "soundex(ht.tribe) = soundex('" + aTribe + "')  ";
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                Query q = session.createQuery(sql);
                Iterator results = q.list().iterator();
                if (results.hasNext()) {
                    // create a two element string array.
                    result = new String[3];
                    // retrieve one row.
                    Object[] row = (Object[]) results.next();
                    // store the family, subfamily, and tribe from that row in the array
                    // to return.
                    result[0] = (String) row[0];
                    result[1] = (String) row[1];
                    result[2] = (String) row[2];
                }
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error(e.getMessage());
            } finally {
                try {
                    session.close();
                } catch (SessionException e) {
                }
            }
            return result;
        } catch (RuntimeException re) {
            log.error("Error", re);
            return null;
        }
    }

    public boolean isFamilyWithCastes(String family) {
        boolean result = false;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Query q = session.createQuery(
                "select count(h) from HigherTaxon h where h.hasCaste = 1 and h.Family = ? ");
        q.setParameter(0, family);
        Iterator results = q.list().iterator();
        while (results.hasNext()) {
            Object[] row = (Object[]) results.next();
            Integer value = (Integer) row[0];
            if (value > 0) {
                result = true;
            }
        }
        return result;
    }
}
