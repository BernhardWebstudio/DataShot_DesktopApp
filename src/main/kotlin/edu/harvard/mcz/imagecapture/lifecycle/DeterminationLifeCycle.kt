package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Determination
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.DeterminationLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Determination.
 *
 * @see Determination
 */
class DeterminationLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Determination?) {
        log!!.debug("persisting Determination instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.persist(transientInstance)
                log.debug("persist successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Unable to save determination record. " +
                        e.message)
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

    @Throws(SaveFailedException::class)
    fun attachDirty(instance: Determination?) {
        log!!.debug("attaching dirty Determination instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.saveOrUpdate(instance)
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Unable to save determination record. " +
                        e.message)
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
    fun attachClean(instance: Determination?) {
        log!!.debug("attaching clean Determination instance")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                session.lock(instance, LockMode.NONE)
                log.debug("attach successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Unable to save determination record. " +
                        e.message)
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
    fun delete(persistentInstance: Determination) {
        log!!.debug("Deleting Determination instance with id " +
                persistentInstance.DeterminationId)
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
                        "Unable to delete determination record. " + e.message)
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
    fun merge(detachedInstance: Determination?): Determination? {
        log!!.debug("merging Determination instance")
        return try {
            var result: Determination? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                result = session.merge(detachedInstance) as Determination
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Unable to save determination record. " +
                        e.message)
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

    fun findById(id: Long?): Determination? {
        log!!.debug("getting Determination instance with id: $id")
        return try {
            var instance: Determination? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                instance = session["edu.harvard.mcz.imagecapture.data.Determination", id] as Determination
                if (instance == null) {
                    log.debug("get successful, no instance found")
                } else {
                    log.debug("get successful, instance found")
                }
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            instance
        } catch (re: RuntimeException) {
            log.error("get failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(DeterminationLifeCycle::class.java)
    }
}
