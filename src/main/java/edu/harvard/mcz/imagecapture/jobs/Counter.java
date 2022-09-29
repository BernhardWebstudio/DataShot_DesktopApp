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
import edu.harvard.mcz.imagecapture.ui.dialog.RunnableJobReportDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Counter
 */
public class Counter implements ScanCounterInterface {

    private static final Logger log =
            LoggerFactory.getLogger(Counter.class);

    private final StringBuffer errorReport;
    private int totalCount = 0;
    private int filesSeen = 0;
    private int filesDatabased = 0;
    private int filesExisting = 0;
    private int filesFailed = 0;
    private int directories = 0;
    private int directoriesFailed = 0;
    private int specimensExisting = 0;
    private int specimensUpdated = 0;
    private int specimensDatabased = 0;
    private int filesUpdated = 0;
    private List<RunnableJobError> errors = null;
    private ArrayList<String> barcodes = null;

    public Counter() {
        totalCount = 0;
        filesSeen = 0;
        filesDatabased = 0;
        filesExisting = 0;
        filesFailed = 0;
        specimensDatabased = 0;
        directories = 0;
        directoriesFailed = 0;
        specimensUpdated = 0;
        filesUpdated = 0;
        errorReport = new StringBuffer();
        errors = new ArrayList<RunnableJobError>();
        barcodes = new ArrayList<String>();
    }

    public void appendError(RunnableJobError anError) {
        errors.add(anError);
        errorReport.append(anError.toString());
        errorReport.append("\n");
    }

    public String getErrorReport() {
        return errorReport.toString();
    }

    public void incrementSpecimenDatabased() {
        specimensDatabased++;
    }

    public void incrementSpecimenExisting() {
        specimensExisting++;
    }

    public void incrementTotal() {
        totalCount++;
    }

    public void incrementFilesSeen() {
        filesSeen++;
    }

    public void logBarcode(String barcode) {
        barcodes.add(barcode);
    }

    /**
     * TODO: automatically increment upon addition of an error, filter by filename if necessary for the number of failed files
     */
    public void incrementFilesFailed() {
        filesFailed++;
    }

    public void incrementFilesDatabased() {
        filesDatabased++;
    }

    public void incrementFilesExisting() {
        filesExisting++;
    }

    public void incrementDirectories() {
        directories++;
    }

    public void incrementDirectoriesFailed() {
        directoriesFailed++;
    }

    public int getSpecimens() {
        return specimensDatabased;
    }

    public int getTotal() {
        return totalCount;
    }

    /**
     * @return the files checked
     */
    public int getFilesSeen() {
        return filesSeen;
    }

    /**
     * @return the files for which new database records were created
     */
    public int getFilesDatabased() {
        return filesDatabased;
    }

    /**
     * @return the files for which database records allready existed
     */
    public int getFilesExisting() {
        return filesExisting;
    }

    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    public int getFilesFailed() {
        return filesFailed;
    }

    /**
     * @return the directories
     */
    public int getDirectories() {
        return directories;
    }

    /**
     * @return the directoriesFailed
     */
    public int getDirectoriesFailed() {
        return directoriesFailed;
    }

    /**
     * @return the specimensUpdated
     */
    public int getSpecimensUpdated() {
        return specimensUpdated;
    }

    /**
     * @return
     */
    public int getSpecimensExisting() {
        return specimensExisting;
    }

    /**
     *
     */
    public void incrementSpecimensUpdated() {
        this.specimensUpdated++;
    }

    /**
     * @return the filesUpdated
     */
    public int getFilesUpdated() {
        return filesUpdated;
    }

    /**
     *
     */
    public void incrementFilesUpdated() {
        this.filesUpdated++;
    }

    public List<RunnableJobError> getErrors() {
        return errors;
    }

    @Override
    public List<String> getBarcodes() {
        return this.barcodes;
    }

