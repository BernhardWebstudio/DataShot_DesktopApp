/**
 * MCZbaseGeogAuthRecLifeCycle.java
 * edu.harvard.mcz.imagecapture.data
 *
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 */
package edu.harvard.mcz.imagecapture.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 *
 */
public class MCZbaseGeogAuthRecLifeCycle {
    private static final Log log =
            LogFactory.getLog(MCZbaseGeogAuthRecLifeCycle.class);

    /**
     * @return
     */
    public List<MCZbaseGeogAuthRec> findAll() {
        log.debug("finding all Higher geographies");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<MCZbaseGeogAuthRec> results = null;
            try {
                results =
                        (List<MCZbaseGeogAuthRec>) session
                                .createQuery("from MCZbaseGeogAuthRec h order by h.higher_geog")
                                .setReadOnly(true)
                                .list();
                log.debug("find all successful, result size: " + results.size());
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error("find all failed", e);
            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return results;
        } catch (RuntimeException re) {
            log.error("Find all failed.  ", re);
            throw re;
        }
    }

    /**
     * @param pattern
     * @return
     */
    public List<MCZbaseGeogAuthRec> findByExample(MCZbaseGeogAuthRec pattern) {
        log.debug("finding Higher Geographies by example");
        try {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            List<MCZbaseGeogAuthRec> results = null;
            try {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery cr =
                        criteriaBuilder.createQuery(MCZbaseGeogAuthRec.class);
                // TODO: refactor to use reflection

                log.debug("find by example successful, result size: " + results.size());
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                log.error("find by example failed", e);
            }
            try {
                session.close();
            } catch (SessionException e) {
            }
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
}
