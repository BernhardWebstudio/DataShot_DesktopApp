package edu.harvard.mcz.imagecapture.jobs


import edu.harvard.mcz.imagecapture.ImageCaptureProperties
import edu.harvard.mcz.imagecapture.Singleton
import edu.harvard.mcz.imagecapture.data.RunStatus
import edu.harvard.mcz.imagecapture.interfaces.RunnableJob
import edu.harvard.mcz.imagecapture.interfaces.RunnerListener
import edu.harvard.mcz.imagecapture.jobs.ThumbnailBuilderJob
import org.apache.commons.logging.LogFactory
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

internal class ThumbnailBuilderJob : Runnable, RunnableJob {
    var startPoint: File?
    var thumbnailCounter: AtomicInteger?
    var thumbHeight: String?
    var thumbWidth: String?
    /**
     * @see RunnableJob.getStartTime
     */
    var startTime: Date? = null public get() {
        return field
    }
        private set
    /**
     * @see RunnableJob.getStatus
     */
    var status: Int = RunStatus.STATUS_NEW
        private set
    private var thumbPercentComplete = 0
    private var thumbListeners: ArrayList<RunnerListener?>? = null

    constructor(aStartPoint: File?, counter: AtomicInteger?) {
        thumbnailCounter = counter
        startPoint = aStartPoint
        thumbHeight = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_THUMBNAIL_HEIGHT)
        thumbWidth = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_THUMBNAIL_WIDTH)
        thumbInit()
    }

    constructor(aStartPoint: File?, thumbHeightPixels: Int, thumbWidthPixels: Int, counter: AtomicInteger?) {
        thumbnailCounter = counter
        startPoint = aStartPoint
        thumbHeight = Integer.toString(thumbHeightPixels)
        thumbWidth = Integer.toString(thumbWidthPixels)
        if (thumbHeightPixels < 10) {
            thumbHeight = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_THUMBNAIL_HEIGHT)
        }
        if (thumbWidthPixels < 10) {
            thumbWidth = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_THUMBNAIL_WIDTH)
        }
        thumbInit()
    }

    protected fun thumbInit() {
        thumbListeners = ArrayList<RunnerListener?>()
    }

    override fun run() {
        startTime = Date()
        status = RunStatus.STATUS_RUNNING
        setThumbPercentComplete(0)
        Singleton.JobList.addJob(this)
        // mkdir thumbs ; mogrify -path thumbs -resize 80x120 *.JPG
        if (startPoint!!.isDirectory && startPoint!!.name != "thumbs") {
            val thumbsDir = File(startPoint!!.path + File.separator + "thumbs")
            log!!.debug(thumbsDir.path)
            if (!thumbsDir.exists()) {
                thumbsDir.mkdir()
                Singleton.MainFrame.setStatusMessage("Creating " + thumbsDir.path)
            }
            // Runtime executes mogrify directly, not through a shell, thus expand list of files to pass
// rather than passing *.JPG
            val potentialFilesToThumb = startPoint!!.listFiles()
            val filesToThumb = StringBuffer()
            var filesToThumbCount = 0
            for (i in potentialFilesToThumb!!.indices) {
                if (potentialFilesToThumb[i]!!.name.endsWith(".JPG")) {
                    filesToThumb.append(potentialFilesToThumb[i]!!.name).append(" ")
                    filesToThumbCount++
                }
            }
            if (filesToThumbCount > 0) {
                var makeWithJava = false
                val mogrify: String = Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_MOGRIFY_EXECUTABLE)
                if (mogrify == null || mogrify.trim { it <= ' ' }.length > 0) {
                    makeWithJava = true
                } else {
                    val runCommand = mogrify + " -path thumbs -resize " + thumbWidth + "x" + thumbHeight + " " + filesToThumb.toString()
                    val r = Runtime.Runtime
                    log.debug(runCommand)
                    try {
                        val env = arrayOf<String?>("")
                        val proc = r!!.exec(runCommand, env, startPoint)
                        val stderr = proc!!.errorStream
                        val isrstderr = InputStreamReader(stderr)
                        val br = BufferedReader(isrstderr)
                        var line: String? = null
                        while (br.readLine().also({ line = it }) != null) {
                            log.debug("stderr:$line")
                        }
                        val exitVal = proc.waitFor()
                        log.debug("Mogrify Process exitValue: $exitVal")
                        if (exitVal == 0) {
                            thumbnailCounter.incrementAndGet()
                            val message = "Finished creating thumbnails in: " + startPoint!!.path
                            Singleton.MainFrame.setStatusMessage(message)
                            log.debug(message)
                        } else {
                            log.error("Error returned running $runCommand")
                            makeWithJava = true
                        }
                    } catch (e: IOException) {
                        log.error("Error running: $runCommand")
                        e.printStackTrace()
                        Singleton.MainFrame.setStatusMessage("Error creating thumbnails " + e.message)
                        makeWithJava = true
                    } catch (e: InterruptedException) {
                        log.error("Mogrify process interupted")
                        e.printStackTrace()
                    }
                }
                if (makeWithJava) {
                    for (i in potentialFilesToThumb.indices) {
                        try {
                            log.debug("Attempting thumbnail generation with java in " + startPoint!!.path)
                            log.debug("Attempting thumbnail generation with java to " + thumbsDir.path)
                            val makeFrom = ArrayList<String?>()
                            val files = Arrays.asList(*startPoint!!.listFiles())
                            val it = files.iterator()
                            var creationCounter = 0
                            val totalFiles = files.size
                            while (it.hasNext() && status != RunStatus.STATUS_CANCEL_REQUESTED) {
                                val file = it.next()
                                if (!file!!.isDirectory && file.exists() && file.canRead()) { // file must exist and be readable to make thumbnail
                                    if (file.name.matches(Singleton.Properties.Properties.getProperty(ImageCaptureProperties.Companion.KEY_IMAGEREGEX))) { // only try to make thumbnails of files that match the image file pattern.
                                        makeFrom.add(file.path)
                                        log.debug(file.path)
                                        val target = File(thumbsDir.path + File.separatorChar + file.name)
                                        log.debug(target.path)
                                        if (!target.exists()) {
                                            val img = BufferedImage(thumbWidth!!.toInt(), thumbHeight!!.toInt(), BufferedImage.TYPE_INT_RGB)
                                            img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(80, 120, Image.SCALE_SMOOTH), 0, 0, null)
                                            ImageIO.write(img, "jpg", target)
                                            creationCounter++
                                        }
                                    }
                                    setThumbPercentComplete((creationCounter.toFloat() / totalFiles * 100).toInt())
                                }
                            }
                            if (creationCounter > 0) {
                                val message = "Finished creating thumbnails (" + creationCounter + ") in: " + startPoint!!.path
                                Singleton.MainFrame.setStatusMessage(message)
                            }
                        } catch (e: IOException) {
                            log.error("Thumbnail generation with thumbnailator library failed")
                            log.error(e.message)
                        }
                    }
                }
            } else {
                val message = "No *.JPG files found in " + startPoint!!.path
                Singleton.MainFrame.setStatusMessage(message)
                log.debug(message)
            }
        }
        val message = "Thumbnail Generation Complete."
        Singleton.MainFrame.setStatusMessage(message)
        Singleton.JobList.removeJob(this)
    }

    /**
     * @see RunnableJob.start
     */
    override fun start() {
        run()
    }

    /**
     * @see RunnableJob.stop
     */
    override fun stop(): Boolean { // TODO Auto-generated method stub
        return false
    }

    /**
     * @see RunnableJob.cancel
     */
    override fun cancel(): Boolean {
        status = RunStatus.STATUS_CANCEL_REQUESTED
        return false
    }

    /**
     * @see RunnableJob.percentComplete
     */
    override fun percentComplete(): Int {
        return thumbPercentComplete
    }

    protected fun setThumbPercentComplete(aPercentage: Int) { //set value
        thumbPercentComplete = aPercentage
        log!!.debug(thumbPercentComplete)
        //notify listeners
        val i: MutableIterator<RunnerListener?> = thumbListeners!!.iterator()
        while (i.hasNext()) {
            i.next().notifyListener(thumbPercentComplete, this)
        }
    }

    /**
     * @see RunnableJob.registerListener
     */
    override fun registerListener(aJobListener: RunnerListener?): Boolean {
        if (thumbListeners == null) {
            thumbInit()
        }
        return thumbListeners!!.add(aJobListener)
    }

    /**
     * @see RunnableJob.getName
     */
    val name: String
        get() = "Thumbnail Generation in: $startPoint"

    companion object {
        private val log = LogFactory.getLog(ThumbnailBuilderJob::class.java)
    }
} // end class ThumbnailBuilder
