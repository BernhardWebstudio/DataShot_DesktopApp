package edu.harvard.mcz.imagecapture.lifecycle;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.ISO3166;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ISO3166LifeCycle extends GenericLifeCycle<ISO3166> {

    private static final Logger log =
            LoggerFactory.getLogger(ICImageLifeCycle.class);

    public ISO3166LifeCycle() {
        super(ISO3166.class, log);
    }


    public ISO3166 findById(java.lang.Long id) {
        log.debug("getting ISO3166 instance with id: " + id);
        try {
            ISO3166 instance = null;
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                instance = (ISO3166) session.get(
                        "edu.harvard.mcz.imagecapture.entity.ISO3166", id);
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

    public ISO3166 findByCountryCode(java.lang.String id) {
        log.debug("getting ISO3166 instance with country code: " + id);

        ISO3166 instance = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery cr = cb.createQuery(ISO3166.class);
            Root<ISO3166> root = cr.from(ISO3166.class);
            cr = cr.where(cb.equal(root.get("isoCode"), id));
            instance = (ISO3166) session.createQuery(cr).getSingleResult();
            if (instance == null) {
                log.debug("get successful, no instance found");
            } else {
                log.debug("get successful, instance found");
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(e.getMessage());
        }
        return instance;
    }
}
