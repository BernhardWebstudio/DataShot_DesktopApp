/**
 * Singleton.java
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
 */
package edu.harvard.mcz.imagecapture


import edu.harvard.mcz.imagecapture.entity.Users
import edu.harvard.mcz.imagecapture.interfaces.BarcodeBuilder
import edu.harvard.mcz.imagecapture.interfaces.BarcodeMatcher
import edu.harvard.mcz.imagecapture.ui.frame.MainFrame
import edu.harvard.mcz.imagecapture.ui.tablemodel.RunnableJobTableModel

/**
 * Thread safe singleton object for imagecapture application.
 *
 *
 * Usage:
 * <pre>
 * Singleton.set{Method}(aRelevantObject) // store single instance to singleton.
 * Singleton.get{Method}().doStuffWithObject() // retrieve single instance from singleton.
 * </pre>
 */
object Singleton {
    // load a default properties if we haven't been given one yet.
    var properties: ImageCaptureProperties = ImageCaptureProperties()

    /**
     * Store a single instance of a MainFrame to be referenced from
     * elsewhere in the program.
     *
     * @param aMainFrame sole instance of MainFrame to be referenced
     */
    var mainFrame: MainFrame = MainFrame()

    private var user: Users? = null

    /**
     * @return the barcodeMatchedr
     */
    /**
     * @param barcodeMatcher the barcodeMatchedr to set
     */
    var barcodeMatcher: BarcodeMatcher? = null
    /**
     * @return the barcodeBuilder
     */
    /**
     * @param barcodeBuilder the barcodeBuilder to set
     */
    var barcodeBuilder: BarcodeBuilder? = null
    var jobList: RunnableJobTableModel? = RunnableJobTableModel()
    var characterWidth = 10
        set(characterWidth) {
            field = if (characterWidth > 8) {
                characterWidth
            } else {
                8
            }
        }

    /**
     * Store the currently authenticated user
     * and look up and store the fullname that goes with this username.
     * A call on this method will make appropriate values available for
     * both getCurrentUsername() and getUserFullName().
     *
     * @param user
     */
    fun setCurrentUser(user: Users) {
        this.user = user
        mainFrame?.setStatusMessage("Connected as " + user.fullname)
    }

    /**
     * @return the db username of what should be the currently
     * authenticated user.
     */
    val currentUsername: String?
        get() = user?.username

    /**
     * Note that there is no setUserFullName() method, the singleton
     * value of userFullName is set automatically through a call to
     * setCurrentUsername().
     *
     * @return the userFullName
     */
    val userFullName: String?
        get() = user?.fullname

    /**
     * Note that there is no setUser() method.  The value is set automatically
     * through a call to setCurrentUsername();
     *
     * @return the current user
     */
    fun getUser(): Users? {
        return user
    }

    fun unsetCurrentUser() {
        user = null
    }
}
