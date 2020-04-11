package edu.harvard.mcz.imagecapture.ui


import java.awt.Component
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import javax.swing.JScrollBar
import javax.swing.JScrollPane
import javax.swing.SwingUtilities

/**
 * Passes mouse wheel events to the parent component if this component
 * cannot scroll further in the given direction.
 *
 *
 * This behavior is a little better than Swing's default behavior but
 * still worse than the behavior of Google Chrome, which remembers the
 * currently scrolling component and sticks to it until a timeout happens.
 *
 * @see [Stack Overflow](https://stackoverflow.com/a/53687022)
 */
class MouseWheelScrollListener(pane: JScrollPane) : MouseWheelListener {
    private val pane: JScrollPane
    private var previousValue: Int
    override fun mouseWheelMoved(e: MouseWheelEvent) {
        var parent: Component? = pane.Parent
        while (parent !is JScrollPane) {
            if (parent == null) {
                return
            }
            parent = parent.parent
        }
        val bar: JScrollBar = pane.VerticalScrollBar
        val limit = if (e.WheelRotation < 0) 0 else bar.Maximum - bar.VisibleAmount
        if (previousValue == limit && bar.Value == limit) {
            parent!!.dispatchEvent(SwingUtilities.convertMouseEvent(pane, e, parent))
        }
        previousValue = bar.Value
    }

    init {
        this.pane = pane
        previousValue = pane.VerticalScrollBar.Value
    }
}
