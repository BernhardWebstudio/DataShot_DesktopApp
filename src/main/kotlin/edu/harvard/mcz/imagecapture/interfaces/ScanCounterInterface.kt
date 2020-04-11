package edu.harvard.mcz.imagecapture.interfaces


import edu.harvard.mcz.imagecapture.jobs.RunnableJobError

interface ScanCounterInterface {
    fun appendError(anError: RunnableJobError?)
    val errorReport: String
    fun incrementSpecimenDatabased()
    fun incrementSpecimenExisting()
    fun incrementTotal()
    fun incrementFilesSeen()
    fun incrementFilesFailed()
    fun incrementFilesDatabased()
    fun incrementFilesExisting()
    fun incrementDirectories()
    fun incrementDirectoriesFailed()
    val specimens: Int
    val total: Int
    /**
     * @return the files checked
     */
    val filesSeen: Int

    /**
     * @return the files for which new database records were created
     */
    val filesDatabased: Int

    /**
     * @return the files for which database records allready existed
     */
    val filesExisting: Int

    /**
     * @return the files for which a check for information needed to create a record Failed.
     */
    val filesFailed: Int

    /**
     * @return the directories
     */
    val directories: Int

    /**
     * @return the directoriesFailed
     */
    val directoriesFailed: Int

    /**
     * @return the specimensUpdated
     */
    val specimensUpdated: Int

    val specimensExisting: Int
    /**
     *
     */
    fun incrementSpecimensUpdated()

    /**
     * @return the filesUpdated
     */
    val filesUpdated: Int

    /**
     *
     */
    fun incrementFilesUpdated()

    /**
     * @return
     */
    val errors: MutableList<Any?>?
}
