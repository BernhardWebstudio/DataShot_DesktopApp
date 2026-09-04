package edu.harvard.mcz.imagecapture.data;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class to obtain access to Hibernate sessions, used in the
 * *LifeCycle classes.
 * <p>
 * Modified from the hibernate tutorial
 * http://www.hibernate.org/hib_docs/v3/reference/en-US/html/tutorial-firstapp.html
 * Changed singleton implementation to allow loading of credentials from config
 * and dialog at runtime
 */
public class HibernateUtil {

	private static final Logger log = LoggerFactory.getLogger(HibernateUtil.class);
	private static final Properties properties = new Properties();
	private static SessionFactory sessionFactory = null;

	public static void terminateSessionFactory() {
		try {
			sessionFactory.getCurrentSession().cancelQuery();
			sessionFactory.getCurrentSession().clear();
			sessionFactory.getCurrentSession().close();
		} catch (Exception e) {
		} finally {
			try {
				sessionFactory.close();
			} catch (Exception e1) {
			} finally {
				sessionFactory = null;
			}
		}
	}

	/**
	 * Reset the session factory by terminating & starting a new one
	 */
	public static void restartSessionFactory() {
		HibernateUtil.terminateSessionFactory();
		Configuration configuration = new Configuration().configure().setProperties(properties);
		sessionFactory = configuration.buildSessionFactory();
	}

	public static Properties getProperties() {
		return HibernateUtil.properties;
	}

