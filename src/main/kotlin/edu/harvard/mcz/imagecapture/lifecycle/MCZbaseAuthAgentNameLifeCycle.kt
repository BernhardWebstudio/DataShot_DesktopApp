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
import edu.harvard.mcz.imagecapture.entity.MCZbaseAuthAgentName
import edu.harvard.mcz.imagecapture.lifecycle.MCZbaseAuthAgentNameLifeCycle
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import org.hibernate.SessionException

/**
 *
 */
class MCZbaseAuthAgentNameLifeCycle {
    /**
     * @return
     */
    fun findAll(): MutableList<MCZbaseAuthAgentName?>? {
        log!!.debug("finding all agent names")
        return try {
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            var results: MutableList<MCZbaseAuthAgentName?>? = null
            try {
                results = session
                        .createQuery(
                                "from MCZbaseAuthAgentName h order by h.agent_name")
                        .setReadOnly(true)
                        .list() as MutableList<MCZbaseAuthAgentName?>
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

    companion object {
        private val log = LogFactory.getLog(MCZbaseAuthAgentNameLifeCycle::class.java)
    }
}
