/**
 * SpecimenPartAttributeLifeCycle.java
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

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.SpecimenPart;
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute;
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 *
 */
public class SpecimenPartAttributeLifeCycle {
    private static final Logger log =
            LoggerFactory.getLogger(SpecimenPartAttributeLifeCycle.class);

    /**
     * Save or update an existing specimen part attribute record.
     *
     * @param instance of a Specimen that that is to be saved.
     * @throws SaveFailedException
     */
    public void attachDirty(SpecimenPartAttribute instance)
            throws SaveFailedException {
        log.debug("attaching dirty SpecimenPartAttribute instance");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.saveOrUpdate(instance);
                session.getTransaction().commit();
                log.debug("attach successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error("attach failed", e);
                throw new SaveFailedException("Unable to save.");
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

    public void persist(SpecimenPartAttribute instance)
            throws SaveFailedException {
        log.debug("persisting detatched SpecimenPartAttribute instance: " +
                instance.getSpecimenPart().getSpecimenPartId());
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.persist(instance);
                session.getTransaction().commit();
                log.debug("persist successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error("persist failed", e);
                throw new SaveFailedException("Unable to save.");
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

    public List<SpecimenPartAttribute> findBySpecimenPart(SpecimenPart part) {
        log.debug("finding SpecimenPartAttribute instance by specimen part");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<SpecimenPartAttribute> results = null;
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery criteria = cb.createQuery(SpecimenPartAttribute.class);
                Root<SpecimenPartAttribute> root =
                        criteria.from(SpecimenPartAttribute.class);
                criteria.where(cb.equal(root.get("specimenPartId"), part));
                Query<SpecimenPartAttribute> query = session.createQuery(criteria);
                results = query.getResultList();
                log.debug("find by example successful, result size: " + results.size());
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

    /**
     * Delete existing specimen part attribute record.
     *
     * @param instance of a SpecimenPartAttribute that that is to be removed.
     * @throws SaveFailedException
     */
    public void remove(SpecimenPartAttribute instance)
            throws SaveFailedException {
        log.debug("attaching dirty SpecimenPartAttribute instance");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            try {
                session.delete(instance);
                session.getTransaction().commit();
                log.debug("delete successful");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error("delete failed", e);
                throw new SaveFailedException("Unable to delete.");
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
}
