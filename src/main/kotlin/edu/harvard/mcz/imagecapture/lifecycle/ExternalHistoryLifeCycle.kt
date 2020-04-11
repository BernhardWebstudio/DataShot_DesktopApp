/**
 * ExternalHistoryLifeCycle.java
 *
 *
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
import edu.harvard.mcz.imagecapture.entity.ExternalHistory
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.ExternalHistoryLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException

/**
 *
 */
class ExternalHistoryLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: ExternalHistory) {
        if (transientInstance.ExternalWorkflowProcess !== "") {
            log!!.debug("persisting ExternalHistory instance")
            try {
                val session = HibernateUtil.sessionFactory!!.currentSession
                session!!.beginTransaction()
                try {
                    session.persist(transientInstance)
                    session.transaction.commit()
                    log.debug("persist successful")
                } catch (e: HibernateException) {
                    session.transaction.rollback()
                    log.error(e.message)
                    throw SaveFailedException(
                            "Unable to save externalHistory record. " + e.message)
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
    }

    @Throws(SaveFailedException::class)
    fun attachDirty(instance: ExternalHistory?) {
        log!!.debug("attaching dirty ExternalHistory instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                session.transaction.commit()
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException(
                        "Unable to save externalHistory record. " + e.message)
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

    fun attachClean(instance: ExternalHistory?) {
        log!!.debug("attaching clean ExternalHistory instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                session.transaction.commit()
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
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
    fun delete(persistentInstance: ExternalHistory?) {
        log!!.debug("deleting ExternalHistory instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.delete(persistentInstance)
                session.transaction.commit()
                log.debug("delete successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException(
                        "Unable to Delete externalHistory record. " + e.message)
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

    @Throws(SaveFailedException::class)
    fun merge(detachedInstance: ExternalHistory?): ExternalHistory? {
        log!!.debug("merging ExternalHistory instance")
        return try {
            var result: ExternalHistory? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as ExternalHistory
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException(
                        "Unable to save externalHistory record. " + e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            result
        } catch (re: RuntimeException) {
            log.error("merge failed", re)
            throw re
        }
    }

    fun findById(id: Long?): ExternalHistory? {
        log!!.debug("getting ExternalHistory instance with id: $id")
        return try {
            var instance: ExternalHistory? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session["edu.harvard.mcz.imagecapture.data.ExternalHistory", id] as ExternalHistory
                session.transaction.commit()
                if (instance == null) {
                    log.debug("get successful, no instance found")
                } else {
                    log.debug("get successful, instance found")
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            instance
        } catch (re: RuntimeException) {
            log.error("get failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(ExternalHistoryLifeCycle::class.java)
    }
}
