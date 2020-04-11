package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.HigherTaxon
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.HigherTaxonLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import java.util.*

class HigherTaxonLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: HigherTaxon?) {
        log!!.debug("persisting HigherTaxon instance")
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
                throw SaveFailedException("Save to HigherTaxon table failed. " +
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
    fun attachDirty(instance: HigherTaxon?) {
        log!!.debug("attaching dirty ICImage instance")
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
                throw SaveFailedException("Save to HigherTaxon table failed. " +
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
    fun attachClean(instance: HigherTaxon?) {
        log!!.debug("attaching clean HigherTaxon instance")
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
                throw SaveFailedException("Save to HigherTaxon table failed. " +
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
    fun delete(persistentInstance: HigherTaxon?) {
        log!!.debug("deleting HigherTaxon instance")
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
                throw SaveFailedException("Delete from HigherTaxon table failed. " +
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
    fun merge(detachedInstance: HigherTaxon?): HigherTaxon? {
        log!!.debug("merging ICImage instance")
        return try {
            var result: HigherTaxon? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as HigherTaxon
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to HigherTaxon table failed. " +
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

    fun findById(id: Long?): HigherTaxon? {
        log!!.debug("getting HigherTaxon instance with id: $id")
        return try {
            var instance: HigherTaxon? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session["edu.harvard.mcz.imagecapture.data.HigherTaxon", id] as HigherTaxon
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
     * @return list of all higher files sorted by family
     */
    fun findAll(): MutableList<HigherTaxon?>? {
        log!!.debug("finding all Higher Taxa")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<HigherTaxon?>? = null
            try {
                results = session
                        .createQuery("From HigherTaxon ht order by ht.family")
                        .list() as MutableList<HigherTaxon?>
                log.debug("find all successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("find all failed", e)
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

    fun isMatched(aFamily: String?, aSubfamily: String?): Boolean {
        var result = false
        return try {
            val sql = "Select distinct family, subfamily from HigherTaxon ht  where soundex(ht.family) = soundex('" +
                    aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                    "')  "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val q = session.createQuery(sql)
                val i = q!!.iterate()
                if (i!!.hasNext()) {
                    result = true
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            } finally {
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
            }
            result
        } catch (re: RuntimeException) {
            log!!.error(re)
            false
        }
    }

    fun isMatched(aFamily: String?, aSubfamily: String?, aTribe: String?): Boolean {
        var result = false
        return try {
            val sql = "Select distinct family, subfamily from HigherTaxon ht where soundex(ht.family) = soundex('" +
                    aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                    "') and soundex(ht.tribe) = soundex('" + aTribe + "')  "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val q = session.createQuery(sql)
                val i = q!!.iterate()
                if (i!!.hasNext()) {
                    result = true
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            } finally {
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
            }
            result
        } catch (re: RuntimeException) {
            log!!.error(re)
            false
        }
    }

    /**
     * Find the first soundex match to family in the Higher Taxon authority file.
     *
     * @param aFamily
     * @return a String containing the matched family name, null
     * if no match or a connection problem.
     */
    fun findMatch(aFamily: String?): String? {
        var result: String? = null
        return try {
            val sql = "Select distinct family from HigherTaxon ht  where soundex(ht.family) = soundex('" +
                    aFamily + "')  "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val q = session.createQuery(sql)
                val results = q!!.list().iterator()
                if (results.hasNext()) { // retrieve one row.
                    result = results.next() as String?
                    // store the family and subfamily from that row in the array to
// return.
                    log!!.debug(result)
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            } finally {
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
            }
            result
        } catch (re: RuntimeException) {
            log!!.error(re)
            null
        }
    }

    /**
     * Find the first soundex match to both family and subfamily in the Higher
     * Taxon authority file.
     *
     * @param aFamily
     * @param aSubfamily
     * @return a String array with the 0th element being the family name and the
     * 1st element being the subfamily name, null
     * if no match or a connection problem.
     */
    fun findMatch(aFamily: String?, aSubfamily: String?): Array<String?>? {
        var result: Array<String?>? = null
        return try {
            val sql = "Select distinct family, subfamily from HigherTaxon ht  where soundex(ht.family) = soundex('" +
                    aFamily + "') and soundex(ht.subfamily) = soundex('" + aSubfamily +
                    "')  "
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val q = session.createQuery(sql)
                val results = q!!.list().iterator()
                if (results.hasNext()) { // create a two element string array.
                    result = arrayOfNulls<String?>(2)
                    // retrieve one row.
                    val row = results.next() as Array<Any?>?
                    // store the family and subfamily from that row in the array to
// return.
                    result[0] = row!![0] as String?
                    result[1] = row[1] as String?
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            } finally {
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
            }
            result
        } catch (re: RuntimeException) {
            log!!.error(re)
            null
        }
    }

    /**
     * Return the first soundex match to family, subfamily, and tribe from the
     * higher taxon authority file.
     *
     * @param aFamily    the text string to check for a matching family name.
     * @param aSubfamily the text string to check for a matching subfamily name.
     * @param aTribe     the text string to check for a matching tribe name.
     * @return a string array of {family,subfamily,tribe} from the database, or
     * null if no match or a connection
     * problem.
     */
    fun findMatch(aFamily: String?, aSubfamily: String?, aTribe: String?): Array<String?>? {
        var result: Array<String?>? = null
        return try {
            val sql = ("Select distinct family, subfamily, tribe from HigherTaxon ht  where "
                    + "soundex(ht.family) = soundex('" + aFamily + "') and "
                    + "soundex(ht.subfamily) = soundex('" + aSubfamily + "')and "
                    + "soundex(ht.tribe) = soundex('" + aTribe + "')  ")
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val q = session.createQuery(sql)
                val results = q!!.list().iterator()
                if (results.hasNext()) { // create a two element string array.
                    result = arrayOfNulls<String?>(3)
                    // retrieve one row.
                    val row = results.next() as Array<Any?>?
                    // store the family, subfamily, and tribe from that row in the array
// to return.
                    result[0] = row!![0] as String?
                    result[1] = row[1] as String?
                    result[2] = row[2] as String?
                }
                session.transaction.commit()
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            } finally {
                try {
                    session!!.close()
                } catch (e: SessionException) {
                }
            }
            result
        } catch (re: RuntimeException) {
            log!!.error(re)
            null
        }
    }

    fun isFamilyWithCastes(family: String?): Boolean {
        var result = false
        val session = HibernateUtil.sessionFactory!!.currentSession
        val q = session!!.createQuery(
                "select count(h) from HigherTaxon h where h.hasCaste = 1 and h.Family = ? ")
        q!!.setParameter(0, family)
        val results = q.list().iterator()
        while (results.hasNext()) {
            val row = results.next() as Array<Any?>?
            val value = (row!![0] as Int?)!!
            if (value > 0) {
                result = true
            }
        }
        return result
    }

    companion object {
        private val log = LogFactory.getLog(HigherTaxonLifeCycle::class.java)
        fun selectDistinctFamily(): Array<String?>? {
            val result: MutableList<String?> = ArrayList()
            return try {
                val sql = " Select distinct family from HigherTaxon ht where ht.family is not null "
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        result.add(value)
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                } finally {
                    try {
                        session!!.close()
                    } catch (e: SessionException) {
                    }
                }
                result.toArray(arrayOf<String?>())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

        fun selectDistinctSubfamily(family: String?): Array<String?>? {
            val result: MutableList<String?> = ArrayList()
            return try {
                var sql = ""
                sql = if (family == null || family == "") {
                    " Select distinct subfamily from HigherTaxon ht where ht.subfamily is not null order by subfamily "
                } else {
                    " Select distinct subfamily from HigherTaxon ht where ht.family = '" +
                            family.trim { it <= ' ' } +
                            "' and ht.subfamily is not null order by subfamily "
                }
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    var q = session.createQuery(sql)
                    var i = q!!.iterate()
                    if (!i!!.hasNext()) { // No results, try without where family='?'
                        sql = " Select distinct subfamily from HigherTaxon ht where ht.subfamily is not null "
                        q = session.createQuery(sql)
                        i = q.iterate()
                    }
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        result.add(value)
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                } finally {
                    try {
                        session!!.close()
                    } catch (e: SessionException) {
                    }
                }
                result.toArray(arrayOf<String?>())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }

        fun selectDistinctTribe(subfamily: String): Array<String?>? {
            val result: MutableList<String?> = ArrayList()
            return try {
                val sql = " Select distinct tribe from HigherTaxon ht where ht.subfamily = '" +
                        subfamily.trim { it <= ' ' } + "' and ht.tribe is not null "
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    val q = session.createQuery(sql)
                    val i = q!!.iterate()
                    while (i!!.hasNext()) {
                        val value = (i.next() as String?)!!
                        result.add(value)
                    }
                    session.transaction.commit()
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log!!.error(e.message)
                } finally {
                    try {
                        session!!.close()
                    } catch (e: SessionException) {
                    }
                }
                result.toArray(arrayOf<String?>())
            } catch (re: RuntimeException) {
                log!!.error(re)
                arrayOf()
            }
        }
    }
}
