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
import java.util.concurrent.atomic.AtomicInteger

/**
 * Counter
 */
class AtomicCounter : ScanCounterInterface {
    private var totalCount: AtomicInteger = AtomicInteger(0)
    private var filesSeen: AtomicInteger = AtomicInteger(0)
    private var filesDatabased: AtomicInteger = AtomicInteger(0)
    private var filesExisting: AtomicInteger = AtomicInteger(0)
    private var filesFailed: AtomicInteger = AtomicInteger(0)
    private var directories: AtomicInteger = AtomicInteger(0)
    private var directoriesFailed: AtomicInteger = AtomicInteger(0)
    private var specimensExisting: AtomicInteger = AtomicInteger(0)
    private var specimensUpdated: AtomicInteger = AtomicInteger(0)
    private var specimensDatabased: AtomicInteger = AtomicInteger(0)
    private var filesUpdated: AtomicInteger = AtomicInteger(0)
    private val errorReport: StringBuffer?
    private var errors: MutableList<RunnableJobError?>? = null
    @Synchronized
    override fun appendError(anError: RunnableJobError) {
        errors!!.add(anError)
        errorReport!!.append(anError.toString())
        errorReport.append("\n")
    }

    override fun getErrorReport(): String {
        return errorReport.toString()
    }

    override fun incrementSpecimenDatabased() {
        specimensDatabased.incrementAndGet()
    }

    override fun incrementSpecimenExisting() {
        specimensExisting.incrementAndGet()
    }

    override fun incrementTotal() {
        totalCount.incrementAndGet()
    }

    override fun incrementFilesSeen() {
        filesSeen.incrementAndGet()
    }

    /**
     * TODO: automatically increment upon addition of an error, filter by filename if necessary for the number of failed files
     */
    override fun incrementFilesFailed() {
        filesFailed.incrementAndGet()
    }

    override fun incrementFilesDatabased() {
        filesDatabased.incrementAndGet()
    }

    override fun incrementFilesExisting() {
        filesExisting.incrementAndGet()
    }

    override fun incrementDirectories() {
        directories.incrementAndGet()
    }

    override fun incrementDirectoriesFailed() {
        directoriesFailed.incrementAndGet()
    }

    val specimens: Int
        get() = specimensDatabased.get()

    val total: Int
        get() = totalCount.get()

    /**
     * @return the files checked
     */
    override fun getFilesSeen(): Int {
        return filesSeen.get()
    }

    /**
     * @return the files for which new database records were created
     */
    override fun getFilesDatabased(): Int {
        return filesDatabased.get()
    }

    /**
     * @return the files for which database records allready existed
     */
    override fun getFilesExisting(): Int {
        return filesExisting.get()
    }

    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    override fun getFilesFailed(): Int {
        return filesFailed.get()
    }

    /**
     * @return the directories
     */
    override fun getDirectories(): Int {
        return directories.get()
    }

    /**
     * @return the directoriesFailed
     */
    override fun getDirectoriesFailed(): Int {
        return directoriesFailed.get()
    }

    /**
     * @return the specimensUpdated
     */
    override fun getSpecimensUpdated(): Int {
        return specimensUpdated.get()
    }

    /**
     * @return
     */
    override fun getSpecimensExisting(): Int {
        return specimensExisting.get()
    }

    /**
     *
     */
    override fun incrementSpecimensUpdated() {
        specimensUpdated.incrementAndGet()
    }

    /**
     * @return the filesUpdated
     */
    override fun getFilesUpdated(): Int {
        return filesUpdated.get()
    }

    /**
     *
     */
    override fun incrementFilesUpdated() {
        filesUpdated.incrementAndGet()
    }

    override fun getErrors(): MutableList<RunnableJobError?>? {
        return errors
    }

    override fun toString(): String {
        var report = "Scanned " + getDirectories() + " directories.\n"
        report += "Scanned  " + getFilesSeen() + " files.\n"
        report += "Created  " + getFilesDatabased() + " new image records.\n"
        if (getFilesUpdated() > 0) {
            report += "Updated  " + getFilesUpdated() + " image records.\n"
        }
        report += "Created  " + specimens + " new specimen records.\n"
        if (getSpecimensUpdated() > 0) {
            report += "Updated  " + getSpecimensUpdated() + " specimen records.\n"
        }
        report += "Found " + getFilesFailed() + " files with problems.\n"
        return report
    }

    init {
        totalCount = AtomicInteger(0)
        filesSeen = AtomicInteger(0)
        filesDatabased = AtomicInteger(0)
        filesExisting = AtomicInteger(0)
        filesFailed = AtomicInteger(0)
        specimensDatabased = AtomicInteger(0)
        specimensExisting = AtomicInteger(0)
        directories = AtomicInteger(0)
        directoriesFailed = AtomicInteger(0)
        specimensUpdated = AtomicInteger(0)
        filesUpdated = AtomicInteger(0)
        errorReport = StringBuffer()
        errors = ArrayList<RunnableJobError?>()
    }
}
