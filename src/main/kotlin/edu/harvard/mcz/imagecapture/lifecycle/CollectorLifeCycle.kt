package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Collector
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.CollectorLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import java.util.*

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Collector.
 *
 * @see Collector
 */
class CollectorLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Collector) {
        if (transientInstance.collectorName !== "") {
            log!!.debug("persisting Collector instance")
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
                    throw SaveFailedException("Unable to save collector record. " +
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
    }

    @Throws(SaveFailedException::class)
    fun attachDirty(instance: Collector?) {
        log!!.debug("attaching dirty Collector instance")
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
                throw SaveFailedException("Unable to save collector record. " +
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

    fun attachClean(instance: Collector?) {
        log!!.debug("attaching clean Collector instance")
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
    fun delete(persistentInstance: Collector) {
        log!!.debug("Deleting Collector instance with id " +
                persistentInstance.collectorId)
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
                throw SaveFailedException("Unable to Delete collector record. " +
                        e.message)
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
    fun merge(detachedInstance: Collector?): Collector? {
        log!!.debug("merging Collector instance")
        return try {
            var result = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as Collector
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Unable to save collector record. " +
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

    fun findById(id: Long?): Collector? {
        log!!.debug("getting Collector instance with id: $id")
        return try {
            var instance: Collector? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session.get<Collector?>(Collector::class.java, id)
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
    }// add, only if value isn't the "" put at top of list above.

    // put blank at top of list.
    val distinctCollectors: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct collectorName from Collector col where col.collectorName is not null order by col.collectorName "
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        // add, only if value isn't the "" put at top of list above.
                        if (value != "") {
                            collections.add(value.trim { it <= ' ' })
                        }
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                }
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
                collections.toArray(arrayOf())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

    companion object {
        private val log = LogFactory.getLog(CollectorLifeCycle::class.java)
    }
}
