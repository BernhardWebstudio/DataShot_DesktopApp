/**
 * MCZbaseGeogAuthRecLifeCycle.java
 * edu.harvard.mcz.imagecapture.data
 * Copyright © 2013 President and Fellows of Harvard College
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of Version 2 of the GNU General Public License
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Author: Paul J. Morris
 */
package edu.harvard.mcz.imagecapture.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SessionException;
import org.hibernate.Session;

/**
 * @author mole
 *
 */
public class MCZbaseAuthAgentNameLifeCycle {
	private static final Log log = LogFactory.getLog(MCZbaseAuthAgentNameLifeCycle.class);

	/**
	 * @return
	 */
	public List<MCZbaseAuthAgentName> findAll() {
		log.debug("finding all agent names");
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			List<MCZbaseAuthAgentName> results = null;
			try { 
			    results = (List<MCZbaseAuthAgentName>) session.createQuery("from MCZbaseAuthAgentName h order by h.agent_name").setReadOnly(true).list();
			    log.debug("find all successful, result size: " + results.size());
			    session.getTransaction().commit();
		    } catch (HibernateException e) {
		    	session.getTransaction().rollback();
		    	log.error("find all failed", e);	
		    }
		    try { session.close(); } catch (SessionException e) { }
			return results;
		} catch (RuntimeException re) {
			log.error("Find all failed.  ", re);
			throw re;
		}
	}
}
