package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.entity.Tracking
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.TrackingLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Tracking.
 *
 * @see Tracking
 */
class TrackingLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Tracking?) {
        log!!.debug("persisting Tracking instance")
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
                throw SaveFailedException("Save to tracking table failed. " +
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

    fun attachDirty(instance: Tracking?) {
        log!!.debug("attaching dirty Tracking instance")
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
    fun attachClean(instance: Tracking?) {
        log!!.debug("attaching clean Tracking instance")
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
                throw SaveFailedException("Save to tracking table failed. " +
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
    fun delete(persistentInstance: Tracking?) {
        log!!.debug("deleting Tracking instance")
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
                throw SaveFailedException("Save to tracking table failed. " +
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
    fun merge(detachedInstance: Tracking?): Tracking? {
        log!!.debug("merging Tracking instance")
        return try {
            var result: Tracking? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as Tracking
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to tracking table failed. " +
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

    fun findById(id: Long?): Tracking? {
        log!!.debug("getting Tracking instance with id: $id")
        return try {
            var instance: Tracking? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session.get<Tracking?>(
                        Tracking::class.java, id)
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

    fun findSpecimensByUser(user: String?): MutableList<Specimen?>? {
        log!!.debug("finding all Tracking instances")
        return try {
            var results: MutableList<Specimen?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                results = session
                        .createQuery(
                                "select Specimen From Tracking t join Specimen where Users = " +
                                        user + " order by t.eventDateTime ")
                        .list()
                log.debug("find all successful, result size: " + results.size)
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

    /**
     * Find the event tracking records for a specimen by its SpecimenId.
     * Given a Specimen.SpecimenId = Tracking.SpecimenId, return a list of
     * Trackings with that SpecimenId.
     *
     * @param specimenId the SpecimenId for the specimen to find tracking records
     * for.
     * @return a list of the tracking records for that specimen.
     */
    fun findBySpecimenId(specimenId: Long?): MutableList<Tracking?>? {
        log!!.debug("finding Tracking instances by specimen")
        return try {
            var results: MutableList<Tracking?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val cb: CriteriaBuilder? = session.criteriaBuilder
                val cr: CriteriaQuery<*> = cb.createQuery(Tracking::class.java)
                val root: Root<Tracking?> = cr.from(Tracking::class.java)
                cr.select(root)
                cr.where(cb.equal(root.get("specimen"), specimenId))
                results = session.createQuery<Any?>(cr).ResultList
                //        results = session
//                      .createQuery("SELECT * From Tracking t where Specimen = " +
//                                   Long.toString(specimenId) +
//                                   " order by t.eventDateTime ")
//                      .list();
                log.debug("find by specimen id successful, result size: " + results.size)
                println("find by specimen successful, result size: " +
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
            log.error("find by specimen id failed", re)
            throw re
        }
    }

    fun findAll(): MutableList<Tracking?>? {
        log!!.debug("finding all Tracking instances")
        return try {
            var results: MutableList<Tracking?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                results = session.createQuery("From Tracking t order by t.eventDateTime")
                        .list()
                log.debug("find all successful, result size: " + results.size)
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
    }// add, only if value isn't the "" put at top of list above.// put blank at top of list.

    /**
     * Obtain a list of the distinct users who have one or more entries in the
     * tracking table.  This list may or may not correspond to the list of users
     * in the Users table.
     *
     * @return a string array of users, with a leading "" element, suitable for
     * populating a pick list.
     */
    val distinctUsers: Array<String?>?
        get() {
            val collections = ArrayList<String?>()
            collections.add("") // put blank at top of list.
            return try {
                val sql = "Select distinct user from Tracking t where t.user is not null order by t.user  "
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
        private val log = LogFactory.getLog(TrackingLifeCycle::class.java)
    }
}
