/**
 * SpecimenPartLifeCycle.java
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
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.exceptions.SpecimenExistsException
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenPartLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException

/**
 *
 */
class SpecimenPartLifeCycle {
    /**
     * Save a new specimen record, and add an entry in the tracking table.
     *
     * @param transientInstance instance of a Specimen that doesn't have a
     * matching
     * database record which is to be saved as a new record in the database.
     * @throws SaveFailedException
     * @throws SpecimenExistsException
     */
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: SpecimenPart?) {
        log!!.debug("persisting SpecimenPart instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(transientInstance)
                session.transaction.commit()
                log.debug("persist successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("persist failed", e)
                throw SaveFailedException("Unable to save specimenPart ")
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

    /**
     * Re-associate a transient instance with a session.
     *
     * @param instance
     */
    fun attachClean(instance: SpecimenPart?) {
        log!!.debug("attaching clean SpecimenPart instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                session.flush()
                session.transaction.commit()
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("attach failed", e)
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

    /**
     * Save or update an existing specimen part record.
     *
     * @param instance of a SpecimenPart that that is to be saved.
     * @throws SaveFailedException
     */
    @Throws(SaveFailedException::class)
    fun attachDirty(instance: SpecimenPart?) {
        log!!.debug("attaching dirty SpecimenPart instance")
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

    /**
     * Update db record and log current status of record.
     *
     * @param detachedInstance
     * @return the current specimen record.
     */
    fun merge(detachedInstance: SpecimenPart?): SpecimenPart {
        log!!.debug("merging SpecimenPart instance")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val result: SpecimenPart = session.merge(detachedInstance) as SpecimenPart
                session.transaction.commit()
                log.debug("merge successful")
                try {
                    session.close()
                } catch (e: SessionException) {
                }
                result
            } catch (e: HibernateException) {
                session.transaction.rollback()
                try {
                    session.close()
                } catch (e1: SessionException) {
                }
                log.error("merge failed", e)
                throw e
            }
        } catch (re: RuntimeException) {
            log.error("merge failed", re)
            throw re
        }
    }

    /**
     * Save or update an existing specimen part record.
     *
     * @param instance of a SpecimenPart that that is to be removed.
     * @throws SaveFailedException
     */
    @Throws(SaveFailedException::class)
    fun remove(instance: SpecimenPart?) {
        log!!.debug("attaching dirty SpecimenPart instance")
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
        private val log = LogFactory.getLog(SpecimenPartLifeCycle::class.java)
    }
}