	private static void createAnonSessionFactory() {
		try {
			if (sessionFactory != null) {
				terminateSessionFactory();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		try {
			Configuration configuration = new Configuration().configure();
			Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();

			properties.setProperty(ImageCaptureProperties.KEY_DB_URL, HibernateUtil.getConfigOrSettingsValue(
					configuration, settings, ImageCaptureProperties.KEY_DB_URL, "URL_PLACEHOLDER"));
			properties.setProperty(ImageCaptureProperties.KEY_DB_USER, HibernateUtil.getConfigOrSettingsValue(
					configuration, settings, ImageCaptureProperties.KEY_DB_USER, "USER_PLACEHOLDER"));
			properties
					.setProperty(ImageCaptureProperties.KEY_DB_PASSWORD,
							LoginDialog.decryptPassword(HibernateUtil
									.getConfigOrSettingsValue(configuration, settings,
											ImageCaptureProperties.KEY_DB_PASSWORD, "PASSWORD_PLACEHOLDER")
									.toCharArray()));
			properties.setProperty(ImageCaptureProperties.KEY_DB_DIALECT, HibernateUtil.getConfigOrSettingsValue(
					configuration, settings, ImageCaptureProperties.KEY_DB_DIALECT, "DIALECT_PLACEHOLDER"));
			properties.setProperty(ImageCaptureProperties.KEY_DB_DRIVER, HibernateUtil.getConfigOrSettingsValue(
					configuration, settings, ImageCaptureProperties.KEY_DB_DRIVER, "DRIVER_CLASS_PLACEHOLDER"));
			configuration.setProperties(properties);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Exception e) {
			log.error("Failed to create anon session factory", e);
			createSessionFactory();
		}
	}

	/**
	 * Using the Hibernate configuration in Configuration from hibernate.cfg.xml
	 * create a Hibernate sessionFactory. Method is private as the the session
	 * factory should be a singleton, invoke getSessionFactory() to create or access
	 * a session.
	 *
	 * @see edu.harvard.mcz.imagecapture.data.HibernateUtil#getSessionFactory
	 */
	private static void createSessionFactory() {
		createSessionFactory(false);
	}

	private static void createSessionFactory(boolean testEnv) {
		try {
			if (sessionFactory != null) {
				terminateSessionFactory();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		try {
			String testEnvEnv = System.getProperty("test.env");
			Configuration configuration;
			if ("true".equalsIgnoreCase(testEnvEnv) || testEnv) {
				// Use test config for in-memory DB
				configuration = new Configuration().configure("hibernate-test.cfg.xml");
				sessionFactory = configuration.buildSessionFactory();
				return;
			}
			// Normal (production) config and login dialog
			// Create the Configuration from hibernate.cfg.xml
			configuration = new Configuration().configure();
			boolean mainFrameAvailable = Singleton.getSingletonInstance().getMainFrame() != null;
			if (mainFrameAvailable) {
				Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Connecting to database");
			}

			LoginDialog loginDialog = new LoginDialog();
			Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
			// Detect usage of placeholders, replace with settings if available
			loginDialog.setConnection(HibernateUtil.getConfigOrSettingsValue(configuration, settings,
					ImageCaptureProperties.KEY_DB_URL, "URL_PLACEHOLDER"));
			loginDialog.setDialect(HibernateUtil.getConfigOrSettingsValue(configuration, settings,
					ImageCaptureProperties.KEY_DB_DIALECT, "DIALECT_PLACEHOLDER"));
			loginDialog.setDriver(HibernateUtil.getConfigOrSettingsValue(configuration, settings,
					ImageCaptureProperties.KEY_DB_DRIVER, "DRIVER_CLASS_PLACEHOLDER"));
			loginDialog.setDBUserName(HibernateUtil.getConfigOrSettingsValue(configuration, settings,
					ImageCaptureProperties.KEY_DB_USER, "USER_PLACEHOLDER"));
			loginDialog.setDBPassword(HibernateUtil.getConfigOrSettingsValue(configuration, settings,
					ImageCaptureProperties.KEY_DB_PASSWORD, "PASSWORD_PLACEHOLDER"));

			loginDialog.setAuthenticationCallback(dialog -> {
				String dbPass = dialog.getDBPassword();
				String dbUser = dialog.getDBUserName();
				String dbUrl = dialog.getConnection();
				String dbDialect = dialog.getDialect();
				String dbDriver = dialog.getDriver();

				boolean needNewFactory = sessionFactory == null || sessionFactory.isClosed()
						|| !Objects.equals(properties.getProperty(ImageCaptureProperties.KEY_DB_PASSWORD), dbPass)
						|| !Objects.equals(properties.getProperty(ImageCaptureProperties.KEY_DB_USER), dbUser)
						|| !Objects.equals(properties.getProperty(ImageCaptureProperties.KEY_DB_URL), dbUrl)
						|| !Objects.equals(properties.getProperty(ImageCaptureProperties.KEY_DB_DIALECT), dbDialect)
						|| !Objects.equals(properties.getProperty(ImageCaptureProperties.KEY_DB_DRIVER), dbDriver);

				if (needNewFactory) {
					if (sessionFactory != null) {
						terminateSessionFactory();
					}
					properties.setProperty(ImageCaptureProperties.KEY_DB_PASSWORD, dbPass);
					properties.setProperty(ImageCaptureProperties.KEY_DB_USER, dbUser);
					properties.setProperty(ImageCaptureProperties.KEY_DB_URL, dbUrl);
					properties.setProperty(ImageCaptureProperties.KEY_DB_DIALECT, dbDialect);
					properties.setProperty(ImageCaptureProperties.KEY_DB_DRIVER, dbDriver);
					configuration.setProperties(properties);
					try {
						sessionFactory = configuration.buildSessionFactory();
					} catch (JDBCConnectionException | ServiceException ex) {
						log.error("Failed to connect to database: " + ex.getMessage(), ex);
						terminateSessionFactory();
						String cause = (ex.getCause() != null && ex.getCause().getMessage() != null)
								? ex.getCause().getMessage()
								: ex.getMessage();
						throw new Exception("Database connection failed: " + cause);
					}
				}

				UsersLifeCycle uls = new UsersLifeCycle();
				List<Users> foundUser = uls.findByCredentials(dialog.getUsername(), dialog.getUserPasswordHash());
				if (foundUser != null && foundUser.size() == 1) {
					Users u = foundUser.get(0);
					if (dialog.getUsername().equals(u.getUsername())
							&& dialog.getUserPasswordHash().equals(u.getHash())) {
						Singleton.getSingletonInstance().setCurrentUser(u);
						return true;
					}
				}
				return false;
			});

			loginDialog.pack();
			loginDialog.setSize(new Dimension(650, loginDialog.getHeight()));
			loginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
			loginDialog.setVisible(true);

			if (loginDialog.getResult() != LoginDialog.RESULT_LOGIN) {
				log.info("Login canceled by user.");
				if (sessionFactory != null) {
					terminateSessionFactory();
				}
			}
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			log.error("SessionFactory creation failed", ex);
			System.out.println("SessionFactory creation failed." + ex);
			if (ex.getCause() != null) {
				System.out.println("Cause: " + ex.getCause().getMessage());
			}
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Get the login dialog, initialized with properties for the advanced, database
	 * (DB) configuration
	 *
	 * @param config
	 *            the Hibernate configuration to use as a base
	 * @param status
	 *            the login status to display
	 * @return
	 */
	private static LoginDialog getLoginDialog(Configuration config, String status) {
		LoginDialog loginDialog = new LoginDialog();
		Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
		// Detect usage of placeholders, replace with settings if available
		loginDialog.setConnection(HibernateUtil.getConfigOrSettingsValue(config, settings,
				ImageCaptureProperties.KEY_DB_URL, "URL_PLACEHOLDER"));
		loginDialog.setDialect(HibernateUtil.getConfigOrSettingsValue(config, settings,
				ImageCaptureProperties.KEY_DB_DIALECT, "DIALECT_PLACEHOLDER"));
		loginDialog.setDriver(HibernateUtil.getConfigOrSettingsValue(config, settings,
				ImageCaptureProperties.KEY_DB_DRIVER, "DRIVER_CLASS_PLACEHOLDER"));
		// If the database username(schema) and password are present load them as
		// well.
		loginDialog.setDBUserName(HibernateUtil.getConfigOrSettingsValue(config, settings,
				ImageCaptureProperties.KEY_DB_USER, "USER_PLACEHOLDER"));
		loginDialog.setDBPassword(HibernateUtil.getConfigOrSettingsValue(config, settings,
				ImageCaptureProperties.KEY_DB_PASSWORD, "PASSWORD_PLACEHOLDER"));
		if (status != null) {
			loginDialog.setStatus(status);
		}
		// Display the LoginDialog as a modal dialog
		loginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		loginDialog.setVisible(true);
		return loginDialog;
	}

	/**
	 * Get a value from settings by key if it has the value, else from config
	 *
	 * @param config
	 *            the config to be treated as a better default
	 * @param settings
	 *            the properties overwriting config, but only if config is still
	 *            default ("placeholder")
	 * @param key
	 *            the property key to get the config/setting by
	 * @param value
	 *            the default we do not want, except we have nothing else
	 * @return the value of the property
	 */
	private static String getConfigOrSettingsValue(Configuration config, Properties settings, String key,
			String value) {
		Enumeration<Object> keys = settings.keys();
		// if (config.getProperty(key) == null ||
		// config.getProperty(key).equals(value)) {
		if (!settings.getProperty(key, value).equals(value)) {
			// log.debug("Found value = '" + value + "' for key " + key + ",
			// getting " + settings.getProperty(key, value));
			return settings.getProperty(key, value);
		} else {
			// log.debug("Did not find value = '" + value + "' for key " +
			// key + ", getting " + config.getProperty(key) + " vs. " +
			// settings.getProperty(key, value));
			return config.getProperty(key);
		}
	}

	public static SessionFactory getSessionFactory() {
		return getSessionFactory(false, false);
	}

	public static SessionFactory getSessionFactory(boolean anon) {
		return getSessionFactory(anon, false);
	}

	public static SessionFactory getTestSessionFactory() {
		return getSessionFactory(false, true);
	}

	/**
	 * Call this method to obtain a Hibernate Session.
	 * <p>
	 * Usage:
	 *
	 * <pre>
	 * Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	 * session.beginTransaction();
	 * </pre>
	 *
	 * @return the Hibernate SessionFactory.
	 */
	public static SessionFactory getSessionFactory(boolean anon, boolean testEnv) {
		if (anon && testEnv) {
			throw new IllegalArgumentException("Cannot create an anonymous session factory in test environment.");
		}
		if (sessionFactory == null) {// || sessionFactory.isClosed()) {
			if (anon) {
				createAnonSessionFactory();
			} else {
				createSessionFactory(testEnv);
			}
		}
		return sessionFactory;
	}
}
