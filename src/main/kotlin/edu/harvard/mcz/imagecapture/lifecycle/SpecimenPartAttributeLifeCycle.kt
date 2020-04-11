/**
 * SpecimenPartAttributeLifeCycle.java
 * edu.harvard.mcz.imagecapture.data
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 *
 *
 */
package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.SpecimenPart
import edu.harvard.mcz.imagecapture.entity.SpecimenPartAttribute
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartAttributeLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.SessionException
import org.hibernate.query.Query
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

/**
 *
 */
class SpecimenPartAttributeLifeCycle {
    /**
     * Save or update an existing specimen part attribute record.
     *
     * @param instance of a Specimen that that is to be saved.
     * @throws SaveFailedException
     */
    @Throws(SaveFailedException::class)
    fun attachDirty(instance: SpecimenPartAttribute?) {
        log!!.debug("attaching dirty SpecimenPartAttribute instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                session.transaction.commit()
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("attach failed", e)
                throw SaveFailedException("Unable to save.")
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log.error("attach failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun persist(instance: SpecimenPartAttribute) {
        log!!.debug("persisting detatched SpecimenPartAttribute instance: " +
                instance.getSpecimenPart().getSpecimenPartId())
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(instance)
                session.transaction.commit()
                log.debug("persist successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("persist failed", e)
                throw SaveFailedException("Unable to save.")
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log.error("persist failed", re)
            throw re
        }
    }

    fun findBySpecimenPart(part: SpecimenPart?): MutableList<SpecimenPartAttribute?>? {
        log!!.debug("finding SpecimenPartAttribute instance by specimen part")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<SpecimenPartAttribute?>? = null
            try {
                val cb: CriteriaBuilder? = session.criteriaBuilder
                val criteria: CriteriaQuery<*> = cb.createQuery(SpecimenPartAttribute::class.java)
                val root: Root<SpecimenPartAttribute?> = criteria.from(SpecimenPartAttribute::class.java)
                criteria.where(cb.equal(root.get("specimenPartId"), part))
                val query: Query<SpecimenPartAttribute?> = session.createQuery<Any?>(criteria)
                results = query.getResultList()
                log.debug("find by example successful, result size: " + results.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("find by example failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log.error("find by example failed", re)
            throw re
        }
    }

    /**
     * Delete existing specimen part attribute record.
     *
     * @param instance of a SpecimenPartAttribute that that is to be removed.
     * @throws SaveFailedException
     */
    @Throws(SaveFailedException::class)
    fun remove(instance: SpecimenPartAttribute?) {
        log!!.debug("attaching dirty SpecimenPartAttribute instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.delete(instance)
                session.transaction.commit()
                log.debug("delete successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("delete failed", e)
                throw SaveFailedException("Unable to delete.")
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log.error("delete failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(SpecimenPartAttributeLifeCycle::class.java)
    }
}
