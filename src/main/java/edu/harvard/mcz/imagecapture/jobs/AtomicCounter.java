/**
 * Counter.java
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
package edu.harvard.mcz.imagecapture.jobs;

import edu.harvard.mcz.imagecapture.interfaces.ScanCounterInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Counter
 */
public class AtomicCounter implements ScanCounterInterface {

    private final StringBuffer errorReport;
    private AtomicInteger totalCount = new AtomicInteger(0);
    private AtomicInteger filesSeen = new AtomicInteger(0);
    private AtomicInteger filesDatabased = new AtomicInteger(0);
    private AtomicInteger filesExisting = new AtomicInteger(0);
    private AtomicInteger filesFailed = new AtomicInteger(0);
    private AtomicInteger directories = new AtomicInteger(0);
    private AtomicInteger directoriesFailed = new AtomicInteger(0);
    private AtomicInteger specimensExisting = new AtomicInteger(0);
    private AtomicInteger specimensUpdated = new AtomicInteger(0);
    private AtomicInteger specimensDatabased = new AtomicInteger(0);
    private AtomicInteger filesUpdated = new AtomicInteger(0);
    private List<RunnableJobError> errors = null;
    private ArrayList<String> barcodes = null;

    public AtomicCounter() {
        totalCount = new AtomicInteger(0);
        filesSeen = new AtomicInteger(0);
        filesDatabased = new AtomicInteger(0);
        filesExisting = new AtomicInteger(0);
        filesFailed = new AtomicInteger(0);
        specimensDatabased = new AtomicInteger(0);
        specimensExisting = new AtomicInteger(0);
        directories = new AtomicInteger(0);
        directoriesFailed = new AtomicInteger(0);
        specimensUpdated = new AtomicInteger(0);
        filesUpdated = new AtomicInteger(0);
        errorReport = new StringBuffer();
        errors = new ArrayList<RunnableJobError>();
        barcodes = new ArrayList<String>();
    }

    @Override
    public void logBarcode(String barcode) {
        barcodes.add(barcode);
    }

    public synchronized void appendError(RunnableJobError anError) {
        errors.add(anError);
        errorReport.append(anError.toString());
        errorReport.append("\n");
    }

    public String getErrorReport() {
        return errorReport.toString();
    }

    public void incrementSpecimenDatabased() {
        specimensDatabased.incrementAndGet();
    }

    public void incrementSpecimenExisting() {
        specimensExisting.incrementAndGet();
    }

    public void incrementTotal() {
        totalCount.incrementAndGet();
    }

    public void incrementFilesSeen() {
        filesSeen.incrementAndGet();
    }

    /**
     * TODO: automatically increment upon addition of an error, filter by filename if necessary for the number of failed files
     */
    public void incrementFilesFailed() {
        filesFailed.incrementAndGet();
    }

    public void incrementFilesDatabased() {
        filesDatabased.incrementAndGet();
    }

    public void incrementFilesExisting() {
        filesExisting.incrementAndGet();
    }

    public void incrementDirectories() {
        directories.incrementAndGet();
    }

    public void incrementDirectoriesFailed() {
        directoriesFailed.incrementAndGet();
    }

    public int getSpecimens() {
        return specimensDatabased.get();
    }

    public int getTotal() {
        return totalCount.get();
    }

    /**
     * @return the files checked
     */
    public int getFilesSeen() {
        return filesSeen.get();
    }

    /**
     * @return the files for which new database records were created
     */
    public int getFilesDatabased() {
        return filesDatabased.get();
    }

    /**
     * @return the files for which database records allready existed
     */
    public int getFilesExisting() {
        return filesExisting.get();
    }

    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    public int getFilesFailed() {
        return filesFailed.get();
    }


    /**
     * @return the directories
     */
    public int getDirectories() {
        return directories.get();
    }


    /**
     * @return the directoriesFailed
     */
    public int getDirectoriesFailed() {
        return directoriesFailed.get();
    }

    /**
     * @return the specimensUpdated
     */
    public int getSpecimensUpdated() {
        return specimensUpdated.get();
    }

    /**
     * @return
     */
    public int getSpecimensExisting() {
        return specimensExisting.get();
    }

    /**
     *
     */
    public void incrementSpecimensUpdated() {
        this.specimensUpdated.incrementAndGet();
    }

    /**
     * @return the filesUpdated
     */
    public int getFilesUpdated() {
        return filesUpdated.get();
    }

    /**
     *
     */
    public void incrementFilesUpdated() {
        this.filesUpdated.incrementAndGet();
    }

    public List<RunnableJobError> getErrors() {
        return errors;
    }

    @Override
    public List<String> getBarcodes() {
        return barcodes;
    }

    @Override
    public String toString() {
        String report = "Scanned " + this.getDirectories() + " directories.\n";
        report += "Scanned  " + this.getFilesSeen() + " files.\n";
        report += "Created  " + this.getFilesDatabased() + " new image records.\n";
        if (this.getFilesUpdated() > 0) {
            report += "Updated  " + this.getFilesUpdated() + " image records.\n";

        }
        report += "Created  " + this.getSpecimens() + " new specimen records.\n";
        if (this.getSpecimensUpdated() > 0) {
            report += "Updated  " + this.getSpecimensUpdated() + " specimen records.\n";

        }
        report += "Found " + this.getFilesFailed() + " files with problems.\n";
        return report;
    }
}
