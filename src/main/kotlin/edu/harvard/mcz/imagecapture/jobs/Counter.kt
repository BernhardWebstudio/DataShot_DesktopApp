/**
 * Counter.java
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
package edu.harvard.mcz.imagecapture.jobs


import edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterface
import java.util.*

/**
 * Counter
 */
class Counter : ScanCounterInterface {
    var total = 0
        private set
    /**
     * @return the files checked
     */
    var filesSeen = 0
        private set
    /**
     * @return the files for which new database records were created
     */
    var filesDatabased = 0
        private set
    /**
     * @return the files for which database records allready existed
     */
    var filesExisting = 0
        private set
    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    var filesFailed = 0
        private set
    /**
     * @return the directories
     */
    var directories = 0
        private set
    /**
     * @return the directoriesFailed
     */
    var directoriesFailed = 0
        private set
    /**
     * @return
     */
    var specimensExisting = 0
        private set
    /**
     * @return the specimensUpdated
     */
    var specimensUpdated = 0
        private set
    var specimens = 0
        private set
    /**
     * @return the filesUpdated
     */
    var filesUpdated = 0
        private set
    private val errorReport: StringBuffer?
    private var errors: MutableList<RunnableJobError?>? = null
    override fun appendError(anError: RunnableJobError) {
        errors!!.add(anError)
        errorReport!!.append(anError.toString())
        errorReport.append("\n")
    }

    override fun getErrorReport(): String {
        return errorReport.toString()
    }

    override fun incrementSpecimenDatabased() {
        specimens++
    }

    override fun incrementSpecimenExisting() {
        specimensExisting++
    }

    override fun incrementTotal() {
        total++
    }

    override fun incrementFilesSeen() {
        filesSeen++
    }

    /**
     * TODO: automatically increment upon addition of an error, filter by filename if necessary for the number of failed files
     */
    override fun incrementFilesFailed() {
        filesFailed++
    }

    override fun incrementFilesDatabased() {
        filesDatabased++
    }

    override fun incrementFilesExisting() {
        filesExisting++
    }

    override fun incrementDirectories() {
        directories++
    }

    override fun incrementDirectoriesFailed() {
        directoriesFailed++
    }

    /**
     *
     */
    override fun incrementSpecimensUpdated() {
        specimensUpdated++
    }

    /**
     *
     */
    override fun incrementFilesUpdated() {
        filesUpdated++
    }

    override fun getErrors(): MutableList<RunnableJobError?>? {
        return errors
    }

    override fun toString(): String {
        var report = "Scanned " + directories + " directories.\n"
        report += "Scanned  " + filesSeen + " files.\n"
        report += "Created  " + filesDatabased + " new image records.\n"
        if (filesUpdated > 0) {
            report += "Updated  " + filesUpdated + " image records.\n"
        }
        report += "Created  " + specimens + " new specimen records.\n"
        if (specimensUpdated > 0) {
            report += "Updated  " + specimensUpdated + " specimen records.\n"
        }
        report += "Found " + filesFailed + " files with problems.\n"
        return report
    }

    /**
     * Merge this counter with another one, adding each others data
     *
     * @param counter the counter to merge with
     */
    fun mergeIn(counter: ScanCounterInterface): Counter {
        total += counter.getTotal()
        filesSeen += counter.getFilesSeen()
        filesDatabased += counter.getFilesDatabased()
        filesExisting += counter.getFilesExisting()
        filesFailed += counter.getFilesFailed()
        directories += counter.getDirectories()
        directoriesFailed += counter.getDirectoriesFailed()
        specimensExisting += counter.getSpecimensExisting()
        specimensUpdated += counter.getSpecimensUpdated()
        specimens += counter.getSpecimens()
        filesUpdated += counter.getFilesUpdated()
        errorReport.append(counter.getErrorReport())
        errors.addAll(counter.getErrors())
        return this
    }

    init {
        total = 0
        filesSeen = 0
        filesDatabased = 0
        filesExisting = 0
        filesFailed = 0
        specimens = 0
        directories = 0
        directoriesFailed = 0
        specimensUpdated = 0
        filesUpdated = 0
        errorReport = StringBuffer()
        errors = ArrayList<RunnableJobError?>()
    }
}
