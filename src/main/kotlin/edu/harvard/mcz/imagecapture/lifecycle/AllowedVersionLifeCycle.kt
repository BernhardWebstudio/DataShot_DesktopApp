/**
 * AllowedVersionLifeCycle.java
 *
 *
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


import edu.harvard.mcz.imagecapture.ImageCaptureApp
import edu.harvard.mcz.imagecapture.data.HibernateUtil
import edu.harvard.mcz.imagecapture.entity.AllowedVersion
import edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycle
import org.apache.commons.logging.LogFactory
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.FlywayException
import org.hibernate.HibernateException
import org.hibernate.SessionException

/**
 *
 */
class AllowedVersionLifeCycle {
    /**
     * Provide a list of all the allowed versions listed in the database.
     *
     * @return List[AllowedVersion] listing all versions according to database
     */
    fun findAll(): MutableList<AllowedVersion?>? {
        log!!.debug("finding all AllowedVersions")
        return try {
            var results: MutableList<AllowedVersion?>? = null
            val session = HibernateUtil.sessionFactory!!.currentSession
            session!!.beginTransaction()
            try {
                results = session
                        .createQuery("SELECT a FROM AllowedVersion a")
                        .list() as MutableList<AllowedVersion?>
                session.transaction.commit()
                log.debug("find all successful, result size: " + results!!.size)
            } catch (e: HibernateException) {
                session.transaction.rollback()
                log.error(e.message)
                throw e
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
        private val log = LogFactory.getLog(AllowedVersionLifeCycle::class.java)
        /**
         * Check to see if the allowed versions in the database include the version
         * of the current ImageCaptureApp.
         *
         * @return true if an AllowedVersion.version in the database is found at the
         * beginning of ImageCaptureApp.APP_VERSION, otherwise false.
         */
        @get:Throws(HibernateException::class)
        val isCurrentAllowed: Boolean
            get() {
                var result = false
                val als = AllowedVersionLifeCycle()
                val allowedVersions: MutableList<AllowedVersion?>? = als.findAll()
                val i: MutableIterator<AllowedVersion?> = allowedVersions!!.iterator()
                while (i.hasNext()) {
                    val version: String = i.next().getVersion()
                    val currentVersion: String = ImageCaptureApp.getAppVersion()
                    if (version != null && currentVersion != null && version.length <= currentVersion.length &&
                            currentVersion.startsWith(version)) {
                        result = true
                    }
                }
                return result
            }

        /**
         * Provide a human readable list of the allowed versions listed in the
         * database.
         *
         * @return string listing allowed versions according to database.
         */
        fun listAllowedVersions(): String {
            val allowed = StringBuilder()
            val als = AllowedVersionLifeCycle()
            try {
                val allowedVersions: MutableList<AllowedVersion?>? = als.findAll()
                val i: MutableIterator<AllowedVersion?> = allowedVersions!!.iterator()
                var separator = ""
                while (i.hasNext()) {
                    allowed.append(separator).append(i.next().getVersion())
                    separator = ", "
                }
            } catch (e: Exception) {
                log!!.error(e.message)
            }
            return allowed.toString()
        }

        /**
         * Execute a migration, powered by FlyWay (https://flywaydb.org/)
         */
        fun upgrade() { // Load the Configuration from hibernate.cfg.xml
/*SessionFactoryImpl sessionFactory =
                (SessionFactoryImpl) HibernateUtil.sessionFactory;
        Map<String, Object> properties = sessionFactory.getProperties();
        String url = (String) properties.get("connection.url");
        String username = (String) properties.get("connection.username");
        String password = (String) properties.get("connection.password");*/
            val properties = HibernateUtil.getProperties()
            val url = properties.getProperty("hibernate.connection.url")
            val username = properties.getProperty("hibernate.connection.username")
            val password = properties.getProperty("hibernate.connection.username")
            // Create the Flyway instance and point it to the database
            val flyway: Flyway = Flyway.configure().dataSource(url, username, password).load()
            // Start the migration
            try {
                flyway.migrate()
            } catch (e: FlywayException) {
                log!!.error(e)
                // not the nice way, ok.
                flyway.repair()
                flyway.migrate()
            }
        }
    }
}
