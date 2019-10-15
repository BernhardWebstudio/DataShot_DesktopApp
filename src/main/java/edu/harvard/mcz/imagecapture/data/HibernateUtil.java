package edu.harvard.mcz.imagecapture.data;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import edu.harvard.mcz.imagecapture.LoginDialog;

import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.net.ConnectException;
import java.util.List;
import java.util.Properties;

import edu.harvard.mcz.imagecapture.MainFrame;
import edu.harvard.mcz.imagecapture.Singleton;
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.spi.ServiceException;

/**
 * Singleton class to obtain access to Hibernate sessions, used in the *LifeCycle classes.
 *  
 * Modified from the hibernate tutorial
 * http://www.hibernate.org/hib_docs/v3/reference/en-US/html/tutorial-firstapp.html
 * Changed singleton implementation to allow loading of credentials from config and dialog at runtime
 */
public class HibernateUtil {

	private static SessionFactory sessionFactory = null;
	
	private static final Log log = LogFactory.getLog(HibernateUtil.class);
	
	public static void terminateSessionFactory() {
		try { 
			sessionFactory.getCurrentSession().cancelQuery();
			sessionFactory.getCurrentSession().clear();
			sessionFactory.getCurrentSession().close();
		} catch (Exception e) { 
		} finally { 
			try { 
				sessionFactory.close();
			} catch  (Exception e1) { 
			} finally { 
				sessionFactory = null;
			} 
		}
	}
	

