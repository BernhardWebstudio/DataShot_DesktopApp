/**
 * UnitTrayLabelLifeCycle.java
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
import edu.harvard.mcz.imagecapture.entity.UnitTrayLabel
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.UnitTrayLabelLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException

/**
 * UnitTrayLabelLifeCycle
 */
class UnitTrayLabelLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: UnitTrayLabel?) {
        log!!.debug("persisting UnitTrayLabel instance")
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
                throw SaveFailedException("Save to UnitTrayLabel table failed. " + e.message)
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
    fun attachDirty(instance: UnitTrayLabel?) {
        log!!.debug("attaching dirty UnitTrayLabel instance")
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
                throw SaveFailedException("Save to UnitTrayLabel table failed. " + e.message)
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

    fun attachClean(instance: UnitTrayLabel?) {
        log!!.debug("attaching clean UnitTrayLabel instance")
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
    fun delete(persistentInstance: UnitTrayLabel?) {
        log!!.debug("deleting UnitTrayLabel instance")
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
                throw SaveFailedException("Delete from UnitTrayLabel table failed. " + e.message)
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
    fun merge(detachedInstance: UnitTrayLabel?): UnitTrayLabel? {
        log!!.debug("merging UnitTrayLabel instance")
        return try {
            var result: UnitTrayLabel? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as UnitTrayLabel
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to UnitTrayLabel table failed. " + e.message)
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

    fun findById(id: Int?): UnitTrayLabel? {
        log!!.debug("getting UnitTrayLabel instance with id: $id")
        return try {
            var instance: UnitTrayLabel? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session.get<UnitTrayLabel?>(UnitTrayLabel::class.java, id)
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

    /**
     * @return
     */
    fun findAll(): MutableList<UnitTrayLabel?>? {
        log!!.debug("finding all UnitTrayLabel")
        return try {
            var results: MutableList<UnitTrayLabel?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                results = session.createQuery("from UnitTrayLabel u order by u.ordinal, u.family, u.subfamily, u.tribe, u.genus, u.specificEpithet ").list() as MutableList<UnitTrayLabel?>
                session.transaction.commit()
                log.debug("find by example successful, result size: "
                        + results!!.size)
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
            } finally {
                try {
                    session.close()
                } catch (e: SessionException) {
                }
            }
            results
        } catch (re: RuntimeException) {
            log.error("find by example failed", re)
            throw re
        }
    }

    fun findMaxOrdinal(): Int {
        log!!.debug("finding max ordinal in UnitTrayLabel")
        var result = 0
        try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val queryString = "select max(ordinal) from UnitTrayLabel"
                val query = session.createQuery(queryString)
                val queryResult = query!!.list()
                if (!queryResult!!.isEmpty()) { // MySQL returns an integer, Oracle returns a BigDecimal
// Need to cast from either in a system independent way.
// NOTE: This will fail if maximum value of ordinal exceeds the size of Integer.
                    val temp = queryResult[0].toString()
                    result = Integer.valueOf(temp)
                    log.debug(result)
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
            } finally {
                try {
                    session.close()
                } catch (e: SessionException) {
                }
            }
        } catch (re: RuntimeException) {
            log.error("find max ordinal failed", re)
            throw re
        }
        return result
    }

    companion object {
        private val log = LogFactory.getLog(UnitTrayLabelLifeCycle::class.java)
    }
}