    public String getBarcodeSequenceDescription() {
        if (barcodes.size() > 2) {
            ArrayList<String> barcodesMissing = new ArrayList<String>();
            ArrayList<String> barcodeSeqWhereMoreThanOneMisses = new ArrayList<String>();
            Collections.sort(barcodes);
            for (int i =0; i < barcodes.size()-1; i++) {
                Integer curr = Integer.valueOf(barcodes.get(i).replaceAll("[^0-9]", ""));
                Integer next = Integer.valueOf(barcodes.get(i+1).replaceAll("[^0-9]", ""));
                if (next - curr != 1 && next - curr != 0) {
                    log.warn("Barcode missing between " + barcodes.get(i) + " and " + barcodes.get(i+1) + "");
                    if (next - curr == 2) {
                        barcodesMissing.add(barcodes.get(i).replace(String.valueOf(curr), String.valueOf(curr+1)));
                    } else {
                        barcodeSeqWhereMoreThanOneMisses.add(barcodes.get(i));
                        barcodeSeqWhereMoreThanOneMisses.add(barcodes.get(i+1));
                    }
                }
            }
            if (barcodesMissing.size() == 0 && barcodeSeqWhereMoreThanOneMisses.size() == 0) {
                return "No missing barcodes detected between " + barcodes.get(0) + " and " + barcodes.get(barcodes.size()-1) + ".\n";
            }
            if (barcodesMissing.size() + barcodeSeqWhereMoreThanOneMisses.size() / 2 > 5) {
                return "Too many missing barcodes in sequence, " + barcodesMissing.size() + " single and " + (barcodeSeqWhereMoreThanOneMisses.size()/2) + " bigger distance.";
            } else {
                StringBuilder report = new StringBuilder("Missing barcode numbers: ");
                if (barcodeSeqWhereMoreThanOneMisses.size() > 0) {
                    for (int i = 0; i < barcodeSeqWhereMoreThanOneMisses.size()/2; ++i) {
                        report.append("between ").append(barcodeSeqWhereMoreThanOneMisses.get(2 * i)).append(" & ").append(barcodeSeqWhereMoreThanOneMisses.get(2 * i + 1));
                        if (i != barcodeSeqWhereMoreThanOneMisses.size() - 1) {
                            report.append(", ");
                        }
                    }
                }
                if (barcodesMissing.size() > 0) {
                    report.append(String.join(", ", barcodesMissing));
                }
                report.append(".\n");
                return report.toString();
            }
        } else {
            return "Not enough barcodes succeeded to analyse sequence.\n";
        }
    }

    @Override
    public String toString() {
        String report = "Scanned " + this.getDirectories() + " directories.\n";
        report += "Scanned  " + this.getFilesSeen() + " files.\n";
        report += "Created  " + this.getFilesDatabased() + " new image records.\n";
        if (this.getFilesUpdated() > 0) {
            report += "Updated  " + this.getFilesUpdated() + " image records.\n";
        }
        report += "Skipped " + this.getSpecimensExisting() + " already existing specimen.\n";
        report += "Created  " + this.getSpecimens() + " new specimen records.\n";
        if (this.getSpecimensUpdated() > 0) {
            report += "Updated  " + this.getSpecimensUpdated() + " specimen records.\n";
        }
        report += "Skipped " + this.getFilesExisting() + " already existing files.\n";
        report += "Found " + this.getFilesFailed() + " files with problems.\n";
        report += this.getBarcodeSequenceDescription();
        return report;
    }

    /**
     * Merge this counter with another one, adding each others data
     *
     * @param counter the counter to merge with
     */
    public Counter mergeIn(ScanCounterInterface counter) {
        this.totalCount += counter.getTotal();
        this.filesSeen += counter.getFilesSeen();
        this.filesDatabased += counter.getFilesDatabased();
        this.filesExisting += counter.getFilesExisting();
        this.filesFailed += counter.getFilesFailed();
        this.directories += counter.getDirectories();
        this.directoriesFailed += counter.getDirectoriesFailed();
        this.specimensExisting += counter.getSpecimensExisting();
        this.specimensUpdated += counter.getSpecimensUpdated();
        this.specimensDatabased += counter.getSpecimens();
        this.filesUpdated += counter.getFilesUpdated();
        this.errorReport.append(counter.getErrorReport());
        this.errors.addAll(counter.getErrors());
        this.barcodes.addAll(counter.getBarcodes());
        return this;
    }
}
