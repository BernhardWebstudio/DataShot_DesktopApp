/**
 * ImageCaptureApp.java
 * edu.harvard.mcz.imagecapture
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
 *
 *
 *
 * File last changed on $Date: 2012-01-06 18:51:55 -0500 (Fri, 06 Jan 2012) $ by $Author: mole $ in $Rev$.
 * $Id: ImageCaptureApp.java 305 2012-01-06 23:51:55Z mole $
 */
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.entity.Specimen
import edu.harvard.mcz.imagecapture.exceptions.ConnectionException
import edu.harvard.mcz.imagecapture.lifecycle.AllowedVersionLifeCycle
import edu.harvard.mcz.imagecapture.lifecycle.SpecimenLifeCycle
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModel
import org.apache.commons.logging.LogFactory
import org.hibernate.HibernateException
import java.awt.Cursor
import java.io.IOException
import java.util.*
import javax.swing.JOptionPane
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException

/**
 * for experimental chat support
 * import javax.jms.ConnectionFactory;
 * import javax.jms.JMSException;
 * import javax.jms.Message;
 * import javax.jms.MessageConsumer;
 * import javax.jms.Session;
 * import javax.jms.TextMessage;
 * import javax.jms.Topic;
 * import javax.jms.TopicConnection;
 * import javax.jms.TopicSession;
 * import javax.jms.TopicSubscriber;
 * import javax.naming.Context;
 * import javax.naming.InitialContext;
 * import javax.naming.NamingException;
 */
/**
 * Main entry point for user interface of ImageCapture Java Application. Creates
 * a SingletonObject, loads the properties file, and opens a MainFrame
 *
 * @see MainFrame
 *
 * @see edu.harvard.mcz.imagecapture.Singleton
 *
 * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties
 */
