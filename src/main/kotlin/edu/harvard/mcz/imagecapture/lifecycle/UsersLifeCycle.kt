package edu.harvard.mcz.imagecapture.lifecycle


import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.exceptions.NoSuchValueException
import edu.harvard.mcz.imagecapture.exceptions.SaveFailedException
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.LockMode
import org.hibernate.SessionException
import org.hibernate.query.Query
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

// Generated Feb 5, 2009 5:23:55 PM by Hibernate Tools 3.2.2.GA
/**
 * Home object for domain model class Users.
 *
 * @see Users
 */
class UsersLifeCycle {
    @Throws(NoSuchValueException::class)
    fun getFullNameForUserName(aUsername: String?): String? {
        var returnValue = ""
        val session = HibernateUtil.sessionFactory!!.currentSession
        session!!.beginTransaction()
        try {
            val results: MutableList<Users?> = (session.createQuery("from Users as u where u.username = '$aUsername'").list() as MutableList<Users?>)
            session.transaction.commit()
            if (results.size > 0) {
                returnValue = results[0]!!.fullname.toString()
            } else {
                throw NoSuchValueException("Couldn't find a person with a username of [$aUsername]")
            }
        } catch (e: HibernateException) {
            session.transaction.rollback()
            session.close()
            log!!.error("Can't retrieve username. " + e.message)
        }
        return returnValue
    }

    @Throws(SaveFailedException::class)
    fun persist(transientInstance: Users?) {
        log!!.debug("persisting Users instance")
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
                throw SaveFailedException("Save to Users table failed. " + e.message)
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
    fun attachDirty(instance: Users?) {
        log!!.debug("attaching dirty Users instance")
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
                throw SaveFailedException("Save to Users table failed. " + e.message)
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

    fun attachClean(instance: Users?) {
        log!!.debug("attaching clean Users instance")
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
    fun delete(persistentInstance: Users?) {
        log!!.debug("deleting Users instance")
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
                throw SaveFailedException("Delete from Users table failed. " + e.message)
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
    fun merge(detachedInstance: Users?): Users? {
        log!!.debug("merging Users instance")
        return try {
            var result: Users? = detachedInstance
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                result = session.merge(detachedInstance) as Users
                session.transaction.commit()
                log.debug("merge successful")
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw SaveFailedException("Save to Users table failed. " + e.message)
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

    fun findById(id: Int?): Users? {
        log!!.debug("getting Users instance with id: $id")
        return try {
            var instance: Users? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                instance = session.get<Users?>(Users::class.java, id)
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

    fun findByNames(username: String?, fullName: String?): MutableList<Users?>? {
        assert(username != null || fullName != null)
        log!!.debug("finding Users instance by names")
        return try {
            var results: MutableList<Users?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val cb: CriteriaBuilder? = session.criteriaBuilder
                val cr: CriteriaQuery<*> = cb.createQuery(Users::class.java)
                val root: Root<Users?> = cr.from(Users::class.java)
                cr.select(root)
                val propertyValueRelations: MutableList<Predicate?> = ArrayList()
                if (username != null) {
                    propertyValueRelations.add(cb.equal(root.get("username"), username))
                }
                if (fullName != null) {
                    propertyValueRelations.add(cb.equal(root.get("fullname"), fullName))
                }
                cr.where(cb.and(*propertyValueRelations.toTypedArray()))
                results = session.createQuery<Any?>(cr).list()
                log.debug("find by name successful, result size: "
                        + results.size)
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log.error("find users by name failed", re)
            throw re
        }
    }

    fun findByCredentials(username: String?, password: String?): MutableList<Users> {
        return try {
            var results: MutableList<Users?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            try {
                session!!.beginTransaction()
                val builder: CriteriaBuilder = session.criteriaBuilder
                var query: CriteriaQuery<*> = builder.createQuery(Users::class.java)
                val root: Root<Users> = query.from(Users::class.java)
                query = query.select(root)
                val propertyValueRelations: MutableList<Predicate> = ArrayList()
                propertyValueRelations.add(builder.like(root.get("username"), username))
                propertyValueRelations.add(builder.like(root.get("hash"), password))
                val res: Predicate = builder.and(*propertyValueRelations.toTypedArray())
                query = query.where(res)
                val q: Query<*> = session.createQuery(query)
                results = q.list() as MutableList<Users>
                log!!.debug("find by credentials successful, result size: "
                        + results!!.size)
            } catch (e: HibernateException) {
                session!!.transaction.rollback()
                log!!.error(e.message)
            }
            try {
                session!!.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log!!.error("find users by credentials failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(UsersLifeCycle::class.java)
        fun isUserAdministrator(aUserID: Int): Boolean {
            var returnValue = false
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val results: MutableList<Users?> = (session.createQuery("from Users as u where role = '" + Users.Companion.ROLE_ADMINISTRATOR + "' and u.userid = '" + aUserID + "'").list() as MutableList<Users?>)
                session.transaction.commit()
                if (results.size == 1) {
                    returnValue = results[0].role == "Administrator"
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                session.close()
                log!!.error("Can't retrieve username. " + e.message)
            }
            return returnValue
        }

        fun isUserChiefEditor(aUserID: Int): Boolean {
            var returnValue = false
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                val results: MutableList<Users?> = (session.createQuery("from Users as u where u.userid = '$aUserID'").list() as MutableList<Users?>)
                session.transaction.commit()
                if (results.size == 1) {
                    returnValue = results[0].isUserRole(Users.Companion.ROLE_CHIEF_EDITOR)
                }
            } catch (e: HibernateException) {
                session.transaction.rollback()
                session.close()
                log!!.error("Can't retrieve username. " + e.message)
            }
            return returnValue
        }

        /**
         * @return
         */
        fun findAll(): MutableList<Users?>? {
            log!!.debug("finding all Users")
            return try {
                var results: MutableList<Users?>? = null
                val session = HibernateUtil.sessionFactory!!.currentSession
                try {
                    session!!.beginTransaction()
                    results = session.createQuery("from Users u order by u.username ").list() as MutableList<Users?>
                    session.transaction.commit()
                    log.debug("find by example successful, result size: "
                            + results!!.size)
                } catch (e: HibernateException) {
                    session!!.transaction.rollback()
                    log.error(e.message)
                } finally {
                    try {
                        session!!.close()
                    } catch (e: SessionException) {
                    }
                }
                results
            } catch (re: RuntimeException) {
                log.error("find by example failed", re)
                throw re
            }
        }
    }
}
