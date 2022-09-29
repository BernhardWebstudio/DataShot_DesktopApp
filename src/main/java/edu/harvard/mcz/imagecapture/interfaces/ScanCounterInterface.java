package edu.harvard.mcz.imagecapture.interfaces;

import edu.harvard.mcz.imagecapture.jobs.RunnableJobError;

import java.util.List;

public interface ScanCounterInterface {
    void logBarcode(String barcode);

    void appendError(RunnableJobError anError);

    String getErrorReport();

    void incrementSpecimenDatabased();

    void incrementSpecimenExisting();

    void incrementTotal();

    void incrementFilesSeen();

    void incrementFilesFailed();

    void incrementFilesDatabased();

    void incrementFilesExisting();

    void incrementDirectories();

    void incrementDirectoriesFailed();

    int getSpecimens();

    int getTotal();

    /**
     * @return the files checked
     */
    int getFilesSeen();

    /**
     * @return the files for which new database records were created
     */
    int getFilesDatabased();

    /**
     * @return the files for which database records allready existed
     */
    int getFilesExisting();

    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    int getFilesFailed();


    /**
     * @return the directories
     */
    int getDirectories();


    /**
     * @return the directoriesFailed
     */
    int getDirectoriesFailed();

    /**
     * @return the specimensUpdated
     */
    int getSpecimensUpdated();

    int getSpecimensExisting();

    /**
     *
     */
    void incrementSpecimensUpdated();

    /**
     * @return the filesUpdated
     */
    int getFilesUpdated();

    /**
     *
     */
    void incrementFilesUpdated();

    /**
     * @return
     */
    List<RunnableJobError> getErrors();

    List<String> getBarcodes();

}
