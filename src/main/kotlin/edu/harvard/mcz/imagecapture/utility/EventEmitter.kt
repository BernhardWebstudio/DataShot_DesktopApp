package edu.harvard.mcz.imagecapture.utility


import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*

abstract class EventEmitter {
    protected var listeners: ArrayList<ActionListener?>? = null
    fun addListener(listener: ActionListener?) {
        listeners!!.add(listener)
    }

    fun removeListener(listener: ActionListener?) {
        listeners!!.remove(listener)
    }

    protected fun emitEvent(event: ActionEvent?) {
        for (listener in listeners!!) {
            listener.actionPerformed(event)
        }
    }
}
