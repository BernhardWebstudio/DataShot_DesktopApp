package edu.harvard.mcz.imagecapture.data


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.lifecycle.UsersLifeCycle
import edu.harvard.mcz.imagecapture.ui.dialog.LoginDialog
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.exception.JDBCConnectionException
import org.hibernate.service.spi.ServiceException
import java.awt.Cursor
import java.awt.Dialog.ModalityType
import java.util.*

/**
 * Singleton class to obtain access to Hibernate sessions, used in the *LifeCycle classes.
 *
 *
 * Modified from the hibernate tutorial
 * http://www.hibernate.org/hib_docs/v3/reference/en-US/html/tutorial-firstapp.html
 * Changed singleton implementation to allow loading of credentials from config and dialog at runtime
 */
object HibernateUtil {
    private val log: Log = LogFactory.getLog(HibernateUtil::class.java)
    /**
     * Call this method to obtain a Hibernate Session.
     *
     *
     * Usage:
     * <pre>
     * Session session = HibernateUtil.sessionFactory.getCurrentSession();
     * session.beginTransaction();
    </pre> *
     *
     * @return the Hibernate SessionFactory.
     */
    var sessionFactory: SessionFactory? = null
        get() {
            if (this.sessionFactory == null) {
                this.createSessionFactory()
            }
            return this.sessionFactory
        }
    val properties: Properties = Properties()

    fun terminateSessionFactory() {
        try {
            this.sessionFactory!!.currentSession.cancelQuery()
            this.sessionFactory!!.currentSession.clear()
            this.sessionFactory!!.currentSession.close()
        } catch (e: Exception) {
        } finally {
            try {
                this.sessionFactory!!.close()
            } catch (e1: Exception) {
            } finally {
                this.sessionFactory = null
            }
        }
    }

    /**
     * Reset the session factory by terminating & starting a new one
     */
    fun restartSessionFactory() {
        this.terminateSessionFactory()
        val configuration: Configuration = Configuration().configure().setProperties(this.properties)
        this.sessionFactory = configuration.buildSessionFactory()
    }

