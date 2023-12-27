/**
 * Singleton.java
 * edu.harvard.mcz.imagecapture
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
package edu.harvard.mcz.imagecapture;

import edu.harvard.mcz.imagecapture.entity.Users;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder;
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher;
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame;
import edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread safe singleton object for imagecapture application.
 * <p>
 * Usage:
 * <pre>
 * Singleton.getSingletonInstance.set{Method}(aRelevantObject) // store single
 * instance to singleton.
 * Singleton.getSingletonInstance.get{Method}().doStuffWithObject() // retrieve
 * single instance from singleton.
 * </pre>
 */
public class Singleton {

    private static final Logger log = LoggerFactory.getLogger(Singleton.class);

    // Eagerly create for thread safety.
    private static final Singleton singletonInstance = new Singleton();

    private ImageCaptureProperties properties = null;
    private MainFrame mainFrame;
    private Users user = null;
    private BarcodeMatcher barcodeMatcher = null;
    private BarcodeBuilder barcodeBuilder = null;
    private RunnableJobTableModel jobList = new RunnableJobTableModel();
    private int characterWidth = 10;

    /**
     * Private constructor to prevent the creation
     * of multiple instances of Singleton objects.
     */
    private Singleton() {
    }

    /**
     * Use this method to access the Singleton.
     *
     * @return the sole Singleton instance.
     */
    public static Singleton getSingletonInstance() {
        return singletonInstance;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Store a single instance of a MainFrame to be referenced from
     * elsewhere in the program.
     *
     * @param aMainFrame sole instance of MainFrame to be referenced
     */
    public void setMainFrame(MainFrame aMainFrame) {
        mainFrame = aMainFrame;
    }

    public ImageCaptureProperties getProperties() {
        if (properties == null) {
            // load a default properties if we haven't been given one yet.
            log.debug("Recreating properties on request...");
            properties = new ImageCaptureProperties();
        }
        return properties;
    }

    public void setProperties(ImageCaptureProperties anImageCaptureProperties) {
        properties = anImageCaptureProperties;
    }

    /**
     * Store the currently authenticated user
     * and look up and store the fullname that goes with this username.
     * A call on this method will make appropriate values available for
     * both getCurrentUsername() and getUserFullName().
     *
     * @param user
     */
    public void setCurrentUser(Users user) {
        this.user = user;
        this.getMainFrame().setStatusMessage("Connected as " +
                this.user.getFullname());
    }

    /**
     * @return the db username of what should be the currently
     * authenticated user.
     */
    public String getCurrentUsername() {
        return this.user.getUsername();
    }

    /**
     * Note that there is no setUserFullName() method, the singleton
     * value of userFullName is set automatically through a call to
     * setCurrentUsername().
     *
     * @return the userFullName
     */
    public String getUserFullName() {
        if (this.user == null) {
            return "Anonymous";
        }
        return this.user.getFullname();
    }

    /**
     * Note that there is no setUser() method.  The value is set automatically
     * through a call to setCurrentUsername();
     *
     * @return the current user
     */
    public Users getUser() {
        return user;
    }

    public void unsetCurrentUser() {
        user = null;
    }

    /**
     * @return the barcodeMatchedr
     */
    public BarcodeMatcher getBarcodeMatcher() {
        return barcodeMatcher;
    }

    /**
     * @param barcodeMatcher the barcodeMatchedr to set
     */
    public void setBarcodeMatcher(BarcodeMatcher barcodeMatcher) {
        this.barcodeMatcher = barcodeMatcher;
    }

    /**
     * @return the barcodeBuilder
     */
    public BarcodeBuilder getBarcodeBuilder() {
        return barcodeBuilder;
    }

    /**
     * @param barcodeBuilder the barcodeBuilder to set
     */
    public void setBarcodeBuilder(BarcodeBuilder barcodeBuilder) {
        this.barcodeBuilder = barcodeBuilder;
    }

    public RunnableJobTableModel getJobList() {
        return jobList;
    }

    public void setJobList(RunnableJobTableModel jobList) {
        this.jobList = jobList;
    }

    public int getCharacterWidth() {
        return characterWidth;
    }

    public void setCharacterWidth(int characterWidth) {
        if (characterWidth > 8) {
            this.characterWidth = characterWidth;
        } else {
            this.characterWidth = 8;
        }
    }
}
