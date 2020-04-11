package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Template
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.TemplateLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Template.
 *
 * @see Template
 */
class TemplateLifeCycle {
    fun cleanUpReferenceImage() {
        log!!.debug("Trying to set all Template.referenceimage values to null.")
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val queryString = "update Template set referenceImage = null"
                val query = session.createQuery(queryString)
                val rows = query!!.executeUpdate()
                session.transaction.commit()
                log.debug("changed $rows rows")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
        } catch (re: RuntimeException) {
            log.error("Set all Template.referenceimage to null failed", re)
            throw re
        }
    }

    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Template?) {
        log!!.debug("persisting Template instance")
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
                throw SaveFailedException("Save to template table failed. " +
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

    fun attachDirty(instance: Template?) {
        log!!.debug("attaching dirty Template instance")
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
    fun attachClean(instance: Template?) {
        log!!.debug("attaching clean Template instance")
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
                throw SaveFailedException("Save to template table failed. " +
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
    fun delete(persistentInstance: Template?) {
        log!!.debug("deleting Template instance")
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
                throw SaveFailedException("Save to template table failed. " +
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
    fun merge(detachedInstance: Template?): Template? {
        log!!.debug("merging Template instance")
        return try {
            var result = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as Template
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to template table failed. " +
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

    fun findById(id: String?): Template? { // log.debug("getting Template instance with id: " + id);
        return try {
            var instance: Template? = null
            // log.debug("getting Template instance 1");
// this causes infinite loop
            val session = HibernateUtil.sessionFactory!!.currentSession
            // never reaches here
            log!!.debug("getting Template instance 2")
            session!!.beginTransaction()
            try {
                instance = session.get<Template?>(
                        Template::class.java, id)
                session.transaction.commit()
                if (instance == null) {
                    log.debug("template get successful, no instance found")
                } else {
                    log.debug("template get successful, instance found")
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
            log!!.error("get failed", re)
            throw re
        }
    }

    fun findAll(): MutableList<Template?>? {
        log!!.debug("finding all Template instances")
        return try {
            var results: MutableList<Template?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                results = session.createQuery("From Template t order by t.name").list()
                log.debug("find all successful, result size: " + results!!.size)
                println("find all successful, result size: " +
                        results.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                println(e.message)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log.error("find all failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(TemplateLifeCycle::class.java)
    }
}
