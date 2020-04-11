/**
 * ThumbnailBuilder.java
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


import edu.harvard.mcz.imagecapture.ThumbnailBuilder
import org.apache.commons.logging.LogFactory
import org.imgscalr.Scalr
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JOptionPane

/**
 *
 */
class ThumbnailBuilder(var startPoint: File?) : Runnable {
    var thumbnailCount = 0

    override fun run() {
        var existsCounter = 0
        // mkdir thumbs ; mogrify -path thumbs -resize 80x120 *.JPG
        if (startPoint!!.isDirectory && startPoint!!.name != "thumbs") {
            val thumbsDir = File(startPoint!!.path + File.separator + "thumbs")
            log!!.debug(thumbsDir.path)
            if (!thumbsDir.exists()) {
                thumbsDir.mkdir()
                thumbsDir.setWritable(true)
                log.debug("Creating " + thumbsDir.path)
            }
            val potentialFilesToThumb = startPoint!!.listFiles()
            val filesToThumb: MutableList<File?> = ArrayList()
            var filesToThumbCount = 0
            for (i in potentialFilesToThumb!!.indices) {
                if (potentialFilesToThumb[i]!!.name.endsWith(".JPG")) {
                    filesToThumb.add(potentialFilesToThumb[i])
                    filesToThumbCount++
                }
            }
            if (filesToThumbCount > 0) {
                val targetWidth = 100
                val targetHeight = 150
                val i = filesToThumb.iterator()
                while (i.hasNext()) {
                    val file = i.next()
                    val output = File(thumbsDir.path
                            + File.separator + file!!.name)
                    if (!output.exists()) { // don't overwrite existing thumnails
                        try {
                            val img: BufferedImage = ImageIO.read(file)
                            val thumbnail: BufferedImage = Scalr.resize(
                                    img, Scalr.Method.BALANCED, Scalr.Mode.FIT_TO_WIDTH,
                                    targetWidth, targetHeight, Scalr.OP_ANTIALIAS)
                            // img.createGraphics().drawImage(ImageIO.read(file).getScaledInstance(targetWidth,
// targetHeigh, Image.SCALE_SMOOTH),0,0,null);
                            ImageIO.write(thumbnail, "jpg", output)
                            thumbnailCount++
                        } catch (e1: IOException) {
                            log.error(e1.message, e1)
                            JOptionPane.showMessageDialog(null, e1.message + "\n",
                                    "Error", JOptionPane.ERROR_MESSAGE)
                        }
                    } else {
                        existsCounter++
                    }
                }
            } else {
                val message = "No *.JPG files found in " + startPoint!!.path
                log.debug(message)
            }
        }
        var exists = ""
        if (existsCounter > 0) {
            exists = "\nSkipped $existsCounter existing thumbnails."
        }
        JOptionPane.showMessageDialog(null,
                "Done building " + thumbnailCount +
                        " thumbnails in ./thumbs/" + exists,
                "Thumbnails Built.",
                JOptionPane.INFORMATION_MESSAGE)
    }

    companion object {
        private val log = LogFactory.getLog(ThumbnailBuilder::class.java)
        fun getThumbFileForFile(file: File): File {
            val thumbsDir = File(file.parentFile.path + File.separator + "thumbs")
            return File(thumbsDir.path + File.separator + file.name)
        }
    }

}
