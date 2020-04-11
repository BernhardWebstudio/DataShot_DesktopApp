package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Number
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.NumberLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import java.util.*

// Generated Jan 23, 2009 8:12:35 AM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Number.
 *
 * @see Number
 */
class NumberLifeCycle {
    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Number?) {
        log!!.debug("persisting Number instance")
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
                throw SaveFailedException("Save to number table failed. " +
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
    fun attachDirty(instance: Number?) {
        log!!.debug("attaching dirty Number instance")
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
                throw SaveFailedException("Save to number table failed. " +
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
    fun attachClean(instance: Number?) {
        log!!.debug("attaching clean Number instance")
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
                throw SaveFailedException("Save to number table failed. " +
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
    fun delete(persistentInstance: Number?) {
        log!!.debug("deleting Number instance")
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
                throw SaveFailedException("Delete from number table failed. " +
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
    fun merge(detachedInstance: Number?): Number? {
        log!!.debug("merging Number instance")
        return try {
            var result = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as Number
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to number table failed. " +
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

    fun findById(id: Long?): Number? {
        log!!.debug("getting Number instance with id: $id")
        return try {
            var instance: Number? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session.get<Number?>(Number::class.java, id)
                if (instance == null) {
                    log.debug("get successful, no instance found")
                } else {
                    log.debug("get successful, instance found")
                }
                session.transaction.commit()
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
        private val log = LogFactory.getLog(NumberLifeCycle::class.java)// add, only if value isn't the "Unknown" put at top of list above.// put blank at top of list.
        // follow with "Unknown", see below.
        /**
         * Retrieve, as a string array suitable for populating a pick list,
         * the distinct values of Number.numberTypes.
         * Example usage:
         * `
         * JComboBox jComboNumberTypes = new
         * JComboBox(NumberLifeCycle.getDistinctTypes());
         * jComboNumberTypes.setEditable(true);
         * TableColumn typeColumn =
         * jTableNumbers.getColumnModel().getColumn(NumberTableModel.COLUMN_TYPE);
         * typeColumn.setCellEditor(new
         * DefaultCellEditor(jComboNumberTypes));
        ` *
         *
         * @return an array of strings consisting of { "", "Unknown", select distinct
         * numberType from Number }
         */
        val distinctTypes: Array<String?>?
            get() {
                var types = ArrayList<String?>()
                if (Singleton
                                .getProperties()
                                .getProperties()
                                .getProperty(ImageCaptureProperties.Companion.KEY_SHOW_ALL_NUMBER_TYPES)
                        == "false") {
                    return Number.Companion.getNumberTypeValues().toArray(arrayOf<String?>())
                } else {
                    types.add("") // put blank at top of list.
                    types.add("Unknown") // follow with "Unknown", see below.
                    try {
                        val sql = "Select distinct numberType from Number num where num.numberType is not null order by num.numberType  "
                        val session = HibernateUtil.sessionFactory!!.currentSession
                        try {
                            session!!.beginTransaction()
                            val q = session.createQuery(sql)
                            val i = q!!.iterate()
                            while (i!!.hasNext()) {
                                val value = (i.next() as String?)!!
                                // add, only if value isn't the "Unknown" put at top of list above.
                                if (value != "Unknown") {
                                    types.add(value)
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
                        val result = types.toArray(arrayOf<String?>())
                    } catch (re: RuntimeException) {
                        log!!.error(re)
                        types = ArrayList()
                    }
                }
                return types.toArray(arrayOf())
            }
    }
}