	/**
	 *  Using the Hibernate configuration in Configuration from hibernate.cfg.xml
	 *  create a Hibernate sessionFactory.  Method is private as the the session factory 
	 *  should be a singleton, invoke getSessionFactory() to create or access a session.
	 *  
	 *  @see edu.harvard.mcz.imagecapture.data.HibernateUtil#getSessionFactory
	 */
	private static void createSessionFactory() {
		try {
			if (sessionFactory!=null) { 
			   terminateSessionFactory();
			}
		} catch (Exception e) { 
			log.error(e.getMessage());
		}
		try {
			// Create the Configuration from hibernate.cfg.xml
			Configuration config = new Configuration().configure();
			// Add authentication properties obtained from the user
			boolean success = false;
			// retrieve the connection parameters from hibernate.cfg.xml and load into the LoginDialog
			LoginDialog loginDialog = HibernateUtil.getLoginDialog(config, null);
			while (!success && loginDialog.getResult()!=LoginDialog.RESULT_CANCEL) {
				// Check authentication (starting with the database user(schema)/password.
				String username;
				if (loginDialog.getResult()==LoginDialog.RESULT_LOGIN) { 
					if (Singleton.getSingletonInstance().getMainFrame() != null) {
			           Singleton.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					   Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Connecting to database");
					} 
					config.setProperty("hibernate.connection.password", loginDialog.getDBPassword());
					username = loginDialog.getDBUserName();
					config.setProperty("hibernate.connection.username", username);
					config.setProperty("hibernate.connection.url", loginDialog.getConnection());
					// Now create the SessionFactory from this configuration
					log.debug(config.getProperty("hibernate.connection.url"));
					try {
						sessionFactory = config.buildSessionFactory();
					} catch (JDBCConnectionException| ServiceException ex) {
						loginDialog = HibernateUtil.getLoginDialog(config, "Initial SessionFactory creation failed. Database not connectable: " + ex.getMessage());
						success = false;
						sessionFactory = null;
						try {
							Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Database connection failed.");
						} catch (NullPointerException e) {
							// expected if we haven't instantiated a main frame.
						}
						System.out.println("Initial SessionFactory creation failed." + ex);
					} catch (Throwable ex) {
						// Make sure you log the exception, as it might be swallowed
						System.out.println("Initial SessionFactory creation failed." + ex);
						throw ex;
						//throw new ExceptionInInitializerError(ex);
					}
					try { 
						// Check database authentication by beginning a transaction.
						Session session = sessionFactory.getCurrentSession();
						session.beginTransaction();
						session.close();
						// If an exception hasn't been thrown, dbuser/dbpassword has 
						// successfully authenticated against the database.
						// Now try authenticating the individual user by the email address/password that they provided.
						UsersLifeCycle uls = new UsersLifeCycle();
						List<Users> foundUser = uls.findByCredentials(loginDialog.getUsername(), loginDialog.getUserPasswordHash());
						if (foundUser.size()==1) {
 						    // There should be one and only one user returned.
						    if (foundUser.get(0).getUsername().equals(loginDialog.getUsername()) && foundUser.get(0).getHash().equals(loginDialog.getUserPasswordHash())) {
							   // and that user must have exactly the username/password hash provided in the dialog. 
						       Singleton.getSingletonInstance().setCurrentUser(foundUser.get(0));
						       success = true;
						       try {
						           Singleton.getSingletonInstance().getMainFrame().setState(MainFrame.STATE_RUNNING);
						       } catch (NullPointerException ex) { 
								   // expected if we haven't instantiated a main frame.
						       }
						   } 
						} 
						if (!success) { 
							loginDialog = HibernateUtil.getLoginDialog(config, "Login failed: Incorrect Email and/or Password.");
							success = false;
							if (loginDialog.getUsername()!=null) { 
						        log.debug("Login failed for " + loginDialog.getUsername());
							}
							sessionFactory.close();
							sessionFactory = null;
							try { 
							    Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Login failed.");
							} catch (NullPointerException ex) { 
								// expected if we haven't instantiated a main frame.
							}
						}
					} catch (Throwable e) {
						log.error(e.getMessage());
						log.trace(e.getMessage(),e);
						System.out.println("Initial SessionFactory creation failed." + e.getMessage());
						loginDialog = HibernateUtil.getLoginDialog(config, "Login failed: " + e.getCause());
						success = false;
						sessionFactory.close();
						sessionFactory = null;
						if (Singleton.getSingletonInstance().getMainFrame() != null) {
						    Singleton.getSingletonInstance().getMainFrame().setStatusMessage("Login failed.");
						} 
					}
					if (Singleton.getSingletonInstance().getMainFrame() != null) { 
					   Singleton.getSingletonInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			}
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			ex.printStackTrace();
			System.out.println("Initial SessionFactory creation failed." + ex);
			System.out.println("Cause" + ex.getCause().getMessage());
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Get the login dialog, initialized with properties for the advanced, database (DB) configuration
	 *
	 * @param config the Hibernate configuration to use as a base
	 * @param status the login status to display
	 * @return
	 */
	private static LoginDialog getLoginDialog(Configuration config, String status) {
		LoginDialog loginDialog = new LoginDialog();
		Properties settings = Singleton.getSingletonInstance().getProperties().getProperties();
		// detect usage of placeholders, replace with settings if available
		loginDialog.setConnection(HibernateUtil.getConfigOrSettingsValue(config, settings, "hibernate.connection.url", "${hibernate.url}"));
		loginDialog.setDialect(HibernateUtil.getConfigOrSettingsValue(config, settings, "hibernate.dialect", "${hibernate.dialect}"));
		loginDialog.setDriver(HibernateUtil.getConfigOrSettingsValue(config, settings, "hibernate.connection.driver_class", "${hibernate.driver_class}"));
		// If the database username(schema) and password are present load them as well.
		loginDialog.setDBUserName(HibernateUtil.getConfigOrSettingsValue(config, settings, "hibernate.connection.username", "${hibernate.user}"));
		loginDialog.setDBPassword(HibernateUtil.getConfigOrSettingsValue(config, settings, "hibernate.connection.password", "${hibernate.password}"));
		// Display the LoginDialog as a modal dialog 
		loginDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		loginDialog.setVisible(true);
		if (status != null) {
			loginDialog.setStatus(status);
		}
		return loginDialog;
	}

	/**
	 * Get a value from settings by key if it has the value in config, else from config
	 *
	 * @param config
	 * @param settings
	 * @param key
	 * @param value
	 * @return the value of the property
	 */
	private static String getConfigOrSettingsValue(Configuration config, Properties settings, String key, String value) {
		if (config.getProperty(key).equals(value)){
			return settings.getProperty(key, value);
		} else {
			return config.getProperty(key);
		}
	}
	
	
    /**
     * Call this method to obtain a Hibernate Session.
     *   
     * Usage:
     * <pre>
         Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	     session.beginTransaction();
	   </pre>
     * 
     * @return the Hibernate SessionFactory.
     */
	public static SessionFactory getSessionFactory() {
		if (sessionFactory==null) { 
			createSessionFactory();
		}
		return sessionFactory;
	}

}
	

