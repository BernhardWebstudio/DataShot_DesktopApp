/**
 * RunStatus.java
 * edu.harvard.mcz.imagecapture.interfaces
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
package edu.harvard.mcz.imagecapture.data


/**
 * Describes the status of a RunnableJob
 */
object RunStatus {
    // use values less than STATUS_FAILED for jobs that aren't complete.
    const val STATUS_NEW = 0
    const val STATUS_RUNNING = 1
    const val STATUS_CANCEL_REQUESTED = 50
    // use values greater than or equal to STATUS_FAILED for completed jobs.
    const val STATUS_FAILED = 100
    const val STATUS_TERMINATED = 102
    const val STATUS_DONE = 101
    /**
     * @param aStatus status code to test, from one of the RunStatus.STATUS_* constants.
     * @return false if aStatus is that of a still running job, true if aStatus is that
     * of a job that has completed either successfully or with an error state.
     */
    fun testComplete(aStatus: Int): Boolean {
        var returnValue = false
        if (aStatus >= STATUS_FAILED) {
            returnValue = true
        }
        return returnValue
    }
}
