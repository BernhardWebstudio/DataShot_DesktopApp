/**
 * AllowedVersionLifeCycle.java
 * <p>
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
package edu.harvard.mcz.imagecapture.lifecycle;

import edu.harvard.mcz.imagecapture.ImageCaptureApp;
import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.data.HibernateUtil;
import edu.harvard.mcz.imagecapture.entity.AllowedVersion;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class AllowedVersionLifeCycle {

	private static final Logger log = LoggerFactory.getLogger(AllowedVersionLifeCycle.class);

	/**
	 * Configure and get a Flyway instance pointing to the configured database.
	 *
	 * @return Flyway instance
	 */
	public static Flyway getFlyway() {
		Properties properties = HibernateUtil.getProperties();
		String url = properties.getProperty(ImageCaptureProperties.KEY_DB_URL);
		String username = properties.getProperty(ImageCaptureProperties.KEY_DB_USER);
		String password = properties.getProperty(ImageCaptureProperties.KEY_DB_PASSWORD);
		return Flyway.configure(AllowedVersionLifeCycle.class.getClassLoader()).dataSource(url, username, password)
				.baselineOnMigrate(true).baselineVersion("1.9.0").load();
	}

	/**
	 * Check to see if the database schema is compatible with the current
	 * ImageCaptureApp. First checks if Flyway has any pending migrations. If Flyway
	 * is not configured or fails, falls back to checking the legacy allowed_version
	 * table.
	 *
	 * @return true if the database schema is up-to-date and compatible, otherwise
	 *         false.
	 */
	public static boolean isCurrentAllowed() throws HibernateException {
		// 1. Primary check: Flyway pending migrations
		try {
			Flyway flyway = getFlyway();
			MigrationInfoService info = flyway.info();
			if (info != null) {
				MigrationInfo[] pending = info.pending();
				if (pending != null && pending.length == 0) {
					log.debug("Flyway reports 0 pending migrations; database is up-to-date.");
					return true;
				} else if (pending != null && pending.length > 0) {
					log.info("Flyway reports {} pending migration(s).", pending.length);
					return false;
				}
			}
		} catch (Exception e) {
			log.warn("Flyway info check failed, falling back to legacy AllowedVersion check: {}", e.getMessage());
		}

		// 2. Fallback check: legacy allowed_version table
		AllowedVersionLifeCycle als = new AllowedVersionLifeCycle();
		List<AllowedVersion> allowedVersions = als.findAll();
		if (allowedVersions != null) {
			for (AllowedVersion av : allowedVersions) {
				if (av.getVersion() != null) {
					String version = av.getVersion().split("[ \\-]")[0];
					String currentVersion = ImageCaptureApp.getAppVersion();
					if (currentVersion != null && version.length() <= currentVersion.length()
							&& currentVersion.startsWith(version)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Provide a human readable list of the allowed versions listed in the database.
	 *
	 * @return string listing allowed versions according to database.
	 */
	public static String listAllowedVersions() {
		StringBuilder allowed = new StringBuilder();
		try {
			Flyway flyway = getFlyway();
			MigrationInfo current = flyway.info().current();
			if (current != null && current.getVersion() != null) {
				allowed.append("Flyway schema v").append(current.getVersion());
			}
		} catch (Exception e) {
			log.debug("Could not get Flyway version: {}", e.getMessage());
		}

		try {
			AllowedVersionLifeCycle als = new AllowedVersionLifeCycle();
			List<AllowedVersion> allowedVersions = als.findAll();
			if (allowedVersions != null && !allowedVersions.isEmpty()) {
				if (allowed.length() > 0) {
					allowed.append("; Legacy allowed_version: ");
				}
				String separator = "";
				for (AllowedVersion av : allowedVersions) {
					allowed.append(separator).append(av.getVersion());
					separator = ", ";
				}
			}
		} catch (Exception e) {
			log.error("Error listing allowed versions: {}", e.getMessage());
		}
		return allowed.toString();
	}

	/**
	 * Execute a migration, powered by FlyWay (https://flywaydb.org/)
	 */
	public static void upgrade() {
		Flyway flyway = getFlyway();
		// Start the migration
		try {
			MigrationInfo current = flyway.info().current();
			String currentVersionStr = (current != null && current.getVersion() != null)
					? current.getVersion().toString()
					: "none";

			MigrationInfo[] pending = flyway.info().pending();
			String pendingStr = (pending != null && pending.length > 0)
					? Arrays.stream(pending).map(m -> m.getVersion() != null ? m.getVersion().toString() : "unknown")
							.collect(Collectors.joining(", "))
					: "none";

			MigrationInfo[] all = flyway.info().all();
			String allStr = (all != null && all.length > 0)
					? Arrays.stream(all).map(m -> m.getVersion() != null ? m.getVersion().toString() : "unknown")
							.collect(Collectors.joining(", "))
					: "none";

			log.info("Starting migration. Current version: {}. Pending: {}. All migrations: {}", currentVersionStr,
					pendingStr, allStr);

			flyway.migrate();
		} catch (FlywayException e) {
			log.error("Flyway migration failed, attempting repair", e);
			flyway.repair();
			flyway.migrate();
		}

		// Also record that we migrated into allowed_version for backward compatibility
		try {
			String currentVersion = ImageCaptureApp.getAppVersion();
			AllowedVersion allowedVersion = new AllowedVersion();
			allowedVersion.setVersion(currentVersion);
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction transaction = session.beginTransaction();
			session.save(allowedVersion);
			transaction.commit();
		} catch (Exception e) {
			log.warn("Could not record current version in allowed_version table: {}", e.getMessage());
		}
	}

	/**
	 * Provide a list of all the allowed versions listed in the database.
	 *
	 * @return List[AllowedVersion] listing all versions according to database
	 */
	public List<AllowedVersion> findAll() {
		log.debug("finding all AllowedVersions");
		try {
			List<AllowedVersion> results = null;
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();

			session.beginTransaction();
			try {
				results = (List<AllowedVersion>) session.createQuery("SELECT a FROM AllowedVersion a").list();
				session.getTransaction().commit();
				log.debug("find all successful, result size: " + results.size());
			} catch (HibernateException e) {
				session.getTransaction().rollback();
				log.error(e.getMessage());
				throw e;
			}
			try {
				session.close();
			} catch (SessionException e) {
			}
			return results;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
