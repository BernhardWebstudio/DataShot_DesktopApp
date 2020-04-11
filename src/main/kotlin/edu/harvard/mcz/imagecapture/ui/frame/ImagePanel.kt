/**
 * ImagePanel.java
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
package edu.harvard.mcz.imagecapture.ui.frame


import edu.harvard.mcz.imagecapture.utility.MathUtility
import java.awt.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JViewport

/**
 * ImagePanel displays a zoomable image in a JPanel.  Support class for ImageZoomPanel,
 * which includes both the controls and the image in an ImagePanel.
 *
 * @see ImageZoomPanel
 */
open class ImagePanel : JPanel {
    //image object
    protected var image: Image?
    // position of top left of underlying image in window,
// default is 0,0
    var ul_x = 0
    var ul_y = 0
    private var originalImage: Image?
    // Zoom level, 1 is pixel for pixel,
// less than 1 for a reduced size image,
// more than 1 for an enlarged image.
    var zoom = 1.0
        private set
    // proportion to zoom in or out in one step
    private val zoomFactor = 0.1 // 10% by default

    constructor(anImage: Image?) {
        image = anImage
        originalImage = anImage
    }

    constructor() {
        image = null
        originalImage = null
    }

    open fun clearOverlay() {
        image = originalImage
        paintComponent(this.Graphics)
    }

    override fun paintComponent(g: Graphics?) {
        val g2D: Graphics2D = g as Graphics2D?
        if (image == null) {
            g2D.setColor(Color.GRAY)
            g2D.fillRect(0, 0, getWidth(), getHeight())
        } else {
            g2D.setColor(Color.WHITE)
            g2D.fillRect(0, 0, getWidth(), getHeight())
        }
        if (MathUtility.equalTenPlaces(zoom, 1.0)) {
            g2D.Transform.setToIdentity()
        } else {
            g2D.scale(zoom, zoom)
        }
        g2D.drawImage(image, ul_x, ul_y, this)
    }

    val preferredSize: Dimension
        get() {
            var result = Dimension(100, 100)
            if (image != null) {
                result = Dimension(
                        (image.getWidth(this) * zoom) as Int,
                        (image.getHeight(this) * zoom) as Int)
            }
            return result
        }

    fun zoomIn() {
        zoom = zoom + zoomFactor
    }

    fun zoomOut() {
        zoom = zoom - zoomFactor
    }

    fun fullSize() {
        zoom = 1.0
        ul_x = 0
        ul_y = 0
    }

    /**
     * Zoom this image to fit entirely into a parent viewport.
     */
    fun zoomToFit() {
        val view: JViewport = this.Parent as JViewport?
        if (view != null) {
            zoomToFit(view.Width, view.Width)
        }
    }

    /**
     * Scale this image to fit entirely within a rectangle
     * defined by a width and height.
     *
     * @param x width this image is to fit into
     * @param y height this image is to fit into.
     */
    fun zoomToFit(x: Int, y: Int) {
        val windowHeight = y.toDouble()
        val windowWidth = x.toDouble()
        val imageHeight: Double = image.getHeight(this).toDouble()
        val imageWidth: Double = image.getWidth(this).toDouble()
        val zh = windowHeight / imageHeight
        val zw = windowWidth / imageWidth
        // pick smallest value (most zoom in).
        zoom = if (zh < zw) {
            zh
        } else {
            zw
        }
    }

    /**
     * Center this image in a parent JScrollPane.
     */
    fun center() {
        val jScrollPane: JScrollPane = this.Parent.Parent as JScrollPane
        if (jScrollPane != null) {
            if (this.Size.height < jScrollPane.Height
                    && this.Size.width < jScrollPane.Width) { // This image is smaller than the scroll pane.
// Move the viewport to the origin.
                jScrollPane.Viewport.setViewPosition(Point(0, 0))
                // Place the image in the center of the scroll pane
//TODO: place image in center.
            } else { // This image is larger than the scroll pane.
// Move the viewport to be centered on the center of the image.
// Get the dimensions of the the viewport
                val view_x: Int = jScrollPane.Viewport.Width
                val view_y: Int = jScrollPane.Viewport.Height
                // Find the offset of the upper left corner of a centered viewport
// of the same size on the image at its current zoom.
// Half the width of the image minus half the width of the viewport.
                val ul_x: Int = preferredSize.width / 2 - view_x / 2
                val ul_y: Int = preferredSize.height / 2 - view_y / 2
                // move the upper left corner of the viewport to this point
                jScrollPane.Viewport.setViewPosition(Point(ul_x, ul_y))
            }
        }
    }

    fun centerOn(aPoint: Point) {
        val jScrollPane: JScrollPane = this.Parent.Parent as JScrollPane
        if (jScrollPane != null) {
            if (this.Size.height < jScrollPane.Height
                    && this.Size.width < jScrollPane.Width) { // This image is smaller than the scroll pane.
// Move the viewport to the origin.
                jScrollPane.Viewport.setViewPosition(Point(0, 0))
                // Place the image in the center of the scroll pane
//TODO: place image in center.
            } else { // This image is larger than the scroll pane.
// Move the viewport to be centered on the point specified.
// Get the dimensions of the the viewport
                val view_x: Int = jScrollPane.Viewport.Width
                val view_y: Int = jScrollPane.Viewport.Height
                val location: Point = jScrollPane.Viewport.ViewPosition
                // determine the offset between the center of the viewport and aPoint
                val ul_x: Int = aPoint.x - view_x / 2
                val ul_y: Int = aPoint.y - view_y / 2
                // move the upper left corner of the viewport to this point
                jScrollPane.Viewport.setViewPosition(Point(ul_x, ul_y))
            }
        }
    }

    fun getImage(): Image? {
        return image
    }

    fun setImage(anImage: Image?) {
        image = anImage
        originalImage = anImage
        ul_x = 0
        ul_y = 0
        this.repaint()
    }// convert get values that came back as unknown yet (-1) to 0,0.

    /**
     * Convenience method to get the size of the current image.  Returns 0,0 if image is null or
     * if its size isn't known.
     *
     * @return size of image as a Dimension.
     */
    val imageSize: Dimension?
        get() {
            var result: Dimension? = null
            if (image == null) {
                result = Dimension(0, 0)
            } else {
                result = Dimension(image.getWidth(this), image.getHeight(this))
                // convert get values that came back as unknown yet (-1) to 0,0.
                if (result.height == -1 || result.width == -1) {
                    result = Dimension(0, 0)
                }
            }
            return result
        }

    companion object {
        private const val serialVersionUID = 8827456895472324699L
    }
}
