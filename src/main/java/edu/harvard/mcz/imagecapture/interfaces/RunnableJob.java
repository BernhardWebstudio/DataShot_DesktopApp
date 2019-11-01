/**
 * RunnableJob.java
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
package edu.harvard.mcz.imagecapture.interfaces;

import java.util.Date;


/**
 * Interface for batch scans of files and other operations for which starting the operation,
 * stopping, the operation, and finding out the status and completeness of the operation may
 * be desirable.  All implementations should have some non trivial start() method, but may or 
 * may not have functional code behind the other methods.  
 *
 * TODO: Break this into multiple interfaces (StartableJob, StoppableJob, etc.) 
 *
 *
 *
 */
public interface RunnableJob {

    /**
     * Start the RunnableJob.
     *
     */
    void start();

    /**
     * If possible, pause the job.  May be implemented to always return false and take no action.
     *
     * @return true if job has been stopped, false otherwise.
     */
    boolean stop();

    /**
     * If possible, permanently stop the job.  May be implemented to always return false and take no action.
     *
     * @return true if job has been canceled, false otherwise.
     */
    boolean cancel();

    /**
     * Report on the status of the job.
     *
     * @return integer from the RunStatus.STATUS_ constants indicating current status of the job.
     */
    int getStatus();

    /**
     * Report the percentage of the job that has been completed.
     *
     * @return integer between 0 and 100 representing the percent of the job that is complete.
     */
    int percentComplete();

    boolean registerListener(RunnerListener aJobListener);

    /**
     * Get a brief description of what sort of job this is in 
     * a form understandable by a user.
     *
     * @return a string representing to the user what sort of job this is.
     */
    String getName();

    /** Get the timestamp for when this job was started.
     *
     * @return a date representing the time this job started.
     */
    Date getStartTime();

}
