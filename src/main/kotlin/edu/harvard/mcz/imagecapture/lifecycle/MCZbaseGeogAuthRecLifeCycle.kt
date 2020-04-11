/**
 * MCZbaseGeogAuthRecLifeCycle.java
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
import edu.harvard.mcz.imagecapture.entity.MCZbaseGeogAuthRec
import edu.harvard.mcz.imagecapture.lifecycle.MCZbaseGeogAuthRecLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.SessionException
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery

/**
 *
 */
class MCZbaseGeogAuthRecLifeCycle {
    /**
     * @return
     */
    fun findAll(): MutableList<MCZbaseGeogAuthRec?>? {
        log!!.debug("finding all Higher geographies")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<MCZbaseGeogAuthRec?>? = null
            try {
                results = session
                        .createQuery("from MCZbaseGeogAuthRec h order by h.higher_geog")
                        .setReadOnly(true)
                        .list() as MutableList<MCZbaseGeogAuthRec?>
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
            log.error("Find all failed.  ", re)
            throw re
        }
    }

    /**
     * @param pattern
     * @return
     */
    fun findByExample(pattern: MCZbaseGeogAuthRec?): MutableList<MCZbaseGeogAuthRec?>? {
        log!!.debug("finding Higher Geographies by example")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            val results: MutableList<MCZbaseGeogAuthRec?>? = null
            try {
                val criteriaBuilder: CriteriaBuilder? = session.criteriaBuilder
                val cr: CriteriaQuery<*> = criteriaBuilder.createQuery(MCZbaseGeogAuthRec::class.java)
                // TODO: refactor to use reflection
                log.debug("find by example successful, result size: " + results!!.size)
                session.transaction.commit()
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error("find by example failed", e)
            }
            try {
                session.close()
            } catch (e: SessionException) {
            }
            results
        } catch (re: RuntimeException) {
            log.error("find by example failed", re)
            throw re
        }
    }

    companion object {
        private val log = LogFactory.getLog(MCZbaseGeogAuthRecLifeCycle::class.java)
    }
}
