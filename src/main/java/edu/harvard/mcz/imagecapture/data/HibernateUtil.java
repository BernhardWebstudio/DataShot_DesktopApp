package edu.harvard.mcz.imagecapture.data;

import edu.harvard.mcz.imagecapture.ImageCaptureProperties;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle;
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog;
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Singleton class to obtain access to Hibernate sessions, used in the
 * *LifeCycle classes. <p> Modified from the hibernate tutorial
 * http://www.hibernate.org/hib_docs/v3/reference/en-US/html/tutorial-firstapp.html
 * Changed singleton implementation to allow loading of credentials from config
 * and dialog at runtime
 */
public class HibernateUtil {

    private static final Logger log =
            LoggerFactory.getLogger(HibernateUtil.class);
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
        Configuration configuration =
                new Configuration().configure().setProperties(properties);
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
            Properties settings =
                    Singleton.getSingletonInstance().getProperties().getProperties();

            properties.setProperty(ImageCaptureProperties.KEY_DB_URL, HibernateUtil.getConfigOrSettingsValue(
                    configuration, settings, ImageCaptureProperties.KEY_DB_URL,
                    "URL_PLACEHOLDER"));
            properties.setProperty(ImageCaptureProperties.KEY_DB_USER,
                    HibernateUtil.getConfigOrSettingsValue(
                            configuration, settings, ImageCaptureProperties.KEY_DB_USER,
                            "USER_PLACEHOLDER"));
            properties.setProperty(ImageCaptureProperties.KEY_DB_PASSWORD,
                    HibernateUtil.getConfigOrSettingsValue(
                            configuration, settings, ImageCaptureProperties.KEY_DB_PASSWORD,
                            "PASSWORD_PLACEHOLDER"));
            properties.setProperty(ImageCaptureProperties.KEY_DB_DIALECT,
                    HibernateUtil.getConfigOrSettingsValue(
                            configuration, settings, ImageCaptureProperties.KEY_DB_DIALECT,
                            "DIALECT_PLACEHOLDER"));
            properties.setProperty(ImageCaptureProperties.KEY_DB_DRIVER,
                    HibernateUtil.getConfigOrSettingsValue(
                            configuration, settings, ImageCaptureProperties.KEY_DB_DRIVER,
                            "DRIVER_CLASS_PLACEHOLDER"));
            configuration.setProperties(properties);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            log.error("Failed to create anon session factory", e);
            createSessionFactory();
        }
    }

    /**
     * Using the Hibernate configuration in Configuration from hibernate.cfg.xml
     * create a Hibernate sessionFactory.  Method is private as the the session
     * factory should be a singleton, invoke getSessionFactory() to create or
     * access a session.
     *
     * @see edu.harvard.mcz.imagecapture.data.HibernateUtil#getSessionFactory
     */
    private static void createSessionFactory() {
        try {
            if (sessionFactory != null) {
                terminateSessionFactory();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        try {
            // Create the Configuration from hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();
            // Add authentication properties obtained from the user
            boolean success = false;
            boolean mainFrameAvailable = Singleton.getSingletonInstance().getMainFrame() != null;
            // retrieve the connection parameters from hibernate.cfg.xml and load into
            // the LoginDialog
            LoginDialog loginDialog =
                    HibernateUtil.getLoginDialog(configuration, null);
            while (!success && loginDialog.getResult() != LoginDialog.RESULT_CANCEL) {
                // Check authentication (starting with the database
                // user(schema)/password.
                if (loginDialog.getResult() == LoginDialog.RESULT_LOGIN) {
                    if (mainFrameAvailable) {
                        Singleton.getSingletonInstance().getMainFrame().setCursor(
                                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                                "Connecting to database");
                    }
                    properties.setProperty(ImageCaptureProperties.KEY_DB_PASSWORD,
                            loginDialog.getDBPassword());
                    properties.setProperty(ImageCaptureProperties.KEY_DB_USER,
                            loginDialog.getDBUserName());
                    properties.setProperty(ImageCaptureProperties.KEY_DB_URL,
                            loginDialog.getConnection());
                    properties.setProperty(ImageCaptureProperties.KEY_DB_DIALECT,
                            loginDialog.getDialect());
                    properties.setProperty(ImageCaptureProperties.KEY_DB_DRIVER,
                            loginDialog.getDriver());
                    configuration.setProperties(properties);
                    // Now create the SessionFactory from this configuration
                    log.debug(
                            configuration.getProperty(ImageCaptureProperties.KEY_DB_URL));
                    try {
                        sessionFactory = configuration.buildSessionFactory();
                    } catch (JDBCConnectionException | ServiceException ex) {
                        configuration = new Configuration().configure();
                        success = false;
                        sessionFactory = null;
                        loginDialog = HibernateUtil.getLoginDialog(
                                configuration,
                                "Initial SessionFactory creation failed. Database not connectable: " +
                                        ex.getMessage());
                        if (mainFrameAvailable) {
                            Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                                    "Database connection failed.");
                        }
                        System.out.println("Initial SessionFactory creation failed." + ex);
                    } catch (Throwable ex) {
                        // Make sure you log the exception, as it might be swallowed
                        System.out.println("Initial SessionFactory creation failed." + ex);
                        throw ex;
                        // throw new ExceptionInInitializerError(ex);
                    }
                    try {
                        // Check database authentication by beginning a transaction.
                        Session session = sessionFactory.getCurrentSession();
                        session.beginTransaction();
                        session.close();
                        // If an exception hasn't been thrown, dbuser/dbpassword has
                        // successfully authenticated against the database.
                        // Now try authenticating the individual user by the email
                        // address/password that they provided.
                        UsersLifeCycle uls = new UsersLifeCycle();
                        List<Users> foundUser = uls.findByCredentials(
                                loginDialog.getUsername(), loginDialog.getUserPasswordHash());
                        if (foundUser.size() == 1) {
                            // There should be one and only one user returned.
                            if (foundUser.get(0).getUsername().equals(
                                    loginDialog.getUsername()) &&
                                    foundUser.get(0).getHash().equals(
                                            loginDialog.getUserPasswordHash())) {
                                // and that user must have exactly the username/password hash
                                // provided in the dialog.
                                Singleton.getSingletonInstance().setCurrentUser(
                                        foundUser.get(0));
                                success = true;
                                if (mainFrameAvailable) {
                                    Singleton.getSingletonInstance().getMainFrame().setState(
                                            MainFrame.STATE_RUNNING);
                                }
                            }
                        }
                        if (!success) {
                            loginDialog = HibernateUtil.getLoginDialog(
                                    configuration,
                                    "Login failed: Incorrect Email and/or Password.");
                            success = false;
                            if (loginDialog.getUsername() != null) {
                                log.debug("Login failed for " + loginDialog.getUsername());
                            }
                            sessionFactory.close();
                            sessionFactory = null;
                            configuration = new Configuration().configure();
                            try {
                                Singleton.getSingletonInstance()
                                        .getMainFrame()
                                        .setStatusMessage("Login failed.");
                            } catch (NullPointerException ex) {
                                // expected if we haven't instantiated a main frame.
                            }
                        }
                    } catch (Throwable e) {
                        log.error(e.getMessage(), e);
                        System.out.println("SessionFactory creation failed." +
                                e.getMessage());
                        loginDialog = HibernateUtil.getLoginDialog(
                                configuration, "Login failed: " + e.getCause());
                        success = false;
                        sessionFactory.close();
                        sessionFactory = null;
                        configuration = new Configuration().configure();
                        if (mainFrameAvailable) {
                            Singleton.getSingletonInstance().getMainFrame().setStatusMessage(
                                    "Login failed.");
                        }
                    }
                    if (mainFrameAvailable) {
                        Singleton.getSingletonInstance().getMainFrame().setCursor(
                                Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            ex.printStackTrace();
            log.error("SessionFactory creation failed", ex);
            System.out.println("SessionFactory creation failed." + ex);
            ex.printStackTrace();
            if (ex.getCause() != null) {
                System.out.println("Cause: " + ex.getCause().getMessage());
            }
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Get the login dialog, initialized with properties for the advanced,
     * database (DB) configuration
     *
     * @param config the Hibernate configuration to use as a base
     * @param status the login status to display
     * @return
     */
    private static LoginDialog getLoginDialog(Configuration config,
                                              String status) {
        LoginDialog loginDialog = new LoginDialog();
        Properties settings =
                Singleton.getSingletonInstance().getProperties().getProperties();
        Enumeration<Object> keys = settings.keys();
        // Detect usage of placeholders, replace with settings if available
        loginDialog.setConnection(HibernateUtil.getConfigOrSettingsValue(
                config, settings, ImageCaptureProperties.KEY_DB_URL,
                "URL_PLACEHOLDER"));
        loginDialog.setDialect(HibernateUtil.getConfigOrSettingsValue(
                config, settings, ImageCaptureProperties.KEY_DB_DIALECT,
                "DIALECT_PLACEHOLDER"));
        loginDialog.setDriver(HibernateUtil.getConfigOrSettingsValue(
                config, settings, ImageCaptureProperties.KEY_DB_DRIVER,
                "DRIVER_CLASS_PLACEHOLDER"));
        // If the database username(schema) and password are present load them as
        // well.
        loginDialog.setDBUserName(HibernateUtil.getConfigOrSettingsValue(
                config, settings, ImageCaptureProperties.KEY_DB_USER,
                "USER_PLACEHOLDER"));
        loginDialog.setDBPassword(HibernateUtil.getConfigOrSettingsValue(
                config, settings, ImageCaptureProperties.KEY_DB_PASSWORD,
                "PASSWORD_PLACEHOLDER"));
        // Display the LoginDialog as a modal dialog
        loginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        loginDialog.setVisible(true);
        if (status != null) {
            loginDialog.setStatus(status);
        }
        return loginDialog;
    }

    /**
     * Get a value from settings by key if it has the value, else from config
     *
     * @param config   the config to be treated as a better default
     * @param settings the properties overwriting config, but only if config is
     *                 still default ("placeholder")
     * @param key      the property key to get the config/setting by
     * @param value    the default we do not want, except we have nothing else
     * @return the value of the property
     */
    private static String getConfigOrSettingsValue(Configuration config,
                                                   Properties settings,
                                                   String key, String value) {
        Enumeration<Object> keys = settings.keys();
        //        if (config.getProperty(key) == null ||
        //        config.getProperty(key).equals(value)) {
        if (!settings.getProperty(key, value).equals(value)) {
            //          log.debug("Found value = '" + value + "' for key " + key + ",
            //          getting " + settings.getProperty(key, value));
            return settings.getProperty(key, value);
        } else {
            //          log.debug("Did not find value = '" + value + "' for key " +
            //          key + ", getting " + config.getProperty(key) + " vs. " +
            //          settings.getProperty(key, value));
            return config.getProperty(key);
        }
    }

    public static SessionFactory getSessionFactory() {
        return getSessionFactory(false);
    }

    /**
     * Call this method to obtain a Hibernate Session.
     * <p>
     * Usage:
     * <pre>
     * Session session = HibernateUtil.getSessionFactory().getCurrentSession();
     * session.beginTransaction();
     * </pre>
     *
     * @return the Hibernate SessionFactory.
     */
    public static SessionFactory getSessionFactory(boolean anon) {
        if (sessionFactory == null) {// || sessionFactory.isClosed()) {
            if (anon) {
                createAnonSessionFactory();
            } else {
                createSessionFactory();
            }
        }
        return sessionFactory;
    }
}