    /**
     * Using the Hibernate configuration in Configuration from hibernate.cfg.xml
     * create a Hibernate sessionFactory.  Method is private as the the session factory
     * should be a singleton, invoke getSessionFactory() to create or access a session.
     *
     * @see this.getSessionFactory
     */
    private fun createSessionFactory() {
        try {
            if (this.sessionFactory != null) {
                this.terminateSessionFactory()
            }
        } catch (e: Exception) {
            this.log.error(e.message)
        }
        try { // Create the Configuration from hibernate.cfg.xml
            var configuration: Configuration = Configuration().configure()
            // Add authentication properties obtained from the user
            var success = false
            // retrieve the connection parameters from hibernate.cfg.xml and load into the LoginDialog
            var loginDialog: LoginDialog = this.getLoginDialog(configuration, null)
            while (!success && loginDialog.result != LoginDialog.Companion.RESULT_CANCEL) { // Check authentication (starting with the database user(schema)/password.
                if (loginDialog.result == LoginDialog.Companion.RESULT_LOGIN) {
                    if (Singleton.mainFrame != null) {
                        Singleton.mainFrame!!.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
                        Singleton.mainFrame!!.setStatusMessage("Connecting to database")
                    }
                    this.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_PASSWORD, loginDialog.getDBPassword())
                    this.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_USER, loginDialog.getDBUserName())
                    this.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_URL, loginDialog.getConnection())
                    this.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_DIALECT, loginDialog.getDialect())
                    this.properties.setProperty(ImageCaptureProperties.Companion.KEY_DB_DRIVER, loginDialog.getDriver())
                    configuration.setProperties(this.properties)
                    // Now create the SessionFactory from this configuration
                    this.log.debug(configuration.getProperty(ImageCaptureProperties.Companion.KEY_DB_URL))
                    try {
                        this.sessionFactory = configuration.buildSessionFactory()
                    } catch (ex: JDBCConnectionException) {
                        configuration = Configuration().configure()
                        success = false
                        this.sessionFactory = null
                        loginDialog = this.getLoginDialog(configuration, "Initial SessionFactory creation failed. Database not connectable: " + ex.message)
                        try {
                            Singleton.mainFrame.setStatusMessage("Database connection failed.")
                        } catch (e: NullPointerException) { // expected if we haven't instantiated a main frame.
                        }
                        println("Initial SessionFactory creation failed.$ex")
                    } catch (ex: ServiceException) {
                        configuration = Configuration().configure()
                        success = false
                        this.sessionFactory = null
                        loginDialog = this.getLoginDialog(configuration, "Initial SessionFactory creation failed. Database not connectable: " + ex.message)
                        try {
                            Singleton.mainFrame.setStatusMessage("Database connection failed.")
                        } catch (e: NullPointerException) {
                        }
                        println("Initial SessionFactory creation failed.$ex")
                    } catch (ex: Throwable) { // Make sure you log the exception, as it might be swallowed
                        println("Initial SessionFactory creation failed.$ex")
                        throw ex
                        //throw new ExceptionInInitializerError(ex);
                    }
                    try { // Check database authentication by beginning a transaction.
                        val session: Session? = this.sessionFactory!!.currentSession
                        session.beginTransaction()
                        session.close()
                        // If an exception hasn't been thrown, dbuser/dbpassword has
// successfully authenticated against the database.
// Now try authenticating the individual user by the email address/password that they provided.
                        val uls = UsersLifeCycle()
                        val foundUser: MutableList<Users> = uls.findByCredentials(loginDialog.username, loginDialog.userPasswordHash)
                        if (foundUser.size == 1) { // There should be one and only one user returned.
                            if (foundUser[0].username == loginDialog.username && foundUser[0].hash == loginDialog.userPasswordHash) { // and that user must have exactly the username/password hash provided in the dialog.
                                Singleton.setCurrentUser(foundUser[0])
                                success = true
                                try {
                                    Singleton.mainFrame.setState(MainFrame.Companion.STATE_RUNNING)
                                } catch (ex: NullPointerException) { // expected if we haven't instantiated a main frame.
                                }
                            }
                        }
                        if (!success) {
                            loginDialog = this.getLoginDialog(configuration, "Login failed: Incorrect Email and/or Password.")
                            success = false
                            if (loginDialog.username != null) {
                                this.log.debug("Login failed for " + loginDialog.username)
                            }
                            this.sessionFactory!!.close()
                            this.sessionFactory = null
                            configuration = Configuration().configure()
                            try {
                                Singleton.mainFrame.setStatusMessage("Login failed.")
                            } catch (ex: NullPointerException) { // expected if we haven't instantiated a main frame.
                            }
                        }
                    } catch (e: Throwable) {
                        this.log.error(e.message)
                        this.log.trace(e.message, e)
                        println("Initial SessionFactory creation failed." + e.message)
                        loginDialog = this.getLoginDialog(configuration, "Login failed: " + e.cause)
                        success = false
                        this.sessionFactory!!.close()
                        this.sessionFactory = null
                        configuration = Configuration().configure()
                        if (Singleton.mainFrame != null) {
                            Singleton.mainFrame.setStatusMessage("Login failed.")
                        }
                    }
                    if (Singleton.mainFrame != null) {
                        Singleton.mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
                    }
                }
            }
        } catch (ex: Throwable) { // Make sure you log the exception, as it might be swallowed
            ex.printStackTrace()
            println("Initial SessionFactory creation failed.$ex")
            println("Cause" + ex.cause!!.message)
            throw ExceptionInInitializerError(ex)
        }
    }

    /**
     * Get the login dialog, initialized with properties for the advanced, database (DB) configuration
     *
     * @param config the Hibernate configuration to use as a base
     * @param status the login status to display
     * @return
     */
    private fun getLoginDialog(config: Configuration, status: String?): LoginDialog {
        val loginDialog = LoginDialog()
        val settings: Properties = Singleton.getProperties().getProperties()
        val keys: Enumeration<Any?> = settings.keys()
        // Detect usage of placeholders, replace with settings if available
        loginDialog.setConnection(this.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_URL, "URL_PLACEHOLDER"))
        loginDialog.setDialect(this.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_DIALECT, "DIALECT_PLACEHOLDER"))
        loginDialog.setDriver(this.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_DRIVER, "DRIVER_CLASS_PLACEHOLDER"))
        // If the database username(schema) and password are present load them as well.
        loginDialog.setDBUserName(this.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_USER, "USER_PLACEHOLDER"))
        loginDialog.setDBPassword(this.getConfigOrSettingsValue(config, settings, ImageCaptureProperties.Companion.KEY_DB_PASSWORD, "PASSWORD_PLACEHOLDER"))
        // Display the LoginDialog as a modal dialog
        loginDialog.setModalityType(ModalityType.APPLICATION_MODAL)
        loginDialog.setVisible(true)
        if (status != null) {
            loginDialog.setStatus(status)
        }
        return loginDialog
    }

    /**
     * Get a value from settings by key if it has the value, else from config
     *
     * @param config   the config to be treated as a better default
     * @param settings the properties overwriting config, but only if config is still default ("placeholder")
     * @param key      the property key to get the config/setting by
     * @param value    the default we do not want, except we have nothing else
     * @return the value of the property
     */
    private fun getConfigOrSettingsValue(config: Configuration, settings: Properties, key: String?, value: String?): String? {
        val keys: Enumeration<Any?> = settings.keys()
        //        if (config.getProperty(key) == null || config.getProperty(key).equals(value)) {
        return if (settings.getProperty(key, value) != value) { //          log.debug("Found value = '" + value + "' for key " + key + ", getting " + settings.getProperty(key, value));
            settings.getProperty(key, value)
        } else { //          log.debug("Did not find value = '" + value + "' for key " + key + ", getting " + config.getProperty(key) + " vs. " + settings.getProperty(key, value));
            config.getProperty(key)
        }
    }

}