object ImageCaptureApp {
    val APP_NAME: String? = "DataShot"
    val APP_DESCRIPTION: String? = "Rapid capture of data from images of pin Labels and pinned insect \nspecimens developed for the MCZ Lepidoptera collection"
    val APP_COPYRIGHT: String? = "Copyright © 2009-2017 President and Fellows of Harvard College"
    val APP_LICENSE: String? = ("This program is free software; you can redistribute it and/or modify \n "
            + "it under the terms of Version 2 of the GNU General Public License \n"
            + "as published by the Free Software Foundation" + " \n "
            + "This program is distributed in the hope that it will be useful,\n "
            + "but WITHOUT ANY WARRANTY; without even the implied warranty of\n "
            + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n "
            + "GNU General Public License for more details.\n " + "\n "
            + "You should have received a copy of the GNU General Public License along\n "
            + "with this program; if not, write to the Free Software Foundation, Inc.,\n "
            + "51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n ")
    val APP_CONTRIBUTORS: String? = "Brendan Haley, Linda Ford, Rodney Eastwood, Paul J. Morris, Tim Bernhard."
    val APP_LIBRARIES: String? = "Hibernate, Tesseract, ZXing, Log4J, drew.metadata.exif, iText, event_date_qc"
    val APP_REV: String? = "\$Rev$" // ImageCapture.jar file built before commit will be one revision
    /**
     * Default regular expression for recognizing drawer numbers and unit tray
     * numbers, used to set default value of property
     * ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER, use that property instead of
     * this hard coded constant.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_REGEX_DRAWERNUMBER
     */
    val REGEX_DRAWERNUMBER: String? = "[0-9]{3}\\Q.\\E[0-9]+"
    /**
     * Default regular expression for recognizing image filenames in pattern decided
     * on for project. Used to set the default value of property
     * ImageCaptureProperties.KEY_IMAGEREGEX, use that property instead of this hard
     * coded constant.
     *
     * @see edu.harvard.mcz.imagecapture.ImageCaptureProperties.KEY_IMAGEREGEX
     */
// public static final String REGEX_IMAGEFILE = "^IMG_[0-9]{6}\\.JPG$";
    val REGEX_IMAGEFILE: String? = "^ETHZ_ENT[0-9]{2}_[0-9]{4}_[0-9]{2}_[0-9]{2}_[0-9]{6}\\.JPG$"
    /**
     * Match blank, or year or year/month or year/month/day.
     */
    val REGEX_DATE: String? = "^([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?(\\-([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?)??$"
    /**
     * Use MCZEntBarcode class instead.
     *
     */
// public static final String REGEX_BARCODE = "^MCZ-ENT[0-9]{8}$";
    /**
     * Code for a normal exit, pass to ImageCaptureApp.exit(EXIT_NORMAL).
     */
    const val EXIT_NORMAL = 0
    /**
     * Error code for an exit after a fatal error. Pass to
     * ImageCaptureApp.exit(EXIT_ERROR);
     */
    const val EXIT_ERROR = 1
    // ^([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)?\-?([12][0-9]{3}((/[01][0-9]){1}(/[0-3][0-9])?)?)??$
    private val log = LogFactory.getLog(ImageCaptureApp::class.java)
    // behind latest commit with changes to this file.
    var lastEditedSpecimenCache: Specimen? = null
    private var APP_VERSION: String? = null
    /**
     * Main method for starting the application.
     *
     * @param args are not used.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            UIManager.setLookAndFeel( // Use cross platform if native uses space on forms much too inefficiently
// UIManager.CrossPlatformLookAndFeelClassName);
                    UIManager.SystemLookAndFeelClassName)
        } catch (e: UnsupportedLookAndFeelException) {
            log!!.error(e)
        } catch (e: ClassNotFoundException) {
            log!!.error(e)
        } catch (e: InstantiationException) {
            log!!.error(e)
        } catch (e: IllegalAccessException) {
            log!!.error(e)
        }
        log!!.debug(UIManager.LookAndFeel.ID)
        println("Starting $APP_NAME $appVersion")
        println(APP_COPYRIGHT)
        println(APP_LICENSE)
        log.debug("Starting $APP_NAME $appVersion")
        // open UI and start
        val mainFrame = MainFrame()
        mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        mainFrame.setStatusMessage("Starting....")
        Singleton.setMainFrame(mainFrame)
        Singleton.unsetCurrentUser()
        log.debug("User interface started")
        // Set up a barcode (text read from barcode label for pin) matcher/builder
        if (Singleton.Properties.Properties
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_MCZENT) { // ** Configured for the MCZ Entomology Collection, use MCZ assumptions.
            val barcodeTextBuilderMatcher = MCZENTBarcode()
            Singleton.setBarcodeBuilder(barcodeTextBuilderMatcher)
            Singleton.setBarcodeMatcher(barcodeTextBuilderMatcher)
        } else if (Singleton.Properties.Properties
                        .getProperty(ImageCaptureProperties.Companion.KEY_COLLECTION) == ImageCaptureProperties.Companion.COLLECTION_ETHZENT) { // ** Configured for the ETHZ Entomology Collection, use MCZ assumptions.
            val barcodeTextBuilderMatcher = ETHZBarcode()
            Singleton.setBarcodeBuilder(barcodeTextBuilderMatcher)
            Singleton.setBarcodeMatcher(barcodeTextBuilderMatcher)
        } else {
            log.error("Configured collection not recognized. Unable to Start")
            exit(EXIT_ERROR)
        }
        // Force a login dialog by connecting to obtain record count;
        val sls = SpecimenLifeCycle()
        try {
            Singleton.MainFrame.setCount(sls.findSpecimenCountThrows())
            doStartUp()
        } catch (e: ConnectionException) {
            log.error(e.message)
            doStartUpNot()
        }
        // Experimental chat support, working on localhost.
        /**
         *
         * Context context = null; Hashtable contextProperties = new Hashtable(2);
         *
         * contextProperties.put(Context.PROVIDER_URL,"iiop://127.0.0.1:3700");
         * contextProperties.put("java.naming.factory.initial",
         * "com.sun.enterprise.naming.SerialInitContextFactory");
         * contextProperties.put("java.naming.factory.url.pkgs",
         * "com.sun.enterprise.naming");
         * contextProperties.put("java.naming.factory.state",
         * "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl"); try { context
         * = new InitialContext(contextProperties); } catch (NamingException ex) {
         * ex.printStackTrace(); } if (context!=null) { ConnectionFactory
         * connectionFactory; try { connectionFactory =
         * (ConnectionFactory)context.lookup("jms/InsectChatTopicFactory"); Topic
         * chatTopic = (Topic)context.lookup("jms/InsectChatTopic"); TopicConnection
         * connection = (TopicConnection) connectionFactory.createConnection();
         * TopicSession session =
         * connection.createTopicSession(false,Session.AUTO_ACKNOWLEDGE);
         * TopicSubscriber subscriber = session.createSubscriber(chatTopic);
         * connection.start(); while (true) { Message m = subscriber.receive(1); if (m
         * != null) { if (m instanceof TextMessage) { TextMessage message =
         * (TextMessage) m; String originator = message.getStringProperty("Originator");
         * String text = message.Text; System.out.println("Message: " + originator
         * + ": " + text); } else { break; } } } } catch (NamingException e) { // TODO
         * Auto-generated catch block e.printStackTrace(); } catch (JMSException e) { //
         * TODO Auto-generated catch block e.printStackTrace(); } } }
         */
    }

    // fetch version
    val appVersion: String?
        get() {
            if (APP_VERSION == null) { // fetch version
                APP_VERSION = try {
                    val privateProperties = Properties()
                    privateProperties.load(ImageCaptureApp::class.java.classLoader.getResourceAsStream("imagecapture.private.properties"))
                    privateProperties.getProperty("project.version")
                } catch (e: IOException) {
                    "failed-loading-version"
                }
            }
            return APP_VERSION
        }

    /**
     * Carry out actions to set user interface into nobody logged in yet state.
     */
    fun doStartUpNot() {
        Singleton.MainFrame.setStatusMessage("Select File/Change User to login.")
        Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        Singleton.MainFrame.setState(MainFrame.Companion.STATE_RESET)
    }

    /**
     * Carry out actions to set up system after a successful login.
     */
    fun doStartUp() {
        var isCurrentAllowed = false
        try {
            isCurrentAllowed = AllowedVersionLifeCycle.Companion.isCurrentAllowed()
        } catch (e: HibernateException) {
            log!!.error(e.message, e)
            val allowed: String = AllowedVersionLifeCycle.Companion.listAllowedVersions()
            if (allowed == null || allowed.trim { it <= ' ' }.length == 0) {
                Singleton.MainFrame
                        .setStatusMessage("Database does not support this version, schema update needed.")
                JOptionPane.showMessageDialog(Singleton.MainFrame,
                        "The database does not support" + APP_NAME + " version " + APP_VERSION
                                + ".  A database schema update to version 1.3 is required.",
                        "Schema Update Required", JOptionPane.OK_OPTION)
                log.error("Database does not test for versioning. Added in 1.3.0 Schema, and required by "
                        + APP_VERSION + "  Unable to Start")
                exit(EXIT_ERROR)
            }
        }
        if (isCurrentAllowed) {
            Singleton.MainFrame.setStatusMessage("$APP_VERSION OK")
        } else {
            val allowed: String = AllowedVersionLifeCycle.Companion.listAllowedVersions()
            Singleton.MainFrame
                    .setStatusMessage("Database does not support version, update needed.")
            val response: Int = JOptionPane.showConfirmDialog(Singleton.MainFrame,
                    "The database does not support" + APP_NAME + " version " + APP_VERSION
                            + ".  A software (or database) update from " + allowed + " is required. "
                            + "Are you ready to try the upgrade of the database? Make sure no one else will need the old version.",
                    "Update Required", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
            log!!.error("Database does not allow version " + APP_VERSION + ". Upgrade will "
                    + (if (response == JOptionPane.YES_OPTION) "" else "not happen") + "happen.")
            log.error("Database recognises version(s): $allowed")
            if (response == JOptionPane.YES_OPTION) { // try to upgrade the database scheme
                try {
                    AllowedVersionLifeCycle.Companion.upgrade()
                    Singleton.MainFrame.setStatusMessage("DB Upgrade OK")
                } catch (e: Exception) {
                    log.error("Upgrade failed", e)
                    JOptionPane.showMessageDialog(Singleton.MainFrame,
                            "Upgrade failed with error: " + e.message, "DB Upgrade failed.",
                            JOptionPane.ERROR_MESSAGE)
                }
            }
            // ImageCaptureApp.exit(EXIT_ERROR);
        }
        // Setup to store a list of running RunnableJobs.
        Singleton.setJobList(RunnableJobTableModel())
        log!!.debug("Set runnable job table.")
        Singleton.MainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
        Singleton.MainFrame.setState(MainFrame.Companion.STATE_RUNNING)
    }

    /**
     * Initiate actions to be taken on shutting down the application.
     */
    fun cleanUp() {
        try {
            Singleton.Properties.saveProperties()
        } catch (e: Exception) {
            println("Properties file save failed.  " + e.localizedMessage)
        }
    }

    /**
     * Shut down the application. Calls cleanUp() on normal exit. Constants
     * EXIT_NORMAL and EXIT_ERROR are available to be passed as the parameter
     * status.
     *
     * @param status 0 for normal exit, positive integer for error condition.
     */
    fun exit(status: Int) {
        if (status == EXIT_NORMAL) {
            cleanUp()
            log!!.debug("Exiting Application.")
        } else {
            log!!.error("Exiting Application.")
        }
        System.exit(status)
    }
}
